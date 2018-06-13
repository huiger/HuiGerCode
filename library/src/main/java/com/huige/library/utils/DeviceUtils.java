package com.huige.library.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

/**
 * Created by lzh on 2017/8/30.
 */

public class DeviceUtils {

    /**
     * 获取屏幕宽和高
     *
     * @param ctx 上下文
     * @return  屏幕宽和高
     */
    public static int[] getScreenWH(Context ctx) {
        int[] wh = new int[3];
        try {
            WindowManager manager = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
            DisplayMetrics dm = new DisplayMetrics();
            manager.getDefaultDisplay().getMetrics(dm);
            wh[0] = dm.widthPixels; // 屏幕宽(像素)
            wh[1] = dm.heightPixels;    // 屏幕高(像素)
            wh[2] = dm.densityDpi;  // 屏幕密度(120/160/240)
        } catch (Exception e) {
            e.printStackTrace();
        }
        return wh;
    }


    /**
     * 手机屏幕宽度
     *
     * @param ctx 上下文
     * @return 屏幕宽
     */
    public static int getWindowWidth(Context ctx) {
        Display display = ((WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        return metrics.widthPixels;
    }

    /**
     * 手机屏幕高度
     *
     * @param ctx 上下文
     * @return 屏幕高
     */
    public static int getWindowHeight(Context ctx) {
        Display display = ((WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        return metrics.heightPixels;
    }

    /**
     * dp2px
     *
     * @param ctx 上下文
     * @param dp dp
     * @return px
     */
    public static int dp2px(Context ctx, float dp) {
        float scale = ctx.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    /**
     * px2dp
     *
     * @param ctx 上下文
     * @param px px
     * @return dp
     */
    public static int px2dp(Context ctx, float px) {
        float scale = ctx.getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }

    /**
     * sp2dp
     *
     * @param ctx 上下文
     * @param sp sp
     * @return dp
     */
    public static int sp2px(Context ctx, float sp) {
        float scale = ctx.getResources().getDisplayMetrics().scaledDensity;
        return (int) (sp * scale + 0.5f);
    }

    /**
     * 返回当前程序版本名
     *
     * @return 版本号
     */
    public static String getAppVersionName(Context ctx) {
        String versionName = "";
        try {
            PackageManager pm = ctx.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), 0);
            versionName = pi.versionName;
            if (versionName == null || versionName.length() <= 0) {
                return "";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return versionName;
    }


}
