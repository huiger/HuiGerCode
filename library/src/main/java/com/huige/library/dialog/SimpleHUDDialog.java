package com.huige.library.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.Html;
import android.view.Gravity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.huige.library.R;

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2018/11/9 0009 下午 04:21.
 *  Email  : zhihuiemail@163.com
 *  Desc   :
 * </pre>
 */
public class SimpleHUDDialog extends Dialog{
    public SimpleHUDDialog(Context context, int theme) {
        super(context, theme);
    }

    public static SimpleHUDDialog createDialog(Context context) {
        SimpleHUDDialog dialog = new SimpleHUDDialog(context, R.style.SimpleHUD);
        dialog.setContentView(R.layout.layout_simplehud);
        dialog.getWindow().getAttributes().gravity = Gravity.CENTER;
        return dialog;
    }
    public static SimpleHUDDialog createDialog(Context context,int layoutId) {
        SimpleHUDDialog dialog = new SimpleHUDDialog(context, R.style.SimpleHUD_NotBackground);
        dialog.setContentView(layoutId);
        dialog.getWindow().getAttributes().gravity = Gravity.BOTTOM;
        return dialog;
    }

    public void setMessage(String message) {
        TextView msgView = (TextView)findViewById(R.id.simplehud_message);
        if(null != msgView){
            msgView.setText(Html.fromHtml(message));
        }
    }
    public void setImage(Context ctx, int resId) {
        ImageView image = (ImageView)findViewById(R.id.simplehud_image);
        if(null != image){
            image.setImageResource(resId);
        }

        if(resId==R.mipmap.simplehud_spinner) {
            Animation anim = AnimationUtils.loadAnimation(ctx, R.anim.simplehud_progressbar);
            anim.start();
            image.startAnimation(anim);
        }
    }
}
