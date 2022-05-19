## 
dart 
结构化程序设计语言

强类型 class-based, optionally typed, and singlethreaded.

强化虚拟机，支持JIT和AOT,单线程

```
+------------------+---------------------------------------------------------------------------------+
|  comunicate      |                                                                                 |
|                  |         ReadWrite Object(String,List,Serializable)/File/Socket/db               |
+------------------+------------------------+------------+---------+-----------+-------+-------------+
|Class-based|concurrency|Aspect|            |            |         |           |       |             |
| Log/Date  |           |      |            |            |         |           |       |             |
+-----------+-----------+------+------+     |            |         |           |       |             |
|        oop                          | pop | Functional | FRP     |Reflective |Generic|             |
+------------------------------+------+------------------------------------------------| Event-driven|      
|            Structured        | Imperative |  Declarative         |  Metaprogramming  |             |
+------------------------------+------------+------------------------------------------+-------------+
|                             conditional/decision-making/loops                                      |
+----------------------------------+------------+---------------+-+-----------+-------------+--------+
|            Whitespace(tab space) |            |               | |           |             |        |
|            comment               |            | Base Data Type| |           |             | other  |
|            separator sign(;)     |10b 01 1 0x1|               | |           |             | symbol/|
+----------------------------------+            +---------------+-+           |             | token  |
|              separator           |  Literals  |  keywords       | Operators | Identifiers |        |
+----------------------------------+------------+-----------------------------+-------------+--------+
|                                            Character set (Unicode,UTF+8)                           |
+----------------------------------------------------------------------------------------------------+
|                                            Byte                                                    |
+----------------------------------------------------------------------------------------------------+

```


``` 61 个
+--------------+---------------------------------------------------------------+
|              |      await 3  yield 3                                         |
+--------------+---------------------------------------------------------------+
|              |      deferred 2   covariant 2   factory 2     mixin 2         |extension late
| built-in     |      as 2             external 2                              |
| identifiers  |      import 2    export 2   library 2    part 2               |
|              |      abstract 2  dynamic 2    implements 2   interface 2      |
|              |      get 2    set 2                                           |
|              |      typedef 2    static 2    operator 2     Function 2       |
+------------------------------------------------------------------------------+
|  contextual  |      on 1    sync 1                                           |
|  keywords    |      async 1                                                  |
|              |      show 1        hide 1                                     |
+------------------------------------------------------------------------------+
|              |      try  catch  finally    rethrow     throw    assert       |
|              |      enum  class  extends     super  this    new              |
|   reserved   |      void    final  const   var    in    is                   | 
|   words      |      if   else    while    do  for                            |
|              |      switch  case  default  with   break    continue  return  |
|              |      null   true  false                                       |
+------------------------------------------------------------------------------+

1 are contextual keywords
2 are built-in identifiers
3 are newer
other words in the table are reserved words

```

```Operaters

+---------------------------------------------------------------------------------+
| unary postfix            () function call    [] List access                     |
|                           .  Member access   ?.  Conditional member access      | 
|                                                                                 |
|                                                                                 |
| additive/multipl       +    -   -expr   *    /    %  ~/                         | 
|                        ++expr    ++expr    expr++    expr++                     |
|                                                                                 |
| bitwise/shift          &     ^     |  ~expr    < <   > >                        |
|                                                                                 |
| relational and type test    >=    >    <=    <    as    is    is!               |
| equality                    ==    !=                                            |
|                                                                                 |
|                                                                                 |
| logical                     &&    ||    !expr                                   |
|                                                                                 |
| if null                     ??                                                  |
|                                                                                 |
| conditional                 expr1 ? expr2 : expr3                               |
|                                                                                 |
| cascade                     ..                                                  |
|                                                                                 |
| assignment                  =                                                   |
|                             *=    /=    ~/=    %=                               |
|                                                                                 |
|                             +=    -=   < <=   > >=    &=    ^=    |=    ??=     |
+---------------------------------------------------------------------------------+



```

## dart 数据类型

```
内置类型

numbers （Int 整数不超过 64 位，编译为js,整数不超过 54 位; double 64位）
strings （UTF-16编码的序列）
booleans
lists (also known as arrays)
maps

runes (for expressing Unicode characters in a string UTF-32代码点)
symbols（ represents an operator or identifier declared in a Dart program. ）
```
var 

字符串转义
$ character or ${ } 
## 声明式编程
Dart is a true object-oriented language, so even functions are objects and have a type, Function.

You can also create a nameless function called an anonymous function, or sometimes a lambda or closure. 
```
([[Type] param1[, …]]) { 
  codeBlock; 
}; 
```

## 纯面向对象编程


### 虚拟机

generational garbage collection


###

封装（class,library,import,part，）
继承（abstract,extends ,implements,super）
多态 Overridable operators

#### import
 不完全导入 **show**
 隐藏导入 **hide**
 命名冲突 **as**
 库的拆分 **part**
```
dart:io File, socket, HTTP, and other I/O
dart:core Built-in types, collections, and other core functionality 
dart: math Mathematical constants and functions, plus a random number generator.
dart: convert Encoders and decoders
dart: typed_data 

```
### 数据并发
isolates
#### [异步支持](https://www.dartlang.org/guides/language/language-tour#asynchrony-support)



## 元编程
### [泛型](https://www.dartlang.org/guides/language/language-tour#generics)

