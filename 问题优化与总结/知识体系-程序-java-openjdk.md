# OpenJDK地址

1.打开地址 jdk.java.net/java-se-ri/11
2.选择左侧选项
REference Implementation <Java版本号> 

-或者
hg clone http://hg.openjdk.java.net/jdk9/jdk9/
chmod u+x ./get_source.sh
./get_source.sh



3.下载
RI Source Code
The source code of the RI binaries is available under the GPLv2 in a single [zip file](https://download.java.net/openjdk/jdk11/ri/openjdk-11+28_src.zip) (sha256) 178.1 MB.


## 下载 Oracle jdk 作为编译的jdk
- 下载Oracle jdk
jdk下载地址，搜索 **archive** 
[Oracle jdk](https://link.zhihu.com/?target=http%3A//www.oracle.com/technetwork/java/javase/downloads/java-archive-javase8-2177648.html)

- configure

根据提示安装需要依赖库
sudo apt-get install  libxext-dev libxrender-dev libxtst-dev libxt-dev
sudo apt-get install libcups2-dev
sudo apt-get install libfreetype6-dev
sudo apt-get install ccache

sudo ./configure --with-boot-jdk=/home/anshu/workspace/jdk1.7.0_80


- make
[常见问题](http://www.jackieathome.net/archives/395.html)
```
 ‘check_os_version’ failed



 make时添加参数 DISABLE_HOTSPOT_OS_VERSION_CHECK=ok

```