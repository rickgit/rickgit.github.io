
## 界面优化

viewstub,

##  刷新 （绘制，局部重绘）

```
            +--------------------------------------------------------------------------------------------+
            |                View                                                                        |
            |                  invalidate()          postInvalidate()       updateDisplayListIfDirty()   |
            |                                                                                            |
            +--------------------------------------------------------------------------------------------+
            |   ViewGroup                                                                                |
            |    in^alidateChild()                                                                       |
            +--------------------------------------------------------------------------------------------+
            |   ViewRootImpl:ViewParent   dispatchInvalidateDelayed() mView:View                         |
            |    invalidateChild()                                    doTraversal()                      |
            |   invalidateChildInParent()                                                                |
            |    invalidateRectOnScreen()                             performTraversals()                |
            |    scheduleTraversals()                                                                    |
            |                                                         measureHierarchy() performlayout() |
            +------------------------------------------------+        performMeasure()   performDraw()   |
            |   Choreographer                                |                                           |
            |     postCallback()                             +-------------------------------------------+
            |     postCallbackDelayedInternal()                                                          |
            |     scheduleFrameLocked()                                                                  |
            |     scheduleVsyncLocked()                                                                  |
            |     mDisplayEventReceiver:FrameDisplayEventReceiver                                        |
            |     doFrame()                                                                              |
            +--------------------------------------------------------------------------------------------+
            |  FrameDisplayEventReceiver :Runnable        CallbackRecord                                 |
            |       scheduleVsync()                             run()                                    |
            |       dispatchVsync()                                                                      |
            |       run()                                 TraversalRunnable:Runnable                     |
            |                                                                                            |
            +--------------------------------------------------------------------------------------------+


```
## 动画

## 界面跨平台 flutter
- 第一代 ionic(webview),PhoneGap，Cordova      H5                语言：js/css      环境：webview

- 第二代 RN（v8,Hermes/jscore）> weex(js,vue)  jit运行时编译     语言：JavaScript  环境：nodejs/v8 
   [RN](https://www.reactnative.cn/docs/components-and-apis)
- 第三代 Flutter（Skia）                                     语言：dart
### Flutter 路由，UI
### dart
https://dart.dev/samples

https://dart.dev/guides/language/language-tour


[内部实现](https://github.com/dart-lang/sdk/blob/master/sdk/lib/_internal/vm/lib/)


调试JIT，发布AOT

```
+-----------------------------------------------------------------------+
|                     Object                                            |
|                        hashCode :int                                  |
|                        runtimeType:Type                               |
|                                                                       |
+-----------------------------------------------------------------------+

+-----------------------------------------------------------------------+ 
|                     num                                               |
+-----------------------------------------------------------------------+
|   int                               double                            |
|    bitLength:int                                                      |
|    isEven: bool                      sign:int                         |
|    isOdd:bool                                                         |
|    sign:int                                                           |
|                                                                       |
+-----------------------------------------------------------------------+
+-----------------------------------------------------------------------+
|                          StringBuffer :::StringSink                   |
|                                                                       |
|                               _parts:List<String>?                    |
|                                                                       |
|                               length:int                              |
+-----------------------------------------------------------------------+
|                                                                       |
|                  String :::Comparable<String>, Pattern                |
|                     codeUnits:List<int>  //UTF-16                     |
|                     runes:Runes                                       |
|                     length:length                                     |
|                     hashCode:int                                      |
|                    _isOneByte :bool                                   |
+-----------------------------------------------------------------------+
|                                                                       |
|                    Runes : Iterable<int>                              |
|                       string: String                                  |
+-----------------------------------------------------------------------+

+-----------------------------------------------------------------------+
|                        _GrowableList                                  | 
|                          _grow()                        _shrink()     |
|                          _nextCapacity()                              |
|                          _allocateData() //odd number                 |
|                                                                       |
|         _List            Lists                                        |
|          length:int         copy()                                    |
|                                                                       |
+-----------------------------------------------------------------------+ 


+-----------------------------------------------------------------------+
|                                                                       |
|            _InternalLinkedHashMap<K, V> : _HashVMBase                 |
|                        ::  MapMixin<K, V>,                            |
|                            _LinkedHashMapMixin<K, V>,                 |
|                            _HashBase,                                 |
|                            _OperatorEqualsAndHashCode                 |
|                        :::  LinkedHashMap                             |
|                                                                       |
+-----------------------------------------------------------------------+
|              _index   :int                                            |
|              _hashMask :int                                           |
|              _data     :List                                          |//偶数位放key,奇数放value
|              _usedData :int                                           |
|              _deletedKeys :int                                        |
|                                                                       |
+-----------------------------------------------------------------------+


+-----------------------------------------------------------------------+
|                                                                       |
|             _CompactLinkedHashSet<E> :_HashFieldBase                  |
|                 :: _HashBase, _OperatorEqualsAndHashCode, SetMixin<E> |
|                 ::: LinkedHashSet                                     |
|                                                                       |
+-----------------------------------------------------------------------+
|              _index   :int                                            |
|              _hashMask :int                                           |
|              _data     :List                                          |
|              _usedData :int                                           |
|              _deletedKeys :int                                        |
|                                                                       |
+-----------------------------------------------------------------------+


```


```
+-----------------------------------------------------------------------------+
|                                                                             |
|       _File : FileSystemEntity ::: File                                     |
|                                                                             |
|           readAsStringSync():String                                         |
|                                                                             |
|          _tryDecode(bytes:List<int> ,encoding):String                       |
|                                                                             |
|          readAsBytesSync(): Uint8List                                       |
|                                                                             |
|          openSync(): RandomAccessFile                                       |
|                                                                             |
|                                                                             |
+-----------------------------------------------+-----------------------------+
|                                               |                             |
|                                               |   BytesBuilder              |
|  _RandomAccessFile ::: RandomAccessFile       |                             |
|        _connectedResourceHandler:bool = false;|       add()                 |
|         path;String                           |                             |
|        _asyncDispatched; bool   = false       |  _CopyingBytesBuilder       |
|        _fileService;SendPort                  |        :BytesBuilder        |
|        _resourceInfo; _FileResourceInfo       |     _length ;  int          |
|        _ops;_RandomAccessFileOps              |     _buffer;  Uint8List     |
|                                               | _BytesBuilder               |
|        lengthSync()                           |       :BytesBuilder         |
|        readSync(int bytes):Uint8List          |     _length:int             |
|                                               |    _chunks:List<Uint8List>  |
|                                               |                             |
+-----------------------------------------------+-----------------------------+


```