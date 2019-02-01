## Android应用体系
> 软件是计算机系统中与硬件相互依存的另一部分，它是包括程序，数据及其相关文档的完整集合 《软件工程概论》
环境 Android 系统
阅读应用流程（以APK文档为主线，程序Main方法为入口）
[启动流程](https://blog.csdn.net/qq_30993595/article/details/82714409#_2)
```diagram
+-----------------------------------------------------------------------+
|                                AppS                                   |
|                                                                       |
+-----------------------------------------------------------------------+
|                              component                                |
|                                                                       |
|  Fragments   Views  Layouts Controls  Intents    Resources    Manifest|
|                                                                       |
|  Activities     Services     Broadcast Receivers     Content Providers|
|                                                                       |
+-----------------------------------------------------------------------+
|                       App Framework                                   |
|                                                                       |
|                                                                       |
|  AMS             WMS           content provider       View System     |
|                                                                       |
|                                                                       |
|  PMS             Tel Mgr       Res Mgr     Loc Mgr    Notify Mgr      |
|                                                                       |
|                                                                       |
+-------------------------------------------------+---------------------+
|                  Libraries                      |  Android Runtime    |
|                                                 |  +----------------+ |
|                                                 |  | dalvik vm      | |
|  OpenGL+ES (3d)  SSL/TLS           Webkit       |  +----------------+ |
|                                                 +---------------------+
|  SGL(Skia 2d)    FreeType          libc(bionic)                       |
|                                                                       |
+-----------------------------------------------------------------------+
|                      Linux kernel                                     |
|                                                                       |
|  Bind (IPC) Driver   Display Driver     Camera Driver   Flash Driver  |
|                                           (V4L2)                      |
|                                                                       |
|  KeyPad Driver      Power Management    Audio Driver   WIFI Driver    |
|                                                                       |
|  Bluetooth Driver   USB Driver                                        |
|                                                                       |
+-----------------------------------------------------------------------+
|                                                                       |
|                                        ^                              |
|                                        |                              |
|  Loader +--->Boot ROM+--->Boot Loader+-+                              |
|                                                                       |
+-----------------------------------------------------------------------+


```

## Linux kernel -ipc
### 进程启动
>启动Kernel的swapper进程(pid=0)：该进程又称为idle进程, 系统初始化过程Kernel由无到有开创的第一个进程, 用于初始化进程管理、内存管理，加载Display,Camera Driver，Binder Driver等相关工作。
启动kthreadd进程（pid=2）：是Linux系统的内核进程，会创建内核工作线程kworkder，软中断线程ksoftirqd，thermal等内核守护进程。kthreadd进程是所有内核进程的鼻祖。
[作者：硬刚平底锅 ] (https://blog.csdn.net/qq_30993595/article/details/82714409 )


[Android Init进程源码分析](https://blog.csdn.net/yangwen123/article/details/9029959)
```
>ps
USER      PID   PPID  VSIZE  RSS   WCHAN            PC  NAME
root      1     0     2732   1264     ep_poll 08126b05 S /init
root      2     0     0      0       kthreadd 00000000 S kthreadd
root      86    2     0      0     rescuer_th 00000000 S binder
root      245   1     1543944 32436 poll_sched b72b7f70 S zygote
system    618   245   1684724 70680    ep_poll b72b7ca5 S system_server （创建wms,ams,pms）
system    231   1     4024   1360  binder_thr b753ae76 S /system/bin/servicemanager（管理service）
root      232   1     46892  3400     ep_poll b746dca5 S /system/bin/surfaceflinger

-----------------------------------
   +---------------------+
   |  start_kernel(^oid) |
   +---------+-----------+
             v

   +---------------------+
   |  reset_init(^oid)   +--------------+-----------------------------+
   +---------------------+              |                             |
                                        |                             |
                                        v                             v

                           +---------------------+      +---------------------------+
                           | Init Process        |      |    KThread Process        |
                           |                     |      |                           |
                           |                     |      |                           |
+------------------+       |                     |      |                           |
| zygote Process   |       |                     |      |                           |
|                  |       |                     |      |                           |
|                  |  <----+                     |      |                           |
|                  |       |                     |      |                           |
|                  |       |                     |      |                           |
|                  |       |                     |      |                           |
| +--------------+ |       |   +---------------+ |      |                           |
| |system_server | |       |   |Service Manager| |      |                           |
| +--------------+ |       |   +---------------+ |      |                           |
| |              | |       |   +---------------+ |      |                           |
| | AMS,WMS,PMS..| |       |   | mediaserver   | |      |                           |
| | ServerThread | |       |   +---------------+ |      |                           |
| |   JSS        | |       |   +---------------+ |      |                           |
| | AlarmManager | |       |   |SurfaceFlinger | |      |                           |
| +--------------+ |       |   +---------------+ |      |                           |
|                  |       |                     |      |                           |
|                  |       |                     |      |                           |
|                  |       |                     |      |                           |
+------------------+       +---------------------+      +---------------------------+

  Ja^a Process                 Nati^e Process                Kernel Dri^er Thread


```

《Linux设备驱动程序》
《Android 开发艺术探索》
基础知识：序列化和Binder
Binder是misc设备上进行注册,作为虚拟字符设备。Binder transaction buffer，这块内存有一个大小限制，目前是1MB
```c
 > ls -l /dev/
crw-rw-rw- root     root      10,  54 2018-12-03 20:23 binder //c代表字符设备文件
drwxr-xr-x root     root              2018-12-13 15:15 input
drwxr-xr-x root     root              2018-12-13 15:16 socket
crw-rw-rw- 1 root   root      10,  62 2019-01-17 09:44 ashmem

LINUX中的七种文件类型
d 目录文件。
l 符号链接(指向另一个文件,类似于瘟下的快捷方式)。
s 套接字文件。
b 块设备文件,二进制文件。
c 字符设备文件。
p 命名管道文件。
- 普通文件

其中Linux中I/O设备分为两类:字符设备和块设备。
[创建7种类型文件](https://blog.csdn.net/furzoom/article/details/77888131)
```
###  IPC机制与方法 
Linux中的RPC方式有管道，消息队列，共享内存等。
消息队列和管道采用存储-转发方式，即数据先从发送方缓存区拷贝到内核开辟的缓存区中，然后再从内核缓存区拷贝到接收方缓存区，这样就有两次拷贝过程。
Binder一次拷贝原理(直接拷贝到目标线程的内核空间，内核空间与用户空间对应)。
```java
性能，并发，一对多
                         +-----------+---------+---------------------+
                         |           |         |                     |
                         |           | Messager|  Content Provider   |
                         | Bundle    |         |                     |
                         |           |         |                     |
         +---------------------------+---------+--------------------------------------+-----------------+
         |               |                                           |                |                 |
         | ashmem        |   AIDL    +-------------------------------+                |                 |
         |               |           | byte, char, short, int, long, |                |                 |
         |               |           | float, double, boolean        |                |                 |
         |               |           | String, CharSequence          |                |                 |
         |               |           | Parcelable                    |                |                 |
         |               |           | List<>, Map<>                 |                |                 |
         |               |           | aidl interface                |                |                 |
         |               |           |                               |                |                 |
         |               |           +-------------------------------+                |                 |
         |               |           | import Parcelable package     |                |                 |
         |               |           +-------------------------------+                |                 |
         |               |           | in out inout                  |                |                 |
         |               |           +-------------------------------+                |                 |
         |               |           | oneway                        |                |                 |
         |               |           +-------------------------------+                |                 |
         |               |                                           |                |                 |
         |               |-------------------------------------------|                |                 |
         |               |   android.os.Binder                       |                |                 |
         +-----------------------------------------------------------+                |                 |
         |               |                                           |  Socket        |  File           |
         | Shared memory |   Binder                                  |  pipe          | SharedPreference|
         |               |                                           |  messagequeue  |                 |
         +----------------------------------------------------------------------------------------------+
         |               |                                           |                |                 |
copy     |      0        |                 1                         |       2        |                 |
times    |               |                                           |                |                 |
         +---------------+-------------------------------------------+----------------+-----------------+

public class Intent implements Parcelable, Cloneable {
    private String mAction;
    private Uri mData;
    private String mType;
    private String mPackage;
    private ComponentName mComponent;
    private int mFlags;
    private ArraySet<String> mCategories;
    private Bundle mExtras;
    private Rect mSourceBounds;
    private Intent mSelector;
    private ClipData mClipData;
    private int mContentUserHint = UserHandle.USER_CURRENT;
    /** Token to track instant app launches. Local only; do not copy cross-process. */
    private String mLaunchToken;
}
public final class Messenger implements Parcelable {
    private final IMessenger mTarget;
}

import android.os.Message;
/** @hide */
oneway interface IMessenger {
    void send(in Message msg);
}
```


```bash
root@x86:/ # ls /dev/socket/
adbd
cryptd
dnsproxyd
fwmarkd
installd
lmkd
logd
logdr
logdw
mdns
netd
property_service
rild
rild-debug
sap_uim_socket1
vold
wpa_eth1
zygote// zygote socket通信设备文件

```
Serializable->Parcelable->Binder->{AIDL,Messenger}

### Binder机制
[Binder在java framework层的框架](http://gityuan.com/2015/11/21/binder-framework/)
binder是C/S架构，包括Bn端(Server)和Bp端(Client)，ServiceManager,Binder驱动
Binder驱动不涉及任何外设，本质上只操作内存，负责将数据从一个进程传递到另外一个进程。
[Binder机制分析](http://gityuan.com/2014/01/01/binder-gaishu/)
```java
n：native
p：proxy

SystemServer，Binder机制
+----------------+------------+-------------------------+--------------------------------------+
|                |            |                         |                                      |
|                |            | +---------------------+ |  BinderProxy   Ser^iceManagerProxy   |
|                |            | | IInterface          | |  Ser^iceManager                      |  binder(0)/binders
|                |  Client    | | IBinder             | +--------------------------------------+ +-----------------+
|                |            | | IServiceManager     | |   BpBinder/BpRefBase   BpInterface   |                   |
|                |  process   | |                     | |                                      |                   |
|                |            | +---------------------+ |   BpServiceManager                   |                   |
|                |            | | Android_util_Binder | |                                      |                   |
|                |            | | android_os_Parcel   | |  frameworks//IPCThreadState.cpp      |                   |
|                +------------+ | AndroidRuntime.cpp  | +--------------------------------------+                   |
|                |            | +---------------------+ | Binder    ServiceManagerNative       |  binder(0)/binders|
|                |            | | IInterface          | | BinderInternal                       | +-----------+     |
|  user space    |  Server    | | IBinder             | +--------------------------------------+             |     |
|                |            | | IserviceManager     | | BBinder/JavaBBinder/JavaBBinderHolder|             |     |
|                |  process   | | ProcessState        | | BnInterface                          |             |     |
|                |            | | IPCThreadState      | |                                      |             |     |
|                |            | +---------------------+ | BnServiceManager                     |             |     |
|                |            |                         | frameworks//IPCThreadState.cpp       |             |     |
|                +--------------------------------------+--------------------------------------+  binder(0)  |     |
|                |  Service   |                                                                | +------+    |     |
|                |            |  servicemanager/binder.c                                       | binders|    |     |
|                |  Manager   |                                                                |        |    |     |
|                |            |  service_manager.c                                             |        |    |     |
|                |  process   |                                                                |   +----+----+-----+--+
+----------------+------------+----------------------------------------------------------------+   |                  |
                                                                                                   |  open/mmap/ioctl |
                                                                                                   |                  |
                                                                                                   +----+----+-----+--+
+----------------+------------+----------------------------------------------------------------+        |    |     |
|                |            |                                                                | <------+    |     |
|                |  Binder    |                                                                |             |     |
|  kernel space  |            |   drivers/staging/android/binder.c                             | <-----------+     |
|                |  Driver    |                                                                |                   |
|                |            |                                                                | <-----------------+
+----------------+------------+----------------------------------------------------------------+

                                                                                  +       ^
                                                                                  |       |
                                                                                  v       +

                                                                               +---------------+
                                                                               |               |
                                                                               |  kernel memory|
                                                                               |               |
                                                                               +---------------+

----

binder的服务实体
+------------+----------------------------------+------------------------------+
|            |   System Service                |    Local Service(bindService) |
|            |                                 |                               |
+------------------------------------------------------------------------------+
|            |                                 |                               |
|  launch    | SystemServer                    |  bindService                  |
|            |                                 |                               |
+------------------------------------------------------------------------------+
|            |                                 |                               |
| regist and |ServiceManager.addService        |  ActivityManagerService       |
| manager    |                                 |                               |
|            |SystemServiceManager.startService|                               |
|------------+---------------------------------+-------------------------------+
|            |                                 |                               |
| communicate| SystemServer#getService         |  ServiceConnection            |
|            |                                 |  (binder.asInterface)         |
|------------+---------------------------------+-------------------------------+

通过startService开启的服务，一旦服务开启，这个服务和开启他的调用者之间就没有任何关系了（动态广播 InnerReceiver）;
通过bindService开启服务，Service和调用者之间可以通讯。
```
AIDL 文件生成对应类，类里包含继承Binder的内部类和实现AIDL的内部类；

```java
public class Binder implements IBinder {//三个非静态字段

    private final long mObject;//BpBinder对象地址

    private IInterface mOwner;//AIDL接口
    private String mDescriptor;//表示AIDL的接口名：edu.ptu.java.aidl.IMyAidlInterface
}


public final class Parcel {
    private long mNativePtr; // used by native code

    /**
     * Flag indicating if {@link #mNativePtr} was allocated by this object,
     * indicating that we're responsible for its lifecycle.
     */
    private boolean mOwnsNativeParcelObject;
    private long mNativeSize;

    private ArrayMap<Class, Object> mClassCookies;

    private RuntimeException mStack;

    private ReadWriteHelper mReadWriteHelper = ReadWriteHelper.DEFAULT;


}
```

- Bundle(实现了接口Parcelable)
```java
public class BaseBundle {
    // Invariant - exactly one of mMap / mParcelledData will be null
    // (except inside a call to unparcel)

    ArrayMap<String, Object> mMap = null;

    /*
     * If mParcelledData is non-null, then mMap will be null and the
     * data are stored as a Parcel containing a Bundle.  When the data
     * are unparcelled, mParcelledData willbe set to null.
     */
    Parcel mParcelledData = null;

    /**
     * Whether {@link #mParcelledData} was generated by native coed or not.
     */
    private boolean mParcelledByNative;

    /**
     * The ClassLoader used when unparcelling data from mParcelledData.
     */
    private ClassLoader mClassLoader;

    /** {@hide} */
    @VisibleForTesting
    public int mFlags;
}
```



## Native Layer
init进程会孵化出ueventd、logd、healthd、installd、adbd、lmkd等用户守护进程
init进程还启动servicemanager(binder服务管家)、bootanim(开机动画)等重要服务
init进程孵化出Zygote进程，Zygote进程是Android系统的第一个Java进程(即虚拟机进程)，Zygote是所有Java进程的父进程
--------------------- 
[作者：硬刚平底锅  ](https://blog.csdn.net/qq_30993595/article/details/82714409)

c++的智能指针有很多实现方式，有auto_ptr ,  unique_ptr , shared_ptr 三种， Android 中封装了sp<> 强指针，wp<>弱指针的操作


### 数据显示 SurfaceFlinger - [Graphic图形系统](http://gityuan.com/2017/02/05/graphic_arch/)

SystemServer的RenderThread线程
黄油计划,用于提升系统流畅度：
- 垂直同步（Vertical Synchronized ） 即 VSYNC 定时中断
- 三重缓存（Triple Buffer ）,跳帧保证不帧
- 编舞者/编排器（Choreographer ）, 起到调度作用（ViewRootImpl实现统一调度界面绘图），绘制速度和屏幕刷新速度保持一致
黄油计划的核心VSYNC信号分为两种，一种是硬件生成（HardwareComposer）的信号，一种是软件模拟（VSyncThread来模拟）的信号。
1. 解决撕裂问题，CPU/GPU调度快于Display，保证双缓冲;
2. 2.janking问题，CPU来不及处理，Display显示前一帧，帧延迟。

CPU：负责 Measure、Layout、Record、Execute 的计算操作。CPU 负责把 UI组件计算成 Polygons（多边形）和 Texture（纹理），然后交给 GPU 进行栅格化。
GPU：负责 Rasterization（栅格化）操作。GPU 的栅格化过程是绘制 Button、Shape、Path、String、Bitmap 等组件最基础的操作。
```java
+--------------------------------------------------------------------------------------------------------------+
|                                                                                                              |
+---------------------+                                                          +-----------------------------+
| App process         |              +------------------------+                  | SurfaceFlinger              |
|                     |              |  wms                   |                  |                             |
|                     |              |                        |                  |                             |
|                     | <----------> | SurfaceComposerClient  |  <------------>  |                             |
|   +-----------------+              |                        |                  |                             |
|   |   Measure()     |              |                        |                  |  +---------+----------+     |
|   |   layout()      |              |                        |                  |  |         |          |     |
|   |   draw()        |              +------------------------+                  |  |  client |  client  |     |
|   |                 |                                                          |  |         |          |     |
|   +-----------------+                                                          |  +---------+----------+     |
|   |   Choreographer |                                                          |                             |
+---+-----------------+----------------------------------------------------------+-----------------------------+
|                                                                                                              |
|  SharedClient (Tmpfs Ashmem)                                                                                 |
|         +---------------------------------+--------------------+                                             |
|         |                                 |                    |                                             |
|         |  SharedBufferStack              |  SharedBufferStack |                                             |
|         |      +--------------------------+                    |                                             |
|         |      | Front Buffer             |                    |                                             |
|         |      | (Display)                |                    |                                             |
|         |      +--------------------------+                    |                                             |
|         |      | Back Buffer| Back Buffer |                    |                                             |
|         |      | (CPU,GPU)  | (CPU,GPU)   |                    |                                             |
|         +------+------------+----------------------------------+                                             |
|         |                                 |                    |                                             |
|         |  SharedBufferStack              |  ...(31)           |                                             |
|         |                                 |                    |                                             |
|         +---------------------------------+--------------------+                                             |
|                                                                                                              |
+--------------------------------------------------------------------------------------------------------------+
|                   Vertical Synchronized                                                                      |
+--------------------------------------------------------------------------------------------------------------+


```
```java
                            VSync                 VSync                VSync           //Display为基准，VSync将其划分成16ms长度的时间段
                               +                    +                    +
          +-------------------------------------------------------------------------+
          |                    |                    |                    |          |
Display   |                    |                    |                    |          |
          +-------------------------------------------------------------------------+
                               |                    |                    |
                               |                    |                    |
                   +-----------+-+      +-----------+----+ +-----------+ |
GPU                | Frame1      |      | Frame2         | | Frame3    | |
                   +-----------+-+      +-----------+----+ +-----------+ |
                               |                    |                    |
                               |                    |                    |
                               |                    |                    |
          +-------+            +--------+           +------+             |
CPU       | Frame1|            | Frame2 |           |Frame3|（前一个CPU Frame占用中）|  //CPU/GPU的FPS不等同Display的FPS，需要三级缓存
          +-------+            +--------+           +------+（使用第三块缓存）       |
                               |                    |                    |
                               |                    |                    |
                               |                    |                    |
                               +                    +                    +

```


```java
    class EventHandler {
        friend class HWComposer;
    };
```

```java
base/core/java/android/view/ViewRootImpl.java:511:        mChoreographer = Choreographer.getInstance();

public final class Choreographer {
...
...
...
    private final FrameHandler mHandler;

    // The display event receiver can only be accessed by the looper thread to which
    // it is attached.  We take care to ensure that we post message to the looper
    // if appropriate when interacting with the display event receiver.
    private final FrameDisplayEventReceiver mDisplayEventReceiver;

    private CallbackRecord mCallbackPool;

    private final CallbackQueue[] mCallbackQueues;//"input", "animation", "traversal"（测量、布局、绘制流程）, "commit"（遍历完成的提交操作，用来修正动画启动时间）

    private boolean mFrameScheduled;
    private boolean mCallbacksRunning;
    private long mLastFrameTimeNanos;
    private long mFrameIntervalNanos;
    private boolean mDebugPrintNextFrameTimeDelta;
    private int mFPSDivisor = 1;

    /**
     * Contains information about the current frame for jank-tracking,
     * mainly timings of key events along with a bit of metadata about
     * view tree state
     *
     * TODO: Is there a better home for this? Currently Choreographer
     * is the only one with CALLBACK_ANIMATION start time, hence why this
     * resides here.
     *
     * @hide
     */
    FrameInfo mFrameInfo = new FrameInfo();
}


private static final class CallbackRecord {
    public CallbackRecord next;
    public long dueTime;
    public Object action; // Runnable or FrameCallback
    public Object token;
}



```


```
root@x86:/ # ls -l /dev/graphics/
crw-rw---- root     graphics  29,   0 2018-12-13 15:15 fb0
```


```java
public class Surface implements Parcelable {

    private final CloseGuard mCloseGuard = CloseGuard.get();

    // Guarded state.
    final Object mLock = new Object(); // protects the native state
    private String mName;
    long mNativeObject; // package scope only for SurfaceControl access
    private long mLockedObject;
    private int mGenerationId; // incremented each time mNativeObject changes
    private final Canvas mCanvas = new CompatibleCanvas();

    // A matrix to scale the matrix set by application. This is set to null for
    // non compatibility mode.
    private Matrix mCompatibleMatrix;

    private HwuiContext mHwuiContext;

    private boolean mIsSingleBuffered;
    private boolean mIsSharedBufferModeEnabled;
    private boolean mIsAutoRefreshEnabled;
}

```
#### 绘制事件 - ViewRootImpl#traversal

#### 动画事件 
[动画天梯榜](https://zhuanlan.zhihu.com/p/45597573?utm_source=androidweekly.io&utm_medium=website)
```
 +-------------+-------------------------------+------------------+-------------------+
 |  layout     |                               | request layout   |  requestLayout()  |
 |  Animation  |                               |                  |  draw Command     |
 +------------------------------------------------------------------------------------+
 |Drawble/Frame|   webp/gif                    |                  |                   |
 | Animation   |                               |                  |                   |
 +---------------------------------------------+  invalidate draw |  draw command     |
 |             |                               |                  |                   |
 |             |   draw() + invalidate()       |                  |                   |
 |  Draw       +-------------------------------+                  |                   |
 |  Animation  |                               |                  |                   |
 |             |   computeScroll()+invalidate()|                  |                   |
 +------------------------------------------------------------------------------------+
 |             |                               |                  |                   |
 |  View/Tween |  translate/rotate/scale/alpha |  parent          |  View Property    |
 |             |                               |                  |                   |
 |  Animation  |  Fragment compat Transition   |  Render Node     |                   |
 |             |                               |                  |                   |
 +------------------------------------------------------------------------------------+
 |             |   View.animate()              |                  |                   |
 |  Animator   |                               |                  |                   |
 |             |   offsetChildrenTopAndBottom  |  Render Property |  View Property    |
 |             |                               |                  |                   |
 |             |   ActivityOptions Transition  |                  |                   |
 +------------------------------------------------------------------------------------+
 |  Render     |   AnimatedVectorDrawable      |                  |                   |
 |  Thread     |   RippleCompount              |  Inter-Process   | view property     |
 |  Amimation  |   Revel Animation             |                  |                   |
 +------------------------------------------------------------------------------------+
 |  Window     |                               | SystemServer     |                   |
 |  Transition |   Window Transition Animation | WindowManager    | some view property|
 |  Animation  |                               |                  |                   |
 +-------------+-------------------------------+------------------+-------------------+

```
- 视图动画 Animation（Rotate,Scale,translate,alpha）
  +android.view.View#draw(android.graphics.Canvas, android.view.ViewGroup, long)
    +android.view.View#applyLegacyAnimation
   箭头动画
   启动图片放大动画
   弹窗动画
```java
public class View implements Drawable.Callback, KeyEvent.Callback,
        AccessibilityEventSource {
    /**
     * The animation currently associated with this view.
     * @hide
     */
    protected Animation mCurrentAnimation = null;
```
```java
public class View implements Drawable.Callback, KeyEvent.Callback,
        AccessibilityEventSource {
    /**
     * The animation currently associated with this view.
     * @hide
     */
    protected Animation mCurrentAnimation = null;
```

```java
final class BackStackRecord extends FragmentTransaction implements
        FragmentManager.BackStackEntry, FragmentManagerImpl.OpGenerator {

    int mEnterAnim;
    int mExitAnim;
    int mPopEnterAnim;
    int mPopExitAnim;
}

```

- 帧动画 AnimationDrawable
     烟花效果
    +android.graphics.drawable.Drawable#scheduleSelf
        +android.view.View#scheduleDrawable //mChoreographer.postCallbackDelayed( Choreographer.CALLBACK_ANIMATION...)
- 属性动画 Animator
    +android.animation.ValueAnimator#start(boolean)
          +android.animation.AnimationHandler#addAnimationFrameCallback// Choreographer#postCallbackDelayedInternal(CALLBACK_ANIMATION
```java
public class View implements Drawable.Callback, KeyEvent.Callback,
        AccessibilityEventSource {
    /**
     * Object that handles automatic animation of view properties.
     */
    private ViewPropertyAnimator mAnimator = null;
}
```
估值器自定义滑动效果
插值器（Interpolator）和估值器（TypeEvaluator）

- 窗口转场
  [窗口转场](https://www.jianshu.com/p/c3217053d18d)
  +com.android.server.wm.WindowManagerService#relayoutWindow
        +com.android.server.wm.WindowManagerService#windowForClientLocked(com.android.server.wm.Session, IWindow, boolean)
        +com.android.server.wm.WindowState#prepareWindowToDisplayDuringRelayout
1. Viewpager转化动画

2. Camera 3D动画
3. AR沉浸式效果（ARCore）
#### 输入事件 - Input

```
+-----------------------------------------------------------------------------------------+
|                                                                                         |
|                                                                                         |
+------------------------------+           +----------------------------------------------+
|                              |           |                                              |
|  App process                 |           | SystemServer IMS         +-----------------+ |
|                              |           |                          |  InputReader    | |
|                              |           |                          |                 | |
|                              |           |                          |                 | |
|                              |           |                          | +------+        | |
|  +---------------------------+           +--------------------+     | |input |        | |
|  |  ViewRootImpl             |           | InputDispatcher    |     | |mapper|        | |
|  |  +------------------------+           +------------------+ |     | +------+        | |
|  |  |  InputEventReceiver    |           | InputPublisher   | |     |      +--------+ | |
|  |  |    +-------------------+           +---------------+  | |     |      |eventhub| | |
|  |  |    |   InputChannel    | <------>  | Inputchannel  |  | |     |      +--------+ | |
|  |  |    |                   |           |               |  | |     +-----------------+ |
+--+--+----+-------------------+-----------+---------------+--+-+-------------------------+


```

```java
android.view.ViewRootImpl.WindowInputEventReceiver extends InputEventReceiver{
    
}


public abstract class InputEventReceiver {
    private final CloseGuard mCloseGuard = CloseGuard.get();

    private long mReceiverPtr;

    // We keep references to the input channel and message queue objects here so that
    // they are not GC'd while the native peer of the receiver is using them.
    private InputChannel mInputChannel;
    private MessageQueue mMessageQueue;

    // Map from InputEvent sequence numbers to dispatcher sequence numbers.
    private final SparseIntArray mSeqMap = new SparseIntArray();

}
```


[事件](https://blog.csdn.net/shareus/article/details/50763237)
[Touch事件](http://gityuan.com/2016/12/10/input-manager/)
```java
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

### 数据渲染 - OpenGL ES 栅格化
```
             +-----------------------------------------------------------------------------+
             |                                                                             |
             |  App                                                                        |
             |                                                                             |
             +-----------------------------------+-----------------------------------------+
             |                                   |                                         |
             |   View/Graphic/Widget             |   OpenGL ES                             |
             |                                   |                                         |
             +-----------------------------------+                                         |
             |   Skia                            |                                         |
             |                                   |                                         |
             |     +-----------------------------+        +--------------------------------+
             |     |                             |        |                                |
             |     | libjpeg/libpng/libgif/libft2|        |  libagl/libhgl                 |
             |     |                             |        |                                |
             +-----+-----------------------------+--------+--------------------------------+
             |                                                                             |
             |   Surface                                                                   |
   Client    |                                                                             |
             +-----------------------------------------------------------------------------+
                                                     wms     SurfaceComposerClient
                                      +-----------------------+
+-------------------------------------+       Binder dri^er   +--------------------------------------------+
                                      +-----------------------+
   Server
                         +------------+     +------------+      +------------+
                         |   Surface  |     |  Surface   |      |   Surface  |
                         +-----+------+     +------+-----+      +-----+------+
                               |                   |                  |
                               |                   |                  |
             +-----------------+-------------------+------------------+--------------------+
             |                                                                             |
             |                                Surfaceflinger                               |
             |           main_surfaceflinger.cpp     SurfaceFlinger.cpp                    |
             +-----------------------------------------------------------------------------+
             |                                                  +--------------------------+
             |                               EGLDisplaySurface  |           | back buffer  |
             |                                                  |frontbuffer+--------------+
             |                                                  |           | back buffer  |
             +--------------------------------------------------+-----------+--------------+
             | HAL                        FramebufferNativeWindow                          |
             |                                                                             |
             +-----------------------------------------------------------------------------+
             |                                                                             |
             |                               Gralloc                                       |
             |                              gpu0 fb0                                       |
             +-----------------------------------------------------------------------------+

             +-----------------------------------------------------------------------------+
    kernel   |                       /dev/graphics/fb*                                     |
             |                                                                             |
             +-----------------------------------------------------------------------------+



```
Canvas是一个2D的概念，是在Skia中定义的
Skia 2D和OpenGL/ES 3D

OpengGL/ES两个基本Java类： GLSurfaceView,Renderer
[渲染流程线](https://blog.csdn.net/cpcpcp123/article/details/79942700?utm_source=blogxgwz8)
UI对象—->CPU处理为多维图形,纹理 —–通过OpeGL ES接口调用GPU—-> GPU对图进行光栅化(Frame Rate ) —->硬件时钟(Refresh Rate)—-垂直同步—->投射到屏幕
```
root      229   1     46892  4392     ep_poll b749cca5 S /system/bin/surfaceflinger
```
skia 图形引擎
[Canvas的底层是用 Skia 的库，cpu绘制](https://zhuanlan.zhihu.com/p/30453831)
[freetype 字体渲染](https://learnopengl-cn.readthedocs.io/zh/latest/06%20In%20Practice/02%20Text%20Rendering/)
[OpenGL 基本数据类型](https://segmentfault.com/a/1190000017246734)
[OpenGL ES 和 OpenGL ES 库的区别](https://woshijpf.github.io/android/2017/09/05/Android系统图形栈OpenGLES和EGL库的加载过程.html)
```java
public class GLSurfaceView extends SurfaceView implements SurfaceHolder.Callback2 {
    private final WeakReference<GLSurfaceView> mThisWeakRef = new WeakReference<GLSurfaceView>(this);
    private GLThread mGLThread;
    private Renderer mRenderer;
    private boolean mDetached;
    private EGLConfigChooser mEGLConfigChooser;
    private EGLContextFactory mEGLContextFactory;
    private EGLWindowSurfaceFactory mEGLWindowSurfaceFactory;
    private GLWrapper mGLWrapper;
    private int mDebugFlags;
    private int mEGLContextClientVersion;
    private boolean mPreserveEGLContextOnPause;
}

public class TextureView extends View {
private TextureLayer mLayer;
    private SurfaceTexture mSurface;
    private SurfaceTextureListener mListener;
    private boolean mHadSurface;

    private boolean mOpaque = true;

    private final Matrix mMatrix = new Matrix();
    private boolean mMatrixChanged;

    private final Object[] mLock = new Object[0];
    private boolean mUpdateLayer;
    private boolean mUpdateSurface;

    private Canvas mCanvas;
    private int mSaveCount;

    private final Object[] mNativeWindowLock = new Object[0];
    // Set by native code, do not write!
    private long mNativeWindow;
    private final SurfaceTexture.OnFrameAvailableListener mUpdateListener =
            new SurfaceTexture.OnFrameAvailableListener();
}

public class GLSurfaceView extends SurfaceView implements SurfaceHolder.Callback2 {
    private final WeakReference<GLSurfaceView> mThisWeakRef =
            new WeakReference<GLSurfaceView>(this);
    private GLThread mGLThread;
    private Renderer mRenderer;
    private boolean mDetached;
    private EGLConfigChooser mEGLConfigChooser;
    private EGLContextFactory mEGLContextFactory;
    private EGLWindowSurfaceFactory mEGLWindowSurfaceFactory;
    private GLWrapper mGLWrapper;
    private int mDebugFlags;
    private int mEGLContextClientVersion;
    private boolean mPreserveEGLContextOnPause;

}
```
### Media Framework

```
+------------------------------------------------------+
|                                                      |
|        MediaPlayer.java                              |
|                                                      |
+------------------------------------------------------+
|                                                      |
|        android_media_player.cpp  (libmedia_jni.so)   |
|                                                      |
+------------------------------------------------------+
|                                                      |
|        MediaPlayer.cpp           (libmedia.so)       |
|                                                      |
+------------------------------------------------------+
|                                                      |
|        HTTP / RTSP / HTTPLive                        |
|                                                      |
+------------------------------------------------------+
|                          +---------------------------+
|        Stagefright       |       NuPlayer            |
|                          |                           |
|                          |                           |
+--------------------------+---------------------------+

```

```java
public class MediaPlayer extends PlayerBase
                         implements SubtitleController.Listener
                                  , VolumeAutomation
                                  , AudioRouting
{

    private long mNativeContext; // accessed by native methods
    private long mNativeSurfaceTexture;  // accessed by native methods
    private int mListenerContext; // accessed by native methods
    private SurfaceHolder mSurfaceHolder;
    private EventHandler mEventHandler;
    private PowerManager.WakeLock mWakeLock = null;
    private boolean mScreenOnWhilePlaying;
    private boolean mStayAwake;
    private int mStreamType = AudioManager.USE_DEFAULT_STREAM_TYPE;
    private int mUsage = -1;
    private boolean mBypassInterruptionPolicy;

    // Modular DRM
    private UUID mDrmUUID;
    private final Object mDrmLock = new Object();
    private DrmInfo mDrmInfo;
    private MediaDrm mDrmObj;
    private byte[] mDrmSessionId;
    private boolean mDrmInfoResolved;
    private boolean mActiveDrmScheme;
    private boolean mDrmConfigAllowed;
    private boolean mDrmProvisioningInProgress;
    private boolean mPrepareDrmInProgress;
    private ProvisioningThread mDrmProvisioningThread;

    private AudioDeviceInfo mPreferredDevice = null;
    private ArrayMap<AudioRouting.OnRoutingChangedListener,
            NativeRoutingEventHandlerDelegate> mRoutingChangeListeners = new ArrayMap<>();
    // We would like domain specific classes with more informative names than the `first` and `second`
    // in generic Pair, but we would also like to avoid creating new/trivial classes. As a compromise
    // we document the meanings of `first` and `second` here:
    //
    // Pair.first - inband track index; non-null iff representing an inband track.
    // Pair.second - a SubtitleTrack registered with mSubtitleController; non-null iff representing
    //               an inband subtitle track or any out-of-band track (subtitle or timedtext).
    private Vector<Pair<Integer, SubtitleTrack>> mIndexTrackPairs = new Vector<>();
    private BitSet mInbandTrackIndices = new BitSet();

    private SubtitleController mSubtitleController;
    private int mSelectedSubtitleTrackIndex = -1;
    private Vector<InputStream> mOpenSubtitleSources;

    private final OnSubtitleDataListener mIntSubtitleDataListener = new OnSubtitleDataListener() ;
 
    private TimeProvider mTimeProvider;
    private OnPreparedListener mOnPreparedListener;
    private OnCompletionListener mOnCompletionListener;
   private final OnCompletionListener mOnCompletionInternalListener = new OnCompletionListener();

       private OnBufferingUpdateListener mOnBufferingUpdateListener;
    private OnSeekCompleteListener mOnSeekCompleteListener;
    private OnVideoSizeChangedListener mOnVideoSizeChangedListener;
    private OnTimedTextListener mOnTimedTextListener;
    private boolean mSubtitleDataListenerDisabled;
    /** External OnSubtitleDataListener, the one set by {@link #setOnSubtitleDataListener}. */
    private OnSubtitleDataListener mExtSubtitleDataListener;
    private Handler mExtSubtitleDataHandler;

    private OnMediaTimeDiscontinuityListener mOnMediaTimeDiscontinuityListener;
    private Handler mOnMediaTimeDiscontinuityHandler;
        private OnTimedMetaDataAvailableListener mOnTimedMetaDataAvailableListener;
    private OnErrorListener mOnErrorListener;


}
```


## dalvik
[支持的垃圾回收机制](https://www.jianshu.com/p/153c01411352)
Mark-sweep算法：还分为Sticky, Partial, Full，根据是否并行，又分为ConCurrent和Non-Concurrent
MarkSweep::MarkSweep(Heap* heap, bool is_concurrent, const std::string& name_prefix)
mark_compact 算法：标记-压缩（整理)算法
concurrent_copying算法：
semi_space算法: 
[android hash](https://blog.csdn.net/xusiwei1236/article/details/45152201)

- **Object**
public class Object {

    private transient Class<?> shadow$_klass_;
    private transient int shadow$_monitor_;
}


**ArrayMap**
相比HashMap使用了两个小的容量的数组
```java
public final class ArrayMap<K, V> implements Map<K, V> {
    final boolean mIdentityHashCode;// 1byte，default false
    int[] mHashes;//4 byte，存储key的hash值
    Object[] mArray;//4byte,size 为mHashes的两倍，存储key（index=hashIndex<<1），value
    int mSize;//4 byte
    MapCollections<K, V> mCollections;//4byte

}

                        +------+            +------+
                        | Key  +----------> | Key1 |
                        | Hash |            |      |
                        +------+            |Value1|
                        |      |            +------+
                        |      |            |      |
                        |      |            |      |
+---------+             |      |            |      |
| Key     |  Binary     |      |            |      |
|         | +------->   |      |            |      |
+---------+  Search     |      |            |      |
                        |      |            |      |
                        |      |            |      |
                        |      |            |      |
                        |      |            |      |
                        |      |            |      |
                        |      |            |      |
                        |      |            |      |
                        |      |            |      |
                        +------+            +------+



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
- **SparseArray**
[SparseArray与HashMap<Integer,Object>对比](https://stackoverflow.com/questions/25560629/sparsearray-vs-hashmap)
[SparseArray与ArrayMap对比，解决基本数据类型自动封箱问题](https://android.jlelse.eu/app-optimization-with-arraymap-sparsearray-in-android-c0b7de22541a)
  稀疏数组问题就是数组中的大部分的内容值都未被使用或者都为0，在数组中仅有少部分的空间使用。
```java
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

### 数据存储
#### 文件存储,SharedPreferences,SQLite数据库方式,内容提供器（Content provider）,网络
```java
final class SharedPreferencesImpl implements SharedPreferences {
    private final File mFile;
    private final File mBackupFile;
    private final int mMode;
    private final Object mLock = new Object();
    private final Object mWritingToDiskLock = new Object();

    @GuardedBy("mLock")
    private Map<String, Object> mMap;
    @GuardedBy("mLock")
    private Throwable mThrowable;

    @GuardedBy("mLock")
    private int mDiskWritesInFlight = 0;

    @GuardedBy("mLock")
    private boolean mLoaded = false;

    @GuardedBy("mLock")
    private StructTimespec mStatTimestamp;

    @GuardedBy("mLock")
    private long mStatSize;

    @GuardedBy("mLock")
    private final WeakHashMap<OnSharedPreferenceChangeListener, Object> mListeners =
            new WeakHashMap<OnSharedPreferenceChangeListener, Object>();

    /** Current memory state (always increasing) */
    @GuardedBy("this")
    private long mCurrentMemoryStateGeneration;

    /** Latest memory state that was committed to disk */
    @GuardedBy("mWritingToDiskLock")
    private long mDiskStateGeneration;

    /** Time (and number of instances) of file-system sync requests */
    @GuardedBy("mWritingToDiskLock")
    private final ExponentiallyBucketedHistogram mSyncTimes = new ExponentiallyBucketedHistogram(16);
    private int mNumSync = 0;
}
```
#### RPC - Protocol Buffer
#### OKIO
Source（io InputStream）,Sink（io OutputStream）,Buffer（io BufferedInputStream,BufferedOutputStream）
```java
public class ByteString implements Serializable, Comparable<ByteString> {

  final byte[] data;
  transient int hashCode; // Lazily computed; 0 if unknown.
  transient String utf8; // Lazily computed.

}

public final class Buffer implements BufferedSource, BufferedSink, Cloneable, ByteChannel {
  @Nullable Segment head;
  long size;

}
```

## FrameWork Layer

Zygote进程启动后，加载ZygoteInit类，注册Zygote Socket服务端套接字；加载虚拟机；加载类，加载系统资源
System Server进程，是由Zygote进程fork而来，System Server是Zygote孵化的第一个进程，System Server负责启动和管理整个Java framework，包含ActivityManager，PowerManager等服务
Media Server进程，是由init进程fork而来，负责启动和管理整个C++ framework，包含AudioFlinger，Camera Service等服务

相关系统服务 PMS（安装），AMS（启动），WMS（输出-显示），IMS（输入-事件），PowerMS,JSS,DMS,DisplayManagerService、BatteryService

### SystemServer - InputManagerService
 [事件](http://gityuan.com/2016/12/31/input-ipc/)
 [事件子系统](https://blog.csdn.net/jscese/article/details/42099381)
- InputReader线程：通过EventHub从/dev/input节点获取事件，转换成EventEntry事件加入到InputDispatcher的mInboundQueue。EventHub采用INotify + epoll机制

- InputDispatcher线程：从mInboundQueue队列取出事件，转换成DispatchEntry事件加入到connection的outboundQueue队列。再然后开始处理分发事件，取出outbound队列，放入waitQueue.InputChannel.sendMessage通过socket方式将消息发送给远程进程；

- UI线程：创建socket pair，分别位于”InputDispatcher”线程和focused窗口所在进程的UI主线程，可相互通信。 
UI主线程：通过setFdEvents()， 监听socket客户端，收到消息后回调NativeInputEventReceiver();【见小节2.1】
“InputDispatcher”线程： 通过IMS.registerInputChannel()，监听socket服务端，收到消息后回调handleReceiveCallback；【见小节3.1】
```

ViewRootImpl的setView()过程:
    创建socket pair，作为InputChannel: 
        socket服务端保存到system_server中的WindowState的mInputChannel；
        socket客户端通过binder传回到远程进程的UI主线程ViewRootImpl的mInputChannel；
    IMS.registerInputChannel()注册InputChannel，监听socket服务端： 
        Loop便是“InputDispatcher”线程的Looper;
        回调方法handleReceiveCallback。

```




ANR 事件 resetANRTimeoutsLocked


```C
struct RawEvent {
    nsecs_t when;
    int32_t deviceId;
    int32_t type;
    int32_t code;
    int32_t value;
};


``` 

```java
public class ActivityStackSupervisor extends ConfigurationContainer implements DisplayListener,
        RecentTasks.Callbacks {
    private final SparseArray<ActivityDisplay> mActivityDisplays = new SparseArray<>();
        
}

```
### SystemServer - PackageManagerService
### SystemServer - LocationManagerService
```java
public class LocationManagerService extends ILocationManager.Stub { 
}
```
### SystemServer - NotificationManagerService
```java
public class NotificationManager {
    private Context mContext;

}
```


   
## 应用层


### 应用进程创建过程/应用安装过程



### Context
```java
public abstract class Context {
}

public class ContextWrapper extends Context {
    Context mBase;//ContextImpl
}
class ContextImpl extends Context {
        /**
     * Map from preference name to generated path.
     */
    @GuardedBy("ContextImpl.class")
    private ArrayMap<String, File> mSharedPrefsPaths;
    

    final @NonNull ActivityThread mMainThread;
    final @NonNull LoadedApk mPackageInfo;
    private @Nullable ClassLoader mClassLoader;

    private final @Nullable IBinder mActivityToken;

    private final @NonNull UserHandle mUser;

    private final ApplicationContentResolver mContentResolver;

    private final String mBasePackageName;
    private final String mOpPackageName;

    private final @NonNull ResourcesManager mResourcesManager;
    private @NonNull Resources mResources;
    private @Nullable Display mDisplay; // may be null if default display

    private final int mFlags;

    private Context mOuterContext;
    private int mThemeResource = 0;
    private Resources.Theme mTheme = null;
    private PackageManager mPackageManager;
    private Context mReceiverRestrictedContext = null;

    // The name of the split this Context is representing. May be null.
    private @Nullable String mSplitName = null;

    private AutofillClient mAutofillClient = null;
    private boolean mIsAutofillCompatEnabled;

    private final Object mSync = new Object();

    @GuardedBy("mSync")
    private File mDatabasesDir;
    @GuardedBy("mSync")
    private File mPreferencesDir;
    @GuardedBy("mSync")
    private File mFilesDir;
    @GuardedBy("mSync")
    private File mNoBackupFilesDir;
    @GuardedBy("mSync")
    private File mCacheDir;
    @GuardedBy("mSync")
    private File mCodeCacheDir;

    // The system service cache for the system services that are cached per-ContextImpl.
    final Object[] mServiceCache = SystemServiceRegistry.createServiceCache();
     /**
     * Initialization state for each service. Any of {@link #STATE_UNINITIALIZED},
     * {@link #STATE_INITIALIZING} or {@link #STATE_READY},
     */
    @ServiceInitializationState
    final int[] mServiceInitializationStateArray = new int[mServiceCache.length];   
}
public class Application extends ContextWrapper implements ComponentCallbacks2 {
    private ArrayList<ComponentCallbacks> mComponentCallbacks = new ArrayList<ComponentCallbacks>();
    private ArrayList<ActivityLifecycleCallbacks> mActivityLifecycleCallbacks = new ArrayList<ActivityLifecycleCallbacks>();
    private ArrayList<OnProvideAssistDataListener> mAssistCallbacks = null;//在任何App中唤起该助手

    /** @hide */
    public LoadedApk mLoadedApk;
}
public final class LoadedApk {
    private final ActivityThread mActivityThread;
    final String mPackageName;
    private ApplicationInfo mApplicationInfo;
    private String mAppDir;
    private String mResDir;
    private String[] mOverlayDirs;
    private String mDataDir;
    private String mLibDir;
    private File mDataDirFile;
    private File mDeviceProtectedDataDirFile;
    private File mCredentialProtectedDataDirFile;
    private final ClassLoader mBaseClassLoader;
    private final boolean mSecurityViolation;
    private final boolean mIncludeCode;
    private final boolean mRegisterPackage;
    private final DisplayAdjustments mDisplayAdjustments = new DisplayAdjustments();
    /** WARNING: This may change. Don't hold external references to it. */
    Resources mResources;
    private ClassLoader mClassLoader;
    private Application mApplication;

    private String[] mSplitNames;
    private String[] mSplitAppDirs;
    private String[] mSplitResDirs;
    private String[] mSplitClassLoaderNames;

    private final ArrayMap<Context, ArrayMap<BroadcastReceiver, ReceiverDispatcher>> mReceivers
        = new ArrayMap<>();
    private final ArrayMap<Context, ArrayMap<BroadcastReceiver, LoadedApk.ReceiverDispatcher>> mUnregisteredReceivers
        = new ArrayMap<>();
    private final ArrayMap<Context, ArrayMap<ServiceConnection, LoadedApk.ServiceDispatcher>> mServices
        = new ArrayMap<>();
    private final ArrayMap<Context, ArrayMap<ServiceConnection, LoadedApk.ServiceDispatcher>> mUnboundServices
        = new ArrayMap<>();
    private AppComponentFactory mAppComponentFactory;
    private SplitDependencyLoaderImpl mSplitLoader;

}
public abstract class Service extends ContextWrapper implements ComponentCallbacks2 {
    // set by the thread after the constructor and before onCreate(Bundle icicle) is called.
    private ActivityThread mThread = null;
    private String mClassName = null;
    private IBinder mToken = null;
    private Application mApplication = null;
    private IActivityManager mActivityManager = null;
    private boolean mStartCompatibility = false;
}
public class ContextThemeWrapper extends ContextWrapper {
    private int mThemeResource;
    private Resources.Theme mTheme;
    private LayoutInflater mInflater;
    private Configuration mOverrideConfiguration;
    private Resources mResources;
}
public class Activity extends ContextThemeWrapper
        implements LayoutInflater.Factory2,
        Window.Callback, KeyEvent.Callback,
        OnCreateContextMenuListener, ComponentCallbacks2,
        Window.OnWindowDismissedCallback, WindowControllerCallback,
        AutofillManager.AutofillClient {
    private SparseArray<ManagedDialog> mManagedDialogs;

    // set by the thread after the constructor and before onCreate(Bundle savedInstanceState) is called.
    private Instrumentation mInstrumentation;
    private IBinder mToken;
    private int mIdent;
    /*package*/ String mEmbeddedID;
    private Application mApplication;
    /*package*/ Intent mIntent;
    /*package*/ String mReferrer;
    private ComponentName mComponent;
    /*package*/ ActivityInfo mActivityInfo;
    /*package*/ ActivityThread mMainThread;
    Activity mParent;
    boolean mCalled;
    /*package*/ boolean mResumed;
    /*package*/ boolean mStopped;
    boolean mFinished;
    boolean mStartedActivity;
    private boolean mDestroyed;
    private boolean mDoReportFullyDrawn = true;
    private boolean mRestoredFromBundle;

    /** {@code true} if the activity lifecycle is in a state which supports picture-in-picture.
     * This only affects the client-side exception, the actual state check still happens in AMS. */
    private boolean mCanEnterPictureInPicture = false;
    /** true if the activity is going through a transient pause */
    /*package*/ boolean mTemporaryPause = false;
    /** true if the activity is being destroyed in order to recreate it with a new configuration */
    /*package*/ boolean mChangingConfigurations = false;
    /*package*/ int mConfigChangeFlags;
    /*package*/ Configuration mCurrentConfig;
    private SearchManager mSearchManager;
    private MenuInflater mMenuInflater;

    /** The autofill manager. Always access via {@link #getAutofillManager()}. */
    @Nullable private AutofillManager mAutofillManager;

    /* package */ NonConfigurationInstances mLastNonConfigurationInstances;

    private Window mWindow;

    private WindowManager mWindowManager;
    /*package*/ View mDecor = null;
    /*package*/ boolean mWindowAdded = false;
    /*package*/ boolean mVisibleFromServer = false;
    /*package*/ boolean mVisibleFromClient = true;
    /*package*/ ActionBar mActionBar = null;
    private boolean mEnableDefaultActionBarUp;

    private VoiceInteractor mVoiceInteractor;

    private CharSequence mTitle;
    private int mTitleColor = 0;

    // we must have a handler before the FragmentController is constructed
    final Handler mHandler = new Handler();
    final FragmentController mFragments = FragmentController.createController(new HostCallbacks());

    @GuardedBy("mManagedCursors")
    private final ArrayList<ManagedCursor> mManagedCursors = new ArrayList<>();

    @GuardedBy("this")
    int mResultCode = RESULT_CANCELED;
    @GuardedBy("this")
    Intent mResultData = null;

    private TranslucentConversionListener mTranslucentCallback;
    private boolean mChangeCanvasToTranslucent;

    private SearchEvent mSearchEvent;

    private boolean mTitleReady = false;
    private int mActionModeTypeStarting = ActionMode.TYPE_PRIMARY;

    private int mDefaultKeyMode = DEFAULT_KEYS_DISABLE;
    private SpannableStringBuilder mDefaultKeySsb = null;

    private ActivityManager.TaskDescription mTaskDescription =
            new ActivityManager.TaskDescription();

    @SuppressWarnings("unused")
    private final Object mInstanceTracker = StrictMode.trackActivity(this);

    private Thread mUiThread;

    ActivityTransitionState mActivityTransitionState = new ActivityTransitionState();
    SharedElementCallback mEnterTransitionListener = SharedElementCallback.NULL_CALLBACK;
    SharedElementCallback mExitTransitionListener = SharedElementCallback.NULL_CALLBACK;

    private boolean mHasCurrentPermissionsRequest;

    private boolean mAutoFillResetNeeded;
    private boolean mAutoFillIgnoreFirstResumePause;

    /** The last autofill id that was returned from {@link #getNextAutofillId()} */
    private int mLastAutofillId = View.LAST_APP_AUTOFILL_ID;

    private AutofillPopupWindow mAutofillPopupWindow;
}

```

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
### 四大组件-Activity
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
- 启动模式
Activity的启动模式必不可少的要是launchMode、Flags、taskAffinity
```
adb shell dumpsys activity---------------查看ActvityManagerService 所有信息
adb shell dumpsys activity activities----------查看Activity组件信息
adb shell dumpsys activity services-----------查看Service组件信息
adb shell dumpsys activity providers----------产看ContentProvider组件信息
adb shell dumpsys activity broadcasts--------查看BraodcastReceiver信息
adb shell dumpsys activity intents--------------查看Intent信息
adb shell dumpsys activity processes---------查看进程信息


adb shell dumpsys activity activities | sed -En -e '/Running activities/,/Run #0/p'
```
```ActivityStarter的启动模式代码阅读
import static android.content.pm.ActivityInfo.LAUNCH_SINGLE_INSTANCE;
import static android.content.pm.ActivityInfo.LAUNCH_SINGLE_TASK;
import static android.content.pm.ActivityInfo.LAUNCH_SINGLE_TOP; 
```

[Activity栈及任务记录结构](https://www.imooc.com/article/31922?block_id=tuijian_wz)
```
ActivityDisplay#0（一般只有一显示器） ActivityDisplay#1
+------------------------------+   +----------------------------------+
|       ActivityStack          |   |   ActivityStack                  |
|    +---------------------+   |   |                                  |
|    |  TaskRecord         |   |   |                                  |
|    +---------------------+   |   |                                  |
|    |                     |   |   |                                  |
|    | +----------------+  |   |   |                                  |
|    | |ActivityRecord  |  |   |   |                                  |
|    | +----------------+  |   |   |                                  |
|    | +----------------+  |   |   |                                  |
|    | |ActivityRecord  |  |   |   |                                  |
|    | +----------------+  |   |   |                                  |
|    | +----------------+  |   |   |                                  |
|    | |ActivityRecord  |  |   |   |                                  |
|    | +----------------+  |   |   |                                  |
|    | +----------------+  |   |   |                                  |
|    | |ActivityRecord  |  |   |   |                                  |
|    | +----------------+  |   |   |                                  |
|    |                     |   |   |                                  |
|    +---------------------+   |   |                                  |
|                              |   |                                  |
|    +---------------------+   |   |                                  |
|    |  TaskRecord         |   |   |                                  |
|    +---------------------+   |   |                                  |
|    | +----------------+  |   |   |                                  |
|    | |ActivityRecord  |  |   |   |                                  |
|    | +----------------+  |   |   |                                  |
|    | +----------------+  |   |   |                                  |
|    | |ActivityRecord  |  |   |   |                                  |
|    | +----------------+  |   |   |                                  |
|    | +----------------+  |   |   |                                  |
|    | |ActivityRecord  |  |   |   |                                  |
|    | +----------------+  |   |   |                                  |
|    |                     |   |   |                                  |
|    |                     |   |   |                                  |
|    +---------------------+   |   |                                  |
+------------------------------+   +----------------------------------+

```


[四大组件的管理](http://gityuan.com/2017/05/19/ams-abstract/)
[Activity启动模式](gityuan.com/2017/06/11/activity_record/)
android:launchMode：
    SingleTop：栈顶复用，如果处于栈顶，则生命周期不走onCreate()和onStart()，会调用onNewIntent()，适合推送消息详情页
    SingleTask：栈内复用，如果存在栈内，则在其上所有Activity全部出栈，使得其位于栈顶，生命周期和SingleTop一样，app首页基本是用这个。
android:taskAffinity 属性主要和 singleTask 或者 allowTaskReparenting 属性配对使用，在其他情况下没有意义。
android:noHistory： “true”值意味着Activity不会留下历史痕迹。比如启用界面的就可以借用这个。


### 其他组件 View（ 测量，布局及绘制,事件，动画），controls,layouts

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
#### 布局- CoordinatorLayout

```java
public class CoordinatorLayout extends ViewGroup implements NestedScrollingParent2 {

    private final List<View> mDependencySortedChildren = new ArrayList<>();
    private final DirectedAcyclicGraph<View> mChildDag = new DirectedAcyclicGraph<>();

    private final List<View> mTempList1 = new ArrayList<>();
    private final List<View> mTempDependenciesList = new ArrayList<>();
    private final int[] mTempIntPair = new int[2];
    private Paint mScrimPaint;

    private boolean mDisallowInterceptReset;

    private boolean mIsAttachedToWindow;

    private int[] mKeylines;

    private View mBehaviorTouchView;
    private View mNestedScrollingTarget;

    private OnPreDrawListener mOnPreDrawListener;
    private boolean mNeedsPreDrawListener;

    private WindowInsetsCompat mLastInsets;
    private boolean mDrawStatusBarBackground;
    private Drawable mStatusBarBackground;

    OnHierarchyChangeListener mOnHierarchyChangeListener;
    private android.support.v4.view.OnApplyWindowInsetsListener mApplyWindowInsetsListener;

    private final NestedScrollingParentHelper mNestedScrollingParentHelper =
            new NestedScrollingParentHelper(this);
}
```




Activity->PhoneWindow
WindowManagerGlobal


- Handler 消息机制

```java
public class Handler {
    final Looper mLooper;
    final MessageQueue mQueue;
    final Callback mCallback;
    final boolean mAsynchronous;
    IMessenger mMessenger;
}
public final class Messenger implements Parcelable {
    private final IMessenger mTarget;
}
oneway interface IMessenger {
    void send(in Message msg);
}
public final class MessageQueue {

    // True if the message queue can be quit.
    private final boolean mQuitAllowed;

    @SuppressWarnings("unused")
    private long mPtr; // used by native code

    Message mMessages;
    private final ArrayList<IdleHandler> mIdleHandlers = new ArrayList<IdleHandler>();
    private SparseArray<FileDescriptorRecord> mFileDescriptorRecords;
    private IdleHandler[] mPendingIdleHandlers;
    private boolean mQuitting;

    // Indicates whether next() is blocked waiting in pollOnce() with a non-zero timeout.
    private boolean mBlocked;

    // The next barrier token.
    // Barriers are indicated by messages with a null target whose arg1 field carries the token.
    private int mNextBarrierToken;
}

public final class Message implements Parcelable {
 /**
     * User-defined message code so that the recipient can identify
     * what this message is about. Each {@link Handler} has its own name-space
     * for message codes, so you do not need to worry about yours conflicting
     * with other handlers.
     */
    public int what;

    /**
     * arg1 and arg2 are lower-cost alternatives to using
     * {@link #setData(Bundle) setData()} if you only need to store a
     * few integer values.
     */
    public int arg1;

    /**
     * arg1 and arg2 are lower-cost alternatives to using
     * {@link #setData(Bundle) setData()} if you only need to store a
     * few integer values.
     */
    public int arg2;

    /**
     * An arbitrary object to send to the recipient.  When using
     * {@link Messenger} to send the message across processes this can only
     * be non-null if it contains a Parcelable of a framework class (not one
     * implemented by the application).   For other data transfer use
     * {@link #setData}.
     *
     * <p>Note that Parcelable objects here are not supported prior to
     * the {@link android.os.Build.VERSION_CODES#FROYO} release.
     */
    public Object obj;

    /**
     * Optional Messenger where replies to this message can be sent.  The
     * semantics of exactly how this is used are up to the sender and
     * receiver.
     */
    public Messenger replyTo;

    /**
     * Optional field indicating the uid that sent the message.  This is
     * only valid for messages posted by a {@link Messenger}; otherwise,
     * it will be -1.
     */
    public int sendingUid = -1;
    /*package*/ int flags;

    /*package*/ long when;

    /*package*/ Bundle data;

    /*package*/ Handler target;

    /*package*/ Runnable callback;

    // sometimes we store linked lists of these things
    /*package*/ Message next;
}
```
- AsyncTask
```java

public abstract class AsyncTask<Params, Progress, Result> {

    private final WorkerRunnable<Params, Result> mWorker;
    private final FutureTask<Result> mFuture;

    private volatile Status mStatus = Status.PENDING;
    
    private final AtomicBoolean mCancelled = new AtomicBoolean();
    private final AtomicBoolean mTaskInvoked = new AtomicBoolean();

    private final Handler mHandler;

}
```        
    容器类：ArrayDeque，LinkedBlockingQueue（ThreadPoolExecutor的线程队列）
    并发类：ThreadPoolExecutor（包含 ThreadFactory属性，用于创建线程），AtomicBoolean，AtomicInteger，FutureTask(包含Callable属性，任务执行的时候调用Callable#call,执行AsyncTask#dobackgroud)

### 编译，打包，优化，签名，安装
gradle,Transform的应用
批量打包
```打包流程
G: gradle build tools
B: android build tools
J: JDK tools


+--------------------------------------------------------------------------------------+
|G                                                                                     |
|    multiple agent tool                                                               |
|                                                                                      |
|                                                                                      |
+---------------------------------------------------------------------------------------+
|B                                                                                     |
|   zipalign                                                                           |
|                                                                                      |
+--------------------------------------------------------------------------------------+
|J                                                                                     |
|   javasigner                                                                         |
|                                                                                      |
|                                                                                      |
+--------------------------------------------------------------------------------------+
|G                                                                                     |
|   ApkBuilder                                                                         |
|                                                                                      |
+--------------------+                          +--------------------------------------+
|B                   |                          |B                                     |
|  linker            |                          |    dex                               |
|                    |                          |                                      |
+--------------------------------------------------------------------------------------+
|B                   |B                         |G             +-----------------------+
|  bcc compat        |   AAPT                   |    proguard  |          Preveirfy    |
|                    |                          |              |          Obfuscate    |
|                    |                          |              |          Optimize     |
|                    |                          |              |          Shrink       |
|                    |                          |              +-----------------------+
|                    |                          |                                      |
+--------------------+                          +--------------------------------------+
|B                   |                          |        J                             |
|  llvm-rs-cc        |                          +-------+    javac                     |
|                    |                          | R.java|                              |
|                    +--------------------------+--------------------------------------+
|                    |G                                 |B                             |
|                    |   menifest/assets/resource merger|    aidl                      |
+--------------------+-----------------------------------------------------------------+

                                                                                              

```

- HandlerThread
- IntentService
- LruCache
- 窗口（Window，Activity，DecorView以及ViewRoot）
- View 测量，布局及绘制

- 进程通信
AIDL
文件
- Bitmap



- 图形及用户界面
1. 界面及事件
2. openGl
   
- Context

- 持久化和序列化（Parcelable，Serializable）



## 函数式编程
Glide



### 3.2 Android 开发模式

#### 性能优化总结
[RelativeLayout的性能损耗](https://zhuanlan.zhihu.com/p/52386900?utm_source=androidweekly.io&utm_medium=website)
>《Android开发艺术探索》
方法：布局，绘制，内存泄漏，响应速度，Listview及Bitmap，线程优化
- 渲染速度
    1. 布局优化（layoutInspector include merge, viewstub）
    分析工具，不必要不加载（include merge, viewstub），ConstaintLayout，Lint
    1. 绘制优化(profiler)
    尽量用Drawable
    1. 响应速度优化(profiler)
    2. ListView/RecycleView及Bitmap优化
    3. 线程优化
- 内存优化
  [微信文章](https://mp.weixin.qq.com/s/KtGfi5th-4YHOZsEmTOsjg?utm_source=androidweekly.io&utm_medium=website)
- 电量消耗
    3.内存泄漏优化
        3.1 单例
        3.2 非静态内部类
        3.3 资源未关闭（webview没执行 destroy）
        3.4 ListView 未缓存
        3.5 集合类未销毁

- 其他性能优化的建议
  [包大小](https://mp.weixin.qq.com/s/_gnT2kjqpfMFs0kqAg4Qig?utm_source=androidweekly.io&utm_medium=website)
工具：profiler，eclipse mat
可维护性：组件化
#### 内存泄漏
 
#### 架构之模块化（插件化及组件化）
插件化
- Dynamic-loader-apk
- Replugin

组件化
- 组件间解耦
  1. MVVM-AAC 
  Android Jetpack(Foundation Architecture Behavior UI  ) ViewModel LiveData
  2. MVP DI框架Dagger2解耦
- 通信
1. 对象持有
2. 接口持有
3. 路由 （ARouter）
   Dagger2 依赖注入控制反转，Dagger 2 是 Java 和 Android 下的一个完全静态、编译时生成代码的依赖注入框架

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