
## 界面优化


viewstub,
### RecyclerView 缓存
```
+---------------------------------------------------------------------------------+
| [android 5.0]    RecyclerView                                                   |
|                       mRecycler:Recycler                                        |
|                       setAdapter()                                              |
|                       setAdapterInternal()                                      |
|                                                                                 |
|                       dispatchLayout()                                          |
|                                                                                 |
|  Adapter<ViewHolder>        ViewHolder            LayoutManager                 |
|      onCreateViewHolder()       itemView;View       mLayoutState:LayoutState    |
|      onBindViewHolder()                             onLayoutChildren()          |
|                                                     fill()                      |
|                                                     layoutChunk()               | 
+---------------------------------------------------------------------------------+
|                                                                                 |
|                                     LayoutState                                 |
|                                        next(recycler:Recycler )                 |
|                                    Recycler                                     |
|                                        getRecycledViewPool():RecycledViewPool   |
|                                        getViewForPosition() :View               |
|                                                                                 |
+---------------------------------------------------------------------------------+

```
##  刷新 （绘制，局部重绘）View#invalite
VSynch 垂直同步
Triple Buffer 三重缓存
Choreographer 编舞者
```
* 001_androi1     54b6cfa9a9e5b861a9930af873580d6dc20f773c Initial Contribution
                  +---------------------------------------------------------------------------------------------+
                  |               View                                                                          |
                  |                  invalidate()                     measure()       mBGDrawable:Drawable      |
                  |                                                   layout()        onDraw()                  |
                  |                                                                   dispatchDraw()            |
                  |                                                                   onDrawScrollBars()        |
                  |---------------------------------------------------------------------------------------------|
                  |               ViewRoot:Handler:: ViewParent                                                 |
                  |                  mDirty:Rect                                                                |
                  |                  invalidateChild()                                                          |
                  |                                                                                             |
                  |                  mTraversalScheduled:boolean                                                |
                  |                  mWillDrawSoon:bool                                                         |
                  |                  scheduleTraversals()                                                       |
                  |                                                                                             |
                  |                   mView:View                                                                |
                  |                  performTraversals()                                                        |
                  |                                                                                             |
                  |                   mSurface:Surface                                                          |
                  |                  draw( canvas:canvas)                                                       |
                  |                                                                                             |
                  |---------------------------------------------------------------------------------------------|
  aosp-new/master 8283939cecfb5538e21ffc7adb017a995f099012 Merge "Create new Carrier configuration for separate MMTEL/RCS features"
                  +--------------------------------------------------------------------------------------------+
                  |                View                                                                        |
                  |                  invalidate()          postInvalidate()       updateDisplayListIfDirty()   |
                  |                                                                                            |
                  +--------------------------------------------------------------------------------------------+
                  |   ViewGroup:ViewParent                                                                     |
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
## 事件相应 InputManager.getInstance().injectInputEvent
```
         +-----------------------------------------------------------------------------------------+
         |  ImputManager                                                                           |
         |       injectInputEvent()                                                                |
         |                                                                                         |
         |       mPtr:long                                                                         |
         |       injectInputE^ent()                                                                |
         |       injectInputEventInternal()                                                        |
         +-------------------------------------------------------------+---------------------------+
         | [platform_frameworks_base\services\core\jni]                |                           |
         |                                                             |                           |
         |             com_android_server_input_InputManagerService.cpp|                           |
         |                       NativeInputManager                    |                           |
         |                          getInputManager()                  |                           |
         +-------------------------------------------------------------+                           |
         | [platform_frameworks_native\services\inputflinger]          |                           |
         |     InputManager.cpp                                        |                           |
         |       mReaderThread :InputReaderThread(mReader);            |                           |
         |       mDispatcherThread:InputDispatcherThread(mDispatcher); |                           |
         |                                                             |                           |
         |       mDispatcher:InputDispatcher                           |                           |
         |       mClassifier:InputClassifier                           |                           |
         |       mReader:InputReader                                   |                           |
         |                                                             |                           |
         |                                                             |                           |
         |     InputDispatcher                                         |                           |
         |                                                             |                           |
         |       injectInputEvent()                                    |                           |
         |       enqueueInboundEventLocked()                           |                           |
         |                                                             |                           |
         |       dispatchOnce()//thread loop                           |                           |
         |                                                             |                           |
         |       mInboundQueue:Queue<EventEntry>                       |                           |
         |       dispatchOnceInnerLocked()                             |                           |
         |                                                             |                           |
         |       dispatchMotionLocked()                                |                           |
         |                                                             |                           |
         |       findTouchedWindowTargetsLocked()//touchscreen         |     WMS                   |
         |       checkWindowReadyForMoreInputLocked()               +----->                        |
         |                                                             |                           |
         |       mLooper:Looper                                        |                           |
         |       handleRecei^eCallback()                               |     //epoll socket channel|
         +-----------------------------------------------------------------------------------------+
         +-------------------------------------------------------------+                           |
         |      ViewRootImpl                                                                       |
         |                                         mFirstPostImeInputStage          mView:DecorView|
         |        mInputEventReceiver                        :InputStage                           |
         |             :WindowInputEventReceiver                                                   |
         |                                                                                         |
         |      WindowInputEventReceiver:                                                          |
         |              InputEventReceiver                                                         |
         |         enqueueInputEvent()                                                             |
         |                                                                                         |
         |    ViewPostImeInputStage                InputStage                                      |
         |         onProcess(q:QueuedInputEvent )      deliver(QueuedInputEvent q)                 |
         |                                             onProcess(QueuedInputEvent q)               |
         |                                                                                         |
         |                                         ViewPostImeInputStage :InputStage               |
         |                                             processPointerEvent()                       |
         |                                                                                         |
         +-----------------------------------------------------------------------------------------+
         |     DecorView:ViewGroup                                                                 |
         |           dispatchPointerEvent()               superDispatchTouchEvent()                |
         |           dispatchTouchEvent()                 //super.dispatchTouchEvent               |
         |                                                                                         |
         |     WindowCallbackWrapper                                                               |
         |     Activity                                                                            |
         |     PhoneWindow                                                                         |
         |          superDispatchTouchEvent()                                                      |
         |                                                                                         |
         +-----------------------------------------------------------------------------------------+
         |    ViewGroup :View                                                                      |
         |       mFirstTouchTarget:TouchTarget//not DownEvent, null not dispatch                   |
         |       dispatchTouchEvent()                                                              |
         |       onInterceptTouchEvent()                                                           |
         |       dispatchTransformedTouchE^ent()                                                   |
         +-----------------------------------------------------------------------------------------+
         |    View                                                                                 |
         |     mListenerInfo:ListenerInfo                                                          |
         |     performClickInternal();                                                             |
         |                                                                                         |
         |    ListenerInfo                                                                         |
         |         mViewFlags:int//ENABLED_MASK                                                    |
         |         mOnClickListener;OnClickListener                                                |
         +-----------------------------------------------------------------------------------------+



