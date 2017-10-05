package com.melvin.mvpframworkdemo.utils;


import android.text.TextUtils;
import android.util.Log;

import com.melvin.mvpframworkdemo.BuildConfig;


/**
 * @Author Melvin
 * @CreatedDate 2017/8/7 15:44
 * @Description ${log工具类}
 */

public class LogUtils {
    public static String className;//类名
    public static String methodName;//方法名
    public static int lineNumber;//行数

    public static final String selfFlag = "Melvin-------";

    private LogUtils(){
        /* Protect from instantiations */
    }

    public static boolean isDebuggable() {
        return BuildConfig.DEBUG;
    }

    private static String createLog(String log) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(methodName);
        buffer.append("方法-----");
        buffer.append("(").append(className).append(":").append(lineNumber).append(")-----");
        buffer.append(log);
        return buffer.toString();
    }

    private static void getMethodNames(StackTraceElement[] sElements){
        className = sElements[1].getFileName();
        methodName = sElements[1].getMethodName();
        lineNumber = sElements[1].getLineNumber();
    }


    public static void e(String message){
        if (!isDebuggable())
            return;

        // Throwable instance must be created before any methods
        getMethodNames(new Throwable().getStackTrace());

        if (TextUtils.isEmpty(message)) {
            Log.e(selfFlag.concat(className),"该log输出信息为空");
        } else {
            Log.e(selfFlag.concat(className), createLog(message));
        }

    }


    public static void i(String message){
        if (!isDebuggable())
            return;

        getMethodNames(new Throwable().getStackTrace());

        if (TextUtils.isEmpty(message)) {
            Log.i(selfFlag.concat(className),"该log输出信息为空");
        } else {
            Log.i(selfFlag.concat(className), createLog(message));
        }
    }

    public static void d(String message){
        if (!isDebuggable())
            return;

        getMethodNames(new Throwable().getStackTrace());

        if (TextUtils.isEmpty(message)) {
            Log.d(selfFlag.concat(className),"该log输出信息为空");
        } else {
            Log.d(selfFlag.concat(className), createLog(message));
        }
    }

    public static void v(String message){
        if (!isDebuggable())
            return;

        getMethodNames(new Throwable().getStackTrace());

        if (TextUtils.isEmpty(message)) {
            Log.v(selfFlag.concat(className),"该log输出信息为空");
        } else {
            Log.v(selfFlag.concat(className), createLog(message));
        }
    }

    public static void w(String message){
        if (!isDebuggable())
            return;

        getMethodNames(new Throwable().getStackTrace());

        if (TextUtils.isEmpty(message)) {
            Log.w(selfFlag.concat(className),"该log输出信息为空");
        } else {
            Log.w(selfFlag.concat(className), createLog(message));
        }
    }

    public static void wtf(String message){
        if (!isDebuggable())
            return;

        getMethodNames(new Throwable().getStackTrace());

        if (TextUtils.isEmpty(message)) {
            Log.wtf(selfFlag.concat(className),"该log输出信息为空");
        } else {
            Log.wtf(selfFlag.concat(className), createLog(message));
        }
    }

}
