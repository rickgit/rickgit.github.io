#ArrayMap分析

>  *ArrayMap* is a generic key->value mapping data structure that is designed to be more memory efficient than a traditional {@link java.util.HashMap}

@(源码分析)[ArrayMap|Android]

**ArrayMap**效率比HashMap高，将从以下几点分析

- **创建及初始化**
- **添加元素**
- **移除元素**
- **清理容器**

---------------------

[TOC]

##ArrayMap创建

ArrayMap包含三个构造函数，接下来只分析无参构造函数。
```
    public ArrayMap() {
        super();
    }

    /**
     * Create a new ArrayMap with a given initial capacity.
     */
    public ArrayMap(int capacity) {
        super(capacity);
    }

    /**
     * Create a new ArrayMap with the mappings from the given ArrayMap.
     */
    public ArrayMap(SimpleArrayMap map) {
        super(map);
    }
```
无参构造函数调用父类SimpleArrayMap的无参构造函数。

构造方法初始化字段 
- *mHashes*
- *mArray*
- *mSize*

