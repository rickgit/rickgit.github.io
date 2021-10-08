echo "sdk.dir=D\:\\Program\\Android\\sdk-ndk" > local.properties
mkdir 'src/main/'
echo '<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="edu.ptu.java.javaproj">
    <application
        android:allowBackup="true"
        android:supportsRtl="true">
    </application>

</manifest>' > 'src\\main\\AndroidManifest.xml'
echo 'buildscript {
    repositories {
        maven { url "https://plugins.gradle.org/m2" }
         google()
        jcenter()
    }
    dependencies {
      classpath "com.android.tools.build:gradle:4.1.3"
	   classpath "com.dorongold.plugins:task-tree:2.1.0"
    }
}
allprojects {
    repositories {
        maven {
            url "https://maven.aliyun.com/repository/public/"
        }
        maven {
            url "https://maven.aliyun.com/repository/spring/"
        }
        google()
        jcenter()
    }
}
apply plugin: "com.android.application"
apply plugin: com.dorongold.gradle.tasktree.TaskTreePlugin
android{
      compileSdkVersion 30
      buildToolsVersion "30.0.3"
}'> build.gradle
gradle build taskTree