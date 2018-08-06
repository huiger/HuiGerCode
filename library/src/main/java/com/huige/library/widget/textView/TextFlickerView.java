package com.huige.library.widget.textView;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Shader;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.animation.LinearInterpolator;

import com.huige.library.R;

/****************************************************************
 * *     *  * * * *     Created by huiGer
 * *     *  *           Time : 2018/5/2 11:27
 * * * * *  *   * *     Email: zhihuiemail@163.com
 * *     *  *     *     blog : huiGer.top
 * *     *  * * * *     Desc : 流光字
 ****************************************************************/
public class TextFlickerView extends AppCompatTextView {

    private Matrix mShadowMatrix;
    private LinearGradient mLinearGradient;
    private int shadowWidth = 50;    // 闪光的宽度
    private long timeDuration;
    private ObjectAnimator objectAnimator;
    private float value;
    private int flickerColor;


    public TextFlickerView(Context context) {
        this(context, null);
    }

    public TextFlickerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TextFlickerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TextFlickerView);
        timeDuration = typedArray.getInteger(R.styleable.TextFlickerView_animatorDuration, 1500);
        flickerColor = typedArray.getColor(R.styleable.TextFlickerView_flickerColor, Color.RED);

        typedArray.recycle();
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
        mShadowMatrix.setTranslate(value, 0);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(mShadowMatrix != null) {
            mLinearGradient.setLocalMatrix(mShadowMatrix);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        init(w);
    }

    private void init(int w) {

        if (mShadowMatrix == null && w != 0) {
            mShadowMatrix = new Matrix();

            // 当前文字颜色
            int currentTextColor = getCurrentTextColor();
            // 线性渐变
            mLinearGradient = new LinearGradient(0, 0, shadowWidth, 0, new int[]{currentTextColor, flickerColor, currentTextColor}, null, Shader.TileMode.CLAMP);
            // 设置
            getPaint().setShader(mLinearGradient);

            objectAnimator = ObjectAnimator.ofFloat(this, "value", -shadowWidth, w).setDuration(timeDuration);
            objectAnimator.setInterpolator(new LinearInterpolator());
            objectAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    mShadowMatrix.reset();
                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                    super.onAnimationRepeat(animation);
                    // 每次动画重复时，将Matrix重置
                    mShadowMatrix.reset();
                }
            });
            objectAnimator.setRepeatCount(ValueAnimator.INFINITE);
            objectAnimator.start();
        }

    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (objectAnimator != null) {
            objectAnimator.removeAllListeners();
            objectAnimator.cancel();
            objectAnimator = null;
        }
        mShadowMatrix = null;
        mLinearGradient = null;
    }
}
