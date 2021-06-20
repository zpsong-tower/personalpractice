# onActivityResult

### 基本用法

Activity A -> Activity B

```java
startActivityForResult(intent, requestCode)
```

> 思考：代码处理逻辑分离，容易出现遗漏和不一致的问题

Activity B -> Activity A

```java
setResult(resultCode, intent)
```

> 思考：写法不够直观，结果数据没有类型安全保障

Activity A

```java
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    switch (requestCode) {
        // TODO
    }
}
```

> 思考：结果种类较多时，onActivityResult逐渐臃肿难以维护

### 使用回调替代带来的问题

可以简化代码编写，但Activity的销毁和恢复机制不允许匿名内部类的出现

比如回调中的引用变换问题

### 一些尝试

注解处理器和Fragment的回调实现

