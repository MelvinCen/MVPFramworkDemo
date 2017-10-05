package com.melvin.mvpframworkdemo.presenter;


import com.melvin.mvpframworkdemo.base.BasePresenter;
import com.melvin.mvpframworkdemo.bean.WeatherBean;
import com.melvin.mvpframworkdemo.callback.BaseImpl;
import com.melvin.mvpframworkdemo.callback.MyBaseObserver;
import com.melvin.mvpframworkdemo.model.WeatherModel;
import com.melvin.mvpframworkdemo.ui.weather.WeatherView;

/**
 * @Author Melvin
 * @CreatedDate 2017/8/15 14:30
 * @Description ${TODO}
 * @Update by       MelvinCen
 * @Date 2017/8/15 14:30
 * @Description ${TODO}
 */

public class WeatherPresenter extends BasePresenter<WeatherView<WeatherBean>> {

    public WeatherPresenter(WeatherView<WeatherBean> weatherView) {
        attachView(weatherView);
    }

    public void getWeather(BaseImpl baseImpl){
        WeatherModel.getInstance().getWeather(getView().getCity(),
                new MyBaseObserver<WeatherBean>(baseImpl,"正在获取数据...") {
            @Override
            protected void onBaseNext(WeatherBean data) {
                getView().onRequestSuccessData(data);
            }

            @Override
            protected void onBaseError(Throwable t) {
                super.onBaseError(t);

                getView().getDataEmpty(t);
            }
        });

    }
}
