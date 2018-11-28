## Android应用体系
> 软件是计算机系统中与硬件相互依存的另一部分，它是包括程序，数据及其相关文档的完整集合 《软件工程概论》
环境 Android 系统
阅读应用流程（以APK文档为主线，程序Main方法为入口）

```diagram
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
|                                                 |  | dalvik vm      | |
|  OpenGL|ES (3d)  FreeType           Webkit      |  +----------------+ |
|                                                 +---------------------|
|  SGL(Skia 2d)    SSL/TLS            libc                              |
|                                                                       |
+-----------------------------------------------------------------------+
|                      Linux kernel                                     |
|  Display Driver      Camera Driver   Flash Driver   Bind (IPC) Driver |
|                                                                       |
|  KeyPad Driver    WIFI Driver    Audio Driver   Power Management      |
|                                                                       |
+-----------------------------------------------------------------------+

```

## Linux kernel -ipc
《Android 开发艺术探索》
基础知识：序列化和Binder
Serializable->Parcelable->Binder->{AIDL,Messenger}

AIDL 文件，方向指示符包括in、out、和inout；
[Binder在java framework层的框架](http://gityuan.com/2015/11/21/binder-framework/)
binder是C/S架构，分为Bn端(Server)和Bp端(Client)
Binder驱动不涉及任何外设，本质上只操作内存，负责将数据从一个进程传递到另外一个进程。
```
framework/base/core/java/android/os/
  - IInterface.java
  - IServiceManager.java
  - ServiceManager.java
  - ServiceManagerNative.java(包含内部类ServiceManagerProxy)

framework/base/core/java/android/os/
  - IBinder.java
  - Binder.java(包含内部类BinderProxy)
  - Parcel.java

framework/base/core/java/com/android/internal/os/
  - BinderInternal.java

framework/base/core/jni/
  - AndroidRuntime.cpp
  - android_os_Parcel.cpp
  - android_util_Binder.cpp

/framework/native/libs/binder         
    - IServiceManager.cpp
    - Interface.cpp
    - Binder.cpp
    - BpBinder.cpp
    - Parcel.cpp
    - IPCThreadState.cpp
    - ProcessState.

/kernel/drivers/android/
    - binder.c
    - binder_alloc.c
    - binder_alloc.h

/kernel/include/uapi/linux/android/
    - binder.h
```
[Binder系统分析](http://gityuan.com/2014/01/01/binder-gaishu/)
```
 +-------------------------------------------------------------------------------------------------------+
                        ServiceManager            IInterface                Binder


                        ServiceManagerNative      IServiceManager(aidl)     BinderProxy
                         (hava BinderProxy)                                  (have BpBinder address) 

  Framework layer       ServiceManagerProxy       IBinder(DeathRecipient)   BinderInternal(GcWathcer)
                        (aidl->stub,have BpBinder)
 +--------------------------------------------------------------------------------------------------------+


  JNI Layer             Android_util_Binder       android_os_Parcel         AndroidRuntime

+--------------------------------------------------------------------------------------------------------+

                        JavaBBinder              JavaBBinderHolder


                        BpServiceManager          BpInterface               BpBinder（client, transact()）
                        (extends Bpinterface)

                        BnServiceManager          BnInterface               BBinder（server, onTransact()）


                        IserviceManager           IInterface                ProcessState(create binder,)))(bbinder,BpBinder)


  C Layer               BpRefBase                 IBinder                   IPCThreadState
                        (base class)
 +----------------------------------------------------------------------------------------------------------+

  Kernel dev
  driver layer          /dev/binder（open/mmap/ioctl指令）

 +-----------------------------------------------------------------------------------------------------------+

```
AIDL 文件生成对应类，类里包含继承Binder的内部类和实现AIDL的内部类；

```
public class Binder implements IBinder {//三个非静态字段

    private final long mObject;//BpBinder对象地址

    private IInterface mOwner;//AIDL接口
    private String mDescriptor;//表示AIDL的接口名：edu.ptu.java.aidl.IMyAidlInterface
}

```
- Bundle(实现了接口Parcelable)
- 文件共享
- AIDL
- Messenger(AIDL)
- contentProvider（Binder）
- socket（ Zygote通信）
## C
c++的智能指针有很多实现方式，有auto_ptr ,  unique_ptr , shared_ptr 三种， Android 中封装了sp<> 强指针，wp<>弱指针的操作
### [图形系统](http://gityuan.com/2017/02/05/graphic_arch/)
Canvas是一个2D的概念，是在Skia中定义的
Skia 2D和OpenGL/ES 3D

OpengGL/ES两个基本Java类： GLSurfaceView,Renderer

## dalvik
[支持的垃圾回收机制](https://www.jianshu.com/p/153c01411352)
Mark-sweep算法：还分为Sticky, Partial, Full，根据是否并行，又分为ConCurrent和Non-Concurrent
MarkSweep::MarkSweep(Heap* heap, bool is_concurrent, const std::string& name_prefix)
mark_compact 算法：标记-压缩（整理)算法
concurrent_copying算法：
semi_space算法: 
[android hash](https://blog.csdn.net/xusiwei1236/article/details/45152201)
- **SparseArray**
  稀疏数组问题就是数组中的大部分的内容值都未被使用或者都为0，在数组中仅有少部分的空间使用。
```
public class SparseArray<E> implements Cloneable {
    private boolean mGarbage = false;// 1byte
    private int[] mKeys;//4 byte
    private Object[] mValues;//4byte
    private int mSize;//4byte
}

```
初始容量  mKeys，mValues 返回是长度为12
加载因子（0.0~1.0）  1f
扩容增量 //TODO

查找 二分法（key 有已排序）
插入 key为Integer，排序。找到key，替换掉原来的值。没找到，根据二分法返回的位置，插入有序key
删除 数组设置为常量 DELETED（new Object()）
gc  标记为DELETED，key,Value替换为有值的数据


**ArrayMap**
```
public final class ArrayMap<K, V> implements Map<K, V> {
    final boolean mIdentityHashCode;// 1byte，default false
    int[] mHashes;//4 byte，存储key的hash值
    Object[] mArray;//4byte,size 为mHashes的两倍，存储key（index=hashIndex<<1），value
    int mSize;//4 byte
    MapCollections<K, V> mCollections;//4byte

}
```
初始容量  mKeys，mValues 0
加载因子（0.0~1.0）  mSize>=mHashes长度
扩容增量（扩容hash表）大于等于8,扩容原来的一半
```
final int osize = mSize;
int n=osize >= (4*2) ? (osize+(osize>>1)): (osize >= 4 ? (4*2) : 4)
```

查找 二分法mHashes表，
插入 从mHashes二分法找key的hash，mHashes向后向前查找key，执行替换。没找到hash或key，System.arraycopy执行插入key,value（先判断扩容）
删除  左移动mhash,mValue，根据情况调整新的大小后，填掉删除的位置
```
删除后调整大小
final int osize = mSize;
if (mHashes.length > (BASE_SIZE*2) && mSize < mHashes.length/3) {
    final int n = osize > (BASE_SIZE*2) ? (osize + (osize>>1)) : (BASE_SIZE*2);
}
```


## 应用层

``` dot
APK文件->Gradle编译脚本->APK打包安装及加载流程->AndroidManifest->四大组件->{Activity,Service,BrocastReceiver,ContentProvider}

APK打包安装及加载流程->Android系统架构->[Android系统启动流程](http://gityuan.com/2016/02/01/android-booting/)->Dalvik及framework初始化（packagemanager,activitymanager,resourcemanager,viewsystem）

四大组件->Handler消息机制

Activity->启动模式与任务栈->Activity生命周期（back和home键）->onCreate->setContentView->常用控件与布局方式->View的绘制流程->"Context 概念"->View获取Res资源流程->动画
 

Activity->"接收数据显示，传递数据到后台"->{SharedPreferences,文件存储,SQLite数据库方式,内容提供器（Content provider）,网络}
常用控件与布局方式->SurfaceView

Activity生命周期->onresume->View的事件响应流程

Service->数据操作,传递前台显示->Activity交互->AIDL等跨进程通信方式

ContentProvider->保存和获取数据，并使其对所有应用程序可见
```
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
         |                                      v    no longe visiable                   |
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

[四大组件的管理](http://gityuan.com/2017/05/19/ams-abstract/)
[Activity启动模式](gityuan.com/2017/06/11/activity_record/)

[事件](https://blog.csdn.net/shareus/article/details/50763237)
[Touch事件](http://gityuan.com/2016/12/10/input-manager/)
```
public final class MotionEvent extends InputEvent implements Parcelable {
    private long mNativePtr;
    private MotionEvent mNext;

    protected int mSeq;
    /** @hide */
    protected boolean mRecycled;
    private RuntimeException mRecycledLocation;

}
```
事件传递由 **Activity#dispatchTouchEvent**开始，有PhoneWindow传到Decorview进行遍历
``` 
+-----------------------------------+
|               Activity            |
|  +-----------------------------+  |
|  |       Phone Window          |  |
|  |   +---------------------+   |  |
|  |   |     DecorView       |   |  |
|  |   |  +---------------+  |   |  |
|  |   |  |  +----------+ |  |   |  |     +------------------+    +-----------+
|  |   |  |  |TitleView +---------------->+ActionBarContainer+--->+ ActionBar |
|  |   |  |  +----------+ |  |   |  |     +------------------+    +-----------+
|  |   |  |  +----------+ |  |   |  |
|  |   |  |  |          | |  |   |  |
|  |   |  |  |          | |  |   |  |
|  |   |  |  | Content  | |  |   |  |
|  |   |  |  |          | |  |   |  |     +------------------+
|  |   |  |  |   View   +---------------->+ FrameLayout      |
|  |   |  |  |          | |  |   |  |     +------------------+
|  |   |  |  |          | |  |   |  |
|  |   |  |  |          | |  |   |  |
|  |   |  |  |          | |  |   |  |
|  |   |  |  |          | |  |   |  |
|  |   |  |  +----------+ |  |   |  |
|  |   |  +---------------+  |   |  |
|  |   +---------------------+   |  |
|  |                             |  |
|  +-----------------------------+  |
+-----------------------------------+

```
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







### 3.2 Android 开发模式

#### 性能优化总结
>《Android开发艺术探索》
方法：布局，绘制，内存泄漏，响应速度，Listview及Bitmap，线程优化
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
工具：profile，eclipse mat
可维护性：组件化
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
[安装](http://gityuan.com/2016/11/13/android-installd/)


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
