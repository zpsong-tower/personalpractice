**ids.xml——为应用的相关资源提供唯一的资源id**。

id是为了获得xml中的对象而需要的参数，也就是Object = findViewById(R.id.id_name)中的id_name。这些值可以在代码中用android.R.id引用到。

若在ids.xml中定义了ID，则在layout中可如下定义@id/price_edit，否则@+id/price_edit。

**为什么使用ids?**

1. 优化编译效率。

    android的组件必须用一个int类型的id属性来标识其唯一性，id属性必须以@开头的值，例如，@id/abc、@+id/xyz等。

    如果使用"@+id/name"形式，当R.java中存在名为name变量时，则该组件会使用该变量的值作为标识。如果不存在该变量，则添加一个新的变量，并为该变量赋相应的值（不会重复）。

    当修改完某个布局文件并保存后，系统会自动在R.java文件中生成相应的int类型变量。变量名就是“/”后面的值，例如，@+id/xyz会在R.java文件中生成int xyz = value，其中value是一个十六进制的数。如果xyz在R.java中已经存在同名的变量，就不再生成新的变量，而该组件会使用这个已存在的变量的值。

    使用@id/name形式，预先定义的id已经生成，修改配置文件时，也不会引起系统重新生成。

2. 统一管理资源Id。