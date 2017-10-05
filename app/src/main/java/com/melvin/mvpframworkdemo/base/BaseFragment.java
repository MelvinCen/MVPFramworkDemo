package com.melvin.mvpframworkdemo.base;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.melvin.mvpframworkdemo.callback.BaseImpl;
import com.melvin.mvpframworkdemo.utils.LogUtils;

import java.lang.ref.WeakReference;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * @Author Melvin
 * @CreatedDate 2017/8/3 13:34
 * @Description fragment生命周期调用
 * setUserVisibleHint(false) -> onAttach -> onCreate -> setUserVisibleHint(true)  -> onCreateView ->
 * onViewCreated() -> onActivityCreated ->.... -> onDetach
 * @Update by       MelvinCen
 * @Date 2017/8/3 13:34
 * @Description ${TODO}
 */

public abstract class BaseFragment<P extends BasePresenter> extends Fragment implements BaseImpl {

    private static final String TAG = BaseFragment.class.getSimpleName();

    //判断fragment可见、不可见、重用的flag
    private boolean isFragmentVisible;
    private boolean isReuseView;
    private boolean isFirstVisible;
    //fragment的根view
    private View rootView;

    //保存activity
    public    WeakReference<Activity> mActivityWeakRef;
    //保存Disposable
    private   CompositeDisposable     mCompositeDisposable;
    //保存presenter
    protected P                       mPresenter;
    private   Unbinder                mUnbinder;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        //保存activity
        mActivityWeakRef = new WeakReference((Activity) context);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //创建Rxjava调用产生的变量存储器
        if (null == mCompositeDisposable) {
            mCompositeDisposable = new CompositeDisposable();
        }
        //创建presenter赋值mPresenter供调用
        if (null == mPresenter) {
            mPresenter = createPresenter();
        }
        //初始化判断fragment可见、不可见、重用的flag的变量
        initVariable();

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        LogUtils.e("isFirstVisible = " + isFirstVisible + " isFragmentVisible = " + isFragmentVisible + " rootView = " + rootView);

        //如果在生命周期之外调用，或者在根视图创建之前调用直接返回不要浪费时间
        //因为setUserVisibleHint在onAttach之前会被调用一次
        if (rootView == null) return;

        //第一次可见
        if (isVisibleToUser && isFirstVisible) {
            LogUtils.e("setUserVisibleHint中去执行onFragmentFirstVisible");
            isFirstVisible = false;
            onFragmentFirstVisible();
        }

        //除了第一次可见之外，由其他不可见 到 可见时触发
        if (isVisibleToUser) {
            LogUtils.e("setUserVisibleHint中去执行onFragmentVisibleChange");
            onFragmentVisibleChange(true);
            isFragmentVisible = true;
            return;

        }

        //由可见 到 不可见时触发
        if (isFragmentVisible) {
            onFragmentVisibleChange(false);
            isFragmentVisible = false;
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (rootView == null) {
            //绑定视图
            View view = inflater.inflate(layoutRes(), container, false);
            //依赖注入,因为需要根view，因此在这个方法中初始化
            mUnbinder = ButterKnife.bind(this, view);

            //初始化视图,默认值
            initView(view);

            return view;
        } else {
            return rootView;
        }

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        if (rootView == null) {
            rootView = view;
            if (getUserVisibleHint()) {
                if (isFirstVisible) {
                    //可见并且是第一次可见的时候
                    isFirstVisible = false;
                    LogUtils.e("onViewCreated中去执行onFragmentFirstVisible");
                    onFragmentFirstVisible();
                }
                LogUtils.e("onViewCreated中去执行onFragmentVisibleChange");
                onFragmentVisibleChange(true);
                isFragmentVisible = true;
            }
        }
        super.onViewCreated(isReuseView && rootView != null? rootView :view, savedInstanceState);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //重置变量
        initVariable();

        //页面销毁时销毁view，释放资源等操作
        destoryViewAndThing();

        //解绑ButterKnife
        mUnbinder.unbind();

        //取消RxJava
        if (null != mCompositeDisposable) {
            mCompositeDisposable.clear();
        }

       //接触view和presenter的绑定
        if (null != mPresenter) {
            mPresenter.detachView();
        }
    }

    @Override
    public boolean addDisposable(Disposable disposable) {
        if (null != mCompositeDisposable) {
            mCompositeDisposable.add(disposable);
        }
        return true;
    }

