## 按照Ubuntu
docker pull ubuntu:14.04
docker run -itd --name ubuntu ubuntu:14.04

docker ps -a
docker exec -i -t 4d10b6dc4d25  /bin/bash

### 挂载文件夹
docker run -it -v /c/Users/Ubuntu:/share ubuntu:14.04 /bin/bash


### 中文环境
//cmd 设置编码
CHCP 65001


export LC_ALL="C.UTF-8"
locale

apt-get install -y language-pack-zh-hans
locale-gen zh_CN.UTF-8

//zh_CN.utf8
locale -a