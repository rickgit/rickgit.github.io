## 架构图

[JavaScript引擎](https://baike.baidu.com/item/javascript%E5%BC%95%E6%93%8E/5356108)
[排版引擎/页面渲染引擎](https://baike.baidu.com/item/%E6%8E%92%E7%89%88%E5%BC%95%E6%93%8E/8371898?fr=aladdin)

四大渲染引擎在线：WebKit,Blink,Trident,Gecko

```         
https://www.html5rocks.com/zh/tutorials/internals/howbrowserswork/


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