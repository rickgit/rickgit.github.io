
# pull to refresh （下拉加载）
## 嵌套滚动
[FlyRefresh](https://github.com/race604/FlyRefresh)
[TwinklingRefreshLayout](https://github.com/lcodecorex/TwinklingRefreshLayout) 

[SmartRefreshLayout](https://github.com/scwang90/SmartRefreshLayout) 支持嵌套各种滑动组件
	NestedScrollingChild 可以滑动的 View，包含一个 NestedScrollingChildHelper 辅助类
	NestedScrollingParent 有一个 NestedScrollingParentHelper 辅助类来默默的帮助你实现和 Child 交互的逻辑。
	滑动动作是 Child 主动发起，Parent 就收滑动回调并作出响应。
	
	子view									父view
	startNestedScroll			onStartNestedScroll、onNestedScrollAccepted
	dispatchNestedPreScroll		onNestedPreScroll
	dispatchNestedScroll		onNestedScroll
	stopNestedScroll			onStopNestedScroll

## RecyclerView
 View 缓存 ：onCreateViewHolder
 View 布局多样性：getItemViewType
 View 重用 ：onBindViewHolder ->设置每次重用 view 的动画
 分割线：

## 下拉刷新（SwipeRefreshLayout-下拉刷新，PullToRefresh-释放刷新）
- SwipeRefreshLayout

- PullToRefresh
[PullToRefresh](https://github.com/chrisbanes/Android-PullToRefresh)

1 “下拉刷新” 提示状态
指示图标，“下拉刷新”，“最近更新 2017年5月16日 下午2:33:53”
2 “松开后加载” 提示状态
指示图标，“松开后加载”，“最近更新 2017年5月16日 下午2:33:53”
3 “刷新...” 提示状态
状态条，“刷新...”，“最近更新 2017年5月16日 下午2:33:53”

- UltraPullToRefresh
[UltraPullToRefresh](https://github.com/liaohuqiu/android-Ultra-Pull-To-Refresh)
[android-Ultra-Pull-To-Refresh-With-Load-More](https://github.com/captainbupt/android-Ultra-Pull-To-Refresh-With-Load-More)


[CommonPullToRefresh](https://github.com/Chanven/CommonPullToRefresh)
在android-Ultra-Pull-To-Refresh的基础上增加了加载更多
下拉刷新支持大部分view：ListView、ScrollView、WebView等，甚至一个单独的TextView
加载更多目前支持ListView、RecyclerView、GridView、SwipeRefreshLayout
支持自定义header以及footer
增加SwipeRefreshLayout刷新方式，同样支持加载更多

[SpringView](https://github.com/liaoinstan/SpringView)
support ScrollView,ListView,RecyclerView,WebView and all another views

- PullLoadMoreRecyclerView，XRecyclerView，AnimRFRecyclerView（耦合太大忽略）

- com.lzy.widget.HeaderViewPager （派发 ViewPager 事件）

- 嵌套滚动（NestedScrollingParent, NestedScrollingChild）

## 加载更多（LoadMoreView）

- 上滑动加载更多
1 “查看更多” 提示状态
2 “松开载入更多” 提示状态
3 “ProgressBar” 等待状态
4 加载完成状态

- 点击 “更多” 加载


c “查看全文”
WebView，文本查看全文


## 固定标题栏
[sticky-headers-recyclerview](https://github.com/timehop/sticky-headers-recyclerview)
[ExpandableRecyclerView](https://github.com/ayalma/ExpandableRecyclerView)

[FlexibleAdapter]()
## 附加控件

### 指示器
引入 ** compile 'com.android.support:design:22.2.0' **
android.support.design.widget.TabLayout


### 
---------------------

[^1]: SwipeToLoadLayout
https://github.com/Aspsine/SwipeToLoadLayout.git


[^2]:UltimateRecyclerView
https://github.com/cymcsg/UltimateRecyclerView

[^3]:IRecyclerView
https://github.com/Aspsine/IRecyclerView


[^4]:PullLoadMoreRecyclerView
https://github.com/WuXiaolong/PullLoadMoreRecyclerView


[^5]:HeaderAndFooterRecyclerView
https://github.com/cundong/HeaderAndFooterRecyclerView