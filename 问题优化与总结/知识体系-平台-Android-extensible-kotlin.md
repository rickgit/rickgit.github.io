## 介绍 
kotlin 封装Java，减少开发成本，提高开发效率
1. 封装字段在构造方法传入 var,val定义字段。
2. 类默认时final ，除非使用openclass,abstractclass,interface
3. 类内定义类， 默认static 嵌套类，除非声明 innerclass 
4. 拓展枚举类 sealclass，可以子类声明
5. 合成复用原则，委托和拓展增加类的功能。
6. 拓展类型，支持高阶函数，函数作为参数
7. 拓展容器sequence，支持复杂流式操作。
8. 拓展线程切换，协程。异步线程的数据流flow，挂起队列Channel

### 封装

### 合成复用原则 
#### 拓展和委托

#### 作用域扩展函数
```
Standard.kt

去掉if-else除了 Elvis 操作符 ?: 
如果需要其他操作，还可以用let、also;
如果判断条件复杂，可以用takeIf，takeUnless;
        if (mAdapter != null && context != null) {
            mAdapter.notifyDataSetChanged()
        }
mAdapter.takeIf { context != null }?.notifyDataSetChanged()

避免重复引用对象
 with(person) { name = "John"; age = 30 }
如需返回结果，使用run；
 person.run { "Name: $name, Age: $age" }
如需返回对象本身，使用apply
 Person().apply { name = "John"; age = 30 }

closable接口的拓展方法
val stream = File("file.txt").inputStream()
stream.use {
    // 使用 stream 的代码块
}
```
#### 伴生对象

### 类型增强
#### sequence
#### 高阶函数
#### null判断
### 线程增强 - coroute

#### flow
Builders.kt 拓展Flow的构建方法
```js
<>().asFlow()
flow(){}
flowOf(){}
channelFlow(){}
callbackFlow(){}
```
Collect.kt 拓展Flow的触发方法
```js
collect()，collectLatest()
launchIn()
collectIndexed()
collectLatest() 节流
```
拓展方法实现构建者模式  
    kotlinx-coroutines-core-1.3.7.jar!\kotlinx\coroutines\flow\FlowKt.class 

#### Channel
可挂起队列

管道 ReceiveChannel，扇出ReceiveChannel 扇入SendChannel
## 图片加载 Coil
足够快速，它在内存、图片存储、图片的采样、Bitmap重用、暂停\取消下载等细节方面都有很大的优化(相比于上面讲的三大框架)

##
```
+----------------------------------------------------------------------------------------------------+
|                                                                                                    |
|                            ReadWrite Object(String,Serializable)/File/Socket/db                    |
+-------------------------------------------+------------+---------+-----------+-------+-------------+
|Class-based|concurrency|Aspect|            |            |         |           |       |             |
|           |Async      |      |            |            |         |           |       |             |
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
|                                            Character set (Unicode,UTF+8)                           |
+----------------------------------------------------------------------------------------------------+
|                                            Byte                                                    |
+----------------------------------------------------------------------------------------------------+


 [kotlin文档地址](http://kotlinlang.org/docs/reference/basic-types.html)
```java
硬关键字：这些关键字无论在什么情况下都不能作为标识符。
软关键字：这些关键字可以在它们不起作用的上下文中用作标识符。
修饰符关键字：这些关键字也可以在代码中用作标识符。
+----------------+--------------------------------------------------------------------------------+
|   Special      |                                                                                |
| Identifiers    |  field it                                                                      |
+------------------------------+------------------------------------------------------------------+
|                |             |  (Generic) out  reified                                          |
|                |             |  (Encapsulation)private protected public internal expect actual  |
|  Modifier      |             |  (Encapsulation)inline noinline   crossinline   infix            |
|                |             |  (abstract) abstract                                             |
|  Keywords      |             |  (Polymorphism)operator   override    final  open                |
|                |             |  (class fun)  external  vararg   tailrec    suspend              |
|                |             |  (class)  data enum annotation sealed    inner  companion        | 
|                |             |  (type)      const  lateinit                                     |
+-------------------------------------------------------------------------------------------------+
|                |             | catch     finally                                                |
|                |             | import    set   get  by                                          |
|  Soft Keywords |             | (annotation) param setparam delegate field file property receiver|
|                |             | (class) constructor init  (Generic) where                        |
|                |             | (type)dynamic                                                    |
+-------------------------------------------------------------------------------------------------+
|                |  exception  |  throw  try                                                      |
|                +--------------------------------------------------------------------------------+
|                |             | (abstract)interface          (Encapsulation)package              |
|                |             | (Polymorphism)    super      (type casts) as as?                 |
|                |  OOPs       | (Polymorphism)    typealias  (type checks)  in !in  is !is       |
| Hard Keywords  |             | this              class    fun                                   |
|                +--------------------------------------------------------------------------------+
|                |  Flow       |   if  else  when while  do  for                                  |
|                |  Control    |   break continue  return                                         |
|                +--------------------------------------------------------------------------------+
|                |  special    |   var val                                                        |
|                |  identifiers|                                                                  |
|                +--------------------------------------------------------------------------------+
|                |  literals   |  false true null object                                          |
+----------------+-------------+------------------------------------------------------------------+

