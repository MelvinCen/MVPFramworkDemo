package com.melvin.mvpframworkdemo.network;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;

/**
 * @Author Melvin
 * @CreatedDate 2017/12/2 22:08
 * @Description ${TODO}
 * @Update by       Melvin
 * @Date 2017/12/2 22:08
 * @Description ${TODO}
 */

public class RxTransforms {

    public static ObservableTransformer getSchedulersTransformer(){
        return new ObservableTransformer() {
            @Override
            public ObservableSource apply(@NonNull Observable upstream) {
                return upstream.subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

}
