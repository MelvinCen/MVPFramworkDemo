package com.melvin.mvpframworkdemo.utils.permission;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;

import com.melvin.mvpframworkdemo.MyApp;
import com.melvin.mvpframworkdemo.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



/**
 * @Author Melvin
 * @CreatedDate 2017/11/1 14:21
 * @Description ${6.0权限的封装}
 * @Update by       Melvin
 * @Date 2017/11/1 14:21
 * @Description ${
 * 封装思路：
 * 1，检查所有的权限是否都已授权  使用checkPermission方法
 * 2，有未授权的权限，用原生的shouldShowRequestPermissionRationale方法判断用户是否拒绝过权限，从而决定是否需要提示用户权限的重要性
 * ps：对于shouldShowRequestPermissionRationale的理解（正常情况）
 * a，如果用户之前拒绝过该权限并没有勾选不再提示选项，方法返回true
 * b，如果用户之前拒绝过该权限并勾选不再提示选项，方法返回false
 * c，设备规范禁用应用具有该权限，返回false
 * d，第一次进来调用，返回false
 * 根据厂商定制可能有bug，例如小米
 * 3，调用系统方法执行真正的请求权限操作
 * 4，请求结果在activity或者fragment的onRequestPermissionsResult父类方法中返回，调用本类封装的onRequestPermissionsResult方法处理
 * 5，回调PermissionListener三个方法，全部权限都被授权，个别权限被授权，有权限被拒绝；
 * 6，在被拒绝回调中判断是否权限被拒绝且勾选了不再提醒选项，
 * 如果是，则提示用户去设置中自己打开权限否则无法使用功能，对话框中的确定直接跳转设置页面
 *
 * 使用：
 * 1，调用requestPermission方法
 * 2，在onRequestPermissionsResult方法中调用本类onRequestPermissionsResult方法处理
 * 3，实现PermissionListener接口
 * 4，在成功回调中处理自己的逻辑，在失败回调中增加判断用户是否勾选“不再提醒”并弹框提示
 * }
 */

public class DefaultPermission {

    /**
     * 检查是否有权限
     *
     * @param context     上下文
     * @param permissions 需要检查的权限
     * @return
     */
    public static boolean checkPermission(@NonNull Context context, @NonNull String... permissions) {
        //6.0以下直接返回true
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            return true;
        //
        for (String permission : permissions) {
            boolean hasPermission = (ContextCompat.checkSelfPermission(context, permission) == PackageManager
                    .PERMISSION_GRANTED);
            if (!hasPermission)
                return false;
        }
        return true;
    }

