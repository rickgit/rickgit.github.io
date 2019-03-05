## 
python 语言特性：动态强类型  

动态类型语言：在运行期进行类型检查的语言，也就是在编写代码的时候可以不指定变量的数据类型
强类型语言，一个变量不经过强制转换，它永远是这个数据类型，不允许隐式的类型转换。

大数据


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
+------------+   Numbers
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
````
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
Python 模块(Module)，是一个 Python 文件，以 .py 结尾，包含了 Python 对象定义和Python语句。
模块与路径
```python 

>>> import sys
>>> sys.path

```

## 数据类型 - 面向对象

## 数据并发 