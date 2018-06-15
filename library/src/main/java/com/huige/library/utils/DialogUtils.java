package com.huige.library.utils;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huige.library.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Author : huiGer
 * Time   : 2018/6/14 0014 下午 05:00.
 * Desc   : dialog 工具类
 */
public class DialogUtils {

    public static final int VISIBLE = 0x00000000;
    public static final int INVISIBLE = 0x00000004;
    public static final int GONE = 0x00000008;

    private static DialogUtils mDialogUtils = null;

    public static DialogUtils getInstance() {
        if (mDialogUtils == null) {
            synchronized (DialogUtils.class) {
                if (mDialogUtils == null) {
                    mDialogUtils = new DialogUtils();
                }
            }
        }
        return mDialogUtils;
    }

    private DialogUtils() {
    }

    public Builder init(Context context) {
        return new Builder(context);
    }

    public static class Builder {
        private View rootView;
        private Dialog mDialog;

        public Builder(Context context) {
            rootView = LayoutInflater.from(context).inflate(R.layout.dialog_utils_layout, null);
            mDialog = new AlertDialog.Builder(context).create();
        }

        public Builder setTitle(CharSequence charSequence) {
            ((TextView) getView(R.id.tv_title)).setText(charSequence);
            return this;
        }

        public Builder setContent(CharSequence charSequence) {
            ((TextView) getView(R.id.tv_content)).setText(charSequence);
            return this;
        }

        public Builder addView(@NonNull OnAddViewListener listener) {
            LinearLayout rl = (LinearLayout) getView(R.id.layout_content);
            rl.removeAllViews();
            rl.addView(listener.addChildView());
            return this;
        }

        public Builder setViewStyle(@IdRes int idRes, @NonNull OnViewResultListener listener) {
            listener.getView(getView(idRes));
            return this;
        }

        public Builder setOnDialogClickListener(@IdRes int idRes, final OnClickListener listener) {
            getView(idRes).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDialog.dismiss();
                    if (listener != null)
                        listener.onClick(v);
                }
            });
            return this;
        }

        public Builder setVisibleById(@IdRes int idRes, @Visibility int visibility) {
            getView(idRes).setVisibility(visibility);
            return this;
        }

        public void show() {
            mDialog.show();
            mDialog.setContentView(rootView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }

        private <T extends View> T getView(@IdRes int idRes) {
            return (T) rootView.findViewById(idRes);
        }
    }


    @IntDef({VISIBLE, INVISIBLE, GONE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Visibility {
    }

    public interface OnViewResultListener {
        void getView(View view);
    }

    public interface OnAddViewListener {
        View addChildView();
    }

    public interface OnClickListener {
        void onClick(View view);
    }
}
