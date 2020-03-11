# Gradle

Gradle provides a Domain Specific Language (DSL), for describing builds. 

Gradle models its builds as Directed Acyclic Graphs (DAGs) of tasks (units of work). 


```
+---------------------------------------------+
|  Project ,Task(Plugin Task)   dependence    |
|                                             |
+---------------------------------------------+
|  Script                                     |
|                                             |
|  GroovyShell/MOP(EMC  )          AntBuilder |  
|                                          Dag|
+---------------------------------------------+
|                 build                       |
+---------------------------------------------+
|                 Gradle                      |
+---------------------------------------------+



```

```
+-----------------------------------------------------------------------------------------------+
| Build                                                                                         |
|         Build Lifecycle                                                                       |
|                     Initialization    Configuration      Execution                            |
|                                                                 +-----------------------------+
|                                                                 |  configuration files        |
|                                                                 |       build.gradle          |
|                                                                 |       gradle.properties     |
|                                                                 |       settings.gradle       |
+-----------------------------------------------------------------+-----------------------------+
|                                                                                               |
|  Object  +------------------------------------------------------------------+-----------------+
|          | Project                                                          |                 |
|          |     +-------------------------------------+----------------------+  Script         |
|          |     |  properties                         | method               |                 |
|          |     |      project                        |   apply plugins(add task)|             |
|          |     |      name                           |        script plugins|                 |
|          |     |      path                           |        binary plugins|                 |
|          |     |      description                    |                      |                 |
|          |     |      projectDir                     |                      |                 |
|          |     |      buildDir                       |   getExtensions      |                 |
|          |     |      group                          |                      |                 |
|          |     |      version                        |                      |                 |
|          |     |      ant                            |                      |                 |
|          |     +-------------------------------------+----------------------+                 |
+----------+------------------------------------------------------------------+-----------------+
|                        Task(similar to ANT’s target)                                          |
|             Actions             Inputs             Outputs                                    |  
+-----------------------------------------------------------------------------------------------+
|                          Groovy comes with various AST transformations                        |
+-----------------------------------------------------------------------------------------------+
|                                    encoded UTF-8                                              |
+-----------------------------------------------------------------------------------------------+


```

