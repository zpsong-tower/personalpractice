# Crash相关

由于Android基于Handler事件驱动的机制，可以在app启动时，向主线程中的MessageQueue中提交一个死循环操作，并对其进行try-catch

```java
new Handler(Looper.getMainLooper()).post(new Runnable() {
    @Override
    public void run() {
        while (true) {
            try {
                Looper.loop();
            } catch (Throwable e) {
                // 处理异常
                Log.e(TAG, "catch exception: " + e.getMessage());
            }
        }
    }
});
```

可以考虑在ActivityLifeCycle中维护一个Activity栈，当catch到主线程异常时，关闭栈顶Activity

