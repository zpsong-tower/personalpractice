| Activity  | Fragment          | 备注                                                         |
| --------- | ----------------- | ------------------------------------------------------------ |
| onCreate  |                   | `setContentView()` `findViewById()`                          |
|           | onAttach          | Fragment 首次附加到其 Activity 时调用                        |
|           | onCreate          |                                                              |
|           | onCreateView      | Fragment 首次绘制其界面时调用，返回的 View 必须是 Fragment 布局的根视图 |
|           | onActivityCreated | Activity 的 `onCreate()` 方法已返回时调用                    |
| onStart   |                   | 为 Activity 进入前台支持互动做准备，开始绘制视觉元素，运行动画等 |
|           | onStart           |                                                              |
| onResume  |                   |                                                              |
|           | onResume          |                                                              |
|           | onPause           |                                                              |
| onPause   |                   | 执行完成后新 Activity 的 `onResume()` 方法才会被执行，不能太耗时。可以停止消耗大量 CPU 的东西以便尽快切换到下一个活动，不应该用来保存应用或用户数据、进行网络调用或执行数据库事务 |
|           | onStop            |                                                              |
| onStop    |                   | 一般停止刷新UI，动画和其他可视化的东西，也可执行保存草稿到数据库等操作 |
|           | onDestroyView     | 移除与 Fragment 关联的视图层次结构时调用                     |
|           | onDestroy         |                                                              |
|           | onDetach          | 取消 Fragment 与 Activity 关联时调用                         |
| onDestroy |                   | 回收工作和最终的资源释放                                     |

## Fragment 的 onCreateView 补充

```java
public View inflate (int resource, ViewGroup root, boolean attachToRoot)
```

- 想要扩展的布局的资源 ID。

- 将作为扩展布局父项的 `ViewGroup`。传递 `container` 对系统向扩展布局的根视图（由其所属的父视图指定）应用布局参数具有重要意义。

- 是否应在扩展期间将扩展布局附加至 `ViewGroup`（第二个参数）的布尔值。（系统已将扩展布局插入 `container`则应为false，传递 true 则会在最终布局中创建一个多余的视图组）

```java
public static class ExampleFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.example_fragment, container, false);
    }
}
```

## Fragment 与 Activity 通信

Fragment 可通过 `getActivity()` 访问 `FragmentActivity` 实例，并执行在 Activity 布局中查找视图等任务：

```java
View listView = getActivity().findViewById(R.id.list);
```

Activity 也可使用 `findFragmentById()` ，通过从 `FragmentManager` 获取对 Fragment 的引用来调用其中的方法：

```java
ExampleFragment fragment = (ExampleFragment) getSupportFragmentManager().findFragmentById(R.id.example_fragment);
```

### 创建 Activity 的事件回调

如果某个新闻应用的 Activity 有两个 Fragment ，其中一个用于显示文章列表（Fragment A），另一个用于显示文章（Fragment B），则 Fragment A 必须在列表项被选定后告知 Activity，以便它告知 Fragment B 显示该文章。

在 Fragment A 内声明 `OnArticleSelectedListener` 接口：

```java
public static class FragmentA extends ListFragment {
    ...
    // Container Activity must implement this interface
    public interface OnArticleSelectedListener {
        public void onArticleSelected(Uri articleUri);
    }
    ...
}
```

然后，该 Fragment 的宿主 Activity 会实现 `OnArticleSelectedListener` 接口并重写 `onArticleSelected()`，将来自 Fragment A 的事件通知 Fragment B。为确保宿主 Activity 实现此接口，Fragment A 的 `onAttach()` 回调方法（系统在向 Activity 添加 Fragment 时调用的方法）会通过转换传递到 `onAttach()` 中的 `Activity` 来实例化 `OnArticleSelectedListener` 的实例：

```java
public static class FragmentA extends ListFragment {
    OnArticleSelectedListener mListener;
    ...
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (OnArticleSelectedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnArticleSelectedListener");
        }
    }
    ...
}
```

如果 Activity 未实现接口，则 Fragment 会抛出 `ClassCastException`。若实现成功，`mListener` 成员会保留对 Activity 的 `OnArticleSelectedListener` 实现的引用，以便 Fragment A 可通过调用 `OnArticleSelectedListener` 接口定义的方法与 Activity 共享事件。例如，如果 Fragment A 是 `ListFragment` 的一个扩展，则用户每次点击列表项时，系统都会调用 Fragment 中的 `onListItemClick()`，然后该方法会通过调用 `onArticleSelected()` 与 Activity 共享事件：

```java
public static class FragmentA extends ListFragment {
    OnArticleSelectedListener mListener;
    ...
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // Append the clicked item's row ID with the content provider Uri
        Uri noteUri = ContentUris.withAppendedId(ArticleColumns.CONTENT_URI, id);
        // Send the event and Uri to the host activity
        mListener.onArticleSelected(noteUri);
    }
    ...
}
```

传递到 `onListItemClick()` 的 `id` 参数是被点击项的行 ID，即 Activity（或其他 Fragment）用来从应用的 `ContentProvider` 获取文章的 ID。

## 一些 Activity 生命周期的思考

1. 如果所有的初始化都在 `onCreate()` 中实现，会有什么问题？

    - Activity 还不可见，既然视图还不存在，启动动画显然不行。

    - `onCreate()` 只在活动被创建时执行一次，后台切换至前台，从其他 Activity 返回所需的初始化也不行。
2. 如果所有的初始化都在 `onStart()` 中实现，会有什么问题？

    - `onResume()` 不是 Activity 对用户可见的最佳指示器，如果在 `onStart()` 中做全部初始化，很有可能初始化还没完成影响到用户的交互体验。
3. 如果所有资源回收都在 `onStop()` 中实现，会有什么问题？
    - `onResume()` 中打开独占设备（比如相机），与 `onResume()` 对应的是 `onPause()`，关闭相机的操作也应该在此方法中被调用。否则 AActivity 跳转 BActivity，后者也需要打开相机， `onStop()` 在BActivity启动后被调用，就会出问题。

