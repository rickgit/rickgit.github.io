
### 2.1 数据集合  - Collection 类（List, Queue, Map）
数学-集合论
```text
                                            +--------------+                                          +----------------+
                                            |              |                                          |                |
                                            |   Collection |                                          |     Map        |
                                            +------+-------+                                          +-------^--------+
                                                   ^                                                     +-----------------------+------------+
                           +---------------------------------------------+                               +                       +            |
                           |                       |                     |                             .....                  ......          |
                   +-------+-------+        +------+------+       ++-----+-----+                 +--+---------+-+         ++-----------+      |    +---------------+
                   |unsort,no repeat        | sortable ,repeatable +                 +              +         +            +                  |    |               |
                   |   Set         +        |   List      +       +    Queue   +                 + HashMap      +         +  SortMap   +      |    |  Dictionary   |
                   +------+--------+        +-----+-------+       +-------+----+                 +-------+------+         +-------+----+      |    +---------------+
                          ^                       ^                       ^                              ^                        ^           |
       +--------------->  +                       + <---------------------+                              |                        |           |            ^
    .......           .......                   ....                      |                              |                        |           |            |
+--------------+   +---------------+  +-----------+  +---------+    +-----+-----------+         +--------+--------+        +------+------+    |  . +-------+--------+
|              |   |               |  |           |  |         |    |                 |         |                 |        |             |    |  . |                |
|  TreeSet     |   |   HashSet     |  | ArrayList |  | Vector  |    |   LinkedList    |         |   LinkedHashMap |        |   TreeMap   |    +--.-+   HashTable    |
+--------------+   +---------------+  +-----------+  +---------+    +-----------------+         +-----------------+        +-------------+       . +----------------+



```
#### ArrayList
```java
            +----------------------------------------------------------------------------------+
            |           ArrayList:AbstractList     AbstractList                                |
            |              elementData:Object[]     modCount:int        DEFAULT_CAPACITY = 10; |
            |              size:int                                                            |
            |                                                                                  |
            |              add(e:E)                   remove(index:int )                       |
            |              add(index:int ,element:E)  remove(o:Object )                        |
            |              ensureCapacityInternal()                                            |
            |                                                                                  |
            |              grow()                                                              |
            |              hugeCapacity()//OutOfMemoryError()                                  |
            +----------------------------------------------------------------------------------+


```
初始容量 0
加载因子（0.0~1.0）  超过容量1.5倍或最初扩容DEFAULT_CAPACITY=10 
扩容增量
#### Vector

```java
            +----------------------------------------------------------------------------------+
            |                      VectorvEv:AbstractList                                      |
            |                                                                                  |
            |                          elementData:Object[]                                    |
            |                                                                                  |
            |                          elementCount: int                                       |
            |                                                                                  |
            |                          capacityIncrement:int //custom                          |
            |                                                                                  |
            +----------------------------------------------------------------------------------+

```

初始容量 10
加载因子（0.0~1.0）  超过容量1.0，执行扩容
扩容增量 增加一倍，或者自定义capacityIncrement。ArrayList比较省空间。 

与ArrayList区别是，所有方法都加Synchronized，性能没有ArrayList高
#### LinkedList
```java
            +----------------------------------------------------------------------------------+
            |           LinkedList:AbstractSequentialList        AbstractSequentialList:       |
            |               first:Node<E>                                  AbstractList        |
            |               last:Node<E>                            modCount :int              |
            |               size:int                                                           |
            |                                     Deque:Queue       Queue                      |
            |               add(e:E ):boolean       push(e:E )       offer(e:E)                |
            |               linkLast( e:E)          addFirst()       linkLast()                |
            |                                                                                  |
            |                                       pop()            poll()                    |
            |                                       removeFirst()    unlinkFirst()             |
            |                                       peek()                                     |
            +----------------------------------------------------------------------------------+ 
            |              Node                                                                |
            |                item: E                                                           |
            |                next:Node<E>                                                      |
            |                                                                                  |
            |                prev:Node<E>                                                      |
            |                                                                                  |
            +----------------------------------------------------------------------------------+

```



