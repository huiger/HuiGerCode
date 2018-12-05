package com.huige.library.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.huige.library.R;


/**
 * Author : huiGer
 * Time   : 2018/8/10 0010 下午 12:03.
 * Desc   : 标题栏
 */
public class MyToolBar extends Toolbar implements View.OnClickListener {

    private OnToolBarClick clickListener;
    private ImageView leftIcon, rightIcon;
    private TextView tvTitle;
    private TextView tvRight;

    public MyToolBar(Context context) {
        this(context, null);
    }

    public MyToolBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyToolBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        LayoutInflater.from(context).inflate(R.layout.toolbar_layout, this);

        leftIcon = findViewById(R.id.toolBar_icon);
        leftIcon.setOnClickListener(this);
        tvRight = findViewById(R.id.tv_right);
        tvTitle = findViewById(R.id.toolBar_title);
        findViewById(R.id.right_layout).setOnClickListener(this);
        rightIcon = findViewById(R.id.right_icon);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MyToolBar);
        setTitle(typedArray.getString(R.styleable.MyToolBar_title));
        tvTitle.setTextColor(typedArray.getColor(R.styleable.MyToolBar_titleTextColor, Color.parseColor("#333333")));
        setRightContent(typedArray.getString(R.styleable.MyToolBar_rightContent));
        float textSize = typedArray.getDimension(R.styleable.MyToolBar_rightTextSize,
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 14, context.getResources().getDisplayMetrics()));
        tvRight.getPaint().setTextSize(textSize);

        Drawable bgDrawable = typedArray.getDrawable(R.styleable.MyToolBar_android_background);
        if(bgDrawable != null) {
            findViewById(R.id.v_line).setBackground(bgDrawable);
            findViewById(R.id.layout_content).setBackground(bgDrawable);
        }

        findViewById(R.id.line).setVisibility((typedArray.getInt(R.styleable.MyToolBar_lineVisible, 1) == 1) ? VISIBLE : GONE);

        leftIcon.setVisibility((typedArray.getInt(R.styleable.MyToolBar_leftIconVisible, 1) == 1) ? VISIBLE : GONE);
        int rightIconResourceId = typedArray.getResourceId(R.styleable.MyToolBar_rightIcon, -1);
        if(rightIconResourceId == -1) {
            rightIcon.setVisibility(GONE);
        }else {
            rightIcon.setVisibility(VISIBLE);
            rightIcon.setImageResource(rightIconResourceId);
        }

        setStatusBarVisible(typedArray.getInt(R.styleable.MyToolBar_statusBarVisible, 1) == 1 ? VISIBLE : GONE);

        typedArray.recycle();
    }

    /**
     * 设置标题
     *
     * @param str   标题
     */
    @Override
    public void setTitle(CharSequence str) {
        if (tvTitle == null) {
            return;
        }
        tvTitle.setText(str);
        tvTitle.setVisibility(VISIBLE);
    }

    /**
     * 设置右边文字
     *
     * @param str 右边文字
     */
    public void setRightContent(CharSequence str) {
        if (tvRight == null || TextUtils.isEmpty(str)) {
            return;
        }
        tvRight.setText(str);
        tvRight.setVisibility(VISIBLE);
    }

    public void setOnToolBarClickListener(OnToolBarClick clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.toolBar_icon) {
            if (clickListener != null) {
                clickListener.onLeftClick();
            }
        } else if (i == R.id.right_layout) {
            if (clickListener != null) {
                clickListener.onRightClick();
            }
        }
    }

    /**
     * 显示状态
     * @param visible 显示状态
     */
    public void setStatusBarVisible(int visible){
        findViewById(R.id.v_line).setVisibility(visible);
    }

    public static class OnToolBarClick {
        public void onLeftClick(){

        }

        public void onRightClick(){}
    }

}
