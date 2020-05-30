## 换源

### Windows
docker-machine ssh [machine-name]           //打开virtual box查看名字
sudo vi /var/lib/boot2docker/profile          // 添加地址 [Docker 镜像使用帮助](https://lug.ustc.edu.cn/wiki/mirrors/help/docker)
添加 --registry-mirror https://xxxxxxxx.mirror.aliyuncs.com1
sudo /etc/init.d/docker restart



## 安装Ubuntu镜像
docker pull ubuntu:14.04 //拉取镜像
docker images 
docker rmi **imageid**

 
//转移镜像
docker save -o ./docker-ubuntu-14.04.tar ubuntu:14.04
docker save -o ./docker-jenkins-latest.tar jenkins:latest

docker load --input docker-ubuntu-14.04.tar
docker load --input docker-jenkins-latest.tar
docker load --input docker-alpine-latest.tar


Alpine Linux
### 容器
docker run -it  --name ubuntu-14.04 -v /c/Users/Ubuntu:/share ubuntu:14.04  //创建容器并使用容器
docker run -it  --name ubuntu-latest -v /c/Users/Ubuntu:/share ubuntu:latest  //创建容器并使用容器
docker run -it  --name alpine -v /c/Users/Ubuntu:/share alpine:latest  //创建容器并使用容器
docker run -i -t -p 8888:8888 continuumio/anaconda3 /bin/bash -c "/opt/conda/bin/conda install jupyter -y --quiet && mkdir /opt/notebooks && /opt/conda/bin/jupyter notebook --notebook-dir=/opt/notebooks --ip='*' --port=8888 --no-browser --allow-root"

docker container ls -a
docker start 4d10b6dc4d25
docker kill  4d10b6dc4d25
docker restart 4d10b6dc4d25
docker ps -a
docker container exec -it 4d10b6dc4d25 bash //使用容器
docker rm **containerid**
>exit //退出交互

docker-machine ip default //虚拟机地址
### 中文环境
//cmd 设置编码
CHCP 65001


export LC_ALL="C.UTF-8"
locale

apt-get install -y language-pack-zh-hans
locale-gen zh_CN.UTF-8

//zh_CN.utf8
locale -a

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




### 家庭版
[Windows10家庭版安装Docker Desktop（非Docker Toolbox）](https://www.jianshu.com/p/1329954aa329)
