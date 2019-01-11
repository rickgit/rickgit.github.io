## Android应用体系
> 软件是计算机系统中与硬件相互依存的另一部分，它是包括程序，数据及其相关文档的完整集合 《软件工程概论》
环境 Android 系统
阅读应用流程（以APK文档为主线，程序Main方法为入口）
[启动流程](https://blog.csdn.net/qq_30993595/article/details/82714409#_2)
```diagram
+-----------------------------------------------------------------------+
|                                AppS                                   |
|                                                                       |
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
|  Surface Mgr     Media Framework    Sqlite      |  | core Libraries | |
|                                                 |  | dalvik vm      | |
|  OpenGL+ES (3d)  FreeType           Webkit      |  +----------------+ |
|                                                 +---------------------+
|  SGL(Skia 2d)    SSL/TLS            libc(bionic)                      |
|                                                                       |
+-----------------------------------------------------------------------+
|                      Linux kernel                                     |
|                                                                       |
|  Display Driver   Camera Driver   Flash Driver   Bind (IPC) Driver    |
|                      (V4L2)                                           |
|                                                                       |
|  KeyPad Driver    WIFI Driver     Audio Driver   Power Management      |
|                                                                       |
|  Bluetooth Driver   USB Driver                                        |
|                                                                       |
+-----------------------------------------------------------------------+
|                                                                       |
|                                        ^                              |
|  Loader +--->Boot ROM+--->Boot Loader+-+                              |
|                                                                       |
+-----------------------------------------------------------------------+


```

## Linux kernel -ipc
启动Kernel的swapper进程(pid=0)：该进程又称为idle进程, 系统初始化过程Kernel由无到有开创的第一个进程, 用于初始化进程管理、内存管理，加载Display,Camera Driver，Binder Driver等相关工作
启动kthreadd进程（pid=2）：是Linux系统的内核进程，会创建内核工作线程kworkder，软中断线程ksoftirqd，thermal等内核守护进程。kthreadd进程是所有内核进程的鼻祖
--------------------- 
[作者：硬刚平底锅 ] (https://blog.csdn.net/qq_30993595/article/details/82714409 )
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



```

[Android Init进程源码分析](https://blog.csdn.net/yangwen123/article/details/9029959)
```
   +---------------------+
   |  start_kernel(void) |
   +---------+-----------+
             v

   +---------------------+
   |  reset_init(void)   +--------------+-----------------------------+
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
| +--------------+ |       |                     |      |                           |
|                  |       |                     |      |                           |
|                  |       |                     |      |                           |
|                  |       |                     |      |                           |
+------------------+       +---------------------+      +---------------------------+

  Java Process                 Native Process                Kernel Driver Thread


```

《Linux设备驱动程序》
《Android 开发艺术探索》
基础知识：序列化和Binder
Binder是misc设备上进行注册,作为虚拟字符设备。Binder transaction buffer，这块内存有一个大小限制，目前是1MB
```
 > ls -l /dev/
crw-rw-rw- root     root      10,  54 2018-12-03 20:23 binder //c代表字符设备文件
drwxr-xr-x root     root              2018-12-13 15:15 input
drwxr-xr-x root     root              2018-12-13 15:16 socket

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
Serializable->Parcelable->Binder->{AIDL,Messenger}

AIDL 文件，方向指示符包括in、out、和inout；AIDL 进程间通信,作用就是不同UID的 APP应用(也就是不同进程)可以实现通过 ADIL 生成的接口类,来调用对方APP的方法。
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
```
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
- 文件共享
- AIDL
- Messenger(AIDL)
- contentProvider（Binder）
- socket（ 与Zygote通信）
```
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
## Native Layer
init进程会孵化出ueventd、logd、healthd、installd、adbd、lmkd等用户守护进程
init进程还启动servicemanager(binder服务管家)、bootanim(开机动画)等重要服务
init进程孵化出Zygote进程，Zygote进程是Android系统的第一个Java进程(即虚拟机进程)，Zygote是所有Java进程的父进程
--------------------- 
[作者：硬刚平底锅  ](https://blog.csdn.net/qq_30993595/article/details/82714409)

c++的智能指针有很多实现方式，有auto_ptr ,  unique_ptr , shared_ptr 三种， Android 中封装了sp<> 强指针，wp<>弱指针的操作
### OpenGL ES
skia 图形引擎
[Canvas的底层是用 Skia 的库，cpu绘制](https://zhuanlan.zhihu.com/p/30453831)
[freetype 字体渲染](https://learnopengl-cn.readthedocs.io/zh/latest/06%20In%20Practice/02%20Text%20Rendering/)
[OpenGL 基本数据类型](https://segmentfault.com/a/1190000017246734)
[OpenGL ES 和 OpenGL ES 库的区别](https://woshijpf.github.io/android/2017/09/05/Android系统图形栈OpenGLES和EGL库的加载过程.html)
```
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

```
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

### SurfaceFlinger - [Graphic图形系统](http://gityuan.com/2017/02/05/graphic_arch/)
Canvas是一个2D的概念，是在Skia中定义的
Skia 2D和OpenGL/ES 3D

OpengGL/ES两个基本Java类： GLSurfaceView,Renderer
[渲染流程线](https://blog.csdn.net/cpcpcp123/article/details/79942700?utm_source=blogxgwz8)
UI对象—->CPU处理为多维图形,纹理 —–通过OpeGL ES接口调用GPU—-> GPU对图进行光栅化(Frame Rate ) —->硬件时钟(Refresh Rate)—-垂直同步—->投射到屏幕
```
root      229   1     46892  4392     ep_poll b749cca5 S /system/bin/surfaceflinger
```
SystemServer的RenderThread线程
黄油计划：垂直同步(VSYNC 定时中断)、Triple Buffer和Choreographer（实现统一调度界面绘图。）
黄油计划的核心VSYNC信号分为两种，一种是硬件生成（HardwareComposer）的信号，一种是软件模拟（VSyncThread来模拟）的信号。
```
                            VSync                 VSync                VSync           //Display为基准，VSync将其划分成16ms长度的时间段
                               +                    +                    +
          +-------------------------------------------------------------------------+
          |                    |                    |                    |          |
 Display  |                    |                    |                    |          |
          +-------------------------------------------------------------------------+
                               |                    |                    |
                               |                    |                    |
                               |                    |                    |
          +-------+            +--------+           +------+             |
CPU       | Frame1|            | Frame2 |           |Frame3|（前一个CPU Frame占用中）|  //CPU/GPU的FPS不等同Display的FPS，需要三级缓存
          +-------+            +--------+           +------+（使用第三块缓存）       |
                               |                    |                    |
                   +-----------+-+      +-----------+----+ +-----------+ |
GPU                | Frame1      |      | Frame2         | | Frame3    | |
                   +-----------+-+      +-----------+----+ +-----------+ |
                               |                    |                    |
                               |                    |                    |
                               |                    |                    |
                               |                    |                    |
                               +                    +                    +

```


```
    class EventHandler {
        friend class HWComposer;
    };
```

```
base/core/java/android/view/ViewRootImpl.java:511:        mChoreographer = Choreographer.getInstance();

public final class Choreographer {
    private final Object mLock = new Object();

    private final Looper mLooper;
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


```
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
```
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

### 数据存储
#### SharedPreferences,文件存储,SQLite数据库方式,内容提供器（Content provider）,网络
```
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
```
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


```
struct RawEvent {
    nsecs_t when;
    int32_t deviceId;
    int32_t type;
    int32_t code;
    int32_t value;
};


```
### SystemServer - WindowsManagerService

启动 涉及“android.display”（DisplayThread）, “android.ui”线程（PolicyHandler）
```
base/services/java/com/android/server/SystemServer.java:671:    private void startOtherServices() {
}


public class WindowManagerService extends IWindowManager.Stub
        implements Watchdog.Monitor, WindowManagerPolicy.WindowManagerFuncs {
    final WindowTracing mWindowTracing;

    final private KeyguardDisableHandler mKeyguardDisableHandler;
    // TODO: eventually unify all keyguard state in a common place instead of having it spread over
    // AM's KeyguardController and the policy's KeyguardServiceDelegate.
    boolean mKeyguardGoingAway;
    boolean mKeyguardOrAodShowingOnDefaultDisplay;
    // VR Vr2d Display Id.
    int mVr2dDisplayId = INVALID_DISPLAY;

    private final BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case ACTION_DEVICE_POLICY_MANAGER_STATE_CHANGED:
                    mKeyguardDisableHandler.sendEmptyMessage(KEYGUARD_POLICY_CHANGED);
                    break;
            }
        }
    };
    final WindowSurfacePlacer mWindowPlacerLocked;

    private final PriorityDump.PriorityDumper mPriorityDumper = new PriorityDump.PriorityDumper() {
        @Override
        public void dumpCritical(FileDescriptor fd, PrintWriter pw, String[] args,
                boolean asProto) {
            doDump(fd, pw, new String[] {"-a"}, asProto);
        }

        @Override
        public void dump(FileDescriptor fd, PrintWriter pw, String[] args, boolean asProto) {
            doDump(fd, pw, args, asProto);
        }
    };

    /**
     * Current user when multi-user is enabled. Don't show windows of
     * non-current user. Also see mCurrentProfileIds.
     */
    int mCurrentUserId;
    /**
     * Users that are profiles of the current user. These are also allowed to show windows
     * on the current user.
     */
    int[] mCurrentProfileIds = new int[] {};

    final Context mContext;

    final boolean mHaveInputMethods;

    final boolean mHasPermanentDpad;
    final long mDrawLockTimeoutMillis;
    final boolean mAllowAnimationsInLowPowerMode;

    final boolean mAllowBootMessages;

    final boolean mLimitedAlphaCompositing;
    final int mMaxUiWidth;

    final WindowManagerPolicy mPolicy;

    final IActivityManager mActivityManager;
    final ActivityManagerInternal mAmInternal;

    final AppOpsManager mAppOps;
    final PackageManagerInternal mPmInternal;

    final DisplaySettings mDisplaySettings;

    /** If the system should display notifications for apps displaying an alert window. */
    boolean mShowAlertWindowNotifications = true;

    /**
     * All currently active sessions with clients.
     */
    final ArraySet<Session> mSessions = new ArraySet<>();

    /**
     * Mapping from an IWindow IBinder to the server's Window object.
     * This is also used as the lock for all of our state.
     * NOTE: Never call into methods that lock ActivityManagerService while holding this object.
     */
    final WindowHashMap mWindowMap = new WindowHashMap();

    /**
     * List of window tokens that have finished starting their application,
     * and now need to have the policy remove their windows.
     */
    final ArrayList<AppWindowToken> mFinishedStarting = new ArrayList<>();

    /**
     * List of app window tokens that are waiting for replacing windows. If the
     * replacement doesn't come in time the stale windows needs to be disposed of.
     */
    final ArrayList<AppWindowToken> mWindowReplacementTimeouts = new ArrayList<>();

    /**
     * Windows that are being resized.  Used so we can tell the client about
     * the resize after closing the transaction in which we resized the
     * underlying surface.
     */
    final ArrayList<WindowState> mResizingWindows = new ArrayList<>();

    /**
     * Windows whose animations have ended and now must be removed.
     */
    final ArrayList<WindowState> mPendingRemove = new ArrayList<>();

    /**
     * Used when processing mPendingRemove to avoid working on the original array.
     */
    WindowState[] mPendingRemoveTmp = new WindowState[20];

    /**
     * Windows whose surface should be destroyed.
     */
    final ArrayList<WindowState> mDestroySurface = new ArrayList<>();

    /**
     * Windows with a preserved surface waiting to be destroyed. These windows
     * are going through a surface change. We keep the old surface around until
     * the first frame on the new surface finishes drawing.
     */
    final ArrayList<WindowState> mDestroyPreservedSurface = new ArrayList<>();

    /**
     * Windows that have lost input focus and are waiting for the new
     * focus window to be displayed before they are told about this.
     */
    ArrayList<WindowState> mLosingFocus = new ArrayList<>();

    /**
     * This is set when we have run out of memory, and will either be an empty
     * list or contain windows that need to be force removed.
     */
    final ArrayList<WindowState> mForceRemoves = new ArrayList<>();

    /**
     * Windows that clients are waiting to have drawn.
     */
    ArrayList<WindowState> mWaitingForDrawn = new ArrayList<>();
    /**
     * And the callback to make when they've all been drawn.
     */
    Runnable mWaitingForDrawnCallback;

    /** List of window currently causing non-system overlay windows to be hidden. */
    private ArrayList<WindowState> mHidingNonSystemOverlayWindows = new ArrayList<>();

    IInputMethodManager mInputMethodManager;

    AccessibilityController mAccessibilityController;
    private RecentsAnimationController mRecentsAnimationController;

    Watermark mWatermark;
    StrictModeFlash mStrictModeFlash;
    CircularDisplayMask mCircularDisplayMask;
    EmulatorDisplayOverlay mEmulatorDisplayOverlay;

    final float[] mTmpFloats = new float[9];
    final Rect mTmpRect = new Rect();
    final Rect mTmpRect2 = new Rect();
    final Rect mTmpRect3 = new Rect();
    final RectF mTmpRectF = new RectF();

    final Matrix mTmpTransform = new Matrix();

    boolean mDisplayReady;
    boolean mSafeMode;
    boolean mDisplayEnabled = false;
    boolean mSystemBooted = false;
    boolean mForceDisplayEnabled = false;
    boolean mShowingBootMessages = false;
    boolean mBootAnimationStopped = false;

    // Following variables are for debugging screen wakelock only.
    WindowState mLastWakeLockHoldingWindow = null;
    WindowState mLastWakeLockObscuringWindow = null;

    /** Dump of the windows and app tokens at the time of the last ANR. Cleared after
     * LAST_ANR_LIFETIME_DURATION_MSECS */
    String mLastANRState;

    // The root of the device window hierarchy.
    RootWindowContainer mRoot;

    int mDockedStackCreateMode = SPLIT_SCREEN_CREATE_MODE_TOP_OR_LEFT;
    Rect mDockedStackCreateBounds;

    boolean mForceResizableTasks = false;
    boolean mSupportsPictureInPicture = false;

    boolean mDisableTransitionAnimation = false;


    ArrayList<RotationWatcher> mRotationWatchers = new ArrayList<>();
    int mDeferredRotationPauseCount;
    final WallpaperVisibilityListeners mWallpaperVisibilityListeners =
            new WallpaperVisibilityListeners();

    int mSystemDecorLayer = 0;
    final Rect mScreenRect = new Rect();

    boolean mDisplayFrozen = false;
    long mDisplayFreezeTime = 0;
    int mLastDisplayFreezeDuration = 0;
    Object mLastFinishedFreezeSource = null;
    boolean mWaitingForConfig = false;
    boolean mSwitchingUser = false;

    int mWindowsFreezingScreen = WINDOWS_FREEZING_SCREENS_NONE;

    boolean mClientFreezingScreen = false;
    int mAppsFreezingScreen = 0;

    // Last systemUiVisibility we received from status bar.
    int mLastStatusBarVisibility = 0;
    // Last systemUiVisibility we dispatched to windows.
    int mLastDispatchedSystemUiVisibility = 0;

    // State while inside of layoutAndPlaceSurfacesLocked().
    boolean mFocusMayChange;

    // This is held as long as we have the screen frozen, to give us time to
    // perform a rotation animation when turning off shows the lock screen which
    // changes the orientation.
    private final PowerManager.WakeLock mScreenFrozenLock;

    final AppTransition mAppTransition;
    boolean mSkipAppTransitionAnimation = false;

    final ArraySet<AppWindowToken> mOpeningApps = new ArraySet<>();
    final ArraySet<AppWindowToken> mClosingApps = new ArraySet<>();

    final UnknownAppVisibilityController mUnknownAppVisibilityController =
            new UnknownAppVisibilityController(this);
    final TaskSnapshotController mTaskSnapshotController;

    boolean mIsTouchDevice;

    final H mH = new H();

    /**
     * Handler for things to run that have direct impact on an animation, i.e. animation tick,
     * layout, starting window creation, whereas {@link H} runs things that are still important, but
     * not as critical.
     */
    final Handler mAnimationHandler = new Handler(AnimationThread.getHandler().getLooper());

    WindowState mCurrentFocus = null;
    WindowState mLastFocus = null;

    /** Windows added since {@link #mCurrentFocus} was set to null. Used for ANR blaming. */
    private final ArrayList<WindowState> mWinAddedSinceNullFocus = new ArrayList<>();
    /** Windows removed since {@link #mCurrentFocus} was set to null. Used for ANR blaming. */
    private final ArrayList<WindowState> mWinRemovedSinceNullFocus = new ArrayList<>();

    /** This just indicates the window the input method is on top of, not
     * necessarily the window its input is going to. */
    WindowState mInputMethodTarget = null;

    /** If true hold off on modifying the animation layer of mInputMethodTarget */
    boolean mInputMethodTargetWaitingAnim;

    WindowState mInputMethodWindow = null;

    boolean mHardKeyboardAvailable;
    WindowManagerInternal.OnHardKeyboardStatusChangeListener mHardKeyboardStatusChangeListener;
    SettingsObserver mSettingsObserver;

    /**
     * A count of the windows which are 'seamlessly rotated', e.g. a surface
     * at an old orientation is being transformed. We freeze orientation updates
     * while any windows are seamlessly rotated, so we need to track when this
     * hits zero so we can apply deferred orientation updates.
     */
    private int mSeamlessRotationCount = 0;
    /**
     * True in the interval from starting seamless rotation until the last rotated
     * window draws in the new orientation.
     */
    private boolean mRotatingSeamlessly = false;
 

    // TODO: Move to RootWindowContainer
    AppWindowToken mFocusedApp = null;

    PowerManager mPowerManager;
    PowerManagerInternal mPowerManagerInternal;

    private float mWindowAnimationScaleSetting = 1.0f;
    private float mTransitionAnimationScaleSetting = 1.0f;
    private float mAnimatorDurationScaleSetting = 1.0f;
    private boolean mAnimationsDisabled = false;

    final InputManagerService mInputManager;
    final DisplayManagerInternal mDisplayManagerInternal;
    final DisplayManager mDisplayManager;

    // Indicates whether this device supports wide color gamut rendering
    private boolean mHasWideColorGamutSupport;

    // Who is holding the screen on.
    private Session mHoldingScreenOn;
    private PowerManager.WakeLock mHoldingScreenWakeLock;

    // Whether or not a layout can cause a wake up when theater mode is enabled.
    boolean mAllowTheaterModeWakeFromLayout;

    final TaskPositioningController mTaskPositioningController;
    final DragDropController mDragDropController;

    // For frozen screen animations.
    private int mExitAnimId, mEnterAnimId;

    // The display that the rotation animation is applying to.
    private int mFrozenDisplayId;

    /** Skip repeated AppWindowTokens initialization. Note that AppWindowsToken's version of this
     * is a long initialized to Long.MIN_VALUE so that it doesn't match this value on startup. */
    int mTransactionSequence;

    final WindowAnimator mAnimator;
    final SurfaceAnimationRunner mSurfaceAnimationRunner;

    /**
     * Keeps track of which animations got transferred to which animators. Entries will get cleaned
     * up when the animation finishes.
     */
    final ArrayMap<AnimationAdapter, SurfaceAnimator> mAnimationTransferMap = new ArrayMap<>();
    final BoundsAnimationController mBoundsAnimationController;

    private final PointerEventDispatcher mPointerEventDispatcher;

    private WindowContentFrameStats mTempWindowRenderStats;

    private final LatencyTracker mLatencyTracker;

    /**
     * Whether the UI is currently running in touch mode (not showing
     * navigational focus because the user is directly pressing the screen).
     */
    boolean mInTouchMode;

    private ViewServer mViewServer;
    final ArrayList<WindowChangeListener> mWindowChangeListeners = new ArrayList<>();
    boolean mWindowsChanged = false;

    final Configuration mTempConfiguration = new Configuration();

    // If true, only the core apps and services are being launched because the device
    // is in a special boot mode, such as being encrypted or waiting for a decryption password.
    // For example, when this flag is true, there will be no wallpaper service.
    final boolean mOnlyCore;

    // List of clients without a transtiton animation that we notify once we are done transitioning
    // since they won't be notified through the app window animator.
    final List<IBinder> mNoAnimationNotifyOnTransitionFinished = new ArrayList<>();

    SurfaceBuilderFactory mSurfaceBuilderFactory = SurfaceControl.Builder::new;
    TransactionFactory mTransactionFactory = SurfaceControl.Transaction::new;

    private final SurfaceControl.Transaction mTransaction = mTransactionFactory.make();

    final WindowManagerInternal.AppTransitionListener mActivityManagerAppTransitionNotifier
            = new WindowManagerInternal.AppTransitionListener();

    final ArrayList<AppFreezeListener> mAppFreezeListeners = new ArrayList<>();

    final InputMonitor mInputMonitor = new InputMonitor(this);
    private boolean mEventDispatchingEnabled;

    MousePositionTracker mMousePositionTracker = new MousePositionTracker();
}
```
### SystemServer - ActivityManagerService
```
public class ActivityManagerService extends IActivityManager.Stub
        implements Watchdog.Monitor, BatteryStatsImpl.BatteryCallback {
    /** All system services */
    SystemServiceManager mSystemServiceManager;

    // Wrapper around VoiceInteractionServiceManager
    private AssistUtils mAssistUtils;

    // Keeps track of the active voice interaction service component, notified from
    // VoiceInteractionManagerService
    ComponentName mActiveVoiceInteractionServiceComponent;

    private Installer mInstaller;

    /** Run all ActivityStacks through this */
    final ActivityStackSupervisor mStackSupervisor;
    private final KeyguardController mKeyguardController;

    private final ActivityStartController mActivityStartController;

    private final ClientLifecycleManager mLifecycleManager;

    final TaskChangeNotificationController mTaskChangeNotificationController;

    final InstrumentationReporter mInstrumentationReporter = new InstrumentationReporter();

    final ArrayList<ActiveInstrumentation> mActiveInstrumentation = new ArrayList<>();

    public final IntentFirewall mIntentFirewall;

    // Whether we should show our dialogs (ANR, crash, etc) or just perform their
    // default action automatically.  Important for devices without direct input
    // devices.
    private boolean mShowDialogs = true;

    private final VrController mVrController;

    // VR Vr2d Display Id.
    int mVr2dDisplayId = INVALID_DISPLAY;

    // Whether we should use SCHED_FIFO for UI and RenderThreads.
    private boolean mUseFifoUiScheduling = false;


BroadcastQueue mFgBroadcastQueue;
    BroadcastQueue mBgBroadcastQueue;
    // Convenient for easy iteration over the queues. Foreground is first
    // so that dispatch of foreground broadcasts gets precedence.
    final BroadcastQueue[] mBroadcastQueues = new BroadcastQueue[2];

    BroadcastStats mLastBroadcastStats;
    BroadcastStats mCurBroadcastStats;


    /**
     * The last resumed activity. This is identical to the current resumed activity most
     * of the time but could be different when we're pausing one activity before we resume
     * another activity.
     */
    private ActivityRecord mLastResumedActivity;

    /**
     * The activity that is currently being traced as the active resumed activity.
     *
     * @see #updateResumedAppTrace
     */
    private @Nullable ActivityRecord mTracedResumedActivity;

    /**
     * If non-null, we are tracking the time the user spends in the currently focused app.
     */
    private AppTimeTracker mCurAppTimeTracker;

    /**
     * List of intents that were used to start the most recent tasks.
     */
    private final RecentTasks mRecentTasks;

    /**
     * The package name of the DeviceOwner. This package is not permitted to have its data cleared.
     */
    String mDeviceOwnerName;

    /**
     * The controller for all operations related to locktask.
     */
    private final LockTaskController mLockTaskController;

    final UserController mUserController;

    /**
     * Packages that are being allowed to perform unrestricted app switches.  Mapping is
     * User -> Type -> uid.
     */
    final SparseArray<ArrayMap<String, Integer>> mAllowAppSwitchUids = new SparseArray<>();

    final AppErrors mAppErrors;

    final AppWarnings mAppWarnings;

    /**
     * Dump of the activity state at the time of the last ANR. Cleared after
     * {@link WindowManagerService#LAST_ANR_LIFETIME_DURATION_MSECS}
     */
    String mLastANRState;

    /**
     * Indicates the maximum time spent waiting for the network rules to get updated.
     */
    @VisibleForTesting
    long mWaitForNetworkTimeoutMs;

    /** Total # of UID change events dispatched, shown in dumpsys. */
    int mUidChangeDispatchCount;



    /**
     * Helper class which strips out priority and proto arguments then calls the dump function with
     * the appropriate arguments. If priority arguments are omitted, function calls the legacy
     * dump command.
     * If priority arguments are omitted all sections are dumped, otherwise sections are dumped
     * according to their priority.
     */
    private final PriorityDump.PriorityDumper mPriorityDumper = new PriorityDump.PriorityDumper()


    /**
     * Keeps track of all IIntentReceivers that have been registered for broadcasts.
     * Hash keys are the receiver IBinder, hash value is a ReceiverList.
     */
final HashMap<IBinder, ReceiverList> mRegisteredReceivers = new HashMap<>();

    /**
     * Resolver for broadcast intents to registered receivers.
     * Holds BroadcastFilter (subclass of IntentFilter).
     */
    final IntentResolver<BroadcastFilter, BroadcastFilter> mReceiverResolver
            = new IntentResolver<BroadcastFilter, BroadcastFilter>() ;
    /**
     * State of all active sticky broadcasts per user.  Keys are the action of the
     * sticky Intent, values are an ArrayList of all broadcasted intents with
     * that action (which should usually be one).  The SparseArray is keyed
     * by the user ID the sticky is for, and can include UserHandle.USER_ALL
     * for stickies that are sent to all users.
     */
    final SparseArray<ArrayMap<String, ArrayList<Intent>>> mStickyBroadcasts =
            new SparseArray<ArrayMap<String, ArrayList<Intent>>>();

final ProviderMap mProviderMap;

    /**
     * List of content providers who have clients waiting for them.  The
     * application is currently being launched and the provider will be
     * removed from this list once it is published.
     */
    final ArrayList<ContentProviderRecord> mLaunchingProviders
            = new ArrayList<ContentProviderRecord>();
.....

}
```


```
public class ActivityStackSupervisor extends ConfigurationContainer implements DisplayListener,
        RecentTasks.Callbacks {
    private final SparseArray<ActivityDisplay> mActivityDisplays = new SparseArray<>();
        
}

```
### SystemServer - PackageManagerService
### SystemServer - LocationManagerService

public class LocationManagerService extends ILocationManager.Stub {
        private final Context mContext;
    private final AppOpsManager mAppOps;

    // used internally for synchronization
    private final Object mLock = new Object();

    // --- fields below are final after systemRunning() ---
    private LocationFudger mLocationFudger;
    private GeofenceManager mGeofenceManager;
    private PackageManager mPackageManager;
    private PowerManager mPowerManager;
    private ActivityManager mActivityManager;
    private UserManager mUserManager;
    private GeocoderProxy mGeocodeProvider;
    private IGnssStatusProvider mGnssStatusProvider;
    private INetInitiatedListener mNetInitiatedListener;
    private LocationWorkerHandler mLocationHandler;
    private PassiveProvider mPassiveProvider;  // track passive provider for special cases
    private LocationBlacklist mBlacklist;
    private GnssMeasurementsProvider mGnssMeasurementsProvider;
    private GnssNavigationMessageProvider mGnssNavigationMessageProvider;
    private IGpsGeofenceHardware mGpsGeofenceProxy;
    // --- fields below are protected by mLock ---
    // Set of providers that are explicitly enabled
    // Only used by passive, fused & test.  Network & GPS are controlled separately, and not listed.
    private final Set<String> mEnabledProviders = new HashSet<>();

    // Set of providers that are explicitly disabled
    private final Set<String> mDisabledProviders = new HashSet<>();

    // Mock (test) providers
    private final HashMap<String, MockProvider> mMockProviders =
            new HashMap<>();

    // all receivers
    private final HashMap<Object, Receiver> mReceivers = new HashMap<>();

    // currently installed providers (with mocks replacing real providers)
    private final ArrayList<LocationProviderInterface> mProviders =
            new ArrayList<>();

    // real providers, saved here when mocked out
    private final HashMap<String, LocationProviderInterface> mRealProviders =
            new HashMap<>();

    // mapping from provider name to provider
    private final HashMap<String, LocationProviderInterface> mProvidersByName =
            new HashMap<>();

    // mapping from provider name to all its UpdateRecords
    private final HashMap<String, ArrayList<UpdateRecord>> mRecordsByProvider =
            new HashMap<>();

    private final LocationRequestStatistics mRequestStatistics = new LocationRequestStatistics();

    // mapping from provider name to last known location
    private final HashMap<String, Location> mLastLocation = new HashMap<>();

    // same as mLastLocation, but is not updated faster than LocationFudger.FASTEST_INTERVAL_MS.
    // locations stored here are not fudged for coarse permissions.
    private final HashMap<String, Location> mLastLocationCoarseInterval =
            new HashMap<>();

    // all providers that operate over proxy, for authorizing incoming location and whitelisting
    // throttling
    private final ArrayList<LocationProviderProxy> mProxyProviders =
            new ArrayList<>();

    private final ArraySet<String> mBackgroundThrottlePackageWhitelist = new ArraySet<>();

    private final ArrayMap<IBinder, Identity> mGnssMeasurementsListeners = new ArrayMap<>();

    private final ArrayMap<IBinder, Identity>
            mGnssNavigationMessageListeners = new ArrayMap<>();

    // current active user on the device - other users are denied location data
    private int mCurrentUserId = UserHandle.USER_SYSTEM;
    private int[] mCurrentUserProfiles = new int[]{UserHandle.USER_SYSTEM};

    private GnssLocationProvider.GnssSystemInfoProvider mGnssSystemInfoProvider;

    private GnssLocationProvider.GnssMetricsProvider mGnssMetricsProvider;

    private GnssBatchingProvider mGnssBatchingProvider;
    private IBatchedLocationCallback mGnssBatchingCallback;
    private LinkedCallback mGnssBatchingDeathCallback;
    private boolean mGnssBatchingInProgress = false;
    private final PackageMonitor mPackageMonitor = new PackageMonitor();
}
### SystemServer - NotificationManagerService
```
public class NotificationManager {
    private Context mContext;

}
```


   
## 应用层
### 应用进程创建过程



### Context
```
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

```
class ActivityDisplay extends ConfigurationContainer<ActivityStack> implements WindowContainerListener {

    private ActivityStackSupervisor mSupervisor;
    /** Actual Display this object tracks. */
    int mDisplayId;
    Display mDisplay;

    /**
     * All of the stacks on this display. Order matters, topmost stack is in front of all other
     * stacks, bottommost behind. Accessed directly by ActivityManager package classes. Any calls
     * changing the list should also call {@link #onStackOrderChanged()}.
     */
    private final ArrayList<ActivityStack> mStacks = new ArrayList<>();
    private ArrayList<OnStackOrderChangedListener> mStackOrderChangedCallbacks = new ArrayList<>();

    /** Array of all UIDs that are present on the display. */
    private IntArray mDisplayAccessUIDs = new IntArray();

    /** All tokens used to put activities on this stack to sleep (including mOffToken) */
    final ArrayList<ActivityManagerInternal.SleepToken> mAllSleepTokens = new ArrayList<>();
    /** The token acquired by ActivityStackSupervisor to put stacks on the display to sleep */
    ActivityManagerInternal.SleepToken mOffToken;

    private boolean mSleeping;

    // Cached reference to some special stacks we tend to get a lot so we don't need to loop
    // through the list to find them.
    private ActivityStack mHomeStack = null;
    private ActivityStack mRecentsStack = null;
    private ActivityStack mPinnedStack = null;
    private ActivityStack mSplitScreenPrimaryStack = null;

    // Used in updating the display size
    private Point mTmpDisplaySize = new Point();
}
```
ActivityStack
```
class ActivityStack<T extends StackWindowController> extends ConfigurationContainer
        implements StackWindowListener {
   final ActivityManagerService mService;
    private final WindowManagerService mWindowManager;
    T mWindowContainerController;

    /**
     * The back history of all previous (and possibly still
     * running) activities.  It contains #TaskRecord objects.
     */
    private final ArrayList<TaskRecord> mTaskHistory = new ArrayList<>();

    /**
     * List of running activities, sorted by recent usage.
     * The first entry in the list is the least recently used.
     * It contains HistoryRecord objects.
     */
    final ArrayList<ActivityRecord> mLRUActivities = new ArrayList<>();

    /**
     * When we are in the process of pausing an activity, before starting the
     * next one, this variable holds the activity that is currently being paused.
     */
    ActivityRecord mPausingActivity = null;

    /**
     * This is the last activity that we put into the paused state.  This is
     * used to determine if we need to do an activity transition while sleeping,
     * when we normally hold the top activity paused.
     */
    ActivityRecord mLastPausedActivity = null;

    /**
     * Activities that specify No History must be removed once the user navigates away from them.
     * If the device goes to sleep with such an activity in the paused state then we save it here
     * and finish it later if another activity replaces it on wakeup.
     */
    ActivityRecord mLastNoHistoryActivity = null;

    /**
     * Current activity that is resumed, or null if there is none.
     */
    ActivityRecord mResumedActivity = null;

    // The topmost Activity passed to convertToTranslucent(). When non-null it means we are
    // waiting for all Activities in mUndrawnActivitiesBelowTopTranslucent to be removed as they
    // are drawn. When the last member of mUndrawnActivitiesBelowTopTranslucent is removed the
    // Activity in mTranslucentActivityWaiting is notified via
    // Activity.onTranslucentConversionComplete(false). If a timeout occurs prior to the last
    // background activity being drawn then the same call will be made with a true value.
    ActivityRecord mTranslucentActivityWaiting = null;
    ArrayList<ActivityRecord> mUndrawnActivitiesBelowTopTranslucent = new ArrayList<>();

    /**
     * Set when we know we are going to be calling updateConfiguration()
     * soon, so want to skip intermediate config checks.
     */
    boolean mConfigWillChange;

    /**
     * When set, will force the stack to report as invisible.
     */
    boolean mForceHidden = false;

    private boolean mUpdateBoundsDeferred;
    private boolean mUpdateBoundsDeferredCalled;
    private final Rect mDeferredBounds = new Rect();
    private final Rect mDeferredTaskBounds = new Rect();
    private final Rect mDeferredTaskInsetBounds = new Rect();

    int mCurrentUser;

    final int mStackId;
    /** The attached Display's unique identifier, or -1 if detached */
    int mDisplayId;

    private final SparseArray<Rect> mTmpBounds = new SparseArray<>();
    private final SparseArray<Rect> mTmpInsetBounds = new SparseArray<>();
    private final Rect mTmpRect2 = new Rect();
    private final ActivityOptions mTmpOptions = ActivityOptions.makeBasic();

    /** List for processing through a set of activities */
    private final ArrayList<ActivityRecord> mTmpActivities = new ArrayList<>();

    /** Run all ActivityStacks through this */
    protected final ActivityStackSupervisor mStackSupervisor;

    private boolean mTopActivityOccludesKeyguard;
    private ActivityRecord mTopDismissingKeyguardActivity;

    final Handler mHandler;


}
```



```
class TaskRecord extends ConfigurationContainer implements TaskWindowContainerListener {
    final int taskId;       // Unique identifier for this task.
    String affinity;        // The affinity name for this task, or null; may change identity.
    String rootAffinity;    // Initial base affinity, or null; does not change from initial root.
    final IVoiceInteractionSession voiceSession;    // Voice interaction session driving task
    final IVoiceInteractor voiceInteractor;         // Associated interactor to provide to app
    Intent intent;          // The original intent that started the task. Note that this value can
                            // be null.
    Intent affinityIntent;  // Intent of affinity-moved activity that started this task.
    int effectiveUid;       // The current effective uid of the identity of this task.
    ComponentName origActivity; // The non-alias activity component of the intent.
    ComponentName realActivity; // The actual activity component that started the task.
    boolean realActivitySuspended; // True if the actual activity component that started the
                                   // task is suspended.
    boolean inRecents;      // Actually in the recents list?
    long lastActiveTime;    // Last time this task was active in the current device session,
                            // including sleep. This time is initialized to the elapsed time when
                            // restored from disk.
    boolean isAvailable;    // Is the activity available to be launched?
    boolean rootWasReset;   // True if the intent at the root of the task had
                            // the FLAG_ACTIVITY_RESET_TASK_IF_NEEDED flag.
    boolean autoRemoveRecents;  // If true, we should automatically remove the task from
                                // recents when activity finishes
    boolean askedCompatMode;// Have asked the user about compat mode for this task.
    boolean hasBeenVisible; // Set if any activities in the task have been visible to the user.

    String stringName;      // caching of toString() result.
    int userId;             // user for which this task was created
    boolean mUserSetupComplete; // The user set-up is complete as of the last time the task activity
                                // was changed.

    int numFullscreen;      // Number of fullscreen activities.

    int mResizeMode;        // The resize mode of this task and its activities.
                            // Based on the {@link ActivityInfo#resizeMode} of the root activity.
    private boolean mSupportsPictureInPicture;  // Whether or not this task and its activities
            // support PiP. Based on the {@link ActivityInfo#FLAG_SUPPORTS_PICTURE_IN_PICTURE} flag
            // of the root activity.
    int mLockTaskAuth = LOCK_TASK_AUTH_PINNABLE;

    int mLockTaskUid = -1;  // The uid of the application that called startLockTask().

    // This represents the last resolved activity values for this task
    // NOTE: This value needs to be persisted with each task
    TaskDescription lastTaskDescription = new TaskDescription();

    /** List of all activities in the task arranged in history order */
    final ArrayList<ActivityRecord> mActivities;

    /** Current stack. Setter must always be used to update the value. */
    private ActivityStack mStack;

    /** The process that had previously hosted the root activity of this task.
     * Used to know that we should try harder to keep this process around, in case the
     * user wants to return to it. */
    private ProcessRecord mRootProcess;

    /** Takes on same value as first root activity */
    boolean isPersistable = false;
    int maxRecents;

    /** Only used for persistable tasks, otherwise 0. The last time this task was moved. Used for
     * determining the order when restoring. Sign indicates whether last task movement was to front
     * (positive) or back (negative). Absolute value indicates time. */
    long mLastTimeMoved = System.currentTimeMillis();

    /** If original intent did not allow relinquishing task identity, save that information */
    private boolean mNeverRelinquishIdentity = true;

    // Used in the unique case where we are clearing the task in order to reuse it. In that case we
    // do not want to delete the stack when the task goes empty.
    private boolean mReuseTask = false;

    CharSequence lastDescription; // Last description captured for this item.

    int mAffiliatedTaskId; // taskId of parent affiliation or self if no parent.
    int mAffiliatedTaskColor; // color of the parent task affiliation.
    TaskRecord mPrevAffiliate; // previous task in affiliated chain.
    int mPrevAffiliateTaskId = INVALID_TASK_ID; // previous id for persistence.
    TaskRecord mNextAffiliate; // next task in affiliated chain.
    int mNextAffiliateTaskId = INVALID_TASK_ID; // next id for persistence.

    // For relaunching the task from recents as though it was launched by the original launcher.
    int mCallingUid;
    String mCallingPackage;

    final ActivityManagerService mService;

    private final Rect mTmpStableBounds = new Rect();
    private final Rect mTmpNonDecorBounds = new Rect();
    private final Rect mTmpRect = new Rect();

    // Last non-fullscreen bounds the task was launched in or resized to.
    // The information is persisted and used to determine the appropriate stack to launch the
    // task into on restore.
    Rect mLastNonFullscreenBounds = null;
    // Minimal width and height of this task when it's resizeable. -1 means it should use the
    // default minimal width/height.
    int mMinWidth;
    int mMinHeight;

    // Ranking (from top) of this task among all visible tasks. (-1 means it's not visible)
    // This number will be assigned when we evaluate OOM scores for all visible tasks.
    int mLayerRank = -1;

    /** Helper object used for updating override configuration. */
    private Configuration mTmpConfig = new Configuration();

    private TaskWindowContainerController mWindowContainerController;
}
```

ActivityRecord
```
final class ActivityRecord extends ConfigurationContainer implements AppWindowContainerListener {
    final ActivityManagerService service; // owner
    final IApplicationToken.Stub appToken; // window manager token
    AppWindowContainerController mWindowContainerController;
    final ActivityInfo info; // all about me
    // TODO: This is duplicated state already contained in info.applicationInfo - remove
    ApplicationInfo appInfo; // information about activity's app
    final int launchedFromPid; // always the pid who started the activity.
    final int launchedFromUid; // always the uid who started the activity.
    final String launchedFromPackage; // always the package who started the activity.
    final int userId;          // Which user is this running for?
    final Intent intent;    // the original intent that generated us
    final ComponentName realActivity;  // the intent component, or target of an alias.
    final String shortComponentName; // the short component name of the intent
    final String resolvedType; // as per original caller;
    final String packageName; // the package implementing intent's component
    final String processName; // process where this component wants to run
    final String taskAffinity; // as per ActivityInfo.taskAffinity
    final boolean stateNotNeeded; // As per ActivityInfo.flags
    boolean fullscreen; // The activity is opaque and fills the entire space of this task.
    // TODO: See if it possible to combine this with the fullscreen field.
    final boolean hasWallpaper; // Has a wallpaper window as a background.
    final boolean noDisplay;  // activity is not displayed?
    private final boolean componentSpecified;  // did caller specify an explicit component?
    final boolean rootVoiceInteraction;  // was this the root activity of a voice interaction?

    private CharSequence nonLocalizedLabel;  // the label information from the package mgr.
    private int labelRes;           // the label information from the package mgr.
    private int icon;               // resource identifier of activity's icon.
    private int logo;               // resource identifier of activity's logo.
    private int theme;              // resource identifier of activity's theme.
    private int realTheme;          // actual theme resource we will use, never 0.
    private int windowFlags;        // custom window flags for preview window.
    private TaskRecord task;        // the task this is in.
    private long createTime = System.currentTimeMillis();
    long displayStartTime;  // when we started launching this activity
    long fullyDrawnStartTime; // when we started launching this activity
    private long startTime;         // last time this activity was started
    long lastVisibleTime;   // last time this activity became visible
    long cpuTimeAtResume;   // the cpu time of host process at the time of resuming activity
    long pauseTime;         // last time we started pausing the activity
    long launchTickTime;    // base time for launch tick messages
    // Last configuration reported to the activity in the client process.
    private MergedConfiguration mLastReportedConfiguration;
    private int mLastReportedDisplayId;
    private boolean mLastReportedMultiWindowMode;
    private boolean mLastReportedPictureInPictureMode;
    CompatibilityInfo compat;// last used compatibility mode
    ActivityRecord resultTo; // who started this entry, so will get our reply
    final String resultWho; // additional identifier for use by resultTo.
    final int requestCode;  // code given by requester (resultTo)
    ArrayList<ResultInfo> results; // pending ActivityResult objs we have received
    HashSet<WeakReference<PendingIntentRecord>> pendingResults; // all pending intents for this act
    ArrayList<ReferrerIntent> newIntents; // any pending new intents for single-top mode
    ActivityOptions pendingOptions; // most recently given options
    ActivityOptions returningOptions; // options that are coming back via convertToTranslucent
    AppTimeTracker appTimeTracker; // set if we are tracking the time in this app/task/activity
    HashSet<ConnectionRecord> connections; // All ConnectionRecord we hold
    UriPermissionOwner uriPermissions; // current special URI access perms.
    ProcessRecord app;      // if non-null, hosting application
    private ActivityState mState;    // current state we are in
    Bundle  icicle;         // last saved activity state
    PersistableBundle persistentState; // last persistently saved activity state
    // TODO: See if this is still needed.
    boolean frontOfTask;    // is this the root activity of its task?
    boolean launchFailed;   // set if a launched failed, to abort on 2nd try
    boolean haveState;      // have we gotten the last activity state?
    boolean stopped;        // is activity pause finished?
    boolean delayedResume;  // not yet resumed because of stopped app switches?
    boolean finishing;      // activity in pending finish list?
    boolean deferRelaunchUntilPaused;   // relaunch of activity is being deferred until pause is
                                        // completed
    boolean preserveWindowOnDeferredRelaunch; // activity windows are preserved on deferred relaunch
    int configChangeFlags;  // which config values have changed
    private boolean keysPaused;     // has key dispatching been paused for it?
    int launchMode;         // the launch mode activity attribute.
    int lockTaskLaunchMode; // the lockTaskMode manifest attribute, subject to override
    boolean visible;        // does this activity's window need to be shown?
    boolean visibleIgnoringKeyguard; // is this activity visible, ignoring the fact that Keyguard
                                     // might hide this activity?
    private boolean mDeferHidingClient; // If true we told WM to defer reporting to the client
                                        // process that it is hidden.
    boolean sleeping;       // have we told the activity to sleep?
    boolean nowVisible;     // is this activity's window visible?
    boolean mClientVisibilityDeferred;// was the visibility change message to client deferred?
    boolean idle;           // has the activity gone idle?
    boolean hasBeenLaunched;// has this activity ever been launched?
    boolean frozenBeforeDestroy;// has been frozen but not yet destroyed.
    boolean immersive;      // immersive mode (don't interrupt if possible)
    boolean forceNewConfig; // force re-create with new config next time
    boolean supportsEnterPipOnTaskSwitch;  // This flag is set by the system to indicate that the
        // activity can enter picture in picture while pausing (only when switching to another task)
    PictureInPictureParams pictureInPictureArgs = new PictureInPictureParams.Builder().build();
        // The PiP params used when deferring the entering of picture-in-picture.
    int launchCount;        // count of launches since last state
    long lastLaunchTime;    // time of last launch of this activity
    ComponentName requestedVrComponent; // the requested component for handling VR mode.

    String stringName;      // for caching of toString().

    private boolean inHistory;  // are we in the history stack?
    final ActivityStackSupervisor mStackSupervisor;
int mStartingWindowState = STARTING_WINDOW_NOT_SHOWN;
    boolean mTaskOverlay = false; // Task is always on-top of other activities in the task.

    TaskDescription taskDescription; // the recents information for this activity
    boolean mLaunchTaskBehind; // this activity is actively being launched with
        // ActivityOptions.setLaunchTaskBehind, will be cleared once launch is completed.

    // These configurations are collected from application's resources based on size-sensitive
    // qualifiers. For example, layout-w800dp will be added to mHorizontalSizeConfigurations as 800
    // and drawable-sw400dp will be added to both as 400.
    private int[] mVerticalSizeConfigurations;
    private int[] mHorizontalSizeConfigurations;
    private int[] mSmallestSizeConfigurations;

    boolean pendingVoiceInteractionStart;   // Waiting for activity-invoked voice session
    IVoiceInteractionSession voiceSession;  // Voice interaction session for this activity

    // A hint to override the window specified rotation animation, or -1
    // to use the window specified value. We use this so that
    // we can select the right animation in the cases of starting
    // windows, where the app hasn't had time to set a value
    // on the window.
    int mRotationAnimationHint = -1;

    private boolean mShowWhenLocked;
    private boolean mTurnScreenOn;

    /**
     * Temp configs used in {@link #ensureActivityConfiguration(int, boolean)}
     */
    private final Configuration mTmpConfig = new Configuration();
    private final Rect mTmpBounds = new Rect();
}

```


[四大组件的管理](http://gityuan.com/2017/05/19/ams-abstract/)
[Activity启动模式](gityuan.com/2017/06/11/activity_record/)
android:launchMode：
    SingleTop：栈顶复用，如果处于栈顶，则生命周期不走onCreate()和onStart()，会调用onNewIntent()，适合推送消息详情页
    SingleTask：栈内复用，如果存在栈内，则在其上所有Activity全部出栈，使得其位于栈顶，生命周期和SingleTop一样，app首页基本是用这个。
android:taskAffinity 属性主要和 singleTask 或者 allowTaskReparenting 属性配对使用，在其他情况下没有意义。
android:noHistory： “true”值意味着Activity不会留下历史痕迹。比如启用界面的就可以借用这个。

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





Activity->PhoneWindow
WindowManagerGlobal


- Handler 消息机制
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
- AsyncTask
```

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


### 编译，打包，签名，安装
gradle,Transform的应用
批量打包

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
[动画天梯榜](https://zhuanlan.zhihu.com/p/45597573?utm_source=androidweekly.io&utm_medium=website)
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
[RelativeLayout的性能损耗](https://zhuanlan.zhihu.com/p/52386900?utm_source=androidweekly.io&utm_medium=website)
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
工具：profile，eclipse mat
可维护性：组件化
#### 内存泄漏
 
#### 架构之模块化（插件化及组件化）
插件化
- Dynamic-loader-apk
- Replugin

组件化
- 组件间解耦
  1. MVVM-AAC 
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


#### FLutter &Fuchsia & dart
