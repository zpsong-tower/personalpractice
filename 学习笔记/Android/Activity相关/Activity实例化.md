# Activity实例化

`Instrumentation.java`

```java
public Activity newActivity(ClassLoader cl, String className,
        Intent intent)
        throws InstantiationException, IllegalAccessException,
        ClassNotFoundException {
    String pkg = intent != null && intent.getComponent() != null
            ? intent.getComponent().getPackageName() : null;
    return getFactory(pkg).instantiateActivity(cl, className, intent);
}
```

`AppComponentFactory.java`

```java
public @NonNull Activity instantiateActivity(@NonNull ClassLoader cl, @NonNull String className,
        @Nullable Intent intent)
        throws InstantiationException, IllegalAccessException, ClassNotFoundException {
    return (Activity) cl.loadClass(className).newInstance();
}
```

传入ClassLoader，类名和intent，利用反射进行实例化

因为系统可能随时会重新创建一个新的Activity实例出来替换现在的（例如转屏），所以并不希望Activity实例的管理让开发者自己处理

从`newInstance()`可以看出，Activity类必须有一个无参的构造方法，这也是Activity开发中不写构造器的主要原因

Fragment可以被开发者自己new出来，但也不能添加有参数的构造方法

因为它涉及到Activity状态的保存和恢复

