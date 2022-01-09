echo "sdk.dir=D\\:\\\\Program\\\\Android\\\\sdk-ndk" > local.properties
echo '# https://developer.android.com/topic/libraries/support-library/androidx-rn
android.useAndroidX=true
# Automatically convert third-party libraries to use AndroidX
android.enableJetifier=true' > gradle.properties
echo 'buildscript {
    repositories {
//        mavenCentral	https://maven.aliyun.com/repository/central
//        jcenter	https://maven.aliyun.com/repository/public
//        public	https://maven.aliyun.com/repository/public
//        google	https://maven.aliyun.com/repository/google
//        gradle-plugin	https://maven.aliyun.com/repository/gradle-plugin
//        spring	https://maven.aliyun.com/repository/spring
//        spring-plugin	https://maven.aliyun.com/repository/spring-plugin
//        grails-core	https://maven.aliyun.com/repository/grails-core
//        apache snapshots	https://maven.aliyun.com/repository/apache-snapshots
        maven { url "https://maven.aliyun.com/repository/public" }
        maven { url "https://maven.aliyun.com/repository/central" }
        //maven { url "http://maven.aliyun.com/nexus/content/repositories/google" }
        //maven { url "http://maven.aliyun.com/nexus/content/repositories/gradle-plugin" }
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
//        mavenCentral	https://maven.aliyun.com/repository/central
//        jcenter	https://maven.aliyun.com/repository/public
//        public	https://maven.aliyun.com/repository/public
//        google	https://maven.aliyun.com/repository/google
//        gradle-plugin	https://maven.aliyun.com/repository/gradle-plugin
//        spring	https://maven.aliyun.com/repository/spring
//        spring-plugin	https://maven.aliyun.com/repository/spring-plugin
//        grails-core	https://maven.aliyun.com/repository/grails-core
//        apache snapshots	https://maven.aliyun.com/repository/apache-snapshots
        maven { url "https://maven.aliyun.com/repository/public" }
        maven { url "https://maven.aliyun.com/repository/central" }
        //maven { url "http://maven.aliyun.com/nexus/content/repositories/google" }
        //maven { url "http://maven.aliyun.com/nexus/content/repositories/gradle-plugin" }
        google()
        jcenter()
    }
}

task clean(type: Delete) {
    delete rootProject.buildDir
}

'> build.gradle



mkdir  -p 'app/src/main/'

echo 'apply plugin: "com.android.application"
android{
      compileSdkVersion 30
      buildToolsVersion "30.0.3"
      defaultConfig {
            applicationId "edu.ptu.java.javaproj"
            minSdkVersion 16
            targetSdkVersion 30
            versionCode 1
            versionName "1.0"
            flavorDimensions "default"
            testInstrumentationRunner "androidx.test.runner.AndroidJUnitRunner"
      }
      dependencies {
        implementation "androidx.appcompat:appcompat:1.1.0"
        implementation "com.google.android.material:material:1.1.0"
        implementation "androidx.constraintlayout:constraintlayout:1.1.3"
      }
}
'> app/build.gradle
mkdir  -p 'app/src/main/java/edu/ptu/java/javaproj'
echo 'package edu.ptu.java.javaproj;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new TextView(this));
    }
}'> 'app/src/main/java/edu/ptu/java/javaproj/MainActivity.java'
echo '<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="edu.ptu.java.javaproj">
    <application
        android:allowBackup="true"
        android:theme="@style/Theme.MaterialComponents.DayNight.DarkActionBar"
        android:supportsRtl="true">
        <activity android:name=".MainActivity">
            <intent-filter>
                <action android:name="android.intent.action.MAIN"/>
                <category android:name="android.intent.category.LAUNCHER"/>
            </intent-filter>
        </activity>
    </application>

</manifest>' >  'app/src/main/AndroidManifest.xml'



mkdir -p 'lib/src/main/java'
echo 'plugins {
    id "java-library"
}
dependencies {
    implementation("com.android.tools.build:gradle:4.1.3")
}
java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}
' > 'lib/build.gradle'


echo 'include ":app"
include ":lib"
' > settings.gradle

gradle wrapper
gradle :app:dependencies