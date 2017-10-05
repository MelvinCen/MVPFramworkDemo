package com.melvin.mvpframworkdemo.utils;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * @Author Melvin
 * @CreatedDate 2017/8/16 14:51
 * @Description ${TODO}
 * @Update by       MelvinCen
 * @Date 2017/8/16 14:51
 * @Description ${TODO}
 */

public class FileUtils {

    /**
     * 检查SD卡是否存在
     *
     * @return
     */
    public static boolean checkSDCard() {
        final String status = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(status)) {
            return true;
        }
        return false;
    }

    /**
     * 创建外部存储公共文件
     * @param fileName 文件名
     * @return
     */
    public static File createExternalPublicFile(String fileName){
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), fileName);

        //需要对父文件存在与否作判断
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        //判断当前文件是否存在
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return file;
    }

    /**
     * 根据路径创建文件
     * @param filePath
     */
    public static File createFileByPath(String filePath){
        File originalFile = new File(filePath);

        if (!originalFile.getParentFile().exists()) {
            originalFile.getParentFile().mkdirs();
        }

        return originalFile;
    }

    /**
     * 获取可以使用的缓存文件
     * @param context
     * @param uniqueName 目录名称
     * @return
     */
    public static String getDiskCacheDir(Context context, String uniqueName) {
        String cachePath = null;
        if (checkSDCard()) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return cachePath;
    }

        /**
         * 获取程序外部的缓存目录
         * @param context
         * @return
         */
    public static File getExternalCacheDir(Context context) {
        // 这个sd卡中文件路径下的内容会随着，程序卸载或者设置中清除缓存后一起清空
        final String cacheDir = "/Android/data/" + context.getPackageName() + "/cache/";
        return new File(Environment.getExternalStorageDirectory().getPath() + cacheDir);
    }

    /**
     * 获取安装在用户手机上的包名下的cache目录
     *
     * @return cache path
     */
    public static String getAppCacheDir(Context context) {
        return context.getCacheDir().getPath();
    }

    /**
     * 获取安装在用户手机上的包名下的files目录
     *
     * @return files path
     */
    public static String getAppFilesDir(Context context) {
        return context.getFilesDir().getAbsolutePath();
    }

    /**
     * 空间大小单位格式化
     * @param size
     * @return
     */
    public static String formatSize(long size) {

        // Formatter.formatFileSize()
        String suffix = null;
        float fSize=0;

        if (size >= 1024) {
            suffix = "KB";
            fSize=size / 1024;
            if (fSize >= 1024) {
                suffix = "MB";
                fSize /= 1024;
            }
            if (fSize >= 1024) {
                suffix = "GB";
                fSize /= 1024;
            }
        } else {
            fSize = size;
        }

        java.text.DecimalFormat df = new java.text.DecimalFormat("#0.00");
        StringBuilder resultBuffer = new StringBuilder(df.format(fSize));
        if (suffix != null)
            resultBuffer.append(suffix);
        return resultBuffer.toString();
    }

    /**
     * 获取文件路径空间大小
     * @param path
     * @return
     */
    public static long getUsableSpace(File path) {
        try{
            final StatFs stats = new StatFs(path.getPath());
            return (long) stats.getBlockSize() * (long) stats.getAvailableBlocks();
        }catch (Exception e) {
            e.printStackTrace();
            return -1;
        }

    }


    /**
     * 保存文件
     *
     * @param content
     * @param fileName
     * @param isAppend
     * @return
     */
    public static boolean writeStringToFile(String content, String fileName, boolean isAppend) {
        return writeStringToFile(content, "", fileName, isAppend);
    }

    public static boolean writeStringToFile(String content,
                                            String directoryPath, String fileName, boolean isAppend) {
        if (!TextUtils.isEmpty(content)) {
            if (!TextUtils.isEmpty(directoryPath)) {// 是否需要创建新的目录
                final File threadListFile = new File(directoryPath);
                if (!threadListFile.exists()) {
                    threadListFile.mkdirs();
                }
            }
            boolean bFlag = false;
            final int iLen = content.length();
            final File file = new File(fileName);
            try {
                if (!file.exists()) {
                    file.createNewFile();
                }
                final FileOutputStream fos = new FileOutputStream(file,
                        isAppend);
                byte[] buffer = new byte[iLen];
                try {
                    buffer = content.getBytes();
                    fos.write(buffer);
                    if (isAppend) {
                        fos.write("||".getBytes());
                    }
                    fos.flush();
                    bFlag = true;
                } catch (IOException ioex) {
                    LogUtils.e(ioex.toString());

                } finally {
                    fos.close();
                    buffer = null;
                }
            } catch (Exception ex) {
                LogUtils.e(ex.toString());

            } catch (OutOfMemoryError o) {
                o.printStackTrace();

            }
            return bFlag;
        }
        return false;
    }

    /**
     * 复制文件(以超快的速度复制文件)
     *
     * @param srcFile
     *            源文件File
     * @param destDir
     *            目标目录File
     * @param newFileName
     *            新文件名
     * @return 实际复制的字节数，如果文件、目录不存在、文件为null或者发生IO异常，返回-1
     */
    @SuppressWarnings("resource")
    public static long copyFile(final File srcFile, final File destDir, String newFileName) {
        long copySizes = 0;
        if (!srcFile.exists()) {
            LogUtils.d("源文件不存在");
            copySizes = -1;
        } else if (!destDir.exists()) {
            LogUtils.d("目标目录不存在");
            copySizes = -1;
        } else if (newFileName == null) {
            LogUtils.d("文件名为null");
            copySizes = -1;
        } else {
            FileChannel fcin = null;
            FileChannel fcout = null;
            try {
                fcin = new FileInputStream(srcFile).getChannel();
                fcout = new FileOutputStream(new File(destDir, newFileName)).getChannel();
                long size = fcin.size();
                fcin.transferTo(0, fcin.size(), fcout);
                copySizes = size;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (fcin != null) {
                        fcin.close();
                    }
                    if (fcout != null) {
                        fcout.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return copySizes;
    }

}