#### HashMap
JDK7中HashMap采用的是位桶+链表的方式，即我们常说的散列链表的方式，而JDK8中采用的是位桶+链表/红黑树
```
            +----------------------------------------------------------------------------------+
            |                      HashMap:AbstractMap                AbstractMap:Map          |
            |                      entrySet:Set<Map.Entry<K,V<>        keySet:Set<K>           |
            |      put(K,V )       table:Node<K,V>[]                   values:Collection<V>    |
            |                      loadFactor:float=0.75f                                      |
            |      resize()        modCount:int                                                |
            |                      size:int                                                    |
            |                      threshold:int                                               |//threshold 容量*加载因子
            |                                                                                  |
            +----------------------------------------------------------------------------------+
 
```
 MIN_TREEIFY_CAPACITY = 64
TREEIFY_THRESHOLD = 8;
UNTREEIFY_THRESHOLD = 6

初始容量  DEFAULT_INITIAL_CAPACITY = 1 << 4
加载因子（0.0~1.0）  0.75f
扩容增量（扩容hash表）  一倍
```

newCap = oldCap << 1
```

- 哈希碰撞
```
    static final int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);//位的亦或运算
    }
```
hash bucket 大小设置为 length=2^n。
2^n-1对应的二进制 1111...111，hash bucket的索引值是通过 hash & (tab.length - 1)，设置为2^n，减少哈希碰撞

- 链地址过长，（红黑树）树化
  TREEIFY_THRESHOLD = 8时，对哈希冲突链地址树化

