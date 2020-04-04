

## Linux
## 计算机硬件 - 主板架构
运算器                      控制器                                      存储器                                 输入设备                  输出设备
CPU(4% ALU)                南桥北桥芯片（Register，Cache/SRAM）   
                           ROM芯片(BIOS)
                                                       内存卡(DRAM)、硬盘(MBR/GPT,os 进程-文件-shell)与存储设备        鼠标/键盘/人体学输入设备      
                           网卡(数据链路层MAC控制器,物理层的芯片PHY芯片,网卡缓冲区)                               RJ-45接口   
                           显卡（GPU芯片 40% ALU,显存）                                                        摄像头                    显示器/投影仪
                           声卡(DSP)                                                                           麦克风                   音箱/耳机/扬声器
主板、电源、扩展卡与接口

CPU Cache就是用来解决CPU与内存之间速度不匹配的问题，避免内存与辅助内存频繁存取数据，这样就提高了系统的执行效率。
[网卡工作原理详解](https://blog.csdn.net/tao546377318/article/details/51602298)
[数据从网卡到应用的过程](https://blog.csdn.net/myle69/article/details/89065432)

《A Heavily Commented Linux kernel Source Code(Kernel 0.11)【Linux 内核 0.11 完全注释】》
## 操作系统
os
   进程            文件                                                             shell
                  buffer cache(buffer偏重于写，而cache偏重于读)



《Operating Systems：Design and Implention（Minix）【操作系统设计与实现】》文件系统
《unix操作系统设计》第三章高速缓存

## 源码

+----------------------------------------------------------------------------------------+
|   boot                                                                                 |
|                boot.S          setup.S          compressed/Head.S                      |
+----------------------------------------------------------------------------------------+
|   init                                                                                 |
|                                                                                        |
|               main.c                                                                   |
|                  init()                                                                |
|                  start_kernel()                                                        |
|                                                                                        |
|                                                                                        |
|                                                                                        |
|                                                                                        |
|                                                                                        |
+----------------------------------------------------------------------------------------+
