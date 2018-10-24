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



## MD5
> The MD5 message-digest algorithm is a widely used cryptographic hash function producing a 128-bit (16-byte) hash value, typically expressed in text format as a 32 digit hexadecimal number.[MD5 - wikipekia](https://en.wikipedia.org/wiki/MD5)

## 安全哈希算法（SHA）
