package com.huige.library.utils;

import android.graphics.Color;

import java.security.SecureRandom;

/**
 * Created by <lzh> on 2017/8/31.
 */

public class Utils {

    public static int getRandomColor(){
        return Color.HSVToColor(150, new float[]{new SecureRandom().nextInt(359), 1, 1});
    }

}
