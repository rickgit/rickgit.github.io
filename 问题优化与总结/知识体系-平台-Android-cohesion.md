
## 

常见优化
解耦：ButterKnife、Dagger2
通讯：Router、eventbus
## 服务
IntentService
JobService

工厂方法 onBinder
命令   onStartCommand()，ServiceConnection#onServiceConnected
 

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

### 通讯 
EventBus
intent,接口，aidl，handler，broadcast，其他第三方


### EventBus
单例 **EventBus defaultInstance** ；构造器 **EventBusBuilder DEFAULT_BUILDER**

享元 **FindState[] FIND_STATE_POOL；FindState#recycle()**；外观 EventBus

观察 **EventBus#register**；访问 **SubscriberMethodFinder#findSubscriberMethods**； 
；状态**ThreadMode 6种状态，priority，sticky**；
责任链 **FindState#moveToSuperclass()**；模板 **@Subscribe；Poster#enqueue**；


#### 吞吐
Map<Class<?>, CopyOnWriteArrayList<Subscription>> subscriptionsByEventType
stick 各个用户界面的用户信息事件

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
### 视图注入 ButterKnife
## 数据对象依赖注入解耦 -Dagger2 依赖注入框架
[见 知识体系-理论-OOAD-DIP.md](./知识体系-理论-OOAD-DIP.md)
```
 ----------------------------------------------------->
+-------------------------+--------------------------------+---------------+
|             ViewClass   |      @Inject                   |               |
|                         +-----------------------+        |               |
+------------+---+--------+  @Component           |        |               |
|            |   |        | (modules, dependencys)|        |               |
|            |   | @Scope | ComponentInterface    |        |               |
|            |   |        |                       |        |InjectDataClass|
|            |   +--------+---------------+       |        |/PresenterClass|
|            |            @Module         |       |        |               |
|            |           ModuleClass      |       |        |               |
| @Named/    +------------+-------------+-+-------+--------+               |
| @Qualifier | @Scope     | @IntoMap    |                  |               |
|            |            | @StringKey/ |                  |               |
|            |            | @ClassKey   | @provide/@Binds  |               |
|            |            +-------------+ provideDataMethod|               |
|            |            | @IntoSet    |                  |               |
+------------+------------+-------------+------------------+---------------+

+--------------------------------+
| AndroidInjection.inject(this); |  <-------+
+--------------------------------+          |
                                            |                    +-----------------------------------+
+--------------------------------+          |                    | @Module                           | 
| AndroidInjection.inject(this); |  <-+     |                    |  abstractActModule1               |
+--------------------------------+    |     |                 +--+     @ContributesAndroidInjectortor(modules={implModule})
                                      |     |                 |  |     abstract Act contributeAct()  |
+-------------------------------------+-----+--+              |  +-----------------------------------+
| App:HasActivityInjector                      |              |
+----------------------------------------------+              |  +------------------------------------------+
|      @Inject                                 |              |  | +------------------------------------+   |
|      DispatchingAndroidInjector              |              |  | |  @SubComponent                     |   |
+----------------------------------------------+              |  | |  ActSubComponent:AndroidInjector   |   |
                                                              |  | |                                    |   |
                ^                                             |  | |        @Subcomponent.Builder       |   |
                |                                             |  | +------------------------------------+   |
+---------------+-----------------------------------+         |  | +------------------------------------+   |
|@Componect(AndroidInjectionModule.class,ActModule1)|  <------+  | |  @Module                           |   |
| IAppComponent:AndroidInjector                     |            | |  ActModule2                        |   |
|                                                   |  <-------+ | |                                    |   |
|                                                   |            | +------------------------------------+   |
+---------------------------------------------------+            +------------------------------------------+

```

 

### 依赖注入解耦google/dagger.android
工厂方法  单例@singleInstance 构造 builder
桥接（setter方法注入。接口注入。构造方法注入。反射（注解/xml）方式注入。convert注入）；组合linker；
门面 Component#inject
享元 

1. dagger
2. dagger+auto
3. dagger.android 废弃（不兼容androidx, 为了简化dagger在Android上的手动调用inject方法）
4. dagger.hilt + viewmodel/WorkManager
   

Dagger 2 是 Java 和 Android 下的一个完全静态、编译时生成代码的依赖注入框架，完全去除了反射。

