

## Linux


+-----------------------------------------------------------------------------------------+ 
|                                                                                         |
+-----------------------------------------------------------------------------------------+
|                                                                                         |
|                                                                                         |
|                                             Cached(for Read)    Buffers(for Write)      |
|                                                                                         |
|                                                                                         |
+-----------------------------------------------------------------------------------------+
|                                                                                         |
|                                      VFS                                                |
+-----------------------------------------------------------------------------------------+
|                 I/O Device          File System                                         |
|                                                                                         |
+-----------------------------------------------------------------------------------------+

[PIC](https://zhuanlan.zhihu.com/p/25894102)

[实验课 - 学习操作系统的知识，看哪本书好？ - tobe的呓语的回答 - 知乎](https://www.zhihu.com/question/27871198/answer/1041427202)
[ucore](https://www.bookstack.cn/read/simple_os_book/zh-chapter-3-x86_pages_hardware.md)

## 计算机硬件 - 主板架构
运算器                      控制器                             存储器                                            输入设备                  输出设备
CPU(4% ALU)             南桥北桥芯片(中断控制器)              Register，Cache/SRAM  
                           ROM芯片(BIOS)
                                                     内存卡(DRAM，kernel-cache-memory)    
                                          硬盘(文件系统格式,MBR/GPT,superblock，inode,block)与存储设备               鼠标/键盘/人体学输入设备  
                           网卡(数据链路层MAC控制器,物理层的芯片PHY芯片,网卡缓冲区)                               RJ-45接口   
                           显卡（GPU芯片 40% ALU,显存）                                                        摄像头                    显示器/投影仪
                           声卡(DSP)                                                                           麦克风                   音箱/耳机/扬声器
主板、电源、扩展卡与接口
显卡、键盘、各种控制器等，都有自己的内存
网卡接收数据的过程中,首先网卡发送中断信号告诉CPU来取数据，然后系统从网卡中读取数据存入系统缓冲区中，再下来解析数据然后送入应用层。

CPU Cache就是用来解决CPU与内存之间速度不匹配的问题，避免内存与辅助内存频繁存取数据，这样就提高了系统的执行效率。
[芯片手册](https://zhuanlan.zhihu.com/p/33574137)
[网卡工作原理详解](https://blog.csdn.net/tao546377318/article/details/51602298)
[数据从网卡到应用的过程](https://blog.csdn.net/myle69/article/details/89065432)
《A Heavily Commented Linux kernel Source Code(Kernel 0.11)【Linux 内核 0.11 完全注释】》
《The Art of Linux Kernel Desigin【Linux 内核设计的艺术】》
内存高速缓存buffer-cache(buffer偏重于写，而cache偏重于读)
## 操作系统
"硬中断是外部设备对CPU的中断"
"软中断通常是硬中断服务程序对内核的中断"
"信号则是由内核（或其他进程）对某个进程的中断"
[wiki](https://baike.baidu.com/item/软中断)
[信号和中断的比较 + 中断和异常的比较 ](https://www.cnblogs.com/charlesblc/p/6277810.html)

[Linux中断实现](https://www.cnblogs.com/lifexy/p/7506504.html)
``` shell
 cat /proc/interrupts 
```

BIOS 实模式寻址为1Mbyte， os 打开A20，关闭回滚机制，进入保护模式（保护中断0x00~0x1f） 32bit寻址
os
   进程                                  OS文件系统                                                             shell
  内存管理，调度，进程间通讯               设备驱动，网络设备， inode



《Operating Systems：Design and Implention（Minix）【操作系统设计与实现】》文件系统
《unix操作系统设计》第三章高速缓存
## 设备
字符设备（/dev下，无缓存），块设备（/dev下，无缓存），网络设备（ifconfig -a查看，register_netdev注册）
[何为文件系统，何为根文件系统？ - Xi Yang的回答 - 知乎](https://www.zhihu.com/question/284540952/answer/446736946)

[linux write系统调用如何实现](https://www.cnblogs.com/alantu2018/p/8460365.html)
获取PCI总线
lspci或wmic path win32_pnpentity where "deviceid like '%PCI%'" get name,deviceid

查看中断
```bash
watch -n1 -d cat /proc/interrupts
```
## [以太网帧格式](https://baike.baidu.com/item/%E4%BB%A5%E5%A4%AA%E7%BD%91%E5%B8%A7%E6%A0%BC%E5%BC%8F/10290427)

[Linux 文档](https://wiki.linuxfoundation.org/networking/kernel_flow)

![以太网帧格式](https://bkimg.cdn.bcebos.com/pic/b21bb051f8198618f1ac9f8b4aed2e738ad4e6d2?x-bce-process=image/watermark,g_7,image_d2F0ZXIvYmFpa2U5Mg==,xp_5,yp_5)

[嵌入式Linux——网卡驱动（1）：网卡驱动框架介绍](https://blog.csdn.net/W1107101310/article/details/79616286)
[Network Driver](http://www.tldp.org/LDP/LG/issue93/bhaskaran.html)

[Linux 网络协议栈开发代码分析篇之VLAN（三）—— VLAN收发处理](https://blog.csdn.net/zqixiao_09/article/details/79185510?utm_medium=distribute.pc_relevant.none-task-blog-BlogCommendFromMachineLearnPai2-8.nonecase&depth_1-utm_source=distribute.pc_relevant.none-task-blog-BlogCommendFromMachineLearnPai2-8.nonecase)
## 源码

+----------------------------------------------------------------------------------------+
|   boot                                                                                 |
|                boot.S          setup.S          compressed/Head.S                      |
|                   (A20)          (IDT,GDT)                                             |
|                                  (8259A/PIC)                                           |
|                                                                                        |
|                                                                                        |
|                                                                                        |
|                                                                                        |
+----------------------------------------------------------------------------------------+
|   init                                                                                 |
|                                                                                        |
|               main.c                                                                   |
|                  start_kernel()                                                        |
|                  init()                                                                |
|                                                                                        |
|                                                                                        |
|                                                                                        |
|     trap.c                                                                             |
|       trap_init()           mm/memory.c   buffer.c                                     |
|       set_trap_gate()                        __getblk()                                |
|                                                                                        |
|                                                                                        |
|                                                                                        |
|    irq.c                                                                               |
|    s3c24xx_init_irq()                                                                  |
|                                                                                        |
|    //x86:in/out;arm:ioremap                                                            |
|                                                                                        |
+----------------------------------------------------------------------------------------+


http://blog.chinaunix.net/uid-27177626-id-3438994.html