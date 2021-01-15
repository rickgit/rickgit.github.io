# 持续集成(Jenkins/TeamCity)
 Continuous integration (CI)
## Apache-commons-cli
```
+---------------------------+--------------------+----------------------+
|                           |                    |                      |
| opt  longOpt hasArg       |    GnuParser       |                      |
| description  required     |                    |     <char,values>    |
| multipleArgs type  values |    PosixParser     | args  options  types |
|                           |                    |                      |
+-----------------------------------------------------------------------+
|         Option            |    parser          | CommandLine          |
+---------------------------+--------------------+----------------------+
|                                                                       |
|                 apache-commons-cli                                    |
+-----------------------------------------------------------------------+ 


  001_initial         aae50c585ec3ac33c6a9af792e80378904a73195 moved cli over from the sandbox to commons proper
  002_posixparser     4cca25d72b216bfc8f2e75e4a99afb608ceb6df8 configurable parser support added, PosixParser added
  003_gnuparser       ead3757ef361f85cfb92754e808dafd74eb15702 GNU parser, one unit test for Ant
  004_parser          dde69934d7f0bee13e4cd1fc99a7d60ce95a0c78 separated the argument flattening from the Option processing
  005_optionvalidator 347bbeb8f98a49744501ac50850457ba8751d545 refactored the option string handling, added property support for options with an argument value
  006_cli2            8f95e4a724350f9f80429c2af1c3ac9bb2b2c2db Merged RESEARCH_CLI_2_ROXSPRING branch back to HEAD
  007_Avalon          107ea069b95617c3c3e537abdaffe11b858d3e16 Added the old Avalon Excalibur CLI package Submitted by: Sebb Issue: 34672
* 008_pom             4b799deefa16558f8af0eaab299e8c0b09c9e2aa Create minimal POM

```
## Apache Ant
使用技术：Java内省
gnu make->git->ant->maven->groovy->gradle->Android gradle sdl
gnu make->Autotools（配置文件复杂） ->Cmake（配置文件只需要写上源文件及生成类型，同一个目标的配置可能会零散分布在各个地方）->gyp（模块化、结构化）->gn（GN比GYP速度快20倍）
```
[organisation]-[module]-[revision]-[type].[ext]
+------------+------------------------------------------------------+
|            |                                                      |
|            |                                                      |
|            |        install report  publish                       |
|            |                                                      |
|            +---------------+--------------------------------------+
|            |    retrieve   |   ivy.retrieve.pattern               |
|            |               |   (project libs path)                |
|            +------------------------------------------------------+
|            |               |   ivy.xml                            |
|            |    resolve    |(which dependencies should be resolved)|
|            |               |   .ivy-cache (cache path)            |
| Ant Task   +------------------------------------------------------+
|            |   Configure   |   ivyconf.xml (resolvers)            |
|            |               |   ivy.properties                     |
|            |               |typedef.properties (resolvers,Strategy,Conflict)|
|            |               |   repository.properties              |
+------------+---------------+--------------------------------------+
|                   build.xml(antlib.xml:fr.jayasoft.ivy.ant)       |
+-------------------------------------------------------------------+
|                        ivy                                        |
+-------------------------------------------------------------------+


+---------------------------------------------------------------+
|                          execute()                            |
+---------------------------------------------------------------+
|     Introspector               replaceProperties              |
+---------------------------------------------------------------+
|                            property                           |
+---------------------------------------------------------------+ 
|  mkdir javac chmod deltree  jar copydir copyfile rmic         | 
|  cvs get expand echo javadoc2 keysubst zip gzip               | 
|  replace java  tstamp property  taskdef ant exec              | 
+---------------------------------------------------------------+
|   Task (taskdefs/defaults.properties)                         |
+---------------------------------------------------------------+
|  initTarget   depends       execute()                         |
|   Target                                                      |
+---------------------------------------------------------------+
|     Project                                                   |
+---------------------------------------------------------------+
|               org.apache.tools.ant.Main     properties        |
+---------------------------------------------------------------+ 
|                      build.xml                                |
+---------------------------------------------------------------+


```
## Apache Maven