[Google dagger](https://developer.android.google.cn/training/dependency-injection/dagger-android?hl=zh-cn)

Android MVVM with Dagger2, Retrofit, RxJava and Android Architecture Components
[AndroidInjector](https://segmentfault.com/a/1190000010016618)

```



  002_injector            04892c03bad8cc45a3c509880c33e7f1d41f5a47 First draft of a new, fast dependency JSR-330 injector.
                          +---------------------------------------------------------------------------------------------------------+
                          |                       Linker                                                                            |
                          |                          link(Collection<Binding<?<>)                                                   |
                          |                          injector                                                                       |
                          +------------------------+--------------------------------------------------------------------------------+
                          |                        |                       UnresolvedBinding<T>    unattachedBindings               |
                          |                        +----------------------------------------------------+---------------------------+
                          |                        |                                                    |                           |
                          |                        |  BuiltInBinding<T>           ConstructorBinding<T> |                           |
                          |                        |        MembersInjector.class                       |                           |
                          |                        |        Provider.class                              |  ProviderMethodBinding<T> |
                          |                        |                     DeferredBinding<T>             |         Provider.class    |
                          |                        |                           root_injection           |         attach(Linker)    |
                          +-----------------------------------------------------------------------------+---------------------------+
                          |     Key<T>             |                            Binding<T>                                          |
                          |        type            |                                get()                                           |
                          |        annotation      |                                                                                |
                          +------------------------+--------------------------------------------------------------------------------+
                          |  Maven                  Injector                                                                        |
                          |                             inject(Class<T> type, Object... modules)                                    |
                          +---------------------------------------------------------------------------------------------------------+

  003_code_gen            576136ba600e81192960350f58503dfed9d824f4 New, very basic Java code generator.
  004_modules             2ecf2ed141990194e89d88f7d159aca86a39c5aa Add Modules.override().
  005_stringKey           733a7f6431616202b248adc6cf9d619bd4750162 Use Strings for Keys.
  006_ProvidesProcessor   2a5de416ac26b37cde2d07c66873931495db6da8 First draft of the @Provides processor.
  007_InjectProcessor     6fd3de3797baa8b44789fd50a43b8daa5bc7fcd7 First draft of the @Inject processor.
  008_DependencyGraph     4890139ad39ae959dc5b0bb9e638fc66ef702513 New annotation-driven API that uses entry points.
                          ----------------------+-------------------------------------------------------------------------------------------------------+
                          |                     |                                                Linker                                                 |
                          |                     |                                                   installModules()                                    |
                          |                     |                                                   link()                                              |
                          |                     +------------------------+------------------------------------------------------------------------------+
                          |                     |                        |                       UnresolvedBinding<T>                                   |
                          |                     |                        |                       unattachedBindings                                     |
                          |                     |                        +----------------------------------------------------+-------------------------+
                          |@interface Injector  |                        |  BuiltInBinding<T>           ConstructorBinding<T> |                         |
                          |        modules()    |                        |        MembersInjector.class       @Inject         |                         |
                          |        entryPoints()|                        |        @Provides                                   |ProviderMethodBinding<T> |
                          |                     |                        |                     DeferredBinding<T>             |       @Provides         |
                          |                     |                        |                           root_injection           |       attach(Linker)    |
                          |                     |                        +----------------------------------------------------+-------------------------+
                          |                     |  Keys                  |                            Binding<T> :Provider<T>, MembersInjector<T>       |
                          |                     |    get(type,annotation)|                                              get()      injectMembers(T)     |
                          |---------------------+------------------------+------------------------------------------------------------------------------+
                          |                     |                         DependencyGraph                                                               |
                          |  CodeGen(java apt)  |                             DependencyGraph( injectorClass, bindings)                                 |
                          ----------------------+-------------------------------------------------------------------------------------------------------+


  009_static_inject       34e2a25c3a6eed0b261c8be4b00610c6df782bc3 Support static injection.
  010_google_guice        63a7e1ce3f7d202d907b4227ab51e98549d91d76 Minimal support for Guice annotations to ease migration and A/B testing.
  011_@module             52a260e81054eec5bfd47f419537cbadc2045612 Replace @Injector with @Module.
                          ----------------------+-------------------------------------------------------------------------------------------------------+
                          |                     |                                                Linker                                                 |
                          |                     |                                                   installModules()                                    |
                          |                     |                                                   link()                                              |
                          |                     +------------------------+------------------------------------------------------------------------------+
                          |                     |                        |                       UnresolvedBinding<T>                                   |
                          |                     |                        |                       unattachedBindings                                     |
                          |                     |                        +----------------------------------------------------+-------------------------+
                          |@interface module    |                        |  BuiltInBinding<T>           ConstructorBinding<T> |                         |
                          |        modules()    |                        |        MembersInjector.class       @Inject         |                         |
                          |        entryPoints()|                        |        @Provides                                   |ProviderMethodBinding<T> |
                          |                     |                        |                     DeferredBinding<T>             |       @Provides         |
                          |                     |                        |                           root_injection           |       attach(Linker)    |
                          |                     |                        +----------------------------------------------------+-------------------------+
                          |                     |  Keys                  |                            Binding<T> :Provider<T>, MembersInjector<T>       |
                          |                     |    get(type,annotation)|                                              get()      injectMembers(T)     |
                          |---------------------+------------------------+------------------------------------------------------------------------------+
                          |   guice             |                         DependencyGraph                                                               |
                          |  CodeGen(java apt)  |                             DependencyGraph( injectorClass, bindings)                                 |
                          ----------------------+-------------------------------------------------------------------------------------------------------+
  012_ProblemDetector     3f013f16c6663fa023c1c64a4a291e7083712fe7 Detect problems at runtime.
  013_LazyInjection       31ad32fd607773ac3c0d0f7fdde26591a459506e Support lazy object graphs.
  014_androidmenifest     4136fd157588b3ea64f054e82d10e4c09419deb4 Initial infrastructure to generate modules from AndroidManifest.xml.
  015_FullGraphProcessor  7b0c34fe4def38191eef738ab391b7a5810bc18a Build time graph construction and validation.
  016_LazyBinding         69c9daf5931d27902dd2282d0e606a83bc993ead Add a Lazy<T> with a delegate binding, so consumers can @Inject a Lazy<T> if T is bound. Tighten up key mangling code
  017_SetBinding          0656b1a9d346099d3138da9d50b20c4efa1486e8 Add support for multi-bindings, such that any method annotated with Provide AND Element contribute that binding to a set-binding, which builds an injectable set at injection time.
  018_OneOf               4670896e18e3e748ac33ff42f388450c082e2c39 Rename Element to OneOf
  019_dagger-compiler     f0de3941d22fa017c377f05822f225005d9dd7fc Migrate code-gen processors to their own project, and separate reflection code.
  020_ModuleGeneratorTask ecfce61047a6aee7b57d27dad9f7f1d7b6339535 Allow manifest module generator invocation as Ant task.
* 021_website             e8586ef986fde827ea43c890da7252c8f66c0a7d Add website.
  092_android_examples    d45582b83d3cd6400a0f7b3a1e98f79ea856404a Add two Android examples.
  100_dagger-parent-1.0.0 21bdcdd0ff1d88b78d5f2be68096f1cc50947c31 [maven-release-plugin] prepare release dagger-parent-1.0.0
                          +-----------------------------------------------------------------------------------------------------------------------+
                          |                                  Linker                                                                               |
                          |                                    plugin         installBindings()      requestBinding()                             |
                          |                                    errorHandler   link()                                                              |
                          +------------------------+-------------------------------------------------+--------------------------------------------+
                          |                        |                                                 |                   ProviderMethodBinding<T> |
                          |                        |                                                 |                    ReflectiveModuleAdapter |
                          |                        |   @Inject          @Provides                    |             @Module                        |
                          |                        |    :Binding          :Binding,Provider          |             ModuleAdapter                  |
                          |                        |                                                 |                   getBindings()            |
                          |                        |                                                 +--------------------------------------------+
                          |                        |  BuiltInBinding<T>                              |  ClassloadingPlugin     ReflectivePlugin   |
                          |                        |        MembersInjector                          |           Plugin                           |
                          |                        |                                                 |              getModuleAdapter()            |
                          |                        +-------------------------------------------------+--------------------------------------------+
                          |  Keys                  |   Binding<T> :Provider<T>, MembersInjector<T>                                                |
                          |    get(type,annotation)|                     get()      injectMembers(T)                                              |
                          +------------------------+----------------------------------------------------------------------------------------------+
                          |                         ObjectGraph                                                                                   |
                          |                             create(modules)      linker      staticInjections                                         |
                          |                             inject(instance)     plugin      injectableTypes                                          |
                          +-----------------------------------------------------------------------------------------------------------------------+
                          |                                  java apt, squareup javawriter                                                        |
                          +-----------------------------------------------------------------------------------------------------------------------+

  200_dagger-2.0          ddbe7f199027f8aacb259c884bf85ee3451d2fe3 Update the README.md to reflect the release of Dagger 2.0, fixing up some filler text, updating versions, and pointing at the right download/repository sources.
                          @mapkey 作为map的key类型。如HashMap<@mapkey,@mapkey(value)>;新的注释 @SubComponent
          +--------------------------------+--------------------+-------------------------------------------+-----------------------------------------------------------
          |                                |                    |                                           |                                                          |
          | @MapKey           @Module      |@ProducerModule     |                                           |                                                          |
          |    unwrapValue()     includes()|                    |                                           |@MapKey                  @Produces,@ProducerModule        |
          |                                |                    |                                           |MapKeyProcessingStep     ProducerModuleProcessingStep     |
          | ModuleType_typeFactory:Factory |                    |ContributionBinding  MembersInjectionBindin|      MapKeyGenerator          ProducerFactoryGenerator   |
          | @Provides                      |@Produces           |                                           |                                                          |
          |    type()                      |                    |                                           |                                                          |
          |                                |                    |                Binding                    |@Providers,@Module       @ProductionComponent             |
          | DaggerOuterType_ComponentName/ |                    |                                           |ModuleProcessingStep     ProductionComponentProcessingStep|
          | DaggerCompnentName             |                    +-------------------------------------------+      FactoryGenerator         ComponentGenerator         |
          | @Component                     |@ProductionComponent|          BindingGraph     RequestResolver |                                                          |
          |    modules()                   |                    +-------------------------------------------+@component                                                |
          |    dependencies()              |                    |   @Inject                                 |ComponentProcessingStep                                   |
          |                                |                    |   InjectProcessingStep                    |      ComponentGenerator                                  |
          | @Subcomponent                  |                    |          InjectBindingRegistry            |                                                          |
          |    modules()                   |                    |                 MembersInjectorGenerator  |                                                          |
          |                                |                    |                 FactoryGenerator          |                                                          |
          | MembersInjector                |                    +-------------------------------------------+----------------------------------------------------------+
          |    injectMembers(instance)     |                    |                                               ProcessingStep                                         |
          |                                |                    +------------------------------------------------------------------------------------------------------+
          | Lazy<T>                        |                    |                                                  @autoservice                                        |
          |    get():T                     |                    |                                                  ComponentProcessor: BasicAnnotationProcessor        |
          |                                |                    |                                                                injectBindingRegistry                 |
          | ScopedProvider                 |                    +------------------------------------------------------------------------------------------------------+
          |         factory                |                    |   java apt(google auto), squareup javawriter , google  guava,truth                                   |
          +------------------------------------------------------------------------------------------------------------------------------------------------------------+
          |     core                       |          Producers |                               compiler                                                               |
          +--------------------------------+--------------------+-------------------------------------------------------------------------------------------------------


  291_android_md          f69e4f3e5372f968306c71b22732f576612ed9bc Add markdown docs for dagger.android classes
  292_android_code        9883435313b7c639a945a443b812858954eac583 Move Android code into java/ and javatests/
  293_core_android        57f302c64dcbe22cfd668e656b446433bb81f270 Make injection of core Android types easier.
  294_androidprocessor    4c05120cd301be6db331402fd989d2269cee9453 Add an annotation processor to guard against potential mistakes when using dagger.android
  2x0_0_service           3f6c59ca7620c45fb4210842da5b0b0b5aba75ee Expand dagger.android for Services and BroadcastReceivers
  2x0_1_example           5bace0b35f9a2184cfb3b8e93eb652f21577f9b0 Opensource the Android example
  2x0_2_compiler          ca8ca996e8428668693679cf96d57c083511cd78 Finish moving code out of compiler/ into java/ and javatests/
* 2x1_dagger-2.11.rc1     159a08c415201b886cfbfea56d5c0eddbb0a0a82 Use Mockito.anyLong() instead of any(Long.class)

                                                                   DependencyRequest
+-------------------------+--------------------------------+----------------------------------------------------------------+---------------------------------------+
|                         |                                |                                                                |                                       |
|  AndroidInjection/      |                                |DaggerCompnentName/                                             |                                       |
| AndroidSupportInjection |                                |DaggerOuterType_ComponentName  @BindsInstance                   |                                       |
|     inject()            |                                |       build()                 BindsInstanceProcessingStep      |                                       |
|                         |                                |@component                                                      |                                       |
|                         |MembersInjectionBinding         |ComponentProcessingStep        @Produces,@ProducerModule        |                                       |
|                         |                                |      ComponentGenerator       ProducerModuleProcessingStep     |                                       |
|                         |                                |                                     ProducerFactoryGenerator   |                                       |
|                         |                                |                                                                |                                       |
|                         |                                |@MapKey                        @ProductionComponent             |                                       |
|                         |ContributionBinding             |MapKeyProcessingStep           ProductionComponentProcessingStep|                                       |
|                         |                                |      MapKeyGenerator                ComponentGenerator         |                                       |
|                         |   Binding                      |                                                                |                                       |
|+-----------------------+|                                |@Providers,@Module             @ForReleasableReferences         |                                       |
| DaggerApplication       +--------------------------------+ModuleProcessingStep           ForReleasableReferencesValidator |                                       |
|   :HasActivityInjector  |          BindingGraph          |      FactoryGenerator                                          |                                       |
|   activityInjector      |                                |                              @CanReleaseReferences             |                                       |
|                         |          RequestResolver       |                              CanReleaseReferencesProcessingStep|                                       |
| DaggerActivity          |                                |@Multibindings                                                  |                                       |
|    :                    |                                |MultibindingsProcessingStep                                     |   @ContributesAndroidInjector         |
|                         |--------------------------------+                                                                |   ContributesAndroidInjectorGenerator |
|                         | @Inject                        |@ProductionComponent,@ProductionSubcomponent                    |                                       |
|                         | InjectProcessingStep           |MonitoringModuleProcessingStep                                  |   AndroidMapKeyValidator              |
|                         |     InjectBindingRegistry      |                                                                |         MapKeyGenerator               |
|                         |       MembersInjectorGenerator |BindingMethodProcessingStep                                     |                                       |
|                         |       FactoryGenerator         |ProductionExecutorModuleProcessingStep                          |                                       |
+---------------------------------------------------------------------------------------------------------------------------+---------------------------------------+
|                         |                                          ProcessingStep                                                                                 |
+---------------------------------------------------------------------------------------------------------------------------+---------------------------------------+
|                         |                                      @autoservice                                               |                                       |
|Component:AndroidInjector|                                      ComponentProcessor: BasicAnnotationProcessor               |                      @autoservice     |
|      inject(instance)   |                                                    injectBindingRegistry                        |                      AndroidProcessor |
+---------------------------------------------------------------------------------------------------------------------------+---------------------------------------+
|      android            |                                                         codegen                                                                         |
+-------------------------+-----------------------------------------------------------------------------------------------------------------------------------------+
|                                                                                         java apt(google auto), squareup javapoet   , google  guava,truth          |
+--------------------------------------------------------------------------------------------------------------------------------------------------------------------





                          
auto
  001_initial_maven         b281ea4dfe5cfc7cf9d1a74541c411a07a89c0b4 An initial project and an initial continuous integration system config, as well as contributors docs, license, and an initial checkstyle, cribbed from Dagger.
  002_generator_integration 85b81393b08319ace2734187ac25ec1371536f36 Initial shape of autofactory and early code drop.
                            +--------------------------------------------------------------------------------------+
                            |              CodeGen(javawriter)                                                     |
                            +---------------------------------------------------+----------------------------------+
                            |            FactoryAdapterGenerator                |  InterfaceLinkAdapterGenerator   |
                            +---------------------------------------------------+----------------------------------+
                            |                     generator                     |         integration (dagger)     |
                            +---------------------------------------------------+----------------------------------+
                            |    @AutoFactory     @Param         @Inject                                           |
                            +--------------------------------------------------------------------------------------+
                            |    FactoryProcessor :AbstractProcessor                                               |
                            |              process(types, env)    processingEnv                                    |
                            +--------------------------------------------------------------------------------------+
  003_truth                 c6d3b38736c93ef69ecd342daa9f3669960fc329 Introduce a Truth subject that uses javac to parse source into an AST and compare them in parallel.
  004_truth_test            513a71854e704a5d1e1078ee8a2703485ffa0b91 Add some simple tests.
  005_generator_test        5466b8f210b86b81ba8fc114087ea19bffaf6af1 An initial addition of some simple usecases
* 006_generator_newapi      d08d9c3828833bcacdc05208842d32f4c3710332 Adopt the new API.  The tests are still failing, but this is a mostly working checkpoint
                            +--------------------------------------------------------------------------------------+
                            |                         Factory                                                      |
                            |                             create()                                                 |
                            +--------------------------------------------------------------------------------------+
                            |                       FactoryDescriptorGenerator   FactoryMethodDescriptor           |
                            |                                 messager                                             |
                            |                                 elements                                             |
                            |       JavaCompiler    FactoryDescriptor                                              |
                            |                                 name                                                 |
                            |                                 methodDescriptors                                    |
                            |                                 providerNames                                        |
                            |                                                                                      |
                            |                   FactoryWriter                                                      |
                            |                            filer                                                     |
                            +--------------------------------------------------+---------------------------------- |
                            |          AutoFactoryProcessor                    |  InterfaceLinkAdapterGenerator    |
                            +--------------------------------------------------+---------------------------------- |
                            |    @Provided            @AutoFactory                                                 |
                            |                              implementing():class                                    |
                            +--------------------------------------------------------------------------------------+
                            | AutoFactoryProcessor:AbstractProcessor                                               |
                            |              process(types, en^)    processingEn^                                    |
                            +--------------------------------------------------------------------------------------+
                            |                     generator                                                        |
                            +--------------------------------------------------------------------------------------+
  007_readme                0265313d9460f732a92b15e01d957d2776f93325 Update the READMEs.
  008_autoservice           afda34252be53cacc35c0732ccf5f1040d2766d3 Add @AutoService for generating META-INF/services configuration files.
  009_autovalue             d67532c38d7fb39906b2ee7de6567e6c2620ca2e An initial import of @AutoValue
* 010_autovalue_test        4e54bae91a70b7c1c6aeec275a0a423a775d53a3 Add the tests as well
                            +---------------------------------------------------------------------+---------------------------+----------------------------------------+
                            |        Factory                                                      |    META-INF/services      | AutoValue_name   AutoValueFactory_name |
                            |            create()                                                 |                           |                                        |
                            +------------------------------------------------------------------------------------------------------------------------------------------+
                            |                                                                     |                           |                                        |
                            |                 FactoryDescriptorGenerator   FactoryMethodDescriptor|                           |                                        |
                            |                           messager                                  |                           |                                        |
                            |                           elements                                  |       ServicesFiles       |TEMPLATE_STRING  FACTORY_TEMPLATE_STRING|
                            | JavaCompiler    FactoryDescriptor                                   |                           |                                        |
                            |                           name                                      |                           |                                        |
                            |                           methodDescriptors                         |                           |                                        |
                            |                           providerNames                             |                           |                                        |
                            |                                                                     |                           |                                        |
                            |             FactoryWriter                                           |                           |                                        |
                            |                      filer                                                                      |                                        |
                            +---------------------------------+---------------------------------- +--------------------------------------------------------------------+
                            |   AutoFactoryProcessor          |  InterfaceLinkAdapterGenerator    |   AutoServiceProcessor    |       AutoValueProcessor               |
                            +---------------------------------+--------------------------------------------------------------------------------------------------------+
                            |          @Provided            @AutoFactory                          |      @AutoService         |           @AutoValue                   |
                            |                                    implementing():class             |                           |                                        |
                            +------------------------------------------------------------------------------------------------------------------------------------------+
                            |    AutoFactoryProcessor:AbstractProcessor                           |      ServiceLoader        |                  |  AutoValues         |
                            |                 process(types, en^)    processingEn^                |                           |                  |                     |
                            +------------------------------------------------------------------------------------------------------------------------------------------+
                            |                          generator                                  |      service              |              value                     |
                            +---------------------------------------------------------------------+---------------------------+----------------------------------------+
  011_auto-common              5834df6914a592731e34a34e4641f162afa7a444 Introduce com.google.auto:auto-common with some simple Element utilities.
  012_dagger_moretype          9b389fa6cb49fd121fb0433241f55863778b1962 Copy MoreTypes from dagger to auto-common so that it can be used by auto-value. Eventually it should be moved rather than copied.
  013_superficialvalidation    4874fbf0c227a62a90af31f37ad2753e6a0adf80 Add a utility that does quick, superficial validation on elements to ensure that all type information is present while running a processor. ------------- Created by MOE: http://code.google.com/p/moe-java MOE_MIGRATED_REVID=70831442
  014_visibility               260023d4c69008ea6646aacb8b378c1f4d3616b9 Add validation to make sure that modules must be public and not inner classes. ------------- Created by MOE: http://code.google.com/p/moe-java MOE_MIGRATED_REVID=74368767
  015_MoreTypsIsTypeOfTest     6992f634601141e4e1529c8c93aae64c6da5a868 Fix the SuperficialValidator to properly handle unreasonable AnnotationValues (specifically  "<any>" and "<error>" strings appearing instead of real values where there are missing imports or other upstream compilation issues).  Migrate isTypeOf from Dagger to perform the key test (is there a sane match between the expected annotation value type and the type returned by the processor environment) ------------- Created by MOE: http://code.google.com/p/moe-java MOE_MIGRATED_REVID=75599279
* 016_BasicAnnotationProcessor d1f1f9a1c13a9ab76db20380ad6479da2af88bc6 Add AnnotationMirrors, AnnotationValues, and a BasicAnnotationProcessor which performs early validation of elements, and stores un-processable elements and re-tries them on subsequent processing rounds (in case the processor depends on code which will become valid in future rounds).  Also,  move some stray tests from dagger to google/auto common. ------------- Created by MOE: http://code.google.com/p/moe-java MOE_MIGRATED_REVID=83567228

                            +---------+---------+-------------------------------------------------+-----------------------------------------------------------+
                            |         |         | AutoAnnotation_enclosedtype_method:returntype   |                                                           |
                            |         |         |                                                 |                                                           |
                            |         |         | AutoValue_enclosedtype: enclosedtype            |                                                           |
                            |         |         +-------------------------------------------------------------------------------------------------------------+
                            |         |         |    AutoAnnotationProcessor   AutoValueProcessor |               ProcessingStep                              |
                            |         |         +-------------------------------------------------+                       annotations():set                   |
                            |         |         |           @AutoAnnotation      @AutoValue       |                       process( elementsByAnnotation);     |
                            |         |         +-------------------------------------------------------------------------------------------------------------+
                            |         |         |                                                 |          BasicAnnotationProcessor                         |
                            |         |         |                                                 |              steps:List^? extends ProcessingStep^         |
                            +---------------------------------------------------------------------------------------------------------------------------------+
                            | factory |ser^ice  |              value                              |                     common                                |
                            +-------------------+-------------------------------------------------+-----------------------------------------------------------+



```


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

## 其他通讯


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


## 解耦 - jetpack
依赖注入：视图绑定，界面数据绑定，依赖注入 hilt
生命周期：LiveData，备忘 ViewModel 生命周期大于Activity，viewModelScope 协程管理；
导航：Navigation

存储：Room，paging，图片（CameraX ）
调度：WorkManager
界面组件：Compose


MVVM： 包含 依赖注入和生命周期管理 + Navigation 
 

### LiveData
观察对象修改，装饰适配Observer维护其的生命周期

适配器 适配 LifecycleBoundObserver 支持 **LifecycleBoundObserver#onStateChanged**
装饰者 装饰**observable**，添加 **ObserverWrapper#mActive** ；

观察者 
1. 订阅 **LiveData#observe**，通过 **liveData.postValue("a"); liveData.setValue("b")**修改**LiveData#mData**，通知**Observer#onChanged**
2. 订阅 **LifecycleRegistry#addObserver**，通过**LifecycleRegistry#handleLifecycleEvent** 修改**LifecycleRegistry#mState**，通知
**LifecycleEventObserver#onStateChanged(LifecycleOwner, Lifecycle.Event)**

### ViewModel
[](https://developer.android.google.cn/topic/libraries/architecture/saving-states)
备忘ViewModel，savedState，ViewModel生命周期维护，SavedStateHandle数据重建
1. ViewModel保存和恢复的仅限于配置更改导致的重建，支持保存大量和复杂的数据
2. SavedStateHandle 资源限制导致Activity重建，只能保存少量简单的数据
简单工厂
     ViewModelProvider#get(java.lang.Class<T>)
     SavedStateHandleController#create
     SavedStateHandle#createHandle
工厂方法   
     ViewModelProvider.AndroidViewModelFactory#create 
     ViewModelProvider.NewInstanceFactory#create
     SavedStateViewModelFactory#create
单例
     ViewModelProvider.NewInstanceFactory#sInstance
     ViewModelProvider.AndroidViewModelFactory#sInstance
装饰 SavingStateLiveData装饰 value，添加 SavedStateHandle 

命令  ViewModel#clear
     执行 **SavedStateRegistry#performSave，SavedStateRegistry#performRestore**，获取或修改 SavedStateRegistry#mComponents，SavedStateRegistry#mRestoredState
观察
     SavedStateHandleController（LifecycleEventObserver#onStateChanged）

     订阅 LifecycleRegistry#addObserver，通过 LifecycleEventObserver#onStateChanged 执行clear命令，通知方法Lifecycle.Event
     ViewModel#onCleared

备忘 
     通过 ViewModelStore#put 备份到 ViewModelProvider#mViewModelStore/ComponentActivity#mViewModelStore 备忘  **HashMap<String, ViewModel> mMap**，恢复备忘 ViewModelStore#get

     通过 Activity#retainNonConfigurationInstances 备忘NonConfigurationInstances#viewModelStore， Activity#attach恢复备忘NonConfigurationInstances#viewModelStore

     通过 SavedStateRegistry#performSave命令，备忘 SavedStateRegistry#mRestoredState，SavedStateRegistry#mComponents，通过 SavedStateRegistry#performRestore， SavedStateHandleController.OnRecreation#onRecreated恢复备忘

     通过 SavedStateProvider#saveState，备忘ViewModel用户自定义的SavedStateHandle#mRegular， SavedStateHandle#createHandle 恢复备忘 SavedStateHandle#mRegular


### NavGraph 
### 视图绑定 代替findviewById
```js
viewBinding {
            enabled = true
        }
```
result_profile.xml->绑定类的名称就为 ResultProfileBinding

桥接 findViewById 业务给转移 Binding类
### 数据绑定
```js
       dataBinding {
            enabled = true
        }
```
DataBindingUtil

### WorkManager
单例 WorkManager
构造 WorkRequest.Builder
命令 WorkManager#enqueue(WorkRequest)添加命令封装类WorkRequest， Worker#doWork

### Room
Dao编译时构建模板适配器类，注解创建模板方法SharedSQLiteStatement#createQuery，适配SupportSQLiteStatement#bindxxx对象

构建器 RoomDatabase.Builder

适配器 SharedSQLiteStatement#bind和EntityInsertionAdapter#bind适配SupportSQLiteStatement的bindxxx接口
代理 FrameworkSQLiteStatement 委托 SQLiteStatement
装饰  RoomDatabase 装饰 RoomDatabase#mOpenHelper；DefaultTaskExecutor 装饰 DefaultTaskExecutor#mDiskIO，添加 DefaultTaskExecutor#mMainHandler主线程Handler支持

命令 RoomDatabase#mQueryExecutor， RoomDatabase#mTransactionExecutor， 
观察者 订阅 RoomDatabase.Builder#addCallback，通过 Callback#onOpen，获取SupportSQLiteDatabase 状态
模板方法 SharedSQLiteStatement#createQuery

### paing
[Paging 3 library overview](https://developer.android.google.cn/topic/libraries/architecture/paging/v3-overview)
 
构建者    LivePagedListBuilder：LiveData<PagedList<Value>>（Java）
          LivePagedList.kt#toLiveData：LiveData<PagedList<Value>> （Kotlin）


## kotlin 封装，高效编程
1. 封装
     封装field 结构体声明var，val
     封装static 默认都是static 嵌套类，除非用inner修饰
     封装final 默认时final类，除非open class,abstract class,interface
     拓展enum类，支持子类创建多个实例seal class
2.  多态/复用
     类型 typealise/inline
     组合复用原则 委托（by 监听器/部分代理），拓展（内置作用域拓展函数），伴生对象
     优化泛型 （泛型编译时校验，复用）
3. 类型拓展
     Unit、Nothing Any
     非空判断 
     序列集合
     方法
4. 线程调度拓展-协程
     协程Scope，协程Context，协程continuation，协程dispatcher：协程scheduler
     1. 协程block作为高阶函数（suspend CoroutineScope.() -> Unit），是协程Scope的匿名拓展函数，可以访问协程Scope的数据。最终编译到Continuation
     2. 协程Context是各种不同元素的集合。其中主元素是协程中的 Job
     3. 协程Context,包含了协程dispatcher，用来确定了哪些线程或与线程相对应的协程执行。将协程限制在一个特定的线程执行，或将它分派到一个线程池，亦或是让它不受限地运行。
     4. AbstractCoroutine 包含父Context和当前Context


### Kotlin 详细
[详见](./知识体系-平台-Android-cohesion-kotlin.md)

### 解耦-DI koin