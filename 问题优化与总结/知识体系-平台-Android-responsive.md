## 总览
[](https://juejin.im/post/6844903974294781965#heading-61)


## 界面
### 四大组件基础 - Context
Context作用
```
 ContextWrapper
       ^   Context
       |
       +
 ContextThemeWrapper
       ^   Theme,LayoutInflater,Resources,Configuration
       |
 Activity
       ^   Dialog,Cursor,Context,生命周期参数的NonConfigurationInstances
       |
ComponentActivity
       ^     LifecycleRegistry,ViewModelStore,SavedStateRegistryController
       |     OnBackPressedDispatcher,mContentLayoutId
 FragmentActivity
       ^     FragmentController,LifecycleRegistry
       |
AppCompatActivity
             ActionBar


+--------------------------------------------------------------------------------------------+
|   ContextImpl                                                                              |
+-----------------------------------------------------+--------------------------------------+
|     ActivityThread mMainThread                      |  File mDatabasesDir                  |
|                                                     |  File mPreferencesDir                |
|     LoadedApk mPackageInfo                          |  File mFilesDir                      |
|     Resources mResources                            |  File mNoBackupFilesDir              |
|     ResourcesManager mResourcesManager              |  File mCacheDir                      |
|     PackageManager mPackageManager                  |  File mCodeCacheDir                  |
|                                                     |                                      |
+-----------------------------------------------------+                                      |
|       Display mDisplay                              |  ArrayMap<String, File>              |
|       Theme mTheme                                  |   mSharedPrefsPaths                  | 
+-----------------------------------------------------+--------------------------------------+
|       Object[] mServiceCache                                                               |
|       ApplicationContentResolver mContentResolver                                          |
|       IBinder mActivityToken                                                               |
|       UserHandle mUser                                                                     |
+--------------------------------------------------------------------------------------------+
+--------------------------------------------------------------------------------------------+
|   Activity                                                                                 |
+-----------------------------------------------------+--------------------------------------+
|  ActivityThread mMainThread       Thread mUiThread  |  boolean mCalled                     |
|  Activity mParent                 Intent mIntent    |  boolean mResumed                    |
|  ComponentName mComponent         mResultData:Intent|  boolean mStopped                    |
|  Application mApplication         String mReferrer  |  boolean mFinished;                  |
|  mFragments:FragmentController                      |  boolean mStartedActivity            |
|  Instrumentation mInstrumentation mHandler:Handler  |  boolean mDestroyed;                 |
|  IBinder mToken                                     |  boolean mDoReportFullyDrawn         |
|  TaskDescription mTaskDescription                   |  boolean mRestoredFromBundle         |
|                                                     |                                      |
|  Window mWindow                                                                            |
|  WindowManager mWindowManager                       |  boolean mWindowAdded                |
|  Configuration mCurrentConfig                       |  boolean mVisibleFromServer          |
|  mLastNonConfigurationInstances                     |  boolean mVisibleFromClient          |
|                                                     |  boolean mCanEnterPictureInPicture   |
|  mDecor:View                                        |  boolean mEnableDefaultActionBarUp   |
|  mActionBar:ActionBar          CharSequence mTitle  |                                      |
|  mMenuInflater                                      |                                      |
|  mSearchManager                   mSearchEvent      |                                      |
|  mAutofillPopupWindow             mAutofillManager  |                                      |
|  SparseArray<ManagedDialog>  mManagedDialogs        |                                      |
|                                                     |                                      |
+-----------------------------------------------------+--------------------------------------+
|                                                                                            |
|  ArrayList<ManagedCursor> mManagedCursors                                                  |
|  TranslucentConversionListener mTranslucentCallback  SpannableStringBuilder mDefaultKeySsb |
|  VoiceInteractor mVoiceInteractor                                                          |
|                                                                                            |
+--------------------------------------------------------------------------------------------+

                                  PKMS
                                +------------------->  Permission/Pkginfo/HindAPI 
                                |                     
                                |  AMS                 Activity
                                |                      BrocastReceiver
                                |------------------->  ContentProvider
                  SystemServer  |                      Service
+------------------+            |
|                  | +----------+   WMS
|     context      |            +------------------->  Activity/Dialog/PopupWindow/Toast  ---->SurfaceFlipper
|                  |
+------------------+ +----------+
                                |
                ActivityThread  |   AssetManager
                                +------------------->  loadResource
```


## 4S (smooth，Stability，saving，streamline)快稳省简
稳定（编码/io问题），精简/包大小，续航/CPU占用率，流程/内存占用率-刷新率，
> <<Android High Performance Programming>>

[Android性能测试（内存、cpu、fps、流量、GPU、电量）——adb篇](https://www.jianshu.com/p/6c0cfc25b038)
稳定（monkey,bugreport），流畅（systrace，卡顿，动画，多线程，zxing），续航（battery historian 后台，发热，功耗），精简（apk），美观（布局layout inspector），安全（加固，插件化）
am_crash
 
性能（ the time taken to execute tasks）
 

## 精简
[参考](./知识体系-平台-Android-latency.md)




## 稳定
Crash 和 ANR

android develop monkey
AndroidJunitRunner


### 代码Review：提高代码质量
Commit 审阅 if，系统版本，模块管理
Push   代码重用,多次提交Review
### Android Lint、QAPlugins（Findbugs、Checkstyle、PMD）
### 日志 Timer
### MONKEY
1. monkey tools 测试
adb shell monkey -p com.bla.yourpackage -v 1000
adb -s 127.0.0.1:7555 shell monkey -p com.example.proj -s 1574490540 --hprof --throttle 200 -v -v -v 90000000 -pct-touch 60% --pct-motion 20% --pct-anyevent 20% --ignore-security-exceptions --kill-process-after-error --monitor-native-crashes >logs/20191123/142900/monkey.txt


adb -s 127.0.0.1:7555 shell monkey -p com.example.proj -s 1574490540 --hprof --throttle 200 -v -v -v 90000000 -pct-touch 60% --pct-motion 20% --pct-anyevent 20% --pct-nav 0% --pct-majornav 0% --ignore-security-exceptions --kill-process-after-error --monitor-native-crashes >logs/20191123/142900/monkey.txt

heisha:
adb -s 127.0.0.1:7555 shell monkey -p com.example.proj -s 9455 --throttle 300 -v -v -v 300000 --pct-appswitch  0 --ignore-security-exceptions --ignore-crashes --ignore-timeouts  --monitor-native-crashes

[--pkg-whitelist-file, /sdcard/systemwhitelist.txt, --ignore-crashes, --ignore-timeouts, --ignore-security-exceptions, --ignore-native-crashes, --monitor-native-crashes, --throttle, 500, -v, -v, -v, -s, 800, 570000]
[-p, com.example.proj, -s, 9455, --throttle, 300, --ignore-security-exceptions, --pct-appswitch, 0, --ignore-crashes, --ignore-timeouts, --ignore-native-crashes, -v, -v, -v, 300000]


停止 monkey
adb shell ps | awk '/com\.android\.commands\.monkey/ { system("adb shell kill " $2) }'

基础参数 | 事件参数 | 调试参数
|------:|---------:|---------:|
-v |     -pct-touch| -hprof   |
-s |            ...|--ignore-security-exceptions|
-p |               |       ...|
--throttle|  |  |

```java
    public static final int FACTOR_TOUCH        = 0;//点击
    public static final int FACTOR_MOTION       = 1;//滑动
    public static final int FACTOR_TRACKBALL    = 2;//滚动
    public static final int FACTOR_NAV          = 3;
    public static final int FACTOR_MAJORNAV     = 4;//back home menu
    public static final int FACTOR_SYSOPS       = 5;//物理按键
    public static final int FACTOR_APPSWITCH    = 6;//startActivity
    public static final int FACTOR_ANYTHING     = 7;
```


```
monkey network
adb forward tcp:1080 tcp:1080
adb shell monkey --port 1080
telnet 127.0.0.1 1080

```


2. 使用 adb 获取错误报告
adb bugreport E:/bugs/
3. anr文件
adb pull /data/anr/anr_2019-11-21-11-41-10-537 e:/bugs/

4. 日志
- ANR **(// NOT RESPONDING: )**,CRASH **(// CRASH: )**
- EXCEPTION,NullPointerException
- ERROR

[ChkBugReport日志报告](https://github.com/sonyxperiadev/ChkBugReport.git)
[ChkBugReport下载地址](https://github.com/sonyxperiadev/ChkBugReport/wiki/Where-to-obtain-it)

### 应用稳定性（Stability：how many failures an application exhibits）-异常及严苛模式
```
services/core/java/com/android/server/am/AppErrors.java:

StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectCustomSlowCalls() //API等级11，使用StrictMode.noteSlowCode
//                    .detectDiskReads()
//                    .detectDiskWrites()
                    .detectNetwork()   // or .detectAll() for all detectable problems
                    .penaltyDialog() //弹出违规提示对话框
                    .penaltyLog() //在Logcat 中打印违规异常信息
                    .penaltyFlashScreen() //API等级11
                    .build());
StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
        .detectLeakedSqlLiteObjects()
        .detectLeakedClosableObjects() //API等级11
        .penaltyLog()
        .detectFileUriExposure()
        .penaltyDeath()
        .build());
```
### （异常 错误 安全）反编译
[dex2jar](https://github.com/pxb1988/dex2jar)
[jd-gui](https://github.com/java-decompiler/jd-gui)
[Apktool](https://github.com/iBotPeaches/Apktool)
[jad（不维护）](http://www.kpdus.com/jad.html)

[ Crash防护](https://www.jianshu.com/p/01b69d91a3a8)
try{Looper.loop()}


### 可维护性/通讯 - 架构之模块化（插件化及组件化）


[详见《知识体系-平台-Android-cohesion.md》](./知识体系-平台-Android-cohesion.md)
插件化（反射；接口；HOOK IActivityManager/Instrumentation+动态代理）
Activity校验，生命周期，Service优先级，资源访问，so插件化
- Dynamic-loader-apk
  [非开放sdk api](https://blog.csdn.net/yun_simon/article/details/81985331)
- Replugin

组件化
- 组件间解耦
  1. MVVM-AAC 
  Android Jetpack(Foundation Architecture Behavior UI  ) ViewModel LiveData
  2. MVP DI框架Dagger2解耦
- 通信
1. 对象持有
2. 接口持有
3. 路由 （ARouter）
   Dagger2 依赖注入控制反转，Dagger 2 是 Java 和 Android 下的一个完全静态、编译时生成代码的依赖注入框架


### 版本特性
[Android api level](https://developer.android.google.cn/guide/topics/manifest/uses-sdk-element?hl=zh-cn#top_of_page)

#### （Android 11）
[行为变更：以 Android 11 为目标平台的应用](https://developer.android.google.cn/preview/behavior-changes-11?hl=zh-cn)

- [Android 11 中的隐私权（存储文件和用户数据、请求权限以及请求位置信息）](https://developer.android.google.cn/preview/privacy?hl=zh-cn)

#### Android 10 API level 29
[面向开发者的 Android 10](https://developer.android.google.cn/about/versions/10/highlights?hl=zh-cn#privacy_for_users)
- 折叠屏（resizeableActivity）
- 用户隐私设置：必须使用 MediaStore 来访问共享媒体文件；阻止设备跟踪 （OAID替换）
 
 
#### Android  9 API level 28
[行为变更：以 API 级别 28 及更高级别为目标的应用](https://developer.android.google.cn/about/versions/pie/android-9.0-changes-28?hl=zh-cn)
- 支持最新的全面屏，其中包含为摄像头和扬声器预留空间的屏幕缺口。 通过 DisplayCutout 
- ImageDecoder 类，可提供现代化的图像解码方法。 使用该类取代 BitmapFactory 和 BitmapFactory.Options
- AnimatedImageDrawable类来绘制和显示GIF和WebP动画图像
- AMS：后台服务JobScheduler；后台进程不允许startService前台服务（IllegalStateException）；FLAG_ACTIVITY_NEW_TASK，才允许在非Activity场景启动Activity；
- webview多进程需要设置setDataDirectorySuffix


#### Android  8 API level 26
[Android 8.0 变更](https://developer.android.google.cn/about/versions/oreo/android-8.0-changes?hl=zh-cn#back-all)

- 后台执行限制
- Notification Channels 创建一个用户可自定义的频道。
- 画中画

[Android 8.0（API 级别 26）及更高版本中，位图像素数据存储在原生堆中](https://developer.android.google.cn/topic/performance/graphics/manage-memory.html#save-a-bitmap-for-later-use)
 26 
@FastNative注解

- Safe Browsing API 的 WebView 实现
 ```xml
 <application>
            ...
            <meta-data android:name="android.webkit.WebView.EnableSafeBrowsing"
                       android:value="true" />
</application>
 ```

 ```java
superSafeWebView.startSafeBrowsing(this, new ValueCallback<Boolean>() {
            @Override
            public void onReceiveValue(Boolean success) {
                safeBrowsingIsInitialized = true;
                if (!success) {
                    Log.e("MY_APP_TAG", "Unable to initialize Safe Browsing!");
                }
            }
        });
 ```
#### Android  7 API Level 24
[Android 7.0 行为变更](https://developer.android.google.cn/about/versions/nougat/android-7.0-changes?hl=zh-cn)
- 低耗电
- 夜间模式

#### Android  6 API level 23
[Android 6.0 变更](https://developer.android.google.cn/about/versions/marshmallow/android-6.0-changes?hl=zh-cn)

- [Android 临时访问权限](https://www.jianshu.com/p/f15f956763c1)

深层链接和 Android 应用链接
#### Android  5 API level 21 
[Android Lollipop](https://developer.android.google.cn/about/versions/lollipop?hl=zh-cn)
- Material Design
Art正式替代Dalvik VM
#### Android  4 API level 14
[Android KitKat 4.4(api level 19)](https://developer.android.google.cn/about/versions/kitkat?hl=zh-cn)
VSYNC/Choreographer

## 流畅（每秒25帧，限时20毫秒内执行完）-界面开发系统
1. Activity，View，Window
   activity在attach时，创建PhoneWindow；onCreate创建DecorView；onResume后创建ViewRootImpl关联WindowManager

2. 绘制invalidate方法
   先申请绘画帧，然后再绘制；
3. 事件
   Activity dispatchTouchEvent；再传给ViewRootImpl的mView（DecorView）dispatchTouchEvent；当有接受事件时target；
   下次传递的时候，走ontouch，判断释放是longclick；或者是否是touch_up，click事件；

```
      +->  TextView  +-+-->   EditText
      |                |
      |                |
      |                +-->   Button    +------>  CompoundButton +--+>  CheckBox
      |                                                             |   RadioButton
View -+                                                             |
      |                                                             |   Switch
      |                                                             +>  ToggleButton
      |
      +->  ImageView  +---->  ImageButton +--->  VisibilityAwareImageButton  +->  FloatingActionButton
      |
      +->  SurfaceView +-+->  GLSurfaceView
      |    TextureView   |
      |                  +->  VideoView
      +->  Space
      |    ViewStub
      |
      +--> ViewGroup ++--->  AbsoluteLayout +->  WebView
                      |
                      +--->  LinearLayout +--+->  TableLayout
                      |                      |    AppBarLayout
                      |                      |
                      |                      |    TextInputLayout
                      |                      |    RadioGroup
                      |                      |    NumberPicker
                      |                      +->  ActionMenuView
                      |
                      +--->  RelativeLayout
                      +--->  GridLayout
                      |
                      +--->  FrameLayout +-+-->   ViewAnimator   +-+->  ViewFlipper
                      |                    |                       |
                      |                    |                       |
                      |                    |                       |
                      |                    |                       +--> ViewSwitcher +-+->  TextSwitcher
                      |                    |                                           |
                      |                    |      CardView                             +->  ImageSwitcher
                      |                    |
                      |                    |      ScrollView
                      |                    |      NestedScrollView
                      |                    |      HorizontalScrollView
                      |                    |
                      |                    |      ScrimInsetsFrameLayout +--> NavigationView
                      |                    |      BottomNavigationView
                      |                    |
                      |                    |      ActionBarContainer
                      |                    +-->   CollapsingToolbarLayout
                      |
                      |       ViewPager
                      |       PagerTitleStrip
                      |
                      |       RecyclerView
                      |       AdapterView  +--->  AbsListView  +------+-->   ListView  +--->  ExpandableListView
                      |                                               |
                      |       CoordinatorLayout                       +-->   GridView
                      |
                      |       Toolbar
                      |       DrawerLayout
                      |       SwipeRefreshLayout
                      |       SlidingPaneLayout
                      |       ConstraintLayout
                      |
                      +-->    FlowLayout

```
### 界面优化
优化LayoutInflater ：缓存避免重复解析（RecyclerView的四级缓存）；iReaderAndroid/X2C 生成Java代码，减少xml转化Java对象的解析时间；异步解析
优化DecorView onDraw/layout ：减少层级（使用merge、Viewstub标签优化）；使用默认背景减少绘制；Litho 异步layout/measure；开发者工具的绘制工具
flutter：不用解析xml，绘制图层
异步耗时业务代码：Handler，线程池，协程，动画

> 性能（ the time taken to execute tasks）

```shell 
#adb shell "getprop | grep heapgrowthlimit"
#adb shell "getprop|grep dalvik.vm.heapstartsize"
#adb shell "getprop|grep dalvik.vm.heapsize"

#adb shell getprop | grep product 获取手机型号

#adb shell "dumpsys meminfo -s <pakagename | pid>"
# while true;do adb shell procrank|grep <proc-keywords>; sleep 6;done


# adb shell "cat  /proc/cpuinfo" //查看cpu核心数
# adb shell "dumpsys cpuinfo | grep <package | pid>"
# adb shell "top -n 5 | grep <package | pid>" 

# adb shell "dumpsys gfxinfo com.example.proj" //GPU 帧数 fps


# adb shell "dumpsys batterystats < package | pid>" //电量采集
```
#### 官方定义
[识别与负载能力相关的卡顿](https://source.android.google.cn/devices/tech/debug/jank_capacity)
[识别与抖动相关的卡顿](https://source.android.google.cn/devices/tech/debug/jank_jitter)

systrace

离线获取systrace
adb shell "atrace -z -b 40000 gfx input view wm am camera hal res dalvik rs sched freq idle disk mmc -t 15 > /data/local/tmp/trace_output &"


```
+-------------+-------------------+----------------------+---------------------------+---------------+
|             |    info           |    tools             |  fix                      |  extension    |
+----------------------------------------------------------------------------------------------------+
|  memory     |                   |                      |                           |  keep alive   |
|             | gc time>frame rate|     profiler cpu     |  ResourceCanary           |               |
|             | leaks             |                      |  Bitmap&Activity          |               |
|             | Memory Churn      |    dumpsys meminfo   |  APKsize                  |    +----------+
+-----------------------------+---+-------------------------------------------------------+ network& |
|  battery    | wakelock time |                          |   Doze /standby           |    | secure   |
|             | gps time      |        battery-historian |   jobscheduler API        |    +----------+
|             | network time  |                          |AlarmManager、Syncs Adapter|+              |
+----------------------------------------------------------------------------------------------------+
|             |   vsync       |                          |                           |               |
|  draw       |               |                          |  canvas.quickreject()     |               |
|  (cpu)      |Refresh Rate/  +---------+----------------+                           |               |
|             |frame Rate     |         | gpu overdraw   |  canvas.cliprect()        | multiThread&  |
|             |               |on-device| gpu render     |  hierachy viewer          | mainThread    |
|             |               |  tools  | gup view update|                           |               |
|             | Activity      |         |                |                           | Webview       |
|             | LauchTime     +---------+----------------+                           |               |
|             | (am start)    |     profiler cpu/gpu     |                           |               |
|             |reportFullyDrawn|                         |                           |               |
+-------------+---------------+--------------------------+---------------------------+---------------+
|   secure    |    debug      |   apktool                |                           | drizzleDumper |  
|             |   https       |   smali                  |                           |               |
|             |               |   (reverse engineering)  |                           |   FDex2       |
+-------------+---------------+--------------------------+---------------------------+---------------+
+-------------+-----------------------+
|             network                 |
+-------------+-----------------------+
|   fast      |  compress data/charge |
+-------------------------------------+
| <60ms GOOD  | compress image        |
|             | serialize data        |
| <200ms OK   |                       |
|             | cache                 |
| other BAD   | jms                   |
+-------------+-----------------------+


[smali](https://blog.csdn.net/linchaolong/article/details/51146492)
```
[Android官网](https://developer.android.com/topic/performance/)
[RelativeLayout的性能损耗](https://zhuanlan.zhihu.com/p/52386900?utm_source=androidweekly.io&utm_medium=website)
[battery-history](https://www.cnblogs.com/jytian/p/5647798.html,https://yeasy.gitbooks.io/docker_practice/install/ubuntu.html)

[启动时间](https://developer.android.com/topic/performance/vitals/launch-time)

[Android 内存泄漏](https://android.jlelse.eu/9-ways-to-avoid-memory-leaks-in-android-b6d81648e35e)

[冷启动](开始记录跟踪数据的位置调用 Debug.startMethodTracing()，要停止跟踪的位置请调用 Debug.stopMethodTracing())
```
+-----------------------------------+
| memory leaks                      |
+-----------------------------------+
| unrelease                         |
+-----------------------------------+
| static Fields                     |
| (View,Context)                    |
|                                   |
| Inner Classes                     |
| That Reference                    |
| Outer Classes/LongLifecycle       |
| (Async Handler)                   |
| (WeakReference view)              |
|                                   |
| ThreadLocals                      |
|                                   |
| Unclosed Resources                |
|(unregisterReceiver)               |
|                                   |
+-----------------------------------+



```
 
>《Android开发艺术探索》
方法：布局，绘制，内存泄漏，响应速度，Listview及Bitmap，线程优化
- 渲染速度
  [https://developer.android.com/studio/profile/inspect-gpu-rendering](https://developer.android.com/studio/profile/inspect-gpu-rendering)
    1. GPU呈现模式(Swap Buffers,Command Issue,Draw,Sync&Upload,Measure&LayoutAnimation,Input Handling,Misc/Vsync Delay)
```
+--------------------+---------------------------------------------+
|  Swap Buffers      |  too much work on the GPU                   |
+------------------------------------------------------------------+
|  Command Issue     |  renderer issuing commands to OpenGL        |
|                    |  to draw and redraw display lists           |
+------------------------------------------------------------------+
|  Sync & Upload     |  take to upload bitmap information          |
|                    |  to the GPU                                 |
+------------------------------------------------------------------+
|  Draw              |   create and update the view's display lists|
+------------------------------------------------------------------+
|  Measure /         |  spent on onLayout and onMeasure callbacks  |
|  Layout            |  in the ^iew hierarchy                      |
+------------------------------------------------------------------+
|  Animation         |  evaluate all the animators that were       |
|                    |  running that frame                         |
+------------------------------------------------------------------+
|  Input Handling    |  spent executing code                       |
|                    |  inside of an input e^ent callback          |
+------------------------------------------------------------------+
|  Misc Time /       |  executing operations in                    |
|  VSync Delay       |  between two consecuti^e frames.            |
+--------------------+---------------------------------------------+

```
    2. 显示CPU使用情况(查看后台运行)，耗电
    3. 绘制优化(profiler) CPU/Memory/Network
控件边界图，绘制条状图
```
+---------+--------+-------------------+---------+--------+------+------+--------+--------+-------+--------+------+-------+--------+
|         |        | Wall clock time/  |         |        |      |      |        |        |       |        |      |       |        |
|         |        |  Thread time      |         |        |      |      |        |        |       |        |      |       |        |
| Call    | Flame  |---------+---------|         |  app   | image|zygote|        |        |       |        |      |       |        |
| Chart   | Chart  |Top down |Bottom up|         |--------+------+------|        |        |       |        |      |       |        |
|         |        |         |         |forece gc|      Dump javaHeap   |        |        |       |        |      |       |        |
+---------+--------+---------+------------------------------------------+        |        |       |        |      |       |        |
|             Threads                  | Image   | Zygote | app  |  JNI |        |        |       |        |      |       |        |
|                                      | Heap    | Heap   | Heap |  Heap| timing |        |       |        |      |       |        |
|                                      |         |        |      |      |        |        |       |        |      |       |        |
+---------+--------+---------+-------------------+--------+------+-----------------------------------------------------------------+
| sample  | Trac   | Sample  |  Trac   | Allocation                     |        |        |       |        |      |       |        |
| java    | java   | C/C++   |  System |  Java/Native/Graphic           |overview|Responce|Request|CallStak| CPU  |Network|Location|
| Method  | Method | Function|  Calls  |     /Stack/Code/other          |        |        |       |        |      |       |        |
+---------+--------+-------------------+--------------------------------+--------+--------+-------+--------+------+-------+--------+
|                                                               Activity Lifecyle                                                  |
+--------------------------------------+--------------------------------+----------------------------------+-----------------------+
|          recod CPU                   |          Memory                |               network            |        Energy         |
+--------------------------------------+--------------------------------+----------------------------------+-----------------------+


Flame chart:横轴不再表示时间轴，相反，它表示每个方法执行的相对时间

[Flame chart](https://blog.csdn.net/sinat_20059415/article/details/80584621)
```
    1. **Studio Inspect Code** Android-Lint-Performace 代码Review不必要不加载（include merge, viewstub），ConstaintLayout
    2. LayoutInspector 布局优化（include merge, viewstub）
    3. 动画硬件加速，勿滥用
    
    尽量用Drawable  
    1. ListView/RecycleView及Bitmap优化
    2. 线程优化 

 [动画大全](https://github.com/OCNYang/Android-Animation-Set)
### 图片闪动
滑动停止才进行显示图片


### 编舞者 Choreographer
#### 刷新->绘制 traversal
invalidate只会调onDraw方法且必须在UI线程中调用
      mPrivateFlags |= PFLAG_INVALIDATED;
postInvalidate只会调onDraw方法，可以再UI线程中回调
      mPrivateFlags |= PFLAG_INVALIDATED;
requestLayout会调onMeasure、onLayout和onDraw(特定条件下)方法
      mPrivateFlags |= PFLAG_FORCE_LAYOUT;
      mPrivateFlags |= PFLAG_INVALIDATED;

#####  刷新 （绘制，局部重绘）View#invalite
VSynch 垂直同步
Triple Buffer 三重缓存
Choreographer 编舞者
```
* 001_androi1     54b6cfa9a9e5b861a9930af873580d6dc20f773c Initial Contribution
                  +---------------------------------------------------------------------------------------------+
                  |               View                                                                          |
                  |                  invalidate()                     measure()       mBGDrawable:Drawable      |
                  |                                                   layout()        onDraw()                  |
                  |                                                                   dispatchDraw()            |
                  |                                                                   onDrawScrollBars()        |
                  |---------------------------------------------------------------------------------------------|
                  |               ViewRoot:Handler:: ViewParent                                                 |
                  |                  mDirty:Rect                                                                |
                  |                  invalidateChild()                                                          |
                  |                                                                                             |
                  |                  mTraversalScheduled:boolean                                                |
                  |                  mWillDrawSoon:bool                                                         |
                  |                  scheduleTraversals()                                                       |
                  |                                                                                             |
                  |                   mView:View                                                                |
                  |                  performTraversals()                                                        |
                  |                                                                                             |
                  |                   mSurface:Surface                                                          |
                  |                  draw( canvas:canvas)                                                       |
                  |                                                                                             |
                  |---------------------------------------------------------------------------------------------|
  aosp-new/master 8283939cecfb5538e21ffc7adb017a995f099012 Merge "Create new Carrier configuration for separate MMTEL/RCS features"
                  +--------------------------------------------------------------------------------------------+
                  |                View                                                                        |
                  |                  invalidate()          postInvalidate()       updateDisplayListIfDirty()   |
                  |                                                                                            |
                  +--------------------------------------------------------------------------------------------+
                  |   ViewGroup:ViewParent                                                                     |
                  |    in^alidateChild()                                                                       |
                  +--------------------------------------------------------------------------------------------+
                  |   ViewRootImpl:ViewParent   dispatchInvalidateDelayed() mView:View                         |
                  |    invalidateChild()                                    doTraversal()                      |
                  |   invalidateChildInParent()                                                                |
                  |    invalidateRectOnScreen()                             performTraversals()                |
                  |    scheduleTraversals()                                                                    |
                  |                                                         measureHierarchy() performlayout() |
                  +------------------------------------------------+        performMeasure()   performDraw()   |
                  |   Choreographer                                |                                           |
                  |     postCallback()                             +-------------------------------------------+
                  |     postCallbackDelayedInternal()                                                          |
                  |     scheduleFrameLocked()                                                                  |
                  |     scheduleVsyncLocked()                                                                  |
                  |     mDisplayEventReceiver:FrameDisplayEventReceiver                                        |
                  |     doFrame()                                                                              |
                  +--------------------------------------------------------------------------------------------+
                  |  FrameDisplayEventReceiver :Runnable        CallbackRecord                                 |
                  |       scheduleVsync()                             run()                                    |
                  |       dispatchVsync()                                                                      |
                  |       run()                                 TraversalRunnable:Runnable                     |
                  |                                                                                            |
                  +--------------------------------------------------------------------------------------------+


```
##@ 事件相应 InputManager.getInstance().injectInputEvent
```
         +-----------------------------------------------------------------------------------------+
         |  ImputManager                                                                           |
         |       injectInputEvent()                                                                |
         |                                                                                         |
         |       mPtr:long                                                                         |
         |       injectInputE^ent()                                                                |
         |       injectInputEventInternal()                                                        |
         +-------------------------------------------------------------+---------------------------+
         | [platform_frameworks_base\services\core\jni]                |                           |
         |                                                             |                           |
         |             com_android_server_input_InputManagerService.cpp|                           |
         |                       NativeInputManager                    |                           |
         |                          getInputManager()                  |                           |
         +-------------------------------------------------------------+                           |
         | [platform_frameworks_native\services\inputflinger]          |                           |
         |     InputManager.cpp                                        |                           |
         |       mReaderThread :InputReaderThread(mReader);            |                           |
         |       mDispatcherThread:InputDispatcherThread(mDispatcher); |                           |
         |                                                             |                           |
         |       mDispatcher:InputDispatcher                           |                           |
         |       mClassifier:InputClassifier                           |                           |
         |       mReader:InputReader                                   |                           |
         |                                                             |                           |
         |                                                             |                           |
         |     InputDispatcher                                         |                           |
         |                                                             |                           |
         |       injectInputEvent()                                    |                           |
         |       enqueueInboundEventLocked()                           |                           |
         |                                                             |                           |
         |       dispatchOnce()//thread loop                           |                           |
         |                                                             |                           |
         |       mInboundQueue:Queue<EventEntry>                       |                           |
         |       dispatchOnceInnerLocked()                             |                           |
         |                                                             |                           |
         |       dispatchMotionLocked()                                |                           |
         |                                                             |                           |
         |       findTouchedWindowTargetsLocked()//touchscreen         |     WMS                   |
         |       checkWindowReadyForMoreInputLocked()               +----->                        |
         |                                                             |                           |
         |       mLooper:Looper                                        |                           |
         |       handleRecei^eCallback()                               |     //epoll socket channel|
         +-----------------------------------------------------------------------------------------+
         +-------------------------------------------------------------+                           |
         |      ViewRootImpl                                                                       |
         |                                         mFirstPostImeInputStage          mView:DecorView|
         |        mInputEventReceiver                        :InputStage                           |
         |             :WindowInputEventReceiver                                                   |
         |                                                                                         |
         |      WindowInputEventReceiver:                                                          |
         |              InputEventReceiver                                                         |
         |         enqueueInputEvent()                                                             |
         |                                                                                         |
         |    ViewPostImeInputStage                InputStage                                      |
         |         onProcess(q:QueuedInputEvent )      deliver(QueuedInputEvent q)                 |
         |                                             onProcess(QueuedInputEvent q)               |
         |                                                                                         |
         |                                         ViewPostImeInputStage :InputStage               |
         |                                             processPointerEvent()                       |
         |                                                                                         |
         +-----------------------------------------------------------------------------------------+
         |     DecorView:ViewGroup                                                                 |
         |           dispatchPointerEvent()               superDispatchTouchEvent()                |
         |           dispatchTouchEvent()                 //super.dispatchTouchEvent               |
         |                                                                                         |
         |     WindowCallbackWrapper                                                               |
         |     Activity                                                                            |
         |     PhoneWindow                                                                         |
         |          superDispatchTouchEvent()                                                      |
         |                                                                                         |
         +-----------------------------------------------------------------------------------------+
         |    ViewGroup :View                                                                      |
         |       mFirstTouchTarget:TouchTarget//not DownEvent, null not dispatch                   |
         |       dispatchTouchEvent()                                                              |
         |       onInterceptTouchEvent()                                                           |
         |       dispatchTransformedTouchE^ent()                                                   |
         +-----------------------------------------------------------------------------------------+
         |    View                                                                                 |
         |     mListenerInfo:ListenerInfo                                                          |
         |     performClickInternal();                                                             |
         |                                                                                         |
         |    ListenerInfo                                                                         |
         |         mViewFlags:int//ENABLED_MASK                                                    |
         |         mOnClickListener;OnClickListener                                                |
         +-----------------------------------------------------------------------------------------+



```
#### 事件 
##### ViewDragHelper 滑动工具类和computeScroll()
移动控件方法： 
        1. layoutparam.margin 
        2. scrollx 
        3. canvas translate
ViewCompat.postInvalidateOnAnimation(Activity.this)
应用常见 DrawerLayout，SlidingPaneLayout

简单工厂 ViewDragHelper#create(ViewGroup, float,Callback)

桥接    滑动判断，滑动处理有ViewDragHelper处理；
静态代理  OverScroller

中介    子view滑动由ViewDrawHelper控制
观察者   Callback
模板方法 ViewDragHelper#shouldInterceptTouchEvent；ViewDragHelper#processTouchEvent
        ViewDragHelper#continueSettling
观察者   ViewDragHelper#smoothSlideViewTo


#### 动画
https://dribbble.com/
##### 插值器
策略模式    TimeInterpolator#getInterpolation
      AccelerateDecelerateInterpolator
      LinearInterpolator
      AccelerateInterpolator
      DecelerateInterpolator
      OvershootInterpolator
      AnticipateInterpolator
      AnticipateOvershootInterpolator
      BounceInterpolator
      CycleInterpolator


##### TWeen Animation
修改matrix，刷新界面，等待下一帧时候，绘制界面，getInvalidateRegion更新父控件

```
+---------------------------------------------------------------------------------------------+
|  [Android 4.4]                                                                              |
|          View                                                                               |
|            mCurrentAnimation:Animation                ViewGroup:View                        |
|                                                          dispatchDraw()                     |
|            startAnimation(animation:Animation)           drawChild()                        |
|            clearAnimation()                              getChildTransformation()           |
|            onDetachedFromWindow()                                                           |
|                                                                                             |
|            draw()                                                                           |
|            drawAnimation():bool                                                             |
+---------------------------------------------------------------------------------------------+
|         ViewRootImpl:Parent                                                                 |
|           invalidateChild()                                                                 |
|          invalidateChildInParent()                                                          |
|           scheduleTraversals()                                                              |
|                                                                                             |
+---------------------------------------------------------------------------------------------+
|   Animation                                                            AlphaAnimation       |
|       mInterpolator:Interpolator                                       RotateAnimation      |
|       getTransformation():bool                                         ScaleAnimation       |
|       applyTransformation(interpolatedTime:float ,t:Transformation)    TranslateAnimation   |
|                                                                                             |
+---------------------------------------------------------------------------------------------+
|    Interpolator              //normalizedTime=dx/duration         Transformation            |
|             getInterpolation(normalizedTime:float):float                 mMatrix:Matrix     |
|                                                                                             |
|     LinearInterpolator :BaseInterpolator                                                    |
|     AccelerateInterpolator                                        Matrix //3x3              |
|     BounceInterpolator                                                                      |
+---------------------------------------------------------------------------------------------+

```
##### 帧动画 AnimationDrawable/ <animation-list>
模板方法 Animatable#start()/Animatable#stop
观察者   View#scheduleDrawable
命令     AnimationDrawable#run
状态    android.graphics.drawable.Drawable#scheduleSelf/

其他 :
      AnimatedImageDrawable/<animated-image>
      AnimatedVectorDrawable/<animated-vector>
##### 属性动画（PropertyValuesHolder） 
1. ValueAnimator 只对属性进行过渡
2. ObjectAnimator 因为属性有其归属的对象，可以通过反射设置对象的属性值 PropertyValuesHolder#setupSetter；不用设置监听修改过渡值
3. ViewPropertyAnimator 观察ValueAnimation回调事件AnimatorEventListener#onAnimationUpdate，更新invalidateViewProperty属性值
   
属性动画可以通过 ValueAnimator#ofPropertyValuesHolder对多个属性同时过渡， ValueAnimator#getAnimatedValue(String) 进行取值；

简单工厂  Keyframe#ofObject(float, Object):Keyframe
          PropertyValuesHolder#ofObject()
          ValueAnimator#ofObject()//ObjectProperty 
          ValueAnimator#ofPropertyValuesHolder()
          AnimationHandler#getInstance ThreadLocal线程单例
单例    AnimationHandler
        ArgbEvaluator
适配器  TypeEvaluator#evaluate():Object//ObjectAnimation返回对象的动画属性的值

装饰者 
        Keyframe装饰动画插值Object，添加mFraction，mValueType， mInterpolator；
        PropertyValuesHolder装饰KeyframeSet，保存属性现在的值 mAnimatedValue
        ObjectAnimator装饰ValueAnimator，需要设置 Property所属的Object
中介者
        AnimationHandler 统一管理所有的申请动画帧，如果mAnimationCallbacks有数据，就不断的申请动画帧
类迭代器
        KeyframeSet#getValue
观察者 
      AnimatorUpdateListener

##### ViewPropertyAnimator
```
+---------------------------------------------------------------------------------------------+
|  [Android 4.4]                                                                              |
|          View                                               mTransformationInfo             |
|            animate()                                        mRenderNode                     |
|            mAnimator:ViewPropertyAnimator                   in^alidate()                    |
|                                                             invalidateViewProperty()        |
|                                                                                             |
+---------------------------------------------------------------------------------------------+
|    ViewPropertyAnimator                        AnimatorEventListener                        |
|          mView:View                                  : Animator.AnimatorListener            |
|          mAnimationStarter:Runnable                                                         |
|                                                                                             |
|          mRTBackend:ViewPropertyAnimatorRT                                                  |
|                                                   onAnimationUpdate(ValueAnimator animation)|
|          onAnimationUpdate()                                                                |
|          setValue()                                                                         |
+---------------------------------------------------------------------------------------------+
|    ValueAnimator                                                                            |
|         addAnimationCallback()                                                              |
|         doAnimationFrame()                                                                  |
|         animateValue()                                                                      |
|                                                                                             |
+---------------------------------------------------------------------------------------------+
|    AnimationHandler               PropertyValuesHolder                                      |
|                                         calculateValue()                                    |
+---------------------------------------------------------------------------------------------+


```
##### GIF
Glide播放多个gif文件卡

android-gif-drawable性能好

android-1.6_r1\external\giflib 系统源码利用
#### RecyclerView 缓存
缓存ViewHolder，刷新控件，滑动停止加载数据

```
+---------------------------------------------------------------------------------+
| [android 5.0]    RecyclerView                                                   |
|                       mRecycler:Recycler                                        |
|                       setAdapter()                                              |
|                       setAdapterInternal()                                      |
|                                                                                 |
|                       dispatchLayout()                                          |
|                                                                                 |
|  Adapter<ViewHolder>        ViewHolder            LayoutManager                 |
|      onCreateViewHolder()       itemView;View       mLayoutState:LayoutState    |
|      onBindViewHolder()                             onLayoutChildren()          |
|                                                     fill()                      |
|                                                     layoutChunk()               | 
+---------------------------------------------------------------------------------+
|                                                                                 |
|                                     LayoutState                                 |
|                                        next(recycler:Recycler )                 |
|                                    Recycler                                     |
|                                        getRecycledViewPool():RecycledViewPool   |
|                                        getViewForPosition() :View               |
|                                                                                 |
+---------------------------------------------------------------------------------+

```
### 可拓展性/异步/多线程（Scalability：the number of tasks a system can execute at the same time.）

```shell
# pidof  system_server
1956

# cat /proc/1956/limits

```
### 自定义解析 flutter
- 第一代 ionic(webview),PhoneGap，Cordova      H5                语言：js/css      环境：webview

- 第二代 RN（v8,Hermes/jscore）> weex(js,vue)  jit运行时编译     语言：JavaScript  环境：nodejs/v8 
   [RN](https://www.reactnative.cn/docs/components-and-apis)
- 第三代 Flutter（Skia）                                     语言：dart
#### Flutter 路由，UI
#### dart
https://dart.dev/samples

https://dart.dev/guides/language/language-tour


[内部实现](https://github.com/dart-lang/sdk/blob/master/sdk/lib/_internal/vm/lib/)


调试JIT，发布AOT

```
+-----------------------------------------------------------------------+
|                     Object                                            |
|                        hashCode :int                                  |
|                        runtimeType:Type                               |
|                                                                       |
+-----------------------------------------------------------------------+

+-----------------------------------------------------------------------+ 
|                     num                                               |
+-----------------------------------------------------------------------+
|   int                               double                            |
|    bitLength:int                                                      |
|    isEven: bool                      sign:int                         |
|    isOdd:bool                                                         |
|    sign:int                                                           |
|                                                                       |
+-----------------------------------------------------------------------+
+-----------------------------------------------------------------------+
|                          StringBuffer :::StringSink                   |
|                                                                       |
|                               _parts:List<String>?                    |
|                                                                       |
|                               length:int                              |
+-----------------------------------------------------------------------+
|                                                                       |
|                  String :::Comparable<String>, Pattern                |
|                     codeUnits:List<int>  //UTF-16                     |
|                     runes:Runes                                       |
|                     length:length                                     |
|                     hashCode:int                                      |
|                    _isOneByte :bool                                   |
+-----------------------------------------------------------------------+
|                                                                       |
|                    Runes : Iterable<int>                              |
|                       string: String                                  |
+-----------------------------------------------------------------------+

+-----------------------------------------------------------------------+
|                        _GrowableList                                  | 
|                          _grow()                        _shrink()     |
|                          _nextCapacity()                              |
|                          _allocateData() //odd number                 |
|                                                                       |
|         _List            Lists                                        |
|          length:int         copy()                                    |
|                                                                       |
+-----------------------------------------------------------------------+ 


+-----------------------------------------------------------------------+
|                                                                       |
|            _InternalLinkedHashMap<K, V> : _HashVMBase                 |
|                        ::  MapMixin<K, V>,                            |
|                            _LinkedHashMapMixin<K, V>,                 |
|                            _HashBase,                                 |
|                            _OperatorEqualsAndHashCode                 |
|                        :::  LinkedHashMap                             |
|                                                                       |
+-----------------------------------------------------------------------+
|              _index   :int                                            |
|              _hashMask :int                                           |
|              _data     :List                                          |//偶数位放key,奇数放value
|              _usedData :int                                           |
|              _deletedKeys :int                                        |
|                                                                       |
+-----------------------------------------------------------------------+


+-----------------------------------------------------------------------+
|                                                                       |
|             _CompactLinkedHashSet<E> :_HashFieldBase                  |
|                 :: _HashBase, _OperatorEqualsAndHashCode, SetMixin<E> |
|                 ::: LinkedHashSet                                     |
|                                                                       |
+-----------------------------------------------------------------------+
|              _index   :int                                            |
|              _hashMask :int                                           |
|              _data     :List                                          |
|              _usedData :int                                           |
|              _deletedKeys :int                                        |
|                                                                       |
+-----------------------------------------------------------------------+


```


```
+-----------------------------------------------------------------------------+
|                                                                             |
|       _File : FileSystemEntity ::: File                                     |
|                                                                             |
|           readAsStringSync():String                                         |
|                                                                             |
|          _tryDecode(bytes:List<int> ,encoding):String                       |
|                                                                             |
|          readAsBytesSync(): Uint8List                                       |
|                                                                             |
|          openSync(): RandomAccessFile                                       |
|                                                                             |
|                                                                             |
+-----------------------------------------------+-----------------------------+
|                                               |                             |
|                                               |   BytesBuilder              |
|  _RandomAccessFile ::: RandomAccessFile       |                             |
|        _connectedResourceHandler:bool = false;|       add()                 |
|         path;String                           |                             |
|        _asyncDispatched; bool   = false       |  _CopyingBytesBuilder       |
|        _fileService;SendPort                  |        :BytesBuilder        |
|        _resourceInfo; _FileResourceInfo       |     _length ;  int          |
|        _ops;_RandomAccessFileOps              |     _buffer;  Uint8List     |
|                                               | _BytesBuilder               |
|        lengthSync()                           |       :BytesBuilder         |
|        readSync(int bytes):Uint8List          |     _length:int             |
|                                               |    _chunks:List<Uint8List>  |
|                                               |                             |
+-----------------------------------------------+-----------------------------+


```





## 续航
```
 pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "keep bright");


 adb shell dumpsys "power|grep -i wake" 
 查看
```

###  battery-historian

docker search battery-historian

go run setup.go 

 
docker run -it --user root --name battery -v /c/Users/docker:/share  -p 9995:9995 bhaavan/battery-historian /bin/bash -c "go run setup.go && go run cmd/battery-historian/battery-historian.go --port 9995"


[第三方 battery-historian （需代理访问）](https://bathist.ef.lc/)



到处文件
adb bugreport > bugreport.txt

### Battery Historian

AlarmManager/Timer
GPS

JobScheduler

