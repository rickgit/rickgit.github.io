

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
   

#### 生命周期植入  LiveData+ViewModel+DataBinding
启用数据绑定和视图绑定
```js
android{
    viewBinding.enabled = true
    dataBinding.enabled true
}
```

Livedata 
    使界面控制器（Activity 和 Fragment）尽可能保持精简
ViewModel 
    将数据逻辑放在
View Binding（替代 ~~ButterKnife~~）
    视图与界面控制器之间维持干净的接口
    build\generated\source\kapt\debug\edu\ptu\java\app_kotlin\databinding\ActivityMainBindingImpl
Data Binding 
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
### paging
### workmanager
WorkManager 适用于可延期工作，即不需要立即运行但需要可靠运行的工作，即使用户退出或设备重启也不受影响。例如：
    向后端服务发送日志或分析数据
    定期将应用数据与服务器同步

Cache模式
androidx.work.impl.WorkDatabase_Impl
## 导航
