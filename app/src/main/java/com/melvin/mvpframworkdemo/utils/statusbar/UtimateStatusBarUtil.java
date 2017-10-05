package com.melvin.mvpframworkdemo.utils.statusbar;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

/**
 * @Author Melvin
 * @CreatedDate 2017/8/8 10:29
 * @Description ${沉浸式状态导航栏的工具类，有四种可能；
 * 1，设置自定义颜色的状态栏和导航栏
 * 2，半透明的状态栏和导航栏
 * 3，完全透明的状态栏和导航栏，这种就是网上说的沉浸式
 * 4，隐藏状态栏和导航栏}
 */

public class UtimateStatusBarUtil {

    private Activity mActivity;

    public UtimateStatusBarUtil(Activity activity) {
        mActivity = activity;
    }

    /**
     * 设置自定义颜色的状态栏和导航栏
     * @param color
     * @param alpha
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void setColor(@ColorInt int color, int alpha){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = mActivity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            int alphaColor = alpha == 0 ? color : calculateColor(color,alpha);
            window.setStatusBarColor(alphaColor);
            window.setNavigationBarColor(alphaColor);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = mActivity.getWindow();
            //把状态栏透明掉
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            int alphaColor = alpha == 0 ? color : calculateColor(color,alpha);
            ViewGroup decorView = (ViewGroup) window.getDecorView();
            //添加一个view到状态栏的位置
            decorView.addView(createStatusBarView(mActivity,color));
            //如果存在导航栏也同样把导航栏透明掉，然后添加一个view
            if (navigationBarExist(mActivity)) {
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
                decorView.addView(createNavBarView(mActivity, alphaColor));
            }
            //避免子view的内容覆盖到状态栏和导航栏上面
            setRootView(mActivity,true);
        }
    }

    /**
     * 重载方法，默认透明度为0，就是全色
     * @param color
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void setColor(@ColorInt int color){
        setColor(color,0);
    }

    /**
     * 设置透明或者半透明的状态栏和导航栏
     * @param color
     * @param alpha
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void setTransparentBar(@ColorInt int color, int alpha){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = mActivity.getWindow();
            View decorView = window.getDecorView();
            int options = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(options);

            int finalColor = alpha == 0 ? Color.TRANSPARENT :
                    Color.argb(alpha, Color.red(color), Color.green(color), Color.blue(color));

            window.setNavigationBarColor(finalColor);
            window.setStatusBarColor(finalColor);

        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = mActivity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            ViewGroup decorView = (ViewGroup) window.getDecorView();
            int finalColor = alpha == 0 ? Color.TRANSPARENT :
                    Color.argb(alpha, Color.red(color), Color.green(color), Color.blue(color));

            decorView.addView(createStatusBarView(mActivity, finalColor));

            if (navigationBarExist(mActivity)) {
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
                decorView.addView(createNavBarView(mActivity,finalColor));
            }
        }
    }

    /**
     * 完全透明的状态栏和导航栏，这种就是网上说的沉浸式
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void setImmersionBar(){
        setTransparentBar(Color.TRANSPARENT,0);
    }

    /**
     * 隐藏状态栏和导航栏
     * 备注：最好在activity的onWindowFocusChanged回调方法中去调用，此时才是真正的visible
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void setHintBar(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            View decorView = mActivity.getWindow().getDecorView();
            int options = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

            decorView.setSystemUiVisibility(options);
        }
    }

    /**
     * drawerLayout的自定义颜色状态栏和导航栏
     * 备注：需要在drawerLayout的布局下的住view的布局中添加android:fitsSystemWindows="true"
     * @param color
     * @param alpha
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void setColorBarForDrawer(@ColorInt int color, int alpha) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = mActivity.getWindow();
            ViewGroup decorView = (ViewGroup) window.getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            if (navigationBarExist(mActivity)) {
                option = option | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
            }
            decorView.setSystemUiVisibility(option);
            window.setNavigationBarColor(Color.TRANSPARENT);
            window.setStatusBarColor(Color.TRANSPARENT);
            int alphaColor = alpha == 0 ? color : calculateColor(color, alpha);
            decorView.addView(createStatusBarView(mActivity, alphaColor), 0);
            if (navigationBarExist(mActivity)) {
                decorView.addView(createNavBarView(mActivity, alphaColor), 1);
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = mActivity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            ViewGroup decorView = (ViewGroup) window.getDecorView();
            int alphaColor = alpha == 0 ? color : calculateColor(color, alpha);
            decorView.addView(createStatusBarView(mActivity, alphaColor), 0);
            if (navigationBarExist(mActivity)) {
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
                decorView.addView(createNavBarView(mActivity, alphaColor), 1);
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void setColorBarForDrawer(@ColorInt int color) {
        setColorBarForDrawer(color, 0);
    }

    /**
     * 判断navigationBar是否存在
     * @return
     */
    private boolean navigationBarExist(Activity activity){
        WindowManager windowManager = activity.getWindowManager();
        Display defaultDisplay = windowManager.getDefaultDisplay();
        DisplayMetrics realDisplayMetrics = new DisplayMetrics();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            defaultDisplay.getRealMetrics(realDisplayMetrics);
        }

