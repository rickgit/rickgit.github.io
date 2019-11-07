## Android应用体系
> 软件是计算机系统中与硬件相互依存的另一部分，它是包括程序，数据及其相关文档的完整集合 《软件工程概论》
环境 Android 系统
阅读应用流程（以APK文档为主线，程序Main方法为入口）
[启动流程](https://blog.csdn.net/qq_30993595/article/details/82714409#_2)
```diagram
+---------+--------------------------------------------------------------------------------------------------+
| AppS    |   Browser.apk  Gallery.apk  Launcher3.apk SystemUI(RecentsActivity)   Home  Contacts.apk  Phone  |
|         |   PackageInstaller.apk   PackageInstaller.apk                                                    |
+---------+--------------------------------------------------------------------------------------------------+
| cmds    |   install  am  wm   pm    appops                                                                 |
+--------------------------------+------------------------------------+---------------------+----------------+
|         |         os           |            content                 |   app               |   util         |
|         |(IPC,message passing) | (accessing and publishing data)    |( app  model)        |                |
|         +--------------------------------------------------------------------------------------------------+
|         |  Binder              |      BroadcastReceiver             |  Application        |  SparseArray   |
|         |  MemoryFile          |      ContentProvider               |  Activity    Dialog |  ArrayMap      |
|         |  AsyncTask           |         Context                    |  Fragment           |  LruCache      |
|         |                      |     ClipboardManager               |  AlarmManager       |                |
|         |  Handler             |         Intent                     |  Notification       |                |
|         |                      |      AssetManager/Resource         |  JobScheduler       |                |
| Android |  Environment         |      SharedPreferences             |                     |                |
| SDK     +-----------------------------------------------+-----------+------------++------------------------+
+         | view                 |      widget/webkit     |       graphics          |       animation        |
|         |(ui,layout,interaction|      (UI elements )    |(drawing2Screen directly)|   (property animation) |
|         +--------------------------------------------------------------------------------------------------+
|         |Window  View  KeyEvent| TextView  ImageView    |     Drawable   Bitmap   |       TypeEvaluator    |
|         |GestureDetector       | EditText  Toast        |     Camera     Canvas   |                        |
|         |InputMethodManager    | RecyclerView           |     ColorFilter Point   |                        |
|         |Animation             | ViewPager              |     Rect                |                        |
+--------------------------------+------------------------+-------------------------+------------------------+
| App     |   AMS             WMS    View System        content provider   XMPP                              |//type: BootStrapService,
|Framework|   PMS             NMS    ResourceManager    TelephonyManager   LMS                               |//coreServices,OtherService
+--------------------------------------------------------------------------------------+---------------------+
|         |   Surface Manager    Media       Webkit                        | Android      +----------------+ +
|         |   OpenGL+ES (3d)                 SQLite                        | Runtime      | dalvik vm      | |
|Libraries|   SGL(Skia 2d)                   SSL/TLS                       |              +----------------+ |
|         |   FreeType                       libc(bionic)                  +---------------------------------+
+------------------------------------------------------------------------------------------------------------+
|         |   Bind (IPC) Driver   Display Driver   USB Driver     Power Management                           |
|  Linux  |                       (FrameBuffer)                                                              |
|         |   Bluetooth Driver    Camera Driver    Flash Driver                                              |
|  kernel |                         (V4L2)                                                                   |
|         |   WIFI Driver         Audio Driver     KeyPad Driver                                             |
|         |                       (oss/alsa)                                                                 |
+---------+------------------------------+-------------------------------------------------------------------+
|                                        ^                                                                   |
|                                        |                                                                   |
|  Loader +--->Boot ROM+--->Boot Loader+-+                                                                   |
+------------------------------------------------------------------------------------------------------------+
| computer|    compute storage communicate(display share)                                                    |
+------------------------------------------------------------------------------------------------------------+



```
## 源码
[源码](https://www.cnblogs.com/shenchanghui/p/8503623.html)
```
+------------------------------------------------------------------------------+
|                              packages/apps                                   |
+------------------------------------------------------------------------------+
|                              frameworks                                      |
+-----------------------------------+------------------------------------------+
|  libraries                        |  android runtime                         |
|              bionic               |                  libcore                 |
|                                   |                  dalvik   art  system    |
|                                   +------------------------------------------|
+-----------------------------------+------------------------------------------+
|                              hardware                                        |
+------------------------------------------------------------------------------+
|                              Kernel                                          |
+------------------------------------------------------------------------------+

```
## Linux kernel
android 驱动 主要分两种类型：Android 专用驱动 和 Android 使用的设备驱动（linux）

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
   |  start_kernel(void) |
   +---------+-----------+
             v

   +---------------------+swapper process
   |  reset_init(void)   +--------------+-----------------------------+
   +---------------------+              |                             |
                                        |                             |
                                        v                             v

                           +---------------------+      +---------------------------+
                           | Init Process        |      |    KThread Process        |
                           |                     |      |                           |
                           |                     |      |                           |
+------------------+       |                     |      |                           |
| zygote Process   |  <----+                     |      |                           |
|                  |       |                     |      |                           |
|   Dvm instance   |       |  +---------------+  |      |                           |
|                  |       |  |Service Manager|  |      |                           |
|                  |       |  +---------------+  |      |                           |
| +--------------+ |       |                     |      |                           |
| |system_server | |       |  +---------------+  |      |                           |
| +--------------+ |       |  | mediaserver   |  |      |                           |
| |SysServiceMgr | |       |  +---------------+  |      |                           |
| | AMS,WMS,PMS..| |       |  | AudioFlinger  |  |      |                           |
| | ServerThread | |       |  |   APS         |  |      |                           |
| |   JSS        | |       |  |   MPS         |  |      |                           |
| | AlarmManager | |       |  +---------------+  |      |                           |
| | bluetooth    | |       |                     |      |                           |
| |   NMS  LMS   | |       |                     |      |                           |
| |contentService| |       |                     |      |                           |
| +--------------+ |       |                     |      |                           |
|                  |       |  +---------------+  |      |                           |
|                  |       |  |SurfaceFlinger |  |      |                           |
|                  |       |  +---------------+  |      |                           |
|                  |       |                     |      |                           |
+------------------+       +---------------------+      +---------------------------+
 
                            startservice from init.rc 
  Java Process                 Native Process                Kernel Driver Thread


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
实用性(Client-Server架构)/传输效率(性能)/操作复杂度/安全性
，并发，一对多
                         +-----------+---------+---------------------+
                         | Bundle    | Messager|  Content Provider   |
         +---------------------------+---------+--------------------------------------+-----------------+
         |               |   AIDL    +-------------------------------+                |                 |
         |               |           | byte, char, short, int, long, |                |                 |
         |               |           | float, double, boolean        |                |                 |
         |               |           | String, CharSequence          |                |                 |
         |               |           | Parcelable                    |                |                 |
         |               |           | List<>, Map<>                 |                |                 |
         |               |           | aidl interface                |                |                 |
         |               |           +-------------------------------+                |                 |
         |               |           | import Parcelable package     |                |                 |
         |               |           +-------------------------------+                |                 |
         |               |           | in out inout                  |                |                 |
         |               |           +-------------------------------+                |                 |
         | MemoryFile    |           | oneway                        |                |                 |
         |               |           +-------------------------------+                |                 |
         |               |-------------------------------------------|                |                 |
         | ashmem        |   android.os.Binder                       |                |                 |
         +-----------------------------------------------------------+                |                 |
         |               |                                           |  Socket        |  File           |
         | Shared memory |   Binder                                  |  pipe          | SharedPreference|
         |               |                                           |  messagequeue  |                 |
         +----------------------------------------------------------------------------------------------+
copy     |      0        |                 1                         |       2        |                 |
times    +---------------+-------------------------------------------+----------------+-----------------+

应用安装器打开应用及应用安装器打开应用，第二次launcher打开应用
{                                                                                {
    "mAction": "android.intent.action.MAIN",                                         "mAction": "android.intent.action.MAIN",
    "mCategories": [                                                                 "mCategories": [
        "android.intent.category.LAUNCHER"                                               "android.intent.category.LAUNCHER"
    ],                                                                               ],
    "mComponent": {                                                                  "mComponent": {
        "mClass": "com.xp.browser.activity.SplashActivity",                              "mClass": "com.xp.browser.activity.SplashActivity",
        "mPackage": "com.xp.browser"                                                     "mPackage": "com.xp.browser"
    },                                                                               },
    "mContentUserHint": -2,                                                          "mContentUserHint": -2,
    "mFlags": 268435456,//10000000000000000000000000000  10000000                    "mFlags": 274726912,//10000011000000000000000000000  10600000 =10400000 |10200000 =
    "mPackage": "com.xp.browser"          //FLAG_ACTIVITY_BROUGHT_TO_FRONT/FLAG_RECEIVER_FROM_SHELL|FLAG_ACTIVITY_RESET_TASK_IF_NEEDED/FLAG_RECEIVER_VISIBLE_TO_INSTANT_APPS
}                                                                                        "mSourceBounds": {
                                                                                         "bottom": 395,
                                                                                         "left": 540,
                                                                                         "right": 800,
                                                                                         "top": 120
                                                                                     }
                                                                                 }

 

直接打开及直接打开第二次
{
    "mAction": "android.intent.action.MAIN",
    "mCategories": [
        "android.intent.category.LAUNCHER"
    ],
    "mComponent": {
        "mClass": "com.xp.browser.activity.SplashActivity",
        "mPackage": "com.xp.browser"
    },
    "mContentUserHint": -2,
    "mFlags": 270532608,//10000001000000000000000000000 10200000 FLAG_ACTIVITY_RESET_TASK_IF_NEEDED/FLAG_RECEIVER_VISIBLE_TO_INSTANT_APPS
    "mSourceBounds": {
        "bottom": 395,
        "left": 540,
        "right": 800,
        "top": 120
    }
}
 


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

### Binder机制

**序列化（Parcelable，Serializable）与通讯** Serializable->Parcelable->Binder->{AIDL,Messenger}

[Binder在java framework层的框架](http://gityuan.com/2015/11/21/binder-framework/)
binder是C/S架构，包括Bn端(Server)和Bp端(Client)，ServiceManager,Binder驱动
Binder驱动不涉及任何外设，本质上只操作内存，负责将数据从一个进程传递到另外一个进程。
[Binder机制分析](http://gityuan.com/2014/01/01/binder-gaishu/)

```java
n：native
p：proxy

SystemServer，Binder机制
+----------------+------------+--------------------------------------+-------------------------+
|                |            |  BinderProxy   ServiceManagerProxy   | +---------------------+ |
|                |            |  ServiceManager                      | | IInterface          | |
|                |  Client    +--------------------------------------+ | IBinder             | |
|                |            | BpBinder/BpRefBase   BpInterface     | | IServiceManager     | |
|                |  process   |                                      | |                     | |
|                |            | BpServiceManager                     | +---------------------+ |
|                |            |                                      | | Android_util_Binder | |
|                |            | frameworks//IPCThreadState.cpp   77  | | android_os_Parcel   | |
|                +---------------------------------------------------+ | AndroidRuntime.cpp  | |
|                |            | Binder    ServiceManagerNative       | +---------------------+ |
|                |            | BinderInternal                       | | IInterface          | |
|  user space    |  Server    +--------------------------------------+ | IBinder             | |
|                |            | BBinder/JavaBBinder/JavaBBinderHolder| | IserviceManager     | |
|                |  process   | BnInterface                          | | ProcessState        | |
|                |            |                                      | |                     | |   binder/binderproxy
|                |            | BnServiceManager                     | | IPCThreadState      | | +-----------+
|                |            | frameworks//IPCThreadState.cpp       | +---------------------+ |             |
|                +------------+--------------------------------------+-------------------------+             |
|                                                                         |     ^              |             |
|                                                             getbinder0  v     |  findBinder  |  binder(0)  |
|                +------------+----------------------------------------------------------------+ +------+    |
|                |  Service   |  (handle id = 0)                                               |        v    v
|                |  Manager   |  servicemanager/binder.c                                       |   +------------------+
|                |  process   |  service_manager.c                                             |   |  open/mmap/ioctl |
+----------------+------------+----------------------------------------------------------------+   +------------------+
+----------------+------------+----------------------------------------------------------------+        |    |      
|                |            |                                                                | <------+    |      
|                |  Binder    |                                                                |             |      
|  kernel space  |  Driver    |   drivers/staging/android/binder.c                             | <-----------+     
+----------------+------------+----------------------------------------------------------------+
                                                                                  +       ^
                                                                                  |       |
                                                                                  v       +
                                                                               +---------------+
                                                                               |  kernel memory|
                                                                               +---------------+

----

binder的服务实体
+------------+----------------------------------+------------------------------+
|            |   System Service                |    anonymous binder           |
|            |                                 |    (bindService)              |
+------------------------------------------------------------------------------+
|  launch    | SystemServer                    |  bindService                  |
+------------------------------------------------------------------------------+
| regist and |ServiceManager.addService        |  ActivityManagerService       |
| manager    |                                 |                               |
|            |SystemServiceManager.startService|                               |
|------------+---------------------------------+-------------------------------+
| communicate| SystemServer#getService         |  ServiceConnection            |
|            |                                 |  (binder.asInterface)         |
|------------+---------------------------------+-------------------------------+

通过startService开启的服务，一旦服务开启，这个服务和开启他的调用者之间就没有任何关系了（动态广播 InnerReceiver）;
通过bindService开启服务，Service和调用者之间可以通讯。

名binder必须是建立在一个实名binder之上的，实名binder就是在service manager中注册过的。
首先client和server通过实名binder建立联系，然后把匿名binder通过这个实名通道“传递过去”

```
AIDL 文件生成对应类，类里包含继承Binder的stub内部类和实现AIDL的内部类；

- Bundle(实现了接口Parcelable)
 
### Bluetooth驱动
```

+----------------------------------------+
|  BluetoothScanManager                  |
|      BluetoothAdapter                  |
|         ScanCallback                   |
|         BluetoothDevice                |
|                BluetoothGatt           |
|                BluetoothGattCallback   |
+----------------------------------------+
+----------------------------------------+
| SystemServer                           |
|     Bluetooth Service                  |
|     BluetoothAdapter Service           |
+----------------------------------------+


```





## Native Layer
 


init进程会孵化出ueventd、logd、healthd、installd、adbd、lmkd等用户守护进程
init进程还启动servicemanager(binder服务管家)、bootanim(开机动画)等重要服务
init进程孵化出Zygote进程，Zygote进程是Android系统的第一个Java进程(即虚拟机进程)，Zygote是所有Java进程的父进程
--------------------- 
[作者：硬刚平底锅  ](https://blog.csdn.net/qq_30993595/article/details/82714409)


[jni方法注册方式](https://www.jianshu.com/p/1d6ec5068d05) 

c++的智能指针有很多实现方式，有auto_ptr ,  unique_ptr , shared_ptr 三种， Android 中封装了sp<> 强指针，wp<>弱指针的操作

在Android中，RefBase结合sp（strong pointer）和wp（weak pointer），实现了一套通过引用计数的方法来控制对象生命周期的机制。


### Dispaly 系统与图片适配（density）
```
显示屏幕信息
adb shell wm size
wm size 1080x1920
wm size reset

wm density

wm screen-capture

adb shell dumpsys window displays |head -n 3
 
```

```
                           OpenGL/ES        Rasterization
                           convert to
                           polygons or textures
+---------+     +---------+         +---------+     +--------+
|  xml/UI +----->  CPU    +-------->+  GPU    +---->+ SCREEN |
+---------+     +---------+         +---------+     +--------+


```

```

显示数据类型
1. UI界面的显示，这部分通常数据类型为RGB格式
2. 大块YUV数据的应用，如camera的preview、视频的播放等。该应用只针对特定的应用程序，开启时通过overlay直接把大块的YUV数据送到kernel显示。
3. 和第一种类似，但在显示之前需要对数据进行2D、3D的处理（使用OpenGL、OpenVG、SVG、SKIA）。一般在Game、地图、Flash等应用中会用到。

```
大致分为构建，绘制，渲染，显示
```
+----------------------------------+
|   GUI/View   draw                |
+----------------------------------+
|   Graphic draw                   |
+----------------------------------+
|   Render(Skia,freetype,Opengles) |
+----------------------------------+
|   Display Driver (FrameBuffer)   |
+----------------------------------+
```
```
     sofeware draw                  Hardware acceleration

+------------------+              +------------------+
|    View.onDraw   |              |    View.onDraw   |
+--------+---------+              +--------+---------+
         v                                 v

+------------------+              +------------------+    +------------------+
|    Canvas.draw   |              |    Canvas.draw   +->  |     Renderthread |
+--------+---------+              +------------------+    +---------+--------+
         v                                                          v

+------------------+                                      +------------------+
|      Skia        |                       +--------------+      GPU         |
+--------+---------+                       |              +------------------+
         v                                 v

+------------------+              +------------------+
|      Display     |              |      Display     |
+------------------+              +------------------+

```
```
                                                   +-----------------------+
                                                   |       Binder driver   |
                                                   +------------+----------+
                                                                |
                                                                |             +------------+ +------------+       +------------+
                                                                | +--->       |   Surface  | |  Surface   |       |   Surface  |
                                                                | Ser^er      +-----+------+ +------+-----+       +-----+------+
                                                                |                   |               |                   |
                                                                |                   |               |                   |
                                                                |         +---------+---------------+-------------------+--------------+
+-------------------------------------------------------        |         |                         Surfaceflinger           SW/HW Vsyc|
|  App                                                 |        |         |    main_surfaceflinger.cpp     SurfaceFlinger.cpp          |
+-----------------------------------+------------------+        |         +------------------------------------------------------------+
|   View/Graphic/Widget             | OpenGL ES        |        |         |                                 +--------------------------+
+-----------------------------------+                  |        |         |     EGLDisplaySurface           |           | back buffer  |
|   Skia                            |                  |        |         |                                 |frontbuffer+--------------+
|     +-----------------------------+  +---------------+        |         |                                 |           | back buffer  |
|     |                             |  |               |        |         +---------------------------------+-----------+--------------+
|     | libjpeg/libpng/libgif/libft2|  |  libagl/libhgl|        |         | HAL    FramebufferNativeWinow                              |
|     |                             |  |               |        |         +------------------------------------------------------------+
+-----+-----------------------------+--+---------------+        |         |                        Gralloc                             |
|   Surface                                            |        |         |                       gpu0 fb0                             |
|                                                      |        |         +------------------------------------------------------------+
+-------------------------------------------------------        |
                                     wms   SurfaceComposerClient|         +------------------------------------------------------------+
                                           +                    |  kernel |                /dev/graphics/fb*                           |
                                           |                    |         +------------------------------------------------------------+
                                           |           Client   |
                                           +---------------->   |
                                                                |



```

#### Skia

Canvas是一个2D的概念，是在Skia中定义的
Skia 2D和OpenGL/ES 3D

[skia api](https://skia.org/user/api?cl=9919)
```
SkCanvas - the drawing context.
SkPaint - color, stroke, font, effects
SkRect - rectangles
SkRegion - set operations with rectangles and paths
SkPath - contours of lines and curves
+--------------------------------------------------------------------+
|                                                                    |
| SkCanvas                        SkImageFilter                      |
| SkPaint                            SkAlphaThresholdFilter          |
| SkPath                             SkBlurImageFilter               |
| SkImage                            SkBitmapSource                  |
| SkSurface                          SkColorFilterImageFilter        |
| SkShader                           SkComposeImageFilter            |
|    SkComposeShader                 SkDisplacementMapEffect         |
|    SkPerlinNoiseShader             SkDownSampleImageFilter         |
|    SkGradientShader                SkDropShadowImageFilter         |
|    SkTransparentShader             SkLightingImageFilter           |
| SkColorFilter                      SkMagnifierImageFilter          |
|    SkColorMatrixFilter             SkMatrixConvolutionImageFilter  |
|    SkLumaColorFilter               SkMergeImageFilter              |
|    SkModeColorFilter               SkDilateImageFilter             |
|    SkPathEffect                    SkErodeImageFilter              |
|    SkPath2DPathEffect              SkOffsetImageFilter             |
|    SkLine2DPathEffect              SkPictureImageFilter            |
|    SkPath1DPathEffect              SkRectShaderImageFilter         |
|    SkArcToPathEffect               SkTileImageFilter               |
|    SkCornerPathEffect              SkXfermodeImageFilter           |
|    SkDashPathEffect             SkMaskFilter                       |
|    SkDiscretePathEffect            SkTableMaskFilter               |
|    SkComposePathEffect          SkDrawLooper                       |
|    SkSumPathEffect                 SkBlurDrawLooper                |
|                                                                    |
+--------------------------------------------------------------------+
```

#### 数据渲染 - OpenGL ES 栅格化
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
+----------------+-----------------------------+-----------+
|                | own   Surface|   EGL/Render |  hard acc |
+----------------------------------------------------------+
| SurfaceView    |    √         |     x        |           |
+----------------------------------------------------------+
| GLSurfaceView  |    √         |     √        |           |
+----------------------------------------------------------+
| TextureView    |    x         |              |     √     |
+----------------+--------------+--------------+-----------+

```


#### 数据渲染 SurfaceFlinger - [Graphic图形系统](http://gityuan.com/2017/02/05/graphic_arch/)

SystemServer的RenderThread线程
   ,用于提升系统流畅度：
- 垂直同步（Vertical Synchronized ） 即 VSYNC 定时中断
- 三重缓存（Triple Buffer ）,跳帧保证不帧
- 编舞者/编排器（Choreographer ）, 起到调度作用（ViewRootImpl实现统一调度界面绘图），绘制速度和屏幕刷新速度保持一致
黄油计划的核心VSYNC信号分为两种，一种是硬件生成（HardwareComposer）的信号，一种是软件模拟（VSyncThread来模拟）的信号。
1. 解决撕裂(yield)问题，CPU/GPU调度快于Display，保证双缓冲(Back buffer,Frame Buffer); 
2. 2.janking问题，CPU来不及处理，Display显示前一帧，帧延迟。

 
[12fps 24fps 30fps 60fps](https://www.youtube.com/watch?v=CaMTIgxCSqU)


CPU：负责 Measure、Layout、Record、Execute 的计算操作。CPU 负责把 UI组件计算成 Polygons（多边形）和 Texture（纹理），然后交给 GPU 进行栅格化。
GPU：负责 Rasterization（栅格化）操作。GPU 的栅格化过程是绘制 Button、Shape、Path、String、Bitmap 等组件最基础的操作。
```java
+--------------------------------------------------------------------------------------------------------------+
+---------------------+                                                          +-----------------------------+
| App process         |              +------------------------+                  | SurfaceFlinger              |
|                     |              |  wms                   |                  |                             |
|                     | <----------> | SurfaceComposerClient  |  <------------>  |                             |
|   +-----------------+              |                        |                  |                             |
|   |   Measure()     |              |                        |                  |  +---------+----------+     |
|   |   layout()      |              |                        |                  |  |         |          |     |
|   |   draw()        |              +------------------------+                  |  |  client |  client  |     |
|   |                 |                                                          |  |         |          |     |
|   +-----------------+                                                          |  +---------+----------+     |
|   |   Choreographer |                                                          |                             |
+---+-----------------+----------------------------------------------------------+-----------------------------+
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
|         |  SharedBufferStack              |  ...(31)           |                                             |
|         +---------------------------------+--------------------+                                             |
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
 
```
```
root@x86:/ # ls -l /dev/graphics/
crw-rw---- root     graphics  29,   0 2018-12-13 15:15 fb0
``` 
 

##### 输入事件 - Input

```
+-----------------------------------------------------------------------------------------+
+------------------------------+           +----------------------------------------------+
|  App process                 |           | SystemServer IMS         +-----------------+ |
|                              |           |                          |  InputReader    | |
|                              |           |                          | +------+        | |
|  +---------------------------+           +--------------------+     | |input |        | |
|  |  ViewRootImpl             |           | InputDispatcher    |     | |mapper|        | |
|  |  +------------------------+           +------------------+ |     | +------+        | |
|  |  |  InputEventReceiver    |           | InputPublisher   | |     |      +--------+ | |
|  |  |    +-------------------+  socket   +---------------+  | |     |      |eventhub| | |
|  |  |    |   InputChannel    | <------>  | Inputchannel  |  | |     |      +--------+ | |
|  |  |    |                   |(low api pipe)|            |  | |     +-----------------+ |
+--+--+----+-------------------+-----------+---------------+--+-+-------------------------+
                                                                                 |  ^
                                                                     Linux epoll |  | inotify
                                                                                 v  |
                                                                       ----------+--+-----+
                                                                       | /dev/input/event |event driver
                                                                       +------------------+

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

##### WMS 窗口动画动画事件 
[动画天梯榜](https://zhuanlan.zhihu.com/p/45597573?utm_source=androidweekly.io&utm_medium=website)
```
 +-------------+-------------------------------+------------------+-------------------+-------------------+
 |  layout     |                               | request layout   |  requestLayout()  |  target          |
 |  Animation  |                               |                  |  draw Command     |                   |
 +------------------------------------------------------------------------------------+-------------------+
 |Drawble/Frame|   webp/gif                    |                  |                   |                   |
 | Animation   |                               |                  |                   |                   |
 +---------------------------------------------+  invalidate draw |  draw command     |   views           |
 |             |   draw() + invalidate()       |                  |                   |                   |
 |  Draw       +-------------------------------+                  |                   |                   |
 |  Animation  |                               |                  |                   |                   |
 |             |   computeScroll()+invalidate()|                  |                   |                   |
 +------------------------------------------------------------------------------------+-------------------+
 |  View/Tween |  translate/rotate/scale/alpha |  parent          |  View Property    |    views          |
 |  Animation  |  Fragment compat Transition   |  Render Node     |                   |alpha,matrix(translate,scale,rotate)|
 +------------------------------------------------------------------------------------+-------------------+
 |             |   View.animate()              |                  |                   |                   |
 |  Animator   |                               |                  |                   |    objects        |
 |             |   offsetChildrenTopAndBottom  |  Render Property |  View Property    |                   |
 |             |   ActivityOptions Transition  |                  |                   |                   |
 +------------------------------------------------------------------------------------+-------------------+
 |  Render     |   AnimatedVectorDrawable      |                  |                   |                   |
 |  Thread     |   RippleComponet              |  Inter-Process   | view property     |                   |
 |  Amimation  |   Revel Animation             |                  |                   |                   |
 +------------------------------------------------------------------------------------+-------------------+
 |  Window     |                               | SystemServer     |                   |                   |
 |  Transition |   Window Transition Animation | WindowManager    | some view property|                   |
 |  Animation  |                               |                  |                   |                   |
 +-------------+-------------------------------+------------------+-------------------+-------------------+

```

插值器是时间的函数，定义了动画的变化规律

- 补间动画  Animation（Rotate,Scale,translate,alpha）
  动画的数据（Transformation/alpha,matrix/），计算（插值器），绘制，回调

```java
计算，回调
  +android.view.View#draw(android.graphics.Canvas, android.view.ViewGroup, long)
    +android.view.View#applyLegacyAnimation
[插值器](http://cogitolearning.co.uk/2013/10/android-animations-tutorial-5-more-on-interpolators/)
绘制
canvas.concat(transformToApply.getMatrix());或
renderNode.setAnimationMatrix(transformToApply.getMatrix());
```

   箭头动画
   启动图片放大动画
   弹窗动画
 

 

- 逐帧动画 AnimationDrawable
     烟花效果
```java
     AnimationDrawable.start()
    +android.graphics.drawable.Drawable#scheduleSelf
        +android.view.View#scheduleDrawable //mChoreographer.postCallbackDelayed( Choreographer.CALLBACK_ANIMATION...)
```
- 属性动画 Animator
补间动画，动画作用目标单一，动画类型少，View只有绘制，属性没有改变
  动画的数据（ValueAnimator#PropertyValuesHolder[]），计算（插值器），绘制，回调

```java
ValueAnimator,ObjectAnimator


    +android.animation.ValueAnimator#start(boolean)
          +android.animation.AnimationHandler#addAnimationFrameCallback// Choreographer#postCallbackDelayedInternal(CALLBACK_ANIMATION
          +android.animation.ValueAnimator#addAnimationCallback //垂直同步回调
          +android.animation.ValueAnimator#doAnimationFrame   //计算数据     
          +android.animation.ValueAnimator#doAnimationFrame   //计算数据   
          +android.animation.ValueAnimator#animateBasedOnTime
          +android.view.ViewPropertyAnimator.AnimatorEventListener#onAnimationUpdate//mView.invalidateParentCaches(); 或更新RenderNode ,
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

##### 绘制事件 - ViewRootImpl#traversal
[Activity的显示之ViewRootImpl的预测量、窗口布局、最终测量、布局、绘制五大过程](http://segmentfault.com/a/1190000012018189)

### 窗口，见WMS

## ART-dalvik
```
              |java compiler(javac)
    +-----------------------+
    | java byte code(.class)|
    +---------+-------------+
              |   Dex compiler
              v   (dx.bat)
     +--------+--------------+
     | Dalvik byte code(.dex)|
     +---+-----------------+-+
         | dex2oat         |dexopt
+--------v---+       +-----+-------+
|.oat(elf file)|     |    .odex    |
+---+--------+       +----+--------+
    |                     |   Register-based
    |  +---------+        |   +---------------+
    |  | ART     |        |   |   Dalvik VM   |
    |  |      AOT|        |   |           JIT |
    |  +---------+        |   +---------------+
    |                     |
    |moving collector     | MarkSweep collector
    v                     v

+--------+---------+------------------------+----------------------+
|        | Active  |                        |                      |
|        | Heap    |                        |    Live Bitmap       |
|DalvikVM|         |                        |                      |
|  Heap  |         |                        |                      |
|(Ashmem)|         |   Mark-Sweep Collector +----------------------+
| mspace |         |                        |                      |
|        +---------+                        |                      |
|        | Zygote  |                        |                      |
|        | Heap    |                        |   Mark Bitmap        |
|        | (shared)|                        |                      |
+----------------------+--------------------+----------------------+
|        |             | Image Space                               |
|        | Continuous  +---------------+---------------------------+
|        |             | Zygote Space  | Zygote Space              |
|  ART   | Space       |               +---------------------------+
|        |             |               | Allocation Space          |
|  Heap  |             |               |     ....                  |
|        +-------------+---------------+---------------------------+
|        |Discontinuous  Large Object                              |
|        |    Space    | Space                                     |
+--------+-------------+-------------------------------------------+


                                                    +
                                                    |new ArrayList
                                                    |
                                                    v
      +--------+  +-------+  +---------+ +------+ +-+-----+ +-------+
      |  ART   |  | Zygote|  | No      | | Image| |       | | Large |
Heap  |        |  | Space |  | Moving  | | Space| | Alloc | | Object|
      |  L+    |  |       |  | space   | |      | | Space | | Space |
      +--------+  +-------+  +---------+ +------+ +-------+ +-------+

      +--------+  +-------+  +---------+ +------+
      |DalvikVM|  | Linear|  | Zygote  | |Alloc |
      |  <L    |  | Alloc |  | space   | |Space |
      |        |  |       |  |         | |      |
      +--------+  +-------+  +---------+ +------+


```

### dalvik bytecode
```
dx --dex --output=Hello.dex Hello.class
```

### 类加载机制，类加载器，双亲委派
```
                 C++
 +-----------------------+
 | Bootstrap ClassLoader |  Framework classs
 +----------^------------+
            |
  +---------+----------+   
  | BaseDexClassLoader |   <|-------------------+
  +---------^----------+                        |
            |  DexPathList                       |
            |                                   |
            |                                   |
+-----------+-------------+                     |
| PathClassLoader         | apk class           |
+-----------^-------------+                     |
            |  parent                           |
+-----------+------------+           extends    |
| DexClassLoader         | +--------------------+
+------------------------+



```



[支持的垃圾回收机制](https://www.jianshu.com/p/153c01411352)
Mark-sweep算法：还分为Sticky, Partial, Full，根据是否并行，又分为ConCurrent和Non-Concurrent
MarkSweep::MarkSweep(Heap* heap, bool is_concurrent, const std::string& name_prefix)
mark_compact 算法：标记-压缩（整理)算法
concurrent_copying算法：
semi_space算法: 


#### android触发垃圾回收
当Bitmap和NIO Direct ByteBuffer对象分配外部存储（机器内存，非Dalvik堆内存）触发。
系统需要更多内存的时候触发。
HPROF时触发。

[回收机制](https://blog.csdn.net/f2006116/article/details/71775193)
[android hash](https://blog.csdn.net/xusiwei1236/article/details/45152201)

[smalidea 无源码调试 apk](https://blog.csdn.net/hackooo/article/details/53114838)
### 类加载问题
[pre-verify问题](https://www.jianshu.com/p/7217d61c513f)
QQ空间补丁
```
1. 阻止相关类打上Class_ispreverified标志
2. 动态更改BaseDexClassLoader间接引用的dexElements
```
[Redex](https://blog.csdn.net/tencent_bugly/article/details/53375240)

### 关键类与对象


 **Object**
public class Object {

    private transient Class<?> shadow$_klass_;
    private transient int shadow$_monitor_;
}


**ArrayMap**
相比HashMap链表存储，使用了两个小的容量的数组
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
```java
final int osize = mSize;
int n=osize >= (4*2) ? (osize+(osize>>1)): (osize >= 4 ? (4*2) : 4)
```

查找 二分法mHashes表，
插入 从mHashes二分法找key的hash，mHashes向后向前查找key，执行替换。没找到hash或key，System.arraycopy执行插入key,value（先判断扩容）
删除  左移动mhash,mValue，根据情况调整新的大小后，填掉删除的位置
```java
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
删除 数组设置为常量 DELETED（new Object()）每次 **remove** 操作都会**mGarbage**设置为**true**
gc  标记为DELETED，key,Value替换为有值的数据。

### 数据存储
#### 文件存储,SharedPreferences,SQLite数据库方式,内容提供器（Content provider）,网络

```
+---------------------------------------+
|         conent provider|              |
+---------------------------------------+
|sharedpreference|SQLite |   network    |
+---------------------------------------+
|              file      |              |
+------------------------+--------------+
```
```
SQLiteOpenHelper

getWritableDatabase()

executeSQL()

executeQuery()

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

相关系统服务（引导服务，核心服务，其他服务）
 PKMS（安装、卸载、更新以及解析AndroidManifest.xml），
 AMS（（1）统一调度各应用程序的Activity
      （2）内存管理
      （3）进程管理），
窗口
- WMS 窗口管理（输出-显示,分为系统窗口，应用窗口，子窗口；包括 Activity，Dialog，PopupWindow，Toast），
- 动画
- IMS（输入-事件），
- NMS(通知,Toast),IMMS(输入法弹窗)
- Surface 窗口绘制


PwMS,JSS,DMS,DisplayManagerService、BatteryService，MSM


```java
IBinder b=ServiceManager.getService(Context.ACTIVITY_SERVICE) 获取远程服务


Context.getSystemService(Context.TELEPHONY_SERVICE) 获取远程服务代理对象
```

```
                                          +------------------------------------------+
                                          | SystemSer^er  Process                    |
                                          |  +-----------------------------------+   |
                                          |  |  PMS                              |   |   install/permission
                                          |  |                                <-------------------------------+
                                          |  |                                   |   |   Ser^iceManager
                                          |  +-----------------------------------+   |                         +----------------------------+
                                          |                                          |                         |  App Process               |
                                          |  +------------------------------------+  |                         |    +---------------------+ |
 +-------------------+    start process   |  |                                    |  |                         |    | ActivityThread      | |
 |                   |                    |  |  AWS                               |  |      startActi^ity      |    |                     | |
 |  Zygote Process   | <---------------   |  |                                    |  |       +------------------------+  Acti^ityRecord   | |
 |                   |                    |  |      ProcessRecord                 |  |       |                 |    |                     | |
 |                   |                    |  |                                    |  |       |                 |    |    ApplicationThread| |
 |                   |                    |  |        IApplicationThread   +-----------------+                 |    |                     | |
 |                   |                    |  |                                    |  |                         |    +---------------------+ |
 |                   |                    |  |        HistoryRecord               |  |                         |                            |
 |                   |                    |  |        ZygoteProcess               |  |                         |                            |
 +-------------------+                    |  |        Ser^iceRecord               |  |                         |                            |
                                          |  |                                    |  |  RootViewImpl#addWindow |                            |
                                          |  |                                    |  |          +----------------+                          |
                                          |  |                                    |  |          |              +----------------------------+
                                          +  +-----------------------------------++  +          +
+-------------------+                     |   +------------------------------------+ |          |
|  Surface Flinger  |                     |   |   WMS                              | |          |
|                   |    <-----------------+  |                                <----------------+
|                   |                     |   |                                    | |          +
+-------------------+                     |   +------------------------------------+ |
                                          |                                          |
                                          +------------------------------------------+

```
### SystemServer - 窗口事件 InputManagerService
 [事件](http://gityuan.com/2016/12/31/input-ipc/)
 [事件子系统](https://blog.csdn.net/jscese/article/details/42099381)
- InputReader线程：通过EventHub从/dev/input节点获取事件，转换成EventEntry事件加入到InputDispatcher的mInboundQueue。EventHub采用INotify + epoll机制

- InputDispatcher线程：从mInboundQueue队列取出事件，转换成DispatchEntry事件加入到connection的outboundQueue队列。再然后开始处理分发事件，取出outbound队列，放入waitQueue.InputChannel.sendMessage通过socket方式将消息发送给远程进程；

- UI线程：创建socket pair，分别位于”InputDispatcher”线程和focused窗口所在进程的UI主线程，可相互通信。 
UI主线程：通过setFdEvents()， 监听socket客户端，收到消息后回调NativeInputEventReceiver();【见小节2.1】
“InputDispatcher”线程： 通过IMS.registerInputChannel()，监听socket服务端，收到消息后回调handleReceiveCallback；【见小节3.1】
```java

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
### SystemServer - 窗口视图（ 测量，布局及绘制,事件，动画，适配）wms
Activity、Dialog、PopWindow、Toast

 popupwindow 与 Dialog
- popupwindow 非阻塞浮层
- Dialog 阻塞式对话框

#### 其他组件 View ，controls,layouts

``` 
SystemUI（Statusbar）
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
SystemUI（NavigateUI）


Toolbar
+-------------+------------+------------+------------------------------+-------------------------+ 
|             |            |   Title    |                              |                         |
|  NavImageBtn|   Logo     |            |   CustomView                 |      ActionMenuView     |
|             |            +------------+                              |                         |
|             |            |   subTitle |                              |                         | 
+-------------+------------+------------+------------------------------+-------------------------+

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

### SystemServer -窗口通知 NMS( NotificationManagerService) 
**通知**是一个可以在应用程序正常的用户界面之外显示给用户的消息。

```java

public class NotificationManagerService extends SystemService {
    private final IBinder mService = new INotificationManager.Stub();
    private WorkerHandler mHandler;
}
}

 protected class NotificationListenerWrapper extends INotificationListener.Stub {
```
**NotificationListenerService** 获取系统通知相关信息，主要包含：通知的新增和删除，获取当前通知数量，通知内容相关信息
NotificationManager/nofitycation
```
+-----------------------------------+         +-------------------------------------+
|  SystemServer                     |         |  SystemUI(App process)              |
|                                   |         |                                     |
|                                   |         |                                     |
|       +-------------------------+ |         |   +-------------------------+       |
|       | NMS                     | |         |   | NotificationListener    |       |
|       |                         | |         |   |                         |       |
|       |   sound/vibrate/LIGHTS  | |         |   |                         +----+  |
|       |                         | |         |   |                         |    |  |
|       |   INotificationListener +<---------------->  onBind(Intent intent)|    |  |
|       |                         | |         |   |                         |    |  |
|       +-------------------------+ |         |   +-------------------------+    |  |
|                                   |         |   +---------------------------+  |  |
|                                   |         |   | NotificationEntryManager  |  |  |
|                                   |         |   |       NotificationPanel   | <+  |
|                                   |         |   |       Heads-up            |     |
|                                   |         |   |       NotificationClicker |     |
|                                   |         |   +---------------------------+     |
+-----------------------------------+         +-------------------------------------+

```
[SystemUI应用启动](https://www.jianshu.com/p/2e0f403e5299)
```
Status Bar状态栏信息显示，比如电池，wifi信号，3G/4G等icon显示
Notification Panel 通知面板，比如系统消息，第三方应用消息
Recents 近期任务栏显示面板，比如长按近期任务快捷键，显示近期使用的应用
NavigationBar（导航栏）
Keyguard（锁屏界面）
Screenshot截图服务
壁纸服务
VolumeUI 音量调节对话框
PowerUI 电源界面
Ringtoneplayer 铃声播放器页面

android 7.0:
StackDriver分屏功能调节器
PipUI画中画界面
```

### SystemServer - PKMS(PackageManagerService)
#### apk安装过程/应用进程创建过程/应用安装过程
[Android系统启动流程](http://gityuan.com/2016/02/01/android-booting/)
- [安装](http://gityuan.com/2016/11/13/android-installd/)

- 运行时权限
```
+----------------------------------------------------------------------------------w---+
|                                                                                     |
|                                                                                     |
+----------+-------+----------+---------+------------+-------+---------+----+---------+
| CALENDAR |CAMERA | CONTACTS | LOCATION| MICROPHONE | PHONE | SENSORS | SMS| STORAGE |
+----------+-------+----------+---------+------------+-------+---------+----+---------+

```
- hind api

### Resource Manager

```
+--------------------------------------------+
|   App                                      |
|   +--------------------------------------+ |
|   |  Resource                            | |
|   |     AssetManager                     | |
|   |     DisplayMetrics                   | |
|   |     Configuration                    | |
|   |     DisplayAdjustments               | |
|   +--------------------------------------+ |
+--------------------------------------------+


```
APK文件中有一个文件resource.arsc。这个文件存放的是APK中资源的ID和资源类型，属性，文件名的读经关系表和所有的字符串，装载APK，就是解析该文件生成ResRTable对象


```
+----------+-------------+-----------+----------+----------------
|          |  make dirs  | compile   |   R.java |  access       |
+---------------------------------------------------------------+
|  assets  |      √      |  x        |     x    | AssetManager  |
|          |             |           |          |               |
+---------------------------------------------------------------+
|  res/raw |      x      |  √        |     √    | Resource      |
|          |             |           |          |               |
+----------+-------------+-----------+----------+---------------+


```
Dex加固
```
    +--------------------------------+
    |  APK                           |
    |         +----------------------+
    |         | Assets               |
    |         |         shell dex    |
    |         |                      |
    |         +----------------------+
    |                                |
    |                  Unpack dex    |
    +--------------------------------+
```
脱壳dex文件的作用主要有两个，一个是解密加密后的dex文件；二是基于dexclassloader动态加载解密后的dex文件 


### SystemServer - LocationManagerService
```java
public class LocationManagerService extends ILocationManager.Stub { 
}
```




### SystemServer - mediaserver
多媒体信息，包括文字，图像，图形，音频，视频

主要解释视频

```
                                                                    +-------------------------+ +-----------------------------------------------+
                                                                    | Camera                  | | CameraManager                                 |
                                                                    |   setPreviewDisplay()   | |   CameraDevice                                |
                                                                    |   takePicture()         | |     CameraDevice.StateCallback                |
                                                                    |   startPreview()        | |     CameraCaptureSession                      |
                                                                    |                         | |          CameraCaptureSession.StateCallback   |
                                                                    |                         | |          setRepeatingRequest()//setPreviewDis |
                                                                    |   Camera.PreviewCallback| |          capture() //                         |
                                                                    |                         | |     CaptureRequest                            |
+--------------------------------------+--------------------------+ |                         | |          Builder.addTarget()                  |
|  AudioTrack/AudioRecorder            | MediaPlayer/MediaRecorder| |                         | |          CameraCaptureSession.CaptureCallback |
|  AudioSystem                         | VideoView                | |   CameraInfo            | |   CameraCharacteristics                       |
+-----------------------------------------------------------------+ +-------------------------+ +-----------------------------------------------+
+-----------------------------------------------------------------+ +---------------------------------------------------------------------------+
| mediaserver                                                     | | mediaserver                                                               |
|  +--------------------+                 +---------------------- | |                                                                           |
|  |                    |                 |  MediaPlayerService | | |                                                                           |
|  | AudioManager       | -------------+  |  AudioTrack         | | |                                                           +---------------+
|  | AudioPolicyService | |            |  |  AudioRecorder      | | |                                                           |               |
|  |                    | |AudioFlinger|  |                     | | |                                                           |  CameraService|
|  +--------------------+ +------------+  +---------------------+ | |                                                           +---------------+
+-----------------------------------------------------------------+ +---------------------------------------------------------------------------+
|      HAL                             |                                                                                        |  HAL          |
+--------------------------------------+                                                                                        +---------------+
+-----------------------------------------------------------------+ +---------------------------------------------------------------------------+
|                 tinyalsa audio driver                           | |                          v4l2 camera driver                               |
+-----------------------------------------------------------------+ +---------------------------------------------------------------------------+
```
#### 音频驱动
- OSS
- ALSA（替代OSS）
- TinyAlsa,ALSA的缩减版本 
录音 
```
1. 调用录音应用 MediaStore.Audio.Media.RECORD_SOUND_ACTION
2. AudioRecorder 录制的是PCM格式的音频文件，需要用AudioTrack来播放，AudioTrack更接近底层。 
3. MediaRecorder 录制的音频文件是经过压缩后的，需要设置编码器。并且录制的音频文件可以用系统自带的Music播放器播放。 

```
播放 声音
```
soundPool 音效，播放比较短的音频片段，比如游戏声音、按键声、铃声片段等等，它可以同时播放多个音频
mediaplayer 背景音乐，播放多种格式的声音文件
AudioTrack 背景音乐，更接近底层。只能播放已经解码的PCM流，播放不需要解码的wav
```
AudioFlinger进行混音


#### Camera 驱动 
V4L2驱动程序就要为这些硬件设备提供音视频的合成以及编解码的功能接口

```
Camera2 CaptureRequest
TEMPLATE_RECORD 创建适合录像的请求。
TEMPLATE_PREVIEW 创建一个适合于相机预览窗口的请求。
TEMPLATE_STILL_CAPTURE 创建适用于静态图像捕获的请求
TEMPLATE_VIDEO_SNAPSHOT 在录制视频时创建适合静态图像捕获的请求。
```
 
1. 拍照： Camera+BitmapFactory或Camera2+ImageReader
2. [预览，保存图片](https://blog.csdn.net/foolish0421/article/details/77732140)
扫描二维码-zxing
3. 录视频：MediaRecorder+Camera



#### 播放视频及视频渲染

```
State Diagram                                            +----+                                                          +-------+
                                           reset()+----> |Idle|                                          release() +---->+ end   |
                                                         +-+--+                                                          +-------+
                        prepareAsync()     setDataSource() |  OnErrorListener.onError()  +-------------+
                                                           |    +------------------------>  error      |
          +-----------+                               +----v------+                      +-------------+
          | preparing <-------------------------------+Initialized|
          +---^---+---+                               +----+------+
              |   |                                        |
              |   | onPreparedListener.onPrepared()        |  prepare()
              |   |                               +--------v-------+
              |   +---------------------------->  |     prepared   <-----+
              |   +--------------------------->   |                |     |
              |   |             +--------------+  +--------+-------+     | seekTo()
              |   |             |                          |    +--------+
              |   |             |                          |
prepareAsync()|   |prepare()    |                          | start()
              |   |             <--------------------------v-----------------------+
              |   |             |     +---->|            started                   <---+
              |   |             |     |    ++--------------+--^------------^--+---++   |  Looping==true&&playback completes
              |   |             |     |     |              |  |            |  |   |    |
              |   |             |     +-----+       pause()|  |start()     |  |  -+----+
              |   |             |    seekTo()/start()      |  |            |  |
              |   |             |                          |  |            |  |
        +-----+---+-+           |                     +----v--+--+         |  | Looping==false&&OnCompletionListener.OnCompletion()
  +-->  |  stoped   |           <---------------------+  paused  <-----+   |  +-----------------+
  |     +-----+---^-+           |                     +---------++     |   +----------------+   |
  |           |   |             |              seekTo()/Pause() |      |                    |   |
  +-----------+   |             |                               +------+                    |   |
stop()            |      stop() |                +--------------------+              start()|   |
                  |             |                |                    +---------------------+   |
                  +-------------v----------------+ PlayBackCompleted  <-------------------------+
                                                 |                    <----+
                                                 +--------------+-----+    |
                                                                |          | seekTo()
                                                                +----------+

```


```
+------------------------------------------------------+
|        MediaPlayer.java                              |
+------------------------------------------------------+
|        android_media_player.cpp  (libmedia_jni.so)   |
+------------------------------------------------------+
|        MediaPlayer.cpp           (libmedia.so)       |
+------------------------------------------------------+
|        HTTP / RTSP / HTTPLive                        |
+------------------------------------------------------+
|                          +---------------------------+
|        Stagefright       |       NuPlayer            |
+--------------------------+---------------------------+

```
获取多媒体信息 使用系统应用
拍照/录像 /录音 （见前文） 
录屏 MediaProjection + MediaRecorder + 组合，或 MediaProjection + MediaCodec + MediaMuxer

显示图片 ImageView
播放音视频 MediaRecorder

- MediaPlayer + SurfaceView
- VideoView
- FFmpeg

#### 图片Bitmap
[bitmap管理](https://developer.android.com/topic/performance/graphics/manage-memory.html)


## 应用层
Zygote 子线程
```
ps -t | grep -E "NAME| <zygote ps id> "
```
### 广播
```

                                                      +---------------------------------------+
+---------------------+-----------+-----------+       | SystemServer                          |
|                     | runtime   | location  |       |                                       |
|                     |           |           |       |   PMS                                 |
+---------------------------------------------+       |     mReceivers:ActivityIntentResolver |
|                     |           |           |       |                                       |
|   mReceivers        |  install  |  WMS      |       |     mReceiverResolver                 |
|                     |           |           |       |                                       |
+---------------------------------------------+       |   AMS                                 |
|   mReisterdReceivers|  run app  |  AMS      |       |     mReisterdReceivers:IIntentReceiver|
|                     |           |           |       |                                       |
+---------------------+-----------+-----------+       +---------------------------------------+

```

### 应用内消息机制（异步）
- Handler        子线程与主线程通讯
- Thread
- AsyncTask      界面回调，异步任务，一次性
- HandlerThread  异步队列，子线程与子线程通讯
- TimeTask       定时任务
- IntentServices 无界面，异步任务
- ThreadPool     并行任务
#### handler
管道pipe唤醒主线程和epoll机制
```
     +------------------------------------------+
     |  Handler                                 |
     +------------------------------------------+
     | +---------------+  +---------------------+
+----+ |enqueueMessage |  | dispatchMessage     |
|    | |               |  |   Message#callback  |  <----+
|    | +---------------+  |   mCallback         |       |
|    |                    |   handleMessage()   |       |
|    +--------------------+---------------------+       |
|                                                       |
|    +--------------+                                   |
|    |Looper Thread |                                   |
|    +--------------+----------------------------+      | (runOnLooperThread)
|    |  Looper                                   |      |
|    +-------------------------------------------+      |
|    |   loop()                                  |      |
|    |                                           |      |
|    |  +---------------------+                  | +----+
|    |  | MessageQueue        | epoll            |  +--------------------+
+--> |  |  Message mMessages  +------>           |  | Message            |
     |  |                     |                  |  |  Handler target    |
     |  +---------------------+                  |  |  Runnable callback |
     +-------------------------------------------+  +--------------------+

Handler{
    final Looper mLooper;
    final MessageQueue mQueue;
    final Callback mCallback;
    final boolean mAsynchronous;
    IMessenger mMessenger;
}

```
[select/poll/epoll对比分析](http://gityuan.com/2015/12/06/linux_epoll/)
 [源码解读poll/select内核机制](http://gityuan.com/2019/01/05/linux-poll-select/)

[MessageQueue采用epoll](http://gityuan.com/2019/01/06/linux-epoll/)
```bash
ls /proc/<pid>/fd/  //可通过终端执行，看到该fd
```
1. 监视的描述符数量不受限制，所支持的FD上限是最大可以打开文件的数目，具体数目可以
```    
cat /proc/sys/fs/file-max
```
2. epoll不同于select和poll轮询的方式，而是通过每个fd定义的回调函数来实现的

####  AsyncTask
```java
                    +--------------------------------------------+  MESSAGE_POST_RESULT
                    |   AsyncTask                                |  MESSAGE_POST_PROGRESS
                    |                                            |              +-----------------------------+
                    |   +----------------------------+           |              |   MainLooper                |
way 1   +---------> |   | FutureTask(WorkerRunnable) |           |        +---> |                             |
                    |   +----------+-----------------+           |        |     |                             |
                    |              |                             |        |     +-----------------------------+
                    |              |     +-----------------------+        |
                    |              |     |Handle:InternalHandler | -------+
                    |              |     |                       |
                    |              |     +-----------------------+  <-----------------------------------------+
                    |              |                             |                                            |
                    +--------------------------------------------+                                            |
                           mStatus |                                                                          |
                           +-------+                                                                          |
                                   |                                                                          |
                           offer() v                                                                          |
 way 2                                                                  +--------------------+------------+   |
+--------------+           +----------------------------+               | ThreadPoolExecutor |            |   |
| Runnable     +------->   | SerialExecutor|            |   ArrayDeque  +--------------------+            |   |
+--------------+           +---------------+            |   #poll()     | LinkedBlockingQueue             |   |
+--------------+   offfer()|          mTasks:ArrayDeque |  +--------->  |    +-----------+ +------------+ |   |
| Runable      +-------->  |                            |               |    |FeueureTask| | FutureTask | +---+
+--------------+           +----------------------------+               |    +-----------+ +------------+ |
                                                                        +---------------------------------+

```        
    容器类：ArrayDeque，LinkedBlockingQueue（ThreadPoolExecutor的线程队列）
    并发类：ThreadPoolExecutor（包含 ThreadFactory属性，用于创建线程），AtomicBoolean，AtomicInteger，FutureTask(包含Callable属性，任务执行的时候调用Callable#call,执行AsyncTask#dobackgroud)



### 四大组件基础 - Context
Context作用
```

+--------------------------------------------------------------------------------------------+
|   ContextImpl                                                                              |
+-----------------------------------------------------+--------------------------------------+
|        ActivityThread mMainThread                   |    File mDatabasesDir                |
+-----------------------------------------------------+    File mPreferencesDir              |
|        LoadedApk mPackageInfo                       |    File mFilesDir                    |
|                                                     |    File mNoBackupFilesDir            |
|        ResourcesManager mResourcesManager           |    File mCacheDir                    |
|        ApplicationContentResolver mContentResolver  |    File mCodeCacheDir                |
+-----------------------------------------------------+                                      |
|       Display mDisplay                              |    ArrayMap<String, File>            |
|                                                     |    mSharedPrefsPaths                 |
+-----------------------------------------------------+--------------------------------------+
|       Object[] mServiceCache                                                               |
+--------------------------------------------------------------------------------------------+

                                    AMS                Activity
                                                       Service
                                +------------------->  ContentProvider
                                |                      BrocastReceiver
                                |
                  SystemServer  |   PKMS
                                |------------------->  Permission/Pkginfo/HindAPI
+------------------+            |
|                  | +----------+   WMS
|     context      |            +------------------->  Activity/Dialog/PopupWindow/Toast  ---->SurfaceFlipper
|                  |
+------------------+ +----------+
                                |
                ActivityThread  |   AssetManager
                                +------------------->  loadResource

查看权限
adb shell pm list permissions -d -g                 
``` 
[hind api](https://android.googlesource.com/platform/prebuilts/runtime/+/master/appcompat)

**/art/tools/veridex/appcompat.sh --dex-file=test.apk**
``` dot
APK文件->Gradle编译脚本->APK打包安装及加载流程->AndroidManifest->四大组件->{Activity,Service,BrocastReceiver,ContentProvider}
 
```
### 四大组件-Activity
```
+---------------------------------------------------------------+
|   AMS                                                         |
|     +-------------------------------------------------------+ |
|     |ProcessRecord                                          | |
|     |                                                       | |
|     |    ActivityRecord                                     | |
|     |                                                       | |
|     |    ServiceRecord   ConnectionRecord                   | |
|     |                                                       | |
|     |    BroadcastRecord  ReceiverList                      | |
|     |                                                       | |
|     |    ContentProviderRecord    ContentProviderConnection | |
|     |                                                       | |
|     +-------------------------------------------------------+ |
+---------------------------------------------------------------+

```
#### 生命周期
```
                                  +--------+
                                  | Start  |
                                  +----+---+
                                       v                                                   +-----------------+
                                  +----+---+                            Activity States    |onAttach         |
      +------------------------>  |onCreate|                                               |oncreate         |
      |                           +---+----+                                               |oncreateView     +<---+
      | back to                       v  created +------------------------------------>    |onActivityCreated|    |
      | foreground                +---+----+        +-----------+                          |                 |    |
      |                           |onStart |  <-----+ onRestart +--+                       +-----------------+    |
      | recreate                  |        |        +-----------+  |                                              |
+-----+---------+                 +----+---+                       |                       +---------+            |
| Process killed|                      v started                   |   +-------------->    |onStart  |            |
+-----+---------+       +-------+ +----+---+                       |                       +---------+            |
      |                 v         |onResume|  <-----+activity      |                                              |
      |             +---+------+  +--------+        |froreground   |                       +---------+            |
      |   other     |Running   |         resumed    |              |   +-------------->    |onResume |<-----+     |
      |   activity  +---+------+                    |              |                       +---------+      |     |
      |   foreground    |         +--------+        |              |                              Fragm is  |     |
      |                 +-------> |onPause |        |    activity  |                              retaininstance  | onBack
      | <-----------------------+ |        | +----->+    foreground|                          onactivityRecreate  |
      |   other app               +---+----+                       |                       +---------+      |     |
      |   need memory                 v  paused                    |     +------------>    |onPause  |------|     |
      |                                  no longe visiable         |                       +---------+            |
      |                           +--------+                       |                                              |
      +-------------------------+ |onStop  | +---------------------+                                              |
                                  +---+----+                                               +---------+            |
                                      v  stoped                       +--------------->    |onStop   |            |
                                  +---+----+                                               +---------+            |
                                  |onDestroy                                                                      |
                                  |        +                                               +-v-----v------+       |
                                  +---+----+                                               |onDestroyView +-------+
                                      v  destroyed  +--------------------------------->    |onDestroy     |
                                  +---+----+                                               |onDetach      |
                                  |shutdown|                                               +--------------+
                                  +--------+


```
#### AMS 栈管理（任务栈），启动模式，亲和度
Activity的启动模式必不可少的要是launchMode、Flags、taskAffinity
```bash
adb shell dumpsys activity---------------查看ActvityManagerService 所有信息
adb shell dumpsys activity activities----------查看Activity组件信息
adb shell dumpsys activity services-----------查看Service组件信息
adb shell dumpsys activity providers----------产看ContentProvider组件信息
adb shell dumpsys activity broadcasts--------查看BraodcastReceiver信息
adb shell dumpsys activity intents--------------查看Intent信息
adb shell dumpsys activity processes---------查看进程信息


adb shell dumpsys activity activities | sed -En -e '/Running activities/,/Run #0/p'
adb shell dumpsys activity activities | sed -En -e '/Stack/p' -e '/Running activities/,/Run #0/p'

adb shell dumpsys activity providers | sed -En -e '/Stack/p' -e '/Running activities/,/Run #0/p'



>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
adb shell am kill <packagename>

If you want to kill the Sticky Service,the following command NOT WORKING:

adb shell am force-stop <PACKAGE>
adb shell kill <PID>
The following command is WORKING:

adb shell pm disable <PACKAGE>
If you want to restart the app,you must run command below first:

adb shell pm enable <PACKAGE>
<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
```
```java
//ActivityStarter的启动模式代码阅读
import static android.content.pm.ActivityInfo.LAUNCH_SINGLE_INSTANCE;
import static android.content.pm.ActivityInfo.LAUNCH_SINGLE_TASK;
import static android.content.pm.ActivityInfo.LAUNCH_SINGLE_TOP; 
```

 
 [ActivityStack](https://blog.csdn.net/guoqifa29/article/details/54863237)

android N开始有5种窗口类型（窗口类型及ActivityType决定ActivityStack） ：
全屏 FullScreenStack
DockedStack（分屏Activity） configChanges:screenLayout   onMultiWindowModeChanged
PinnedStack（画中画Activity）。PinnedStack非Focusable stack，处于paused状态，故无法接受key事件，也无法成为输入法焦点窗口
freeformstack(自由模式Activity) ：FreeForm Stack
homeStack（launcher和recents Activity）和其他
```java
import static android.app.WindowConfiguration.ACTIVITY_TYPE_HOME;
import static android.app.WindowConfiguration.ACTIVITY_TYPE_RECENTS;
import static android.app.WindowConfiguration.ACTIVITY_TYPE_STANDARD;
import static android.app.WindowConfiguration.ACTIVITY_TYPE_UNDEFINED;
import static android.app.WindowConfiguration.WINDOWING_MODE_FREEFORM;
import static android.app.WindowConfiguration.WINDOWING_MODE_FULLSCREEN;
import static android.app.WindowConfiguration.WINDOWING_MODE_FULLSCREEN_OR_SPLIT_SCREEN_SECONDARY;
import static android.app.WindowConfiguration.WINDOWING_MODE_PINNED;
import static android.app.WindowConfiguration.WINDOWING_MODE_SPLIT_SCREEN_PRIMARY;
import static android.app.WindowConfiguration.WINDOWING_MODE_SPLIT_SCREEN_SECONDARY;
import static android.app.WindowConfiguration.WINDOWING_MODE_UNDEFINED;
```


 桌面Launcher、任务栏Recents属于id=HOME_STACK的栈中。多窗口不仅仅是控制Activity放入不同ActivityStack中，同时还要改变Activity的生命周期，即Focus Activity是resume状态，其他可见Activity是Pause状态，并不会进入Stop状态

```
ActivityDisplay#0（一般只有一显示器） 
+--------------------------------------------------------------------------------------+
| ActivityStack#0                                                                      |//不用类型（mHomeStack，mFocusedStack）
|    +---------------------+                                                           |//区分ActivityStack
|    | +----------------+  |      +---------------------+                              |
|    | |ActivityRecord  |  |      |                     |                              |
|    | +----------------+  |      |                     |                              |
|    | +----------------+  |      | +----------------+  |                              |
|    | |ActivityRecord  |  |      | |ActivityRecord  |  |                              |
|    | +----------------+  |      | +----------------+  |                              |
|    | +----------------+  |      | +----------------+  |                              |
|    | |ActivityRecord  |  |      | |ActivityRecord  |  |                              |
|    | +----------------+  |      | +----------------+  |                              |
|    +---------------------+      +---------------------+                              |
|    +---------------------+      +---------------------+                              |
|    |  TaskRecord#0       |      | TaskRecord#1        |                              |//亲和度（taskAffinity）区分TaskRecord
|    +---------------------+      +---------------------+                              |
+--------------------------------------------------------------------------------------+
+--------------------------------------------------------------------------------------+
| ActivityStack#1                                                                      |
|    +---------------------+                                                           |
|    | +----------------+  |      +---------------------+                              |
|    | |ActivityRecord  |  |      |                     |                              |
|    | +----------------+  |      |                     |                              |
|    | +----------------+  |      | +----------------+  |                              |
|    | |ActivityRecord  |  |      | |ActivityRecord  |  |                              |
|    | +----------------+  |      | +----------------+  |                              |
|    +---------------------+      +---------------------+                              |
|    +---------------------+      +---------------------+                              |
|    |  TaskRecord#0       |      | TaskRecord#1        |                              |
|    +---------------------+      +---------------------+                              |
+--------------------------------------------------------------------------------------+




```


[四大组件的管理](http://gityuan.com/2017/05/19/ams-abstract/)
[Activity启动模式](gityuan.com/2017/06/11/activity_record/)

```
+------------+-----------------------+------------------------------+
|            |                       |newIntent()|taskAffinity      |
|            +------------------------------------------------------+
|            |        standard       |           |                  |
|            +------------------------------------------------------+
|            |        singleTop      |   √       |                  |
| launch mode+------------------------------------------------------+
|            |        singleTask     |   √       |choice  TaskRecord|
|            +------------------------------------------------------+
|            |        singleInstance |   √       |                  |
+-------------------------------------------------------------------+
|  Flags     | FLAG_ACTIVITY_NEW_TASK|           |choice TaskRecord |
+-------------------------------------------------------------------+
|            |   allowTaskReparenting|           |change to affinity task|
+------------+------------------------------------------------------+
FLAG_ACTIVITY_NEW_TASK
在google的官方文档中介绍，它与launchMode="singleTask"具有相同的行为。实际上，并不是完全相同！
很少单独使用FLAG_ACTIVITY_NEW_TASK，通常与FLAG_ACTIVITY_CLEAR_TASK或FLAG_ACTIVITY_CLEAR_TOP联合使用。因为单独使用该属性会导致奇怪的现象，通常达不到我们想要的效果！尽管如何，后面还是会通过"FLAG_ACTIVITY_NEW_TASK示例一"和"FLAG_ACTIVITY_NEW_TASK示例二"会向你展示单独使用它的效果。

FLAG_ACTIVITY_SINGLE_TOP
在google的官方文档中介绍，它与launchMode="singleTop"具有相同的行为。实际上，的确如此！单独的使用FLAG_ACTIVITY_SINGLE_TOP，就能达到和launchMode="singleTop"一样的效果。

FLAG_ACTIVITY_CLEAR_TOP
顾名思义，FLAG_ACTIVITY_CLEAR_TOP的作用清除"包含Activity的task"中位于该Activity实例之上的其他Activity实例。FLAG_ACTIVITY_CLEAR_TOP和FLAG_ACTIVITY_NEW_TASK两者同时使用，就能达到和launchMode="singleTask"一样的效果！

FLAG_ACTIVITY_CLEAR_TASK
FLAG_ACTIVITY_CLEAR_TASK的作用包含Activity的task。使用FLAG_ACTIVITY_CLEAR_TASK时，通常会包含FLAG_ACTIVITY_NEW_TASK。这样做的目的是启动Activity时，清除之前已经存在的Activity实例所在的task；这自然也就清除了之前存在的Activity实例！
```

android:noHistory： “true”值意味着Activity不会留下历史痕迹。比如启用界面的就可以借用这个。
android:alwaysRetainTaskState触发时机在系统清理后台Task，且Activity实例为根Activity时。 
android:finishOnTaskLaunch Task重新启动时(比如桌面上点击某一个应用图标)，会销销毁此Task中的该Activity实例。
android:clearTaskOnLaunch 只会作用于某一Task的根Activity。


单任务无法内存回收。多任务，内存回收
```
单栈的进程，Activity跟进程声明周期一致
多栈的，只有不可见栈的Activity可能被销毁（Java内存超过3/4,不可见）
该回收机制利用了Java虚拟机的gc机finalize(ActivityThread->BinderInternal.addGcWatcher)
至少两个TaskRecord占才有效，所以该机制并不激进，因为主流APP都是单栈。
```
### contentProvider

ContentService扮演者ContentObserver的注册中心

ContentProvider——内容提供者， 在android中的作用是对外共享数据，也就是说你可以通过ContentProvider把应用中的数据共享给其他应用访问，其他应用可以通过ContentProvider 对你应用中的数据进行添删改查。

ContentResolver——内容解析者， 其作用是按照一定规则访问内容提供者的数据（其实就是调用内容提供者自定义的接口来操作它的数据）。
ContentObserver——内容观察者，目的是观察(捕捉)特定Uri引起的数据库的变化，继而做一些相应的处理，它类似于数据库技术中的触发器(Trigger)，当ContentObserver所观察的Uri发生变化时，便会触发它。 

```java
    private static final class ApplicationContentResolver extends ContentResolver {
        private final ActivityThread mMainThread;
    }
 

    public class ContentProviderHolder implements Parcelable {
            public final ProviderInfo info;
        public IContentProvider provider;
        public IBinder connection;// IContentProvider 通过这个对象传输数据,由ContentProvider#mTransport赋值
        public boolean noReleaseNeeded;
    }

                                          +-------------------------------------------------------------+
                                          |  SystemServer                                               |
                                          |             +-------------------------------------+         |
                   +---------------------------+        |   ContentService                    |   +----------------------+
                   |                      |    |        |      ObserverNode                   |   |     |                |
                   v                      |    +-------------+   IContentObserver      +----------+     |                v
                                          |             |                                     |         |
+--------------------------------------+  |             +-------------------------------------+         |  +---------------------------------+
|  App                                 |  |                                                             |  |  App2                           |
|                     ActivityManager. |  |     +-------------------------------------------------------+  |                                 |
|    mProviderMap       getService()   |  |     | AMS                 +->  mProviderMap                ||  |    +---->  mProviderMap         |
|                                      |  |     +                     |         +                      ||  |    |                            |
|      +  ^            +-------------------> getContentProvider()  +--+         v                      ||  |    |             +              |
|      |  |                            |  |     +                                                      ||  |    |             |              |
|      |  |                            |  |     |                         +-----------------------------+  |    |             v              |
|      |  |                            |  |     |                         |        IApplicationThread  |   |    |                            |
|      |  |                            |  |     +                         |                            +--------+   scheduleInstallProvider()|
|      |  +---------------------------------+ ContentProviderHolder  <--+-+ContentPro^iderRecord.wait()||  |                                 |
|      |                               |  |     +                       ^ +-----------------------------|  |                                 |
|      |                               |  |     |               +-------+                              ||  |                                 |
|      |                               |  |     |               |   +------------------------------+   ||  |          ActivityManager.       |
|      |                               |  |     |               |   |     publishContentProviders()|  <--------------+  getService()         |
|      |                               |  |     |               +---+ContentPro^iderRecord.notify()|   ||  |                                 |
|      |                               |  |     |                   +------------------------------+   ||  | +-------------------------------+
|      |                               |  |     +-------------------------------------------------------+  | |  ContentProvider             ||
|      |                               |  |                                                             |  | |    Transport:IContentProvider||
|      |                               |  |                                                             |  | +-------------------------------|
+--------------------------------------+  +-------------------------------------------------------------+  +---------------------------------+
       |                                                                                                                  ^
       +------------------------------------------------------------------------------------------------------------------+


   URI = scheme:[//authority]path[?query][#fragment]

   normal
低风险权限，只要申请了就可以使用，安装时不需要用户确认。 
dangerous
高风险权限，安装时需要用户确认授权才可使用。 
signature
只有当申请权限应用与声明此权限应用的数字签名相同时才能将权限授给它。 
signatureOrSystem
签名相同或者申请权限的应用为系统应用才能将权限授给它 
```


### 数据存储
SharedPreferences,文件存储,SQLite数据库方式,内容提供器（Content provider）,网络

ContentProvider->保存和获取数据，并使其对所有应用程序可见

### 编译，打包，优化，签名，安装
gradle,Transform的应用
批量打包
```打包流程
G: gradle build tools
B: android build tools
J: JDK tools


+--------------------------------------------------------------------------------------+
| /META-INF                                                                             |
| /assets                                                                                |
| /res                                                                                  |
| /libs                                                                                 |
| class.dex                                                                            |
| resources.arsc                                                                       |
| AndroidManifest.xml                                                                  |
+--------------------------------------------------------------------------------------+
|G                                                                                     |
|    multiple agent tool                                                               |
+--------------------------------------------------------------------------------------+
|B                                                                                     |
|   zipalign                                                                           |
+--------------------------------------------------------------------------------------+
|J                                                                                     |
|   javasigner  V1, V2(N), V3(P)                                                       |
+--------------------------------------------------------------------------------------+
|G                                                                                     |
|   ApkBuilder                                                                         |
+--------------------+                          +--------------------------------------+
|B                   |                          |B                                     |
|  linker            |                          |    dex                               |
+--------------------------------------------------------------------------------------+
|B                   |B                         |G             +-----------------------+
|  bcc compat        |   AAPT                   |    proguard  |          Preveirfy    |
|                    |                          |              |          Obfuscate    |
|                    |                          |              |          Optimize     |
|                    |                          |              |          Shrink       |
|                    |                          |              +-----------------------+
+--------------------+                          +--------------------------------------+
|B                   |                          |        J                             |
|  llvm-rs-cc        |                          +-------+    javac                     |
|                    |                          | R.java|                              |
|                    +--------------------------+--------------------------------------+
|                    |G                                 |B                             |
|                    |   menifest/assets/resource merger|    aidl                      |
+--------------------+-----------------------------------------------------------------+

                                                                                              

```

## 函数式编程
Glide


                                                                                                                                                                                        
## Android 开发模式

### 性能优化总结
> <<Android High Performance Programming>>

> 性能（ the time taken to execute tasks）

[Android性能测试（内存、cpu、fps、流量、GPU、电量）——adb篇](https://www.jianshu.com/p/6c0cfc25b038)
稳定，流畅，续航，精简
am_crash

```shell 
#adb shell "getprop | grep heapgrowthlimit"
#adb shell "getprop|grep dalvik.vm.heapstartsize"
#adb shell "getprop|grep dalvik.vm.heapsize"

#adb shell "dumpsys meminfo -s <pakagename | pid>"
# while true;do adb shell procrank|grep <proc-keywords>; sleep 6;done


# adb shell "cat  /proc/cpuinfo" //查看cpu核心数
# adb shell "dumpsys cpuinfo | grep <package | pid>"
# adb shell "top -n 5 | grep <package | pid>" 

# adb shell "dumpsys gfxinfo com.xp.browser" //GPU 帧数 fps


# adb shell "dumpsys batterystats < package | pid>" //电量采集
```

#### 应用稳定性（Stability：how many failures an application exhibits）-异常及严苛模式
```
services/core/java/com/android/server/am/AppErrors.java:

StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectCustomSlowCalls() //API等级11，使用StrictMode.noteSlowCode
//                    .detectDiskReads()
//                    .detectDiskWrites()
                    .detectNetwork()   // or .detectAll() for all detectable problems
                    .penaltyDialog() //弹出违规提示对话框
                    .penaltyLog() //在Logcat 中打印违规异常信息
                    .penaltyFlashScreen() //API等级11
                    .build());
StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
        .detectLeakedSqlLiteObjects()
        .detectLeakedClosableObjects() //API等级11
        .penaltyLog()
        .detectFileUriExposure()
        .penaltyDeath()
        .build());
```
#### 内存泄漏
 
 工具：profiler，eclipse mat
 [Activity 泄漏和重复创建的冗余Bitmap-ResourceCanary](https://mp.weixin.qq.com/s/KtGfi5th-4YHOZsEmTOsjg?utm_source=androidweekly.io&utm_medium=website)
```
LeakCanary通过ApplicationContext统一注册监听的方式，来监察所有的Activity生命周期，并在Activity的onDestroy时，执行RefWatcher的watch方法，该方法的作用就是检测本页面内是否存在内存泄漏问题。

所有未被回收的 Bitmap 的数据 buffer 取出来，然后先对比所有长度为 1 的 buffer，找出相同的，记录所属的 Bitmap 对象；再对比所有长度为 2 的、长度为 3 的 buffer ……直到把所有buffer都比对完，这样就记录下了所有冗余的 Bitmap 对象了，接着再套用 LeakCanary 获取引用链的逻辑把这些 Bitmap 对象到 GC Root 的最短强引用链找出来即可。
```
 内存泄漏优化 - 资源未释放
        3.1 单例
        3.2 非静态内部类
        3.3 资源未关闭（webview没执行 destroy）
        3.4 ListView 未缓存
        3.5 集合类未销毁


```sh
>adb shell dumpsys meminfo edu.ptu.java.kotlinbase
Applications Memory Usage (kB):
Uptime: 53403267 Realtime: 53403267

** MEMINFO in pid 32724 [edu.ptu.java.kotlinbase] **
                   Pss  Private  Private  Swapped     Heap     Heap     Heap
                 Total    Dirty    Clean    Dirty     Size    Alloc     Free
                ------   ------   ------   ------   ------   ------   ------
  Native Heap        0        0        0        0    15616    14393     1222
  Dalvik Heap     2908     2416        0        0    13722     9283     4439
 Dalvik Other      680      532        0        0
        Stack      260      260        0        0
       Ashmem        2        0        0        0
    Other dev        4        0        4        0
     .so mmap     3713      340      924        0
    .apk mmap      972        0       88        0
    .ttf mmap      164        0      132        0
    .dex mmap     3259        4     2652        0
    .oat mmap     2602        0      520        0
    .art mmap     1016      680        4        0
   Other mmap       39        8        0        0
      Unknown     3795     3612        0        0
        TOTAL    19414     7852     4324        0    29338    23676     5661

 App Summary
                       Pss(KB)
                        ------
           Java Heap:     3100
         Native Heap:        0
                Code:     4660
               Stack:      260
            Graphics:        0
       Private Other:     4156
              System:     7238

               TOTAL:    19414      TOTAL SWAP (KB):        0

 Objects
               Views:       18         ViewRootImpl:        1
         AppContexts:        3           Activities:        1
              Assets:        2        AssetManagers:        2
       Local Binders:        9        Proxy Binders:       13
       Parcel memory:        3         Parcel count:       12
    Death Recipients:        0      OpenSSL Sockets:        0

 SQL
         MEMORY_USED:        0
  PAGECACHE_OVERFLOW:        0          MALLOC_SIZE:        0
```
## 性能（ the time taken to execute tasks）
```
+-------------+-------------------+----------------------+---------------------------+---------------+
|             |    info           |    tools             |  fix                      |  extension    |
+----------------------------------------------------------------------------------------------------+
|  memory     |                   |                      |                           |  keep alive   |
|             | gc time>frame rate|     profiler cpu     |  ResourceCanary           |               |
|             | leaks             |                      |  Bitmap&Activity          |               |
|             | Memory Churn      |    dumpsys meminfo   |  APKsize                  |    +----------+
+-----------------------------+---+-------------------------------------------------------+ network& |
|  battery    | wakelock time |                          |   Doze /standby           |    | secure   |
|             | gps time      |        battery-historian |   jobscheduler API        |    +----------+
|             | network time  |                          |AlarmManager、Syncs Adapter|+              |
+----------------------------------------------------------------------------------------------------+
|             |   vsync       |                          |                           |               |
|  draw       |               |                          |  canvas.quickreject()     |               |
|  (cpu)      |Refresh Rate/  +---------+----------------+                           |               |
|             |frame Rate     |         | gpu overdraw   |  canvas.cliprect()        | multiThread&  |
|             |               |on-device| gpu render     |  hierachy viewer          | mainThread    |
|             |               |  tools  | gup view update|                           |               |
|             | Activity      |         |                |                           | Webview       |
|             | LauchTime     +---------+----------------+                           |               |
|             | (am start)    |     profiler cpu/gpu     |                           |               |
|             |reportFullyDrawn|                         |                           |               |
+-------------+---------------+--------------------------+---------------------------+---------------+
|   secure    |    debug      |   apktool                |                           | drizzleDumper |  
|             |   https       |   smali                  |                           |               |
|             |               |   (reverse engineering)  |                           |   FDex2       |
+-------------+---------------+--------------------------+---------------------------+---------------+
+-------------+-----------------------+
|             network                 |
+-------------+-----------------------+
|   fast      |  compress data/charge |
+-------------------------------------+
| <60ms GOOD  | compress image        |
|             | serialize data        |
| <200ms OK   |                       |
|             | cache                 |
| other BAD   | jms                   |
+-------------+-----------------------+


[smali](https://blog.csdn.net/linchaolong/article/details/51146492)
```
[Android官网](https://developer.android.com/topic/performance/)
[RelativeLayout的性能损耗](https://zhuanlan.zhihu.com/p/52386900?utm_source=androidweekly.io&utm_medium=website)
[battery-history](https://www.cnblogs.com/jytian/p/5647798.html,https://yeasy.gitbooks.io/docker_practice/install/ubuntu.html)

[启动时间](https://developer.android.com/topic/performance/vitals/launch-time)

[Android 内存泄漏](https://android.jlelse.eu/9-ways-to-avoid-memory-leaks-in-android-b6d81648e35e)

[冷启动](开始记录跟踪数据的位置调用 Debug.startMethodTracing()，要停止跟踪的位置请调用 Debug.stopMethodTracing())
```
+-----------------------------------+
| memory leaks                      |
+-----------------------------------+
| unrelease                         |
+-----------------------------------+
| static Fields                     |
| (View,Context)                    |
|                                   |
| Inner Classes                     |
| That Reference                    |
| Outer Classes/LongLifecycle       |
| (Async Handler)                   |
| (WeakReference view)              |
|                                   |
| ThreadLocals                      |
|                                   |
| Unclosed Resources                |
|(unregisterReceiver)               |
|                                   |
+-----------------------------------+



```
>《Android开发艺术探索》
方法：布局，绘制，内存泄漏，响应速度，Listview及Bitmap，线程优化
- 渲染速度
  [https://developer.android.com/studio/profile/inspect-gpu-rendering](https://developer.android.com/studio/profile/inspect-gpu-rendering)
    1. GPU呈现模式(Swap Buffers,Command Issue,Draw,Sync&Upload,Measure&LayoutAnimation,Input Handling,Misc/Vsync Delay)
```
+--------------------+---------------------------------------------+
|  Swap Buffers      |  too much work on the GPU                   |
+------------------------------------------------------------------+
|  Command Issue     |  renderer issuing commands to OpenGL        |
|                    |  to draw and redraw display lists           |
+------------------------------------------------------------------+
|  Sync & Upload     |  take to upload bitmap information          |
|                    |  to the GPU                                 |
+------------------------------------------------------------------+
|  Draw              |   create and update the view's display lists|
+------------------------------------------------------------------+
|  Measure /         |  spent on onLayout and onMeasure callbacks  |
|  Layout            |  in the ^iew hierarchy                      |
+------------------------------------------------------------------+
|  Animation         |  evaluate all the animators that were       |
|                    |  running that frame                         |
+------------------------------------------------------------------+
|  Input Handling    |  spent executing code                       |
|                    |  inside of an input e^ent callback          |
+------------------------------------------------------------------+
|  Misc Time /       |  executing operations in                    |
|  VSync Delay       |  between two consecuti^e frames.            |
+--------------------+---------------------------------------------+

```
    2. 显示CPU使用情况(查看后台运行)，耗电
    3. 绘制优化(profiler) CPU/Memory/Network
控件边界图，绘制条状图
```
+---------+--------+-------------------+---------+--------+------+------+--------+--------+-------+--------+------+-------+--------+
|         |        | Wall clock time/  |         |        |      |      |        |        |       |        |      |       |        |
|         |        |  Thread time      |         |        |      |      |        |        |       |        |      |       |        |
| Call    | Flame  |---------+---------|         |  app   | image|zygote|        |        |       |        |      |       |        |
| Chart   | Chart  |Top down |Bottom up|         |--------+------+------|        |        |       |        |      |       |        |
|         |        |         |         |forece gc|      Dump javaHeap   |        |        |       |        |      |       |        |
+---------+--------+---------+------------------------------------------+        |        |       |        |      |       |        |
|             Threads                  | Image   | Zygote | app  |  JNI |        |        |       |        |      |       |        |
|                                      | Heap    | Heap   | Heap |  Heap| timing |        |       |        |      |       |        |
|                                      |         |        |      |      |        |        |       |        |      |       |        |
+---------+--------+---------+-------------------+--------+------+-----------------------------------------------------------------+
| sample  | Trac   | Sample  |  Trac   | Allocation                     |        |        |       |        |      |       |        |
| java    | java   | C/C++   |  System |  Java/Native/Graphic           |overview|Responce|Request|CallStak| CPU  |Network|Location|
| Method  | Method | Function|  Calls  |     /Stack/Code/other          |        |        |       |        |      |       |        |
+---------+--------+-------------------+--------------------------------+--------+--------+-------+--------+------+-------+--------+
|                                                               Activity Lifecyle                                                  |
+--------------------------------------+--------------------------------+----------------------------------+-----------------------+
|          recod CPU                   |          Memory                |               network            |        Energy         |
+--------------------------------------+--------------------------------+----------------------------------+-----------------------+


Flame chart:横轴不再表示时间轴，相反，它表示每个方法执行的相对时间

[Flame chart](https://blog.csdn.net/sinat_20059415/article/details/80584621)
```
    1. **Studio Inspect Code** Android-Lint-Performace 代码Review不必要不加载（include merge, viewstub），ConstaintLayout
    2. LayoutInspector 布局优化（include merge, viewstub）
    3. 动画硬件加速，勿滥用
    
    尽量用Drawable  
    1. ListView/RecycleView及Bitmap优化
    2. 线程优化 

 
#### 包大小
  [包大小](https://mp.weixin.qq.com/s/_gnT2kjqpfMFs0kqAg4Qig?utm_source=androidweekly.io&utm_medium=website)


#### 可拓展性/异步/多线程（Scalability：the number of tasks a system can execute at the same time.）

```shell
# pidof  system_server
1956

# cat /proc/1956/limits

```
#### 可维护性 - 架构之模块化（插件化及组件化）
插件化（反射；接口；HOOK IActivityManager/Instrumentation+动态代理）
Activity校验，生命周期，Service优先级，资源访问，so插件化
- Dynamic-loader-apk
  [非开放sdk api](https://blog.csdn.net/yun_simon/article/details/81985331)
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
### 3.3 开源框架
#### OkHttp2

#### Retrofit

#### RPC

#### EventBus
反射与注解 
#### ARouter
控制反转和面向切面



## NDK

```md
GCC 就是把内核的源代码输出成二进制代码而已。生成的二进制代码不存在 GCC 的内容。GCC 只是根据程序源代码计算出来二进制代码。新 GCC ，可能会有新的语法检查，导致旧版本的内核无法符合“新规范”而报错，有的时候新 GCC 也会引入新的编译参数，新内核用新的参数，会导致旧的 GCC 无法识别对应的参数来进行编译。

[编译linux内核所用的gcc版本？ - jiangtao9999的回答 - 知乎](https://www.zhihu.com/question/58955848/answer/305063368)
```

```md
    1.预处理是解决一些宏定义的替换等工作，为编译做准备,对应的gcc操作为：gcc -E xx.c -o xx.i(xx为源文件名)。
    2.编译是将源码编译为汇编语言的过程，对应的gcc操作为:gcc -S xx.i -o xx.s。由xx.i 产生xx.s文件。
    3.汇编是将汇编代码的文件汇编为机器语言的过程，对应的gcc操作为：gcc -c xx.s -o xx.o
    4.链接是将目标文件链接为一个整的可执行文件的过程，对应的gcc操作为 gcc xx.o -o xx(xx成为可执行,运行时候可以用 "./xx" 的方式运行)。

    [程序员的自我修养--链接、装载与库](https://www.cnblogs.com/zhouat/p/3485483.html)
```

[Android-MD doc](https://beijing.source.codeaurora.org/quic/la/platform/ndk/docs/ANDROID-MK.html)

```
+------------------------------------------------------------+
|                 build-binary.mk                            |
|                                                            |
|                 setup-toolchain.mk                         |
|                 setup-abi.mk                               |
|                                                            |
|                  setup-app.mk                              |
|                  build-all.mk                              |
|                  init.mk                                   |
|                  build-local.mk                            |
|                                                            |
+------------------------------------------------------------+
|                                                            |
|   Module-description variables                             |
|                                                            |
|   NDK-provided variables    NDK-provided function macros   |
|                                                            |
+------------------------------------------------------------+
|                                                            |
|              ndk-build                                     |
+------------------------------------------------------------+
|                     NDK                                    |
+------------------------------------------------------------+
|                 GNU Make                                   |
+------------------------------------------------------------+



```



## 权限

[Android 临时访问权限](https://www.jianshu.com/p/f15f956763c1)