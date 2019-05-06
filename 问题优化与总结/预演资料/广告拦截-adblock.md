

## C库编译
[libadblockplus](https://github.com/adblockplus/libadblockplus)

[adblockplusandroid](https://github.com/adblockplus/adblockplusandroid)
[adblockplusandroid](https://github.com/adblockplus/libadblockplus-android)

Consider using CMake or ndk-build integration. For more information, go to:

```
//依赖环境
sudo apt-get install p7zip-full
 
update-alternatives --install /usr/bin/python python /usr/bin/python2.7 2


//必须下载对应操作系统的NDK
https://developer.android.com/ndk/downloads/older_releases.html?hl=zh-cn#ndk-16b-downloads
export PATH=/home/anshu/workspace/ws-adblock/android-ndk-r16b:$PATH


./ensure_dependencies.py


//编译
//下载对应环境的v8头文件及静态库
make TARGET_OS=android ABP_TARGET_ARCH=arm Configuration=release get-prebuilt-v8
make TARGET_OS=android ABP_TARGET_ARCH=ia32 Configuration=release get-prebuilt-v8

//
make TARGET_OS=android ABP_TARGET_ARCH=arm Configuration=release
make TARGET_OS=android ABP_TARGET_ARCH=ia32 Configuration=release

```

v8支持库下载
``` 

 https://v8.eyeofiles.com/v8-4d72a9931a125d21901d25d67896f0e40105bd16//android-ia32-release.tar.xz


```



## ABP项目
```
+-----------------------------------------------------------------+----------------+----------+
|                                                                 |                |          |
|                                                                 |                |          |
|                                                                 |                |          |
|                                                                 |                |          |
|                                                                 | ElemHideThread |          |
+---------------------------------------------------------------------------------------------+
|                                                                 |                |          |
|  Platform     FilterEngine  LogSystem   FileSystem   WebRequest | AdblockWebView |          |
|                                                                 |                |          |
+-----------------------------------------------------------------+----------------+----------+
|                          AdblockEngine                                                      |
+---------------------------------------------------------------------------------------------+
|                         adblockplus                                                         |
+---------------------------------------------------------------------------------------------+
|                           v8                                                                |
+---------------------------------------------------------------------------------------------+
V8更加直接的将抽象语法树通过JIT技术转换成本地代码，
放弃了在字节码阶段可以进行的一些性能优化，但保证了执行速度。
在V8生成本地代码后，也会通过Profiler采集一些信息，来优化本地代码。
虽然，少了生成字节码这一阶段的性能优化，但极大减少了转换时间。

api
https://adblockplus.org/jsdoc/adblockpluscore/elemHide.js.html
```

加载规则路径
```
/data/data/org.adblockplus.libadblockplus.android.webviewapp/shared_prefs/ADBLOCK_PRELOAD.xml

<map>
    <set name="urls">
        <string>https://easylist-downloads.adblockplus.org/exceptionrules.txt</string>
        <string>https://easylist-downloads.adblockplus.org/easylistchina+easylist.txt</string>
    </set>
</map> 

```
更新规则地址
```
/data/data/org.adblockplus.libadblockplus.android.webviewapp/app_adblock/patterns.ini
# Adblock Plus preferences
version=5

[Subscription]
url=https://easylist-downloads.adblockplus.org/easylistchina+easylist.txt
title=EasyList China+EasyList
homepage=http://abpchina.org/forum/
lastDownload=1553247024
downloadStatus=synchronize_connection_error
lastCheck=1553249062
errors=1

[Subscription]
url=https://easylist-downloads.adblockplus.org/exceptionrules.txt
title=https://easylist-downloads.adblockplus.org/exceptionrules.txt
lastDownload=1553247480
downloadStatus=synchronize_connection_error
lastCheck=1553249062
errors=1

```


```
+-----------------------+
|    AdblockHelper      |                      +------------------------+
+-----------------------+   <------------------+ SingleInsEngineProvider|
|SingleInsEngineProvider|                      +------------------------+                                +---------------------------------+
|                       |                      |   AdblockEngine        | <-------------+                |          JAVA                   |
|                       |                      |                        |               |                |                                 |
|                       |                      |                        |               |                +---------------------------------+
+-----------------------+                      |                        |               |                |          JNI                    |
                                               |                        |               |                |                                 |
                                               +-------+----------------+               |                +---------------------------------+
                                                       ^                                |                |                                 |
                                                       |                                |                |                                 |
                                                       |                                |                |                                 |
                          +----------------------------+                                |                |                                 |
                          |                                                             |                |                                 |
                          |                          +------------------------+         |                |                                 |
                          |                          |     AdblockEngine      |  +------+                +---------------------------------+
                          |                          +------------------------+
                          |                          | WebRequest             |
                          |                          | LogSystem              |
                          |                          | FileSystem             |
+----------------------+  |                          | Platform               |
|     AdblockWebView   |  |                 +------> | FilterEngine           |  +-----------+
+----------------------+  |                 |        |                        |              |
|  ElemHideThread      |  |                 |        +------------------------+              |        +-------------------+
|AdblockEngineProvider +--+                 |                                                +----->  |  FilterEngine.cpp |
|  WebViewClient       |                    |                                                         +-------------------+
|                      |        +----+ ElemH+deThread  <-------------+                                | libadblockplus    |
+----------------------+        +                                    |                                |    /lib/*.js      |
|@JavascriptInterface  |  CountDownLatch.countDown()                 |                                +-------------------+
|getElemhideSelectors()|        |                         +----------+-------------+                  | adblockpluscore   |
+----------------------+  <-----v-------------------------+ CountDownLatch.await() +<--------+        |    /lib/*.js      |
                                                          +------------------------+         |        |                   |
                                                                                             |        +-------------------+
+-------------------+                                                                        |
|   WebChromeClient |    +-----------------------+                                           |
+-------------------+    |AdblockWebViewClient   |                                           |
|onProgressChanged()|    +-----------------------+         +-------------------+          +--+------------+
|                   |    |   onPageStarted()     | +-----> |   inject.js       | +------> |  css.js       |
|                   |    |shouldInterceptRequest()         +-------------------+          +---------------+
+-------------------+    |                       |
                         |                       |
                         +-----------------------+


```


```
onProgressChanged
       FilterEngine#getElementHidingSelectors
            ------> elemHide.js#getSelectorsForDomain

shouldInterceptRequest (block <iframe>)
       FilterEngine#matches
            ------> matcher.js#matchesAny
                                blacklist
                                whitelist


AndroidWebRequest#GET
            synchronizer.js

                      +----------------+
                      |  Subscription  |
                      +----------------+
                      | url            |
                      | type           |
                      | filters:[]     |
                      | _title         |
                      | _fixedTitle    |
                      | _disabled:false|
                      |                |
                      +----------------+


prefs.js 偏好设置文件
```


```
https://easylist-downloads.adblockplus.org/easylistchina+easylist.txt

白名单（有问题，加载完应用会闪退）
https://easylist-downloads.adblockplus.org/exceptionrules.txt
 [Adblock Plus 2.0]
    ! Checksum: fPAP6CiW0rRHYnKgbqcpdw
    ! Version: 201903260841
    ! Title: EasyList China+EasyList
    ! Last modified: 26 Mar 2019 08:41 UTC
    ! Expires: 1 days (update frequency)
    ! Homepage: http://abpchina.org/forum/
    !
    ! EasyList China and EasyList combination subscription
    !
    ! *** easylistchina:easylistchina.txt ***
    ! Chinese supplement for the EasyList filters
    ! License: https://easylist-downloads.adblockplus.org/COPYING
    !
    ! Please report any unblocked adverts or problems
    ! in the forums (http://abpchina.org/forum/)
    ! or via e-mail (easylist.china@gmail.com).
    !
    !-----------------------General advert blocking filters-----------------------!
    -1688-wp-media/ads/
    -880-80-4.jpg



```

### 过滤规则（elehide.js）

[过滤规则](https://adblockplus.org/zh_TW/filters#basic)

概念：
[PSL（Public Suffix List，公共后缀列表）](https://publicsuffix.org/list/public_suffix_list.dat)
利用 **WebStorm** 阅读JS代码
```
startup入口：
notification.js#init()

filterListener.js#init()


synchronizer.js//同步下载的规则文本


elemHide.js //过滤业务

filtersByDomain: [domain,[filter,isIncluded]]

filterBySelector

knownFilters



filterClasses.js //规则文本转化为Filter对象
Filter.fromText


            +----------------------------+
            |                #Filter     |
            +----------------------------+
            |domains:{[domain,isInclude]}|
            |                            |
            |                            |
            |                            |
            +----------------------------+


+-------------------------------------------------+
|elemHide.js                                      |
+-------------------------------------------------+
|                                                 |
|filtersByDomain:Map.<string,Map.<Filter,boolean> |
|                                                 |
|filterBySelector:Map.<string,Filter>             |
|                                                 |
|unconditionalSelectors:?string[]                 |
|                                                 |
|defaultDomains:Map.<string,boolean>              |
|                                                 |
|knownFilters:Set.<ElemHideFilter>                |
|                                                 |
+-------------------------------------------------+

                                                        Filter
                                                           ^
                                                           |
                                     +---------------------+-----------------+
                                   ActiveFilter       InvalidFilter    CommentFilter
                                      ^
                  +-------------------+----------+
            RegExpFilter                      ContentFilter
                                                       ^
                 ^                             +-------+---------------------------+
      +----------+-----+                ElemHideBase                             SnippetFilter
BlockingFilter    WhitelistFilter
                                              ^
                                              |
                             +----------------+--------------------+
                        ElemHideFilter   ElemHideException   ElemHideEmulationFilter

CommentFilter:（包含!）   ! Checksum: Z5CwsZg8Z8oioygwsmTblA


ElemHideFilter: （包含##）                         数据管理类：ElemHide：filtersByDomain，filterBySelector
hk.yahoo.com###mntl1 
hk.yahoo.com##li[class="js-stream-content Cf Pos-r RevealNested      "][data-uuid]:not([data-uuid*="-"])

ElemHideException:（包含#@#）       数据管理类：ElemHideExceptions：exceptionsBySelector [selector,domain]
 comicbookmovie.com#@#.skyscraperAd


ElemHideEmulationFilter:（包含#?#）   
aliexpress.com#?#.list-item:-abp-has(span.sponsored)   

SnippetFilter: （包含#$#）  
abpchina.org#$#log Hello


BlockingFilter: 数据管理类：AdMatcher:filterByKey
/adsfooter   
||imagebam.com/image/  
          
WhitelistFilter:（包含@@）    数据管理类：AdMatcher:filterByKey
@@|blob:$script,domain=dato.porn
```
#### css 相邻兄弟选择器


### 存在问题
1. 白名单使用不了
2. 线程同步，需要先下载规则，如果出问题，可能导致页面显示不出来


### 优化速度

Profier优化工作线程，

1. Record 后，分析线程对应的方法耗时时间，找到主要耗时的方法，（>200ms,>20ms 最近查找），重复调用的方法必须优化时间。
2. 本来使用 Bloom Filter，保存前三个字符串及字符串长度两个List。List 缓存查询时间是N，小数据量的情况下，不一定是合适的。采用数据库缓存，安装后，生成Index，上万条查询时间大概 15ms（ log2(10000) ）。
3. 使用 LruCache 缓存，数量不多，不太会改变的数据

4. Mat 分析内存泄漏，异步关掉 Cursor。


## 本地实现项目 AdBlock_Android

```
gitee账号： anshu.wang@qq.com
```

```

shouldInterceptRequest
```


### DevTools 调试（需要代理访问）

```
git clone https://git.coding.net/scaffrey/hosts.git


chrome://inspect/#devices


file:///android_asset/testRule.html
```