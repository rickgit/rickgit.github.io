## Flutter
[性能对比，包大小，启动速度，CUP/内存占用，刷新率；原生优于flutter](https://juejin.im/post/6844903666433032199)

[教程](https://github.com/ahyangnb/flutter_interview)
### 包大小与编译

Android debug模式（Flutter使用Kernel Snapshot 模式编译）
Android release模式（Flutter使用Core JIT 模式编译）


### 界面系统
```
                                         +> Center
                                         |
                     +---> InlineSpan +--+
                     |                   +->TextSpan
                     |
                     |
DiagnosticableTree +-+----> Widget ++--> PreferredSizeWidget +-+->  AppBar
                                    |                          |
                                    |                          +->  TabBar
                                    |
                                    +--> ProxyWidget+---------+-->  ParentDataWidget
                                    |                         |
                                    |                         +-->  Positioned
                                    |
                                    +--> RenderObjectWidget +-+---> SliverWithKeepAliveWidget  +---> SliverMultiBoxAdaptorWidget  +-> SliverGrid
                                    |                         |                                                                        SliverList
                                    |                         |
                                    |                         +-->  SingleChildRenderObjectWidget++> DecoratedBox
                                    |                         |                                   |                                   Center
                                    |                         |                                   +>  Align +---------------------->  ClipRect
                                    |                         |                                                                       AspectRatio
                                    |                         |                                                                       SizedBox
                                    |                         +-->  MultiChildRenderObjectWidget +->  RichText                        ColorFiltered
                                    |                                                            |
                                    |                                                            +-> Stack
                                    |                                                            |
                                    |                                                            +-> Flex  +----------------+------>  Row
                                    +-->  StatefulWidget +----+-->  PageView                                                |
                                    |                         |                                                             +------>  Column
                                    |                         |     MaterialApp
                                    |                         |     Scaffold
                                    |                         |     UserAccountsDrawerHeader
                                    |                         |
                                    |                         |     DefaultTabController
                                    |                         |     TabBarView
                                    |                         |     TabBar
                                    |                         |
                                    |                         |     SliverAppBar
                                    |                         |     Slider
                                    |                         |     SliverAnimatedList
                                    |                         |     FlexibleSpaceBar
                                    |                         |
                                    |                         |     TextField
                                    |                         |     Navigator
                                    |                         +->   StreamBuilderBase  +-------->  StreamBuilder
                                    |
                                    |
                                    +-->  StatelessWidget +--+---->  ScrollView   +------+-------> CustomScrollView
                                                             |                           |
                                                             |                           +-------> BoxScrollView +---+---->  ListView
                                                             |      IconButton                                       |
                                                             |      Icon                                             +---->  GridView
                                                             |      IconButton
                                                             |      CircleAvatar
                                                             |      FloatingActionButton
                                                             |
                                                             |      Container
                                                             |      SafeArea
                                                             |
                                                             |      Drawer
                                                             |      ListTile
                                                             |      Tab
                                                             |
                                                             +---> InkResponse   +--------------->  InkWell

```
#### 控件与容器对比
```
AppBar                           ActionBar/ToolBar


Text                             TextView
RaisedButton/Button              Button
DecorationImage/Image/BoxFit     ImageView
FloatingActionButton             FloatingActionButton
BottomNavigationBar              BottomNavigation
Center                           ViewGroup

Container/CustomMultiChildLayout RecyclerView 

Column/Row                       LinearLayout 

Stack                            FrameLayout/RelativeLayout

SingleChildScrollView            ScrollView

ListView                         ListView/RecyclerView

CustomScrollerView               RecyclerView

```

##### webview 建议用原生

####  当数据集比较大或列表动态加载时，需要使用更高效的方式ListView.Builder。这种方式基本上相当于Android的RecycleView
#### 字体
pubspec.yaml文件 定义
#### 装饰边框
InputDecoration

#### 手势检测和触摸事件处理


widget不支持事件检测，则需要包裹一个GestureDetector小部件
#### 主题

#### 生命周期

#### 窗口
resizeToAvoidBottomPadding 相当于
android:windowSoftInputMode="adjustsize"


#### 热加载

flutter\packages\flutter_tools\lib\src
### 消息传递
#### 路由 Navigator和Routes

#### MethodChannel 获取 Intent 数据
MethodChannel(dart)       MethodChannel(java)
    invokeMethod()          setMethodCallHandler()

#### startActivityForResult()
Map data = await Navigator.of(context).pushNamed('/page'); 
Navigator.of(context).pop("");


#### 异步 async/await
使用async/await执行网络请求，而不会引起UI线程挂起

#### NDK
不支持，先调用Java 访问jni方法



### 国际化 intl库





### 硬件等耗时操作

#### GPS geolocator
#### 相机 image_picker
#### 通知：firebase_messaging插件
#### Shared_Preferences插件/SQFlite插件




### 依赖管理 Pub包管理
 