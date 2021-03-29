****## Android应用体系
> 软件是计算机系统中与硬件相互依存的另一部分，它是包括程序，数据及其相关文档的完整集合 《软件工程概论》
环境 Android 系统
阅读应用流程（以APK文档为主线，程序Main方法为入口）
[启动流程](https://blog.csdn.net/qq_30993595/article/details/82714409#_2)

![](https://developer.android.google.cn/guide/platform/images/android-stack_2x.png?hl=zh-cn)
```diagram
+---------+--------------------------------------------------------------------------------------------------+
| AppS    |   Browser.apk  Gallery.apk  Launcher3.apk SystemUI(RecentsActivity)   Home  Contacts.apk  Phone  |
|         |   PackageInstaller.apk   PackageInstaller.apk                                                    |
+---------+--------------------------------------------------------------------------------------------------+
| cmds    |   install  am  wm   pm    appops                                                                 |
+---------+----------------+------------------------------------+----------------------+---------------------+
|         |   util         |content/provider/database/preference|         os           |   app               |
|         |   (DSA)        | (accessing and publishing data)    |(IPC,message passing) |(app model,UI Control)|
|         +--------------------------------------------------------------------------------------------------+
|         |  SparseArray   |      BroadcastReceiver             |  Binder              |  Application        |
|         |  ArrayMap      |      ContentProvider               |  MemoryFile          |  Activity    Dialog |
|         |  LruCache      |         Context                    |  AsyncTask           |  Fragment           |
|         |                |     ClipboardManager               |                      |  AlarmManager       |
|         |                |         Intent                     |  Handler             |  Notification       |
|         |                |      AssetManager/Resource         |                      |  JobScheduler       |
| Android |                |      SharedPreferences             |  Environment         |                     |
| SDK     +-----------------------------------------------+-----------+------------+---+---------------------+
|         | view/gesture         | widget/webkit/appwidget|graphics/opengl/media/hardware|       animation   |     text/sax net/nfc/bluetooth dalvik
|         |(window,interaction)  |      (UI elements )    |(drawing2Screen directly)    |(property animation)|
|         +--------------------------------------------------------------------------------------------------+
|         |Window  View  KeyEvent| TextView  ImageView    |     Drawable   Bitmap       |   TypeEvaluator    |
|         |GestureDetector       | EditText  Toast        |     Camera     Canvas       |                    |
|         |InputMethodManager    | RecyclerView           |     ColorFilter Point       |                    |
|         |Animation             | ViewPager              |     Rect                    |                    |
+--------------------------------+------------------------+-----------------------------+--------------------+
| App     |   AMS             WMS    View System        content provider   XMPP                              |//type: BootStrapService,
|Framework|   PMS             NMS    ResourceManager    TelephonyManager   LMS                               |//coreServices,OtherService
+--------------------------------------------------------------------------------------+---------------------+
|         |   Surface Manager    Media       Webkit                        | Android      +----------------+ +
|         |   OpenGL+ES (3d)                 SQLite                        | Runtime      | dalvik vm      | |
|Libraries|   SGL(Skia 2d)                   SSL/TLS                       |              +----------------+ |
|         |   FreeType                       libc(bionic)                  +---------------------------------+
+------------------------------------------------------------------------------------------------------------+
|         |   Bind (IPC) Driver   Display Driver               USB Driver     Power Management               |
|  Linux  |                       (FrameBuffer[VESA Standard])                                               |//[Framebuffer](https://www.cnblogs.com/sky-heaven/p/8934568.html)
|         |   Bluetooth Driver    Camera Driver                Flash Driver                                  |
|  kernel |                         (V4L2)                                                                   |
|         |   WIFI Driver         Audio Driver                 KeyPad Driver                                 |
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
[](https://en.wikipedia.org/wiki/Android_version_history)
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
[作者：硬刚平底锅  ](https://blog.csdn.net/qq_30993595/article/details/82714409)


[jni方法注册方式](https://www.jianshu.com/p/1d6ec5068d05) 

c++的智能指针有很多实现方式，有auto_ptr ,  unique_ptr , shared_ptr 三种， Android 中封装了sp<> 强指针，wp<>弱指针的操作

在Android中，RefBase结合sp（strong pointer）和wp（weak pointer），实现了一套通过引用计数的方法来控制对象生命周期的机制。

### Dispaly 系统

```java
                           OpenGL/ES        Rasterization
                           convert to
                           polygons or textures
+---------+     +---------+         +---------+     +--------+
|  xml/UI +----->  CPU    +-------->+  GPU    +---->+ SCREEN |
+---------+     +---------+         +---------+     +--------+


```

```java

显示数据类型
1. UI界面的显示，这部分通常数据类型为RGB格式
2. 大块YUV数据的应用，如camera的preview、视频的播放等。该应用只针对特定的应用程序，开启时通过overlay直接把大块的YUV数据送到kernel显示。
3. 和第一种类似，但在显示之前需要对数据进行2D、3D的处理（使用OpenGL、OpenVG、SVG、SKIA）。一般在Game、地图、Flash等应用中会用到。

```
大致分为构建，绘制，渲染，显示
```java
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
```java
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

 
### 关键类与对象


 **Object**
public class Object {

    private transient Class<?> shadow$_klass_;
    private transient int shadow$_monitor_;
}




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





```java
public class ActivityStackSupervisor extends ConfigurationContainer implements DisplayListener,
        RecentTasks.Callbacks {
    private final SparseArray<ActivityDisplay> mActivityDisplays = new SparseArray<>();
        
}

```
### SystemServer - 窗口视图（ 测量，布局及绘制,事件，动画，适配）wms

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


## 应用层
Zygote 子线程
```
ps -t | grep -E "NAME| <zygote ps id> "
```



## Android 开发模式

### 刷机方法
厂刷，线刷，卡刷，软刷

 
9008是终极救砖，一定可以的
adb reboot edl

### 唯一ID

1. Build.SERIAL：Android 9.0

2. IMEI / DeviceID： Android 10.0 Q版本中，禁止获取
```java
TelephonyManager tm = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
deviceId = tm.getDeviceId(); 
```
```java
Object defaultTlpMgrInstance = Class.forName("miui.telephony.TelephonyManager").getMethod("getDefault").invoke(null);
Object imeiObj = defaultTlpMgrInstance.getClass().getMethod("getMiuiDeviceId").invoke(defaultTlpMgrInstance);
```

```java
private String getIMEI(int slotId){
    try {
        Class clazz = Class.forName("android.os.SystemProperties");
        Method method = clazz.getMethod("get", String.class, String.class);
        String imei = (String) method.invoke(null, "ril.gsm.imei", "");
        if(!TextUtils.isEmpty(imei)){
            String[] split = imei.split(",");
            if(split.length > slotId){
                imei = split[slotId];
            }
            Log.d(TAG,"getIMEI imei: "+ imei);
            return imei;
        }
    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | ClassNotFoundException e) {
        e.printStackTrace();
        Log.w(TAG,"getIMEI error : "+ e.getMessage());
    }
    return "";
}
```


4. Android ID：手机恢复出厂设置或root了手机，会重置。 
```java
Settings.System.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID)
```
5. Mac地址：Android 9 P版本中，地址会随机变化。没有wifi的时候，我们是无法获得数据的

6. 蓝牙MAC地址。

```java
BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
String btMAC = mBluetoothAdapter.getAddress(); 
```

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
 
#### RPC
 


## 源码
https://android.stackexchange.com/questions/51651/which-android-runs-which-linux-kernel

Android Version    |API Level  |Linux Kernel in AOSP
----------------------------------------------------
1.5   Cupcake      |3          |2.6.27
1.6   Donut        |4          |2.6.29
2.0/1 Eclair       |5-7        |2.6.29
2.2.x Froyo        |8          |2.6.32
2.3.x Gingerbread  |9, 10      |2.6.35
3.x.x Honeycomb    |11-13      |2.6.36
4.0.x Ice Cream San|14, 15     |3.0.1
4.1.x Jelly Bean   |16         |3.0.31
4.2.x Jelly Bean   |17         |3.4.0
4.3   Jelly Bean   |18         |3.4.39
4.4   Kit Kat      |19, 20     |3.10
5.x   Lollipop     |21, 22     |3.16.1
6.0   Marshmallow  |23         |3.18.10
7.0   Nougat       |24         |4.4.1
7.1   Nougat       |25         |4.4.1 (To be updated)



[1798个项目（2019-12-11统计）](https://source.codeaurora.cn/quic/la)
 [android studio profilers源码](https://github.com/JetBrains/android)




[libcore](https://mirrors.ustc.edu.cn/aosp/platform/libcore.git )
    FileSystem native代码，正则表达式（libcore/luni/src/main/native/java_util_regex_Pattern.cpp,java_util_regex_Matcher.cpp）[正则实现](https://github.com/unicode-org/icu.git)
[art](https://source.codeaurora.cn/quic/la/platform/art/)
object.cc IdentityHashCode()
[程序入口包（java_lang_Object）](http://source.codeaurora.cn/quic/la/platform/dalvik/)
含有61个内置platform/system
[platform/system/core](https://source.codeaurora.cn/quic/la/platform/system/core)
adb，logcat	，fastboot

含有61个内置platform/frameworks
[应用入口 Binder及AIDL（Activity,Brocast,Content provider,Service）platform/frameworks/base](https://source.codeaurora.cn/quic/la/platform/frameworks/base/)
framework源码，开发必备

```java
            +---------------------------------------------------------------------------------------------------------------------------------------+
            |     Activity                                                                                                                          |
            |         startActivity(intent)                                                                                                         |
            |         mMainThread                                                                                                                   |
            |    Instrumentation                                                                                                                    |
            |       execStartActivity()                                                                                                             |
            |                                                ActivityManagerNative                                                                  |
            |                                                          :IActivityManager            ActivityManagerService:ActivityManagerNative    |
            |                                                   getDefault():IActivityManager            startActivity()                            |
            |                                                                                                                                       |
            |                                                                                            startActivityLocked()                      |
            |                                                                                                                                       |
            |                                                                                            resumeTopActivityLocked()                  |
            |                                                                                                                                       |
            |       ApplicationThread                        ApplicationThreadProxy                      startPausingLocked()                       |
            |           :ApplicationThreadNative                     :IApplicationThread                                                            |
            |                                                   scheduleLaunchActivity()                 startSpecificActivityLocked()              |
            |         scheduleLaunchActivity()                  mRemote:IBinder                                                                     |
            |                                                                                            realStartActivityLocked()                  |
            |                                                                                                                                       |
            |                                                                                            mProcessNames:ProcessMap<ProcessRecord>    |
            |                                                                                                                                       |
            |                                                                                                                                       |
            |                                                                                       ProcessRecord                                   |
            |                                                                                         thread:IApplicationThread                     |
            +---------------------------------------------------------------------------------------------------------------------------------------+
```


[agl（opengl\libagl，opengl es实现）+egl（实现平台无关） ](http://mirrors.ustc.edu.cn/aosp/platform/frameworks/native.git)
[OpenGL ES：Android平台EGL环境](https://www.jianshu.com/p/d5ff1ff4ee2a)
[OpenGL ES教程](https://zhuanlan.zhihu.com/p/56031071)https://www.cnblogs.com/kiffa/archive/2013/02/21/2921123.html
[SurfaceFlinger整合layer的过程](https://blog.csdn.net/prike/article/details/72810027)
```
            +----------------------------------------------------------------------------------------------+
            | [surfaceflinger]                                                                             |
            |                                                                                              |
            |       SurfaceFlinger                                                                         |
            |               instantiate()                     LayerBaseClient:LayerBase                    |
            |               createSurface() :sp<ISurface>                                                  |
            |               threadLoop()                                                                   |
            |               waitForEvent()                    SurfaceComposerClient                        |
            |               handlePageFlip()                       createSurface():sp<Surface>             |
            |               handleRepaint()                        mClient:sp<ISurfaceFlingerClient>       |
            |               unlockClients()                                                                |
            |               postFramebuffer()                                                              |
            +----------------------------------------------------------------------------------------------+
            |    DisplayHardware                                                                           |
            |       init()                                                                                 |
            |       mDisplaySurface:sp<EGLDisplaySurface>                                                  |
            |       mDisplay:EGLDisplay                                                                    |
            |       mSurface:EGLSurface                                                                    |
            |       mContext:EGLContext                                                                    |
            |       flip()                                                                                 |
            +----------------------------------------------------------------------------------------------+
            | [ui]                                            [libagl]                                     |
            |       EGLDisplaySurface                              eglSwapBuffers()                        |
            |              mapFrameBuffer():status_t                                                       |
            |              // "/dev/graphics/fb%u"                                                         |
            |                                                                                              |
            |              // "/dev/fb%u"                                                                  |
            |                                                                                              |
            +----------------------------------------------------------------------------------------------+
            |                      framebuffer                                                             |
            +----------------------------------------------------------------------------------------------+               
```

含有118个内置platform/packages/apps
[platform/packages/apps/PackageInstaller](https://source.codeaurora.cn/quic/la/platform/packages/apps/PackageInstaller)
“点击通知”需要访问文件权限intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);





[浏览器模块](https://source.codeaurora.cn/quic/la/platform/external/chromium_org)
jni_android.cc


[浏览器源码](https://cs.chromium.org/chromium/)
 

### [platform/packages/providers 系统交互providers]()

含有18个内置platform/packages/providers
[platform/packages/providers/MediaProvider](https://source.codeaurora.cn/quic/la/platform/packages/providers/MediaProvider)
```java
 添加到相册，需要uri.getScheme().equals("file")
public class MediaScannerReceiver extends BroadcastReceiver
{
    private final static String TAG = "MediaScannerReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Uri uri = intent.getData();
        String externalStoragePath = Environment.getExternalStorageDirectory().getPath();

        if (action.equals(Intent.ACTION_BOOT_COMPLETED)) {
            // scan internal storage
            scan(context, MediaProvider.INTERNAL_VOLUME);
            
            // scan external storage if it is mounted
            String state = Environment.getExternalStorageState();
            if (Environment.MEDIA_MOUNTED.equals(state) || 
                    Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
               scan(context, MediaProvider.EXTERNAL_VOLUME);
            }
        } else {
            if (uri.getScheme().equals("file")) {
                // handle intents related to external storage
                String path = uri.getPath();
                if (action.equals(Intent.ACTION_MEDIA_MOUNTED) && 
                        externalStoragePath.equals(path)) {
                    scan(context, MediaProvider.EXTERNAL_VOLUME);
                } else if (action.equals(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE) &&
                        path != null && path.startsWith(externalStoragePath + "/")) {
                    scanFile(context, path);
                }
            }
        }
    }
```
### [Monkey](https://source.codeaurora.cn/quic/la/platform/development/)
包含项目开发中apps,cmds（Monkey），模拟器，ndk，sample，
tools（apkbuilder,ddms，draw9path,eclipse,hierarchyviewer,ninepatch,screenshots,sdkstats）

### test
              // adb shell am instrument -w -r    -e debug false
              // -e class 'edu.ptu.test.AndroidClassLoaderTest#testLooper'
              // edu.ptu.test.test/androidx.test.runner.AndroidJUnitRunner
+------------------------------------------------------------------------------------------------+
|                               AndroidJUnitRunner :Instrumentation                              |
|                                                                                                |
|                                                                                                |
|                               Instrumentation                                                  |
|                                  mRunner:InstrumentationThread                                 |
|                                  start()                                                       |
|                                                                                                |
|                                                                                                |
|                                                                                                |
|                                                                                                |
+------------------------------------------------------------------------------------------------+

### [APKsigner源码签名](http://source.codeaurora.cn/quic/la/platform/tools/apksig)
```
    +-------------------------------------------------------------------+
    |                          ApkSignerTool                            |
    |                                                                   |
    |                         OptionsParser                             |
    |                                                                   |
    |                       ApkSigner     SignerParams                  |
    |                                                                   |
    +-------------------------------------------------------------------+

```

### [zipalign ](http://source.codeaurora.cn/quic/la/platform/build)
### [support 各类控件](https://source.codeaurora.cn/quic/la/platform/frameworks/support/)

```java
  001_v4_FragmentAct cba2e2c881e8e16ea5025b564c94320174d65f01 First checkin!
            //FragmentActivity 添加声明周期
            +-------------------------------------------------------------------------------------------------+-----------------------+
            | FragmentActivity                        FragmentManagerImpl             Fragment                |                       |
            |      mFragments:FragmentActivity          *mCurState                        mState              |                       |
            |      onCreate()                           mBackStack:List<BackStackRecord>  mSavedViewState     |   INITIALIZING = 0    |
            |                                            mActive:List<Fragment>           mTarget             |                       |
            |                                            mActivity                        mActivity           |                       |
            |                                            attachActivity()                 mFragmentManager    |                       |
            |                                            dispatchCreate()                 mHidden             |  ->CREATED = 1        |
            |                                           *moveToState(f,newState,transit,transitionStyle)      |                       |
            |                                                                            onAttach()           |                       |
            |      onAttachFragment()                                                    onCreate()           |                       |
            |                                                                            onCreateView()       |                       |
            |                                                                                                 |                       |
            |     *onCreateView()                        findFragmentByTag()             instantiate()        |                       |
            |                                            findFragmentById()              addFragment()        |                       |
            |      onPostCreate()                        dispatchActivityCreated()                            | ->ACTIVITY_CREATED = 2|
            |      onStart()                             noteStateNotSaved()           dispatchStart()        | ->STARTED = 3         |
            |                                            execPendingActions()                                 |                       |
            |      onPostResume()                        dispatchResume()                                     | ->RESUMED = 4         |
            |                                            execPendingActions()                                 |      CREATED = 1      |
            |      onResume()                            execPendingActions()                                 |                       |
            |                                            outState                                             |                       |
            |      onPause()                             dispatchPause()                 onPause()              ->STARTED = 3         |
            |      onStop()                              dispatchStop()                                         ->ACTIVITY_CREATED = 2|
            |      onDestroy()                           dispatchDestroy()               onDestroy()            ->INITIALIZING = 0    |
            |                                                                            onDetach()                                   |
            |      onSaveInstanceState()                 saveAllState():Parcelable                                                    |
            |      dispatchCreateOptionsMenu()           newMenus:List<Fragment>         mAdded                                       |
            |                                            dispatchCreateOptionsMenu()     onCreateOptionsMenu()                        |
            |      onMenuItemSelected()                                                                                               |
            |                                            mBackStack                                                                   |
            |      onKeyDown()                           mPendingActions                                                              |
            |                                            popBackStackImmediate()                                                      |
            |      onMenuItemSelected()                  dispatchOptionsItemSelected()  mHidden                                       |
            |                                            dispatchContextItemSelected()  mHasMenu                                      |
            |                                                                           onOptionsItemSelected()                       |
            |      onPanelClosed()                       dispatchOptionsMenuClosed()    onOptionsMenuClosed()                         |
            |                                            dispatchLowMemory()                                  |                       |
            |      onLowMemory()                         mAdded:List<Fragment>          onLowMemory()         |                       |
            |                                            dispatchLowMemory()                                  |                       |
            +-------------------------------------------------------------------------------------------------------------------------+
            //FragmentTransaction添加Fragment
            +-------------------------------------------------------------------------------------------------------------------------+
            | FragmentManagerImpl                        BackStackRecord:FragmentTransaction  Op                         Fragment     |
            |    mCurState                                               ,Runnable              next       enterAnim      mCalled     |
            |    mPendingActions:List<Action>              mManager:FragmentManagerImpl         prev       exitAnim       mRetaining  |
            |    beginTransaction():FragmentTransaction    mHead:Op                             cmd                       mView       |
            |    allocBackStackIndex()                     mTail:Op                             fragment                              |
            |    enqueueAction()                                                                removed:List<Fragment>                |
            |                                                                                                                         |
            |    mAdded:List<Fragment>                                                          OP_NULL = 0;                          |
            |    addFragment()                             add():FragmentTransaction            OP_ADD = 1;               mAdded      |
            |    removeFragment()                          replace():FragmentTransaction        OP_REPLACE = 2;                       |
            |    hideFragment()                            remove():FragmentTransaction         OP_REMOVE = 3;            mRemoving   |
            |    showFragment()                            hide():FragmentTransaction           OP_HIDE = 4;              mHidden     |
            |                                              show():FragmentTransaction           OP_SHOW = 5;                          |
            |    mBackStack:                                                                                                          |
            |    List<BackStackRecord>                     commit()                                                                   |
            |                                              commitInternal()                                                           |
            |                                                                                                                         |
            |                                              run()                                                                      |
            +-------------------------------------------------------------------------------------------------------------------------+


            LoaderManager
            +-------------------------------------------------+------------------------------+----------------------------------------+
            |                                                 |                              |                                        |
            |                                                 |                              |                                        |
            |   FragmentActivity                              | LoaderCallbacks              |CursorAdapter：Filterable,               |
            |                                                 |   onCreateLoader():Loader<D> |              CursorFilterClient        |
            |     mAllLoaderManagers:Array<LoaderManagerImpl> |   onLoadFinished()           |    mCursorFilter:CursorFilter          |
            |     getSupportLoaderManager():LoaderManager     |   onLoaderReset()            |                                        |
            |                                                 |                              |                                        |
            |                                                 |                              |CursorFilter:Filter                     |
            |  LoaderManagerImpl:LoaderManager                |                              |   mClient:CursorFilterClient           |
            |    updateActivity()                             |                              |   performFiltering(constraint)         |
            |    initLoader(id,args,callback):Loader<D>       |                              |   publishResults(constraint,results)   |
            |                                                 |                              |                                        |
            |                                                 |                              |                                        |
            |  AsyncTaskLoader<D>:Loader<D>                   |                              |                                        |
            +-------------------------------------------------+------------------------------+----------------------------------------+

* 002_FragmentPager  eedc67283a5a49dce86c625e54596dfdea9465a7 First submit of FragmentPager class.
  003_v4demo         c644c91b91b83a6b400a57b02671f4ef7b7a810b Extract support lib samples out of ApiDemos and in to their own app.
  004_ViewPager      ea2c91b0198855073983b4a8437aa71cbd83872f New super-spiffier ViewPager class.
            +-------------------------------------------------------------------------------------------------------------------------+
            |                                    ViewPager                                                                            |
            |                                        populate()                                                                       |
            |                                                                                                                         |
            |                                                                                                                         |
            +-------------------------------------------------------------------------------------------------------------------------+
            |                                                                                                                         |
            |                                       PagerAdapter                                                                      |
            |                                           getCount()                                                                    |
            |                                           startUpdate()                                                                 |
            |                                           instantiateItem(viewId,position)                                              |
            |                                           destroyItem(position, object)                                                 |
            |                                           finishUpdate()                                                                |
            |                                           isViewFromObject(view, object)                                                |
            |                                           saveState():Parcelable                                                        |
            |                                           restoreState(Parcelable state)                                                |
            |                                                                                                                         |
            |                       FragmentPagerAdapter:PagerAdapter     FragmentStatePagerAdapter:PagerAdapter                      |
            |                                                                   mFragments:List<Fragment>                             |
            |                                                                                                                         |
            +-------------------------------------------------------------------------------------------------------------------------+

  005_v13demo        4557342eeab7018e2edece1d3265819737d078fc New demos for the v13 support library.
* 006_localBroadcast 3083afddf1baabb57e801d2aa7d9c59e8b1e1c19 Add LocalBroadcastManager.
  007_EdgeEffect     560114f591be31d0fb73c26a1ee1cc0a15184aba Make ViewPager aware of EdgeEffect on ICS devices.
  008_volley         3713094c56d25e25df2a508dbee4aea869ffdea1 Volley: an HTTP RPC library.

            +-------------------------------------------------------------------------------------------------------------------------+
            |                              RequestQueue                                                                               |
            |                                   mNetworkQueue         mDispatchers:NetworkDispatcher                                  |
            |                                   mCacheQueue           mDelivery:ExecutorDelivery                                      |
            |                                   mWaitingRequests      mCacheDispatcher:CacheDispatcher                                |
            |                                   mCache                                                                                |
            |                                                                                                                         |
            |                                   start()                                                                               |
            |                                   add(request)                                                                          |
            +-----------------------------------------------------------+-------------------------------------------------------------+
            | NetworkDispatcher:Thread       NetworkDispatcher:Thread   |     Request                                                 |
            |        mQueue:BlockingQueue                 mQueue        |         parseNetworkResponse(response:NetworkResponse )     |
            |        networkQueue                         mNetwork      |         deliverResponse()                                   |
            |        cache                                mCache        |                                                             |
            |        mDelivery                            mDelivery     |         deliverError()                                      |
            |        mNetwork:BaseNetwork                               |                                                             |
            |                                                           |                                                             |
            +-------------------------------------------------------------------------------------------------------------------------+
            |  BasicNetwork:Network                                     |     ImageRequest    JsonRequest    StringRequest            |
            |      performRequest():NetworkResponse                     |                                                             |
            +-----------------------------------------------------------+-------------------------------------------------------------+
  009_SearchView     fe32563fd610767a2d3eea8dbd96e6bae87739d5 Fixing a comment in SearchViewCompat
  010_LongSparseArray 97b687d492c63a6a016f420835d5457d8b4b55b1 Add support lib LongSparseArray.
            +-------------------------------------------------------------------------------------------------------------------------+
            |                                  LongSparseArray                                                                        |
            |                            mGarbage                                                                                     |
            |                            mKeys               gc()                                                                     |
            |                            mValues             binarySearch()                                                           |
            |                            mSize                                                                                        |
            +-------------------------------------------------------------------------------------------------------------------------+
* 011_gridlayout      e1feb53bd8abfa46613fdd0abcf7a015c7e706c1 Add GridLayout as a support project library.
            +-------------------------------------------------------------------------------------------------------------------------+
            |  GridLayout                                                                                                             |
            |     horizontalAxis:Axis                                                                                                 |
            |     verticalAxis:Axis                                                                                                   |
            |     onlayouot()                                                                                                         |
            +----------------------------------------+--------------------------------------------------------------------------------+
            |   Axis                                 |               LayoutParams:MarginLayoutParams                                  |
            |     layout(size)                       |                     rowSpec:Spec                                               |
            |     getGroupBounds():Bounds            |                     columnSpec:Spec                                            |
            |     horizontal(orientation)            +--------------------------------------------------------------------------------+
            |     definedCount( rowCount/columnCount)|                     Spec                                +----------------------+
            |                                        |                       span:Interval                     | (layout_gravity)     |
            |                                        |                       alignment:Alignment               |                      |
            +--------------------------------------------------------------------------------------------------+----------------------+
            |   Bounds                               |          Interval                                         Alignment            |
            |    *getOffset()                        |             min:int(layout_column/row )                     *getGravityOffset()|
            |                                        |             max:int                                         *getSizeInCell()   |
            |                                        |                (min+layout_column/rowSpan)                                     |
            +----------------------------------------+--------------------------------------------------------------------------------+

  012_ByteArrayOS     c4cbfcb8d044cea99e2471ce5c401cd959b6cdfe Creates a pooled ByteArrayOutputStream
  013_Nofitication    f021758934b35e3b842c6799344531d7ea2969da Add v16 Notification APIs to NotificationCompat.
  014_AtomicFile      b87fe4a348db4e64876052619036232749e70d9f Support lib version of AtomicFile.
  015_SlidingPane     97341bdc5bea1d7bf777de65228039142d249f38 Add SlidingPaneLayout
  016_rs              98a281354fe06d1f970d0521c9a08d9eb0aa1a45 Initial pass at RS compatibility library.
  017_actionbar       bbbb8f39d1b1d1b317c5f9237f20fe6b1d9eb17f Initial work on ActionBar support library
  031_GestureDetecotr 1ce805e30800bf2852fa5421b7277a18e089ee31 Add GestureDetectorCompat
  032_ViewDragHelper  c56ba65d20be8742ff717907a3a2cd81dd0e5f3c Factor ViewDragHelper out from SlidingPaneLayou
  033_DrawerLayout    1d26501f0c8e9f3577f651938a03f6b3a1a672c7 Initial DrawerLayout implementation
            +-------------------------------------------------------------------------------------------------------------------------+
            |                DrawerLayout:ViewGroup                                                                                   |
            |                        onLayout()                                                                                       |
            |                        isContentView()                                                                                  |
            |                                                                                                                         |
            |                        mLeftDragger:ViewDragHelper                                                                      |
            |                        mRightDragger:ViewDragHelper                                                                     |
            |                        computeScroll()                                                                                  |
            |                        closeDrawer()                                                                                    |
            |        ViewDragHelper                                                                                                   |
            |           mCapturedView:View                             Callback                                                       |
            |           captureChildView()                                onViewDragStateChanged(state)                               |
            |           mCallback:Callback                                onViewPositionChanged()                                     |
            |                                                             onViewPositionChanged()                                     |
            |           mScroller:ScrollerCompat                                 ...                                                  |
            |           smoothSlideViewTo()                                                                                           |
            |           continueSettling()                                                                                            |
            |                                                                                                                         |
            +-------------------------------------------------------------------------------------------------------------------------++

  034_PopupMenu       dbfc21aa98c4a1092204854b99830a50557aa969 Add support version of PopupMenu to AppCompat
  034_multidex        acedbc72ca7c30a24d869f5067ed89ec4dead7c8 Add ADT project for android-support-multidex.
  035_recyclerview    009b4ef9d97e1cc237477e3284fc305bb1438cc9 Add RecylerView to the support library
  035_rv_Examample     f6c36bb9b61083b25397a6cff82ddbb102cacfbd Example activity for RecyclerView
  039_resourceAnno    75aea14c26565d3fde46c4ce410f5c384c42162c Add typedef enum and resource type annotations
  040_leanback        20c094c196271089a7119a965b6a99786ea9ed36 Initial import of leanback into support lib (4).
  041_rv_itemDecor    8e032f7070214b27589e80935e618315f6a41d3a Implement RecyclerView ItemDecorations
  042_listLM          7dad56243ebcde65d75d592dc802269a4d86c875 List Layout Manager
  043_rv_anim         33b18903168c177d65e3c2ef7398c1b2ca0c826f Add animations to RecyclerView
  044_palette         059178a8c7cc80848e5594a9287be91bd924831a New Palette support library
  045_rectShadow      767f1e75ad6e96042c6e4466c363b285a8bfda68 Merge "Use raw shadow size when deciding cardview bounds" into lmp-mr1-dev
  050_vectorDrawable  824b14618bca27526ec3de3a5ef28e3cb3c40c23 Setup the code structure for VectorDrawable's support lib.
  051_tinting         cd6e77607caba0b3b26163791a361938afb8b9c5 Improvements to AppCompat's tinting
  052_pathinterpolat  94213f7183ca8a0a91ee0cf25723e2791a078ae0 Create v4 PathInterpolatorCompat
  054_snackbar        b7f9224b1495db47eb8fd813b5912250e900770a Snackbar
  055_vd              4fcaa70c2362e58a3fb30d140f0a0eeda8e35b44 Add the support lib for VD and AVD
* 056_percentage      f9cabe2ad76a19d555b5b656d8167bdb167c9d03 Percentage based layouts.
                      过时，使用ConstraintLayout
  057_newpermission   36c328cb8d2b86ce99c9e7c7382478b2b496bdd3 Add support lib shims for new permissions APIs
  060_ripple          8c3f75732bc35cf683f9014816b26c77d9c8f4e8 Add workaround for ripple + alpha animation bug
 
```
[ConstraintLayout布局 where to find ConstraintLayout source](http://source.codeaurora.cn/quic/la/platform/frameworks/opt/sherpa)

```java
+-------------------------------------------------------------------------------------------------------------------------+
|                                                                                                                         |
|                ConstraintLayout                                                                                         |
|                     onLayout()                                                                                          |
|                     mConstrainedWidgets:List<ConstraintWidget>                                                          |
|                     mLayoutWidget                                                                                       |
|                     mConstrainedTargets:List<ConstraintWidget>                                                          |
|                     onFinishInflate()                                                                                   |
|                     updateHierarchy()                                   Guideline:View                                  |
+-------------------------------------------------------------------------------------------------------------------------+
|                ConstraintWidget:Solvable                                Guideline:ConstraintWidget                      |
|                         getCompanionWidget():View                                                                       |
|                         getLeft()                                                                                       |
|                         getTop()                                                                                        |
|                         getRight()                                      ConstraintWidgetContainer:ConstraintWidget      |
|                         getBottom()                                                                                     |
|                         connect()                                                                                       |
|                         getAnchor()                                                                                     |
|                         setHorizontalDimensionBehaviour()                                                               |
|                         setVerticalDimensionBehaviour()                                                                 |
|                                                                                                                         |
+-------------------------------------------------------------------------------------------------------------------------+

>两个控件联合，位于Relative居中，Relative宽度是WrapContent会出问题,部分视图不可见
+-----------------------+
|                       |
|   +-----+  +------+   |
|   |     |  |      |   |
|   | V1  |  | V2   |   |
|   |     | .|      |   |
|   |     |  |      |   |
|   +-----+  +------+   |
|                       |
+-----------------------+

```
### 编程源码
(从jvm源码看synchronized)[https://www.cnblogs.com/kundeg/p/8422557.html]


### 项目源码


#### 浏览器chromium


 

[rxjava.ReactiveX Dagger.Android.di (2.11-2.17)](..\问题优化与总结\知识体系-理论-OOAD.md)


[Doc: Jekyll]()

#### 服务器jetty,tomcat
