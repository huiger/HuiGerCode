package com.huige.library.utils;

import android.graphics.Color;

import java.security.SecureRandom;
import java.util.Random;

/**
 * Created by <lzh> on 2017/8/31.
 */

public class Utils {

    /**
     * 有透明度
     *
     * @return
     */
    public static int getRandomColor() {
        return Color.HSVToColor(150, new float[]{new SecureRandom().nextInt(359), 1, 1});
    }

    /**
     * 没有透明度
     * @return
     */
    public static int getRandomColorUnAlpha() {
        Random random = new Random();
        return Color.rgb(random.nextInt(255), random.nextInt(255), random.nextInt(255));
    }

}
