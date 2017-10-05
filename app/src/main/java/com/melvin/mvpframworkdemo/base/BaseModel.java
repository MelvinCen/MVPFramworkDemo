package com.melvin.mvpframworkdemo.base;

import android.text.TextUtils;

import com.melvin.mvpframworkdemo.network.retrofit.BaseRetrofit;
import com.melvin.mvpframworkdemo.network.service.ApiService;
import com.melvin.mvpframworkdemo.utils.LogUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author Melvin
 * @CreatedDate 2017/8/3 15:27
 * @Description ${TODO}
 * @Update by       MelvinCen
 * @Date 2017/8/3 15:27
 * @Description ${TODO}
 */

public class BaseModel extends BaseRetrofit {

    private static final String TAG = "BaseModel";

    protected ApiService mApiService;

    protected Map<String, String> mParams = new HashMap<>();

    public BaseModel() {
        super();
        mApiService = mRetrofit.create(ApiService.class);
    }


    @Override
    protected Map<String, String> getCommonMap() {
        Map<String, String> commonMap = new HashMap<>();
        return commonMap;
    }

    /**
     * 添加单个参数
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
