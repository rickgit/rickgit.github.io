
## 

常见优化
ButterKnife、Dagger2、Router、eventbus
## 服务
IntentService
JobService




## 组件化
组件指的是单一的功能组件，如 [视频组件]、[支付组件] 等，每个组件都可以以一个单独的 module 开发，并且可以单独抽出来作为 SDK 对外发布使用。
- 组件单独调试
- 组件间界面跳转 Arouter


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

### 通讯 EventBus
intent,接口，aidl，handler，broadcast，其他第三方
livedata
### 数据路由/通讯 - ARouter
- apt技术
- spi技术
 
```
  101_1.0.1 57b46dca65c329d7500c8cc9507fcb2f264cd3d2 First commit.

            +------------------------------------------------------------------------------------------------------------------------------+
            |[arouter-annotation]                                                                                                          |
            |  @Route      RouteType                        @Interceptor                                                                   |
            |     path()      [ACTIVITY,SERVICE,PROVIDER,                                                                                  |
            |                  CONTENT_PROVIDER,BOARDCAST,                                                                                 |
            |  @Param           METHOD,FRAGMENT,UNKNOWN]                                                                                   |
            |                                                                                                                              |
            +------------------------------------------------------------------------------------------------------------------------------+
            |[arouter-compiler]                                |[arouter-api]                                                              |
            |                                                  | ARouter                                                                   |
            |   RouteProcessor        InterceptorProcessor     |   init()                        build():Postcard     navigation(service:T)|
            |                                                  |                                                         :T                |
            +--------------------------------------------------|---------------------------------------------------------------------------|
            |   ARouter$$Root$$M      ARouter$$Interceptors$$M |                                 Postcard                                  |
            |        :IRouteRoot            :IInterceptorGroup |                                    navigation() getExtras()               |
            |   ARouter$$Group$$M                              |---------------------------------------------------------------------------+
            |        :IRouteGroup                              | LogisticsCenter                                      //PathReplaceService |
            |                                                  |   groupsIndex:Map                                    //DegradeService     |
            |   ARouter$$Providers$$M                          |   interceptorsIndex:Map                                                   |
            |         :IProviderGroup                          |   providersIndex:Map                                                      |
            |                                                  |   executor:ThreadPoolExecutor                                             |
            |                                                  |                                                                           |
            |                                                  |   init()//load gen class                                                  |
            |                                                  |                                                                           |
            |                                                  |                                                                           |
            |                                                  |   interceptors:List               interceptions(post:PostCard,callback)   |
            |                                                  |   initInterceptors()                                                      |
            |                                                  |                                                                           |
            +--------------------------------------------------+---------------------------------------------------------------------------+

* 150_1.5.0 790d091266c4ab011b1b2c984662ec37ba2c6e44 Merge pull request #683 from alibaba/develop
            +------------------------------------------------------------------------------------------------------------------------------+
            |[arouter-annotation]                                                                                                          |
            |  @Route      RouteType                        @Interceptor                                                                   |
            |     path()      [ACTIVITY,SERVICE,PROVIDER,                                                                                  |
            |                  CONTENT_PROVIDER,BOARDCAST,                                                                                 |
            |                   METHOD,FRAGMENT,UNKNOWN]                                                                                   |
            | @Autowired/@Param                                                                                                            |
            +------------------------------------------------------------------------------------------------------------------------------+
            |[arouter-compiler]                             |[arouter-api]                                                                 |
            |                                               | ARouter                                                                      |
            | RouteProcessor                                |   init()              build():Postcard   navigation(service:T)       inject()|
            |                      ARouter$$Root$$M         |                                             :T                               |
            |                           :IRouteRoot         +------------------------------------------------------------------------------+
            |                      ARouter$$Group$$M        |                          Postcard                                            |
            |                           :IRouteGroup        |                             na^igation() getExtras()                         |
            |                      ARouter$$Providers$$M    +------------------------------------------------------------------------------+
            |                            :IProviderGroup    | LogisticsCenter                        //PathReplaceService                  |
            |                                               |   groupsIndex:Map                      //DegradeService                      |
            |                                               |   interceptorsIndex:Map                                                      |
            | InterceptorProcessor                          |   providersIndex:Map                                                         |
            |                      ARouter$$Interceptors$$M |   executor                                                                   |
            |                            :IInterceptorGroup |      :ThreadPoolExecutor                                                     |
            |                                               |   init()//load gen class                               //AutowiredServiceImpl|
            | AutowiredProcessor                            |                                                                              |
            |                                               |                                                                              |
            |           ARouterActivity$$ARouter$$Autowired |   interceptors:List    interception(postcard,callback)                       |
            |                               :ISyringe       |   initInterceptors()                                                         |
            |                                               |                                                                              |
            +-----------------------------------------------+------------------------------------------------------------------------------+

```
### 依赖注入 dagger2/ 视图注入 ButterKnife


