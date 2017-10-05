package com.melvin.mvpframworkdemo.network.retrofit;

/**
 * @Author Melvin
 * @CreatedDate 2017/8/4 14:33
 * @Description ${数据回调接口，由view实现，presenter调用；可以继续添加失败数据方法等附加方法}
 * @Update by       MelvinCen
 * @Date 2017/8/4 14:33
 * @Description ${TODO}
 */
public interface BaseRequestContract<T> {
    //数据成功回调
    void onRequestSuccessData(T data);


}