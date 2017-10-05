package com.melvin.mvpframworkdemo.network.service;

import com.melvin.mvpframworkdemo.bean.WeatherBean;
import com.melvin.mvpframworkdemo.network.retrofit.BaseResponse;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

/**
 * @Author Melvin
 * @CreatedDate 2017/8/2 16:23
 * @Description ${在这里获取service}
 * @Update by       MelvinCen
 * @Date 2017/8/2 16:23
 * @Description ${TODO}
 */

public interface ApiService {

    @GET("{url}")
    Observable<BaseResponse<WeatherBean>> getWeather(@Path("url") String url, @QueryMap Map<String, String> params);

}
