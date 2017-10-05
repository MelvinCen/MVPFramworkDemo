package com.melvin.mvpframworkdemo.utils.glide;


import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

/**
 * Glide
 * 如果要加载https的图片
 * 1，请修改自定义GlideModule的registerComponents方法，去掉Factory中的参数
 * 2，去掉compile 'com.github.bumptech.glide:okhttp3-integration:1.5.0'依赖
 * 因为其中的两个类已经copy到文件夹下，并修改了internalClient中的参数赋值
 * 3，在清单文件的application截点下配置
 * <meta-data
 *      android:name="***.OkHttpGlideModule"
 *      android:value="GlideModule" />
 * 4，添加混淆 -keep class cn.manmanda.util.https_glide.OkHttpGlideModule
 */
public class GlideUtils {

    public static void showAvatar(Context context, String avatar, ImageView iv) {
        Glide.with(context)
                .load(avatar)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .placeholder(R.mipmap.ic_account_circle_grey600_24dp)
//                .error(R.mipmap.ic_account_circle_grey600_24dp)
                .transform(new GlideCircleTransform(context))
                .crossFade()
                .into(iv);
    }

    public static void showImage(Context context, String imageUrl, ImageView iv) {
        Glide.with(context)
                .load(imageUrl)
                .asBitmap()
                .animate(android.R.anim.fade_in)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(iv);
    }
}
