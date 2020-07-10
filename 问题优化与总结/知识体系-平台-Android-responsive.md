
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