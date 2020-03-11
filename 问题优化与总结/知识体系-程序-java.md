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
+-------------------------+--------------------------------+-----------+-+------------+-------------+----------+
|                         |       |true ,10b(2) ,1f(float) |           | |            |             |          |
|  Whitespace(tab space)  |       |false, 01(8) ,1d(double)|   oops/.. | |            |             |          |
|  comment                | memory|        1(10),""(Str)   |  decision | |            |             | other    |
|  separator sign(;)      |       |      0x1(16),[](Arr)   |  Data Type| |            |             | symbol/  |
+                         |       |escape char   ADT       |           | |            |             | token    |
+---------------------------------+------------------------------------+-+            |             | token    |
|     separator           |  Literals(num)                 |  keywords   |  Operators | Identifiers |          |
+-------------------------+--------------------------------+-------------+------------+-------------+----------+
|                                            Character set (Unicode,UTF-8)                                     |
+--------------------------------------------------------------------------------------------------------------+
|                                            Byte                                                              |
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
17个0xffff，即0x10ffff
```
[unicode 及位置](https://unicode-table.com/en/#control-character)
 [emoji位置](https://unicode-table.com/en/#emoticons)  80个字符(1F600— 1F64F)
UCS-4 第1个字节2^7=128个group，第2个字节2^8=256个平面（plane）,第3个字节分为256行 （row），第4个字节代表每行有256个码位（cell）
unicode 有0x10FFFF个cell，分为 17平面，(2^8=256)行，(2^8=256)单元

```
              2^8=256 cells
          +----------------+

          128 ascii
          +-------+

      +   +----------------+----------------+----------------+       +----------------+----------------+----------------+
      |   ++ascii++        |                |                |       |                |                |                |
256   |   |                |                |                |  ...  |                |                |                |
lines |   |    plane 0     |    plane 1     |    plane 2     |       |     plane 14   |    plane 15    |    plane 16    |
      |   |     BMP        |      SMP       |      SIP       |       |       SSP      |     SPUA-A     |     SPUA-B     |
      +   |                |                |                |       |                |                |                |
          +----------------+----------------+----------------+       +----------------+----------------+----------------+

          +-------------------------------------------------------------------------------------------------------------+


                                                            17 panes   (0x10FFFF cells)

```
- 涉及类
  1. [Character.UnicodeBlock](https://en.wikipedia.org/wiki/Unicode_block)
- [Unicode与UTF-8转化](https://zh.wikipedia.org/wiki/UTF-8)
- [UTF-16](https://en.wikipedia.org/wiki/UTF-16) UTF-16编码（二进制）就是：110110yyyyyyyyyy 110111xxxxxxxxxx
  超过三个字节 Unicode 用四个字节的UTF-16编码
  
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
第一步：[176.0625换成二进制数](https://blog.csdn.net/crjmail/article/details/79723051)，
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
E 第一位（1：大于1的十进制，0：0~1之间的十进制）
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
|           +------------------------------+-------------------------+    | |                 | Frame          |Frame   | | |
|           |               | Resolution   |                         |    | |                 |   LVT          |        | | |
|Continuity |               +--------------+                         |    | |                 |   Operand Stack|        | | |
|Complete   | linking       | Preparation  |                         |    | |                 |   Frame Data   |        | | |
|           |               +--------------+                         |    | |                 |                |        | | |
|           |               | Verification |                         |    | |                 |                |        | | |
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
ObjectHeader64Coops
          +-            +------------------------+
          |             |     Mark Word          |   8Byte
Header    |             +------------------------+
          |             |     Klass Pointer      |   4Byte
          ++            +------------------------+
                        |     Array Length       |   4Byte
                        +------------------------+
                        |                        |
                        +------------------------+
                        |                        |
                        +------------------------+

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
|                 |-load_<n>  -store_<slot>               |lvt <-> OperandStack
|  stack operator |-const_<num>   -push -ldc  -pop   dup  |constant ->OperandStack     
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


### 2.1 数据集合  - Collection 类（List, Queue, Map）
数学-集合论
```text
                                            +--------------+                                          +----------------+
                                            |              |                                          |                |
                                            |   Collection |                                          |     Map        |
                                            +------+-------+                                          +-------^--------+
                                                   ^                                                     +-----------------------+------------+
                           +---------------------------------------------+                               +                       +            |
                           |                       |                     |                             .....                  ......          |
                   +-------+-------+        +------+------+       ++-----+-----+                 +--+---------+-+         ++-----------+      |    +---------------+
                   |unsort,no repeat        | sortable ,repeatable +                 +              +         +            +                  |    |               |
                   |   Set         +        |   List      +       +    Queue   +                 + HashMap      +         +  SortMap   +      |    |  Dictionary   |
                   +------+--------+        +-----+-------+       +-------+----+                 +-------+------+         +-------+----+      |    +---------------+
                          ^                       ^                       ^                              ^                        ^           |
       +--------------->  +                       + <---------------------+                              |                        |           |            ^
    .......           .......                   ....                      |                              |                        |           |            |
+--------------+   +---------------+  +-----------+  +---------+    +-----+-----------+         +--------+--------+        +------+------+    |  . +-------+--------+
|              |   |               |  |           |  |         |    |                 |         |                 |        |             |    |  . |                |
|  TreeSet     |   |   HashSet     |  | ArrayList |  | Vector  |    |   LinkedList    |         |   LinkedHashMap |        |   TreeMap   |    +--.-+   HashTable    |
+--------------+   +---------------+  +-----------+  +---------+    +-----------------+         +-----------------+        +-------------+       . +----------------+



