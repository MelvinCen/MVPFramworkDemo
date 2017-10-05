package com.melvin.mvpframworkdemo.callback;

import android.accounts.NetworkErrorException;
import android.net.ParseException;
import android.text.TextUtils;

import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.melvin.mvpframworkdemo.MyApp;
import com.melvin.mvpframworkdemo.network.retrofit.ApiException;
import com.melvin.mvpframworkdemo.utils.LogUtils;
import com.melvin.mvpframworkdemo.utils.NetworkAvailableUtils;
import com.melvin.mvpframworkdemo.utils.ToastUtils;

import org.json.JSONException;

import java.io.InterruptedIOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeoutException;

/**
 * @Author Melvin
 * @CreatedDate 2017/8/3 11:37
 * @Description ${本类是实际请求的时候用的Observer;需要加载框请传titleMsg参数，不需要则传空}
 * @Update by       MelvinCen
 * @Date 2017/8/3 11:37
 * @Description ${TODO}
 */

public abstract class MyBaseObserver<T> extends BaseObserver<T> {


    private static final String TAG = "MyBaseObserver";

    private boolean isNeedProgress;
    private String titleMsg;
    private BaseImpl mBaseImpl;

    public MyBaseObserver() {
        this(null, null);
    }

    public MyBaseObserver(BaseImpl base) {
        this(base, null);
    }

    public MyBaseObserver(BaseImpl base, String titleMsg) {
        super(base);
        this.mBaseImpl = base;
        this.titleMsg = titleMsg;
        if (TextUtils.isEmpty(titleMsg)) {
            this.isNeedProgress = false;
        } else {
            this.isNeedProgress = true;
        }
    }

    @Override
    protected boolean isNeedProgressDialog() {
        return isNeedProgress;
    }

    @Override
    protected String getTitleMsg() {
        return titleMsg;
    }

    @Override
    protected void onBaseError(Throwable t) {
        LogUtils.e("onBaseError: " + t.toString());
        StringBuffer sb = new StringBuffer();
        sb.append("请求失败：");
        if (!NetworkAvailableUtils.isNetworkAvailable(MyApp.getAppContext())) {
            sb.append("无网络连接");
        }else if (t instanceof NetworkErrorException || t instanceof UnknownHostException || t instanceof ConnectException) {
            sb.append("网络异常");
        } else if (t instanceof SocketTimeoutException || t instanceof InterruptedIOException || t instanceof TimeoutException) {
            sb.append("请求超时");
        } else if (t instanceof JsonSyntaxException) {
            sb.append("请求不合法");
        } else if (t instanceof JsonParseException
                || t instanceof JSONException
                || t instanceof ParseException) {   //  解析错误
            sb.append("解析错误");
        } else if (t instanceof ApiException) {
            if (((ApiException) t).isTokenExpried()) {
                sb.append("Token出错");
            }
            //其他的可以再添加
        } else {
            sb.append("未知错误");
            ToastUtils.showToastSafe(sb.toString());
            return;
        }

        ToastUtils.showToastSafe(sb.toString());
    }
}
