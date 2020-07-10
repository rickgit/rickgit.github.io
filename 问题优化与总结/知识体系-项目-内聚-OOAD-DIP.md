

## 依赖倒置设计原则（DIP）
### 依赖倒置实现：控制反转（IOC）

#### 控制反转实现：依赖注入（DI）
对象依赖注入方式： 接口，set方法,构造方法,Java注解


### koin

### google/dagger.android

Dagger 2 是 Java 和 Android 下的一个完全静态、编译时生成代码的依赖注入框架
，完全去除了反射。

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
                          @mapkey 作为map的key类型。如HashMap<@mapkey,@mapkey(value)>
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