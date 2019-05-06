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
|                  |  grap       |      |AVL Trees     |red-black  | btree |
|                  |             |      +--------------------------+       |
|                  |             |      |Left rotation |           |       |
|                  |             |      |Right rotation|           |       |
|                  |             |      |Left-Right    |           |       |
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