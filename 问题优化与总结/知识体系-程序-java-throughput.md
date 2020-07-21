## 提高吞吐量 J.U.C
- 内存原理（AQS）
- 并发容器与阻塞队列
- 线程及线程池

## 数据安全共享访问
### volatile

```
+-------------+--------------+----------------------+------------------+------------------------+-----------------------+
|             |              |                      |                  |                        |                       |
| Object      |  DCL problem |    synchronized      |  Object.wait()   |                        |                       |
|             |              | (Reentrant,unfair)   |  Object.notify() |                        |                       |
|             |              | (Exclusi^e,pessimism)|                  |                        |                       |
+----------------------------+-----------------------------------------+------------------------+-----------------------+
|             | J.U.C.atomic                                                                                            |
|  volatile   +---------------------------------------------------------------------------------------------------------+
|             | ConcurrentLinkedDeque                                                                                   |
|             | ConcurrentSkipListMap                                                                                   |
|             +---------------------------------------------------------------------------------------------------------+
|             |              |ReentrantReadWriteLock                                                                    |
|             |     AQS      |(shared Read)                                                                             |
|             |              |StampedLock                                                                               |
|             |              +----------------------+------------------+------------------------+-----------------------+
|             |              |                      |   Condition      |    CountDownLatch      |   ArrayBlockingQueue  |
|    CAS      |              |                      |                  |    CyclicBarrier       |   LinkedBlockingQueue |
|             |              |  ReentrantLock       |                  |                        |                       |
|             |              |(Exclusi^e,optimistic)|                  |   Semaphore,Exchanger  |   ConcurrentHashMap   |
|             |              |                      |                  |                        |   CopyOnWriteArrayList|
|             |              |  ThreadPoolExecutor  |                  |                        |                       |
|             |              |                      |                  |                        |   Fork/Join           |
+-------------+--------------+----------------------+------------------+------------------------+-----------------------+



```

#### volatile 内存模型
 volatile 内存可见性，重排序,顺序一致性
 final 内存语义，读写重排序规则
 hanpen before，指两个操作指令的执行顺序
 CAS 读改写原子性

 可以保证变量的可见性 ，不能保证变量状态的“原子性操作”，原子性操作需要lock或cas
#### 原子性问题（CAS 保证读改写）

