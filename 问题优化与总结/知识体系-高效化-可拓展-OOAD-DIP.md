

### 依赖倒置设计原则（DIP）
high-level and low-level layers through shared abstractions
A.高层次的模块不应该依赖于低层次的模块，他们都应该依赖于抽象。
B.抽象不应该依赖于具体，具体应该依赖于抽象。

实现：Model View Controller
工厂方法/简单工厂/DI
### 依赖倒置实现：控制反转（IOC）
[控制反转（IOC）](https://baike.baidu.com/item/%E6%8E%A7%E5%88%B6%E5%8F%8D%E8%BD%AC/1158025)
[](https://en.wikipedia.org/wiki/Inversion_of_control)
#### 控制反转实现：依赖注入（DI）
对象依赖注入方式： 接口，set方法,构造方法,Java注解/泛型参数注入

dagger,koin
#### 控制反转实现：“依赖查找”（Dependency Lookup）

#### 其他实现
模板方法
策略