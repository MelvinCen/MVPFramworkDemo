package com.melvin.mvpframworkdemo.network.download;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;

import com.melvin.mvpframworkdemo.MyApp;
import com.melvin.mvpframworkdemo.utils.FileUtils;
import com.melvin.mvpframworkdemo.utils.LogUtils;
import com.melvin.mvpframworkdemo.utils.VersionUpdateUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.ResponseBody;

import static com.melvin.mvpframworkdemo.network.download.DownObserver.checkMain;

/**
 * @Author Melvin
 * @CreatedDate 2017/12/3 13:09
 * @Description ${TODO}
 * @Update by       Melvin
 * @Date 2017/12/3 13:09
 * @Description ${TODO}
 */

public class DownLoadManager {

    private DownLoadCallBack callBack;

    public static boolean isDownLoading = false;

    public static boolean isCancel = false;

    private final Handler mMainThreadHandler;

    private static String fileSuffix = ".apk";

    public DownLoadManager(DownLoadCallBack callBack) {
        this.callBack = callBack;
        mMainThreadHandler = MyApp.getMainThreadHandler();

    }

    public boolean writeResponseBodyToDisk(final String key, String path, String name, Context context, ResponseBody body) {
        if (body == null) {
            finalonError(new NullPointerException("the "+ key + " ResponseBody is null"));
        }

        String type ="";
        if (body.contentType() != null) {
            type = body.contentType().toString();
        } else {
            LogUtils.e("MediaType-->,无法获取");
        }

        if (!TextUtils.isEmpty(type)) {
            LogUtils.e("contentType:>>>>" + body.contentType().toString());
            if (!TextUtils.isEmpty(MimeType.getInstance().getSuffix(type))){
                fileSuffix = MimeType.getInstance().getSuffix(type);
            }
        }

        if (!TextUtils.isEmpty(name)) {
            if (!name.contains(".")) {
                name = name + fileSuffix;
            }
        }

//        if (path == null) {
//            File filepath = new File(path = context.getExternalFilesDir(null) + File.separator +"DownLoads");
//            if (!filepath.exists()){
//                filepath.mkdirs();
//            }
//            path = context.getExternalFilesDir(null) + File.separator +"DownLoads" + File.separator;
//        }

        path = VersionUpdateUtil.downLoadPath;

        if (new File(path + name).exists()) {
            LogUtils.e("文件已存在");
            FileUtils.deleteFile(path);
        }

        LogUtils.e("path:-->" + path);
        LogUtils.e("name:->" + name);

        try {

            File downloadFile = new File(path + name);
            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];

                final long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;//下载的大小
                int updateCount = 0;
                LogUtils.e("file length: " + fileSize);
                inputStream = body.byteStream();

                try {
                    outputStream = new FileOutputStream(downloadFile);
                } catch (FileNotFoundException e) {
                    LogUtils.e("FileNotFoundException");
                    e.printStackTrace();
                }
                while (true) {

                    int read = inputStream.read(fileReader);
                    if (read == -1) {
                        break;
                    }
                    outputStream.write(fileReader, 0, read);
                    fileSizeDownloaded += read;
                    LogUtils.e("file download: " + fileSizeDownloaded + " of " + fileSize);
                    int  progress = 0;
                    if (fileSize == -1 || fileSize ==  0) {
                        progress = 100;
                    } else {
                        progress = (int) (fileSizeDownloaded * 100 / fileSize);
                    }

                    LogUtils.e("file download progress : " + progress);
                    if (updateCount == 0 || progress >= updateCount) {
                        updateCount += 1;
                        if (callBack != null) {
                            final long finalFileSizeDownloaded = fileSizeDownloaded;
                            final int finalProgress = progress;
                            mMainThreadHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    callBack.onProgress(key, finalProgress, finalFileSizeDownloaded, fileSize);
                                }
                            });
                        }
                    }
                }

                outputStream.flush();
                LogUtils.e("file downloaded: " + fileSizeDownloaded + " of " + fileSize);
                isDownLoading = false;
                if (callBack != null) {
                    final String finalName = name;
                    final String finalPath = path;
                    mMainThreadHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            callBack.onSucess(key, finalPath, finalName, fileSize);
                        }
                    });
                    LogUtils.e("file downloaded: " + fileSizeDownloaded + " of " + fileSize);
                    LogUtils.e("file downloaded: is sucess");
                }
                return true;
            } catch (IOException e) {
                finalonError(e);
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            finalonError(e);
            return false;
        }
    }

    /**
     * 失败回调
     * @param e
     */
    private void finalonError(final Exception e) {
        if (callBack == null) {
            return;
        }

        if (checkMain()) {
            callBack.onError(e);
        } else {
            mMainThreadHandler.post(new Runnable() {
                @Override
                public void run() {
                    callBack.onError(e);
                }
            });
        }
    }
}
