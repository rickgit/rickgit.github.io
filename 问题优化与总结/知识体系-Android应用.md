## Android应用体系
> 软件是计算机系统中与硬件相互依存的另一部分，它是包括程序，数据及其相关文档的完整集合 《软件工程概论》
环境 Android 系统
阅读应用流程（以APK文档为主线，程序Main方法为入口）

``` dot
APK文件->Gradle编译脚本->APK打包安装及加载流程->AndroidManifest->四大组件->{Activity,Service,BrocastReceiver,ContentProvider}

APK打包安装及加载流程->Android系统架构->Android系统启动流程->Dalvik及framework初始化（packagemanager,activitymanager,resourcemanager,viewsystem）

四大组件->Handler消息机制

Activity->启动模式与任务栈->Activity生命周期（back和home键）->onCreate->setContentView->常用控件与布局方式->View的绘制流程->"Context 概念"->View获取Res资源流程->动画
 

Activity->"接收数据显示，传递数据到后台"->{SharedPreferences,文件存储,SQLite数据库方式,内容提供器（Content provider）,网络}
常用控件与布局方式->SurfaceView

Activity生命周期->onresume->View的事件响应流程

Service->数据操作,传递前台显示->Activity交互->AIDL等跨进程通信方式

ContentProvider->保存和获取数据，并使其对所有应用程序可见
```

## 3 Android 基础

### 3.1  Android 系统体系
```
+-----------------------------------------------------------------------+
|                                AppS                                   |
|                                                                       |
|                                                                       |
+-----------------------------------------------------------------------+
|                       App Framework                                   |
|                                                                       |
|     Activity Mgr        window Mgr    content provider    View System |
|                                                                       |
|  Package Mgr     Tel Mgr      Res Mgr      Loc Mgr      Notify Mgr    |
|                                                                       |
|                                                                       |
|                                                                       |
|                                                                       |
+-------------------------------------------------+---------------------+
|                  Libraries                      |  Android Runtime    |
|                                                 |  +----------------+ |
|  Surface Mgr     Media Framework    Sqlite      |  | core Libraries | |
|                                                 |  | dal^ik ^m      | |
|  OpenGL|ES       FreeType           Webkit      |  +----------------+ |
|                                                 +---------------------|
|  SGL             SSL/TLS            libc                              |
|                                                                       |
+-----------------------------------------------------------------------+
|                      Linux kernel                                     |
|  Display Driver      Camera Driver   Flash Driver   Bind (IPC) Driver |
|                                                                       |
|  KeyPad Driver    WIFI Driver    Audio Driver   Power Management      |
|                                                                       |
+-----------------------------------------------------------------------+

```

