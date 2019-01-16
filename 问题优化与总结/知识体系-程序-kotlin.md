

## 
 [kotlin文档地址](http://kotlinlang.org/docs/reference/basic-types.html)
```
+----------------+----------------------------------------------------------------+
|   Special      |                                                                |
| Identifiers    |  field it                                                      |
+------------------------------+--------------------------------------------------+
|                |             |  final  open                                     |
|                |             |  private   protected   public  internal          |
|                |             |  abstract data sealed inner enum annotation      |
|  Modifier      |             |  companion                                       |
|                |             |  operator   override  suspend   external  tailrec|
|  Keywords      |             |  inline noinline   crossinline   infix           |
|                |             |  const  lateinit   vararg   out  reified         |
|                |             |  expect   actual                                 |
+---------------------------------------------------------------------------------+
|                |             | catch     finally                                |
|                |             | import file                                      |
|  Soft Keywords |             | by  delegate                                     |
|                |             | constructor init     param                       |
|                |             | receiver  property                               |
|                |             | where     setparam   set   get   field           |
|                |             | dynamic                                          |
+---------------------------------------------------------------------------------+
|                |  exception  |  throw  try                                      |
|                |             |                                                  |
|                |             |                                                  |
|                +----------------------------------------------------------------+
|                |  func       |  func class interface  package                   |
|                |  class      |                                                  |
| Hard Keywords  |  package    |  super this                                      |
|                +----------------------------------------------------------------+
|                |  Flow       |   if  else  when while  do  for                  |  
|                |  Control    |   break continue  return                         |
|                |             |   as as? in !in  is !is                          |
|                |             |                                                  |
|                +----------------------------------------------------------------+
|                |  special    |  typealias var val                               |
|                |  identifiers|                                                  |
|                +----------------------------------------------------------------+
|                |  literals   |  false true null object                          |
|                |             |                                                  |
+----------------+-------------+--------------------------------------------------+


```

``` Operators
+-------------+--------------------------------------------------------------+
|             |                                                              |
|  Misc       |                                                              |
|             |                                                              |
|             |  [] !! ?. ?:(elvis operator)   ::  ..  :   ?    ->   @   ;   |
|             |  $   _                                                       |
|             |                                                              |
+----------------------------------------------------------------------------+
|             |                                                              |
|  Assignment |  = += -= *= /= %=   < < =   > > =   &=   ^=   |=             |
|             |                                                             ++
+----------------------------------------------------------------------------+
|             |                                                              |
|  Logical    |  && || !                                                     |
|             |                                                              |
+----------------------------------------------------------------------------+
|             |                                                              |
|  Bitwise    |  & | ^  ~    < <   > >   > > >                               |
|             |                                                              |
+----------------------------------------------------------------------------+
|             |                                                              |
|  Relational |  == != > < >= <=                                             |
|             |                                                              |
|             |  === !==                                                     |
+----------------------------------------------------------------------------+
|             |                                                              |
|  Arithmetic |  + - * / % ++ --                                             |
|             |                                                              |
+-------------+--------------------------------------------------------------+


```
##   数据类型
kotlin 静态强类型的显式类型语言
- 字面量
Kotlin 是一种在 Java 虚拟机上运行的静态类型编程语言
```
Kotlin 内置数字类型：
Type    Bit width
Double   64
Float    32
Long     64
Int      32
Short    16
Byte     8


注意在 Kotlin 中字符Char不能转化为数字 

booleans类
arrays类
strings类
```
字符串转义
$ character or ${ } 

- 变量（var）与常量（val）



## 数据控制流
if ,when ,for/while loops
break;continue;return;
lablels（@ sign）
## 数据操作

```
== 数值是否相等

=== 两个对象的地址是否相等
```

## 数据类型 - 面向对象-类
Kotlin 中所有类都继承该 Any 类。

Any?可空类型。Any?是Any的超集，Any?是整个类型体系的顶部，Nothing是底部

- 封装
  1. class，constructor
  2. func,get,set
  3. interface 
  4. private, protected, internal ,public
  5. 扩展函数（let、with、run、apply、also）
  6. data class
  7. sealed class
  8. 泛型
  9. 内部类（inner ），super@Outer，this@label
  10. enum class 
  11. 对象表达式与对象声明 object
  12. Inline Class（类内联化）
  13. 函数，inline fun，lambda
  14. 类型别名
  15. companion object （伴生对象）
- 继承
  1. abstract，open/：,is （类似instanseof） 
  2. super
- 多态
  1. 继承方法
  2. 重写方法override
  3. 父类引用指向子类
  4. 委托 by
  5. 属性委派 by
## 运行时数据解析 - 反射（Any::class，动态代理，annotation class），序列化，泛型
```java
@JvmOverloads
修饰有默认值的方法，那么在 Kotin 中会暴露多个重载方法。
@JvmField
该字段将具有与底层属性相同的可见性。
@JvmStatic
编译器既会在相应对象的类中生成静态方法，也会在对象自身中生成实例方法。

@JvmDefault
指定为非抽象Kotlin接口成员生成JVM默认方法。

@JvmName
告诉编译器生成的Java类或者方法的名称

@JvmMultifileClass
这个注解让Kotlin编译器生成一个多文件类，该文件具有在此文件中声明的顶级函数和属性作为其中的一部分，JvmName注解提供了相应的多文件的名称.


@Strictfp 
将从注释函数生成的JVM方法标记为strictfp，意味着需要限制在方法内执行的浮点运算的精度，以实现更好的可移植性。

@Synchronized 
将从带注释的函数生成的JVM方法标记为synchronized，这意味着该方法将受到定义该方法的实例（或者对于静态方法，类）的监视器的多个线程的并发执行的保护。

@Volatile 
将带注释属性的JVM支持字段标记为volatile，这意味着对此字段的写入立即对其他线程可见.

@Transient 
将带注释的属性的JVM支持字段标记为transient，表示它不是对象的默认序列化形式的一部分。
```
## 异常，日志
## 数据集合
Collections: List, Set, Map


## 数据并发
\