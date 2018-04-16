1. 项目：
https://github.com/scwang90/SmartRefreshLayout

2. 工程目录：
com.woqutz.didi.custom.scroll.layout


3. 布局：
```xml
    <com.woqutz.didi.custom.scroll.layout.ScrollRefreshLayout
        android:layout_width="match_parent"
        android:id="@+id/betchp_sl"
        android:background="@color/betjc_jczq_duizhen_bg"
        android:layout_height="match_parent">

        <android.support.v7.widget.RecyclerView
            android:id="@+id/rv_duizhen_list"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:background="@color/betjc_jczq_duizhen_bg"
            android:cacheColorHint="@android:color/transparent"
            android:scrollingCache="false">
        </android.support.v7.widget.RecyclerView>
    </com.woqutz.didi.custom.scroll.layout.ScrollRefreshLayout>
```

4. 代码：
``` java
 rl = (ScrollRefreshLayout) getView().findViewById(R.id.betchp_sl);
        rv.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
//        rl.setRefreshHeader()
        rl.setEnableLoadMore(false);
        rl.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                queryData();

            }
        });
```
5. 默认刷新控件

com.woqutz.didi.custom.scroll.layout.footer.ClassicsFooter
com.woqutz.didi.custom.scroll.layout.header.ClassicsHeader

6. 原理：
 
- ScrollRefreshLayout 通过  dispatchTouchEvent(MotionEvent) 拦截事件，View#obtain 获取 MotionEvent 模拟触摸事件；
- View.setTranslateY(int) 设置 RecyclerView，刷新头部控件和加载更多控件的位置
- View.canScrollVertically(int)，判断 RecyclerView 边界；Android api 14;
- Scroller，VelocityTracker 自动滚动页面；
- 联动 NestedScrollingChildHelper，NestedScrollingChild，coordinatorlayout




 ``` plumtuml
 @startuml
title SmartRefreshLayout 下拉刷新
/' You can also declare:
   participant L as "I have a really\nlong name"  #99FF99，
inclue:
actor
boundary
control
entity
database
  '/
participant SmartRefreshLayout #Cyan

participant RecyclerView #Cyan
participant NestedScrollingChildHelper #Cyan

 

SmartRefreshLayout->RecyclerView:super.dispatchTouchEvent()#DOWN
RecyclerView->RecyclerView:onInterceptTouchEvent()
RecyclerView->NestedScrollingChildHelper:NestedScrollingChild#startNestedScroll()
NestedScrollingChildHelper->NestedScrollingChildHelper:startNestedScroll()
SmartRefreshLayout<--NestedScrollingChildHelper:ViewParentCompat.onStartNestedScroll(p, child, mView, axes)\n((NestedScrollingParent) parent).onStartNestedScroll()
SmartRefreshLayout<--NestedScrollingChildHelper:ViewParentCompat.onNestedScrollAccepted()\n((NestedScrollingParent) parent).onNestedScrollAccepted(child, target,nestedScrollAxes);


SmartRefreshLayout->RecyclerView:super.dispatchTouchEvent()#MOVE
RecyclerView->NestedScrollingChildHelper:NestedScrollingChild#dispatchNestedPreScroll()
NestedScrollingChildHelper->NestedScrollingChildHelper:dispatchNestedPreScroll()
SmartRefreshLayout<--NestedScrollingChildHelper:((NestedScrollingParent) parent).onNestedPreScroll()


SmartRefreshLayout->RecyclerView:super.dispatchTouchEvent()#MOVE
RecyclerView->RecyclerView:super.dispatchTouchEvent()#MOVE#scrollByInternal()
RecyclerView->RecyclerView:NestedScrollingChild#dispatchNestedScroll()
RecyclerView->NestedScrollingChildHelper:NestedScrollingChild#dispatchNestedScroll()
NestedScrollingChildHelper->NestedScrollingChildHelper:dispatchNestedScroll()
SmartRefreshLayout<--NestedScrollingChildHelper:((NestedScrollingParent) parent).onNestedScroll()


SmartRefreshLayout->RecyclerView:super.dispatchTouchEvent()#UP
RecyclerView->RecyclerView:onTouch()\nfling()
RecyclerView->RecyclerView:NestedScrollingChild#dispatchNestedPreFling()
RecyclerView->NestedScrollingChildHelper:onNestedPreFling()
SmartRefreshLayout<--NestedScrollingChildHelper:ViewParentCompat.onNestedPreFling()



SmartRefreshLayout->RecyclerView:super.dispatchTouchEvent()#CANCEL/UP
RecyclerView->RecyclerView:cancelTouch()\nresetTouch()
RecyclerView->NestedScrollingChildHelper:NestedScrollingChild#stopNestedScroll()
NestedScrollingChildHelper->NestedScrollingChildHelper:ViewParentCompat.onStopNestedScroll()
SmartRefreshLayout<--NestedScrollingChildHelper:((NestedScrollingParent) parent).onStopNestedScroll(target)






@enduml
```