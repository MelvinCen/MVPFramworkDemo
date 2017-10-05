package com.melvin.mvpframworkdemo.base;

import java.lang.ref.WeakReference;

/**
 * @Author Melvin
 * @CreatedDate 2017/8/2 13:40
 * @Description ${Presenter基类}
 * @Update by       MelvinCen
 * @Date 2017/8/2 13:40
 * @Description ${TODO}
 */

public class BasePresenter<VIEW> {


    //用弱引用保存view
    private WeakReference<VIEW> attachedViews;

    //绑定view
    protected void attachView(VIEW view){
        attachedViews = new WeakReference<VIEW>(view);
    }
    //解绑view
    protected void detachView(){
        if (attachedViews != null) {
            attachedViews.clear();
            attachedViews = null;
        }
    }
    //获取绑定的view
    protected VIEW getView() {
        return isViewAttached() ? attachedViews.get() : null;
    }
    //判断view是否被绑定
    private boolean isViewAttached() {
        return null != attachedViews && null != attachedViews.get();
    }

}