-  Lock-Free操作中ABA问题（AtomicStampedReference ）
  定义：meaning that writes to this field are immediately made visible to other threads.
  [正确使用 Volatile 变量](https://www.ibm.com/developerworks/cn/java/j-jtp06197.html)
  保证修改的值会立即被更新到主存，当有其他线程需要读取时，它会去内存中读取新值。在某些情况下性能要优于synchronized，但对变量的写操作不依赖于当前值。
[volatile的读写操作的过程: ](https://blog.csdn.net/jiuqiyuliang/article/details/62216574)

```

（1）线程写volatile变量的过程：
         1、改变线程工作内存中volatile变量的副本的值
         2、将改变后的副本的值从工作内存刷新到主内存
（2）线程读volatile变量的过程：
        1、从主内存中读取volatile变量的最新值到线程的工作内存中
        2、从工作内存中读取volatile变量的副本
```

- 原子操作类：采用 volatile和[Unsafe#Unsafe_CompareAndSwapInt](/home/anshu/workspace/openjdk/hotspot/src/share/vm/prims)的CAS原子操作
```
java.util.concurrent.atomic.AtomicInteger object internals:
 OFFSET  SIZE   TYPE DESCRIPTION                               VALUE
      0     4        (object header)                           05 00 00 00 (00000101 00000000 00000000 00000000) (5)
      4     4        (object header)                           00 00 00 00 (00000000 00000000 00000000 00000000) (0)
      8     4    int AtomicInteger.value                       0 //volatile修饰
     12     4        (loss due to the next object alignment)
Instance size: 16 bytes
Space losses: 0 bytes internal + 4 bytes external = 4 bytes total

```


AtomicInteger的cas原理
```
jdk/src/share/classes/sun/misc/Unsafe.java
public final int getAndAddInt(Object o, long offset, int delta) {
    int v;
    do {
        v = getIntVolatile(o, offset);//读取线程共享主存的最新值
    } while (!compareAndSwapInt(o, offset, v, v + delta));//没有置换成功，继续循环直到竞争成功
    return v;
}

hotspot/src/share/vm/prims/unsafe.cpp
UNSAFE_ENTRY(jboolean, Unsafe_CompareAndSwapInt(JNIEnv *env, jobject unsafe, jobject obj, jlong offset, jint e, jint x))
  UnsafeWrapper("Unsafe_CompareAndSwapInt");
  oop p = JNIHandles::resolve(obj);
  jint* addr = (jint *) index_oop_from_field_offset_long(p, offset);
  return (jint)(Atomic::cmpxchg(x, addr, e)) == e;// 将主存的副本和线程的新值传进去，后续cpu进行CAS操作。查看是否把主存原来的值置换出来，新增的值置换到主存
UNSAFE_END

```
AtomicInteger,AtomicLong,AtomicBoolean...,AtomicReference
##### AtomicStampedReference
```

+--------------------------------------------------------------------------------------+
|                     AtomicStampedReference                                           |
|                                                                                      |
|                        pair:Pair<V>                                                  |
|                        compareAndSet()                                               |
|                                                                                      |
+--------------------------------------------------------------------------------------+
|                     Pair                                                             |
|                       reference; T                                                   |
|                       stamp;int                                                      |
+--------------------------------------------------------------------------------------+

```
 
  - 乐观锁思想-CAS原子操作。修改前，对比直到共享内存和当前值（当前线程临时内存）一致，才做修改，这个流程不会加锁阻塞（《Java 并发编程的艺术》2.3节）
      - AtomicStampedReference来解决ABA问题；
      - 循环时间长开销大
      - AtomicReference类来多个共享变量合成一个共享变量来操作



#### 同步问题（AQS 实现 **乐观锁**）
**os::PlatformEvent::park()** 相对于 wait/notify 能精确唤醒线程
饥饿现象（即某个线程长时间未竞争到锁）使用公平锁，ReentrantLock默认用非公平锁
  -非公平锁，非是按照申请锁的时间前后给等待线程分配锁的
  -公平锁，多个线程按照申请锁的顺序来获取锁
1. ReentrantLock/Condigtion 排它锁，同一时间只允许一个线程访问
2. ReentrantReadWriteLock  读操作远多于写操作情况，提供并发性和吞吐量
4. StampedLock              CLH锁
5. CountDownLatch、Semaphore、CyclicBarrier
```java
 +----------------------------------------------------------------------------------------------+
 |  ReentrantLock                                                                               |
 |         sync:Sync                                                                            |
 |         lock()                                      unlock()                                 |
 +----------------------------------------------------------------------------------------------++----------------------------------------------------------------------------------------------+
 |  NonfairSync :Sync             FairSync:Sync                                                 |
 |   lock()                          lock()                                                     |
 |   tryAcquire(acquires:int)        tryAcquire(acquires:int)                                   |
 +----------------------------------------------------------------------------------------------+
 |  Sync :AbstractQueuedSynchronizer                                                            |
 |                                                                                              |
 |          nonfairTryAcquire(acquires:int )           tryRelease(releases:int)   newCondition()|
 +------------------------------------------------------------------------+---------------------+
 | AbstractQueuedSynchronizer :AbstractOwnableSynchronizer                |                     |
 |   state:int//cas        head:Node                                      |                     |
 |                         tail:Node                                      |                     |
 |                                                                        | ConditionObject     |
 |   lock()                tryAcquire(acquires:int )   release(arg:int)   |         :Condition  |
 |   acquire()             addWaiter(mode:Node)//enqueues                 |    firstWaiter:Node |
 |  //cas state            hasQueuedPredecessors():boolean                |    lastWaiter:Node  |
 |   compareAndSetState()  acquireQueued()                                |                     |
 |                         shouldParkAfterFailedAcquire()                 |                     |
 |                         parkAndCheckInterrupt()                        |                     |
 +------------------------------------------------------------------------+                     |
 | AbstractOwnableSynchronizer                                            |                     |
 |       exclusiveOwnerThread:Thread                                      |                     |
 |                                                                        |                     |
 +---------------------------------+--------------------------------------+---------------------+
 +---------------------------------+------------------------------------------------------------+
 | Node                            |   LockSupport                                              |
 |   waitStatus:int                |        park()                                              |
 |   thread:Thread                 |        unpark()                                            |
 |                                 |                                                            |
 |   next:Node                     |   Thread                                                   |
 |   nextWaiter: Node              |      interrupted()                                         |
 |    prev: Node                   |      parkBlocker:Object                                    |
 |   predecessor()                 |                                                            |
 +---------------------------------+   Unsafe                                                   |
 |                                        unpark()                                              |
 |                                        park()                                                |
 |                                                                                              |
 +----------------------------------------------------------------------------------------------+

+----------------------------------------------------------------------------------------------+
|  ReentrantReadWriteLock:Lock          ReadLock:Lock       WriteLock:Lock                     |
|        readerLock: ReadLock                 sync:Sync        sync:Sync                       |
|        writerLock:WriteLock              //nextWaiter=SHARED                                 |
|        sync:.Sync                                                                            |
|                                                                                              |
|                                                                                              |
|     FairSync:Sync                  NonfairSync:Sync                                          |
|         writerShouldBlock():bool                                                             |
|         readerShouldBlock():bool                                                             |
+----------------------------------------------------------------------------------------------+
|                                                                                              |
| Sync : AbstractQueuedSynchronizer                                                            |
|    cachedHoldCounter:HoldCounter                 acquireShared()         acquire()           |
|    readHolds: ThreadLocal<HoldCounter>                                                       |
|                                                                                              |
|    firstReader:Thread                            releaseShared()         release()           |
|    firstReaderHoldCount:int                                                                  |
|                                                                                              |
+----------------------------------------------------------------------------------------------+
|   HoldCounter       ThreadLocal                                                              |
|         count:int       threadLocalHashCode:int                                              |
|                                                                                              |
|                     Thread                                                                   |
|                         threadLocals:ThreadLocalMap                                          |
|                                                                                              |
|                     ThreadLocalMap                                                           |
|                         size:int                                                             |
|                         table:Entry[]                                                        |
|                         threshold:int                                                        |
|                                                                                              |
+----------------------------------------------------------------------------------------------+


+----------------------------------------------------------------------------------------------+
|  StampedLock                                                                                 |
|                                                                                              |
|      readLockView;     ReadLockView       tryOptimisticRead():long   writeLock():long        |
|       writeLockView;   WriteLockView      readLock():long//pessimism                         |
|      readWriteLockView;ReadWriteLockView                                                     |
|      stat:long                            tryConvertToReadLock()     tryConvertToWriteLock() |
|      readerOverflow:int                                                                      |
|      whead;WNode                          unlockRead(stamp:long)     unlockWrite(stamp:long) |
|      wtail;WNode                                                                             |
+----------------------------------------------------------------------------------------------+
|                                                                                              |
|  WNode              ReadLockView :Lock         WriteLockView :Lock        ReadWriteLockView  |
|     prev;  WNode      lock()                      lock()                       :ReadWriteLock|
|     next;  WNode      lockInterruptibly()         lockInterruptibly()                        |
|     cowait;WNode      tryLock()                   tryLock()                                  |
|     thread;Thread     unlock()                    unlock()                                   |
|     status;int        newCondition():Condition    newCondition():Condition                   |
|     mode;  int                                                                               |
|                                                                                              |
+----------------------------------------------------------------------------------------------+



```

```
1. 独占式获取与释放同步状态
2. 共享式获取与释放同步状态
3. 查询同步队列中的等待线程情况
```

**重入锁** ReentrantLock（**独享锁**,**互斥锁**）/ReentrantReadWriteLock（**读锁是共享锁，其写锁是独享锁**）/StampedLock （内部通过 AbstractQueuedSynchronizer同步器，实现**公平锁和非公平锁**，AbstractQueuedSynchronizer包含Condition，使用volatile修饰的state变量维护同步状态），解决复杂锁问题，如先获得锁A，然后再获取锁B，当获取锁B后释放锁A同时获取锁C，当锁C获取后，再释放锁B同时获取锁D。


##### CountDownLatch，CyclicBarrier，Semaphore，Exchanger
- ReentrantLock: 使用了AQS的独占获取和释放,用state变量记录某个线程获取独占锁的次数,获取锁时+1，释放锁时-1，在获取时会校验线程是否可以获取锁。
- Semaphore: 使用了AQS的共享获取和释放，用state变量作为计数器，只有在大于0时允许线程进入。获取锁时-1，释放锁时+1。
- CountDownLatch: 使用了AQS的共享获取和释放，用state变量作为计数器，在初始化时指定。只要state还大于0，获取共享锁会因为失败而阻塞，直到计数器的值为0时，共享锁才允许获取，所有等待线程会被逐一唤醒

```
+----------------------------------------------------------------------------------------------+
|  CountDownLatch                                                                              |
|        sync:.Sync                                                                            |
|        countDown()                                                                           |
|                                                                                              |
+----------------------------------------------------------------------------------------------+
|  Sync :AbstractQueuedSynchronizer                                                            |
|       tryAcquireShared()             acquireSharedInterruptibly()     releaseShared(int)     |
|       tryReleaseShared()                                                                     |
|                                                                                              |
+----------------------------------------------------------------------------------------------+

+----------------------------------------------------------------------------------------------+
|  CyclicBarrier                                                                               |
|        lock:ReentrantLock                                                                    |
|        trip:lock.newCondition                 await()                                        |
|        barrierCommand:Runnable                                                               |
|        generation: Generation                                                                |
|        count:int                                                                             |
|        parties:int //backup                                                                  |
|                                                                                              |
+----------------------------------------------------------------------------------------------+
|   Generation                                                                                 |
|        broken;boolean                                                                        |
|                                                                                              |
+----------------------------------------------------------------------------------------------+
+----------------------------------------------------------------------------------------------+
|  Semaphore                                                                                   |
|       sync;.Sync                                                                             |
|                                                                                              |
|       acquire()                                                                              |
|       release()                                                                              |
+----------------------------------------------------------------------------------------------+
|                                           NonfairSync :Sync           FairSync :Sync         |
|                                                 tryAcquireShared()        tryAcquireShared() |
+----------------------------------------------------------------------------------------------+
|   Sync                                                                                       |
|       acquireSharedInterruptibly()                                                           |
|       releaseShared()                                                                        |
|                                                 nonfairTryAcquireShared()                    |
+----------------------------------------------------------------------------------------------+

```

```java
 
 
+-------------------------------------------------------------------------------------------+
|                           await()                             doBussiness()               |
|                           +---------------------------------+                             |
|             +-------------+   block                         +----------->                 |
|    WorkThread             +---------------------------------+                             |
|                                                                                           |
|                                                                                           |
|   CountDownLatch(2)          1                              0                             |
|   +--------------------------------------------------------->                             |
|                                                                                           |
|                              ^                              ^                             |
|    Thread 1                  | cutdown()                    |                             |
|             +----------------+--->                          |                             |
|    Thread 2                                                 | cutdown()                   |
|             +-----------------------------------------------+-------->                    |
|                                                                                           |
+-------------------------------------------------------------------------------------------+

+-------------------------------------------------------------------------------------------+
|                                                                                           |
|   CyclicBarrier(3)                                                                        |
|                           2      1                            0                           |
|   +----------------------------------------------------------->                           |
|                                                                                           |
|   Thread 1                await()                                                         |
|            doneSomething()+-----------------------------------+ doOther()                 |
|   +-----------------------+            block                  +----------------->         |
|                           +-----------------------------------+                           |
|                                                                                           |
|   Thread 2                       await()                                                  |
|                 doneSomething()   +---------------------------+ doOther()                 |
|   +-------------------------------+     block                 +----------------->         |
|                                   +---------------------------+                           |
|   Thread 3                                                                                |
|                                          doneSomething()    await()                       |
|   +----------------------------------------------------------------------------->         |
|                                                                 doOther()                 |
|                                                                                           |
+-------------------------------------------------------------------------------------------+


+-------------------------------------------------------------------------------------------+
|                                                                                           |
|  Semaphore                                                                                |
|             1    2                        1                       0                       |
|  +----------------------------------------------------------------------------->          |
|                                                                                           |
|  Thread 1              doSyncWork()                                                       |
|           acquire()                   release()                                           |
|  +----------------------------------------------------------------------------->          |
|                                                                                           |
|  Thread 2    acquire()                                        release()                   |
|                +--------------------------+    doSyncWork()                               |
|  +-------------+       block              +------------------------------------>          |
|                +--------------------------+                                               |
|                                                                                           |
+-------------------------------------------------------------------------------------------+

```

- 生产者与消费者。Condition配合ReentrantLock，实现了wait()/notify()（阻塞与通知）,LockSupport的park()/unpark()


### SynChronized
```
object moniter 重量级锁流程:
        +--------------------------------------+    +----------------------+
        |         waiting Queue                |    |  ReadyThread         |
        |                                      |    |                      |
  1     | +--------------+    2    +---------+ | 3  | +------------------+ |
+-----> | |              |         |         | |    | |                  | |
        | |ContentionList| +-----> |EntryList| +--> | |OnDeck            | |
+-----> | |    (cxl)     |         |         | |    | |                  | |
        | +--------------+         +---+-----+ |    | +------------------+ |
        |                              ^       |    |                      |
        +--------------------------------------+    +------+---------------+
                                       | 6              4  |
                                       |                   v
        +--------------------------------------+    +------+---------------+
        |           BlockingQueue              |    |  RunningThread       |
        |                          +---------+ | 5  | +------------------+ |
        |                          |         | |    | |                  | |
        |                          |WaitSet  | +<---+ |Owner             | |
        |                          |         | |    | |                  | |
        |                          +---------+ |    | +------------------+ |
        +--------------------------------------+    +----------------------+



源碼：
      +----------------------------------------------------------------------------------+
      |       BytecodeInterpreter : StackObj               TemplateTable: AllStatic      |
      |                 run()   //sample,no use in hotspot        monitorenter()         |
      |                                                                                  |
      +----------------------------------------------------------------------------------+
      |    [hotspot/share/vm/oops]                                                       |
      |                                 oopDesc                                          |
      |                                   _mark:markOopDesc                              |
      |                                   _klass:klassOop                                |
      |                                                                                  |
      |                  instanceOopDesc:oopDesc       arrayOopDesc:oopDesc              |
      +----------------------------------------------------------------------------------+
      |                                markOopDesc:oopDesc                               |
      |                                    monitor()                                     |
      |                                    has_bias_pattern():bool                       |
      |                                    biased_locker():JavaThread*                   |
      |                                                                                  |
      |                                ObjectMonitor                                     |
      |                                     _cxq:ObjectWaiter *           _owner:void *  |
      |                                     _EntryList:ObjectWaiter                      |
      |                                     _WaitSet:ObjectWaiter *                      |
      |                                                                                  |
      +----------------------------------------------------------------------------------+
      |       BiasedLocking : AllStatic         JavaThread                               |
      |                                             cached_monitor_info()                |
      |                                               :GrowableArray<MonitorInfo*>*      |
      |                                         MonitorInfo                              |
      |                                             _owner:oop                           |
      |                                             _lock:BasicLock*                     |
      |                                                                                  |
      +----------------------------------------------------------------------------------+


```


  1. volatile（内存可见性，其他线程看到的value值都是最新的value值，即修改之后的volatile的值; 指令有序序，解决重排序） + cas 原子操作(atomic operation)是不需要synchronized，不会被线程调度机制打断的操作。
  2. synchronized。当synchronized锁住一个对象后，别的线程如果也想拿到这个对象的锁，就必须等待这个线程执行完成释放锁，才能再次给对象加锁，这样才达到线程同步的目的。
  3. synchronized(Sync.class)/ static synchronized方法 为全局锁，相当于锁住了代码段。限制多线程中该类的**所有实例**同时访问jvm中该类所对应的代码块。
上下文切换查看工具 **vmstat**,**LMbench**

#### 内置锁（synchronized）
 [从jvm源码看synchronized](https://www.cnblogs.com/kundeg/p/8422557.html)
    - 静态的锁顺序死锁。一个线程执行a方法且已经获得了A锁，在等待B锁；另一个线程执行了b方法且已经获得了B锁，在等待A锁。这种状态，就是发生了静态的锁顺序死锁。
    - [动态的锁顺序死锁](https://www.androidos.net.cn/codebook/AndroidRoad/java/concurrence/deadlock.md)
    ```java
    //可能发生动态锁顺序死锁的代码
    class DynamicLockOrderDeadLock {
        public void transefMoney(Account fromAccount, Account toAccount, Double amount) {
            synchronized (fromAccount) {
                synchronized (toAccount) {
                    //...
                    fromAccount.minus(amount);
                    toAccount.add(amount);
                    //...
                }
            }
        }
    }
    ```

    -  协作对象之间发生的死锁
- 内置的锁 synchronized，可重入非公平锁（是独占锁，是一种悲观锁），会导致饥饿效应，不可中断

  - Object.wait(),Object.notify()



## 并发集合 (ArrayBlockingQueue,LinkedBlockingQueue)，Fork/Join框架，工具类
Collections.synchronized
ArrayList ->                                      CopyOnWriteArrayList
HashMap   ->                                      ConcurrentHashMap 
TreeMap   -> Collections.synchronizedSortedMap -> ConcurrentSkipListMap 
TreeSet   ->                                      ConcurrentSkipListSet
HashSet   ->                                      CopyOnWriteArraySet
LinkedList->                                      ConcurrentLinkedDeque

BlockingQueue
了解LinkedBlockingQueue、ArrayBlockingQueue、DelayQueue、SynchronousQueue
#### CopyOnWriteArrayList
读操作是可以不用加锁的
```
+----------------------------------------------------------------------------------------------+
| ConcurrentHashMap                                                                            |
|       lock:Object                                                                            |
|       elements:Object[]                                                                      | 
+----------------------------------------------------------------------------------------------+
|       Arrays                                                                                 | 
|           copyOf()                                                                           | 
+----------------------------------------------------------------------------------------------+
```
写时复制机制

合适读多写少
不能用于实时读，会消耗内存

#### ConcurrentHashMap。有并发要求，使用该类替换HashTable
java 7 **分段锁**技术,java 8 摒弃了Segment（锁段）的概念，采用CAS + synchronized保证并发更新的安全性，底层采用数组+链表+红黑树的存储结构。
```
      +----------------------------------------------------------------------------------------------+
      | ConcurrentHashMap                                                                            |
      |   table: Node<K,V>[]                       baseCount:long                //threhold&threads  |
      |   nextTable:Node<K,V>[]                    addCount()                    sizeCtl:int         |
      |                                                                          //split table transf|
      |                                                                          transferIndex:int   |
      |   // views                   put()         counterCells;CounterCell[]                        |
      |   keySet;  KeySetView<K,V>   putvalue()    cellsBusy:int                 resizeStamp(n:int)  |
      |   values; ValuesView<K,V>    initTable()   fullAddCount()                transfer()          |
      |   entrySet;EntrySetView<K,V> casTabAt()                                                      |
      |                              helpTransfer()                                                  |
      |                              treeifyBin()                                                    |
      +----------------------------------------------------------------------------------------------+
      |                                                                                              |
      |           TreeBin:Node                       CounterCell           ForwardingNode<K,V>:Node  |
      |             putTreeVal():TreeNode<K,V>           value                 hash:int=MOVE         |
      |             balanceInsertion():TreeNode<K,V>                           nextTable:Node<K,V>[] |
      |                                                                                              |
      +----------------------------------------------------------------------------------------------+
      |                                                               ThreadLocalRandom              |
      |                                                                    localInit()               |
      |                                                                    getProbe():int            |
      |                                                                                              |
      +----------------------------------------------------------------------------------------------+


《Hacker's Delight》
```
优化HashMap兼容并发 
加载因子（0.0~1.0）  0.75f
扩容增量（扩容hash桶）  
     1. 自旋插入
     2. 空table CAS竞争初始容量；竞争失败，自旋等待，hashtable初始化；
     3. 空桶 CAS置换tab位置的Entry ；竞争失败，步骤0自旋插入
     4. MOVED 扩容，帮助扩容
     5. 同步插入，确保锁桶底位置没变，插入操作，判断是否树化
树化
帮助数据迁移：将原来的 tab 数组的元素迁移到新的 nextTab 数组中。在多线程条件下，当前线程检测到其他线程正进行扩容操作（Thread.yield()），则协助其一起进行数据迁移。扩容后  sizeCtl = (n << 1) - (n >>> 1);
### SkipMap

### 阻塞队列
#### LinkedBlockingQueue（newFixedThreadPool）

```
+----------------------------------------------------------------------------------------------+
| LinkedBlockingQueue                                                                          |
|        capacity:int  //                                                                      |
|        count: AtomicInteger                                                                  |
|        head:Node<E>                                                                          |
|        last:Node<E>                                                                          |
|                                                                                              |
|        takeLock:ReentrantLock                                                                |
|        notEmpty:Condition=takeLock.newCondition()                                            |
|        notFull :Condition=takeLock.newCondition()                                            | 
|                                                                                              |
|        take()     offer()//                                                                  |
|        poll()                                                                                |
+----------------------------------------------------------------------------------------------+
|ReentrantLock                      Condition                         Node                     |
|        lockInterruptibly()            awaitNanos()                    item                   |
|        lock()                         signal()                        next:Node              |
|                                                                                              |
+----------------------------------------------------------------------------------------------+

```
#### SynchronousQueue （newCachedThreadPool）

```
      +--------------------------------------------------------------------------------------+
      | SynchronousQueue                                                                     |
      |   transferer:Transferer<E>                              qlock; ReentrantLock         |
      |   take()         offer(E e)                             waitingProducers; WaitQueue  |
      |   poll()                                                waitingConsumers; WaitQueue  |
      +--------------------------------------------------------------------------------------+
      |  TransferQueue:Transferer      TransferStack:Transferer |LifoWaitQueue :WaitQueue    |
      |           head;    QNode          head:SNode            |FifoWaitQueue:WaitQueue     |
      |           tail;    QNode                                |                            |
      |           cleanMe; QNode                                |                            |
      |                      transfer()                         |                            |
      |                      awaitFulfill()                     |                            |
      +----------------------------+----------------------------+                            |
      |  .QNode                    |   .QNode                   |                            |
      |     next;   QNode          |       next; SNode          |                            |
      |     item;  Object          |       match;SNode          |                            |
      |     waiter;Thread          |      waiter;Thread         |                            |
      |     isData;boolean         |        item;Object         |                            |
      |                            |        mode;int            |                            |
      |                            |      //REQUEST,DATA        |                            |
      |                            |        FULFILLING          |                            |
      +----------------------------+----------------------------+----------------------------+
      |       Thread            LockSupport                                                  |
      |          onSpinWait()      park()                                                    |
      |                            unpark()                                                  |
      +---------------------------------------------------------------------------------------



```
#### DelayQueue
#### ArrayBlockingQueue

#### 框架
  - 并发框架 Fork/Join

## 线程
### Timer 调度；吞吐量
单线程，串行
```
+------------------------------------------------------------------------------------------+
|                                        Timer                                             |
|                                          thread: TimerThread                             |
|                                          queue:TaskQueue                                 |
|                                                                                          |
|                                                                                          |
|                                       TaskQueue                  TimerThread:Thread      |
|                                          queue:TimerTask              queue:TaskQueue    |
|                                          size:int                                        |
|                                          isEmpty()                                       |
|                                          wait()                                          |
|                                          removeMin()                                     |
|                                                                                          |
+------------------------------------------------------------------------------------------+

```

### ThreadPoolExecutor 线程池

 

```

源码

      +--------------------------------------------------------------------------------------------+
      |                        ThreadPoolExecutor:AbstractExecutorService                          |
      |                                                                                            |
      |                         corePoolSize:    int                                               |
      |                         maximumPoolSize: int                                               |
      |                         keepAliveTime: long                                                |
      |                         unit: TimeUnit                                                     |
      |                         workQueue: BlockingQueue<Runnable>     workers:HashSet//work thread|
      |                                                                  termination:Condition    |
      |                         ctl :AtomicInteger//state and task count                           |
      |                                                                                            |
      |                         execute(r:Runnable)        beforeExecute()          shutdown()     |
      |                         runWorker( w:Worker)        afterExecute()          shutdownNow()  |
      |                         getTask():Runnable                                                 |
      |                         processWorkerExit()                                                |
      |                                                                                            |
      +-------------------------------------------+------------------------------------------------+
      |Worker:AbstractQueuedSynchronizer,Runnable | BlockingQueue        |RejectedExecutionHandler |
      |                                           |   SynchronousQueue   |   AbortPolicy           |
      |                                           |   LinkedBlockingQueue|   CallerRunsPolicy      |
      |   thread:Thread                           |   DelayQueue         |   DiscardPolicy         |
      |   firstTask : Runnable                    |                      |   DiscardOldestPolicy   |
      |   completedTasks:long                     |   ArrayBlockingQueue |                         |
      |                                           |                      |                         |
      +-------------------------------------------+----------------------+-------------------------+




                         //ThreadPoolExecutor/ScheduledThreadPoolExecutor
                         +--------------------+---------------------------------+
                         | ThreadPoolExecutor |              keepAliveTime      |
                         +--------------------+              corePoolSize       |
                         | RejectedExecutionHandler                             |
   firstTask             |  ^                                                   |
   +-------------+       |  | >maximumPoolSize                                  |
   | Runnable    |       |  |   +----------------+                              |
   +-------------+       |  |   |Worker          | +-------> works:set<Workser> |
   | Callable    | +--------+-> | Thread         |                              |
   +-------------+       |      +----------------+                              |
   | FutureTask  |       +------------------------------------------------------+
   +-------------+                  |
                                    v            +-----------------------------------+
                         +----------+----+       | +--------------+ +--------------+ |
   queue Task            |  ThreadFactory| +---->+ |Thread(Worker)| |Thread(Worker)| |
   +-------------+       +---------------+       | +--------------+ +--------------+ |
   | Runnable    |                               +-----------------------------------+
   +-------------+                                  ^
   | Callable    |                                  | poll()
   +-------------+   offer()    +-------------------+----+   //SynchronousQueue,
   | FutureTask  | +--------->  |workQueue：BlockingQueue |   //LinkedBlockingDeque,
   +-------------+              +------------------------+   //ArrayBlockingQueue





```
 
### 多线程实现
- 风险
  1. 安全(原子性，可见性，有序性)
  2. 跳跃性
  3. 性能（上下文切换，死锁，资源限制）


[JUC 简介](https://www.cnblogs.com/linkworld/p/7819270.html)


[Java 异步编程：从 Future 到 Loom](https://www.jianshu.com/p/5db701a764cb)
线程初始化三种方式： Thread,Runnable,Callable，Feature
线程的生命周期
```
                                       +---------------+
                            +----------+ Block         | <-----------------+   run synchronized
                            |          +---------------+                   |
                            |          +---------------+                   |
                            +----------+ Time_waitting | <-----------------+   sleep(),wait(ms),join(ms)
                            |          +---------------+                   |   LockSupport.parkNanos(),LockSupport.parkUntil()
                            |          +---------------+                   |
                            +----------+ Waitting      | <-----------------+   wait(),join(),LockSupport.park()
                            v          +---------------+                   |
                     +-------------------------------------------------------------+
+---------+          |   +-----------+         system call        +------------+   |      +--------------+
|         |          |   |           | +------------------------> |            |   |      |              |
|  New    | +------> |   |  Ready    |                            |  Running   |   +----> |  Terminated  |
|         |          |   |           |                            |            |   |      |              |
+---------+          |   +-----------+ <------------------------+ +------------+   |      +--------------+
                     |                          yield()                            |
                     +-------------------------------------------------------------+
                                                 Runnable

```

- 多线程
  线程通讯：volatile/synchronized，wait()/nofity()，pipe,join(),ThreadLocal
  线程（实现**异步**）
  线程池（实现**并发**）

- 并发：为了提高效率，减少时间，引入多线程实现并发，同时多线程带来些问题，包括共享变量（内存可见的happens-before原则，避免重排序），锁活跃性问题(死锁,饥饿、活锁) ,性能问题

 

