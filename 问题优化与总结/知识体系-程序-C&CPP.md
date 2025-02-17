

```
+------------------+---------------------------------------------------------------------------------+
|  compiler        |         Clang ,GCC ,Microsoft Visual Studio / Express / C++                     |
+------------------+---------------------------------------------------------------------------------+
|  library         |         Bionic libhybris,      glibc ,      Microsoft Run-time Library          |
+------------------+---------------------------------------------------------------------------------+
|  comunicate      | Char (ctype.h)   File I/O (stdio.h)   Math (math.h)   Dynamic memory(stdlib.h)  |
|                  | String (string.h)     Time (time.h)   Variadic (stdarg.h)  POSIX(unistd.h)      |
+------------------+------------------------+------------+---------+-----------+-------+-------------+
|Class-based|concurrency|Aspect|            |            |         |           |       |             |
| Log/Date  |           |      |            |            |         |           |       |             |
+-----------+-----------+------+------+     |            |         |           |       |             |
|        oop                          | pop | Functional | FRP     |Reflective |Generic|             |
+------------------------------+------+------------------------------------------------| Event-driven|      
|            Structured        | Imperative |  Declarative         |  Metaprogramming  |             |
+------------------------------+------------+------------------------------------------+-------------+
|                             conditional/decision-making/loops                                      |
+----------------------------------------------------------------------------------------------------+
|                                   Preprocessor Commands                                            |
+----------------------------------+------------+---------------+-+-----------+-------------+--------+
|            Whitespace(tab space) |            |               | |           |             |        |
|            comment               |            | Base Data Type| |           |             | other  |
|        line separator sign(;)    |10b 01 1 0x1|               | |           |             | symbol/|
+----------------------------------+            +---------------+-+           |             | token  |
|              separator           |  Literals  |  keywords       | Operators | Identifiers |        |
+----------------------------------+------------+-----------------------------+-------------+--------+
|                                            Character set (Unicode,UTF+8)                           |
+----------------------------------------------------------------------------------------------------+
|                                            Byte                                                    |
+----------------------------------------------------------------------------------------------------+


```
## 系统

C语言产生的原因：汇编需要操作地址，C语言不用关注代码物理地址,编译器已经做了看护工作，保证地址不会出问题
类型，数组与指针，方法，文件io，线程

