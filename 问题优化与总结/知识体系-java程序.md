# Android 高级工程师知识体系
程序：一些列数据结构和指令（《计算机程序设计艺术》）
软件：计算机系统中程序和文档的总称。
文档：需求开发计划（排期）及进度，设计说明书（架构图，项目结构图），规范文档（命名，注释），功能预演文档，开发总结文档，用户指南(使用手册)
## 1 数据 - 通信
数据与二进制
- 字长
  [字长](http://www.cnblogs.com/chakyu/p/7405275.html)
  [进制转化](https://www.branah.com/ascii-converter)
- 字节与字符编码
《The Unicode® Standard Version 9.0 》
[unicode 及位置](https://unicode-table.com/en/#control-character)
- 涉及类
  1. [Character.UnicodeBlock](https://en.wikipedia.org/wiki/Unicode_block)

- [Unicode与UTF-8转化](https://zh.wikipedia.org/wiki/UTF-8)
- [UTF-16](https://en.wikipedia.org/wiki/UTF-16)
  超过三个字节 Unicode 用四个字节的UTF-16编码
  
```
  System.out.println("a".getBytes(StandardCharsets.UTF_16).length);//结果为4，是因为加上BOM(字节顺序标记(ByteOrderMark))大端序，用FEFF表示，FEFF占用两个字节。
```

  大端，高位字节存储在内存地址的低位地址

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
> 《Core Java》（延展阅读《C Primer Plus》《C++ Primer》）
-- 基本语法
注释，关键字、标识符、常量、字符串值，或者是一个符号

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
            ++primitive  ++ Char
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
  jdk 9采用压缩字符串，都是 Iso-8891，使用一个字节，否则用 UTF-16 编码。

- Boolean
- Byte



### 指令 - 运算符
Byte通过加法实现加减，移位和加法实现乘除法

- 原码，反码，补码
  正数：原码，反码，补码一致
  负数：反码符号位不变其他位按位取反，补码为反码加1（取反加一，两个过程符号位都不变）
```
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

- 位运算
**~** 按位取反


### 数据 - 大数操作
只要你的计算机的内存足够大，可以有无限位的



常见构造大数方法，指数，阶乘，高德纳箭头，葛立恒数和Tree(3)
3^3=3

- BigInteger、BigDecimal
[-2^(2147483647*32-1) ，2^(2147483647*32-1)-1]

### 数据 - 引用类型
```
dx --dex --output=Hello.dex Hello.class

javap -c -classpath . Hello
```
运行时数据区：线程共用：方法区，堆；线程独立分配：栈，本地方法栈，程序计数器 
```


```
[jol查看内存结构](http://hg.openjdk.java.net/code-tools/jol/file/tip/jol-samples/src/main/java/org/openjdk/jol/samples/)
- 数组 
  
Arrays.sort 双轴快排算法（包含归并排序算法，经典快速排序算法，插入排序算法混用，及**jdk 1.7**废弃掉的归并排序和插入排序混用）

```ObjectHeader64Coops
          +-            +------------------------+
          |             |                        |
          |             |     Mark Word          |   8Byte
          |             |                        |
Header    |             +------------------------+
          |             |                        |
          |             |     Klass Pointer      |   4Byte
          |             |                        |
          ++            +------------------------+
                        |     Array Length       |   4Byte
                        |                        |
                        +------------------------+
                        |                        |
                        |                        |
                        |                        |
                        |                        |
                        +------------------------+
                        |                        |
                        |                        |
                        +------------------------+

```

- [类的内存大小](https://segmentfault.com/a/1190000007183623)

```ObjectHeader64Coops
          +-            +------------------------+
          |             |                        |
          |             |     Mark Word          |   8Byte
          |             |                        |
Header    |             +------------------------+
          |             |                        |
          |             |     Klass Pointer      |   4Byte
          |             |                        |
          ++            +------------------------+
                        |                        |
                        |                        |
                        |                        |
                        |                        |
                        |                        |
                        |                        |
                        |                        |
                        +------------------------+
                        |                        |
                        |                        |
                        +------------------------+


```

[内存占用查看工具](https://segmentfault.com/a/1190000007183623)

以下是 64bit电脑，打印的信息。markword 8bytes,
```
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
```
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


ObjectHeader64
|------------------------------------------------------------------------------------------------------------|--------------------|

|                                            Object Header (128 bits)                                        |        State       |

|------------------------------------------------------------------------------|-----------------------------|--------------------|

|                                  Mark Word (64 bits)                         |    Klass Word (64 bits)     |                    |

|------------------------------------------------------------------------------|-----------------------------|--------------------|

| unused:25 | identity_hashcode:31 | unused:1 | age:4 | biased_lock:1 | lock:2 |    OOP to metadata object   |       Normal       |

|------------------------------------------------------------------------------|-----------------------------|--------------------|

| thread:54 |       epoch:2        | unused:1 | age:4 | biased_lock:1 | lock:2 |    OOP to metadata object   |       Biased       |

|------------------------------------------------------------------------------|-----------------------------|--------------------|

|                       ptr_to_lock_record:62                         | lock:2 |    OOP to metadata object   | Lightweight Locked |

|------------------------------------------------------------------------------|-----------------------------|--------------------|

|                     ptr_to_heavyweight_monitor:62                   | lock:2 |    OOP to metadata object   | Heavyweight Locked |

|------------------------------------------------------------------------------|-----------------------------|--------------------|

|                                                                     | lock:2 |    OOP to metadata object   |    Marked for GC   |

|------------------------------------------------------------------------------|-----------------------------|--------------------|


ObjectHeader64Coops

|--------------------------------------------------------------------------------------------------------------|--------------------|

|                                            Object Header (96 bits)                                           |        State       |

|--------------------------------------------------------------------------------|-----------------------------|--------------------|

|                                  Mark Word (64 bits)                           |    Klass Word (32 bits)     |                    |

|--------------------------------------------------------------------------------|-----------------------------|--------------------|

| unused:25 | identity_hashcode:31 | cms_free:1 | age:4 | biased_lock:1 | lock:2 |    OOP to metadata object   |       Normal       |

|--------------------------------------------------------------------------------|-----------------------------|--------------------|

| thread:54 |       epoch:2        | cms_free:1 | age:4 | biased_lock:1 | lock:2 |    OOP to metadata object   |       Biased       |

|--------------------------------------------------------------------------------|-----------------------------|--------------------|

|                         ptr_to_lock_record                            | lock:2 |    OOP to metadata object   | Lightweight Locked |

|--------------------------------------------------------------------------------|-----------------------------|--------------------|

|                     ptr_to_heavyweight_monitor                        | lock:2 |    OOP to metadata object   | Heavyweight Locked |

|--------------------------------------------------------------------------------|-----------------------------|--------------------|

|                                                                       | lock:2 |    OOP to metadata object   |    Marked for GC   |

|--------------------------------------------------------------------------------|-----------------------------|--------------------|
```
- 字符串

```

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


- 引用数据类型回收（虚拟机，垃圾回收器，回收算法）
1. 对象的创建
2. Java内存区域与内存模型
```
+--------------------------------+--------------------------------------------+
|                                |                                            |
|                                |                                            |
|                                |                                            |
|     +------------------+       |  +--------------+   +-------------------+  |
|     |                  |       |  |              |   |                   |  |
|     |  Heap            |       |  |  VM Stack    |   |  Native Method    |  |
|     |                  |       |  |              |   |  Stack            |  |
|     +------------------+       |  +--------------+   +-------------------+  |
|                                |                                            |
|                                |                                            |
|                                |                                            |
|                                |                                            |
|     +------------------+       |  +--------------------------------------+  |
|     |                  |       |  |                                      |  |
|     |  Method Area     |       |  |  Program Couter Register             |  |
|     |                  |       |  |                                      |  |
|     +------------------+       |  +--------------------------------------+  |
|                                |                                            |
|                                |                                            |
|                                |                                            |
+--------------------------------+--------------------------------------------+

```
```
运行时数据区

        JavaStack  Heap
             ^     ^
Method Zone  |     |
        ^    |     |
        |    |     |
        +    +     +
      Object o = new Object()    +-----------> ProgramCounter//放执行当前指令的地址



      int    i = 0

                 +
                 |
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
3. Java类加载机制及类加载器
4. Java垃圾回收算法及垃圾收集器
   标记-清除算法(mark-sweep), dalvikvm
   标记-压缩算法(mark-compact),
   复制算法(copying)  hotspot
   引用计数算法(reference counting)
5. JVM判断对象是否已死（根节点没有引用）
>《Inside the Java Virtual Machine》



[HotSpot 虚拟机](http://openjdk.java.net/groups/hotspot/docs/HotSpotGlossary.html)

Generational Collection（分代收集）算法
  1. 新生代都采取Copying算法
  2. 老年代的特点是每次回收都只回收少量对象，一般使用的是Mark-Compact算法
  3. 永久代（Permanet Generation），它用来存储class类、常量、方法描述等。对永久代的回收主要回收两部分内容：废弃常量和无用的类。

####  类的相关概念（数据封装，信息结构，复杂数据 - 面向对象）
《Effect java》
- 类与对象
字段，方法，构造方法，方法参数
包

- 封装，继承，多态，父类与子类，重载与重写，与抽象类，访问修饰符
this,super

equals()，hashcode()，toString()
##### [hashcode() 基础知识](https://www.cnblogs.com/mengfanrong/p/4034950.html)
混合hash （MD5）

自动装箱和拆箱

- 枚举类，接口，Lambda表达式，内部类
- 反射，代理，注解接口，泛型
1. 泛型：Generics in Java is similar to templates in C++.
集合容器和网络请求经常用到

2. 动态代理

#### 异常，断言，日志

### 2.1 数据集合 - Collection 类（List, Queue, Map）

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
#### TreeMap
红黑树排序

初始容量 11
加载因子（0.0~1.0）  0.75f
扩容增量  一倍+1

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
红黑树平衡调整
二叉树搜索

#### SortedMap
Charset#availableCharsets():SortedMap


 
### 2.3 数据访问 - 流（IO, NIO）
文件（xml,json），内存，网络，数据库

字符与字节
UTF-8 
RandomAccessFile

#### NIO（BUffer,Channel ,Selector）

  
### 2.4 数据并发访问 - 线程与并发
线程初始化 Thread,Runnable
线程的生命周期
```
                                       +---------------+
                                       |               |
                            +----------+ Block         | <-----------------+   run synchronized
                            |          |               |                   |
                            |          +---------------+                   |
                            |          +---------------+                   |
                            |          |               |                   |
                            +----------+ Time_waitting | <-----------------+   sleep(),wait(ms),join(ms)
                            |          |               |                   |
                            |          +---------------+                   |   LockSupport.parkNanos(),LockSupport.parkUntil()
                            |          +---------------+                   |
                            |          |               |                   |
                            +----------+ Waitting      | <-----------------+   wait(),join(),LockSupport.park()
                            |          |               |                   |
                            v          +---------------+                   |
                     +-------------------------------------------------------------+
+---------+          | +------------+            yield()             +-----------+ |      +--------------+
|         |          | |            |   +------------------------>   |           | |      |              |
|  New    | +------> | | Runnable   |                                |  Ready    | +----> |  Terminated  |
|         |          | |            |                                |           | |      |              |
+---------+          | +------------+   <------------------------+   +-----------+ |      +--------------+
                     |          (Running)        system call                       |
                     +-------------------------------------------------------------+


```

- 多线程
  线程通讯：volatile/synchronized，wait()/nofity()，pipe,join(),ThreadLocal
 
  线程池：ThreadPoolExecutor/ScheduledThreadPoolExecutor
    - 任务队列 SynchronousQueue,LinkedBlockingDeque（常用）,ArrayBlockingQueue
    - 任务 FutureTask（get方法会阻塞，知道FutureTask执行结束）,Callable
  
- 并发：为了提高效率，减少时间，引入多线程实现并发，同时多线程带来些问题，包括共享变量（内存可见的happens-before原则，避免重排序），锁活跃性问题(死锁,饥饿、活锁) ,性能问题


- 风险
  1. 安全(原子性，可见性，有序性)
  2. 跳跃性
  3. 性能（上下文切换，死锁，资源限制）

#### 并发底层实现
  1. volatile（内存可见性，其他线程看到的value值都是最新的value值，即修改之后的volatile的值） + cas 原子操作(atomic operation)是不需要synchronized，不会被线程调度机制打断的操作。
  2. synchronized
上下文切换查看工具 **vmstat**,**LMbench**

#### 内置锁（synchronized）
    
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

  

#### AQS
重入锁 ReentrantLock/ReentrantReadWriteLock/StampedLock （内部通过 AbstractQueuedSynchronizer同步器，实现公平锁和非公平锁，AbstractQueuedSynchronizer包含Condition，使用volatile修饰的state变量维护同步状态），解决复杂锁问题，如先获得锁A，然后再获取锁B，当获取锁B后释放锁A同时获取锁C，当锁C获取后，再释放锁B同时获取锁D。


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
```

- 生产者与消费者。Condition配合ReentrantLock，实现了wait()/notify()（阻塞与通知）



#### 并发集合 (ArrayBlockingQueue,LinkedBlockingQueue)，Fork/Join框架，工具类
  - ConcurrentHashMap。有并发要求，使用该类替换HashTable
  - 并发框架 Fork/Join

### 数据展示 - 图形化及用户组件
awt/swing

#### 多媒体
[图形化](知识体系-多媒体.md)

### 数据类结构 - 类实现面向对象与设计模式
 五大基本原则：单一职责原则（接口隔离原则），开放封闭原则，Liskov替换原则，依赖倒置原则，良性依赖原则

- 创建型
  - 构建者模式
  
  Notification，AlertDialog，StringBuilder 和StringBuffer，OKhttp构建Request，Glide

  - 单例模式
  
  Application，LayoutInflater 
  - 工厂方法
  
  BitmapFactory
  - 原型模式
  - 抽象工厂
- 结构型
  - 适配器模式（Adapter）
  - 装饰模式（Retrofit）
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
  - 模板方法
  - 策略模式

    时间插值器，如LinearInterpolator
  - 状态模式
  - 责任链

    对事件的分发处理，很多启动弹窗
  - 备忘录模式

    onSaveInstanceState和onRestoreInstanceState
  - 迭代器模式（集合迭代器）
  - 解释器模式

    PackageParser
  - 访问者模式
  - 中介者模式

    Binder机制
 




### 2.5 C语言，NDK，汇编及CPU处理二进制
> [知识体系-C&CPP程序.md](./知识体系-C&CPP程序.md)
###jni
####数据类型定义：
- jdk/src/share/javavm/export/jni.h:

```
#ifdef __cplusplus
extern "C" {
#endif

/*
 * JNI Types
 */

#ifndef JNI_TYPES_ALREADY_DEFINED_IN_JNI_MD_H

typedef unsigned char   jboolean;
typedef unsigned short  jchar;
typedef short           jshort;
typedef float           jfloat;
typedef double          jdouble;

typedef jint            jsize;

#ifdef __cplusplus

class _jobject {};
class _jclass : public _jobject {};
class _jthrowable : public _jobject {};
class _jstring : public _jobject {};
class _jarray : public _jobject {};
class _jbooleanArray : public _jarray {};
class _jbyteArray : public _jarray {};
class _jcharArray : public _jarray {};
class _jshortArray : public _jarray {};
class _jintArray : public _jarray {};
class _jlongArray : public _jarray {};
class _jfloatArray : public _jarray {};
class _jdoubleArray : public _jarray {};
class _jobjectArray : public _jarray {};

typedef _jobject *jobject;
typedef _jclass *jclass;
typedef _jthrowable *jthrowable;
typedef _jstring *jstring;
typedef _jarray *jarray;
typedef _jbooleanArray *jbooleanArray;
typedef _jbyteArray *jbyteArray;
typedef _jcharArray *jcharArray;
typedef _jshortArray *jshortArray;
typedef _jintArray *jintArray;
typedef _jlongArray *jlongArray;
typedef _jfloatArray *jfloatArray;
typedef _jdoubleArray *jdoubleArray;
typedef _jobjectArray *jobjectArray;

#else

struct _jobject;

typedef struct _jobject *jobject;
typedef jobject jclass;
typedef jobject jthrowable;
typedef jobject jstring;
typedef jobject jarray;
typedef jarray jbooleanArray;
typedef jarray jbyteArray;
typedef jarray jcharArray;
typedef jarray jshortArray;
typedef jarray jintArray;
typedef jarray jlongArray;
typedef jarray jfloatArray;
typedef jarray jdoubleArray;
typedef jarray jobjectArray;

#endif

typedef jobject jweak;

typedef union jvalue {
    jboolean z;
    jbyte    b;
    jchar    c;
    jshort   s;
    jint     i;
    jlong    j;
    jfloat   f;
    jdouble  d;
    jobject  l;
} jvalue;

struct _jfieldID;
typedef struct _jfieldID *jfieldID;

struct _jmethodID;
typedef struct _jmethodID *jmethodID;

/* Return values from jobjectRefType */
typedef enum _jobjectType {
     JNIInvalidRefType    = 0,
     JNILocalRefType      = 1,
     JNIGlobalRefType     = 2,
     JNIWeakGlobalRefType = 3
} jobjectRefType;


#endif /* JNI_TYPES_ALREADY_DEFINED_IN_JNI_MD_H */

/*
 * jboolean constants
 */

#define JNI_FALSE 0
#define JNI_TRUE 1

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

### 人工智能
[见 知识体系-人工智能.md](知识体系-人工智能.md)

## 6 计算机网络
网络数据：报文格式
- Socket 
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

### XMPP/MQTT
见[知识总结-im篇](./知识体系-im.md)
### RPC

##### 基础
欧拉公式、费马小定理、中国剩余定理，可以说是三大加密算法的基础

三大公钥加密算法（RSA、离散对数、椭圆曲线）都依赖数论与群论的知识

常见 MD5,DES,RSA 算法
#### SSL/TLS

**Content Type**指示SSL通信处于哪个阶段，分为 ：握手(Handshake)，开始加密传输(ChangeCipherSpec)，正常通信(Application)，（EncryptedAlert）四种

**version** 
0: SSL 3.0，
1: TLS 1.0，
2: TLS 1.1,
3: TLS 2.1

**handshanke** ：当**Content Type**为握手阶段的标识，有以下几个过程
```
0 HelloRequest
1 ClientHello
2 ServerHello
11 Certificate
12 ServerKeyExchange
13 CertificateRequest
14 ServerHelloDone
15 CertificateVerify
16 ClientKeyExchange
20 Finished
```

**Cipher Suit**分为 四部分
```
密钥交换算法，用于决定客户端与服务器之间在握手的过程中如何认证，用到的算法包括RSA，Diffie-Hellman，ECDH，PSK等，非对称加密算法主要用于 交换对称加密算法的密钥，而非数据交换
加密算法，用于加密消息流，该名称后通常会带有两个数字，分别表示密钥的长度和初始向量的长度，比如DES 56/56, RC2 56/128, RC4 128/128, AES 128/128, AES 256/256
报文认证信息码（MAC）算法，用于创建报文摘要，确保消息的完整性（没有被篡改），算法包括MD5，SHA，SHA256等。
PRF（伪随机数函数），用于生成“master secret”。

```

例如：TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA

```
基于TLS协议的；
使用ECDHE、RSA作为密钥交换算法；
加密算法是AES（密钥和初始向量的长度都是256）；
MAC算法（这里就是哈希算法）是SHA。
``` 
[密钥协商类型一，RSA](https://blog.csdn.net/andylau00j/article/details/54583769)
[RSA 证明过程](https://www.di-mgt.com.au/rsa_theory.html)
[RSA key生成过程 RSAKeyPairGenerator](openjdk/jdk/src/share/classes/sun/security/rsa/RSAKeyPairGenerator.java)介绍 n,p,q（p > q）,φ(n),e（RSAKeyGenParameterSpec.F4=65537）,d的胜场

```
public KeyPair generateKeyPair() {
        // accommodate odd key sizes in case anybody wants to use them
        int lp = (keySize + 1) >> 1;
        int lq = keySize - lp;
        if (random == null) {
            random = JCAUtil.getSecureRandom();
        }
        BigInteger e = publicExponent;
        while (true) {
            // generate two random primes of size lp/lq
            BigInteger p = BigInteger.probablePrime(lp, random);
            BigInteger q, n;
            do {
                q = BigInteger.probablePrime(lq, random);
                // convention is for p > q
                if (p.compareTo(q) < 0) {
                    BigInteger tmp = p;
                    p = q;
                    q = tmp;
                }
                // modulus n = p * q
                n = p.multiply(q);
                // even with correctly sized p and q, there is a chance that
                // n will be one bit short. re-generate the smaller prime if so
            } while (n.bitLength() < keySize);

            // phi = (p - 1) * (q - 1) must be relative prime to e
            // otherwise RSA just won't work ;-)
            BigInteger p1 = p.subtract(BigInteger.ONE);
            BigInteger q1 = q.subtract(BigInteger.ONE);
            BigInteger phi = p1.multiply(q1);
            // generate new p and q until they work. typically
            // the first try will succeed when using F4
            if (e.gcd(phi).equals(BigInteger.ONE) == false) {//不是互质，重新找p,q
                continue;
            }

            // private exponent d is the inverse of e mod phi
            BigInteger d = e.modInverse(phi);  //e*d 与phi欧拉函数互质

            // 1st prime exponent pe = d mod (p - 1)
            BigInteger pe = d.mod(p1);
            // 2nd prime exponent qe = d mod (q - 1)
            BigInteger qe = d.mod(q1);

            // crt coefficient coeff is the inverse of q mod p
            BigInteger coeff = q.modInverse(p);

            try {
                PublicKey publicKey = new RSAPublicKeyImpl(n, e);
                PrivateKey privateKey =
                        new RSAPrivateCrtKeyImpl(n, e, d, p, q, pe, qe, coeff);
                return new KeyPair(publicKey, privateKey);
            } catch (InvalidKeyException exc) {
                // invalid key exception only thrown for keys < 512 bit,
                // will not happen here
                throw new RuntimeException(exc);
            }
        }
    }

```

```
1. 客户端连上服务端
2. 服务端发送 CA 证书给客户端
3. 客户端验证该证书的可靠性
4. 客户端从 CA 证书中取出公钥
5. 客户端生成一个随机密钥 k，并用这个公钥加密得到 k'
6. 客户端把 k' 发送给服务端
7. 服务端收到 k' 后用自己的私钥解密得到 k
8. 此时双方都得到了密钥 k，协商完成。
```

密钥协商类型二，hm（离散对数问题）
```
1. 客户端先连上服务端
2. 服务端生成一个随机数 y1 作为自己的私钥，然后根据算法参数计算出公钥 b1（算法参数通常是固定的）
3. 服务端使用某种签名算法把“算法参数（模数p，基数g）和服务端公钥b1”作为一个整体进行签名
4. 服务端把“算法参数（模数p，基数a）、服务端公钥b1、签名”发送给客户端
5. 客户端收到后验证签名是否有效
6. 客户端生成一个随机数 y2 作为自己的私钥，然后根据算法参数计算出公钥 b2
7. 客户端把 b2 发送给服务端
8. 客户端和服务端（根据上述 DH 算法）各自计算出 k 作为会话密钥


定义（数论中同余，指数，原根等概念）：
a^y1≡b1 (mod p)
a^y2≡b2 (mod p)

计算：
K=(b2)^y1 mod p
或
K=(b1)^y2 mod p
```
密钥协商类型三，ECDH（ 依赖的是——求解“椭圆曲线离散对数问题”的困难。）
密钥协商类型四，PSK 
```
　　在通讯【之前】，通讯双方已经预先部署了若干个共享的密钥。
　　为了标识多个密钥，给每一个密钥定义一个唯一的 ID
　　协商的过程很简单：客户端把自己选好的密钥的 ID 告诉服务端。
　　如果服务端在自己的密钥池子中找到这个 ID，就用对应的密钥与客户端通讯；否则就报错并中断连接。
```
密钥协商类型五，SRP 
```
 client/server 双方共享的是比较人性化的密码（password）而不是密钥（key）。该算法采用了一些机制（盐/salt、随机数）来防范“嗅探/sniffer”或“字典猜解攻击”或“重放攻击”。
```
[SSL/TLS 协议报文](https://www.cnblogs.com/findumars/p/5929775.html)
```
+---------------------------------------------------------------+
|                                                               |
|                    APPLICATION PROTOCOL                       |
|                                                               |
+-----------------+----------------------+----------------------+
|                 |                      |                      |
|  SSL HandSHARK  |SSL Change Cipher Spec|   SSL Alert Protocol |
+-----------------+----------------------+----------------------+
|                                                               |
|                      SSL RECORD（封装SSL/TLS握手协议或http数据）|
+---------------------------------------------------------------+
|                                                               |
|                      TCP                                      |
|                                                               |
+---------------------------------------------------------------+
|                                                               |
|                                                               |
|                      IP                                       |
|                                                               |
+---------------------------------------------------------------+

```

```
             +------------+                            +------------+
             |            |                            |            |
             |   Client   |                            |   Ser^er   |
             |            |                            |            |
             +-----+------+                            +------+-----+
                   |                                          |
                   |             Client Hello                 +
                   |            （Support CipSuites,Client RN）+
                   |    +---------------------------------->  +
                   |                                          |
                   |                                          |
                   |                                          | Choose CipherSuites
                   |                                          |
                   |             Server Hello                 | (Ser^er RN,Choosed chip)
                   |                                          |
                   |                                          |
                   |             Certificate                  | (CA and Key Exchange Pubkey)
                   |             Server Key Exchange          | (encrypted Pubkey,Signature Hash key)
                   |                                          |
                   |             Server Hello DONE            |
                   |    <---------------------------------+   |
                   |                                          |
Client RN,Server RN|                                          |
to generate        |                                          |
premaster secret   |                                          |
                   | (premaster secret,Signature Hash key)    |
                   |             Client Key Exchange          |
                   |    +--------------------------------->   |
msg Pubkey=        |               ChangeCipherSpec           |(notify use encrypted meg)
Client RN          |              encrypted handshake msg     |
+Server RN         |                                          |
+premaster secret  |                                          |
                   |                                          |
                   |                                          |
                   |                                          |
                   |   <---------------------------------+    |
                   |            ChangeCipherSpec              |
                   |            encrypted handshake msg       |
                   |                                          |
                   |                                          |
                   |                                          |
                   +                                          +




```

## 7 操作系统
参考 Brian Ward,《How Linux works - what every superuser should know》[M].No Starch Press(2014)

7.1 用户进程
7.2 内核（进程，内存，设备驱动，系统调用 System Call）
7.3 硬件（CPU，内存，磁盘，网络端口 ）

32位系统的最大寻址空间是2 的32次方= 4294967296（bit）= 4（GB）左右

## 8 数据库与SQL
三大范式

事务 ACID

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