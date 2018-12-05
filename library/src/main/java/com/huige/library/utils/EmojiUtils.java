package com.huige.library.utils;

import android.text.TextUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2018/11/19 0019 下午 12:03.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 表情符 emoji
 * </pre>
 */

public class EmojiUtils {


    /**
     *
     * @param str 字符串
     * @return 带emj的字符串
     */
    public static String getEmoji(String str) {

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
     * @param string 字符串转换unicode
     * @return 字符串转换unicode
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
     * @param unicode 转字符串
     * @return unicode 转字符串
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

    /*--------------------------------- 处理Emoji表情（解决数据库不支持utf8mb4的问题）---------------------------------*/


    //编码
    public static String emojiConvert(String str) throws UnsupportedEncodingException {
        String patternString = "([\\x{10000}-\\x{10ffff}\ud800-\udfff])";
        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            try {
                matcher.appendReplacement(
                        sb,
                        "[["
                                + URLEncoder.encode(matcher.group(1),
                                "UTF-8") + "]]");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                throw e;
            }
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    //解码
    public static String emojiRecovery(String str) throws UnsupportedEncodingException {
        String patternString = "\\[\\[(.*?)\\]\\]";
        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            try {
                matcher.appendReplacement(sb,
                        URLDecoder.decode(matcher.group(1), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                throw e;
            }
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    /**
     * @param str 原字符串
     * @return 删除emj后的字符串
     */
    public static String cleanEmoji(String str) {
        String patternString = "([\\x{10000}-\\x{10ffff}\ud800-\udfff])";
        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb,"");
        }
        matcher.appendTail(sb);
        return sb.toString();
    }
}
