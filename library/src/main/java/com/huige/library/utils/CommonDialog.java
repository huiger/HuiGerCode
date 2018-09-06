package com.huige.library.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.annotation.IdRes;
import android.support.annotation.IntDef;
import android.support.annotation.StringRes;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

    public CommonDialog init(Context context) {
        init(context, DialogType.content);
        return this;
    }

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
     * @param resId
     * @param visible
     * @return CommonDialog
     */
    public CommonDialog setVisible(@IdRes int resId, boolean visible) {
        getView(resId).setVisibility(visible ? View.VISIBLE : View.GONE);
        return this;
    }

    /**
     * 查找控件
     *
     * @param resId id
     * @param <T> view
     * @return CommonDialog
     */
    public <T extends View> T getView(@IdRes int resId) {
        return (T)rootView.findViewById(resId);
    }

    /**
     * 确认
     *
     */
    public CommonDialog setSubmitListener(OnClickListener listener) {
        this.submitClick = listener;
        return this;
    }

    public CommonDialog setSubmitListener(OnEditSubmitListener listener) {
        this.editSubmitClick = listener;
        return this;
    }

    /**
     * 取消
     *
     */
    public CommonDialog setCancelListener(OnClickListener listener) {
        this.cancelClick = listener;
        return this;
    }

    /**
     * 设置主题色
     *
     * @param color 颜色
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
     */
    public CommonDialog setTitle(@StringRes int resId) {
        setTitle(mContext.getResources().getString(resId));
        return this;
    }

    /**
     * 设置标题
     *
     */
    public CommonDialog setTitle(CharSequence str) {
        TextView tv = getView(R.id.tv_dialog_title);
        tv.setVisibility(View.VISIBLE);
        tv.setText(str);
        return this;
    }

    /**
     * 设置标题
     *
     */
    public CommonDialog setMessage(@StringRes int resId) {
        setTitle(mContext.getResources().getString(resId));
        return this;
    }

    /**
     * 设置标题
     */
    public CommonDialog setMessage(CharSequence str) {
        ((TextView) getView(R.id.tv_dialog_msg)).setText(str);
        return this;
    }

    /**
     * 根据id返回该view
     */
    public CommonDialog setViewStyle(@IdRes int id, CustomViewCallBack callBack) {
        callBack.onCallBack(rootView.findViewById(id));
        return this;
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