```
#### ArrayList
```
java.util.ArrayList object internals:
 OFFSET  SIZE                 TYPE DESCRIPTION                               VALUE
      0     4                      (object header)                           05 00 00 00 (00000101 00000000 00000000 00000000) (5)
      4     4                      (object header)                           00 00 00 00 (00000000 00000000 00000000 00000000) (0)
      8     4                  int AbstractList.modCount                     0//在使用迭代器遍历集合的时候同时修改集合元素
     12     4                  int ArrayList.size                            0
     16     4   java.lang.Object[] ArrayList.elementData                     []
     20     4                      (loss due to the next object alignment)
Instance size: 24 bytes
Space losses: 0 bytes internal + 4 bytes external = 4 bytes total

java.util.ArrayList$Itr object internals:
 OFFSET  SIZE                  TYPE DESCRIPTION                               VALUE
      0     4                       (object header)                           05 00 00 00 (00000101 00000000 00000000 00000000) (5)
      4     4                       (object header)                           00 00 00 00 (00000000 00000000 00000000 00000000) (0)
      8     4                   int Itr.cursor                                0
     12     4                   int Itr.lastRet                               -1
     16     4                   int Itr.expectedModCount                      0
     20     4   java.util.ArrayList Itr.this$0                                (object)
     24     8                       (loss due to the next object alignment)
Instance size: 32 bytes
Space losses: 0 bytes internal + 8 bytes external = 8 bytes total

```
初始容量 10
加载因子（0.0~1.0）  超过容量1.0，执行扩容
扩容增量 
```
int newCapacity = oldCapacity + (oldCapacity >> 1);//整除则结果为 1.5倍，不能整除，结果为1.5倍加1

java.util.ArrayList object internals:
 OFFSET  SIZE                 TYPE DESCRIPTION                               VALUE
      0     4                      (object header)                           05 00 00 00 (00000101 00000000 00000000 00000000) (5)
      4     4                      (object header)                           00 00 00 00 (00000000 00000000 00000000 00000000) (0)
      8     4                  int AbstractList.modCount                     11
     12     4                  int ArrayList.size                            11
     16     4   java.lang.Object[] ArrayList.elementData                     [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, null, null, null, null]
     20     4                      (loss due to the next object alignment)
Instance size: 24 bytes
Space losses: 0 bytes internal + 4 bytes external = 4 bytes total
```
#### LinkedList
```
java.util.LinkedList object internals:
 OFFSET  SIZE                        TYPE DESCRIPTION                               VALUE
      0     4                             (object header)                           05 00 00 00 (00000101 00000000 00000000 00000000) (5)
      4     4                             (object header)                           00 00 00 00 (00000000 00000000 00000000 00000000) (0)
      8     4                         int AbstractList.modCount                     0
     12     4                         int LinkedList.size                           0
     16     4   java.util.LinkedList.Node LinkedList.first                          null //数据存放在双向链表
     20     4   java.util.LinkedList.Node LinkedList.last                           null
     24     8                             (loss due to the next object alignment)
Instance size: 32 bytes
Space losses: 0 bytes internal + 8 bytes external = 8 bytes total

java.util.LinkedList$ListItr object internals:
 OFFSET  SIZE                        TYPE DESCRIPTION                               VALUE
      0     4                             (object header)                           05 00 00 00 (00000101 00000000 00000000 00000000) (5)
      4     4                             (object header)                           00 00 00 00 (00000000 00000000 00000000 00000000) (0)
      8     4                         int ListItr.nextIndex                         0
     12     4                         int ListItr.expectedModCount                  0
     16     4   java.util.LinkedList.Node ListItr.lastReturned                      null
     20     4   java.util.LinkedList.Node ListItr.next                              null
     24     4        java.util.LinkedList ListItr.this$0                            (object)
     28     4                             (loss due to the next object alignment)
Instance size: 32 bytes
Space losses: 0 bytes internal + 4 bytes external = 4 bytes total

```
#### Vector

```
java.util.Vector object internals:
 OFFSET  SIZE                 TYPE DESCRIPTION                               VALUE
      0     4                      (object header)                           05 00 00 00 (00000101 00000000 00000000 00000000) (5)
      4     4                      (object header)                           00 00 00 00 (00000000 00000000 00000000 00000000) (0)
      8     4                  int AbstractList.modCount                     0
     12     4                  int Vector.elementCount                       0
     16     4                  int Vector.capacityIncrement                  0//自定义扩容大小
     20     4   java.lang.Object[] Vector.elementData                        [null, null, null, null, null, null, null, null, null, null]
     24     8                      (loss due to the next object alignment)
Instance size: 32 bytes
Space losses: 0 bytes internal + 8 bytes external = 8 bytes total

java.util.Vector$Itr object internals:
 OFFSET  SIZE               TYPE DESCRIPTION                               VALUE
      0     4                    (object header)                           05 00 00 00 (00000101 00000000 00000000 00000000) (5)
      4     4                    (object header)                           00 00 00 00 (00000000 00000000 00000000 00000000) (0)
      8     4                int Itr.cursor                                0
     12     4                int Itr.lastRet                               -1
     16     4                int Itr.expectedModCount                      0
     20     4   java.util.Vector Itr.this$0                                (object)
     24     8                    (loss due to the next object alignment)
Instance size: 32 bytes
Space losses: 0 bytes internal + 8 bytes external = 8 bytes total
```

初始容量 10
加载因子（0.0~1.0）  超过容量1.0，执行扩容
扩容增量 增加一倍，或者自定义。ArrayList比较省空间。
```
 int newCapacity = oldCapacity + ((capacityIncrement > 0) ?
                                         capacityIncrement : oldCapacity);

```

与ArrayList区别是，所有方法都加Synchronized，性能没有ArrayList高
#### HashSet
```
java.util.HashSet object internals:
 OFFSET  SIZE                TYPE DESCRIPTION                               VALUE
      0     4                     (object header)                           05 00 00 00 (00000101 00000000 00000000 00000000) (5)
      4     4                     (object header)                           00 00 00 00 (00000000 00000000 00000000 00000000) (0)
      8     4   java.util.HashMap HashSet.map                               (object)
     12     4                     (loss due to the next object alignment)
