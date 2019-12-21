## 
python 语言特性：动态强类型  

动态类型语言：在运行期进行类型检查的语言，也就是在编写代码的时候可以不指定变量的数据类型
强类型语言，一个变量不经过强制转换，它永远是这个数据类型，不允许隐式的类型转换。

大数据

```

+------------------+-------------------------------------------------------------------------------------------+
|  security        |                                                                                           |
+--------------------------------------------------------------------------------------------------------------+
|  perfomance      | memory(leaks,chunk)                                                                       |
+--------------------------------------------------------------------------------------------------------------+
|  test            |                                                                                           |
+--------------------------------------------------------------------------------------------------------------+
|                  | Built-in Functions         Data Types                         File Formats                |(https://docs.python.org/3/library/)
|  API /MOdule     | Built-in Constants         Numeric and Mathematical Modules   Cryptographic Services         |
|                  | Built-in Types             Functional Programming Modules     Generic os Services      |
|                  | Built-in Exceptions        File and Directory Access          Concurrent Execution                    |
|                  | Text Processing Services   Data Persistence                   contextvars           |
|                  | Binary Data Services       Data Compression and Archiving     Networking and Interprocess Communication       |
|                  |                                                               Internet Data Handling|
|                  |                                                               Structured Markup Processing Tools|
|                  |                                                              Internet Protocols and Support|
|                  |                                                             Multimedia Services|
|                  |                                                              Internationalization|
|                  |                                                              Program Frameworks|
|                  |                                                              Graphical User Interfaces with Tk|
|                  |                                                              Development Tools|
|                  |                                                              Debugging and Profiling|
|                  |                                                              Software Packaging and Distribution|
|                  |                                                              Python Runtime Services|
|                  |                                                              Custom Python Interpreters|
|                  |                                                              Importing Modules|
|                  |                                                              Python Language Services|
|                  |                                                              Miscellaneous Services|
|                  |                                                              MS Windows Specific Services|
|                  |                                                              Unix Specific Services|
|                  |                                                             Superseded Modules|
|                  |                                                             Undocumented Modules|
+--------------------------------------------------------------------------------------------------------------+
|                  |                                                                                           |
|                  +-------------------------------------------------------------------------------------------+
|  comunicate      |       String           List      Serializable                               C&C++         |
|                  | Thread/ThreadLocal  concurrent    File             Socket     db                          |
|                  |  Reflect/Annotation                                                                       |
+------------------+------------------------+------------+---------+-----------+-------+-----------------------+
|Class-based|concurrency|Aspect|            |            |         |           |       |                       |
| Log/Date  |           |      |            |            |         |           |       |                       |
+-----------+-----------+------+------+     |            |         |Annotation |       |                       |
|        oop                          | pop | Functional | FRP     |Reflecti^e |Generic|                       |
+------------------------------+------+------------------+-----------------------------+ Event+driven          |
|            Structured        | Imperative |  Declarative         |  Metaprogramming  |                       |
+------------------------------+------------+----------------------+-------------------+-----------------------+
|                             conditional/decision|making/loops                                                |
+-------------------------+--------------------------------+-----------+-+------------+-------------+----------+
|                         |       |true ,10b(2) ,1f(float) |           | |            |             |          |
|  Whitespace(tab space)  |       |false, 01(8) ,1d(double)|   oops/.. | |            |             |          |
|  comment                | memory|        1(10),""(Str)   |  decision | |            |             | other    |
|  separator sign(;)      |       |      0x1(16),[](Arr)   |  Data Type| |            |             | symbol/  |
+                         +       +              ADT       +           + +            |             | token    |
+---------------------------------+------------------------------------+-+            |             | token    |
|     separator           |  Literals                      |  keywords   |  Operators | Identifiers |          |
+-------------------------+--------------------------------+-------------+------------+-------------+----------+
|                                            Character set (Unicode,UTF-8)                                     |
+--------------------------------------------------------------------------------------------------------------+
|                                            Byte                                                              |
+--------------------------------------------------------------------------------------------------------------+
```


## 关键字与符号


```python
help("keywords")
或者
import keyword
print(keyword.kwlist)

                                                   
+----------+-----------------------------------------------------------+
|          |                                                           |
|exception/|                                                           |
| assert   |  raise   try    except   assert   finally  with           |
|          |                                                           |
+----------------------------------------------------------------------+
|  func    |  import  from  del as   lambda  yield                     |
|          |                                                           |
| class    |   class                                                   |
|          |                                                           |
| module   |  nonlocal   global                                        |
+----------------------------------------------------------------------+
|          |  if    elif   else   and     not   or   is                |
| flow     |                                                           |
|          |  for  in      while                                       |
| control  |                                                           |
|          |  break  continue  return   pass                           |
+----------------------------------------------------------------------+
|identifier|  def                                                      |
|          |                                                           |
+----------------------------------------------------------------------+
|  literal |  False  True  None                                        |
|          |                                                           |
+----------+-----------------------------------------------------------+


```

``` Operators
+-------------+--------------------------------------------------------------+
|             |                                                              |
|  Misc       |  in, not in ,is is not                                       |
|             |                                                              |
+----------------------------------------------------------------------------+
|             |                                                              |
|  Assignment |  = += -= *= /= %=                                            |
|             |  **= //=                                                     |
+----------------------------------------------------------------------------+
|             |                                                              |
|  Logical    |  and or not                                                  |
|             |                                                              |
+----------------------------------------------------------------------------+
|             |                                                              |
|  Bitwise    |  & | ^  ~    < <   > >                                       |
|             |                                                              |
+----------------------------------------------------------------------------+
|             |                                                              |
|  Relational |  == != > < >= <=                                             |
|             |  <>                                                          |
+----------------------------------------------------------------------------+
|             |                                                              |
|  Arithmetic |  + - * / %                                                   |
|             |  ** //                                                       |
+-------------+--------------------------------------------------------------+


```
##  数据类型
 
