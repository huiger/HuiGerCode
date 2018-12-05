package com.huige.library.utils;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.huige.library.HGUtilsApp;

/**
 * Author : huiGer
 * Time   : 2018/8/11 0011 下午 02:23.
 * Desc   : 键盘操作
 */
public class KeyboardUtils {

    /**
     * 显示软键盘
     *
     * SHOW_FORCED 强制显示
     * @param v view
     */
    public static void showKeyBoard(View v){
        InputMethodManager imm = (InputMethodManager) HGUtilsApp.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(v, InputMethodManager.SHOW_FORCED);
    }

    /**
     * 如果显示则隐藏,反之显示
     */
    public static void changeKeyBoard(){
        InputMethodManager imm = (InputMethodManager) HGUtilsApp.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 隐藏软键盘
     * @param v view
     */
    public static void hideKeyBoard(View v){
        InputMethodManager imm = (InputMethodManager) HGUtilsApp.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    /**
     * 根据传入控件的坐标和用户的焦点坐标，判断是否隐藏键盘，如果点击的位置在控件内，则不隐藏键盘
     * 用于点击空白处, 影藏输入框
     *
     * @param view
     *            控件view
     * @param event
     *            焦点位置
     */
    public static void hideKeyboard(MotionEvent event, View view) {
        try {
            if (view != null && view instanceof EditText) {
                int[] location = { 0, 0 };
                view.getLocationInWindow(location);
                int left = location[0], top = location[1], right = left
                        + view.getWidth(), bootom = top + view.getHeight();
                // 判断焦点位置坐标是否在控间内，如果位置在控件外，则隐藏键盘
                if (event.getRawX() < left || event.getRawX() > right
                        || event.getY() < top || event.getRawY() > bootom) {
                    // 隐藏键盘
                    hideKeyBoard(view);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
