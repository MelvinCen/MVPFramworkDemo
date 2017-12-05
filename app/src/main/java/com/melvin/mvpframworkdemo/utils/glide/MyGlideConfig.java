package com.melvin.mvpframworkdemo.utils.glide;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.GlideModule;
import com.melvin.mvpframworkdemo.network.RetrofitClient;

import java.io.InputStream;

/**
 * @Author Melvin
 * @CreatedDate 2017/8/7 11:34
 * @Description ${TODO}
 * @Update by       MelvinCen
 * @Date 2017/8/7 11:34
 * @Description ${TODO}
 */

public class MyGlideConfig implements GlideModule {
    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        //通过builder设置缓存等
        builder.setDecodeFormat(DecodeFormat.PREFER_ARGB_8888);
    }

    @Override
    public void registerComponents(Context context, Glide glide) {
        glide.register(GlideUrl.class, InputStream.class,
                new OkHttpUrlLoader.Factory(RetrofitClient.getOkhttpClient()));
    }
}
