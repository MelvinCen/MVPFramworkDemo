package com.melvin.mvpframworkdemo.presenter;


import com.melvin.mvpframworkdemo.base.BasePresenter;
import com.melvin.mvpframworkdemo.bean.WeatherBean;
import com.melvin.mvpframworkdemo.network.RxActionManager;
import com.melvin.mvpframworkdemo.network.MyBaseObserver;
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

    public void getWeather(RxActionManager rxActionManager) {
                WeatherModel.getInstance().getWeather(getView().getCity(),
                        new MyBaseObserver<WeatherBean>(rxActionManager, "正在获取数据...") {
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

//        WeatherModel.getInstance().getWeather2(getView().getCity(), new MyBaseObserver<WeatherBean>(rxActionManager, "正在获取数据...") {
//            @Override
//            protected void onBaseNext(WeatherBean data) {
//                getView().onRequestSuccessData(data);
//            }
//
//            @Override
//            protected void onBaseError(Throwable t) {
//                super.onBaseError(t);
//                getView().getDataEmpty(t);
//            }
//        });

    }
}
