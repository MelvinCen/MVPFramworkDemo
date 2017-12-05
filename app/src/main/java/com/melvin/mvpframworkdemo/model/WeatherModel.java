package com.melvin.mvpframworkdemo.model;


import com.melvin.mvpframworkdemo.base.BaseModel;
import com.melvin.mvpframworkdemo.bean.WeatherBean;
import com.melvin.mvpframworkdemo.config.Constant;
import com.melvin.mvpframworkdemo.network.api.ApiConfig;

import io.reactivex.Observable;
import io.reactivex.Observer;

/**
 * @Author Melvin
 * @CreatedDate 2017/8/15 14:41
 * @Description ${TODO}
 * @Update by       MelvinCen
 * @Date 2017/8/15 14:41
 * @Description ${TODO}
 */

public class WeatherModel extends BaseModel {

    public static WeatherModel getInstance() {
        return getPresent(WeatherModel.class);
    }

    public void getWeather(String cityName, Observer<WeatherBean> observer) {
        addParams("cityname", cityName);
        addParams("key", Constant.JUHE_API_KEY_WEATHER);
        Observable observable = mApiService.getWeather(ApiConfig.QUERY_WEATHER, mParams);
        toSubscribe(observable, observer);

    }

}
