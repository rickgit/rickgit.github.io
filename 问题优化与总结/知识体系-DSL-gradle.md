# Gradle

Gradle provides a Domain Specific Language (DSL), for describing builds. 

Gradle models its builds as Directed Acyclic Graphs (DAGs) of tasks (units of work). 


```
 +--------------------------------------------+ 
 |   project,Task                             |
 +---------------------------------------------+
 +--------------------------------------------+ 
 |   gradleshell(parse gradle file)     Dag   |
 +---------------------------------------------+
 |                 build                     |
 +---------------------------------------------+
 |                 Gradle                     |
 +--------------------------------------------+


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