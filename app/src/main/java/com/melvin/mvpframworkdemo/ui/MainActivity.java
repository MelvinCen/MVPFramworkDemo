package com.melvin.mvpframworkdemo.ui;

import android.Manifest;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;

import com.google.gson.Gson;
import com.melvin.mvpframworkdemo.R;
import com.melvin.mvpframworkdemo.base.BaseActivity;
import com.melvin.mvpframworkdemo.base.BaseFragment;
import com.melvin.mvpframworkdemo.bean.VersionInfo;
import com.melvin.mvpframworkdemo.network.RetrofitClient;
import com.melvin.mvpframworkdemo.network.RxTransforms;
import com.melvin.mvpframworkdemo.network.api.ApiService;
import com.melvin.mvpframworkdemo.presenter.WeatherPresenter;
import com.melvin.mvpframworkdemo.ui.weather.WeatherFragment;
import com.melvin.mvpframworkdemo.utils.AppInfoUtils;
import com.melvin.mvpframworkdemo.utils.LogUtils;
import com.melvin.mvpframworkdemo.utils.ToastUtils;
import com.melvin.mvpframworkdemo.utils.VersionUpdateUtil;
import com.melvin.mvpframworkdemo.utils.permission.DefaultPermission;
import com.melvin.mvpframworkdemo.utils.permission.PermissionListener;
import com.melvin.mvpframworkdemo.versionupdate.ApkUpdateUtils;

import java.io.IOException;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import okhttp3.ResponseBody;


public class MainActivity extends BaseActivity<WeatherPresenter> implements PermissionListener{



    private FragmentManager mFragmentManager;
    private BaseFragment    mCurrentFragment;//目前显示的fragment
    private String          downUrl = "http://192.168.31.211:8080/app-release.apk";

    @Override
    protected WeatherPresenter createPresenter() {
        return null;
    }

    @Override
    protected int layoutRes() {
        LogUtils.e("测试logUtils");
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {

        DefaultPermission.requestPermission(this,100, Manifest.permission.READ_EXTERNAL_STORAGE);

        mFragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        WeatherFragment weatherFragment = new WeatherFragment();
        fragmentTransaction.add(R.id.fragment_content,weatherFragment,"weather");
        fragmentTransaction.commit();

        mCurrentFragment = weatherFragment;


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        DefaultPermission.onRequestPermissionsResult(requestCode,permissions,grantResults,this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (mCurrentFragment instanceof WeatherFragment) {
                boolean onKeyDownResult = mCurrentFragment.onKeyDown(keyCode, event);
                if (onKeyDownResult) {
                    return true;
                } else {
                    return super.onKeyDown(keyCode, event);
                }
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onGranted(int requestCode, List<String> grantedPermission) {

    }

    @Override
    public void onAllGranted(int requestCode, List<String> allGrantedPermission) {
        LogUtils.e("onAllGranted");
        //你看到的这个是方案二，使用的是google的downloadmanager
       String versionQueryUrl = "http://192.168.31.211:8080/versionInfo.json";
        RetrofitClient.getInstance(this).create(ApiService.class).geServertVersionCode(versionQueryUrl)
                .compose(RxTransforms.getSchedulersTransformer())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@io.reactivex.annotations.NonNull ResponseBody responseBody) {
                        try {
                            String string = responseBody.string();
                            VersionInfo versionInfo = new Gson().fromJson(string, VersionInfo.class);

                            if (Integer.valueOf(versionInfo.getVersionCode()) > AppInfoUtils.getVersionCode(MainActivity.this)) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                AlertDialog dialog = builder.setTitle("版本更新")
                                        .setMessage("修复部分bug")
                                        .setCancelable(true)
                                        .setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                ApkUpdateUtils.download(MainActivity.this, downUrl, getResources().getString(R.string.app_name));
                                                ToastUtils.showToastSafe("正在后台下载，请稍候...");
                                                dialog.dismiss();
                                            }
                                        })
                                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        }).create();
                                dialog.show();
                            }


                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }



    @Override
    public void onDenied(int requestCode, List<String> deniedPermission) {

    }

    //方案一
    private void updateVersion(int versionCode) {
        new VersionUpdateUtil().checkUpdate(this, versionCode,downUrl,"版本更新",true);
    }
}
