#ArrayMap分析

>  *ArrayMap* is a generic key->value mapping data structure that is designed to be more memory efficient than a traditional {@link java.util.HashMap}

@(源码分析)[ArrayMap|Android]

**ArrayMap**效率比HashMap高，将从以下几点分析

- **ArrayMap创建及初始化**
- **存储键值对**
- **移除键值对**
- **清理容器**
**ArraySet**实现和*ArrayMap*一样，只不过少了key的存储。**HashSet**Entry是存储的是value的hashcode和HashSet的实例，所以会占用额外空间

---------------------

[TOC]

##ArrayMap创建及初始化

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

构造方法初始化字段 ，mHashes和mArray组成堆，所以索引快了
- *mHashes* 存储key的hash值，默认是ContainerHelpers.EMPTY_INTS，空的int数组
- *mArray*  存储对象key和value的值，value的索引是key的加1，默认是ContainerHelpers.EMPTY_OBJECTS，空的数组
- *mSize* 设置为0,改值是mHashes长度的大小

可以看出无参构造方法，默认不预先分配内存。

但传入一个大于0的*int*类型的capacity参数时候，执行android.support.v4.util.SimpleArrayMap#allocArrays <br/>
需要注意下面几个字段,都是static修饰的全局变量
```
    static Object[] mBaseCache;长度为2, index为0时候是缓存hash值，1是缓存key和value
    static int mBaseCacheSize;
    static Object[] mTwiceBaseCache;长度为2, index为0时候是缓存hash值，1是缓存key和value
    static int mTwiceBaseCacheSize;
```
allocArrays根据要分配的大小，分3种情况处理
- size为BASE_SIZE*2，即8时；
```
        if (size == (BASE_SIZE*2)) {
            synchronized (ArrayMap.class) {
                if (mTwiceBaseCache != null) {
                    final Object[] array = mTwiceBaseCache;
                    mArray = array;
                    mTwiceBaseCache = (Object[])array[0];
                    mHashes = (int[])array[1];
                    array[0] = array[1] = null;
                    mTwiceBaseCacheSize--;
                    if (DEBUG) Log.d(TAG, "Retrieving 2x cache " + mHashes
                            + " now have " + mTwiceBaseCacheSize + " entries");
                    return;
                }
            }
        }
```
分别对*mArray*，*mHashes*,*mTwiceBaseCache*，*mTwiceBaseCacheSize*重新赋值

- size为BASE_SIZE,及4时，只是上面情况进行mBaseCache和mBaseCacheSize
```
		if (size == BASE_SIZE) {
            synchronized (ArrayMap.class) {
                if (mBaseCache != null) {
                    final Object[] array = mBaseCache;
                    mArray = array;
                    mBaseCache = (Object[])array[0];
                    mHashes = (int[])array[1];
                    array[0] = array[1] = null;
                    mBaseCacheSize--;
                    if (DEBUG) Log.d(TAG, "Retrieving 1x cache " + mHashes
                            + " now have " + mBaseCacheSize + " entries");
                    return;
                }
            }
        }
```

- size为其他情况，直接创建数组
```
        mHashes = new int[size];
        mArray = new Object[size<<1];
```
上面这段代码，可以知道mArray是mHashes数组长度的一倍。

##存储键值对
android.support.v4.util.SimpleArrayMap#put调用indexOfNull获取一个没用过的index(key的hash一样，mArray数组里面key的值一样，
根据hash值的一倍寻找存储的值，解决Hash冲突的和Hashmap一样。
- 找到index,返回就的值。
- 找不到index，重新分配mHashes的大小，下面的代码可以看出分配的规则
```
            final int n = mSize >= (BASE_SIZE*2) ? (mSize+(mSize>>1))
                    : (mSize >= BASE_SIZE ? (BASE_SIZE*2) : BASE_SIZE);
```
重新分配内存，请看**ArrayMap创建及初始化**的AllocArrays方法
```
 freeArrays(ohashes, oarray, mSize);
```
重新分配内存是用新的数组，所以就的mHashes，mArray数组需要释放

##移除键值对
从android.util.ArrayMap#remove开始分析
```
    public V remove(Object key) {
        final int index = indexOfKey(key);
        if (index >= 0) {
            return removeAt(index);
        }

        return null;
    }
```
indexOfKey方法，调用indexOfNull方法和indexOf上文已经提及，就不累述
```
    public int indexOfKey(Object key) {
        return key == null ? indexOfNull() : indexOf(key, key.hashCode());
    }
```
##清理容器
- clear 清除mArray数据，并缓存mBaseCache或mTwiceBaseCache
```
    public void clear() {
        if (mSize > 0) {
            freeArrays(mHashes, mArray, mSize);
            mHashes = EmptyArray.INT;
            mArray = EmptyArray.OBJECT;
            mSize = 0;
        }
    }

```
- erase 清除mArray数据，没缓存
```
    public void erase() {
        if (mSize > 0) {
            final int N = mSize<<1;
            final Object[] array = mArray;
            for (int i=0; i<N; i++) {
                array[i] = null;
            }
            mSize = 0;
        }
    }
```