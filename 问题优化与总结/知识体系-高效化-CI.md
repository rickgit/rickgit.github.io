# 持续集成(Jenkins/TeamCity)
 Continuous integration (CI)
 
 软件打包和分发自动化

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
## Apache Ant （Java内省实现xml对象化）
gnu make->git->ant->maven->groovy->gradle->Android gradle sdl
gnu make（make包含很多默认）->Autotools（配置文件复杂） ->Cmake（配置文件只需要写上源文件及生成类型，同一个目标的配置可能会零散分布在各个地方）->gyp（python 项目，模块化、结构化）->gn（c++项目，GN比GYP速度快20倍）

### 配置文件
build.xml

### 源码
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
## Apache Maven 项目管理（Plexus容器）

### 配置文件
pom.xml
1. 生命周期
Maven 有三套独立的 Lifecycle:default、clean 和 site，每个 Lifecycle 包含多个 Phase。
2. 插件拓展
3. 依赖机制。坐标五元组，即：（groupId，artifactId，version，type，classifier）



### 仓库
``` java
  mavenCentral(); //最早
  jcenter() //Android Studio 0.8 版本起的默认 ；
  google()  //https://maven.google.com	

  //其他仓库
  maven{ url 'https://maven.aliyun.com/repository/public'}
  maven { url 'https://maven.aliyun.com/repositories/jcenter' }
  maven { url 'https://maven.aliyun.com/repositories/google' }
  maven { url 'https://maven.aliyun.com/repository/central' }
  maven { url "https://jitpack.io" }

  //本地仓库
  mavenLocal()   //{USER_HOME}/.m2/repository	                              
  maven {
      url 'file:///e:/repo/'                   
  } 



```
#### 仓库配置
[本地仓库配置](https://www.cnblogs.com/Bugtags2015/p/5168763.html)

```java

  apply plugin: 'maven'

  uploadArchives {
      repositories.mavenDeployer {
          repository(url: LOCAL_REPO_URL)
          pom.groupId = PROJ_GROUP
          pom.artifactId = PROJ_ARTIFACTID
          pom.version = PROJ_VERSION     
      }   
  }


task uploadLocalMaven(type: Upload) {
    group 'upload'
}
```

### 上传组件

[](https://juejin.im/post/5df10c116fb9a0165936e0b7)
//个人账号
https://bintray.com/signup/oss
//企业账号
https://bintray.com/

https://bintray.com/profile/edit key :44097c6d8dc66a328312ced58d25c46cdc346af2

### 源码


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

 
## Gradle 编译（ task闭包和task依赖构成生命周期）
``` bash
//打印信息

echo    println 'project '+ project.getProperties() >build.gradle & gradle 
 echo    "println \"project \"+ project.getProperties()" >build.gradle & gradle | sed 's/,/,\n/g' | sort
 echo    println "project ant "+project.ant.getProperties() >build.gradle & gradle
echo    println "project ant lib "+project.ant['ant.core.lib'] >build.gradle & gradle
echo    println 'project gradle'+ project.gradle.getProperties() >build.gradle & gradle 
echo    println "startParameter "+gradle.startParameter.getProperties() >build.gradle & gradle
echo    println "startParameter "+gradle.startParameter['taskNames'][0] >build.gradle & gradle
 echo    println "project plugins "+project.plugins.toString().replace(',',',\n') >build.gradle & gradle
echo    println "project extension"+project.extensions.getProperties() >build.gradle & gradle
echo    println "project java "+project.extensions['java'].getProperties() >build.gradle & gradle
echo    println "android "+project.extensions['android'].getProperties() >build.gradle & gradle
echo    println "applicationVariants "+project.extensions['android']['applicationVariants'][0].getProperties() >build.gradle & gradle
echo    println "variantData "+project.extensions['android']['applicationVariants']['variantData'][0].getProperties() >build.gradle & gradle
echo    println "assemble "+project.extensions['android']['applicationVariants'][0]['assemble'].name  >build.gradle & gradle
echo    println "project.extension "+project.extensions['android']['sourceSets']['s601']['res']['source'][0] >build.gradle & gradle


project [ant,gradle,startParameter,buildscript,extensions,repositories]
    //解析xml：XmlParser (dom)vs XmlSlurper(sax)
    //生成xml：MarkupBuilder和StreamingMarkupBuilder
```
### 概念
project 顶级（包含startParameter，gradle，ant，extension[]）
植入代码
``` bash
project.afterEvaluate {
}
assemble.doFirst
assemble.doLast
assemble dependencies Task

applay from:'xx.gradle'
ext.External=[
  funGenerate:{

  }
]
```
### 配置文件
1. build.gradle

2. 插件配置文件
resources\META-INF\gradle-plugins\xxx.properties

### 加速
gradlew build -X lint -X lintVitalRelease

jar作为本地仓库
implementation (name:'lib',ext:'aar')
换

configurations.maybeCreate("default")
artifacts.add("default",file('lib.aar'))

或
api(name: 'lib', ext: 'aar')

资源 依赖 资源

### android-gradle-plugin( /platform/tools/build/ 0.9以前;/platform/tools/base/build-system  0.9以后)
[编译](https://fucknmb.com/2017/06/01/Android-Gradle-Plugin%E6%BA%90%E7%A0%81%E9%98%85%E8%AF%BB%E4%B8%8E%E7%BC%96%E8%AF%91/)
Gradle Transform API 在2.x加入
#### 插件
1. 依赖 JavaBasePlugin（apply plugin: 'java-base'），关注 createAssembleTask()，
2. "android" Extension 关联 **AppExtension** ，productFlavors,buildTypes,signiingConfigs

#### tasks
```java
依赖java-base 的 task，查看源码classpath的plugin脚本
查看 android Gradle build 源码 ，java-base 依赖
      implementation gradleApi()
    implementation localGroovy()
     compileOnly 'com.android.tools.build:gradle:3.4.1'
查看 DefaultTask的继承关系，子类注释 @TaskAction @Input 是执行 
``` 
#### 源码
查询Gradle 版本 
```bash
Gradle
# git log --reverse --all --grep='Gradle\ 6\.0'

android gradle plugin

#git tag | grep -P 'gradle'
```
[android sdl /platform/tools/base/+/refs/tags/gradle_3.0.0/build-system](https://source.codeaurora.cn/quic/la/platform/tools/base/) 
 
[gradle.build(ant-javacompiler;ivy;maven-repo;groovy-asm-parseclass;jetbrains-kotlin-gradle-plugin;android-gradle-plugin ) dex2jar,jd-gui,apktool)](./知识体系-项目-efficiency-CI.md:)
#### Android-DSL (Android BUild Gradle)

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

bouncycastle 签名
#### 问题
```
Could not find com.android.tools.build:aapt2 AndroidStudio

Android: A problem occurred configuring project ':app'. > java.lang.NullPointerException (no error message) 
org.gradle.java.home=/Library/Java/JavaVirtualMachines/{your jdk}/Contents/Home


https://stackoverflow.com/questions/44185165/what-are-the-differences-between-gradle-assemble-and-gradle-build-tasks
使用framework 无法打包情况：
使用studio -> build -> build Apk(s)
或使用命令 gradlew.bat build --dry-run :app:assembleR605Debug ,跳过 lint 和 test


请注意，.iml文件名和路径根据Android Studio版本的不同而不同。
在Android Studio 4.0下：Project / app / app.iml（其中“ app”是您的项目名称）
Android Studio 4.0：Project / .idea / modules / app / app.iml（其中“ app”是您的项目名称）
Android Studio 4.1或更高版本：Project / .idea / modules / app / CustomFramework.app.iml（其中“ app”是项目名称，CustomFramework是根项目名称）

preBuild {
    doLast {
        def imlFile = file("..\\.idea\\modules\\"+project.name+"\\"+project.rootProject.name+"."+project.name+ ".iml")
        println 'Change ' + project.name + '.iml order'
        try {
            def parsedXml = (new XmlParser()).parse(imlFile)
            def jdkNode = parsedXml.component[1].orderEntry.find { it.'@type' == 'jdk' }
            parsedXml.component[1].remove(jdkNode)
            def sdkString = "Android API " + android.compileSdkVersion.substring("android-".length()) + " Platform"
            println 'what' + sdkString
            new Node(parsedXml.component[1], 'orderEntry', ['type': 'jdk', 'jdkName': sdkString, 'jdkType': 'Android SDK'])
            groovy.xml.XmlUtil.serialize(parsedXml, new FileOutputStream(imlFile))
        } catch (FileNotFoundException e) {
            println "no iml found"
        }
    }
}

```

```
清空本地缓存
gradlew build --refresh-dependencies  

```
[Beginning with Android Studio 3.2, AAPT2 moved to Google's Maven repository](https://developer.android.com/studio/releases)




#### Android gradle

```gradle
    gradle -q dependencies your-app-project:dependencies
```
[android gradl dsl com.android.tools](https://source.codeaurora.cn/quic/la/platform/prebuilts/gradle-plugin)

[android gradle-plugin relsease notes （插件版本	所需的 Gradle 版本）](https://developer.android.google.cn/studio/releases/gradle-plugin)
[gradle 版本](https://gradle.org/releases/)

Studio 依赖 gradle 最低版本和 android_gradle_build 版本范围 ，android_gradle_build 依赖 gradle 最低版本




android.viewBinding.enabled android.dataBinding.enabled 5.0将被移除，使用 android.buildFeatures.viewBinding android.buildFeatures.dataBinding 替换
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
### ⭐ 优化
gradle 依赖优化， 
//https://zwc365.com/2021/09/04/%E4%B8%87%E8%83%BD%E9%95%9C%E5%83%8F%E5%B7%A5%E5%85%B7
allprojects {
    repositories {
        maven { url 'https://pd.zwc365.com/cfworker/https://dl.google.com/dl/android/maven2/' }
        maven { url 'https://pd.zwc365.com/cfworker/https://jcenter.bintray.com' }
 
//        google()
//        jcenter()
    }
}
//1 不使用缓存，使用仓库中最新的包
    configurations.all {
        resolutionStrategy.cacheDynamicVersionsFor 0, 'seconds' // 动态版本 x.x.+
        resolutionStrategy.cacheChangingModulesFor 0, 'seconds'//  变化版本 x.x.x
    }

--refresh-dependencies
//2 使用exclude、force解决冲突
configurations.all {
  resolutionStrategy {
    force 'org.hamcrest:hamcrest-core:1.3'
  }
}
//3 maven android BasePlugin 默认task
apply plugin: 'maven'
android.uploadArchives {
      repositories.mavenDeployer {//gradle mavenDeployer
          repository(url: LOCAL_REPO_URL)
          pom.groupId = PROJ_GROUP
          pom.artifactId = PROJ_ARTIFACTID
          pom.version = PROJ_VERSION     
      }   
  }


//4 product 动态生成
android.productFlavors{
  register(['flavor'])
}

//5 版本
android.compileOptions {
    sourceCompatibility JavaVersion.VERSION_1_8
    targetCompatibility JavaVersion.VERSION_1_8
}
kotlinOptions {
    jvmTarget = JavaVersion.VERSION_1_8.toString()
}

//6 打包
android.packageOptions{
  //exclude、pickFirst、doNotStrip、merge
}
//7 重命名
android.applicationVariants.output.all {
    outputFileName ="the_file_name_that_i_want.apk"
}

//6 lint 异常
android.lintOptions{
  abortOnError false
}
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