``` 
  001_initial_maven-mboot             f646e34f614ca93b7ca3319834582422b5f07a8b Initial revision
  002_modello                         323b3bdaab45eb94f69e52bab77b6a4f28688997 o adding a script for modello which is now used to generate the maven model   sources from a modello model file.
  
  003_compiler-jar-resources-surefire ee951b16093bdbfc264108732cd894e83097d3ab Initial revision
  004_clean                           fe5c4741abbceee024b6197f3616d079fb7fe53f A simple clean plugin
  005_core-it                         34b0e98e97fead5f2319e6b3579e069df851b60a Initial revision
  006_pom                             85107479c032e250ec212915e7693758c30f7f1d Initial version of pom plugin
  007_mboot2                          65a5fee93523664945a532ec541c225ad18c6a03 Initial revision
  008_repository                      b85639283fffaadc8168c68f290bca4ffea15f94 *** empty log message ***
  009_marmalade                       859b2cac34dd248c31334152ee0077b898be077f o Added marmalade scripted mojo discoverer and tests. o Minimal modifications to the MavenPluginDescriptor to allow building of instances without privileged field access. o Still need to add a pom.xml for the marmalade-plugin-loader (or whatever we should call it).
  
  010_core_m2                         601320d28fe961c1d5f01de7af6273488ca0f7a9 Initial revision
  011_diagram                         63c6bcdcc03e321c6f73c87be0dcfb484225519c o Cleaned up a bit. Made the diagram prettier, and cleaned up the footnotes.
  012_archetype                       9a893526e582c9f3c5bb572bcc709638025222af Initial revision
  013_reporting                       7b7f7659d812310dcc0a9763bde554d103e0f310 Initial revision
 
```

```
Maven Sources Overview
+--------------------------------------------------------------------------------+
|       mvn clean dependency:copy-dependencies package                          |
|             ^        ^           ^             ^                              |
|             |        |           |             |                              |
|           phase  plugin        goal           goal                            |
+-------------------------------------------------------------------------------+
|         LifecycleMapping                                                      |
|     goal->plugin(mojo#execute())                                              |
|        phase                   properties         Model(project.xml,pom.xml)  |
|                           (system,build.properties,*)                         |
|    MavenLifecycleManager                    MavenProject                      |
|       #execute()                       MavenProjectBuilder                    |
+----------+--------------------------------------------------------------------+
|   core:  | maven                                                              |
+-------------------------------------------------------------------------------+
|          |core: clean compiler deploy install resource site surefire verifier |
|  plugins |package: jar                                                        |
|          |reporting:                                                          |
|          | Tools: archetype assembly dependency    plugin repository          | 
+----------+--------------------------------------------------------------------+
|   doxia:                                                                      |
|   misc ：       archetypes poms wago                                          |
+----------+--------------------------------------------------------------------+
|   shared |     jar resources                                                  |
+-------------------------------------------------------------------------------+
|   plexus | classloadworld (classloader)  containers (IOC) Modello (code gen)  |
+----------+--------------------------------------------------------------------+
|     convention over configuration       commons-cli                           |
+-------------------------------------------------------------------------------+

```


