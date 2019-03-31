## 
 

```

+------------------+-------------------------------------------------------------------------------------------+
|  security        |                                                                                           |
+--------------------------------------------------------------------------------------------------------------+
|  perfomance      | memory(leaks,chunk)                                                                       |
+--------------------------------------------------------------------------------------------------------------+
|  test            |                                                                                           |
+--------------------------------------------------------------------------------------------------------------+
|  API /SDK        |       lang,util,io,net,text,math,sql,security,awt/swing,xml,time                          |
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
|                             conditional/decision-making/loops                                                |
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

## 字面量
### [ADT](https://en.wikipedia.org/wiki/Abstract_data_type)
```
Container    Map(ES6+)    Stack               Double-ended priority queue
List         Multimap     Queue
Set(ES6+)    Graph        Priority queue
Multiset     Tree         Double-ended queue

```


## 关键字与符号


```js
 (59个)
封装，抽象，关联，继承，多态
+------------------------------------------------------------+
|                                                            |
|  	try catch   debugger  finally                        |
|  	throw                                                |
|   throws(-)                                                |
+------------------------------------------------------------+
|   package    import(es5+) 	  await*   yield(es6+)       |
|   private    export(es6+)    synchronized(-)    native(-)  |
|   protected 	 	       transient(-)                  |
|   public	               volatile(-)        final(-)   |
+------------------------------------------------------------+
|   instanceof	typeof      	      	                     |
|   implements  extends(es5+)   super(es5+)  function        |
|   class(es5+) enum (es5+)     interface     abstract(-)    |
+------------------------------------------------------------+
|    var  void  let(es6+) new  this     const static  delete |
+------------------------------------------------------------+
|    if	 else  switch 	case	default  with  	             |
|    do	 while for  in	                                     |
|    continue  break return goto(-)	 	             |
+------------------------------------------------------------+
|    byte(-) boolean(-) char(-) short(-)  	             | 
|    int(-) float(-)  long(-)  double(-) 		     |
+------------------------------------------------------------+
|    null	   true      false                           |
+------------------------------------------------------------+

*/+ 新增
-   es5标准以上去掉

```
 **variable** names are case-sensitive

``` Operators
+-------------+--------------------------------------------------------------+
|             |                                                              |
|  Misc       |  ? : (Conditional )  , typeof                                |
|             |                                                              |
+----------------------------------------------------------------------------+
|             |                                                              |
|  Assignment |  = += -= *= /= %=                                            |
+----------------------------------------------------------------------------+
|             |                                                              |
|  Logical    |  &&  || !                                                    |
|             |                                                              |
+----------------------------------------------------------------------------+
|             |                                                              |
|  Bitwise    |  & | ^  ~    < <   > >    > > >                              |
|             |                                                              |
+----------------------------------------------------------------------------+
|             |                                                              |
|  Relational |  == != > < >= <=                                             |
+----------------------------------------------------------------------------+
|             |                                                              |
|  Arithmetic |  + - * / %                                                   |
|             |  ++ --                                                       |
+-------------+--------------------------------------------------------------+


```
##  数据类型
 

``` 
                                                          + Undefined
                                                          | Null,
                               ++  primitive data types +-+ Boolean
                               |                          | Number
        +--+ Native Objects+---+                          + String
        |                      |
        |                      +-----------------+-------+  Array
        |                                        |
        |                                        |          Date
        |                                        |
Object  +---+Host Objects                        |          Math
        |                                        |
        |                                        |          RegExp
        |                                        |
        |                                        +-------+  Function
        |
        +--+ user+defined objects



```
JavaScript built-in objects, properties, and methods
```
Object     Array    	Date	    Math	    
undefined Number  String
hasOwnProperty	name length toString valueOf
function eval
Infinity	isFinite 	isNaN NaN
isPrototypeOf	  prototype 	   
```

Java Reserved Words

```
getClass	java	JavaArray	javaClass
JavaObject	JavaPackage
```

Other Reserved Words(HTML and Window objects and properties)

## 数据类型 - 面向对象
## 数据-正则
```Regex Quantifiers
                                                                   {n,}
                                                            +------------------------------------------>
            *
                                                                   {n,m}
