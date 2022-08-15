# Compose入门

## 1. 可组合函数

```kotlin
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // setContent 块定义了 activity 的布局
        // 我们会在其中调用可组合函数 可组合函数只能从其他可组合函数调用
        // 编译器插件将这些可组合函数转换为应用的界面元素
        setContent {
            MessageCard("Android")
        }
    }
}

// 添加 @Composable 注解可使函数成为可组合函数
@Composable
fun MessageCard(name: String) {
    // Text 可组合函数会在屏幕上显示一个文本标签
    Text(text = "Hello $name!")
}

// 标有 @Preview 注解的可组合函数会显示在 Android Studio 的预览中
// 该注解必须用于不接受参数的可组合函数
// 该函数未在任何位置受到调用，因此应用本身不会更改
@Preview
@Composable
fun PreviewMessageCard() {
    MessageCard("Compose")
}
```

## 2. 布局

```kotlin
data class Message(val title: String, val body: String)

@Composable
fun MessageCard(msg: Message) {
    // Row: 水平排列元素 | Column: 垂直排列元素 | Box: 堆叠元素
    Row(modifier = Modifier.padding(all = 8.dp)) { // 内边距设置为 8 dp
        Image(
            painter = painterResource(R.drawable.ic_launcher_background),
            contentDescription = "Hello Compose!",
            modifier = Modifier
                .size(40.dp) // 图像大小设置为 40 dp
                .clip(CircleShape) // 图像裁剪为圆形
        )

        // 在图像和列之间添加水平空间
        Spacer(modifier = Modifier.width(8.dp))

        Column {
            Text(text = msg.title)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = msg.body)
        }
    }
}

@Preview
@Composable
fun PreviewMessageCard() {
    MessageCard(
        msg = Message("Hello Compose!", "Welcome to the world of compose!")
    )
}
```

## 3. Material Design

Theme 是围绕三大要素构建的：`Color`、`Typography`、`Shape`

```kotlin
data class Message(val title: String, val body: String)

@Composable
fun MessageCard(msg: Message) {
    Row(modifier = Modifier.padding(all = 8.dp)) {
        Image(
            painter = painterResource(R.drawable.ic_launcher_background),
            contentDescription = null,
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .border(1.5.dp, MaterialTheme.colors.secondary, CircleShape) // Color
        )

        Spacer(modifier = Modifier.width(8.dp))

        Column {
            Text(
                text = msg.title,
                color = MaterialTheme.colors.secondaryVariant, // Color
                style = MaterialTheme.typography.subtitle2 // Typography
            )

            Spacer(modifier = Modifier.height(4.dp))

            Surface(shape = MaterialTheme.shapes.medium, elevation = 1.dp) { // Shape
                Text(
                    text = msg.body,
                    modifier = Modifier.padding(all = 4.dp),
                    style = MaterialTheme.typography.body2 // Typography
                )
            }
        }
    }
}

@Preview( // 浅色主题预览
    name = "Light Mode"
)
@Preview( // 深色主题预览
    name = "Dark Mode",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun PreviewMessageCard() {
    DefaultTheme {
        MessageCard(
            msg = Message("Hello Compose!", "Welcome to the world of compose!")
        )
    }
}
```

## 4. 列表和动画

```kotlin
data class Message(val title: String, val body: String)

@Composable
fun MessageCard(msg: Message) {
    Row(modifier = Modifier.padding(all = 8.dp)) {
        Image(
            painter = painterResource(R.drawable.ic_launcher_background),
            contentDescription = null,
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .border(1.5.dp, MaterialTheme.colors.secondary, CircleShape)
        )

        Spacer(modifier = Modifier.width(8.dp))

        // remember 将本地状态存储在内存中
        // 跟踪传递给 mutableStateOf 的值的变化 消息是否展开
        var isExpanded by remember { mutableStateOf(false) }

        // 颜色渐变动画
        val surfaceColor by animateColorAsState(
            if (isExpanded) MaterialTheme.colors.primary else MaterialTheme.colors.surface,
        )

        // 单击此列时 切换isExpanded变量
        Column(modifier = Modifier.clickable { isExpanded = !isExpanded }) {
            Text(
                text = msg.title,
                color = MaterialTheme.colors.secondaryVariant,
                style = MaterialTheme.typography.subtitle2
            )

            Spacer(modifier = Modifier.height(4.dp))

            Surface(
                shape = MaterialTheme.shapes.medium,
                elevation = 1.dp,
                color = surfaceColor,

                // 容器大小改变渐变动画 内边距
                modifier = Modifier.animateContentSize().padding(1.dp)
            ) {
                Text(
                    text = msg.body,
                    modifier = Modifier.padding(all = 4.dp),

                    // 消息被展开，显示所有内容 | 否则，只显示第一行
                    maxLines = if (isExpanded) Int.MAX_VALUE else 1,

                    style = MaterialTheme.typography.body2
                )
            }
        }
    }
}

@Composable
fun Conversation(messages: List<Message>) {
    //  LazyColumn 和 LazyRow 组合项只会呈现屏幕上显示的元素
    LazyColumn {
        // items 子项 接受 List 作为参数
        items(messages) { message ->
            // 系统会针对提供的 List 的每个项调用此 lambda
            MessageCard(message)
        }
    }
}

@Preview
@Composable
fun PreviewConversation() {
    DefaultTheme {
        Conversation(SampleData.conversationSample) // 数据
    }
}
```