# HandlerThread

继承自Thread，对自带Looper的生命周期进行了简单的封装，处理了一个获取Looper的异步问题，核心方法如下：

**创建Looper**

```java
@Override
public void run() {
    mTid = Process.myTid();
    Looper.prepare();
    synchronized (this) {
        mLooper = Looper.myLooper();
        notifyAll(); // 唤醒等待线程
    }
    Process.setThreadPriority(mPriority); // 线程优先级
    onLooperPrepared(); // 空方法 必要时可以去重写
    Looper.loop();
    mTid = -1;
}
```

**获取Looper**

```java
public Looper getLooper() {
    if (!isAlive()) {
        return null;
    }

    synchronized (this) {
        while (isAlive() && mLooper == null) {
            try {
                wait(); // 等待唤醒
            } catch (InterruptedException e) {
            }
        }
    }
    return mLooper;
}
```

**退出与安全退出**

```java
public boolean quit() {
    Looper looper = getLooper();
    if (looper != null) {
        // 实际调用MessageQueue的removeAllMessagesLocked()，清空所有消息
        looper.quit();
        return true;
    }
    return false;
}

public boolean quitSafely() {
    Looper looper = getLooper();
    if (looper != null) {
        // 实际调用MessageQueue的removeAllFutureMessagesLocked()，清空所有延迟消息，将非延迟消息派发出去让Handler处理完成后再停止循环
        looper.quitSafely();
        return true;
    }
    return false;
}
```

