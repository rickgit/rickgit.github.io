

## 源码

1. 下载groovy，入口

```groovy
groovyc MyClass.groovy
```
[org.codehaus.groovy.ant.Groovyc官方文档](http://groovy-lang.org/groovyc.html)


```
  001_initial_maven      a9da497ee8aff08c1ae03a816488601dc80b9a2d Initial revision
  002_classworld         f1d001cb3c2e628888e49aebc967a744a1a4799c -- Addded classworlds as a dependency -- Simple classworlds script and config for compiler -- Maven target to install groovy toolchain to a staging dir
  003_grok               9072d39d15bdf14c2624f6f81b696d4d031e55ea Mutating the classworlds config stuff to support mulitple binaries, so we can run the compiler or grok (groovydoc).
  004_code               de588ab0be7501f55518e5067e70d17b7343fed5 refactored the source tree to make things a little cleaner.
  005_RangeExp           b02feeab2e78130ae259e463927fddda2365c9c6 added implementation of RangeExpression
  006_closures           20c88544e4f4b3c39ef344eb91dd6ea1ee83cc0a added support for closures in the
  007_operation          63726dafc5074107fd885ac5b6e8981c51e481d8 implemented + - * / for numbers and + - for Strings
  008_FileSystemCompiler 28f7cd947c9fec0d596f42c1f5542f74a316a355 Hey, fix that command-line bin/groovy scripts classworlds config for the new filesystem compiler.
  009_GDO                d56ce8e4e03187e6e53e20d257b8182bbeba8c73 Added initial checkin of the GroovyDataObjects (GDO) developed at GeekNight with Chris Stevenson for creating groovy data objects via normal Groovy syntax (rather than SQL) and using closures for predicates or view projections or order by clauses
  010_script             00f64cb5bdc0d06c8a6bf36084e5e89e3711ecd3 Static methods (such as scripts) can now use closures. Also fixed a bug in the codegen of static methods.
  011_groovyshell        a2920acebb4082d5f1e770f557eb13ca9025c21a Refactored Groovy to be called GroovyShell which is a bit more descriptive.
  012_bsf                76ffae17999c7a6f7bbceab680e6117184a7ac74 Initial cut of a BSF engine for Groovy
  013_console            03519132b091a3c956da2494f40eb3c0e037622c Added a simple (and so far quite crap) groovy console in Swing
  014_GroovyMock         9d52257fc429f90582fce6b3358f0aae687fe489 Added GroovyMock library for mock testing of groovy objects
* 015_InteractiveShell   ffc170359391e00ebc97dc24d23156730fd17ef8 Added a really simple interactive command line shell
 

+-------------------------------------------------------------------+ 
|  CSTNode    SemanticVerifier   ASTNode/ClassNode                  |
|                                 codegen                           |
|         parser                                                    |
|                                                                   |
|       Token lexer                                                 |
|         syntax                                                    |
|                                                                   |
|       Compiler                                AntBuilder   grok       |
+-------------------------------------------------------------------+
|         classworlds           asm(objectweb)                      |
+-------------------------------------------------------------------+
|                    Groovy                                         |
+-------------------------------------------------------------------+


```
## 特性
Groovy has the following features −
    Support for both static and dynamic typing.
    Support for operator overloading.
    Native syntax for lists and associative arrays.
    Native support for regular expressions.
    Native support for various markup languages such as XML and HTML.
    Groovy is simple for Java developers since the syntax for Java and Groovy are very similar.
    You can use existing Java libraries.
    Groovy extends the java.lang.Object.

## 
```
+----------------------------------------------------------------------------------------------------+
|                                                                                                    |
|                            ReadWrite Object(String,Serializable)/File/Socket/db                    |
+-------------------------------------------+------------+---------+-----------+-------+-------------+
|Class-based|concurrency|Aspect|            |            |         |           |       |             |
|           |           |      |            |            |         |           |       |             |
+-----------+-----------+------+------+     |            |         |           |       |             |
|                    oop              | pop | Functional | FRP     |Reflective |Generic|             |
+------------------------------+------+------------------------------------------------| Event-driven|      
|            Structured        | Imperative |  Declarative         |  Metaprogramming  |             |
+------------------------------+------------+------------------------------------------+-------------+
|                                                                                                    |
|                             conditional/decision-making/loops                                      |
+----------------------------------+------------+---------------+-+-----------+-------------+--------+
|            Whitespace(tab space) |    0b1     |               | |           |             |        |
|            comment               |    01      | Base Data Type| |           |             | other  |
|            separator sign(;)     |√1g(big num)|               | |           |Quoted id    | symbol/|
|                                  |   0x1      |               | |           |             | token  |
+----------------------------------+ [1,3],[1:2]+---------------+-+           |+------------|        |
|              separator           |   Literals |  keywords       |Operators  | Identifiers |        |
+----------------------------------+------------+-----------------------------+-------------+--------+
|                                                                                                    |
|                                            Character set (Unicode,UTF+8)                           |
+----------------------------------------------------------------------------------------------------+
|                                                                                                    |
|                                            Byte                                                    |
+----------------------------------------------------------------------------------------------------+


```



```
keyword extends java's
+-----------+-----------------------------------------------------------------------+
|  special  |                                                                       |
|identifiers| √def   √in √pull √trait                                               |
+-----------+-----------------------------------------------------------------------+
|           |                                                                       |
|  reserved |  goto    const                                                        |
|           |                                                                       |
+-----------------------------------------------------------------------------------+
|  exception|  try    catch    finally    throw    throws                           |
|  debug    |                                                                       |
|           |  assert                                                               |
+-----------------------------------------------------------------------------------+
|  Access   |                                                                       |
|  modifiers|  public protected    private                                          |
+-----------------------------------------------------------------------------------+
|  package  |  package    import                                                    |
|  control  |                                                                       |
+-----------------------------------------------------------------------------------+
|           |  class  enum    interface                                             |
|  class    |  extends   implements                                                 |
| interface |  new                                                                  |
| modifiers |  this    super                                                        |
+-----------------------------------------------------------------------------------+
|  variable |  void                                                                 |
|  method   |  final    static     abstract   synchronized                          |
|  modifiers|  volatile    native     transient     strictfp                        |
|           |                                                                       |
+-----------+-----------------------------------------------------------------------+
|  Flow     |  if     else                                                          |
|  control  |  do    while    for                                                   |
|  keyword  |  switch    case    default                                            |
|           |  break    continue    return                                          |
|           |  instanceof √as                                                       |
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
|             |   *.( Spread operator)                                       |
| Misc        |   ..(range operator)                                         |
|             |   <=>   (Spaceship operator,delegates to compareTo )         |
|             |  .[index]   (Subscript operator)                             |
|             |  in   (Membership operator)                                  |
|             |  ==/is   (Identity operator.==,equals;is ,reference)         |
|             |  as   (Coercion operator)                                    |
|             |  <>   (Diamond operator,generic types )                      |
|             |  ()   (Call operator,call a method named call implicitly. )  |
+-------------+--------------------------------------------------------------+
|             |                                                              |
|  re         |  ~ ( create a java.util.regex.Pattern)                       |
|             |  =~ ( build a java.util.regex.Matcher)                       |
|             |  ==~                                                         |
+-------------+--------------------------------------------------------------+
|             |                                                              |
|  Object     |  ?(Safe navigation operator) ) .@(Direct field access operator) |
|             |   .& (Method pointer operator                                   |+-------------+--------------------------------------------------------------+
|             |                                                              |
|  conditional|  (? :)                                                       |
|             |                                                              |
+----------------------------------------------------------------------------+
|             |                       |                                      |
|  Assignment |  = += -= *= /= %= **= |< < =   > > =   &=   ^=   |=          |
|             |                       |                                      |
+----------------------------------------------------------------------------+
|             |                                                              |
|  Logical    |  && || !                                                     |
|             |                                                              |
+----------------------------------------------------------------------------+
|             |                                                              |
|  Relational |  == != > < >= <=                                             |
|             |                                                              |
+----------------------------------------------------------------------------+
|             |                                                              |
|  Bitwise    |  & | ^  ~    < <   > >   > > >                               |
|             |                                                              |
+----------------------------------------------------------------------------+
|             |                                                              |
|  Arithmetic |  + - * / % ++ -- **(pow)                                     |
|             |                                                              |
+-------------+--------------------------------------------------------------+

```

### 注释
(Shebang line)
```groovy
#!/usr/bin/env groovy
println "Hello from the shebang line"
```

### 文本字面量（Text literals ）

```
+-----------------------+--------------------------------------------------------+
|       String name     |  String syntax|Interpolated|Multiline |scape character |
|                       |               |            |          |                |
+--------------------------------------------------------------------------------+
|      Single quoted    |      '…​'      |            |          |     \          | 
+--------------------------------------------------------------------------------+
|  Triple single quoted |    '''…​'''    |            |    √     |     \          | 
+--------------------------------------------------------------------------------+
|    Double quoted      |       "…​"     |     √      |          |     \          | 
+--------------------------------------------------------------------------------+
| Triple double quoted  |    """…​"""    |     √      |    √     |     \          | 
+--------------------------------------------------------------------------------+
|        Slashy         |      /…​/      |     √      |    √     |     \          | 
+--------------------------------------------------------------------------------+
| Dollar slashy         |    $/…​/$      |     √      |    √     |     $          |
+-----------------------+---------------+------------+----------+----------------+

```
### 值型字面量及其符号（Number type suffixes/prefix）
```
+-------------------+---------+
| Type      |Suffix |  prefix |
+-----------------------------+
| BigInteger|G or g |         |
|           |       |         |
| Long      |L or l |         |
|           |       |         |
| Integer   |I or i |0b 0 0x  |
|           |       |         |
| BigDecimal|G or g |         |
|           |       |         |
| Double    |D or d |         |
|           |       |         |
| Float     |F or f |         |
+-------------------+---------+

```

### 容器字面量
```
+--------------+-----------------------+-----------------------+-------------------------+
|              |       Lists           |         Arrays        |           Map           |
|              |                       |                       |                         |
+----------------------------------------------------------------------------------------+
|              |   [1, 2, 3]           |  [1, 2, 3] as int[]   |     [key: 'Guillaume']  |
|              |                       |                       |                         |
+----------------------------------------------------------------------------------------+
|  default     | java.util.ArrayList   |                       | java.util.LinkedHashMap |
|              |                       |                       |                         |
+----------------------------------------------------------------------------------------+
|              | [2, 3]  as LinkedList |                       |                         |
|              | java.util.LinkedList  |                       |                         |
+--------------+-----------------------+-----------------------+-------------------------+

```

### 操作符重载
```groovy
+-----------------------------------------------------------------+
| Operator     Method       |Operator         Method              |
+--+--------------------------------------------------------------+
|  +           a.plus(b)    |  a[b]        a.getAt(b)             |
|                           |                                     |
|  -           a.minus(b)   |  a[b] = c   a.putAt(b, c)           |
|                           |                                     |
|  *           a.multiply(b)|  a in b      b.isCase(a)            |
|                           |                                     |
|  /           a.div(b)     |  <<          a.leftShift(b)         |
|                           |                                     |
|  %           a.mod(b)     |  >>          a.rightShift(b)        |
|                           |                                     |
|  **          a.power(b)   |  >>>         a.rightShiftUnsigned(b)|
|                           |                                     |
|  |           a.or(b)      |  ++          a.next()               |
|                           |                                     |
|  &           a.and(b)     |  --          a.previous()           |
|                           |                                     |
|  ^           a.xor(b)     |  +a          a.positive()           |
|                           |                                     |
|  as          a.asType(b)  |  -a          a.negative()           |
|                           |                                     |
|  a()         a.call()     |  ~a          a.bitwiseNegate()      |
+---------------------------+--------------------------------------

```
## 程序结构
```
+-------------------------------------------+
|       Package names                       |
|                                           |
|       Imports                             |
|             Static import/ Star import    |
|             Import aliasing               |
|                                           |
|       Scripts versus classes              |
|                                           |
|           Methods/Variables               |
+-------------------------------------------+

```
 
### 表达式
GPath  
### 类型

##  元编程范式
[官网地址](http://groovy-lang.org/metaprogramming.html)
>groovy.lang.GroovyObject is the main interface in Groovy as the Object class is in Java. 

ExpandoMetaClass和Category可以实现元编程。

```groovy
package groovy.lang;

public interface GroovyObject {

    Object invokeMethod(String name, Object args);

    Object getProperty(String propertyName);

    void setProperty(String propertyName, Object newValue);

    MetaClass getMetaClass();

    void setMetaClass(MetaClass metaClass);
}

```

### Runtime  metaprogramming
###  compile-time metaprogramming
AST转换

##  函数式编程范式

闭包(与java/C++/python的lambda，oc的blocks是一个东西，只是不同语言的不同称呼,都是匿名函数
作用：简洁，捕获变量
### 定义
```groovy
{ [closureParameters -> ] statements }
```
### Default Parameters
```groovy
def someMethod(parameter1, parameter2 = 0, parameter3 = 0) { 
   // Method code goes here 
} 
```



### Delegation strategy
this /owner /delegate 
#### DSL
- "command chain" feature
If a call is executed as a b c d, this will actually be equivalent to a(b).c(d).


### 函数式编程概念

柯里化(currying):将多参数的函数转为单参数的形式
Memoization/Composition/Trampoline/Method pointers
## 面向对象编程


## 文件

eachLine 


