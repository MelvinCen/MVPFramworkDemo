package com.melvin.mvpframworkdemo.network;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * created by Melvin 2017/08/04
 * 服务器返回的json基类根据结构更改
 *
 */
public class BaseResponse<T> implements Serializable {

    @SerializedName("resultcode")
    private String resultCode;

    @SerializedName("error_code")
    private int errorCode;

    @SerializedName("reason")
    private String reason;

    @SerializedName("result")
    private T result;

    public Integer getStatus() {
        return errorCode;
    }

    public void setStatus(Integer status) {
        this.errorCode = status;
    }

    public String getMsg() {
        return reason;
    }

    public void setMsg(String msg) {
        this.reason = msg;
    }

    public T getData() {
        return result;
    }

    public void setData(T data) {
        this.result = data;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    @Override
    public String toString() {
        return "BaseResponse{" +
                "resultCode=" + resultCode +
                ", errorCode=" + errorCode +
                ", reason='" + reason + '\'' +
                ", result=" + result +
                '}';
    }

    /**
     * API是否请求成功
     *
     * @return 成功返回true, 失败返回false
     */
    public boolean isRequestSuccess() {
        return resultCode.equals("200");
    }
}