#### 应用层
- 四大组件，Fragment
```
                                            +--------+
                                            | Start  |
                                            |        |
                                            +----+---+
                                                 v

                                            +--------+
         +------------------------------->  |onCreate|
         |                                  |        |
         |                                  +---+----+
         |                                      v
         |  back to
         |  foreground                      +--------+                 +-----------+
         |                                  |onStart |  <--------------+ onRestart +-----+
         |                                  |        |                 |           |     |
+--------+---------+                        +----+---+                 +-----------+     |
| Process killed   |                             v                                       |
|                  |                                                                     |
+--------+---------+              +-------+ +--------+                                   |
         |                        v         |onResume|  <-----+                          |
         |                   +----+-----+   |        |        |                          |
         |                   |Running   |   +--------+        |  activity                |  activity
         |                   |          |                     |  froreground             |  foreground
         |            other  +----+-----+                     |                          |
         |            activity    |         +--------+        |                          |
         |            foreground  +-------> |onPause |        |                          |
         |  <-----------------------------+ |        | +----->+                          |
         | other app need memory            +---+----+                                   |
         |                                      v                                        |
         |                                                                               |
         |                                  +--------+                                   |
         |                                  |onStop  |                                   |
         +--------------------------------+ |        | +---------------------------------+
                                            +---+----+
                                                v

                                            +--------+
                                            |onDestroy
                                            |        +
                                            +---+----+
                                                v

                                            +--------+
                                            |shutdown|
                                            |        |
                                            +--------+



```
[事件](https://blog.csdn.net/shareus/article/details/50763237)
```
                                    +-------------------------+        +-----------------+
                                    |        Activity         |        | ACTION_DOWN     |
                                    |  +------------------+   |  <---+ |                 |
              +---------------------+  |dispatchTouchEvent|   |        +-----------------+
              |                     |  |                  |   |
              |            +------> |  +------------------+   |
              |            | True   |                         |
              |            |        |  +------------------+   |
              |            |        |  |onTouchEvent      |   |
              |            +----->  |  |                  |   |
              |            | False  |  +------------------+   |
              |            |        +-------------------------+
              |            |
              |            |                        True
              v            |             +---------------------------------+
                           |             |                                 |
       +-------------------+-------+     |                     +-----------+-----------------+
       |         ViewGroup         |     |                     |          View               |
       |    +-----------------+    |     |                     |     +-----------------+     |
+----> | +--+dispatchTouchEvent    |     |             +-----> |  +--+dispatchTouchEvent <-+ |
|      | |  |                 +    | <---+             |       |  |  |                 |   | |
|      | |  +-----------------+    |                   |       |  |  +-----------------+   | | True
|      | |                         |            False  |       |  |                        | |
+----+ | |  +-----------------+    |                   |       |  |  +-----------------+   | |
       | +> |onIntercept      |    |                   |       |  |  |onTouchEvent     |   | |
       |    |TouchE^ent       |  +---------------------+       |  +> |                 | +-+ |
       |    +-----------------+    |                           |     +-----------------+     |
       |                           |                           |                             |
       |    +-----------------+    |                           |                             |
       |    |onTouchEvent     |    |                           |                             |
       |    |                 |    |                           |                             |
       |    +-----------------+    |                           |                             |
       +---------------------------+                           +-----------------------------+


```

[渲染流程线](https://blog.csdn.net/cpcpcp123/article/details/79942700?utm_source=blogxgwz8)
UI对象—->CPU处理为多维图形,纹理 —–通过OpeGL ES接口调用GPU—-> GPU对图进行光栅化(Frame Rate ) —->硬件时钟(Refresh Rate)—-垂直同步—->投射到屏幕

Activity->PhoneWindow
WindowManagerGlobal


- Handler 消息机制
- AsyncTask
    
    容器类：ArrayDeque，LinkedBlockingQueue（ThreadPoolExecutor的线程队列）
    并发类：ThreadPoolExecutor（包含 ThreadFactory属性，用于创建线程），AtomicBoolean，AtomicInteger，FutureTask(包含Callable属性，任务执行的时候调用Callable#call,执行AsyncTask#dobackgroud)
- HandlerThread
- IntentService
- LruCache
- 窗口（Window，Activity，DecorView以及ViewRoot）
- View 测量，布局及绘制

- 进程通信
AIDL
文件
- Bitmap

- 动画
1. 补间动画
   烟花效果
2. 视图动画（Rotate,Scale,translate,alpha）
   箭头动画
   启动图片放大动画
   弹窗动画
3. 属性动画，插值器（Interpolator）和估值器（TypeEvaluator）
   估值器自定义滑动效果
4. Viewpager转化动画
5. SVG动画
6. Activity转场动画
7. Camera 3D动画
8. AR沉浸式效果（ARCore）

- 图形及用户界面
1. 界面及事件
2. openGl
   
- Context

- 持久化和序列化（Parcelable，Serializable）

#### dalvk
支持的垃圾回收机制
Mark-sweep算法：还分为Sticky, Partial, Full，根据是否并行，又分为ConCurrent和Non-Concurrent
MarkSweep::MarkSweep(Heap* heap, bool is_concurrent, const std::string& name_prefix)
mark_compact 算法：标记-压缩（整理)算法
concurrent_copying算法：
semi_space算法:


作者：Little熊猫
链接：https://www.jianshu.com/p/153c01411352
來源：简书
简书著作权归作者所有，任何形式的转载都请联系作者获得授权并注明出处。

### 3.2 Android 开发模式

#### 性能优化总结
- 渲染速度
    1. 布局优化（include merge, viewstub）
    分析工具，不必要不加载（include merge, viewstub），ConstaintLayout，Lint
    1. 绘制优化
    尽量用Drawable
    1. 响应速度优化
    2. ListView/RecycleView及Bitmap优化
    3. 线程优化
- 内存优化
- 电量消耗
    3.内存泄漏优化
        3.1 单例
        3.2 非静态内部类
        3.3 资源未关闭（webview没执行 destroy）
        3.4 ListView 未缓存
        3.5 集合类未销毁

- 其他性能优化的建议

#### 内存泄漏
 
#### 架构之模块化（插件化及组件化）
插件化
- Dynamic-loader-apk
- Replugin

组件化
- 组件间解耦
  1. AAC 
   ViewModel LiveData
  2. MVP DI框架Dagger2解耦
- 通信
1. 对象持有
2. 接口持有
3. 路由 （ARouter）
   Dagger2 依赖注入控制反转，

#### apk安装过程



#### 推送
- MQTT
1.  最核心的传输协议 Subcribe（定阅）和Publish（推送）
2. QoS（定阅等级）
   
- XMPP
1.  OpenFire管理服务器
2.  asmack包提供协议支持
3.  XMPP通信原语有3种：message、presence和iq

#### 直播
- 腾讯云直播（互动直播，收费）
1.  环境初始化与销毁
2.  房间初始化与销毁，声音控制
3.  上麦推流，下麦拉流
   
- 即构（互动视频）
1.  环境初始化与销毁
2.  房间初始化与销毁，声音控制
3.  推流和拉流

#### popupwindow 与 Dialog
- popupwindow 非阻塞浮层
- Dialog 阻塞式对话框
### 3.3 开源框架
#### OkHttp2

#### Retrofit

#### RPC

#### EventBus
反射与注解


#### ARouter
控制反转和面向切面

#### FLutter