    /**
     * 没有授权的权限列表
     *
     * @param o
     * @param permissions
     * @return
     */
    public static List<String> getDeniedPermissions(Object o, String[] permissions) {
        List<String> needRequestPermissionList = new ArrayList<>();
        Context context = getContext(o);
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission) !=
                    PackageManager.PERMISSION_GRANTED ||
                    shouldShowRequestPermissionRationale(o, permission)) {
                needRequestPermissionList.add(permission);
            }
        }
        return needRequestPermissionList;
    }

    /**
     * 调用申请权限的方法
     * @param object 一般是本类实例
     * @param permissions 需要申请的权限
     * @param requestCode 申请码
     */
    public static void requestPermission(final Object object, final int requestCode,final String... permissions) {
        //检查是否可执行
        checkCallingObjectSuitability(object);
        //获取context
        Context context = getContext(object);

        if (checkPermission(context, permissions)) {
            //发现权限都是有的
            ((PermissionListener) object).onAllGranted(requestCode, Arrays.asList(permissions));
        } else {

            //是否应该展示警示dialog
            boolean shouldShowRationale = false;
            for (String permission : permissions) {
                shouldShowRationale =
                        shouldShowRationale || shouldShowRequestPermissionRationale(object, permission);
            }

            if (shouldShowRationale) {

                AlertDialog dialog = new AlertDialog.Builder(context)
                        .setTitle("权限已被拒绝")
                        .setMessage("您已经拒绝过我们申请授权，请您同意授权，否则功能无法正常使用！")
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //执行正真的权限请求
                                excutePermissionRequest(object, permissions, requestCode);
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //取消直接就返回授权失败
                                if (object instanceof PermissionListener) {
                                    ((PermissionListener) object).onDenied(requestCode, Arrays.asList(permissions));
                                }
                            }
                        })
                        .create();
                dialog.show();
            } else {
                //第一次进入的时候，或者点击了拒绝并且不再提醒
                //执行正真的权限请求
                excutePermissionRequest(object, permissions, requestCode);
            }

        }


    }

    /**
     * 执行正真的权限请求操作
     */
    @TargetApi(Build.VERSION_CODES.M)
    private static void excutePermissionRequest(Object object, String[] permissions, int requestCode) {

        List<String> needReuestPermissions = getDeniedPermissions(object, permissions);
        if (object instanceof Activity) {
            ActivityCompat.requestPermissions((Activity) object, needReuestPermissions.toArray(new String[needReuestPermissions.size()]), requestCode);
        } else if (object instanceof Fragment) {
            ((Fragment) object).requestPermissions(needReuestPermissions.toArray(new String[needReuestPermissions.size()]), requestCode);
        } else if (object instanceof android.app.Fragment) {
            ((android.app.Fragment) object).requestPermissions(needReuestPermissions.toArray(new String[needReuestPermissions.size()]), requestCode);
        }


    }

    /**
     * 权限申请结果返回的处理，需要在原生的onRequestPermissionsResult调用!!!!!
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     * @param object
     */
    public static void onRequestPermissionsResult(int requestCode, String[] permissions,
                                                  int[] grantResults, Object object) {
        //检查可执行性
        checkCallingObjectSuitability(object);
        //创建两个存放被授权和被拒绝的权限的集合
        ArrayList<String> granted = new ArrayList<>();
        ArrayList<String> denied = new ArrayList<>();
        //遍历
        for (int i = 0; i < permissions.length; i++) {
            String permission = permissions[i];
            if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                granted.add(permission);
            } else {
                denied.add(permission);
            }
        }

        // 有被授权的权限
        if (!granted.isEmpty()) {
            // Notify callbacks
            if (object instanceof PermissionListener) {
                ((PermissionListener) object).onGranted(requestCode, granted);
            }
        }

        // 有被拒绝的权限
        if (!denied.isEmpty()) {
            if (object instanceof PermissionListener) {
                ((PermissionListener) object).onDenied(requestCode, denied);
            }
        }

        // 所有申请的权限都被授权
        if (!granted.isEmpty() && denied.isEmpty()) {
            if (object instanceof PermissionListener)
                ((PermissionListener) object).onAllGranted(requestCode, Arrays.asList(permissions));
        }
    }

    /**
     * 检查是否权限被拒绝并勾选了不再提醒，如果是即弹出提示框提示
     *
     * @param object
     * @param deniedPermissions
     * @return
     */
    public static boolean checkDeniedPermissionsNeverAskAgain(final Object object,
                                                              List<String> deniedPermissions, int settingRequestCode) {
        return checkDeniedPermissionsNeverAskAgain(object, MyApp.getAppContext().getString(R.string.permission_tips_to_setting),
                "去设置", "取消", null, deniedPermissions, settingRequestCode);
    }


    public static boolean checkDeniedPermissionsNeverAskAgain(final Object object,
                                                              String tips,
                                                              String positiveButton,
                                                              String negativeButton,
                                                              @Nullable DialogInterface.OnClickListener negativeButtonOnClickListener,
                                                              List<String> deniedPerms, final int settingRequestCode) {
        boolean shouldShowRationale;
        for (String perm : deniedPerms) {
            shouldShowRationale = shouldShowRequestPermissionRationale(object, perm);
            if (!shouldShowRationale) {
                final Context context = getContext(object);

                AlertDialog dialog = new AlertDialog.Builder(context)
                        .setMessage(tips)
                        .setPositiveButton(positiveButton, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package", context.getPackageName(), null);
                                intent.setData(uri);
                                startAppSettingsForResult(object, intent, settingRequestCode);
                            }
                        })
                        .setNegativeButton(negativeButton, negativeButtonOnClickListener)
                        .create();
                dialog.show();

                return true;
            }
        }

        return false;
    }


    //---------------------------------------------------以下为工具类-------------------------------------------------------------------------

    /**
     * 判断shouldShowRequestPermissionRationale返回值，是否应该展示请求权限的界面
     *
     * @param object
     * @param permission
     * @return
     */
    @TargetApi(Build.VERSION_CODES.M)
    private static boolean shouldShowRequestPermissionRationale(Object object, String permission) {
        if (object instanceof Activity) {
            return ActivityCompat.shouldShowRequestPermissionRationale((Activity) object, permission);
        } else if (object instanceof Fragment) {
            return ((Fragment) object).shouldShowRequestPermissionRationale(permission);
        } else if (object instanceof android.app.Fragment) {
            return ((android.app.Fragment) object).shouldShowRequestPermissionRationale(permission);
        } else {
            return false;
        }
    }

    /**
     * 在不同类型中startActivityForResult
     *
     * @param object
     * @param intent
     * @param settingRequestCode
     */
    private static void startAppSettingsForResult(Object object,
                                                  Intent intent, int settingRequestCode) {
        if (object instanceof Activity) {
            ((Activity) object).startActivityForResult(intent, settingRequestCode);
        } else if (object instanceof Fragment) {
            ((Fragment) object).startActivityForResult(intent, settingRequestCode);
        } else if (object instanceof android.app.Fragment) {
            ((android.app.Fragment) object).startActivityForResult(intent, settingRequestCode);
        }
    }

    /**
     * 根据类型转换
     *
     * @param o
     * @return
     */
    public static Context getContext(Object o) {
        if (o instanceof Activity)
            return (Activity) o;
        else if (o instanceof Fragment)
            return ((Fragment) o).getActivity();
        else if (o instanceof android.app.Fragment)
            return ((android.app.Fragment) o).getActivity();
        throw new IllegalArgumentException("The " + o.getClass().getName() + " is not support.");
    }

    /**
     * 检查是否在合适的地方是有使用此工具类
     *
     * @param object
     */
    private static void checkCallingObjectSuitability(Object object) {
        // Make sure Object is an Activity or Fragment
        boolean isActivity = object instanceof Activity;
        boolean isSupportFragment = object instanceof Fragment;
        boolean isAppFragment = object instanceof android.app.Fragment;
        boolean isMinSdkM = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;

        if (!(isSupportFragment || isActivity || (isAppFragment && isMinSdkM))) {
            if (isAppFragment) {
                throw new IllegalArgumentException(
                        "Target SDK needs to be greater than 23 if caller is android.app.Fragment");
            } else {
                throw new IllegalArgumentException("Caller must be an Activity or a Fragment.");
            }
        }
    }
}
