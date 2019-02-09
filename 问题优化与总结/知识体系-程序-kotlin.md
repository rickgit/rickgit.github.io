## 
```
+----------------------------------------------------------------------------------------------------+
|                                                                                                    |
|                            ReadWrite Object(String,Serializable)/File/Socket/db                    |
+-------------------------------------------+------------+---------+-----------+-------+-------------+
|Class-based|concurrency|Aspect|            |            |         |           |       |             |
|           |           |      |            |            |         |           |       |             |
+-----------+-----------+------+            |            |         |           |       |             |
|                    oop                    | Functional | FRP     |Reflective |Generic|             |
+------------------------------+------+------------------------------------------------|             |      
|            Structured        | Imperative |  Declarative         |  Metaprogramming  |             |
+------------------------------+------------+------------------------------------------+-------------+
|                                                                                                    |
|                             conditional/decision-making/loops                                      |
+----------------------------------+------------+---------------+-+-----------+-------------+--------+
|            Whitespace(tab space) |    0b1     |               | |           |             |        |
|            comment               |    01      | Base Data Type| |           |             | other  |
|            separator sign(; )    |    1       |               | |           |             | symbol/|
+----------------------------------+    0x1     +---------------+-+           |             | token  |
|              separator           |   Literals |  keywords       |Operators  | Identifiers |        |
+----------------------------------+------------+-----------------------------+-------------+--------+
|                                                                                                    |
|                                            Character set (Unicode,UTF+8)                           |
+----------------------------------------------------------------------------------------------------+
|                                                                                                    |
|                                            Byte                                                    |
+----------------------------------------------------------------------------------------------------+


 [kotlin文档地址](http://kotlinlang.org/docs/reference/basic-types.html)
```java
硬关键字：这些关键字无论在什么情况下都不能作为标识符。
软关键字：这些关键字可以在它们不起作用的上下文中用作标识符。
修饰符关键字：这些关键字也可以在代码中用作标识符。
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
|                +----------------------------------------------------------------+
|                |  func       |  func                                            |
|                |  class      |  package class interface                         |
| Hard Keywords  |  package    |  super this                                      |
|                +----------------------------------------------------------------+
|                |  Flow       |   if  else  when while  do  for                  |  
|                |  Control    |   break continue  return                         |
|                |             |   (type casts) as as?                            |
|                |             |   (type checks)  in !in  is !is                  |
|                +-------------------------------------------- --------------------+
|                |  special    |  typealias var val                               |
|                |  identifiers|                                                  |
|                +----------------------------------------------------------------+
|                |  literals   |  false true null object                          |
+----------------+-------------+--------------------------------------------------+


```

``` Operators
+-------------+--------------------------------------------------------------+
|             | ?:(elvis operator)  []( indexed access operator)  ..(Range)  |
| Misc        | ;(separates on same lines)                                   |
|             | :(separates from a type in declarations)                     |
|             |--------------------------------------------------------------|
|             | ?(nullable types)  ?.(safe call)   !!(non-null asserts)      |
|             |--------------------------------------------------------------|
|             | ::(reference)                                                |
|             | @ (references outer superclass/'this'/loop/lambda)           |
|             | $ (references in a string template )                         |
|             |--------------------------------------------------------------|
|             | -> (lambda ,function type, when expression)                  |
|             | _  ( lambda/destructuring declaration)                       |
+----------------------------------------------------------------------------+
|             |                                                              |
|  Logical    |  && || !                                                     |
|             |                                                              |
+----------------------------------------------------------------------------+
|             |                                                              |
|  Relational |  == != > < >= <=                                             |
|             |                                                              |
|             |  === !==                                                     |
+----------------------------------------------------------------------------+
|             |                                                              |
|  Assignment |  = += -= *= /= %=   < < =   > > =   &=   ^=   |=             |
|             |                                                              |
+----------------------------------------------------------------------------+
|             |                                                              |
|  Bitwise    |  & | ^  ~    < <   > >   > > >                               |
|             |                                                              |
+----------------------------------------------------------------------------+
|             |                                                              |
|  Arithmetic |  + - * / % ++ --                                             |
|             |                                                              |
+-------------+--------------------------------------------------------------+


