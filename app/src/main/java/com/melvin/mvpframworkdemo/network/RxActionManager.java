package com.melvin.mvpframworkdemo.network;

import android.content.Context;

import io.reactivex.disposables.Disposable;

/**
 *
 */

public interface RxActionManager {

    /**
     * 将RxJava的disposable统一管理
     * 在BaseObserver中的onSubscribe中调用添加disposable
     * 在activity和fragment中实现该方法，用容器存储从上面添加的disposable，以便之后的取消
     *
     * @param disposable
     * @return
     */
    boolean addDisposable(Disposable disposable);

    /**
     * 将RxJava的disposable移除,即取消观察
     * 在activity和fragment销毁的时候调用了容器的清楚方法，所以不需要调用这个，只是实现了这个方法
     * 为了能在dialog取消时被我调用，其他地方需要添加的可以使用
     */
    void removeDisposable();

    //超时
//    void timeOut();

    Context getContext();

}
