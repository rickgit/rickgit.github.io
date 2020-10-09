# 设计原则
> [](https://java-design-patterns.com/principles)
<<Design Patterns>> <<Enterprise Integration Patterns>> <<Code Complete>> <<POSA>>

1. 三大通用原则
KISS
YAGNI 
  (关注分离，面向维护者代码，避免过早优化）
DRY   

[Boy-Scout Rule（童子军规则）:何一个成员都有义务去改善代码](《Bad Code, Craftsmanship, Engineering, and Certification》)

1. 模块内部与模块间
低耦合（模块间）高内聚（模块内）
CARP（模块间，属OOAD），LoD（模块间），IoC（模块间）
SOLID（模块内，属OOAD），良性依赖原则（dagger）， 命令-查询分离原则（模块内）

# 模式
[](https://en.wikipedia.org/wiki/Software_design_pattern)
## GOF23
```
                                                               association
                                                               aggregation
                             inheritance                       composition
    encapsulation            polymorphism                      dependencies
+----------------------+----------------------------------+---------------------------------------+
|                      |                                  | Visitor                               |
|                      |                                  |                                       |
|                      |                                  | Template                              |
|                      |                                  |                                       |
|                      |                                  | Strategy                              |
|                      |                                  |                                       |
|                      |                                  |*Null Object                           |
|                      | proxy pattern                    | State Pattern                         |
|                      |                                  |                                       |
|                      | Flyweight pattern                | Observer Pattern                      |
|                      |  reduce num of objects created   |  one-to-many relationship             |
| Prototype pattern    | Facade pattern                   | Memento Pattern                       |
|  create duplicate obj|  hides complexities of the system|                                       |
|                      | Decorator pattern                | Mediator Pattern                      |
| Builder pattern      |   wrapper to existing class      |                                       |
|  builds complex obj  |   add new functionality          | Iterator Pattern                      |
|                      | Composite pattern                | Interpreter Pattern                   |
| Singleton pattern    |                                  |  evaluate language grammar            |
|                      |*Filter pattern                   |  or expression                        |
| Abstract Factory     | Bridge Pattern                   | Command Pattern                       |
|  creates factories   |  decouple abstraction            |  object as command                    |
|                      |  from implementation             |                                       |
| Factory pattern      | Adapter pattern                  | Chain of Responsibility               |
|  hidin creation logic|  two incompatible interfaces     |  creates a chain of recei^er obj      |
+-------------------------------------------------------------------------------------------------+
| Creational Patterns  | Structural Patterns              | Behavioral Patterns                   |
|   create objects     |  class and object composition    |  communication                        |
|                      |  obtain new functionalities      |  between objects                      |
+----------------------+----------------------------------+---------------------------------------+

```

  - 构建者模式
  Notification，AlertDialog，StringBuilder 和StringBuffer，OKhttp构建Request，Glide

  - 单例模式
  Application，LayoutInflater
  - 工厂方法
  BitmapFactory
  - 享元模式
    Message.obtainMessage
  - 代理模式
    静态代理：封装ImageLoader、Glide；动态代理：面向切面。AIDL
  - 组合模式
    View和ViewGroup的组合
  - 外观模式
    ContextImpl
  - 桥接模式
    Window和WindowManager之间的关系
- 行为型
  - 命令模式
    EventBus，Handler.post后Handler.handleMessage
  - 观察者模式
    RxAndroid，BaseAdapter.registerDataSetObserver
  - 策略模式
    时间插值器，如LinearInterpolator
  - 状态模式
  - 责任链
    对事件的分发处理，很多启动弹窗
  - 备忘录模式
    onSaveInstanceState和onRestoreInstanceState
  - 解释器模式
    PackageParser
  - 中介者模式
    Binder机制
## 其他：
Creational 
        Converter转换器
        DI
        Factory Kit
        简单工厂/静态工厂
        RAII
        （实例）
        MonoState
        Multition  多例模式
        Object Mother
        动态属性
        Step Builder //多个builder组成一个大的Builder
        Value Object //重写equals()、hashCode()
        ⭐（性能）
       Object pool
        Lazy Loading

 Structural
        委托
        Front Controller Pattern
        Marker Interface 标记接口
        Module
        Event Aggregator
        Flux
        Page Object
        Role Object
        Strangler
        Twin

 Behavioral 
        Null Object
        Servant
        
       字节码
        Acyclic Visitor
        Extension objects
        Feature Toggle
        Game Loop
        Intercepting Filter
        Leader Election


       ⭐（性能）
        Data Locality
        缓存（some fast-access storage， re-used to avoid having to acquire them again）

        Dirty Flag
        Double Buffer

        Throttling 节流模式
        Retry
        
        Sharding 分片模式
        Spatial Partition 空闲分区
        Trampoline
        Circuit Breaker

 Concurrency 
        Active Object	（POSA2）
        Double-checked locking（POSA2）
        Monitor object	（POSA2）
        Reactor（POSA2）
        Thread-specific storage（POSA2）
        Scheduler	
        Event Queue
        （性能）
        Half-Sync/Half-Async
        Leader/Followers
        Master-Worker
        Queue based load leveling
        Reader Writer Locker
        Semaphore
        Thread Pool
        （reactive）
        生产者消费者
        异步方法调用
        异步事件
        Promise
 Functional 
        Collection Pipeline
        Filterer
        Fluent Interface
        Monad
 Architectural 
        （数据访问）
        DAO
        Repository
        Service Layer
        Unit of Work
        （解耦）
        API GateWay
        MVC/MVP
 Idiom
        CallBack（与观察者模式区别，被观察者维护一个观察者列表）
        Private Class Data
        Thread Local Storage
        AAA
 Integration 


# OOAD IDEF4：面向对象设计(Object-Oriented Design)

《Pattern-oriented Software Architecture 》五套书
《Software Architecture: Perspectives on an Emerging Discipline》



## 封装性与内聚耦合

>《UML面向对象程序设计基础》

封装级别：
0级，代码行
1级，函数
2级，类
3级，包
4级，组件，模块

编写**内聚**的代码-[《高效程序员的45个习惯》]

耦合问题->内聚方法（如换行）->重用方法（如封装）
```
1. 降低耦合（语言，性能，内存/代码数量，可拓展），LKP（最少知识原则,LoD），SOLID原则；
    函数式编程->AOP（通过预编译时期APT实现静态代理或运行期动态代理实现程序功能的统一维护的一种技术。）；
    IoC（配置文件，如反射，注解，xml等，决定接口的实现类，而不是代码）/DI
2. 内聚处理，冗余（Redundancy）产生，如重复出现三次以上的代码；
3. 重用/删除代码（design pattern:reusable code；CARP原则：泛化/实现 > 关联{组合 > 聚合} > 依赖）；
4. 又出现耦合问题，反复处理。解耦到达一定程度，出现组件、架构等概念，涉及通讯问题。
方法間耦合，内聚为（抽象為/开闭原则）类，重用Class的代码產生Class和Class之间的关系，
类与类之间的关系强弱（需要降低耦合），将类放到不同Package，Package调用频率的情况下，重用为依赖包（模块/组件），开闭原则支持拓展。

面向对象耦合（接口注入。setter方法注入。构造方法注入。注解方式注入）：
1. 创建类。解法：注解（Dagger），AOP
2. 注入代码。解法：面向切面解耦。
3. 依赖注入。长生命周期类，依赖的对象解耦，面向方法变成。
4. 类代码量大。桥接模式解耦


                                             object-oriented
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
+---------+--------------------+---------------------------------------+--------------------------------------+-------------------+
|         |                    | Interface segregation principle       |                                      |                   |
|         |                    |   never be forced to implement        |  Dependency Inversion Principle      |                   |
|         |                    |   an interface that it doesn't use;   |    depend on abstractions            |                   |
|         |                    |   or shouldn't be forced to depend on |    not on concretions                |                   |
|         |                    |   methods they do not use             |                                      |                   |
|         |                    +------------------------------------------------------------------------------+                   |
|         | protected          |                                       |  Liskov substitution principle       |                   |
|         | variations         | S.R.P                  LoD/LKP        |    every subclass/derived class      |                   |
|   GRASP |                    | for short              loose coupling |    should be                         |                   |
|         |                    |                                       |    for their base/parent class       |                   |
|         |                    +---------------------------------------+--------------------------------------+                   |DRY > KISS > YAGNI原则
|         |                    |                    Open-closed Principle                                     |                   |
|         |                    |                      open for extension,                                     |CARP/CRP reusablity|
|         |                    |                      but closed for modification                             |                   |
|         |                    |                                                                              |                   |
+---------+--------------------+------------------------------------------------------------------------------+-------------------+ 
                                                      SOLID Principles 

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

工厂单例构造原型，桥接适配组合 代理装饰享元外观,命令中介观察访问备忘  解释责任模板 迭代策略状态 
```
1. Strategy:
Defines a family of algorithms, encapsulates each one, and make them interchangeable.
 Strategy lets the algorithm vary independently from clients who use it.
    双检测：保证效率，再加锁检测。voliate保证指令不重排序，防止获取属性为null
    静态内部类：cinit加锁，保证类加载只加载一次
    枚举类： 枚举类实现
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





## 架构（稳定，流畅，续航，精简）

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
## 高耦合产生问题


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
## 观察者模式：数据通讯-EventBus事件总线
[Observer ](./知识体系-理论-OOAD-Observer.md)

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
 

## 数据异步链式开发框架-Rxjava+Rxlife+RxCache
### [Rxjava ](./知识体系-理论-OOAD-Observer.md)
Rxjava 
工厂方法 构建Observer，通过Observable，Single，Maybe，Completable构建**io.reactivex.rxjava3.internal.operators包**内的 Observable 对象
模板模式
      public interface Observer<@NonNull T> { 
          void onSubscribe(@NonNull Disposable d); 
          void onNext(@NonNull T t); 
          void onError(@NonNull Throwable e); 
          void onComplete();

      }

      public interface Subscriber<T> { 
          public void onSubscribe(Subscription s); 
          public void onNext(T t); 
          public void onError(Throwable t); 
          public void onComplete();
      }

代理模式 onSubscribe()代理subscribeActual()方法，subscribeActual(Observer<? super T> observer)调用的时候，用装饰模式装饰当前的observer
装饰模式 Observer装饰下游 Observer的 subscribe() 方法，装饰调度器

RxJava2.0是非常好用的一个异步链式库,响应式编程，遵循观察者模式。
```
  001_Jersey              697fd66aae9beed107e13f49a741455f1d9d8dd9 Initial commit, working with Maven Central
* 002_rx                  87cfa7a445f7659ef46d1a6a4eb38daa46f5c97a Initial commit
  003_Observer_Observable 2a4122c11b95eaa3213c2c3e54a93d28b9231eec Rename to Observer/Observable
  004_refactoring         9d48f996e4ee55e89dc3c60d9dd7a8d644316140 Refactoring for consistent implementation approach.
                         Observable静态代理，Observer适配器模式，Func通过桥接Observer的具体实现，operations为Observable的子类
                        +----------------------------------------------------------------------------+ 
                        |AtomicObserverSingleThreaded AtomicObserverMultiThreaded                    |decorate pattern
                        |                      AtomicObserver                                        |state pattern
                        |  ToObservableIterable                                                      |
                        +----------------------------------------------------------------------------+ 
                        |Observable<T>           Observer<T>       Subscription                      |
                        |  subscribe(Observer<T>)   onCompleted()      unsubscribe()                 |
                        |                            onError()                        Func0<R>       |
                        |                            onNext(T)     Notification<T>     call():R      | 
                        +----------------------------------------------------------------------------+
                        |                     reactive                           functions operations| 
                        +----------------------------------------------------------------------------+
  005_languageAdapter     f57a242b17f1214142dea97c0cb9049b106378a0 LanguageAdaptor for Function execution
  006_readme              fbbac394fbc2e0af4ef3a507ad3e15dd18bfb10c Create README.md
  007_gh-pages            6797c4384d91a2567cf0b429ccbd8053c72b716f Create gh-pages branch via GitHub
  008_performace          787d8fc0215c5bab541f61c2d69a91753d462559 Refactoring towards performance improvements
                          gradle
  009_example             b61b7607e0f2b00a0d36672a95999f1fb081dbf1 Start of examples with clojure and groovy
  010_readme              10fe96474a6ab4ec62c7cab50fb376a173bda78e Create README.md
  012_0.1.2               74da0769266a8fd5832e558f1a6e0081895b9201 Gradle Release Plugin - pre tag commit:  'rxjava-0.1.2'.
  013_takeLast                      4c5c41364411e062d5d71f22ce311700d045821b TakeLast basic implementation
  014_RxJavaPlugins                 ee001549ef06f17f38139b4cfecd4ce4445ecb6d RxJavaErrorHandler Plugin
  015_operationNext                 d72c5892774a2c67327e07943f6b92416317871f Implemented Operation Next
  062_schedulers                    dfc784126f259361f01f1927f44f5d1aa4e49a43 Naive schedulers implementation
  063_RxJavaObservableExecutionHook c2a40bd1a391edf7f8b71965ca20fa84c72c0bb4 RxJavaObservableExecutionHook
  064_multicast                     0499cffcb53f928a5083f701603ccf3ec3c81c60 Multicast implemented
  065_sample                        1aa722d3379df88d05c9455d7630b7236edb9d9b Merge pull request #248 from jmhofer/sample
  066_throttle                      2ea065c0ef22ea7cf58e9fb6d6f24c69f365bed6 Created and wired an implementation for the throttle operation on Observables.
  067_AndroidSchedulers             3919547f1e5f7940974e383f4f573e48cac7e09b Expose main thread scheduler through AndroidSchedulers class
  068_window                        5789894638a62ac17b5276053e3bea8bdd570580 Merge window operator commit to master
  069_debound_throttleWithTimeout   5fabd5883561ff18b18b0d1dfb7001e2959cb11d Use 'debounce' as proper name for ThrottleWithTimeout which unfortunately is the poorly named Rx Throttle operator.
  070_ApachHttpAsyncClient          db2e08ca039c59d51349255c4b8b3c65b26d52de Observable API for Apache HttpAsyncClient 4.0
  071_AndroidObservables            715dcece5c781c394b59f86e32f1f514fc9f7a31 Drop use of WeakReferences and use a custom subscription instead
  072_backwards                     abff40fd0a40bee4f97b0363014e98aecb50d7ff Backwards compatible deprecated rx.concurrency classes
  073_single                        96064c37af520de375929ae8962c527dd869ad57 Implement the blocking/non-blocking single, singleOrDefault, first, firstOrDefault, last, lastOrDefault
  074_subscribeOn                   89bb9dbdf7e73c8238dc4a92c8281e8ca3a5ec53 Reimplement 'subscribeOn' using 'lift'
  075_observeOn                     9e2691729d94f00cde97efb0b39264c2f0c0b7f5 ObserveOn Merge from @akarnokd:OperatorRepeat2
  076_RxJavaSchedulers              d07d9367911d8ec3d0b65846c8707e0a41d1cf1f RxJavaSchedulers Plugin
  077_math-module                   68d40628a78db18ecb49d880798f1a03551ccd59 math-module
  078_operatorSerialize             4427d03db0d8ba67137654447c6c7a57615c0b00 OperatorSerialize
* 079_contrib-math                  26a4a1a05a6cc367061639fb19bfdda9d5b97fab Operators of contrib-math
  100_1.0.0-rc.1          6de88d2a39e2ae84744cd1f350e28bef4de7dacb Travis build image
                        observeOn 切换了消费者的线程，因此内部实现用队列存储事件。
                        +-----------------------------------------------------------------------+-------------------+
                        |AtomicObserverSingleThreaded AtomicObserverMultiThreaded               |                   |
                        |                      AtomicObserver                                   |                   |
                        |  ToObser^ableIterable                                                 |OperatorSubscribeOn|
                        +-----------------------------------------------------------------------+                   |
                        |ObservablevTv           ObservervTv       Subscription                 |OperatorObserveOn  |
                        |  subscribe(Observer<T>)    onCompleted()     unsubscribe()            |                   |
                        |  observeOn(Scheduler)      onError()                        Func0<R>  +-------------------+
                        |                            onNext(T)     Notification<T>     call():R |      schedulers   |
                        |                                                                       |                   |
                        +-------------------------------------------------------------------------------------------+
                        |                     reactive                                          |      plugins      |
                        +-----------------------------------------------------------------------+-------------------+
                        |                                         functions operations                              |
                        +-------------------------------------------------------------------------------------------+

  200_v2.0.0-RC1          fa565cb184d9d7d45c257afa1fbbec6ab488b1cf Update changes.md and readme.md
                        Disposable装饰Observer，使得Observer可以销毁。ObservableSource,CompletableSource， MaybeSource<T>,SingleSource<T>,FlowableProcessor。
                        io.reactivex.Flowable：发送0个N个的数据，支持Reactive-Streams和背压。Flowable.subscribe(4 args)
                        io.reactivex.Observable：发送0个N个的数据，Rx 2.0 中，不支持背压，Observable.subscribe(4 args)
                        io.reactivex.Single：只能发送单个数据或者一个错误，不支持背压。 Single.toCompletable()
                        io.reactivex.Completable：没有发送任何数据，但只处理 onComplete 和 onError 事件，不支持背压。 Completable.blockingGet()
                        io.reactivex.Maybe：能够发射0或者1个数据，要么成功，要么失败，不支持背压。Maybe.toSingle(T)
                        RxJavaPlugins 转化Obserable.
                        +----------------------------------------------------------------------------+---------------+-------------------+
                        |  FromArrayDisposable                                                       |               |                   |
                        |  ObservableFromArray                                                       |               |                   |
                        +----------------------------------------------------------------------------+               |                   |
                        |                             RxJavaPlugins                                  |               |                   |
                        |                                      apply(Function<T, R> f, T t)          |               |                   |
                        +----------------------------------------------------------------------------+               |                   |
                        |ObservablevTv:ObservableSource          ObservervTv           Disposable    |               |OperatorSubscribeOn|
                        |             subscribe(Observer<T>)         onCompleted()       dispose()   |               |                   |
                        |   subscribeWith(Observer<T>)               onError()           isDisposed()|Function<T, R> |OperatorObserveOn  |
                        |   subscribeActual(ObservervT>)             onNext(T)                       | apply(T):R    |                   |
                        |                                            onSubscribe(Disposable)         |               +-------------------+
                        |                                                                            +---------------+      schedulers   |
                        |                             RxJavaPlugins                                  |   functions   |                   |
                        +----------------------------------------------------------------------------+-----------------------------------+
                        |                                         functions operations                               |      plugins      |
                        +----------------------------------------------------------------------------------------------------------------+
                        |                     reactive-streams      jmh                                              |                   |
                        +--------------------------------------------------------------------------------------------+-------------------+

* 300_v3.0.0-RC0          fb37226be292c8ee0934311f8ca2f139dfd0dc5a 3.x: remove no-arg, dematerialize(); remove replay(Scheduler) variants (#6539)





[reactivestreams](https://github.com/reactive-streams/reactive-streams-jvm.git)
  001_scala           e58fed62249ad6fbd36467d1bbe5c486f31a8c0e Initial implementation
  020_java            6e5c63f3492524c21fda0bc1a08d72b52c9e9692 Reimplements the SPI and API in Java
  021_tck_java        3e67969207994b8e34ff49dffbf9fe3ac2d51284 !tck #12 Migrated TCK to plain Java
  022_api_spi_example f3a43863bd7c370a1f292a456eddcd1e9d226738 API/SPI Combination and Examples
* 101_gradle          dddbd3a52fcb7d71059a30760b5e77127c785c7c fix #96
                        +-------------------------------------------------------------------+--------+
                        |                                                                   |        |
                        |        Processor<T, R> :Subscriber<T>, Publisher<R>               |        |
                        +-------------------------------------------------------------------+        |
                        |                                                                   |        |
                        |  Publisher<T>                Subscriber<T>          Subscription  |        |
                        |     subscribe(Subscriber<T>)    onSubscribe()           cancel()  |        |
                        |                                 onNext(T)                         |        |
                        |                                 onComplete()                      |        |
                        |                                 onError(Throwable)                |        |
                        +-------------------------------------------------------------------+        |
                        |                       api                                         |  tck   |
                        +-------------------------------------------------------------------+--------+

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

背压策略：即生产者的速度大于消费者的速度带来的问题，比如在Android中常见的点击事件，点击过快则经常会造成点击两次的效果。
Flowable背压策略
```
onBackpressureBuffer()
onBackpressureDrop()
onBackpressureLatest() 

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



                    RxThreadFactory :ThreadFactory
                         Thread newThread()


 //RxComputationThreadPool          //RxSchedulerPurge
ComputationScheduler :Scheduler     SchedulerPoolFactory


 //RxCachedThreadScheduler
 //RxCachedWorkerPoolEvictor
 IoScheduler :Scheduler
                                                    CachedWorkerPool                                
 //RxNewThreadScheduler                                   evictorService:ScheduledExecutorService
 NewThreadScheduler :Scheduler                            allWorkers:CompositeDisposable
                                                          createWorker():Worker
 //RxSingleScheduler                                      expiringWorkerQueue:ConcurrentLinkedQueue<ThreadWorker> 
 SingleScheduler :Scheduler                         ThreadWorker: NewThreadWorker

                                                    EventLoopWorker:Scheduler.Worker//访问者模式，访问threadworker
                                                          threadWorker:ThreadWorker
                                                          schedule(run:Runnable):Disposable

                                                    ScheduledRunnable :AtomicReferenceArray,Runnable


FlowableSubscribeOn
       scheduler:Scheduler
```
线程间切换



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