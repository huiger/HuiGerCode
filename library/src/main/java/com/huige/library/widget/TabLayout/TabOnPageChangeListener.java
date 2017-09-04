package com.huige.library.widget.TabLayout;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.widget.TextView;

import com.huige.library.utils.DeviceUtils;

import java.util.List;

/**
 * Created by <lzh> on 2017/8/31.
 */

public class TabOnPageChangeListener implements ViewPager.OnPageChangeListener {

    private TabScrollView tabScrollView;
    private List<TextView> titleViews;
    private ViewPager viewPager;
    private TabLineView tabLineView;


    private int screenWidth;
    private int lineWidth;
    private int everyLength;
    private int lastPosition;
    private int dis;
    private int[] location = new int[2];
    private int paddingLeft;


    public TabOnPageChangeListener(Context ctx, TabScrollView tabScrollView, TabLineView tabLineView, ViewPager viewPager,
                                   List<TextView> titleViews, int titlesLength, int defaultIndex, int margin) {
        this.tabScrollView = tabScrollView;
        this.tabLineView = tabLineView;
        this.viewPager = viewPager;
        this.titleViews = titleViews;

        screenWidth = DeviceUtils.getWindowWidth(ctx);

        TextView textView = titleViews.get(defaultIndex);
        lineWidth = (int) textView.getPaint().measureText(textView.getText().toString());

        everyLength = titlesLength / titleViews.size();
        dis = margin;
        paddingLeft = (screenWidth - titlesLength) / 2;

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//        Log.d("msg", "TabOnPageChangeListener -> onPageScrolled: " + lastPosition + "\t\t\t" + position);
        if (lastPosition > position) {
//            Log.d("msg", "TabOnPageChangeListener -> onPageScrolled: " + "1\t" + ((position + positionOffset) * everyLength + dis) + "\t" + ((lastPosition + 1) * everyLength - dis));
//            tabLineView.updateLineStatus((position + positionOffset) * everyLength + dis + fixLeftDis, (lastPosition + 1) * everyLength - dis);
            tabLineView.updateLineStatus((position + positionOffset) * everyLength + dis + paddingLeft, paddingLeft + (lastPosition + 1) * everyLength + dis);
//            Log.d("msg", "TabOnPageChangeListener -> onPageScrolled: " + "paddingLeft="+paddingLeft+"\tlastPosition="+lastPosition+"\teveryLength="+everyLength+"\tdis="+dis);

        } else {
            if (positionOffset > 0.5f) {
                positionOffset = 0.5f;
            }

//            Log.d("msg", "TabOnPageChangeListener -> onPageScrolled: " + "2\t" + (lastPosition * everyLength + dis) + "\t" + ((position + positionOffset * 2) * everyLength + dis + lineWidth));
//            tabLineView.updateLineStatus(lastPosition * everyLength + dis + fixLeftDis, (position + positionOffset * 2) * everyLength + dis + lineWidth);
//            tabLineView.updateLineStatus(lastPosition * everyLength + dis , (position + positionOffset * 2) * everyLength + dis + lineWidth);
            tabLineView.updateLineStatus(lastPosition * everyLength + dis + paddingLeft, paddingLeft + (position + positionOffset * 2) * everyLength + dis + lineWidth);
        }
    }

    @Override
    public void onPageSelected(int position) {
        tabScrollView.setDefaultIndex(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        boolean scrollRight;//页面向右
        if (state == ViewPager.SCROLL_STATE_SETTLING) {
            scrollRight = lastPosition < viewPager.getCurrentItem();
            lastPosition = viewPager.getCurrentItem();
            if (lastPosition + 1 < titleViews.size() && lastPosition - 1 >= 0) {
                titleViews.get(scrollRight ? lastPosition + 1 : lastPosition - 1).getLocationOnScreen(location);
                if (location[0] > screenWidth) {
                    tabScrollView.smoothScrollBy(screenWidth / 2, 0);
                } else if (location[0] < 0) {
                    tabScrollView.smoothScrollBy(-screenWidth / 2, 0);
                }
            }

        }

    }
}

