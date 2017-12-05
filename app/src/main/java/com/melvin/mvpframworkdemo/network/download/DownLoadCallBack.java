package com.melvin.mvpframworkdemo.network.download;

/**
 * @Author Melvin
 * @CreatedDate 2017/12/2 16:23
 * @Description ${下载回调}
 * @Update by       MelvinCen
 * @Date 2017/12/2 16:23
 * @Description ${TODO}
 */
public abstract class DownLoadCallBack {

    public void onStart(String key){}

    public void onCancel(){}

    public void onCompleted(){}


    /** Note : the Fun run not MainThred
     * @param e
     */
    abstract public void onError(Throwable e);

    public void onProgress(String key, int progress, long fileSizeDownloaded, long  totalSize ){}

    /**  Note : the Fun run UIThred
     * @param path
     * @param name
     * @param fileSize
     */
    abstract public void onSucess(String key, String path, String name, long fileSize);
}