identifiers：对象的名字( a name of  a unique object )
```

``` 
Operators
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
|  Logical    |  && || !                                                     |
+----------------------------------------------------------------------------+
|  Relational |  == != > < >= <=                                             |
|             |  === !==                                                     |
+----------------------------------------------------------------------------+
|  Assignment |  = += -= *= /= %=   < < =   > > =   &=   ^=   |=             |
+----------------------------------------------------------------------------+
|  Bitwise    |  & | ^  ~    < <   > >   > > >                               |
+----------------------------------------------------------------------------+
|  Arithmetic |  + - * / % ++ --                                             |
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

```
_Collections.kt
转换函数，过滤函数，排序函数，分组函数，聚合函数
元素操作,集合操作


转换函数

map(transform: (T) -> R)：返回一个包含每个元素转换后结果的新列表。
flatMap(transform: (T) -> Iterable<R>)：返回一个包含每个元素转换后结果的新列表，但是转换函数的结果是一个集合。
toSet()：返回一个包含集合中所有元素的新 Set。
toMutableSet()：返回一个包含集合中所有元素的新可变 Set。
toMap(keySelector: (T) -> K, valueTransform: (T) -> V)：返回一个根据指定键和值转换后的新 Map。
associate(transform: (T) -> Pair<K, V>)：返回一个根据元素转换成键值对后的新 Map。
zip(other: Iterable<R>)：返回一个每个元素都是 Pair 的新列表，每个 Pair 包含两个集合中相同索引处的元素。

过滤函数

filter(predicate: (T) -> Boolean)：返回一个包含符合条件的元素的新列表。
filterNot(predicate: (T) -> Boolean)：返回一个不符合条件的元素的新列表。
take(n: Int)：返回一个包含前 n 个元素的新列表。
takeWhile(predicate: (T) -> Boolean)：返回一个包含前面符合条件的元素的新列表。
drop(n: Int)：返回一个删除前 n 个元素后的新列表。
dropWhile(predicate: (T) -> Boolean)：返回一个删除前面符合条件的元素后的新列表。
distinct()：返回一个去重后的新列表。
distinctBy(selector: (T) -> K)：返回一个根据指定键去重后的新列表。

排序函数

sorted()：返回一个按升序排列的新列表。
sortedBy(selector: (T) -> R)：返回一个按指定属性升序排列后的新列表。
sortedDescending()：返回一个按降序排列的新列表。
sortedByDescending(selector: (T) -> R)：返回一个按指定属性降序排列后的新列表。

分组函数

groupBy(keySelector: (T) -> K)：返回一个根据指定键分组后的新 Map。
partition(predicate: (T) -> Boolean)：返回一个符合条件和不符合条件的元素的新 Pair。

聚合函数

fold(initial: R, operation: (acc: R, T) -> R)：从左到右遍历集合，依次将元素和累加器传入指定的操作函数，并返回最终的累加器结果。
reduce(operation: (T, T) -> T)：从左到右遍历集合，依次将元素和累加器传入指定的操作函数，并返回最终的结果。

元素操作函数

indexOf(element: T)：返回第一个匹配元素的索引，如果没有找到则返回 -1。
lastIndexOf(element: T)：返回最后一个匹配元素的索引，如果没有找到则返回 -1。
elementAt(index: Int)：返回指定索引处的元素。
distinct()：返回一个去重后的新列表。
distinctBy(selector: (T) -> K)：返回一个根据指定键去重后的新列表。
associateBy(keySelector: (T) -> K)：返回一个根据指定键转换后的新 Map。
associateWith(valueSelector: (T) -> V)：返回一个根据指定值转换后的新 Map。
plus(element: T)：返回一个包含指定元素的新列表。

集合操作函数

all(predicate: (T) -> Boolean)：检查集合中的所有元素是否符合指定条件。
any(predicate: (T) -> Boolean)：检查集合中是否存在符合指定条件的元素。
count(predicate: (T) -> Boolean)：返回符合条件的元素的数量。
max()：返回集合中最大的元素。
min()：返回集合中最小的元素。
none()：检查集合中是否不存在元素。
sumBy(selector: (T) -> Int)：返回集合中所有元素按指定条件计算后的总和。
average()：返回集合中所有元素的平均值。

```
## 异步

