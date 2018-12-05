package com.huige.huigercode.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.View;

/**
 * Author : huiGer
 * Time   : 2018/11/8 0008 上午 11:26.
 * Desc   :
 */
public class CustomView extends View {

    private final int STEP = 50;
    private int mWidth;
    private int mHeight;
    private Paint mGridPaint;
    private Point mCoo;

    public CustomView(Context context) {
        this(context, null);
    }

    public CustomView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        mGridPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mGridPaint.setColor(Color.GRAY);
        mGridPaint.setStrokeWidth(2);
        mGridPaint.setStyle(Paint.Style.STROKE);

        mCoo = new Point(500, 500);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawGrid(canvas, mGridPaint);

    }

    /**
     * 绘制网格
     * @param canvas
     * @param paint
     */
    public void drawGrid(Canvas canvas, Paint paint) {
        Path path = new Path();
        // 横线
        for (int i = 0; i < mHeight / STEP + 1; i++) {
            path.moveTo(0, STEP * i);
            path.lineTo(mWidth, STEP * i);
        }

        // 竖线
        for (int i = 0; i < mWidth / STEP + 1; i++) {
            path.moveTo(STEP * i, 0);
            path.lineTo(STEP * i, mHeight);
        }

        // 设置虚线效果new float[]{可见长度, 不可见长度},偏移值
        paint.setPathEffect(new DashPathEffect(new float[]{10, 5}, 0));
        canvas.drawPath(path, paint);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);
    }
}
