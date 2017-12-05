package com.melvin.mvpframworkdemo.bean;

/**
 * @Author Melvin
 * @CreatedDate 2017/12/3 17:54
 * @Description ${TODO}
 * @Update by       Melvin
 * @Date 2017/12/3 17:54
 * @Description ${TODO}
 */

public class VersionInfo {


    /**
     * code : 200
     * message : ok
     * versionCode : 2
     */

    private String code;
    private String message;
    private String versionCode;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
    }
}
