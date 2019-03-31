


>《UML面向对象程序设计基础》

封装级别：
0级，代码行
1级，函数
2级，类
3级，包
4级，模块

```
                                             object+oriented
                                                    ^
                                                    |
                                                    +                  aggregation
                                                 abstract +----> Type --------> inheritance +----> polymorphism
                                                    ^
                                                    |
               +------------+  +-----------+   +----+----+   +----------+   +----------+
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
                                              |           |            association
                                              v           v            aggregation
                                      class cohension    connascence   composition            +------------------+
                                           domain        encumbrance   dependencies           |     quality      |
                                          extrinsic                                           +------------------+
                                               +                                              | Robustness       |
                                               |                                              | reliable         |
                                               |         how to reuse                         | Extensibility    |
                                               +------------------------------------------->  | reusability      |
                                                                                              | maintainability  |
                                                                                              +------------------+


[职责分配原则](《Applying UML and Patterns》)
+---------+-----------+--------------------------------+-----------+------------------------------------------+-------------------+
|         |           |                                |           |                                          |                   |
|         |           | Dependency Inversion Principle |           |                                          |                   |
|         |           |   depend on abstractions       |           |                                          |                   |
|         |           |   not on concretions           |           |                                          |                   |
|         |           +---------------------------------------------------------------------------------------+                   |
|         |           | Liskov substitution principle  |           |                                          |                   |
|         | protected |   every subclass/derived class |           |                                          |                   |
|         | variations|   should be substitutable      |           |                                          |                   |
|   GRASP |           |   for their base/parent class  |           |                                          |                   |
|         |           +---------------------------------------------------------------------------------------+                   |
|         |           |  Open-closed Principle         | S.R.P     | Interface segregation principle          |                   |
|         |           |   open for extension,          |           |   never be forced to implement           |   LoD/PLK         |
|         |           |   but closed for modification  | for short |   an interface that it doesn't use;      |   loose coupling  |
|         |           |                                |           |   or shouldn't be forced to depend on    |                   |
|         |           |                                |           |   methods they do not use                |   CARP reusablity |
|         +-----------+--------+-----------------------+-----------+------------------------------------------+                   |
|         |creator,            |                                                                              |                   |
|         |controller,         |                                                                              |                   |
|         |indirection,        |          23 design model                                                     |                   |
|         |information expert, |                                                                              |                   |
|         |high cohesion,      |                                                                              |                   |
|         |low coupling,       |                                                                              |                   |
|         |polymorphism,       |                                                                              |                   |
|         |pure fabrication    |                                                                              |                   |
+------------------------------+------------------------------------------------------------------------------+-------------------+


```

内聚：度量一个给定的**程序**内多行代码的单一功能性，以确定是否达到该程序所实现的目的
耦合性：度量**程序**之间联系的次数和强度
共生性：软件中一组相互依赖关系。（二级封装-类，带来复杂的相互依赖关系）

依附集：测量一个类的所有附属物的方法，衡量类复杂度的方法
类的内聚：衡量该类的特性（操作运算符和属性）属于一个单一整体的完善程度。


抽象:所带来的问题（个性与共性问题，对象类型层次）