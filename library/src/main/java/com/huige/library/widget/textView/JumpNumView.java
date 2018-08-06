package com.huige.library.widget.textView;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.huige.library.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Author : huiGer
 * Time   : 2018/7/5 0005 下午 03:01.
 * Desc   : 跳动的数字
 */
public class JumpNumView extends AppCompatTextView {

    private float num, curNum;
    private int duration;
    private int placeNum;
    private String lastStr; // 数字后面的字符串
    private ObjectAnimator objectAnimator;

    public JumpNumView(Context context) {
        this(context, null);
    }

    public JumpNumView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public JumpNumView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.JumpNumView);
        num = typedArray.getFloat(R.styleable.JumpNumView_jnv_num, 666f);
        duration = typedArray.getInt(R.styleable.JumpNumView_jnv_duration, 1000);
        placeNum = typedArray.getInt(R.styleable.JumpNumView_Jnv_placeNum, 2);
        typedArray.recycle();
    }


    private void initAnim() {
        objectAnimator = ObjectAnimator.ofFloat(this, "curNum", 0, num).setDuration(duration);
        objectAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                isAnimator = false;
            }
        });
        objectAnimator.start();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if(objectAnimator != null) {
            objectAnimator.cancel();
        }
    }

    public float getCurNum() {
        return curNum;
    }

    public void setCurNum(float curNum) {
        this.curNum = curNum;
        isAnimator = true;
        setText(String.format("%." + placeNum + "f", curNum) + lastStr);
    }

    private boolean isAnimator = false;

    @Override
    public void setText(CharSequence text, BufferType type) {
        super.setText(text, type);
        if (!isAnimator && !TextUtils.isEmpty(text) && !text.equals("0")) {
            Pattern pattern = Pattern.compile("\\d+"); // 在字符串中查找要跳动的数字, 只匹配前面的
            Matcher matcher = pattern.matcher(text);

            if (matcher.find()){
                String group = matcher.group(0);
                setNum(Float.parseFloat(group));
                lastStr = text.toString().substring(group.length());
            }
        }
    }

    /**
     * 设置跳动的数字
     *
     * @param num 跳动的数字
     */
    public void setNum(float num) {
        this.num = num;
        initAnim();
    }

    /**
     * 设置动画时长
     *
     * @param duration 时长(毫秒)
     */
    public void setDuration(int duration) {
        this.duration = duration;
    }

    /**
     * 设置保留的位数
     *
     * @param placeNum 位数
     */
    public void setPlaceNum(int placeNum) {
        this.placeNum = placeNum;
    }
}
