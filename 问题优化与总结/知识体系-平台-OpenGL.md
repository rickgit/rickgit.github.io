## OpenGL type
```
+-----------------------------------------------------------------------------------+
|  Suffix       Data Type        Typical Corresponding   OpenGL Type Definition     |
|                                  C-Language Type                                  |
+-----------------------------------------------------------------------------------+
|  b           8-bit integer      signed char             GLbyte                    |
|                                                                                   |
|  s          16-bit integer      short                   GLshort                   |
|                                                                                   |
|  i           32-bit integer     int or long            GLint, GLsizei             |
|                                                                                   |
|  f           32-bit                                                               |
|             floating-point      float                  GLfloat, GLclampf          |
|                                                                                   |
|  d          64-bit              double                 GLdouble, GLclampd         |
|              floating-point                                                       |
|                                                                                   |
|  ub         8-bit               unsigned char           GLubyte, GLboolean        |
|             unsigned integer                                                      |
|                                                                                   |
|  us         16-bit              unsigned short         GLushort                   |
|             unsigned integer                                                      |
|                                                                                   |
|  ui         32-bit              unsigned int           GLuint, GLenum, GLbitfield |
|             unsigned integer    or unsigned long                                  |
+-----------------------------------------------------------------------------------+


```

## Opengl
OpenGl只是一个标准；实际的OpenGL库的开发者通常是显卡的生产商，Windows中的opengl32.dll，以及Unix /usr/lib/libGL.so，Android 实现是 platform_frameworks_native  agl;
EGL代替的是原先wgl/glx 管理context，Android 使用 egl。
GBM还是在Chromium的开源项目中，它和EGL功能类似，但是比EGL功能多一些。在XDC2014，Nvidia员工Andy Ritger提议增强EGL以取代GBM。

DRM(Direct Rendering Manager)由两个部分组成：
一是 Kernel 的子系统，这个子系统对硬件 GPU 操作进行了一层框架封装。
二是提供了一个 libdrm 库，里面封装了一系列 API，用来进行图像显示。


显示存储器简称显存，也称为帧缓存，顾名思义，其主要功能就是暂时储存显示芯片处理过或即将提取的渲染数据。帧缓冲可能是GPU专属内存，也可能是GPU和CPU共享内存，看硬件。手机一般是共享内存，PC独立显卡一般是专属内存，集成显卡是共享内存。
系统内存只是暂时存放数据的地方，不能处理数据；要想显示数据，还得把数据传输到显卡内存里。显卡内存（常见的有2、16、32、64、128MB）要存储FRONT和BACK缓冲区，Z 缓冲区，其他的顶点缓存、索引缓存、纹理缓存、模板缓存 [显存与纹理内存详解](https://blog.csdn.net/mmqqyyqqyyq/article/details/84001790)

Linux FrameBuffer 本质上只是提供了对图形设备的硬件抽象，在开发者看来，FrameBuffer 是一块显示缓存，往显示缓存中写入特定格式的数据就意味着向屏幕输出内容。
帧(frame)是指整个屏幕范围。


### 显存与显示
glReadPixels，它可以直接把显存中的数据拷贝到内存中，读取帧缓存数据，效率慢。帧实时读取时，画面就会出现卡顿，需要使用更快的读取方式，比如通过内存映射。如果使用了双缓冲区，则默认是从正在显示的缓冲（即前缓冲）中读取，而绘制工作是默认绘制到后缓冲区的。如果需要读取已经绘制好的像素，往往需要先交换前后缓冲。其他glDrawPixels/glCopyPixels
### 内存映射
glGenBuffers 创建顶点缓存（位于显存），glReadPixels 就可以将帧缓存数据（位于显存）读取到顶点缓冲，glMapBuffer 顶点缓存映射到内存。
PBO是OpenGL ES 3.0开始提供的一种方式，主要应用于从内存快速复制纹理到显存，或从显存复制像素数据到内存。由于现在Android的生态还有大部分只支持到OpenGL ES 2.0的硬件存在，所以通常需要跟glReadPixels配合使用。判断硬件api版本，如果是3.0就使用PBO，否则使用glReadPixels [Alimin利民](https://www.jianshu.com/p/3be97e897531)


渲染操作是将场景送至backbuffer
单缓冲区渲染，把渲染结果实际绘制到屏幕上，需要调用glFlush()或glFinsh()
双缓冲区渲染,GL_FRONT缓冲区（位于显存）和GL_BACK缓冲区（位于显存）。glDrawBuffer(GL_BACK); glReadBuffer(GL_BACK); 
立体渲染，左和右缓冲区以及辅助缓冲区。

gles是opengl的缩减版，gles没有glDrawBuffer和glReadBuffer接口，没法直接操纵前后缓冲区。 
gles1.1开始支持FBO，gl变量和指令加OES后缀。 通过glTexSubImage2D函数直接将图像数据更新到颜色缓冲区中，功能跟glDrawPixels完全一致，避免走OpenGL流水线。