package com.huige.library.utils;

import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;

import java.security.SecureRandom;
import java.util.Random;

/**
 * Created by lzh on 2017/8/31.
 */

public class Utils {

    /**
     * 有透明度
     *
     * @return 随机颜色
     */
    public static int getRandomColor() {
        return Color.HSVToColor(150, new float[]{new SecureRandom().nextInt(359), 1, 1});
    }

    /**
     * 没有透明度
     * @return 随机颜色
     */
    public static int getRandomColorUnAlpha() {
        Random random = new Random();
        return Color.rgb(random.nextInt(255), random.nextInt(255), random.nextInt(255));
    }

    /**
     * 检查是否有可用网络
     * @param ctx 上下文
     * @return 手否有网
     */
    public static boolean isNetworkConnected(Context ctx) {
        ConnectivityManager connectivityManager = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo() != null;
    }

}