### 概念
[UNIX](https://en.wikipedia.org/wiki/Unix)
![UNIX家族](https://upload.wikimedia.org/wikipedia/commons/7/77/Unix_history-simple.svg)
[POSIX](https://en.wikipedia.org/wiki/POSIX)

### 说明
[API](https://en.cppreference.com/w/)
[教程](https://www.tutorialspoint.com/cplusplus/cpp_classes_objects.htm)
## 库文件实现
dlopen打开，dlsym链接方法
### C库
只要是符合ANSI（美国国家标准学会） C标准的C库都可以叫做标准C库。

现有的著名的POSIX标准的C库有：GUN C库（glibc）和Embedded Linux C库（uClibc） Bionic（Android）

[Single_UNIX_Specification](https://en.wikipedia.org/wiki/Single_UNIX_Specification)

[C POSIX library](https://en.wikipedia.org/wiki/C_POSIX_library)
 
```
[c header](https://en.cppreference.com/w/c/header)
[cpp header](https://en.cppreference.com/w/cpp/header)

+-------+-------------------------------------------------------------------------------+------------------------------------+
|       |                                                                               |                                    |
|       | <aio.h>       <iconv.h>       <poll.h>       <string.h>       <sys/uio.h>     |  <assert.h>        <stdatomic.h>c11|
|       |                                                                               |                                    |
|       | <arpa/inet.h> <inttypes.h>    <pthread.h>    <strings.h>      <sys/un.h>      |  <complex.h> C99   <stdbool.h> C99 |
|       |                                                                               |                                    |
|       | <assert.h>    <iso646.h>      <pwd.h>        <stropts.h>      <sys/utsname.h> |  <ctype.h>         <stddef.h>      |
|       |                                                                               |                                    |
|       | <complex.h>   <langinfo.h>    <regex.h>      <sys/ipc.h>      <sys/wait.h>    |  <errno.h>         <stdint.h>  C99 |
|       |                                                                               |                                    |
| POSXI | <cpio.h>      <libgen.h>      <sched.h>      <sys/mman.h>     <syslog.h>      |  <fenv.h>    C99   <stdio.h>       |
|       |                                                                               |                                    |
|       | <dirent.h>    <limits.h>      <search.h>     <sys/msg.h>      <tar.h>         |  <float.h>         <stdlib.h>      |
|       |                                                                               |                                    |
|       | <dlfcn.h>     <locale.h>      <semaphore.h>  <sys/resource.h> <termios.h>     |  <inttypes.h>      <stdnoreturn.h> |
|       |                                                                               |                                c11 |
|       | <errno.h>     <math.h>        <setjmp.h>     <sys/select.h>   <tgmath.h>      |  <iso646.h>  c95   <string.h>      |
|       |                                                                               |                                    |
|       | <fcntl.h>     <monetary.h>    <signal.h>     <sys/sem.h>      <time.h>        |  <limits.h>        <tgmath.h>  C99 |
|       |                                                                               |                                    |
|       | <fenv.h>      <mqueue.h>      <spawn.h>      <sys/shm.h>      <trace.h>       |  <locale.h>        <threads.h> C11 |
|       |                                                                               |                                    |
|       | <float.h>     <ndbm.h>        <stdarg.h>     <sys/socket.h>   <ulimit.h>      |  <math.h>          <time.h>        |
|       |                                                                               |                                    |
|       | <fmtmsg.h>    <net/if.h>      <stdbool.h>    <sys/stat.h>     <unistd.h>      |  <setjmp.h>        <uchar.h>   C11 |
|       |                                                                               |                                    |
|       | <fnmatch.h>   <netdb.h>       <stddef.h>     <sys/statvfs.h>  <utime.h>       |  <signal.h>        <wchar.h>   C95 |
|       |                                                                               |                                    |
|       | <ftw.h>       <netinet/in.h>  <stdint.h>     <sys/time.h>     <utmpx.h>       |  <stdalign.h> c11  <wctype.h>  C95 |
|       |                                                                               |                                    |
|       | <glob.h>      <netinet/tcp.h> <stdio.h>      <sys/times.h>    <wchar.h>       |  <stdarg.h>                        |
|       |                                                                               |                                    |
|       | <grp.h>       <nl_types.h>    <stdlib.h>     <sys/types.h>    <wctype.h>      +------------------------------------+
|       |                                                                               |           basic C library          |
|       |                                                               <wordexp.h>     |                                    |
|       |                                                                               |           (ANSI or ISO C Library)  |
+-------+-------------------------------------------------------------------------------+------------------------------------+


[POSIX Library Functions](https://www.mkompf.com/cplus/posixlist.html)

[Classification of Library Functions](https://www.improgrammer.net/header-file-list-functions-c-language/)

Type support 
Program utilities 
Variadic functions 
Error handling
Dynamic memory management 
Date and time utilities
Strings library
Null-terminated strings:
Algorithms
Numerics
    Common mathematical functions
    Floating-point environment (C99)
    Pseudo-random number generation
    Complex number arithmetic (C99)
    Type-generic math (C99)

Input/output support
Localization support
Atomic operations library (C11)
Thread support library (C11)


```
[POSIX 定义](https://pubs.opengroup.org/onlinepubs/9699919799/basedefs/contents.html)
[POSIX 描述](https://en.wikipedia.org/wiki/C_POSIX_library)

[C ANSI/POSIX library 常用api](https://web.archive.org/web/20100724201155/http://www.space.unibe.ch/comp_doc/c_manual/C/FUNCTIONS/funcref.htm)
 [api](https://en.cppreference.com/w/)

### C++ 库
C++ Standard Library is a collection of classes and functions

(Apache stdcxx)[https://stdcxx.apache.org/download.html]
(GNU libstdc++)[https://gcc.gnu.org/onlinedocs/libstdc++/]
(llvm libcxx)[https://libcxx.llvm.org/docs/]
```
[The Standard Function Library](https://www.tutorialspoint.com/cplusplus/cpp_standard_library.htm)


Language support library

Concepts library (C++20)

Error Handling

General utilities library

Strings library

Containers library

Iterators library

Ranges library (C++20)

Algorithms library

Numerics library

Input/output library

Localizations library

Regular expressions library (C++11)

Atomic operations library (C++11)

Thread support library (C++11)

Filesystem library (C++17)

```


(C++ Standard Library headers)[https://en.cppreference.com/w/cpp/header]
## 符号
符号包括：标识符，关键字，注释
### 数据标识（变量，常量）的定义，声明，初始化，赋值及作用域

- C/C++ 中，有两种简单的定义常量的方式：
1. 使用 #define 预处理器。
2. 使用 const 关键字


#### 宏定义（#define）

- （#）字符串化操作符
- （##）符号连接操作符
- （#@）单字符化操作符
- （\） 续行操作符

### 声明(Declarations)
#### Specifiers
- storage class specifier (register, static, thread_local (since C++11), extern, mutable).
- Type specifiers( class,emum,simple type,...)

``` C
extern "C" {       //告诉编译器下面的函数是c语言函数（因为c++和c语言对函数的编译转换不一样，主要是c++中存在重载）
```

### 关键字与符号
[关键词](https://blog.csdn.net/weibo_dm/article/details/81629693)
```
+----+---------------------------------------------------+
|    | _Alignas _Alignof _Atomic _Static_assert          |
|C11 | _Noreturn  _Generic     _Thread_local             |
+--------------------------------------------------------+
|C99 | _Bool inline restrict _Complex _Imaginary         |
+--------------------------------------------------------+
|    | continue break return goto                        |
|    | if else switch  case default for  do while        |
|    | typedef  sizeof volatile                          |
|ANSI|                                                   |
|  C | const static extern register auto                 |
|(32)|                                                   |
|    | union struct enum  void                           |
|    | char  int float double                            |
|    | short long unsigned signed                        |
|----+---------------------------------------------------|

+---------+-----------------------------------------------------------------------+
| c++ 11  | alignas alignof                                                       |
| (10)    | constexpr decltype  thread_loca                                       |
|         | noexcept  static_assert                                               |
|         | char16_t char32_t  nullptr                                            |
+---------------------------------------------------------------------------------+
|         | try   catch    throw                                                  |
|         | typedef  sizeof  typeid   Template    typename                        |
|         | static_cast dynamic_cast const_cast reinterpret_cast                  |
|         | namespace  using export                                               |
|         | new delete  operator this                                             |
|         | inline   virtual  explicit                                            |
|         | private protected  friend public                                      |
|         | asm                                                                   |
|         +----------+------------------------------------------------------------+
|         | Storage  |    auto register static extern mutable                     |
|         | Class    |                                                            |
|         +-----------------------------------------------------------------------+
|         |          |   signed unsigned   long  short                            |
| C++ 98  | Modifier +------------------+-----------------------------------------+
|         | Types    | Type Qualifiers  |   const  volatile restrict              |
| (63)    +----------+------------------+-----------------------------------------+
|         | break  continue  goto  return                                         |
|         | if  else   switch case default for do while                           |
|         |                                                                       |
|         | union  enum struct   class                                            |
|         |                                                                       |
|         | bool char wchar_t   int  float double   void                          |
|         | false true                                                            |
+---------+-----------------------------------------------------------------------+


```
``` Operators
+-------------+--------------------------------------------------------------+
|  C++        |  sizeof                                                      |
|  Misc       |  (? :)                                                       |
|(member access)|  ,(comma operator)                                         |
|             |  .(dot)and->(arrow) (reference field)                        |
|             |  cast               (type cast)                              |
|             |  &(pointer operator)                                         |
|             |  *(pointer operator)                                         |
+-------------+--------------------------------------------------------------+
|  C          |  sizeof                                                      |
|  Misc       |  & (return address)                                          |
|(member access)|  *(pointer operator)                                         |
|             |  (? :)                                                       |
+----------------------------------------------------------------------------+
|  Logical    |  && || !                                                     |
+----------------------------------------------------------------------------+
|  Relational |  == != > < >= <=                                             |
+----------------------------------------------------------------------------+
|  Assignment |  = += -= *= /= %=   < < =   > > =   &=   ^=   |=             |
+----------------------------------------------------------------------------+
|  Bitwise    |  & | ^  ~    < <   > >                                       |
+----------------------------------------------------------------------------+
|  Arithmetic |  + - * / % ++ --                                             |
+-------------+--------------------------------------------------------------+


```
## 数据类型与内存结构
[C内存模型](https://blog.csdn.net/ufolr/article/details/52833736)
```
                                                 ++   [<signed | unsigned>] char
                                                 |
                                  +IntegerType   ++    [<signed | unsigned>] [<short | long >] int
                                  |
                         +  Base  |
                         |  Type  |
                         |        |               +   Float
                         |        +floating-point |
                         |                        +   [long]  double
            + arithmetic + EnumType
            | Type
            |            +  pointerType
            |            |                +   Array
            |  derived   |  AggregateType |
            |  Type      |                +   Struct // 结构是 C
            |            |  functionType
            |            +  union // 共用体是一种特殊的数据类型，允许您在相同的内存位置存储不同的数据类型。
            |                     //任何时候只能有一个成员带有值。
            + VoidType


                                  +IntegerType      [<signed | unsigned>] [<short | long | long long>] int
                                  |                （修饰符类型）  
                         +  Number|
                         |  Type  |
                         |        |            +    float
                         |        +DecimalType |
                         |                     +    [long] double
            ++Primitive  ++ Char  +  [<signed | unsigned >]char
            |  Built-in  |  Type  |
            |            |        +  [<signed | unsigned >]wchar_t
            |            |
            |            ++ boolean
            |            |
            |            ++ void
            |
            |            +  Arr: [<extern|static>][const] type array[size][={list}];
C++         |            |
data type   | derived+-+ |  Struct
            | Type       |
            |            |  Union
            |            |
            |            ++ Enum
            |
            |            +  Addr（&）
            | PointerType|
            |            +  Pointer（*）:[const] type * [const] ptr[=pointer expression];
            |
            + Class （String class）:[const] type object[(argument list)];

```


### 指针类型
- C++ 引用

智能指针在C++11版本之后提供，包含在头文件<memory>中，shared_ptr、unique_ptr、weak_ptr
**智能指针**是利用了一种叫做RAII（资源获取即初始化）的技术对普通的指针进行封装，这使得智能指针实质是一个**对象**，行为表现的却像一个**指针**。
**智能指针**利用了**栈对象**的生存期，将资源的获取放在构造函数里面，资源的释放放在析构函数里面，从而保证了资源一定会被正确释放。
```c

shared_ptr 允许多个指针指向同一个对象。为了解决 auto_ptr 在对象所有权上的局限性(auto_ptr 是独占的)。
unique_ptr 独占所指向的对象 , 替代auto_ptr。不能进行拷贝、赋值等操作，但是可以通过release函数在unique_ptr之间转移控制权；
weak_ptr shared_ptr的弱引用 
```
### 函数类型
构造函数 跟冒号 
```
1、对含有对象成员的对象进行初始化
2、对于不含对象成员的对象，初始化时也可以套用上面的格式
3、对父类进行初始化

```
函数冒号后面跟冒号的是赋值，这种写法是C++的特性。

- 函数指针变量的声明：
```c
typedef int (*fun_ptr)(int,int); // 声明一个指向同样参数、返回值的函数指针类型
```
- Lambda 函数
C++11 提供了对匿名函数的支持,称为 Lambda 函数(也叫 Lambda 表达式)
```c
[capture](parameters)->return-type{body}

[] 内是一个capture，可以在lambda内部访问的"nonstatic外部变量"，如果没有要访问的变量，可以为空。static变量是可以直接被访问的。
() 内是参数，和函数参数一样。
... 是mutable, 异常明细, 属性说明符(noexcept等), 或者返回类型。如果其中之一出现，那么必须出现()。
{} 内是函数体，在这里面写明lambda要完成的工作。
```

```
[]      // 沒有定义任何变量。使用未定义变量会引发错误。
[x, &y] // x以传值方式传入（默认），y以引用方式传入。
[&]     // 任何被使用到的外部变量都隐式地以引用方式加以引用。将外部所有变量传递给lambda， 可以在内部修改，并且影响外部变量 -- passing by ref
[=]     // 任何被使用到的外部变量都隐式地以传值方式加以引用。所有变量传递给lambda，但是不能在内部修改 -- passing by value
[&, x]  // x显式地以传值方式加以引用。
[=, &z] // z显式地以引用方式加以引用。
```
## 编译

### 预处理命令

[预处理命令](https://blog.csdn.net/qq_35038153/article/details/71293265)
```
+--------------------+--------------------+-------------------------+----------------------+
|                    |  Predefined Macros |  Operator               |                      |
+------------------------------------------------------------------------------------------+
| #include           |                    |                         |                      |
+------------------------------------------------------------------------------------------+
| #define  #undef    | __DATE__           | Macro Continuation (\)  |                      |
|                    |("MMM DD YYYY" )    |                         |                      |
|(preprocessor macro)|   __TIME__         |  Stringize (#)          | Parameterized Macros |
|                    |  ("HH:MM:SS")      |                         |                      |
|                    | __FILE__           | Token Pasting (##)      |                      |
|                    |                    |                         |                      |
|                    | __LINE__           | Defined()               |                      |
|                    |                    |                         |                      |
|                    | __STDC__           |                         |                      |
|                    |                    |                         |                      |
+------------------------------------------------------------------------------------------+
| #ifdef   #ifndef   |                    |                         |                      |
| #if      #elif     |                    |                         |                      |
| #else    #endif    |                    |                         |                      |
|                    |                    |                         |                      |
| #error             |                    |                         |                      |
|                    |                    |                         |                      |
| #pragma            |                    |                         |  region，pack ，data_seg，disable   |
+--------------------+--------------------+-------------------------+----------------------+
```  
The #define preprocessor directive creates symbolic constants. The symbolic constant is called a macro 
```c
#define macro-name replacement-text 
```
- 预处理器
  C 预处理器只不过是一个文本替换工具而已，它们会指示编译器在实际编译之前完成所需的预处理。


为了尽可能地兼容，一般都遵循#define定义“可读”的常量以及一些宏语句的任务，而typedef则常用来定义关键字、冗长的类型的别名。


### namespace 和 using

```
在C语言中只有一个全局作用域：

1.C语言中所有的全局标识符共享一个作用域
2.标识符之间可能发生冲突
C++中提出了命名空间的概念：

1.命名空间将全局作用域分成不同的部分，
2.不同命名空间中的标识符可以同名而不会发生冲突
3.命名空间可以发生嵌套
4.全局作用域也叫默认命名空间
```
>[本文为CSDN博主「李燕良」的原创文章，遵循 CC 4.0 BY-SA 版权协议，转载请附上原文出处链接及本声明。](https://blog.csdn.net/qq_40416052/article/details/82528676)

### 编译构建流程
``` shell

--build=X86, --host=X86, --target=X86

```
### 交叉编译（cross compiler）

[ 交叉编译 ](https://blog.csdn.net/LEON1741/article/details/81537529)


 [GCC Compilation Process（Build）](https://www3.ntu.edu.sg/home/ehchua/programming/cpp/gcc_make.html)
```
                         +
                         |
 Source Code(.c,.cpp,.h) |
                         v
                 +-------+----+
                 |Preprocessin|  step1:Preprocessor(cpp)
                 +-------+----+
                         |
 Include Header,         v
 Expand Macro    +-------+----+
 (.i,.ii)        | Compilation|  step 2:Compiler(gcc,g++)
                 +-------+----+
                         |
 Assembly(.s)            v
                 +-------+----+
                 | Assemble   |  step 3: Assembler(as)
                 +-------+----+
                         |
 Machine Code(.o,.obj)   v
                 +-------+----+
Static Library+->+ Linking    |  step 4:Linker(ld)
(.lib,.a)        +-------+----+
                         |
 Executable Machine Code |
 (.exe)                  v

```
compile, make都编译，但make是增量编译，compile是全新编译， build整个工程的全新编译，要生成binary的。
CMake的是构建系统的生成器，CMake代表跨平台Make。
- 可以产生Makefile文件，
- 可以产生Ninja构建文件，
- 可以产生KDevelop或XCode的项目，
- 能产生Visual Studio解决方案。

```
+----------+---------------------------------------------------------------------------------------------+
|          |      GNU C Library (glibc): core C library including headers, libraries, and dynamic loader |
|          |                                                                                             |
|          |                                                                                             |
|          |      GNU m4: an m4 macro processor                                                          |
|          |                                                                                             |
|          |      GNU Debugger (GDB)                                                                     |
|          +---------------------------------------------------------------------------------------------+
|   GNU    |   GNU Binutils:                                                                             |
|          |       GNU profiler  gprof    addr2line   dlltool   nlmconv   objdump   size	  windmc       |
|          |       linking         ld     ar          gold      nm        ranlib    strings   windres    |
|toolchain |       GNU Assembler(as)      c++filt               objcopy   readelf   strip	               |
|          +---------------------------------------------------------------------------------------------+
|          |    GNU Compiler Collection (GCC)                                                            |
|          +-------------+-------------------------------------------------------------------------------+
|          |             | GNU Bison: a parser generator, often used with the Flex lexical analyser      |
|          | Development |                                                                               |
|          |             | GNU build system (autotools):                                                 |
|          | tools       |        Autoconf(generates a configure), Automake and Libtool                  |
|          |             | GNU make: an automation tool for compilation and build                        |
|          |             |                                                                               |
+------------------------+-------------------------------------------------------------------------------+
| Terminals|    mintty(a program that run a shell)                                                       |
+--------------------------------------------------------------------------------------------------------+
|  shell   |    Bash( a program which processes commands) ,Coreutils                                     |
+----------+---------------------------------------------------------------------------------------------+
|                                configure scripts                                                       |
+----------+---------------------------------------------------------------------------------------------+


[autotools介绍](https://www.jianshu.com/p/ff361db3e6d3)
 
```
[编写autoconf（configure.ac）生成Configure，编写automake（Makefile.am）生成makefile.in，构建](https://thoughtbot.com/blog/the-magic-behind-configure-make-make-install)
[autoconf编写](https://www.gnu.org/software/autoconf/manual/autoconf-2.69/html_node/Setup.html#Setup)





## 对象

对象的创建方法
1. 栈中分配内存（分为隐式创建和显示创建）不带new关键词
2. 堆中创建（new 关键字）**A * a = new A()**

为什么要用堆创建
```
一是需要一块大内存的时候，一般栈的大小在8M左右（具体看编译器，不过一般都不会太大），所以需要大内存时需要new。
二是给全局或作用域较大的指针分配对象时。栈创建智能在一个线程内
```

### C++类型转化
C++中强制类型转换操作符有static_cast、dynamic_cast、const_cast、reinterpert_cast四个
基本数据类型之间的转换用static_cast, 但是由于数值范围的不同，需要用户保证转换的安全性
类指针或引用的下行转换用dynamic_cast并且判断转换后是否为空
消除数据的const、volatile、__unaligned属性，用const_cast
不同类型之间的指针或引用的转换用reinterpret_cast，它的本质是对指向内存的比特位的重解释

### 结构类型
C
```
struct tag { 
    member-list
    member-list 
    member-list  
    ...
} variable-list ;

位域
有些信息在存储时，并不需要占用一个完整的字节，而只需占几个或一个二进制位。

```
C++ 标准库没有提供所谓的日期类型。C++ 继承了 C 语言用于日期和时间操作的结构和函数。需要在 C++ 程序中引用 <ctime> 头文件。
四个与时间相关的类型：clock_t、time_t、size_t 和 tm
### 内存查看工具
  1. vs环境下，选项设置C/C++->命令行，然后在其他选项这里写上/d1 reportAllClassLayout，查看内存结构
  2. sizeOf()

c ：Stack,Heap， Dat区/GVAR(global value),Bss（未初始化的和零值全局变量）,Text代码段(Code)
c++ ：5个区：栈、堆、自由存储区、全局/静态存储区、常量存储区

内存分为三块:静态区，堆区，栈区。外部变量和全局变量存放在静态区，局部变量存放在栈区，动态开辟的内存存在堆区。 

```shell
查看内存分布

> gcc memory-layout.c -o memory-layout
> size memory-layout
```

```
<pre>
    XX----------------------------------------+XX      
    |                                         |        
    |              Stack                      |        //动态分配内存,每个线程都会有自己的栈
|   |                                         |        
|   +-----------------------------------------+        
v   |                                         |        
    |                                         |        
    |                                         |        
    |                                         |        
    |                                         |        
    |                                         |        
    |                                         |        
    |                                         |        
^   |                                         |        
|   |                                         |        
|   +-----------------------------------------+        //动态分配内存。堆是操作系统维护的一块内存，
    |              Heap                       |        //而自由存储是C++中通过new与delete动态分配和释放对象的抽象概念。
    |                                         |        //C++ new 本质也是通过malloc()分配
    +-----------------------------------------+        
    |              Bass          |            |        // 未初始化的和零值全局变量。静态分配，在程序开始时通常会被清零。
    |                            |            |        
    +-----------------------------   c++      |
    |              GVAR          |  不区分     |        // 已初始化的非零全局变量。静态分配。
    |                            |  有无初始化 |       
    +-----------------------------------------+        
    |              TEXT                       |        //存放程序执行代码，同时也可能会包含一些常量(如一些字符串常量等）
    |                                         |        // 该段内存为静态分配，只读(某些架构可能允许修改)。 
    +-----------------------------------------X 

</pre>

```

 

#### 关键词
 typedef 
  - 抑制劣质代码，容易记忆的**类型名**
  - 代码简化，定义[**复杂声明**](https://blog.csdn.net/skywalker_leo/article/details/48622193)“右左法则”： 从变量名看起，先往右，再往左，碰到一个圆括号就调转阅读的方向；括号内分析完就跳出括号，还是按先右后左的顺序，如此循环，直到整个声明分析完。
  - typedef 声明看起来象 static，extern 等**存储类关键字**类型的变量声明，typedef声明中不能用 register（或任何其它存储类关键字）
  - typedef 有另外一个重要的用途，那就是**定义机器无关的类型**

```
int My_Add(int a, int b)
{
    return a + b;
}
int My_Sub(int a, int b)
{
    return a - b;
}
struct  CTest
{
    int(*Add)(int, int); //函数指针
    int(*Sub)(int, int);
};
```
 const char * 、char const *、 char * const 三者的区别
 * 隔开声明
  const char * 、char const * 一致，引用常量区字符串，可以修改指针不可修改数据
  char * const 不可修改指针，可以修改数据

  char s1[] 常量池栈中备份，地址不能变，数据可以变
  char* s2  引用常量区，引用可变，数据不能变

![](.\res\cppstr.png)
存储类的关键字（如auto、extern、mutable、static、register等一样）


- inline
 在c/c++中，为了解决一些频繁调用的小函数大量消耗栈空间（栈内存）的问题，特别的引入了inline修饰符，表示为内联函数。，使用在函数声明处，表示程序员请求编译器在此函数的被调用处将此函数实现插入，而不是像普通函数那样生成调用代码(申请是否有效取决于编译器)。 
 ## 指令 -运算符

- sizeof
sizeof 运算符返回变量的大小。例如，sizeof(a) 将返回 4，其中 a 是整数。
- .（点，C++）和 ->（箭头）
成员运算符用于引用类（C++）、结构和共用体的成员。
- Cast（C++）
强制转换运算符把一种数据类型转换为另一种数据类型。例如，int(2.2000) 将返回 2。
- &
指针运算符 & 返回变量的地址。例如 &a; 将给出变量的实际地址。
- *
指针运算符 * 指向一个变量。例如，*var; 将指向变量 var。

- 随机数
## 字符串
c 字符串

在 C 语言中，字符串实际上是使用 null 字符 '\0' 终止的一维字符数组。
char greeting[6] = {'H', 'e', 'l', 'l', 'o', '\0'};
char greeting[] = "Hello";

- 字符串操作
  c #include <string.h>
  c++ #include <cstring>
strcpy()，strcat（）

c++ 包含string类，通过 **#include <string>** 引入

## 数据面向对象 - C++类
###  类与对象

C与C++结构体区别：
```
1. C语言中的结构体不能为空
2. C语言的结构体中不能定义成员函数，但是却可以定义函数指针，C++可以直接定义函数
3. C语言中结构体变量定义的时候，若为struct 结构体名 变量名定义的时候，struct不能省略
```
C++中的struct对C中的struct进行了扩充，C++为C语言中的结构体引入了成员函数、访问控制权限、继承、包含多态等面向对象特性。
 
C++中的struct与类区别
```
1. struct是值类型，class是引用类型。值类型变量的赋值操作，仅仅是2个实际数据之间的复制。而引用类型变量的赋值操作，复制的是引用，即内存地址，由于赋值后二者都指向同一内存地址，所以改变其中一个，另一个也会跟着改变。值类型的内存不由垃圾回收控制，作用域结束时，值类型会自行释放，减少了托管堆的压力，因此具有性能上的优势。
2. struct作为数据结构的实现体，它默认的数据访问控制是public的，而class作为对象的实现体，它默认的成员变量访问控制是private的
3. class还可以用于表示模板类型，struct则不行。

```
### 对象的创建
1. 栈上创建对象（Objects on the Stack）
2. 在堆上创建对象（Objects on the Heap）
使用new方法。可以用在反射


### 抽象，封装，继承（重载），多态

- 数据封装是一种把数据和操作数据的函数捆绑在一起的机制，数据抽象是一种仅向用户暴露接口而把具体的实现细节隐藏起来的机制。
- 继承类型（class DeriveClass:acess-label BaseClassName）
[继承中的内存布局](http://blog.csdn.net/randyjiawenjie/article/details/6693337)
1. 虚函数指针(vptr)放最前，之后放变量。
2. 多个父类排着放，再放子类
3. 子类的覆盖的虚函数将所有祖先的同名虚函数都覆盖。
4. 子类其它的虚函数指针放在第一个父类的虚函数表里。
5. 虚拟继承的情况只需要在钻石继承中有必要使用（避免二义性），子类中最先的祖先放最后。
 
分为四种情况：
1.单继承
2.多继承（不含钻石继承）
3.非虚继承的钻石继承
4.虚继承的钻石继承

- 多态意味着相同方法名，不同操作（重载）或不同子类调用成员函数时，会根据调用函数的对象的类型来执行不同的函数。（重写）

C++ 用virtual修饰**虚函数/纯虚函数**实现重写，避免父类指针调用父类方法，而不是衍生类方法
```
Shape * shape1=new Circle();
shape1->calArea();//Circle#calArea没有用virtural修饰，会调用父类Shape的calArea方法
```
- 接口（抽象类）
如果类中至少有一个函数被声明为纯虚函数，则这个类就是抽象类

- 命名空间
  区分不同库中相同名称的函数、类、变量等。


### 智能指针
shared_ptr
### 类的生命周期


### 类型转换
```
+---------------------------------+-------------------------+
|                    |class type  | base type|   other      |
+-----------------------------------------------------------+
|  static_cast       |   √        |     √    |              |编译期间转换，会抛出一个编译错误
|                    |            |          |              |
+-----------------------------------------------------------+
|  dynamic_cast      |   √        |          |              |类继承层次间的类型安全的向下转型，运行时类型识别(RTTI)
+-----------------------------------------------------------+
|  reinterpert_cast  |            |          |  any pointer |高度危险的转换
+-----------------------------------------------------------+
|  const_cast        |            |          |  const       |
|                    |            |          |  volatile    | 字符串，用于 const 与非 const
|                    |            |          |  __unaligned |volatile 与非 volatile 之间的转换
|                    |            |          |              |
+--------------------+------------+----------+--------------+

```

## 元编程
### 反射
C++11，运行时类型识别(RTTI)
关于RTTI:dynamic_cast（动态强制转换）,typeid,type_info（类型信息）
```c
type_info = dynamic_cast < type-id > ( expression)

```
是专门用于具有继承关系的类之间转换的，尤其是向下类型转换，是安全的.RTTI 依赖于虚表,即dynamic_cast有虚函数


### template
- 模板与泛型编程
  模板是泛型编程的基础，泛型编程即以一种独立于任何特定类型的方式编写代码。
- typename:1.模板定义,2.使用嵌套依赖类型(nested depended name)

## 数据访问
内存，文件，网络，数据库
### 数据容器
[Containers](http://www.cplusplus.com/reference/stl/)

### 标准IO
[CPP IO 类图](http://www.cplusplus.com/reference/iolibrary/)
- C 
stdin 键盘 
stdout 屏幕 
stderr 您的屏幕

- C++
```
<iostream>
处理控制台IO。
该文件定义了 cin、cout、cerr 和 clog 对象，分别对应于标准输入流、标准输出流、非缓冲标准错误流和缓冲标准错误流。
<iomanip>
该文件通过所谓的参数化的流操纵器（比如 setw 和 setprecision），来声明对执行标准化 I/O 有用的服务。
<fstream>
处理命名文件IO。
<stringstream>
内存string的IO。
```
### 文件和流
C
```
#include <stdio.h>
 
int main()
{
   FILE *fp = NULL;
 
   fp = fopen("/tmp/test.txt", "w+");
   fprintf(fp, "This is testing for fprintf...\n");
   fputs("This is testing for fputs...\n", fp);
   fclose(fp);
}
```

C++
```
ofstream
该数据类型表示输出文件流，用于创建文件并向文件写入信息。
ifstream
该数据类型表示输入文件流，用于从文件读取信息。
fstream
该数据类型通常表示文件流，且同时具有 ofstream 和 ifstream 两种功能，这意味着它可以创建文件，向文件写入信息，从文件读取信息。
```

### C++异常处理
```
throw: 当问题出现时，程序会抛出一个异常。这是通过使用 throw 关键字来完成的。
catch: 在您想要处理问题的地方，通过异常处理程序捕获异常。catch 关键字用于捕获异常。
try: try 块中的代码标识将被激活的特定异常。它后面通常跟着一个或多个 catch 块。
```

std::runtime_error
理论上不可以通过读取代码来检测到的异常。

std::logic_error
理论上可以通过读取代码来检测到的异常。


## 系统数据

信号处理

``` s
SIGABRT
程序的异常终止，如调用 abort。
SIGFPE
错误的算术运算，比如除以零或导致溢出的操作。
SIGILL
检测非法指令。
SIGINT
接收到交互注意信号。
SIGSEGV
非法访问内存。
SIGTERM
发送到程序的终止请求。
```

## 数据并发 -多线程

[多线程](http://www.cplusplus.com/reference/multithreading/)

```
#include <pthread.h>
pthread_create (thread, attr, start_routine, arg) 

#include <pthread.h>
pthread_exit (status) 


c++11有了标准的线程库
#include <thread>


```

## 网络数据

CGI

## [内存泄漏](https://blog.nelhage.com/post/three-kinds-of-leaks/)




## 源码

```
glibc
 http://ftp.gnu.org/gnu/glibc/
gcc-libstdc++
 http://ftp.gnu.org/gnu/gcc/


clang+llvm

 http://llvm.org/git/libc
 http://llvm.org/git/libcxx
 https://beijing.source.codeaurora.org/quic/la/platform/external/libcxx


```


[clang get_started](https://clang.llvm.org/get_started.html)
[llvm GettingStarted.html#an-example-using-the-llvm-tool-chain](http://llvm.org/docs/GettingStarted.html#an-example-using-the-llvm-tool-chain)


## Linux 内核

``` js

nm XXX.a //函数，全局变量
readelf -h XXX.a //OS，ABI，架构信息，类别（32/64位）等等

```
### 系统调用定义
声明：
linux\include\asm-arm\unistd.h
定义：
linux\arch\arm\kernel\calls.S
函数地址：
...

### 驱动函数注册