```
哈希碰碰撞时的解决方法
1. 开放地址法（HashTable），包括 线性探测再散列，二次探测再散列，伪随机探测再散列
2. 链地址法（HashMap 用红黑树代替链表，加快搜索）
3. 再哈希法
4. 建立一个公共溢出区
```
[xorshift](http://csuncle.com/2018/08/03/梅森旋转安全性分析及改进/)

#### HashSet
```
            +----------------------------------------------------------------------------------+
            |                      HashSe: AbstractSet, Set                                    |
            |                               map :HashMap<E,Object>                             |
            |                                                                                  |
            |                                                                                  |
            |                                                                                  |
            |                                                                                  |
            |                                                                                  |
            |                                                                                  |
            +----------------------------------------------------------------------------------+

```
初始容量 （HashMap决定）16
加载因子（0.0~1.0）  0.75f
扩容增量  一倍
```

newCap = oldCap << 1
```
#### TreeMap
红黑树排序

初始容量 11
加载因子（0.0~1.0）  0.75f
扩容增量  没有数组结构，不需要扩容
```java
            +----------------------------------------------------------------------------------+
            |                      TreeMap:AbstractMap,NavigableMap                            |
            |                                                                                  |
            |  descendingMap:NavigableMap<K,V>                   AbstractMap                   |
            |   size:int                                            values :Collection<V>      |
            |   root:TreeMapEntry<K,V>                              keySet :Set<K>             |
            |     comparator:Comparator<? super K>                                             |
            |                                                                                  |
            |  entrySet:EntrySet                                                               |
            |  navigableKeySet: KeySet<K>                                                      |
            |   modCount:int                                                                   |
            |                                                                                  |
            +----------------------------------------------------------------------------------+
            |    TreeMapEntryvK,Vv : Map.EntryvK,Vv                                            |
            |         K key;                                                                   |
            |         V value;                                                                 |
            |         left:  TreeMapEntry                                                      |
            |         right: TreeMapEntry                                                      |
            |         parent:TreeMapEntry                                                      |
            |         color:  boolean= BLACK                                                   |
            +----------------------------------------------------------------------------------+

```
红黑树平衡调整:
叔父红色
    1. 变三色（父叔黑，祖红）
    2. 祖父节点继续转换
叔父黑色，
    1. 祖父子节点不是直线，做子父节点方向，父节点转换。父节点继续转换。
    2. 变祖父两色（父黑祖红），子祖节点方向，祖节点旋转。
    3. 当前节点继续转换。
```java
    private void fixAfterInsertion(TreeMapEntry<K,V> x) {
        x.color = RED;

        while (x != null && x != root && x.parent.color == RED) {
            if (parentOf(x) == leftOf(parentOf(parentOf(x)))) {
                TreeMapEntry<K,V> y = rightOf(parentOf(parentOf(x)));
                if (colorOf(y) == RED) {
                    setColor(parentOf(x), BLACK);
                    setColor(y, BLACK);
                    setColor(parentOf(parentOf(x)), RED);
                    x = parentOf(parentOf(x));
                } else {
                    if (x == rightOf(parentOf(x))) {
                        x = parentOf(x);
                        rotateLeft(x);
                    }
                    setColor(parentOf(x), BLACK);
                    setColor(parentOf(parentOf(x)), RED);
                    rotateRight(parentOf(parentOf(x)));
                }
            } else {
                TreeMapEntry<K,V> y = leftOf(parentOf(parentOf(x)));
                if (colorOf(y) == RED) {
                    setColor(parentOf(x), BLACK);
                    setColor(y, BLACK);
                    setColor(parentOf(parentOf(x)), RED);
                    x = parentOf(parentOf(x));
                } else {
                    if (x == leftOf(parentOf(x))) {
                        x = parentOf(x);
                        rotateRight(x);
                    }
                    setColor(parentOf(x), BLACK);
                    setColor(parentOf(parentOf(x)), RED);
                    rotateLeft(parentOf(parentOf(x)));
                }
            }
        }
        root.color = BLACK;
    }
```
二叉树搜索

Charset#availableCharsets():SortedMap

#### TreeSet
```



            +----------------------------------------------------------------------------------+
            |                      TreeSet: AbstractSet,NavigableSet                           |
            |                                                                                  |
            |                               m:NavigableMap<E,Object>                           |
            |                                                                                  |
            +----------------------------------------------------------------------------------+

```
初始容量 （HashMap决定）16
加载因子（0.0~1.0）  0.75f
扩容增量  一倍
#### HashTable
开放地址法解决Hash冲突

```
            +----------------------------------------------------------------------------------+
            |Hashtable:Dictionary,Map                                                          |
            |   put(K, V):V                count:int                table:HashtableEntry<?,?>[]|
            |    addEntry(hash,K,V,index)  entrySet:Set<Map.Entry>  threshold:int              |
            |   rehash()                   keySet:Set               values:Collection<V>       |
            |                              loadFactor:float                                    |
            |                              modCount:int                                        |
            |                                                                                  |
            |                                                                                  |
            +----------------------------------------------------------------------------------+


```
初始容量 11
加载因子（0.0~1.0）  0.75f
扩容增量  一倍+1，保证奇数，有可能是质数

```

int newCapacity = (oldCapacity << 1) + 1;
```
#### LinkedHashMap

LinkedHashMap节点类 LinkedHashMapEntry 包含 before, after;
HashMap节点类 Node 包好 next;
```
            +----------------------------------------------------------------------------------+
            |                      LinkedHashMap:HashMap, MapbleMap                            | 
            |                          head:LinkedHashMapEntry<K,V>                            | 
            |                          tail:LinkedHashMapEntry<K,V>                            |
            |                          accessOrder:boolean                                     |
            |                                                                                  |
            +----------------------------------------------------------------------------------+
            |                                                                                  |
            |                      LinkedHashMapEntry:HashMap.Node                             |
            |                                  before,after: LinkedHashMapEntry                |
            |                                                                                  |
            +----------------------------------------------------------------------------------+

```
初始容量（继承Hashmap）  16
加载因子（0.0~1.0）  0.75f
扩容增量（扩容hash表）  一倍
```

newCap = oldCap << 1
```







## TimSort

https://blog.csdn.net/bryansun/article/details/105182778?utm_medium=distribute.pc_relevant.none-task-blog-BlogCommendFromMachineLearnPai2-7.nonecase&depth_1-utm_source=distribute.pc_relevant.none-task-blog-BlogCommendFromMachineLearnPai2-7.nonecase