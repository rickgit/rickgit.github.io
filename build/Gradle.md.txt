#Android常见Gradle分析

> http://www.vogella.com/tutorials/Gradle/article.html

> https://docs.gradle.org/current/userguide/userguide.html
> https://github.com/davenkin/gradle-learning

@(源码分析)[HashMap|LinkedHashMap|Android]

本文对比下HashMap,LinkedHashMap,TreeMap;涉及*数组链表结构*，*双向链表*，*双重链表*,*排序模式*，*AVL结构*，*红黑树*和*Hash冲突*

- **文件的入口**
- **存储键值对**
- **获取键值对**
- **移除键值对**
- **清理容器**

---------------------

[TOC]

##文件的入口
```bat
gradle-2.10\bin\gradle.bat

:execute
@rem Setup the command line

set CLASSPATH=%APP_HOME%\lib\gradle-launcher-2.10.jar

@rem Execute Gradle
"%JAVA_EXE%" %DEFAULT_JVM_OPTS% %JAVA_OPTS% %GRADLE_OPTS% "-Dorg.gradle.appname=%APP_BASE_NAME%" -classpath "%CLASSPATH%" org.gradle.launcher.GradleMain %CMD_LINE_ARGS%

//使用echo命令，输出前面的命令执行内容
 "C:\Program Files\Java\jdk1.7.0_79/bin/java.exe"    "-Dorg.gradle.appname=gradle" -classpath "C:\Users\Haibin\Downloads\anshu\software\Android\android-studio-2.2\gradle\gradle-2.10\bin\..\lib\gradle-launcher-2.10.jar" org.gradle.launcher.GradleMain :Main:assembleRelease


```
##Android项目的gradle wrapper
```bat
:execute
@rem Setup the command line

set CLASSPATH=%APP_HOME%\gradle\wrapper\gradle-wrapper.jar

@rem Execute Gradle
"%JAVA_EXE%" %DEFAULT_JVM_OPTS% %JAVA_OPTS% %GRADLE_OPTS% "-Dorg.gradle.appname=%APP_BASE_NAME%" -classpath "%CLASSPATH%" org.gradle.wrapper.GradleWrapperMain %CMD_LINE_ARGS%

```
使用命令行打印出gradle wrapper 最后的的命令结果如下
```shell
"C:\Program Files\Java\jdk1.7.0_79/bin/java.exe"    "-Dorg.gradle.appname=g
radlew" -classpath "C:\Users\Haibin\Downloads\anshu\workspace\ws-ticket-plugin\Ticket\\gradle\wrapper\gradle-wrapper.jar" org.gradle.wrapper.GradleWrapperMain :Main:assembleRelease

```


可以看到程序的入口是*gradle-launcher-2.10.jar*文件中的*org.gradle.launcher.GradleMain*类。阅读源码就从这个类为突破口进行调试分析。



##存储键值对

##移除键值对

##获取键值对

##清理容器