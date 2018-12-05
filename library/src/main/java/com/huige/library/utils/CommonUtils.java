package com.huige.library.utils;

import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.widget.Toast;

import com.huige.library.HGUtils;
import com.huige.library.HGUtilsApp;

import java.security.SecureRandom;
import java.util.Random;

/**
 * Created by lzh on 2017/8/31.
 */

public class CommonUtils {

    private static Toast mToast;

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
     *
     * @return 随机颜色
     */
    public static int getRandomColorUnAlpha() {
        Random random = new Random();
        return Color.rgb(random.nextInt(255), random.nextInt(255), random.nextInt(255));
    }

    /**
     * 检查是否有可用网络
     *
     * @return 是否有网
     */
    public static boolean isNetworkConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) HGUtilsApp.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo() != null;
    }




}
