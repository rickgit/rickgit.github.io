## 架构图

[JavaScript引擎](https://baike.baidu.com/item/javascript%E5%BC%95%E6%93%8E/5356108)
[排版引擎/页面渲染引擎](https://baike.baidu.com/item/%E6%8E%92%E7%89%88%E5%BC%95%E6%93%8E/8371898?fr=aladdin)

四大渲染引擎在线：WebKit,Blink,Trident,Gecko

```         
https://www.html5rocks.com/zh/tutorials/internals/howbrowserswork/


+-------------------------------------------------------------------------------+
|  repo wtf weekly  git-gs zsh-goodies gn ninja                                                         |
|  cpplint.py pylint presubmit_support.py                                                          |
|  gclient gcl git-cl svn drover                                                         |
|   depot_tools                                                                 |
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
#git clone https://beijing.source.codeaurora.org/quic/lc/chromium/tools/depot_tools

#git show-ref

sed -i s#"https://beijing.source.codeaurora.org/quic/lc/chromium/src.git"#"https://beijing.source.codeaurora.org/quic/lc/chromium/src"#g  `grep "https://beijing.source.codeaurora.org/quic/lc/chromium/src.git" -rl ./`

sed -i s#"https://chromium.googlesource.com/chromium/src.git"#"https://beijing.source.codeaurora.org/quic/lc/chromium/src"#g  `grep "https://chromium.googlesource.com/chromium/src.git" -rl ./`

sed -i s#"https://chromium.googlesource.com/chromium/src.git"#"https://beijing.source.codeaurora.org/quic/lc/chromium/src"#g  `grep "https://chromium.googlesource.com/chromium/src.git" -rl ./`

1>fatal: unable to access 'https://chromium.googlesource.com/chromium/src.git/': Failed to connect to chromium.googlesource.com port 443: Connection timed out


#fetch --nohooks --nohistory android
Running: gclient root
curl: (7) Failed to connect to chrome-infra-packages.appspot.com port 443: 连接超时
Your current directory appears to already contain, or be part of, 
a checkout. "fetch" is used only to get new checkouts. Use 
"gclient sync" to update existing checkouts.

Fetch also does not yet deal with partial checkouts, so if fetch
failed, delete the checkout and start over (crbug.com/230691).

#gclient config https://beijing.source.codeaurora.org/quic/lc/chromium/src@refs/remotes/origin/m52
#gclient config https://beijing.source.codeaurora.org/quic/lc/chromium/src@refs/tags/75.0.3770.101

https://beijing.source.codeaurora.org/quic/lc/chromium/src@refs/remotes/tags/75.0.3770.101

#修改.gclient文件的url
#https://beijing.source.codeaurora.org/quic/lc/chromium/src@refs/remotes/origin/m52

#gclient sync //获取Android版的代码：
curl: (7) Failed to connect to chrome-infra-packages.appspot.com port 443: 连接超时
1>________ running 'git -c core.deltaBaseCacheLimit=2g clone --no-checkout --progress https://beijing.source.codeaurora.org/quic/lc/chromium/src /home/anshu/workspace/chromium/_gclient_src_B70pn4' in '/home/anshu/workspace/chromium'
1>Cloning into '/home/anshu/workspace/chromium/_gclient_src_B70pn4'...

修改DEPS文件的GIT 路径
git -c core.deltaBaseCacheLimit=2g clone --no-checkout --progress
https://quiche.googlesource.com/quiche.git 需代理
https://dawn.googlesource.com/dawn.git
protobuf修改DEPS 的hash值
修改DEPS feed的hash值
`git rev-list -n 1` ambigous argument 'HEAD'://重新迁出分支master

注释gclient_scm.py中的 cipd ensure


./build/install-build-deps-android.sh
./build/android/envsetup.sh
gclient runhooks

 

查看gn
https://chrome-infra-packages.appspot.com/p/gn/gn/linux-amd64/+/git_revision:64b846c96daeb3eaf08e26d8a84d8451c6cb712b
下载gn，到chromium/src/buildtools/linux64/gn/gn最后个gn是下载的可执行文件
https://chrome-infra-packages.appspot.com/dl/gn/gn/linux-amd64/+/git_revision:64b846c96daeb3eaf08e26d8a84d8451c6cb712b


下载Linux编译库（chromium/src/build/linux/system_script/，参数在sysroots.json）
https://commondatastorage.googleapis.com/chrome-linux-sysroot/toolchain/9e6279438ece6fb42b5333ca90d5e9d0c188a403/debian_sid_i386_sysroot.tar.xz
https://commondatastorage.googleapis.com/chrome-linux-sysroot/toolchain/e7c53f04bd88d29d075bfd1f62b073aeb69cbe09/debian_sid_amd64_sysroot.tar.xz
注释 install_system_root.py的下载流程


以下都是 chromium/src/DEPD 文件下载内容

gs:// 替换为 https://storage.googleapis.com/ 
下载到的：
https://storage.googleapis.com/chromium-binutils/69bedb1192a03126687f75cb6cf1717758a1a59f
/src/third_party/binutils/download.py文件注释掉下载chromium-binutils的代码，及修改保存文件的后缀名

src/tools/clang/scripts/update.py#DownloadAndUnpackClangPackage

https://storage.googleapis.com/chromium-clang-format/942fc8b1789144b8071d3fc03ff0fcbe1cf81ac8

https://storage.googleapis.com/chromium-fonts/a22de844e32a3f720d219e3911c3da3478039f89
https://storage.googleapis.com/chromium-instrumented-libraries/0185d9b6c6fdfbcfffa61d8ac9f19e8879c4dee2
https://storage.googleapis.com/chromium-instrumented-libraries/d429da145648e1795ad8b9005b219b8e6888b79f
https://storage.googleapis.com/v8-wasm-fuzzer/f6b95b7dd8300efa84b6382f16cfcae4ec9fa108 //需注释下载
https://storage.googleapis.com/chromium-nodejs/10.15.3/3f578b6dec3fdddde88a9e889d9dd5d660c26db9 //node_linux
https://storage.googleapis.com/chromium-nodejs/c0e0f34498afb3f363cc37cd2e9c1a020cb020d9 //node_model

gn gen out/Default --args='target_os="android" is_debug=false'
```

[代理处理文件下载失败](https://idom.me/articles/843.html)


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
### android webview 2.2

https://www.cnblogs.com/lfsblack/p/5342631.html
```
webview -> webviewcore -> BrowserFrame 
-> external/WebKit/android/jni/WebCoreFrameBridge.cpp 
-> external/WebKit/webcore/loader/FrameLoader::load
-> external/WebKit/webcore/../MainResourceLoader
-> external/WebKit/webcore/../MainResourceLoader
-> ResourceHandle::create //网络及回调
-> ../jni/ResourceLoaderAndroid::start
-> external/WebKit/webcore/../DocumentLoader::receivedData
-> FrameLoader::write //写入解析


```

```java 
结构 webview->page->frame->document/loader
WebView::initWithFrame

```
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


## 稳定
### 版本
https://src.chromium.org/viewvc/chrome/releases/1.0.154.53 第一版


## 加载快
+--------------------------------------------------------------------------+
|                            AdblockPlus:App                               |
|                                js:JSThread                               |
|                                subscriptions:List<Subscription>          |
|                                                                          |
|                           JSThread:Thread                                |
|                               jsEngine:JSEngine                          |
|                                                                          |
|                           JSEngine                                       |
|                                nativeInitialize()                        |
|                               nativeExecute()                            |
|                               methods:ObjMethod[]                        |
|                                                                          |
|                                                                          |
|                                      [v8] v8::Handle< v8::String>        |
|                                                                          |
|                                           v8::Handle< v8::Script>        |
|                                                  Run()                   |
+--------------------------------------------------------------------------+
|   [js]                                                                   |
|         start.js                                                         |
|    XMLHttpRequest.jsm  FilterNotifier.jsm  FilterClasses.jsm             |
|   SubscriptionClasses.jsm   FilterStorage.jsm   FilterListener.jsm       |
|    Matcher.jsm          Synchronizer.jsm                                 |
+--------------------------------------------------------------------------+
