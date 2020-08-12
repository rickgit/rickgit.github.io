## CPU有限，防止抢占，降低吞吐量，不考虑低延时。用于消息传递


## Looper 封装线程

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

6.0 eventfile+epoll阻塞唤醒
<6.0 epoll+pipe
```


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