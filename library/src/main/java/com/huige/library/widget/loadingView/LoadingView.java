package com.huige.library.widget.loadingView;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;

import com.huige.library.R;


/**
 * Created by <huiGer> on 2018/4/3 17:27.
 */

public class LoadingView extends View {

    private float mRadius;  // 大半径
    private float mRadiusFloat;  // 小半径
    int[] mCirclesX;   // 圆心
    private int mCirclesY;
    private Paint mPaint;
    private int duration;   // 动画时长
    private float progess;  // 小圆圆心X轴
    private int length; // 大圆圆心间距
    private int cirClesNum = 3;
    private Path mPath;

    public float getProgess() {
        return progess;
    }

    public void setProgess(float progess) {
        this.progess = progess;
        invalidate();
    }

    public LoadingView(Context context) {
        this(context, null);
    }

    public LoadingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.LoadingView);
        duration = typedArray.getInteger(R.styleable.LoadingView_loadingViewDuration, 2000);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(typedArray.getColor(R.styleable.LoadingView_loadingViewColor, Color.parseColor("#2b2b2b")));
        mPath = new Path();
        mCirclesX = new int[cirClesNum];
        typedArray.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        for (int i = 0; i < cirClesNum; i++) {
            canvas.drawCircle(mCirclesX[i], mCirclesY, mRadius, mPaint);
        }
        canvas.drawCircle(progess, mCirclesY, mRadiusFloat, mPaint);

        drawBezier(canvas);
    }

    /**
     * 贝塞尔
     *
     * @param canvas
     */
    private void drawBezier(Canvas canvas) {
        float mindis = length;
        int postion = 0;
        for (int i = 0; i < cirClesNum; i++) {
            // 大圆与小圆, 圆心间距离
            float dis = Math.abs(progess - mCirclesX[i]);
            if (dis < mindis) {
                mindis = dis;
                postion = i;
            }
        }

        if (mindis < length) {
            // 取中点x轴位置
            float midX = (mCirclesX[postion] + progess) / 2;
            // 下半部分贝塞尔
            mPath.moveTo(mCirclesX[postion], mCirclesY + mRadius);
            mPath.quadTo(midX, mCirclesY, progess, mCirclesY + mRadiusFloat);

            mPath.lineTo(progess, mCirclesY - mRadiusFloat);
            mPath.quadTo(midX, mCirclesY, mCirclesX[postion], mCirclesY - mRadius);
            mPath.lineTo(mCirclesX[postion], mCirclesY + mRadius);
            mPath.close();

            canvas.drawPath(mPath, mPaint);
            mPath.reset();


        }


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width, height;
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(widthMeasureSpec);

        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else {
            width = getPaddingLeft() + 480 + getPaddingRight();
        }
        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            height = getPaddingTop() + 100 + getPaddingBottom();
        }
        setMeasuredDimension(width, height);


        length = width / 4;

        for (int i = 0; i < cirClesNum; i++) {
            mCirclesX[i] = length * (i + 1);
        }

        mRadius = height / cirClesNum;
        if (mRadius >= length) {
            mRadius = length / 4;
        }
        mRadiusFloat = mRadius * 0.8f;

        mCirclesY = height / 2;

        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(this, "progess", mRadius, width - mRadius);
        objectAnimator.setDuration(duration);
        // 匀速
        objectAnimator.setInterpolator(new LinearInterpolator());
        // 从结束的地方, 返回
        objectAnimator.setRepeatMode(ValueAnimator.REVERSE);
        // 一直循环
        objectAnimator.setRepeatCount(Animation.INFINITE);
        objectAnimator.start();
    }
}
