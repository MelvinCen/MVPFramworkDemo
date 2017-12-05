package com.melvin.mvpframworkdemo.network.interceptor;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @Author Melvin
 * @CreatedDate 2017/12/2 14:30
 * @Description ${添加拦截器，可以用来添加请求头}
 * @Update by       Melvin
 * @Date 2017/12/2 14:30
 * @Description ${TODO}
 */

public class BaseInterceptor implements Interceptor {

    private Map<String, String> headers;

    public BaseInterceptor(Map<String, String> headers) {
        this.headers = headers;
    }
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder builder = chain.request()
                .newBuilder();
        if (headers != null && headers.size() > 0) {
            Set<String> keys = headers.keySet();
            for (String headerKey : keys) {
                builder.addHeader(headerKey,headers.get(headerKey)).build();
            }
        }
        return chain.proceed(builder.build());
    }
}
