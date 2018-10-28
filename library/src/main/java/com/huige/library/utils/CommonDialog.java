package com.huige.library.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.annotation.IdRes;
import android.support.annotation.IntDef;
import android.support.annotation.StringRes;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.huige.library.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Author : huiGer
 * Time   : 2018/7/26 0026 下午 05:15.
 * Desc   : 通用的dialog
 */
public class CommonDialog {

    private static CommonDialog mCommonDialog = null;
    private Context mContext;
    private int mType;
    private View rootView;
    private AlertDialog dialog;
    private OnClickListener submitClick, cancelClick;
    private OnEditSubmitListener editSubmitClick;
    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.btn_dialog_submit) {
                if (mType == DialogType.content) {
                    if (submitClick != null) {
                        submitClick.onClick(v);
                    }
                } else {
                    if (editSubmitClick != null) {
                        editSubmitClick.onClick(v, ((EditText) getView(R.id.et_dialog)).getText().toString().trim());
                    }
                }
            } else if (v.getId() == R.id.btn_dialog_cancel) {
                if (cancelClick != null)
                    cancelClick.onClick(v);
            }
            dialog.dismiss();
        }
    };

    private CommonDialog() {
    }

    public static CommonDialog getInstance() {
        if (mCommonDialog == null) {
            synchronized (CommonDialog.class) {
                if (mCommonDialog == null) {
                    mCommonDialog = new CommonDialog();
                }
            }
        }
        return mCommonDialog;
    }

    /**
     * @param context 上下文
     * @return CommonDialog
     */
    public CommonDialog init(Context context) {
        init(context, DialogType.content);
        return this;
    }

    /**
     * @param context 上下文
     * @param type    区分是文本还是输入框
     * @return CommonDialog
     */
    public CommonDialog init(Context context, @DialogType int type) {
        this.mContext = context;
        this.mType = type;
        dialog = new AlertDialog.Builder(mContext, R.style.CommonDialogStyle).create();
        rootView = LayoutInflater.from(mContext).inflate(R.layout.dialog_comment_layout, null);
        dialog.setView(rootView);

        if (type == DialogType.edit) {
            setVisible(R.id.tv_dialog_msg, false);
            setVisible(R.id.et_dialog, true);
        } else {
            setVisible(R.id.tv_dialog_msg, true);
            setVisible(R.id.et_dialog, false);
        }

        getView(R.id.btn_dialog_cancel).setOnClickListener(clickListener);
        getView(R.id.btn_dialog_submit).setOnClickListener(clickListener);

        return this;
    }

    /**
     * 设置显示状态
     *
     * @param resId   id
     * @param visible 显示状态
     * @return CommonDialog
     */
    public CommonDialog setVisible(@IdRes int resId, boolean visible) {
        getView(resId).setVisibility(visible ? View.VISIBLE : View.GONE);
        return this;
    }

    /**
     * 查找控件
     *
     * @param <T> 泛型
     * @param resId id
     * @return 查找到的view
     */
    public <T extends View> T getView(@IdRes int resId) {
        return (T) rootView.findViewById(resId);
    }

    /**
     * 设置外部是否可点击
     *
     * @param flag 外部是否可点击
     * @return CommonDialog
     */
    public CommonDialog setCancelable(boolean flag) {
        dialog.setCancelable(flag);
        return this;
    }

    /**
     * 确认
     *
     * @param listener 确定按钮监听
     * @return CommonDialog
     */
    public CommonDialog setSubmitListener(OnClickListener listener) {
        this.submitClick = listener;
        return this;
    }

    /**
     * 确认
     *
     * @param listener 提交输入的内容
     * @return CommonDialog
     */
    public CommonDialog setSubmitListener(OnEditSubmitListener listener) {
        this.editSubmitClick = listener;
        return this;
    }

    /**
     * 取消
     *
     * @param listener 取消
     * @return CommonDialog
     */
    public CommonDialog setCancelListener(OnClickListener listener) {
        this.cancelClick = listener;
        return this;
    }

    /**
     * 设置主题色
     *
     * @param color 颜色
     * @return CommonDialog
     */
    public CommonDialog setThemeColor(@ColorInt int color) {
        ((TextView) getView(R.id.tv_dialog_title)).setTextColor(color);
        ((Button) getView(R.id.btn_dialog_cancel)).setTextColor(color);
        ((Button) getView(R.id.btn_dialog_submit)).setTextColor(color);
        return this;
    }


    /**
     * 设置标题
     *
     * @param resId id
     * @return CommonDialog
     */
    public CommonDialog setTitle(@StringRes int resId) {
        setTitle(mContext.getResources().getString(resId));
        return this;
    }

    /**
     * 设置标题
     *
     * @param str 标题内容
     * @return CommonDialog
     */
    public CommonDialog setTitle(CharSequence str) {
        TextView tv = getView(R.id.tv_dialog_title);
        tv.setVisibility(View.VISIBLE);
        tv.setText(str);
        return this;
    }

    /**
     * 设置内容
     *
     * @param resId 内容资源id
     * @return CommonDialog
     */
    public CommonDialog setMessage(@StringRes int resId) {
        setTitle(mContext.getResources().getString(resId));
        return this;
    }

    /**
     * 设置内容
     *
     * @param str 内容
     * @return CommonDialog
     */
    public CommonDialog setMessage(CharSequence str) {
        ((TextView) getView(R.id.tv_dialog_msg)).setText(str);
        return this;
    }

    /**
     * 根据id返回该view
     *
     * @param id       id
     * @param callBack 返回就该view
     * @return CommonDialog
     */
    public CommonDialog setViewStyle(@IdRes int id, CustomViewCallBack callBack) {
        callBack.onCallBack(rootView.findViewById(id));
        return this;
    }


    /**
     * 添加内容布局
     *
     * @param v 添加的布局
     * @return dialog
     */
    public CommonDialog addContentView(View v) {
        FrameLayout contentView = (FrameLayout) rootView.findViewById(R.id.layout_content);
        contentView.removeAllViews();
        contentView.addView(v);
        return this;
    }

    /**
     * @return dialog
     */
    public Dialog getDialog() {
        return dialog;
    }

    /**
     * 显示
     */
    public void show() {
        if (!dialog.isShowing()) dialog.show();
    }

    @IntDef({DialogType.edit, DialogType.content})
    @Retention(RetentionPolicy.SOURCE)
    public @interface DialogType {
        int edit = 1;
        int content = 2;
    }

    public interface OnClickListener {
        void onClick(View v);
    }

    public interface OnEditSubmitListener {
        void onClick(View v, String s);
    }

    public interface CustomViewCallBack {
        void onCallBack(View v);
    }
}
