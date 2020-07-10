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

## 1 数据 - 编码

数据与二进制
- 字长
  [字长](http://www.cnblogs.com/chakyu/p/7405275.html)
  [进制转化](https://www.branah.com/ascii-converter)
- 字节，编码，字符集 
sun.jnu.encoding是指操作系统的默认编码，
file.encoding是指JAVA文件的编码
```java
javac -encoding utf-8 TextFileEncoding.java  //必须设置和文件编码一直的编码

java -Dfile.encoding=utf-8 TextFileEncoding

```
[java -h 相关启动参数帮助文档](https://docs.oracle.com/javase/7/docs/technotes/tools/windows/java.html)

《The Unicode® Standard Version 9.0 》
BCD->ASCII（128）->ISO/IEC8859-1，又称Latin-1（256）->Unicode(1_114_112)
```
                            256
                   128
              16
            +------+---------+------------+
        16  | 128  |         |            |
            | 0xf  |         |            |
            +------+         |            |
            |       256      |            |
    128     |      0xff      |            |
            |                |            |
256         +----------------+            |
            |               65536         |Redundancy
            |               0xffff        |
            |                             |
            |                             |
            |                             |
            |                             |
            +-----------------------------+
 
       +--------------------------------+ colomns 256  +--------------------------------+

 +    +---------------------------------------------------------------------------------+    +
 |    |             ascii                 |                   latin1                    |BMP |
 |    +---------------------------------------------------------------------------------+    |
 |    |                                                                                 |    |
 |    |                                                                                 |    |
 |    |                                                                                 |    |
 |    |                                                                           .     |    +
 |    |                                                                           .     |   rows 256
 |    |                                                                           .     |    +
 |    |                                                                                 |    |
 |    +---------------------------------------------------------------------------------+    +
 |                                                                                       SMP
 |                                                                                       SIP     
 |                                           .
                                             .
planes 0x10                                  .                                           SSP
 +                                                                                       SPUA-A
 |    +---------------------------------------------------------------------------------+
 |    |                                                                                 |SPUA-B
 |    |                                                                                 |
 |    |                                                                                 |
 |    |                                                                                 |
 |    |                                                                                 |
 +    +---------------------------------------------------------------------------------+
 


1字节 0xxxxxxx 
2字节 110xxxxx 10xxxxxx 
3字节 1110xxxx 10xxxxxx 10xxxxxx 
4字节 11110xxx 10xxxxxx 10xxxxxx 10xxxxxx 
5字节 111110xx 10xxxxxx 10xxxxxx 10xxxxxx 10xxxxxx 
6字节 1111110x 10xxxxxx 10xxxxxx 10xxxxxx 10xxxxxx 10xxxxxx 
```
[unicode 及位置](https://unicode-table.com/en/#control-character)
 [emoji位置](https://unicode-table.com/en/#emoticons)  80个字符(1F600— 1F64F)
UCS-4 第1个字节2^7=128个group，第2个字节2^8=256个平面（plane）,第3个字节分为256行 （row），第4个字节代表每行有256个码位（cell）
unicode 有0x10FFFF个cell，分为 17平面，(2^8=256)行，(2^8=256)单元
 
- 涉及类
  1. [Character.UnicodeBlock](https://en.wikipedia.org/wiki/Unicode_block)
- [Unicode与UTF-8转化](https://zh.wikipedia.org/wiki/UTF-8)
- [UTF-16](https://en.wikipedia.org/wiki/UTF-16) 
  UTF-16编码（二进制）就是：xxxxxxxx xxxxxxxx（0区）
  或110110yy yyyyyyyy 110111xx xxxxxxxx（超过三个字节 Unicode 用四个字节的UTF-16编码）
  
```java
  System.out.println("a".getBytes(StandardCharsets.UTF_16).length);//结果为4，是因为加上BOM(字节顺序标记(ByteOrderMark))大端序，用FEFF表示，FEFF占用两个字节。
```

  BOM大端，高位字节存储在内存地址的低位地址

```
    0x CC AA 88 66

   Big-Endian
   +--------+--------+-------+--------+
   |  CC    |  AA    |  88   |  66    |
   |        |        |       |        |
   +--------+--------+-------+--------+

   Little-Endian
   +--------+--------+-------+--------+
   |  66    |  88    |  AA   |  CC    |
   |        |        |       |        |
   +--------+--------+-------+--------+
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
      E 第一位（1：大于1的十进制，0：0~1之间的十进制），整合（E=[小数点移位到个位1.左移1，右移0][其他7位，小数点移位的二进制] −1）
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

### JVM 类加载机制及GC
[类](https://docs.oracle.com/javase/specs/jvms/se7/html/jvms-5.html)
[类加载器（日文）](http://fits.hatenablog.com/entry/2016/05/06/200824)
[OpenJDK备忘录（日文）](http://hsmemo.github.io/index.html)
[jclasslib 查看类结构]()
```
JVM
+--------------------------------------------------------------------+    +-------------------------------------------------+
| heap:  memory allocation and garbage collection                    |    |  Thread                                         |
+--------------------------------------------------------------------+    | +--------------+ +-------------+                |
| Structure of JVM          | Run-Time Data Areas                    |    | | Native Method| |  Program    |                |
|                           |                                     +-----> | | Stacks       | |  Couter     |                |
|                           |                                        |    | |              | |  Register   |                |
|                           |                                        |    | +--------------+ +-------------+                |
+-----------+--------------------------------------------------------+    | +---------------------------------------------+ |
|           | Initialization|                                        |    | | JVM Stacks      +-------------------------+ | |
|           +------------------------------+-------------------------+    | |                 | Frame                   | | |
|           |               | Resolution   |                         |    | |                 |   LVT                   | | |
|Continuity |               +--------------+                         |    | |                 |   Operand Stack         | | |
|Complete   | linking       | Preparation  |                         |    | |                 |   Frame Data            | | |
|           |               +--------------+                         |    | |                 |   method  return addr   | | |
|           |               | Verification |                         |    | |                 |   dynamic addr          | | |
|lifetime of+--------------------------------------------------------+    | |                 +-------------------------+ | |
|class      | loaders       |  ClassLoader | Application ClassLoader |    | +---------------------------------------------+ |
|           |               |              | Extension ClassLoader   |    +-------------------------------------------------+
|           |               |              | BootstrapClassLoader    |    | +-------------+     +-------------------------+ |
+-----------+------------------------------+-------------------------+    | | Heap        |     |Method Area              | |
|     class File Format     |                                        |    | |             |     | +-----------------------+ |
+--------------------------------------------------------------------+    | |             |     | | Runtime Constant Pool | |
|     JVM  Instruction Set  |                javap (java bytecode)   |    | +-------------+     | +-----------------------+ |
+--------------------------------------------------------------------+    |                     | | Constant Pool Table   | |
|     Compiling             |                javac                   |    |                     +-------------------------+ |
+---------------------------+----------------------------------------+    +-------------------------------------------------+




                                                                          executor

                                                                          +-------------+----------+---------------+       +---------------+
                                                                          |             |          |               |       |               |
                                                                          |             |          |               |       |               |
                                                                          |   interpret |   jit    | jvm           |       |    jni        |
                                                                          |             |          |               |       |               |
                                                                          +-------------+----------+---------------+       +---------------+

                    heap                      method zone
+---------------------+----------------+------------------------+
|                     |                |                        |
|      Eden           |                |                        |
|                     |                |                        |
+-----------+---------+                |                        |
|           |         |                |                        |
|  s0       |   s1    |                |                        |
|           |         |                |                        |
+-----------+---------+----------------+------------------------+

       yuang/new           tunure/old           permanent


-xx:+PrintGCDetials 打印堆空间信息
```


jni方法 通过 宏**DT_RETURN_MARK_DECL** 注册方法
```cpp
DT_RETURN_MARK_DECL(SomeFunc, int);
JNI_ENTRY(int, SomeFunc, ...) int return_value = 0;
DT_RETURN_MARK(SomeFunc, int, (const int&)return_value);
foo(CHECK_0)
return_value = 5;
return return_value;
JNI_END

```
#### 编译
 常量传播(constant propagation) 常量折叠(constant folding)


[《Compilers-Principles, Techniques, & Tools》, Second Edition  # 9.4 Constant Propagation ](https://www.slideshare.net/kitocheng/ss-42438227)


#### 加载，连接（校验，准备，解析），初始化
1.加载：查找并加载Class文件。(五种主动加载)
2.链接：验证、准备、以及解析。
  验证：确保被导入类型的正确性。
  准备：为类的静态字段分配字段，并用默认值初始化这些字段。（heap开辟空间）
  解析：根据运行时常量池的符号引用来动态决定具体值得过程。（查找接口，父类，其他符号）
3.初始化：将类变量初始化为正确初始值。
```java
dx --dex --output=Hello.dex Hello.class

javap -c  Hello.class

javap –verbose Hello.class 可以看到更加清楚的信息

[在线查看java字节码](https://javap.yawk.at/)
```
[**Jasmin**](http://jasmin.sourceforge.net/guide.html) 是一种免费的开源的 JAVA 汇编器 ，它将使用**Java虚拟机指令集**以人类容易阅读方式编写的类汇编语法文件编译成class文件，注意jasmin并不是Java语言的汇编器。
[Dalvik寄存器指令](http://pallergabor.uw.hu/androidblog/dalvik_opcodes.html)，有64k个寄存器，只用到前256个
 smali - An assembler/disassembler for Android's dex format

[ **初始化**](https://blog.csdn.net/sujz12345/article/details/52590095/)
 ```
<init>与<clinit> 对象和类字段初始化

1. 父类静态变量/语句块 （代码顺序执行）
3. 子类静态变量/语句块 
5. 父类变量/语句块 
7. 父类构造函数 
8. 子类变量/语句块 
10. 子类构造函数
 ```

#### 类加载器与双亲委派模型(Parents Dlegation Mode)
```
                 C++
 +-----------------------+
 | Bootstrap ClassLoader |  <JAVA_HOME>\lib,-Xbootclasspath
 +----------^------------+
            |    java,parentclass=null
 +----------+-----------+
 | Extension ClassLoader|  <JAVA_HOME>\lib\ext,java.ext.dirs
 +----------^-----------+
            |
+-----------+-------------+
| Application ClassLoader | ClassPath
+-----------^-------------+
            |
+-----------+------------+
|   Custom ClassLoader   |
+------------------------+


```

#### 类的加载与对象创建的方法
```
new
反射（Class#newInstance，Constructor#newInstance)
clone
反序列化
```


```java
运行时数据区

        JavaStack  Heap
            ^     ^
            |     |  Method Zone
            |     |     ^
            |     |     |
            +     +     +
        Object o = new Object()    +-----------> ProgramCounter//放执行当前指令的地址



      int    i = 0
                 +
                 |
                 v
                 JavaStack


      Object.java
          public final native void notify();
                          +
                          |
                          |
                          |
                          +------------------> nativeStack

```
#### JVM 内存区域与内存模型

[jol查看内存结构](http://hg.openjdk.java.net/code-tools/jol/file/tip/jol-samples/src/main/java/org/openjdk/jol/samples/)
- 数组 
  
Arrays.sort 双轴快排算法（包含归并排序算法，经典快速排序算法，插入排序算法混用，及**jdk 1.7**废弃掉的归并排序和插入排序混用）

```java
数组
                        ObjectHeader64Coops
          +-            +------------------------+
          |             |     Mark Word          |   8Byte
Header    |             +------------------------+
          |             |     Klass Pointer      |   4Byte
          ++            +------------------------+
                        |     Array Length       |   4Byte
                        +------------------------+
                        |                        |   //基本数据类型或每个对象的引用
                        +------------------------+
                        |                        |   //基本数据类型或每个对象的引用
                        +------------------------+

字符串32位
java.lang.String object internals:
 OFFSET  SIZE     TYPE DESCRIPTION                               VALUE
      0     4          (object header)                           01 00 00 00 (00000001 00000000 00000000 00000000) (1)
      4     4          (object header)                           00 00 00 00 (00000000 00000000 00000000 00000000) (0)
      8     4   char[] String.value                              [我, 4]
     12     4      int String.hash                               0
     16     8          (loss due to the next object alignment)
Instance size: 24 bytes
Space losses: 0 bytes internal + 8 bytes external = 8 bytes total
```

- [类的内存大小](https://segmentfault.com/a/1190000007183623)

```java
                        ObjectHeader64Coops
          +-            +------------------------+
          |             |     Mark Word          |   8Byte
Header    |             +------------------------+
          |             |     Klass Pointer      |   4Byte
          ++            +------------------------+
                        |                        |
                        +------------------------+
                        |                        |
                        +------------------------+


```

[内存占用查看工具](https://segmentfault.com/a/1190000007183623)

以下是 64bit电脑，打印的信息。markword 8bytes,
```java
edu.ptu.java.lib.RefType object internals:
 OFFSET  SIZE   TYPE DESCRIPTION                               VALUE
      0    12        (object header)                           N/A
     12     4        (loss due to the next object alignment)
Instance size: 16 bytes
Space losses: 0 bytes internal + 4 bytes external = 4 bytes total

16
Footprint{Objects=1, References=0, Primitives=[]}
```

[ObjectHeader64Coops 内存结构](https://gist.github.com/arturmkrtchyan/43d6135e8a15798cc46c)
```java
ObjectHeader32
|----------------------------------------------------------------------------------------|--------------------|
|                                    Object Header (64 bits)                             |        State       |
|-------------------------------------------------------|--------------------------------|--------------------|
|                  Mark Word (32 bits)                  |      Klass Word (32 bits)      |                    |
|-------------------------------------------------------|--------------------------------|--------------------|
| identity_hashcode:25 | age:4 | biased_lock:1 | lock:2 |      OOP to metadata object    |       Normal       |
|-------------------------------------------------------|--------------------------------|--------------------|
|  thread:23 | epoch:2 | age:4 | biased_lock:1 | lock:2 |      OOP to metadata object    |       Biased       |
|-------------------------------------------------------|--------------------------------|--------------------|
|               ptr_to_lock_record:30          | lock:2 |      OOP to metadata object    | Lightweight Locked |
|-------------------------------------------------------|--------------------------------|--------------------|
|               ptr_to_heavyweight_monitor:30  | lock:2 |      OOP to metadata object    | Heavyweight Locked |
|-------------------------------------------------------|--------------------------------|--------------------|
|                                              | lock:2 |      OOP to metadata object    |    Marked for GC   |
|-------------------------------------------------------|--------------------------------|--------------------|

 
```
- 字符串

```java

java.lang.String object internals:
 OFFSET  SIZE     TYPE DESCRIPTION                               VALUE
      0    12          (object header)                           N/A
     12     4   char[] String.value                              N/A
     16     4      int String.hash                               N/A
     20     4          (loss due to the next object alignment)
Instance size: 24 bytes
Space losses: 0 bytes internal + 4 bytes external = 4 bytes total


----------------------------------

java.lang.String object internals:
 OFFSET  SIZE     TYPE DESCRIPTION                               VALUE
      0     4          (object header)                           01 00 00 00 (00000001 00000000 00000000 00000000) (1)
      4     4          (object header)                           00 00 00 00 (00000000 00000000 00000000 00000000) (0)
      8     4          (object header)                           c2 02 00 f8 (11000010 00000010 00000000 11111000) (-134217022)
     12     4   char[] String.value                              [a]
     16     4      int String.hash                               0
     20     4          (loss due to the next object alignment)
Instance size: 24 bytes
Space losses: 0 bytes internal + 4 bytes external = 4 bytes total

```

#### Class文件格式及指令
[jvms Instruction Set ](https://docs.oracle.com/javase/specs/jvms/se11/html/jvms-2.html#jvms-2.11)
[jvms Instruction Set ](https://docs.oracle.com/javase/specs/jvms/se11/html/jvms-6.html)
``` bytecode instruction operator
+-----------------+---------------------------------------+
|  misc           | athrow   monitorenter,monitorexit     |
+---------------------------------------------------------+
|                 |-load_<n>  -store_<slot>               |lvt -> OperandStack; lvt <- OperandStack
|  stack operator |-const_<num>  -push  -ldc              |constant ->OperandStack     
|                 | -pop   dup                            |OperandStack     
+---------------------------------------------------------+
|  type conversion|  i2b, i2c,f2i                         |
+----------------------------+----------------------------+
|  object operator|    field |  getstatic  putstatic      |
|                 |          |  getfield   putfield       |
|                 +---------------------------------------+
|                 |    invoke|invokevirtual invokestatic  |
|  Creation       |    method|invokeinterface invokespecial|
|    &            |          |invokedynamic               |
| Manipulation    +---------------------------------------+
|                 |    method|  ireturn  lreturn   freturn|
|                 |    return|  dreturn   areturn         |
|                 +---------------------------------------+
|                 | object   |  new  newarray anewarray   |
|                 |          |           multianewarray   |
|                 |          |  -aload   -astore          |
|                 |          |  arraylength               |
|                 |          |  instanceof  checkcast     |
+----------------------------+----------------------------+
|  decision       |          ifeq,iflt,ifnull,ifnonnull   |
|                 |          tableswitch,lookupswitch     |
|                 |          goto,goto_w,jsr,jsr_w,ret    |
+---------------------------------------------------------+
|  alth           | -add -sub -mul -div -rem -neg         |
|                 |  -shl,-shr，-ushl,-ushr  iinc         |
|                 |  -or,-and , -xor                      |
|                 |  -cmpg,-cmpl,-cmp                     |
+-----------------+---------------------------------------+
-代表类型
+--------------------------------------------------------------+
|opcode| byte| short| int| long| float| double|char| reference |
+--------------------------------------------------------------+
|      | b   | s    | i  | l   | f    | d     |c   | a         |
+------+-----+------+----+-----+------+------------+-----------+

```
**Jasmin** 是一种免费的开源的 JAVA 汇编器 ，它将使用Java虚拟机指令集以人类容易阅读方式编写的类汇编语法文件编译成class文件，注意jasmin并不是Java语言的汇编器。

#### 引用数据类型回收（虚拟机，垃圾回收器，回收算法）
 [mat gc root](https://www.cnblogs.com/set-cookie/p/11069748.html)
3. Java垃圾回收算法及垃圾收集器
   标记-清除算法(mark-sweep), dalvikvm
   标记-压缩算法(mark-compact),
   复制算法(copying)  hotspot
   引用计数算法(reference counting)
   分代收集算法（Generational Collection）
4. JVM可回收对象判定方法 : 对象回收判定(可达性分析算法&对象引用) 
>《Inside the Java Virtual Machine》 ,Wiley (1996)《Garbage Collection- Algorithms for Automatic Dynamic Memory Management》 
JVM给了三种选择收集器：串行收集器、并行收集器、并发收集器

[垃圾回收概念Thread，Root Set of References，Reachable and Unreachable Objects，garbage collection](https://www.javarticles.com/2016/09/java-garbage-collector-reachable-and-unreachable-objects.html)
```
GC Roots 的对象包括下面几种： 
虚拟机栈（栈帧中的本地变量表）中引用的对象
方法区中类静态属性引用的对象
方法区中常量引用的对象
本地方法栈中 JNI （即一般说的 Native 方法）引用的对象
```

```

+----------+-----------+----------------+---------------------------+
|(generation collection)Minor GC/Full GC                            |
+----------+-----------+----------------+---------------------------+
|safepoint/Safe Region                                              |
+----------+-----------+----------------+---------------------------+
|          |           | G1             |                           |
|          | concurrent| CMS            |                           |
|          +----------------------------+                           |
|          | parallel                   |                           |
|Collector +----------------------------+                           |
|          | ParallelOld                |                           |
|          +----------------------------+                           |
|          | Serial                     |                           |
+-------------------------------------------------------------------+
|Collection| mark-compact               |  reference counting       |
|Algorithms| copying                    |                           |
|          | mark-sweep                 |                           |
+-------------------------------------------------------------------+
| Garbage  |Reachability Analysis(java) |  Reference Counting       |
|          | GC Roots Tracing           |  (objc,Python)            |
+----------+----------------------------+---------------------------+

Generational Collection
+---------------------------------------------------------------------+
|  new gen       copying        copying           copying             |
| (Minor GC)     +--------+    +--------+     +------------------+    |
|                | Serial |    | ParNew |     | Parallel Scavenge|    |
|                +--+--+--+    +--+-+---+     +-----+----+-------+    |
|                   |  |          | |               |    |            |
|                +--+  +-----+    | |   +-----------+    |            |
|                |           |    | |   |                |    +-----+ |
+---------------- ----------- ---- - --- ---------------- ----+ G1  +-| mark-compact+copying
| old gen        | +---------+----+ |   |                |    +-----+ |
|                | |         |      |   |                |            |
|            +---+-+        ++------+---+--+    +--------+-----+      |
|            | CMS +--------+ Parallel Old |    | Serial Old   |      |
|            +-----+        +--------------+    +--------------+      |
|          mark-compact        mark-compact       mark-compact        |
+---------------------------------------------------------------------+
| Permanet Generation                                                 |
+---------------------------------------------------------------------+
并行（Parallel）：指多条垃圾收集线程并行工作，但此时用户线程仍然处于等待状态。
并发（Concurrent）：指用户线程与垃圾收集线程同时执行（但不一定是并行的，可能会交替执行），用户程序在继续运行。而垃圾收集程序运行在另一个CPU上。
```
Reference Counting：难解决对象之间循环引用的问题

[“长时间执行”](https://crowhawk.github.io/2017/08/10/jvm_2/)的最明显特征就是指令序列复用;
例如方法调用、循环跳转、异常跳转等，所以具有这些功能的指令才会产生Safepoint。
在GC发生时让所有线程（这里不包括执行JNI调用的线程）都“跑”到最近的安全点上再停顿下来。
**安全区域**是指在一段代码片段之中，引用关系不会发生变化。在这个区域中的任意地方开始GC都是安全的。

[HotSpot 虚拟机](http://openjdk.java.net/groups/hotspot/docs/HotSpotGlossary.html)
[官方文档 Hotspot](https://openjdk.java.net/groups/hotspot/docs/RuntimeOverview.html)


Generational Collection（分代收集）算法
  1. 新生代都采取Copying算法
  2. 老年代的特点是每次回收都只回收少量对象，一般使用的是Mark-Compact算法
  3. 永久代（Permanet Generation），它用来存储class类、常量、方法描述等。对永久代的回收主要回收两部分内容：废弃常量和无用的类。


《如何监控Java GC》中已经介绍过了jstat

JVM性能调优监控工具jps、jstack、jmap、jhat、jstat、hprof

###  类的相关概念（数据封装，信息结构，复杂数据 - 面向对象）
高级特性：强类型，静态语言，混合型语言（编译，解释）
动态类型语言是指在运行期间才去做数据类型检查的语言，说的是数据类型，动态语言说的是运行是改变结构，说的是代码结构。
强类型语言，一旦一个变量被指定了某个数据类型，如果不经过强制类型转换，那么它就永远是这个数据类型。
静态语言的数据类型是在编译其间确定的或者说运行之前确定的，编写代码的时候要明确确定变量的数据类型。
[oops](https://www.javatpoint.com/java-oops-concepts)
```
---------+---------------------------------------------------------------------------------+
|        |  Design patterns    | SOLID and GRASP guidelines                                |
|        |(cohesion & coupling)|  Gof                                                       |
|        +---------------------------------------------------------------------------------+
|        |  reference variable| this                                                       |
|        +---------------------------------------------------------------------------------+
|        | (memory management)| Variable Method  Block Nested class                        |
|        |     static         |                                                            |
|        +-----------------+--+------------------------------------------------------------+
|        |  Encapsulation  |  package                                                      |
|        |                 |  Access Modifiers                                             |
|        |                 |   member, method, constructor or class                        |
|        +---------------------------------------------------------------------------------+
|        |  Abstraction    |  Abstract class                                               |
|        |                 |  Interface                                                    |
|        +---------------------------------------------------------------------------------+
|        |                 |  +--------------------+  Overloading   Overriding             |
|        |                 |  |final      variable |  Covariant Return Type ,    Super     |
|        |                 |  |           method   |  instance initializer block           |
|        |  Polymorphism   |  |           class    |  Runtime polymorphism, Dynamic Binding|
|        |                 |  +--------------------+  instanceof operator                  |
|        +---------------------------------------------------------------------------------+
|        |  Inheritance    |  Method Overriding                                            |
|        |  ( IS-A )       |  Code Reusability                                             |
|        +---------------------------------------------------------------------------------+
| OOPs   |                 |                              +----------------------------+   |
|        |  Class          | Fields    Methods  Blocks    |             |default       |   |
|        |                 |                              |Constructors |Parameterized |   |
|        |                 | Nested class and interface   +----------------------------+   |
|        +-----------------+---------------------------------------------------------------+
|        |  Object  (data and functions)                                                   |
+--------+---------------------------------------------------------------------------------+
| ooad   |                                                                                 |
+--------+---------------------------------------------------------------------------------+

```


《Effect java》
- Object方法

equals()，hashcode()，toString()
##### [hashcode() 基础知识](https://www.cnblogs.com/mengfanrong/p/4034950.html)
混合hash （MD5）
 
JDK Hash算法
1. Map.hash: Austin Appleby's MurmurHash3

2. String.hashCode
```
public int hashCode() {
    int h = hash;
    if (h == 0 && value.length > 0) {
        char val[] = value;

        for (int i = 0; i < value.length; i++) {
            h = 31 * h + val[i];
        }
        hash = h;
    }
    return h;
}
```

3. Object.hashCode
```
 java -XX:+PrintFlagsFinal -version|grep hashCode

static inline intptr_t get_next_hash(Thread * Self, oop obj) {
}
```

Redis
1. Thomas Wang's 32 bit Mix Function
2. Austin Appleby's MurmurHash2
3. DJB Hash



面向对象的三大特性，五大原则，23个设计模式
- 封装
    1. 枚举类,内部类（静态，成员，局部，匿名）
    2. 自动装箱和拆箱
    3. 日期
- 继承
     1. 抽象类，接口
- 多态 
    1. 继承
    2. 重写
    3. 父类引用指向子类对象
- 重载（面向方法特性）

```

匿名函数接口可以有多个抽象方法，不能有默认方法；lambda实现接口时对应的函数接口只能有一个抽象方法，但是可以有多个默认方法

函数式接口指的是只定义了唯一的抽象方法的接口（除了隐含的Object对象的公共方法）， 因此最开始也就做SAM类型的接口（Single Abstract Method）

```

```
public enum NumEnum {
 ONE;
 }
public final class NumEnum extends java.lang.Enum<NumEnum> {
  public static final NumEnum ONE;
  public static NumEnum[] values();
  public static NumEnum valueOf(java.lang.String);
  static {};
} 

```
### 字符串
```
+---------------+----------+----------+
|               |  final   |  synchro |
+-------------------------------------+
| String        |   √      |          |
+-------------------------------------+
| StringBuffer  | char[]   |    √     |
+-------------------------------------+
| StringBuilder | char[]   |    x     |
+---------------+----------+----------+

```



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
## 元编程
RTTI，即Run-Time Type Identification，运行时类型识别。RTTI能在运行时就能够自动识别每个编译时已知的类型。

反射机制就是识别未知类型的对象。反射常用于动态代理中。

### 应用：内省（Introspector）

### 序列化Serializable

字节码分析：序列化后，存储java信息，类信息，字段信息
[透过byte数组简单分析Java序列化、Kryo、ProtoBuf序列化](https://www.cnblogs.com/softlin/archive/2015/07/17/4653168.html)


### 类创建/运行时数据解析 - 反射（动态代理，注解接口）

#### 1.动态代理（序列化与RPC）
```
public class Proxy implements java.io.Serializable {
         /**
     * the invocation handler for this proxy instance.
     * @serial
     */
    protected InvocationHandler h;
}

sun.misc.ProxyGenerator#generateProxyClass(java.lang.String, java.lang.Class<?>[], int)//类信息转化为字节数组，在转化为代理Class对象。



```

```
    static interface Animal{

    }
    static class Dog implements Animal{
        @Override
        public String toString() {
            return "dog tostring()";
        }
    }
    public static void main(String[] args) {
        Dog dog = new Dog();
        Animal proxy = (Animal) Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(), new Class[]{Animal.class}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxyMethodNameLabel, Method method, Object[] args) throws Throwable {
                System.out.println("before");
                Object invoke = method.invoke(dog, args);
                System.out.println(invoke);
                System.out.println("after");
                return invoke;
            }
        });
//        System.out.println(proxy.toString());
        proxy.toString();
        System.out.println();
    }

```

```
@Retention(RetentionPolicy.RUNTIME)
public @interface A {
}

编译后：

public interface A extends java.lang.annotation.Annotation {
}

```
#### 2.字节码增强
- 加载时
ASM
cglib
javassist

- 运行时，动态加载
#### 3.注解

**@interface**

java 8 重复注解

Java 8拓宽了注解的应用场景。现在，注解几乎可以使用在任何元素上：局部变量、接口类型、超类和接口实现类，甚至可以用在函数的异常定义上。
##  类创建/运行时数据解析 - 泛型
1. 泛型：Generics in Java is similar to templates in C++.
特点：运行时类型擦除
集合容器和网络请求经常用到

泛型是一种多态技术。而多态的核心目的是为了消除重复，隔离变化，提高系统的正交性。
```
+----------------------------------------------------------------------------------------------------------+
|                                                                                                          |
| GenericArrayType            ParameterizedType          TypeVariable            WildcardType         Class|
|   getGenericComponentType()   getActualTypeArguments()  getBounds()              getUpperBounds()        |
|                               getRawType()              getGenericDeclaration()  getLowerBounds()        |
|                               getOwnerType()            getName()                                        |
|                                                         getAnnotatedBounds()                             |
+----------------------------------------------------------------------------------------------------------+
|                                      Type  (reflect)                                                     |
+----------------------------------------------------------------------------------------------------------+

```
```
javax annotation apt/serviceloader
+-------------------------------------------------------------------------+------------------+
|         VariableElement           ExecutableElement:Parameterizable     |                  |
|TypeElement:Parameterizable,QualifiedNameable    TypeParameterElement    |                  |
|                    PackageElement:QualifiedNameable                     |                  |
+-------------------------------------------------------------------------+------------------+
|                        Element                                          | AnnotationMirror |
+-------------------------------------------------------------------------+------------------+
|                        AnnotatedConstruct                                                  |
+--------------------------------------------------------------------------------------------+

```

## 2.3 数据访问 - 流（IO, NIO）
字符串，类，文件（xml,json），内存，网络，数据库

### 2.3.1 数据匹配（正则）
《Java编程思想-14章》

[Java正则教程](https://www.tutorialspoint.com/javaregex/javaregex_characters.htm)
[Java官网正则教程](https://docs.oracle.com/javase/7/docs/api/java/util/regex/Pattern.html)

```Regex Quantifiers
                                                                   {n,}
                                                            +------------------------------------------>
            *
                                                                   {n,m}
+------------------------------------------------->         +------------------->

      ?                    +

<----------- ------------------------------------->        {n}

|           |                                               |                   |
+-----------+-----------------------------------------------+-------------------+------------------------>

0           1                                               n                   m

```

```Regex
+---------------+----------------------------------------------------------------------------------------------------------------------+
|  flags        |  CASE_INSENSITIVE MULTILINE DOTALL  UNICODE_CASE  CANON_EQ  UNIX_LINES  LITERAL  UNICODE_CHARACTER_CLASS  COMMENTS   |
|               |                                                                                                                      |
+---------------+---------------------------------------------+------------------------------------------------------------------------+
|  Quotation    |   \             \Q...\E                                                                                               |转义
+--------------------------------------------------------------------------------------------------------------------------------------+
| Back          |  \n                                  \k<name>                                                                        |
| references    |  nth capturing group matched         named-capturing group "name" matched                                            |
+-------------------------------------------------------------+------------------------------------------------------------------------+
|               |                                             |              |     (?<name>X) a named-capturing group                  |
|               |                                             |              |     (?:X) a non-capturing group                         |
|               |                                             |              |     (?>X) an independent, non-capturing group           |
|               |                                             |              |                                                         |
|               |                                             |              |     (?idmsuxU-idmsuxU)                                  |
|               |                                             |              |     turns match flags i d m s u x U on - off            |
|               |                                             |              |     (?idmsux-idmsux:X)                                  |
|               |                                             | Special      |     non-capturing group                                 |
|               |                                             |              |     with the given flags i d m s u x on - off           |
|               |                                             | Constructs   |                                                         |
|               |                                             |              +---------------------------------------------------------+
|               |                                             |              |     (?=X)   via zero-width positive look ahead          |
|               |                                             |              |     (?!X)   via zero-width negative lookahead           |
|               |                                             |              |     (?<=X)  via zero-width positive lookbehind          |
|               |                                             |              |     (?<!X)  via zero-width negative lookbehind          |
|               |                                             |              |     (?>X)   as an independent, non-capturing group      |
|               +                                             +------------------------------------------------------------------------+
|  Logical      | XY                          X|Y             |  (X)                                                                   |
|  Operators    | X followed by Y             Either X or Y   |  group                                                                 |
+--------------------------------------------------------------------------------------------------------------------------------------+
+--------------------------------------------------------------------------------------------------------------------------------------+
|               |  X?   X, once or not at all     X{n}     X, exactly n times       |                     |                            |
|  Quantifiers  |  X*   X, zero or more times     X{n,}    X, at least n times      |                     |                            |
|               |  X+   X,mone or more times      X{n,m}   X, at least n            |       ?             |                 +          |
|               |                                         but not more than m times |                     |                            |
|               +----------------------------------------------------------------------------------------------------------------------+
|               |         Greedy                                                    |     Reluctant       |              Possessive    |
|               |       (matches entire input,then backtrack)                       | (matches as little) | (Greedy, doesn't backtrack)|
+--------------------------------------------------------------------------------------------------------------------------------------+
| Boundary      | ^ The beginning of a line          \b   A word boundary         \A   The beginning of the input                      |
| Matchers      | $  The end of a line               \B   A non-word boundary     \z   The end of the input                            |
|               | \G  The end of the previous match                     \Z  The end of the input but for the final terminator, if any  |
+---------------+-------------------------------------------------------+-------------------------------+------------------------------+
|               | \p{Lower}              \p{Upper}                      |   \p{javaLowerCase}           | \p{IsLatin}                  |
|               | char:[a+z].            char:[A+Z]                     |   Character.isLowerCase()     | A Latin script char          |
|               |                                                       |                               |                              |
|               |                                                       |                               | \p{InGreek}                  |
|               | \p{ASCII}               \p{Alpha}                     |    \p{javaUpperCase}          | A char in the Greek block    |
|               | All ASCII:[\x00+\x7F]   char:[\p{Lower}\p{Upper}]     |    Character.isUpperCase()    |                              |
|               |                                                       |                               | \p{Lu}                       |
|               | \p{Digit}               \p{Alnum}                     |                               | An uppercase letter          |
|               | [0+9]                   [\p{Alpha}\p{Digit}]          |                               |                              |
|  char classes |                                                       |    \p{javaWhitespace}         |  \p{IsAlphabetic}            |
|               | \p{Punct}                                             |    Character.isWhitespace()   |  An alphabetic char          |
|               | Punctuation:                                          |                               |                              |
|  in java      | One of !"#$%&'()*+,+./:;^=^?@[\]^_^{+}^               |                               |  \p{Sc}                      |
|               |                                                       |    \p{javaMirrored}           |  A currency symbol           |
|               | \p{Graph}               \p{Print}                     |    Character.isMirrored()     |  (binary property)           |
|               | A ^isible char:         A printable char:             |                               |                              |
|               | [\p{Alnum}\p{Punct}]    [\p{Graph}\x20].              |                               |                              |
|               |                                                       |                               |  \P{InGreek}                 |
|               |                                                       |                               |  except one                  |
|               |  \p{Blank}                                            |                               |  in the Greek block          |
|               |  A space or a tab:                                    |                               |                              |
|               |  [ \t]                                                |                               |  [\p{L}&&[^\p{Lu}]]          |
|               |                                                       |                               |  except an uppercase letter  |
|               |   \p{XDigit}             \p{Space}                    |                               |                              |
|               |   A hexadecimal digit:   A whitespace char:           |                               |                              |
|               |   [0+9a+fA+F]            [ \t\n\x0B\f\r]              |                               |                              |
|               +-------------------------------------------------------+-------------------------------+------------------------------+
|               |       POSIX                                           |              JAVA             |              Unicode         |
+--------------------------------------------------------------------------------------------------------------------------------------+
|  predefined   |                                                                                                                      |
|               |     .         \d              \D            \s                 \S                     \w            \W               |
|  char classes |     Any char  A digit: [0-9]  A non-digit:  A whitespace char: A non-whitespace char: A word char:  A non-word char: |
|               |                               [^0-9]        [ \t\n\x0B\f\r]    [^\s].                 [a-zA-Z_0-9]. [^\w]            |
|  in java      |                                                                                                                      |
+--------------------------------------------------------------------------------------------------------------------------------------+
|  Grouping     |                                                                                                                      |
|               |     [abc]           [^abc]       [a-zA-Z]      [a-d[m-p]]      [a-z&&[def]]    [a-z&&[^bc]]    [a-z&&[^m-p]]         |
|  char classes |     a, b, or c      (negation)   (range)       (union)         (intersection)  (subtraction)   (subtraction)         |
|               |                                                                                                                      |
+--------------------------------------------------------------------------------------------------------------------------------------+
|               |     x             \\                  \0n                     \0nn                     \0mnn                         |
|               |     char x        backslash char      char with octal         char with octal         char with octal                |
|               |                                       0n (0 ≤ n ≤ 7)          0nn (0 ≤ n ≤ 7)         0mnn (0 ≤ m ≤ 3, 0 ≤ n ≤ 7)    |
|               |                                                                                                                      |
|               |     \xhh                              \uhhhh                                                                         |
| matching char |     char with hexadecimal             char with hexadecimal                                                          |
|               |     0xhh                              0xhhhh                                                                         |
(Literal escape)|                                                                                                                      |
|               |     \t           \n                   \r                      \f                                                     |
|               |     tab char     newline char         carriage-return char    form-feed char                                         |
|               |     ('\u0009')   ('\u000A')           ('\u000D')              ('\u000C')                                             |
+---------------+----------------------------------------------------------------------------------------------------------------------+


```
```java
Pattern/Matcher

Matcher
matches:整个匹配，只有整个字符序列完全匹配成功，才返回True，否则返回False。但如果前部分匹配成功，将移动下次匹配的位置。
lookingAt:部分匹配，总是从第一个字符进行匹配,匹配成功了不再继续匹配，匹配失败了,也不继续匹配。
find:部分匹配，从当前位置开始匹配，找到一个匹配的子串，将移动下次匹配的位置。
reset:给当前的Matcher对象配上个新的目标，目标是就该方法的参数；如果不给参数，reset会把Matcher设到当前字符串的开始处。


String
replace:没有用到正则表达式
replaceFirst: 用到了正则表达式
replaceAll: 用到了正则表达式（后溯，replaceAll("\\W","\\\\$0")）

```
### Unix 5种I/O模型
```
+---------------+------------------+--------------------------+--------------------+-----------------+
|   blocking IO | nonblocking IO   |    IO multiplexing       |  signal driven IO  | asynchronous IO |
|               |                  | select poll epoll(Linux) |                    |                 |
|  Socket       |           | SocketServer,javaNIO , javaRAF  |                    |    Datagram     |
+----------------------------------------------------------------------------------------------------+   +
|               |                  |                          |                    |                 |   |
|initiate       |  initiate        |   check                  |                    |  initiate       |   |
| |             |  check           |     +                    |                    |                 |   | wait data
| |             |  check           |     |  block             |                    |                 |   |
| |             |  check           |     |                    |                    |                 |   |
| |             |    +             |     v                    |                    |                 |
| | block       |    |  block      |  ready                   |    notification    |                 |   + recvfrom block
| |             |    |             |  initiate                |    initiate        |                 |   |
| |             |    |             |     +                    |         +          |                 |   | copy from kernel
| |             |    |             |     |  block             |         | block    |                 |   | to user
| |             |    |             |     |                    |         |          |                 |   |
| v complete    |    v complete    |     v  complete          |         v complete |  notification   |   +
|               |                  |                          |                    |                 |
+---------------+------------------+--------------------------+--------------------+-----------------+
|               |                  | select    poll   epoll   |                    |                 |
|               |                  | fd-limit   x      x      |                    |                 |
|               |                  | poll-ready √ notify-ready|                    |                 |
+---------------+------------------+--------------------------+--------------------+-----------------+
```

```
+---------+----------------------------------+----------------------+----------------------+
|         | fd|limit      |copy_kernel_user  |  event               |  event handle        |
+-------------------------------------------------------------------+----------------------+
|  select | Polling       |  copy fd         | polling fd structure |  sync handle  fd     |
+-------------------------------------------------------------------+----------------------+
|  poll   | linklist      |  copy fd         | polling fd structure |   sync handle fd     |
+-------------------------------------------------------------------+----------------------+
|  epoll  | epoll_create()|  epoll_ctl()     | epoll_wait()         |   epoll_wait() /1 fd |
+---------+----------------------------------+----------------------+----------------------+

```
### BIO (阻塞 I/O)
```

基于字节操作的 I/O 接口：InputStream 和 OutputStream
  内存字节流 I/O ：ByteArrayInputStream 和 ByteArrayOutputStream
基于字符操作的 I/O 接口：Writer 和 Reader
基于磁盘操作的 I/O 接口：File,RandomAccessFile
基于网络操作的 I/O 接口：Socket
```
UTF-8



#### NIO（BUffer,Channel ,Selector）
IO是面向流的，NIO是面向缓冲区的。
NIO机制，它是一种基于通道与缓冲区的新I/O方式，可以直接从操作系统中分配直接内存，即在堆外分配内存

Channel 和 Selector
```java
public abstract class AbstractInterruptibleChannel
    implements Channel, InterruptibleChannel
{
    private final Object closeLock = new Object();
    private volatile boolean open = true;
    private Interruptible interruptor;
    private volatile Thread interrupted;

}


public abstract class AbstractSelector
    extends Selector
{
    private AtomicBoolean selectorOpen = new AtomicBoolean(true);

    // The provider that created this selector
    private final SelectorProvider provider;
    private final Set<SelectionKey> cancelledKeys = new HashSet<SelectionKey>();
    private Interruptible interruptor = null;
 []


}

```
### 数据展示 - 图形化及用户组件
awt/swing

#### 多媒体
[图形化](知识体系-多媒体.md)





## 2.5 C语言，NDK，汇编及CPU处理二进制
> [知识体系-C&CPP程序.md](./知识体系-C&CPP程序.md)
###jni
(jni注册本地方法的两种方式](https://blog.csdn.net/zerokkqq/article/details/79143834)

动态注册
```

+-------------------------------------------------------------------------------------------+
|                                      add();//invokevirtual #7   // Method add:()I         |
+-------------------------------------------------------------------------------------------+
|                               public native int add();                                    |
|                                                                                           |
+-------------------------------------------------------------------------------------------+
| [jni]                          System                                                     |
|                                    loadLibrary()                                          |
|                                                                                           |
|                                                                                           |
|                                JNI_OnLoad()                                               |
|                                                                                           |
|                                JavaEnv                                                    |
|                                    RegisterNatives()                                      |
+-------------------------------------------------------------------------------------------+

```
#### 数据类型定义：
[JNI Types and Data Structures](https://docs.oracle.com/javase/7/docs/technotes/guides/jni/spec/types.html#wp9502)
- jdk/src/share/javavm/export/jni.h:

```
+----------------------------------------------------------------------------+
| Primitive Types                                                            |
|                                                                            |
|     Java Type    Native Type     Description                               | Type Signatures
|                                                                            |
|     boolean      jboolean        unsigned 8 bits      #define JNI_FALSE  0 |z
|                                                       #define JNI_TRUE   1 |
|     byte         jbyte           signed 8 bits                             |b
|     char         jchar           unsigned 16 bits                          |c
|     short        jshort          signed 16 bits                            |x
|     int          jint            signed 32 bits                            |i
|     long         jlong           signed 64 bits                            |j
|     float        jfloat          32 bits                                   |f
|     double       jdouble         64 bits                                   |d
|     void         void            N/A                                       |
+----------------------------------------------------------------------------+//other
|  Reference Types                                                           |
|     jobject                                                                |L fully-qualified-class ; fully-qualified-class
|         jclass                                                             |
|         jstring                                                            |
|         jarray                                                             |[ type type[]
|              jobjectArray                                                  |
|              jbooleanArray                                                 |
|              jbyteArray                                                    |
|              jshortArray                                                   |
|              jcharArray                                                    |
|              jintArray                                                     |
|              jlongArray                                                    |
|              jfloatArray                                                   |
|              jdoubleArray                                                  |
|         jthrowable                                                         |
+----------------------------------------------------------------------------+( arg-types ) ret-type method type


```


[jni methods](https://docs.oracle.com/javase/7/docs/technotes/guides/jni/spec/functions.html)
```
JavaVM *jvm;       /* denotes a Java VM */
JNIEnv *env;       /* pointer to native method interface */ 当前线程有效
(*env)->GetFieldID
+---------------------------------------------------------------------+-------------------------------------------------------------------------------------------+
|  Weak Global   jweak NewWeakGlobalRef(JNIEnv *env, jobject obj);    |                                                                                           |
|  References    void DeleteWeakGlobalRef(JNIEnv *env, jweak obj);    |   Java VM Interface   GetJavaVM                                                           |
|                                                                     |   Reflection Support  FromReflectedMethod     FromReflectedField                          |
+---------------------------------------------------------------------+                                                                                           |
|               jobject NewGlobalRef(JNIEn^ *en^, jobject obj);       |                       ToReflectedMethod       ToReflectedField                            |
|               void DeleteGlobalRef(JNIEnv *env, jobject globalRef); |                                                                                           |
|                                                                     |                                                                                           |
|  Global\      void DeleteLocalRef(JNIEnv *en^, jobject localRef);   |  NIO Support         NewDirectByteBuffer    GetDirectBufferAddress                        |
| Local         jint EnsureLocalCapacity(JNIEnv *env, jint capacity); |                      GetDirectBufferCapacity                                              |
| References    jint PushLocalFrame(JNIEnv *env, jint capacity);      |                                                                                           |
|               jobject PopLocalFrame(JNIEnv *env, jobject result);   |  Monitor             MonitorEnter           MonitorExit                                   |
|               jobject NewLocalRef(JNIEnv *env, jobject ref);        |                                                                                           |
|                                                                     |  Registering        RegisterNatives        UnregisterNatives                              |
+---------------------------------------------------------------------+  Native Methods                                                                           |
| Exceptions     ExceptionClear         Throw         ThrowNew        |                                                                                           |
|                FatalError             ExceptionOccurred             |                    GetArrayLength            Get<PrimitiveType>ArrayElements Routines     |
|                ExceptionCheck         ExceptionDescribe             |  Array Operations  SetObjectArrayElement                                                  |
|                                                                     |                    NewObjectArray            Release<PrimitiveType>ArrayElements Routines |
+---------------------------------------------------------------------+                    GetObjectArrayElement                                                  |
| Accessing          GetFieldID                                       |                    GetPrimitiveArrayCritical      Get<PrimitiveType>ArrayRegion Routines  |
| Fields of Objects  Get^type^Field Routines  Set^type^Field Routines |                    ReleasePrimitiveArrayCritical                                          |
+---------------------------------------------------------------------+                                                   Set<PrimitiveType>ArrayRegion Routines  |
|  Object        GetObjectClass            AllocObject                |                    New<PrimitiveType>Array Routines                                       |
|  Operations    GetObjectRefType          NewObject                  |                                                                                           |
|                IsInstanceOf              NewObjectA                 |                     GetStaticMethodID                CallStatic<type>Method Routines      |
|                IsSameObject              NewObjectV                 |  Calling                                                                                  |
+---------------------------------------------------------------------+  Static Methods     SetStatic<type>Field Routines    CallStatic<type>MethodA Routines     |
|                                                                     |                                                                                           |
|               GetMethodID      Call<type>Method Routines            |                                                      CallStatic<type>MethodV Routines     |
|                               CallNonvirtual<type>Method Routines   |                                                                                           |
| Calling                       CallNonvirtual<type>MethodA Routines  |  Accessing           GetStaticFieldID      GetStatic<type>Field Routines                  |
| Instance                                                            |  Static Fields                                                                            |
|  Methods                      CallNonvirtual<type>MethodV Routines  |                      NewStringUTF                                                         |
|                               Call<type>MethodA Routines            |                      GetStringUTFLength         GetStringRegion                           |
|                                         Call<type>MethodV Routines  |                      GetStringUTFChars                                                    |
|                                                                     |   String             ReleaseStringUTFChars      GetStringUTFRegion                        |
|               jclass DefineClass                                    |   Operations                                                                              |
| Class         jclass FindClass(JNIEnv *env, const char *name);      |                      NewString                  GetStringCritical                         |
| Operations    jclass GetSuperclass(JNIEnv *en^, jclass clazz);      |                      GetStringLength            ReleaseStringCritical                     |
|               jboolean IsAssignableFrom                             |                      GetStringChars                                                       |
| Version                                                             |                      ReleaseStringChars                                                   |
| Information   jint GetVersion(JNIEn^ *en^);                         |                                                                                           |
+---------------------------------------------------------------------+-------------------------------------------------------------------------------------------+

```
#### JNI签名
**JNICALL**表示调用约定，相当于C++的stdcall，说明调用的是本地方法
**JNIEXPORT**表示函数的链接方式，当程序执行的时候从本地库文件中找函数



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

### 数据学习 - 人工智能
[见 知识体系-人工智能.md](知识体系-人工智能.md)

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
Simple： A-Za-z0-9+/.

URL： A-Za-z0-9+_.

MIME： A-Za-z0-9+/=
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

### HTTP/HTTP2
[HTTP 1.1 RCF](https://tools.ietf.org/html/rfc2068)
[HTTP/2 RCF](https://tools.ietf.org/html/rfc7540)

[wiredshark抓包](https://blog.csdn.net/u014530704/article/details/78842000)
1. http contains  baidu.com （SSL 过滤 https）
2.  ip.src==47.95.165.112 or ip.dst==47.95.165.112

> HTTP/2。HTTP/2**支持明文传输**，而SPDY强制使用HTTPS；HTTP/2消息头的压缩算法采用HPACK，而非SPDY采用的DEFLATE。
相较于HTTP1.1，HTTP/2的主要优点有采用二进制帧封装，传输变成多路复用，流量控制算法优化，服务器端推送，首部压缩，优先级等特点。
> Hypertext Transfer Protocol Secure (HTTPS) is an extension of the Hypertext Transfer Protocol (HTTP) for secure communication over a computer network, and is widely used on the Internet.（Https使用了安全协议SSL）

> SSL更新到3.0时，IETF对SSL3.0进行了标准化，并添加了少数机制(但是几乎和SSL3.0无差异)，标准化后的IETF更名为TLS1.0(Transport Layer Security 安全传输层协议)，可以说TLS就是SSL的新版本3.1。

SSL使用40 位关键字作为RC4流加密算法，这对于商业信息的加密是合适的。

#### NPN和ALPN
NPN（Next Protocol Negotiation，下一代协议协商），是一个 TLS 扩展，由 Google 在开发 SPDY 协议时提出。随着 SPDY 被 HTTP/2 取代，NPN 也被修订为 ALPN（Application Layer Protocol Negotiation，应用层协议协商）。二者目标一致，但实现细节不一样，相互不兼容。以下是它们主要差别：

NPN 是服务端发送所支持的 HTTP 协议列表，由客户端选择；而 ALPN 是客户端发送所支持的 HTTP 协议列表，由服务端选择；
NPN 的协商结果是在 Change Cipher Spec 之后加密发送给服务端；而 ALPN 的协商结果是通过 Server Hello 明文发给客户端；

[](https://imququ.com/post/enable-alpn-asap.html)

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
## 数据完整性与安全
MD5
RSA


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