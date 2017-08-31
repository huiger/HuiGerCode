package com.huige.library.widget.likeview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.huige.library.R;

/**
 * 点赞
 * Created by <lzh> on 2017/8/1.
 */

public class LikeView extends View {

    /**
     * 默认颜色
     */
    private final int mDefaultColor;
    /**
     * 圆最大半径（心形）
     */
    private float mRadius;
    /**
     * View变化用时
     */
    private int mCycleTime;
    /**
     * Bézier曲线画圆的近似常数
     */
    private static final float c = 0.551915024494f;
    /**
     * 选择/取消选择
     */
    private boolean state;
    /**
     * 心形默认颜色
     */
    private static final int CLICKED_CLOLOR = 0xffe53a42;
    /**
     * 环绕圆点的颜色
     */
    private static final int[] dotColors = {0xffdaa9fa, 0xfff2bf4b, 0xffe3bca6, 0xff329aed, 0xffb1eb99, 0xff67c9ad, 0xffde6bac};
    /**
     * 1.绘制心形并伴随缩小和颜色渐变
     */
    private static final int HEART_VIEW = 0;
    /**
     * 2.绘制圆并伴随放大和颜色渐变
     */
    private static final int CIRCLE_VIEW = 1;
    /**
     * 3.绘制圆环并伴随放大和颜色渐变
     */
    private static final int RING_VIEW = 2;
    /**
     * 4.圆环减消失、心形放大、周围环绕十四圆点
     */
    private static final int RING_DOT_HEART_VIEW = 3;
    /**
     * 5.环绕的十四圆点向外移动并缩小、透明度渐变、渐隐
     */
    private static final int DOT_HEART_VIEW = 4;

    private float mCenterX;
    private float mCenterY;
    private Canvas mCanvas;
    private Paint mPaint;
    private float mOffset;
    private OnClickListener mListener;
    private ValueAnimator animatorTime;
    private ValueAnimator animatorArgb;
    private int mCurrentRadius;
    private int mCurrentColor;
    private int mCurrentState;
    private float mCurrentPercent;

    private PointF tPointA;
    private PointF tPointB;
    private PointF tPointC;
    private PointF rPointA;
    private PointF rPointB;
    private PointF rPointC;
    private PointF bPointA;
    private PointF bPointB;
    private PointF bPointC;
    private PointF lPointA;
    private PointF lPointB;
    private PointF lPointC;
    private float rDotL;
    private float rDotS;
    private float offS;
    private float offL;
    private boolean isMax;
    private float dotR;

    public LikeView(Context context) {
        this(context, null);
    }

