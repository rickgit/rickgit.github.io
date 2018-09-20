#DiskLruCache分析

> 

@(源码分析)[DiskLruCache|Android]

**DiskLruCache**，将从以下几点分析

- **DiskLruCache创建及初始化**
- **存储键值对**
- **移除键值对**
- **清理容器**

---------------------

[TOC]

##DiskLruCache创建及初始化
相关属性 

LruCache包含个构造函数，接下来只分析构造函数。  
##清理容器 