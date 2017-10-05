package com.melvin.mvpframworkdemo.network.retrofit;


import com.melvin.mvpframworkdemo.utils.LogUtils;
import com.orhanobut.hawk.Hawk;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

/**
 * @Author Melvin
 * @CreatedDate 2017/8/18 14:23
 * @Description ${TODO}
 * @Update by       MelvinCen
 * @Date 2017/8/18 14:23
 * @Description ${TODO}
 */

public class RetrofitCache {

    public static <T> Observable<T> load(final String cacheKey,
                                         Observable<T> fromNetWork, boolean isSave,
                                         boolean forceRefresh){
        LogUtils.e("RetrofitCache -- load");

        Observable<T> fromCache = Observable.create(new ObservableOnSubscribe<T>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<T> emitter) throws Exception {
                LogUtils.e("RetrofitCache -- load -- fromCache");
                //这里需要从本地数据库获取数据
                T cache = Hawk.get(cacheKey);
                if (cache != null) {
                    LogUtils.e("RetrofitCache -- load -- cache != null");
                    emitter.onNext(cache);
                } else {
                    LogUtils.e("RetrofitCache -- load -- cache = null");
                    emitter.onComplete();
                }
            }
        });

        if (isSave) {

            fromNetWork.map(new Function<T, T>() {
                @Override
                public T apply(@NonNull T t) throws Exception {
                    //把数据存到本地数据库
                    Hawk.put(cacheKey,t);
                    return t;
                }
            });
        }

        if (forceRefresh) {
            return fromNetWork;
        } else {
            LogUtils.e("RetrofitCache -- load -- forceRefresh == false");
            LogUtils.e("RetrofitCache -- load -- forceRefresh == false -- fromCache = " + fromCache);
//            return fromNetWork;
            return Observable.concat(fromCache,fromNetWork).filter(new Predicate<T>() {
                @Override
                public boolean test(@NonNull T t) throws Exception {
                    return t!=null;
                }
            }).firstElement().toObservable().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        }

    }
}
