package com.huige.library.utils;

import android.os.Build;
import android.os.Looper;
import android.text.TextUtils;
import android.widget.Toast;

import com.huige.library.HGUtilsApp;

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2018/10/27 0027 下午 12:07.
 *  Email  : zhihuiemail@163.com
 *  Desc   :
 * </pre>
 */
public class ToastUtils {

    private static Toast mToast;

    /***
     * 弹出提示
     * @param resId string配置的资源id
     */
    public static void showToast(int resId) {
        showToast(HGUtilsApp.getContext().getText(resId));
    }

    /***
     * 弹出提示
     *
     * 解决子线程不加Loop, toast会奔溃, 而主线程加了, 也会奔溃
     *
     * @param message 提示信息文言
     */
    public static void showToast(CharSequence message) {
        if ("main".equals(Thread.currentThread().getName())) {
            toast(message);
        } else {
            Looper.prepare();
            toast(message);
            Looper.loop();
        }
    }

    public static void showToast(CharSequence message, CharSequence msgDefault){
        if(TextUtils.isEmpty(message)) {
            message = msgDefault;
        }
        showToast(message);
    }

    /**
     * 解决子线程和主线程
     *
     * @param message
     */
    private static void toast(CharSequence message) {
        if(TextUtils.isEmpty(message)) {
            return;
        }
        if (mToast == null) {
            mToast = Toast.makeText(HGUtilsApp.getContext(), message, Toast.LENGTH_SHORT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mToast.cancel();
            mToast = Toast.makeText(HGUtilsApp.getContext(), message, Toast.LENGTH_SHORT);
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
