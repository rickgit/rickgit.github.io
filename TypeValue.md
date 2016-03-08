#Android TypeValue分析

> 在Android中，我们可以在values文件夹中定义各种资源，其中有一种就是dimension。
dimension是一个包含单位（dp、dip、sp、pt、px、mm、in）的尺寸，可以用于定义视图的宽度、字号等。如下图所示。[Android Dimension转换算法原理分析](http://johnsonxu.iteye.com/blog/1929027)

@(源码分析)[TypeValue|Android]

 

- **创建及初始化**
- **complexToDimension方法** 

---------------------

[TOC]

##创建及初始化


##complexToDimension方法
-- android.content.res.Resources#getDimension
-- android.view.animation.ScaleAnimation#resolveScale
