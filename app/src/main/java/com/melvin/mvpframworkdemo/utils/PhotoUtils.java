package com.melvin.mvpframworkdemo.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static com.melvin.mvpframworkdemo.utils.FileUtils.createExternalPublicFile;


/**
 * @Author Melvin
 * @CreatedDate 2017/9/1 17:46
 * @Description ${拍照，相册，裁剪等功能}
 * @Update by       MelvinCen
 * @Date 2017/9/1 17:46
 * @Description ${TODO}
 */

public class PhotoUtils {
    //拍照的请求码
    public static final int REQUEST_CODE_TAKE_PHOTO = 0x110;
    //相册选择照片的请求码
    public static final int REQUEST_CODE_SELECT_PHOTO = 0x111;
    //裁剪照片的请求码
    public static final int REQUEST_CODE_CROP_PHOTO = 0x112;
    //照片存储路径
    private static String CurrentPhotoPath;
    //裁剪后照片存储路径
    private static String CropPhotoPath;
    //照片名称
    private static String fileName;
    //裁剪后的照片名称
    private static String cropedFileName;

    /**
     * 调用手机本身的相机拍照的方法
     * @param activity
     *
     */
    public static void takePhoto(Activity activity){
        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //首先判断是否有相机应用
        if (takePhotoIntent.resolveActivity(activity.getPackageManager()) != null) {
            if (FileUtils.checkSDCard()) {
                //用时间格式对图片命名
                fileName = getDataFormatString() + ".jpeg";

                File file = createExternalPublicFile(fileName);

                //记录照片保存的路径
                CurrentPhotoPath = file.getAbsolutePath();
                //获取uri
                Uri uri = getUri(activity, file);
                //拍照后写入这个uri
                takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT,uri);
                activity.startActivityForResult(takePhotoIntent, REQUEST_CODE_TAKE_PHOTO);

            } else {
                ToastUtils.showToastSafe("SD卡不存在");
            }

        }
    }

    /**
     *
     * @param activity        当前activity,会回调该activity的onActivityResult方法
     * @param originalImgPath 原图片路径
     * @param width           剪裁图片的宽度
     * @param height          剪裁图片高度
     * @param requestCode     请求码
     */
    public static void cropImgUri(Activity activity, String originalImgPath,
                                  int width, int height, int requestCode){
        //用时间格式对图片命名
        cropedFileName = getDataFormatString() + "-crop.jpeg";
        //创建裁剪后存储的文件夹
        File cropFile = createExternalPublicFile(cropedFileName);

        //记录照片保存的路径
        CropPhotoPath = cropFile.getAbsolutePath();
        //获取原文件
        File originalFile = FileUtils.createFileByPath(originalImgPath);

        //裁剪图片的意图
        Intent intent = new Intent("com.android.camera.action.CROP");
        //声明源uri，目的地uri
        Uri orgUri = null;
        Uri desUri = null;
        //7.0适配
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //授权
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            //获取uri，原图uri在7.0以上需要适配，裁剪后的uri因为不是要暴露出去的，所以要用原来的。
            orgUri = getUri(activity, originalFile);
            desUri = Uri.fromFile(cropFile);

        } else {
            orgUri = Uri.fromFile(originalFile);
            desUri = Uri.fromFile(cropFile);
        }

        intent.setDataAndType(orgUri, "image/*");
        intent.putExtra("crop", "true");//发送裁剪信号
        intent.putExtra("aspectX", 1);//X方向的比例
        intent.putExtra("aspectY", 1);//y方向的比例
        intent.putExtra("outputX", width);//剪裁图片的宽度
        intent.putExtra("outputY", height);//剪裁图片高度
        intent.putExtra("scale", true);//是否保留裁剪比例
        //将剪切的图片保存到目标Uri中
        intent.putExtra(MediaStore.EXTRA_OUTPUT, desUri);//裁剪的图片保存的地址
        intent.putExtra("return-data", false);//是否将数据保留在bitmap中返回
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());//图片输出格式
        intent.putExtra("noFaceDetection", true);//是否取消人脸识别
        activity.startActivityForResult(intent, requestCode);


    }

    /**
     * 打开相册
     * @param activity
     * @param requestCode
     */
    public static void openAlbum(Activity activity, int requestCode){
        Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
        photoPickerIntent.setType("image/*");
        activity.startActivityForResult(photoPickerIntent, requestCode);
    }

    /**
     * 适配7.0获取uri
     * @param activity
     * @param file
     * @return
     */
    public static Uri getUri(Activity activity, File file) {
        //创建URI
        Uri uri = null;

        //android7.0以上适配
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //7.0以上调用系统封装的方法
            uri = FileProvider.getUriForFile(activity, activity.getPackageName() + ".android7.fileprovider", file);
        } else {
            //7.0以下还是按照原来的方式
            uri = Uri.fromFile(file);
        }
        return uri;
    }

    //获取日期的String形式（当天）
    public static String getDataFormatString(){
        String todayStr = new SimpleDateFormat("yyyyMMdd-HHmmss", Locale.CHINA).format(new Date());
        return todayStr;
    }

    /**
     * 公共回调方法
     * @param activity
     * @param requestCode
     * @param resultCode
     * @param data
     * @param updateAlbum
     * @param listener
     */
    public static void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data,
                                 boolean updateAlbum,OnPhotoResultListener listener){
        LogUtils.e("onActivityResult");
            switch (requestCode) {
                case REQUEST_CODE_TAKE_PHOTO://拍照
                    if (!TextUtils.isEmpty(CurrentPhotoPath)) {
                        File file = new File(CurrentPhotoPath);
                        if (file.isFile() && listener != null) {
                            listener.takePhotoFinish(CurrentPhotoPath);
                            if (updateAlbum) {
                                // 把文件插入到系统图库
                                try {
                                    MediaStore.Images.Media.insertImage(activity.getContentResolver(),
                                            file.getAbsolutePath(), fileName, null);
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }
                                // 最后通知图库更新
                                Uri uri = getUri(activity, file);
                                activity.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
                            }
                        }
                    }
                    break;
                case REQUEST_CODE_SELECT_PHOTO://选择照片
                    LogUtils.e("REQUEST_CODE_SELECT_PHOTO");
                    if (data != null) {
                        Uri selectedPhotoUri = data.getData();
                        if (listener != null) {
                            listener.selectPictureFinish(selectedPhotoUri);
                        }
                    }
                    break;
                case REQUEST_CODE_CROP_PHOTO://裁剪照片
                    if (!TextUtils.isEmpty(CropPhotoPath)) {
                        File file = new File(CropPhotoPath);
                        if (file.isFile() && listener != null) {
                            listener.cropPictureFinish(Uri.fromFile(file));
                        }
                    }

                    break;

            }

    }

    /**
     * 拍照、选择、裁剪图片回调类
     */
    public interface OnPhotoResultListener {
        //拍照回调
        void takePhotoFinish(String path);

        //选择图片回调
        void selectPictureFinish(Uri uri);

        //裁剪回调
        void cropPictureFinish(Uri uri);
    }

    /**
     * 读取uri所在的图片
     *
     * @param uri      图片对应的Uri
     * @param mContext 上下文对象
     * @return 获取图像的Bitmap
     */
    public static Bitmap getBitmapFromUri(Uri uri, Context mContext) {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), uri);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 摘自http://blog.csdn.net/zz110753/article/details/60877594博客
     * Android4.4之后选取中的图片不再返回真实的Uri了,
     * 而是封装过的Uri,所以在4.4以上，就要对这个Uri进行解析
     * 就需要用这个方法
     * @param context 上下文对象
     * @param uri     当前相册照片的Uri
     * @return 解析后的Uri对应的String 即uriString
     */
    @SuppressLint("NewApi")
    public static String getPath(Context context, Uri uri){

        boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        String pathHead = "file:///";
        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if (type.equalsIgnoreCase("primary")) {
                    return pathHead + Environment.getExternalStorageDirectory() + File.separator + split[1];

                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return pathHead + getDataColumn(context, contentUri, null, null);
            }

            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;

                if (type.equals("image")) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                }else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};
                return pathHead + getDataColumn(context, contentUri, selection, selectionArgs);

            }
        }
        // MediaStore (and general)
        else if (uri.getScheme().equalsIgnoreCase("content")) {
            return pathHead + getDataColumn(context, uri, null, null);
        }
        // File
        else if (uri.getScheme().equalsIgnoreCase("file")) {
            return pathHead + uri.getPath();
        }
        //else
        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    private static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    private static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }
}