Instance size: 16 bytes
Space losses: 0 bytes internal + 4 bytes external = 4 bytes total

java.util.HashMap$KeyIterator object internals:
 OFFSET  SIZE                     TYPE DESCRIPTION                               VALUE
      0     4                          (object header)                           05 00 00 00 (00000101 00000000 00000000 00000000) (5)
      4     4                          (object header)                           00 00 00 00 (00000000 00000000 00000000 00000000) (0)
      8     4                      int HashIterator.expectedModCount             0
     12     4                      int HashIterator.index                        0
     16     4   java.util.HashMap.Node HashIterator.next                         null
     20     4   java.util.HashMap.Node HashIterator.current                      null
     24     4        java.util.HashMap HashIterator.this$0                       (object)
     28     4        java.util.HashMap KeyIterator.this$0                        (object)
     32     8                          (loss due to the next object alignment)
Instance size: 40 bytes
Space losses: 0 bytes internal + 8 bytes external = 8 bytes total
```
初始容量 （HashMap决定）16
加载因子（0.0~1.0）  0.75f
扩容增量  一倍
```

newCap = oldCap << 1
```
#### TreeSet
```
java.util.TreeSet object internals:
 OFFSET  SIZE                     TYPE DESCRIPTION                               VALUE
      0     4                          (object header)                           05 00 00 00 (00000101 00000000 00000000 00000000) (5)
      4     4                          (object header)                           00 00 00 00 (00000000 00000000 00000000 00000000) (0)
      8     4   java.util.NavigableMap TreeSet.m                                 (object) //默认TreeMap实现
     12     4                          (loss due to the next object alignment)
Instance size: 16 bytes
Space losses: 0 bytes internal + 4 bytes external = 4 bytes total

java.util.TreeMap$KeyIterator object internals:
 OFFSET  SIZE                      TYPE DESCRIPTION                               VALUE
      0     4                           (object header)                           05 00 00 00 (00000101 00000000 00000000 00000000) (5)
      4     4                           (object header)                           00 00 00 00 (00000000 00000000 00000000 00000000) (0)
      8     4                       int PrivateEntryIterator.expectedModCount     0
     12     4   java.util.TreeMap.Entry PrivateEntryIterator.next                 null
     16     4   java.util.TreeMap.Entry PrivateEntryIterator.lastReturned         null
     20     4         java.util.TreeMap PrivateEntryIterator.this$0               (object)
     24     4         java.util.TreeMap KeyIterator.this$0                        (object)
     28     4                           (loss due to the next object alignment)
Instance size: 32 bytes
Space losses: 0 bytes internal + 4 bytes external = 4 bytes total
```
初始容量 （HashMap决定）16
加载因子（0.0~1.0）  0.75f
扩容增量  一倍

#### HashMap
JDK7中HashMap采用的是位桶+链表的方式，即我们常说的散列链表的方式，而JDK8中采用的是位桶+链表/红黑树
```
java.util.HashMap object internals:
 OFFSET  SIZE                       TYPE DESCRIPTION                               VALUE
      0     4                            (object header)                           05 00 00 00 (00000101 00000000 00000000 00000000) (5)
      4     4                            (object header)                           00 00 00 00 (00000000 00000000 00000000 00000000) (0)
      8     4              java.util.Set AbstractMap.keySet                        null
     12     4       java.util.Collection AbstractMap.values                        null
     16     4                        int HashMap.size                              0
     20     4                        int HashMap.modCount                          0
     24     4                        int HashMap.threshold                         0//自增阀值，判断是否需要自增容量，等于容量*加载因子
     28     4                      float HashMap.loadFactor                        0.75
     32     4   java.util.HashMap.Node[] HashMap.table                             null// Node对象，包含key，value和下一个Node对象
     36     4              java.util.Set HashMap.entrySet                          null
     40     8                            (loss due to the next object alignment)
Instance size: 48 bytes
Space losses: 0 bytes internal + 8 bytes external = 8 bytes total


java.util.HashMap$EntryIterator object internals:
 OFFSET  SIZE                     TYPE DESCRIPTION                               VALUE
      0     4                          (object header)                           05 00 00 00 (00000101 00000000 00000000 00000000) (5)
      4     4                          (object header)                           00 00 00 00 (00000000 00000000 00000000 00000000) (0)
      8     4                      int HashIterator.expectedModCount             0
     12     4                      int HashIterator.index                        0
     16     4   java.util.HashMap.Node HashIterator.next                         null
     20     4   java.util.HashMap.Node HashIterator.current                      null
     24     4        java.util.HashMap HashIterator.this$0                       (object)
     28     4        java.util.HashMap EntryIterator.this$0                      (object)
     32     8                          (loss due to the next object alignment)
Instance size: 40 bytes
Space losses: 0 bytes internal + 8 bytes external = 8 bytes total
```

初始容量  16
加载因子（0.0~1.0）  0.75f
扩容增量（扩容hash表）  一倍
```

newCap = oldCap << 1
```

- 哈希碰撞
```
    static final int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);//位的亦或运算
    }
