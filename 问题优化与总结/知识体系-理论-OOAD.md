
## 封装性与内聚耦合

>《UML面向对象程序设计基础》

封装级别：
0级，代码行
1级，函数
2级，类
3级，包
4级，组件，模块

```
编写代码功能，处理耦合问题（语言，性能，内存/代码数量，可拓展），产生内聚现象/冗余（Redundancy）问题，重用/删除代码；又出现耦合问题，反复处理。解耦到达一定程度，出现组件、架构等概念，涉及通讯问题。
方法間耦合，内聚为（抽象為/开闭原则）类，重用Class的代码產生Class和Class之间的关系（泛化/实现 > 关联{组合 > 聚合} > 依赖），CARP原则，尽量使用组合/聚合重用。
类与类之间的关系强弱（需要降低耦合），将类放到不同Package，Package调用频率的情况下，重用为依赖包（模块/组件），开闭原则支持拓展。


                                             object+oriented
                                                    ^
                                                    |
                                                    +                  
                                                 abstract +----> Type +-------> inheritance +----> polymorphism
                 datatype/adt                       ^                 Generalization/Realization
                                                    |
infomation     +------------+  +-----------+   +----+----+   +----------+   +----------+
encapsulation  |  level 0,  |  | level 1,  |   |level 2, |   | level 3, |   | level 4, |
level          |  code line |  | function  |   |class    |   | package  |   | module   |
               +------+-----+--+--+-+-+--+-+-----+--+--+-+---+----------+---+----------+-->
                      ^           | | ^  ^       |  |  ^
                      |           | | |  |       |  |  |
                      +-----------+ +-+  +-------+  +--+
                       cohesion   coupling cohesion coupling
                                              +           +
                                              |           |
                                              |           |
                                              |           |            association(Composition/aggregation)
                                              v           v             
                                      class cohension    connascence                          +------------------+
                                           domain        encumbrance   dependencies           |     quality      |
                                          extrinsic                                           +------------------+
                                               +                                              | Robustness       |
                                               |                                              | reliable         |
                                               |         how to reuse                         | Extensibility    |
                                               +------------------------------------------->  | reusability      |
                                                                                              | maintainability  |
                                                                                              +------------------+



[职责分配原则](《Applying UML and Patterns》) 

+------------------------------+--------------------------------------------------------------------------------------------------+
|         |creator,            |                                                                                                  |
|         |controller,         |                                                                                                  |
|         |indirection,        |          23 Design Patterns                            J2EE Patterns                             |
|         |information expert, +--------------------------------------------------------------------------------------------------+
|         |high cohesion,      |                                                                                                  |
|         |low coupling,       |     Design Patterns - Elements of Reusable Object-Oriented Software                              |
|         |polymorphism,       |                                                                                                  |
|         |pure fabrication    |                                                                                                  |
+---------------------------------------------------------------+---------------------------------------------+-------------------+
|         |                    |                                |        S.R.P                  LoD/PLK       |                   |
|         |                    | Dependency Inversion Principle |        for short              loose coupling|                   |
|         |                    |   depend on abstractions       |                                             |                   |
|         |                    |   not on concretions           |                                             |                   |
|         |                    +------------------------------------------------------------------------------+                   |
|         |                    | Liskov substitution principle  |      Interface segregation principle        |                   |
|         | protected          |   every subclass/derived class |        never be forced to implement         |                   |
|         | variations         |   should be                    |        an interface that it doesn't use;    |                   |
|   GRASP |                    |   for their base/parent class  |        or shouldn't be forced to depend on  |                   |
|         |                    |                                |        methods they do not use              |                   |
|         |                    +--------------------------------+---------------------------------------------+                   |
|         |                    |   Open-closed Principle                                                      |                   |
|         |                    |     open for extension,                                                      |   CARP reusablity |
|         |                    |     but closed for modification                                              |                   |
|         |                    |                                                                              |                   |
+---------+--------------------+------------------------------------------------------------------------------+-------------------+ 

```

内聚：度量一个给定的**程序**内多行代码的单一功能性，以确定是否达到该程序所实现的目的
耦合性：度量**程序**之间联系的次数和强度
共生性：软件中一组相互依赖关系。（二级封装-类，带来复杂的相互依赖关系）

