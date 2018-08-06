package com.huige.library.widget.keyboard;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.support.annotation.IntegerRes;
import android.util.AttributeSet;

import com.huige.library.R;

import java.util.List;

/**
 * Author : huiGer
 * Time   : 2018/8/6 0006 下午 04:17.
 * Desc   : 键盘
 */
public class CustomKeyboardView extends KeyboardView {
    final int kbdPaddingLeft = getPaddingLeft();
    final int kbdPaddingTop = getPaddingTop();

    public CustomKeyboardView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Keyboard keyboard = getKeyboard();
        if (keyboard == null) return;
        List<Keyboard.Key> keys = keyboard.getKeys();
        if (keys != null && keys.size() > 0) {
            Paint paint = new Paint();
            paint.setTextAlign(Paint.Align.CENTER);
            Typeface font = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD);
            paint.setTypeface(font);
            paint.setAntiAlias(true);
            for (Keyboard.Key key : keys) {

//                if (key.label != null) {
//                    if (key.codes[0] == -4 ||
//                            key.codes[0] == -5) {
//                        paint.setTextSize(17 * 2);
//                    } else {
//                        paint.setTextSize(20 * 2);
//                    }
//                    if (key.codes[0] == -4) {
//                        paint.setColor(getContext().getResources().getColor(R.color.white));
//                    } else {
//                        paint.setColor(getContext().getResources().getColor(R.color.blue_03A9F4));
//                    }
//                    Rect rect = new Rect(key.x, key.y, key.x + key.width, key.y + key.height);
//                    Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
//                    int baseline = (rect.bottom + rect.top - fontMetrics.bottom - fontMetrics.top) / 2;
//                    // 下面这行是实现水平居中，drawText对应改为传入targetRect.centerX()
//                    paint.setTextAlign(Paint.Align.CENTER);
//                    canvas.drawText(key.label.toString(), rect.centerX(), baseline, paint);
//                }

                if (key.codes[0] == -5) {
//                    Drawable dr = getContext().getResources().getDrawable(R.drawable.key_num_del_bg);
                    Drawable dr = getContext().getResources().getDrawable(R.mipmap.ic_num_del);
//                    dr.setBounds(key.x, key.y, key.x + key.width, key.y + key.height);
//                    dr.draw(canvas);

                    Rect bounds = dr.getBounds();
                    if (key.width != bounds.right ||
                            key.height != bounds.bottom) {
                        dr.setBounds(0, 0, key.width/3, key.height/3);
                    }
                    canvas.translate(key.x + kbdPaddingLeft + key.width/3, key.y + kbdPaddingTop+key.height/3);
                    dr.draw(canvas);

                }

            }
        }

    }

}
