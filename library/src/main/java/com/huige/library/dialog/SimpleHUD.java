package com.huige.library.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;

import com.huige.library.R;
import com.huige.library.utils.log.LogUtils;

import java.lang.ref.WeakReference;

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2018/11/9 0009 下午 04:21.
 *  Email  : zhihuiemail@163.com
 *  Desc   :
 * </pre>
 */
public class SimpleHUD {
    private static int DELAYTIME = 1500;
    private static SimpleHUD mSimpleHUD = null;
    private SimpleHUDDialog mDialog;
    private Context context;
    @SuppressLint("HandlerLeak")
    private Handler handler = new MyHandler(this) {
        public void handleMessage(Message msg) {
            if (msg.what == 0)
                dismiss();
        }
    };

    private SimpleHUD() {
    }

    public static SimpleHUD getInstance() {
        synchronized (SimpleHUD.class) {
            if (mSimpleHUD == null) {
                LogUtils.d("创建了SimpleHUD");
                mSimpleHUD = new SimpleHUD();
            }
            return mSimpleHUD;
        }
    }

    public void showLoadingMessage(Context context, String msg, boolean cancelable) {
        dismiss();
        if (msg == null) {
            msg = "超速为你加载中...";
        }
        setDialog(context, msg, R.mipmap.simplehud_spinner, cancelable);
        if (mDialog != null && !mDialog.isShowing())
            mDialog.show();

    }

    public void dismiss() {
        if (isContextValid() && mDialog != null && mDialog.isShowing())
            mDialog.dismiss();
        mDialog = null;
        context = null;
    }

    private void setDialog(Context ctx, String msg, int resId, boolean cancelable) {
        setDialog(ctx, msg, resId, -1, cancelable, false);

    }

    /**
     * 判断parent view是否还存在
     * 若不存在不能调用dismis，或setDialog等方法
     *
     * @return
     */
    private boolean isContextValid() {
        if (context == null)
            return false;
        if (context instanceof Activity) {
            Activity act = (Activity) context;
            if (act.isFinishing())
                return false;
        }
        return true;
    }

    private void setDialog(Context ctx, String msg, int resId, int layoutId, boolean cancelable, boolean canceledOnTouchOutside) {
        context = ctx;

        if (!isContextValid())
            return;
        if (layoutId == -1) {
            mDialog = SimpleHUDDialog.createDialog(ctx);
            mDialog.setImage(ctx, resId);
        } else {
            mDialog = SimpleHUDDialog.createDialog(ctx, layoutId);
        }
        mDialog.setMessage(msg);
        mDialog.setCanceledOnTouchOutside(canceledOnTouchOutside);
        mDialog.setCancelable(cancelable);        //back键是否可dimiss对话框
    }

    public void showLoadingMessage(Context context, String msg, int time) {
        dismiss();
        setDialog(context, msg, R.mipmap.simplehud_spinner, true);
        if (mDialog != null) {
            mDialog.show();
            dismissAfterToTime(time);
        }
    }

    /**
     * 计时关闭对话框
     */
    private void dismissAfterToTime(final int time) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (time > 0) {
                        Thread.sleep(time);
                    } else {
                        Thread.sleep(DELAYTIME);
                    }
                    handler.sendEmptyMessage(0);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void showLoadingMessage(Context context, String msg, boolean cancelable, DialogInterface.OnCancelListener listener) {
        dismiss();
        if (msg == null) {
            msg = "超速为你加载中...";
        }
        setDialog(context, msg, R.mipmap.simplehud_spinner, cancelable);
        if (mDialog != null) {
            mDialog.setOnCancelListener(listener);
            mDialog.show();
        }

    }

    public void showErrorMessage(Context context, String msg) {
        dismiss();
        setDialog(context, msg, R.mipmap.simplehud_error, true);
        if (mDialog != null) {
            mDialog.show();
        }
    }

    public void showErrorMessage(Context context, String msg, int time) {
        dismiss();
        setDialog(context, msg, R.mipmap.simplehud_error, true);
        if (mDialog != null) {
            mDialog.show();
            dismissAfterToTime(time);
        }
    }

    public void showSuccessMessage(Context context, String msg) {
        showSuccessMessage(context, msg, DELAYTIME);
    }

    public void showSuccessMessage(Context context, String msg, int time) {
        dismiss();
        setDialog(context, msg, R.mipmap.simplehud_success, true);
        if (mDialog != null) {
            mDialog.show();
            dismissAfterToTime(time);
        }
    }

    public void showInfoMessage(Context context, String msg) {
        showInfoMessage(context, msg, DELAYTIME);
    }

    public void showInfoMessage(Context context, String msg, int time) {
        showInfoMessage(context, msg, time, null);
    }

    public void showInfoMessage(Context context, String msg, int time, DialogInterface.OnCancelListener l) {
        dismiss();
        setDialog(context, msg, R.mipmap.simplehud_info, true);
        if (mDialog != null) {
            mDialog.setOnCancelListener(l);
            mDialog.show();
            dismissAfterToTime(time);
        }
    }

    public void showErrorMessage(Context context, String msg, boolean cancelable) {
        showErrorMessage(context, msg, DELAYTIME, cancelable);
    }

    public void showErrorMessage(Context context, String msg, int time, boolean cancelable) {
        dismiss();
        setDialog(context, msg, R.mipmap.simplehud_error, cancelable);
        if (mDialog != null) {
            mDialog.show();
            dismissAfterToTime(time);
        }
    }

    public void showSuccessMessage(Context context, String msg, boolean cancelable) {
        showSuccessMessage(context, msg, DELAYTIME, cancelable);
    }

    public void showSuccessMessage(Context context, String msg, int time, boolean cancelable) {
        dismiss();
        setDialog(context, msg, R.mipmap.simplehud_success, cancelable);
        if (mDialog != null) {
            mDialog.show();
            dismissAfterToTime(time);
        }
    }

    public void showInfoMessage(Context context, String msg, boolean cancelable) {
        showInfoMessage(context, msg, DELAYTIME, cancelable);
    }

    public void showInfoMessage(Context context, String msg, int time, boolean cancelable) {
        dismiss();
        setDialog(context, msg, R.mipmap.simplehud_info, cancelable);
        if (mDialog != null) {
            mDialog.show();
            dismissAfterToTime(time);
        }
    }

    private void setDialog(Context ctx, String msg, int resId, int layoutId, boolean cancelable) {
        setDialog(ctx, msg, resId, layoutId, cancelable, false);

    }

    private static class MyHandler extends Handler {
        WeakReference<SimpleHUD> mSimpleHUD;

        public MyHandler(SimpleHUD simpleHUD) {
            mSimpleHUD = new WeakReference<>(simpleHUD);
        }
    }
}
