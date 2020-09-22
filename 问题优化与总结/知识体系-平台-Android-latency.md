## 精简
[square](https://github.com/square?q=android&type=&language=)

常见优化库
LeakCanary、Glide、Retrofit、OkHttp、RxJava、GreenDAO
[Studio Profiler](https://github.com/JetBrains/android/tree/master/profilers/src/com/android/tools/profilers/memory)
### 数据存储
SharedPreferences,文件存储,SQLite数据库方式,内容提供器（Content provider）,网络

ContentProvider->保存和获取数据，并使其对所有应用程序可见

### SharedPreference
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
                |                             |                                                          |
                +-----------------------------+                                                          |
                |                             |                                                          |
                | KXmlParser: XmlPullParser   |                                                          |
                ------------------------------+----------------------------------------------------------+


```

###  MMKV for Android


## 包内精简 - APK打包 （编译，打包，优化，签名，安装）
  [包大小](https://mp.weixin.qq.com/s/_gnT2kjqpfMFs0kqAg4Qig?utm_source=androidweekly.io&utm_medium=website)

gradle,Transform的应用
批量打包
```打包流程
G: gradle build tools
B: android build tools
J: JDK tools


+--------------------------------------------------------------------------------------+
| /META-INF                                                                            |
| /assets                                                                              |
| /res                                                                                 |
| /libs                                                                                |
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
|   ApkBuilder (so,FontCreator)                                                        |
+--------------------+                          +--------------------------------------+
|B                   |                          |B                                     |
|  linker            |                          |    dex                               |
+--------------------------------------------------------------------------------------+
|B                   |B                         |G             +-----------------------+
|  bcc compat        |   AAPT                   |    proguard  |          Preveirfy    |
|                    |  (Lint,TinyPNG,tintcolor)|              |          Obfuscate    |
|                    |  (WebP,svg)              |              |          Optimize     |
|                    |                          |              |          Shrink       |
|                    |                          |              +-----------------------+
+--------------------+                          +--------------------------------------+
|B                   |                          |        J                             |
|  llvm-rs-cc        |                          +-------+    javac                     |
|                    |                          | R.java|                              |
|                    +--------------------------+--------------------------------------+
|                    |G                                 |B                             |
|                    | menifest/assets/resource merger  |    aidl                      |
+--------------------+-----------------------------------------------------------------+

                                                                                              

```

walle
```
+---------------------------------------------------------------------------------------------+
|                                        walle-cli                                            |
|                                                                                             |
+---------------------------------------------------------------------------------------------+
|                         jcommander                                                          |
|                                                               Batch2Command                 |
|                                                                                             |
+---------------------------------------------------------------------------------------------+
|                                                                                             |
|                      ChannelReader                ChannelWriter                             |
|                                                                                             |
|                                                                                             |
|                      PayloadReader                                                          |
|                                                                                             |
+---------------------------------------------------------------------------------------------+
|                      ApkUtil                                                                |
|                         findApkSigningBlock()                                               |
+---------------------------------------------------------------------------------------------+

```

### 打包自动化
[gradle.build(ant-javacompiler;ivy;maven-repo;groovy-asm-parseclass;jetbrains-kotlin-gradle-plugin;android-gradle-plugin ) dex2jar,jd-gui,apktool)](..\问题优化与总结\知识体系-DSL-gradle.md)
### [gradle sdl ](https://source.codeaurora.cn/quic/la/platform/tools/base)

## 缓存篇

### Bitmp
简单工厂 Bitmap（无数据源）：wrapHardwareBuffer，createScaledBitmap/createBitmap 
        BitmapFactory（有数据源）：decodeFile（decodeResource/decodeResourceStream）/decodeStream，decodeByteArray，decodeFileDescriptor

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
### “零拷贝问题” - MMKV- sharepreference优化
mmap（微信mars，美图logan，网易）




## 网络
###  Rxjava，线程切换 ，异步执行耗时代码
流式构建，订阅及观察事件传递
flowable，observable，single，maybe，completable，mixed，parallel
响应式设计模式：
  异步方法调用：
  回调；
  集合管道：filter、map和reduce
  流接口：编写具有自然语言一样可读性的代码，即方法链
  基于事件的异步模式：执行多个操作，每个操作完成后接收通知
  事件驱动的架构：
  毒丸模式：
  生产者消费者：
  承诺模式：
  Reactor模式：处理一个或多个客户端同时传递到应用程序的服务请求


[rx操作符](http://reactivex.io/documentation/operators.html)
ReactiveX provides a collection of operators with which you can filter, select, transform, combine, and compose Observables.
```
定时分发：倒计时
过滤：搜索防抖/点击防抖
数据/流切换：缓存优先
异常处理：token处理，重试机制
调度：线程调度，时间调度，生命周期管理及观察
+--------------------------------------------------------------------------------------------------------------------------------------------------------------+
|  Create/Defer⭐  Debounce/throttleFirst⭐                                           SubscribeOn                                                            |
|  Just/From       Distinct                                                             /ObserveOn⭐                                                           |
|  Empty           First/Last                       Map/FlatMap                        Backpressure                                                             |
|  /Never          ElementAt          All           /ConcatMap⭐                        Subscribe                                                             |
|  /*Throw         IgnoreElement      *Amb          Window                                Delay/Timeout   And/Then/When                                           |
|                                  Contains         Buffer                               TimeInterval     CombineLatest⭐  Concat⭐                              | 
| Interval/Timer⭐ Filter         DefaultIfEmpty                                          /Timestamp        Merge            *Average                            |            
|                  Sample        *SequenceEqual                                                                StartWith     Count                               |
|  Range           Skip/Take                                                             Serialize          *Join            *Max                   Connect        |
|  *Repeat         TakeLast/TakeLastTimed           Scan           onErrorResumeNext⭐    Materialize      *Switch         *Min                  Publish         |
|  *Start          TakeLastOne                      GroupBy          Catch              /Dematerialize    *Zip            Reduce                RefCount          |
|                  /TakeUntilPredicate        switchIfEmpty⭐     retryWhen⭐            Using             *Sum                                   Replay     to  |
|                  TakeUntil/TakeWhile⭐                                                Do                                                                       |
+-----------------------------------------------------------------------------------------------------------------------------------------------------------------+
|  Creating        Filtering     Conditional   Transforming      Error                  Utility        Combining       Mathematical                               |
|                                and Boolean                     Handling                                              and Aggregate          Connectable  Con^ert|
+-----------------------------------------------------------------------------------------------------------------------------------------------------------------

```

静态代理
    构建时，Observable静态代理上游订阅方法subscribe
    subscribe订阅时，upstream订阅，Observer静态代理下游的观察者方法


门面模式 Observer,flowable,single,maby,completation

适配器   
静态代理 
      
      Observable 代理上游方法，Observer代理下游方法

命令 Publisher#subscribe，ObservableSource#subscribe
观察者模式 rx.Observer#onNext；rx.Observer#onCompleted；
策略 BackpressureStrategy（MissingEmitter，ErrorAsyncEmitter，DropAsyncEmitter...)

装饰者
      创建操作类，添加构造参数，装饰Observable，增加 subscribeActual 功能
      过滤操作类，装饰观察者类，上游可以发送数据时，下游的观察类，onNext增加功能
      Call CallEnqueueOnSubscribe#call()


模板方法
        flowable，observable，
        single，completable，maybe，
        mixed，parallel

#### 源码
RxJava2.0是非常好用的一个异步链式库,响应式编程，遵循观察者模式。
```
  001_Jersey              697fd66aae9beed107e13f49a741455f1d9d8dd9 Initial commit, working with Maven Central
* 002_rx                  87cfa7a445f7659ef46d1a6a4eb38daa46f5c97a Initial commit
  003_Observer_Observable 2a4122c11b95eaa3213c2c3e54a93d28b9231eec Rename to Observer/Observable
  004_refactoring         9d48f996e4ee55e89dc3c60d9dd7a8d644316140 Refactoring for consistent implementation approach.
                         Observable静态代理，Observer适配器模式，Func通过桥接Observer的具体实现，operations为Observable的子类
                        +----------------------------------------------------------------------------+ 
                        |AtomicObserverSingleThreaded AtomicObserverMultiThreaded                    |decorate pattern
                        |                      AtomicObserver                                        |state pattern
                        |  ToObservableIterable                                                      |
                        +----------------------------------------------------------------------------+ 
                        |Observable<T>           Observer<T>       Subscription                      |
                        |  subscribe(Observer<T>)   onCompleted()      unsubscribe()                 |
                        |                            onError()                        Func0<R>       |
                        |                            onNext(T)     Notification<T>     call():R      | 
                        +----------------------------------------------------------------------------+
                        |                     reactive                           functions operations| 
                        +----------------------------------------------------------------------------+
  005_languageAdapter     f57a242b17f1214142dea97c0cb9049b106378a0 LanguageAdaptor for Function execution
  006_readme              fbbac394fbc2e0af4ef3a507ad3e15dd18bfb10c Create README.md
  007_gh-pages            6797c4384d91a2567cf0b429ccbd8053c72b716f Create gh-pages branch via GitHub
  008_performace          787d8fc0215c5bab541f61c2d69a91753d462559 Refactoring towards performance improvements
                          gradle
  009_example             b61b7607e0f2b00a0d36672a95999f1fb081dbf1 Start of examples with clojure and groovy
  010_readme              10fe96474a6ab4ec62c7cab50fb376a173bda78e Create README.md
  012_0.1.2               74da0769266a8fd5832e558f1a6e0081895b9201 Gradle Release Plugin - pre tag commit:  'rxjava-0.1.2'.
  013_takeLast                      4c5c41364411e062d5d71f22ce311700d045821b TakeLast basic implementation
  014_RxJavaPlugins                 ee001549ef06f17f38139b4cfecd4ce4445ecb6d RxJavaErrorHandler Plugin
  015_operationNext                 d72c5892774a2c67327e07943f6b92416317871f Implemented Operation Next
  062_schedulers                    dfc784126f259361f01f1927f44f5d1aa4e49a43 Naive schedulers implementation
  063_RxJavaObservableExecutionHook c2a40bd1a391edf7f8b71965ca20fa84c72c0bb4 RxJavaObservableExecutionHook
  064_multicast                     0499cffcb53f928a5083f701603ccf3ec3c81c60 Multicast implemented
  065_sample                        1aa722d3379df88d05c9455d7630b7236edb9d9b Merge pull request #248 from jmhofer/sample
  066_throttle                      2ea065c0ef22ea7cf58e9fb6d6f24c69f365bed6 Created and wired an implementation for the throttle operation on Observables.
  067_AndroidSchedulers             3919547f1e5f7940974e383f4f573e48cac7e09b Expose main thread scheduler through AndroidSchedulers class
  068_window                        5789894638a62ac17b5276053e3bea8bdd570580 Merge window operator commit to master
  069_debound_throttleWithTimeout   5fabd5883561ff18b18b0d1dfb7001e2959cb11d Use 'debounce' as proper name for ThrottleWithTimeout which unfortunately is the poorly named Rx Throttle operator.
  070_ApachHttpAsyncClient          db2e08ca039c59d51349255c4b8b3c65b26d52de Observable API for Apache HttpAsyncClient 4.0
  071_AndroidObservables            715dcece5c781c394b59f86e32f1f514fc9f7a31 Drop use of WeakReferences and use a custom subscription instead
  072_backwards                     abff40fd0a40bee4f97b0363014e98aecb50d7ff Backwards compatible deprecated rx.concurrency classes
  073_single                        96064c37af520de375929ae8962c527dd869ad57 Implement the blocking/non-blocking single, singleOrDefault, first, firstOrDefault, last, lastOrDefault
  074_subscribeOn                   89bb9dbdf7e73c8238dc4a92c8281e8ca3a5ec53 Reimplement 'subscribeOn' using 'lift'
  075_observeOn                     9e2691729d94f00cde97efb0b39264c2f0c0b7f5 ObserveOn Merge from @akarnokd:OperatorRepeat2
  076_RxJavaSchedulers              d07d9367911d8ec3d0b65846c8707e0a41d1cf1f RxJavaSchedulers Plugin
  077_math-module                   68d40628a78db18ecb49d880798f1a03551ccd59 math-module
  078_operatorSerialize             4427d03db0d8ba67137654447c6c7a57615c0b00 OperatorSerialize
* 079_contrib-math                  26a4a1a05a6cc367061639fb19bfdda9d5b97fab Operators of contrib-math
  100_1.0.0-rc.1          6de88d2a39e2ae84744cd1f350e28bef4de7dacb Travis build image
                        observeOn 切换了消费者的线程，因此内部实现用队列存储事件。
                        +-----------------------------------------------------------------------+-------------------+
                        |AtomicObserverSingleThreaded AtomicObserverMultiThreaded               |                   |
                        |                      AtomicObserver                                   |                   |
                        |  ToObser^ableIterable                                                 |OperatorSubscribeOn|
                        +-----------------------------------------------------------------------+                   |
                        |ObservablevTv           ObservervTv       Subscription                 |OperatorObserveOn  |
                        |  subscribe(Observer<T>)    onCompleted()     unsubscribe()            |                   |
                        |  observeOn(Scheduler)      onError()                        Func0<R>  +-------------------+
                        |                            onNext(T)     Notification<T>     call():R |      schedulers   |
                        |                                                                       |                   |
                        +-------------------------------------------------------------------------------------------+
                        |                     reactive                                          |      plugins      |
                        +-----------------------------------------------------------------------+-------------------+
                        |                                         functions operations                              |
                        +-------------------------------------------------------------------------------------------+

  200_v2.0.0-RC1          fa565cb184d9d7d45c257afa1fbbec6ab488b1cf Update changes.md and readme.md
                        Disposable装饰Observer，使得Observer可以销毁。ObservableSource,CompletableSource， MaybeSource<T>,SingleSource<T>,FlowableProcessor。
                        io.reactivex.Flowable：发送0个N个的数据，支持Reactive-Streams和背压。Flowable.subscribe(4 args)
                        io.reactivex.Observable：发送0个N个的数据，Rx 2.0 中，不支持背压，Observable.subscribe(4 args)
                        io.reactivex.Single：只能发送单个数据或者一个错误，不支持背压。 Single.toCompletable()
                        io.reactivex.Completable：没有发送任何数据，但只处理 onComplete 和 onError 事件，不支持背压。 Completable.blockingGet()
                        io.reactivex.Maybe：能够发射0或者1个数据，要么成功，要么失败，不支持背压。Maybe.toSingle(T)
                        RxJavaPlugins 转化Obserable.
                        +----------------------------------------------------------------------------+---------------+-------------------+
                        |  FromArrayDisposable                                                       |               |                   |
                        |  ObservableFromArray                                                       |               |                   |
                        +----------------------------------------------------------------------------+               |                   |
                        |                             RxJavaPlugins                                  |               |                   |
                        |                                      apply(Function<T, R> f, T t)          |               |                   |
                        +----------------------------------------------------------------------------+               |                   |
                        |ObservablevTv:ObservableSource          ObservervTv           Disposable    |               |OperatorSubscribeOn|
                        |             subscribe(Observer<T>)         onCompleted()       dispose()   |               |                   |
                        |   subscribeWith(Observer<T>)               onError()           isDisposed()|Function<T, R> |OperatorObserveOn  |
                        |   subscribeActual(ObservervT>)             onNext(T)                       | apply(T):R    |                   |
                        |                                            onSubscribe(Disposable)         |               +-------------------+
                        |                                                                            +---------------+      schedulers   |
                        |                             RxJavaPlugins                                  |   functions   |                   |
                        +----------------------------------------------------------------------------+-----------------------------------+
                        |                                         functions operations                               |      plugins      |
                        +----------------------------------------------------------------------------------------------------------------+
                        |                     reactive-streams      jmh                                              |                   |
                        +--------------------------------------------------------------------------------------------+-------------------+

* 300_v3.0.0-RC0          fb37226be292c8ee0934311f8ca2f139dfd0dc5a 3.x: remove no-arg, dematerialize(); remove replay(Scheduler) variants (#6539)





[reactivestreams](https://github.com/reactive-streams/reactive-streams-jvm.git)
  001_scala           e58fed62249ad6fbd36467d1bbe5c486f31a8c0e Initial implementation
  020_java            6e5c63f3492524c21fda0bc1a08d72b52c9e9692 Reimplements the SPI and API in Java
  021_tck_java        3e67969207994b8e34ff49dffbf9fe3ac2d51284 !tck #12 Migrated TCK to plain Java
  022_api_spi_example f3a43863bd7c370a1f292a456eddcd1e9d226738 API/SPI Combination and Examples
* 101_gradle          dddbd3a52fcb7d71059a30760b5e77127c785c7c fix #96
                        +-------------------------------------------------------------------+--------+
                        |                                                                   |        |
                        |        Processor<T, R> :Subscriber<T>, Publisher<R>               |        |
                        +-------------------------------------------------------------------+        |
                        |                                                                   |        |
                        |  Publisher<T>                Subscriber<T>          Subscription  |        |
                        |     subscribe(Subscriber<T>)    onSubscribe()           cancel()  |        |
                        |                                 onNext(T)                         |        |
                        |                                 onComplete()                      |        |
                        |                                 onError(Throwable)                |        |
                        +-------------------------------------------------------------------+        |
                        |                       api                                         |  tck   |
                        +-------------------------------------------------------------------+--------+

```

+----------------------------------------------------------------------------------+
|                                                                                  |
|       subscribOn()//upstream first effict                                        |
|                                                                                  |
|       observeOn()//downstream effect                                             |
|                                                                                  |
|                          newThread():Scheduler     HandlerScheduler:Scheduler    |
|                                                                                  |
|                          ScheduledFutureTask       ScheduledRunnable:Runnable    |
|                                                                                  |
+----------------------------------------------------------------------------------+

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
工厂方法 
        Call.Factory#newCall(): Call //构造请求回调
构建器 
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
#### RPC - Protocol Buffer
tlv 存储格式，可变长int

构建器 ProtoModel.User#newBuilder()
解析器 ProtoModel.User#parseFrom(byte[])
#### Flatbuffer
[flatbuffer编码的内存的结构](https://blog.csdn.net/weixin_42869573/article/details/83820166)
向量表访问buffer

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

##### Mson 优化Gson反射

### 长连接
轮询 Ajax
长轮询 socket（mqtt实现方法） 有消息才返回，长期占用资源
事件驱动/双向通信 websocket

IM：
增加重传和排重机制
回执
### 通讯协议
#### TCP/UDP

[](https://tools.ietf.org/html/rfc793#section-3.1)
```
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
#### HTTP
快（缓存，字节），穩定/可靠（长连接，多路复用）
[HTTP/1.1](https://httpwg.org/specs/rfc7230.html)
  1. 缓存控制策略例如Entity tag，If-Unmodified-Since, If-Match, If-None-Match
  2. 支持长连接
[websocket] 基于HTTP/1.1
  1. 第一次升级http协议，切换成功后，全双工通讯。
   [WebSocket传输的数据：Frame（帧）](https://tools.ietf.org/html/rfc6455#section-5)
   [](https://github.com/abbshr/abbshr.github.io/issues/22)
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
[HTTP/2](https://httpwg.org/specs/rfc7540.html) 
  1. 第一次升级http协议，切换成功后，然后面向字节流
  2. head压缩，且双方cache一份header fields表
  3. 多路复用（SocketChannel +Epoll机制 实现）
  4. 支持服务端推送（只能主动将资源推送到客户端缓存，适合用于展示实时数据；WebSocket实时双向通信）
  

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
#define PUBLISH 0x30
#define PUBACK 0x40
#define PUBREC 0x50
#define PUBREL 0x60
#define PUBCOMP 0x70
#define SUBSCRIBE 0x80
#define SUBACK 0x90
#define UNSUBSCRIBE 0xA0
#define UNSUBACK 0xB0
#define PINGREQ 0xC0
#define PINGRESP 0xD0
#define DISCONNECT 0xE0
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
Qos 1
    需要应用层处理重复消息
Qos 2
（消息的丢失会造成生命或财产的损失），且不希望收到重复的消息
数据完整性与及时性要求较高的银行、消防、航空等行业。

### xmpp
openfire
asmack
#### XMPP通信原语有3种：message、presence和iq。


## Sqlite
### h2 /JOOQ/SnakeYAML 
### xutils
[xutils view,img,http,orm](https://github.com/zhuer0632/xUtils.git)

### 缓存GreenDAO /Jetpack-Room

### Mqtt服务器
## 多媒体
[zxing, ffmpeg]()
[ ffmpeg](知识体系-平台-多媒体.md)
