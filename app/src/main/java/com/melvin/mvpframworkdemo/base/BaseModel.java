package com.melvin.mvpframworkdemo.base;

import android.text.TextUtils;

import com.melvin.mvpframworkdemo.MyApp;
import com.melvin.mvpframworkdemo.network.HttpFunction;
import com.melvin.mvpframworkdemo.network.RetrofitClient;
import com.melvin.mvpframworkdemo.network.RxTransforms;
import com.melvin.mvpframworkdemo.network.api.ApiService;
import com.melvin.mvpframworkdemo.utils.LogUtils;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Observer;

/**
 * @Author Melvin
 * @CreatedDate 2017/8/3 15:27
 * @Description ${TODO}
 * @Update by       MelvinCen
 * @Date 2017/8/3 15:27
 * @Description ${TODO}
 */

public class BaseModel {

    private static final String TAG = "BaseModel";

    protected ApiService mApiService;

    protected Map<String, String> mParams = new HashMap<>();

    public BaseModel() {
        mApiService = RetrofitClient.getInstance(MyApp.getAppContext()).create(ApiService.class);
    }

    protected <T> void toSubscribe(Observable observable, Observer<T> observer) {
        LogUtils.e("toSubscribe");
        observable.map(new HttpFunction())
                .compose(RxTransforms.getSchedulersTransformer())
                .subscribe(observer);

        //缓存数据,暂时不做
        // RetrofitCache.load(cacheKey, observableFromNetwork, isSave, forceRefresh).subscribe(observer);
    }


    /**
     * 本方法目的是提供给继承BaseModel类的子类获取其实例对象的方法
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

    /**
     * 添加单个参数
     *
     * @param key
     * @param value
     */
    protected void addParams(String key, String value) {
        if (TextUtils.isEmpty(key)) {
            LogUtils.e("the key is null");
            return;
        }
        mParams.put(key, value);
    }

    /**
     * 添加多个参数，以map的形式
     *
     * @param params
     */
    protected void addParams(Map<String, String> params) {
        if (null == params) {
            LogUtils.e("the map is null");
            return;
        }
        mParams.putAll(params);
    }

}
