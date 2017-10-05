package com.melvin.mvpframworkdemo.network.retrofit;


import com.melvin.mvpframworkdemo.utils.LogUtils;

import io.reactivex.functions.Function;

/**
 * 变换: 将服务器返回的原始数据剥离成我们最终想要的数据
 * 将BaseResponse<T> 转换成 T
 */

public class HttpFunction<T> implements Function<BaseResponse<T>, T> {

    @Override
    public T apply(BaseResponse<T> response) throws Exception {
        LogUtils.e("response: " + response.toString());
        if (!response.isRequestSuccess()) {
            throw new ApiException(response.getStatus(), String.valueOf(response.getMsg()));
        }
        return response.getData();
    }
}