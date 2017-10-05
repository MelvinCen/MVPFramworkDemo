package com.melvin.mvpframworkdemo.ui.weather;

import android.content.Context;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.melvin.mvpframworkdemo.R;
import com.melvin.mvpframworkdemo.base.BaseFragment;
import com.melvin.mvpframworkdemo.bean.WeatherBean;
import com.melvin.mvpframworkdemo.presenter.WeatherPresenter;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @Author Melvin
 * @CreatedDate 2017/8/15 14:24
 * @Description ${TODO}
 * @Update by       MelvinCen
 * @Date 2017/8/15 14:24
 * @Description ${TODO}
 */

public class WeatherFragment extends BaseFragment<WeatherPresenter> implements WeatherView<WeatherBean> {

    @BindView(R.id.et_month_weather)
    EditText     mEtMonthWeather;
    @BindView(R.id.btn_search_weather)
    Button       mBtnSearchWeather;
    @BindView(R.id.ll_empty_weather)
    LinearLayout mLlEmptyWeather;
    @BindView(R.id.tv_content_weather)
    TextView     mTvContentWeather;

    @Override
    protected int layoutRes() {
        return R.layout.fragment_weather;
    }

    @Override
    protected void initView(View view) {

    }

    @Override
    protected WeatherPresenter createPresenter() {
        return new WeatherPresenter(this);
    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }


    @Override
    public String getCity() {
        return mEtMonthWeather.getText().toString().trim();
    }

    @Override
    public void getDataEmpty(Throwable t) {
        mTvContentWeather.setVisibility(View.INVISIBLE);
        mTvContentWeather.setText("没有查找到数据");

    }

    @Override
    public void onRequestSuccessData(WeatherBean data) {
        mTvContentWeather.setVisibility(View.VISIBLE);
        mTvContentWeather.setText(data.getToday().getTemperature());
    }

    @OnClick(R.id.btn_search_weather)
    public void onClick() {
        //点击后隐藏键盘
        if (getBaseActivity() != null) {
            InputMethodManager imm = (InputMethodManager) getBaseActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        }
        //获取天气情况的数据
        mPresenter.getWeather(this);
    }

}
