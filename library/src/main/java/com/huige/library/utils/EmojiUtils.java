package com.huige.library.utils;

import android.text.TextUtils;
import android.util.Log;

/**
 * 表情符
 * Created by lzh on 2017/3/17.
 */

public class EmojiUtils {


    public static String getEmoji(String str) {

        Log.d("msg", "EmojiUtils -> getEmoji: " + "name - " + str);

        if (TextUtils.isEmpty(str)) {
            return "";
        }
        if (str.length() < 12 || !str.contains("\\u")) {
            return str;
        }

        String emojiStr = "";
        try {
            emojiStr = str.substring(0, str.indexOf("\\u"));
            String temp = str.substring(str.indexOf("\\u"), str.length());
            int a = temp.length();
            for (int i = 0; i < a; i++) {
                if (temp.indexOf("\\u") == 0) {
                    emojiStr += unicode2String(temp.substring(0, 12));
                    temp = temp.substring(12, temp.length());
                    i += 11;
                } else if (temp.contains("\\u")) {
                    emojiStr += temp.substring(0, temp.indexOf("\\u"));
                    i += temp.indexOf("\\u");
                    temp = temp.substring(temp.indexOf("\\u"), temp.length());
                } else {
                    emojiStr += temp;
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return emojiStr;
    }


    /**
     * 字符串转换unicode
     */
    public static String string2Unicode(String string) {

        StringBuffer unicode = new StringBuffer();

        for (int i = 0; i < string.length(); i++) {

            // 取出每一个字符
            char c = string.charAt(i);

            // 转换为unicode
            unicode.append("\\u" + Integer.toHexString(c));
        }

        return unicode.toString();
    }

    /**
     * unicode 转字符串
     */
    public static String unicode2String(String unicode) {

        StringBuffer string = new StringBuffer();
        try {
            String[] hex = unicode.split("\\\\u");

            for (int i = 1; i < hex.length; i++) {

                // 转换出每一个代码点
                int data = Integer.parseInt(hex[i], 16);

                // 追加成string
                string.append((char) data);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        return string.toString();
    }
}