依附集：测量一个类的所有附属物的方法，衡量类复杂度的方法
类的内聚：衡量该类的特性（操作运算符和属性）属于一个单一整体的完善程度。


抽象:所带来的问题（个性与共性问题，对象类型层次）
- DRY《The Pragmatic Programmer》
- YAGNI "极限编程"
- Rule of three 《Refactoring》

工厂单例构造原型，桥接适配组合 代理享元装饰外观,命令中介观察访问备忘  解释责任模板 迭代策略状态 
```
1. Strategy:
Defines a family of algorithms, encapsulates each one, and make them interchangeable.
 Strategy lets the algorithm vary independently from clients who use it.
2. Decorator:
Attach additional responsibilities to an object dynamically.
Decorators provide a flexible alternative to subclassing for extending functionality.
3. Factory Method
Define an interface for creating an object, but let the subclasses decide which class to instantiate. 
Factory Method lets a class defer instantiation to subclasses.
4. Observer
Define a one-to-many dependency between objects so that when one object changes state, all its dependents are notified and updated automatically.
5. Chain of Responsibility
Avoid coupling the sender of a request to its receiver by giving more then one object a chance to handle the request. 
Chain the receiving objects and pass the request along the chain until an object handles it.
6. Singleton
Ensure a class only has one instance, and provide a global point of access to it.
7. Flyweight
Use sharing to support large numbers of fine-grained objects efficiently. 
A flyweight is a shared object that can be used in multiple contexts simultaneously. 
The flyweight acts as an independent object in each context; it’s indistinguishable from an instance of the object that’s not shared.
8. Adapter
Convert the interface of a class into another interface clients expect. 
Adapter lets classes work together that couldn’t otherwise because of incompatibility interfaces.
9. Façade
Provide a unified interface to a set of interfaces in a system. 
Façade defines a higher-level interface that makes the subsystem easier to use.
10. Template
Define a skeleton of an algorithm in an operation, deferring some steps to subclasses. 
Template Method lets subclasses redefine certain steps of an algorithm without changing the algorithms structure.
11. Builder
Separate the construction of a complex object from its representation so that the same construction processes can create different representations.
12. Iterator
Provide a way to access the elements of an aggregate object sequentially without exposing its underlying representation.
13. Composite
Compose objects into tree structures to represent part-whole hierarchies. 
Composite lets clients treat individual objects and compositions of objects uniformly.
14. Command
Encapsulate a request as an object, thereby letting you parameterize clients with different requests, queue or log requests, and support undoable operations.
15. Mediator
Define an object that encapsulates how a set of objects interact. 
Mediator promotes loose coupling by keeping objects from referring to each other explicitly, and lets you vary their interaction independently.
16. State
Allow an object to alter its behavior when its internal state changes. 
The object will appear to change its class.
17. Proxy
Provide a surrogate or placeholder for another object to control access to it.
18. Abstract Factory
Provide an interface for creating families of related or dependent objects without specifying their concrete classes.
19. Bridge
Decouple an abstraction from its implementation so that the two can vary independently.
20. Interpreter
Given a language, define a representation for its grammar along with an interpreter that uses the representation to interpret sentences in the language.
21. Memento
Without violating encapsulation, capture and externalize an object’s internal state so that the object can be restored to this state later.
22. Prototype
Specify the kinds of objects to create using a prototypical instance, and create new objects by copying this prototype.
23. Visitor
Represent an operation to be performed on the elements of an object structure. 
Visitor lets you define a new operation without changing the classes of the elements on which it operates.
```





