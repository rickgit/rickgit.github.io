## 数据结构与算法

Array
String (装饰 char array)
time/Date （装饰 long）
collection
Concurrent access


模板方法
### 可视化
https://www.cs.usfca.edu/~galles/visualization/Algorithms.html
### 基本数据类型封装类
简单工厂 valueof

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



#### 字符串
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
#### 日期Date/Canadar
装饰 long时间戳
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



红黑树插入，根据hash值，找节点，若没找到，找到叶子节点

#### HashSet
```
            +----------------------------------------------------------------------------------+
            |                      HashSet: AbstractSet, Set                                    |
            |                               map :HashMap<E,Object>                             |
            +----------------------------------------------------------------------------------+

```
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
？父红，旋转，黑色子节点数量不变
？父黑子红，换红色，需要旋转才能保证黑色子节点数量不变
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
 
fixAfterInsertion/fixAfterDeletion删除如果黑色需要平衡
 插入默认红色，如果父节点是红色需要平衡。（解析：不允许两个子父节点是红色，需要平衡）
 删除节点是黑色，且**替换**删除节点位置的**节点是黑色**，需要平衡操作（解析：替换节点的分支少了一个黑色节点，根节点到每个叶节点的黑色节点不一样了，需要改平衡）

- fixAfterInsertion：红黑树平衡调整（循环条件：非空，非跟节点，父节点非红色）（不允许null节点。先查找节点替换，如果不存在，则变红插入再平衡）:
叔父红色
    1. 祖父叔三角变色（父叔黑，祖红，父叔红色传递到祖父；并以祖父节点继续平衡； 解析：不平衡原因，红色相邻；平衡处理，红色向上传递）
    2. 祖节点继续转换
叔父黑色，
    1. 祖父子节点不是直线（否则进行步骤2），往子父直线方向，父节点转换。以转换后的父节点，继续步骤2。（解析：直线原因，后续旋转后，避免红色节点再次相连）
    2. 变父祖两色（父黑祖红），父祖节点方向，祖节点旋转。（解析：红色向上传递，并旋转，分支到叶节点的黑色节点数不变）
    3. 继续步骤1，直到根节点或父节点不是红色（解析：当前子节点是红色，父节点也红色，则需要平衡操作）
平衡后根节点设置为黑色


- 删除（概念：前驱节点，删除节点，替换节点）（解析：删除黑色叶子节点；删除节点有一个是黑色子节点。这两种情况会导致当前分支少一个黑色节点；需要找到一个红色节点，涂黑）
  概念：子节点，少一个黑色节点分支的不平衡节点；兄，父，近侄，远侄都是根据定义的子节点的最新状态相对位置节点。
    1. 如果删除节点有两个子节点。找到前驱节点（右节点的最小子节点），前驱节点的数据复制到删除节点，转而前驱节点作为删除节点。步骤3（解析：靠近叶子节点，删除比较好平衡；情况如下，黑色少一个或红色无影响）
    2. 如果一个子节点或零个子节点，执行步骤3
    3. 用左右子节点作为替换节点，替换删除节点。
    4. 如果删除节点是黑色，需要对替换节点做平衡操作（没有子节点且删除节点是黑色节点，需要先平衡在unlink）
fixAfterDeletion删除平衡（循环条件：不是根节点并且当前是黑色节点）（解析：如果删除和替换叶节点都是黑色节点，则分支少了一个黑色节点；或无子节点，删除黑色节点，导致少一个黑色节点）
    1. 兄弟节点为红色，父红兄黑，兄父方向旋转父节点。重新设置兄弟节点（解析：转化缺少黑节点的分支为红色，为了后续涂黑增加黑色节点。转化父兄颜色，通过旋转，各分支黑色节点数不变）
    2. 兄弟节点子节点都是黑色，兄弟节点变红色。以父节点作为新节点，并重复步骤1。否步骤3（解析：红色节点向上传递，左右节点都少了黑色节点，即父节点少了一个节点，需要继续下个循环）
    3. **距离远的侄节点**是黑色，变兄红近侄黑，且按照侄兄方向旋转兄弟节点。重新设置兄节点。到3.2（解析：变色后，近侄黑色数不变，远侄多了少了一个，通过旋转平衡数量；）
    3.2 兄备份父节点颜色，父和远侄变黑，侄父方向旋转。当前节点设置为根节点，跳出循环（解析：当前兄黑远侄红，对调兄父颜色，旋转增加子节点黑色；旋转后兄少了黑色节点，当前兄为红色，变黑节点，）

    跳出循环，设置x为黑色



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


#### WeakHashMap



## TimSort
Arrays.sort
https://blog.csdn.net/bryansun/article/details/105182778?utm_medium=distribute.pc_relevant.none-task-blog-BlogCommendFromMachineLearnPai2-7.nonecase&depth_1-utm_source=distribute.pc_relevant.none-task-blog-BlogCommendFromMachineLearnPai2-7.nonecase

## Hash和随机数


