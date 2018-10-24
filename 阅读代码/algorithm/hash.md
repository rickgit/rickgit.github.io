# 哈希算法
## Object.hashCode()

查找调用 Object#hashCode() 调用的层方法
```
grep -rn "FastHashCode"
hotspot/src/share/vm/runtime/synchronizer.hpp:100:  static intptr_t FastHashCode (Thread * Self, oop obj) ;

```

---
- 以下分析 FashHashCode方法
  
has_bias_pattern() 涉及偏向锁，oops_Klass原理
SafepointSynchronize::is_at_safepoint() 安全点，STW 工作，本质时等于常量 **_synchronized=2**的时候
```
hotspot/src/share/vm/runtime/safepoint.hpp:64:      _synchronized     = 2 
```

ReadStableMark()
```
livelock problem

活锁问题

```

```
markOop类型实现Mark Word。

```
markOop 长度，可能32bit 或64bit
```

hotspot/src/share/vm/utilities/globalDefinitions.hpp

#ifdef _LP64
const int LogBytesPerWord    = 3;
#else
const int LogBytesPerWord    = 2;
#endif
const int LogBitsPerByte     = 3;

hotspot/src/share/vm/utilities/globalDefinitions.hpp:96:const int LogBitsPerWord     = LogBitsPerByte + LogBytesPerWord;
hotspot/src/share/vm/utilities/globalDefinitions.hpp:102:const int BitsPerWord        = 1 << LogBitsPerWord;


```
> [HotSpot虚拟机中，对象在内存中存储的布局可以分为三块区域：对象头（Header）、实例数据（Instance Data）和对齐填充（Padding）。](https://blog.csdn.net/zhoufanyang_china/article/details/54601311)，对象头分为两部分，其中前一部分，官方称它为“Mark Word”，Mark Word用于存储对象自身的运行时数据，如哈希码（HashCode）、GC分代年龄、锁状态标志、线程持有的锁、偏向线程ID、偏向时间戳等等，占用内存大小与虚拟机位长一致。
；另外一部分是类型指针。

## MD5
> The MD5 message-digest algorithm is a widely used cryptographic hash function producing a 128-bit (16-byte) hash value, typically expressed in text format as a 32 digit hexadecimal number.[MD5 - wikipekia](https://en.wikipedia.org/wiki/MD5)

## 安全哈希算法（SHA）
