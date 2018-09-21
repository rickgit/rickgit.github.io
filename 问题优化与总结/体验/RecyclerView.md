#RecyclerView 

视觉吸引（主屏幕） Header，Footer，分割线，Item 动画
搜索、分类、过滤   下拉刷新, 加载更多
排序和筛选 拖拽换位和滑动删除
反馈与功能可见性（避免无结果或无关结果） 加载中/空数据/错误页面
导航 分组显示，多层分组，悬浮分组头（ItemDecoration或ViewHolder实现），侧滑菜单功能，长按菜单，单击导航

##
[SwipeToLoadLayout](https://github.com/Aspsine/SwipeToLoadLayout.git)

[UltimateRecyclerView](https://github.com/cymcsg/UltimateRecyclerView)

[IRecyclerView](https://github.com/Aspsine/IRecyclerView)

[PullLoadMoreRecyclerView](https://github.com/WuXiaolong/PullLoadMoreRecyclerView)

[HeaderAndFooterRecyclerView](https://github.com/cundong/HeaderAndFooterRecyclerView)


# ExpandableLayout
[ExpandableLayout](https://github.com/AAkira/ExpandableLayout)

[ExpandableLayout](https://github.com/cachapa/ExpandableLayout)

# PullToRefresh 
[AnimRefreshRecyclerView](https://github.com/shichaohui/AnimRefreshRecyclerView.git)
通过装饰模式，设计 Adapter，添加头部和底部控件
- 刷新通过监听滑动的距离，分为过度滑动的长度和最大滑动长度；
- 底部刷新控件

# The most important classes of the RecyclerView API
- Adapter	    	Wraps the data set and creates views for individual items
[定制 Adapter 的方法](http://blog.csdn.net/jxxfzgy/article/details/47012097)

- ViewHolder		Holds all sub views that depend on the current item’s data
- LayoutManager		Places items within the available area
- ItemDecoration	Draws decorations around or on top of each item’s view
- ItemAnimator		Animates items when they are added, removed or reordered
 