## 二进制编码-Base64
> Base64是一种基于64个可打印字符来表示二进制数据的表示方法。log{2}64=6 。[](https://zh.wikipedia.org/wiki/Base64)
```java 
Apache-codec
    private static final byte[] STANDARD_ENCODE_TABLE = {
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
            'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
            'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/'
    };

    /**
     * This is a copy of the STANDARD_ENCODE_TABLE above, but with + and /
     * changed to - and _ to make the encoded Base64 results more URL-SAFE.
     * This table is only used when the Base64's mode is set to URL-SAFE.
     */
    private static final byte[] URL_SAFE_ENCODE_TABLE = {
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
            'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
            'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '-', '_'
    };
```
### 其他 Base16（用于编码比特流）/Hex（用于打印字符），Base32
## 网络编码-URL
### URL（安全字符+非安全字符Base16编码）
```java
Apache-codec
    static {
        // alpha characters
        for (int i = 'a'; i <= 'z'; i++) {
            WWW_FORM_URL_SAFE.set(i);
        }
        for (int i = 'A'; i <= 'Z'; i++) {
            WWW_FORM_URL_SAFE.set(i);
        }
        // numeric characters
        for (int i = '0'; i <= '9'; i++) {
            WWW_FORM_URL_SAFE.set(i);
        }
        // special chars
        WWW_FORM_URL_SAFE.set('-');
        WWW_FORM_URL_SAFE.set('_');
        WWW_FORM_URL_SAFE.set('.');
        WWW_FORM_URL_SAFE.set('*');
        // blank to be replaced with +
        WWW_FORM_URL_SAFE.set(' ');

        // Create a copy in case anyone (ab)uses it
        WWW_FORM_URL = (BitSet) WWW_FORM_URL_SAFE.clone();
    }
```
非安全字符
```java
        buffer.write(ESCAPE_CHAR);
        final char hex1 = Utils.hexDigit(b >> 4);
        final char hex2 = Utils.hexDigit(b);
        buffer.write(hex1);
        buffer.write(hex2);
```
## 数字摘要-MD5，SHA1，SHA265
MD5需要注意编码格式，使用new String("str".getBytes("UTF-8"), "UTF-8");需要设置编码格式

 
```java
Apache-Codec
new String[] {
            MD2, MD5, 
            SHA_1, //SHA1 已发现碰撞
            SHA_224, SHA_256, SHA_384,SHA_512, SHA_512_224, SHA_512_256,//SHA-2 安全，算法同SHA1
             SHA3_224, SHA3_256, SHA3_384, SHA3_512//SHA-3 安全，算法不同SHA1
        }
```

### MD5 实现
[freebsd MD5](https://svnweb.freebsd.org/base/stable/12/lib/libcrypt/crypt-md5.c?view=markup)
1. initial val word h0(32bit),h1,h2,h3
1. Value+padding0+length（64bit）
2. chunk (512bit)
        words (32bit)
        loop 64
        右移轮换h0,h1,h2,h3
4. h0 append h1 append h2 append h3
### SHA1
1. initial h0(32bit),h1,h2,h3,h4
1. Value+padding0+length（64bit）
2. chunk (512bit)
        words (32bit)
        loop 80
        右移轮换h0,h1,h2,h3,h4
4. h0 append h1 append h2 append h3 append h4

```
SHA-224和SHA-256基本上是相同的，除了：
    h0到h7的初始值不同，以及
    SHA-224输出时截掉h7的函数值。

SHA-512和SHA-256的结构相同，但：
    SHA-512所有的数字都是64位，
    SHA-512运行80次加密循环而非64次，
    SHA-512初始值和常量拉长成64位，以及
    二者比特的偏移量和循环位移量不同。

SHA-384和SHA-512基本上是相同的，除了：
    h0到h7的初始值不同，以及
    SHA-384输出时截掉h6和h7的函数值。
```

### SHA3
Keccak算法

## 非对称-RSA、DSA DH
### RSA
[质数列表](https://www.shuxuele.com/numbers/prime-numbers-to-10k.html)
[在线欧拉函数计算](https://www.dcode.fr/euler-totient)
[在线欧拉函数计算(大于1000000000)](http://www.javascripter.net/math/calculators/eulertotientfunction.htm)
```wiki
首选取两个互素数 p和q, 乘法计算p * q 得到 N。

然后计算出欧拉φ(N)： φ函数φ(N)是小于或等于N的正整数中与N互质的数的数目。 根据欧拉公式，由于p和q都是素数，故 φ(N) = (p-1)*(q-1)

随机选择一个整数e，条件是1< e < φ(n)，且e与φ(n) 互质。
 接着我们计算e对φ(n)的模逆元得到d： e*d ≡ 1 (mod φ(n)) 这个公式简单的说就是 e*d除以φ(N)得到的余数为1，
这个公式可以转换成 ed%((p-1)(q-1))=1 => ed=k(p-1)(q-1)+1

```
#### java-sun
[RSA key生成过程 RSAKeyPairGenerator](openjdk/jdk/src/share/classes/sun/security/rsa/RSAKeyPairGenerator.java)

### DH

```wiki
       Alice                         Bob
+----------------+             +---------------+
|      a,g,p     |             |     b         |
|                |             |               |
|      a         |             |     b         |
|   A=g  mod  p  | --g,p,A---> |  B=g  mod  p  |
|                |             |               |
|      a         |             |     b         |
|   K=B  mod  p  | <----B----- |  K=A  mod  P  |
|                |             |               |
|                |             |               |
+----------------+             +---------------+

```
模p下 **g^{ab}** 和  **g^{ba}**  相等，都等于 **K**

## 对称-DES、3DES AES RC
jdk\src\share\classes\sun\security\krb5\internal\crypto\Aes128.java