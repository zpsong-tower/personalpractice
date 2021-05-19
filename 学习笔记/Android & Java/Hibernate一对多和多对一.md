# Hibernate一对多和多对一

首先关注下 ManyToOne 的属性

```java
public @interface ManyToOne {
    Class targetEntity() default void.class;

    CascadeType[] cascade() default {};

    FetchType fetch() default FetchType.EAGER;

    boolean optional() default true;
}
```

1. ***targetEntity***

    定义关系类的类型，默认是该成员属性对应的类类型，所以通常不需要提供定义。

2. ***cascade***

    该属性定义类和类之间的级联关系。定义的级联关系将被容器视为对当前类对象及其关联类对象采取相同的操作，而且这种关系是递归调用的。举个例子：Country 和 Person 有级联关系，那么删除 Country 时将同时删除它所对应的 Person 对象。而如果 Person 还和其他的对象之间有级联关系，那么这样的操作会一直递归执行下去。

    该属性值可选择项包括 **CascadeType.PERSIST (级联新建)**、**CascadeType.REMOVE (级联删除)**、**CascadeType.REFRESH (级联刷新)**、**CascadeType.MERGE (级联更新)** 中的一个或多个。还有一个 **CascadeType.ALL (四项全选)**。

3. ***fetch***

    该属性可选择项包括 **FetchType.EAGER (在主类加载时同时加载)** 和 **FetchType.LAZY (类在被访问时才加载)**。默认值为前者，OneToMany默认值则相反。

4. ***optional***

    该属性定义该关联类是否必须存在，默认值为 true。

    - **false**：关联类双方都必须存在，如果关系被维护端不存在，查询的结果为null。

    - **true**：关系被维护端可以不存在，查询的结果仍然会返回关系维护端，在关系维护端中指向关系被维护端的属性为null。

-----



#### 1. 一对多单向

有 User 表和 UserFollow 表，那么一定是 UserFollow 表中有 user_id 字段。

由于是一对多，所以站在一的角度，也就是 User 的角度，在 User 类中加入 @JoinColumn：

```java
// 我关注的人
@OneToMany(cascade = CascadeType.ALL)
@JoinColumn(name = "originId")
private Set<UserFollowEntity> following = new HashSet<>();
```

在一对多单向关系中，多的一方（UserFollow）没有注解，一的一方（User）有注解，如果一的一方不加 @JoinColumn 指定外键字段的话，Hibernate 会自动生成一张中间表来对 User 和 UserFollow 进行绑定。

#### 2. 多对一单向

反过来，将 User 中的注解去掉，挪到 UserFollow 类中

```java
// 关注发起人
@ManyToOne(optional = false)
@JoinColumn(name = "originId")
private UserEntity origin;
```

与一对多单向不同的是，此时若不用 @JoinColumn 指定外键字段的话，不会生成中间表，而是在 UserFollow 表中生成一列指向 User 的外键。

#### 3. 一对多双向

单向关系的缺点还是比较明显的，比如一对多单向，多的那一方没有任何关于另一方的信息，而一对多双向可以让双方都有彼此的信息。

单向变双向，只需在单向的基础上，在 UserFollow 类中加入多对一的注解即可，即将上面两者结合。