        int realHeight = realDisplayMetrics.heightPixels;
        int realWidth = realDisplayMetrics.widthPixels;

        DisplayMetrics displayMetrics = new DisplayMetrics();
        defaultDisplay.getMetrics(displayMetrics);

        int displayHeight = displayMetrics.heightPixels;
        int displayWidth = displayMetrics.widthPixels;

        return (realWidth - displayWidth) > 0 || (realHeight - displayHeight) > 0;

    }

    /**
     * 避免子view的内容覆盖到状态栏和导航栏上面
     * @param activity
     * @param fit
     */
    private void setRootView(Activity activity,boolean fit){
        ViewGroup parent = (ViewGroup) activity.findViewById(android.R.id.content);

        for (int i = 0, count = parent.getChildCount(); i < count; i++) {
            View childView = parent.getChildAt(i);
            if (childView instanceof ViewGroup) {
                childView.setFitsSystemWindows(fit);
                ((ViewGroup) childView).setClipToPadding(fit);
            }
        }
    }

    /**
     * 计算颜色值
     * @param color
     * @param alpha
     * @return
     */
    @ColorInt
    private int calculateColor(@ColorInt int color, int alpha) {
        float a = 1 - alpha / 255f;
        int red = color >> 16 & 0xff;
        int green = color >> 8 & 0xff;
        int blue = color & 0xff;
        red = (int) (red * a + 0.5);
        green = (int) (green * a + 0.5);
        blue = (int) (blue * a + 0.5);
        return 0xff << 24 | red << 16 | green << 8 | blue;
    }

    /**
     * 生成一个和状态栏同样高度的view
     * @param context
     * @param color
     * @return
     */
    private View createStatusBarView(Context context,@ColorInt int color){
        View mStatusBarTintView = new View(context);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getStatusBarHeight(context));
        layoutParams.gravity = Gravity.TOP;
        mStatusBarTintView.setLayoutParams(layoutParams);
        mStatusBarTintView.setBackgroundColor(color);
        return mStatusBarTintView;


    }

    /**
     * 生成一个和导航栏同样高度的view
     * @param context
     * @param color
     * @return
     */
    private View createNavBarView(Context context,@ColorInt int color){
        View mNavBarTintView = new View(context);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams
                (FrameLayout.LayoutParams.MATCH_PARENT, getNavigationHeight(context));
        params.gravity = Gravity.BOTTOM;
        mNavBarTintView.setLayoutParams(params);
        mNavBarTintView.setBackgroundColor(color);
        return mNavBarTintView;
    }

    /**
     * 获取状态栏的高度，目前貌似有其他方法可以获取
     * @param context
     * @return
     */
    private int getStatusBarHeight(Context context) {
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        return context.getResources().getDimensionPixelSize(resourceId);
    }

    /**
     * 获取导航栏的高度，目前貌似有其他方法可以获取
     * @param context
     * @return
     */
    private int getNavigationHeight(Context context){
        int resourceId = context.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        return context.getResources().getDimensionPixelSize(resourceId);
    }
}
