# 设计原则
> [](https://java-design-patterns.com/principles)
<<Design Patterns>> <<Enterprise Integration Patterns>> <<Code Complete>> <<POSA>>

1. 三大通用原则
KISS （高效化）
DRY 不要写重复的代码

YAGNI 
  (关注分离，面向维护者代码，避免过早优化）
[Boy-Scout Rule（童子军规则）:何一个成员都有义务去改善代码](《Bad Code, Craftsmanship, Engineering, and Certification》)



2. 模块内部与模块间依赖
低耦合（模块间）高内聚（模块内）

良性依赖原则（dagger/rxjava 《拥抱变化：敏捷设计从理论到实践》，不同项目，减少迁移代码）
  
  分层
    MVC MVP（依赖抽象view方法接口，变异移植） MVI（回调用状态表示，view只负责更新）
    LoD（迪米特法则 模块间，尽量只依赖必要的接口，不同项目平台有多种实现）
      IoC（模块间，OOAD控制反转[依赖注入 ， “依赖查找”]，配置大于反射） 
      CARP（模块间，属OOAD，合成/聚合）
  非对称分层
    CQRS  命令-查询分离原则（content provider）
SOLID（模块内，属OOAD，单一功能、开闭原则、里氏替换、接口隔离以及依赖反转）
  设计模式
# 模式
[](https://en.wikipedia.org/wiki/Software_design_pattern)
## 面向对象（GOF23，GRASP）
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
## 单例
- 双检锁(synchronized) + volatile
- 静态内部类
    classloader 机制来保证初始化 instance 时只有一个线程。
- 枚举
  自动支持序列化机制。不能通过 reflection attack 来调用私有构造方法。

## OOAD IDEF4：面向对象设计(Object-Oriented Design)

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


## Android 设计模式
工厂方法 AppComponentFactory 创建Application，Activity等
C/S模式 binder