```
## 数据类型
kotlin 静态强类型的显式类型语言
- 字面量
Kotlin 是一种在 Java 虚拟机上运行的静态类型编程语言
```kotlin 
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
```java
if ,when ,for/while loops
break;continue;return;
lablels（@ sign）

``` 
**==** 数值是否相等

**===** 两个对象的地址是否相等


## 编程泛型 - 函数式编程范式
### 一等公民 - 函数
**函数字面量**

  1.  函数，inline fun，lambda
```Lambda expression


val sum = { x: Int, y: Int -> x + y }
```

```Anonymous functions
fun(x: Int, y: Int): Int = x + y

fun(x: Int, y: Int): Int {
    return x + y
}
```

A **lambda expression** or **anonymous function** (as well as a **local function** and an **object expression**) can access its closure, i.e. the variables declared in the outer scope. 
```Closures
var sum = 0
ints.filter { it > 0 }.forEach {
    sum += it
}
print(sum)
```
### 扩展函数
```
+---------+----------------+-------------------+-----------------+
|         | object as param|  object as return |  extension fun  |
+----------------------------------------------------------------+
|  also   |  √             |  √                |  √              |
+----------------------------------------------------------------+
|  let    |  √             |  x                |  √              |
+----------------------------------------------------------------+
|  apply  |  x             |  √                |  √              |
+----------------------------------------------------------------+
|  run    |  x             |  x                |  √              |
+----------------------------------------------------------------+
|  with   |  x             |  x                |  x              |
+---------+----------------+-------------------+-----------------+
```

```java
@kotlin.internal.InlineOnly public inline fun TODO(): kotlin.Nothing { /* compiled code */ }

@kotlin.internal.InlineOnly public inline fun TODO(reason: kotlin.String): kotlin.Nothing { /* compiled code */ }

@kotlin.internal.InlineOnly public inline fun repeat(times: kotlin.Int, action: (kotlin.Int) -> kotlin.Unit): kotlin.Unit { /* compiled code */ }

@kotlin.internal.InlineOnly public inline fun <R> run(block: () -> R): R { /* compiled code */ }

@kotlin.internal.InlineOnly public inline fun <T, R> with(receiver: T, block: T.() -> R): R { /* compiled code */ }

@kotlin.internal.InlineOnly @kotlin.SinceKotlin public inline fun <T> T.also(block: (T) -> kotlin.Unit): T { /* compiled code */ }

@kotlin.internal.InlineOnly public inline fun <T> T.apply(block: T.() -> kotlin.Unit): T { /* compiled code */ }

@kotlin.internal.InlineOnly public inline fun <T, R> T.let(block: (T) -> R): R { /* compiled code */ }

@kotlin.internal.InlineOnly public inline fun <T, R> T.run(block: T.() -> R): R { /* compiled code */ }

@kotlin.internal.InlineOnly @kotlin.SinceKotlin public inline fun <T> T.takeIf(predicate: (T) -> kotlin.Boolean): T? { /* compiled code */ }

@kotlin.internal.InlineOnly @kotlin.SinceKotlin public inline fun <T> T.takeUnless(predicate: (T) -> kotlin.Boolean): T? { /* compiled code */ }

```
## 编程泛型 - 面向对象-类
Kotlin 中所有类都继承该 Any 类。

Any?可空类型。Any?是Any的超集，Any?是整个类型体系的顶部，Nothing是底部

- 封装
  1. class，constructor
  2. func,get,set
  3. interface 
  4. private, protected, internal ,public
  5. data class
  6. sealed class
  7. 泛型
  8. 内部类（inner ），super@Outer，this@label
  9.  enum class 
  10. 对象表达式与对象声明 object
  11. Inline Class（类内联化）
  12. 类型别名
  13. companion object （伴生对象）
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
## 声明式编程-函数编程范式
