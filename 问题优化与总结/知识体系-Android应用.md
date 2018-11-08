## Android知识体系

## 3 Android 基础

### 3.1  Android 系统体系

应用层，framework层，libs和Runtime层，内核层
#### 应用层
- 四大组件，Fragment

- Handler 消息机制
- AsyncTask
    
    容器类：ArrayDeque，LinkedBlockingQueue（ThreadPoolExecutor的线程队列）
    并发类：ThreadPoolExecutor（包含 ThreadFactory属性，用于创建线程），AtomicBoolean，AtomicInteger，FutureTask(包含Callable属性，任务执行的时候调用Callable#call,执行AsyncTask#dobackgroud)
- HandlerThread
- IntentService
- LruCache
- 窗口（Window，Activity，DecorView以及ViewRoot）
- View 测量，布局及绘制

- 进程通信
AIDL
文件
- Bitmap

- 动画
1. 补间动画
   烟花效果
2. 视图动画（Rotate,Scale,translate,alpha）
   箭头动画
   启动图片放大动画
   弹窗动画
3. 属性动画，插值器（Interpolator）和估值器（TypeEvaluator）
   估值器自定义滑动效果
4. Viewpager转化动画
5. SVG动画
6. Activity转场动画
7. Camera 3D动画
8. AR沉浸式效果（ARCore）

- 图形及用户界面
1. 界面及事件
2. openGl
   
- Context

- 持久化和序列化（Parcelable，Serializable）

### 3.2 Android 开发模式

#### 性能优化总结
- 渲染速度
    1. 布局优化（include merge, viewstub）
    分析工具，不必要不加载（include merge, viewstub），ConstaintLayout，Lint
    1. 绘制优化
    尽量用Drawable
    1. 响应速度优化
    2. ListView/RecycleView及Bitmap优化
    3. 线程优化
- 内存优化
- 电量消耗
    3.内存泄漏优化
        3.1 单例
        3.2 非静态内部类
        3.3 资源未关闭（webview没执行 destroy）
        3.4 ListView 未缓存
        3.5 集合类未销毁

- 其他性能优化的建议

#### 内存泄漏
 
#### 架构之模块化（插件化及组件化）
插件化
- Dynamic-loader-apk
- Replugin

组件化
- 组件间解耦
  1. AAC 
   ViewModel LiveData
  2. MVP DI框架Dagger2解耦
- 通信
1. 对象持有
2. 接口持有
3. 路由 （ARouter）
   Dagger2 依赖注入控制反转，

#### apk安装过程



#### 推送
- MQTT
1.  最核心的传输协议 Subcribe（定阅）和Publish（推送）
2. QoS（定阅等级）
   
- XMPP
1.  OpenFire管理服务器
2.  asmack包提供协议支持
3.  XMPP通信原语有3种：message、presence和iq

#### 直播
- 腾讯云直播（互动直播，收费）
1.  环境初始化与销毁
2.  房间初始化与销毁，声音控制
3.  上麦推流，下麦拉流
   
- 即构（互动视频）
1.  环境初始化与销毁
2.  房间初始化与销毁，声音控制
3.  推流和拉流

#### popupwindow 与 Dialog
- popupwindow 非阻塞浮层
- Dialog 阻塞式对话框
### 3.3 开源框架
#### OkHttp

#### Retrofit

#### RPC

#### EventBus
反射与注解


#### ARouter
控制反转和面向切面

#### FLutter
