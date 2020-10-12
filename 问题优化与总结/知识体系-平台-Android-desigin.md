

## Kotlin 
封装细节（协程），重用代码，有助于提高工作效率
安全注解，提高代码安全性

基于 Kotlin 的应用，添加 kotlin-kapt 插件，使用 kapt 而不是annotationProcessor
## Jetpack
## 架构组件
androidx.arch.core:core-runtime 主线程和io线程
androidx.arch.core:core-common FastSafeIterableMap

### lifecycle
androidx.lifecycle:lifecycle-runtime/lifecycle-common
    ComponentActivity#LifecycleRegistry 观察者模式
    ReportFragment

androidx.lifecycle:lifecycle-viewmodel
    ComponentActivity#mViewModelStore   备忘录模式，Activity销毁可以复用
    androidx.activity.ComponentActivity#getViewModelStore 工厂方法，获取activity，fragment的 ViewModelStore

androidx.lifecycle:lifecycle-viewmodel-savestate
    SavedStateHandle 备忘录，小数据

androidx.lifecycle:lifecycle-viewmodel-ktx
    ViewModel.viewModelScope 增加协程支持

androidx.lifecycle:lifecycle-liveData
    LiveData 回调模式，适配器模式支持activity生命周期回调
   

####  LiveData+ViewModel+DataBinding 生命周期植入 
启用数据绑定和视图绑定
```js
android{
    viewBinding.enabled = true
    dataBinding.enabled true
}
```

##### Livedata 
    使界面控制器（Activity 和 Fragment）尽可能保持精简
##### ViewModel 
    将数据逻辑放在
##### View Binding（替代 ~~ButterKnife~~）
    视图与界面控制器之间维持干净的接口
    build\generated\source\kapt\debug\edu\ptu\java\app_kotlin\databinding\ActivityMainBindingImpl
##### Data Binding 
    数据绑定，事件绑定
    @Bindable CallBack模式
        1. onPropertyChanged，kotlin委托模式
            1.1 Bindable所在类需要继承BaseObservable
            1.2 set方法后，调用notifyPropertyChanged，触发BaseObservable回调，通知界面
        2. <data>需要放在<layout>下面；
        3. 支持 viewbinding 绑定或 databingding 绑定
    @BindingAdapter
        1. 必须静态方法。
        2. 适配ImageView 图片路径属性
    @BindingConversion
    @BindingMethods 内省及适配属性的setter，

⭐最佳实践：
    BaseObservable 替换为 ViewModel ，再引用LiveData
    （必须要设置lifecycleOwner，**viewBinding.lifecycleOwner=activity**，才能绑定Livedata和界面）
    

命令模式：  
    edu.ptu.java.app_kotlin.databinding.ActivityMainBindingImpl#executeBindings
代理模式：LiveDataListener代理访问WeakListener<LiveData<?>>，注册生命周期
    androidx.databinding.ViewDataBinding.LiveDataListener
中介者模式：管理mLocalFieldId和LiveData
    androidx.databinding.ViewDataBinding.LiveDataListener
### Initializer
androidx.startup.InitializationProvider
```
抽象工厂（四大组件）
    AppComponentFactory
        instantiateClassLoader()
        instantiateApplication()
        instantiateActivity()
        instantiateReceiver()
        instantiateService()
        instantiateProvider()


```
简单工厂
androidx.startup.Initializer
    discoverAndInitialize()

核心：控制反转
    AppInitializer.getInstance(context)
    .initializeComponent(ExampleLoggerInitializer.class);
### DataStore - 数据存储（Kotlin扩展）
```java
Context.createDataStore()://拓展Context方法；存储和读取，委托Serializer代理实现

工厂方法：
DataStoreFactory.create()
PreferenceDataStoreFactory.create()
```
编译时增强，每个视图文件增加Binding类，（Kotlin属性定义）
### Room
    构建者模式，通过数据库名，回调，失败策略构建RoomDatabase对象
    适配器模式，Dao_Impl 通过 EntityInsertionAdapter#bind 适配对Entity对象数据库操作，
    解释器模式，解释注解生成实现类 RoomDatabase_Impl，Dao_Impl
### workmanager
WorkManager 适用于**可延期**工作，即不需要立即运行但需要**可靠**运行的工作，即使用户退出或设备重启也不受影响。例如：
    向后端服务发送日志或分析数据
    定期将应用数据与服务器同步

