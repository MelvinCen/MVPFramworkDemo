package com.melvin.mvpframworkdemo.network;


import com.melvin.mvpframworkdemo.network.progressdialog.ProgressCancelListener;
import com.melvin.mvpframworkdemo.network.progressdialog.ProgressDialogHandler;
import com.melvin.mvpframworkdemo.utils.LogUtils;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;


/**
 *封装Observer的基类
 */

public abstract class BaseObserver<T> implements Observer<T> {

    private static final String TAG = "BaseObserver";

    protected abstract void onBaseError(Throwable t);

    protected abstract void onBaseNext(T data);

    protected abstract boolean isNeedProgressDialog();

    protected abstract String getTitleMsg();

    private ProgressDialogHandler mProgressDialogHandler;
    private RxActionManager       mRxActionManager;

    public BaseObserver(RxActionManager rxActionManager) {
        mRxActionManager = rxActionManager;
        if (null != mRxActionManager) {
            if (null == mProgressDialogHandler) {
                LogUtils.e(" BaseObserver初始化创建handler是在线程："+Thread.currentThread().getName());
                mProgressDialogHandler = new ProgressDialogHandler(rxActionManager.getContext(), true);
                //取消progressdialog时取消请求
                if (isNeedProgressDialog()) {
                    mProgressDialogHandler.setOnProgressCancelListener(new ProgressCancelListener() {
                        @Override
                        public void onCancelProgress() {
                            mRxActionManager.removeDisposable();
                        }
                    });
                }
            }
        }
    }

    private void showProgressDialog() {
        if (mProgressDialogHandler != null) {
            mProgressDialogHandler.obtainMessage(ProgressDialogHandler.SHOW_PROGRESS_DIALOG, getTitleMsg()).sendToTarget();
        }
    }

    private void dismissProgressDialog() {
        if (mProgressDialogHandler != null) {
            mProgressDialogHandler.obtainMessage(ProgressDialogHandler.DISMISS_PROGRESS_DIALOG).sendToTarget();
            mProgressDialogHandler = null;
        }
    }

    @Override
    public void onSubscribe(Disposable d) {
        //显示进度条
        if (isNeedProgressDialog()) {
            showProgressDialog();
        }

        if (null != mRxActionManager) {
            if (null != d) {
                mRxActionManager.addDisposable(d);
            }
        }
    }

    @Override
    public void onNext(T value) {
        //成功
        LogUtils.e("http is onNext" + value);
        if (null != value) {
            onBaseNext(value);
        }
    }

    @Override
    public void onError(Throwable e) {
        //关闭进度条
        LogUtils.e("http is onError");
        if (isNeedProgressDialog()) {
            dismissProgressDialog();
        }
        onBaseError(e);
    }

    @Override
    public void onComplete() {
        //关闭进度条
        if (isNeedProgressDialog()) {
            dismissProgressDialog();
        }
    }


}