# 算法

## 随机数生成算法 Random
初始化
```java
    private static final long multiplier = 0x5deece66dL;//final 说明不可再赋值

    /**
     * The boolean value indicating if the second Gaussian number is available.
     *
     * @serial
     */
    private boolean haveNextNextGaussian;

    /**
     * @serial It is associated with the internal state of this generator.
     */
    private long seed;

    /**
     * The second Gaussian generated number.
     *
     * @serial
     */
    private double nextNextGaussian;

    /**
      * Used to generate initial seeds.
      */
    private static volatile long seedBase = 0;
    public Random() {
        // Note: Don't use identityHashCode(this) since that causes the monitor to
        // get inflated when we synchronize.
        setSeed(System.nanoTime() + seedBase);
        ++seedBase;
    }
```
设置Seed为初始化当前系统纳秒的时间戳，加上seedBase。然后seedBase加1。
```java

```
nanoTime方法调用的linux的clock_gettime,实现文件在libcore/luni/src/main/native/java_lang_System.cpp
```c
static jlong System_nanoTime(JNIEnv*, jclass) {
    #if defined(HAVE_POSIX_CLOCKS)
        timespec now;
        clock_gettime(CLOCK_MONOTONIC, &now);
        return now.tv_sec * 1000000000LL + now.tv_nsec;
    #else
        timeval now;
        gettimeofday(&now, NULL);
        return static_cast<jlong>(now.tv_sec) * 1000000000LL + now.tv_usec * 1000LL;
    #endif
}
```
其中timespec格式如下，分为秒和纳秒两部分。
```
struct timespec
{
    time_t tv_sec;       
    long int tv_nsec;      
};
```
timeval格式如下，分为秒和微妙两部分。
```
struct timeval {
　　      time_t tv_sec;
　　      suseconds_t tv_usec;
};
```
回到Random，setSeed以当前纳秒的时间戳，加上seedBase进行操作

```
    public synchronized void setSeed(long seed) {
        this.seed = (seed ^ multiplier) & ((1L << 48) - 1);
        haveNextNextGaussian = false;
    }
```
亦或multiplier常量0x5deece66dL，seed取48位。<br/>
接下来看下，产生随机整数值。
```
    public int nextInt(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("n <= 0: " + n);
        }
        if ((n & -n) == n) {
            return (int) ((n * (long) next(31)) >> 31);
        }
        int bits, val;
        do {
            bits = next(31);
            val = bits % n;
        } while (bits - val + (n - 1) < 0);
        return val;
    }
    protected synchronized int next(int bits) {
        seed = (seed * multiplier + 0xbL) & ((1L << 48) - 1);
        return (int) (seed >>> (48 - bits));
    }
```
1. （n&-n）==n代表2的幂指数，及(1<<k)，((n * (long) next(31)) >> 31)相当于(0,1)*n
2. (bits - val + (n - 1) < 0)。val是bits取模产生的。所以代表bits要是n的两倍以上。
3. next方法是根据《The Art of Computer Programming》Volume 2: Seminumerical Algorithms</i>, section 3.2.1，采用现流行的线性同余序列（[linear congruential generator](https://en.wikipedia.org/wiki/Linear_congruential_generator
)即 x(n+1) = {a*x(n)+c}mod m）。seed开始值，multiplier倍数，0xbL增量

```
    public long nextLong() {
        return ((long) next(32) << 32) + next(32);
    }

```
nextLong分两个次计算int.
```
    public float nextFloat() {
        return (next(24) / 16777216f);
    }
```
nextFloat在半开闭区间[0.0, 1.0)的随机数,16777216f是2的24次方。

```
    public double nextDouble() {
        return ((((long) next(26) << 27) + next(27)) / (double) (1L << 53));
    }
```
nextDouble在半开闭区间[0.0, 1.0)的随机数

```
    public boolean nextBoolean() {
        return next(1) != 0;
    }
```
基本上都是基于next做处理。

```
    public synchronized double nextGaussian() {
        if (haveNextNextGaussian) {
            haveNextNextGaussian = false;
            return nextNextGaussian;
        }

        double v1, v2, s;
        do {
            v1 = 2 * nextDouble() - 1;
            v2 = 2 * nextDouble() - 1;
            s = v1 * v1 + v2 * v2;
        } while (s >= 1 || s == 0);

        // The specification says this uses StrictMath.
        double multiplier = StrictMath.sqrt(-2 * StrictMath.log(s) / s);
        nextNextGaussian = v2 * multiplier;
        haveNextNextGaussian = true;
        return v1 * multiplier;
    }
```
nextGaussian() 方法用于获取下一个伪高斯(正常地)分布的均值为0.0,标准差为1.0从此随机数生成器的序列的double值。
<br/>
Math.random(),其实是调用Random.nexDouble方法。


2. compression: DEFLATE
