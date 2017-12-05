package com.melvin.mvpframworkdemo.network.api;

import com.melvin.mvpframworkdemo.bean.WeatherBean;
import com.melvin.mvpframworkdemo.network.BaseResponse;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * @Author Melvin
 * @CreatedDate 2017/8/2 16:23
 * @Description ${在这里获取service}
 * @Update by       MelvinCen
 * @Date 2017/8/2 16:23
 * @Description ${TODO}
 */

public interface ApiService {

    @GET()
    Observable<BaseResponse<WeatherBean>> getWeather(@Url() String url, @QueryMap Map<String, String> params);

    @Streaming
    @GET
    Observable<ResponseBody> downloadFile(@Url String fileUrl);

    @GET()
    Observable<ResponseBody> geServertVersionCode(@Url() String url);

}
