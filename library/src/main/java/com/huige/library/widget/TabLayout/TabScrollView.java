package com.huige.library.widget.TabLayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huige.library.R;
import com.huige.library.utils.DeviceUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by <lzh> on 2017/8/30.
 */

public class TabScrollView extends HorizontalScrollView {

    public static final int VISIBLE = 1;
    public static final int GONE = -1;

    private List<String> titles;
    private ViewPager viewPager;
    private List<TextView> textViews;
    private int titleSize;
    private int titleSelColor;
    private int titleUnSelColor;
    private int titlesLength;
    private int tabTitlePadding;
    private boolean tabTitleLineVisible;
    private int tabTitleLineBgColor;
    private TabLineView tabLineView;
    private int screenWidth;
    private int margins;


    private OnTabTitleClickListener onTabTitleClickListener;

    public TabScrollView(Context context) {
        this(context, null);
    }

    public TabScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TabScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TabScrollView, defStyleAttr
                , 0);
        titleSize = typedArray.getDimensionPixelSize(R.styleable.TabScrollView_tabTitleSize,
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, 8, getResources().getDisplayMetrics()));
        titleSelColor = typedArray.getColor(R.styleable.TabScrollView_tabTitleSelColor, Color.BLACK);
        titleUnSelColor = typedArray.getColor(R.styleable.TabScrollView_tabTitleUnSelColor, Color.GRAY);
        tabTitlePadding = (int) typedArray.getDimension(R.styleable.TabScrollView_tabTitlePadding, 10);
        tabTitleLineVisible = typedArray.getInt(R.styleable.TabScrollView_tabTitleLineVisible, VISIBLE) == 1;
        tabTitleLineBgColor = typedArray.getColor(R.styleable.TabScrollView_tabTitleLineBgColor, -1);
        typedArray.recycle();


        titles = new ArrayList<>();
        textViews = new ArrayList<>();

        screenWidth = DeviceUtils.getWindowWidth(getContext());

    }


    public void initData(List<String> titles, ViewPager viewPager, int defaultIndex) {
        this.titles = titles;
        this.viewPager = viewPager;
        margins = getTabContentMargins(titles);

        initView();

        setDefaultIndex(defaultIndex);
        viewPager.setCurrentItem(defaultIndex);

        if (tabTitleLineVisible)
            viewPager.addOnPageChangeListener(new TabOnPageChangeListener(getContext(), this, tabLineView,
                    viewPager, textViews, titlesLength, defaultIndex, margins));


    }

    private void initView() {

        // parent
        LinearLayout contentLayout = new LinearLayout(getContext());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);

        contentLayout.setOrientation(LinearLayout.VERTICAL);
        layoutParams.setMargins((screenWidth - titlesLength) / 2, 0, 0, 0);
        contentLayout.setLayoutParams(layoutParams);
        addView(contentLayout);

        LinearLayout titleLayout = new LinearLayout(getContext());
        titleLayout.setOrientation(LinearLayout.HORIZONTAL);
        titleLayout.setLayoutParams(layoutParams);

        // textView
        LinearLayout.LayoutParams textViewParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textViewParams.setMargins(margins, 0, margins, 0);
        for (int i = 0; i < titles.size(); i++) {
            TextView tv = new TextView(getContext());
            tv.setText(titles.get(i));
            tv.setTextSize(TypedValue.COMPLEX_UNIT_PX,titleSize);
            tv.setTextColor(titleUnSelColor);
            tv.setLayoutParams(textViewParams);
            tv.setOnClickListener(onClickListener);
            tv.setTag(i);
            textViews.add(tv);
            titleLayout.addView(tv);
        }

        contentLayout.addView(titleLayout);

        // lineView
        if (tabTitleLineVisible) {
            tabLineView = new TabLineView(getContext());
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            tabLineView.setLayoutParams(lp);
            tabLineView.setLineColor(tabTitleLineBgColor);
            contentLayout.addView(tabLineView);
        }


    }

    private OnClickListener onClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            int tag = (int) v.getTag();
            setDefaultIndex(tag);
            viewPager.setCurrentItem(tag);

            if (onTabTitleClickListener != null)
                onTabTitleClickListener.onTitleClickListener(v, tag);

        }
    };


    public void setOnTabTitleClickListener(OnTabTitleClickListener listener) {
        this.onTabTitleClickListener = listener;
    }


    /**
     * set default TextView Size
     *
     * @param defaultIndex
     */
    public void setDefaultIndex(int defaultIndex) {
        for (int i = 0; i < textViews.size(); i++) {
            TextView tv = textViews.get(i);
            if (i == defaultIndex) {
                tv.setTextColor(titleSelColor);
                tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, (float) (titleSize * 1.2));
            } else {
                tv.setTextColor(titleUnSelColor);
                tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, titleSize);
            }
        }
    }

    private int getTabContentMargins(List<String> titles) {

        float countLength = 0;
        TextView textView = new TextView(getContext());
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, titleSize);
        TextPaint textPaint = textView.getPaint();

        for (int i = 0; i < titles.size(); i++) {
            countLength += textPaint.measureText(titles.get(i)) + tabTitlePadding * 2;
        }

        if (countLength > screenWidth) { //
            titlesLength = screenWidth;
            return tabTitlePadding;
        } else {
            titlesLength = (int) countLength;
            return tabTitlePadding;
        }
    }


}
