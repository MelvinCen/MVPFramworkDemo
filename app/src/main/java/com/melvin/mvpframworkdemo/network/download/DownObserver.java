package com.melvin.mvpframworkdemo.network.download;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.melvin.mvpframworkdemo.utils.LogUtils;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * @Author Melvin
 * @CreatedDate 2017/12/3 12:59
 * @Description ${TODO}
 * @Update by       Melvin
 * @Date 2017/12/3 12:59
 * @Description ${TODO}
 */

public class DownObserver<ResponseBody extends okhttp3.ResponseBody> implements Observer<ResponseBody> {
    private DownLoadCallBack callBack;
    private Context          context;
    private String           path;
    private String           name;
    private String           key;

    public DownObserver(String key, String path, String name, DownLoadCallBack callBack, Context context) {
        this.key = key;
        this.path = path;
        this.name = name;
        this.callBack = callBack;
        this.context = context;
    }

    @Override
    public void onSubscribe(@NonNull Disposable d) {
        LogUtils.e("DownObserver的onSubscribe方法");
        if (callBack != null) {
            callBack.onStart(key);
        }
    }

    @Override
    public void onNext(@NonNull ResponseBody responseBody) {
        LogUtils.e("DownObserver的onNext方法");
        new DownLoadManager(callBack).writeResponseBodyToDisk(key, path, name, context, responseBody);
    }

    @Override
    public void onError(@NonNull final Throwable e) {
        LogUtils.e("DownObserver的onError方法" + e.toString());
        if (callBack != null) {
            if (checkMain()) {
                callBack.onError(e);
            } else {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onError(e);
                    }
                });
            }
        }
    }

    @Override
    public void onComplete() {
        if (callBack != null) {
            callBack.onCompleted();
        }
    }

    public static boolean checkMain() {
        return Thread.currentThread() == Looper.getMainLooper().getThread();
    }
}
