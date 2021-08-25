**唯一Hook点 `RepluginClassLoader`**

在应用启动的时候，Replugin使用`RepluginClassLoader`将系统的`PathClassLoader`替换掉，并且只篡改了`loadClass`方法的行为，用于加载插件的类。

每一个插件都会有一个`PluginDexClassLoader`，`RepluginClassLoader`会调用插件的`PluginDexClassLoader`来加载插件中的类与资源

**UI进程，Persistent进程**

Replugin启动时会默认启动两个进程，一个是UI进程，一个是Persistent进程(常驻进程)，在`IPluginManager`接口中定义了两个常量`PROCESS_UI`和`PROCESS_PERSIST`来表示这两个进程。

UI进程就是程序的主进程。

Persistent进程是一个服务器进程，所有其他的进程在启动组件的时候都会通过`PmHostSvc` 与这个进程通信，该进程中运行的两个重要服务:

- `PluginManagerServer` 用于插件的管理，比如加载插件，更新插件信息，签名验证，版本检查，插件卸载等
- `PluginServiceServer` 用于`Service`的启动调度等

**坑位**

坑位就是预先在Host的Manifest中注册的一些组件（Activity, Service, Content Provider，没有Broadcast Receiver)，需要与`RepluginClassLoader`配合才能实现。

坑位组件的代码由gradle插件在编译时生成，实际上并不会被用到。

启动插件的组件时，这些坑位会替代要启动的组件，并建立一个坑位与真实组件之间的对应关系（用`ActivityState`表示)，然后在加载类的时候`RepluginClassLoader` 会根据被篡改过的行为使用插件的`PluginDexClassLoader`加载要启动的真实组件类。



[github RePlugin官方文档](https://github.com/Qihoo360/RePlugin/wiki/%E5%BF%AB%E9%80%9F%E4%B8%8A%E6%89%8B)

