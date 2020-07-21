## SparseArray 
[SparseArray与HashMap<Integer,Object>对比](https://stackoverflow.com/questions/25560629/sparsearray-vs-hashmap)
[SparseArray与ArrayMap对比，解决基本数据类型自动封箱问题](https://android.jlelse.eu/app-optimization-with-arraymap-sparsearray-in-android-c0b7de22541a)
  稀疏数组问题就是数组中的大部分的内容值都未被使用或者都为0，在数组中仅有少部分的空间使用。
```java
public class SparseArray<E> implements Cloneable {
    private boolean mGarbage = false;// 1byte
    private int[] mKeys;//4 byte
    private Object[] mValues;//4byte
    private int mSize;//4byte
}

```
初始容量  mKeys，mValues 返回是长度为12
加载因子（0.0~1.0）  1f
扩容增量 //TODO

查找 二分法（key 有已排序）
插入 key为Integer，排序。找到key，替换掉原来的值。没找到，根据二分法返回的位置，插入有序key
删除 数组设置为常量 DELETED（new Object()）每次 **remove** 操作都会**mGarbage**设置为**true**
gc  标记为DELETED，key,Value替换为有值的数据。



## ArrayMap 
相比HashMap链表存储，使用了两个小的容量的数组
```java
public final class ArrayMap<K, V> implements Map<K, V> {
    final boolean mIdentityHashCode;// 1byte，default false
    int[] mHashes;//4 byte，存储key的hash值
    Object[] mArray;//4byte,size 为mHashes的两倍，存储key（index=hashIndex<<1），value
    int mSize;//4 byte
    MapCollections<K, V> mCollections;//4byte

}

                        +------+            +------+
                        | Key  +----------> | Key1 |
                        | Hash |            |      |
                        +------+            |Value1|
                        |      |            +------+
                        |      |            |      |
                        |      |            |      |
+---------+             |      |            |      |
| Key     |  Binary     |      |            |      |
|         | +------->   |      |            |      |
+---------+  Search     |      |            |      |
                        |      |            |      |
                        |      |            |      |
                        |      |            |      |
                        |      |            |      |
                        |      |            |      |
                        |      |            |      |
                        |      |            |      |
                        |      |            |      |
                        +------+            +------+



```
初始容量  mKeys，mValues 0
加载因子（0.0~1.0）  mSize>=mHashes长度
扩容增量（扩容hash表）大于等于8,扩容原来的一半
```java
final int osize = mSize;
int n=osize >= (4*2) ? (osize+(osize>>1)): (osize >= 4 ? (4*2) : 4)
```

查找 二分法mHashes表，
插入 从mHashes二分法找key的hash，mHashes向后向前查找key，执行替换。没找到hash或key，System.arraycopy执行插入key,value（先判断扩容）
删除  左移动mhash,mValue，根据情况调整新的大小后，填掉删除的位置
```java
删除后调整大小
final int osize = mSize;
if (mHashes.length > (BASE_SIZE*2) && mSize < mHashes.length/3) {
    final int n = osize > (BASE_SIZE*2) ? (osize + (osize>>1)) : (BASE_SIZE*2);
}
```