    public LikeView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LikeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.LikeView, defStyleAttr, 0);
        mRadius = typedArray.getDimension(R.styleable.LikeView_likeViewCirRadius, dp2px(10));
        mCycleTime = typedArray.getInt(R.styleable.LikeView_likeViewCirTime, 2000);
        mDefaultColor = typedArray.getColor(R.styleable.LikeView_likeViewDefaultColor, 0Xff657487);
        typedArray.recycle();

        mOffset = c * mRadius;
        mCenterX = mRadius;
        mCenterY = mRadius;
        mPaint = new Paint();

        mCurrentRadius = (int) mRadius;
        mCurrentColor = mDefaultColor;
        dotR = mRadius / 6;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mCanvas = canvas;
        mCanvas.translate(mCenterX, mCenterY);   // 使坐标移至中心

        switch (mCurrentState) {
            case HEART_VIEW:
                drawHeart(mCurrentRadius, mCurrentColor);
                break;
            case CIRCLE_VIEW:
                drawCirce(mCurrentRadius, mCurrentColor);
                break;
            case RING_VIEW:
                drawRing(mCurrentRadius, mCurrentColor, mCurrentPercent);
                break;
            case RING_DOT_HEART_VIEW:
                drawDotWithRing(mCurrentRadius, mCurrentColor);
                break;
            case DOT_HEART_VIEW:
                drawDot(mCurrentRadius, mCurrentColor);
                break;
            default:

        }

    }

    /**
     * 绘制圆点、心形
     *
     * @param radius
     * @param color
     */
    private void drawDot(int radius, int color) {
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);

        double angleA = 0;
        double angleB = -Math.PI / 20;
        float dotRS;
        float dotRL;
        if (rDotL < 2.6 * mRadius) {//限制圆点的扩散范围
            rDotS += dotR / 17;
            rDotL += dotR / 14;
        }
        if (!isMax && mCurrentRadius <= 1.1 * mRadius) {
            offL += dotR / 14;
            mCurrentRadius = (int) (mRadius / 3 + offL * 4);

        } else {
            isMax = true;
        }

        if (isMax && mCurrentRadius > mRadius) {
            mCurrentRadius = (int) (mCurrentRadius - dotR / 16);

        }
        drawHeart(mCurrentRadius, CLICKED_CLOLOR);

        mPaint.setAlpha((int) (255 * (1 - mCurrentPercent)));//圆点逐渐透明
        dotRS = (float) (dotR * (1 - mCurrentPercent));
        dotRL = (float) (dotR * (1 - mCurrentPercent) * 4) > dotR ? dotR : (float) (dotR * (1 - mCurrentPercent) * 3);
        for (int i = 0; i < 7; i++) {
            mPaint.setColor(dotColors[i]);
            mCanvas.drawCircle((float) (rDotS * Math.sin(angleA)), (float) (rDotS * Math.cos(angleA)), dotRS, mPaint);
            angleA += 2 * Math.PI / 7;
            mCanvas.drawCircle((float) (rDotL * Math.sin(angleB)), (float) (rDotL * Math.cos(angleB)), dotRL, mPaint);
            angleB += 2 * Math.PI / 7;
        }
    }

    /**
     * 绘制圆点、圆环、心形
     *
     * @param radius
     * @param color
     */
    private void drawDotWithRing(int radius, int color) {
        mPaint.setColor(color);
        mPaint.setAntiAlias(true);

        mPaint.setStyle(Paint.Style.STROKE);
        if (mCurrentPercent <= 1) {
            RectF rectF = new RectF(-radius, -radius, radius, radius);
            mCanvas.drawArc(rectF, 0, 360, false, mPaint);
        }
        mCurrentPercent = (1f - mCurrentPercent > 1f ? 1f : 1f - mCurrentPercent) * 0.2f;//用于计算圆环宽度，最小0，与动画进度负相关
        mPaint.setStrokeWidth(2 * mRadius * mCurrentPercent);

        float innerR = radius - mRadius * mCurrentPercent + dotR;
        double angleA = 0;
        double angleB = -Math.PI / 20;

        offS += dotR / 17;
        offL += dotR / 14;
        rDotS = radius - mRadius / 12 / 2 + offS;
        rDotL = innerR + offL;

        mPaint.setStyle(Paint.Style.FILL);
        for (int i = 0; i < 7; i++) {
            mCanvas.drawCircle((float) (rDotS * Math.sin(angleA)), (float) (rDotS * Math.cos(angleA)), dotR, mPaint);
            angleA += 2 * Math.PI / 7;
            mCanvas.drawCircle((float) (rDotL * Math.sin(angleB)), (float) (rDotL * Math.cos(angleB)), dotR, mPaint);
            angleB += 2 * Math.PI / 7;
        }
        mCurrentRadius = (int) (mRadius / 3 + offL * 4);
        drawHeart(mCurrentRadius, CLICKED_CLOLOR);

    }

    /**
     * 绘制圆环
     *
     * @param radius
     * @param color
     * @param percent
     */
    private void drawRing(int radius, int color, float percent) {
        mPaint.setColor(color);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(2 * mRadius * percent);
        RectF rectF = new RectF(-radius, -radius, radius, radius);
        mCanvas.drawArc(rectF, 0, 360, false, mPaint);
    }

    /**
     * 绘制圆
     *
     * @param radius
     * @param color
     */
    private void drawCirce(int radius, int color) {
        mPaint.setColor(color);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mCanvas.drawCircle(0f, 0f, radius, mPaint);
    }

    /**
     * 绘制心形
     *
     * @param radius
     * @param color
     */
    private void drawHeart(int radius, int color) {
        initControlPoint(radius);
        mPaint.setColor(color);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);

        Path path = new Path();
        path.moveTo(tPointB.x, tPointB.y);
        path.cubicTo(tPointC.x, tPointC.y, rPointA.x, rPointA.y, rPointB.x, rPointB.y);
        path.cubicTo(rPointC.x, rPointC.y, bPointC.x, bPointC.y, bPointB.x, bPointB.y);
        path.cubicTo(bPointA.x, bPointA.y, lPointC.x, lPointC.y, lPointB.x, lPointB.y);
        path.cubicTo(lPointA.x, lPointA.y, tPointA.x, tPointA.y, tPointB.x, tPointB.y);
        mCanvas.drawPath(path, mPaint);

    }

    /**
     * 初始化Bézier 曲线四组控制点
     *
     * @param radius
     */
    private void initControlPoint(int radius) {
        mOffset = c * radius;

        tPointA = new PointF(-mOffset, -radius);
        tPointB = new PointF(0, -radius * 0.5f);
        tPointC = new PointF(mOffset, -radius);

        rPointA = new PointF(radius, -mOffset);
        rPointB = new PointF(radius, 0);
        rPointC = new PointF(radius * 0.9f, mOffset);

        bPointA = new PointF(-mOffset, radius * 0.7f);
        bPointB = new PointF(0, radius);
        bPointC = new PointF(mOffset, radius * 0.7f);

        lPointA = new PointF(-radius, -mOffset);
        lPointB = new PointF(-radius, 0);
        lPointC = new PointF(-radius * 0.9f, mOffset);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (x + getLeft() < getRight() && y + getTop() < getBottom()) {
                if (state) {
                    deselectLike();
                } else {
                    startViewMotion();
                }

                if (mListener != null) {
                    mListener.onClick(this);
                }
            }
        }
        return true;
    }

    /**
     * 点击
     */
    private void startViewMotion() {
        if (animatorTime != null && animatorTime.isRunning())
            return;
        resetState();
        animatorTime = ValueAnimator.ofInt(0, 1200);
        animatorTime.setDuration(mCycleTime);
        animatorTime.setInterpolator(new LinearInterpolator());//需要随时间匀速变化
        animatorTime.start();
        animatorTime.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int animatedValue = (int) animation.getAnimatedValue();

                if (animatedValue == 0) {
                    if (animatorArgb == null || !animatorArgb.isRunning()) {
                        animatorArgb = ofArgb(mDefaultColor, 0Xfff74769, 0Xffde7bcc);//100?
                        animatorArgb.setDuration(mCycleTime * 28 / 120);
                        animatorArgb.setInterpolator(new LinearInterpolator());
                        animatorArgb.start();
                    }
                } else if (animatedValue <= 100) {
                    float percent = calcPercent(0f, 100f, animatedValue);
                    mCurrentRadius = (int) (mRadius - mRadius * percent);
                    if (animatorArgb != null && animatorArgb.isRunning())
                        mCurrentColor = (int) animatorArgb.getAnimatedValue();
                    mCurrentState = HEART_VIEW;
                    invalidate();

                } else if (animatedValue <= 280) {
                    float percent = calcPercent(100f, 340f, animatedValue);//此阶段未达到最大半径
                    mCurrentRadius = (int) (2 * mRadius * percent);
                    if (animatorArgb != null && animatorArgb.isRunning())
                        mCurrentColor = (int) animatorArgb.getAnimatedValue();
                    mCurrentState = CIRCLE_VIEW;
                    invalidate();
                } else if (animatedValue <= 340) {
                    float percent = calcPercent(100f, 340f, animatedValue);//半径接上一阶段增加，此阶段外环半径已经最大值
                    mCurrentPercent = 1f - percent + 0.2f > 1f ? 1f : 1f - percent + 0.2f;//用于计算圆环宽度，最小0.2，与动画进度负相关
                    mCurrentRadius = (int) (2 * mRadius * percent);
                    if (animatorArgb != null && animatorArgb.isRunning())
                        mCurrentColor = (int) animatorArgb.getAnimatedValue();
                    mCurrentState = RING_VIEW;
                    invalidate();
                } else if (animatedValue <= 480) {
                    float percent = calcPercent(340f, 480f, animatedValue);//内环半径增大直至消亡
                    mCurrentPercent = percent;
                    mCurrentRadius = (int) (2 * mRadius);//外环半径不再改变
                    mCurrentState = RING_DOT_HEART_VIEW;
                    invalidate();
                } else if (animatedValue <= 1200) {
                    float percent = calcPercent(480f, 1200f, animatedValue);
                    mCurrentPercent = percent;
                    mCurrentState = DOT_HEART_VIEW;
                    if (animatedValue == 1200) {
                        animatorTime.cancel();
                        animatorTime.removeAllListeners();
                    }
                    invalidate();

                }


            }
        });
    }

    private float calcPercent(float start, float end, float current) {
        return (current - start) / (end - start);
    }

    /**
     * @param values
     * @return 由于颜色变化的动画API是SDK21 添加的，这里导入了源码的 ArgbEvaluator
     */
    private ValueAnimator ofArgb(int... values) {
        ValueAnimator anim = new ValueAnimator();
        anim.setIntValues(values);
        anim.setEvaluator(ArgbEvaluator.getInstance());
        return anim;
    }

    /**
     * 重置选择
     */
    private void resetState() {
        mCurrentPercent = 0;
        mCurrentRadius = 0;
        isMax = false;
        rDotS = 0;
        rDotL = 0;
        offS = 0;
        offL = 0;
        state = true;
    }

    /**
     * 取消选择
     */
    private void deselectLike() {
        if (animatorTime != null && animatorTime.isRunning())
            return;
        mCurrentColor = mDefaultColor;
        mCurrentRadius = (int) mRadius;
        mCurrentState = HEART_VIEW;
        state = false;
        invalidate();
    }

    /**
     * 设置选中
     */
    public void setSelectLike() {
        mCurrentColor = CLICKED_CLOLOR;
        state = true;
        invalidate();
    }

    public boolean getStatus() {
        return this.state;
    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        mListener = l;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mCenterX = w / 2;
        mCenterY = h / 2;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width, height;
        width = (int) (5.2 * mRadius + 2 * dotR);
        height = (int) (5.2 * mRadius + 2 * dotR);
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (animatorTime != null)
            animatorTime.removeAllListeners();
        if (animatorArgb != null)
            animatorArgb.removeAllListeners();
    }

    private float dp2px(int value) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, getResources().getDisplayMetrics());
    }
}
