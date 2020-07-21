

### 字符串
stringbuilder 
```
+----------------------------------------------------------------------------+
|                 StringBuilder                StringBuffer                  |
|                                                                            |
+----------------------------------------------------------------------------+
|                                                                            |
|                      AbstractStringBuilder                                 |
|                            value:char[]                                    |
|                            count:int                                       |
|                                                                            |
+----------------------------------------------------------------------------+


```
初始 默认16
扩容 插入时，期望数组长度大于value数组长度，(value.length << 1) + 2，如果不够直接用期望数组长度。[(value.length << 1) + 2](https://stackoverflow.com/questions/45094521/java-stringbuilderstringbuffers-ensurecapacity-why-is-it-doubled-and-incre)
    （保证初始化长度为0，也能扩容。并且可能JVM内存结构有关，不考虑markword和clazz，数组长度 4byte+（16*2+2)*2byte(char) 有概率能被8整除，内存对齐）
Arrays.copyof 扩容空间，拷贝源数组
System.arraycopy 拷贝到新数据

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

 
### 字符串
```
+---------------+----------+----------+
|               |  final   |  synchro |
+-------------------------------------+
| String        |   √      |          |
+-------------------------------------+
| StringBuffer  | char[]   |    √     |
+-------------------------------------+
| StringBuilder | char[]   |    x     |
+---------------+----------+----------+

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
加载因子 首次扩容DEFAULT_CAPACITY=10，超过容量1.5倍//适用非线程安全，效率高，

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

初始容量 默认10，超过容量，执行扩容
扩容增量 增加一倍，或者自定义capacityIncrement。ArrayList比较省空间。 //同步方法，效率慢，需要扩容大，保证并发时，不用频繁扩容

与ArrayList区别是，所有方法都加Synchronized，性能没有ArrayList高
#### LinkedList
```java
            +----------------------------------------------------------------------------------+
            |           LinkedList:AbstractSequentialList                                      |
            |               last:Node<E>         first:Node<E>    size:int                     |
            |                                                                                  |
            |               add(e:E ):boolean                                                  |
            |               linkLast( e:E)                                                     |
            +----------------------------------------------------------------------------------+
            |               Queue                Deque:Queue      AbstractSequentialList:      |
            |                offer(e:E)            push(e:E )               AbstractList       |
            |                linkLast()            addFirst()          modCount :int           |
            |                                                                                  |
            |                poll()                pop()                                       |
            |                unlinkFirst()         removeFirst()                               |
            |                                      peek()                                      |
            +----------------------------------------------------------------------------------+
            |              Node                                                                |
            |                item: E                                                           |
            |                next:Node^E^                                                      |
            |                pre^:Node<E>                                                      |
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
            |           treeifyBin()                                                           |
            +----------------------------------------------------------------------------------+
            |         TreeNode: LinkedHashMap.LinkedHashMapEntry:.Node         .Node:Map.Entry |
            |            parent;TreeNode       before, after                           hash    |
            |            left;  TreeNode                                               key     |
            |            right; TreeNode                                               value   |
            |            prev;  TreeNode                                               next    |
            |            red;   boolean                                                        |
            |                                                                                  |
            |            balanceDeletion()               removeTreeNode()                      |
            |                                            untreeify()                           |
            |                                            split()                               |
            +----------------------------------------------------------------------------------+
    static class Node<K,V> implements Map.Entry<K,V> {
        final int hash;
        final K key;
        V value;
        Node<K,V> next;
```
MIN_TREEIFY_CAPACITY = 64
TREEIFY_THRESHOLD = 8;
UNTREEIFY_THRESHOLD = 6

**tab扩容**
初始容量 table默认0，默认加载因子0.75（泊松分布有关）//tab长度不是质数或奇数，hash在tab上的分布不确定是否分散，需要用泊松分布来保证分散分布到tab位置
扩容增量（扩容hash表）  
    put 首次16（如果oldThr>0，优先取oldThr），Map阈值为table乘上加载因子；//2的幂次方，Boolean的hash 1101(2) 11
    putAll 首次设置 expect/0.75+1，阈值是最接近容量的2次幂
    Map阈值和 table扩容2倍（减少扩容后重新hash）//2的幂次方，容易位运算取tabindex=i = (n - 1) & hash；位运算代替取模效率高
红黑树链表化
    扩容，拆分红黑树，节点小于等于6
    remove，当前红黑树满足一定条件 (root.right == null  || root.left == null,|| root.left.left == null )

取消树化：table小于64



- 哈希碰撞
```java
    static final int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);//浮点型低位放位置有可能是0（SEEEEEEEEMMMMMMMMMMMMMMMMMMMMMMM）hash容易为0，hash冲突
    }

    class Map.Entry{
        public final int hashCode() {
            return Objects.hashCode(key) ^ Objects.hashCode(value);
        }
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
初始容量 默认11，加载因子0.75，（Android 首次Map阈值和table容量一样）//默认初始化tab长度为奇数，保证分散分布到tab的位置
扩容增量  2倍+1，保证奇数，有可能是质数//质数，奇数 hash的值在tab上更分散 index = (hash & 0x7FFFFFFF) % tab.length

```java

int newCapacity = (oldCapacity << 1) + 1;

    class HashTableEntry{
        public int hashCode() {
            return hash ^ Objects.hashCode(value);//减少key的hash计算
        }
    }
```
#### HashSet
```
            +----------------------------------------------------------------------------------+
            |                      HashSet: AbstractSet, Set                                    |
            |                               map :HashMap<E,Object>                             |
            +----------------------------------------------------------------------------------+

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
            |         TreeNode: LinkedHashMap.LinkedHashMapEntry                               |
            |            parent;TreeNode                                                       |
            |            left;  TreeNode                                                       |
            |            right; TreeNode                                                       |
            |            prev;  TreeNode                                                       |
            |            red;   boolean                                                        |
            |                                                                                  |
            |                                                                                  |
            |                                                                                  |
            |                                                                                  |
            +----------------------------------------------------------------------------------+


```
初始容量（继承Hashmap）  16
加载因子（0.0~1.0）  0.75f
扩容增量（扩容hash表）  一倍
```

newCap = oldCap << 1
```


#### TreeMap

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

不允许null节点。先查找节点替换，如果不存在，则变红插入再平衡。
红黑树平衡调整:
叔父红色
    1. 祖父叔三角变色（父叔黑，祖红）
    2. 祖节点继续转换
叔父黑色，
    1. 祖父子节点不是直线（否则进行步骤2），往子父直线方向，父节点转换。以转换后的父节点，继续步骤2。
    2. 变父祖两色（父黑祖红），父祖节点方向，祖节点旋转。
    3. 继续步骤1，知道根节点
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






## TimSort
Arrays.sort
https://blog.csdn.net/bryansun/article/details/105182778?utm_medium=distribute.pc_relevant.none-task-blog-BlogCommendFromMachineLearnPai2-7.nonecase&depth_1-utm_source=distribute.pc_relevant.none-task-blog-BlogCommendFromMachineLearnPai2-7.nonecase