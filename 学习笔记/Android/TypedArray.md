**TypedArray**

自定义View，规定自定义属性

```xml
<resources>
    <declare-styleable name="EmptyView">
        <attr name="comEmptyText" format="integer" />
        <attr name="comErrorText" format="integer" />
        <attr name="comLoadingText" format="integer" />
        <attr name="comEmptyDrawable" format="integer" />
        <attr name="comErrorDrawable" format="integer" />
    </declare-styleable>
</resources>
```

在自定义View初始化中，规定属性的默认值并获取其实际值

```java
TypedArray typedArray = getContext().obtainStyledAttributes(
        attrs, R.styleable.EmptyView, defStyle, 0);

mDrawableIds[0] = typedArray.getInt(R.styleable.EmptyView_comEmptyDrawable, R.drawable.status_empty);
mDrawableIds[1] = typedArray.getInt(R.styleable.EmptyView_comErrorDrawable, R.drawable.status_empty);
mTextIds[0] = typedArray.getInt(R.styleable.EmptyView_comEmptyText, R.string.label_empty_empty);
mTextIds[1] = typedArray.getInt(R.styleable.EmptyView_comErrorText, R.string.label_empty_error);
mTextIds[2] = typedArray.getInt(R.styleable.EmptyView_comLoadingText, R.string.label_empty_loading);

typedArray.recycle();
```

