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