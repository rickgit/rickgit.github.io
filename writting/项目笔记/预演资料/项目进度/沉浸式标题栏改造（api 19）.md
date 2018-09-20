
## 沉浸式标题栏

1. 修改*Host项目*主题
Theme.AppCompat.Light.NoActionBar


2. BaseUI
```java
    public static void translateStatusBar(Window window) {
        if (isSupportTranslateStatusBar()) {//color os
            WindowManager.LayoutParams localLayoutParams = window.getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        }
//        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
//            View decorView = window.getDecorView();
//            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                    |View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.setStatusBarColor(Color.TRANSPARENT);
//        }
    }
```

3. TitleControl
```java
 if (isSupportTranslateStatusBar()) {
            ViewGroup.LayoutParams statuVHLp = statusViewholder.getLayoutParams();
            statuVHLp.height = statubarHeight;
            statusViewholder.setLayoutParams(statuVHLp);
//            ViewCompat.setFitsSystemWindows(rootView,false);
            mTitleView.setFitsSystemWindows(false);
        } else {
            ViewGroup.LayoutParams statuVHLp = statusViewholder.getLayoutParams();
            statuVHLp.height = 0;
            statusViewholder.setLayoutParams(statuVHLp);
        }
```


4. Dialog
```java
        if (TitleControl.isSupportTranslateStatusBar()) {
//            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE );
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
////        getWindow().setStatusBarColor(Color.TRANSPARENT);
            WindowManager.LayoutParams attributes = getWindow().getAttributes();
            attributes.width = AppConfig.S_scrWidth;
            attributes.height = AppConfig.S_scrHeight;
            getWindow().setAttributes(attributes);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
```


5. Popupwindow
```java
public void showPopWindow(PopupWindow popupWindow, View archor) {
        if (Build.VERSION.SDK_INT < 24) {
            popupWindow.showAsDropDown(archor);
        } else {
            int[] location = new int[2];  // 获取控件在屏幕的位置
            archor.getLocationOnScreen(location);
            if (Build.VERSION.SDK_INT == 25) {
                int tempheight = popupWindow.getHeight();
                if (tempheight == WindowManager.LayoutParams.MATCH_PARENT || AppConfig.S_scrHeight <= tempheight) {
                    popupWindow.setHeight(AppConfig.S_scrHeight - location[1] - archor.getHeight());
                }
            }
            popupWindow.showAtLocation(archor, Gravity.NO_GRAVITY, location[0], location[1] + archor.getHeight());
        }


    }
```

6. 标题栏
- 标题栏一样的颜色
- 无标题栏，需要一个透明的占位

TitleControl titleControl = (TitleControl)findViewById(R.id.loginTitleControl);
titleControl.bindView(null);
titleControl.setBgColor(0x00ffffff);//具体看是否有必要


7.键盘问题
```
/**
 * 解决WebView 输入框被键盘挡住
 * 1，页面是非全屏模式的情况下，给activity设置adjustPan会失效。
 * 2，页面是全屏模式的情况，adjustPan跟adjustResize都会失效。
 * 这里的全屏模式即是页面是全屏的，包括Application或activity使用了Fullscreen主题、
 * 使用了『状态色着色』、『沉浸式状态栏』、『Immersive Mode』等等——总之，基本上只要是App自己接管了状态栏的控制，就会产生这种问题。
 *
 *  Created by xiaojia.rui on 2017/12/26.
 */

public class SoftHideKeyBoardUtil {

    public static void assistActivity(Activity activity) {
        new SoftHideKeyBoardUtil(activity);
    }
    private View mChildOfContent;
    private int usableHeightPrevious;
    private ViewGroup.LayoutParams frameLayoutParams;
    //为适应华为小米等手机键盘上方出现黑条或不适配
    private int contentHeight;//获取setContentView本来view的高度
    private boolean isfirst = true;//只用获取一次
    private  int statusBarHeight;//状态栏高度
    private SoftHideKeyBoardUtil(Activity activity) {
        //1､找到Activity的最外层布局控件，它其实是一个DecorView,它所用的控件就是FrameLayout
        FrameLayout content = (FrameLayout) activity.findViewById(android.R.id.content);statusBarHeight=DeviceUtils.getStatubarHeight(activity);
        content.setBackgroundColor(0x00ffffff);
        //2､获取到setContentView放进去的View
        ViewGroup frame = (ViewGroup) content.getChildAt(0);
        mChildOfContent = frame.getChildAt(frame.getChildCount()-2);//XXX 特殊标题栏需要特殊处理
        //3､给Activity的xml布局设置View树监听，当布局有变化，如键盘弹出或收起时，都会回调此监听
        mChildOfContent.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            //4､软键盘弹起会使GlobalLayout发生变化
            public void onGlobalLayout() {
                if (isfirst) {
                    contentHeight = mChildOfContent.getHeight();//兼容华为等机型
                    isfirst = false;
                }
                //5､当前布局发生变化时，对Activity的xml布局进行重绘
                possiblyResizeChildOfContent();
            }
        });
        //6､获取到Activity的xml布局的放置参数
        frameLayoutParams = (ViewGroup.LayoutParams) mChildOfContent.getLayoutParams();
    }
    // 获取界面可用高度，如果软键盘弹起后，Activity的xml布局可用高度需要减去键盘高度
    private void possiblyResizeChildOfContent() {
        //1､获取当前界面可用高度，键盘弹起后，当前界面可用布局会减少键盘的高度
        int usableHeightNow = computeUsableHeight();
        //2､如果当前可用高度和原始值不一样
        if (usableHeightNow != usableHeightPrevious) {
            //3､获取Activity中xml中布局在当前界面显示的高度
            int usableHeightSansKeyboard = mChildOfContent.getRootView().getHeight();
            //4､Activity中xml布局的高度-当前可用高度
            int heightDifference = usableHeightSansKeyboard - usableHeightNow;
            //5､高度差大于屏幕1/4时，说明键盘弹出
            if (heightDifference > (usableHeightSansKeyboard/4)) {

                // 6､键盘弹出了，Activity的xml布局高度应当减去键盘高度
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
                    frameLayoutParams.height = usableHeightSansKeyboard - heightDifference + statusBarHeight;
                } else {
                    frameLayoutParams.height = usableHeightSansKeyboard - heightDifference;
                }

            } else {
                // keyboard probably just became hidden
                frameLayoutParams.height = usableHeightSansKeyboard;
            }
            //7､ 重绘Activity的xml布局
            mChildOfContent.requestLayout();
            usableHeightPrevious = usableHeightNow;
        }
    }
    private int computeUsableHeight() {
        Rect r = new Rect();
        mChildOfContent.getWindowVisibleDisplayFrame(r);
        // 全屏模式下：直接返回r.bottom，r.top其实是状态栏的高度
        return (r.bottom - r.top);// 全屏模式下： return r.bottom
    }

}


 ```