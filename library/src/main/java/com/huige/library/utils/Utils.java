package com.huige.library.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

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


}
