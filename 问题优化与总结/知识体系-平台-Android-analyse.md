# 可分析
https://developer.android.google.cn/studio/debug/am-logcat?hl=el
https://developer.android.google.cn/topic/performance/vitals/launch-time?hl=zh-cn

## 界面调试

uetool/
布局分析 https://github.com/Grigory-Rylov/android-layout-inspector
投屏工具——qtscrcpy
## 调试工具


adb shell input keyevent   
```java
public class KeyEvent extends InputEvent implements Parcelable {
    public static final int KEYCODE_HOME            = 3;
    /** Key code constant: Back key. */
    public static final int KEYCODE_BACK            = 4; 

    /** Key code constant: Enter key. */
    public static final int KEYCODE_ENTER           = 66;
}
```
### hidden api
https://source.codeaurora.cn/quic/la/platform/prebuilts/runtime/tree/appcompat/veridex-linux.zip?h=LA.AU.0.1.0.r6
/platform/prebuilts/runtime/archive/master/appcompat.tar.gz
./appcompat.sh --dex-file=app-debug.apk --imprecise


## 加密

## 反编译
[dex 版本](https://source.android.google.cn/devices/tech/dalvik/dex-format)
```java
注意：Android 9.0 版本中新增了对 039 版格式的支持，其中引入了两个新字节码 const-method-handle 和 const-method-type。（字节码集合的总结表中介绍了这些字节码。）在 Android 10 中，版本 039 扩展了 DEX 文件格式，以包含仅适用于启动类路径上的 DEX 文件的隐藏 API 信息。

注意：Android 8.0 版本中新增了对 038 版格式的支持。038 版本中添加了新字节码（invoke-polymorphic 和 invoke-custom）和用于方法句柄的数据。

注意：Android 7.0 版本中新增了对 037 版格式的支持。在 037 版本之前，大多数 Android 版本都使用过 035 版格式。035 版与 037 版之间的唯一区别是，是否添加默认方法以及是否调整 invoke。

注意：至少有两种早期版本的格式已在广泛提供的公开软件版本中使用。例如，009 版本已用于 M3 版 Android 平台（2007 年 11 月至 12 月），013 版本已用于 M5 版 Android 平台（2008 年 2 月至 3 月）。在有些方面，这些早期版本的格式与本文档中所述的版本存在很大差异。
```
[java 二进制修改](https://github.com/Col-E/Recaf)


[⭐jadx 支持dex 039](https://github.com/skylot/jadx.git)
[jd-gui](https://github.com/java-decompiler/jd-gui)
[Apktool](https://github.com/iBotPeaches/Apktool)
[dex2jar 废弃 支持 dex 035,036; enjarify 支持35 ](https://github.com/pxb1988/dex2jar)
[jad（不维护）](http://www.kpdus.com/jad.html)
[javaparser,Java 1.0 - Java 15 Parser](https://github.com/javaparser/javaparser)

jeb 调试反编译app


## 日活量大的应用

### tiktop

#### 2020年6月17日
```xml
<manifest
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:versionCode="110501"
    android:versionName="11.5.0"
    android:compileSdkVersion="28"
    android:compileSdkVersionCodename="9"
    package="com.ss.android.ugc.aweme"
    platformBuildVersionCode="28"
    platformBuildVersionName="9">

    <uses-sdk
        android:minSdkVersion="16"
        android:targetSdkVersion="26" />
 
    <application
        android:theme="@ref/0x7f0c000f"
        android:label="@ref/0x7f0d002f"
        android:icon="@ref/0x7f100002"
        android:name="com.ss.android.ugc.aweme.app.host.HostApplication"
        android:launchMode="3"
        android:alwaysRetainTaskState="true"
        android:allowBackup="false"
        android:largeHeap="true"
        android:supportsRtl="false"
        android:resizeableActivity="true"
        android:networkSecurityConfig="@ref/0x7f150003"
        android:appComponentFactory="android.support.v4.app.CoreComponentFactory">
        <activity
            android:theme="@ref/0x7f0c011a"
            android:name="com.ss.android.ugc.aweme.main.MainActivity"
            android:launchMode="1"
            android:screenOrientation="1"
            android:configChanges="0xdb0"
            android:windowSoftInputMode="0x12"
            android:hardwareAccelerated="true">

            <intent-filter>

                <action
                    android:name="android.intent.action.VIEW" />

                <category
                    android:name="android.intent.category.DEFAULT" />

                <data
                    android:scheme="taobao"
                    android:host="main.aweme.sdk.com" />
            </intent-filter>
        </activity>

        <activity-alias
            android:theme="@ref/0x7f0c011a"
            android:name="com.ss.android.ugc.aweme.splash.SplashActivity"
            android:screenOrientation="1"
            android:targetActivity="com.ss.android.ugc.aweme.main.MainActivity">

            <intent-filter>

                <action
                    android:name="android.intent.action.MAIN" />

                <category
                    android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity-alias>

```
[Robust](https://github.com/Meituan-Dianping/Robust)


## wechat
### 2020年6月17日
```xml
<manifest
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:versionCode="1680"
    android:versionName="7.0.15"
    android:installLocation="0"
    android:compileSdkVersion="28"
    android:compileSdkVersionCodename="9"
    package="com.tencent.mm"
    platformBuildVersionCode="28"
    platformBuildVersionName="9">

    <uses-sdk
        android:minSdkVersion="21"
        android:targetSdkVersion="28" />
    <application
        android:theme="@ref/0x7f110245"
        android:label="@ref/0x7f10032e"
        android:icon="@ref/0x7f0d0002"
        android:name="com.tencent.mm.app.Application"
        android:persistent="false"
        android:allowBackup="false"
        android:hardwareAccelerated="true"
        android:largeHeap="true"
        android:supportsRtl="false"
        android:usesCleartextTraffic="true"
        android:networkSecurityConfig="@ref/0x7f13004f"
        android:appComponentFactory="android.support.v4.app.CoreComponentFactory">
        <activity
            android:theme="@ref/0x7f11024d"
            android:label="@ref/0x7f101624"
            android:name="com.tencent.mm.ui.LauncherUI"
            android:launchMode="1"
            android:configChanges="0xda0"
            android:windowSoftInputMode="0x32">

            <intent-filter>

                <action
                    android:name="android.intent.action.MAIN" />

                <category
                    android:name="android.intent.category.LAUNCHER" />

                <category
                    android:name="android.intent.category.MULTIWINDOW_LAUNCHER" />
            </intent-filter>

            <intent-filter>

                <action
                    android:name="com.tencent.mm.action.BIZSHORTCUT" />

                <category
                    android:name="android.intent.category.DEFAULT" />
            </intent-filter>
        </activity>

```
[tinker](https://github.com/Tencent/tinker)



[Systrace](https://www.androidperformance.com/2019/12/21/Android-Systrace-CPU/)
[耗时方法统计](https://github.com/zhengcx/MethodTraceMan)