package com.melvin.mvpframworkdemo;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.telephony.TelephonyManager;

import com.orhanobut.hawk.Hawk;

/**
 * @Author Melvin
 * @CreatedDate 2017/8/2 15:58
 * @Description ${TODO}
 * @Update by       MelvinCen
 * @Date 2017/8/2 15:58
 * @Description ${TODO}
 */

public class MyApp extends Application {

    private static MyApp sInstance;

    private static Context appContext;

    /**
     * 主线程Handler
     */
    public static Handler mMainThreadHandler;

    /**
     * 主线程ID
     */
    public static int mMainThreadId = -1;
    /**
     * 主线程
     */
    public static Thread mMainThread;

    /**
     * 主线程Looper
     */
    public static Looper mMainLooper;

    public MyApp() {
        sInstance = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化thread和handler
        mMainThreadId = android.os.Process.myTid();
        mMainThread = Thread.currentThread();
        mMainThreadHandler = new Handler();
        mMainLooper = getMainLooper();
        appContext = getApplicationContext();
        Hawk.init(this).build();

    }

//    public static MyApp getInstance() {
//        return sInstance;
//    }

    public static Context getAppContext(){
        return appContext;
    }

    /**
     * 获取主线程的handler
     */
    public static Handler getMainThreadHandler() {
        return mMainThreadHandler;
    }

    /**
     * 获取主线程ID
     */
    public static int getMainThreadId() {
        return mMainThreadId;
    }

    /**
     * 获取主线程
     */
    public static Thread getMainThread() {
        return mMainThread;
    }

    /**
     * 获取主线程的looper
     */
    public static Looper getMainThreadLooper() {
        return mMainLooper;
    }

    /**
     * 获取设备唯一标识
     *
     */

    public String getDeviceId() {
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        String device_token = tm.getDeviceId();
        return device_token;
    }

}
