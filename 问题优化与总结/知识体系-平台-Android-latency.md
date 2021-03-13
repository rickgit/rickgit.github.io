## 精简
[square](https://github.com/square?q=android&type=&language=)

常见优化库
LeakCanary、Glide、Retrofit、OkHttp、RxJava、GreenDAO
[Studio Profiler](https://github.com/JetBrains/android/tree/master/profilers/src/com/android/tools/profilers/memory)

和内核模块类似：内存管理，文件存储，进程调度，进程间调度，网络接口


## 缓存篇

### Bitmp
简单工厂 Bitmap（无数据源）：wrapHardwareBuffer，createScaledBitmap/createBitmap 
        BitmapFactory（有数据源）：decodeFile（decodeResource/decodeResourceStream）/decodeStream，decodeByteArray，decodeFileDescriptor
### 编译，加载

#### 类加载机制，类加载器，双亲委派
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
#### 类加载问题
[pre-verify问题](https://www.jianshu.com/p/7217d61c513f)
QQ空间补丁
```
1. 阻止相关类打上Class_ispreverified标志
2. 动态更改BaseDexClassLoader间接引用的dexElements
```
[Redex](https://blog.csdn.net/tencent_bugly/article/details/53375240)

### 虚拟机与执行器

#### dalvik bytecode
```
dx --dex --output=Hello.dex Hello.class
```

#### NDK

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

[Android-MD doc](https://source.codeaurora.cn/quic/la/platform/ndk/docs/ANDROID-MK.html)

```java
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
 
###
⭐ ndk-build出问题，需要用 命令行 gradlew assemble，提示的信息更全
```
Android NDK: APP_STL gnustl_static is no longer supported. Please switch to either c++_static or c++_shared.

1. APP_STL  := gnustl_static 改为 APP_STL := c++_static；

2.删除NDK_TOOLCHAIN or NDK_TOOLCHAIN_VERSION；




found local symbol '__bss_start' 
查看：readelf -s *.so |grep "__bss_start"  
解决：Android.mk :：     APP_LDFLAGS := -fuse-ld=gold



not found file "...h"
查看：$(warning "the value of LOCAL_C_INCLUDES is$(LOCAL_C_INCLUDES)") 查看路径是否正确。
解决：在wls 下面，$(win)返回的仍然是1，导致路径都替换成window的“c：/。。/”，注释掉代码。


Operation not permitted
虽然是警告，但是可能导致文件不能拷贝。
使用sudo ndk-build

```


###  2 内存泄漏/内存抖动（Android Profiler- memory）
GC Root :
堆，方法区内存：static（对象，容器），final，
栈：ActivityThread的activitys，Handler使用WeakReference持有activity引用
本地方法栈：File，Cursor，WebView

#### GC
Reference
Lrucache,Bitmap
ArrayMap

##### ART-dalvik 
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

+--------+---------+
|        | Active  |
|        | Heap    |
|DalvikVM|         |
|  Heap  |         |
|(Ashmem)|         |
| mspace |         |
|        +---------+
|        | Zygote  |
|        | Heap    |
|        | (shared)|
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
      +--------+  +------+ +-------+  +-+-----+ +-------+ 
      |  ART   |  | Image| | Zygote|  |       | | Large | 
Heap  |        |  | Space| | Space |  | Alloc | | Object| 
      |  L+    |  |      | |       |  | Space | | Space | 
      +--------+  +------+ +-------+  +-------+ +-------+ 

      +--------+           +-------+  +------+           
      |DalvikVM|           | Zygote|  |Active|           
      |  <L    |           | Heap  |  |Heap  |           
      |        |           |       |  |      |           
      +--------+           +-------+  +------+           


```
Art Java堆的主要组成包括Image Space、Zygote Space、Allocation Space和Large Object Space四个Space
        （详细的话main space、image space、zygote space、non moving space、large object space）
        Image Space用来存在一些预加载的类（boot.art ） 
        Zygote Space和Allocation Space对应Dalvik虚拟机垃圾收集机制中的Zygote堆和Active堆的。
        ( 创建进程时，已经使用了的那部分堆内存Zygote Space，还没有使用的堆内存划分为Allocation Space)
        Large Object Space就是一些离散地址的集合

###### GC分类
日志：D/dalvikvm: <GC_Reason> <Amount_freed>, <Heap_stats>, <External_memory_stats>, <Pause_time>
Dalvik 有两种基本的 GC 模式， GC_CONCURRENT 和 GC_FOR_ALLOC 。
GC_CONCURRENT 对于每次收集将阻塞主线程大约 5ms 。GC_CONCURRENT 通常不会造成你的应用丢帧。
              GC_MALLOC, 内存分配失败时触发
              GC_CONCURRENT，当分配的对象大小超过384K时触发
              GC_EXPLICIT，对垃圾收集的显式调用(System.gc)
              GC_EXTERNAL_ALLOC，外部内存分配失败时触发
              GC_HPROF_DUMP_HEAP：当你请求创建 HPROF 文件来分析堆内存时出现的GC。
GC_FOR_ALLOC 是一种 stop-the-world 收集，可能会阻塞主线程达到 125ms 以上。
              GC_FOR_ALLOC 几乎每次都会造成你的应用丢失多个帧，导致视觉卡顿，特别是在滑动的时候。

I/art: <GC_Reason> <GC_Name> <Objects_freed>(<Size_freed>) AllocSpace Objects,<Large_objects_freed>(<Large_object_size_freed>) <Heap_stats> LOS objects, <Pause_time(s)>
ART新增GC原因：
              LOS_Space_Status

###### GC回收
Davik 仅有一种 Mark-Sweep。
        Live Bitmap和Mark Bitmap分代标记上次GC存活和这次需标记被引用的对象。
        mark阶段，其他线程可以并发执行（Concurrent GC）。CardTable记录非垃圾回收线程对对象的引用

ART：
 
zygote space 类似Davik

##### android触发垃圾回收
[android gc](https://proandroiddev.com/collecting-the-garbage-a-brief-history-of-gc-over-android-versions-f7f5583e433c)
[dalvik:tracing garbage collector, using a Mark and Sweep approach](https://android.googlesource.com/platform/dalvik.git/+/android-4.3_r2/vm/alloc/MarkSweep.cpp)
[art 粘性CMS和部分CMS](https://android.googlesource.com/platform/art/+/master/runtime/gc/)
当Bitmap和NIO Direct ByteBuffer对象分配外部存储（机器内存，非Dalvik堆内存）触发。
系统需要更多内存的时候触发。
HPROF时触发。

[回收机制](https://blog.csdn.net/f2006116/article/details/71775193)
[android hash](https://blog.csdn.net/xusiwei1236/article/details/45152201)

[smalidea 无源码调试 apk](https://blog.csdn.net/hackooo/article/details/53114838)

#### Handler/Dialog/Thread泄漏

1. PopupWindow 

```java 
android.view.WindowManager$BadTokenException: Unable to add window -- token null is not valid; is your activity running?
>  popwindow必须依附于某一个view

1. onWindowFocusChanged()或使用view的post()显示界面
2. if (!isFinishing()|| Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1&&!isDestroyed()){}

```

2.  AlertDialog不能使用application作为context 
```
android.view.WindowManager$BadTokenException: Unable to add window --token null is not for an application
 
```
3. dialog.show()
```java
android.view.WindowManager$BadTokenException
if (!isFinishing()|| Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1&&!isDestroyed()){
   dialog.show();
}
```

4. Dialog.dismiss()
```java
 View not attached to window manager
    if(mDialog != null) {
        if(mDialog.isShowing()) {   
            Context context = ((ContextWrapper)mDialog.getContext()).getBaseContext();  
            if(context instanceof Activity) { 
                if(!((Activity)context).isFinishing() && !((Activity)context).isDestroyed()) 
                    mDialog.dismiss();
            } else
                mDialog.dismiss();
        }
        mDialog = null;
    }
```
5. 没有及时关闭dialog
```java
  Activity xxxx has leaked， that was originally added here

@Override
public void onDestroy(){
    super.onDestroy();
    if ( mDialog!=null && mDialog.isShowing() ){
        mDialog.cancel();
    }
}

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

adb shell am dumpheap $(ps | grep com.example.proj | awk '{print $2}') /mnt/sdcard/my_heap/dumpheap.hprof

adb shell 'am dumpheap com.example.proj /mnt/sdcard/dumpheap.hprof'


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

#### LeakCanary&shark（Square）
 LRUCACHE
```
+------------------------------------------------------------------------------+
|                                    LruCache                                  |
|                                         cache:LinkedHashMap                  |
|                                         initialMaxSize:long                  |
|                                         maxSize:long                         |
|                                         currentSize:long                     |
|                                                                              |
|                                         put()                                |
|                                         getSize()           entryRemoved()   |
|                                         onItemEvicted()                      |
|                                         evict()                              |
|                                                                              |
|                                         get()                                |
+------------------------------------------------------------------------------+


```

```
* 001_initial f0cc04dfbf3cca92a669f0d250034d410eb05816 Initial import
                +-----------------------------------------------------------------------------------------------------+------------------------------+
                |                                                LeakCanary                                           |                              |
                |                                                   install(app:Application)                          |                              |
                +------------------------------------------------------------------------------------------------------------------------------------+
                |                                 RefWatcher                                                          |                              |
                |                                     queue:ReferenceQueue                                            |                              |
                |                                     retainedKeys:Set<String>                                        |                              |
                |                                                                                                     |                              |
                |                                     watch()                                                         |                              |
                |                                     removeWeaklyReachableReferences()                               |                              |
                |                                     gone()                                                          |                              |
                +------------------------------------------------------------------------------------------------------------------------------------+
                | AndroidWatchExecutor    GcTrigger   AndroidHeapDumper    ServiceHeapDumpListener: HeapDump.Listener |                              |
                |                            runGc()       dumpHeap()          analyze(h:HeapDump)                    |                              |
                |                                                                                                     |                              |
                | AndroidDebuggerControl              Debug                HeapAnalyzerService                        |                              |
                |                                        dumpHprofData()     heapAnalyzer:HeapAnalyzer                |                              |
                |                              dalvik_system_VMDebug.c       runAnalysis()                            | DisplayLeakActivity          |
                |           Dalvik_dalvik_system_VMDebug_dumpHprofData()     listenerServiceClass                     |                              |
                |                                                              :DisplayLeakService                    | AbstractAnalysisResultService|
                |                                                                                                     |                              |
                +------------------------------------------------------------------------------------------------------------------------------------+
                |                                         HeapDump               HeapAnalyzer                         |                              |
                |                                           heapDumpFile:File        checkForLeak():AnalysisResult    |                              |
                |                                                                    findLeakTrace():AnalysisResult   |                              |
                +------------------------------------------------------------------------------------------------------------------------------------+
                |                                                              proj(:haha-1.1)                        |                              |
                |                                                                                                     |                              |
                +-----------------------------------------------------------------------------------------------------+------------------------------+

                +----------------------------------------------------------------------------------------------------------------+ 
                | [haha]                                    SnapshotFactory                                                      |
                |                                                  openSnapshot():ISnapshot                                      | 
                |                                           Snapshot                                                             |
                |                                               createSnapshot():ISnapshot                                       | 
                |                                           SnapshotImpl                                                         |
                |                                               readFromFile():ISnapshot                                         |
                |                                               parse():ISnapshot                                                | 
                +----------------------------------------------------------------------------------------------------------------+
 
* 200_v2.0    49510378fa14e14e110985da4cab838facbf4864 Prepare 2.0 release
使用[shark]代理[haha]作为dump parse

                +-----------------------------------------------------------------------------------------------------+
                |[leakcanary-object-watcher-android]                                                                  |
                |                          sealed AppWatcherInstaller:ContentProvider()                               |
                +-----------------------------------------------------------------------------------------------------+
                |                                   InternalAppWatcher                                                |
                |                                                                                                     |
                |        ActivityDestroyWatcher                 FragmentDestroyWatcher                                |
                |                                                                                                     |
                +-----------------------------------------------------------------------------------------------------+
                | [leakcanary-object-watcher]                                                                         |
                |                                ObjectWatcher                                                        |
                +-----------------------------------------------------------------------------------------------------+
                |  [leakcanary-android-core]                                                                          |
                |                            HeapDumpTrigger                                                          |
                |                                 onObjectRetained()                                                  |
                |                                 heapDumper: HeapDumper                                              |
                |                                                                                                     |
                |                            AndroidHeapDumper                                                        |
                |                                  dumpHeap()                                                         |
                |                                                                                                     |
                |                             Debug                                                                   |
                |                               dumpHprofData()                                                       |
                +-----------------------------------------------------------------------------------------------------+
                |   [shark]                                                                                           |
                |             HeapAnalyzer                 FindLeakInput                                              |
                |                 analyze()                      findLeaks()                                          |
                |                                                buildLeakTraces()                                    |
                +-----------------------------------------------------------------------------------------------------+
                |        [shark-hprof]                      [shark-graph]                                             |
                |             Hprof                             HprofHeapGraph                                        |
                |               open(hprofFile: File)                                                                 |
                +-----------------------------------------------------------------------------------------------------+

```
#### 对象的生命周期绑定
Obsevable
### 缓存
 活动缓存 - WeakReference提高命中率；字节缓存，防止内存抖动；

#### 磁盘缓存 - DiskLruCache
1. 文档及配置文件
2. 源码
3. 单元测试
4. 性能及容错
5. 重用，封装，解耦，通讯
6. 文档

```
初定修订版本数据结构
+-----------------------+----------------------------+
|    maxSize            |  lruEntries(LinkedHashMap) |
+----------------------------------------------------+
|                       |  edit()  remove() get()    |
|                       +----------------------------+
|    magic              |  DIRTY   REMOVE READ  CLEAN|
|    version            | (Editor)      (Snapshot)   |
|    appVersionString   +----------------------------+
|    valueCountString   |    Entry                   |
+-----------------------+----------------------------+
|                   JOURNAL_FILE                     |
+----------------------------------------------------+
|                  DiskLruCache                      |
+----------------------------------------------------+

valueCountString: hash冲突时候，保留的多个冲突对象。后缀名解决冲突 0,1,2,3

```


### Glide
Glide是一个快速高效的Android图片加载库，注重于平滑的滚动。
不同数据源加载Fetcher，解码decode，变换transform，平滑过渡transition
三级缓存和ResourceManager及Target生命周期管理
[中文文档](https://muyangmin.github.io/glide-docs-cn/)
三级缓存 
      ActiveResources 活动缓存，weakreference提高命中率
      LruResourceCache 最近最少使用，剔除
      InternalCacheDiskCacheFactory（装饰DiskLruCache） 磁盘缓存
图片解码缓存
      LruBitmapPool    缓存bitmap，复用                              //BitmapFactory.Options#inBitmap
      LruArrayPool     复用字节/Integer数组，避免频繁GC，导致内存抖动 // BitmapFactory.Options#inTempStorage

单例     
         Glide#get() //volatile+双检锁
简单工厂      
        //资源简单工厂
        Glide#with(android.content.Context):RequestManager
        RequestManager#as():RequestBuilder::BaseRequestOptions
        RequestBuilder#into():Target//ViewTaget 对View的生命周期管理，构建Request下载到Target
        RequestBuilder#buildRequest():Request/SingleRequest#obtain():Request
        DecodeJob#getNextGenerator():DataFetcherGenerator// 数据请求，磁盘缓存
        ModelLoader#buildLoadData():LoadData             //加载图片数据
        BitmapResource#obtain():BitmapResource

工厂方法 
        /request/transition/TransitionFactory#build
        RequestManagerFactory#build():RequestManager


构建器   
         GlideBuilder#build()
原型    
        BaseRequestOptions#clone

转化器
       ResourceTranscoder#transcode():T
       Transformation#transform():Resource<T>
桥接
        RequestManagerRetriever#get(android.app.Activity):RequestManager//获取RequestManager 给RequestManagerRetriever实现

外观模式 
        Glide，GlideContext ，
        Engine类：
        Registry：
                ModelLoaderRegistry       //判断是否可以加载数据，并构建 ModelLoader#buildLoadData():LoadData
                EncoderRegistry          //流编码，写入文件等
                ResourceEncoderRegistry   //流编码，写入文件等
                ResourceDecoderRegistry   //数据源转化为Resource<T>
                DataRewinderRegistry      //流或buffer可以重置状态
                TranscoderRegistry         
                ImageHeaderParserRegistry //解析文件类型 ImageHeaderParser#getType():ImageType
享元  
        SingleRequest#POOL//复用Request

静态代理 
      Glide代理Engine
装饰   
      EngineJob 封装 DecodeJob
      LoadData 装饰 DataFetcher
      BitmapResource 装饰 Bitmap ，增加 BitmapPool 回收功能
      DataRewinder#rewindAndGet() //根据不同策略，增加流或buffer可以重置状态
      EngineResource              //装饰Resource，增添acquired计数功能
命令
       Engine#load()              //先从缓存获取
       DecodeJob#run()
观察者 
      /request/target/ViewTarget
       RequestManagerFragment 观察生命周期，管理 RequestManager
      DataFetcher.DataCallback#onDataReady                            //DataFetcherGenerator 观察 DataFetcher 返回图片数据数据
      DataFetcherGenerator.FetcherReadyCallback#onDataFetcherReady() //DecodeJob观察DataFetcherGenerator返回原生图片数据
      DecodeJob.DecodeCallback#onResourceDecoded                     //DecodeJob通过DecodeCallback，观察DecodePath，返回图片数据。并进行Transformation
      EngineJob#onResourceReady                                      //EnginJob观察DecodeJob返回 DataSource
      Engine#onEngineJobComplete()                                   //Engine观察EngineJob返回EngineResource数据
      SingleRequest#onResourceReady()                                //Request观察Engine返回<Resource>
      Target#onResourceReady()                                       //Target观察Request，返回<Resource>
      OnAttachStateChangeListener#onViewAttachedToWindow()           //Target观察View添加到界面，启动Request或暂停Request
      BaseTarget：观察Request生命周期状态                             //BaseTarget

      RequestManager                                                 //RequestManager观察ApplicationLifecycle，ActivityFragmentLifecycle生命周期执行
      ActivityFragmentLifecycle                                       //ActivityFragmentLifecycle观察RequestManagerFragment的onstart，onstop，onDestroy事件
      TargetTracker                                                  //TargetTracker观察ActivityFragmentLifecycle，给Target发送事件
      Target                                                         //Target通过TargetTracker#track，观察生命周期，并设置ViewHold，onResourceReady进行转场动画transition
适配器 
       ViewAdapter#transition

模板。。。

策略   LruPoolStrategy（SizeConfigStrategy SizeStrategy AttributeStrategy）
       Encoder#encode()
       DataFetcher#loadData()：AssetPathFetcher，HttpUrlFetcher，FileFetcher，HttpUrlFetcher
       /load/model/ModelLoader 工厂方法
       磁盘缓存策略：ResourceCacheGenerator，DataCacheGenerator，SourceGenerator
      BitmapTransformation：
              CenterCrop，CenterInside，CircleCrop，FitCenter，Rotate，RoundedCorners

迭代 
      DataCacheGenerator#hasNextModelLoader() //迭代ModelLoader，加载图片数据

##### 源码
RequestManager：请求管理（ RequestManager#requestTracker）， 生命周期管理 （观察Fragment生命周期）
      Request 构建器
Engine（命令模式load；）：
    EngineJob：图片加载。
    DecodeJob：图片处理

```

----------------+---------------------------------------------------------------------+--------------------------------------------+---------------------------------+--------------+-----------------------------------------+
|               |            |assets  raw  drawable ContentProvider| Picking a        |     Glide.with(fragment).asDrawable()      |                                 |              |                                         |
|               |ModelLoaders|SD   http/https                      | resource type    |                                            |                                 |              |                                         |
|               +-----------------------------------------------------------------------------------------+------------------------+                                 |              |                                         |
|    Component  |                                                  |                  |                   |   Placeholder          |                                 |              |                                         |
|    Options    |   ResourceEncoders, Encoders                     |                  |  placeholders     |   Error                |                                 |              |                                         |
|    load()     |   ResourceDecoders,                              |                  |                   |   Fallback             |                                 |              |                                         |
+------------------------------------------------------------------+                  +--------------------------------------------+                                 |              |                                         |
|  Application  |  Memory cache   LruResourceCache                 |  Request         | Transformations   |  circleCrop CenterCrop |                                 |              |                                         |
|  Options      |                                                  |  options         +--------------------------------------------+                                 |              |                                         |
|               |  Bitmap pool    LruBitmapPool                    |                  |  Caching          |                        |                                 |              |                                         |
|               |  Disk Cache     DiskLruCacheWrapper              |  apply()         |  Strategies       |                        |                                 |              |                                         |
|               +--------------------------------------------------+                  +--------------------------------------------+                                 |              |                                         |
|               | Default         format(DecodeFormat.RGB_565)     |                  |  Component        |                        |  View fade in                   |              |                                         |
|               | Request Options disallowHardwareBitmaps()        |                  |  specific         |                        |  Cross fade from the placeholder|  into()  size|  GlideExtension                         |
|               | UncaughtThrowableStrategy                        +------------------+-------------------+------------------------+  No transition                  |              |               GlideOption               |
|               | Log le^el                                        |  Thumbnail                                                    |                                 |              |               GlideType (GIFs, SVG etc) |
|               +------------------------------------------------------------------------------------------------------------------+                                 |              |                                         |
|               |  GlideModule                                     |  error                                                        |                                 |              |                                         |
+---------------+-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------+
|                                                                  |                                                               |                                 |              |                                         |
|                                                                  |                 Request                                       |    Transition                   |   Targets    |     Generated API                       |
|             Configuration                                        |                 Builder                                       |                                 |              |                                         |
+------------------------------------------------------------------+---------------------------------------------------------------+---------------------------------+--------------+-----------------------------------------+

Singleton: Platform 
Builder:OkHttpClient、Request、Response、MultipartBody、HttpUrl
Strategy: CookieJar
Observer:EventListener， WebSocketListener
Chain of Responsibility:Interceptor

  001_initial_code          1903a3ba2b980fd5c0556bfe869333a34411f5f5 initial commit of source from project
  002_presenter_imageloader f9a436a1bfb5e4b6901506ea61dc490a9b2fe5ae Add presenter system for wrapping imageviews
  003_pathLoader            1afd6153d474f6f54a9b42d0df263e48ffaf4154 Refactor ImageLoader -> PathLoader + ImageLoader
  004_start_stop_diskcache  b362a3df8471b8d8716b5747690b492e8fb2a984 Add start/stop to disk cache
* 005_flickr_sample         51c51e7fa4d247864f3debfff8aaff4078aaac7b Initial commit of flickr sample
                            +-------------------------------------------------------------------+
                            |  diskCache                                                        |
                            |                                                                   |
                            |  memoryCache  mainHandler                                         |
                            |                                                                   |
                            |  bitmapCache  executor    resizer                     pathLoader  |
                            |                                                       imageLoader |
                            |                                                                   |
                            +----------------------------------------+--------------------------+
                            |      ImageManager                      |            ImagePresenter|
                            |                                        |                          |
                            +----------------------------------------+--------------------------+


  200_v2.0_a                64186e3971a8f9ec14a8da2bec752e8182a2057a Bump targetSdkVersion
                            +----------------------------------------+--------------------------+
                            |                                        |      pathLoader          |
                            |  diskCache                             |      imageLoader         |
                            |                                        |                          |
                            |  memoryCache  mainHandler              +--------------------------+
                            |                                        |      ImagePresenter      |
                            |  bitmapCache  executor    resizer      |                          |
                            |                                        +--------------------------+
                            |                                        |       ModelLoader        |
                            +----------------------------------------+                          |
                            |      ImageManager                      |   volley.RequestQueue    |
                            |                                        |                          |
                            +----------------------------------------+--------------------------+
                            |                         Glide                                     |
                            +-------------------------------------------------------------------+


 490_v4.9.0                3035749168c8a4187cf3a51d19a6aee3bc5958d1 Bump version to 4.9.0
                          +------------------------------------+------------------------------+
                          |                   resourceRecycler |   DiskCache                  |
                          |                   diskCacheProvider|   MemoryCache                |
                          |                   memoryCache      |   BitmapPool                 |
                          |                          engine    |   arrayPool                  |
                          +-------------------------------------------------------------------+
                          |   ViewTarget       Request         |                              |
                          |      :Target                       |                              |
                          +------------------------------------+                              |
                          |  RequestBuilder                    |                              |
                          |                                    |                              |
                          |GlideRequests                       |                              |
                          | :RequestManager                    |                              |
                          +------------------------------------+                              |
                          |          Glide                                                    |
                          +-------------------------------------------------------------------+

```


## 数据存储/Context


文件存储,SharedPreferences/MMKV,SQLite数据库方式,内容提供器（Content provider）,网络
ContentProvider->保存和获取数据，并使其对所有应用程序可见
### Context

```java
W/Resources: Drawable com.android.systemui:drawable/ic_lockscreen_ime has unresolved theme attributes! 
ContextCompat.getDrawable() 获取vector

```
### 配置参数存储
#### SharedPreference
```
                +----------------------------------------------------------------------------------------+
                |  ContextImpl                                                                           |
                |    mSharedPrefsPaths:ArrayMap<String, File>                                            |
                |    getSharedPreferencesPath(String name): File                                         |
                |                                                                                        |
                |    sSharedPrefsCache                                                                   |
                |         :ArrayMap<String, ArrayMap<File, SharedPreferencesImpl> >                      |
                |    getSharedPreferences():SharedPreferencesImpl                                        |
                |                                                                                        |
                |    getSharedPreferencesCacheLocked()                                                   |
                |         :ArrayMap<File, SharedPreferencesImpl>                                         |
                |                                                                                        |
                +----------------------------------------------------------------------------------------+
                |   SharedPreferencesImpl             mMap:Map<String, Object>        enqueueDiskWrite() |
                |           makeBackupFile():File     edit():EditorImpl               writeToFile()      |
                |           loadFromDisk()                                                               |
                |                             +----------------------------------------------------------+
                |                             | EditorImpl:Editor                                        |
                |                             |    mModified:Map<String, Object>   mEditorLock:Object    |
                |                             |    apply()                         mModified             |
                |                             |    commit()                         :Map<String, Object> | 
                |                             |                                                          |
                |                             | commitToMemory():MemoryCommitResult                      |
                |                             | mListeners                                               |
                |                             |   :WeakHashMap<OnSharedPreferenceChangeListener, Object> |
                +----------------------------------------------------------------------------------------+
                |XmlUtils                     |                                                          |
                |  readMapXml()               |     MemoryCommitResult                                   |
                +-----------------------------+          writtenToDiskLatch //commit() wait return       |
                |Xml                          |                                                          |
                |  newPullParser():KXmlParser |                                                          |
                +-----------------------------+                                                          |
                | KXmlParser: XmlPullParser   |                                                          |
                ------------------------------+----------------------------------------------------------+


```

####  MMKV for Android “零拷贝问题” -  sharepreference优化
mmap（微信mars，美图logan，网易）
## Sqlite
### h2 /JOOQ/SnakeYAML 
### xutils
[xutils view,img,http,orm](https://github.com/zhuer0632/xUtils.git)

### 缓存GreenDAO /Jetpack-Room

### Mqtt服务器

## 进程内通讯
### EventBus
反射与注解
观察者模式
### ARouter
控制反转和面向切面
### 应用内消息机制（异步）
- Thread
- Handler        子线程与主线程通讯
- AsyncTask      界面回调，异步任务，一次性
- HandlerThread  异步队列，子线程与子线程通讯
- Timer/TimerTask  定时任务
- IntentServices 无界面，异步任务
- ThreadPool     并行任务

```
查看权限
adb shell pm list permissions -d -g                 
```

[hind api](https://android.googlesource.com/platform/prebuilts/runtime/+/master/appcompat)

**/art/tools/veridex/appcompat.sh --dex-file=test.apk**
``` dot
APK文件->Gradle编译脚本->APK打包安装及加载流程->AndroidManifest->四大组件->{Activity,Service,BrocastReceiver,ContentProvider}
 
```

```

打包参数
manifestPlaceholders = [ app_label_name:"xxxxxxx"]
//${app_label_name}
getPackageManager().getApplicationInfo(getPackageName(),PackageManager.GET_META_DATA).metaData.getString("app_label_name")


```
#### HandlerThread
装饰模式（封装Thread）
  装饰Thread，增加mLooper，可以让工作Handler设置Looper
## 进程间通讯
###  IPC机制与方法 
1. 1940 年，计算机存储中就使用了"文件"。
2. 1961 年，由Buroughs MCP和麻省理工学院兼容时间共享系统引入的"文件系统"的概念
3. 1973年，管道被实现，Ken Thompson将管道添加到了UNIX操作系统。使用的记号(垂直线)
    传统管道属于匿名管道，是计算机进程间的一种单工先进先出通信机制。
    ❌ 只能在具有亲缘关系的进程间使用
    ❌ 生存期不超过创建管道的进程的生存期
    ❌ 不支持异步读、写操作
4. 命名管道	被视为文件的管道。进程与使用匿名管道时那样使用标准输入和输出，而是从命名管道写入和读取，就像它是常规文件一样。
   ⭐允许无亲缘关系进程间的通信
5. 信号起源于20世纪70年代的贝尔实验室Unix。
   通常不用于传输数据，而是用于远程命令合作伙伴进程。
6. 1963年，Dijkstra提出了n个进程互斥算法（Dekker算法的泛化）信号量机制
7. POSIX 还提供用于将文件映射到内存的 API;可以共享映射，允许将文件的内容用作共享内存。mmap
   最快的 IPC 方式
8. 消息队列类似于套接字的数据流，但通常保留消息边界。
    ⭐克服了信号传递信息少、管道只能承载无格式字节流以及缓冲区大小受限等缺点
9. 1983年8月4.2BSD，包含socket
    ⭐不同主机，C/S架构
10. 2005年，Linux 3.19版本集成 OpenBinder
    ⭐安全性
趋势：导向，传输数据量，C/S一对多架构，速度，安全性
Linux中的RPC方式有管道，消息队列，共享内存等。（传统 pipe，无名管道fifo，信号；AT&T 信号量， 共享内存，消息队列；BSD 跨单机的socket）
管道：**ls |grep "hello"** ls进程输出，输入到grep进程

```
pipe：
process1 | process2 | process3
ls -l | grep key | less

```

消息队列和管道采用存储-转发方式，即数据先从发送方缓存区拷贝到内核开辟的缓存区中，然后再从内核缓存区拷贝到接收方缓存区，这样就有两次拷贝过程。
Binder一次拷贝原理(直接拷贝到目标线程的内核空间，内核空间与用户空间对应)。
```java
实用性(Client-Server架构)/传输效率(性能)/操作复杂度/安全性
，并发，一对多
                         +--------------+---------+------------------+
                         |Intent/Bundle | Messager|  Content Provider|
         +------------------------------+---------+-----------------------------------+-----------------+
         |               |   AIDL    +-------------------------------+                |                 |
         |               |           | byte, char, short, int, long, |                |                 |
         |               |           | float, double, boolean        |                |                 |
         |               |           | String, CharSequence          |                |                 |
         |               |           | Parcelable                    |                |                 |
         |               |           | List<>, Map<>                 |                |                 |
         |               |           | interface                     |                |                 |
         |               |           +-------------------------------+                |                 |
         |               |           | import Parcelable package     |                |                 |
         |               |           +-------------------------------+                |                 |
         |               |           | in out inout                  |                |                 |
         | SendFile      |           +-------------------------------+                |                 |
         | MemoryFile    |           | oneway                        |                |                 |
         |               |           +-------------------------------+                |                 |
         |               |-------------------------------------------|                |                 |
         | ashmem        |   read/write  Parcel                      |  pipe/fifo     |                 |
         +-----------------------------------------------------------+  signal        |                 |
         |               |                                           |  messagequeue  |  File           |
         | Shared memory |   Binder                                  |  semaphore     | SharedPreference|
         |               |                                           |  Socket        |                 |
         +----------------------------------------------------------------------------------------------+
copy     |      0        |                 1                         |       2                          |
times    +---------------+-------------------------------------------+----------------+-----------------+

应用安装器打开应用及应用安装器打开应用，第二次launcher打开应用
{                                                                                {
    "mAction": "android.intent.action.MAIN",                                         "mAction": "android.intent.action.MAIN",
    "mCategories": [                                                                 "mCategories": [
        "android.intent.category.LAUNCHER"                                               "android.intent.category.LAUNCHER"
    ],                                                                               ],
    "mComponent": {                                                                  "mComponent": {
        "mClass": "com.example.proj.activity.SplashActivity",                              "mClass": "com.example.proj.activity.SplashActivity",
        "mPackage": "com.example.proj"                                                     "mPackage": "com.example.proj"
    },                                                                               },
    "mContentUserHint": -2,                                                          "mContentUserHint": -2,
    "mFlags": 268435456,//10000000000000000000000000000  10000000                    "mFlags": 274726912,//10000011000000000000000000000  10600000 =10400000 |10200000 =
    "mPackage": "com.example.proj"          //FLAG_ACTIVITY_BROUGHT_TO_FRONT/FLAG_RECEIVER_FROM_SHELL|FLAG_ACTIVITY_RESET_TASK_IF_NEEDED/FLAG_RECEIVER_VISIBLE_TO_INSTANT_APPS
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
        "mClass": "com.example.proj.activity.SplashActivity",
        "mPackage": "com.example.proj"
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

### Binder机制/通讯协议
OpenBinder以及合入到Linux Kernel主线 3.19版本

**序列化（Parcelable，Serializable）与通讯** Serializable->Parcelable->Binder->{AIDL,Messenger}

Binder驱动不涉及任何外设，本质上只操作内存，负责将数据从一个进程传递到另外一个进程。
[Binder机制分析](http://gityuan.com/2014/01/01/binder-gaishu/) 
[Binder在java framework层的框架](http://gityuan.com/2015/11/21/binder-framework/)

#### Linux Binder driver
Binder定向制导
Binder binder_proc结构体的四棵红黑树  ，threads，nodes，refs_by_desc，refs_by_node
[](https://maoao530.github.io/2016/12/21/android-binder-01/)

[](./知识体系-平台-Linux.md)
#### IPCThreadState （client/server libs）
系统调用 ioctl的 BINDER_WRITE_READ，BINDER_THREAD_EXIT
存储结构 Parcel
#### ServiceManager
系统调用 open 打开驱动

系统调用 ioctl的 BINDER_WRITE_READ，BINDER_SET_CONTEXT_MGR

##### SystemServer
binder是C/S架构，包括Bn端(Server)和Bp端(Client)，ServiceManager（系统服务路由）,Binder驱动

```java
n：native
p：proxy

+----------------+------------+--------------------------------------+-------------------------+
+----------------+------------+          java  layer  / JNI          |          Native         |frameworks/base/libs/utils
+----------------+------------+--------------------------------------+-------------------------+
|                |            | Binder                BinderProxy    | +---------------------+ |              
|                |            +--------------------------------------| |  AndroidRuntime.cpp | |       
|                |  Client    |Android_util_Binder                   | |                     | |BpInterface/BnInterface
|                |            |    JavaBBinderHolder    (BpBinder)   + |                     | |      
|                |  process   |    JavaBBinder                       | |  IBinder IInterface | |   
|                |            |android_os_Parcel                     | +                     | |BBinder/
|                +---------------------------------------------------+ |  BpBinder           | |
|                |            | Binder                               | |  ProcessState       | |   binder/binderproxy
|                |            | BinderInternal                       | |  IPCThreadState     | | +-----------+
|                |  Server    +--------------------------------------+ +                     + |             |
|                |  process   |                                      | |                     | |             |
|  user space    +---------------------------------------------------+ |---------------------+ |             |
|                |            | ServiceManager                       | |                     | |             |
|                | system     |          ServiceManagerNative        | |IserviceManager      | |             |   
|                | service    |          ServiceManagerProxy         | |    BpServiceManager | |  binder(0)  |
|                |            |                                      | |    BnServiceManager | | +------+    |    
|                |            |                                      | +---------------------+ |        |    | 
|                +------------+--------------------------------------+-------------------------+        |    |
|                                                                         |     ^              |        |    |
|                                                             getbinder0  v     |  findBinder  |        |    |
|                +------------+----------------------------------------------------------------+        |    |
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
java层->JNI层->libs/IPCThreadState ==> driver （binder_proc）==>servicemanager/server

binder_write_read
  read_buffer(servicemanager/binder_txn 或server)



```

##### AMS 栈管理（任务栈），启动模式，亲和度
Activity的启动模式必不可少的要是launchMode、Flags、taskAffinity

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
freeformstack(自由模式  Activity) ：FreeForm Stack
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
##### WIFI
"am start -a android.net.wifi.PICK_WIFI_NETWORK"

```
  this.onCheckedChanged(!this.mWifiManager.isWifiEnabled());
    mWifiManager.getWifiApState();
 
    java.lang.IllegalStateException: java.io.IOException: Failed to parse network stats
        at com.android.server.NetworkManagementService.getNetworkStatsUidDetail(NetworkManagementService.java:1877)
        at com.android.server.net.NetworkStatsService.getNetworkStatsUidDetail(NetworkStatsService.java:1619)
        at com.android.server.net.NetworkStatsService.recordSnapshotLocked(NetworkStatsService.java:1204)
        at com.android.server.net.NetworkStatsService.performPollLocked(NetworkStatsService.java:1294)
        at com.android.server.net.NetworkStatsService.updateIfacesLocked(NetworkStatsService.java:1108)
        at com.android.server.net.NetworkStatsService.updateIfaces(NetworkStatsService.java:1084)
        at com.android.server.net.NetworkStatsService.forceUpdateIfaces(NetworkStatsService.java:862)
        at com.android.server.ConnectivityService.notifyIfacesChangedForNetworkStats(ConnectivityService.java:5793)
        at com.android.server.ConnectivityService.disconnectAndDestroyNetwork(ConnectivityService.java:2499)
        at com.android.server.ConnectivityService.updateNetworkInfo(ConnectivityService.java:5651)
        at com.android.server.ConnectivityService.access$1500(ConnectivityService.java:198)
        at com.android.server.ConnectivityService$NetworkStateTrackerHandler.maybeHandleNetworkAgentMessage(ConnectivityService.java:2203)
        at com.android.server.ConnectivityService$NetworkStateTrackerHandler.handleMessage(ConnectivityService.java:2342)
        at android.os.Handler.dispatchMessage(Handler.java:106)
        at android.os.Looper.loop(Looper.java:193)
        at android.os.HandlerThread.run(HandlerThread.java:65)


 @hide，不对外开放，但通过revoke机制调用到。
    getWifiApState
    setWifiApEnabled
    getWifiApConfiguration
    isWifiApEnabled
```

SSI 更新改为 30s
##### 静默安装
```java
小于Android 5      通过IPackageInstallObserver进行跨进程通信
                    1.6 base/core/java/android/app/ApplicationContext.java:1531:    static final class ApplicationPackageManager extends PackageManager
                    2.2 base/core/java/android/app/ContextImpl.java:1638:    static final class ApplicationPackageManager extends PackageManager {
                    4.4 base/core/core/java/android/app/ApplicationPackageManager.java:61:final class ApplicationPackageManager extends PackageManager {
Android 5（api21） 调用PackageManager#installPackage(Uri.class,android.app.PackageInstallObserver.int.class,String.class)；
                      通过PackageInstallObserver的binder（IPackageInstallObserver2）进行进程间通信
                     base/core/java/android/app/ApplicationPackageManager.java:78:final class ApplicationPackageManager extends PackageManager {
Android 7.0（api24）（和5.0 通用的方法可行） 调用PackageManager#installPacakageAsUser
                  base/core/java/android/app/ApplicationPackageManager.java:98:public class ApplicationPackageManager extends PackageManager
Android 9.0（api28） 调用PackageManager#getPackageInstaller() 安装，PackageInstaller.Session写入pms,广播接收通知
                  base/core/java/android/app/ApplicationPackageManager.java:111:public class ApplicationPackageManager extends PackageManager 已经删掉installPackage方法

```

##### 蓝牙连接
控制蓝牙图标显示
com.android.systemui.statusbar.phone.StatusBarIconController#getIconBlacklist
##### AudioManager
frameworks/base/services/core/java/com/android/server/audio/AudioService.java

#### 四大组件之Service
```
+------------+----------------------------------+------------------------------+
|            |   System Service                |    anonymous binder           |
|            | (AMS, PMS, WMS)                 | (depend on ServiceConn#binder)|   
+------------------------------------------------------------------------------+
|  launch    | SystemServer                    |   bindService                 |
+------------------------------------------------------------------------------+
| regist and |ServiceManager.addService        |  ActivityManagerService       |
| manager    |                                 |                               |
|            |SystemServiceManager.startService|                               |
|------------+---------------------------------+-------------------------------+
| communicate| SystemServer#getService         |  ServiceConnection            |
|            |                                 |  (binder.asInterface)         |
|------------+---------------------------------+-------------------------------+

通过startService开启的服务，一旦服务开启，这个服务和开启他的调用者之间就没有任何关系了（动态广播 InnerReceiver）;
通过bindService开启服务， ServiceConnection#asbinder()与AMS关联通讯，Service#onBind()与client关联通讯。

实名binder必须是建立在一个实名binder之上的，实名binder就是在service manager中注册过的。
首先client和server通过实名binder建立联系，然后把匿名binder通过这个实名通道“传递过去”

```

AIDL 文件生成对应类，类里包含继承Binder的stub内部类和实现AIDL的内部类；

- Bundle(实现了接口Parcelable)

[Android O 后台startService限制简析](https://www.jianshu.com/p/f2db0f58d47f)
```java
不允许Application启动服务。kill应用会出现问题。
startService(new Intent(this,BackService.class));
    java.lang.RuntimeException: Unable to create application edu.ptu.gson.DApplication: java.lang.IllegalStateException: Not allowed to start service Intent { cmp=edu.ptu.gson/.BackService }: app is in background uid UidRecord{e41908c u0a129 SVC  idle change:idle|uncached procs:1 seq(0,0,0)}
        at android.app.ActivityThread.handleBindApplication(ActivityThread.java:6227)
        at android.app.ActivityThread.access$1100(ActivityThread.java:211)
        at android.app.ActivityThread$H.handleMessage(ActivityThread.java:1778)
        at android.os.Handler.dispatchMessage(Handler.java:107)
        at android.os.Looper.loop(Looper.java:214)
        at android.app.ActivityThread.main(ActivityThread.java:7116)
        at java.lang.reflect.Method.invoke(Native Method)
        at com.android.internal.os.RuntimeInit$MethodAndArgsCaller.run(RuntimeInit.java:492)
        at com.android.internal.os.ZygoteInit.main(ZygoteInit.java:925)
     Caused by: java.lang.IllegalStateException: Not allowed to start service Intent { cmp=edu.ptu.gson/.BackService }: app is in background uid UidRecord{e41908c u0a129 SVC  idle change:idle|uncached procs:1 seq(0,0,0)}
        at android.app.ContextImpl.startServiceCommon(ContextImpl.java:1616)
        at android.app.ContextImpl.startService(ContextImpl.java:1571)
        at android.content.ContextWrapper.startService(ContextWrapper.java:669)
        at edu.ptu.gson.DApplication.onCreate(DApplication.java:10)
        at android.app.Instrumentation.callApplicationOnCreate(Instrumentation.java:1182)
        at android.app.ActivityThread.handleBindApplication(ActivityThread.java:6222)
        at android.app.ActivityThread.access$1100(ActivityThread.java:211) 
        at android.app.ActivityThread$H.handleMessage(ActivityThread.java:1778) 
        at android.os.Handler.dispatchMessage(Handler.java:107) 
        at android.os.Looper.loop(Looper.java:214) 
        at android.app.ActivityThread.main(ActivityThread.java:7116) 
        at java.lang.reflect.Method.invoke(Native Method) 
        at com.android.internal.os.RuntimeInit$MethodAndArgsCaller.run(RuntimeInit.java:492) 
        at com.android.internal.os.ZygoteInit.main(ZygoteInit.java:925) 

服务所在的应有在后台60秒后，不允许启动服务
    @Override
    protected void onPause() {
        super.onPause();
        new Handler().postDelayed(() -> {
            startService(new Intent(MainActivity.this,BackService.class));
        },TimeUnit.SECONDS.toMillis(65));

    }
    java.lang.IllegalStateException: Not allowed to start service Intent { cmp=edu.ptu.gson/.BackService }: app is in background uid UidRecord{f7b20eb u0a129 LAST bg:+1m4s234ms idle change:idle procs:1 seq(0,0,0)}
        at android.app.ContextImpl.startServiceCommon(ContextImpl.java:1616)
        at android.app.ContextImpl.startService(ContextImpl.java:1571)
        at android.content.ContextWrapper.startService(ContextWrapper.java:669)
        at edu.ptu.gson.MainActivity.lambda$onPause$1$MainActivity(MainActivity.java:48)
        at edu.ptu.gson.-$$Lambda$MainActivity$wl8e-hH5EF00KzpSg6rkpcKD2N8.run(Unknown Source:2)
        at android.os.Handler.handleCallback(Handler.java:883)
        at android.os.Handler.dispatchMessage(Handler.java:100)
        at android.os.Looper.loop(Looper.java:214)
        at android.app.ActivityThread.main(ActivityThread.java:7116)
        at java.lang.reflect.Method.invoke(Native Method)
        at com.android.internal.os.RuntimeInit$MethodAndArgsCaller.run(RuntimeInit.java:492)
        at com.android.internal.os.ZygoteInit.main(ZygoteInit.java:925)

需要设置startForeground()
startForegroundService(new Intent(MainActivity.this,BackService.class));
    android.app.RemoteServiceException: Context.startForegroundService() did not then call Service.startForeground(): ServiceRecord{266d400 u0 edu.ptu.gson/.BackService}
        at android.app.ActivityThread$H.handleMessage(ActivityThread.java:1864)
        at android.os.Handler.dispatchMessage(Handler.java:107)
        at android.os.Looper.loop(Looper.java:214)
        at android.app.ActivityThread.main(ActivityThread.java:7116)
        at java.lang.reflect.Method.invoke(Native Method)
        at com.android.internal.os.RuntimeInit$MethodAndArgsCaller.run(RuntimeInit.java:492)
        at com.android.internal.os.ZygoteInit.main(ZygoteInit.java:925)

/**
 * 8.0以上需要增加channel
 */
```


#### 四大组件-Activity
1. 协议
client binder: 
  IApplicationThread(ActivityThread.ApplicationThread)
server binder:
  ams(IActivityManager)
  IApplicationToken(ActivityRecord)
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
##### 生命周期
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
+-----+---------+       +-------+ +----+---+   (singleTop/Task)    |                       +---------+            |
      |                 v         |onResume|  <-----+activity      |                                              |
      |             +---+------+  +--------+        |froreground   |                       +---------+            |
      |   other     |Running   |         resumed    |              |   +-------------->    |onResume |<-----+     |
      |   activity  +---+------+                    |              |                       +---------+      |     |
      |   foreground    |         +--------+        |              |                              Fragm is  |     |
      |                 +-------> |onPause |        |    activity  |                        retaininstance  | onBack
      | <-----------------------+ |        | +----->+    foreground|                          onactivityRecreate  |
      |   other app               +---+----+                       |                       +---------+      |     |
      |   need memory                 v  paused                    |     +------------>    |onPause  |------|     |
      |                                  no longe visiable         |                       +---------+            |
      |                           +--------+                       |                                              |
      +-------------------------+ |onStop  | +---------------------+                                              |
                                  +---+----+                                               +---------+            |
                                      v  stoped                       +--------------->    |onStop   |            |
                                  +---+----+                                               +---------+            |
  *configChanges                  |onDestroy                                                                      |
                                  |        +                                               +-v-----v------+       |
                                  +---+----+                                               |onDestroyView +-------+
                                      v  destroyed  +--------------------------------->    |onDestroy     |
                                  +---+----+                                               |onDetach      |
                                  |shutdown|                                               +--------------+
                                  +--------+


```




#### 四大组件之广播
1. 协议
client binder: 
  IIntentReceiver(LoadedApk.ReceiverDispatcher.InnerReceiver)
  IApplicationThread(ActivityThread.ApplicationThread)
server binder:
  ams(IActivityManager)

2. 通讯
   
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
3. LocalBroadCastManager
   使用Handler处理penddingBroadCast

#### 四大组件之contentProvider
安装时候，会在AMS注册providers
1. 协议
client binder: 
  IApplicationThread(ActivityThread.ApplicationThread)
  IContentProvider(ContentProvider.Transport holder:ContentProviderRecord)
server binder:
  ams(IActivityManager)
1. registerContentObserver协议
client binder: 
  IContentObserver
server binder:
  IContentService(ContentService holder:ApplicationContentResolver)

3. ContentService扮演者ContentObserver的注册中心

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

### Configuration
![蓝牙连接手持按键](configuration.png)

```java 
android 7 Configuration.java

    public String toString() {
        StringBuilder sb = new StringBuilder(128);
        sb.append("{");
        sb.append(fontScale);
        sb.append(" ");
        if (mcc != 0) {
            sb.append(mcc);
            sb.append("mcc");
        } else {
            sb.append("?mcc");
        }
        if (mnc != 0) {
            sb.append(mnc);
            sb.append("mnc");
        } else {
            sb.append("?mnc");
        }
        fixUpLocaleList();
        if (!mLocaleList.isEmpty()) {
            sb.append(" ");
            sb.append(mLocaleList);
        } else {
            sb.append(" ?localeList");
        }
        int layoutDir = (screenLayout&SCREENLAYOUT_LAYOUTDIR_MASK);
        switch (layoutDir) {
            case SCREENLAYOUT_LAYOUTDIR_UNDEFINED: sb.append(" ?layoutDir"); break;
            case SCREENLAYOUT_LAYOUTDIR_LTR: sb.append(" ldltr"); break;
            case SCREENLAYOUT_LAYOUTDIR_RTL: sb.append(" ldrtl"); break;
            default: sb.append(" layoutDir=");
                sb.append(layoutDir >> SCREENLAYOUT_LAYOUTDIR_SHIFT); break;
        }
        if (smallestScreenWidthDp != SMALLEST_SCREEN_WIDTH_DP_UNDEFINED) {
            sb.append(" sw"); sb.append(smallestScreenWidthDp); sb.append("dp");
        } else {
            sb.append(" ?swdp");
        }
        if (screenWidthDp != SCREEN_WIDTH_DP_UNDEFINED) {
            sb.append(" w"); sb.append(screenWidthDp); sb.append("dp");
        } else {
            sb.append(" ?wdp");
        }
        if (screenHeightDp != SCREEN_HEIGHT_DP_UNDEFINED) {
            sb.append(" h"); sb.append(screenHeightDp); sb.append("dp");
        } else {
            sb.append(" ?hdp");
        }
        if (densityDpi != DENSITY_DPI_UNDEFINED) {
            sb.append(" "); sb.append(densityDpi); sb.append("dpi");
        } else {
            sb.append(" ?density");
        }
        switch ((screenLayout&SCREENLAYOUT_SIZE_MASK)) {
            case SCREENLAYOUT_SIZE_UNDEFINED: sb.append(" ?lsize"); break;
            case SCREENLAYOUT_SIZE_SMALL: sb.append(" smll"); break;
            case SCREENLAYOUT_SIZE_NORMAL: sb.append(" nrml"); break;
            case SCREENLAYOUT_SIZE_LARGE: sb.append(" lrg"); break;
            case SCREENLAYOUT_SIZE_XLARGE: sb.append(" xlrg"); break;
            default: sb.append(" layoutSize=");
                    sb.append(screenLayout&SCREENLAYOUT_SIZE_MASK); break;
        }
        switch ((screenLayout&SCREENLAYOUT_LONG_MASK)) {
            case SCREENLAYOUT_LONG_UNDEFINED: sb.append(" ?long"); break;
            case SCREENLAYOUT_LONG_NO: /* not-long is not interesting to print */ break;
            case SCREENLAYOUT_LONG_YES: sb.append(" long"); break;
            default: sb.append(" layoutLong=");
                    sb.append(screenLayout&SCREENLAYOUT_LONG_MASK); break;
        }
        switch (orientation) {
            case ORIENTATION_UNDEFINED: sb.append(" ?orien"); break;
            case ORIENTATION_LANDSCAPE: sb.append(" land"); break;
            case ORIENTATION_PORTRAIT: sb.append(" port"); break;
            default: sb.append(" orien="); sb.append(orientation); break;
        }
        switch ((uiMode&UI_MODE_TYPE_MASK)) {
            case UI_MODE_TYPE_UNDEFINED: sb.append(" ?uimode"); break;
            case UI_MODE_TYPE_NORMAL: /* normal is not interesting to print */ break;
            case UI_MODE_TYPE_DESK: sb.append(" desk"); break;
            case UI_MODE_TYPE_CAR: sb.append(" car"); break;
            case UI_MODE_TYPE_TELEVISION: sb.append(" television"); break;
            case UI_MODE_TYPE_APPLIANCE: sb.append(" appliance"); break;
            case UI_MODE_TYPE_WATCH: sb.append(" watch"); break;
            default: sb.append(" uimode="); sb.append(uiMode&UI_MODE_TYPE_MASK); break;
        }
        switch ((uiMode&UI_MODE_NIGHT_MASK)) {
            case UI_MODE_NIGHT_UNDEFINED: sb.append(" ?night"); break;
            case UI_MODE_NIGHT_NO: /* not-night is not interesting to print */ break;
            case UI_MODE_NIGHT_YES: sb.append(" night"); break;
            default: sb.append(" night="); sb.append(uiMode&UI_MODE_NIGHT_MASK); break;
        }
        switch (touchscreen) {
            case TOUCHSCREEN_UNDEFINED: sb.append(" ?touch"); break;
            case TOUCHSCREEN_NOTOUCH: sb.append(" -touch"); break;
            case TOUCHSCREEN_STYLUS: sb.append(" stylus"); break;
            case TOUCHSCREEN_FINGER: sb.append(" finger"); break;
            default: sb.append(" touch="); sb.append(touchscreen); break;
        }
        switch (keyboard) {
            case KEYBOARD_UNDEFINED: sb.append(" ?keyb"); break;
            case KEYBOARD_NOKEYS: sb.append(" -keyb"); break;
            case KEYBOARD_QWERTY: sb.append(" qwerty"); break;
            case KEYBOARD_12KEY: sb.append(" 12key"); break;
            default: sb.append(" keys="); sb.append(keyboard); break;
        }
        switch (keyboardHidden) {
            case KEYBOARDHIDDEN_UNDEFINED: sb.append("/?"); break;
            case KEYBOARDHIDDEN_NO: sb.append("/v"); break;
            case KEYBOARDHIDDEN_YES: sb.append("/h"); break;
            case KEYBOARDHIDDEN_SOFT: sb.append("/s"); break;
            default: sb.append("/"); sb.append(keyboardHidden); break;
        }
        switch (hardKeyboardHidden) {
            case HARDKEYBOARDHIDDEN_UNDEFINED: sb.append("/?"); break;
            case HARDKEYBOARDHIDDEN_NO: sb.append("/v"); break;
            case HARDKEYBOARDHIDDEN_YES: sb.append("/h"); break;
            default: sb.append("/"); sb.append(hardKeyboardHidden); break;
        }
        switch (navigation) {
            case NAVIGATION_UNDEFINED: sb.append(" ?nav"); break;
            case NAVIGATION_NONAV: sb.append(" -nav"); break;
            case NAVIGATION_DPAD: sb.append(" dpad"); break;
            case NAVIGATION_TRACKBALL: sb.append(" tball"); break;
            case NAVIGATION_WHEEL: sb.append(" wheel"); break;
            default: sb.append(" nav="); sb.append(navigation); break;
        }
        switch (navigationHidden) {
            case NAVIGATIONHIDDEN_UNDEFINED: sb.append("/?"); break;
            case NAVIGATIONHIDDEN_NO: sb.append("/v"); break;
            case NAVIGATIONHIDDEN_YES: sb.append("/h"); break;
            default: sb.append("/"); sb.append(navigationHidden); break;
        }
        if (seq != 0) {
            sb.append(" s.");
            sb.append(seq);
        }
        sb.append('}');
        return sb.toString();
    }

```
##### 发送广播出现权限问题
```java
java.lang.SecurityException: Permission Denial: not allowed to send broadcast android.intent.action.TIME_SET from pid=4404, uid=10033

1. 查找位置：
grep -irn 'Permission\ Denial:\ not\ allowed\ to\ send\ broadcast'

>base/services/core/java/com/android/server/am/ActivityManagerService.java:17701: 

2. 定位代码
isProtectedBroadcast = AppGlobals.getPackageManager().isProtectedBroadcast(action);

3. PMS 查找代码

    @Override
    public boolean isProtectedBroadcast(String actionName) {
        synchronized (mPackages) {
            if (mProtectedBroadcasts.contains(actionName)) {
                return true;
            } else if (actionName != null) {
                // TODO: remove these terrible hacks
                if (actionName.startsWith("android.net.netmon.lingerExpired")
                        || actionName.startsWith("com.android.server.sip.SipWakeupTimer")
                        || actionName.startsWith("com.android.internal.telephony.data-reconnect")
                        || actionName.startsWith("android.net.netmon.launchCaptivePortalApp")) {
                    return true;
                }
            }
        }
        return false;
    }
grep -irn --include="*.xml" 'protected-broadcast android:name="android.intent.action.TIME_SET'
base/core/res/AndroidManifest.xml:32:    <protected-broadcast android:name="android.intent.action.TIME_SET" />
```
### SystemUI
QuickStatusBarHeader
  quick_status_bar_expanded_header.xml
  QuickStatusBarHeader（system_icons.xml）
    BluetoothLayout
    BatteryMeterLayout


## 网络通讯
[高性能浏览器网络](https://hpbn.co/)

### Retrofit
动态代理创建Service类，适配返回类型Call支持其他类型， 注解信息依赖注入ServiceMethod

转换器注入 Converter#convert
          HttpServiceMethod.CallAdapted#adapt
简单工厂 
         Retrofit#create():Proxy.newProxyInstance()；//代理类
         RequestFactory#parseAnnotations():RequestFactory //构建RequestFactory，使用Convert解析Request
         ServiceMethod#parseAnnotations():ServiceMethod //API方法装饰类。装饰ServiceMethod，增加返参适配器，运用工厂方法，创///返参对象；
         HttpServiceMethod#invoke()://使用适配器（适配返参）， 创建返参对象

         //解析注解时候，创建RequestFactory，调用OkHttpCall 时
         RequestFactory#create():Okhttp.Request //OkHttp请求类，此过程中convert RequestBody，生成 Request

        //OkHttpCall#parseResponse() //创建Retrofit装饰类Response，封装Okhttp的Response
工厂方法 
        DefaultCallAdapterFactory#get():CallAdapter；//解析注解时候，创建适配器
        HttpServiceMethod#adapt() //创建返参对象

抽象工厂
        Converter.Factory
                Converter.Factory#requestBodyConverter//转化@Body为RequestBody
                Converter.Factory#responseBodyConverter//转化ResponseBody为Bean

单例 Retrofite对象；
        retrofit2.Platform#PLATFORM
构造器 
        Retrofit.Builder
        RequestFactory.Builder#Builder

适配 retrofit2.CallAdapter#adapt // 将 Call接口 适配为 returnType


观察者  Callback#onResponse                    //OkhttpCall 观察 Okhttp 回调
命令 retrofit2.Platform#defaultCallbackExecutor
装饰 
      DefaultCallAdapterFactory.ExecutorCallbackCall //装饰OkHttpCall，增加接口返回的主线程调度器
      retrofit2.HttpServiceMethod#invoke 装饰 

     retrofit2.adapter.rxjava.BodyOnSubscribe 装饰 CallExecuteOnSubscribe，CallEnqueueOnSubscribe，支持
代理 retrofit2.adapter.rxjava.CallExecuteOnSubscribe#call 静态代理 retrofit2.Call#execute
     retrofit2.adapter.rxjava.CallEnqueueOnSubscribe#call 静态代理retrofit2.Call#enqueue

解释 类的annotation
策略 retrofit2.Platform#findPlatform
     ParameterHandler#apply
     //根据Retrofit是否设置callbackExecutor
     ExecutorCallbackCall

      //不同的ParameterHandler ，不同的策略。Body使用的是自定义Convert
       ParameterHandler.Body#apply()

模板方法 retrofit2.Call#execute()

#### Rxjava3

工厂方法

```kotlin

interface IRetrofitService {
    //https://suggest.taobao.com/sug?code=utf-8&q=%E4%B9%A6&callback=
    @GET("/sug?code=utf-8&q=书&callback=")
   suspend fun getSearchResult():retrofit2.Response<ResponseBody>
}


val retrofit = Retrofit.Builder()
        .baseUrl("https://suggest.taobao.com")
        .build()
val service = retrofit.create(IRetrofitService::class.java)
val searchResult = service.getSearchResult()

```

[Retrofit 2.6 对协程的支持](https://blog.csdn.net/weixin_44946052/article/details/93225439)
### Okhttp（Square）
可加快内容加载速度并节省带宽。
缓存
 

转化器  
简单工厂 RealCall.kt#newRealCall():RealCall
         Transmitter#newExchange():Exchange
         ExchangeFinder:find():ExchangeCodec
         ExchangeFinder#findHealthyConnection():RealConnection
工厂方法 ⭐
        Call.Factory#newCall(): Call //构造请求回调
构建器 ⭐
        OkHttpClient.Builder
        Request.Builder      //构建请求，method，url，headers，body

模板方法
        Call#execute()， Call#enqueue()
观察者
        Callback.kt#onResponse() //RealCall观察回调网络请求
装饰
        RealConnection 装饰socket，对source，sink操作。
命令
        RealCall#run()
        Dispatcher
责任链 
        RealInterceptorChain#proceed(Request) //负责处理Request

状态 
        RetryAndFollowUpInterceptor 重试 
        BridgeInterceptor            CookieJar
        CacheInterceptor             缓存
        ConnectInterceptor           请求
        CallServerInterceptor 
 
>
复用连接池 
缓存策略
DiskLruCache  LinkedHashMap
任务队列 线程池文,反向代理模型,OkHttp的任务调度

@(源码分析)[OkHttp3.2|Android]

 

- **创建及初始化**
- **connection** 

---------------------

[TOC]

##创建及初始化
###HttpClient
```java
    OkHttpClient okHttpClient = new OkHttpClient();

    public OkHttpClient() {
      this(new Builder());
    }
    private OkHttpClient(Builder builder) {
        ···
        ···
        ···
    }
```
提供一个public的构造方法。
-- 创建Builder，并把默认构建的信息，参数方式传给另一个构造方法，进行赋值
-- private的构造方法，

先看下Builder的创建，这个类用来构造URL的信息，采用构建者模式。
```java
    public Builder() {
      dispatcher = new Dispatcher();//异步请求调度
      protocols = DEFAULT_PROTOCOLS;//Http协议 包括http2,http1_1,spdy
      connectionSpecs = DEFAULT_CONNECTION_SPECS;//数组 包括元素 ConnectionSpec.MODERN_TLS, ConnectionSpec.COMPATIBLE_TLS, ConnectionSpec.CLEARTEXT
      proxySelector = ProxySelector.getDefault();//代理选择，默认是java.net.ProxySelectorImpl
      cookieJar = CookieJar.NO_COOKIES;//使用cookie
      socketFactory = SocketFactory.getDefault();//socket工厂，默认javax.net.DefaultSocketFactory
      hostnameVerifier = OkHostnameVerifier.INSTANCE;
      certificatePinner = CertificatePinner.DEFAULT;
      proxyAuthenticator = Authenticator.NONE;
      authenticator = Authenticator.NONE;
      connectionPool = new ConnectionPool();//连接池
      dns = Dns.SYSTEM; //
      followSslRedirects = true;// ssl重定向
      followRedirects = true;//重定向
      retryOnConnectionFailure = true;// 重连
      connectTimeout = 10_000; // 连接超时时间
      readTimeout = 10_000;//读取时间
      writeTimeout = 10_000;//写超时时间
    }
```
默认初始化了一些信息。

- ConnectionSpec 连接的相关规范 tls的密钥,交换密钥,hash算法.默认有三种连接规范,cipherSuites为tls加密的加密套件,百度首页用的是 **CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256**
okhttp3.CipherSuite包含了所有加密套件,但只有少量在Android平台支持,定义在okhttp3.ConnectionSpec.APPROVED_CIPHER_SUITES.
```java

  /** A modern TLS connection with extensions like SNI and ALPN available. */
  public static final ConnectionSpec MODERN_TLS = new Builder(true)
      .cipherSuites(APPROVED_CIPHER_SUITES)
      .tlsVersions(TlsVersion.TLS_1_2, TlsVersion.TLS_1_1, TlsVersion.TLS_1_0)
      .supportsTlsExtensions(true)
      .build();

  /** A backwards-compatible fallback connection for interop with obsolete servers. */
  public static final ConnectionSpec COMPATIBLE_TLS = new Builder(MODERN_TLS)
      .tlsVersions(TlsVersion.TLS_1_0)
      .supportsTlsExtensions(true)
      .build();

  /** Unencrypted, unauthenticated connections for {@code http:} URLs. */
  public static final ConnectionSpec CLEARTEXT = new Builder(false).build();

```
值得注意ConnectionSpec的hashCode方法,哈希算法参考 [如何生成一个合适的hashcode方法](http://www.importnew.com/8189.html)

- ProxySelectorImpl 用来创建Proxy代理对象.能用代理的有http代理,socks代理
1. HTTP代理：能够代理客户机的HTTP访问，主要是代理浏览器访问网页，它的端口一般为80、8080、3128等；
2. SOCKS代理：SOCKS代理与其他类型的代理不同，它只是简单地传递数据包，而并不关心是何种应用协议，既可以是HTTP请求，所以SOCKS代理服务器比其他类型的代理服务器速度要快得多。
SOCKS代理又分为SOCKS4和SOCKS5，二者不同的是SOCKS4代理只支持TCP协议（即传输控制协议），而SOCKS5代理则既支持TCP协议又支持UDP协议（即用户数据包协议），还支持各种身份验证机制、服务器端域名解析等。
SOCK4能做到的SOCKS5都可得到，但SOCKS5能够做到的SOCK4则不一定能做到，比如我们常用的聊天工具QQ在使用代理时就要求用SOCKS5代理，因为它需要使用UDP协议来传输数据。

```java
    public enum Type {
        /**
         * Direct connection. Connect without any proxy.
         */
        DIRECT,

        /**
         * HTTP type proxy. It's often used by protocol handlers such as HTTP,
         * HTTPS and FTP.
         */
        HTTP,

        /**
         * SOCKS type proxy.
         */
        SOCKS
    }
```

- CookieJar接口, cookie 策略和持久化方法,含有两个方法,提供没有cookie的实现. **A reasonable policy is to reject all cookies**

```java
public interface CookieJar {
  /** A cookie jar that never accepts any cookies. */
  CookieJar NO_COOKIES = new CookieJar() {
    @Override public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
    }

    @Override public List<Cookie> loadForRequest(HttpUrl url) {
      return Collections.emptyList();
    }
  };

  /**
   * Saves {@code cookies} from an HTTP response to this store according to this jar's policy.
   *
   * <p>Note that this method may be called a second time for a single HTTP response if the response
   * includes a trailer. For this obscure HTTP feature, {@code cookies} contains only the trailer's
   * cookies.
   */
  void saveFromResponse(HttpUrl url, List<Cookie> cookies);

  /**
   * Load cookies from the jar for an HTTP request to {@code url}. This method returns a possibly
   * empty list of cookies for the network request.
   *
   * <p>Simple implementations will return the accepted cookies that have not yet expired and that
   * {@linkplain Cookie#matches match} {@code url}.
   */
  List<Cookie> loadForRequest(HttpUrl url);
}

```

- Cache类,缓存http,https的相应内容,以节省时间和流量

```
 /** <h3>Cache Optimization</h3>
 *
 * <p>To measure cache effectiveness, this class tracks three statistics:
 * <ul>
 *     <li><strong>{@linkplain #requestCount() Request Count:}</strong> the number of HTTP
 *         requests issued since this cache was created.
 *     <li><strong>{@linkplain #networkCount() Network Count:}</strong> the number of those
 *         requests that required network use.
 *     <li><strong>{@linkplain #hitCount() Hit Count:}</strong> the number of those requests
 *         whose responses were served by the cache.
 * </ul>
 */
```
CacheControl包含了缓存相关的http头参数
1. only-if-cached,force a cached response to be validated by the server
2. no-cache ,Force a Network Response
3. max-age=0,Force a Cache Response
4. max-stale

Cache类的重要属性 **DiskLruCache cache**,下面看下构造方法

```java
  public static DiskLruCache create(FileSystem fileSystem, File directory, int appVersion,
      int valueCount, long maxSize) {
    if (maxSize <= 0) {
      throw new IllegalArgumentException("maxSize <= 0");
    }
    if (valueCount <= 0) {
      throw new IllegalArgumentException("valueCount <= 0");
    }

    // Use a single background thread to evict entries.
    Executor executor = new ThreadPoolExecutor(0, 1, 60L, TimeUnit.SECONDS,
        new LinkedBlockingQueue<Runnable>(), Util.threadFactory("OkHttp DiskLruCache", true));

    return new DiskLruCache(fileSystem, directory, appVersion, valueCount, maxSize, executor);
  }
```
FileSystem是个接口类型,只包含常量 **FileSystem SYSTEM**,这个单例通过 Okio进行文件访问.

```java
  FileSystem SYSTEM = new FileSystem() {
    @Override public Source source(File file) throws FileNotFoundException {
      return Okio.source(file);
    }

    @Override public Sink sink(File file) throws FileNotFoundException {
      try {
        return Okio.sink(file);
      } catch (FileNotFoundException e) {
        // Maybe the parent directory doesn't exist? Try creating it first.
        file.getParentFile().mkdirs();
        return Okio.sink(file);
      }
    }

    @Override public Sink appendingSink(File file) throws FileNotFoundException {
      try {
        return Okio.appendingSink(file);
      } catch (FileNotFoundException e) {
        // Maybe the parent directory doesn't exist? Try creating it first.
        file.getParentFile().mkdirs();
        return Okio.appendingSink(file);
      }
    }

    @Override public void delete(File file) throws IOException {
      // If delete() fails, make sure it's because the file didn't exist!
      if (!file.delete() && file.exists()) {
        throw new IOException("failed to delete " + file);
      }
    }
```
回到DiskLruCache创建,接下来创建了线程池,管理单个守候线程,守护线程是用来服务用户线程的，如果没有其他用户线程在运行，那么就没有可服务对象，也就没有理由继续下去。
```java
  DiskLruCache(FileSystem fileSystem, File directory, int appVersion, int valueCount, long maxSize,
      Executor executor) {
    this.fileSystem = fileSystem;
    this.directory = directory;
    this.appVersion = appVersion;
    this.journalFile = new File(directory, JOURNAL_FILE);
    this.journalFileTmp = new File(directory, JOURNAL_FILE_TEMP);
    this.journalFileBackup = new File(directory, JOURNAL_FILE_BACKUP);
    this.valueCount = valueCount;
    this.maxSize = maxSize;
    this.executor = executor;
  }
```
三个重要文件
1. journalFile,文件内容如下

```
   /**     libcore.io.DiskLruCache
     *     1
     *     100
     *     2
     *
     *     CLEAN 3400330d1dfc7f3f7f4b8d4d803dfcf6 832 21054
     *     DIRTY 335c4c6028171cfddfbaae1a9c313c52
     *     CLEAN 335c4c6028171cfddfbaae1a9c313c52 3934 2342
     *     REMOVE 335c4c6028171cfddfbaae1a9c313c52
     *     DIRTY 1ab96a171faeeee38496d8b330771a7a
     *     CLEAN 1ab96a171faeeee38496d8b330771a7a 1600 234
     *     READ 335c4c6028171cfddfbaae1a9c313c52
     *     READ 3400330d1dfc7f3f7f4b8d4d803dfcf6
     */
```
第一行固定字符串libcore.io.DiskLruCache<br/>
第二行DiskLruCache的版本号，源码中为常量1<br/>
第三行为你的app的版本号，当然这个是你自己传入指定的<br/>
第四行指每个key对应几个文件，一般为1<br/>
第五行，空行<br/>

- InternalCache接口,具体实现在 **okhttp3.Cache.internalCache**,其实是调用Cache的方法

```java
public interface InternalCache {
  Response get(Request request) throws IOException;

  CacheRequest put(Response response) throws IOException;

  /**
   * Remove any cache entries for the supplied {@code request}. This is invoked when the client
   * invalidates the cache, such as when making POST requests.
   */
  void remove(Request request) throws IOException;

  /**
   * Handles a conditional request hit by updating the stored cache response with the headers from
   * {@code network}. The cached response body is not updated. If the stored response has changed
   * since {@code cached} was returned, this does nothing.
   */
  void update(Response cached, Response network) throws IOException;

  /** Track an conditional GET that was satisfied by this cache. */
  void trackConditionalCacheHit();

  /** Track an HTTP response being satisfied with {@code cacheStrategy}. */
  void trackResponse(CacheStrategy cacheStrategy);
}
```
get方法调用的是Cache的get方法
```java
  Response get(Request request) {
    String key = urlToKey(request);
    DiskLruCache.Snapshot snapshot;
    Entry entry;
    try {
      snapshot = cache.get(key);
      if (snapshot == null) {
        return null;
      }
    } catch (IOException e) {
      // Give up because the cache cannot be read.
      return null;
    }

    try {
      entry = new Entry(snapshot.getSource(ENTRY_METADATA));
    } catch (IOException e) {
      Util.closeQuietly(snapshot);
      return null;
    }

    Response response = entry.response(snapshot);

    if (!entry.matches(request, response)) {
      Util.closeQuietly(response.body());
      return null;
    }

    return response;
  }
```
urlTokey是将url转化为32个字符的十六进制串 *Util.md5Hex(request.url().toString());* ,通过缓存,重新封装成Response对象.


InternalCache方法,执行的也是Cache的put方法
```java
  private CacheRequest put(Response response) throws IOException {
    String requestMethod = response.request().method();

    if (HttpMethod.invalidatesCache(response.request().method())) {
      try {
        remove(response.request());
      } catch (IOException ignored) {
        // The cache cannot be written.
      }
      return null;
    }
    if (!requestMethod.equals("GET")) {
      // Don't cache non-GET responses. We're technically allowed to cache
      // HEAD requests and some POST requests, but the complexity of doing
      // so is high and the benefit is low.
      return null;
    }

    if (OkHeaders.hasVaryAll(response)) {
      return null;
    }

    Entry entry = new Entry(response);
    DiskLruCache.Editor editor = null;
    try {
      editor = cache.edit(urlToKey(response.request()));
      if (editor == null) {
        return null;
      }
      entry.writeTo(editor);
      return new CacheRequestImpl(editor);
    } catch (IOException e) {
      abortQuietly(editor);
      return null;
    }
  }
```
只缓存请求成功的Get请求. *cache.edit(urlToKey(response.request()));* 将获取或封装entry放入lruEntries对象 ,entry.writeTo(editor);使用的是Sink对象,写入缓存文件.


Cache还有个内部类 Entry
```java
    /**
     * Reads an entry from an input stream. A typical entry looks like this:
     * <pre>{@code
     *   http://google.com/foo
     *   GET
     *   2
     *   Accept-Language: fr-CA
     *   Accept-Charset: UTF-8
     *   HTTP/1.1 200 OK
     *   3
     *   Content-Type: image/png
     *   Content-Length: 100
     *   Cache-Control: max-age=600
     * }</pre>
     *
     * <p>A typical HTTPS file looks like this:
     * <pre>{@code
     *   https://google.com/foo
     *   GET
     *   2
     *   Accept-Language: fr-CA
     *   Accept-Charset: UTF-8
     *   HTTP/1.1 200 OK
     *   3
     *   Content-Type: image/png
     *   Content-Length: 100
     *   Cache-Control: max-age=600
     *
     *   AES_256_WITH_MD5
     *   2
     *   base64-encoded peerCertificate[0]
     *   base64-encoded peerCertificate[1]
     *   -1
     *   TLSv1.2
     * }</pre>
     */
  private static final class Entry {
    private final String url;
    private final Headers varyHeaders;
    private final String requestMethod;
    private final Protocol protocol;
    private final int code;
    private final String message;
    private final Headers responseHeaders;
    private final Handshake handshake;
    ...
    ...
    ...
  }
```

###Request创建
Request只有一个私有的构造方法，传入Request的内部类Builder
```java
      Request build = new Request.Builder().url("http://169.254.64.194/test/").build();
      private Request(Builder builder) {
        this.url = builder.url;
        this.method = builder.method;
        this.headers = builder.headers.build();
        this.body = builder.body;
        this.tag = builder.tag != null ? builder.tag : this;
      }
```
看下Builder构造方法（okhttp3.Request.Builder）
```java
  public static class Builder {
    public Builder() {
      this.method = "GET";
      this.headers = new Headers.Builder();
    }

    private Builder(Request request) {
      this.url = request.url;
      this.method = request.method;
      this.body = request.body;
      this.tag = request.tag;
      this.headers = request.headers.newBuilder();
    }
    ···
    ···
  }
```
外部只能用Builder进行public构造方法初始化。设置请求方法为 *GET*，创建了Headers.Builderd对象。okhttp的构建者模式到现在已经用了三次，*Httpclient.Builder*,*Request.Builder*,*Headers.Builder*。看完构造够着方法，再看下 *Request.Builder*的属性。
```java
    private HttpUrl url;
    private String method;
    private Headers.Builder headers;
    private RequestBody body;
    private Object tag;
```
下面是一段Post请求，和Request.Builder对比起来，多了个tag，这个tag用来标记当前的Request.Builder。
```
    POST http://plugin.open.___.com/clientrequest/queryPluginByhostVersion.do HTTP/1.1
    Content-Length: 187
    Content-Type: application/x-www-form-urlencoded
    Host: plugin.open.___.com
    Connection: Keep-Alive
    User-Agent: Apache-HttpClient/UNAVAILABLE (java 1.4)

    sign=j9hiUV8ue%2F%2Ba%2F1tatjLdp0oCxZ0%3D&base=%7B%22agentId%22%3A%22100001%22%2C%22hostVersion%22%3A%221.0.0%22%2C%22osVersion%22%3A%2217%22%7D&buzz=%7B%22hostVersion%22%3A%221.0.0%22%7D
```
接着分析报文对应的属性。
-- HttpUrl
只有一个私有的构造方法
```java
  private HttpUrl(Builder builder) {
    this.scheme = builder.scheme;
    this.username = percentDecode(builder.encodedUsername, false);
    this.password = percentDecode(builder.encodedPassword, false);
    this.host = builder.host;
    this.port = builder.effectivePort();
    this.pathSegments = percentDecode(builder.encodedPathSegments, false);
    this.queryNamesAndValues = builder.encodedQueryNamesAndValues != null
        ? percentDecode(builder.encodedQueryNamesAndValues, true)
        : null;
    this.fragment = builder.encodedFragment != null
        ? percentDecode(builder.encodedFragment, false)
        : null;
    this.url = builder.toString();
  }
```
HttpUrl也是通过构造者模式创建，HttpUrl.Builder也是在HttpUrl的内部类，现在看下HttpUrl.Builder的属性和构造方法。先参考下通用的url格式
> scheme:[//[user:password@]host[:port]][/]path[?query][#fragment] [A generic URI is of the form](https://en.wikipedia.org/wiki/Uniform_Resource_Identifier#Generic_syntaxr)

```
                    hierarchical part
        ┌───────────────────┴─────────────────────┐
                    authority               path
        ┌───────────────┴───────────────┐┌───┴────┐
  abc://username:password@example.com:123/path/data?key=value#fragid1
  └┬┘   └───────┬───────┘ └────┬────┘ └┬┘           └───┬───┘ └──┬──┘
scheme  user information     host     port            query   fragment

  urn:example:mammal:monotreme:echidna
  └┬┘ └──────────────┬───────────────┘
scheme              path
```


```java
  public static final class Builder {
    String scheme;
    String encodedUsername = "";
    String encodedPassword = "";
    String host;
    int port = -1;
    final List<String> encodedPathSegments = new ArrayList<>();
    List<String> encodedQueryNamesAndValues;
    String encodedFragment;

    public Builder() {
      encodedPathSegments.add(""); // The default path is '/' which needs a trailing space.
    }
    ···
    ···
    ···
  }
```
支持通用的格式。
--- scheme只支持http,https
```java
    public Builder scheme(String scheme) {
      if (scheme == null) {
        throw new IllegalArgumentException("scheme == null");
      } else if (scheme.equalsIgnoreCase("http")) {
        this.scheme = "http";
      } else if (scheme.equalsIgnoreCase("https")) {
        this.scheme = "https";
      } else {
        throw new IllegalArgumentException("unexpected scheme: " + scheme);
      }
      return this;
    }
```
--- username,password连个处理方式一样，就以username作为分析
```java
    public Builder username(String username) {
      if (username == null) throw new IllegalArgumentException("username == null");
      this.encodedUsername = canonicalize(username, USERNAME_ENCODE_SET, false, false, false, true);
      return this;
    }

    public Builder encodedUsername(String encodedUsername) {
      if (encodedUsername == null) throw new IllegalArgumentException("encodedUsername == null");
      this.encodedUsername = canonicalize(
          encodedUsername, USERNAME_ENCODE_SET, true, false, false, true);
      return this;
    }
```
canonicalize方法的四个布尔类型参数如下
1. **alreadyEncoded** true to leave '%' as-is; false to convert it to '%25'.
2. **strict** true to encode '%' if it is not the prefix of a valid percent encoding.
3. **plusIsSpace** true to encode '+' as "%2B" if it is not already encoded.
4. **asciiOnly** true to encode all non-ASCII codepoints.

--- host
支持域名，IDN,ipv4,ipv6。下面是ipv6的格式
```http
    https://[2001:db8:85a3:8d3:1319:8a2e:370:7348]:443/
```
--- port 
支持到65539, http默认80，https默认443

-- Headers.builder
Headers的构建类，维护一个数组链表
```java
    private final List<String> namesAndValues = new ArrayList<>(20);
```
-- RequestBody
```java
  public static RequestBody create(MediaType contentType, String content) {
    Charset charset = Util.UTF_8;
    if (contentType != null) {
      charset = contentType.charset();
      if (charset == null) {
        charset = Util.UTF_8;
        contentType = MediaType.parse(contentType + "; charset=utf-8");
      }
    }
    byte[] bytes = content.getBytes(charset);
    return create(contentType, bytes);
  }
```
Request由多种静态方法创建，其中参数MediaType是对content type 的封装。
<br/>
到现在，简要分析了Request的初始化。
##添加请求
```java
        //异步
        Response response = client.newCall(request).execute();
        
        //同步
        okHttpClient.newCall(build).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

            }
        });
```
看下OkHttpClient的newCall方法
```java
  @Override public Call newCall(Request request) {
    return new RealCall(this, request);
  }
```
RealCall关联重要的两个对象，即OkHttpClient和Request。<br/>
再往下看同步执行的RealCall的execute方法
```java
  @Override public Response execute() throws IOException {
    synchronized (this) {
      if (executed) throw new IllegalStateException("Already Executed");
      executed = true;
    }
    try {
      client.dispatcher().executed(this);
      Response result = getResponseWithInterceptorChain(false);
      if (result == null) throw new IOException("Canceled");
      return result;
    } finally {
      client.dispatcher().finished(this);
    }
  }
```
刚开始执行时，采用同步快，保证executed多线程访问。
<br/>然后使用OkHttpClient的调度对象，执行RealCall。最终添加到类型为ArrayDeque的runningSyncCalls容器中。关于ArrayDeque可以参考下AsyncTask。因为ArrayDeque非线程安全，所以加了同步块。在上面的方法执行到finally时候移除掉client.dispatcher().finished(this);
```java
  synchronized void executed(RealCall call) {
    runningSyncCalls.add(call);
  }
```
接着执行getResponseWithInterceptorChain方法，看起来使用责任链模式。
```java
  private Response getResponseWithInterceptorChain(boolean forWebSocket) throws IOException {
    Interceptor.Chain chain = new ApplicationInterceptorChain(0, originalRequest, forWebSocket);
    return chain.proceed(originalRequest);
  }
```
参数index等于0，Request请求，和是否是webSocket
```java

  class ApplicationInterceptorChain implements Interceptor.Chain {
    private final int index;
    private final Request request;
    private final boolean forWebSocket;

    ApplicationInterceptorChain(int index, Request request, boolean forWebSocket) {
      this.index = index;
      this.request = request;
      this.forWebSocket = forWebSocket;
    }
    ····
    ····
    ····
  }
```

执行proceed方法
```java
    @Override public Response proceed(Request request) throws IOException {
      // If there's another interceptor in the chain, call that.
      if (index < client.interceptors().size()) {
        Interceptor.Chain chain = new ApplicationInterceptorChain(index + 1, request, forWebSocket);
        Interceptor interceptor = client.interceptors().get(index);
        Response interceptedResponse = interceptor.intercept(chain);

        if (interceptedResponse == null) {
          throw new NullPointerException("application interceptor " + interceptor
              + " returned null");
        }

        return interceptedResponse;
      }

      // No more interceptors. Do HTTP.
      return getResponse(request, forWebSocket);
    }
  }
```
先是查询是否有拦截器，现在暂时不考虑。则走getResponse方法。
```java
```

##缓存策略
Cache和InternalCache

##连接池
> 外部网络请求的入口都是通过Transport接口来完成。该类采用了桥接模式将HttpEngine和HttpConnection来连接起来。

ConnectionPool,缓存RealConnection,RealConnection封装socket
```
```
##重连机制

RouteSelector,在StreamAllocation构造方法初始化.HttpEngine调用StreamAllocation的newStream方法时候,最终会走到RouteSelector
```java
  private HttpStream connect() throws RouteException, RequestException, IOException {
    boolean doExtensiveHealthChecks = !networkRequest.method().equals("GET");
    return streamAllocation.newStream(client.connectTimeoutMillis(),
        client.readTimeoutMillis(), client.writeTimeoutMillis(),
        client.retryOnConnectionFailure(), doExtensiveHealthChecks);
  }
```
StreamAllocation包含了Connections,Streams,Calls三者的关系
```java
 /**
 * <ul>
 *     <li><strong>Connections:</strong> physical socket connections to remote servers. These are
 *         potentially slow to establish so it is necessary to be able to cancel a connection
 *         currently being connected.
 *     <li><strong>Streams:</strong> logical HTTP request/response pairs that are layered on
 *         connections. Each connection has its own allocation limit, which defines how many
 *         concurrent streams that connection can carry. HTTP/1.x connections can carry 1 stream
 *         at a time, SPDY and HTTP/2 typically carry multiple.
 *     <li><strong>Calls:</strong> a logical sequence of streams, typically an initial request and
 *         its follow up requests. We prefer to keep all streams of a single call on the same
 *         connection for better behavior and locality.
 * </ul>
 */
```

##Gzip的使用方式
RequestBodyCompression

Gzip实现的方式主要是通过GzipSink对普通sink的封装压缩。

##安全性

java的SSLSocket相关的开发再来了解本框架中的安全性
##平台适应性

Platform是整个平台适应的核心类。同时它封装了针对不同平台的三个平台类Android和JdkWithJettyBootPlatform。




``` java
Okhttp
  001_initial_mockwebserver 0c4790dafaf6ccf7d8d34d04d24aac65cac910eb Initial checkin of MockWebServer.
  002_ant_maven             4ebac2656c870a090141106ad9ce267d25863301 Upgrade from ant to maven.
  003_initial_client        ea63463c2ff8a67f63e541036f0d5998b5bb1f3e Initial import.

                                          google mockwebserver
                            +-------------------------------------------------------+
                            |                  HttpConnectionPool                   |
                            |                           +---------------------------+
                            |                           |    bouncycastle           |
                            |                           |    conscrypt              |
                            |                           |    JettyNpnProvider       |
                            |                           |    HttpsEngine            |
                            +---------------------------+---------------------------+
                            |  HttpEngine                                           |
                            |     responseCache                                     |
                            |     transparentGzip                                   |
                            +---------------------------+---------------------------+
                            |  HttpURLConnectionImpl    |    HttpsURLConnectionImpl |
                            |           proxy           |            HttpsEngine    |
                            |           redirectionCount+---------------------------+
                            |           httpEngine      |    OkHttpsConnection      |
                            +---------------------------+---------------------------+
                            |             OkHttpConnection : URLConnection          |
                            |                         open()                        |
                            +-------------------------------------------------------+
 
  100_parent1.0.0           d95ecff5423f19e019e178baddfe4211f2fe57aa [maven-release-plugin] prepare release parent-1.0.0
                            +-------------------------------------------------------+
                            |                  HttpConnectionPool                   |
                            |                           +---------------------------+
                            |                           |    bouncycastle           |
                            |                           |    conscrypt              |
                            |                           |    JettyNpnProvider       |
                            |                           |    HttpsEngine            |
                            +---------------------------+---------------------------+
                            |  HttpEngine                                           |
                            |     responseCache                                     |
                            |     transparentGzip                                   |
                            +---------------------------+---------------------------+
                            |  HttpURLConnectionImpl    |    HttpsURLConnectionImpl |
                            |           proxy           |            HttpsEngine    |
                            |           redirectionCount+---------------------------+
                            |           httpEngine      |    OkHttpsConnection      |
                            +---------------------------+---------------------------+
                            |              OkHttpClient                             |
                            |                   failedRoutes                        |
                            |                   open()                              |
                            +-------------------------------------------------------+ 

* 200_parent-2.0.0-RC1      8dcc74d339e0664580756063ff47c65c6f1a17ae [maven-release-plugin] prepare release parent-2.0.0-RC1 
                            Okio+Request
                            +-------------------------------------------------------+
                            |                  HttpConnectionPool                   |
                            |                           +---------------------------+
                            |                           |    bouncycastle           |
                            |                           |    conscrypt              |
                            |                           |    JettyNpnProvider       |
                            |                           |    HttpsEngine            |
                            +---------------------------+---------------------------+
                            |  HttpEngine                                           |
                            |       connection                                      |
                            |       routeSelector                                   |
                            |        sendRequest()                                  |
                            +-------------------------------------------------------+
                            |  Call                                                 |
                            |      client        execute()                          |
                            |      dispatcher    enqueue()                          |
                            |      request                                          |
                            |      engine                                           |
                            +-------------------------------------------------------+
                            |  OkHttpClient                                         |
                            |       routeDatabase                                   |
                            |       dispatcher                                      |
                            |       newCall()                                       |
                            +-------------------------------------------------------+
* 300_parent-3.0.0-RC1      ffc35dbd02822bf6584c6144266cbbca6b348b17 [maven-release-plugin] prepare release parent-3.0.0-RC1
                            Spidy->Http/2
  400_parent-4.0.0-ALPHA01  8f21b934f928986bba7e50114911c3c494e1d5c5 Prepare for release 4.0.0-ALPHA01.
                            gradle+kotlin


```


OKIO 封装链表缓存字节，避免空间浪费，避免频繁GC（zero-fill and GC churn ）
```
* 001_okbuffer              2d3cdc9e5dfb593f8eb7da38459163b902a95d7f OkBuffer API sketch.
                            +--------------------------------------------+
                            |           ByteString                       |
                            +--------------------------------------------+
                            |                            SegmentPool     |
                            +--------------------------------------------+
                            |         Segment                            |
                            |             pos//for read                  |
                            |             limite // for write            |
                            +--------------------------------------------+
                            |         OKBuffer:Sink,Source               |
                            |                segment                     |
                            |                byteCount                   |
                            +--------------------------------------------+

  002_okbuffer_dapter       bece94f8970fa6d0615581f81f71efa4b950d3b2 Adapters for sources and sinks.
  003_InflaterSource        88b2592d0aea290aecd38bc24a9e3ed66bb38d05 InflaterSource. Like InflaterInputStream.
  004_GipSource             8efe816e6fa54efd935b08050dfdcf43a50f8bbe GzipSource beginnings.
  005_BufferedSink          d1dcbcf786fff9f1d1da0ffea6703e2f749c032b BufferedSink.
  006_DeflaterSink          2fcbac79930492eeb379b9c9b3c63cca4e8789df DeflaterSink.
  007_RealBufferedSink      c287d63829969fe03f559e76deb735c8286d4d32 Interfaces for BufferedSource and BufferedSink.
  008_GzipSink              d0a461ffd277ab8e34edd7ca995a0fae0b02b790 Add GzipSink for writing Gzip data.
  050_okio-parent0.5.0      21362a2634b287f3d2136327a56ebf1b9738a7ae [maven-release-plugin] prepare release okio-parent-0.5.0
                            maven
* 200_okio_parent-2.0.0-RC1 4da4d9e3e311cdce7294fc59507f00dca2cf2ce0 Update changelog for 2.0.0-RC1
                            Gradle+Kotlin


```



```

retrofit 核心：动态代理访问注解方法，返回Call适配器
  001_ant_ivy                  0404ce4a2ef46e4ed9c5f06da6ebf862cc52253d Initial code drop, includes Ant buildfile and QueueFile
  002_ShakeDetector            fb98822a9c17acfb6846d3f07d368804e155fd3f moved util to android, added ShakeDetector
  003_restAdapter_core_io_http 42dba217c69705a456b837bdd3cc2966c8d67b52 added core, io, and http modules
                              +-------------------+-----------------+---------+----------------------------------------+
                              |                   |                 |         |                      Sink              |
                              +-------------------+-----------------+         +----------------------------------------+
                              |                   |                 |  Gson   | httpClientProvider Executor MainThread |
                              |                   |                 |  guice  |                            Fetcher     |
                              |                   |                 | easymock|                                        |
                              |                   |                 +---------+----------------------------------------+
                              |QueueFile          | ShakeDetector   |core  io                            http          |
                              |  RandomAccessFile |                 +--------------------------------------------------+
                              |                   |                 |                     restAdapter                  |
                              +-------------------+-----------------+--------------------------------------------------+
  004_test_app                 9c195d377fe86f8d59c20a8ec012b24bdf4288f4 Added support for fsyncing a directory.
  100_parent-1.0.0             193b3f0027dbbcb99d1701e6106a9856197824e4 [maven-release-plugin] prepare release parent-1.0.0

                              +--------------------------------------------------------+---------------+
                              |           (Proxy)                                      |               |
                              |          requestHeaders                                |               |
                              | server   RestMethodInfo                                |   debug       |
                              |     clientProvider       converter  callbackExecutor   |   profiler    |
                              |                                                        |   log         |
                              |               httpExecutor                             |               |
                              +--------------------------------------------------------+---------------+
                              |                        RestAdapter                                     |
                              +-------------------------+----------------------------------------------+
                              |         okhttp  gson    |   junit   easytesting  mockito    guava      |
                              +-------------------------+----------------------------------------------+
* 200_parent-2.0.0-beta1       bcf627578ae8fd593e1c8a4a85a841f6a68b072c [maven-release-plugin] prepare release parent-2.0.0-beta1
                              +-----------------------------------------------------------------------------------+
                              |                       OkHttpCall:Call                                             |
                              |                             enqueue()                                             |
                              |                             execute()                                             |
                              |                                                                                   |
                              | methodHandlerCache    adapterFactories       converterFactories  callbackExecutor |
                              | baseUrl                  get()                  get()                             |
                              | client                      responseType()         fromBody()                     |
                              |                             adapt():Call            toBody()                      |
                              +-----------------------------------------------------------------------------------+
                              |                       Retrofit                                                    |
                              +-----------------------------------------------------------------------------------+
                              |adapters: convert:            okhttp|                                              |
                              |   Rxjava  Gson    protobuf   smocks|                                              |
                              |           jackson simplexml        |junit assertj-core mockito guava mockwebserver|
                              |           moshi   wire             |                                              |
                              +------------------------------------+----------------------------------------------+



```
#### 拦截器
Application Interceptor 适用于在请求前统一添加一些公共参数
NetwrokInterceptor 在这一层拦截器中可以获取到最终发送请求的 request ，也可以获取到真正发生网络请求回来的 response 响应，从而修改对应的请求或者响应数据

1. customInterceptor
2. RetryAndFollowInterceptor（错误重试,自动重定向 ）
3. BridgeInterceptor（转化为网络请求）
4. CacheInterceptor（缓存管理）
5. ConnectInterceptor （连接复用）
6. NetworkInterceptor
7. CallServerInterceptor（支持Http/2,gzip压缩响应体）

#### OKIO
 装饰者模式、享元模式、模板模式

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


### 数据交换格式

#### Serial（Twitter）
模板方法 ObjectSerializer 

#### Parcelable
内部类抽象工厂，创建Parcelable对象或数组
  Creator

模板方法
  android.os.Parcelable#writeToParcel

解析式
  Parcel，序列化与反序列化，字节流和对象

1. 交换效率相对Serialization高，使用Jni TLV索引逻辑结构

2. Parcel 数据不能存储在磁盘上或通过网络发送
   
[Binder 事务缓冲区的大小固定有限，目前为 1MB](https://developer.android.google.cn/guide/components/activities/parcelables-and-bundles)
对于 savedInstanceState 的具体情况，应将数据量保持在较小的规模，50k
#### Protocol Buffer
索引逻辑结构。tlv 存储格式，可变长int

构建器 ProtoModel.User#newBuilder()
解析器 ProtoModel.User#parseFrom(byte[])


Android Studio gradle插件 protobuf-gradle-plugin ，编译期间自动地执行 Protocol Buffers 编译器


#### Flatbuffer
[flatbuffer编码的内存的结构](https://blog.csdn.net/weixin_42869573/article/details/83820166)
索引逻辑结构。向量表访问buffer

类型：com.google.flatbuffers.Constants
Table 定义（Table#__reset()）：
      bb_pos=_bb.getInt(0) + 0：Object在buffer位置
      vtable_start = bb_pos - bb.getInt(bb_pos)：回溯到vtable元信息
      vtable_size = bb.getShort(vtable_start)：vtable的长度
Table 结构：
    （root_table：int）+（vtable元信息：长度+内容offset）+（object：offset+length+content）

简单工厂    FbHello#getRootAsFbHello(java.nio.ByteBuffer)
工厂方法    ByteBufferFactory#newByteBuffer
构建器      FlatBufferBuilder

解析器模式  Table；解释ByteBuffer里的复杂类型，如string，Table，
装饰器      flatbuffer 生成的rootTable类；定位object字段在vtable的偏移量
#### Gson
工厂方法  Gson#factories 

适配器 ReflectiveTypeAdapterFactory.Adapter
组合模式 ReflectiveTypeAdapterFactory.Adapter和BoundField

享元 共享工厂类 Gson#factories 
代理 BoundField 代理方法write和read访问reader和writer
装饰 TypeToken装饰token；JsonReader封装inputstream
外观 Gson提供堆外服务

备忘 JsonReader#stack 
解析器 JsonParser,JsonReader

Gson
工厂方法： com.google.gson.Gson#factories用来创建TypeAdapter
适配器模式：com.google.gson.internal.bind.TypeAdapters适配类型解析
装饰模式：com.google.gson.reflect.TypeToken装饰class
##### Mson 优化Gson反射

### 长连接
轮询 Ajax
长轮询 socket（mqtt实现方法） 有消息才返回，长期占用资源
事件驱动/双向通信 websocket

IM：
增加重传和排重机制
回执
### 通讯协议
[通讯协议称谓](https://en.wikipedia.org/wiki/Alice_and_Bob)
字节流协议：TagVersionLengthValue
#### TCP/UDP


```js
+-------------+
|  Http data  |        App layer        应 用 层
+-------------+
+--------------+
|  TCP Header  |
|    Http Data |
|              |       Transport layer  传 输 层
+--------------+
+---------------+
| IP Header     |
|   TCP Header  |     Network layer    网 络 层
|     Http Data |
+---------------+
+-------------------+
| Eth Header        |
|   IP Header       |  Data link layer  链 路 层
|     TCP Header    |
|       Http Data   |
+-------------------+
```

```js
      0      7 8     15 16    23 24    31
      +--------+--------+--------+--------+
      |     Source      |   Destination   |
      |      Port       |      Port       |
      +--------+--------+--------+--------+
      |                 |                 |
      |     Length      |    Checksum     |
      +--------+--------+--------+--------+
      |
      |          data octets ...
      +---------------- ...

          User Datagram Header Format
```
[TCP Header Format](https://tools.ietf.org/html/rfc793#section-3.1)
```js
    0                   1                   2                   3
    0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1
   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
   |          Source Port          |       Destination Port        |
   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
   |                        Sequence Number                        |
   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
   |                    Acknowledgment Number                      |
   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
   |  Data |           |U|A|P|R|S|F|                               |
   | Offset| Reserved  |R|C|S|S|Y|I|            Window             |
   |       |           |G|K|H|T|N|N|                               |
   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
   |           Checksum            |         Urgent Pointer        |
   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
   |                    Options                    |    Padding    |
   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
   |                             data                              |
   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+

                            TCP Header Format
```
TCP层的Flag
```
SYN表示建立连接，
FIN表示关闭连接，
ACK表示响应，
PSH表示有 DATA数据传输，
RST表示连接重置。
URG(urgent紧急)

```
##### 抓包
// testSocket("104.31.70.56");//tcp http://www.plantuml.com/
```plantuml
@startuml
Client -> Server++: [SYN]

Server-> Client : [SYN,ACK]
Server-> Client --: [ACK]
 
Client -> Server++: [PSH,ACK](write bytes)
Client -> Server: [FIN,ACK]
Server-> Client --: [ACK]

Client -> Server++: [FIN,ACK]
Server-> Client  : [FIN,ACK]
Client -> Server --: [ACK]
@enduml
```

TCP协议是一种面向连接的、可靠的、基于字节流的运输层通信协议。
TCP是全双工模式，当主机1发出FIN报文段时，只是表示主机1已经没有数据要发送了
    TCP断开连接时，会有四次挥手过程。
```ascii
三次握手四次挥手
    +-----------+                    +-------------+                +------------+                      +--------------+
    |           |                    |             |                |            |                      |              |
    |  Client   |                    |   Ser^er    |                |   Client   |                      |    Ser^er    |
    |           |                    |             |                |            |                      |              |
    +-----+-----+                    +-------+-----+                +-----+------+                      +-------+------+
Close     |                                  |  Close                     |                                     |
          |      SYN=1 Seq=X                 |                            |        FIN=1 ACK=Z   Seq=X          |
          |   +--------------------------->  |                            |  +------------------------------->  |
          |                                  |                 FIN        |                                     |
          |                                  |                 WAIT-1     |        ACK=X+1 Seq=Z                |
          |                                  |  Listen                    | <------------------------------+    |
          |                                  |                 FIN        |                                     | CLOSE_WAIT
          |      SYN=1 ACK=X+1 Seq=Y         |                 WAIT-2     |        FIN=1 ACK=X Seq=Y            |
          |   <--------------------------+   |                            |                                     |
          |                                  |                            |  <-----------------------------+    |
          |                                  |                            |                                     | CLOSE_WAIT
SYN_SENT  |                                  |                            |                                     |
          |      ACK=Y+1 Seq=Z               |                            |        ACK=Y Seq=X                  |
          |    +-------------------------->  |  SYN_RCVN                  |  +----------------------------->    | LAST_ACK
          |                                  |                TIME WAIT   |                                     |
          |                                  |                            |                                     |
          |                                  |                            |   2MSL                              |
          +                                  +                            +                                     +
     ESTABLISHED                          ESTABLISHED                  Closed                                Closed

```

#### HTTP
```js
请求行（例如GET /images/logo.gif HTTP/1.1，表示从/images目录下请求logo.gif这个文件）
请求头（例如Accept-Language: en）
空行
其他消息体
```
快（缓存，字节），穩定/可靠（长连接，多路复用）
1989，由蒂姆·伯纳斯-李在欧洲核子研究组织（CERN）所发起。
1991，HTTP V0.9 ，支持 GET 请求
1996，HTTP/1.0 RFC 1945，支持GET、HEAD 和 POST ，为每个资源请求单独连接到同一服务器
1997，HTTP/1.1首次记录在 RFC 2068 中，支持9种请求方法（PATCH 后来补充），多次重用连接，支持Range请求数据，ETag判断是否资源过期。
2015，HTTP/2(RFC 7540)，“在线”语义，服务端推送，管线化，多路复用；头部压缩，cache header
2019，HTTP/3，基于UDP的 QUIC，TCP 多路复用，丢包或重排序导致所有活动事务遇到停滞，即"线头阻塞"；QUIC 本机多路复用，丢包只会影响数据丢失的流。
##### HTTP 1.0
Connection:keep-alive：一次 TCP 连接中完成多个 HTTP 请求，但是对每个请求仍然要单独发 header；
js 轮询：polling 是指从客户端（一般就是浏览器）不断主动的向服务器发 HTTP 请求查询是否有新数据

##### HTTP 1.1

默认采用持续连接（Connection: keep-alive），关闭 Connection:keep-alive。避免重复TCP初始握手活动，减少网络负荷和响应周期
[HTTP 1.1 RCF](https://tools.ietf.org/html/rfc2068)
[HTTP/1.1](https://httpwg.org/specs/rfc7230.html)
  1. 缓存控制策略例如Entity tag，If-Unmodified-Since, If-Match, If-None-Match
  2. 支持长连接
[websocket] 基于HTTP/1.1
  1. 第一次升级http协议，切换成功后，全双工通讯。
请求头第一个强制Host：
Host: 123.207.136.134:9010

#### HTTP/2
 [HTTP/2](https://httpwg.org/specs/rfc7540.html) 
 减少 网络延迟
  1. 对HTTP头字段进行数据压缩(即HPACK算法)，且双方cache一份header fields表
  2. 支持服务端推送（只能主动将资源推送到客户端缓存，适合用于展示实时数据；WebSocket实时双向通信）
  3. 请求管线化
  4. 多路复用（SocketChannel +Epoll机制 实现），让多个请求合并在同一 TCP 连接内。
   （第一次升级http协议，切换成功后，然后面向字节流，每个用户分配streamId 通道）


```js
Frame format
 +-----------------------------------------------+
 |                 Length (24)                   |
 +---------------+---------------+---------------+
 |   Type (8)    |   Flags (8)   |
 +-+-------------+---------------+-------------------------------+
 |R|                 Stream Identifier (31)                      |
 +=+=============================================================+
 |                   Frame Payload (0...)                      ...
 +---------------------------------------------------------------+
                   Frame Layout
```

##### SSL/TLS
[SSL/TLS Handshake](http://blog.fourthbit.com/2014/12/23/traffic-analysis-of-an-ssl-slash-tls-session/#:~:text=Record%20Protocol%20format.%20The%20TLS%20Record%20header%20comprises,header%20itself%29.%20The%20maximum%20supported%20is%2016384%20%2816K%29.)
###### TLS Handshake
```js
               TLS Handshake

               +-----+                              +-----+
               |     |                              |     |
               |     |        ClientHello           |     |
               |     o----------------------------> |     |
               |     |                              |     |
       CLIENT  |     |        ServerHello           |     |  SERVER
               |     |       [Certificate]          |     |
               |     |    [ServerKeyExchange]       |     |
               |     |    [CertificateRequest]      |     |
               |     |      ServerHelloDone         |     |
               |     | <----------------------------o     |
               |     |                              |     |
               |     |       [Certificate]          |     |
               |     |     ClientKeyExchange        |     |
               |     |    [CertificateVerify]       |     |
               |     |   ** ChangeCipherSpec **     |     |
               |     |         Finished             |     |
               |     o----------------------------> |     |
               |     |                              |     |
               |     |   ** ChangeCipherSpec **     |     |
               |     |         Finished             |     |
               |     | <----------------------------o     |
               |     |                              |     |
               +-----+                              +-----+
 Optional messages
 --------------------------------------------------------------------------------------------
 Certificate (server)     needed with all key exchange algorithms, except for anonymous ones.
 ServerKeyExchange        needed in some cases, like Diffie-Hellman key exchange algorithm.
 CertificateRequest       needed if Client authentication is required.
 Certificate (client)     needed in response to CertificateRequest by the server.
 CertificateVerify        needed if client Certificate message was sent.
```
###### Record Protocol format
```js
        Record Protocol format

         record type (1 byte)
        /
       /    version (1 byte major, 1 byte minor)
      /    /
     /    /         length (2 bytes)
    /    /         /
 +----+----+----+----+----+
 |    |    |    |    |    |
 |    |    |    |    |    | TLS Record header
 +----+----+----+----+----+


 Record Type Values       dec      hex
 -------------------------------------
 CHANGE_CIPHER_SPEC        20     0x14
 ALERT                     21     0x15
 HANDSHAKE                 22     0x16
 APPLICATION_DATA          23     0x17


 Version Values            dec     hex
 -------------------------------------
 SSL 3.0                   3,0  0x0300
 TLS 1.0                   3,1  0x0301
 TLS 1.1                   3,2  0x0302
 TLS 1.2                   3,3  0x0303
```
Byte 0: TLS record type
Bytes 1-2: TLS version (major/minor)
Bytes 3-4: Length of data in the record (excluding the header itself). The maximum supported is 16384 (16K).
###### Handshake Protocol format

```js

                           |
                           |
                           |
         Record Layer      |  Handshake Layer
                           |                                  |
                           |                                  |  ...more messages
  +----+----+----+----+----+----+----+----+----+------ - - - -+--
  | 22 |    |    |    |    |    |    |    |    |              |
  |0x16|    |    |    |    |    |    |    |    |message       |
  +----+----+----+----+----+----+----+----+----+------ - - - -+--
    /               /      | \    \----\-----\                |
   /               /       |  \         \
  type: 22        /        |   \         handshake message length
                 /              type
                /
           length: arbitrary (up to 16k)


   Handshake Type Values    dec      hex
   -------------------------------------
   HELLO_REQUEST              0     0x00
   CLIENT_HELLO               1     0x01
   SERVER_HELLO               2     0x02
   CERTIFICATE               11     0x0b
   SERVER_KEY_EXCHANGE       12     0x0c
   CERTIFICATE_REQUEST       13     0x0d
   SERVER_DONE               14     0x0e
   CERTIFICATE_VERIFY        15     0x0f
   CLIENT_KEY_EXCHANGE       16     0x10
   FINISHED                  20     0x14
```

#### HTTP/HTTP2 历史
 
[wiredshark抓包](https://blog.csdn.net/u014530704/article/details/78842000)
1. http contains  baidu.com （SSL 过滤 https）
2.  ip.src==47.95.165.112 or ip.dst==47.95.165.112

> HTTP/2。HTTP/2**支持明文传输**，而SPDY强制使用HTTPS；HTTP/2消息头的压缩算法采用HPACK，而非SPDY采用的DEFLATE。
相较于HTTP1.1，HTTP/2的主要优点有采用二进制帧封装，传输变成多路复用，流量控制算法优化，服务器端推送，首部压缩，优先级等特点。
> Hypertext Transfer Protocol Secure (HTTPS) is an extension of the Hypertext Transfer Protocol (HTTP) for secure communication over a computer network, and is widely used on the Internet.（Https使用了安全协议SSL）

> SSL更新到3.0时，IETF对SSL3.0进行了标准化，并添加了少数机制(但是几乎和SSL3.0无差异)，标准化后的IETF更名为TLS1.0(Transport Layer Security 安全传输层协议)，可以说TLS就是SSL的新版本3.1。

SSL使用40 位关键字作为RC4流加密算法，这对于商业信息的加密是合适的。

##### HTTP/2 前身 NPN和ALPN
NPN（Next Protocol Negotiation，下一代协议协商），是一个 TLS 扩展，由 Google 在开发 SPDY 协议时提出。随着 SPDY 被 HTTP/2 取代，NPN 也被修订为 ALPN（Application Layer Protocol Negotiation，应用层协议协商）。二者目标一致，但实现细节不一样，相互不兼容。以下是它们主要差别：

NPN 是服务端发送所支持的 HTTP 协议列表，由客户端选择；而 ALPN 是客户端发送所支持的 HTTP 协议列表，由服务端选择；
NPN 的协商结果是在 Change Cipher Spec 之后加密发送给服务端；而 ALPN 的协商结果是通过 Server Hello 明文发给客户端；

[](https://imququ.com/post/enable-alpn-asap.html)
####  WebSocket
TCP三次握手后，HTTP/1.1 升级协议 **Upgrade: websocket**
[HTTP1.1 握手（升级后），WebSocket frame 格式](https://tools.ietf.org/html/rfc6455#section-5.2)
[](https://github.com/abbshr/abbshr.github.io/issues/22)
[org.java-websocket:Java-WebSocket:1.3.0@jar 使用 SelectorProvider.provider().openSocketChannel()](org.java_websocket.WebSocketImpl) 
```js
      0                   1                   2                   3
      0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1
     +-+-+-+-+-------+-+-------------+-------------------------------+
     |F|R|R|R| opcode|M| Payload len |    Extended payload length    |
     |I|S|S|S|  (4)  |A|     (7)     |             (16/64)           |
     |N|V|V|V|       |S|             |   (if payload len==126/127)   |
     | |1|2|3|       |K|             |                               |
     +-+-+-+-+-------+-+-------------+ - - - - - - - - - - - - - - - +
     |     Extended payload length continued, if payload len == 127  |
     + - - - - - - - - - - - - - - - +-------------------------------+
     |                               |Masking-key, if MASK set to 1  |
     +-------------------------------+-------------------------------+
     | Masking-key (continued)       |          Payload Data         |
     +-------------------------------- - - - - - - - - - - - - - - - +
     :                     Payload Data continued ...                :
     + - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - +
     |                     Payload Data continued ...                |
     +---------------------------------------------------------------+
```
 
[在线测试及WireShark捕获](http://coolaf.com/tool/chattest)
```js
客户端（请求升级/握手 websocket）：
GET /ajaxchattest HTTP/1.1
Host: 123.207.136.134:9010
Connection: Upgrade
Pragma: no-cache
Cache-Control: no-cache
User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/85.0.4183.83 Safari/537.36 Edg/85.0.564.44
Upgrade: websocket
Origin: http://coolaf.com
Sec-WebSocket-Version: 13
Accept-Encoding: gzip, deflate
Accept-Language: zh-CN,zh;q=0.9,en;q=0.8,en-GB;q=0.7,en-US;q=0.6
Sec-WebSocket-Key: PJ4FKzlQx176GkF30UIu1Q==
Sec-WebSocket-Extensions: permessage-deflate; client_max_window_bits

服务端（升级协议）
HTTP/1.1 101 Switching Protocols
Upgrade: websocket
Connection: Upgrade
Sec-WebSocket-Accept: ps47fOR7Y0K9tgpbHog3Zw6H7/4=

升级后，8s左右心跳包，服务端【TCP Keep-Alive】,客户端【TCP Keep-Alive ACK】

发送数据，客户端发送【WebSocket FIN/MASKED】,服务器接受响应【Websocket FIN】

断开连接
  客户端发送【TCP PSH，ACK】0x88804e30bea2（字符串：xxNOxx），服务端相应【TCP ACK】
  服务端发送【TCP PSH，ACK】0x..454f46（字符串：...EOF）
  客户端发送【TCP FIN，ACK】
  客户端发送【TCP ACK】
  服务端发送【TCP ACK】
  服务端发送【TCP ACK】
```
#### WebRTC
[](知识体系-平台-多媒体.md)

### 即时聊天协议
[即时聊天协议](https://blog.csdn.net/netease_im/article/details/83823212)
#### vcard 通讯录名片

### moquette Broker
[协议](知识体系-存储-Networks.md)

#### 消息类型
```c
"lib\mqtt3_protocol.h"

/* Message types */
#define CONNECT 0x10
#define CONNACK 0x20

#define SUBSCRIBE 0x80
#define SUBACK 0x90
#define UNSUBSCRIBE 0xA0
#define UNSUBACK 0xB0
#define PINGREQ 0xC0
#define PINGRESP 0xD0
#define DISCONNECT 0xE0

#define PUBLISH 0x30
#define PUBACK 0x40
#define PUBREC 0x50
#define PUBREL 0x60
#define PUBCOMP 0x70
```
#### 消息体
```c
"lib\mosquitto_internal.h"
struct _mosquitto_packet{
	uint8_t command;
	uint8_t have_remaining;
	uint8_t remaining_count;
	uint16_t mid;
	uint32_t remaining_mult;
	uint32_t remaining_length;
	uint32_t packet_length;
	uint32_t to_process;
	uint32_t pos;
	uint8_t *payload;
	struct _mosquitto_packet *next;
};
```
#### [qos](https://www.jianshu.com/p/ff04cbb21865)
Qos衰减：以发布Qos为准，如果订阅qos小于发布Qos，接受方Qos以订阅Qos为准
 QoS 0
    消息偶尔丢失
```plantuml
@startuml
Publisher -> Broker: publish(Qos0,msg)
Broker -> Subscriber: publish(Qos0,msg)

Publisher -> Publisher : Delete Message
@enduml
```
Qos 1
    需要应用层处理重复消息
```plantuml
@startuml
Publisher -> Publisher : Store Message
Publisher -> Broker: publish(Qos1,msg)

Broker -> Broker : Store Message
Broker -> Subscriber: publish(Qos1,msg)
Broker -> Publisher: pubback
Publisher -> Publisher : Delete Message
Subscriber -> Broker: pubback
Broker -> Broker : Delete Message
@enduml
```
Qos 2
（消息的丢失会造成生命或财产的损失），且不希望收到重复的消息
```plantuml
@startuml
Publisher -> Publisher : Store Message
Publisher -> Broker: publish(Qos2,msg)

Broker -> Broker : Store Message
Broker -> Subscriber: publish(Qos2,msg)

Subscriber -> Broker: pubrec
Broker -> Subscriber: pubbrel

Broker -> Publisher: pubrec
Publisher -> Broker: pubbrel

Subscriber -> Broker: pubcomp
Subscriber -> Subscriber : Store Message
Broker -> Broker : Delete Message

Broker -> Publisher: pubcomp

Publisher -> Publisher : Delete Message
Subscriber -> Subscriber: push message
Subscriber -> Subscriber: Delte message

@enduml
```
数据完整性与及时性要求较高的银行、消防、航空等行业。

### xmpp
openfire
asmack
#### XMPP通信原语有3种：message、presence和iq。

#### 缺点
xml传输，二进制需要转化Base64
