package com.melvin.mvpframworkdemo.network;


import com.melvin.mvpframworkdemo.config.Constant;

/**
 * 异常处理的一个类
 * 实现项目可以在这里处理message后再返回给MyBaseObserver
 */
public class ApiException extends RuntimeException {

    private int mErrorCode;
    private String mErrorMsg;

    public ApiException(int errorCode, String errorMessage) {
        super(errorMessage);
        mErrorCode = errorCode;
        mErrorMsg = errorMessage;
    }

    /**
     * 判断是否是token失效
     *
     * @return 失效返回true, 否则返回false;
     */
    public boolean isTokenExpried() {
        return mErrorCode == Constant.EXCEPTION_TOKEN;
    }

    public String getErrorMessage(){
        return mErrorMsg;
    }
}