```
## 动画
https://dribbble.com/
### TWeen Animation
修改matrix，刷新界面，等待下一帧时候，绘制界面

```
+---------------------------------------------------------------------------------------------+
|  [Android 4.4]                                                                              |
|          View                                                                               |
|            mCurrentAnimation:Animation                ViewGroup:View                        |
|                                                          dispatchDraw()                     |
|            startAnimation(animation:Animation)           drawChild()                        |
|            clearAnimation()                              getChildTransformation()           |
|            onDetachedFromWindow()                                                           |
|                                                                                             |
|            draw()                                                                           |
|            drawAnimation():bool                                                             |
+---------------------------------------------------------------------------------------------+
|         ViewRootImpl:Parent                                                                 |
|           invalidateChild()                                                                 |
|          invalidateChildInParent()                                                          |
|           scheduleTraversals()                                                              |
|                                                                                             |
+---------------------------------------------------------------------------------------------+
|   Animation                                                            AlphaAnimation       |
|       mInterpolator:Interpolator                                       RotateAnimation      |
|       getTransformation():bool                                         ScaleAnimation       |
|       applyTransformation(interpolatedTime:float ,t:Transformation)    TranslateAnimation   |
|                                                                                             |
+---------------------------------------------------------------------------------------------+
|    Interpolator              //normalizedTime=dx/duration         Transformation            |
|             getInterpolation(normalizedTime:float):float                 mMatrix:Matrix     |
|                                                                                             |
|     LinearInterpolator :BaseInterpolator                                                    |
|     AccelerateInterpolator                                        Matrix //3x3              |
|     BounceInterpolator                                                                      |
+---------------------------------------------------------------------------------------------+

```

### ViewPropertyAnimator
```
+---------------------------------------------------------------------------------------------+
|  [Android 4.4]                                                                              |
|          View                                               mTransformationInfo             |
|            animate()                                        mRenderNode                     |
|            mAnimator:ViewPropertyAnimator                   in^alidate()                    |
|                                                             invalidateViewProperty()        |
|                                                                                             |
+---------------------------------------------------------------------------------------------+
|    ViewPropertyAnimator                        AnimatorEventListener                        |
|          mView:View                                  : Animator.AnimatorListener            |
|          mAnimationStarter:Runnable                                                         |
|                                                                                             |
|          mRTBackend:ViewPropertyAnimatorRT                                                  |
|                                                   onAnimationUpdate(ValueAnimator animation)|
|          onAnimationUpdate()                                                                |
|          setValue()                                                                         |
+---------------------------------------------------------------------------------------------+
|    ValueAnimator                                                                            |
|         addAnimationCallback()                                                              |
|         doAnimationFrame()                                                                  |
|         animateValue()                                                                      |
|                                                                                             |
+---------------------------------------------------------------------------------------------+
|    AnimationHandler               PropertyValuesHolder                                      |
|                                         calculateValue()                                    |
+---------------------------------------------------------------------------------------------+


```
### GIF
Glide播放多个gif文件卡

android-gif-drawable性能好

android-1.6_r1\external\giflib 系统源码利用

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