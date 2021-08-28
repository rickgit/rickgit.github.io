# 高并发提高吞吐量

adb shell "ps | grep -E 'zygote|PID'"
adb shell "ps -T -p <pid>"
adb shell "top -H -p <pid>"
```java
android 线程

main                                     	 RUNNABLE 	 5 	 Normal
ReferenceQueueDaemon                     	 WAITING 	   5 	 Daemon
Signal Catcher                           	 WAITING 	   5 	 Daemon
FinalizerDaemon                          	 WAITING 	   5 	 Daemon //java.lang.Daemons.FinalizerDaemon
FinalizerWatchdogDaemon                  	 WAITING 	   5 	 Daemon //java.lang.Daemons.FinalizerDaemon
Binder:8901_2                            	 RUNNABLE 	 5 	 Normal
Binder:8901_1                            	 RUNNABLE 	 5 	 Normal
Binder:8901_4                            	 RUNNABLE 	 5 	 Normal
Binder:8901_3                            	 RUNNABLE 	 5 	 Normal
HeapTaskDaemon                           	 WAITING 	   5 	 Daemon
Profile Saver                            	 RUNNABLE 	 5 	 Daemon
Jit thread pool worker thread 0          	 RUNNABLE 	 5 	 Daemon

```

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
     Binder_xx:xx 普通线程
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

#### HandlerThread结合Handler#callback的用法
```java
mediaSourceHandler = Util.createHandler(mediaSourceThread.getLooper(), /* callback= */ this);
```