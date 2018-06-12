package com.huige.library.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huige.library.R;

/**
 * Author : huiGer
 * Time : 2018/6/12 0012 下午 05:20.
 * Desc :
 */
public class ButtonMenu extends LinearLayout {
    TextView mTextView;
    ImageView mImageView;
    boolean bSelect;// 标识本身是否选中状态
    private int iResid_nomalpic;
    private int iResid_presspic;
    private boolean isAnimation;


    public ButtonMenu(Context context) {
        this(context, null);
    }

    public ButtonMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ButtonMenu(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        View view = LayoutInflater.from(context).inflate(R.layout.layout_buttom_menu, this);
        findViews(view);

        // 得到XML中设置的自定义属性，并且设置到自定义控件中
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ButtonMenu);
        // 得到设置的字符串
        String strText = typedArray.getString(R.styleable.ButtonMenu_text);
        // 得到没有点击时的图片ID
        iResid_nomalpic = typedArray.getResourceId(R.styleable.ButtonMenu_normalpic, -1);
        // 得到点击后的图片ID
        iResid_presspic = typedArray.getResourceId(R.styleable.ButtonMenu_presspic, -1);
        isAnimation = typedArray.getBoolean(R.styleable.ButtonMenu_isAnimation, false);
        // 设置默认属性
        mTextView.setText(strText);
        mImageView.setImageResource(iResid_nomalpic);

        // 释放资源
        typedArray.recycle();
    }

    /**
     * 查找自定义菜单中的子控件
     *
     * @param view
     */
    protected void findViews(View view) {
        mTextView = (TextView) view.findViewById(R.id.tvMenu);
        mImageView = (ImageView) view.findViewById(R.id.ivMenu);
    }

    /**
     * 点击菜单时，切换图片，并且放大切换后的图片，并且隐藏文字
     */
    public void onSelect() {
        /*if (bSelect) {// 如果为选中状态，不做任何处理
            return;
        }*/
        //bSelect = true;

        // 切换图片，并且隐藏文字
        mImageView.setImageResource(iResid_presspic);
//        mTextView.setVisibility(GONE);

        if (isAnimation) {
            this.measure(0, 0);
            int width = this.getMeasuredWidth();
            int height = this.getMeasuredHeight();

//         对切换后的图片进行放大
            ScaleAnimation animation = new ScaleAnimation(1, 1.2f, 1, 1.2f, width / 2, height / 2);// 把图片放大1.5倍
            animation.setFillAfter(true); // 停留在动画的最后一帧
            animation.setDuration(500);
            mImageView.startAnimation(animation);// 播放动画
        }
    }

    public void UnSelect() {

        // 切换图片，并且隐藏文字
        mImageView.setImageResource(iResid_nomalpic);
        //bSelect = false;

        if (isAnimation) {
            // 对切换后的图片进行缩小
            ScaleAnimation animation = new ScaleAnimation(1.2f, 1, 1.2f, 1);// 把图片缩小1.2倍
            animation.setFillAfter(true); // 停留在动画的最后一帧
            animation.setDuration(200);
            mImageView.startAnimation(animation);// 播放动画
        }



//        // 将按钮下面的文字显示
//        mTextView.setVisibility(VISIBLE);
    }
}