Cache模式
androidx.work.impl.WorkDatabase_Impl
### 导航
IOC模式
通过 navigation/navgraph.xml ，动态生成 NavDirections，NavArgs子类，辅助导航 NavHostFragment 布局 FragmentContainerView 内的fragment
将 Add，modify，detail 页面整合在一个 activity。

与 ViewPager切换fragment 区别，view pager使用滑动，
### paging 3
对Kotlin协程和Flow流支持
PagingSource（v2 DataSource ）
PagingData （v2 PagedList ）
PagingDataAdapter （v2 PagedListAdapter ）


Pager 
    通过PagingConfig（分页配置）和PagingSource（Page工厂），构造PageFetcher，进而获取到 Flow<PaingData> 流

PagingSource工厂方法：
    PagingSource#load():LoadResult（子类 Error，Page）

PagingDataAdapter 装饰 Adapter，提供数据源 deffer:Channel
    添加 PaingData 数据：
    PagingDataAdapter#submitData(pagingData: PagingData<T>) 

## dagger-hilt 封装 dagger-android
2012，dagger1/dagger-square 使用Guice注入
    反射用于构建depencence Graph
2015，dagger-2.0，dagger2/dagger-Google 使用Auto注入
    没有反射
    component
    module
    @Inject `target` 
2017，v2.10-rc1，dagger-Android
    ⭐Component关联生命周期
    ❌需要编写AndroidInjection.inject()
2020，v2.25，dagger-hilt
    ⭐减少dagger-Android的Component复杂配置，动态生成Component
    ⭐编译时生成Inject方法
    ❌不直接支持内容提供程序（使用EntryPointAccessors）
    @HiltAndroidApp 编译时注释类修改为继承 Hilt_HApplication
    @AndroidEntryPoint build\generated\source\kapt\debug\edu\ptu\java\app_kotlin\di\hilt\app\Hilt_HActivity
### dagger-hilt
#### dagger-hilt 注解处理
使用@HiltAndroidApp ，@AndroidEntryPoint注解，编译时对含有生命周期的Application，Activity，Fragment，View 父类生成。
状态模式
```java 
java\dagger\hilt\android\processor\internal\androidentrypoint\AndroidEntryPointProcessor.java
注解入口
        new ApplicationGenerator(getProcessingEnv(), metadata).generate(); 
        new ActivityGenerator(getProcessingEnv(), metadata).generate(); 
        new BroadcastReceiverGenerator(getProcessingEnv(), metadata).generate(); 
        new FragmentGenerator(getProcessingEnv(), metadata).generate(); 
        new ServiceGenerator(getProcessingEnv(), metadata).generate(); 
        new ViewGenerator(getProcessingEnv(), metadata).generate();
```
```java
java\dagger\hilt\android\processor\internal\androidentrypoint\ApplicationGenerator.java
生成 Application 父类
build\generated\source\kapt\debug\edu\ptu\java\app_kotlin\di\hilt\app\Hilt_HApplication.java

编译时生成app的父类
  // @Generated("ApplicationGenerator")
  // abstract class Hilt_$APP extends $BASE implements ComponentManager<ApplicationComponent> {
  //   ...
  // }

编译时生成oncreate方法
  // @CallSuper
  // @Override
  // public void onCreate() {
  //   // This is a known unsafe cast but should be fine if the only use is
  //   // $APP extends Hilt_$APP
  //   generatedComponent().inject(($APP) this);
  //   super.onCreate();
  // }

  ApplicationComponentManager 抽象工厂，生成 DaggerHApplication_HiltComponents_ApplicationC
```
```java
java\dagger\hilt\android\processor\internal\androidentrypoint\ActivityGenerator.java
生成 Activity 父类
  // @Generated("ActivityGenerator")
  // abstract class Hilt_$CLASS extends $BASE implements ComponentManager<?> {
  //   ...
  // }
```
#### component注解生成
使用 @InstallIn 对 Module 所在的component进行标注，hilt支持的component有@ApplicationComponent，@ActivityComponent，@FragmentComponent，@ServiceComponent，@ViewComponent，@ViewWithFragmentComponent
```java
dagger.internal.codegen.ComponentProcessor
生成Component类
build\generated\source\kapt\debug\edu\ptu\java\app_kotlin\di\hilt\app\DaggerHApplication_HiltComponents_ApplicationC.java
```
Provider装饰工厂类，管理生成的类
管理工厂类
#### 预定义对象注解
@ApplicationContext
@ActivityContext


## 可维护性/通讯 - 架构之模块化（插件化及组件化）


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

