package com.huige.huigercode.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.widget.Toast;

import com.huige.huigercode.App;

/**
 * Author : huiGer
 * Time   : 2018/6/26 0026 下午 02:26.
 * Desc   :
 */
public class CommonUtils {

    private static Toast mToast;

    /**
     * 消息提示
     *
     * @param charSequence 消息
     */
    public static void showToast(CharSequence charSequence) {
        if (mToast == null) {
            mToast = Toast.makeText(App.getContext(), charSequence, Toast.LENGTH_SHORT);
        }
        mToast.setText(charSequence);
        mToast.show();
    }

    /**
     * 检查是否有可用网络
     */
    public static boolean isNetworkConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) App.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo() != null;
    }


}