```
          +--+   Byte
          |
          +--+   Booleans
          |
          |
+------------+   Numbers int, float, complex¶
|         |
|         |
|         +--+   Strings
|         |
|         |
|         +----+ Tuples
|
|
|         +--+   Lists
|         |
|         |
+------------+   Sets
          |
          |
          +---+  Dictionaries

```

```python
struct _longobject {
    PyObject_VAR_HEAD //24 byte
    digit ob_digit[1];
};


from sys import getsizeof
   
class A(object): pass
class B: pass

for x in (None, 1, 1L, 1.2, 'c', [], (), {}, set(), B, B(), A, A()):
  print "{0:20s}\t{1:d}".format(type(x).__name__, sys.getsizeof(x))



NoneType                16
int                     24
long                    28
float                   24
str                     34
list                    64
tuple                   48
dict                    272
set                     224
classobj                96
instance                64
type                    896
A                       56
```

## 数据类型 - 函数
内置函数
``` 
+-----------+---------------------------------------------------------------+
| decore    |  property      classmethod    staticmethod                    |
|           |                                                               |
+---------------------------------------------------------------------------+
|  compile  |  compile   eval     exec     repr                             |
|           |                                                               |
+---------------------------------------------------------------------------+
|  file     |  open                                                         |
|           |                                                               |
+---------------------------------------------------------------------------+
|  io       |  print   input                                                ++
|           |                                                               |
+---------------------------------------------------------------------------+
|  var      |  globals   locals                                             |
|           |                                                               |
+---------------------------------------------------------------------------+
|  refector |   __import__   isinstance   issubclass   hasattr  getattr     |
            |                                                               |
|           |   setattr    delattr  callable                                |
+---------------------------------------------------------------------------+
|  object   |  help   dir   id  hash   type   len   ascii  format           |
|           |                                                               |
+---------------------------------------------------------------------------+
| serialable| all    any   filter    map  next   reversed   sorted  zip     |
|           |                                                               |
+---------------------------------------------------------------------------+
|           |  bool   int  float   complex bin oct  hex   bytearray  bytes  |
|           |                                                               |
|           |  ord  chr  str                                                |
| type      |                                                               |
|           |  tuple list   dict set frozenset  enumerate                   |
| convert   |                                                               |
|           |  range  iter  slice                                           |
|           |                                                               |
|           |  super object  memoryview                                     |
+-----------+---------------------------------------------------------------+
| arithmetic|  abs   divmod   max   min   pow   round  sum                  |
|           |                                                               |
+-----------+---------------------------------------------------------------+

```
## 数据类型 - 模块
+------------------------------------------------------------------------------------------------------------+
|                                                                                                            |
|                       Custom Python Interpreters                                                           |
|                                                                                                            |
|                                                                                                            |
|                       Program Frameworks             Development Tools       Debugging and Profiling       |
|                                                                                                            |
|                       Graphical User Interfaces with Tk       Software Packaging and Distribution          |
|                                                                                                            |
|                                                                                                            |
|                                                                                                            |
|                       Networking and Interprocess Communication             Internet Data Handling         |
|                                                                                                            |
|                       Structured Markup Processing Tools           Internet Protocols and Support          |
|                                                                                                            |
|                       File and Directory Access         Data Persistence                                   |
|                                                                                                            |
|                       Data Compression and Archiving     File Formats                                      |
|                                                                                                            |
|                       Internationalization                                                                 |
|                                                                                                            |
|                                                                                                            |
|                                                                                                            |
|           Modules     Numeric and Mathematical           Functional Programming                            |
|                                                                                                            |
|                       Importing                     Superseded                                             |
|                                                                                                            |
|                       Data Types                 Undocumented                                              |
|                                                                                                            |
|                                                                                                            |
|          Services     Text Processing    Binary Data        Cryptographic           Multimedia             |
|                                                                                                            |
|                       Generic Operating System    Concurrent Execution     Context Variables               |
|                                                                                                            |
|                        Python Runtime    Miscellaneous           Unix Specific                             |
|                                                                                                            |
|                       Python Language     MS Windows Specific                                              |
|                                                                                                            |
|                                                                                                            |
|                                                                                                            |
|          Built-in     Functions      Constants      Types      Exceptions                                  |
|                                                                                                            |
|                                                                                                            |
+------------------------------------------------------------------------------------------------------------+

Python 模块(Module)，是一个 Python 文件，以 .py 结尾，包含了 Python 对象定义和Python语句。
模块与路径
```python 

>>> import sys
>>> sys.path

```

## 数据类型 - 面向对象

## 数据并发 


## 应用
[python包管理工具:Conda和pip比较 - 简书](https://www.jianshu.com/p/5601dab5c9e5)



## 源码
```
cpython/
│
├── Doc      ← Source for the documentation
├── Grammar  ← The computer-readable language definition
├── Include  ← The C header files
├── Lib      ← Standard library modules written in Python
├── Mac      ← macOS support files
├── Misc     ← Miscellaneous files
├── Modules  ← Standard Library Modules written in C
├── Objects  ← Core types and the object model
├── Parser   ← The Python parser source code
├── PC       ← Windows build support files
├── PCbuild  ← Windows build support files for older Windows versions
├── Programs ← Source code for the python executable and other binaries
├── Python   ← The CPython interpreter source code
└── Tools    ← Standalone tools useful for building or extending Python

内置方法
cpython/Python/bltinmodule.c

程序C api
cpython/Modules

程序py api
cpython/Lib

```