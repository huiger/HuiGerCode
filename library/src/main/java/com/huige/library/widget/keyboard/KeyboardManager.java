package com.huige.library.widget.keyboard;

import android.app.Activity;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.os.Build;
import android.support.annotation.IntegerRes;
import android.text.Editable;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.PopupWindow;

import com.huige.library.R;
import com.huige.library.utils.PopupWindowUtils;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

/**
 * Author : huiGer
 * Time   : 2018/8/3 0003 上午 11:10.
 * Desc   : 键盘管理
 * <p>
 * 参数一: Activity, 参数二: 输入的类型, 参数三: 可传入多个EditText,
 * KeyboardManager.getInstance().init(this, KeyboardType.NUMBER, et1, et2);
 * <p>
 * 提供这个方法, 主要是因为有时候页面可能又有纯数字,又有纯字母
 * KeyboardManager getNewInstance(){}
 */
public class KeyboardManager implements KeyboardView.OnKeyboardActionListener, View.OnFocusChangeListener, View.OnClickListener {


    private static KeyboardManager mKeyboardManager = null;
    private List<EditText> mEtList;
    private FrameLayout rootView;
    private Activity mContext;
    private Keyboard keyboard = null;
    private PopupWindowUtils mPopupWindow;
    private int index = 0;

    private KeyboardManager() {
    }

    public static KeyboardManager getInstance() {
        if (mKeyboardManager == null) {
            synchronized (KeyboardManager.class) {
                if (mKeyboardManager == null) {
                    mKeyboardManager = new KeyboardManager();
                }
            }
        }
        return mKeyboardManager;
    }

    public static KeyboardManager getNewInstance() {
        return new KeyboardManager();
    }

    public void init(Activity ctx, @KeyboardType int type, EditText... ets) {

        mContext = ctx;

        mEtList = Arrays.asList(ets);

        hideSystemSoftKeyboard();

        rootView = (FrameLayout) LayoutInflater.from(ctx).inflate(R.layout.layout_keyboard, null);
//        KeyboardView keyboardView = (KeyboardView) rootView.findViewById(R.id.keyboardView);
        CustomKeyboardView keyboardView = (CustomKeyboardView) rootView.findViewById(R.id.keyboardView);

        if (type == KeyboardType.NUMBER) {
            // 数字
            keyboard = new Keyboard(ctx, R.xml.keyboard_number);
        } else if (type == KeyboardType.ABC) {
            // 字母
            keyboard = new Keyboard(ctx, R.xml.keyboard_abc);
        }

        keyboardView.setKeyboard(keyboard);
        keyboardView.setEnabled(true);
        // 预览
        keyboardView.setPreviewEnabled(false);
        keyboardView.setOnKeyboardActionListener(this);

        initListener();

        initPopwind();

    }

    /**
     * 隐藏系统键盘
     */
    public void hideSystemSoftKeyboard() {
        for (EditText editText : mEtList) {
            int sdkInt = Build.VERSION.SDK_INT;
            if (sdkInt >= 11) {
                try {
                    Class<EditText> cls = EditText.class;
                    Method setShowSoftInputOnFocus;
                    setShowSoftInputOnFocus = cls.getMethod("setShowSoftInputOnFocus", boolean.class);
                    setShowSoftInputOnFocus.setAccessible(true);
                    setShowSoftInputOnFocus.invoke(editText, false);
                } catch (SecurityException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                editText.setInputType(InputType.TYPE_NULL);
            }
        }
    }

    private void initListener() {
        for (EditText et : mEtList) {
            et.setOnFocusChangeListener(this);
            et.setOnClickListener(this);
        }
    }

    private void initPopwind() {
        mPopupWindow = new PopupWindowUtils(mContext, rootView);
        mPopupWindow.setLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setFocusable(false);
//        mPopupWindow.setOutsideTouchable(false);
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                hintKeyboard();
            }
        });
    }

    /**
     * 隐藏键盘
     */
    private void hintKeyboard() {

        for (int i = 0; i < mEtList.size(); i++) {
            EditText et = mEtList.get(i);
            et.setFocusable(false);
            et.setFocusableInTouchMode(false);
            et.clearFocus();
        }

    }

    @Override
    public void onFocusChange(View view, boolean b) {
        for (int i = 0; i < mEtList.size(); i++) {
            EditText editText = mEtList.get(i);
            if (view == editText && b) {
                index = i;
                if (mPopupWindow != null && !mPopupWindow.isShowing()) {
                    mPopupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
                }
                return;
            }
        }


    }

    @Override
    public void onPress(int i) {

    }

    @Override
    public void onRelease(int i) {

    }

    @Override
    public void onKey(int primaryCode, int[] ints) {
        // 键被按下时回调，在onPress后面。如果isRepeat属性设置为true，长按时会连续回调
        if (primaryCode == getKeyCode(R.integer.action_done)) {
            if (index == mEtList.size() - 1) {
                // 确认
                mPopupWindow.dismiss();
            } else {
                EditText nextEditText = mEtList.get(index + 1);
                setFocus(nextEditText);
            }
        } else {

            EditText et = mEtList.get(index);
            Editable editable = et.getText();
            int start = et.getSelectionStart();
            int end = et.getSelectionEnd();
            if (end > start) {
                editable.delete(start, end);
            }
            if (primaryCode == Keyboard.KEYCODE_DELETE) {
                // 如果按下的是delete键，就删除EditText中的str
                if (editable != null && editable.length() > 0) {
                    if (start > 0) {
                        editable.delete(start - 1, start);
                    }
                }
            } else {
                // 把该键对应的string值设置到EditText中
                editable.insert(start, Character.toString((char) primaryCode));
            }
        }
    }

    public int getKeyCode(@IntegerRes int redId) {
        return mContext.getResources().getInteger(redId);
    }

    private void setFocus(EditText et) {
        et.setFocusable(true);
        et.setFocusableInTouchMode(true);
        et.requestFocus();
        et.findFocus();
    }

    @Override
    public void onText(CharSequence charSequence) {

    }

    @Override
    public void swipeLeft() {

    }

    @Override
    public void swipeRight() {

    }

    @Override
    public void swipeDown() {

    }

    @Override
    public void swipeUp() {

    }

    @Override
    public void onClick(View view) {
        for (EditText editText : mEtList) {
            if (view == editText) {
                if (!editText.isFocused()) {
                    setFocus(editText);
                    return;
                }
            }
        }
    }
}
