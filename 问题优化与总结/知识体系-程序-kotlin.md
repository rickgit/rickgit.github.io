

## 
 [kotlin文档地址](http://kotlinlang.org/docs/reference/basic-types.html)



##   数据类型
kotlin 静态强类型的显式类型语言
- 字面量 

- 变量（var）与常量（val）
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
  1. class
  2. get,set
  3. interface 
  4. private, protected, internal ,public
  5. 扩展函数
  6. data class
  7. sealed class
  8. 泛型
  9. 内部类（inner ），super@Outer，this@label
  10. enum class 
  11. 对象表达式与对象声明 object
  12. Inline Class（类内联化）
  13. 函数，inline fun，lambda
  14. 类型别名
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

## 异常，日志
## 数据集合
Collections: List, Set, Map


## 数据并发
\