## 架构
##
```
 https://en.wikipedia.org/wiki/Computer_programming

+-----------------------+------------------------+--------------------------+
|  programming          |    Reliability         |                          |
|                       |    Robustness(not bugs)|                          |
|  Quality requirements |    Usability           |                          |
|                       |    Portability         |                          |
|                       |    Maintainability     |                          |
|                       +---------------------------------------------------+
|                       |                        |(processor time,          |
|                       |  Efficiency/performance|  memory space            |
|                       |                        | slow devices such as     |
|                       |  (the less, the better)|disks, network bandwidth  |
|                       |                        |and to some extent even   |                                                                                           :
|                       |                        | user interaction)        |
+------------------------------------------------+--------------------------+ 
|                       |                                                   |
+-----------------------+---------------------------------------------------+


+------------------------------------------------------------+
|  培训/沟通                                                  |
+------------------------------------------------------------+
|  Project(documents)                                        |
+------------------------------------------------------------+
|  Application                                               |
+------------------------------------------------------------+
|  API(Application Programming Interface) (compiler/tools )  |
+------------------------------------------------------------+
|  knowledge                                                 |
+------------------------------------------------------------+

```
##
高耦合产生问题
- 修改代码，改动量大，造成程序不稳定，容易产生异常，错误等问题
[架构,框架,设计模式](https://blog.csdn.net/CillyB/article/details/79464339)
[软件架构、框架、模式、模块、组件、插件概念汇总 ](https://www.cnblogs.com/doit8791/p/6129963.html)

[mvc mvp mvvm解释](https://medium.com/@ankit.sinhal/mvc-mvp-and-mvvm-design-pattern-6e169567bbad)
### 数据架构 -MVC
 
```
                 +--------------------+
                 |  View              |
       +------+  |                    |  < - +
       |         | disply view/       |      |
       |         | control  layouts   |
       |         +--------------------+      | update
       | events/
       v user action                          +

+-------------+                   +---------------+
|             |                   |    Model      |
|   Control   |    +---------->   |               |
|             |                   | serialize     |
|             |   data operation  | operation     |
+-------------+                   +---------------+

 
```


### 数据结构 - MVP
MVP的目的就是解藕，把业务逻辑，网络请求丢在P层，页面不发生变化，就只用改P层逻辑，从而达到了解藕的目的
P层不包含更新界面数据的代码。
```
+-----------------+             +------------------+            +------------------+
|                 | <--------+  |                  | < - - - +  |                  |
|   View          |             |  presenter       |            |   model          |
|                 | +-------->  |                  | +--------> |                  |
+-----------------+             +------------------+            +------------------+

```
相对与MVP，解耦View和Model
####  MVP实现库 Mosby

### MVVM
```
+-----------------+             +------------------+            +------------------+
|                 | <--------+  |                  | < - - - +  |                  |
|   View          |             |  presenter       |            |   model          |
|                 | ----------  |                  | +--------> |                  |
+-----------------+             +------------------+            +------------------+

```


## 数据通讯- 进程通信方式

### RPC

### IDL 

### IPC

1. Android Binder（AIDL/服务，Messenger/Handler,contentProvider，广播）
2. 文件
3. Socket
进程间（内）通信
本质:通过 Binder 实现，AIDL定义服务，对 Parcelable 对象序列化与反序列化，传输数据

其他进程间通信方式：**文件**，**Socket** （引用于《Android 开发艺术探索》 ）


- 只有允许不同应用的客户端用 IPC 方式调用远程方法，并且想要在服务中处理多线程时，才有必要使用 AIDL
- 如果需要调用远程方法，但不需要处理并发 IPC，就应该通过实现一个 Binder 创建接口
- 如果您想执行 IPC，但只是传递数据，不涉及方法调用，也不需要高并发，就使用 Messenger 来实现接口
- 如果需要处理一对多的进程间数据共享（主要是数据的 CRUD），就使用 ContentProvider
- 如果要实现一对多的并发实时通信，就使用 Socket
## 数据通讯-EventBus事件总线
进程内线程间（内）通信，用于解耦代码，触发消息，多处代码需要做不同的响应处理
```
                +-------------+
                |             |
                | publisher   | EventBus.getDefault()
                |             |
                +-----+-------+
                      |
                      |
                      |   +------------+
        postSticky()  |   |            |
        post()        |   |  event     | POSTING，MAIN，BACKGROUND，ASYNC
                      |   |(class type)|
                      v   +------------+

                +------------------------------------------------------------------+
                |         +------+            +------+                             |
event bus       |         | event|            | event|                             |
                |         |      |            |      |                             |
                |         +------+            +------+                             |
                +-------------------------+----------------------+-----------------+
                                        |                      |
                                        |                      |
                                        v                      v

                                +-------------+       +-------------+
                                |             |       |             |
                register        | subscriber  |       | subscriber  |
                                | @Subscrie   |       | @Subscrie   |
                                | eventMethod |       | eventMethod |
                                +-------------+       +-------------+


```

## 数据对象依赖注入解耦 -Dagger2 依赖注入框架

 
```
 ----------------------------------------------------->
+-------------------------+--------------------------------+---------------+
|             ViewClass   |      @Inject                   |               |
|                         +-----------------------+        |               |
+------------+---+--------+  @Component           |        |               |
|            |   |        | (modules, dependencys)|        |               |
|            |   | @Scope | ComponentInterface    |        |               |
|            |   |        |                       |        |InjectDataClass|
|            |   +--------+---------------+       |        |/PresenterClass|
|            |            @Module         |       |        |               |
|            |           ModuleClass      |       |        |               |
| @Named/    +------------+-------------+-+-------+--------+               |
| @Qualifier | @Scope     | @IntoMap    |                  |               |
|            |            | @StringKey/ |                  |               |
|            |            | @ClassKey   | @provide/@Binds  |               |
|            |            +-------------+ provideDataMethod|               |
|            |            | @IntoSet    |                  |               |
+------------+------------+-------------+------------------+---------------+

```



对象依赖注入方式： 接口，set方法,构造方法,Java注解
   Dagger2 依赖注入控制反转，Dagger 2 是 Java 和 Android 下的一个完全静态、编译时生成代码的依赖注入框架
，完全去除了反射。

Dagger2解决了这些问题，帮助我们管理实例，并进行解耦。

 解决android View与presenter解耦


 1. 注入数据类 @Inject
 2. 关联数据类 @Module @Provides
 3. 注解生成路径

**@Inject** 注解生成的代码。初始化数据对象的代码，用于稍后component关联Activity和数据类
 **Dagger\app\build\generated\source\apt\debug\edu\ptu\java\dagger\MainActivity_MembersInjector.java**


**@Modul** 类下的 **@Provides** 注解生成的代码。缓存Module类的代码
**Dagger\app\build\generated\source\apt\debug\edu\ptu\java\dagger\DataModule_ProvidesAFactory.java**



 4. 使用@Component 关联 @Inject 数据类 与 @Module 数据提供类
@Component生成的代码。调用 **injectActivity** 向包含 **@Inject** 的Activity 注入数据类
**Dagger\app\build\generated\source\apt\debug\edu\ptu\java\dagger\DaggerDataComponent.java**


**@Scope** 注解如何保证依赖在 component 生命周期内的单例性，必须在**Component**先定义声明周期，才能Provider定义**Scope**
**@Named**/**@Qualifier**  @Inject和@Provider匹配,解决依赖冲突
 
 
### dagger2 新的注释 @SubComponent
@Component dependencies 和 @SubComponent 两种方式关联Comonponent
 @Binds 替代 @Provider
### dagger.android
Android MVVM with Dagger2, Retrofit, RxJava and Android Architecture Components
[AndroidInjector](https://segmentfault.com/a/1190000010016618)

```
+--------------------------------+
| AndroidInjection.inject(this); |  <-------+
+--------------------------------+          |
                                            |                    +-----------------------------------+
+--------------------------------+          |                    | @Module                           | 
| AndroidInjection.inject(this); |  <-+     |                    |  abstractActModule1               |
+--------------------------------+    |     |                 +--+     @ContributesAndroidInjectortor(modules={implModule})
                                      |     |                 |  |     abstract Act contributeAct()  |
+-------------------------------------+-----+--+              |  +-----------------------------------+
| App:HasActivityInjector                      |              |
+----------------------------------------------+              |  +------------------------------------------+
|      @Inject                                 |              |  | +------------------------------------+   |
|      DispatchingAndroidInjector              |              |  | |  @SubComponent                     |   |
+----------------------------------------------+              |  | |  ActSubComponent:AndroidInjector   |   |
                                                              |  | |                                    |   |
                ^                                             |  | |        @Subcomponent.Builder       |   |
                |                                             |  | +------------------------------------+   |
+---------------+-----------------------------------+         |  | +------------------------------------+   |
|@Componect(AndroidInjectionModule.class,ActModule1)|  <------+  | |  @Module                           |   |
| IAppComponent:AndroidInjector                     |            | |  ActModule2                        |   |
|                                                   |  <-------+ | |                                    |   |
|                                                   |            | +------------------------------------+   |
+---------------------------------------------------+            +------------------------------------------+

```

*dagger-android* 包含 **@ContributesAndroidInjectortor**的 **抽象buildsModule** 用来根据Activity创建**具体Module**

MVP+daggers
1. 如何再Activivty注入(@Inject)Presenter


kotlin
```
D:\workspace\ws-component\Dagger2App\app\build\tmp\kapt3\stubs\debug\edu\ptu\java\dagger2app
```



## 数据异步链式开发框架-Rxjava+Rxlife+RxCache
RxJava2.0是非常好用的一个异步链式库,响应式编程，遵循观察者模式。
```
  002_initial_commit      87cfa7a44 Initial commit
  003_observer_observable 2a4122c11 Rename to Observer/Observable
* 004_rxwork              9d48f996e Refactoring for consistent implementation approach.
  005_rx                  aa423afe5 changing package to rx.*
  006_subject             967337e24 Adding a draft of Subject class https://github.com/Netflix/RxJava/issues/19


```

```

+--------------------------+        +----------------------------+       +------------------------------+
|                          |        |                            |       |                              |
|     create layer         |        |     subscribe call layer   |       |    execute call layer        |
|                          |        |                            |       | (subscriber/observer method) |
+--------------------------+        +----------------------------+       +------------------------------+
|                          |        |                            |       |                              |
|    +---------------+     |        |    +-------------------+   |       |     +---------------------+  |
|    |    create()   |     |        |    |                   |   |       |     |                     |  |
|    |     just()    |     |        |    |subscribe(observer)|---------------> |   call()            |  |
|    |     from()    |     |        |    |                   |   |       |     |                     |  |
|    +-------+-------+     |        |    +-------------------+   |       |     +---------+-----------+  |
|            |             |        |                            |       |               |              |
|            |             |        |             ^              |       |               v              |
|            v             |        |             |              |       |                              |
|                          |        |             |              |       |     +---------------------+  |
|    +---------------+     |        |    +--------+----------+   |       |     |                     |  |
|    | map<Obj,Str>()|     |        |    |                   |   |       |     |   onNext()          |  |
|    | flatMap ()    |     |        |    |subscribe(observer)|   |       |     |                     |  |
|    |  compose()    |     |        |    |                   |   |       |     +----------+----------+  |
|    +-------+-------+     |        |    +-------------------+   |       |                |             |
|            |             |        |                            |       |                v             |
|            v             |        |             ^              |       |                              |
|                          |        |             |              |       |                              |
|    +---------------+     |        |    +--------+----------+   |       |     +---------------------+  |
|    |               |     |        |    |                   |   |       |     |                     |  |
|    |   observeOn() | +---------------> |subscribe(observer)|   |       |     |   switch thread     |  |
|    | subscribeOn(  |     |        |    |                   |   |       |     |                     |  |
|    |   Schedulers) |     |        |    +-------------------+   |       |     +---------------------+  |
|    +---------------+     |        |                            |       |                              |
|                          |        |                            |       |                              |
+--------------------------+        +----------------------------+       +------------------------------+

```

需要有订阅，Observable才会执行

[rx操作符](http://reactivex.io/documentation/operators.html)
ReactiveX provides a collection of operators with which you can filter, select, transform, combine, and compose Observables.
```
+------------------------------------------------------------------------------------------------------------------------------------------------+
|  Create                  Debounce                              Delay                                                                           |
|  Defer                   Distinct                              Do                                                                              |
|  Empty                   ElementAt                             Materialize                                                                     |
|  /Never                  Filter                                /Dematerialize All                                                              |
|  /Throw                  First                                 ObserveOn      Amb                                                              |
|  From      Buffer        IgnoreElement  And/Then/When          Serialize      Contains          Average                                        |
|  Interval  FlatMap       Last           CombineLatest          Subscribe      DefaultIfEmpty    Concat                                         |
|  Just      GroupBy       Sample         Join                   SubscribeOn    SequenceEqual     Count                                          |
|  Range     Map           Skip           Merge                  TimeInterval   SkipUntil         Max                         Connect            |
|  Repeat    Scan          SkipLast       StartWith              Timeout        SkipWhile         Min                         Publish            |
|  Start     Window        Take           Switch        Catch    Timestamp      TakeUntil         Reduce                      RefCount           |
|  Timer                   TakeLast       Zip           Retry    Using          TakeWhile         Sum                         Replay        to   |
+------------------------------------------------------------------------------------------------------------------------------------------------+
|  Creating  Transforming  Filtering     Combining      Error    Utility        Conditional     Mathematical                                     |
|                                                       Handling                and Boolean     and Aggregate  Backpressure  Connectable  Convert|
+------------------------------------------------------------------------------------------------------------------------------------------------+

```
[Rxjava Operater](https://github.com/ReactiveX/RxJava/wiki/Alphabetical-List-of-Observable-Operators)
```
+----------------------------------+--------------------------------------+----------------+-----------------------+
|                                  |                                      |                |                       |
|deferFuture( )    runAsync( )     | getIterator( )                       |                |                       |
|                                  | toIterable( )      mostRecent( )     |                | stringConcat( )       |
|startFuture( )    fromRunnable( ) |  toFuture( )       lastOrDefault( )  |                |                       |
|                                  | singleOrDefault( ) last( )           |                | join( )   split( )    |
|toAsync( )        fromCallable( ) |  single( )         firstOrDefault( ) |                |                       |
|or asyncAction( )                 |                    first( )          |                | encode( )   from( )   |
|or asyncFunc( )   fromAction( )   |  latest( )                           | Flowable       |                       |
|                                  |                    forEach( )        |                | decode( )   byLine( ) |
|start( )          forEachFuture( )|   next( )                            |                |                       |
+------------------------------------------------------------------------------------------------------------------+
|        Async                     |             Blocking Observable      | Parallel flows |    String             |
+----------------------------------+--------------------------------------+----------------+-----------------------+


```
背压策略：即生产者的速度大于消费者的速度带来的问题，比如在Android中常见的点击事件，点击过快则经常会造成点击两次的效果。
Flowable背压策略
```
onBackpressureBuffer()
onBackpressureDrop()
onBackpressureLatest() 

```

### Rxjava Scheduler / Worker / Runnable 

```
+-----------------+---------------------------------------------------------------------------+
|                 |                             Schedulers                                    |
+--------------------------------------+------------------------------------------------------+
|                 |computation() |io() | trampoline() |newThread()|  single() |from(Executor) |
+---------------------------------------------------------------------------------------------+
|  corePoolSize   |              |1    |              |           |           |               |
+---------------------------------------------------------------------------------------------+
|  maximumPoolSize|              |     |              |           |           |               |
+---------------------------------------------------------------------------------------------+
|  keepAliveTime  |              |     |              |           |           |               |
|                 |              |     |              |           |           |               |
+---------------------------------------------------------------------------------------------+
|  unit           |              |     |              |           |           |               |
+---------------------------------------------------------------------------------------------+
|  workQueue      |              |     |              |           |           |               |
+-----------------+--------------+-----+--------------+-----------+-----------+---------------+

```
线程间切换

## 数据路由 - Activity栈

## 数据路由/通讯 - ARouter
- apt技术
- spi技术

```
----------------------------------------------->


+-----------------------------+             +----------------------------+
|                             |             |                            |
|  @Route                     |             |  @Route                    |
|                             |             |                            |
|  Activity/   +--------------+             |  Activity/   +-------------+
|  Fragment/   |              |             |  Fragment/   |             |
|  Service     |  @AutoWire   |             |  Service     |  @AutoWire  | 
|              |              |             |              |             |
|              |  extraField  |             |              |  extraField |
+-----------------------------+             +----------------------------+
+----------------------------------------------------------------------------+
|                           @Interceptor                                     |
+----------------------------------------------------------------------------+


```

## 数据结构 - AAC
AAC是一套MVVM架构组件

```
               +--------------------------+
               |    Activity/Framgent     |
               +------------+-------------+
                            v

             +------------------------------+
             |      ViewModel +-------------+
             |                |  LiveData   |
             +--------------+-+-------------+
                            |
                            v

             +------------------------------+
        +----+                              +--+
        |    +------------------------------+  |
        v                                      v
+--------------------+       +---------------------+
|   Model            |       |   Remote Data Source|
|        +-----------+       |        +------------+
|        |  Room     |       |        |  Retrofit  |
+-------++-----------+       +--------+------------+
        |
        v
+--------------------+
|   Sqlite           |
+--------------------+

```

- LiveData 数据对象更新观察类
- ViewModel 提供了创建和检索（类似于单例）绑定到特定生命周期对象的方法，有生命周期的Presenter
- LifeCycle
- Room

## 整合框架
```
 DaggerAndroidMVVM

                                                                                      +-------------------+
                                                                                      |                   |
                                                                                      | Retrofite+OkHttp  |
                                                                                      +-------------------+


                                                +-------------------------+
+------------------------------------+          | VM                      |           +-----------------------+
|   view                             |          +-------------------------+           |  model                |
|                                    | <------+ |  lifecycle:android+arch |           +-----------------------+
+------------------------------------+          |                         |  +------> |                       |
|                                    |          +-------------------------+           |  data reactivex:rxjava|
|  view di:ButterKnife/*DataBinding  |          |   di:  daggger+android  |           |                       |
|         :kotlin+android+extensions |          |                         |   <-----+ |                       |
+------------------------------------+          +-------------------------+           +-----------------------+

 
 

```
## 插件 - RePlugin

```
+--------------+---------------+--------------------+----------------------+-----------------+-------------------+
|              | DLA           |  QZone             | Andfix               | tinker          | Replugin          |
|              | 2014          |  ( Nuwa,HotFix)    | 2015 (2017 Rubust)   | 2016            | 2017              |
+----------------------------------------------------------------------------------------------------------------+
|          code| √             |  √                 |  x                   |  √              |                   |
|fix       res | √             |  √                 |  x                   |  √              |                   |
|          so  |               |                    |  x                   |  √              |                   |
+----------------------------------------------------------------------------------------------------------------+
|         hot  |               |  x                 |  √                   |  √              |                   |
|install  patch| √             |                    |  √                   |                 |                   |
|       package| √             |                    |                      |                 |                   |
+----------------------------------------------------------------------------------------------------------------+
|              | ProxyActivity |CLASS_ISPREVERIFIED | native hook          |PathClassLoader  | placeholdActivity |
|insignt       |               |PathClassLoader     | dalvik_replaceMethod |                 | PathClassloader   |
|              | DexClassloder |                    |                      |custom App class | custom App class  |
|              | replace dex   |replace class       |                      |replace dex      | replace dex       |
+----------------------------------------------------------------------------------------------------------------+
|auto gen code |               |                    |                      |                 |                   |
|(gradle)      | x             | x                  |  x                   | √               | √                 |
+----------------------------------------------------------------------------------------------------------------+
|  performation|               | slow               |                      |                 |                   |
+------------------------------+--------------------+----------------------+-----------------+-------------------+

```
- 生命周期管理
- res资源加载
- 插件Dex的加载


```
+--------------------------------------------------------------------+
|                          plugins                                   |
|                                     +------------------------------+
|                                     |  webview   download          |
|                                     |  protobuf  share             |
+-------------------------------------+------------------------------+
|                        Plugin Framework                            |
| Replugin plugin_activity  component    Replugin       process      |
|                           pit_manager  classloader    manager      |
|                                                                    |
| Fast-sync plugin   task-affinity   plugin service  plugin provider |
| AIDL      context  pit manager     client          client          |
+--------------------------------------------------------------------+
|                         Android                                    |
|   Context Application ClassLoader Resources    Package             |
|                                                Manager             |
|   Intent  Application        +-------------------------------------+
|           info               |      Android os                     |
+------------------------------+-------------------------------------+


+------------------------------+                    +-----------------------------+
|                              |                    |                             |
|        Host Proj             |                    |        Main Plugin          |
|                              |                    |                             |
+------------------------------+                    +-----------------------------+
|                              |                    |                             | 
|                +-------------+                    |               +-------------+
|                |             |                    |               |             | 
+----------------+-------------+                    +---------------+-------------+


```