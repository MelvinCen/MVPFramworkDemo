package com.melvin.mvpframworkdemo.network;


import android.content.Context;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.melvin.mvpframworkdemo.BuildConfig;
import com.melvin.mvpframworkdemo.network.api.ApiConfig;
import com.melvin.mvpframworkdemo.network.interceptor.BaseInterceptor;
import com.melvin.mvpframworkdemo.network.interceptor.NetWorkCacheInterceptor;
import com.melvin.mvpframworkdemo.network.interceptor.NoNetCacheInterceptor;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.melvin.mvpframworkdemo.network.HttpsFactory.getHostnameVerifier;
import static com.melvin.mvpframworkdemo.network.HttpsFactory.getSslSocketFactory;

/**
 * @Author Melvin
 * @CreatedDate 2017/8/7 9:21
 * @Description ${TODO}
 * @Update by       MelvinCen
 * @Date 2017/8/7 9:21
 * @Description ${TODO}
 */

public class RetrofitClient {

    private static final int CONNECT_TIMEOUT   = 60;    //默认超时时间
    private static final int READ_TIMEOUT      = 60;    //默认超时时间
    private static final int WRITE_TIMEOUT     = 30;    //默认超时时间
    private static final long DEFAULT_DIR_CACHE = 100 * 1024 * 1024;//默认缓存大小100M

    private static Context        mContext;

    private        File           httpCacheDirectory;
    private Cache cache = null;
    //网络客户端
    private static OkHttpClient   okHttpClient;
    private static Retrofit retrofit;

    private Observable<ResponseBody> downObservable;
    private Map<Object, Observable<ResponseBody>> downMaps = new HashMap<Object, Observable<ResponseBody>>() {
    };


    private RetrofitClient() {
    }

    private RetrofitClient(Context context) {
        this(context, null);
    }

    private RetrofitClient(Context context, Map<String, String> headers) {

//        //如果是空则使用默认的baseUrl
//        if (TextUtils.isEmpty(url)) {
//            url = ApiConfig.BASE_URL;
//        }

        if (httpCacheDirectory == null) {
            httpCacheDirectory = new File(mContext.getCacheDir(), "cache");
        }

        try {
            if (cache == null) {
                cache = new Cache(httpCacheDirectory, DEFAULT_DIR_CACHE);
            }
        } catch (Exception e) {

        }

        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                .addInterceptor(new BaseInterceptor(headers))
                .addInterceptor(new NoNetCacheInterceptor())
                .addNetworkInterceptor(new NetWorkCacheInterceptor())
                .cache(cache)
                .sslSocketFactory(getSslSocketFactory())
                .hostnameVerifier(getHostnameVerifier());

        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLogger());
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addNetworkInterceptor(loggingInterceptor);

        }

        okHttpClient = builder.build();

        retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(ApiConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

    }

    //单例，静态内部类
    private static class SingletonHolder {
        private static RetrofitClient INSTANCE = new RetrofitClient(mContext);
    }

    //获取实例方法
    public static RetrofitClient getInstance(Context context) {
        if (context != null) {
            mContext = context;
        }
        return SingletonHolder.INSTANCE;
    }

    //添加header
    public static RetrofitClient getInstance(Context context, Map<String, String> headers) {
        if (context != null) {
            mContext = context;
        }
        return new RetrofitClient(context, headers);
    }

    //利用retrofit的create方法创建service
    public <T> T create(final Class<T> service) {
        if (service == null) {
            throw new RuntimeException("Api service is null!");
        }
        return retrofit.create(service);
    }

    public static OkHttpClient getOkhttpClient() {

        return okHttpClient;
    }

}
