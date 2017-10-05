package com.melvin.mvpframworkdemo.utils.eventbus;

/**
 * @Author Melvin
 * @CreatedDate 2017/8/21 17:50
 * @Description ${TODO}
 * @Update by       MelvinCen
 * @Date 2017/8/21 17:50
 * @Description ${TODO}
 */

public class Event<T> {
    private int code;
    private T data;

    public Event(int code) {
        this.code = code;
    }

    public Event(int code, T data) {
        this.code = code;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
