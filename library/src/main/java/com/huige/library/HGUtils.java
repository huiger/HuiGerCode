package com.huige.library;

import android.content.Context;

import com.huige.library.utils.SharedPreferencesUtils;

/**
 * Author : huiGer
 * Time   : 2018/11/6 0006 下午 03:24.
 * Desc   :
 */
public class HGUtils {


    public static void init(Context ctx){
        // AppContext
        HGUtilsApp.setContext(ctx);

        // init SharedPreferences
        SharedPreferencesUtils.getInstance(ctx);

    }

}
