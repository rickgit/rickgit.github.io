## Flutter
[性能对比，包大小，启动速度，CUP/内存占用，刷新率；原生优于flutter](https://juejin.im/post/6844903666433032199)

[教程](https://github.com/ahyangnb/flutter_interview)

### 自定义解析 flutter
- 第一代 ionic(webview),PhoneGap，Cordova      H5                语言：js/css      环境：webview

- 第二代 RN（v8,Hermes/jscore）> weex(js,vue)  jit运行时编译     语言：JavaScript  环境：nodejs/v8 
   [RN](https://www.reactnative.cn/docs/components-and-apis)
- 第三代 Flutter（Skia）                                     语言：dart
#### Flutter 路由，UI
#### dart
https://dart.dev/samples

https://dart.dev/guides/language/language-tour


[内部实现](https://github.com/dart-lang/sdk/blob/master/sdk/lib/_internal/vm/lib/)


调试JIT，发布AOT

```
+-----------------------------------------------------------------------+
|                     Object                                            |
|                        hashCode :int                                  |
|                        runtimeType:Type                               |
|                                                                       |
+-----------------------------------------------------------------------+

+-----------------------------------------------------------------------+ 
|                     num                                               |
+-----------------------------------------------------------------------+
|   int                               double                            |
|    bitLength:int                                                      |
|    isEven: bool                      sign:int                         |
|    isOdd:bool                                                         |
|    sign:int                                                           |
|                                                                       |
+-----------------------------------------------------------------------+
+-----------------------------------------------------------------------+
|                          StringBuffer :::StringSink                   |
|                                                                       |
|                               _parts:List<String>?                    |
|                                                                       |
|                               length:int                              |
+-----------------------------------------------------------------------+
|                                                                       |
|                  String :::Comparable<String>, Pattern                |
|                     codeUnits:List<int>  //UTF-16                     |
|                     runes:Runes                                       |
|                     length:length                                     |
|                     hashCode:int                                      |
|                    _isOneByte :bool                                   |
+-----------------------------------------------------------------------+
|                                                                       |
|                    Runes : Iterable<int>                              |
|                       string: String                                  |
+-----------------------------------------------------------------------+

+-----------------------------------------------------------------------+
|                        _GrowableList                                  | 
|                          _grow()                        _shrink()     |
|                          _nextCapacity()                              |
|                          _allocateData() //odd number                 |
|                                                                       |
|         _List            Lists                                        |
|          length:int         copy()                                    |
|                                                                       |
+-----------------------------------------------------------------------+ 


+-----------------------------------------------------------------------+
|                                                                       |
|            _InternalLinkedHashMap<K, V> : _HashVMBase                 |
|                        ::  MapMixin<K, V>,                            |
|                            _LinkedHashMapMixin<K, V>,                 |
|                            _HashBase,                                 |
|                            _OperatorEqualsAndHashCode                 |
|                        :::  LinkedHashMap                             |
|                                                                       |
+-----------------------------------------------------------------------+
|              _index   :int                                            |
|              _hashMask :int                                           |
|              _data     :List                                          |//偶数位放key,奇数放value
|              _usedData :int                                           |
|              _deletedKeys :int                                        |
|                                                                       |
+-----------------------------------------------------------------------+


+-----------------------------------------------------------------------+
|                                                                       |
|             _CompactLinkedHashSet<E> :_HashFieldBase                  |
|                 :: _HashBase, _OperatorEqualsAndHashCode, SetMixin<E> |
|                 ::: LinkedHashSet                                     |
|                                                                       |
+-----------------------------------------------------------------------+
|              _index   :int                                            |
|              _hashMask :int                                           |
|              _data     :List                                          |
|              _usedData :int                                           |
|              _deletedKeys :int                                        |
|                                                                       |
+-----------------------------------------------------------------------+


```


```
+-----------------------------------------------------------------------------+
|                                                                             |
|       _File : FileSystemEntity ::: File                                     |
|                                                                             |
|           readAsStringSync():String                                         |
|                                                                             |
|          _tryDecode(bytes:List<int> ,encoding):String                       |
|                                                                             |
|          readAsBytesSync(): Uint8List                                       |
|                                                                             |
|          openSync(): RandomAccessFile                                       |
|                                                                             |
|                                                                             |
+-----------------------------------------------+-----------------------------+
|                                               |                             |
|                                               |   BytesBuilder              |
|  _RandomAccessFile ::: RandomAccessFile       |                             |
|        _connectedResourceHandler:bool = false;|       add()                 |
|         path;String                           |                             |
|        _asyncDispatched; bool   = false       |  _CopyingBytesBuilder       |
|        _fileService;SendPort                  |        :BytesBuilder        |
|        _resourceInfo; _FileResourceInfo       |     _length ;  int          |
|        _ops;_RandomAccessFileOps              |     _buffer;  Uint8List     |
|                                               | _BytesBuilder               |
|        lengthSync()                           |       :BytesBuilder         |
|        readSync(int bytes):Uint8List          |     _length:int             |
|                                               |    _chunks:List<Uint8List>  |
|                                               |                             |
+-----------------------------------------------+-----------------------------+


```

### Flutter UI框架

```
+-------------+-----------------+----------------------+
|             |   Material      |    Cupertino         |
|             +-----------------+----------------------+
|   Framework |             Wedgets                    |
|             +----------------------------------------+
|    (Dart)   |                                        |
|             |             Rendering                  |
|             +----------------------------------------+
|             |  Animation   Painting   Gestures       |
|             +----------------------------------------+
|             |             Foundation                 |
+-------------+----------------------------------------+
+-------------+----------------------------------------+
| Engine(C++) |    Skia     Dart    Text               |
+-------------+----------------------------------------+


```

Flutter动画分为两类: 补间动画(Tween Animation) 基于物理的动画(Physics-based animation) 

physics，scheduler,sematics,services
### 包大小与编译

Android debug模式（Flutter使用Kernel Snapshot 模式编译）
Android release模式（Flutter使用Core JIT 模式编译）


### 界面系统
```
                                         +- Center
                                         |
                     +--- InlineSpan <--+
                     |                   +- TextSpan
                     |
                     |
DiagnosticableTree <+-+---- Widget ++--  PreferredSizeWidget <-+--  AppBar
                                    |                          |
                                    |                          +--  TabBar
                                    |
                                    +--  ProxyWidget <--------+---  ParentDataWidget
                                    |                         |
                                    |                         +---  Positioned
                                    |
                                    +--  RenderObjectWidget <-+---  SliverWithKeepAliveWidget  <---  SliverMultiBoxAdaptorWidget  +-  SliverGrid
                                    |                         |                                                                        SliverList
                                    |                         |
                                    |                         +---  SingleChildRenderObjectWidget<-+- DecoratedBox
                                    |                         |                                    |                                   Center
                                    |                         |                                    +-  Align +----------------------   ClipRect
                                    |                         |                                                                       AspectRatio
                                    |                         |                                                                       SizedBox
                                    |                         +---  MultiChildRenderObjectWidget<-+--  RichText                        ColorFiltered
                                    |                                                             |
                                    |                                                             +-- Stack
                                    |                                                             |
                                    |                                                             +-- Flex +----------------+-------  Row
                                    +---  StatefulWidget <----+---  PageView                                                |
                                    |                         |                                                             +-------  Column
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
                                    |                         +--   StreamBuilderBase  +---------  StreamBuilder
                                    |
                                    |
                                    +---  StatelessWidget <--+-----  ScrollView   <------+-------- CustomScrollView
                                                             |                           |
                                                             |                           +-------- BoxScrollView <---+-----  ListView
                                                             |      IconButton                                       |
                                                             |      Icon                                             +-----  GridView
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
                                                             +---- InkResponse   <----------------  InkWell

```
#### 控件与容器对比
```
Flutter                          Android                          IOS
————————————————————————————————————————————————————————————————————————————————
AppBar                           ActionBar/ToolBar


Text                             TextView
TextField                        EditText

RaisedButton、FlatButton、        Button
OutlineButton、IconButton
(RawMaterialButton、
PopupMenuButton、
DropdownButton、BackButton、
CloseButton、ButtonBar、
ToggleButtons)

Radio                            RadioButton
Checkbox                         多选
Slider                           progressbar
Switch                           开关

DecorationImage/Image/BoxFit     ImageView
FloatingActionButton             FloatingActionButton
BottomNavigationBar              BottomNavigation
Center                           ViewGroup

Container/CustomMultiChildLayout RecyclerView 

Column/Row                       LinearLayout 

Stack/IndexedStack               FrameLayout/RelativeLayout

SingleChildScrollView            ScrollView

ListView                         ListView/RecyclerView

CustomScrollerView               RecyclerView

Wrap                             Constraint#flow
装饰权重组件SizedBox/AspectRatio  Constraint
```

##### webview 建议用原生

####  当数据集比较大或列表动态加载时，需要使用更高效的方式ListView.Builder。这种方式基本上相当于Android的RecycleView
#### 字体
pubspec.yaml文件 定义
#### 装饰边框
InputDecoration

#### 绘制
毛玻璃 DrawIndicator widget
绘制模糊,光影流动  paint.maskFilter = MaskFilter.blur(BlurStyle.outer, 20.0);
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
使用 async/await 执行网络请求，而不会引起UI线程挂起

#### NDK
不支持，先调用Java 访问jni方法



### 国际化 intl库
flutter.bat --no-color pub global list
flutter.bat --no-color pub global run intl_utils:generate

[Flutter Intl插件](https://juejin.cn/post/6996872498738364447)


### 硬件等耗时操作

#### GPS geolocator
#### 相机 image_picker
#### 通知：firebase_messaging插件
#### path_provider插件
#### Shared_Preferences插件/SQFlite插件

#### 图片 cached_network_image



### 依赖管理 Pub包管理


## 编译出错
[Gradle sync failed: Could not create task ](https://www.jianshu.com/p/9901c359cb19)


## ⭐lint项目
flutter pub get
flutter analyze


## dart 源码
https://github.com/dart-lang/sdk/blob/master/runtime/lib
https://github.com/dart-lang/sdk/blob/main/sdk/lib/_internal/vm/lib/array.dart
https://github.com/dart-lang/sdk/tree/main/sdk/lib/collection
