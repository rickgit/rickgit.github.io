# SparseMap分析

> SparseArrays map integers to Objects.  Unlike a normal array of Objects, there can be gaps in the indices.  It is intended to be more memory efficient than using a HashMap to map Integers to Objects, both because it avoids auto-boxing keys and its data structure doesn't rely on an extra entry object for each mapping.
> 采用[稀疏数组](http://hi.baidu.com/piaopiao_0423/item/d8cc2b99729f8380581461d1)

@(源码分析)[SparseMap|Android]

**SparseMap，将从以下几点分析

- **SparseMap创建及初始化**
- **存储键值对**
- **移除键值对**
- **清理容器**

---------------------

[TOC]

## SparseMap 创建及初始化
相关属性
```java
    private static final Object DELETED = new Object();
    private boolean mGarbage = false;//有对象被删除

    private int[] mKeys;  
    private Object[] mValues;
    private int mSize;//含有的数据
```

SparseArrays 包含个构造函数，接下来只分析无参构造函数。
```java
    public SparseArray() {
        this(10);
    }

    /**
     * Creates a new SparseArray containing no mappings that will not
     * require any additional memory allocation to store the specified
     * number of mappings.  If you supply an initial capacity of 0, the
     * sparse array will be initialized with a light-weight representation
     * not requiring any additional array allocations.
     */
    public SparseArray(int initialCapacity) {
        if (initialCapacity == 0) {
            mKeys = EmptyArray.INT;
            mValues = EmptyArray.OBJECT;
        } else {
            mValues = ArrayUtils.newUnpaddedObjectArray(initialCapacity);
            mKeys = new int[mValues.length];
        }
        mSize = 0;
    }
```
默认开辟 10 单位的内存空间 

##存储键值对
```java
    public void put(int key, E value) {
        int i = ContainerHelpers.binarySearch(mKeys, mSize, key);

        if (i >= 0) {
            mValues[i] = value;
        } else {
            i = ~i;

            if (i < mSize && mValues[i] == DELETED) {
                mKeys[i] = key;
                mValues[i] = value;
                return;
            }

            if (mGarbage && mSize >= mKeys.length) {
                gc();

                // Search again because indices may have changed.
                i = ~ContainerHelpers.binarySearch(mKeys, mSize, key);
            }

            mKeys = GrowingArrayUtils.insert(mKeys, mSize, i, key);
            mValues = GrowingArrayUtils.insert(mValues, mSize, i, value);
            mSize++;
        }
    }
```
1. 二分查找 mkeys，判断是否有存在 key,有则设置，没有走下一步
2. 判读最后查找的是否是已经标记为*DELETED*,若是则覆盖，否走下一步
3. 若 mGarbage 为真，且大小已经超过mKeys的大小，则运行 gc 方法, gc 方法将没有标记为*DELETED*覆盖，数组前面有标记*DELETED*，重新查找mkey
4. 设置新的*mKeys*,*mValues*,*mSize*
```java
    private void gc() {
        // Log.e("SparseArray", "gc start with " + mSize);

        int n = mSize;
        int o = 0;
        int[] keys = mKeys;
        Object[] values = mValues;

        for (int i = 0; i < n; i++) {
            Object val = values[i];

            if (val != DELETED) {
                if (i != o) {
                    keys[o] = keys[i];
                    values[o] = val;
                    values[i] = null;
                }

                o++;
            }
        }

        mGarbage = false;
        mSize = o;

        // Log.e("SparseArray", "gc end with " + mSize);
    }
```

## 移除键值对
remove开始分析
```java
    public void remove(int key) {
        delete(key);
    }
    public void delete(int key) {
        int i = ContainerHelpers.binarySearch(mKeys, mSize, key);

        if (i >= 0) {
            if (mValues[i] != DELETED) {
                mValues[i] = DELETED;
                mGarbage = true;
            }
        }
    }
```

## 清理容器
- clear 清除mArray数据
```java
    public void clear() {
        int n = mSize;
        Object[] values = mValues;

        for (int i = 0; i < n; i++) {
            values[i] = null;
        }

        mSize = 0;
        mGarbage = false;
    }

``` 
