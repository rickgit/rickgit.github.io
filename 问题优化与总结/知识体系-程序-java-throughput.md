## 提高吞吐量 

## SynChronized
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

## Timer 调度；吞吐量
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

## 线程池

 

```
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
 
## 多线程实现
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

 

### 并发底层实现
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

#### volatile 与 CAS
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

#### volatile 内存模型
 volatile 内存语义， 重排序,顺序一致性
 内存可见性、volatile锁
 final 内存语义，读写重排序规则
 hanpen before，指两个操作指令的执行顺序

 可以保证变量的可见性 ，不能保证变量状态的“原子性操作”，原子性操作需要lock或cas
#### 原子操作类：采用 volatile和[Unsafe#Unsafe_CompareAndSwapInt](/home/anshu/workspace/openjdk/hotspot/src/share/vm/prims)的CAS原子操作
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

```

java.util.concurrent.atomic.AtomicStampedReference object internals:
 OFFSET  SIZE                                                      TYPE DESCRIPTION                               VALUE
      0     4                                                           (object header)                           05 00 00 00 (00000101 00000000 00000000 00000000) (5)
      4     4                                                           (object header)                           00 00 00 00 (00000000 00000000 00000000 00000000) (0)
      8     4   java.util.concurrent.atomic.AtomicStampedReference.Pair AtomicStampedReference.pair               (object)  //volatile修饰
     12     4                                                           (loss due to the next object alignment)
Instance size: 16 bytes
Space losses: 0 bytes internal + 4 bytes external = 4 bytes total

java.util.concurrent.atomic.AtomicStampedReference$Pair object internals:
 OFFSET  SIZE               TYPE DESCRIPTION                               VALUE
      0     4                    (object header)                           01 00 00 00 (00000001 00000000 00000000 00000000) (1)
      4     4                    (object header)                           00 00 00 00 (00000000 00000000 00000000 00000000) (0)
      8     4                int Pair.stamp                                0  //final修饰
     12     4   java.lang.Object Pair.reference                            0  //final修饰
     16     8                    (loss due to the next object alignment)
Instance size: 24 bytes
Space losses: 0 bytes internal + 8 bytes external = 8 bytes total
```

```
java.util.concurrent.atomic.AtomicReference object internals:
 OFFSET  SIZE               TYPE DESCRIPTION                               VALUE
      0     4                    (object header)                           01 00 00 00 (00000001 00000000 00000000 00000000) (1)
      4     4                    (object header)                           00 00 00 00 (00000000 00000000 00000000 00000000) (0)
      8     4   java.lang.Object AtomicReference.value                     null //volatile修饰
     12     4                    (loss due to the next object alignment)
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
  return (jint)(Atomic::cmpxchg(x, addr, e)) == e;//只需要关注到这。将主存的副本和线程的新值传进去，后续cpu进行CAS操作。查看是否把主存原来的值置换出来，新增的值置换到主存
UNSAFE_END

```
  - 乐观锁思想-CAS原子操作。修改前，对比直到共享内存和当前值（当前线程临时内存）一致，才做修改，这个流程不会加锁阻塞（《Java 并发编程的艺术》2.3节）
      - AtomicStampedReference来解决ABA问题；
      - 循环时间长开销大
      - AtomicReference类来多个共享变量合成一个共享变量来操作



#### AQS 实现 **乐观锁**
```java
public abstract class AbstractQueuedSynchronizer{
    /**
     * Head of the wait queue, lazily initialized.  Except for
     * initialization, it is modified only via method setHead.  Note:
     * If head exists, its waitStatus is guaranteed not to be
     * CANCELLED.
     */
    private transient volatile Node head;

    /**
     * Tail of the wait queue, lazily initialized.  Modified only via
     * method enq to add new wait node.
     */
    private transient volatile Node tail;

    /**
     * The synchronization state.
     */
    private volatile int state;
}
```

```
1. 独占式获取与释放同步状态
2. 共享式获取与释放同步状态
3. 查询同步队列中的等待线程情况
```

**重入锁** ReentrantLock（**独享锁**,**互斥锁**）/ReentrantReadWriteLock（**读锁是共享锁，其写锁是独享锁**）/StampedLock （内部通过 AbstractQueuedSynchronizer同步器，实现**公平锁和非公平锁**，AbstractQueuedSynchronizer包含Condition，使用volatile修饰的state变量维护同步状态），解决复杂锁问题，如先获得锁A，然后再获取锁B，当获取锁B后释放锁A同时获取锁C，当锁C获取后，再释放锁B同时获取锁D。


CountDownLatch，CyclicBarrier，Semaphore，Exchanger （这类使用AQS）
- ReentrantLock: 使用了AQS的独占获取和释放,用state变量记录某个线程获取独占锁的次数,获取锁时+1，释放锁时-1，在获取时会校验线程是否可以获取锁。
- Semaphore: 使用了AQS的共享获取和释放，用state变量作为计数器，只有在大于0时允许线程进入。获取锁时-1，释放锁时+1。
- CountDownLatch: 使用了AQS的共享获取和释放，用state变量作为计数器，在初始化时指定。只要state还大于0，获取共享锁会因为失败而阻塞，直到计数器的值为0时，共享锁才允许获取，所有等待线程会被逐一唤醒

```java
java.util.concurrent.locks.ReentrantLock object internals:
 OFFSET  SIZE                                            TYPE DESCRIPTION                               VALUE
      0     4                                                 (object header)                           01 00 00 00 (00000001 00000000 00000000 00000000) (1)
      4     4                                                 (object header)                           00 00 00 00 (00000000 00000000 00000000 00000000) (0)
      8     4   java.util.concurrent.locks.ReentrantLock.Sync ReentrantLock.sync                        (object)  //AQS子类
     12     4                                                 (loss due to the next object alignment)
