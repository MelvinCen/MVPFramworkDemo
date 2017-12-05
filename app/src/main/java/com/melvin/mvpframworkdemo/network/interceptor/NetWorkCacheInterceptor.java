package com.melvin.mvpframworkdemo.network.interceptor;

import com.melvin.mvpframworkdemo.MyApp;
import com.melvin.mvpframworkdemo.utils.NetworkStatusUtils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @Author Melvin
 * @CreatedDate 2017/12/2 22:48
 * @Description ${TODO}
 * @Update by       Melvin
 * @Date 2017/12/2 22:48
 * @Description ${TODO}
 */

public class NetWorkCacheInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);
        if (NetworkStatusUtils.isNetworkAvailable(MyApp.getAppContext())) {
            int maxAge = 0 * 60; // 有网络时 设置缓存超时时间0个小时
            response.newBuilder()
                    .header("Cache-Control", "public, max-age=" + maxAge)
                    .removeHeader("Pragma")
                    .build();
        } else { // 无网络时，设置超时为4周
            int maxStale = 60 * 60 * 24 * 28;
            response.newBuilder()
                    .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                    .removeHeader("Pragma") .build();
        }
        return response;
    }
}