[Maven运行原理](https://blog.csdn.net/shaoweijava/article/details/74370312)

#### Plexus
容器
+-----------------------------------------+---------------------------------------+
|          RealmClassLoader               |                                       |
|                   addConstituent        |                                       |
+-----------------------------------------+                                       |
|            ClassWorld                   |                                       |
|                  Map<ClassRealm>        |                                       |
+-----------------------------------------+                                       |
|             Launcher                    |                                       |
+-----------------------------------------+                                       |
|           Configurator                  |                                       |
|                  classworlds.conf       |  components.xml                       |
+---------------------------------------------------------------------------------+
|           Plexus classworlds            |   Plexus Container                    |
+-----------------------------------------+---------------------------------------+



####  plugins
```
Maven is - at its heart - a plugin execution framework; 
```
[Maven Plugin](http://maven.apache.org/plugins/index.html)
   
+--------------------------------------------------------------+
|    reportClassNames   batteries                              |
|             BatteryExecutor                                  |
+--------------------------------------------------------------+
|                                surefire-battery      junit   |
+--------------------------------------------------------------+
|                        maven-surefire                        |
+--------------------------------------------------------------+  

 
## Gradle 

[](https://juejin.im/post/5cf3e4dfe51d454d56535790)

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

#### Android-DSL
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

#### 问题
```
Could not find com.android.tools.build:aapt2 AndroidStudio



Android: A problem occurred configuring project ':app'. > java.lang.NullPointerException (no error message) 
org.gradle.java.home=/Library/Java/JavaVirtualMachines/{your jdk}/Contents/Home


```
[Beginning with Android Studio 3.2, AAPT2 moved to Google's Maven repository](https://developer.android.com/studio/releases)


[JetBrains intellij android](https://github.com/JetBrains/android.git)
[android gradl dsl com.android.tools](https://source.codeaurora.cn/quic/la/platform/prebuilts/gradle-plugin)

[android gradle-plugin relsease notes](https://developer.android.google.cn/studio/releases/gradle-plugin)
#### Android gradle

```gradle

    gradle -q dependencies your-app-project:dependencies


```


### 源碼

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




### Dependency injector(dagger-google)
```
  002_injector            04892c03bad8cc45a3c509880c33e7f1d41f5a47 First draft of a new, fast dependency JSR-330 injector.
  003_code_gen            576136ba600e81192960350f58503dfed9d824f4 New, very basic Java code generator.
  004_modules             2ecf2ed141990194e89d88f7d159aca86a39c5aa Add Modules.override().
  005_stringKey           733a7f6431616202b248adc6cf9d619bd4750162 Use Strings for Keys.
  006_ProvidesProcessor   2a5de416ac26b37cde2d07c66873931495db6da8 First draft of the @Provides processor.
  007_InjectProcessor     6fd3de3797baa8b44789fd50a43b8daa5bc7fcd7 First draft of the @Inject processor.
  008_DependencyGraph     4890139ad39ae959dc5b0bb9e638fc66ef702513 New annotation-driven API that uses entry points.
  009_static_inject       34e2a25c3a6eed0b261c8be4b00610c6df782bc3 Support static injection.
  010_google_guice        63a7e1ce3f7d202d907b4227ab51e98549d91d76 Minimal support for Guice annotations to ease migration and A/B testing.
  011_@module             52a260e81054eec5bfd47f419537cbadc2045612 Replace @Injector with @Module.
  012_ProblemDetector     3f013f16c6663fa023c1c64a4a291e7083712fe7 Detect problems at runtime.
  013_LazyInjection       31ad32fd607773ac3c0d0f7fdde26591a459506e Support lazy object graphs.
  014_androidmenifest     4136fd157588b3ea64f054e82d10e4c09419deb4 Initial infrastructure to generate modules from AndroidManifest.xml.
  015_FullGraphProcessor  7b0c34fe4def38191eef738ab391b7a5810bc18a Build time graph construction and validation.
  016_LazyBinding         69c9daf5931d27902dd2282d0e606a83bc993ead Add a Lazy<T> with a delegate binding, so consumers can @Inject a Lazy<T> if T is bound. Tighten up key mangling code
  017_SetBinding          0656b1a9d346099d3138da9d50b20c4efa1486e8 Add support for multi-bindings, such that any method annotated with Provide AND Element contribute that binding to a set-binding, which builds an injectable set at injection time.
  018_OneOf               4670896e18e3e748ac33ff42f388450c082e2c39 Rename Element to OneOf
  019_dagger-compiler     f0de3941d22fa017c377f05822f225005d9dd7fc Migrate code-gen processors to their own project, and separate reflection code.
* 020_ModuleGeneratorTask ecfce61047a6aee7b57d27dad9f7f1d7b6339535 Allow manifest module generator invocation as Ant task.
  master                  a8a00fa96d0a2b661b9e2f05bbc91e091e978d14 [behind 9] Improvements to Dagger BindingGraphConverter performance (part 2).

```
类转化为Binding,通过获取泛型类型为Key，转化为Map，在最终调用Binding的get或injectMembers方法，反射实例化对象。
最初实例用反射生成，改用生成模板代码，new 生成实例。
代码生成器CodeGen(JavaPoet)
注解处理器APT(google auto)
ProvidesProcessor 将provideMode转化为类似于ProviderMethodBinding类；
InjectProcessor 将含有InJect注解转化为类似于ConstructorBinding类；

DependencyGraph替代原本com.squareup.injector類，com.squareup.injector變爲接口，作爲依賴圖的入口

ObjectGraph替代DependencyGraph；Injector換爲Module，由入口含義改爲包含提供Object的對象

使用Guice(Google Inject)

```
            +-------------------------------+
            |                               |
            v                               |
+-----------+-----------------+-----------+-+----------+-------------------+
|  BuiltInBinding             |           |            |                   |
|(Provider<T>,MeberrsInjector)|           |            |                   |
|  ConstructorBinding         |           |Deferred    |Provider<T>.get()  |
|  (AtInjectBinding)          |           |            |                   |
+-----------------------------+ unattached|Binding     |                   |
| ProviderMethodBinding       |  Bindings |            |                   |
|     (@Provides)             |           |            |                   |
|Binding(singleInstance)      |           |            |                   |
|(@Pro^ides @Singleton)       |           |            |                   |
+-----------------------------+           |            |MembersInjector<T>.|
|  bindings                   |           |            |injectMembers(T t) |
|(Keys<Type+Qualifier>,bindding)|         |            |                   |
|                             |           |            |                   |
+-----------------------------------------+--------------------------------+
|     Inject                  |   linker(attach)       |binding            |
+--------+--------------+-----+--------------------------------------------+
|  Field | Constructor  | method parameters                                |
+--------+--------------+--------------------------------------------------+
|         google  dagger   injecter                                        |
+--------------------------------------------------------------------------+



```

## 持续集成 Jenkins
[持续集成、持续交付与持续部署](https://blog.csdn.net/shi_chang_zhi/article/details/80724801)

### 源码
1. [入门](https://www.jenkins.io/zh/doc/pipeline/tour/getting-started/)
```
下载 Jenkins.
打开终端进入到下载目录.
运行命令 java -jar jenkins.war --httpPort=8080.
打开浏览器进入链接 http://localhost:8080.
按照说明完成安装.
```
[加速](https://www.cnblogs.com/hellxz/p/jenkins_install_plugins_faster.html)
```bash
docker-machine ssh default // 先进入虚拟机，default 是默认的虚拟机名称
sudo vi /var/lib/boot2docker/profile // 编辑这个文件，添加镜像源 --registry-mirror https://registry.docker-cn.com
sudo /etc/init.d/docker restart // 重启 docker 进程
exit // 退出虚拟机
docker info // 看一下镜像源是否设置成功（是否有刚刚设置的 --registry-mirror 这一行）

sed -i 's/http:\/\/updates.jenkins-ci.org\/download/https:\/\/mirrors.tuna.tsinghua.edu.cn\/jenkins/g' default.json && sed -i 's/http:\/\/www.google.com/https:\/\/www.baidu.com/g' default.json
```

2. 入口
jenkins.war/META-INF/MANIFEST.MF

```
+-----------------------------------------------------------------------+
|    org.kohsuke.stapler.Stapler             hudson.WebAppMain          |
|                                                                       |
|                                                                       |
|                                              HudsonIsLoading          |
|                                                                       |
+-----------------------------------------------------------------------+


```
## GNU make,CMake，Ninja
[GNU ](git clone https://git.savannah.gnu.org/git/make.git)

[](https://www.gnu.org/software/make/manual/make.html#Simple-Makefile)

```

* 000_tex           9f8301ae1ac6d9076e38ec86f12d59ba40b851bd * Added RCS log.
            +--------------------------------------------------------------------------+
            |  make.texinfo                                                            |
            +--------------------------------------------------------------------------+
            |   @section                                                               |
            |               Simple           Functions     Archives                    |
            |               Makefiles  	     Running       Missing                     |
            |               Rules                          Concept Index               |
            |               Commands         Implicit      Name Index                  |
            |               Variables                                                  |
            |               Conditionals                                               |
            |                                                                          |
            +--------------------------------------------------------------------------+

  001_initial       83fcf12d25ee341440cead2dcc87451c55dd8203 Initial revision
  002_vpath         59fdb0d242076fc124f2049bd7590d7b451cd9bd Initial revision
  003_expand        1a51e308e5009c9e98e431bc368c28aee644176d Initial revision
  004_implicit      b9c983dee3c6aff460532d9b4fd7aaea079c672b Initial revision
  005_arscan        bd3a693fd4cd0f91bc0a6e598dac260438d2acbd Initial revision
  006_misc_variable 97d38d08176611af5e10ab84e4c812962bf98600 Initial revision
  007_command       a4a1d4a78d32dfaf23f3ab96f4ea5771ef289446 Initial revision
  008_dir           da136cba6f72f16396e1e9be07443b540dc97199 Initial revision
  009_makefile      1eadb68598fde7e391414fd2651ee33906a2c5d7 Initial revision

            +--------------------------------------------------------------------------------------------------+
            |  make.texinfo                                                                                    |
            |   @section                                                                                       |
            |   Simple           Functions     Archives                                                        |
            |   Makefiles  	     Running       Missing                                                         |
            |   Rules                          Concept Index                                                   |
            |   Commands         Implicit      Name Index                                                      |
            |   Variables                                                                                      |
            |   Conditionals                                                                                   |
            |                                                                                                  |
            +--------------------------------------------------------------------------------------------------+
            |                           main.c                                                                 |
            |                              decode_switches()              enter_file() :file                   |
            |                              other_args:stringlist *        read_all_makefiles():dep             |
            |                              switches:command_switch                                             |
            |                                                                                                  |
            |                              update_goal_chain():int                                             |
            |   job.c                                                                                          |
            |      child *children //xmalloc (sizeof (struct child))      stringlist:struct                    |
            |      new_job ()                                                 list:char **                     |
            |                                                                 idx:uint                         |
            |                                                                 max:uint                         |
            +--------------------------------------------------------------------------------------------------+

  370_3.70.2        408e868fd24232052b37c7e188efb321acc0391f (Using Variables): Fixed @xref node to `Automatic'.
 

```
```

+-------------------------------------------------------------------------------+
|                                                                               |
|   ar.dep         default.dep    expand.dep      rule.dep     variable.dep     |
|                                                                               |
|   arscan.dep     dir.dep        file.dep        signame.dep  version.dep      |
|                                                                               |
|   commands.dep                                               vpath.dep        |
|                                                                               |
+-------------------------------------------------------------------------------+
|                       GNU make                                                |
+-------------------------------------------------------------------------------+

+-------------------------------------------------------------------------------------------+--------------+------------+
|                                            vfork execve                                   |              |            |
+-------------------------------------------------------------------------------------------+              |            |
|                                         command(job.c) command.h                          |              |            |
+-------------------------------------------------------------------------------------------+--------------+------------+
|                      switch                                                                                           |
+-----------------------------------------------------------------------------------------------------------------------+
|                     GNU make (Automake)                                                                               |
+-----------------------------------------------------------------------------------------------------------------------+

+-----------------+-------------------------------------+ 
|     a c d i p s |                                     |
+-------------------------------------------------------+
|    -e   -f      |      -h     -n -V                   |
|                 |                                     |
+-----------------+-------------------------------------+
|               Gnu sed                                 |
+-------------------------------------------------------+

[GNU 命令大全](https://www.runoob.com/linux/linux-comm-cat.html)

[gnu](http://www.gnu.org/software/software.html)
cat tr sed grep
```

[GNU make,makefile](https://www.gnu.org/software/make/manual/make.html)

### Cmake
gcc 编译少个源文件
makefile 编译复杂多个源文件
autotool（autoconf+automake） 生成makefile文件
        [autoconf 文档](https://www.gnu.org/savannah-checkouts/gnu/autoconf/manual/autoconf-2.69/autoconf.html#Making-configure-Scripts)
```python
apt-get install autoconf 
    //涉及命令autoscan，aclocal，autoheader,autoconf，automake,configure，make
    //-------------------------------------------------------------------------------------------------------------
     your source files --> [autoscan*] --> [configure.scan] --> configure.ac
     
     configure.ac --.                                                      
                    |   .------> autoconf* -----> configure
     [aclocal.m4] --+---+
                    |   `-----> [autoheader*] --> [config.h.in]
     [acsite.m4] ---'
     
     Makefile.in（需要手动编写）

    //-------------------------------------------------------------------------------------------------------------
     [acinclude.m4] --.
                      |
     [local macros] --+--> aclocal* --> aclocal.m4
                      |
     configure.ac ----'
     
     configure.ac --.
                    +--> automake* --> Makefile.in
     Makefile.am ---'

    //-------------------------------------------------------------------------------------------------------------
                            .-------------> [config.cache]
     configure* ------------+-------------> config.log
                            |
     [config.h.in] -.       v            .-> [config.h] -.
                    +--> config.status* -+               +--> make*
     Makefile.in ---'                    `-> Makefile ---'
    //-------------------------------------------------------------------------------------------------------------
1、autoscan
2、修改生成的configure.scan为configure.ac，修改内容 ----- AC_INIT行下添加AM_INIT_AUTOMAKE(hello,1.0) --- AC_OUTPUT改为 AC_OUTPUT(Makefile)
3、aclocal
4、autoheader
5、autoconf
6、创建Makefile.am并进行具体内容的写入（touch Makefile.am INSTALL NEWS README AUTHORS ChangeLog COPYING  compile install-sh missing  depcomp ）
    bin_PROGRAMS = hello
    hello_SOURCES = hello.c
7、automake
8、./configure生成Makefile
9、make得到可执行程序
 [](https://blog.csdn.net/yygydjkthh/article/details/43197031)


cmake 替换 autoconf+automake，因为其流程过于复杂
      [](https://cmake.org/cmake/help/v3.18/guide/tutorial/index.html#a-basic-starting-point-step-1)
 
1. 创建CMakeLists.txt（touch CMakeLists.txt）,并写入以下信息
  cmake_minimum_required(VERSION  2.8.12.2) 
  project(Tutorial) 
  add_executable(Tutorial hello.c)
2. mkdir build & cd build //创建编译文件夹
3. cmake ..        //生成makefile
4. cmake --build . //生成二进制文件
``

### ninja 构建Android和chromium
 ninja 代替 make
  CMake或GN 生成 ninja 编译文件


```ninga
+------------------------------------------------------------------------------------+
|                                                                                    |
|                         Plan                                                       |
+------------------------------------------------------------------------------------+
|                        State                                                       |
+-----------------+------------------------------------------------------------------+
|   StatCache     |             Edge                                                 |
|                 +------------------------------------------------------------------+
|                 |             Rule        Node                                     |
|                 +------------------------------------------------------------------+
|                 |             command_   FileStat                                  |
+-----------------+------------------------------------------------------------------+
|                              Ninga                                                 |
+------------------------------------------------------------------------------------+

```
