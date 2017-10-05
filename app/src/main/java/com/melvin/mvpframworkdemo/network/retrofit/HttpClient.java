package com.melvin.mvpframworkdemo.network.retrofit;


import com.melvin.mvpframworkdemo.BuildConfig;
import com.melvin.mvpframworkdemo.MyApp;
import com.melvin.mvpframworkdemo.utils.LogUtils;
import com.melvin.mvpframworkdemo.utils.NetworkAvailableUtils;

import java.io.File;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * @Author Melvin
 * @CreatedDate 2017/8/7 9:21
 * @Description ${TODO}
 * @Update by       MelvinCen
 * @Date 2017/8/7 9:21
 * @Description ${TODO}
 */

public class HttpClient {

    private static final int CONNECT_TIMEOUT   = 60;    //默认超时时间
    private static final int READ_TIMEOUT      = 60;    //默认超时时间
    private static final int WRITE_TIMEOUT     = 30;    //默认超时时间
    private static final long DEFAULT_DIR_CACHE = 100 * 1024 * 1024;//默认缓存大小100M

    private static Map<String,String> commonHeaderMap = new HashMap<>();

    public static final String TOKEN = "token";


    private HttpClient() {
    }

    public static OkHttpClient getOkhttpClient() {

//        File cacheFile = FileUtils.getDiskCacheDir(MyApp.getInstance().getApplicationContext(), "cacheData");
        File cacheFile = new File(MyApp.getInstance().getApplicationContext().getCacheDir(), "cache");
        LogUtils.e("cacheFile = " + cacheFile);
        Cache cache = new Cache(cacheFile, DEFAULT_DIR_CACHE);//google建议放到这里

        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                .addInterceptor(getInterceptor())
                .addNetworkInterceptor(getNetWrokInterceptor())
                .cache(cache)
                .sslSocketFactory(getSslSocketFactory())
                .hostnameVerifier(getHostnameVerifier());

        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLogger());
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addNetworkInterceptor(loggingInterceptor);

        }
        return builder.build();
    }


    /**
     * ssl摘自网络，关闭了所有的SSL认证检查
     * @return
     */
    private static SSLSocketFactory getSslSocketFactory(){
        X509TrustManager xtm = new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        };

        SSLContext sslContext = null;

        try {
            sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null,new TrustManager[]{xtm},new SecureRandom());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }

        return sslContext.getSocketFactory();

    }

    private static HostnameVerifier getHostnameVerifier(){
        HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };

        return DO_NOT_VERIFY;
    }

    /**
     * Interceptor 和 NetWrokInterceptor
     * 前者肯定会执行，后者只在有网的情况下发出网络请求才执行
     * 所以在前者拦截request，后者拦截response
     * 但是此中拦截器改变请求头的方法只能缓存GET方式
     * @return
     */
    public static Interceptor getInterceptor(){
        return  new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                if (!NetworkAvailableUtils.isNetworkAvailable(MyApp.getAppContext())) {
                    request = request.newBuilder()
                            .cacheControl(CacheControl.FORCE_CACHE)
                            .build();
                }
                return chain.proceed(request);

            }
        };
    }

    public static Interceptor getNetWrokInterceptor(){
        return  new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                Response response = chain.proceed(request);
                if (NetworkAvailableUtils.isNetworkAvailable(MyApp.getAppContext())) {
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
        };
    }

    //---修改了系统方法--这是设置在多长时间范围内获取缓存里面
    public static final CacheControl FORCE_CACHE1 = new CacheControl.Builder()
            .onlyIfCached()
            .maxStale(7, TimeUnit.SECONDS)//这里是7s，CacheControl.FORCE_CACHE--是int型最大值,不能用默认的CacheControl.FORCE_CACHE--是int型最大值，就相当于断网的情况下，一直不清除缓存
            .build();


    public static Interceptor getTokenInterceptor(){
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request newRequest = chain.request();
                if (commonHeaderMap != null) {
                    newRequest.newBuilder()
                            .addHeader(TOKEN, commonHeaderMap.get(TOKEN))
                            .build();
                    return chain.proceed(newRequest);
                } else {
                    return chain.proceed(newRequest);
                }

            }
        };
    }

    public static void addCommonHeader(Map<String, String> map){
        commonHeaderMap = map;
    }
}
