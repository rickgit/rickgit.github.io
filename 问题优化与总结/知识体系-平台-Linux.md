

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

### 安装
1. 开机慢
sudo gedit /etc/default/grub 
修改 GRUB_CMDLINE_LINUX_DEFAULT="quiet splash" 
为 GRUB_CMDLINE_LINUX_DEFAULT=""
为了 查看开关机后台服务

sudo gedit /etc/systemd/system.conf
修改为
DefaultTimeoutStartSec=3s
DefaultTimeoutStopSec=3s
重新加载
systemctl daemon-reload

2. 识别固态硬盘
dell bios选项里面，SATA 修改为 AHCI模式
[磁盘工具](https://cloud.tencent.com/developer/article/1389853)
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


## 内存管理

```

                       page table                   物理地址 
+----------+      +---------+---------+         +----------+
| logic    |      |  page   | page    |         |          |
| address  +----> |         | frame   |   +---> | memory   |
+----------+      +-------------------+         |          |
| 28bit    |      |         |         |         |          |
|          |      |         |         |         |          |
|          |      |         |         |         |          |
+----------+      |         |         |         +----------+
                  |         |         |
                  |         |         |
                  |         |         |
                  |         |         |
                  |         |         |
                  +---------+---------+

```
[](http://lday.me/2019/09/09/0023_linux_page_cache_and_buffer_cache/)
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

## 通讯驱动
### openbinder 通讯驱动
linux-kernel\drivers\staging\android\binder.c
#### 系统调用 （用户态转化内核态）
binder.c/binder_miscdev 方法注册
```c
static struct miscdevice binder_miscdev = {
    .minor = MISC_DYNAMIC_MINOR,
    .name = "binder",
    .fops = &binder_fops
};
static const struct file_operations binder_fops = {
    .owner = THIS_MODULE,
    .poll = binder_poll,
    .unlocked_ioctl = binder_ioctl,
    .compat_ioctl = binder_ioctl,
    .mmap = binder_mmap,
    .open = binder_open,
    .flush = binder_flush,
    .release = binder_release,
};
```
#### 传输存储格式
```C++
struct binder_write_read {//不一定传输这个数据，也有可能传输servicemanager的BC_FREE_BUFFER数据结构；BC_ACQUIRE 直接传递local binder对象
	signed long	write_size;	/* bytes to write */
	signed long	write_consumed;	/* bytes consumed by driver */
	unsigned long	write_buffer;
	signed long	read_size;	/* bytes to read */
	signed long	read_consumed;	/* bytes consumed by driver */
	unsigned long	read_buffer;
};
```
#### 传输协议
```c++
binder.h
enum binder_driver_return_protocol {// binder driver return client/server
	BR_ERROR = _IOR('r', 0, int), 
	BR_OK = _IO('r', 1), 
	BR_TRANSACTION = _IOR('r', 2, struct binder_transaction_data),
	BR_REPLY = _IOR('r', 3, struct binder_transaction_data),
	BR_ACQUIRE_RESULT = _IOR('r', 4, int),
	BR_DEAD_REPLY = _IO('r', 5),
	BR_TRANSACTION_COMPLETE = _IO('r', 6),
	BR_INCREFS = _IOR('r', 7, struct binder_ptr_cookie),
	BR_ACQUIRE = _IOR('r', 8, struct binder_ptr_cookie),
	BR_RELEASE = _IOR('r', 9, struct binder_ptr_cookie),
	BR_DECREFS = _IOR('r', 10, struct binder_ptr_cookie),
	BR_ATTEMPT_ACQUIRE = _IOR('r', 11, struct binder_pri_ptr_cookie),
	BR_NOOP = _IO('r', 12),
	BR_SPAWN_LOOPER = _IO('r', 13),
	BR_FINISHED = _IO('r', 14),
	BR_DEAD_BINDER = _IOR('r', 15, void *),
	BR_CLEAR_DEATH_NOTIFICATION_DONE = _IOR('r', 16, void *),
	BR_FAILED_REPLY = _IO('r', 17),
};

enum binder_driver_command_protocol {//client/server to binder driver
	BC_TRANSACTION = _IOW('c', 0, struct binder_transaction_data),
	BC_REPLY = _IOW('c', 1, struct binder_transaction_data),
	BC_ACQUIRE_RESULT = _IOW('c', 2, int),
	BC_FREE_BUFFER = _IOW('c', 3, int),
	BC_INCREFS = _IOW('c', 4, int),
	BC_ACQUIRE = _IOW('c', 5, int),
	BC_RELEASE = _IOW('c', 6, int),
	BC_DECREFS = _IOW('c', 7, int),
	BC_INCREFS_DONE = _IOW('c', 8, struct binder_ptr_cookie),
	BC_ACQUIRE_DONE = _IOW('c', 9, struct binder_ptr_cookie),
	BC_ATTEMPT_ACQUIRE = _IOW('c', 10, struct binder_pri_desc),
	BC_REGISTER_LOOPER = _IO('c', 11),
	BC_ENTER_LOOPER = _IO('c', 12),
	BC_EXIT_LOOPER = _IO('c', 13),
	BC_REQUEST_DEATH_NOTIFICATION = _IOW('c', 14, struct binder_ptr_cookie),
	BC_CLEAR_DEATH_NOTIFICATION = _IOW('c', 15, struct binder_ptr_cookie),
	BC_DEAD_BINDER_DONE = _IOW('c', 16, void *),
};


struct binder_transaction_data {// command_protocol 是 TRANSACTION 时的数据；注意servicemanager是定义为binder_txn，多了offs；用户数据放在最后data.ptr.buffer
	union {
		size_t	handle;	/* target descriptor of command transaction */
		void	*ptr;	/* target descriptor of return transaction */
	} target;
	void		*cookie;	/* target object cookie */
	unsigned int	code;		/* transaction command */
	unsigned int	flags;
	pid_t		sender_pid;
	uid_t		sender_euid;
	size_t		data_size;	/* number of bytes of data */
	size_t		offsets_size;	/* number of bytes of offsets */
	union {
		struct {
			const void __user	*buffer;/* transaction data */
			const void __user	*offsets;/* offsets from buffer to flat_binder_object structs */
		} ptr;
		uint8_t	buf[8];
	} data;
};
// command_protocol 是 BC_TRANSACTION 时的数据


struct flat_binder_object {// binder_transaction_data 数据的data.ptr.offsets数据结构
	unsigned long		type;/* 8 bytes for large_flat_header. */
	unsigned long		flags;
	union {	/* 8 bytes of data. */
		void __user	*binder;	/* local object */
		signed long	handle;		/* remote object */
	};
	void __user		*cookie;/* extra data associated with local object */
};

```
#### 内核态切换到进程所在的用户态
```
Linux kernel 的 wake_up_interruptible()
```

### 蓝牙驱动
### Wifi驱动
## 显卡声卡摄像头驱动

### FrameBuffer 显卡驱动
2.2以下，vga直接对显卡上的寄存器操作；2.2以上加入fb
```C++
drivers/video/fbmem.c#fb_fops //提供系统调用
```
https://tldp.org/HOWTO/html_single/Framebuffer-HOWTO/#AEN131
https://www.cnblogs.com/linfeng-learning/p/9478048.html

### V4l2 摄像头驱动
v4l2的代码：
linux-kernel/drivers/media/v4l2-core/v4l2-device.c
linux-3.0.35/drivers/media/video/mxc/capture/mxc_v4l2_capture.c
### ALSA 声卡驱动
 sound/core ALSA的核心驱动
## 外设驱动
### USB
### Flash
### KeyPad


### initcall机制
linux对驱动程序提供静态编译进内核和动态加载两种方式

### 静态注册
module_init

mem.c/chr_dev_init()



## 高效 GNU

### grep  sed awk
grep：适合单纯的查找或匹配文本；
sed：适合对匹配到的文本进行编辑；
awk：适合对文本进行较复杂的格式化处理；


### find

### locate whereis which type 

### 磁盘分析
du -c -d 1  -m | sort -n