package com.melvin.mvpframworkdemo.ui.weather;


import com.melvin.mvpframworkdemo.base.BaseView;

/**
 * @Author Melvin
 * @CreatedDate 2017/8/15 14:26
 * @Description ${TODO}
 * @Update by       MelvinCen
 * @Date 2017/8/15 14:26
 * @Description ${TODO}
 */

public interface WeatherView<T> extends BaseView<T> {

    String getCity();

    void getDataEmpty(Throwable t);
}
