## [数据结构](https://www.tutorialspoint.com/data_structures_algorithms/index.htm)
>[搜索算法wiki](https://en.wikipedia.org/wiki/Sorting_algorithm)

```
DSA
+------------------+-------------+-----------------------------------------+
|                  |   Array     |                 tree                    |
|                  |             +------+----------------------------------+
|                  |   List      | Heap |    Search tree                   |
+                  +             +------+--------------------------+-------+
|  data structures |  stack/queue|      |          BST             |       |
|                  |             |      +--------------------------+       |
|                  |  graph      |      |AVL Trees     |red-black  | btree |
|                  |             |      +--------------------------+       |
|                  |  matrix     |      |Left rotation |           |       |
|                  |             |      |Right rotation|           |       |
|                  |  String     |      |Left-Right    |           |       |
|                  |             |      |right-Left    |           |       |
+------------------+-------------+------+--------------+-----------+-------+
|                  |Search Insert|                 Sort                    |
|                  |       Update|                                         |
|                  |       Delete|                                         |
|                  +----------------------------------------------+--------+
|                  |              | Exchange|Selection|Insertion  | Merge  |
|   alth           |  Linear      +---------------------------------------+
|                  |              |Bubble(s)|Selection|Insertion(s)|       |  simple sorts
|                  |  Binary      |         |         |           |        |
|                  |              | Quick   | heapsort| Shell     |Merge(s)| Efficient sorts
|                  |  Hash Table  |         |         |           |        |
|                  |              |         |         |           |        |
|                  | Interpolation|         |         |           |        |
|                  |              |         |         |           |        |
+------------------+--------------+----------+---------+----------+--------+
|                  |                                                       |
|  Asymptotic      |     Ο(n)    Ω(n)    θ(n)                              |
|  Analysis        |                                                       |
+--------------------------------------------------------------------------+
|  Characteristics | Finiteness    Unambiguous   Feasibility               |
|                  |                                                       |
|                  | Input   Output                  Independent           |
+------------------+-------------------------------------------------------+
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
### 排序涉及的元素
由小到大排序,简单到高效算法

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

```shell sort
                 +--------------------------------------------------+
                 | Unsorted sublist                                 |
                 +--------------------------------------------------+

sorted sublist   +--+    gap            +--+      gap             +-+
                 +--+                   +--+                      +-+


sorted sublist   +--+ gap1   +--+  gap1 +--+  gap1  +---+  gap1   +-+
                 +--+        +--+       +--+        +---+         +-+

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
## Hash 与 Bloom Filter


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


### QR Code Graph
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
```