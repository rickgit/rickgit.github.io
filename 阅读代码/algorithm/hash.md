# 哈希算法
## Object.hashCode()

[hashCode分析文章](https://srvaroa.github.io/jvm/java/openjdk/biased-locking/2017/01/30/hashCode.html)

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


hash 本质时调用 markOop.cpp  文件中 markOopDesc 类的地址进行操作
```
hotspot/src/share/vm/oops/markOop.cpp

intptr_t hash() const {

    //32位的Mark word格式             hash:25 ------------>| age:4    biased_lock:1 lock:2 (normal object)
    //hash_shift=4+1+2，hash_mask = right_n_bits(4+1+2)=2^7=bits:100 0000
    return mask_bits(value() >> hash_shift, hash_mask);
}
  
uintptr_t value() const { return (uintptr_t) this; }//方法在markOopDesc内中，获取的是 markOopDesc 对象的地址
```

如果没有从 mark word获取到 hash值，调用 **get_next_hash** 生成新的Hash值，包含[五种不同的hashCode生成策略](https://blog.csdn.net/topc2000/article/details/79454064?utm_source=blogxgwz6)
[OpenJDK 默认算法](https://srvaroa.github.io/jvm/java/openjdk/biased-locking/2017/01/30/hashCode.html)

1. 第一种，
```
hotspot/src/share/vm/runtime/os.hpp:732:  static long random();                    // return 32bit pseudorandom number

  /* standard, well-known linear congruential random generator with
   * next_rand = (16807*seed) mod (2**31-1)
   * see
   * (1) "Random Number Generators: Good Ones Are Hard to Find",
   *      S.K. Park and K.W. Miller, Communications of the ACM 31:10 (Oct 1988),
   * (2) "Two Fast Implementations of the 'Minimal Standard' Random
   *     Number Generator", David G. Carta, Comm. ACM 33, 1 (Jan 1990), pp. 87-88.
  */
```

Hash高效，循环周期要大，求余数要大。计算要快，即余数是字长（32bit）

2.第二种，对象地址与垃圾回收随机数（本质为os::Random）操作

## MD5
> The MD5 message-digest algorithm is a widely used cryptographic hash function producing a 128-bit (16-byte) hash value, typically expressed in text format as a 32 digit hexadecimal number.[MD5 - wikipekia](https://en.wikipedia.org/wiki/MD5)

## 安全哈希算法（SHA）

## [字节转化](http://atool.org/hexconvert.php)