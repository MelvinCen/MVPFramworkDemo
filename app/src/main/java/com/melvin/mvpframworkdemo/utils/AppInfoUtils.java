package com.melvin.mvpframworkdemo.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.telephony.TelephonyManager;

import java.util.List;
import java.util.Locale;

/**
 * @Author Melvin
 * @CreatedDate 2017/8/22 13:53
 * @Description ${TODO}
 * @Update by       MelvinCen
 * @Date 2017/8/22 13:53
 * @Description ${TODO}
 */

public class AppInfoUtils {
    /**
     * 获取当前手机语言设置类别，调用android的local类实现
     * 中文简体与繁体是通过countryCode来区分
     */
    public static String getCountryCode(){
        return Locale.getDefault().getCountry();
    }
    /**
     * 获取当前手机语言设置类别，调用android的local类实现
     */
    public static String getLanguage(){
        return Locale.getDefault().getLanguage();
    }
    /**
     * 获取系统软件包名
     */
    public static String getPackageName(Context context){
        return context.getPackageName();
    }
    /**
     * 获取系统软件包版本号
     */
    public static String getVersionName(Context context){
        PackageManager pkgManager = context.getPackageManager();

        try{
            PackageInfo info = pkgManager.getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);

            return info.versionName;
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * 获取手机品牌
     */
    public static String getBrand(){
        return Build.BRAND;
    }
    /**
     * 获取手机型号
     */
    public static String getModel(){
        return Build.MODEL;
    }
    /**
     * 获取操作系统版本
     */
    public static int getOSVersion(){
        return Build.VERSION.SDK_INT;
    }
    /**
     * 获取操作系统版本
     */
    public static String getOSRelease(){
        return Build.VERSION.RELEASE;
    }

    public static Intent getExplicitIntent(Context context, Intent implicitIntent) {
        // Retrieve all services that can match the given intent
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> resolveInfo = pm.queryIntentServices(implicitIntent, 0);
        // Make sure only one match was found
        if (resolveInfo == null || resolveInfo.size() != 1) {
            return null;
        }
        // Get component info and create ComponentName
        ResolveInfo serviceInfo = resolveInfo.get(0);
        String packageName = serviceInfo.serviceInfo.packageName;
        String className = serviceInfo.serviceInfo.name;
        ComponentName component = new ComponentName(packageName, className);
        // Create a new intent. Use the old one for extras and such reuse
        Intent explicitIntent = new Intent(implicitIntent);
        // Set the component to be explicit
        explicitIntent.setComponent(component);
        return explicitIntent;
    }

    public static String byte2hex(byte[] b) {
        StringBuffer hs = new StringBuffer(b.length);
        String stmp = "";
        int len = b.length;
        for (int n = 0; n < len; n++) {
            stmp = Integer.toHexString(b[n] & 0xFF);
            if (stmp.length() == 1)
                hs = hs.append("0").append(stmp);
            else {
                hs = hs.append(stmp);
            }
        }
        return String.valueOf(hs);
    }

    /**
     * 获取IMEI
     * @param context
     * @return
     */
    public static String getIMEI(final Context context) {
        try {
            TelephonyManager manger = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            return manger.getDeviceId();
        } catch (Exception e) {
            return "";
        }
    }
}
