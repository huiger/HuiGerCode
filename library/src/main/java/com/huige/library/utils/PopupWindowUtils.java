package com.huige.library.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.IdRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.PopupWindow;

/**
 * Author : huiGer
 * Time   : 2018/6/15 0015 下午 02:50.
 * Desc   : popupwind 工具类
 */
public class PopupWindowUtils extends PopupWindow {
    private View rootView;
    private WindowManager.LayoutParams params;

    public PopupWindowUtils(Context context, int layoutId) {
        super(context);
        setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setOutsideTouchable(true);
        setFocusable(true);
        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        rootView = LayoutInflater.from(context).inflate(layoutId, null, false);
        setContentView(rootView);
    }

    public PopupWindowUtils setStyle(int style){
        setAnimationStyle(style);
        return this;
    }

    public PopupWindowUtils setLayoutParams(int width, int height) {
        setWidth(width);
        setHeight(height);
        return this;
    }

    public PopupWindowUtils setOnClickListenerByViewId(@IdRes int id, View.OnClickListener listener) {
        if (listener != null) {
            rootView.findViewById(id).setOnClickListener(listener);
        }
        dismiss();
        return this;
    }


    public void showAtLocation(final Activity activity, View parent, int gravity, int x, int y) {
        showAtLocation(parent, gravity, x, y);
        Window window = activity.getWindow();
        params = window.getAttributes();
        //当弹出Popupwindow时，背景变半透明
        params.alpha = 0.7f;
        window.setAttributes(params);
        //设置Popupwindow关闭监听，当Popupwindow关闭，背景恢复1f
        setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                params = activity.getWindow().getAttributes();
                params.alpha = 1f;
                activity.getWindow().setAttributes(params);
            }
        });
    }
}