```
hash bucket 大小设置为 length=2^n。
2^n-1对应的二进制 1111...111，hash bucket的索引值是通过 hash & (tab.length - 1)，设置为2^n，减少哈希碰撞

- 链地址过长，（红黑树）树化
  TREEIFY_THRESHOLD = 8时，对哈希冲突链地址树化

```
哈希碰碰撞时的解决方法
1. 开放地址法（HashTable），包括 线性探测再散列，二次探测再散列，伪随机探测再散列
2. 链地址法（HashMap 用红黑树代替链表，加快搜索）
3. 再哈希法
4. 建立一个公共溢出区
```
[xorshift](http://csuncle.com/2018/08/03/梅森旋转安全性分析及改进/)
#### HashTable
开放地址法解决Hash冲突

```
java.util.Hashtable object internals:
 OFFSET  SIZE                          TYPE DESCRIPTION                               VALUE
      0     4                               (object header)                           05 00 00 00 (00000101 00000000 00000000 00000000) (5)
      4     4                               (object header)                           00 00 00 00 (00000000 00000000 00000000 00000000) (0)
      8     4                           int Hashtable.count                           0
     12     4                           int Hashtable.threshold                       8
     16     4                         float Hashtable.loadFactor                      0.75
     20     4                           int Hashtable.modCount                        0
     24     4   java.util.Hashtable.Entry[] Hashtable.table                           [null, null, null, null, null, null, null, null, null, null, null]
     28     4                 java.util.Set Hashtable.keySet                          null
     32     4                 java.util.Set Hashtable.entrySet                        null
     36     4          java.util.Collection Hashtable.values                          null
     40     8                               (loss due to the next object alignment)
Instance size: 48 bytes
Space losses: 0 bytes internal + 8 bytes external = 8 bytes total

java.util.Collections$EmptyIterator object internals:
 OFFSET  SIZE   TYPE DESCRIPTION                               VALUE
      0     4        (object header)                           05 00 00 00 (00000101 00000000 00000000 00000000) (5)
      4     4        (object header)                           00 00 00 00 (00000000 00000000 00000000 00000000) (0)
      8     8        (loss due to the next object alignment)
Instance size: 16 bytes
Space losses: 0 bytes internal + 8 bytes external = 8 bytes total

```
初始容量 11
加载因子（0.0~1.0）  0.75f
扩容增量  一倍+1
```

int newCapacity = (oldCapacity << 1) + 1;
```
#### LinkedHashMap

LinkedHashMap节点类 LinkedHashMapEntry 包含 before, after;
HashMap节点类 Node 包好 next;
```
java.util.LinkedHashMap object internals:
 OFFSET  SIZE                            TYPE DESCRIPTION                               VALUE
      0     4                                 (object header)                           05 00 00 00 (00000101 00000000 00000000 00000000) (5)
      4     4                                 (object header)                           00 00 00 00 (00000000 00000000 00000000 00000000) (0)
      8     4                   java.util.Set AbstractMap.keySet                        null
     12     4            java.util.Collection AbstractMap.values                        null
     16     4                             int HashMap.size                              0
     20     4                             int HashMap.modCount                          0
     24     4                             int HashMap.threshold                         0
     28     4                           float HashMap.loadFactor                        0.75
     32     4        java.util.HashMap.Node[] HashMap.table                             null
     36     4                   java.util.Set HashMap.entrySet                          null
     40     1                         boolean LinkedHashMap.accessOrder                 false  //相比Hashmap多出的部分，访问顺序排序，可以LinkedEntryIterator，打印出来。LinkedHashMapEntry，相比Hashmap新增before,after两个字段，用来排序。false则保存插入顺序，true则按访问顺序
     41     3                                 (alignment/padding gap)                  
     44     4   java.util.LinkedHashMap.Entry LinkedHashMap.head                        null  //相比Hashmap多出的部分,双向链表
     48     4   java.util.LinkedHashMap.Entry LinkedHashMap.tail                        null  //相比Hashmap多出的部分,双向链表
     52     4                                 (loss due to the next object alignment)
Instance size: 56 bytes
Space losses: 3 bytes internal + 4 bytes external = 7 bytes total

java.util.LinkedHashMap$LinkedEntryIterator object internals:
 OFFSET  SIZE                            TYPE DESCRIPTION                               VALUE
      0     4                                 (object header)                           05 00 00 00 (00000101 00000000 00000000 00000000) (5)
      4     4                                 (object header)                           00 00 00 00 (00000000 00000000 00000000 00000000) (0)
      8     4                             int LinkedHashIterator.expectedModCount       0
     12     4   java.util.LinkedHashMap.Entry LinkedHashIterator.next                   null
     16     4   java.util.LinkedHashMap.Entry LinkedHashIterator.current                null
     20     4         java.util.LinkedHashMap LinkedHashIterator.this$0                 (object)
     24     4         java.util.LinkedHashMap LinkedEntryIterator.this$0                (object)
     28     4                                 (loss due to the next object alignment)
Instance size: 32 bytes
Space losses: 0 bytes internal + 4 bytes external = 4 bytes total
```
初始容量（继承Hashmap）  16
加载因子（0.0~1.0）  0.75f
扩容增量（扩容hash表）  一倍
```

newCap = oldCap << 1
```


#### TreeMap
红黑树排序

初始容量 11
加载因子（0.0~1.0）  0.75f
扩容增量  一倍+1

红黑树平衡调整
二叉树搜索
```
public class TreeMap<K, V> extends AbstractMap<K, V> implements NavigableMap<K, V>, Cloneable, Serializable {
    private final Comparator<? super K> comparator;
    private transient TreeMap.Entry<K, V> root;
    private transient int size = 0;
    private transient int modCount = 0;
    private transient TreeMap<K, V>.EntrySet entrySet;
    private transient TreeMap.KeySet<K> navigableKeySet;
    private transient NavigableMap<K, V> descendingMap;
}


java.util.TreeMap object internals:
 OFFSET  SIZE                         TYPE DESCRIPTION                               VALUE
      0     4                              (object header)                           05 00 00 00 (00000101 00000000 00000000 00000000) (5)
      4     4                              (object header)                           00 00 00 00 (00000000 00000000 00000000 00000000) (0)
      8     4                java.util.Set AbstractMap.keySet                        null
     12     4         java.util.Collection AbstractMap.values                        null
     16     4                          int TreeMap.size                              0
     20     4                          int TreeMap.modCount                          0
     24     4         java.util.Comparator TreeMap.comparator                        null
     28     4      java.util.TreeMap.Entry TreeMap.root                              null
     32     4   java.util.TreeMap.EntrySet TreeMap.entrySet                          null
     36     4     java.util.TreeMap.KeySet TreeMap.navigableKeySet                   null
     40     4       java.util.NavigableMap TreeMap.descendingMap                     null
     44     4                              (loss due to the next object alignment)
