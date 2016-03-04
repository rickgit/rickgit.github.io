#LruCache分析

> A cache that holds strong references to a limited number of values. Each time a value is accessed, it is moved to the head of a queue. When a value is added to a full cache, the value at the end of that queue is evicted and may become eligible for garbage collection.

@(源码分析)[LruCache|Android]

**LruCache，将从以下几点分析

- **LruCache创建及初始化**
- **存储键值对**
- **移除键值对**
- **清理容器**

---------------------

[TOC]

##LruCache创建及初始化
相关属性
```java
    private final LinkedHashMap<K, V> map;//LinkedHashMap比HashMap多了个指向前一个和后一个元素的指针

    /** Size of this cache in units. Not necessarily the number of elements. */
    private int size; //手动计算对象的大小，如bitmap width*height*4
    private int maxSize;//构造方法设置的

    private int putCount;
    private int createCount;
    private int evictionCount;
    private int hitCount;
    private int missCount;

```

LruCache包含个构造函数，接下来只分析构造函数。
```java
    public LruCache(int maxSize) {
        if (maxSize <= 0) {
            throw new IllegalArgumentException("maxSize <= 0");
        }
        this.maxSize = maxSize;
        this.map = new LinkedHashMap<K, V>(0, 0.75f, true);
    }
```
维护的是一个LinkedHashMap, 

##存储键值对
```java
    public final V put(K key, V value) {
        if (key == null || value == null) {
            throw new NullPointerException("key == null || value == null");
        }

        V previous;
        synchronized (this) {
            putCount++;
            size += safeSizeOf(key, value);
            previous = map.put(key, value);
            if (previous != null) {
                size -= safeSizeOf(key, previous);
            }
        }

        if (previous != null) {
            entryRemoved(false, key, previous, value);
        }

        trimToSize(maxSize);
        return previous;
    }
```
- safeSizeOf方法是判断当前对象的size必须大于0，否则抛异常
- entryRemoved模板方法，需要重写，做该对象相应的处理
- trimToSize方法移除不常使用的对象，

##移除键值对
remove开始分析
```java
    public final V remove(K key) {
        if (key == null) {
            throw new NullPointerException("key == null");
        }

        V previous;
        synchronized (this) {
            previous = map.remove(key);
            if (previous != null) {
                size -= safeSizeOf(key, previous);
            }
        }

        if (previous != null) {
            entryRemoved(false, key, previous, null);
        }

        return previous;
    }
```

##清理容器
- evictAll 清除map数据
```java
    public final void evictAll() {
        trimToSize(-1); // -1 will evict 0-sized elements
    }

``` 