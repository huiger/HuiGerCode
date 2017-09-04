package com.huige.library.widget.TabLayout;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.support.annotation.ColorInt;
import android.util.AttributeSet;
import android.view.View;

import com.huige.library.utils.DeviceUtils;

/**
 * Created by <lzh> on 2017/9/1.
 */

public class TabLineView extends View{

    private Paint paint;
    private float startX, endX;
    private RectF rectF;

    public TabLineView(Context context) {
        this(context, null);
    }

    public TabLineView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TabLineView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initView();


    }



    private void initView() {

        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(5);
//        paint.setColor();
        paint.setShader(new LinearGradient(0, 100, DeviceUtils.getWindowWidth(getContext()),
                100,  Color.parseColor("#ffc125"), Color.parseColor("#ff4500"), Shader.TileMode.MIRROR));

        rectF = new RectF(startX, 0, endX, 0);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(20, MeasureSpec.getMode(heightMeasureSpec));
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        rectF.set(startX, 0, endX, 10);
        canvas.drawRoundRect(rectF, 5, 5, paint);

    }

    public void updateLineStatus(float startX, float endX){
        this.startX = startX;
        this.endX = endX;
        invalidate();
    }

    public void setLineColor(@ColorInt int color){
        if(color != -1) {
            paint.setColor(color);
            paint.setShader(null);
            invalidate();
        }
   }

}
