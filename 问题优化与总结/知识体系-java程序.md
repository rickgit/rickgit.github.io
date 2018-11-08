# Android 高级工程师知识体系
程序：一些列数据结构和指令
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
                         |        |            +    Float
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



### 指令 - 操作符
Byte通过加法实现加减，移位和加法实现乘除法

- 原码，反码，补码
  正数：原码，反码，补码一致
  负数：反码符号位不变其他位按位取反，补码为反码加1

  原码：加负数，不是预期值（1-1= 00000001^1000001（原码）=10000010=-2）
  反码：正数原码加负数的原码，计算的结果不是想要的值（1-1= 00000001（反）^11111110（反码）=11111111（反）=10000000（原））
  补码：正数加负数的反码，符号位不对，用补码可以正确（1-1= 00000001（补）^11111111(补码)=00000000=0）
- [补码原理：同余](https://www.cnblogs.com/baiqiantao/p/7442907.html)
  负数取模：A mod b= A-B*Math.floor（A/B）
  
  1. 反码，实际上是这个数对于一个膜的同余数；而这个膜并不是我们的二进制，而是所能表示的最大值
  2. 反码的基础上+1，只是相当于增加了膜的值
```
byte （byte范围 -128~127）取反求值，相当于值 (a+b) mod 127
    取补求值，相当于（a+b） mod 128，保证不会溢出
```

### 数据 - 大数操作
只要你的计算机的内存足够大，可以有无限位的



常见构造大数方法，指数，阶乘，高德纳箭头

- BigInteger、BigDecimal
[-2^(2147483647*32-1) ，2^(2147483647*32-1)-1]

### 数据 - 引用类型
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

- 虚拟机，垃圾回收器，回收算法
1. 对象的创建
2. Java内存区域与内存模型
3. Java类加载机制及类加载器
4. Java垃圾回收算法及垃圾收集器
5. JVM判断对象是否已死（根节点没有引用）
>《Inside the Java Virtual Machine》


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
[HotSpot 虚拟机](http://openjdk.java.net/groups/hotspot/docs/HotSpotGlossary.html)

Generational Collection（分代收集）算法
  1. 新生代都采取Copying算法
  2. 老年代的特点是每次回收都只回收少量对象，一般使用的是Mark-Compact算法
  3. 永久代（Permanet Generation），它用来存储class类、常量、方法描述等。对永久代的回收主要回收两部分内容：废弃常量和无用的类。

####  类的相关概念（数据封装 - 面向对象）
- 类与对象
字段，方法，构造方法，方法参数
包

- 封装，继承，多态，父类与子类，重载与重写，与抽象类，访问修饰符
this,super

equals()，hashcode()，toString()
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
                                ^                                                     +-----------------------+
        +---------------------------------------------+                               |                       |
        |                       |                     |                             .....                  ......
+-------+-------+        +------+------+       +------+-----+                 +--------------+         +------------+
|无序的、不可重复|        |有序的、可重复 |       |            |                 |              |         |            |
|   Set         |        |   List      |       |    Queue   |                 | HashMap      |         |  SortMap   |
+------+--------+        +-----+-------+       +-------+----+                 +-------+------+         +-------+----+
       ^                       ^                       ^                              ^                        ^
     ..^..                     | <---------------------+                              |                        |
       |                     ....                      |                              |                        |
+------+--------+  +-----------+  +---------+    +-----+-----------+         +--------+--------+        +------+------+
|               |  |           |  |         |    |                 |         |                 |        |             |
|   HashSet     |  | ArrayList |  | Vector  |    |   LinkedList    |         |   LinkedHashMap |        |   TreeMap   |
+---------------+  +-----------+  +---------+    +-----------------+         +-----------------+        +-------------+


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
扩容增量 增加一倍，或者自定义
```
 int newCapacity = oldCapacity + ((capacityIncrement > 0) ?
                                         capacityIncrement : oldCapacity);

```
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
红黑树平衡调整
二叉树搜索

#### SortedMap
Charset#availableCharsets():SortedMap


 
### 2.3 数据访问 - 流（IO, NIO）
字符与字节
UTF-8 
RandomAccessFile

BUffer,Channel ,Selector

- 文件（xml,json）

- 网络
查看 **计算机网络**
- 数据库
  
### 2.4 数据并发访问 - 多线程与并发
线程初始化 Thread,Runnable
线程的生命周期
```

```

- 多线程
  
  线程池：ThreadPoolExecutor
    - 任务队列 SynchronousQueue,LinkedBlockingDeque（常用）,ArrayBlockingQueue
    - 任务 FutureTask（get方法会阻塞，知道FutureTask执行结束）,Callable,
- 并发是为了提高效率，减少时间，引入多线程实现并发，同时多线程带来些问题，包括共享变量，锁活跃性问题(死锁,饥饿、活锁) ,性能问题

- 死锁
    
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
- volatile。保证修改的值会立即被更新到主存，当有其他线程需要读取时，它会去内存中读取新值。在某些情况下性能要优于synchronized，但对变量的写操作不依赖于当前值。

```
volatile的读写操作的过程: 
（1）线程写volatile变量的过程：  
         1、改变线程工作内存中volatile变量的副本的值  
         2、将改变后的副本的值从工作内存刷新到主内存  
（2）线程读volatile变量的过程：  
        1、从主内存中读取volatile变量的最新值到线程的工作内存中  
        2、从工作内存中读取volatile变量的副本
--------------------- 
作者：于亮 
来源：CSDN 
原文：https://blog.csdn.net/jiuqiyuliang/article/details/62216574 
版权声明：本文为博主原创文章，转载请附上博文链接！

```
- 内置的锁 synchronized，可重入非公平锁（是独占锁，是一种悲观锁），会导致饥饿效应，不可中断
- 重入锁 ReentrantLock（内部通过 AbstractQueuedSynchronizer实现公平锁和非公平锁，AbstractQueuedSynchronizer包含Condition），解决复杂锁问题，如先获得锁A，然后再获取锁B，当获取锁B后释放锁A同时获取锁C，当锁C获取后，再释放锁B同时获取锁D。
- 生产者与消费者。Condition配合ReentrantLock，实现了wait()/notify()
- 乐观锁思想-CAS原子操作。修改前，对比直到共享内存和当前值（当前线程临时内存）一致，才做修改，这个流程不会加锁阻塞
    - AtomicStampedReference来解决ABA问题；
    - 循环时间长开销大
    - AtomicReference类来保证引用对象之间的原子性
    
- 并发集合 ArrayBlockingQueue,LinkedBlockingQueue,
    - ConcurrentHashMap。有并发要求，使用该类替换HashTable
### 面向对象与设计模式
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
[C内存模型](https://blog.csdn.net/ufolr/article/details/52833736)
```
                                               ++   char
                                               |
                                  +IntegerType ++   int
                                  | +short
                                  | |long
                                  | |sign
                         +  Number| +unsign
                         |  Type  |
                         |        |            +    Float
                         |        +DecimalType |
                         |                     ++   Long
                         |
                         |
                         |
                         |
            ++Base      ++
            | Type
            |
            |            +  pointerType
            |            |
            |            |
C data type |            |  functionType
            |  derived   |
            |  Type      |               +   Array
            |            |               |
            |            |  AggregateType|
            |            |               +   Struct
            |            |
            |            +  union
            | 
            |
            | EnumType
            |
            |
            |
           ++ VoidType


                                  +IntegerType      int
                                  | +short
                                  | |long
                                  | |sign
                         +  Number| +unsign
                         |  Type  |
                         |        |            +    Float
                         |        +DecimalType |
                         |                     ++   Long
                         |
                         |
                         |
                         |
            ++Predifine  ++ Char  +  Char
            | Typeifine  |  Type  |
            |            | +sign  +  wchar_t
            |            | +unsign
            |            |
            |            ++ boolean
            |            |
            |            ++ void
            |
            |            +  Arr
C++         |            |
data type   | derived+-+ |  Struct
            | Type       |
            |            |  Union
            |            |
            |            ++ Enum
            |
            |            +  Addr（&）
            | PointerType|
            |            +  Pointer（*）
            |
            |
            + Class （String class）



```

## 4 算法与数据结构
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

## 6 计算机网络
- TCP/IP
- HTTP/HTTPS
http2.0支持长连接
## 7 操作系统
参考 Brian Ward,《How Linux works - what every superuser should know》[M].No Starch Press(2014)

7.1 用户进程
7.2 内核（进程，内存，设备驱动，系统调用 System Call）
7.3 硬件（CPU，内存，磁盘，网络端口 ）

## 8 数据库与SQL
三大范式

事务 ACID

### Sql 
- 多表查询（内连接，外连接）
- 嵌套查询
  与IN、ALL、ANY、EXISTS配合使用。
- 派生表查询
```sql
SELECT * FROM student,(
    SELECT sno FROM SC WHERE cno=1//派生查询
) AS tempSC
WHERE student.sno = tempSC.sno

```
- 集合查询
  UNION、UNION ALL、INTERSECT、EXCEPT