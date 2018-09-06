package com.huige.library.utils;

import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Looper;
import android.widget.Toast;

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
     * @param ctx 上下文
     * @return 手否有网
     */
    public static boolean isNetworkConnected(Context ctx) {
        ConnectivityManager connectivityManager = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo() != null;
    }

    /***
     * 弹出提示
     * @param ctx 上下文
     * @param resId string配置的资源id
     */
    public static void toast(Context ctx, int resId) {
        toast(ctx, ctx.getText(resId));
    }

    /***
     * 弹出提示
     *
     * 解决子线程不加Loop, toast会奔溃, 而主线程加了, 也会奔溃
     *
     * @param ctx 上下文
     * @param message 提示信息文言
     */
    public static void toast(Context ctx, CharSequence message) {
        if (Thread.currentThread().getName().equals("main")) {
            showToast(ctx, message);
        } else {

            Looper.prepare();
            showToast(ctx, message);
            Looper.loop();
        }
    }

    /**
     * 解决子线程和主线程
     *
     * @param ctx     上下文
     * @param message msg
     */
    private static void showToast(Context ctx, CharSequence message) {
        if (mToast == null) {
            mToast = Toast.makeText(ctx, message, Toast.LENGTH_SHORT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mToast.cancel();
            mToast = Toast.makeText(ctx, message, Toast.LENGTH_SHORT);
        } else {
            mToast.setDuration(Toast.LENGTH_SHORT);
            mToast.setText(message);
        }
//		toast.setGravity(Gravity.CENTER, 0, 0);//设置显示的位置
//	    LinearLayout toastView = (LinearLayout) toast.getView(); //带图片效果
//	    ImageView imageCodeProject = new ImageView(getApp().getApplicationContext());
//	    imageCodeProject.setImageResource(R.drawable.f047);
//	    toastView.addView(imageCodeProject, 0);
        mToast.show();
    }



}
