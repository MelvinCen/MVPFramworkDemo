package com.melvin.mvpframworkdemo.utils.eventbus;

import org.greenrobot.eventbus.EventBus;

/**
 * @Author Melvin
 * @CreatedDate 2017/8/21 17:45
 * @Description ${TODO}
 * @Update by       MelvinCen
 * @Date 2017/8/21 17:45
 * @Description ${TODO}
 */

public class EventBusUtils {
    public static void register(Object subscriber) {
        if (!EventBus.getDefault().isRegistered(subscriber)) {
            EventBus.getDefault().register(subscriber);
        }
    }

    public static void unregister(Object subscriber) {
        if(EventBus.getDefault().isRegistered(subscriber)){
            EventBus.getDefault().unregister(subscriber);
        }
    }

    public static void sendEvent(Event event) {
        EventBus.getDefault().post(event);
    }

    public static void sendStickyEvent(Event event) {
        EventBus.getDefault().postSticky(event);
    }
}