Instance size: 48 bytes
Space losses: 0 bytes internal + 4 bytes external = 4 bytes total

java.util.TreeMap$EntryIterator object internals:
 OFFSET  SIZE                      TYPE DESCRIPTION                               VALUE
      0     4                           (object header)                           05 00 00 00 (00000101 00000000 00000000 00000000) (5)
      4     4                           (object header)                           00 00 00 00 (00000000 00000000 00000000 00000000) (0)
      8     4                       int PrivateEntryIterator.expectedModCount     1
     12     4   java.util.TreeMap.Entry PrivateEntryIterator.next                 (object)
     16     4   java.util.TreeMap.Entry PrivateEntryIterator.lastReturned         null
     20     4         java.util.TreeMap PrivateEntryIterator.this$0               (object)
     24     4         java.util.TreeMap EntryIterator.this$0                      (object)
     28     4                           (loss due to the next object alignment)
Instance size: 32 bytes
Space losses: 0 bytes internal + 4 bytes external = 4 bytes total


```
Charset#availableCharsets():SortedMap



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


## 2.4 数据并发访问 - 线程与并发

### 异步实现（多线程编程）
线程初始化三种方式： Thread,Runnable,Callable，Feature
线程的生命周期
```
                                       +---------------+
                            +----------+ Block         | <-----------------+   run synchronized
                            |          +---------------+                   |
                            |          +---------------+                   |
                            +----------+ Time_waitting | <-----------------+   sleep(),wait(ms),join(ms)
                            |          +---------------+                   |   LockSupport.parkNanos(),LockSupport.parkUntil()
                            |          +---------------+                   |
                            +----------+ Waitting      | <-----------------+   wait(),join(),LockSupport.park()
                            v          +---------------+                   |
                     +-------------------------------------------------------------+
+---------+          |   +-----------+         system call        +------------+   |      +--------------+
|         |          |   |           | +------------------------> |            |   |      |              |
|  New    | +------> |   |  Ready    |                            |  Running   |   +----> |  Terminated  |
|         |          |   |           |                            |            |   |      |              |
+---------+          |   +-----------+ <------------------------+ +------------+   |      +--------------+
                     |                          yield()                            |
                     +-------------------------------------------------------------+
                                                 Runnable

```

- 多线程
  线程通讯：volatile/synchronized，wait()/nofity()，pipe,join(),ThreadLocal
  线程（实现**异步**）
  线程池（实现**并发**） 
```
                         //ThreadPoolExecutor/ScheduledThreadPoolExecutor
                         +--------------------+---------------------------------+
                         | ThreadPoolExecutor |              keepAliveTime      |
                         +--------------------+              corePoolSize       |
                         | RejectedExecutionHandler                             |
   firstTask             |  ^                                                   |
   +-------------+       |  | >maximumPoolSize                                  |
   | Runnable    |       |  |   +----------------+                              |
   +-------------+       |  |   |Worker          | +-------> works:set<Workser> |
   | Callable    | +--------+-> | Thread         |                              |
   +-------------+       |      +----------------+                              |
   | FutureTask  |       +------------------------------------------------------+
   +-------------+                  |
                                    v            +-----------------------------------+
                         +----------+----+       | +--------------+ +--------------+ |
   queue Task            |  ThreadFactory| +---->+ |Thread(Worker)| |Thread(Worker)| |
   +-------------+       +---------------+       | +--------------+ +--------------+ |
   | Runnable    |                               +-----------------------------------+
   +-------------+                                  ^
   | Callable    |                                  | poll()
   +-------------+   offer()    +-------------------+----+   //SynchronousQueue,
   | FutureTask  | +--------->  |workQueue：BlockingQueue |   //LinkedBlockingDeque,
   +-------------+              +------------------------+   //ArrayBlockingQueue

```
- 并发：为了提高效率，减少时间，引入多线程实现并发，同时多线程带来些问题，包括共享变量（内存可见的happens-before原则，避免重排序），锁活跃性问题(死锁,饥饿、活锁) ,性能问题


- 风险
  1. 安全(原子性，可见性，有序性)
  2. 跳跃性
  3. 性能（上下文切换，死锁，资源限制）

### 并发底层实现
```
+-------------+--------------+----------------------+------------------+------------------------+-----------------------+
|             |              |                      |                  |                        |                       |
| Object      |  DCL problem |    synchronized      |  Object.wait()   |                        |                       |
|             |              | (Reentrant,unfair)   |  Object.notify() |                        |                       |
|             |              | (Exclusi^e,pessimism)|                  |                        |                       |
+----------------------------+-----------------------------------------+------------------------+-----------------------+
|             | J.U.C.atomic                                                                                            |
|  volatile   +---------------------------------------------------------------------------------------------------------+
|             | ConcurrentLinkedDeque                                                                                   |
|             | ConcurrentSkipListMap                                                                                   |
|             +---------------------------------------------------------------------------------------------------------+
|             |              |ReentrantReadWriteLock                                                                    |
|             |     AQS      |(shared Read)                                                                             |
|             |              |StampedLock                                                                               |
|             |              +----------------------+------------------+------------------------+-----------------------+
|             |              |                      |   Condition      |    CountDownLatch      |   ArrayBlockingQueue  |
|    CAS      |              |                      |                  |    CyclicBarrier       |   LinkedBlockingQueue |
|             |              |  ReentrantLock       |                  |                        |                       |
|             |              |(Exclusi^e,optimistic)|                  |   Semaphore,Exchanger  |   ConcurrentHashMap   |
|             |              |                      |                  |                        |   CopyOnWriteArrayList|
|             |              |                      |                  |                        |                       |
|             |              |                      |                  |                        |   Fork/Join           |
+-------------+--------------+----------------------+------------------+------------------------+-----------------------+



