## 架构图

[JavaScript引擎](https://baike.baidu.com/item/javascript%E5%BC%95%E6%93%8E/5356108)
[排版引擎/页面渲染引擎](https://baike.baidu.com/item/%E6%8E%92%E7%89%88%E5%BC%95%E6%93%8E/8371898?fr=aladdin)

四大渲染引擎在线：WebKit,Blink,Trident,Gecko

```         
https://www.html5rocks.com/zh/tutorials/internals/howbrowserswork/


+-------------------------------------------------------------------------------+
|  gclient                                                                       |
|   depot_tools                                                                     |
+-------------------------------------------------------------------------------+
|                                                                               |
|   chromium                                                                    |
+-------------------------------------------------------------------------------+
|                                                                               |
|    Html/Css rendering  Engine(WebKit,Blink,Trident,Gecko)                     |
|                                                 +-----------------------------+
|                                                 |  Js Engine (v8)  Skia mojo  |
+-------------------------------------------------+-----------------------------+


 ```
 [how blilk work](https://docs.google.com/document/d/1aitSOucL0VHZa9Z2vbRJSyAIsAz24kX8LFByQ5xQnUg)


 [chromium 设计文档](https://www.chromium.org/developers/design-documents)

[视频]( https://www.chromium.org/developers/design-documents/video)

 [chrome webview 介绍](https://developer.chrome.com/multidevice/webview/overview)
 ## chromium for webview
 [chromium for android](https://chromium.googlesource.com/chromium/src/+/master/docs/android_build_instructions.md)

 [chromium for webview](https://www.chromium.org/developers/how-tos/build-instructions-android-webview)

[chrome high-level](http://dev.chromium.org/developers/how-tos/getting-around-the-chrome-source-code)
[chromium.org/developers](https://www.chromium.org/developers/)


更换git地址
```shell
sed -i s#"https://beijing.source.codeaurora.org/quic/lc/chromium/src.git"#"https://beijing.source.codeaurora.org/quic/lc/chromium/src"#g  `grep "https://beijing.source.codeaurora.org/quic/lc/chromium/src.git" -rl ./`

sed -i s#"https://chromium.googlesource.com/chromium/src.git"#"https://beijing.source.codeaurora.org/quic/lc/chromium/src"#g  `grep "https://chromium.googlesource.com/chromium/src.git" -rl ./`

sed -i s#"https://chromium.googlesource.com/chromium/src.git"#"https://beijing.source.codeaurora.org/quic/lc/chromium/src"#g  `grep "https://chromium.googlesource.com/chromium/src.git" -rl ./`

1>fatal: unable to access 'https://chromium.googlesource.com/chromium/src.git/': Failed to connect to chromium.googlesource.com port 443: Connection timed out


#fetch --nohooks android
Running: gclient root
curl: (7) Failed to connect to chrome-infra-packages.appspot.com port 443: 连接超时
Your current directory appears to already contain, or be part of, 
a checkout. "fetch" is used only to get new checkouts. Use 
"gclient sync" to update existing checkouts.

Fetch also does not yet deal with partial checkouts, so if fetch
failed, delete the checkout and start over (crbug.com/230691).

#gclient config https://beijing.source.codeaurora.org/quic/lc/chromium/src@refs/remotes/origin/m52
#修改.gclient文件的url
#https://beijing.source.codeaurora.org/quic/lc/chromium/src@refs/remotes/origin/m52

#gclient sync //下载代码
curl: (7) Failed to connect to chrome-infra-packages.appspot.com port 443: 连接超时
1>________ running 'git -c core.deltaBaseCacheLimit=2g clone --no-checkout --progress https://beijing.source.codeaurora.org/quic/lc/chromium/src /home/anshu/workspace/chromium/_gclient_src_B70pn4' in '/home/anshu/workspace/chromium'
1>Cloning into '/home/anshu/workspace/chromium/_gclient_src_B70pn4'...

```


[Google 源码编译](https://blog.csdn.net/Mymain/article/details/45399025)

[Chromium去Google化](https://github.com/Eloston/ungoogled-chromium)
https://blog.csdn.net/u014743697/article/details/52890036


[国内镜像编译chromium](https://blog.csdn.net/mogoweb/article/details/52125581)

[webviewchromium裁剪](https://blog.csdn.net/mogoweb/article/details/76653627)

[编译说明 webview.apk](https://www.chromium.org/developers/how-tos/build-instructions-android-webview)

[详细说明 webview.apk](https://www.codetd.com/article/61662)

## 下载chromium
[获取Chromium 源码的三种方式](https://blog.csdn.net/business122/article/details/80512218)
```
x 1.下载zip包
x 2. git clone -b 42.0.2311.90 --depth 1 https://chromium.googlesource.com/chromium/src.git
x 3. gclient sync --nohooks --no-history  -r 7d100c0e9df1d093c61d7e2c16daf1327d7cc163
√ 4. 稳定版本 https://github.com/zcbenz/chromium-source-tarball/releases  
```


[Chromium版本介绍](http://www.chromium.org/developers/calendar)
[Chromium稳定版本](https://omahaproxy.appspot.com/)

### tarball
[tarball](http://www.voidcn.com/article/p-bbehtnem-qq.html)
```
set DEPOT_TOOLS_UPDATE=0 //不自动更新


git -c core.deltaBaseCacheLimit=2g clone --no-checkout --progress https://chromium.googlesource.com/chromium/src.git /home/anshu/workspace/chrome/_gclient_src_khRm5A
```
## webview实现
### crosswalk
 [crosswalk](https://github.com/crosswalk-project/crosswalk.git)


```
architecture

+------------------------------------------------+
|                  web Runntime                  |
+------------------------------------------------+
|           Multi-Process Component              |
|                                                |
+------------------------------------------------+
|          webCore(webkit-WebCore from Blink)    |
|                                                |
+------------------------------------------------+
|          Graphics(Skia)                        |
|          Multimedia (FFMpeg)                   |
|          Javascript Engine(V8)                 |
+------------------------------------------------+


```
###  GeckoView