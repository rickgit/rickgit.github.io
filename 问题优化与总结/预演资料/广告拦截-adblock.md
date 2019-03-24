

## C库编译
[libadblockplus](https://github.com/adblockplus/libadblockplus)

[adblockplusandroid](https://github.com/adblockplus/adblockplusandroid)
[adblockplusandroid](https://github.com/adblockplus/libadblockplus-android)

Consider using CMake or ndk-build integration. For more information, go to:

```
update-alternatives --install /usr/bin/python python /usr/bin/python2.7 2


export PATH=/home/anshu/workspace/ws-adblock/android-ndk-r16b:$PATH
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
|                                                                                             |
|                          AdblockEngine                                                      |
|                                                                                             |
+---------------------------------------------------------------------------------------------+
|                         adblockplus                                                         |
|                                                                                             |
+---------------------------------------------------------------------------------------------+
|                                                                                             |
|                           v8                                                                |
|                                                                                             |
+---------------------------------------------------------------------------------------------+


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


## 本地实现项目 AdBlock_Android

```
gitee账号： anshu.wang@qq.com
```

```

shouldInterceptRequest
```