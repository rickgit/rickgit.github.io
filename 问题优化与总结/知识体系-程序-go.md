## 
 A token is either a keyword, an identifier, a constant, a string literal, or a symbol

- keyword
 ```
+---------------------------------------------+
|  defer                                      |
+---------------------------------------------+
|  chan       Go                              |
+---------------------------------------------+
|  import     package     func                |
|                                             |
|  Type   Struct  interface   map             |
+---------------------------------------------+
|  if  else                                   |
|                                             |
|  Switch select case default fallthrough     |
|                                             |
|  for   range                                |
|                                             |
|  break continue  return    Goto             |
+---------------------------------------------+
|  Var   const                                |
+---------------------------------------------+

```

标识符(identifier)：大小写敏感，不允许 **@, $, %**字符

分隔符：
1. 注释
2. 行分隔符（**;**）
3. 空格（blanks,tabs,newline chars,comments）

```go
                       +---- true
                       |
++-+   Boolean types +-+
 |                     |
 |                     +---- false
 |
 |
 |                     +---- integer types : uint8/uint16/uint32/uint64/int8/int16/int32/int64
 +-+   Numeric types   |                     
 |                  +--+
 |                     |
 |                     +---- floating point : float32/float64/complex64/complex128
 |                     |
 |                     +---- other :          byte/rune/uint/int/  uintptr 
 |
 +--+  String types
 |                     +---- Pointer types
 |                     |
 |                     +---- Array types
 |                     |
 +-+   Derived types   +---- Structure types
                    +--+
                       +---+ Union types
                       |
                       +---- Function types
                       |
                       +---- Slice types
                       |
                       +---- Interface types
                       |
                       +---- Map types
                       |
                       +---- Channel Types

```

字面量（literals）：integer constant, a floating constant, a character constant(Escape Sequence), or a string literal( "")

```
和C语言一致

+-------------+--------------------------------------------------------------+
|  C          |  (? :)                                                       |
|  Misc       |  sizeof                                                      |
|             |  *(pointer operator)                                         |
|             |  & (return address)                                          |
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
|  Bitwise    |  & | ^  ~    < <   > >                                       |
|             |                                                              |
+----------------------------------------------------------------------------+
|             |                                                              |
|  Relational |  == != > < >= <=                                             |
|             |                                                              |
+----------------------------------------------------------------------------+
|             |                                                              |
|  Arithmetic |  + - * / % ++ --                                             |
|             |                                                              |
+-------------+--------------------------------------------------------------+

```
##   数据类型 -函数

```
    Package Declaration
    Import Packages
    Functions
    Variables
    Statements and Expressions
    Comments
```

```
func function_name( [parameter list] ) [return_types]
{
   body of the function
}
```

 
## 数据并发
 
- goroutine **go**

- channel **chan**
  CSP模式

-  **select**
通过select可以监听channel上的数据流动