package com.melvin.mvpframworkdemo.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.melvin.mvpframworkdemo.callback.BaseImpl;

import org.greenrobot.eventbus.EventBus;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import butterknife.ButterKnife;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * @Author Melvin
 * @CreatedDate 2017/8/3 11:52
 * @Description ${TODO}
 * @Update by       MelvinCen
 * @Date 2017/8/3 11:52
 * @Description ${TODO}
 */

public abstract class BaseActivity<P extends BasePresenter> extends AppCompatActivity implements BaseImpl {

    //简单的类名获取
    protected String TAG;
    //Rxjava调用生成的变量存储器
    private CompositeDisposable mCompositeDisposable;
    //Presenter变量
    protected P mPresenter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //创建Rxjava调用产生的变量存储器
        if (null == mCompositeDisposable) {
            mCompositeDisposable = new CompositeDisposable();
        }
        //创建presenter赋值mPresenter供调用
        if (null == mPresenter) {
            mPresenter = createPresenter();
        }
        //获取简单类名
        TAG = getClass().getSimpleName();
        //绑定布局
        setContentView(layoutRes());
        //绑定ButterKnife
        ButterKnife.bind(this);
        //利用注解注册EventBus
        if (this.getClass().isAnnotationPresent(BindEventBus.class)) {
            EventBus.getDefault().register(this);
        }
        //初始化view等
        initView();
    }

    /**
     * 创建一个presenter并绑定，调用presenter方法
     * @return
     */
    protected abstract P createPresenter();

    /**
     * 添加布局文件
     * @return
     */
    protected abstract int layoutRes();

    /**
     * 初始化view等
     */
    protected abstract void initView();

    /**
     * 为了绑定eventBus
     * 继承BaseActivity的类需要绑定的时候采用注解
     * 在类名上一行写：@BindEventBus
     * 使用@Subscribe接受事件
     */
    @Target(ElementType.TYPE) //接口、类、枚举、注解
    @Retention(RetentionPolicy.RUNTIME)// 注解会在class字节码文件中存在，在运行时可以通过反射获取到
    public @interface BindEventBus {
    }

    /**
     * Rxjava添加disposable
     * @param disposable
     * @return
     */
    @Override
    public boolean addDisposable(Disposable disposable) {
        if (null != mCompositeDisposable) {
            mCompositeDisposable.add(disposable);
        }
        return true;
    }

    /**
     * Rxjava删除disposable
     */
    @Override
    public void removeDisposable() {
        if (null != mCompositeDisposable) {
            mCompositeDisposable.clear();
        }
    }

    /**
     * 获取activity上下文
     * @return
     */
    @Override
    public Context getContext() {
        return this;
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //通过Rxjava调用产生的变量存储器来取消调用
        if (null != mCompositeDisposable) {
            mCompositeDisposable.clear();
        }
        //取消presenter和View的绑定
        if (null != mPresenter) {
            mPresenter.detachView();
        }
        //利用注解注销EventBus
        if (this.getClass().isAnnotationPresent(BindEventBus.class)) {
            EventBus.getDefault().unregister(this);
        }
    }
}