    @Override
    public void removeDisposable() {
        if (null != mCompositeDisposable) {
            mCompositeDisposable.clear();
        }
    }

    /**
     * 该抽象方法就是 onCreateView中需要的layoutID,必须覆写
     * @return
     */
    protected abstract int layoutRes();

    /**
     *该抽象方法就是 初始化view，不一定要覆写
     */
    protected void initView(View view){}

    /**
     * 创建presenter
     * @return
     */
    protected abstract P createPresenter();

    /**
     * 页面销毁时销毁view，释放资源等操作,不一定要覆写
     */
    protected void destoryViewAndThing(){}

    /**
     * 在fragment首次可见时回调，可在这里进行加载数据，保证只在第一次打开Fragment时才会加载数据，
     * 这样就可以防止每次进入都重复加载数据
     * 该方法会在 onFragmentVisibleChange() 之前调用，所以第一次打开时，可以用一个全局变量表示数据下载状态，
     * 然后在该方法内将状态设置为下载状态，接着去执行下载的任务
     * 最后在 onFragmentVisibleChange() 里根据数据下载状态来控制下载进度ui控件的显示与隐藏
     */
    protected void onFragmentFirstVisible() {}

    /**
     * 去除setUserVisibleHint()多余的回调场景，保证只有当fragment可见状态发生变化时才回调
     * 回调时机在view创建完后，所以支持ui操作，解决在setUserVisibleHint()里进行ui操作有可能报null异常的问题
     *
     * 可在该回调方法里进行一些ui显示与隐藏，比如加载框的显示和隐藏
     *
     * @param isVisible true  不可见 -> 可见
     *                  false 可见  -> 不可见
     */
    protected void onFragmentVisibleChange(boolean isVisible) {}

    /**
     * 设置是否使用 view 的复用，默认开启
     * view 的复用是指，ViewPager 在销毁和重建 Fragment 时会不断调用 onCreateView() -> onDestroyView()
     * 之间的生命函数，这样可能会出现重复创建 view 的情况，导致界面上显示多个相同的 Fragment
     * view 的复用其实就是指保存第一次创建的 view，后面再 onCreateView() 时直接返回第一次创建的 view
     *
     * @param isReuse
     */
    protected void reuseView(boolean isReuse) {
        isReuseView = isReuse;
    }

    /**
     * 获取fragment是否可见的状态
     * @return
     */
    protected boolean isFragmentVisible() {
        return isFragmentVisible;
    }

    /**
     * 初始化变量
     */
    private void initVariable() {
        //第一次进去肯定是true，进去之后直接就是false了
        isFirstVisible = true;
        //默认是不可见状态
        isFragmentVisible = false;
        //是否重用，默认是
        isReuseView = true;
        rootView = null;
    }

    /**
     * 返回键监听
     * @param keyCode
     * @param event
     * @return
     */
    public boolean onKeyDown(int keyCode, KeyEvent event){
        return false;
    }

    public BaseActivity getBaseActivity(){
        if (mActivityWeakRef.get() != null) {
            return (BaseActivity) mActivityWeakRef.get();
        } else {
            return null;
        }
    }

    /**
     * findviewById方法
     * @param id
     * @param <VIEW>
     * @return
     */
    public <VIEW extends View> VIEW findView(int id) {
        if (null != rootView) {
            View child = rootView.findViewById(id);
            try {
                return (VIEW) child;
            } catch (Exception e) {
                e.printStackTrace();
                LogUtils.e("findView: " + String.valueOf(e.getMessage()));
                return null;
            }
        }
        return null;
    }

    /**
     * 打开一个Activity 默认 不关闭当前activity
     */
    public void gotoActivity(Class<?> clz) {
        gotoActivity(clz, false, null);
    }

    public void gotoActivity(Class<?> clz, boolean isCloseCurrentActivity) {
        gotoActivity(clz, isCloseCurrentActivity, null);
    }

    public void gotoActivity(Class<?> clz, boolean isCloseCurrentActivity, Bundle ex) {
        if (mActivityWeakRef.get() != null) {
            Intent intent = new Intent(mActivityWeakRef.get(), clz);

            if (ex != null) intent.putExtras(ex);

            startActivity(intent);

            if (isCloseCurrentActivity) {
                mActivityWeakRef.get().finish();
            }
        }

    }

}
