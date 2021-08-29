# Handler

sendMessage → enqueueMessage → 【进入】 → 消息队列MessageQueue → 【取出】 → Looper.loop → dispatchMessage → handleMessage

### new Handler()

```java
public Handler(@Nullable Callback callback, boolean async) {
    if (FIND_POTENTIAL_LEAKS) {
        final Class<? extends Handler> klass = getClass();
        if ((klass.isAnonymousClass() || klass.isMemberClass() || klass.isLocalClass()) &&
                (klass.getModifiers() & Modifier.STATIC) == 0) {
            Log.w(TAG, "The following Handler class should be static or leaks might occur: " +
                klass.getCanonicalName());
        }
    }

    mLooper = Looper.myLooper();
    if (mLooper == null) {
        throw new RuntimeException(
            "Can't create handler inside thread " + Thread.currentThread()
                    + " that has not called Looper.prepare()");
    }
    mQueue = mLooper.mQueue;
    mCallback = callback;
    mAsynchronous = async;
}
```

无参构造器里调用了重载的构造方法并分别传入 null 和 false。并且在构造方法中给两个全局变量赋值：**mLooper** 和 **mQueue**

### Looper初始化

ActivityThread的main方法是一个新的 App 进程的入口，具体实现如下

```java
public static void main(String[] args) {
    ...

    // 初始化当前进程的Looper对象
    Looper.prepareMainLooper();

    ...

    if (sMainThreadHandler == null) {
        sMainThreadHandler = thread.getHandler();
    }

    // 调用Looper的loop方法开启无限循环
    Looper.loop();

    throw new RuntimeException("Main thread loop unexpectedly exited");
}
```

`prepareMainLooper()` 确保

- prepare方法在一个线程中只能被调用 1 次

- Looper的构造方法在一个线程中只能被调用 1 次

- MessageQueue在一个线程中只会被初始化 1 次

**即UI 线程中只会存在1个MessageQueue对象，后续通过Handler发送的消息都会被发送到这个MessageQueue 中**

### Looper.loop()

```java
public static void loop() {
    final Looper me = myLooper();
    if (me == null) {
        throw new RuntimeException("No Looper; Looper.prepare() wasn't called on this thread.");
    }
    final MessageQueue queue = me.mQueue;

    ...

    for (;;) {
        // 不断地调用MessageQueue的next方法取出Message
        Message msg = queue.next();
        if (msg == null) {
            return;
        }

        ...

        try {
            // 从Message中取出target(Handler)对象，调用其dispatchMessage方法处理Message自身
            msg.target.dispatchMessage(msg);
            dispatchEnd = needEndTime ? SystemClock.uptimeMillis() : 0;
        } finally {
            if (traceTag != 0) {
                Trace.traceEnd(traceTag);
            }
        }
    }
}
```

loop方法中执行了一个死循环，这也是一个Android App进程能够持续运行的原因

一句话总结：**不断从MessageQueue中取出Message，然后处理 Message 中指定的任务**

### Handler的dispatchMessage方法

```java
public void dispatchMessage(@NonNull Message msg) {
    if (msg.callback != null) {
        // Message的Callback不为null
        // 一般为通过post(Runnabl)方式，会直接执行Runnable的run方法
        // 这里的Runnable实际上就是一个回调接口，跟线程Thread没有任何关系
        handleCallback(msg);
    } else {
        // Message的Callback为null
        if (mCallback != null) {
            if (mCallback.handleMessage(msg)) {
                return;
            }
        }
        
        // 调用Handler子类实现的hanlerMessage方法进行处理
        handleMessage(msg);
    }
}

private static void handleCallback(Message message) {
    message.callback.run();
}

public void handleMessage(@NonNull Message msg) {
}
```

如果 Message 的 Callback 不为 null，一般为通过 post(Runnabl) 方式，会直接执行 Runnable 的 run 方法。因此这里的 Runnable 实际上就是一个回调接口，跟线程 Thread 没有任何关系。
如果 Message 的 Callback 为 null，这种一般为 sendMessage 的方式，则会调用 Handler 的 hanlerMessage 方法进行处理。

### Handler的sendMessage方法

```java
public final boolean sendMessage(@NonNull Message msg) {
    return sendMessageDelayed(msg, 0);
}
```

```java
public final boolean sendMessageDelayed(@NonNull Message msg, long delayMillis) {
    if (delayMillis < 0) {
        delayMillis = 0;
    }
    return sendMessageAtTime(msg, SystemClock.uptimeMillis() + delayMillis);
}
```

```java
public boolean sendMessageAtTime(@NonNull Message msg, long uptimeMillis) {
    MessageQueue queue = mQueue;
    if (queue == null) {
        RuntimeException e = new RuntimeException(
                this + " sendMessageAtTime() called with no mQueue");
        Log.w("Looper", e.getMessage(), e);
        return false;
    }
    return enqueueMessage(queue, msg, uptimeMillis);
}
```

sendMessage最终会调用enqueueMessage方法将Message插入到消息队列MessageQueue中

而这个消息队列就是ActivityThread的main方法中通过Looper创建的MessageQueue

### Handler的enqueueMessage方法

```java
private boolean enqueueMessage(@NonNull MessageQueue queue, @NonNull Message msg,
        long uptimeMillis) {
    // 将Handler自身设置为Message的target对象
    // 所以Looper.loop()中调用Message的target对象的dispatchMessage来处理
    msg.target = this;
    if (mAsynchronous) {
        msg.setAsynchronous(true);
    }
    return queue.enqueueMessage(msg, uptimeMillis);
}
```

跳转到MessageQueue的enqueueMessage方法

```java
boolean enqueueMessage(Message msg, long when) {
    // Message中的target没有被设置,直接抛出异常
    if (msg.target == null) {
        throw new IllegalArgumentException("Message must have a target.");
    }
    synchronized (this) {
        msg.markInUse();
        msg.when = when;
        Message p = mMessages;
        boolean needWake;

        // 按照Message的时间when来有序地插入MessageQueue中
        if (p == null || when == 0 || when < p.when) {
            msg.next = p;
            mMessages = msg;
            needWake = mBlocked;
        } else {
            needWake = mBlocked && p.target == null && msg.isAsynchronous();
            Message prev;
            for (;;) {
                prev = p;
                p = p.next;
                if (p == null || when < p.when) {
                    break;
                }
				if (needWake && p.isAsynchronous()) {
                    needWake = false;
                }
            }
            msg.next = p;
            prev.next = msg;
        }
        if (needWake) {
            nativeWake(mPtr);
        }
    }
    return true;
}
```

**MessageQueue实际上是一个有序队列，按照Message的执行时间来排序**

