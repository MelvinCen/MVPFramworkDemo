package com.melvin.mvpframworkdemo.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.NotificationCompat;
import android.text.TextUtils;
import android.widget.Toast;

import com.melvin.android7.FileProviderTool;
import com.melvin.mvpframworkdemo.R;
import com.melvin.mvpframworkdemo.network.RetrofitClient;
import com.melvin.mvpframworkdemo.network.api.ApiService;
import com.melvin.mvpframworkdemo.network.download.DownLoadCallBack;
import com.melvin.mvpframworkdemo.network.download.DownObserver;

import java.io.File;

import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * @Author Melvin
 * @CreatedDate 2017/11/30 14:25
 * @Description ${
 * 1、非wifi情况下，弹出版本更新提示框，用户点击“立即升级”按钮开始下载apk，下载完成后提示安装。
 * 2、wifi情况下，直接后台下载apk，下载完后弹出版本更新提示框，用户点击“立即安装”按钮开始安装apk。
 * 3、强制更新为true的时候，无论是否wifi情况下，都是应该弹出更新提示框，用户点击“立即升级”按钮开始下载升级，提示框不能取消，点击“关闭”按钮直接退出app}
 * @Update by       Melvin
 * @Date 2017/11/30 14:25
 * @Description ${TODO}
 */

public class VersionUpdateUtil {

    public   static String downLoadPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/downloads/";//下载保存文件夹的路径
    private int type = 0;//更新方式，0：引导更新，1：安装更新，2：强制更新
    private String url = "";//apk下载地址
    private String updateMessage = "";//更新内容
    private String fileName = null;//apk名称
    private boolean isDownload = false;//是否下载
    private boolean cancelable = true;//更新对话框是否能取消
    private AlertDialog                mDialog;//提示更新对话框
    private ProgressDialog             mProgressDialog;//强制更新进度条
    private NotificationManager        mNotifyManager;//通知栏相关
    private NotificationCompat.Builder mBuilder;//通知栏相关

    /**
     * @param context
     * @param versionCode 从服务器获取的版本号
     */
    public void checkUpdate(Context context, int versionCode, String downUrl, String updateMessage,boolean isForced) {
        LogUtils.e("版本跟新方法");
        if (versionCode > AppInfoUtils.getVersionCode(context)) {
            //判断是在哪种网络状态下
            if (NetworkStatusUtils.isWifi(context)) {
                type = 1;
            }
            //判断是否需要强制更新
            if (isForced) {
                type = 2;
            }
            this.url = downUrl;
            this.updateMessage = updateMessage;

            //检查是否以下载

            //获得文件夹，如果没有则创建
            File downLoadFile = new File(downLoadPath);
            if (!downLoadFile.exists()) {
                downLoadFile.mkdirs();
            }
            //获取文件名
            fileName = downUrl.substring(downUrl.lastIndexOf("/") + 1, downUrl.length());
            if (fileName == null && TextUtils.isEmpty(fileName) && !fileName.contains(".apk")) {
                fileName = context.getPackageName() + ".apk";
            }

            File apkFile = new File(downLoadPath + fileName);

            isDownload = apkFile.exists();

            if (type == 1 && !apkFile.exists()) {//wifi状态下，并且apk不存在，静默下载
                LogUtils.e("静默下载");
                downLoadFile(context);
            } else {//否则提对话框去引导更新或者强制更新
                showDialog(context);
            }

        }
    }

    private void downLoadFile(final Context context) {
        LogUtils.e("下载方法");
        RetrofitClient.getInstance(context).create(ApiService.class).downloadFile(url)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new DownObserver<ResponseBody>(FileUtils.generateFileKey(url, fileName), downLoadPath, fileName, new DownLoadCallBack() {
                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onSucess(String key, String path, String name, long fileSize) {
                        if (type == 0) {
                            mBuilder.setContentTitle("下载完成");
                            mBuilder.setContentText("点击安装");
                            mBuilder.setProgress(100, 100, false);
                            //点击安装PendingIntent
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_VIEW);
                            intent.setDataAndType(Uri.fromFile(new File(downLoadPath, fileName)), "application/vnd.android.package-archive");
                            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                            mBuilder.setContentIntent(pendingIntent);
                            mNotifyManager.notify(10086, mBuilder.build());
                        } else if (type == 2) {
                            mProgressDialog.setMessage("下载完成");
                        }
                        if (type == 1) {
                            showDialog(context);
                        } else {
                            installApk(context, new File(downLoadPath, fileName));
                        }
                    }

                    @Override
                    public void onProgress(String key, int progress, long fileSizeDownloaded, long totalSize) {
                        super.onProgress(key, progress, fileSizeDownloaded, totalSize);
                        //实时更新通知栏进度条
                        if (type == 0) {
                            mBuilder.setProgress((int) totalSize,progress, false);
                            mNotifyManager.notify(10086, mBuilder.build());
                        } else if (type == 2) {
                            mProgressDialog.setProgress(progress);
                        }
                    }
                },context));
    }

    /**
     * 安装apk
     * @param context
     * @param file
     */
    private void installApk(Context context, File file) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        FileProviderTool.setIntentDataAndType(context,intent,"application/vnd.android.package-archive",file,true);
        context.startActivity(intent);
    }

    /**
     * 创建更新提示对话框
     * @param context
     */
    private void showDialog(final Context context) {
        //提示框标题
        String title = "";
        //提示框左按键
        String left = "";
        if (type == 1 | isDownload) {
            title = "安装新版本";
            left = "立即安装";
        } else {
            title = "发现新版本";
            left = "立即更新";
        }
        //强制更新下不可取消对话框
        if (type == 2) {
            cancelable = false;
        }
        //构建提示框
        mDialog = new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(updateMessage)
                .setCancelable(cancelable)
                .setPositiveButton(left, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mDialog.dismiss();
                        if (type == 1 | isDownload) {//下载完成的直接安装
                            installApk(context, new File(downLoadPath, fileName));
                        } else {//去下载
                            if (url != null && !TextUtils.isEmpty(url)) {
                                if (type == 2) {//强制更新的，显示进度条
                                    createProgress(context);
                                } else {//引导更新的，通知栏显示进度条
                                    createNotitication(context);
                                }
                                //界面显示后去真正的下载
                                downLoadFile(context);
                            }else {
                                Toast.makeText(context, "下载地址错误", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mDialog.dismiss();
                        //如果是强制更新，取消则不能进入app，强制退出
                        if (type == 2) {

                            System.exit(0);
                        }
                    }
                })
                .create();
        mDialog.show();
    }

    /**
     * 创建显示通知栏通知
     * @param context
     */
    private void createNotitication(Context context) {
        mNotifyManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(context);
        mBuilder.setSmallIcon(R.mipmap.ic_launcher);
        mBuilder.setContentTitle("app版本更新");
        mBuilder.setContentText("正在下载新版本...");
        mBuilder.setProgress(100, 0, false);
        Intent intent = new Intent();
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pendingIntent);
        Notification notification = mBuilder.build();
        notification.flags = Notification.FLAG_AUTO_CANCEL;
        mNotifyManager.notify(10086, notification);//10086只是一个id
    }

    /**
     * 创建显示进度条
     * @param context
     */
    private void createProgress(Context context) {
        mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setMax(100);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMessage("正在下载...");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setButton(DialogInterface.BUTTON_POSITIVE, "取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mProgressDialog.dismiss();
                System.exit(0);
            }
        });
        mProgressDialog.show();
    }

}
