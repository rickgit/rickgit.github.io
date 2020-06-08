

## 源码
1. [入门](https://www.jenkins.io/zh/doc/pipeline/tour/getting-started/)
```
下载 Jenkins.
打开终端进入到下载目录.
运行命令 java -jar jenkins.war --httpPort=8080.
打开浏览器进入链接 http://localhost:8080.
按照说明完成安装.
```
[加速](https://www.cnblogs.com/hellxz/p/jenkins_install_plugins_faster.html)
```bash
docker-machine ssh default // 先进入虚拟机，default 是默认的虚拟机名称
sudo vi /var/lib/boot2docker/profile // 编辑这个文件，添加镜像源 --registry-mirror https://registry.docker-cn.com
sudo /etc/init.d/docker restart // 重启 docker 进程
exit // 退出虚拟机
docker info // 看一下镜像源是否设置成功（是否有刚刚设置的 --registry-mirror 这一行）

sed -i 's/http:\/\/updates.jenkins-ci.org\/download/https:\/\/mirrors.tuna.tsinghua.edu.cn\/jenkins/g' default.json && sed -i 's/http:\/\/www.google.com/https:\/\/www.baidu.com/g' default.json
```
2. 入口
jenkins.war/META-INF/MANIFEST.MF

```
+-----------------------------------------------------------------------+
|    org.kohsuke.stapler.Stapler             hudson.WebAppMain          |
|                                                                       |
|                                                                       |
|                                              HudsonIsLoading          |
|                                                                       |
+-----------------------------------------------------------------------+


```