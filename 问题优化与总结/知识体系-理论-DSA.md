## [数据结构](https://www.tutorialspoint.com/data_structures_algorithms/index.htm)
>[搜索算法wiki](https://en.wikipedia.org/wiki/Sorting_algorithm)

```
DSA
+------------------+-----------------------------------------------------------------------------+
|                  | Array                                                                       |
| data structures  |                                                                             |
|                  | List                queue      Deque           stack   Map                  |
|                  |  add                 offer      offerFirst      push      HashMap  Hashtable|
|                  |  remove              poll       offerLast       pop       TreeMap  SortedMap|
|                  |  Positional Access   peek       pollFirst       peek                        |
|                  |  Search/indexOf                 pollLast                                    |
|                  |  traverse                                                                   |
|                  |                                                                             |
|                  | graph      matrix      String                                               |
|                  |    Depth                                                                    |
|                  |    Breadth                                                                  |
|                  +------+------+---------------------------------------------------------------+
|                  |      | Heap |    Search tree                 |Spanning                      |
|                  |      +--------------------------------+------+                              |
|                  |      |      |         BST             |      |                              |
|                  |      |      +-------------------------+      |                              |
|                  |      |      |AVL Trees     |red|black | btree|                              |
|                  | tree |      +-------------------------+      |                              |
|                  |      |      |Left rotation |          |      |                              |
|                  |      |      |Right rotation|          |      |                              |
|                  |      |      |Left+Right    |          |      |                              |
|                  |      |      |right+Left    |          |      |                              |
+-------------------------+---------------------+----------+------+------------------------------+
|                  |search       |           Sort                                                |
|                  +-------------------------------------------------------------+---------------+
|                  |             |  compared                                     |  key          |
|                  +-----------------------------+-----------------------------------------------+
|                  |  Linear     | Exchange      | Merge  |Insertion   |Selection|               |
|   alth           |  Binary     +---------------------------------------------------------------+
|                  |  Hash Table |Bubble(s)      |        |Insertion(s)|Selection|Counting  Radix|  simple sorts
|                  |Interpolation|               |        |            |         |               |
|                  |             | Quick         |Merge(s)| Shell      | heapsort|         bucket| Efficient sorts
|                  |             +-----------------------------------------------+               |
|                  |             | Cocktail/Comb |   Tim               |         |               |
|                  +-------------+---------------+---------------------+---------+               |
|                  |  time,random                                                |               |
+--------------------------------------------------------------------------------+---------------+
|                  |                                                                             |
|  Asymptotic      |     Ο(n)    Ω(n)    θ(n)                                                    |
|  Analysis        |                                                                             |
+------------------------------------------------------------------------------------------------+
|  Characteristics | Finiteness    Unambiguous   Feasibility                                     |
|                  |                                                                             |
|                  | Input   Output                  Independent                                 |
+------------------+-----------------------------------------------------------------------------+

```



```DS
                     Operation

+------+-------------+---------------+------------------------------------------+
|      | array       |  Element      |   Traverse    Insertion    Deletion      |
|      |             |  Index        |   Search/Update                          |
+------+-------------+---------------+------------------------------------------+
|      |             | Link (Node)   |                                          |
|      | Linked List | Next          |  Display    Insertion    Deletion        |
|      |             | LinkedList    |                                          |
|      |             | Prev          |  Search       Delete                     |
| DSA  +-----------------------------+------------------------------------------+
|      | stack       |   push()         pop()                                   |
|      |             |   peek()         isFull()  isEmpty()                     |
|      +------------------------------------------------------------------------+
|      | Queue       |  enqueue()   dequeue()                                   |
|      |             |  peek()      isfull()  isempty()                         |
|      +------------------------------------+-----------------------------------+
|      | Graph       |   Vertex   Edge      |        Add Vertex     Add Edge    |
|      |             |   Adjacency  Path    |       Display Vertex              |
|      |             |                      |                                   |
|      |             |                      |       Depth First Traversal       |
|      |             |                      |       Breadth First Traversal     |
|      +------------------------------------------------------------------------+
|      | Tree        | Path   Root          |        Insert      Search         |
|      |             |                      |                                   |
|      |             | Parent   Child       |        Preorder Traversal         |
|      |             |                      |                                   |
|      |             | Leaf    Subtree      |        Inorder Traversal          |
|      |             |                      |                                   |
|      |             | Visiting Traversing  |        Postorder Traversal        |
|      |             |                      |                                   |
|      |             | Levels    keys       |                                   |
+------+-------------+----------------------+-----------------------------------+
|      |             |  node(red or black)  |                                   |
|      |  red-black  |  root (black)        |                                   |
|      |             |  leaves (NIL) (black)|                                   |
|      |             |  node(r),children(b) |                                   |
|      |             |each path,same nodes(b)|                                  |
+------+-------------+----------------------+-----------------------------------+
|      |             |                                                          |
|      |             | leaf nodes same level                                    |
|      |   b-tree    | leaf nodes max n-1 keys                                  |
|      |             | root at least two children.                              |
+------+-------------+----------------------+-----------------------------------+

```
![leetcode](./res/leetcode.svg)


### 排序涉及的元素
衡量：时间，空间，稳定

代码实现 https://github.com/TheAlgorithms/Python
排序演示图 sort visualiser

由小到大排序,简单到高效算法



```bubblesort
select max num to exchange
+---------------------------------------------------
| Unsorted sublist                  |Sorted sublist|
+--------------------------------------------------+
|                                   |              |
+-----------------------------------+--------------+
        +-------------------+
        | index1   | index2 |
        +-------------------+
    +----------------------->
          exchange
for unsorted time
    for unsorted to exchange
```


```quicksort
select pivot,exchange unsorted sublist
          +--------------------------------------------------+
          |               Unsorted sublist                   |
          +-------+------------------------------------------+
          |pivot0 |                                          |
          +----------------------+---------------------------+
sort0     |       <=pivot        |      >pivot               |
          +------------------------------+-------------------+
          |pivot1 |              |pivot2 |                   |
          +-------+--+-------------------+----+--------------+
sort1/2   | <=pivot1 | >pivot1   | <=pivot2   |  >pivot2     |
          +----------+-----------+------------+--------------+

应用：Java Fork/Join

```


```mergesort
                  +---------------------------------------------------     ^
                  | Unsorted sublist                                 |     |
                  +--------------------------------------------------+     |
                                                                           |
                  +-----------------------+--------------------------+     |
split  unsorted   |                       |                          |     |
                  +-----------------------+--------------------------+     |
                                                                           |
                  +----------+------------+-------------+------------+     |
split  unsorted   |          |            |             |            |     |
                  +----------+------------+-------------+------------+     |
                                                                           |  sort and merge
                  +-----------------+-----+------+------+------+-----+     |
split  unsorted   |item| item|      |     |      |      |      |     |     |
                  +-----------------+-----+------+------+------+-----+     |
                                                                           |
                   +--------->                                             |
                    sorted and merge                                       |
                                                                           |
                   +-------------------------------------------------------+

```
```insertsort
insert a num on unsorted sublist to sorted sublist
+--------------------------------+------------------+
|          Sorted sublist        | Unsorted sublist |
+---------------+-----------------------------------+
| sort          |  Least element | unsorted         |
+---------------+----------------+------------------+

for unsorted times
    for unsorted item to insert
```
```shell sort
                 +--------------------------------------------------+
                 | Unsorted sublist                                 |
                 +--------------------------------------------------+

sorted sublist   +--+    gap            +--+      gap             +-+
                 +--+                   +--+                      +-+


sorted sublist   +--+ gap1   +--+  gap1 +--+  gap1  +---+  gap1   +-+
                 +--+        +--+       +--+        +---+         +-+

```
 ```selectsort
select mini num on unsorted sublist to sorted sublist
+---------------+-----------------------------------+
| Sorted sublist| Unsorted sublist                  |
+-------------------------------+-------------------+
|               | Least element |  unsorted         |
+---------------+---------------+-------------------+

for unsorted times
    for unsorted select min
```

```heapsorts
+---------------------------------------------------
| Unsorted sublist                  |Sorted sublist|
+--------------------------------------------------+
|                                   |              |
+-----------------------------------+--------------+

 <--------------------------------+   ^
       ajust unsorted to max heap     |
|                                     |
|                                     |
|                                     |
+-------------------------------------+
         select root value to final

```
 [其他排序](https://www.cnblogs.com/kkun/archive/2011/11/23/2260312.html)


## Search
String-searching algorithm （string-searching algorithms）
bloom filter
+----------------+---------------------------------------------------+
|  String metric |    Bitap algorithm                                |
+--------------------------------------------------------------------+
| string-search  |    Boyer–Moore      Rabin–Karp                    |
|   algorithm    |                                                   |
+----------------+---------------------------------------------------+

StringSearch.jar (Boyer–Moore, Bitap algorithm)

### Binary
n：数组长度
k：次数
第k次折半：还剩n/(2^k)个元素.
最后还剩1个元素，n/(2^k)= 1，耗时 2^k=n，即k=log2(n)




### 字符串
[String_processing_algorithms](https://en.wikipedia.org/wiki/String_(computer_science)#String_processing_algorithms)
```
String searching algorithms for finding a given substring or pattern
String manipulation algorithms
Sorting algorithms
Regular expression algorithms
Parsing a string
Sequence mining
```

### 树

树的操作
```

Operations 
Searching 
Insertion 
Deletion 
Traversal （二叉树根节点访问顺序分为 先序遍历（中左右） 中序遍历（左中右） 后序遍历（左右中））
Verification
```

 [md5 冲撞](https://github.com/corkami/collisions)
## 多媒体 - QR Code Graph
>History. The smallest QR codes are 21x21 pixels, and the largest are 177x177. The sizes are called versions. The 21x21 pixel size is version 1, 25x25 is version 2, and so on. The 177x177 size is version 40.Each version is 4 pixels larger than the previous version.[（引用）](https://www.thonky.com/qr-code-tutorial/introduction)




[Character Capacities by Version, Mode, and Error Correction](https://www.thonky.com/qr-code-tutorial/character-capacities)
[Error Correction Code Words and Block Information](https://www.thonky.com/qr-code-tutorial/error-correction-table)

#### 生成QR
- Data Analysis()
- Data Encoding(**Mode Indicator**,**Character Count Indicator**,**Encoded Data**,**Terminator**)
- Structure Final Message(**Data Encoding**,**Error Correction Coding**)
里德-所罗门码
- Data Masking(**Structure Final Message**)

- Format Information(**error correction level**,**mask pattern**,**Error Correction Bits**)
- Version Information(**version string**,**error correction string**)

- Matrix(**Data Masking**,**Format Information**,**Version Information**)
 


## LEETCODE
```java
class Solution {
    public int[] twoSum(int[] nums, int target) {
        int unsortindex,compareindex;
        //unsorted
        for(unsortindex=0;unsortindex<nums.length;unsortindex++){
            for(compareindex=unsortindex+1;compareindex<nums.length;compareindex++){
                if(nums[unsortindex]+nums[compareindex]==target){
                        return new int[]{unsortindex,compareindex}; 
                }
            }
        }
        return null;
    }
}

class Solution {
    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        int overValue=0;int value=0;
        ListNode first=null;ListNode prenode=null;ListNode node=null;
        while(l1!=null||l2!=null){
            value=(l1==null?0:l1.val)+(l2==null?0:l2.val)+overValue;
            node=new ListNode(value%10); 
            
            overValue=value/10;
            if(prenode==null)
                first=node;
            else
                prenode.next=node;
            prenode=node;
            
            if(l1!=null)
            l1=l1.next;
            if(l2!=null)
            l2=l2.next;
        }
        if(overValue!=0)
            prenode.next=new ListNode(overValue);
        return first;
    }
}

class Solution {
    public int lengthOfLongestSubstring(String s) {
        if(s==null||"".equals(s))
            return 0;
        
        char[]  chars=s.toCharArray();
        if(chars.length==1)
            return 1;
        
        int lastFindindex=0,lastLenth=0;
        int recentFindIndex=0,recentLenth=0;
        int index=0;
        
        for(index=0;index<chars.length;index++){//"dvdf" : 1 0 1,1 2 2, 
            for(recentFindIndex=index-1;recentFindIndex>=0&&recentFindIndex>=index-recentLenth;recentFindIndex--){
                if(chars[recentFindIndex]!=chars[index])
                    continue;
                break;                    
            }
            
            if(index-recentFindIndex<=recentLenth){
                recentLenth=index-recentFindIndex;
            }else{
                recentLenth++;
            }
            
            if(recentLenth>lastLenth){
                lastFindindex=index;
                lastLenth=recentLenth;
            }
            
        }
        
        return lastLenth;
        
    }
}

1. Two Sum                                        18 ms 36.8 MB
2. Add Two Numbers                                2 ms 44.6 MB
3. Longest Substring Without Repeating Characters 2 ms 36.7 MB
4. Median of Two Sorted Arrays                    2 ms 46 MB 
5. Longest Palindromic Substring  135 ms 35.9 MB
6. ZigZag Conversion               3 ms 37.8 MB
7. Reverse Integer                 15 ms  35.6 MB
```



### 算法

字符哈希算法
```java
Brian Kernighan 与 Dennis Ritchie 《The C Programming Language》 多项式 hash
BKDRHash java.lang.String.hashCode() 简单快捷，正确率高 

APHash

DJBHash
JSHash

Robert Sedgwicks 《Algorithms in C》
RSHash
SDBMHash
PJWHash
ELFHash 

DEKHash, FNVHash, DJB2Hash, PJWHash
```
对象一致性Hash
```java
java9随机数+三个确定值，运用Marsaglia's xorshift scheme随机数算法得到的一个随机数
xor-shift

Android 32位的monitor_ 描述对象的Hash Code信息
Object::hash_code_seed(987654321U + std::time(nullptr))
 do {
    expected_value = hash_code_seed.load(std::memory_order_relaxed);
    new_value = expected_value * 1103515245 + 12345;
  } while (!hash_code_seed.CompareAndSetWeakRelaxed(expected_value, new_value) ||
      (expected_value & LockWord::kHashMask) == 0);
```
