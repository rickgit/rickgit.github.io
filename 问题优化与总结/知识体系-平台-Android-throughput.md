# 高并发提高吞吐量

## CPU有限，防止抢占，降低吞吐量，不考虑低延时。用于消息传递
[](https://developer.android.google.cn/topic/performance/threads)
每个线程至少需要占用 64k 内存
## Android 多线程
1. main
2. GC时，为了减少应用程序的停顿，会启动四个GC相关的守护线程。
     1. ReferenceQueueDaemon 
     2. FinalizerDaemon 执行Finalizer方法
     3. FinalizerWatchdogDaemon 
        1. 监控FinalizerDaemon线程的执行。检测到执行成员函数finalize时超出一定的时间，那么就会退出VM
     4. HeapTaskDaemon 堆整理线程
        1. for Alloc ： 内存分配的时候
        2. Explicit  ： 显示调用的时候
        3. Background ： 在后台的时候
3. Signal Dispatcher

4. Binder 
     Binder_1 主线程，编号为1，并且主线程是不会退出的。
     Binder_xx 普通线程
     Binder 其他线程：其他线程是指并没有调用 spawnPooledThread 方法，而是直接调用 IPC.joinThreadPool()


## Looper 封装线程


简单工厂  Looper.prepare()
单例      sThreadLocal每个线程一个单例

桥接      将获取下一个消息，移动到MessageQueue#next()
静态代理  MessageQueue#next() 装饰 Message#next
享元      Message#sPool/Message#obtain()

观察者    Looper#sObserver
状态模式  Handler#dispatchMessage
迭代      Message#next
责任      遇到屏蔽消息，处理异步消息

Message类型  :
          屏障消息(postSyncBarrier,target==null)
          异步消息isAsynchronous：可以在屏障消息种被处理
          普通消息按照时间排序和现在时间对比，判断是否调用message回调
最后处理   IdleHandler#queueIdle
  

```java
            +------------------------------------------------------------------------------------------+
            |                           LooperThread                                                   |
            +-------------------------------------------------------------------+----------------------+
            |   Looper                                                          | Handler              |
            |     mQueue:MessageQueue                  prepare(quitAllowed)     |   mQueue:MessageQueue|
            |     mThread:Thread                       loop()                   |                      |
            |                                                                   |                      |
            |     sThreadLocal:ThreadLocal<Looper>()   prepareMainLooper()      |                      |
            |                                          sMainLooper:Looper       |                      |
            +-------------------------------------------------------------------+                      |
            |   MessageQueue                                                    |                      |
            |      mMessages:Message                    postSyncBarrier()       |                      |
            |      mPtr:long                                                    |                      |
            |      mIdleHandlers:ArrayList<IdleHandler>()                       |                      |
            |      mFileDescriptorRecords:SparseArray<FileDescriptorRecord>     |                      |
            |      mPendingIdleHandlers:IdleHandler[]                           |                      |
            |                                                                   |                      |
            |      nativePollOnce() //epoll         enqueueMessage(msg) :boolean|                      |
            |      next():Message //may be block     nativeWake()//pipe         |                      |
            +-------------------------------------------------------------------+                      |
            |   Message                            IdleHandler                  |                      |
            |      mPtr:long                            queueIdle()             |                      |
            |      target:Handler                                               |                      |
            |      callback:Runnable                                            |                      |
            |      recycleUnchecked()                                           |                      |
            |      sendToTarget()                                               |                      |
            |      obtain(h:Handler,callback:Runnable):Message                  |                      |
            +--------------------------------------------------------------------                      |
            |                                                                                          |
            |      dispatchMessage()                                          post(r:Runnable )        |
            |      handleCallback(msg:Message)                                sendMessage(msg:Message) |
            |      mCallback:Callback                                                                  |
            |      handleMessage(msg:Message)                                                          |
            +------------------------------------------------------------------------------------------+
            |                                                                                          |
            |      epoll                                                                               |
            +------------------------------------------------------------------------------------------+
异步消息与同步消息，屏蔽消息（ message.target ==null为屏障消息)）
空闲任务


```
### Handler


```java
     +------------------------------------------+
     |  Handler                                 |
     +------------------------------------------+
     | +---------------+  +---------------------+
+----+ |enqueueMessage |  | dispatchMessage     |
|    | |               |  |   Message#callback  |  <----+
|    | +---------------+  |   mCallback         |       |
|    |                    |   handleMessage()   |       |
|    +--------------------+---------------------+       |
|                                                       |
|    +--------------+                                   |
|    |Looper Thread |                                   |
|    +--------------+----------------------------+      | (runOnLooperThread)
|    |  Looper                                   |      |
|    +-------------------------------------------+      |
|    |   loop()                                  |      |
|    |                                           |      |
|    |  +---------------------+                  | +----+
|    |  | MessageQueue        | epoll            |  +--------------------+
+--> |  |  Message mMessages  +------>           |  | Message            |
     |  |                     |                  |  |  Handler target    |
     |  +---------------------+                  |  |  Runnable callback |
     +-------------------------------------------+  +--------------------+

 

```

### 阻塞唤醒
epoll是linux2.6内核的一个新的系统调用
6.0  kernel/fs/eventpoll.c + kernel/fs/eventfd.c 阻塞唤醒
<6.0 kernel/fs/eventpoll.c + kernel/fs/pipe.c
schedule_timeout 函数，使进程进入休眠，让出CPU
[select/poll/epoll对比分析](http://gityuan.com/2015/12/06/linux_epoll/)
 [源码解读poll/select内核机制](http://gityuan.com/2019/01/05/linux-poll-select/)

[MessageQueue采用epoll](http://gityuan.com/2019/01/06/linux-epoll/)
```bash
ls /proc/<pid>/fd/  //可通过终端执行，看到该fd
```
1. 监视的描述符数量不受限制，所支持的FD上限是最大可以打开文件的数目，具体数目可以
```    
cat /proc/sys/fs/file-max
```
2. epoll不同于select和poll轮询的方式，而是通过每个fd定义的回调函数来实现的

 
进程不会卡住原因：

1. 当没有消息的时候会epoll.wait，等待句柄写的时候再唤醒
2. 屏幕刷新16ms一个消息，句柄写操作，唤醒上文的wait操作


## AsyncTask 封装handle


```
            +------------------------------------------------------------------------------------------+
            |                                                                                          |
            |                            AsyncTask<Params, Progress, Result>                           |
            |                                                                                          |
            |  mHandler:Handler                            sDefaultExecutor:SerialExecutor             |
            |  execute(exec:Executor ,params:Params...)    execute(runnable:Runnable)                  |
            |                                                                                          |
            |  mWorker:WorkerRunnable<Params, Result>  +-----------------------------------------------+
            |  mFuture:FutureTask<Result>              |                                               |
            |                                          | SerialExecutor:Executor                       |
            |  onPreExecute()                          |    mTasks:ArrayDeque<Runnable>()              |
            |  doInBackground(params:Params... )       |    mActive:Runnable                           |
            |   publishProgress(values:Progress... )   |                                               |
            |  onPostExecute(result:Result )           |    scheduleNext()                             |
            |   postResultIfNotInvoked(result:Result)  |                                               |
            |                                          | ArrayDeque  ThreadPoolExecutor                |
            +------------------------------------------+   offer()      CORE_POOL_SIZE                 |
            |                                          |   poll()       MAXIMUM_POOL_SIZE              |
            |                                          |                KEEP_ALIVE_SECONDS:3           |
            |  WorkerRunnable<Params, Result>:Callable |                TimeUnit.SECONDS,              |
            |       mParams:Params[]                   |                sPoolWorkQueue:                |
            |                                          |             LinkedBlockingQueue<Runnable>(128)|
            |                                          |                 sThreadFactory:ThreadFactory  |
            |  Handler                                 |                                               |
            |      obtainMessage()                     |             Runtime.getRuntime()              |
            |                                          |                .availableProcessors()         |
            |                                          |                                               |
            +------------------------------------------+-----------------------------------------------+


```


```java

 
                    +--------------------------------------------+  MESSAGE_POST_RESULT
                    |   AsyncTask                                |  MESSAGE_POST_PROGRESS
                    |                                            |              +-----------------------------+
                    |   +----------------------------+           |              |   MainLooper                |
way 1   +---------> |   | FutureTask(WorkerRunnable) |           |        +---> |                             |
                    |   +----------+-----------------+           |        |     |                             |
                    |              |                             |        |     +-----------------------------+
                    |              |     +-----------------------+        |
                    |              |     |Handle:InternalHandler | -------+
                    |              |     |                       |
                    |              |     +-----------------------+  <-----------------------------------------+
                    |              |                             |                                            |
                    +--------------------------------------------+                                            |
                           mStatus |                                                                          |
                           +-------+                                                                          |
                                   |                                                                          |
                           offer() v                                                                          |
 way 2                                                                  +--------------------+------------+   |
+--------------+           +----------------------------+               | ThreadPoolExecutor |            |   |
| Runnable     +------->   | SerialExecutor|            |   ArrayDeque  +--------------------+            |   |
+--------------+           +---------------+            |   #poll()     | LinkedBlockingQueue             |   |
+--------------+   offfer()|          mTasks:ArrayDeque |  +--------->  |    +-----------+ +------------+ |   |
| Runable      +-------->  |                            |               |    |FeueureTask| | FutureTask | +---+
+--------------+           +----------------------------+               |    +-----------+ +------------+ |
                                                                        +---------------------------------+
        
    容器类：ArrayDeque，LinkedBlockingQueue（ThreadPoolExecutor的线程队列）
    并发类：ThreadPoolExecutor（包含 ThreadFactory属性，用于创建线程），AtomicBoolean，AtomicInteger，FutureTask(包含Callable属性，任务执行的时候调用Callable#call,执行AsyncTask#dobackgroud)

```


## HandlerThread:Thread


```
            +------------------------------------------------------------------------------------------+
            |                                     HandlerThread :Thread                                |
            |                                             mHandler:Handler                             |
            |                                             mLooper:Looper                               |
            |                                                                                          |
            +------------------------------------------------------------------------------------------+

```


```

          +------------------------------------------------------------------------------------------+
          |                                     HandlerThread :Thread                                |
          |                                             mHandler:Handler                             |
          |                                             mLooper:Looper                               | 
          +------------------------------------------------------------------------------------------+
          |                                     IntentService:Service                                |
          |                                           mSer^iceLooper:Looper                          |
          |                                           mServiceHandler:ServiceHandler                 |
          |                                                                                          |
          |                                           onStart()                                      |
          |                                           onHandleIntent()                               |
          |                                           stopSelf()                                     |
          +------------------------------------------------------------------------------------------+
          |                                     ServiceHandler:Handler                               |
          |                                          sendMessage(msg:Message)                        |
          |                                                                                          |
          +------------------------------------------------------------------------------------------+

```

## RxJava
### [Rxjava ](./知识体系-理论-OOAD-Observer.md)
Rxjava 
工厂方法 构建Observer，通过Observable，Single，Maybe，Completable构建**io.reactivex.rxjava3.internal.operators包**内的 Observable 对象
模板模式
      public interface Observer<@NonNull T> { 
          void onSubscribe(@NonNull Disposable d); 
          void onNext(@NonNull T t); 
          void onError(@NonNull Throwable e); 
          void onComplete();

      }

      public interface Subscriber<T> { 
          public void onSubscribe(Subscription s); 
          public void onNext(T t); 
          public void onError(Throwable t); 
          public void onComplete();
      }

代理模式 onSubscribe()代理subscribeActual()方法，subscribeActual(Observer<? super T> observer)调用的时候，用装饰模式装饰当前的observer
装饰模式 Observer装饰下游 Observer的 subscribe() 方法，装饰调度器

RxJava2.0是非常好用的一个异步链式库,响应式编程，遵循观察者模式。
```
  001_Jersey              697fd66aae9beed107e13f49a741455f1d9d8dd9 Initial commit, working with Maven Central
* 002_rx                  87cfa7a445f7659ef46d1a6a4eb38daa46f5c97a Initial commit
  003_Observer_Observable 2a4122c11b95eaa3213c2c3e54a93d28b9231eec Rename to Observer/Observable
  004_refactoring         9d48f996e4ee55e89dc3c60d9dd7a8d644316140 Refactoring for consistent implementation approach.
                         Observable静态代理，Observer适配器模式，Func通过桥接Observer的具体实现，operations为Observable的子类
                        +----------------------------------------------------------------------------+ 
                        |AtomicObserverSingleThreaded AtomicObserverMultiThreaded                    |decorate pattern
                        |                      AtomicObserver                                        |state pattern
                        |  ToObservableIterable                                                      |
                        +----------------------------------------------------------------------------+ 
                        |Observable<T>           Observer<T>       Subscription                      |
                        |  subscribe(Observer<T>)   onCompleted()      unsubscribe()                 |
                        |                            onError()                        Func0<R>       |
                        |                            onNext(T)     Notification<T>     call():R      | 
                        +----------------------------------------------------------------------------+
                        |                     reactive                           functions operations| 
                        +----------------------------------------------------------------------------+
  005_languageAdapter     f57a242b17f1214142dea97c0cb9049b106378a0 LanguageAdaptor for Function execution
  006_readme              fbbac394fbc2e0af4ef3a507ad3e15dd18bfb10c Create README.md
  007_gh-pages            6797c4384d91a2567cf0b429ccbd8053c72b716f Create gh-pages branch via GitHub
  008_performace          787d8fc0215c5bab541f61c2d69a91753d462559 Refactoring towards performance improvements
                          gradle
  009_example             b61b7607e0f2b00a0d36672a95999f1fb081dbf1 Start of examples with clojure and groovy
  010_readme              10fe96474a6ab4ec62c7cab50fb376a173bda78e Create README.md
  012_0.1.2               74da0769266a8fd5832e558f1a6e0081895b9201 Gradle Release Plugin - pre tag commit:  'rxjava-0.1.2'.
  013_takeLast                      4c5c41364411e062d5d71f22ce311700d045821b TakeLast basic implementation
  014_RxJavaPlugins                 ee001549ef06f17f38139b4cfecd4ce4445ecb6d RxJavaErrorHandler Plugin
  015_operationNext                 d72c5892774a2c67327e07943f6b92416317871f Implemented Operation Next
  062_schedulers                    dfc784126f259361f01f1927f44f5d1aa4e49a43 Naive schedulers implementation
  063_RxJavaObservableExecutionHook c2a40bd1a391edf7f8b71965ca20fa84c72c0bb4 RxJavaObservableExecutionHook
  064_multicast                     0499cffcb53f928a5083f701603ccf3ec3c81c60 Multicast implemented
  065_sample                        1aa722d3379df88d05c9455d7630b7236edb9d9b Merge pull request #248 from jmhofer/sample
  066_throttle                      2ea065c0ef22ea7cf58e9fb6d6f24c69f365bed6 Created and wired an implementation for the throttle operation on Observables.
  067_AndroidSchedulers             3919547f1e5f7940974e383f4f573e48cac7e09b Expose main thread scheduler through AndroidSchedulers class
  068_window                        5789894638a62ac17b5276053e3bea8bdd570580 Merge window operator commit to master
  069_debound_throttleWithTimeout   5fabd5883561ff18b18b0d1dfb7001e2959cb11d Use 'debounce' as proper name for ThrottleWithTimeout which unfortunately is the poorly named Rx Throttle operator.
  070_ApachHttpAsyncClient          db2e08ca039c59d51349255c4b8b3c65b26d52de Observable API for Apache HttpAsyncClient 4.0
  071_AndroidObservables            715dcece5c781c394b59f86e32f1f514fc9f7a31 Drop use of WeakReferences and use a custom subscription instead
  072_backwards                     abff40fd0a40bee4f97b0363014e98aecb50d7ff Backwards compatible deprecated rx.concurrency classes
  073_single                        96064c37af520de375929ae8962c527dd869ad57 Implement the blocking/non-blocking single, singleOrDefault, first, firstOrDefault, last, lastOrDefault
  074_subscribeOn                   89bb9dbdf7e73c8238dc4a92c8281e8ca3a5ec53 Reimplement 'subscribeOn' using 'lift'
  075_observeOn                     9e2691729d94f00cde97efb0b39264c2f0c0b7f5 ObserveOn Merge from @akarnokd:OperatorRepeat2
  076_RxJavaSchedulers              d07d9367911d8ec3d0b65846c8707e0a41d1cf1f RxJavaSchedulers Plugin
  077_math-module                   68d40628a78db18ecb49d880798f1a03551ccd59 math-module
  078_operatorSerialize             4427d03db0d8ba67137654447c6c7a57615c0b00 OperatorSerialize
* 079_contrib-math                  26a4a1a05a6cc367061639fb19bfdda9d5b97fab Operators of contrib-math
  100_1.0.0-rc.1          6de88d2a39e2ae84744cd1f350e28bef4de7dacb Travis build image
                        observeOn 切换了消费者的线程，因此内部实现用队列存储事件。
                        +-----------------------------------------------------------------------+-------------------+
                        |AtomicObserverSingleThreaded AtomicObserverMultiThreaded               |                   |
                        |                      AtomicObserver                                   |                   |
                        |  ToObser^ableIterable                                                 |OperatorSubscribeOn|
                        +-----------------------------------------------------------------------+                   |
                        |ObservablevTv           ObservervTv       Subscription                 |OperatorObserveOn  |
                        |  subscribe(Observer<T>)    onCompleted()     unsubscribe()            |                   |
                        |  observeOn(Scheduler)      onError()                        Func0<R>  +-------------------+
                        |                            onNext(T)     Notification<T>     call():R |      schedulers   |
                        |                                                                       |                   |
                        +-------------------------------------------------------------------------------------------+
                        |                     reactive                                          |      plugins      |
                        +-----------------------------------------------------------------------+-------------------+
                        |                                         functions operations                              |
                        +-------------------------------------------------------------------------------------------+

  200_v2.0.0-RC1          fa565cb184d9d7d45c257afa1fbbec6ab488b1cf Update changes.md and readme.md
                        Disposable装饰Observer，使得Observer可以销毁。ObservableSource,CompletableSource， MaybeSource<T>,SingleSource<T>,FlowableProcessor。
                        io.reactivex.Flowable：发送0个N个的数据，支持Reactive-Streams和背压。Flowable.subscribe(4 args)
                        io.reactivex.Observable：发送0个N个的数据，Rx 2.0 中，不支持背压，Observable.subscribe(4 args)
                        io.reactivex.Single：只能发送单个数据或者一个错误，不支持背压。 Single.toCompletable()
                        io.reactivex.Completable：没有发送任何数据，但只处理 onComplete 和 onError 事件，不支持背压。 Completable.blockingGet()
                        io.reactivex.Maybe：能够发射0或者1个数据，要么成功，要么失败，不支持背压。Maybe.toSingle(T)
                        RxJavaPlugins 转化Obserable.
                        +----------------------------------------------------------------------------+---------------+-------------------+
                        |  FromArrayDisposable                                                       |               |                   |
                        |  ObservableFromArray                                                       |               |                   |
                        +----------------------------------------------------------------------------+               |                   |
                        |                             RxJavaPlugins                                  |               |                   |
                        |                                      apply(Function<T, R> f, T t)          |               |                   |
                        +----------------------------------------------------------------------------+               |                   |
                        |ObservablevTv:ObservableSource          ObservervTv           Disposable    |               |OperatorSubscribeOn|
                        |             subscribe(Observer<T>)         onCompleted()       dispose()   |               |                   |
                        |   subscribeWith(Observer<T>)               onError()           isDisposed()|Function<T, R> |OperatorObserveOn  |
                        |   subscribeActual(ObservervT>)             onNext(T)                       | apply(T):R    |                   |
                        |                                            onSubscribe(Disposable)         |               +-------------------+
                        |                                                                            +---------------+      schedulers   |
                        |                             RxJavaPlugins                                  |   functions   |                   |
                        +----------------------------------------------------------------------------+-----------------------------------+
                        |                                         functions operations                               |      plugins      |
                        +----------------------------------------------------------------------------------------------------------------+
                        |                     reactive-streams      jmh                                              |                   |
                        +--------------------------------------------------------------------------------------------+-------------------+

* 300_v3.0.0-RC0          fb37226be292c8ee0934311f8ca2f139dfd0dc5a 3.x: remove no-arg, dematerialize(); remove replay(Scheduler) variants (#6539)
                        io.reactivex.rxjava3




[reactivestreams](https://github.com/reactive-streams/reactive-streams-jvm.git)
  001_scala           e58fed62249ad6fbd36467d1bbe5c486f31a8c0e Initial implementation
  020_java            6e5c63f3492524c21fda0bc1a08d72b52c9e9692 Reimplements the SPI and API in Java
  021_tck_java        3e67969207994b8e34ff49dffbf9fe3ac2d51284 !tck #12 Migrated TCK to plain Java
  022_api_spi_example f3a43863bd7c370a1f292a456eddcd1e9d226738 API/SPI Combination and Examples
* 101_gradle          dddbd3a52fcb7d71059a30760b5e77127c785c7c fix #96
                        +-------------------------------------------------------------------+--------+
                        |                                                                   |        |
                        |        Processor<T, R> :Subscriber<T>, Publisher<R>               |        |
                        +-------------------------------------------------------------------+        |
                        |                                                                   |        |
                        |  Publisher<T>                Subscriber<T>          Subscription  |        |
                        |     subscribe(Subscriber<T>)    onSubscribe()           cancel()  |        |
                        |                                 onNext(T)                         |        |
                        |                                 onComplete()                      |        |
                        |                                 onError(Throwable)                |        |
                        +-------------------------------------------------------------------+        |
                        |                       api                                         |  tck   |
                        +-------------------------------------------------------------------+--------+

```

```

+--------------------------+        +----------------------------+       +------------------------------+
|                          |        |                            |       |                              |
|     create layer         |        |     subscribe call layer   |       |    execute call layer        |
|                          |        |                            |       | (subscriber/observer method) |
+--------------------------+        +----------------------------+       +------------------------------+
|                          |        |                            |       |                              |
|    +---------------+     |        |    +-------------------+   |       |     +---------------------+  |
|    |    create()   |     |        |    |                   |   |       |     |                     |  |
|    |     just()    |     |        |    |subscribe(observer)|---------------> |   call()            |  |
|    |     from()    |     |        |    |                   |   |       |     |                     |  |
|    +-------+-------+     |        |    +-------------------+   |       |     +---------+-----------+  |
|            |             |        |                            |       |               |              |
|            |             |        |             ^              |       |               v              |
|            v             |        |             |              |       |                              |
|                          |        |             |              |       |     +---------------------+  |
|    +---------------+     |        |    +--------+----------+   |       |     |                     |  |
|    | map<Obj,Str>()|     |        |    |                   |   |       |     |   onNext()          |  |
|    | flatMap ()    |     |        |    |subscribe(observer)|   |       |     |                     |  |
|    |  compose()    |     |        |    |                   |   |       |     +----------+----------+  |
|    +-------+-------+     |        |    +-------------------+   |       |                |             |
|            |             |        |                            |       |                v             |
|            v             |        |             ^              |       |                              |
|                          |        |             |              |       |                              |
|    +---------------+     |        |    +--------+----------+   |       |     +---------------------+  |
|    |               |     |        |    |                   |   |       |     |                     |  |
|    |   observeOn() | +---------------> |subscribe(observer)|   |       |     |   switch thread     |  |
|    | subscribeOn(  |     |        |    |                   |   |       |     |                     |  |
|    |   Schedulers) |     |        |    +-------------------+   |       |     +---------------------+  |
|    +---------------+     |        |                            |       |                              |
|                          |        |                            |       |                              |
+--------------------------+        +----------------------------+       +------------------------------+

```

需要有订阅，Observable才会执行

背压策略：即生产者的速度大于消费者的速度带来的问题，比如在Android中常见的点击事件，点击过快则经常会造成点击两次的效果。
Flowable背压策略
```
onBackpressureBuffer()
onBackpressureDrop()
onBackpressureLatest() 

```
[Rxjava Operater](https://github.com/ReactiveX/RxJava/wiki/Alphabetical-List-of-Observable-Operators)
```
+----------------------------------+--------------------------------------+----------------+-----------------------+
|                                  |                                      |                |                       |
|deferFuture( )    runAsync( )     | getIterator( )                       |                |                       |
|                                  | toIterable( )      mostRecent( )     |                | stringConcat( )       |
|startFuture( )    fromRunnable( ) |  toFuture( )       lastOrDefault( )  |                |                       |
|                                  | singleOrDefault( ) last( )           |                | join( )   split( )    |
|toAsync( )        fromCallable( ) |  single( )         firstOrDefault( ) |                |                       |
|or asyncAction( )                 |                    first( )          |                | encode( )   from( )   |
|or asyncFunc( )   fromAction( )   |  latest( )                           | Flowable       |                       |
|                                  |                    forEach( )        |                | decode( )   byLine( ) |
|start( )          forEachFuture( )|   next( )                            |                |                       |
+------------------------------------------------------------------------------------------------------------------+
|        Async                     |             Blocking Observable      | Parallel flows |    String             |
+----------------------------------+--------------------------------------+----------------+-----------------------+


```
### Rxjava Scheduler / Worker / Runnable 
```
+-----------------+---------------------------------------------------------------------------+
|                 |                             Schedulers                                    |
+--------------------------------------+------------------------------------------------------+
|                 |computation() |io() | trampoline() |newThread()|  single() |from(Executor) |
+---------------------------------------------------------------------------------------------+
|  corePoolSize   |              |1    |              |           |           |               |
+---------------------------------------------------------------------------------------------+
|  maximumPoolSize|              |     |              |           |           |               |
+---------------------------------------------------------------------------------------------+
|  keepAliveTime  |              |     |              |           |           |               |
|                 |              |     |              |           |           |               |
+---------------------------------------------------------------------------------------------+
|  unit           |              |     |              |           |           |               |
+---------------------------------------------------------------------------------------------+
|  workQueue      |              |     |              |           |           |               |
+-----------------+--------------+-----+--------------+-----------+-----------+---------------+



                    RxThreadFactory :ThreadFactory
                         Thread newThread()


 //RxComputationThreadPool          //RxSchedulerPurge
ComputationScheduler :Scheduler     SchedulerPoolFactory


 //RxCachedThreadScheduler
 //RxCachedWorkerPoolEvictor
 IoScheduler :Scheduler
                                                    CachedWorkerPool                                
 //RxNewThreadScheduler                                   evictorService:ScheduledExecutorService
 NewThreadScheduler :Scheduler                            allWorkers:CompositeDisposable
                                                          createWorker():Worker
 //RxSingleScheduler                                      expiringWorkerQueue:ConcurrentLinkedQueue<ThreadWorker> 
 SingleScheduler :Scheduler                         ThreadWorker: NewThreadWorker

                                                    EventLoopWorker:Scheduler.Worker//访问者模式，访问threadworker
                                                          threadWorker:ThreadWorker
                                                          schedule(run:Runnable):Disposable

                                                    ScheduledRunnable :AtomicReferenceArray,Runnable


FlowableSubscribeOn
       scheduler:Scheduler
```
线程间切换
### 背压
 
MISSING（配合onBackPressure，达到后面四种效果）、BUFFER（接收性能比Observable低）、ERROR、DROP、LATEST

### 搜索业务
 
```java
        mSubscribe = Observable.unsafeCreate((Observable.OnSubscribe<String>) subscriber -> mSearch = subscriber)
                .debounce(400, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                .subscribeOn(AndroidSchedulers.mainThread())
                .filter(key -> key.toString().trim().length() > 0)
                .switchMap((Func1<String, Observable<List<Result>>>) key -> searchObservable(key))//避免每次搜索都要重新设置监听
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(results -> {
                    mSearchListener.onSearchComplete();
                }, throwable -> throwable.printStackTrace());

       mSearch.onNext(key);         
```