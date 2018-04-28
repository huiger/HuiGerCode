package com.huige.library.widget.viewpage;

import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

import com.huige.library.utils.DeviceUtils;

/****************************************************************
 * *     *  * * * *     Created by huiGer
 * *     *  *           Time : 2018/4/28 9:13
 * * * * *  *   * *     Email: zhihuiemail@163.com
 * *     *  *     *     blog : huiGer.top
 * *     *  * * * *     Desc :
 ****************************************************************/
public class HorizontalPageTransformer implements ViewPager.PageTransformer {
    private static final float CENTER_PAGE_SCALE = 0.8f;
    private final int offscreenPageLimit;   // 预加载页面数
    private final float horizontalOffsetBase;   // 每个viewpager叠出的距离

    public HorizontalPageTransformer(ViewPager viewPager) {
        offscreenPageLimit = viewPager.getOffscreenPageLimit();
        Log.d("msg", "HorizontalPageTransformer -> HorizontalPageTransformer: offscreenPageLimit=" + offscreenPageLimit);
        int pageWidth = viewPager.getWidth();
        horizontalOffsetBase = (pageWidth - pageWidth * CENTER_PAGE_SCALE) / 2 / offscreenPageLimit + DeviceUtils.dp2px(viewPager.getContext(), 15);
        Log.d("msg", "HorizontalPageTransformer -> transformPage: pageWidth=" + pageWidth);
        Log.d("msg", "HorizontalPageTransformer -> transformPage: horizontalOffsetBase=" + horizontalOffsetBase);

    }

    @Override
    public void transformPage(View page, float position) {

        // 主要就是让预加载的那几个页面显示, 其他的都隐藏
        if (position >= offscreenPageLimit || position <= -1)
            page.setVisibility(View.GONE);
        else
            page.setVisibility(View.VISIBLE);

        if (position >= 0) {    // 往右滑
            float translationX = (horizontalOffsetBase - page.getWidth()) * position;
            page.setTranslationX(translationX);
        }

        if (position > -1 && position < 0) {
            float rotation = position * 30;
            page.setRotation(rotation);
            page.setAlpha((position * position * position + 1));
        } else if (position > offscreenPageLimit - 1) {
            page.setAlpha((float) (1 - position + Math.floor(position)));
        } else {
            page.setRotation(0);
            page.setAlpha(1.0f);
        }

        if(position == 0) {
            page.setScaleX(CENTER_PAGE_SCALE);
            page.setScaleY(CENTER_PAGE_SCALE);
        }else{
            float scaleFactor = Math.min(CENTER_PAGE_SCALE - position * 0.1f, CENTER_PAGE_SCALE);
            page.setScaleX(scaleFactor);
            page.setScaleY(scaleFactor);
        }
        ViewCompat.setElevation(page, (offscreenPageLimit - position) * 5);
    }
}