```

  1. volatile（内存可见性，其他线程看到的value值都是最新的value值，即修改之后的volatile的值; 指令有序序，解决重排序） + cas 原子操作(atomic operation)是不需要synchronized，不会被线程调度机制打断的操作。
  2. synchronized。当synchronized锁住一个对象后，别的线程如果也想拿到这个对象的锁，就必须等待这个线程执行完成释放锁，才能再次给对象加锁，这样才达到线程同步的目的。
  3. synchronized(Sync.class)/ static synchronized方法 为全局锁，相当于锁住了代码段。限制多线程中该类的**所有实例**同时访问jvm中该类所对应的代码块。
上下文切换查看工具 **vmstat**,**LMbench**

#### 内置锁（synchronized）
 [从jvm源码看synchronized](https://www.cnblogs.com/kundeg/p/8422557.html)
    - 静态的锁顺序死锁。一个线程执行a方法且已经获得了A锁，在等待B锁；另一个线程执行了b方法且已经获得了B锁，在等待A锁。这种状态，就是发生了静态的锁顺序死锁。
    - [动态的锁顺序死锁](https://www.androidos.net.cn/codebook/AndroidRoad/java/concurrence/deadlock.md)
    ```java
    //可能发生动态锁顺序死锁的代码
    class DynamicLockOrderDeadLock {
        public void transefMoney(Account fromAccount, Account toAccount, Double amount) {
            synchronized (fromAccount) {
                synchronized (toAccount) {
                    //...
                    fromAccount.minus(amount);
                    toAccount.add(amount);
                    //...
                }
            }
        }
    }
    ```

    -  协作对象之间发生的死锁
- 内置的锁 synchronized，可重入非公平锁（是独占锁，是一种悲观锁），会导致饥饿效应，不可中断

  - Object.wait(),Object.notify()

#### volatile 与 CAS
  定义：meaning that writes to this field are immediately made visible to other threads.
  [正确使用 Volatile 变量](https://www.ibm.com/developerworks/cn/java/j-jtp06197.html)
  保证修改的值会立即被更新到主存，当有其他线程需要读取时，它会去内存中读取新值。在某些情况下性能要优于synchronized，但对变量的写操作不依赖于当前值。
[volatile的读写操作的过程: ](https://blog.csdn.net/jiuqiyuliang/article/details/62216574)

```

（1）线程写volatile变量的过程：  
         1、改变线程工作内存中volatile变量的副本的值  
         2、将改变后的副本的值从工作内存刷新到主内存  
（2）线程读volatile变量的过程：  
        1、从主内存中读取volatile变量的最新值到线程的工作内存中  
        2、从工作内存中读取volatile变量的副本 
```

#### volatile 内存模型
 volatile 内存语义， 重排序,顺序一致性
 内存可见性、volatile锁 
 final 内存语义，读写重排序规则
 hanpen before，指两个操作指令的执行顺序

 可以保证变量的可见性 ，不能保证变量状态的“原子性操作”，原子性操作需要lock或cas
#### 原子操作类：采用 volatile和[Unsafe#Unsafe_CompareAndSwapInt](/home/anshu/workspace/openjdk/hotspot/src/share/vm/prims)的CAS原子操作
```
java.util.concurrent.atomic.AtomicInteger object internals:
 OFFSET  SIZE   TYPE DESCRIPTION                               VALUE
      0     4        (object header)                           05 00 00 00 (00000101 00000000 00000000 00000000) (5)
      4     4        (object header)                           00 00 00 00 (00000000 00000000 00000000 00000000) (0)
      8     4    int AtomicInteger.value                       0 //volatile修饰
     12     4        (loss due to the next object alignment)
Instance size: 16 bytes
Space losses: 0 bytes internal + 4 bytes external = 4 bytes total

```

```

java.util.concurrent.atomic.AtomicStampedReference object internals:
 OFFSET  SIZE                                                      TYPE DESCRIPTION                               VALUE
      0     4                                                           (object header)                           05 00 00 00 (00000101 00000000 00000000 00000000) (5)
      4     4                                                           (object header)                           00 00 00 00 (00000000 00000000 00000000 00000000) (0)
      8     4   java.util.concurrent.atomic.AtomicStampedReference.Pair AtomicStampedReference.pair               (object)  //volatile修饰
     12     4                                                           (loss due to the next object alignment)
Instance size: 16 bytes
Space losses: 0 bytes internal + 4 bytes external = 4 bytes total

java.util.concurrent.atomic.AtomicStampedReference$Pair object internals:
 OFFSET  SIZE               TYPE DESCRIPTION                               VALUE
      0     4                    (object header)                           01 00 00 00 (00000001 00000000 00000000 00000000) (1)
      4     4                    (object header)                           00 00 00 00 (00000000 00000000 00000000 00000000) (0)
      8     4                int Pair.stamp                                0  //final修饰
     12     4   java.lang.Object Pair.reference                            0  //final修饰
     16     8                    (loss due to the next object alignment)
Instance size: 24 bytes
Space losses: 0 bytes internal + 8 bytes external = 8 bytes total
```

```
java.util.concurrent.atomic.AtomicReference object internals:
 OFFSET  SIZE               TYPE DESCRIPTION                               VALUE
      0     4                    (object header)                           01 00 00 00 (00000001 00000000 00000000 00000000) (1)
      4     4                    (object header)                           00 00 00 00 (00000000 00000000 00000000 00000000) (0)
      8     4   java.lang.Object AtomicReference.value                     null //volatile修饰
     12     4                    (loss due to the next object alignment)