[官方文档](https://docs.gradle.org/current/userguide/dependency_types.html)

## Android-DSL
```
+---------------------------------------------+
|                                             |
|  gradle 2.x           gradle >=3.0          |
|                                             |
+---------------------------------------------+
|  provided           compileOnly             |
|  apk                runtimeOnly             |
|  compile            api/implementation      |
+---------------------------------------------+

```
api传递依赖库
implement不传递依赖库

[Android Plugin DSL Reference](http://google.github.io/android-gradle-dsl/current/index.html)
[Android Plugin new future](https://developer.android.com/studio/releases/gradle-plugin)

### 问题
```
Could not find com.android.tools.build:aapt2 AndroidStudio

```
[Beginning with Android Studio 3.2, AAPT2 moved to Google's Maven repository](https://developer.android.com/studio/releases)


[JetBrains intellij android](https://github.com/JetBrains/android.git)
[android gradl dsl com.android.tools](https://beijing.source.codeaurora.org/quic/la/platform/prebuilts/gradle-plugin)
[android gradle-plugin relsease notes](https://developer.android.google.cn/studio/releases/gradle-plugin)
## Android gradle

```gradle

    gradle -q dependencies your-app-project:dependencies


```


## 源碼

```
* 001_code c296cd129b7e2b647cf1302042d9fbedb59fca32 Move HEAD to trunk directory
            +-------------------------------------------------------------------------------------------------------------------------------------------------------+
            |                   gradle.bat                                                                                                                          |
            +-------------------------------------------------------------------------------------------------------------------------------------------------------+
            |               org.gradle.ToolsMain(windowsStartScriptTail.txt)                                                                                        |
            |                        contextClassLoader:URLClassLoader                                                                                              |
            +-------------------------------------------------------------------------------------------------------------------------------------------------------+
            |               org.gradle.Main                                                                                                                         |
            |                             CliBuilder                                                                                                                |
            +-------------------------------------------------------------------------------------------------------------------------------------------------------+
            |                Build                                                                                                                                  |
            |                   settingsProcessor     projectLoader                                   buildConfigurer                     buildExecuter             |
            |                   run(taskNames)                                                                                                                      |
            +-------------------------------------------------------------------------------------------------------------------------------------------------------+
            |SettingsProcessor                        ProjectsLoader                                 |BuildConfigurer                    |BuildExecuter             |
            |  settingsFileHandler                     projectFactory                                |  ProjectsTraverser                |     dag                  |
            |  dependencyManager                       buildScriptProcessor                          |  projectDependencies2TasksResolver|                          |
            |  process():DefaultSettings               buildScriptFinder                             |  buildClasspathLoader             |                          |
            |                                          pluginRegistry                                |  projectTasksPrettyPrinter        |Dag           DefaultTask |
            |                                          load(settings)                                |  process(rootproject)             | fOut:MultiMap   execute()|
            |                                                                                        |                                   | fIn:MultiMap             |
            |SettingsFileHandler DefaultSettings      ProjectFactory           DefaultProject:Project| ProjectDependencies2TasksResolver | execute()                |
            |  settingsText       dependencyManager    createProject()           createTask()        |  projectsTraverser                |                          |
            |Script               createClassLoader()  dependencyManagerFactory  evaluate()          |                                   |                          |
            |  metaClass                                                                             |                                   |                          |
            |                                         BuildScriptProcessor                           |                                   |                          |
            |                                            evaluate(project):ProjectScript             |                                   |                          |
            +-------------------------------------------------------------------------------------------------------------------------------------------------------+
            |                                       initial                                          |       configuration               |       execution          |
            +----------------------------------------------------------------------------------------+-----------------------------------+--------------------------+
            | DefaultProject:Project.DynamicObjectAware                                                                                                             |
            |     getAsDynamicObject():DynamicObject                  projectScript:Script                                             usePlugin(pluginName:String) |
            |                                                         dependencies :DefaultDependencyManager                           createTask()                 |
            +     +-------------------------------------------------------------------------------------------------------------------------------------------------+
            |     |             (groovy) GroovyShell                  Script                            Closure                (gradle)Plugin  //only help tasks    |
            |     |                            parse():Script            metaClass:ExpandoMetaClass        delegate                       apply()//create tasks     |
            +     +--------------------------------------------------------------------------------------------------+----------------------------------------------+
            |     |                DefaultDependencyManager                                                          | JavaPlugin//TaskGroup:build,doc,other,veri   |
            |     |                         ivy                                                                      |     configureDependencyManager()             |
            |     |                       dependencyFactory:[ArtifactDependency, ModuleDependency, ProjectDependency]|           :Compile                           |
            |     |                        resolveClasspath(taskName):List                                           | Compile:Task                                 |
            +     |                                                                                                  |----------------------------------------------+
            |     |                                                                                                  |ConventionTask: IConventionAware              |
            |     |                                                                                                  |  getConventionMapping():ConventionAwareHelper|
            |     |                                                                                                  |  configure(closure):Task                     |
            +-----+--------------------------------------------------------------------------------------------------+----------------------------------------------+
            +-------------------------------------------------------------------------------------------------------------------------------------------+
            |         BuildScopeServices                                                                                                                |
            |              GradleScopeServices                                                                                                          |
            |                     ProjectScopeServices                                                                                                  |
            |                           TaskScopeServices                                                                                               |
            |                                                                                                                                           |
            |              SettingsScopeServices                                                                                                        |
            +-------------------------------------------------------------------------------------------------------------------------------------------+
          dependences {classpath 应用外部插件}
  002_ant     768b9a07b0d52e1c15d72aeec6224514fcf6c5b5 Move HEAD to trunk directory
  003_gradle  fb4a693417fe219d578060b1d4232c731c63732e Move HEAD to trunk directory
  004_ivy     cf9bc100f289ec5e659a85250494d13428b81fde Move HEAD to trunk directory
  005_website d9b34c1d1414ecfc0c36312c880b3b59740ac378 adding website source to svn
  100_REL_1.0 c69571460f7035fa40827f72b2b5cfeb362afd20 updated for 1.0 release
              [1.0/release-notes](https://docs.gradle.org/1.0/release-notes.html)  dependency management
              IOC
              +-------------------------------------------------------------------------------------------------------------------------------------------+
              |                   gradlew                                                                                                                 |
              +-------------------------------------------------------------------------------------------------------------------------------------------+
              |    (plugins)        StartScriptGenerator                                                                                                  |
              +-------------------------------------------------------------------------------------------------------------------------------------------+
              |    (launcher)   org.gradle.launcher.GradleMain                                                                                            |
              +-------------------------------------------------------------------------------------------------------------------------------------------+
              |                          ProcessBootstrap                                                                                                 +
              |                                   MutableURLClassLoader(antClassLoader, runtimeClasspath)                                                 |
              +-------------------------------------------------------------------------------------------------------------------------------------------+
              |                          Main                                                                                                             |
              |                            doAction(listener)                                                                                             |
              +-------------------------------------------------------------------------------------------------------------------------------------------+
              |         CommandLineActionFactory                                                                                                          |
              +-------------------------------------------------------------------------------------------------------------------------------------------+
              | ExceptionReportingAction     WithLogging                       ParseAndBuildAction                                                        |
              |       action:WithLogging        action :ParseAndBuildAction         loggingServices                                                       |
              |       reporter                  loggingServices                     args                                                                  |
              |                                 loggingConfiguration                execute()                                                             |
              |                                 args                                                                                                      |
              +-------------------------------------------------------------------------------------------------------------------------------------------+
              |                                    CommandLineAction                                                                                      |
              |                                       configureCommandLineParser()                                                                        |
              |                                       createAction()                                                                                      |
              | BuiltInActions      GuiActionsFactory  +--------------------------------------------------------------------------------------------------+
              |   ShowUsageAction      (ui)            |                    BuildActionsFactory                                                           |
              |   ShowVersionAction                    |                                                                                                  |
              +-----------------------------------------                                                                                                  |
              |  StopDaemonAction      ForegroundDaemonMain               DaemonBuildAction                        RunBuildAction                         |
              |                            :DaemonMain                        executer                                                                    |
              |                                                               *startParameter                                                             |
              +-----------------------+---------------------------+---------------------------------+                                                     |
              |                       |  (base+services)          | DaemonClient                    |                                                     |
              |HandleStop             | DaemonServices            |    :GradleLauncherActionExecuter|                                                     |
              |   :DaemonCommandAction|   :DefaultServiceRegistry |    execute()                    |                                                     |
              |                       |   createxxx()             | Build:Command                   |                                                     |
              |                       | Daemon                    |     action                      |                                                     |
              |                       |   connector               | BuildCommandOnly                |                                                     |
              |                       |   stateCoordinator        |   :DaemonCommandAction          |                                                     |
              |                       |   start()                 +---------------------------------------------------------------------------------------+
              |                       |                           | (core)*DefaultGradleLauncher                                                          |
              |                       |                           |          run()                                                                        |
              |                       |                           |          gradle:DefaultGradle                                                         |
              +-----------------------+---------------------------|                                                                                       |
              |     initScriptHandler                 settingsHandler                  buildLoader                     buildConfigurer                    |   
              |                                          findAndLoadSettings()           :InstantiatingBuildLoader         :DefaultBuildConfigurer        |
              |                                                :SettingsInternal         load()                       buildExecuter                       |
              |                                      settingsProcessor                                                    :DefaultBuildExecuter           |
              +------------------------------------------------------------------------+------------------------+----------------------------+----+--------+
              | DefaultInitScriptProcessor         |ScriptEvaluatingSettingsProcessor  |@NoConventionMapping    | BuildScriptProcessor       |    |       |
              |    configurerFactory               |              :SettingsProcessor   |DefaultProject          |       :ProjectEvaluator    |    |       |
              |        :DefaultScriptPluginFactory |    configurerFactory              |      :ProjectInternal  |    configurerFactory       |    |       |
              |                                    |       :DefaultScriptPluginFactory |   projectEvaluator     |       :ScriptPluginFactory |    |       |
              |                                    |                                   |   addChildProject()    |                            |    |       |
              |  DefaultGradle                     |DefaultSettings:SettingsInternal   |   evaluate()           |                            |    |       |
              |          :GradleInternal           |           rootProjectDescriptor   |   beforeCompile()      |                            |    |       |
              |       *taskGraph:TaskGraphExecuter |                                   |   *apply(closure)      |                            |    |       |
              |        startParameter              |                                   |   dependencies(closure)|                            |    |       |
              |        setRootProject()            |                                   |   getRepositories()    |                            |    |       |
              |                                    |                                   |   pluginContainer      |                            |    |       |
              +------------------------------------------------------------------------------------------------------------------------------+    |       |
              |                                 +-------------------------------------------------------------------------------------------------+       |
              |                                 | DefaultBuildExecuter                                                                                    |
              |                                 |       configurationActions:<BuildConfigurationAction>   DefaultTasksBuildExecutionAction                |
              |                                 |       executionActions:<BuildExecutionAction>           ExcludedTaskFilteringBuildConfigurationAction   |
              |                                 |       gradle:GradleInternal                             TaskNameResolvingBuildConfigurationAction       |
              |                                 |       configure(index) //chain of respon                       taskNameResolver//Task DAG,load repo     |
              |                                 |       execute(index)//chain of respon                                                                   |
              |                                 |                                                         DryRunBuildExecutionAction                      |
              |                                 |                                                         TaskCacheLockHandlingBuildExecuter              |
              |                                 |                                                         SelectedTaskExecutionAction                     |
              |                                 +---------------------------------------------------------------------------------------------------------++-------------------------------------------------------------------------------------------------------------------------------------------+
              | @NoConventionMapping    ClassGeneratorBackedInstantiator:Instantiator   AsmBackedClassGenerator                                           |
              | @NoDynamicObject               classGenerator:AsmBackedClassGenerator           generate(type)                                            |
              |                                instantiator:DirectInstantiator                                                                            |
              |                                                                                                                                           |
              |        IConventionAware                 DynamicObjectAware        ExtensionAware               GroovyObject                               |
              |            getConventionMapping             getAsDynamicObject()      getExtensions()             getMetaClass():MetaClass                |
              |                 :ConventionAwareHelper           :DynamicObject          :ExtensionContainer      setProperty()                           |
              |                                                                                                   invokeMethod()                          |
              +-------------------------------------------------------------------------------------------------------------------------------------------+
              |  DefaultScriptPluginFactory:ScriptPluginFactory                                                          DefaultTaskGraphExecuter         |
              |     scriptHandlerFactory                                                                                         :TaskGraphExecuter       |
              |     scriptCompilerFactory                                                                                         execute()               |
              |                                                                                                                                           |
              | DefaultScriptCompilerFactory                                        ScriptCompilerImpl:ScriptCompiler                                     |
              |    scriptRunnerFactory:DefaultScriptCompilerFactory                       compile():ScriptRunner                                          |
              |    scriptClassCompiler:DefaultScriptRunnerFactory                                                                                         |
              |    createCompiler(source):ScriptCompiler                            ConfigureUtil                                                         |
              |                                                                         configure(configureClosure,delegate):delegate                     |
              | FileCacheBackedScriptClassCompiler:ScriptClassCompiler                                                                                    |
              |   cacheRepository                                                   DefaultAntBuilder                                                     |
              |   scriptCompilationHandler                                          DefaultProject                                                        |
              |                                                                     DefaultRepositoryHandler：RepositoryHandler, ResolverProvider         |
              | DefaultScriptCompilationHandler   DefaultPersistentDirectoryCache   DefaultDependencyHandler                                              |
              |       :ScriptCompilationHandler      :PersistentCache                      configurationContainer:DefaultConfigurationContainer           |
              |       compileToDir()                 init()                         DefaultArtifactHandler                                                |
              |       loadFromDir():DefaultScript    open()                         DefaultScriptHandler                                                  |
              |                                                                            CLASSPATH_CONFIGURATION// META-INF/gradle-plugins custom plugin|
              |                                                                                                   // ADD DefaultPluginRegistry            |
              +-------------------------------------------------------------------------------------------------------------------------------------------+
              | (plugins//to add tasks )  BasePlugin,  GroovyPlugin , JavaPlugin ,JavaBasePlugin ,ProjectReportsPlugin,ApplicationPlugin                  |
              +-------------------------------------------------------------------------------------------------------------------------------------------+
              |    JavaBasePlugin:Plugin                                                                                                                  |
              |          configureSourceSetDefaults()//add Compile task                                                                                   |
              +-------------------------------------------------------------------------------------------------------------------------------------------+
              | AntJavaCompiler  DefaultConfiguration                             DefaultDependencyResolver           ResolveIvyFactory                   |
              |                     dependencyResolver:DefaultDependencyResolver       ivyFactory                          resolverProvider               |
              |                     resol^e()                                          moduleDescriptorConverter               :DefaultRepositoryHandler  |
              |                                                                         :PublishModuleDescriptorCon^erter  transportFactory: RepositoryTransportFactory|
              +-------------------------------------------------------------------------------------------------------------------------------------------+
              |  (Dynamic ivy)                                                        DependencyGraphBuilder           PublishModuleDescriptorConverter   |
              |                                                                           resolve():                         convert():ModuleDescriptor   |
              | (module descriptor)                                                       moduleDescriptorConverter                                       |
              |                                                                                                                                           |
              +-------------------------------------------------------------------------------------------------------------------------------------------+
                  


              file -> scriptsource->ScriptPlugin->ScriptCompiler->ScriptRunner


  200_REL_2.0 e698cb5abc9cbb32aeab9ed8d5aa59ec633bcf61 Removed unused import.
              [2.0/release-notes](https://docs.gradle.org/2.0/release-notes.html)  move to Groovy 2.3.2 from Groovy 1.8 
              IOC/DI
              +-------------------------------------------------------------------------------------------------------------------------------------------+
              |                   gradlew                                                                                                                 |
              +-------------------------------------------------------------------------------------------------------------------------------------------+
              |(build-init)               Wrapper:DefaultTask                                                                                             |
              +-------------------------------------------------------------------------------------------------------------------------------------------+
              |(wrapper)          GradleWrapperMain                                                                                                       |
              |           Install         BootstrapMainStarter                                                                                            |
              |              createDist()           start(args,gradleHome)                                                                                |
              |              download                                                                                                                     |
              +-------------------------------------------------------------------------------------------------------------------------------------------+
              |         (launcher)GradleMain                                                     Main         CommandLineActionFactory                    |
              |                     MutableURLClassLoader(antClassLoader, runtimeClasspath)       doAction()      execute()                               |
              +-------------------------------------------------------------------------------------------------------------------------------------------+
              |        WithLogging                             JavaRuntimeValidationAction                                                                |
              |            execute()                                  action:ParseAndBuildAction                                                          |
              |            action:JavaRuntimeValidationAction                                                                                             |
              +-------------------------------------------------------------------------------------------------------------------------------------------+
              |                                       CommandLineAction                                                                                   |
              +-----------------------------------------------------------------+-------------------------------------------------------------------------+
              |    BuiltInActions                   GuiActionsFactory           |  BuildActionsFactory                                                    |
              |          ShowUsageAction                  ShowGuiAction         |                                                                         |
              |          ShowVersionAction                                      |                                                                         |
              +-----------------------------------------------------------------+     +-------------------------------------------------------------------+
              |    StopDaemonAction         ForegroundDaemonAction                    |       RunBuildAction                                              |
              |       client:DaemonClient           Daemon                            |          *startParameter                                          |
              +-----------------------------------------------------------------------+           executer:BuildActionExecuter                            |
              |                                                     DaemonClient:BuildActionExecuter                SingleUseDaemonClient:DaemonClient    |
              |                                                      connector                       Build:Command                                        |
              |                                                                                           action                                          |
              |                                                                                           parameters                                      |
              |                                                   +---------------------------------------------------------------------------------------+
              |                                                   | DaemonClientConnection:Connection  Daemon            DefaultIncomingConnectionHandler |
              |                                                   |       dispatch(message)               daemonRegistry           daemonStateControl     |
              |                                                   |       receive()                                                                       |
              |                                                   | DaemonCommandExecuter                                 DaemonCommandExecution          |
              |                                                   |        executeCommand()                                 command                       |
              |                                                   |                                                         daemonStateControl            |
              |                                                   |                                                         actions:DaemonCommandAction   |
              |                                                   |                                                         proceed()//chain of Respon    |
              |                                                   +---------------------------------------------------------------------------------------+
              |                                                   |       DaemonStateCoordinator:DaemonStateControl    ExecuteBuild:DaemonCommandAction   |
              |                                                   |              runCommand()                             execute(execution)              |
              |                                                   |              onStartCommand                           doBuild(execution,build)        |
              |                                                   +---------------------------------------------------------------------------------------+
              | InProcessBuildActionExecuter:BuildActionExecuter  |
              |     execute(action:ExecuteBuildAction,            |
              |          parameters:DefaultBuildActionParameters) |
              +-------------------------------------------------------------------------------------------------------------------------------------------+
              |  DefaultBuildController:BuildController           | (core)                                                                                |
              |             actionParameters   run()              |    DefaultGradleLauncher                                                              |
              |             startParameter                        |              run()       initScriptHandler buildLoader  buildConfigurer  buildExecuter|
              |             gradleLauncher                        |                                                                                       |
              +-------------------------------------------------------------------------------------------------------------------------------------------+
              | @NoConventionMapping    ClassGeneratorBackedInstantiator:Instantiator   AsmBackedClassGenerator  @Inject                                  |
              | @NoDynamicObject               classGenerator:AsmBackedClassGenerator           generate(type)   DependencyInjectingInstantiator          |
              |                                instantiator:DirectInstantiator                                       services:ServiceRegistry             |
              |                                                                                                                                           |
              |        IConventionAware                 DynamicObjectAware        ExtensionAware               GroovyObject                               |
              |            getConventionMapping             getAsDynamicObject()      getExtensions()             getMetaClass():MetaClass                |
              |                 :ConventionAwareHelper           :DynamicObject          :ExtensionContainer      setProperty()                           |
              |                                                                                                   invokeMethod()                          |
              +-------------------------------------------------------------------------------------------------------------------------------------------+



  300_REL_3.0 ad76ba00f59ecb287bd3c037bd25fc3df13ca558 Update gradle wrapper version
  400_REL_4.0 316546a5fcb4e2dfe1d6aa0b73a4e09e8cecb5a5 Add link to maven performance comparison
  500_v5.0.0  7fc6e5abf2fc5fe0824aec8a0f5462664dbcd987 Merge pull request #7821 from gradle/js/docs/update-note-about-min-android-version
* 600_v6.0.0  0a5b531749138f2f983f7c888fa7790bfc52d88a Merge pull request #11204 from gradle/eskatos/smoke-test/generated-api-jars

            +-------------------------------------------------------------------------------------------------------------------------------------------+
            |(launcher)              (bootstrap)                             (laucher)                                                                  |
            |   launcher.gradle.kts    org.gradle.launcher.GradleMain             org.gradle.launcher.Main                                              |
            |                                                                                                                                           |
            |                                                                                                                                           |
            +-------------------------------------------------------------------------------------------------------------------------------------------+
            |                                                   DefaultCommandLineActionFactory                                                         |
            |                                                                                                                                           |
            +-------------------------------------------------------------------------------------------------------------------------------------------+
            |                                                   DefaultGradleLauncher                                                                   |
            |                                                            gradle:DefaultGradle                                                           |
            +-------------------------------------------------------------------------------------------------------------------------------------------+
            |                                                   DefaultGradle:AbstractPluginAware,GradleInternal                                        |
            |                                                            defaultProject:DefaultProject ,AbstractPluginAware implements ProjectInternal, |
            |                                                                           DynamicObjectAware,FileOperations, ProcessOperations            |
            +-------------------------------------------------------------------------------------------------------------------------------------------+            
            |   (DefaultProject)                                                                                                                        |
            |                        PluginUseScriptBlockMetadataCompiler   DefaultScriptCompilationHandler                                            |
            |                                                                                                                                           |
            +-------------------------------------------------------------------------------------------------------------------------------------------+
            |   (lib)                       serialize-kryo                                                                                            |
            +-------------------------------------------------------------------------------------------------------------------------------------------+
     
```


### android-gradle-plugin( /platform/tools/build/ 0.9以前;/platform/tools/base/build-system  0.9以后)

查询Gradle 版本 
```bash
Gradle
# git log --reverse --all --grep='Gradle\ 6\.0'

android gradle plugin

#git tag | grep -P 'gradle'
```
[/platform/tools/base](https://beijing.source.codeaurora.org/quic/la/platform/tools/base/)
```
  002_initial_base             940b0f9046fe7ca212f8f1c24b373dd7b0dc0af7 Copy code from sdk.git to base.git
                              +--------------------------------------------------------------------------------------------------------+
                              |    (resources\META-INF\gradle-plugins)                                                                 |
                              |            android.properties          android-library.properties   android-reporting.properties       |
                              |                                        LibraryPlugin                ReportingPlugin                    |
                              +--------------------------------------------------------------------------------------------------------+
                              |  AppPlugin:BasePlugin                                                                                  |
                              |    sdk                                                                                                 |
                              |    registry:ToolingModelBuilderRegistry                                                                |
                              |            ModelBuilder                                                                                |
                              |                                                                                                        |
                              |    extension:AppExtension                                                                              |
                              |    createAndroidTasks(force)                                                                           |
                              |    variantManager:VariantManager                                                                       |
                              |                                                                                                        |
                              |  VariantManager                                                                                        |
                              |    createAndroidTasks()                                                                                |
                              +--------------------------------------------------------------------------------------------------------+
                              |  PrepareLibraryTask     Lint:DefaultTask                                                               |
                              |        :DefaultTask         //lombok  ecj                                                              |
                              |                                                                                                        |
                              |                         LintDriver                                                                     |
                              |                           analyze()                                                                    |
                              |                           runFileDetectors()                                                           |
                              +--------------------------------------------------------------------------------------------------------+

  003_gradle_plugin            5ca08b98c7a58e51c5f38ee25d16ffcd55349e15 Update internal gradle plugin.
  004_build                    e494641b510d866001a0bc10a8b00990ebc02021 Merge "move build into tools/base"
* 090_android_gradle_0.9.0     b8c5a93aaf8e5e189249d4651b69da8ed3523f4c Merge "Fix RS tests." into gradle_0.9
  100_Gradle_2.1               4d871a7914395be94cd611928b5b8cd3361dca4e Made Gradle plug-in 0.14.0 and Gradle 2.1 the minimum supported versions.
  200_Gradle_2.2               ffb22733515ed2dcffdacc59fdefd946e7f4d300 Allowed Gradle 2.2+ versions to be used with the gradle plugin
  200_android_gradle_1.0.0-rc1 ff9120792abc817e17e4f1cf26e44c6ae26312fc Merge "Update gradle plugin version to 1.0.0-rc1" into studio-1.0-release
  214_Gradle_2.14              1a0f93f48e8030ebeeaa2732f6885ab837697a29 Use fixed location for unit test reports
  220_android_gradle_2.2.0     050fc0b64b5bff7ca6905c1236297479ca495578 Make 2.2 build: Remove non-open-source plugins such as the C++ support
  231_Gradle_3.1               12bbadcc0367f9d954156e6681ace0a95aded631 Update to Gradle 3.1 in tools/base.
  240_Gradle_4.0               b6cadb26f569aa88d0e8e4d72262e6216f44a36d Update recommended plugin to 2.3.0
  300_android_gradle_3.0.0     5d179ada33c355edb5c7da0f5f98321116476ce0 Snap for 4407048 from eff7292a4b2f52a1fe1b871012fdef5a4209f35f to studio-3.0-release
  350_Gradle_5.0               83693d991dae267d6a354afa6f8e43d6bd825d8e Changes to support Gradle 5.0 milestone-1
  360_Gradle_6.0               708d9be0dbb33101d2f84031e3408cb71f87d00b Update AGP to Gradle 6.0



```

### kotlin-gradle-plugin