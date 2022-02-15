echo "sdk.dir=D\\:\\\\Program\\\\Android\\\\sdk-ndk

" > local.properties
echo 'org.gradle.jvmargs=-Xmx2048m -Dfile.encoding=UTF-8
android.useAndroidX=true
android.enableJetifier=true
kotlin.code.style=official' > gradle.properties
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
      classpath "com.android.tools.build:gradle:7.1.1"
      classpath "org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.10"
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
        maven {
            url "file://./file/repos"
        }
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

        externalNativeBuild {
            cmake {
                cppFlags ""
            }
        }
    }
    externalNativeBuild {
        cmake {
            path "src/main/cpp/CMakeLists.txt"
            version "3.10.2"
        }
    }
    ndkPath "D:/Program/Android/sdk-ndk/ndk-bundle"
    ndkVersion "23.1.7779620"
}
dependencies {
  implementation "androidx.appcompat:appcompat:1.1.0"
  implementation "com.google.android.material:material:1.1.0"
  implementation "androidx.constraintlayout:constraintlayout:1.1.3"
}
'> app/build.gradle
mkdir  -p 'app/src/main/java/edu/ptu/java/javaproj'
echo 'package edu.ptu.java.javaproj;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
public class MainActivity extends AppCompatActivity {
    static {
        System.loadLibrary("native-lib");
    }
    public native String stringFromJNI();
    public native String stringFromDynamicJNI();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView view = new TextView(this);
        view.setText(stringFromJNI()+" "+stringFromDynamicJNI());
        setContentView(view);
    }
}
'> 'app/src/main/java/edu/ptu/java/javaproj/MainActivity.java'
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

mkdir  -p 'app/src/main/cpp'
echo 'cmake_minimum_required(VERSION 3.10.2)
project("myapplication")
add_library( native-lib
             SHARED
             native-lib.cpp )
find_library( log-lib
              log )
target_link_libraries( native-lib
                       ${log-lib} )' > 'app/src/main/cpp/CMAKELists.txt'

echo '#include <jni.h>
#include <string>

extern "C"
JNIEXPORT jstring JNICALL
Java_edu_ptu_java_javaproj_MainActivity_stringFromJNI(JNIEnv *env, jobject thiz) {
    std::string hello = "Hello from auto gen C++";
    return env->NewStringUTF(hello.c_str());
}

extern "C"
JNIEXPORT jstring JNICALL stringFromDynamicJNI(JNIEnv *env, jobject thiz) {
    return env->NewStringUTF("Hello from dynamic C++");
}

#include <android/log.h>
JNIEXPORT jint JNICALL
JNI_OnLoad(JavaVM *vm, void *reserved) {
    JNIEnv *env = NULL;
    jint result = -1;
    //打印日志，说明已经进来了
    __android_log_print(ANDROID_LOG_DEBUG,"JNITag","enter jni_onload");
    if (vm->GetEnv((void **) &env, JNI_VERSION_1_6) != JNI_OK) {
        return result;
    }

    //动态注册
    const char *CLASS_NAME = "edu/ptu/java/javaproj/MainActivity";//类名
    jclass clazz = env->FindClass(CLASS_NAME);
    if (clazz == NULL) {
        return false;
    }
    JNINativeMethod g_Methods[] = {  {//本地方法描述
            "stringFromDynamicJNI",//Java方法名
            "()Ljava/lang/String;",//方法签名
            (void *) stringFromDynamicJNI //绑定本地函数
    }};
    int ret=env->RegisterNatives(clazz,g_Methods,sizeof(g_Methods) / sizeof(g_Methods[0]));
    if (ret != JNI_OK) {
        __android_log_print(ANDROID_LOG_DEBUG,"JNITag","Register Error");
        return -1;
    }
    // 返回jni的版本
    return JNI_VERSION_1_6;
}' > 'app/src/main/cpp/native-lib.cpp'


mkdir  -p 'KotlinAarLib/src/main/java'
echo 'plugins {
    id "com.android.library"
    id "org.jetbrains.kotlin.android"
}

android {
    compileSdkVersion 31
    defaultConfig {
        minSdkVersion 22
        targetSdkVersion 31
        testInstrumentationRunner "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles "consumer-rules.pro"
    }

    buildTypes {
        release {
            minifyEnabled false
            //proguardFiles getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
        }
    }
    compileOptions {
        sourceCompatibility JavaVersion.VERSION_1_8
        targetCompatibility JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}
configurations.all {
    resolutionStrategy {
        force "androidx.core:core:1.6.0"
    }
}
dependencies {
    implementation "androidx.core:core-ktx:1.7.0"
    implementation "androidx.appcompat:appcompat:1.3.0"
    implementation "com.google.android.material:material:1.4.0"
}
'> 'KotlinAarLib/build.gradle'
echo '<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="edu.ptu.java.kotlinaarlib">
</manifest>' > 'KotlinAarLib/src/main/AndroidManifest.xml'

mkdir -p 'lib/src/main/java'
echo 'plugins {
    id "java-library"
    id "maven-publish"
}
dependencies {
    implementation("com.android.tools.build:gradle:7.1.1")
}
java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}
// uploadArchivesToLocal {
//     repositories {
//         mavenDeployer {
//             //提交到远程服务器：
//             // repository(url: "http://www.xxx.com/repos") {
//             //    authentication(userName: "admin", password: "admin")
//             // }
//             //本地的Maven地址设置为D:/repos
//             pom.groupId = "edu.ptu.java.gradleplugin"
//             pom.artifactId = "appt2-fix"
//             pom.version = "1.0.0"
//             repository(url: uri("../file/repos"))
//         }
//     }
// }

publishing  {
    repositories {
        maven {
            url = file("../file/repos")
        }
    }
    publications {
        mavenJava(MavenPublication) {
            groupId = "edu.ptu.java.gradleplugin"
            artifactId = "appt2-fix"
            version = "1.0.0"
        }
    }
}
' > 'lib/build.gradle'


mkdir -p 'kotlinLib/src/main/java'
echo 'plugins {
    id "java-library"
    id "org.jetbrains.kotlin.jvm"
}
dependencies {
    implementation("com.android.tools.build:gradle:7.1.1")
}
java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}
' > 'kotlinLib/build.gradle'

echo 'include ":app"
include ":lib"
include ":kotlinLib"
' > settings.gradle

gradle wrapper
gradle :app:dependencies