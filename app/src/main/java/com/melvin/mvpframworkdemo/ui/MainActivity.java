package com.melvin.mvpframworkdemo.ui;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.view.KeyEvent;
import android.webkit.WebViewFragment;
import android.widget.FrameLayout;

import com.melvin.mvpframworkdemo.R;
import com.melvin.mvpframworkdemo.base.BaseActivity;
import com.melvin.mvpframworkdemo.base.BaseFragment;
import com.melvin.mvpframworkdemo.presenter.WeatherPresenter;
import com.melvin.mvpframworkdemo.ui.weather.WeatherFragment;
import com.melvin.mvpframworkdemo.utils.LogUtils;


public class MainActivity extends BaseActivity<WeatherPresenter> {


    private FrameLayout mFlContent;
    private FragmentManager mFragmentManager;

    private WebViewFragment mWebViewFragment;

    private BaseFragment mCurrentFragment;//目前显示的fragment

    @Override
    protected WeatherPresenter createPresenter() {
        return null;
    }

    @Override
    protected int layoutRes() {
        LogUtils.e("测试logUtils");
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {


        mFragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        WeatherFragment weatherFragment = new WeatherFragment();
        fragmentTransaction.add(R.id.fragment_content,weatherFragment,"weather");
        fragmentTransaction.commit();

        mCurrentFragment = weatherFragment;

    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (mCurrentFragment instanceof WeatherFragment) {
                boolean onKeyDownResult = mCurrentFragment.onKeyDown(keyCode, event);
                if (onKeyDownResult) {
                    return true;
                } else {
                    return super.onKeyDown(keyCode, event);
                }
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