Instance size: 16 bytes
Space losses: 0 bytes internal + 4 bytes external = 4 bytes total
```

AtomicInteger的cas原理
```
jdk/src/share/classes/sun/misc/Unsafe.java
public final int getAndAddInt(Object o, long offset, int delta) {
    int v;
    do {
        v = getIntVolatile(o, offset);//读取线程共享主存的最新值
    } while (!compareAndSwapInt(o, offset, v, v + delta));//没有置换成功，继续循环直到竞争成功
    return v;
}

hotspot/src/share/vm/prims/unsafe.cpp
UNSAFE_ENTRY(jboolean, Unsafe_CompareAndSwapInt(JNIEnv *env, jobject unsafe, jobject obj, jlong offset, jint e, jint x))
  UnsafeWrapper("Unsafe_CompareAndSwapInt");
  oop p = JNIHandles::resolve(obj);
  jint* addr = (jint *) index_oop_from_field_offset_long(p, offset);
  return (jint)(Atomic::cmpxchg(x, addr, e)) == e;//只需要关注到这。将主存的副本和线程的新值传进去，后续cpu进行CAS操作。查看是否把主存原来的值置换出来，新增的值置换到主存
UNSAFE_END 

```
  - 乐观锁思想-CAS原子操作。修改前，对比直到共享内存和当前值（当前线程临时内存）一致，才做修改，这个流程不会加锁阻塞（《Java 并发编程的艺术》2.3节）
      - AtomicStampedReference来解决ABA问题；
      - 循环时间长开销大
      - AtomicReference类来多个共享变量合成一个共享变量来操作

  

#### AQS 实现 **乐观锁**
```java
public abstract class AbstractQueuedSynchronizer{
    /**
     * Head of the wait queue, lazily initialized.  Except for
     * initialization, it is modified only via method setHead.  Note:
     * If head exists, its waitStatus is guaranteed not to be
     * CANCELLED.
     */
    private transient volatile Node head;

    /**
     * Tail of the wait queue, lazily initialized.  Modified only via
     * method enq to add new wait node.
     */
    private transient volatile Node tail;

    /**
     * The synchronization state.
     */
    private volatile int state; 
}
```

```
1. 独占式获取与释放同步状态
2. 共享式获取与释放同步状态
3. 查询同步队列中的等待线程情况
```

**重入锁** ReentrantLock（**独享锁**,**互斥锁**）/ReentrantReadWriteLock（**读锁是共享锁，其写锁是独享锁**）/StampedLock （内部通过 AbstractQueuedSynchronizer同步器，实现**公平锁和非公平锁**，AbstractQueuedSynchronizer包含Condition，使用volatile修饰的state变量维护同步状态），解决复杂锁问题，如先获得锁A，然后再获取锁B，当获取锁B后释放锁A同时获取锁C，当锁C获取后，再释放锁B同时获取锁D。


CountDownLatch，CyclicBarrier，Semaphore，Exchanger （这类使用AQS）
- ReentrantLock: 使用了AQS的独占获取和释放,用state变量记录某个线程获取独占锁的次数,获取锁时+1，释放锁时-1，在获取时会校验线程是否可以获取锁。
- Semaphore: 使用了AQS的共享获取和释放，用state变量作为计数器，只有在大于0时允许线程进入。获取锁时-1，释放锁时+1。
- CountDownLatch: 使用了AQS的共享获取和释放，用state变量作为计数器，在初始化时指定。只要state还大于0，获取共享锁会因为失败而阻塞，直到计数器的值为0时，共享锁才允许获取，所有等待线程会被逐一唤醒
  
```
java.util.concurrent.locks.ReentrantLock object internals:
 OFFSET  SIZE                                            TYPE DESCRIPTION                               VALUE
      0     4                                                 (object header)                           01 00 00 00 (00000001 00000000 00000000 00000000) (1)
      4     4                                                 (object header)                           00 00 00 00 (00000000 00000000 00000000 00000000) (0)
      8     4   java.util.concurrent.locks.ReentrantLock.Sync ReentrantLock.sync                        (object)  //AQS子类
     12     4                                                 (loss due to the next object alignment)
Instance size: 16 bytes
Space losses: 0 bytes internal + 4 bytes external = 4 bytes total

java.util.concurrent.locks.ReentrantLock$NonfairSync object internals://继承AQS，没有加字段，只是重写lock()，tryRequire()方法
 OFFSET  SIZE                                                         TYPE DESCRIPTION                                        VALUE
      0     4                                                              (object header)                                    01 00 00 00 (00000001 00000000 00000000 00000000) (1)
      4     4                                                              (object header)                                    00 00 00 00 (00000000 00000000 00000000 00000000) (0)
      8     4                                             java.lang.Thread AbstractOwnableSynchronizer.exclusiveOwnerThread   null //竞争到的线程
     12     4                                                          int AbstractQueuedSynchronizer.state                   0     // volatile 修饰符
     16     4   java.util.concurrent.locks.AbstractQueuedSynchronizer.Node AbstractQueuedSynchronizer.head                    null
     20     4   java.util.concurrent.locks.AbstractQueuedSynchronizer.Node AbstractQueuedSynchronizer.tail                    null
     24     8                                                              (loss due to the next object alignment)
Instance size: 32 bytes
Space losses: 0 bytes internal + 8 bytes external = 8 bytes total

 

 
java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject object internals://new ReentrantLock().newCondition()
 OFFSET  SIZE                                                         TYPE DESCRIPTION                               VALUE
      0     4                                                              (object header)                           05 00 00 00 (00000101 00000000 00000000 00000000) (5)
      4     4                                                              (object header)                           00 00 00 00 (00000000 00000000 00000000 00000000) (0)
      8     4   java.util.concurrent.locks.AbstractQueuedSynchronizer.Node ConditionObject.firstWaiter               null
     12     4   java.util.concurrent.locks.AbstractQueuedSynchronizer.Node ConditionObject.lastWaiter                null
     16     4        java.util.concurrent.locks.AbstractQueuedSynchronizer ConditionObject.this$0                    (object)
     20     4                                                              (loss due to the next object alignment)
