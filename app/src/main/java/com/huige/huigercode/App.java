package com.huige.huigercode;

import android.app.Application;
import android.content.Context;

import com.huige.library.HGUtils;

/**
 * Author : huiGer
 * Time   : 2018/6/26 0026 下午 02:26.
 * Desc   :
 */
public class App extends Application{

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        HGUtils.init(this);
    }


    public static Context getContext(){
        return context;
    }

}