+------------------------------------------------->         +------------------->

      ?                    +

<----------- ------------------------------------->        {n}

|           |                                               |                   |
+-----------+-----------------------------------------------+-------------------+------------------------>

0           1                                               n                   m

```

```Regex
+---------------+----------------------------------------------------------------------------------------------------------------------+
|               |  exec() Returns the first match                                                                                      |
|  methods      |  test() Returns true or false                                                                                        |
+---------------+----------------------------------------------------------------------------------------------------------------------+
|  flags        |  /i(CASE_INSENSITIVE)    /m(MULTILINE)        /s(DOTALL)                                                             |
|               |  UNICODE_CASE  CANON_EQ  UNIX_LINES  LITERAL  UNICODE_CHARACTER_CLASS  COMMENTS                                      |
+---------------+---------------------------------------------+------------------------------------------------------------------------+
|  Quotation    |   \             \Q...\E                                                                                               |转义
+--------------------------------------------------------------------------------------------------------------------------------------+
| Back          |  \n                                  \k<name>                                                                        |
| references    |  nth capturing group matched         named-capturing group "name" matched                                            |
+-------------------------------------------------------------+------------------------------------------------------------------------+
|               |                                             |              |     (?<name>X) a named-capturing group                  |
|               |                                             |              |     (?:X) a non-capturing group                         |
|               |                                             |              |     (?>X) an independent, non-capturing group           |
|               |                                             |              |                                                         |
|               |                                             |              |     (?idmsuxU-idmsuxU)                                  |
|               |                                             |              |     turns match flags i d m s u x U on - off            |
|               |                                             |              |     (?idmsux-idmsux:X)                                  |
|               |                                             | Special      |     non-capturing group                                 |
|               |                                             |              |     with the given flags i d m s u x on - off           |
|               |                                             | Constructs   |                                                         |
|               |                                             |              +---------------------------------------------------------+
|               |                                             |              |     (?=X)   via zero-width positive look ahead          |
|               |                                             |              |     (?!X)   via zero-width negative lookahead           |
|               |                                             |              |     (?<=X)  via zero-width positive lookbehind          |
|               |                                             |              |     (?<!X)  via zero-width negative lookbehind          |
|               |                                             |              |     (?>X)   as an independent, non-capturing group      |
|               +                                             +------------------------------------------------------------------------+
|  Logical      | XY                          X|Y             |  (X)                                                                   |
|  Operators    | X followed by Y             Either X or Y   |  group                                                                 |
+--------------------------------------------------------------------------------------------------------------------------------------+
+--------------------------------------------------------------------------------------------------------------------------------------+
|               |  X?   X, once or not at all     X{n}     X, exactly n times       |                     |                            |
|  Quantifiers  |  X*   X, zero or more times     X{n,}    X, at least n times      |                     |                            | 
|               |  X+   X,mone or more times      X{n,m}   X, at least n            |       ?             |                 +          |
|               |                                         but not more than m times |                     |                            |
|               +----------------------------------------------------------------------------------------------------------------------+
|               |        /g                                                         |     Reluctant       |              Possessive    |
|               |      Greedy (matches entire input,then backtrack)                 | (matches as little) | (Greedy, doesn't backtrack)|
+--------------------------------------------------------------------------------------------------------------------------------------+
| Boundary      | ^ The beginning of a line          \b   A word boundary         \A   The beginning of the input                      |
| Matchers      | $  The end of a line               \B   A non-word boundary     \z   The end of the input                            |
|               | \G  The end of the previous match                     \Z  The end of the input but for the final terminator, if any  |
+---------------+-------------------------------------------------------+-------------------------------+------------------------------+
|               | \p{Lower}              \p{Upper}                      |   \p{javaLowerCase}           | \p{IsLatin}                  |
|               | char:[a+z].            char:[A+Z]                     |   Character.isLowerCase()     | A Latin script char          |
|               |                                                       |                               |                              |
|               |                                                       |                               | \p{InGreek}                  |
|               | \p{ASCII}               \p{Alpha}                     |    \p{javaUpperCase}          | A char in the Greek block    |
|               | All ASCII:[\x00+\x7F]   char:[\p{Lower}\p{Upper}]     |    Character.isUpperCase()    |                              |
|               |                                                       |                               | \p{Lu}                       |
|               | \p{Digit}               \p{Alnum}                     |                               | An uppercase letter          |
|               | [0+9]                   [\p{Alpha}\p{Digit}]          |                               |                              |
|  char classes |                                                       |    \p{javaWhitespace}         |  \p{IsAlphabetic}            |
|               | \p{Punct}                                             |    Character.isWhitespace()   |  An alphabetic char          |
|               | Punctuation:                                          |                               |                              |
|  in java      | One of !"#$%&'()*+,+./:;^=^?@[\]^_^{+}^               |                               |  \p{Sc}                      |
|               |                                                       |    \p{javaMirrored}           |  A currency symbol           |
|               | \p{Graph}               \p{Print}                     |    Character.isMirrored()     |  (binary property)           |
|               | A ^isible char:         A printable char:             |                               |                              |
|               | [\p{Alnum}\p{Punct}]    [\p{Graph}\x20].              |                               |                              |
|               |                                                       |                               |  \P{InGreek}                 |
|               |                                                       |                               |  except one                  |
|               |  \p{Blank}                                            |                               |  in the Greek block          |
|               |  A space or a tab:                                    |                               |                              |
|               |  [ \t]                                                |                               |  [\p{L}&&[^\p{Lu}]]          |
|               |                                                       |                               |  except an uppercase letter  |
|               |   \p{XDigit}             \p{Space}                    |                               |                              |
|               |   A hexadecimal digit:   A whitespace char:           |                               |                              |
|               |   [0+9a+fA+F]            [ \t\n\x0B\f\r]              |                               |                              |
|               +-------------------------------------------------------+-------------------------------+------------------------------+
|               |       POSIX                                           |              JAVA             |              Unicode         |
+--------------------------------------------------------------------------------------------------------------------------------------+
|               |     .         \d              \D            \s                 \S                     \w            \W               |
| Metacharacter |     Any char  A digit: [0-9]  A non-digit:  A whitespace char: A non-whitespace char: A word char:  A non-word char: |
|               |                               [^0-9]        [ \t\n\x0B\f\r]    [^\s].                 [a-zA-Z_0-9]. [^\w]            |
+--------------------------------------------------------------------------------------------------------------------------------------+
|  Grouping     |                                                                                                                      |
|               |     [abc]           [^abc]       [a-zA-Z]      [a-d[m-p]]      [a-z&&[def]]    [a-z&&[^bc]]    [a-z&&[^m-p]]         |
|  char classes |     a, b, or c      (negation)   (range)       (union)         (intersection)  (subtraction)   (subtraction)         |
|               |                                                                                                                      |
+--------------------------------------------------------------------------------------------------------------------------------------+
|               |     x             \\                                                                   \xxx                          |
|               |     char x        backslash char                                                      char with octal                |
|               |                                                                                       xmnn (0 ≤ m ≤ 3, 0 ≤ n ≤ 7)    |
|               |                                                                                                                      |
|               |     \xhh                              \uhhhh                                                                         |
| matching char |     char with hexadecimal             char with hexadecimal                                                          |
|               |     0xhh                              0xhhhh                                                                         |
(Literal escape)|                                                                                                                      |
|               |     \t           \n                   \r                      \f                      \v                             |
|               |     tab char     newline char         carriage-return char    form-feed char          vertical tab character         |
|               |     ('\u0009')   ('\u000A')           ('\u000D')              ('\u000C')                                             |
+---------------+----------------------------------------------------------------------------------------------------------------------+


```

## 数据并发 