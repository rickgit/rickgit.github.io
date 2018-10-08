## Hanlder 异步消息处理机制

- A Handler allows you to send and process {@link Message} and Runnable objects associated with a thread's {@link MessageQueue}.Each Handler instance is associated with a single thread and that thread's message queue.  When you create a new Handler, it is bound to the thread / message queue of the thread that is creating it -- from that point on, it will deliver messages and runnables to that message queue and execute them as they come out of the message queue.
（功能，类结构，类初始化，分类，类方法，特殊的主线程）进行描述

- a message containing a description and arbitrary data object that can be sent to a {@link Handler}.  This object contains two extra int fields and an extra object field that allow you to not do allocations in many cases.
While the constructor of Message is public, the best way to get one of these is to call {@link #obtain Message.obtain()} or one of the {@link Handler#obtainMessage Handler.obtainMessage()} methods, which will pull them from a pool of recycled objects. 
（类结构，类初始化）进行描述

- MessageQueue is Low-level class holding the list of messages to be dispatched by a {@link Looper}.  Messages are not added directly to a MessageQueue, but rather through {@link Handler} objects associated with the Looper. You can retrieve the MessageQueue for the current thread with {@link Looper#myQueue() Looper.myQueue()}.
  （类结构，类管理，初始化）

- A <i>thread</i> is a thread of execution in a program. The Java Virtual Machine allows an application to have multiple threads of execution running concurrently.


- Looper  used to run a message loop for a thread.  Threads by default do not have a message loop associated with them; to create one, call {@link #prepare} in the thread that is to run the loop, and then {@link #loop} to have it process messages until the loop is stopped.
  （功能，方法）进行描述


### 类图
![类图](./handler_class_diagram.svg)


### 主线程Looper初始化

1. SystemServer#prepareMainLooper 系统应用的主线程
   SystemServer#main调用
2. ActivityThread#main 用户应用的主线程
   ActivitymanagerService#startProcessLocked() 反射调用