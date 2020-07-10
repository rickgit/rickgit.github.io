## 通讯 EventBus
## 数据路由/通讯 - ARouter
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

## 组件
组件指的是单一的功能组件，如 [视频组件]、[支付组件] 等，每个组件都可以以一个单独的 module 开发，并且可以单独抽出来作为 SDK 对外发布使用。
- 组件单独调试
- 组件间界面跳转 Arouter
## 插件/补丁
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


## Tinker
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


## 插件
Dynamic-Loader-Apk，Replugin