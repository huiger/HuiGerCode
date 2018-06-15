package com.huige.library.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
public class ItemLayout extends RelativeLayout {

    private ImageView ivLeft, ivRight;
    private TextView tvContent, tvRight;
    private View line;

    public ItemLayout(Context context) {
        this(context, null);
    }

    public ItemLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ItemLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        LayoutInflater.from(context).inflate(R.layout.layout_item, this);

        findView();

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ItemLayout);
        int leftImg = typedArray.getResourceId(R.styleable.ItemLayout_itemLeftImg, -1);
        if (leftImg != -1) {
            ivLeft.setVisibility(VISIBLE);
            ivLeft.setImageResource(leftImg);
            int leftImgSize = (int) typedArray.getDimension(R.styleable.ItemLayout_itemLeftImgSize, -1);
            if(leftImgSize != -1){
                ViewGroup.LayoutParams leftImgLp = ivLeft.getLayoutParams();
                leftImgLp.width = leftImgSize;
                leftImgLp.height = leftImgSize;
            }
            ivLeft.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener != null){
                        listener.onLeftClick();
                    }
                }
            });
        }

        // 中间文字
        String contentStr = typedArray.getString(R.styleable.ItemLayout_itemText);
        if(!TextUtils.isEmpty(contentStr)) {
            int paddingLeft = 0;

            tvContent.setText(contentStr);
            // 字体大小
            int contentTextSize = typedArray.getDimensionPixelSize(R.styleable.ItemLayout_itemTextSize, -1);
            if(contentTextSize != -1) {
                tvContent.setTextSize(TypedValue.COMPLEX_UNIT_PX, contentTextSize);
            }
            tvContent.setTextColor(typedArray.getColor(R.styleable.ItemLayout_itemTextColor, Color.BLACK));
            int textGravity = typedArray.getInt(R.styleable.ItemLayout_itemTextGravity, -1);
            // 字体居中样式
            if (textGravity == -1) {
                tvContent.setGravity(Gravity.LEFT);
                if(ivLeft.getVisibility() == VISIBLE) { // 在居左是才生效
                    paddingLeft = DeviceUtils.dp2px(context, 50);
                }
            } else if (textGravity == 0) {
                tvContent.setGravity(Gravity.CENTER);
            } else {
                tvContent.setGravity(Gravity.RIGHT);
            }

            paddingLeft += (int) typedArray.getDimension(R.styleable.ItemLayout_itemTextPaddingLeft, 0);
            if (paddingLeft > 0) {
                tvContent.setPadding(paddingLeft, 0, 0, 0);
            }
        }

        // 右边图片
        if (typedArray.getInt(R.styleable.ItemLayout_itemRightImgVisible, 1) == 1) {
            int rightImg = typedArray.getResourceId(R.styleable.ItemLayout_itemRightImg, R.mipmap.right_arrow);
            ivRight.setVisibility(VISIBLE);
            ivRight.setImageResource(rightImg);
            int rightImgSize = (int) typedArray.getDimension(R.styleable.ItemLayout_itemRightImgSize, -1);
            if(rightImgSize != -1){
                ViewGroup.LayoutParams rightImgLp = ivRight.getLayoutParams();
                rightImgLp.width = rightImgSize;
                rightImgLp.height = rightImgSize;
            }
        } else {
            ivRight.setVisibility(GONE);
        }

        // 右边文字
        String rightStr = typedArray.getString(R.styleable.ItemLayout_itemRightText);
        if (!TextUtils.isEmpty(rightStr)) {
            tvRight.setVisibility(VISIBLE);
            tvRight.setText(rightStr);
            // 字体大小
            int rightTextSize = typedArray.getDimensionPixelSize(R.styleable.ItemLayout_itemRightTextSize, -1);
            if(rightTextSize != -1) {
                tvRight.setTextSize(TypedValue.COMPLEX_UNIT_PX, rightTextSize);
            }
            tvRight.setTextColor(typedArray.getColor(R.styleable.ItemLayout_itemRightTextColor, Color.BLACK));
        }

        findViewById(R.id.right_layout).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener !=null){
                    listener.onRightClick();
                }
            }
        });

        line.setVisibility(typedArray.getInt(R.styleable.ItemLayout_itemLineVisible, 1) == 1 ? VISIBLE : GONE);
        typedArray.recycle();
    }

    private void findView() {
        ivLeft = (ImageView) findViewById(R.id.iv_left);
        ivRight = (ImageView) findViewById(R.id.iv_right);
        tvContent = (TextView) findViewById(R.id.tv_content);
        tvRight = (TextView) findViewById(R.id.tv_right);
        line = findViewById(R.id.line);
    }

    private OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public static abstract class OnItemClickListener {
        public void onLeftClick() {
        }

        public void onRightClick() {
        }
    }
}