Instance size: 24 bytes
Space losses: 0 bytes internal + 4 bytes external = 4 bytes total



+-------------------------------------------------------------------------------------------+
|                           await()                             doBussiness()               |
|                           +---------------------------------+                             |
|             +-------------+   block                         +----------->                 |
|    WorkThread             +---------------------------------+                             |
|                                                                                           |
|                                                                                           |
|   CountDownLatch(2)          1                              0                             |
|   +--------------------------------------------------------->                             |
|                                                                                           |
|                              ^                              ^                             |
|    Thread 1                  | cutdown()                    |                             |
|             +----------------+--->                          |                             |
|    Thread 2                                                 | cutdown()                   |
|             +-----------------------------------------------+-------->                    |
|                                                                                           |
+-------------------------------------------------------------------------------------------+

+-------------------------------------------------------------------------------------------+
|                                                                                           |
|   CyclicBarrier(3)                                                                        |
|                           2      1                            0                           |
|   +----------------------------------------------------------->                           |
|                                                                                           |
|   Thread 1                await()                                                         |
|            doneSomething()+-----------------------------------+ doOther()                 |
|   +-----------------------+            block                  +----------------->         |
|                           +-----------------------------------+                           |
|                                                                                           |
|   Thread 2                       await()                                                  |
|                 doneSomething()   +---------------------------+ doOther()                 |
|   +-------------------------------+     block                 +----------------->         |
|                                   +---------------------------+                           |
|   Thread 3                                                                                |
|                                          doneSomething()    await()                       |
|   +----------------------------------------------------------------------------->         |
|                                                                 doOther()                 |
|                                                                                           |
+-------------------------------------------------------------------------------------------+


+-------------------------------------------------------------------------------------------+
|                                                                                           |
|  Semaphore                                                                                |
|             1    2                        1                       0                       |
|  +----------------------------------------------------------------------------->          |
|                                                                                           |
|  Thread 1              doSyncWork()                                                       |
|           acquire()                   release()                                           |
|  +----------------------------------------------------------------------------->          |
|                                                                                           |
|  Thread 2    acquire()                                        release()                   |
|                +--------------------------+    doSyncWork()                               |
|  +-------------+       block              +------------------------------------>          |
|                +--------------------------+                                               |
|                                                                                           |
+-------------------------------------------------------------------------------------------+

```

- 生产者与消费者。Condition配合ReentrantLock，实现了wait()/notify()（阻塞与通知）



#### 并发集合 (ArrayBlockingQueue,LinkedBlockingQueue)，Fork/Join框架，工具类
  - ConcurrentHashMap。有并发要求，使用该类替换HashTable
java 7 **分段锁**技术,java 8 摒弃了Segment（锁段）的概念，采用CAS + synchronized保证并发更新的安全性，底层采用数组+链表+红黑树的存储结构。
```
java.util.concurrent.ConcurrentHashMap object internals:
 OFFSET  SIZE                                                   TYPE DESCRIPTION                               VALUE
      0     4                                                        (object header)                           05 00 00 00 (00000101 00000000 00000000 00000000) (5)
      4     4                                                        (object header)                           00 00 00 00 (00000000 00000000 00000000 00000000) (0)
      8     4                                          java.util.Set AbstractMap.keySet                        null
     12     4                                   java.util.Collection AbstractMap.values                        null
     16     8                                                   long ConcurrentHashMap.baseCount               0
     24     4                                                    int ConcurrentHashMap.sizeCtl                 0 //volatile 并发利用CAS算法
     28     4                                                    int ConcurrentHashMap.transferIndex           0
     32     4                                                    int ConcurrentHashMap.cellsBusy               0
     36     4          java.util.concurrent.ConcurrentHashMap.Node[] ConcurrentHashMap.table                   null
     40     4          java.util.concurrent.ConcurrentHashMap.Node[] ConcurrentHashMap.nextTable               null//用于迁移到table属性的临时属性
     44     4   java.util.concurrent.ConcurrentHashMap.CounterCell[] ConcurrentHashMap.counterCells            null//用于并行计算每个bucket的元素数量。
     48     4      java.util.concurrent.ConcurrentHashMap.KeySetView ConcurrentHashMap.keySet                  null
     52     4      java.util.concurrent.ConcurrentHashMap.ValuesView ConcurrentHashMap.values                  null
     56     4    java.util.concurrent.ConcurrentHashMap.EntrySetView ConcurrentHashMap.entrySet                null
     60     4                                                        (loss due to the next object alignment)
Instance size: 64 bytes
Space losses: 0 bytes internal + 4 bytes external = 4 bytes total

```
桶初始容量  16
加载因子（0.0~1.0）  0.75f
扩容增量（扩容hash桶）  增加一倍
树化
帮助数据迁移：将原来的 tab 数组的元素迁移到新的 nextTab 数组中。在多线程条件下，当前线程检测到其他线程正进行扩容操作（Thread.yield()），则协助其一起进行数据迁移。扩容后  sizeCtl = (n << 1) - (n >>> 1);



  - 并发框架 Fork/Join

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
  001_hotspot         5522af6c5028c6cfdf382b78796dd7ac77eb1bbd Initial load
  002_jdk             ce0984a87ec46c2ffe3ed80d51ed350826331197 Initial load
  006_langtools       84e2484ba645990f4c35e60d08db791806ae40be Initial load
* 007_init_load_merge 0d206a7adbbc58f8b70c96d1b65da1e391c62474 Merge


