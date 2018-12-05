package com.huige.library.widget;

import android.animation.Animator;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.EditText;

import com.huige.library.utils.DeviceUtils;
import com.huige.library.utils.KeyboardUtils;

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2018/11/9 0009 下午 04:08.
 *  Email  : zhihuiemail@163.com
 *  Desc   :
 * </pre>
 */
public class SearchViewManage {

    /**
     *
     * @param ctx       上下文
     * @param rootView  显示布局
     * @param editText  输入窗口
     */
    public static void showSearchView(final Context ctx, final View rootView, EditText editText){
        if(rootView.getVisibility() == View.VISIBLE) {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Animator animator = ViewAnimationUtils.createCircularReveal(rootView,
                        DeviceUtils.dp2px(ctx, 30),
                        DeviceUtils.dp2px(ctx, 23),
                        (float) Math.hypot(rootView.getWidth(), rootView.getHeight()),
                        0);
                animator.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        KeyboardUtils.hideKeyBoard(rootView);
                        rootView.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });

                animator.setDuration(300);
                animator.start();
            }else{
                KeyboardUtils.hideKeyBoard(rootView);
                rootView.setVisibility(View.GONE);
            }
            editText.setText("");
            rootView.setEnabled(false);
        }else{
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Animator animator = ViewAnimationUtils.createCircularReveal(rootView,
                        DeviceUtils.dp2px(ctx, 30),
                        DeviceUtils.dp2px(ctx, 23),
                        0,
                        (float) Math.hypot(rootView.getWidth(), rootView.getHeight()));
                rootView.setVisibility(View.VISIBLE);
                animator.setDuration(300);
                animator.start();
                rootView.setEnabled(true);
            }else{
                rootView.setVisibility(View.VISIBLE);
                rootView.setEnabled(true);
            }
        }

    }
}
