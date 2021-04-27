# ~~Loader~~

***Android P（API 级别 28）中已弃用 Loader。官方更推荐 ViewModels 和 LiveData 的组合来处理加载数据，相关笔记有时间通读源码后更新。***

## 1 简介

为耗时操作提供更便利的异步加载，优势如下：

- 在单独的线程上运行，以免界面出现卡顿或无响应问题
- 对数据源变化进行监听，实时更新数据；
- 在 Activity 配置发生变化（如横竖屏切换）时不用重复加载数据；
- 适用于任何 Activity 和 Fragment ；

## 2 基础使用

### 2-1 API概述：

| Class/Interface               | Description                                                  |
| ----------------------------- | ------------------------------------------------------------ |
| LoaderManager                 | 一个与 Activity、Fragment 关联的抽象类，用于管理一个或多个Loader实例。每个 Activity 或 Fragment 只能有一个 LoaderManager，而一个 LoaderManager 可以有多个 Loader。 |
| LoaderManager.LoaderCallbacks | 用于和 LoaderManager 交互的回调接口。譬如，可以使用 `onCreateLoader()` 创建一个新的Loader。 |
| AsyncTaskLoader               | 抽象的 Loader，提供一个 AsyncTask 继承实现。                 |
| CursorLoader                  | AsyncTaskLoader 的子类，用于向 ContentResover 请求返回一个 Cursor。该类以标准游标查询实现了 Loader 协议，使用后台线程进行查询，使用这个 Loader 是从 ContentProvider 加载异步数据最好的方式。 |

### 2-2 启动一个Loader

一个 Activity 或 Fragment 中 LoaderManager 管理一个或多个 Loader 实例，每个 Activity 或 Fragment 只有一个 LoaderManager，我们可以在 Activity 的 `onCreate()` 或 Fragment 的 `onActivityCreated()` 里初始化一个 Loader。

```java
LoaderManager.getInstance(this).initLoader(0x0100, null, mLoaderCallback);
```

```java
/**
 * @param id 当前Loader的ID
 * @param args 提供给Loader构造函数的参数，可选
 * @param callback LoaderManager.LoaderCallbacks的回调实现
 * @return 创建的Loader
 */
@MainThread
@NonNull
public abstract <D> Loader<D> initLoader(int id, @Nullable Bundle args,
                                         @NonNull LoaderManager.LoaderCallbacks<D> callback);
```

- 如果代表该 Loader 的 ID 已经存在，则后面创建的 Loader 将直接复用已经存在的；
- 如果代表该 Loader 的 ID 不存在，`initLoader()` 会触发 LoaderManager.LoaderCallbacks 回调的 `onCreateLoader()` 方法创建一个 Loader

该方法会返回一个创建的 Loader，但是不必获取它的引用，因为 LoadeManager 会自动管理该 Loader 的生命周期，只需在它回调提供的生命周期方法中做数据逻辑的处理即可。

### 2-2 重启一个Loader

需要丢弃旧数据重新创建一个相同 ID 的 Loader 时

```java
LoaderManager.getInstance(this).restartLoader(0x0100, null, mLoaderCallback);
```

### 2-3 LoaderManager Callbacks

回调需要实现以下三个方法

1. `onCreateLoader()`
    实例化并返回一个新创建给定 ID 的 Loader 对象；
2. `onLoadFinished()`
    当创建好的 Loader 完成了数据的 load 之后回调此方法；
3. `onLoaderReset()`
    当创建好的 Loader 被 reset 时调用此方法，这样保证它的数据无效；

#### 2-3-1 onCreateLoader

```java
/**
 * @param id initLoader规定的Loader的ID
 * @param args initLoader提供的参数
 * @return 创建的Loader
 */
@MainThread
@NonNull
Loader<D> onCreateLoader(int id, @Nullable Bundle args);
```

常用的 CursorLoader 的构造方法：

```java
/**
 * @param context 上下文
 * @param uri 准备获取内容的URI
 * @param projection 要返回的列key list，null表示返回所有列，但返回所有列很多时候会降低性能
 * @param selection 要返回的行过滤，即SQL中的WHERE语句，null代表返回uri指定的所有行
 * @param selectionArgs 用来替换上面selection中包含的“?”
 * @param sortOrder 结果的行排序，即SQL中的ORDER BY，传递null则无序
 */
public CursorLoader(@NonNull Context context, @NonNull Uri uri, @Nullable String[] projection,
                    @Nullable String selection, @Nullable String[] selectionArgs,
                    @Nullable String sortOrder) {
```

#### 2-3-2 onLoadFinished

```java
/**
 * @param loader 完成数据加载的Loader
 * @param data 数据
 */
@MainThread
void onLoadFinished(@NonNull Loader<D> loader, D data);
```

该方法在释放现有维持的数据之前被调用，需要在此移除所有需要使用的旧数据，因为之后 Loader 很快会自行释放这些数据。

#### 2-3-3 onLoadReset

```java
/**
 * @param loader 被重置或销毁的Loader
 */
@MainThread
void onLoaderReset(@NonNull Loader<D> loader);
```

当重置先前创建的加载器并因此导致其数据不可用时，将调用此方法，以便及时移除数据的引用。  

## 3 源码浅析

- 一次完整的数据加载流程为 Activity 调用 LoaderManager 的 `doStart()` 方法，然后 LoaderManager 调用 Loader 的 `startLoading()` 方法，然后 Loader 调运 AsyncTaskLoader 的 `doingBackground()` 方法进行耗时数据加载，然后 AsyncTaskLoader 回调 LoaderManager 的 `onLoadComplete()` 数据加载完成方法，接着 LoaderManager 回调在 Activity 中实现的 callback 中的 `onLoadFinish()` 方法。

- Acivity 和 Fragment 的生命周期主动管理了 LoaderManager，每个 Activity 用一个 ArrayMap 的 mAllLoaderManager 来保存当前 Activity 及其附属 Frament 的唯一 LoaderManager。在 Activity 配置发生变化时，Activity 在 destory 前会保存 mAllLoaderManager，当 Activity 再重新创建时，会在 Activity 的 `onAttcach()`、`onCreate()`、`performStart()` 方法中恢复 mAllLoaderManager。

- LoaderManager 给 Activity 提供了管理自己的一些方法，同时主动管理了对应的 Loader。它把每一个 Loader 封装为 LoadInfo 对象，同时它负责主动调运管理 Loader 的 `startLoading()`、`stopLoading()`、`forceLoad()` 等方法。

- 由于整个 Activity 和 Fragment 主动管理了 Loader，所以关于 Loader 的释放（譬如 CursorLoader 的 Cursor 关闭等）不需要人为处理，Loader 框架自己会很好的处理。需要留意的是，对于 CursorLoader，当数据源发生变化时 Loader 框架会通过观察者模式 ContentObserver 调用 `onContentChanged` 的 `forceLoad()` 方法重新请求数据进行回调刷新。