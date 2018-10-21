


## android ndk 18 （clang），ffmpeg 4.0
```
C compiler test failed.
解決：查看ffbuild/config.log文件末尾，一般是類似”unrecognized argument in option xxx”的錯誤，按照提示修改即可。 
我的錯誤出在“-mcpu=arm”，直接去掉，就可以了。

https://www.smwenku.com/a/5b8872592b71775d1cdc768e/
```

```
libavdevice/v4l2.c
Open this file and find the following line:
int (*ioctl_f)(int fd, unsigned long request, ...);
to
int (*ioctl_f)(int fd, unsigned int request, ...);



Open configure script with any text editor and navigate to line #5021 or:
SHFLAGS='-shared -Wl,-soname,$(SLIBNAME)'
inside “android)” case in “case $target_os in” switch.
Change the line to:
SHFLAGS='-shared -soname $(SLIBNAME)'




https://medium.com/@ilja.kosynkin/building-ffmpeg-4-0-for-android-with-clang-642e4911c31e
```



```
libavcodec/aaccoder.c:803:25: error: expected identifier or '('
                    int B0 = 0, B1 = 0;
                        ^
/home/anshu/workspace/toolchain/sysroot/usr/include/asm-generic/termbits.h:118:12: note: 
      expanded from macro 'B0'
#define B0 0000000

#define B0 0000000



https://medium.com/@piyush1995bhandari/cross-compiling-ffmpeg-for-android-545a1cfca31e

解决：将libavcodec/hevc_mvs.c文件的变量B0改成b0，xB0改成xb0，yB0改成yb0


将libavcodec/hevc_mvs.c文件的变量B0改成b0，xB0改成xb0，yB0改成yb0

https://blog.csdn.net/not_in_mountain/article/details/79085173


https://www.smwenku.com/a/5b8872592b71775d1cdc768e/

https://www.jianshu.com/p/484db5ec733f

```