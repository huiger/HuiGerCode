package com.huige.library.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.huige.library.interfaces.OnViewResultListener;

/**
 * Author : huiGer
 * Time   : 2018/6/15 0015 下午 02:50.
 * Desc   : popupwind 工具类
 */
public class PopupWindowUtils extends PopupWindow {
    private View rootView;
    private WindowManager.LayoutParams params;

    public PopupWindowUtils(Context context, int layoutId) {
        this(context, LayoutInflater.from(context).inflate(layoutId, null, false));
    }

    public PopupWindowUtils(Context context, View rootView) {
        super(context);
        setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setOutsideTouchable(true);
        setFocusable(true);
        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        this.rootView = rootView;
        setContentView(rootView);
    }

    public PopupWindowUtils setStyle(int style) {
        setAnimationStyle(style);
        return this;
    }

    public PopupWindowUtils setLayoutParams(int width, int height) {
        setWidth(width);
        setHeight(height);
        return this;
    }

    public PopupWindowUtils setOnClickListenerByViewId(@IdRes int id, final onPopupWindClickListener listener) {
        rootView.findViewById(id).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onClick(view);
                }
                dismiss();
            }
        });
        return this;
    }

    public PopupWindowUtils setViewStyle(@IdRes int idRes, @NonNull OnViewResultListener listener) {
        listener.getView(getView(idRes));
        return this;
    }

    private <T extends View> T getView(@IdRes int idRes) {
        return (T) rootView.findViewById(idRes);
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

    public interface onPopupWindClickListener {
        void onClick(View view);
    }
}
