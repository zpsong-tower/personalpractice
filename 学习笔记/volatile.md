**解决CPU高速缓存数据同步的问题，禁止指令重排以保证程序安全性**

一个变量被声明成volatile之后，任何一个线程对它进行修改，都会让所有其他CPU高速缓存中的值过期，这样其他线程就必须去内存中重新获取最新的值，也就解决了可见性的问题。

安卓多线程下载例子：

```java
public class DownloadTask {
    volatile boolean isCanceled = false;

    public void download() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (!isCanceled) {
                    byte[] bytes = readBytesFromNetwork();
                    if (bytes.length == 0) {
                        break;
                    }
                    writeBytesToDisk(bytes);
                }
            }
        }).start();
    }

    public void cancel() {
        isCanceled = true;
    }
}
```

