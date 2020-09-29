# Android 高级工程师知识体系
程序：一些列数据结构和指令（《计算机程序设计艺术》）
软件：计算机系统中程序和文档的总称。
文档：需求开发计划（排期）及进度，设计说明书（架构图，项目结构图，类图），规范文档（命名，注释），功能预演文档，开发总结文档，用户指南(使用手册)
[java 规范文档](https://docs.oracle.com/javase/specs/index.html)
```

hotspot,jdk,corba,jaxp,jaxws,langtools(javac,javadoc,javap)
+--------------+------------------+----------------------------------------------------------------------------+
|                 java(oop)->kotlin(oop+minor fp)->scala(oop+fp)                                               |
+--------------+------------------+----------------------------------------------------------------------------+
|              |   battery        simplify     elegant        security                                         |
|              +-----------------------------------------------------------------------------------------------+
|   app        |  perfomance      | memory(leaks,chunk)                                                        |
|              +-----------------------------------------------------------------------------------------------+
|              |  test/stable     |                                                                            +
+--------------------------------------------------------------------------------------------------------------+
|              |  lang(ooad)        util(dsa)      io/nio    net       sql   math/text  rmi/security   ui      |
|              +-----------------------------------------------------------------------------------------------+
|  API /SDK    |      String           List      Serializable                 BigInteger     C&C++    awt      |
|              | Thread/ThreadLocal  concurrent    File       Socket     db   DecimalFormat                    |
|              |  Reflect/Annotation   regex                                                                   |
|              |  (InvocationHandler)                                                                          |
+--------------+----------------------------+------------+---------+-----------+-------+-----------------------+
|Class-based|concurrency|Aspect|            |            |         |           |       |                       |
| Log/Date  |           |      |            |            |         |           |       |                       |
+-----------+-----------+------+------+     |            |         |Annotation |       |                       |
|        oop                          | pop | Functional | FRP     |Reflective |Generic|                       |
+------------------------------+------+------------------+-----------------------------+ Event+driven          |
|            Structured        | Imperative |  Declarative         |  Metaprogramming  |                       |
+------------------------------+------------+----------------------+-------------------+-----------------------+
|                             conditional/decision-making/loops                                                |
+-------+------------------------+-------------+-------------+------------+-------------------------+----------+
|       |                        |   reserved| |             |            |                         |          |
|       |                        |Declaration| |             |            |                         |          |
|       |true ,10b(2) ,1f(float) | Modifier  | |             |            |                         |          |
|       |false, 01(8) ,1d(double)|   decision| |             |            |  Whitespace(tab space)  |          |
| memory|        1(10),""(Str)   | Expression| |             |            |  comment                | other    |
|       |      0x1(16),[](Arr)   |           | |             |            |  separator sign(;)      | symbol/  |
|       |escape char   ADT       | DataType  | |             |            |                         | token    |
+--------------------------------------------+-+             |            +-------------------------+ token    |
|  Literals(num)                 |  keywords   | Identifiers |  Operators |     separator           |          |
+--------------------------------+-------------+-------------+------------+-------------------------+----------+
|                                Byte-Value /CharsSet-msg (Unicode,UTF-8)                                      |
+--------------------------------------------------------------------------------------------------------------+

identifiers（标识符）："对象"的名字( a name of  a unique object )

```


## 2 Java 基础
[Java 8 规范文档](https://docs.oracle.com/javase/8/docs/)
[Java Conceptual Diagram](https://docs.oracle.com/javase/8/docs/index.html)
### 特性
[程序通常对每一个值关系一个特定的类型。](https://zh.wikipedia.org/wiki/類型系統)
数据类型,一个数值的类型;类别,一个对象的类型;种类,一个类型的类型
```
+---------------------------+--------------------+------------------------------+
|   type|    data type      |    class           |  kind                        |
+-------------------------------------------------------------------------------+
|  value|  a type of a value| a type of an object| a type of a type, or metatype|
|       |                   |                    |                              |
+-------+-------------------+--------------------+------------------------------+

```
x编译型语言/x解释型语言/√混合型语言	
x动态语言/√静态语言	
√静态/√动态类型(java 10 auto)	
√强类型/x弱类型
[](https://www.cnblogs.com/zy1987/p/3784753.html?utm_source=tuicool)
### 语法
分隔符，字面量（字符串值），关键字与操作符、标识符（Identifiers 变量与常量等）、或者是一个符号

分隔符有空白符、注释和普通分隔符三种
    空白符：空格符(Space)、制表符(Tab)、和回车(Enter)、换行 
    注释:// 进行单行注释；/* */多行注释 
    普通分隔包括分号（;），逗号（,）、圆点（.）、花括号（{}）、方括号（[]）、圆括号（()）、冒号（:）

标识符是用来为程序中的类、方法、变量命名的符号。

```java
keyword(53)

+-----------+-----------------------------------------------------------------------+
|  special  |                                                                       |
|identifiers|  var(Java 10)                                                         |
+-----------+-----------------------------------------------------------------------+
|  reserved |  goto    const                                                        |
+-----------------------------------------------------------------------------------+
|  exception|  try    catch    finally                                              |
|  debug    |  throw    throws                                                      |
|           |  assert                                                               |
+-----------------------------------------------------------------------------------+
|           |                 |  access modifiers  | public protected    private    |
|           |  Encapsulation  +--------------------+--------------------------------+                
|           |                 |                   package import                    |
|  OOPs     |-----------------------------------------------------------------------|
|           |  interface  abstract                                                  |
|           |  super  instanceof   final                                            |
|           |  extends   implements                                                 |
| modifiers |  class  enum                                                          |
|           |  new    this(reference variable)   static                             +
+-----------------------------------------------------------------------------------+
|  variable |  volatile transient                                                   |
|  method   |  void native  strictfp                                                |
|  modifiers|  synchronized                                                         |
+-----------+-----------------------------------------------------------------------+
|  Flow     |  if     else                                                          |
|  control  |  do    while    for                                                   |
|  keyword  |  switch    case    default                                            |
|           |  break    continue    return                                          |
+-----------------------------------------------------------------------------------+
| Primitive |                                                                       |
| types     |  byte    short     int    long    float    double    boolean    char  |
| keyword   |                                                                       |
+-----------------------------------------------------------------------------------+
|           |                                                                       |
| literals  |  true false null                                                      |
+-----------+-----------------------------------------------------------------------+

```

``` Operators
+-------------+--------------------------------------------------------------+
|  Misc       |  (? :)  instanceof  ..(range operator)                       |
|             |                                                              |
+----------------------------------------------------------------------------+
|  Assignment |  = += -= *= /= %=   < < =   > > =   &=   ^=   |=             |
+----------------------------------------------------------------------------+
|  Logical    |  && || !                                                     |
+----------------------------------------------------------------------------+
|  Relational |  == != > < >= <=                                             |
+----------------------------------------------------------------------------+
|  Bitwise    |  & | ^  ~    < <   > >   > > >                               |
+----------------------------------------------------------------------------+
|  Arithmetic |  + - * / % ++ --                                             |
+-------------+--------------------------------------------------------------+


```
> 《Core Java》（延展阅读《C Primer Plus》《C++ Primer》）
-- 基本语法

```
                                               +   byte
                                               |
                                               |   short
                                               |
                                  +IntegerType |   int
                                  |            |
                                  |            +   long
                                  |
                         +  Number|
                         |  Type  |
                         |        |            ++   Float
                         |        +DecimalType |
                         |                     ++   Long
                         |
                         |
                         |
                         |
            ++primitive  ++ char 
            | Typeifine  |
            |            |
            |            |
            |            |
Java        |            ++ boolean
data type   |
            |
            |
            |            +  class/Enum-class
            |            |
            | reference  |  interface/Annotation interface
            ++Type       |
                         +  Array

```
### 数据 - 基本数据类型
- Short
Short.MIN_VALUE ~ Short.MAX_VALUE
=-2^15~（2^15）-1
=-32768~-32767（计数单位约3万）

Android 资源65534问题

- Integer
Integer.MIN_VALUE ~ Integer.MAX_VALUE
=-2^31~（2^31）-1
=-2_147_483_648 ~ 2_147_483_647(计数单位约等于21亿)

- Long
Long.MIN_VALUE ~ Long.MAX_VALUE
==-2^63~（2^63）-1
=-9_223_372_036_854_775_808L~-9_223_372_036_854_775_807L
（计数单位约等于922京）


- Float （遵循IEEE-754格式标准）

```
第一步：[176.0625换成二进制数](https://blog.csdn.net/k346k346/article/details/50487127)，
        整数部分采用"除2取余，逆序排列"法：
        小数点前:176 / 2 = 88  
        余数为 088 / 2=44 余数为 0                             
        44 / 2 =22    余数为 0                       
        22 / 2= 11    余数为 0                              
        11 / 2 =5     余数为 1        
        5 / 2=2       余数为 1                             
        2/ 2  =1      余数为 0                                                                             
        1/ 2=0        余数为 1    商为0，结束。                                                                       
        小数点前整数转换为二进制:10110000    
        ---------------------  
        
        小数部分采用 "乘2取整，顺序排列"法部分：
        0.0625 * 2 = 0.125   整数为0                 
        0.125 * 2 = 0.25     整数为0             
        0.25* 2 = 0.50       整数为0             
        0.5* 2 = 1.0         整数为1，小数部分为0,结束
        小数点后整数转换为二进制:0001 

        得到二进制位：10110000.0001

第二步：在换算成内存格式（IEEE-754格式标准）  SEEEEEEE    EMMMMMMM    MMMMMMMM    MMMMMMMM
      S 0正数，1负数
      E 第一位（1：大于1的十进制，0：0~1之间的十进制），第2到8位（减法后的值： [占1位；小数点移位至到个位1.若右移0，否则1][占7位，小数点移位的二进制] −1）
      M 小数点移位到个位1后，截取后面23二进制
      即 0 10000110 0110000 00010000 00000000

```
-Float.MAX_VALUE ~ Float.MAX_VALUE
=[-3.40282346638528860e+38 , -1.40129846432481707e-45] ∪ [1.40129846432481707e-45 ~ 3.40282346638528860e+38]
正负是对称的，看下正数部分
Float.MIN_VALUE ~ Float.MAX_VALUE
=2^(-126) ~~ 2(1-2^(-24)) * 2^(2^7-1)
=2^(-126) ~~ 2(1-2^(-24)) * 2^(127)
=1.1754943508223e-38 ~ 3.4e+38
= （已知e-24位 涅槃寂静，暂时找不到可以衡量的计数单位） ~ 3.4*(10^38)(计数单位约等于34涧)

- Double
内存里的存储结构（IEEE-754格式标准）：SEEE EEEE EEEE MMMM MMMM MMMM MMMM MMMM MMMM MMMM MMMM MMMM MMMM MMMM MMMM MMMM
正数范围：
2^(-1022-52) ~~ 2-2^-52)*(2^((2^10)-1))

- Char
  jdk 9采用压缩字符串，Iso-8891，使用一个字节，否则用 UTF-16 编码。

- Boolean
- Byte

### 指令 - 运算符（算术，位运算，赋值，比较，逻辑）
Byte通过加法实现加减，移位和加法实现乘除法

- 原码，反码，补码
  正数：原码，反码，补码一致
  负数：反码符号位不变其他位按位取反，补码为反码加1（取反加一，两个过程符号位都不变）
```
或者换成时钟，0，1，2，3，4，5，-6，-1，-2，-3，-4，-5
3bit有符号二进制
+--------+-----------+-----------+---------+--------+---------+----------+---------+--------+
|        |           |           |         |        |         |          |         |        |
| Decimal|  -3       |    -2     |   -1    |  -4    |   3     | 2        | 1       |    0   |
+-------------------------------------------------------------------------------------------+
|        |           |           |         |        |         |          |         |        |
| 原码    |  111      |    110    |  101    |  100   |  011    |  010     |  001    | 000    |
+--------+-----------+-----------+---------+--------+---------+----------+---------+--------+

负数反码，相当与在负数范围换下位置。 
+--------+-----------+-----------+---------+--------+---------+----------+---------+--------+
|        |           |           |         |        |         |          |         |        |
| Decimal|   -4      |    -1     |   -2    |  -3    |   3     | 2        | 1       |    0   |
+-------------------------------------------------------------------------------------------+
|        |           |           |         |        |         |          |         |        |
| 反码    |  111      |    110    |  101    |  100   |  011    |  010     |  001    | 000    |
+--------+-----------+-----------+---------+--------+---------+----------+---------+--------+
 
负数补码，整个数变得有序
+--------+-----------+-----------+---------+--------+---------+----------+---------+--------+
|        |           |           |         |        |         |          |         |        |
| Decimal|   -1      |    -2     |   -3    |  -4    |   3     | 2        | 1       |    0   |
+-------------------------------------------------------------------------------------------+
|        |           |           |         |        |         |          |         |        |
| 反码    |  111      |    110    |  101    |  100   |  011    |  010     |  001    | 000    |
+--------+-----------+-----------+---------+--------+---------+----------+---------+--------+


```
  原码：加负数，不是预期值（1-1= 00000001^1000001（原码）=10000010=-2）
  反码：正数原码加负数的原码，计算的结果不是想要的值（1-1= 00000001（反）^11111110（反码）=11111111（反）=10000000（原））。
  补码：正数加负数的反码，符号位不对，用补码可以正确（1-1= 00000001（补）^11111111(补码)=00000000=0）
- [补码原理：同余](https://www.cnblogs.com/baiqiantao/p/7442907.html)
  负数取模：A mod b= A-B*Math.floor（A/B）
  
  1. 反码，实际上是这个数对于一个膜的同余数；而这个膜并不是我们的二进制，而是所能表示的最大值
  2. 反码的基础上+1，只是相当于增加了膜的值
```
byte （byte范围 -128~127）取反求值，相当于值 (a+b) mod 127
    取补求值，相当于（a+b） mod 128，保证不会溢出
```

- 位运算 **& | ~ ^**
 

### 数据 - 大数操作
只要你的计算机的内存足够大，可以有无限位的。

常见构造大数方法：指数，阶乘，高德纳箭头，葛立恒数和Tree(3)
3^3=3

- BigInteger、BigDecimal
[-2^(2147483647*32-1) ，2^(2147483647*32-1)-1]


## 面向过程（命令式编程）
方法（函数）
## 数据 -   基于类的面向对象（命令式编程）



### 数据类结构 - 类实现面向对象与设计模式
三特性， 基于内聚与藕合(cohesion & coupling)五大基本原则(SOLID )：单一职责原则（接口隔离原则），开放封闭原则，Liskov替换原则，依赖倒置原则，良性依赖原则
23设计模式
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
 #### 性能（异常，断言，日志）
 《Efficiency Java》
[**JMH** 即Java Microbenchmark Harness](http://openjdk.java.net/projects/code-tools/jmh/)
[java memory leaks](https://www.baeldung.com/java-memory-leaks)
```
+-----------------------------------------------------+
| memory leaks                                        |
+--------------------+--------------------------------+
| unrelease          | improper api                   |
+-----------------------------------------------------+
| static Fields      | Improper equals()              |
| (Collection,SingleIns)| and hashCode() Implementations |
|                    |                                |
| Inner Classes      |                                |
| That Reference     |                                |
| Outer Classes      | finalize() Methods             |
|                    |                                |
| ThreadLocals       | Interned Strings               |
|                    |                                |
| Unclosed Resources |                                |
+--------------------+--------------------------------+


```

## 声明式编程-函数式编程/响应式编程（java 1.8）
函数是一等公民
1. 函数接口/SAM接口 **@FunctionalInterface**
```java
@FunctionalInterface 注释的约束：

1、接口有且只能有个一个抽象方法，只有方法定义，没有方法体

2、在接口中覆写Object类中的public方法，不算是函数式接口的方法。


+----------------+---------+------------
|                | param   | return    |
+--------------------------------------+
|Function<T,R>   |   T     |  R        |功能型函数式接口
+--------------------------------------+
|UnaryOperator<T>|   T     |  T        |继承Function
+--------------------------------------+
|Predicate<T>    |   T     |  boolean  |断言型函数式接口
+--------------------------------------+
|Consumer<T>     |   T     |  X        |消费型函数式接口
+--------------------------------------+
|Supplier<R>     |   x     |  R        |供给型函数式接口
+----------------+---------+-----------+
```
1. 高阶函数

2. 文件读写
#### **Lambda 表达式**
lambda表达式实例化函数接口，创建该接口的对象
```java
(参数) -> 表达式
```
- 可选参数类型定义
- 单个参数定义，可选圆括号
- 单个编码语句，可选大括号
- 可选**returan**关键字


**闭包**的定义是: 引用了自由变量的函数。

1. 增强接口方法
- Default Methods。List/Collection interface can have a default implementation of forEach method
- 静态方法

处理以上方法可以用，方法引用符号 **::**引用。构造方法也可以

### 函数式编程

#### **java 8 Stream API** 详解 (Map-reduce、Collectors收集器、并行流)
```
+------------------------------------------------------------+
|  Pipelining       Automatic iterations                     |
+-------------+----------------------------------------------+
|  Aggregate  |  filter, map, limit, reduce, find, match     |
|  operations |                                              |
+------------------------------------------------------------+
|  Source     |Collections, Arrays,     //List.stream()      |
|             |or I/O resources as input source //stream.of()|
+-------------+----------------------------------------------+
| Sequence of elements                                       |
+------------------------------------------------------------+

```
构造Stream对象
```java
stream()
parallelStream()
```
#### 应式编程（FRP）与Optional
> Java 8中引入的Streams API非常适合处理数据流（map，reduce和所有变体），但Flow API会在通信方面（请求，减速，丢弃，阻塞等）发挥作用。[https://www.jdon.com/50854](https://www.jdon.com/50854)

响应式编程是一种面向数据流和变化传播的编程范式；Reactive Streams主要解决背压（back-pressure）问题。当传入的任务速率大于系统处理能力时，数据处理将会对未处理数据产生一个缓冲区。
- JDK 9 Flow API，响应式流(Reactive Streams)类**Flow**和SubmissionPublisher<T>
Java 9 为 Stream 新增了几个方法：dropWhile、takeWhile、ofNullable
- Reactive Streams由4个Java接口定义
```
Publisher<T>
Subscriber<T>
Subscription
Processor<T,R>
```
java 9 Optional 可以直接转为 stream

Stream,Optional,CompletableFuture
Rxjava实现



#### CompletableFuture


## 2.4 数据（吞吐量）并发访问 - 线程与并发
[知识体系-程序-java-throughput.md](知识体系-程序-java-throughput.md)

## 2.3 数据访问 - 流（IO, NIO）
字符串，类，文件（xml,json），内存，网络，数据库



 
### 数据展示 - 图形化及用户组件
awt/swing

#### 多媒体
[图形化](知识体系-多媒体.md)





## 4 算法与数据结构
《Intruduce arthrigsim》
KNUTH -《The Art of Computer Programming》基本算法，排序与搜索，半数值计算，组合算法（枚举与回溯-图论-最优化与递归），造句算法
- 线性表
- 栈和队列
- 树
- 图
- 散列查找
- 排序
- 海量数据
- 堆
  PriorityQueue
- 线性同余随机算法
- HashMap hash冲突链表的红黑树平衡算法
- 二叉查找树
  1. AVL平衡树
    1.1 其根的左右子树高度之差的绝对值不能超过1

  2. 红黑平衡树
    2.1 每一个节点不是红色的就是黑色的
    2.2. 根总是黑色的
    2.3. 如果节点是红色的，则它的子节点必须是黑色的（反之不一定成立）
    2.4. 从根到叶节点或空子节点的每条路径，必须包含相同数目的黑色节点。



## 数据传输 - 计算机网络
TCP使用内存
```
cat /proc/sys/net/ipv4/tcp_mem

sysctl -a

sudo tcpdump
```

网络数据：报文格式
- Socket
### 编码
Java 8 Base64
使用64个字符（2^6）编码内容
```
Simple： A-Za-z0-9+/（.用来填充）

URL： A-Za-z0-9+_（.用来填充）

MIME： A-Za-z0-9+/（=用来填充）
```

```java

 +--+
             4 encode char uint
+--------------------------------------------------------+
        Byte1               Byte2             Byte3
+-----------------+  +----------------+ +----------------+
```


### TCP/IP
 TCP协议是一种面向连接的、可靠的、基于字节流的运输层通信协议。TCP是全双工模式，这就意味着，当主机1发出FIN报文段时，只是表示主机1已经没有数据要发送了，主机1告诉主机2，它的数据已经全部发送完毕了

[TCP Header Format](https://tools.ietf.org/html/rfc793)
```
    0                   1                   2                   3
    0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1
   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
   |          Source Port          |       Destination Port        |
   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
   |                        Sequence Number                        |
   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
   |                    Acknowledgment Number                      |
   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
   |  Data |           |U|A|P|R|S|F|                               |
   | Offset| Reserved  |R|C|S|S|Y|I|            Window             |
   |       |           |G|K|H|T|N|N|                               |
   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
   |           Checksum            |         Urgent Pointer        |
   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
   |                    Options                    |    Padding    |
   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
   |                             data                              |
   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+

                            TCP Header Format


```
TCP层的Flag
```
SYN表示建立连接，
FIN表示关闭连接，
ACK表示响应，
PSH表示有 DATA数据传输，
RST表示连接重置。
URG(urgent紧急)

```

```
+-------------+
|  Http data  |        App layer        应 用 层
+-------------+



+--------------+
|  TCP Header  |
|              |
|    Http Data |
|              |       Transport layer  传 输 层
+--------------+



+---------------+
| IP Header     |
|               |
|   TCP Header  |
|               |      Network layer    网 络 层
|     Http Data |
|               |
+---------------+

+-------------------+
| Eth Header        |
|                   |
|   IP Header       |  Data link layer  链 路 层
|                   |
|     TCP Header    |
|                   |
|       Http Data   |
+-------------------+

```
TCP断开连接时，会有四次挥手过程。

```ascii
三次握手四次挥手
    +-----------+                                    +-------------+                +------------+                               +--------------+
    |           |                                    |             |                |            |                               |              |
    |  Client   |                                    |   Ser^er    |                |   Client   |                               |    Ser^er    |
    |           |                                    |             |                |            |                               |              |
    +-----+-----+                                    +-------+-----+                +-----+------+                               +-------+------+
Close     |                                                  |  Close                     |                                              |
          |            SYN=1 Seq=X                           |                            |             FIN=1 ACK=Z   Seq=X              |
          |   +------------------------------------------->  |                            |  +---------------------------------------->  |
          |                                                  |                 FIN        |                                              |
          |                                                  |                 WAIT-1     |             ACK=X+1 Seq=Z                    |
          |                                                  |  Listen                    | <---------------------------------------+    |
          |                                                  |                            |                                              |
          |                                                  |                 FIN        |                                              | CLOSE_WAIT
          |            SYN=1 ACK=X+1 Seq=Y                   |                 WAIT-2     |             FIN=1 ACK=X Seq=Y                |
          |   <------------------------------------------+   |                            |                                              |
          |                                                  |                            |  <--------------------------------------+    |
          |                                                  |                            |                                              |
          |                                                  |                            |                                              | CLOSE_WAIT
SYN_SENT  |                                                  |                            |                                              |
          |                                                  |                            |                                              |
          |            ACK=Y+1 Seq=Z                         |                            |             ACK=Y Seq=X                      |
          |    +------------------------------------------>  |  SYN_RCVN                  |  +-------------------------------------->    | LAST_ACK
          |                                                  |                TIME WAIT   |                                              |
          |                                                  |                            |                                              |
          |                                                  |                            |                                              |
          |                                                  |                            |                                              |
          |                                                  |                            |                                              |
          |                                                  |                            |   2MSL                                       |
          |                                                  |                            |                                              |
          |                                                  |                            |                                              |
          |                                                  |                            |                                              |
          +                                                  +                            +                                              +
     ESTABLISHED                                          ESTABLISHED                  Closed                                         Closed

```


### XMPP/MQTT
见[知识总结-im篇](./知识体系-im.md)
### RPC




## 7 操作系统
参考 Brian Ward,《How Linux works - what every superuser should know》[M].No Starch Press(2014)

7.1 用户进程
7.2 内核（进程，内存，设备驱动，系统调用 System Call）
7.3 硬件（CPU，内存，磁盘，网络端口 ）

32位系统的最大寻址空间是2 的32次方= 4294967296（bit）= 4（GB）左右

## 8 数据库与SQL
三大范式

事务 ACID

```
JDBC
+-----------------------------------------------------+
|                                                     |
|                                                     |
|                                                     |
+-----------+--------------------+--------------------+
| Statement |  PreparedStatement | CallableStatement  |
+-----------+--------------------+--------------------+
|                 Connection                          |
+-----------------------------------------------------+

```

### Sql
基本操作：增删改查
前缀索引
```
create database <dbName>
```

```
create table <tableName>(
  <column0> <columnType0>,
  <column1> <column1Type>
  )
```

```
drop table <tableName>
```
```
alert table <tableName> add <columnName> <columnType>
```

```
insert into <tableName> (
  <colunm0>,
  <column1>)
  values (
    <column0Value>,
    <column1Value>
  )

```
```
delete from <tableName> where <columnName>=<columnNewValue>
```
```
update <tableName> set <columnName>=<columnNewValue> where <column1Name>=<column1Value>
```
```
SELECT column_name [, column_name ]
FROM   table1 [, table2 ]
WHERE  column_name OPERATOR
      (SELECT column_name [, column_name ]
      FROM table1 [, table2 ]
      [WHERE])

SELECT * FROM student,(
    SELECT sno FROM SC WHERE cno=1//派生查询
) AS tempSC
WHERE student.sno = tempSC.sno
```
模糊查询，分组查询，having查询

- 多表查询（内连接，外连接）
- 嵌套查询
  与IN、ALL、ANY、EXISTS配合使用。
- 派生表查询

- 集合查询
  UNION、UNION ALL、INTERSECT、EXCEPT
- 事务
```
begin;
<insert,delete,update,select>
rollback;/commit;
```
### sqlite 数据类型

```
[Architecture of SQLite](https://sqlite.org/arch.html)


 +--------------------------------------------+       +-------------------------------
 |  Core                                      |       |  SQL Compiler                |
 |                                            |       |                              |
 |            +------------------+            |       |       +------------------+   |
 |            |  Interface       |            |       |  +->  |   Tokenizer      |   |
 |            +-------+----------+            |       |  |    +------+-----------+   |
 |                    |                       |       |  |           |               |
 |                    v                       |       |  |           v               |
 |                                            |       |  |                           |
 |            +---------------------------+ +------------+    +-------------------   |
 |            |  SQL Command Processor    |   |       |       |   Parser         |   |
 |            +-------+-------------------+ <------------+    +------+-----------+   |
 |                    |                       |       |  |           |               |
 |                    v                       |       |  |           v               |
 |                                            |       |  |                           |
 |            +-------------------+           |       |  |    +------------------+   |
 |            |  Virtual Machine  |           |       |  +----+  Code Generator  |   |
 |            +-------+-----------+           |       |       +------------------+   |
 |                    |                       |       |                              |
 +--------------------------------------------+       +-------------------------------
                      |
                      |
 +--------------------------------------------+      +-------------------------------+
 |                    v                       |      |                               |
 |  Backhand                                  |      |   Accessories                 |
 |            +----------------------+        |      |                               |
 |            |                      |        |      |                               |
 |            |  B-Tree              |        |      |        +---------------+      |
 |            |                      |        |      |        |  Utilities    |      |
 |            +-------+--------------+        |      |        +----------------      |
 |                    |                       |      |                               |
 |                    v                       |      |                               |
 |            +----------------------+        |      |        +---------------+      |
 |            |  Pager               |        |      |        |  Test Code    |      |
 |            +-------+--------------+        |      |        +----------------      |
 |                    |                       |      |                               |
 |                    v                       |      |                               |
 |            +----------------------+        |      |                               |
 |            |  OS Interface        |        |      |                               |
 |            +----------------------+        |      |                               |
 +--------------------------------------------+      +-------------------------------+

```
 常用数据类型及**亲和类型**
```sql
NULL 值是一个 NULL 值。
INTEGER 值是一个带符号的整数，根据值的大小存储在 1、2、3、4、6 或 8 字节中。
REAL 值是一个浮点值，存储为 8 字节的 IEEE 浮点数字。
TEXT 值是一个文本字符串，使用数据库编码（UTF-8、UTF-16BE 或 UTF-16LE）存储。
BLOB 值是一个 blob 数据，完全根据它的输入存储。
```
B-tree和page cache共同对数据进行管理。
 

## OpenJDK
```
  070001_initial 686d76f7721f9e5f4ccccf0f4e7147bd3ba5da6f Initial load
  071201_hotspot 8153779ad32d1e8ddd37ced826c76c7aafc61894 Initial load
                +-------------------------------------------------------------------------------------+-----------------------------------------+
                |  java.c                                                                             | classLoader                  Class      |
                |    options:JavaVMOption*                        java_md.c                           |      loadClass()               forName()|
                |    main()                                                                           |      findClass()                        |
                |    SelectVersion(char **main_class)               LocateJRE():char*                 |      findBootstrapClassOrNull()         |
                |    ParseArguments():jboolean                      LoadJavaVM()//dlsym()             |                                         |
                |    InitializeJVM( vm, env, ifn)                                                     |                 Constructor             |
                |    GetMainClassName():jstring                                                       |                    newInstance()        |
                |    LoadClass(JNIEnv *env, char *name):jclass                                        |                    constructorAccessor  |
                |    CreateExecutionEnvironment()                                                     |                                         |
                |                                                                                     |  jvm.cpp                                |
                |jni.cpp                    Threads              vmSymbols.hpp                        |     JVM_NewInstance(cls)                |
                |  jni_invoke_static()          create_vm()                                           |     JVM_LoadClass0                      |
                |  JNI_CreateJavaVM()           add()                                                 |     find_class_from_class_loader()      |
                |  get_method_id()                                                                    |     thread_entry()                      |
                |  jni_FindClass()         JavaThread:Thread   VMThread :Thread     Universe          |                                         |
                |  jni_DefineClass()           _osthread         _vm_queue           universe_init()  | systemDictionary.cpp                    |
                |                              create()           :VMOperationQueue  initialize_heap()|    resolve_or_fail(name,loader):klassOop|
                |                              run()             vm_thread()                          |                                         |
                |                              entry_point()     execute(op)                          |    resolve_or_null(name,loader):klassOop|
                |dtrace.hpp                                                                           |                                         |
                |  HS_DTRACE_PROBE_DECL1()                                                            |    resolve_from_stream()                |
                |  DTRACE_PROBE1()         ciInstanceKlass.cpp  os                  GCCause           |    load_instance_class()                |
                |                                find_method()   create_thread()        to_string()   |    find_class()                         |
                |                           instanceKlass.cpp       //pthread_create()                |    parse_stream()                       |
                |                                 find_method()  start_thread()                       | dictionary.cpp                          |
                |                                                java_start()                         |    find_class()                         |
                |                                                pd_start_thread()                    |    get_entry()                          |
                |                                                                                     |                                         |
                |                                                                                     |                                         |
                |                                                                                     |                                         |
                +-----------------------------------------------------------+-------------------------++----------------------------------------+
                |JavaCalls                    CompileBroker                 | VM_Operation             |  ClassLoader.cpp                       |
                |  call()                      _method_queue:CompileQueue*  |      evaluate()          |      first_entry:ClassPathEntry        |
                |  call_helper()               _task_free_list:CompileTask* |      doit()              |      load_classfile()                  |
                |  call_default_constructor()  compile_method()             |                          |  ClassPathEntry                        |
                |  can_not_compile_call_site() compile_method_base()        | VM_GC_Operation          |      open_stream(name):ClassFileStream |
                |  call_virtual()              create_compile_task()        |        :VM_Operation     |                                        |
                |                              allocate_task():CompileTask* |                          |  ClassPathDirEntry:ClassPathEntry      |
                |os_linux.cpp                  compiler_thread_loop()       |                          |  ClassPathZipEntry:ClassPathEntry      |
                |  os::os_exception_wrapper()  invoke_compiler_on_method()  | ConcurrentMarkSweepThread|  LazyClassPathEntry:ClassPathEntry     |
                |                                                           |    :ConcurrentGCThread   |                                        |
                |                              StubRoutines.cpp             |                          |  ClassFileParser                       |
                |  CompileTask                    call_stub()               |                          |     _stream:ClassFileStream            |
                |        initialize()          stubGenerator_x86_64.cpp     |                          |     parseClassFile()                   |
                |                               generate_call_stub()        |                          |                                        |
                | ciEnv.cpp                                                 |                          |                                        |
                |    get_method_from_handle()                               |                          |                                        |
                |                              assembler.cpp                |                          |                                        |
                |                                                           |                          |                                        |
                +--------------------------------------+--------------------+--------------------------+--------------+-------------------------+
                | verifier.cpp                         |               CollectedHeap                                  |                         |
                |                                      |                   *obj_allocate()                            |                         |
                | linkResolver.cpp                     |                                                              |                         |
                |       check_klass_accessability()    |  parallelScavengeHeap    GenCollectedHeap                    |                         |
                |                                      |        GenerationSizer                                       |                         |
                |                                      |                                                              |                         |
                | TemplateTable                        |                                                              |                         |
                |     resolve_cache_and_index()        |                              _gens[max_gens]:Generation*     |                         |
                |                                      |                              initialize()                    |                         |
                +--------------------------------------+                              _gen_specs: GenerationSpec      +-------------------------+
                | InterpreterMacroAssembler            |                               do_collection()                                          |
                |       get_cache_and_index_at_bcp()   |                                                                                        |
                | InterpreterRuntime                   |                                                                                        |
                |       resol^e_get_put()              |                               GenCollectorPolicy       MarkSweepPolicy                 |
                |       resolve_in^oke()               |                                     generations()      ASConcurrentMarkSweepPolicy     |
                |       resolve_invokehandle()         |                                                        ConcurrentMarkSweepPolicy       |
                |       resolve_invokedynamic()        |                                                                                        |
                |                                      |                               GenerationSpec                                           |
                |                                      |                                     init():Generation                                  |
                |                                      |                                                                                        |
                +--------------------------------------+----------------------------------------------------------------------------------------+
                |                                                                      ConcurrentMarkSweepGeneration                            |
                |                                                                            ref_processor_init()                               |
                |                                                                            _ref_processor                                     |
                |                                                                                                                               |
                |                                                                            allocate()                                         |
                |                                                                            should_collect()                                   |
                |                                                                            collect()                                          |
                |                                                                      ReferenceProcessor                                       |
                |                                                                            process_discovered_references()                    |
                |                                                                                                                               |
                |                                                                                                                               |
                +-------------------------------------------------------------------------------------------------------------------------------+

          [模板解释器--字节码的resolve过程](https://www.cnblogs.com/foreach-break/p/hotspot_jvm_resolve_and_link.html)


  071201_jdk     319a3b994703aac84df7bcde272adfcb3cdbbbf0 Initial load
                +-------------------------------------------------------------------------------+
                |javac                                                                          |
                |                         sun.tools.javac.Main                                  |
                |                                                                               |
                |                                                                               |
                |                           BatchEnvironment:Environment                        |
                |                                 parseFile(file:ClassFile )                    |
                |                                                                               |
                |                           BatchParser:Parser                                  |
                |                                 parseFile()                                   |
                |                                 imports:Imports                               |
                |                                 classes:Vector                                |
                |                          Parser:Scanner                                       |
                |                                parseFile()                                    |
                |                                scan()                                         |
                |                                actions:ParserActions                          |
                |                                                                               |
                +-------------------------------------------------------------------------------+
                |                          TreeMaker      ParserActions                         |
                |                                              packageDeclaration()             |
                |                                              importClass()                    |
                |                                              importPackage()                  |
                |                                              beginClass()                     |
                |                                              endClass()                       |
                |                                              defineField()                    |
                |                                                                               |
                |                                                                               |
                |                           Imports:Constants                                   |
                |                               resolve()                                       |
                |                                                                               |
                +-------------------------------------------------------------------------------+


https://www.cnblogs.com/wade-luffy/p/5925728.html
  ```


  ```
  000006_langtools       84e2484ba645990f4c35e60d08db791806ae40be Initial load
* 000007_init_load_merge 0d206a7adbbc58f8b70c96d1b65da1e391c62474 Merge






https://blog.csdn.net/li1376417539/article/details/102009807


```