package com.huige.library.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huige.library.R;
import com.huige.library.utils.DeviceUtils;

/**
 * Author : huiGer
 * Time : 2018/6/12 0012 下午 03:53.
 * Desc : 个人中心的item
 */
public class MineItemLayout extends RelativeLayout {

    private ImageView ivLeft, ivRight;
    private TextView tvContent;
    private View line;

    public MineItemLayout(Context context) {
        this(context, null);
    }

    public MineItemLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MineItemLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        LayoutInflater.from(context).inflate(R.layout.layout_item_mine, this);

        findView();

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MineItemLayout);
        int leftImg = typedArray.getResourceId(R.styleable.MineItemLayout_itemLeftImg, R.mipmap.setting);
        ivLeft.setImageResource(leftImg);
        int rightImg = typedArray.getResourceId(R.styleable.MineItemLayout_itemRightImg, R.mipmap.right_arrow);
        ivRight.setImageResource(rightImg);
        String contentStr = typedArray.getString(R.styleable.MineItemLayout_itemText);
        tvContent.setText(TextUtils.isEmpty(contentStr) ? "设置" : contentStr);
        tvContent.setTextColor(typedArray.getColor(R.styleable.MineItemLayout_itemTextColor, Color.BLACK));
        float paddingLeft = typedArray.getDimension(R.styleable.MineItemLayout_itemTextPaddingLeft, 0);
        if (paddingLeft > 0) { 
            tvContent.setPadding(DeviceUtils.dp2px(context, paddingLeft), 0, 0, 0);
        }
        line.setVisibility(typedArray.getInt(R.styleable.MineItemLayout_itemLineVisible, 1) == 1 ? VISIBLE : GONE);
        typedArray.recycle();
    }

    private void findView() {
        ivLeft = (ImageView) findViewById(R.id.iv_left);
        ivRight = (ImageView) findViewById(R.id.iv_right);
        tvContent = (TextView) findViewById(R.id.tv_content);
        line = findViewById(R.id.line);
    }
}