## 插件/补丁（客户端动态运行加载）
插件化（反射；接口；HOOK IActivityManager/Instrumentation+动态代理）
Activity校验，生命周期，Service优先级，资源访问，so插件化
- Dynamic-loader-apk
  [非开放sdk api](https://blog.csdn.net/yun_simon/article/details/81985331)
- Replugin


Tinker,Sophix
饿了吗的Amigo,美团的Robust,大众Nuwa
               +--------------------------+
               |nativeHook     AndFix     |
          +--- |               HotFix     |
          |    +--------------------------+
          |    +--------------------------+
          |    |            QZone,QFix    |
          +--- |multidex       Nuwa       |
          |    +--------------------------+         +---------------------------+
          |    +--------------------------+       +-+interface replace    Tinker|
          +--- |hook           Robust     |       | |                           |
          |    +--------------------------+       | |                           |
          |    +--------------------------+       | +---------------------------+
          |    |               Tinker     |       |
          +--- |dex replace               |       |
          |    |               Amigo      |       |  +--------------------------+
          |    +--------------------------+       |  |                  Amigo   |
          |    +--------------------------+       |  |plugin                    |
          +--- |Hybrid                    |       |  |                  Sophix  |
          |    +--------------------------+       +--+                          |
          |                                       |  +--------------------------+
          +                                       |
+-----+ class  +-----------+ res  +-------------+ So
                              +
                              |
                              |    +---------------------+
                              +----+ Full         Amigo  |
                              |    +---------------------+
                              |
                              |
                              |    +---------------------+
                              +----+patch       Tinker   |
                                   |            Sophix   |
                                   +---------------------+

andfix 替换方法 ArtMethod属性
Sophix dlopen和dvm_dlsym


### 补丁-Tinker
发布。Tinker沒有免费发布于。 
     Bugly：热修复（闭源）
     GitHub：tinker-manager
     [tinkerpatch（Android 热更新服务平台，闭源收费）](http://www.tinkerpatch.com/Price)
生成
     [aapt2 适配之资源 id 固定](https://fucknmb.com/2017/11/15/aapt2%E9%80%82%E9%85%8D%E4%B9%8B%E8%B5%84%E6%BA%90id%E5%9B%BA%E5%AE%9A/)
          aapt  -p public.xml
          aapt2 --stable-ids ,--emit-ids
     代码混淆 -printmapping ，-applymapping
加载
loadArmLibrary
installDexes
ResDiffDecoder.patch/ loadTinkerResources/addAssetPath  
```
  001_initial   23e414f4b505d6cceb993a7f8948a054fa23332f add tinker first version 1.2.0

               +----------------------------------------------------------------------------------------------------------------------------------+
               | [tinker-android-anno]                                               | [tinker-sample-android]                                    |
               |     @DefaultLifeCycle SampleApplication:TinkerApplication           |     MainActivity                                           |
               |                                                                     |[tinker+android+lib]                                        |
               | [tinker-android-loader]                                             |     TinkerInstaller                                        |
               |                                                                     |        loadArmLibrary()                                    |
               |                                                                     |        loadLibraryFromTinker()                             |
               |  DefaultApplicationLifeCycle                                        |                                                            |
               |           application:TinkerApplication                             |        onReceiveUpgradePatch()                             |
               |  TinkerApplication:Application                                      |                                                            |
               |         attachBaseContext()                                         |  Tinker                                                    |
               |         onBaseContextAttached()                                     |    create()                                                |
               |         loadTinker()                                                |    install()                                               |
               |         setTinkerResources()                                        |    listener:DefaultPatchListener                           |
               |  TinkerLoader:AbstractTinkerLoader                                  |                                                            |
               |         tryLoad()                                                   |                                                            |
               |         tryLoadPatchFilesInternal()                                 |  DefaultPatchListener                                      |
               |                                                                     |    onPatchReceived()                                       |
               |                                                                     |    patchCheck()                                            |
               +----------------------------------------------+----------------------+                                                            |
               | TinkerDexLoader                              | TinkerSoLoader       |                                                            |
               |    loadTinkerJars()                          |       checkComplete()|  TinkerPatchService                                        |
               | SystemClassLoaderAdder                       |                      |    runPatchService()                                       |
               |    installDexes()                            |                      |    upgradePatchProcessor:AbstractPatch                     |
               |    setupClassLoaders()                       |                      |    repairPatchProcessor:AbstractPatch                      |
               | IncrementalClassLoader                       |                      |                                                            |
               |    inject()                                  |                      |                                  AbstractResultService     |
               |    delegateClassLoader:DelegateClassLoader   |                      |  RepairPatch:AbstractPatch         runResultService()      |
               |    findClass()                               |                      |  UpgradePatch:AbstractPatch                                |
               |    findLibrary()                             |                      |     tryPatch()                   DefaultTinkerResultService|
               | DelegateClassLoader:BaseDexClassLoader       |                      |                                     :AbstractResultService |
               |    originClassLoader:PathClassLoader         |                      |                                     onPatchResult()        |
               |                                              |                      |                                                            |
               +----------------------------------------------+----------------------+------------------------------------------------------------+
* 191_v1.9.14.7 05e689a38e4e4690a078af8460f59c44bb033cfb [tinker] CI AGAIN tagged commit: update version to 1.9.14.7.

               +---------------------------------------------------------------------+------------------------------------------------------------+
               | [tinker+android+anno]                                               | [tinker+sample+android]                                    |
               |     @DefaultLifeCycle SampleApplication:TinkerApplication           |     MainActivity                                           |
               |                                                                     |[tinker+android+lib]                                        |
               | [tinker+android+loader]                                             |     TinkerInstaller          TinkerLoadLibrary             |
               |                                                                     |        loadArmLibrary()        installNavitveLibraryABI()  |
               |                                                                     |        loadLibraryFromTinker()                             |
               |  DefaultApplicationLifeCycle                                        |                                                            |
               |           application:TinkerApplication                             |        onReceiveUpgradePatch()                             |
               |  TinkerApplication:Application                                      |                                                            |
               |         attachBaseContext()                                         |  Tinker                                                    |
               |         onBaseContextAttached()                                     |    create()                                                |
               |         loadTinker()                                                |    install()                                               |
               |         setTinkerResources()                                        |    listener:DefaultPatchListener                           |
               |  TinkerLoader:AbstractTinkerLoader                                  |                                                            |
               |         tryLoad()                                                   |                                                            |
               |         tryLoadPatchFilesInternal()                                 |  DefaultPatchListener                                      |
               |                                                                     |    onPatchReceived()                                       |
               |                                                                     |    patchCheck()                                            |
               +---------------------------------------------------------------------+                                                            |
               | TinkerDexLoader          TinkerSoLoader                             |                                                            |
               |    loadTinkerJars()            checkComplete()                      |  TinkerPatchService                                        |
               |                                                                     |    runPatchService()                                       |
               |                                                                     |    upgradePatchProcessor:AbstractPatch                     |
               |                                                                     |    repairPatchProcessor:AbstractPatch                      |
               |                                                                     |                                                            |
               | TinkerArkHotLoader                                                  |                                  AbstractResultService     |
               |                                                                     |  RepairPatch:AbstractPatch         runResultService()      |
               |                                                                     |  UpgradePatch:AbstractPatch                                |
               | TinkerClassLoader                                                   |     tryPatch()                   DefaultTinkerResultService|
               |                                                                     |                                     :AbstractResultService |
               |                                                                     |                                     onPatchResult()        |
               | TinkerResourceLoader                                                |                                                            |
               |                                                                     |                                                            | 
               +---------------------------------------------------------------------+------------------------------------------------------------+

```


### 插件
Dynamic-Loader-Apk，Replugin

## 通讯



### 跨进程通讯

```
+------------------------------------------------------------------------------------------------+
|  [kernel]                                     |[servicemanager]                                |
|       binder.c                                |   servicemanager.c                             |
|          binder_init()//misc_register         |       main()                                   |
|                                               |       svcmgr_handler()                         |
|                                               |   binder.c                                     |
|          binder_get_thread()                  |      binder_open():binder_state                |
|                 :binder_thread                |      binder_loop()                             |
|                                               |      binder_mmap()                             |
|   binder_proc                                 |      binder_update_page_range()                |
|      buffer:void * //virtual mem              |                                                |
|      user_buffer_offset:size_t                |      binder_parse()                            |
|      *vma:vm_area_struct                      |      binder_release()    svclist:svcinfo*      |
|      buffers:list_head                        |                                                |
|                                               |               // binder_ref : BpBinder client  |
|  vm_area_struct       binder_buffer           |               // binder_node: BBinder  server  |
|                       binder_transaction_data |               // adb shell service list        |
|  vmalloc.c                                    |                               // BC_ACQUIRE    |
|     map_kernel_range_noflush()//kernel map    |  binder_state                 // BC_INCREFS    |
|     vm_insert_page() //user map               |    fd;int                     // BC_RELEASE    |
|                                               |    mapped;void *              // BC_DECREFS    |
|                                               |    mapsize;size_t                              |
+-----------------------------------------------+------------------------------------------------+
| [jni-app]                                                                                      |
|    ProcessState                                                                                |
|         open_driver():int //binder fd                //BR_RELEASE                              |
|                                                      mPendingStrongDerefs:Vector<BBinder*>     |
|         mDriverFD;int                                                                          |
|         mHandleToObject;Vector<handle_entry>                                                   |
|         startThreadPool()                                                                      |
|                                                                                                |
|         spawnPooledThread()                          talkWithDriver()                          |
|                                                                                                |
|                                                                                                |
|                                                                                                |
|       PoolThread:Thread  IPCThreadState              Parcel      BpBinder                      |
|         threadLoop()        writeTransactionData()                   mHandle                   |
|                                                                                                |
+------------------------------------------------------------------------------------------------+

```
### 四大组件


## kotlin 封装，高效编程
1. 封装
     封装field 结构体声明var，val
     封装static 默认都是static 嵌套类，除非用inner修饰
     封装final 默认时final类，除非open class,abstract class,interface
     拓展enum类，支持子类创建多个实例seal class
2.  多态/复用
     类型 typealise/inline
     组合复用原则 委托（by 监听器/部分代理），拓展（内置作用域拓展函数）
     优化泛型 
3. 类型拓展
   非空判断 
     序列集合
     方法
4. 线程调度拓展-协程
     协程Scope，协程Context，协程continuation，协程dispatcher：协程scheduler
### Kotlin 详细
[详见](./知识体系-平台-Android-cohesion-kotlin.md)