package com.melvin.mvpframworkdemo.network.interceptor;

import com.melvin.mvpframworkdemo.MyApp;
import com.melvin.mvpframworkdemo.utils.NetworkStatusUtils;

import java.io.IOException;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @Author Melvin
 * @CreatedDate 2017/12/2 22:46
 * @Description ${
 * Interceptor 和 NetWrokInterceptor
 * 前者肯定会执行，后者只在有网的情况下发出网络请求才执行
 * 所以在前者拦截request，后者拦截response
 * 但是此中拦截器改变请求头的方法只能缓存GET方式}
 * @Update by       Melvin
 * @Date 2017/12/2 22:46
 * @Description ${TODO}
 */

public class NoNetCacheInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        if (!NetworkStatusUtils.isNetworkAvailable(MyApp.getAppContext())) {
            request = request.newBuilder()
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build();
        }
        return chain.proceed(request);
    }
}
