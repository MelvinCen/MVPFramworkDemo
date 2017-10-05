package com.melvin.mvpframworkdemo.network.retrofit;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.melvin.mvpframworkdemo.network.api.ApiConfig;
import com.melvin.mvpframworkdemo.network.convert.CustomGsonConverterFactory;
import com.melvin.mvpframworkdemo.utils.LogUtils;

import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * @Author Melvin
 * @CreatedDate 2017/8/4 11:02
 * @Description ${TODO}
 * @Update by       MelvinCen
 * @Date 2017/8/4 11:02
 * @Description ${TODO}
 */

public abstract class BaseRetrofit {

    protected Retrofit mRetrofit;

    public BaseRetrofit() {
        if (mRetrofit == null) {
            OkHttpClient client = HttpClient.getOkhttpClient();
            mRetrofit = new Retrofit.Builder()
                    .baseUrl(ApiConfig.BASE_URL)
                    .addConverterFactory(CustomGsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(client)
                    .build();

        }
    }

    /**
     * 公共参数，添加请求头之类的(这边暂时没想好,可能会去掉采用接口形式)
     *
     * @return
     */
    protected abstract Map<String, String> getCommonMap();


    protected <T> void toSubscribe(Observable observable, Observer<T> observer, String cacheKey, boolean isSave, boolean forceRefresh) {
        LogUtils.e("toSubscribe");
        observable.map(new HttpFunction())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);


        //缓存数据
        // RetrofitCache.load(cacheKey, observableFromNetwork, isSave, forceRefresh).subscribe(observer);
    }


    /**
     * 提供给其他类使用model类时，获取model类的实例对象，让model类提供静态方法暴露本身
     *
     * @param cls 类名
     * @return
     */

    protected static <T> T getPresent(Class<T> cls) {
        T instance = null;
        try {
            instance = cls.newInstance();
            if (instance == null) {
                return null;
            }
            return instance;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

}
