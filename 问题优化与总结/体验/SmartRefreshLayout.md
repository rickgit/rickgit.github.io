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