Threading
Callbacks
Futures, Promises et al.
Reactive Extensions 响应式拓展
Coroutines 协程

## 数据并发
## 声明式编程-函数编程范式


## Android 切换为kotlin


代码

```groovy
1. 
buildscript {
    ext.kotlin_version = '1.3.60-eap-25'
    repositories {
        google()
        jcenter()
        maven { url 'https://dl.bintray.com/kotlin/kotlin-eap' }

    }
    dependencies {
        classpath 'com.android.tools.build:gradle:3.5.0'
        classpath "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version"
    }
}

2. 
apply plugin: 'kotlin-android-extensions'
apply plugin: 'kotlin-android'
dependencies {
    implementation "androidx.core:core-ktx:+"
    implementation "org.jetbrains.kotlin:kotlin-stdlib-jdk7:$kotlin_version"
}
repositories {
    maven { url 'https://dl.bintray.com/kotlin/kotlin-eap' }
    mavenCentral()
}


3. annotationCompiler 转为 kapt
apply plugin: 'kotlin-android'
kapt 'com.xx.xx'


4. 32bit studio 配置
org.gradle.jvmargs=-Xmx1536m -XX\:MaxHeapSize\=1536m

5. APK大小增加600k
 
```
## 源码
[Kotlin源码分析](https://zhuanlan.zhihu.com/p/76622754)
https://blog.csdn.net/weixin_34283445/article/details/89580460

1. [Quick Start](https://kotlinlang.org/docs/tutorials/command-line.html)
```shell
$ kotlinc hello.kt -include-runtime -d hello.jar
$ java -jar hello.jar
```
认知所有语法糖。
Intellj IDA 的 Kotlin ByteCode插件，查看编译成Java代码
2. 调试
```
将kotlin release的jar加入环境，调试
K2JVMCompiler.main(new String[]{"hello.kt ","-include-runtime"," -d","hello.jar"});
```
3. 源码阅读
```

  001_collection    369b1974782b821e44b7aa6cd68e2e41eb2ba036 Initial
  002_io            54ccb2e184b822db5f4ec3a8e2390f7bf74c0f94 Enhanced iterator
  003_grammar       6d6a22de1d8bc6a4faff1618e8202c65b9a42c32 Projections & Initial grammar
        +----------------------------------------------------------------------+
        |                       class.grm                                      |
        +----------------------------------------------------------------------+
        |                   class_members.grm                                  |
        +----------------------------------------------------------------------+
        |                   expressions.grm                                    |
        +----------------------------------------------------------------------+
        |                  types.grm        other.grm      lexical.grm         |
        |----------------------------------------------------------------------|

  004_control       807062a1f8914c24d84884b9753064e4baf1c92a Binary operations, control structures
  005_bit           661931469b96e1f645553709e3ed5a1529eb9fab Expressions
  006_Extensions    171a5250f85f411aa1b37c38e89863338a65c63a Extensions
        +----------------------------------------------------------------------+
        |                      toplevel.grm                                    |
        +----------------------------------------------------------------------+
        |                       class.grm                                      |
        +----------------------------------------------------------------------+
        |                   class_members.grm                                  |
        +----------------------------------------------------------------------+
        |                   expressions.grm                                    |
        +----------------------------------------------------------------------+
        |                  types.grm        other.grm      lexical.grm         |
        |----------------------------------------------------------------------|
  006_Anonymous     8beb0da924b18145059bd78f18d3e5d693fe2f4a Anonymous Objects
  007_with          73b12271c06078b74bce9548a207dfaa9918f8b7 Calling closures on objects
  008_Default       dd49fc6ce2e18909ca8fd8be379283f22d7a68f0 Default parameters, function type examples fixed, tuples with named entries
  009_Matches       ff3f7d82265cc958b14296fe19ea3770a55bb81d Matches reorganized
  010_Namespaces    03ef48508fa17f1e9a6766100b22a03a3ef1a3f1 Namespaces and imports
  011_scripts       d9cdd4ee34fa5df2198c81576408e598dceceae5 Scripts
  012_Attributes    0c90942472375f7212ef926786cf3091106ca134 Attributes
  013_keywords      274cf80336940757365d16e84b3d84e474e4b80f Preliminary list of keywords
  014_lexer         a7919fdc1db453fd228739a94652ebf2ae8a6159 lexer/highlighting initial
  015_parser        4f18686978ebcfa5970dfafdbe5143e0d507bc9f Stub parser
        [Intellij IDEA插件开发（五）自定义语言支持](https://www.tuicool.com/articles/eQJNRzb)
        [custom_language_support](https://www.jetbrains.org/intellij/sdk/docs/reference_guide/custom_language_support.html)
        [idea-jflex](https://github.com/jflex-de/idea-jflex)
        +----------------------------------------------------------------------------------------------------------+
        |                                idea                                                                      |
        +----------------------------------------------------------------------------------------------------------+
        |                             plugin.xml                                                                   |
        +----------------------------------------------------------------------------------------------------------+
        |                    JetParserDefinition:ParserDefinition                                                  |
        |                                                                                                          |
        |  JetLexer:FlexAdapter         JetParser:PsiParser      JetFile:PsiFileBase  PsiElement      asm          |
        |    (Scanning)                      (Parsing)               (semantic)                    (codegen)       |
        |                                                                                                          |
        |                                                                                                          |
        |  Jet.flex                         JetParsing                                                             |
        |                                        parseFile()                                                       |
        |    JFlex                           ANTLR/psi                                                             |
        +----------------------------------------------------------------------------------------------------------+
* 016_Soft_keywords 17d9240d148e721c06da86f494a6c693f2103e3e parser, baby steps. Soft keywords highlighting
  017_parsingtest   5a31694d70c1371361422afb012d69bca76c843c Parsing tests
        +----------------------------------------------------------------------------------------------------------+
        |                                idea                                                                      |
        +----------------------------------------------------------------------------------------------------------+
        |                             plugin.xml                                                                   |
        +----------------------------------------------------------------------------------------------------------+
        |                    JetParserDefinition:ParserDefinition                                                  |
        |                                                                                                          |
        |  JetLexer:FlexAdapter         JetParser:PsiParser      JetFile:PsiFileBase  PsiElement      asm          |
        |    (Scanning)                      (Parsing)               (semantic)                    (codegen)       |
        |                                                                                                          |
        |                                                                                                          |
        |  Jet.flex                         JetParsing           JetParsingTest:ParsingTestCase                    |
        |                                        parseFile()                                                       |
        |    JFlex                           ANTLR/psi                                                             |
        +----------------------------------------------------------------------------------------------------------+

  018_psi_nodes     fce72774392674d579a16237499e230530a5f51a psi nodes, initial
  019_resolve       1ce2ed8 ("Initial stub version of resolve for types", 2011-01-24)
        +----------------------------------------------------------------------------------------------------------+
        |                                idea                                                                      |
        +----------------------------------------------------------------------------------------------------------+
        |                             plugin.xml                                                                   |
        +----------------------------------------------------------------------------------------------------------+
        |                    JetParserDefinition:ParserDefinition                                                  |
        |                                                                                                          |
        |  JetLexer:FlexAdapter         JetParser:PsiParser                                           asm          |
        |    (Scanning)                      (Parsing)                           (semantic)        (codegen)       |
        |                                                                                                          |
        |                                                                                                          |
        |  Jet.flex              JetParsing           JetParsingTest        TypeResolver                           |
        |                             parseFile()         :ParsingTestCase       resolveClass()                    |
        |    JFlex                 ANTLR/psi                                                                       |
        +----------------------------------------------------------------------------------------------------------+


  
  019_codegen       c5d8aae5152e7e3838dd7d609cc491ded0b30513 codegen, initial objectweb
        +----------------------------------------------------------------------------------------------------------+
        |                                idea                                                                      |
        +----------------------------------------------------------------------------------------------------------+
        |                             plugin.xml                                                                   |
        +----------------------------------------------------------------------------------------------------------+
        |                    JetParserDefinition:ParserDefinition                                                  |
        |                                                                                                          |
        |  JetLexer:FlexAdapter         JetParser:PsiParser                                           asm          |
        |    (Scanning)                      (Parsing)                           (semantic)        (codegen)       |
        |                                                                                                          |
        |                                                                                                          |
        |  Jet.flex              JetParsing           JetParsingTest        TypeResolver      LightDaemonAnalyzerTestCase |
        |                             parseFile()         :ParsingTestCase       resolveClass()                    |
        |    JFlex                 ANTLR/psi                                                                       |
        +----------------------------------------------------------------------------------------------------------+
  020_psvm          26c5a07a6bb55c0163084be86f129ea9bf138bab dummy test
        +----------------------------------------------------------------------------------------------------------+
        |                                idea                                                                      |
        +----------------------------------------------------------------------------------------------------------+
        |                             plugin.xml                                                                   |
        +----------------------------------------------------------------------------------------------------------+
        |                    JetParserDefinition:ParserDefinition                                                  |
        |                                                                                                          |
        |  JetLexer:FlexAdapter         JetParser:PsiParser                                           asm          |
        |    (Scanning)                      (Parsing)                           (semantic)        (codegen)       |
        |                                                                                                          |
        |                                                                                                          |
        |  Jet.flex              JetParsing           JetParsingTest        TypeResolver           LightDaemonAnalyzerTestCase |
        |                             parseFile()         :ParsingTestCase       resolveClass()    LightCodeInsightFixtureTestCase |
        |    JFlex                 ANTLR/psi                                                                       |
        +----------------------------------------------------------------------------------------------------------+

  021_return        8e6c2995d0a542bf0105039dfa37f6bc9bfc6224 return 42
  022_as            445736bfbd83b97c19515fd992730098f95c5705 Priorities for ":" and "as" changed
  023_!             2e8b828ee009afa115465a8e548a559f4b06807c Resolve for "!"
* 024_in            e414413112078b4739943174f06dcb98ada4c27a Contains (in, !in)
  025_helloWorld    af4c77719768df7f5476abc1341f2c70e2e8612f hello world can be compiled
  026_cf            5888ac5a606a286a1c7ea43657a63e1c1c202c54 Working on CFG building
        +---------------------------------------------------------------------------------------+
        |                                idea                                                   |
        +---------------------------------------------------------------------------------------+
        |                             plugin.xml                                                |
        +---------------------------------------------------------------------------------------+
        |                    JetParserDefinition:ParserDefinition                               |
        |                                                                                       |
        |JetLexer:FlexAdapter                           JetParser:PsiParser                     |
        |  (Scanning)                                        (Parsing)                          |
        |                                                                                       |
        |                   (cfg tree)                                                          |
        |Jet.flex           (Context Free Grammar)      JetParsing       JetParsingTest         |
        |                                                    parseFile()   :ParsingTestCase     |
        |  JFlex            TopDownAnalyzer                                                     |
        |                   JetControlFlowProcessor     AST(abstract syntax tree)/              |
        |                         generate()            psi(Program Structure Interface,ANTRL)  |
        +---------------------------------------------------------------------------------------+
        |   (semantic)        (ir)                                                              |
        |                                                       (codegen)                       |
        |                                                        asm                            |
        |   TypeResolver                                                                        |
        |        resolveClass()                                 LightDaemonAnalyzerTestCase     |
        |                                                       LightCodeInsightFixtureTestCase |
        |                                                                                       |
        +---------------------------------------------------------------------------------------+

  027_mockJDK       dd3f2b3afbc21487e0ce7524c12fe918c7354c71 add mock JDK to Jet repo
  028_bottles       1d01d519b98d5ab5317217b32658292461fa721f a working version of 99 bottles
* 029_stblib        b95498624fe04e35390994ec27dfa32045716e63 range literals initial
  029_24game        93b30bd7c7ed5f737b2e5b40540e42166857efd5 TwentyFourGame wip
* 030_Confluence     bf7be08e3b9ba79dc2f18928f5a922f33a3e595b Working on the Confluence highlighter
  030_kt            9bfa61bfb23618d705f2ea5ead44bc45a04e7e16 accept .kt extension for Kotlin files
  031_split2frontend 07e0a332c335aeb5fb57022f0c9e6cd4db1577f7 Project split into four modules
  032_compiler       116f35c650c411e7a3477dc5bde85f9273eb6824 "compiler" folder created
  033_Docs          7629ebe272d90be0f810df337f7def0a3b49a06b Docs
  034_bin_kotlin       8336c647878562f10c315461d259fa69abd43e48 bin/kotlin script
        +-----------------------------------------------------------------------------------------------------------------+
        |(compiler)                                                                                                       |
        +-----------------------------------------------------------------------------------------------------------------+
        |(cli)                      kotlinc.bat                                                                           |
        +-----------------------------------------------------------------------------------------------------------------+
        |                          KotlinCompiler                                                                         |
        +-----------------------------------------------------------------------------------------------------------------+
        |                          CompileEnvironment                                                                     |
        |                          initializeKotlinRuntime()                                                              |
        |    compileModuleScript()                       compileBunchOfSources()                                          |
        +-----------------------------------------------------------------------------------------------------------------+
        |  CompileSession                                                                                                 |
        |      addSources()            analyze()                                      generate()                          |
        +-----------------------+------------------------------------------------+----------------------------------------+
        | (intellij)            |   AnalyzerFacade                               |  GenerationState                       |
        | PsiManager            |                                                |      compileCorrectFiles()             |
        |     findFile():PsiFile|                                                |       generateNamespace()              |
        |                       |                                                |                                        |
        |                       |   AnalyzingUtils                               |                                        |
        | (intellij)            |         analyzeFilesWithGivenTrace()           |  NamespaceCodegen                      |
        | PsiManager            |  TopDownAnalyzer                               |       generate()                       |
        |     findFile():PsiFile|         process()                              |       v:ClassBuilder                   |
        |                       |                                                |  (asm)                                 |
        |                       |   TypeHierarchyResolver  BodyResolver          |     ClassBuilder                       |
        |                       |   DeclarationResolver    ControlFlowAnalyzer   |                                        |
        |                       |   DelegationResolver     DeclarationsChecker   |                                        |
        |                       |   O^errideResol^er                             |                                        |
        |                       |   OverloadResolver                             |                                        |
        +-----------------------+------------------------------------------------+----------------------------------------+
        [参见](https://zhuanlan.zhihu.com/p/76622754)
        1. 词法分析 
        2. 语法分析
        3. 语义分析及中间代码生成 
        4. 目标代码生成
        [Java 中调用 Kotlin](www.kotlincn.net/docs/reference/java-to-kotlin-interop.html)
  034_buildTools     28044c18cdcb9a4450d256809784e3dc3d4008ad "build-tools" module + "buildTools" Ant target added
  034_jdkHeaders     3e29aad6dd1bb5909c5dd94682c2f586b1e699aa KT-987 Unboxing_nulls: Kotlinized versions of basic Java collections
  034_j2k            8cf4c809511530577a6c074917b4fd10e90c4aa7 initial commit for j2k
  034_codegenDOC     f82259ae84f2bd48b7fa4eb020c73aba014638e6 Detailed session desc for CodeGen
  035_k2js           27bddcd2a4b08dde73812ebc42cf28d3e78e297e initial commit
  036_puzzlers       89fa7cd29a386b37c079a5f058cc85d80f7ee36a More Kotlin vs Java puzzlers
  037_examples       e787d94b96bf7b98c374bcc3fd1db96e8c1f35da ++/-- performance optimization
  038_DSLs           db76e28c6da37521b3d5e7d68bf11bc4fea5b0dd added a little experimental spike of a text and markup based template library for internal DSLs for templating (which external DSLs like Jade / Razor / Velocity / Erb / JSP style) could layer on top of. Mails to follow shortly :)
  039_buildToolsTest 823852cc6c875f658092a29fcd47c9c075c9b779 First Ant task + "buildToolsTest" Ant target added
  040_collections    3e29aad6dd1bb5909c5dd94682c2f586b1e699aa KT-987 Unboxing_nulls: Kotlinized versions of basic Java collections
  041_Post-build     ea6b6f7495f7587b8c07eb2723fb98584d516aeb Post-build step: renaming the zip artifact
  042_webdemo        2a515b2eb935026af6fc94f9ae0f744506c77d24 working on examples
  043_kdoc           9af12d1bacc75e1ef958e3d0c14c4a66edda9f4a added a little experimental kdoc module using the maven plugin for dogfooding

* 103_v1.0.3        2f47e30a1a12347759dbb8707f5137178de65696 Fixed UnsupportedOperationException when a namespace is used in place of an expression.

```
### 协程
解决问题：
调度（分发器），生命周期（协程范围，协程上下文-组合模式，Job-状态），异常处理

[kotlinx.coroutines](https://github.com/Kotlin/kotlinx.coroutines.git)
```
[](https://blog.csdn.net/qfanmingyiq/article/details/105184822)
            +---------------------------------------------------------------------------------------------------+
            |                                                                                                   |
            |      CoroutineScheduler:Executor            CoroutineDispatcher:ContinuationInterceptor           |
            |                                                    interceptContinuation()                        |
            |                                                                                                   |
            |                                                                                                   |
            |                                             DispatchedContinuation:DispatchedTask                 |
            |                                                                                                   |
            |                                                  dispatcher: CoroutineDispatcher                  |
            |                                                  continuation: Continuation                       |
            |                                                  resumeWith()                                     |
            |                                                                                                   | 
            |                                                                                                   |
            +---------------------------------------------------------------------------------------------------+


```
## Regular languages and lexer generators
### jflex 词法分析（java fast lexer generator）
[Comparison of parser generators](https://en.wikipedia.org/wiki/Comparison_of_parser_generators)
《编译原理（第三章第五节）》
1. [使用说明 ](wwww.jflex.de)
    1.1 [How to get it building](wwww.jflex.de/manual.html#running-jflex)
    ```bat
        jflex java-lang.flex
    ``` 
    1.2 [example standalone.flex](http://wwww.jflex.de/manual.html#Example)
    ```
        +----------------------------------------------------------------------------------------------------+
        |                                 SubstTest.java                                                     |
        |                                                                                                    |
        +----------------------------------------------------------------------------------------------------+
        |                                                                                                    |
        |                                Subst.java                                                          |
        |                                main()                                                              |
        |                                                                                                    |
        |                                ZZ_TRANS         zzBuffer                                           |
        |                                ZZ_ROWMAP        zzStartRead                                        |
        |                                ZZ_ATTRIBUTE     zzMarkedPos                                        |
        |                                yylex()          yytext()                                           |
        +----------------------------------------------------------------------------------------------------+

    ```
2. 源码从init开始阅读，读到第一步提交入口的commit
3. release note 选择版本阅读
    [release note ](https://jflex.de/changelog.html)
    ```
    +------------------------------------------------------------------------------------+
    |                                   JFlex.Main                                       |
    +------------------------------------------------------------------------------------+
    |     LexParse.java    sym.java                      LexParse.java                   |
    |                                                 skeleton.nested                    |
    |    java_cup.Main  LexParse.cup                   JFlex/LexScan.flex                |
    |                                                                                    |
    +------------------------------------------------------------------------------------+


    ```

```                                                                                  runBlocking
+----------------------------------------------------------------------------------+
|  GlobalScope                                                                     |
|        launch()                                          async()                 |
|                                                                                  |
|                                                                                  |
+----------------------------------------------------------------------------------+
|  CombinedContext                                                                 |
|      left:CorountineId                                                           |
|      ele: Dispatchers.Default                                                    |ele:ThreadLocalEventLoop
+--------------------------------------------------------+-------------------------+
|  StandaloneCoroutine  : AbstractCoroutine,Continuation | DeferredCoroutine       |BlockingCoroutine
|        parentContext: CoroutineContext,                |                         |
|        active: Boolean                                 |                         |
|                                                        |                         |
|        start()                                         |                         |
|        startCoroutine()                                |                         |
|                                                        |                         |
+--------------------------------------------------------+-------------------------+
|                                                                                  |
|      block: Callable,DispatchedTask :: continuation                              |
|         startCoroutineCancellable()     intercepted():DispatchedContinuation     |
|                                                                                  |
|                                                                                  |
|       DispatchedContinuation                                                     |
|                  dispatcher:CoroutineDispatcher                                  |dispatcher:BlockingEventLoop
|                             //DefaultScheduler                                   |
|                  resumeCancellable()                                             |
|                                                                                  |
|        DefaultScheduler :ExperimentalCoroutineDispatcher:Runnable                |BlockingEventLoop
|             dispatch()                                                           |  enqueueImpl()
|             coroutineScheduler:DefaultDispatcher                                 |  thread: Thread
|             run()                                                                |
|             notifyStartup()                                                      |
|                                                                                  |
+----------------------------------------------------------------------------------+
|[threadpool]                                                                      |
|      DefaultDispatcher: CoroutineScheduler :Executor                             |
|              dispatch(block:)                                                    |
|              createTask()                                                        |
|                                                                                  |
|              globalQueue: GlobalQueue//LockFreeTaskQueueCore                     |
|              submitToLocalQueue()                                                |
|              requestCpuWorker()                                                  |
|              tryUnpark()                                                         |
|                                                                                  |
|              corePoolSize://12                                                   |
|                                                                                  |
+----------------------------------------------------------------------------------+
|  LockFreeTaskQueue    LockFreeTaskQueueCore                                      |Parksupport   
|                                                                                  |   park() 
+----------------------------------------------------------------------------------+

//---------------delay
delay


CancellableContinuationImpl
     disposeOnCancellation()
     invokeOnCancellation()
 DefaultExecutor : EventLoopImplBase()
    scheduleResumeAfterDelay()
     run()
     _delay:DelayedTaskQueue
DelayedResumeTask
    scheduleTask()

DelayedTaskQueue
    addLastIf()
//---------------job cancel
JobSupport  : Job
cancelInternal()
makeCancelling()

//---------------time out
withTimeout()
setupTimeout()

TimeoutCoroutine:AbstractCoroutine
    disposeOnCompletion()
    startUndispatchedOrReturnIgnoreTimeout()


/// runBlocking， launch ，withContext ，async，doAsync


线程池
+------------------------------------------------------------------------------------------------------+
| CoroutineScheduler : Executor                                                                        |
|            //core = 12, max = 1536                                                                   |
|                                                                                                      |
|         controlState:atomic                                                                          |
|         cpuPermits:Semaphore                                                                         |
|         globalQueue: GlobalQueue                                                                     |
|         execute(command:Runnable)                                                                    |
|         dispatch(block: Runnable,                                                                    |
|                 taskContext: TaskContext = NonBlockingContext,                                       |
|                 fair: Boolean = false)                                                               |
|                                                                                                      |
|                                                                                                      |
| Worker: Thread       DispatchedContinuation:DispatchedTask  GlobalQueue : LockFreeTaskQueue          |
|    state:WorkerState                       :Runnable           _cur = LockFreeTaskQueueCore          |
|                        dispatcher: CoroutineDispatcher,                                              |
|    findTask():Task     continuation: Continuation<T>                                                 |
|    queue: WorkQueue          //client block                                                          |
|    run()                                                   //ThreadLocal container                   |
|                                                            BlockingEventLoop : EventLoopImplBase     |
|WorkerState                                                                                           |
|    CPU_ACQUIRED,                                                 joinBlocking()                      |
|    BLOCKING,                                                     processNextEvent()                  |
|    PARKING,                                                      dequeue(): Runnable                 |
|    RETIRING,                                                                                         |
|    TERMINATED                                                    _queue = atomic<Any?>               |
|                                                                                                      |
|WorkQueue                                                                                             |
|    lastScheduledTask = atomic<Task?>                       EventLoopImplPlatform                     |
|    buffer: AtomicReferenceArray<Task?>                         :  EventLoopImplPlatform(), Delay     |
|                                                                  _queue = atomic<Any?>               |
|                                                                                                      |
|                                                                                                      |
|                                                                                                      |
|                                                            EventLoopImplPlatform: EventLoop          |
|                                                                 thread: Thread//blocking thread      |
|                                                                                                      |
|                                                            EventLoop : CoroutineDispatcher           |
|                                                                                                      |
+------------------------------------------------------------------------------------------------------+

```

## 语法解析（Parsing, and Context-Free Grammars）
### java_cup Parser Generator 

[official website of CUP](http://www2.cs.tum.edu/projects/cup)
[github](github.com/duhai-alshukaili/CUP)
[](https://www.youtube.com/watch?v=zWoDiDy5c-U)

```
+-----------------------------------------------------------------------------------------+
|  generate files                                                                         |
|                    parser.cup( parse.java     sym.java  )                               |
|                                                                                         |
+-----------------------------------------------------------------------------------------+
|                           Main   //System.in                                            |
|                             parse_grammar_spec()                                        |
|                             action_table                                                |
|                             reduce_table                                                |
|                             start_state:lalr_state                                      |
|                             build_parser()                                              |
|                                                                                         |
|                             emit_parser()                                               |
+-----------------------------------------------------------------------------------------+
|   parse:lr_parser                        lalr_state                  emit               |
|       parse():Symbol                        build_machine()            symbols()        |
|       action_obj:CUP$parser$actions         build_table_entries()      parser()         |
|       scan()                                                                            |
|       stack:Stack                                                                       |
+-----------------------------------------------------------------------------------------+
|                               java cup                                                  |
|                                                                                         |
+-----------------------------------------------------------------------------------------+


```

## antllr 
1. [Getting started with v4](https://github.com/antlr/antlr4/blob/master/doc/getting-started.md)
    1.1 Hello.g4
    ```
        grammar Hello;
        r  : 'hello' ID ;         // match keyword hello followed by an identifier
        ID : [a-z]+ ;             // match lower-case identifiers
        WS : [ \t\r\n]+ -> skip ; // skip spaces, tabs, newlines
    ```
    1.2
    java  -jar .\antlr-4.8-complete.jar Hello.g4
    javac -cp ".;.\antlr-4.8-complete.jar" Hello*.java

    1.3
    java -cp ".;.\antlr-4.8-complete.jar" org.antlr.v4.gui.TestRig Hello r -tree
2. Structure main entrance
    ```
        +--------------------------------------------------------------------------------------+
        |                       g4 file                                                        |
        +--------------------------------------------------------------------------------------+
        |                       org.antlr.v4.Tool                                              |
        +--------------------------------------------------------------------------------------+
        |                       java files (HelloLexer.java HelloParse.java)                   |
        +--------------------------------------------------------------------------------------+
        |                        org.antlr.v4.gui.TestRig                                      |
        |                        process()                                                     |
        |                                                                                      |
        +--------------------------------------------------------------------------------------+
        |      //+tree System.out.println             //+gui                                   |
        |                                             org.antlr.v4.gui.Trees                   |
        |                                             inspect()                                |
        +--------------------------------------------------------------------------------------+
        |                             HelloParse:Parser                                        |
        |                                 r()//rule     _input:CommonTokenStream               |
        |                                 match()       _ctx:ParserRuleContext                 |
        |                                                                                      |
        +--------------------------------------------------------------------------------------+
        |                                                                                      |
        |       ParserRuleContext:RuleContext       CommonTokenStream:BufferedTokenStream      |
        |           start:Token                        LT():Token                              |
        |           parent:RuleContext                  tokens:ArrayList                       |
        |                                                                                      |
        +--------------------------------------------------------------------------------------+

    ```
《The Definitive ANTLR 4 Reference》
《编译原理（第四章第4.4节）》

3.[Release notes](https://github.com/antlr/antlr4/releases)



## viewModelScope
[Retrofit+Coroutines+viewModelScope] 代替 [Retrofit+Rxjava+AutoDispose]
https://juejin.im/post/5cb473e66fb9a068af37a6ce
