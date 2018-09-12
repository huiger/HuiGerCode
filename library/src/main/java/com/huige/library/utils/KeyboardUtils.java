package com.huige.library.utils;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

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
     * @param ctx 上下文
     * @param v view
     */
    public static void showKeyBoard(Context ctx, View v){
        InputMethodManager imm = (InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(v, InputMethodManager.SHOW_FORCED);
    }

    /**
     * 如果显示则隐藏,反之显示
     * @param ctx 上下文
     */
    public static void changeKeyBoard(Context ctx){
        InputMethodManager imm = (InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 隐藏软键盘
     * @param ctx 上下文
     * @param v view
     */
    public static void hideKeyBoard(Context ctx, View v){
        InputMethodManager imm = (InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

}
