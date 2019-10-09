
## 封装性与内聚耦合

>《UML面向对象程序设计基础》

封装级别：
0级，代码行
1级，函数
2级，类
3级，包
4级，模块

```
编写代码功能，处理耦合问题（语言，性能，内存/代码数量，可拓展 ），产生内聚现象，重用代码，又出现耦合问题，反复处理。
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

工厂单例构造原型，组合适配桥接 代理享元装饰外观,命令中介观察访问备忘  解释策略状态 责任迭代模板 
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