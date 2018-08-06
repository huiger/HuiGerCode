package com.huige.library.widget.keyboard;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Author : huiGer
 * Time   : 2018/8/3 0003 上午 11:11.
 * Desc   : 键盘模式
 */

@IntDef({KeyboardType.NUMBER, KeyboardType.ABC})
@Retention(RetentionPolicy.SOURCE)
public @interface KeyboardType {

    int NUMBER = 1;
    int ABC = 2;

}
