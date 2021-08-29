# UI线程相关补充

### Looper.loop() 为什么不会阻塞主线程

MessageQueue 的 next 方法：

```java
Message next() {
    int pendingIdleHandlerCount = -1; // -1 only during first iteration
    int nextPollTimeoutMillis = 0;
    for (;;) {
        if (nextPollTimeoutMillis != 0) {
            Binder.flushPendingCommands();
        }
        
        // nextPollTimeoutMillis为-1则阻塞
        nativePollOnce(ptr, nextPollTimeoutMillis);

        synchronized (this) {
            final long now = SystemClock.uptimeMillis(); // 从开机到现在的毫秒数
            Message prevMsg = null;
            Message msg = mMessages;

            ...

            if (msg != null) {
                if (now < msg.when) {
                    // 消息未准备好 设置一个时间在之后唤醒UI线程
                    nextPollTimeoutMillis = (int) Math.min(msg.when - now, Integer.MAX_VALUE);
                } else {
                    // 得到一个消息
                    mBlocked = false;
                    if (prevMsg != null) {
                        prevMsg.next = msg.next;
                    } else {
                        mMessages = msg.next;
                    }
                    msg.next = null;
                    msg.markInUse();
                    return msg;
                }
            } else {
                // 没有更多的消息
                nextPollTimeoutMillis = -1;
            }
        }
    }
}
```

nativePollOnce方法是一个 native 方法，它会使主线程释放CPU资源进入休眠状态，直到下条消息到达或者有事务发生，通过往pipe管道写端写入数据来唤醒主线程工作，采用的epoll机制

关于nativePollOnce的详细分析可以参考：[nativePollOnce函数分析](https://www.kancloud.cn/alex_wsc/android-deep2/413394)

### sendMessageDelayed / postDelayed

由于消息队列的机制，只能保证消息在when之前不被处理，不能够保证一定在 when 时被处理

大于Handler Looper的周期时delay的值基本可靠 （例如主线程 > 50ms）

Looper负载越高，任务越容易挤压，进而导致卡顿

对时间精度要求较高时，不要用Handler的delay作为计时的依据

### UI为什么不设计成线程安全的

- UI具有可变性，甚至是高频可变性
- UI对响应时间的敏感性要求UI操作必须高效
- UI组件必须批量绘制来保证效率

### SurfaceView / TextureView

