package com.huige.library;

import android.content.Context;

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2018/11/9 0009 下午 03:46.
 *  Email  : zhihuiemail@163.com
 *  Desc   :
 * </pre>
 */
public class HGUtilsApp {

    private static Context mContext;

    public static void setContext(Context ctx){
        mContext = ctx;
    }

    public static Context getContext(){
        if(mContext == null) {
            throw new NullPointerException("HGUtils context is null, please add the 'HGUtils.init()' in your Application");
        }
        return mContext;
    }
}
