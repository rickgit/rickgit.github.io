

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