Instance size: 16 bytes
Space losses: 0 bytes internal + 4 bytes external = 4 bytes total

java.util.concurrent.locks.ReentrantLock$NonfairSync object internals://继承AQS，没有加字段，只是重写lock()，tryRequire()方法
 OFFSET  SIZE                                                         TYPE DESCRIPTION                                        VALUE
      0     4                                                              (object header)                                    01 00 00 00 (00000001 00000000 00000000 00000000) (1)
      4     4                                                              (object header)                                    00 00 00 00 (00000000 00000000 00000000 00000000) (0)
      8     4                                             java.lang.Thread AbstractOwnableSynchronizer.exclusiveOwnerThread   null //竞争到的线程
     12     4                                                          int AbstractQueuedSynchronizer.state                   0     // volatile 修饰符
     16     4   java.util.concurrent.locks.AbstractQueuedSynchronizer.Node AbstractQueuedSynchronizer.head                    null
     20     4   java.util.concurrent.locks.AbstractQueuedSynchronizer.Node AbstractQueuedSynchronizer.tail                    null
     24     8                                                              (loss due to the next object alignment)
Instance size: 32 bytes
Space losses: 0 bytes internal + 8 bytes external = 8 bytes total




java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject object internals://new ReentrantLock().newCondition()
 OFFSET  SIZE                                                         TYPE DESCRIPTION                               VALUE
      0     4                                                              (object header)                           05 00 00 00 (00000101 00000000 00000000 00000000) (5)
      4     4                                                              (object header)                           00 00 00 00 (00000000 00000000 00000000 00000000) (0)
      8     4   java.util.concurrent.locks.AbstractQueuedSynchronizer.Node ConditionObject.firstWaiter               null
     12     4   java.util.concurrent.locks.AbstractQueuedSynchronizer.Node ConditionObject.lastWaiter                null
     16     4        java.util.concurrent.locks.AbstractQueuedSynchronizer ConditionObject.this$0                    (object)
     20     4                                                              (loss due to the next object alignment)
Instance size: 24 bytes
Space losses: 0 bytes internal + 4 bytes external = 4 bytes total



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

- 生产者与消费者。Condition配合ReentrantLock，实现了wait()/notify()（阻塞与通知）



#### 并发集合 (ArrayBlockingQueue,LinkedBlockingQueue)，Fork/Join框架，工具类
  - ConcurrentHashMap。有并发要求，使用该类替换HashTable
java 7 **分段锁**技术,java 8 摒弃了Segment（锁段）的概念，采用CAS + synchronized保证并发更新的安全性，底层采用数组+链表+红黑树的存储结构。
```
java.util.concurrent.ConcurrentHashMap object internals:
 OFFSET  SIZE                                                   TYPE DESCRIPTION                               VALUE
      0     4                                                        (object header)                           05 00 00 00 (00000101 00000000 00000000 00000000) (5)
      4     4                                                        (object header)                           00 00 00 00 (00000000 00000000 00000000 00000000) (0)
      8     4                                          java.util.Set AbstractMap.keySet                        null
     12     4                                   java.util.Collection AbstractMap.values                        null
     16     8                                                   long ConcurrentHashMap.baseCount               0
     24     4                                                    int ConcurrentHashMap.sizeCtl                 0 //volatile 并发利用CAS算法
     28     4                                                    int ConcurrentHashMap.transferIndex           0
     32     4                                                    int ConcurrentHashMap.cellsBusy               0
     36     4          java.util.concurrent.ConcurrentHashMap.Node[] ConcurrentHashMap.table                   null
     40     4          java.util.concurrent.ConcurrentHashMap.Node[] ConcurrentHashMap.nextTable               null//用于迁移到table属性的临时属性
     44     4   java.util.concurrent.ConcurrentHashMap.CounterCell[] ConcurrentHashMap.counterCells            null//用于并行计算每个bucket的元素数量。
     48     4      java.util.concurrent.ConcurrentHashMap.KeySetView ConcurrentHashMap.keySet                  null
     52     4      java.util.concurrent.ConcurrentHashMap.ValuesView ConcurrentHashMap.values                  null
     56     4    java.util.concurrent.ConcurrentHashMap.EntrySetView ConcurrentHashMap.entrySet                null
     60     4                                                        (loss due to the next object alignment)
Instance size: 64 bytes
Space losses: 0 bytes internal + 4 bytes external = 4 bytes total

```
桶初始容量  16
加载因子（0.0~1.0）  0.75f
扩容增量（扩容hash桶）  增加一倍
树化
帮助数据迁移：将原来的 tab 数组的元素迁移到新的 nextTab 数组中。在多线程条件下，当前线程检测到其他线程正进行扩容操作（Thread.yield()），则协助其一起进行数据迁移。扩容后  sizeCtl = (n << 1) - (n >>> 1);



  - 并发框架 Fork/Join
