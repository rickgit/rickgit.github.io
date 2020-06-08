## 换源

### Windows
docker-machine ssh [machine-name]           //打开virtual box查看名字
sudo vi /var/lib/boot2docker/profile          // 添加地址 [Docker 镜像使用帮助](https://lug.ustc.edu.cn/wiki/mirrors/help/docker)
添加 --registry-mirror=https://xxxxxxxx.mirror.aliyuncs.com1
sudo /etc/init.d/docker restart

docker-machine ip default //虚拟机地址


## 安装镜像
docker pull ubuntu:14.04 //拉取镜像
docker images 
docker rmi **imageid**

 
//转移镜像
docker save -o ./docker-ubuntu-14.04.tar ubuntu:14.04
docker save -o ./docker-jenkins-latest.tar jenkins:latest
docker save -o ./docker-anaconda3_kotlin.tar docker-anaconda3_kotlin:latest
docker save -o ./docker-minicoda3_cpp.tar minicoda3_cpp:latest
docker save -o ./docker-miniconda3_lang.tar miniconda3_lang:latest

docker load --input docker-ubuntu-14.04.tar
docker load --input docker-jenkins-latest.tar
docker load --input docker-alpine-latest.tar
docker load --input docker-anaconda3_kotlin.tar 
docker load --input docker-miniconda3_lang.tar 

//容器转为镜像
docker commit containername newImageName
Alpine Linux
### 容器（windows容器文件夹只能挂载在/c/Users下）
docker run -it  --name ubuntu-14.04 -v /c/Users/docker:/share ubuntu:14.04  //创建容器并使用容器
docker run -p 8080:8080 -p 50000:50000 --name jenkins --user root -v /c/Users/docker:/share jenkins:latest

docker run -it --user root --name ubuntu-latest -v /c/Users/docker:/share ubuntu:latest  //创建容器并使用容器
docker run -it --user root --name alpine -v /c/Users/docker:/share alpine:latest  //创建容器并使用容器

docker run -it --user root --name anaconda3 -v /c/Users/docker:/share  -p 8888:8888 continuumio/anaconda3 /bin/bash -c "/opt/conda/bin/conda install jupyter -y --quiet && mkdir /opt/notebooks && /opt/conda/bin/jupyter notebook --notebook-dir=/share --ip='*' --port=8888 --no-browser --allow-root"

docker run -it --user root --name anaconda3_kotlin -v /c/Users/docker:/share  -p 8888:8888 anaconda3_kotlin /bin/bash -c "/opt/conda/bin/conda install jupyter -y --quiet   && /opt/conda/bin/jupyter notebook --notebook-dir=/share --ip='*' --port=8888 --no-browser --allow-root"

docker run -i -t -p 8888:8888 continuumio/miniconda3 /bin/bash -c "/opt/conda/bin/conda install jupyter -y --quiet && mkdir /opt/notebooks && /opt/conda/bin/jupyter notebook --notebook-dir=/opt/notebooks --ip='*' --port=8888 --no-browser"

docker run -it --user root --name miniconda3 -v /c/Users/docker:/share  -p 9999:9999 continuumio/miniconda3 /bin/bash -c "sed -i 's/deb.debian.org/mirrors.ustc.edu.cn/g' /etc/apt/sources.list && /opt/conda/bin/conda install jupyter -y --quiet && mkdir /opt/notebooks && /opt/conda/bin/jupyter notebook --notebook-dir=/share --ip='*' --port=9999 --no-browser --allow-root"

docker run -it --user root --name miniconda3 -v /c/Users/docker:/share  -p 9999:9999 minicoda3_lang /bin/bash -c "/opt/conda/bin/conda install jupyter -y --quiet   && /opt/conda/bin/jupyter notebook --notebook-dir=/share --ip='*' --port=9999 --no-browser --allow-root"


docker container ls -a
docker start 4d10b6dc4d25
docker kill  4d10b6dc4d25
docker restart 4d10b6dc4d25
docker ps -a
docker container exec -it 4d10b6dc4d25 bash //使用容器
docker rm **containerid**
>exit //退出交互

### Ubuntu中文环境
//cmd 设置编码
CHCP 65001


export LC_ALL="C.UTF-8"
locale

apt-get install -y language-pack-zh-hans
locale-gen zh_CN.UTF-8

//zh_CN.utf8
locale -a

更换源
vim /etc/apt/sources.list


[或更新debin源](http://mirrors.ustc.edu.cn/help/debian.html)
sudo sed -i 's/deb.debian.org/mirrors.ustc.edu.cn/g' /etc/apt/sources.list
sudo apt-get update
### C语言环境
``` 
E: Unable to correct problems, you have held broken packages.

$ sudo apt-get upgrade

$ sudo apt-get update
```

sudo apt-get install build-essential



### window环境
git clone https://github.com/Zeranoe/mingw-w64-build.git

sudo apt-get install texinfo (makeinfo )
apt install subversion

### jenkins
//插件下载不了，需要更新Jenkins
cp /share/jenkins.war /usr/share/jenkins/jenkins.war

重启

### canaconda3+kotlin
[jupyter notebook + kotlin](https://blog.jetbrains.com/kotlin/2020/05/kotlin-kernel-for-jupyter-notebook-v0-8/?_ga=2.246012621.813816282.1590907491-895964087.1587827860)
 conda config
 conda config --show channels
 conda config --add channels https://mirrors.tuna.tsinghua.edu.cn/anaconda/pkgs/free/
conda config --add channels https://mirrors.tuna.tsinghua.edu.cn/anaconda/cloud/conda-forge 
conda config --add channels https://mirrors.tuna.tsinghua.edu.cn/anaconda/cloud/msys2/ 
conda config --set show_channel_urls yes
cat ~/.condarc

conda: conda install kotlin-jupyter-kernel -c jetbrains //需要设置添加[清华源](https://mirrors.tuna.tsinghua.edu.cn/help/anaconda/),中科大暂时用不了
或
pip install kotlin-jupyter-kernel -i https://pypi.tuna.tsinghua.edu.cn/simple //-i 临时设置tsinghua源

apt-cache search openjdk
apt-get install openjdk-11-jre
或
sudo apt install software-properties-common
sudo apt-get update
sudo add-apt-repository ppa:openjdk-r/ppa //sudo add-apt-repository ppa:webupd8team/java

mkdir -p /usr/share/man/man1 //dpkg: error processing package openjdk-8-jre:amd64 (--configure):


#### 安装 C++/C
apt-get install gcc

conda create -n clang
conda activate clang
conda install xeus-cling -c conda-forge //用 minicondas 安装

pip install jupyter-c-kernel
install_c_kernel
jupyter kernelspec list


### 家庭版
[Windows10家庭版安装Docker Desktop（非Docker Toolbox）](https://www.jianshu.com/p/1329954aa329)
