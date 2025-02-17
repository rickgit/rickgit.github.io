


[源码地址](https://gist.github.com/devunwired/4479231)

```
+--------------------------------------------------------------------------------------------------------------------------------------+
|                                                        Bitmap                                                                        |
|                                                                                                                                      |
+--------+----------------------------+---------------------+--------------------------------------------------------------------------+
|        |                            |                     |                                                                          |
|        |         ColorTable         |    bgColor          |                                                                          |
|        |                            |                     |                                                                          |
|        +--------------------------------------------------------------------------------------------+-------------+------------------+
|        |                            |                     |                                         |             |                  |
|        |                            |                     |      graphics                           |             |                  |
|        |                            |                     | app  control    comment plain other     |    LZW      |                  |
|        |                            |                     |     (GifFrame)          text            |    lct      |                  |
|        | flag color      sort  size |                     +--------------------------------------------------------------------------+
|        |      resolution            |  background pixel   |                                         |   frame     |                  |
|        +----------------------------+  color      aspect  |                  extension              |    npix     |  terminator bad  |
|        |           gct              |  index      ratio   |                                         |             |             byte |
+-------------------------------------+-------------------+-+-----------------------------------------+-------------+------------------+
|GIF flag|       LSD                                      |                                                                            |
+---------------------------------------------------------+                                   Contents                                 |
|   File Header                                           |                                                                            |
+---------------------------------------------------------+----------------------------------------------------------------------------+
|                                                            InputStream/byte[]                                                        |
+--------------------------------------------------------------------------------------------------------------------------------------+
|                                                                GifDecoder                                                            |
+--------------------------------------------------------------------------------------------------------------------------------------+


```

## decodeBitmapData 源码

### 压缩
[LZW 原理](https://www.geeksforgeeks.org/lzw-lempel-ziv-welch-compression-technique/)

[LZW 原理 中文](https://segmentfault.com/a/1190000011425787)

### 源码LZW解压原理
通过每次读取 256个字节到 **block[]** ，从 **block[]** 一个字节读取到 **datum[]** 中，当 **datum[]** 达到 **code**大小后，LZ解码 **code**

### 相关参数详解
```java

LZW压缩对象：每个像素点位置的值，该值不是像素值，而是彩色版中像素颜色对应的索引整数值。


prefix[] LZW 用于存放压缩码的关系数组
suffix[] LZW用于存放压缩码对应的压缩码或源码
pixelStack[] 像素栈，由于可能有多个临时存放像素


available LZW 当前对应的解码中，prfix数组和suffix数组的索引值
clear ：1 << data_size，读到这个bit流，重新设置解码表（prefix数组和suffix数组）
code_mask：code掩码，从bit流获取单个code
end_of_information：clear + 1，当读取到这个bite流，将跳出解码
in_code：临时存放code比特流
old_code：临时存放code比特流，用于下次循环赋值
bits：当前读取的比特流数量，用于判读当读取比特流超过code长度，进行解码
code：LZW一个压缩码，像素颜色值在颜色板中的位置或解码表（prefix数组和suffix数组）中的一个压缩码
count：当前读取并且还未放入解码流的字节数量
i：像素的位置整数值
datum：用于解码的比特流
data_size：LZW未压缩的所有code的数量，GIF中代表的是颜色板的颜色数量

first：LZW解码中，第一个code
top：解码栈顶位置
bi： block字节索引值
pi：单张图片，像素在图片的索引值，用于索引当前像素位于像素素组的位置并存放像素


```