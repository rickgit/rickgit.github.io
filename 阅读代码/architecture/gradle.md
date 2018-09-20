
>Apache Groovy is an object-oriented programming language（面向对象语言） for the Java platform. It is a dynamic language（动态语言） with features similar to those of Python, Ruby, Perl, and Smalltalk. It can be used as a scripting language（脚本语言） for the Java Platform, is dynamically compiled to Java Virtual Machine (JVM) bytecode, and interoperates with other Java code and libraries [wiki](https://en.wikipedia.org/wiki/Groovy_(programming_language))

>Gradle is an open source build automation system that builds upon the concepts of Apache Ant and Apache Maven and introduces a Groovy-based domain-specific language (DSL) instead of the XML form used by Apache Maven of declaring the project configuration.[2] Gradle uses a directed acyclic graph ("DAG") to determine the order in which tasks can be run.[wiki](https://en.wikipedia.org/wiki/Gradle)

> [hello](http://blog.csdn.net/innost/article/details/48228651)

## gradle

## groovy特性
###	GroovyBeans / Properties

```groovy

	class AGroovyBean {
	  String color
	}

	def myGroovyBean = new AGroovyBean()

	myGroovyBean.setColor('baby blue')
	assert myGroovyBean.getColor() == 'baby blue'

	myGroovyBean.color = 'pewter'
	assert myGroovyBean.color == 'pewter'

```

###	Prototype extension

```groovy

	Number.metaClass {
	  sqrt = { Math.sqrt(delegate) }
	}

	assert 9.sqrt() == 3
	assert 4.sqrt() == 2

```

###	Dot and parentheses
```groovy
	take(coffee).with(sugar, milk).and(liquor)
	can be written as

	take coffee with sugar, milk and liquor
```

###	Functional programming
####	Closures
####	Curry
curry 过的闭包使得实现计算的函数模式出奇得容易
###	XML and JSON processing
> [processing-xml](http://groovy-lang.org/processing-xml.html)
### 1. Parsing XML
### 2. GPath
### 3. Creating XML
### 4. Manipulating XML


1. JsonSlurper
```
def jsonSlurper = new JsonSlurper()
def object = jsonSlurper.parseText('{ "name": "John Doe" } /* some comment */')

```
2. JsonOutput
```
def json = JsonOutput.toJson([name: 'John Doe', age: 42])

assert json == '{"name":"John Doe","age":42}'
```
###	String interpolation
###	AST (Abstract Syntax Tree) Transformation

- Singleton transformation;
- Category and Mixin transformation;
- Immutable AST Macro;
- Newify transformation;



###	Traits
### Groovy and HTTP
- httpbuilder
httpBuilder需要添加依赖库
[httpbuilder jar and dependence](https://mvnrepository.com/artifact/org.codehaus.groovy.modules.http-builder/http-builder/0.7.1)


```
//保存文本
String html = 'http://localhost:8081/groovy-http'.toURL().text


//下载
def url = 'http://www.google.com/images/logo.gif'
def file = new File('c://google_logo.gif').newOutputStream()
file << new URL(url).openStream()
file.close()
[代码来源](http://groovy-almanac.org/save-url-to-file/)
```



###


## gradle
google 搜索[android gradle dsl](http://google.github.io/android-gradle-dsl/current/)

## task
**C:/Users/Haibin/.gradle/wrapper/dists/gradle-2.10-all/a4w5fzrkeut1ox71xslb49gst/gradle-2.10/src/core/org/gradle/api/Task.java**
第一个脚本
```gradle 


```