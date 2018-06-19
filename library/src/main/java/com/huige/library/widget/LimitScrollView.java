package com.huige.library.widget;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.huige.library.R;
import com.huige.library.interfaces.OnItemClickListener;

/**
 * Author : huiGer
 * Time   : 2018/6/19 0019 下午 03:48.
 * Desc   : 滚动广告
 */
public class LimitScrollView extends LinearLayout implements View.OnClickListener {

    /**
     * 显示数量
     */
    private int count;

    /**
     * 滚动速度
     */
    private long durationTime;

    /**
     * 停留时间
     */
    private long periodTime;

    private LinearLayout visibleLayout, goneLayout;
    private int scrollHeight;   //滚动高度（控件高度）
    private int dataIndex;

    private boolean isCancel;      //是否停止滚动动画
    private boolean boundData;     //是否已经第一次绑定过数据

    private final int MSG_SETDATA = 1;
    private final int MSG_SCROL = 2;

    private LimitScrollViewAdapter mAdapter;
    private OnItemClickListener onItemClickListener;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == MSG_SETDATA) {
                addData(true);
            } else if (msg.what == MSG_SCROL) {
                if (isCancel) return;
                startAnimation();
            }
        }
    };

    public LimitScrollView(Context context) {
        this(context, null);
    }

    public LimitScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LimitScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        LayoutInflater.from(context).inflate(R.layout.layout_limit_scroller, this);
        visibleLayout = (LinearLayout) findViewById(R.id.ll_content1);
        goneLayout = (LinearLayout) findViewById(R.id.ll_content2);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.LimitScrollView);
        count = typedArray.getInt(R.styleable.LimitScrollView_limitCount, 1);
        durationTime = typedArray.getInt(R.styleable.LimitScrollView_limitDurationTime, 500);
        periodTime = typedArray.getInt(R.styleable.LimitScrollView_limitPeriodTime, 5000);
        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 设置高度为整体高度的一般，以达到遮盖预备容器的效果
        setMeasuredDimension(getMeasuredWidth(), getMeasuredHeight() / 2);
        // 此处记下控件的高度，此高度就是动画执行时向上滚动的高度
        scrollHeight = getMeasuredHeight();
    }

    /**
     * 添加view
     *
     * @param isFirst 是否为第一次
     */
    private void addData(boolean isFirst) {
        if (mAdapter == null || mAdapter.getCount() == 0) {
            return;
        }

        if (isFirst) { // 第一次绑定数据
            boundData = true;
            visibleLayout.removeAllViews();

            for (int i = 0; i < count; i++) {
                if (dataIndex >= mAdapter.getCount()) {
                    dataIndex = 0;
                }
                View view = mAdapter.getView(dataIndex);
                view.setClickable(true);
                view.setOnClickListener(this);
                visibleLayout.addView(view);
                dataIndex++;
            }
        }

        goneLayout.removeAllViews();
        for (int i = 0; i < count; i++) {
            if (dataIndex >= mAdapter.getCount())
                dataIndex = 0;
            View view = mAdapter.getView(dataIndex);
            //设置点击监听
            view.setClickable(true);
            view.setOnClickListener(this);
            goneLayout.addView(view);
            dataIndex++;
        }
    }

    private void startAnimation() {
        if (isCancel) return;

        ObjectAnimator obj1 = ObjectAnimator.ofFloat(visibleLayout, "y", visibleLayout.getY(), visibleLayout.getY() - scrollHeight);
        ObjectAnimator obj2 = ObjectAnimator.ofFloat(goneLayout, "y", goneLayout.getY(), goneLayout.getY() - scrollHeight);
        AnimatorSet animSet = new AnimatorSet();
        animSet.setDuration(durationTime);
        animSet.playTogether(obj1, obj2);
        animSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                visibleLayout.setY(scrollHeight);
                goneLayout.setY(0);
                LinearLayout temp = visibleLayout;
                visibleLayout = goneLayout;
                goneLayout = temp;
                addData(false);
                handler.removeMessages(MSG_SCROL);
                if (isCancel) return;
                handler.sendEmptyMessageDelayed(MSG_SCROL, periodTime);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        animSet.start();
    }


    public void setAdapter(LimitScrollViewAdapter adapter) {
        this.mAdapter = adapter;
        handler.sendEmptyMessage(MSG_SETDATA);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public void onClick(View view) {
        if (onItemClickListener != null) {
            onItemClickListener.onItemClick(dataIndex, view);
        }
    }

    /**
     * 开始滚动
     */
    public void startScroll(){
        if(mAdapter == null || mAdapter.getCount() == 0) {
            return;
        }
        if(!boundData) {
            handler.sendEmptyMessage(MSG_SETDATA);
        }
        isCancel = false;
        handler.removeMessages(MSG_SCROL);   //先清空所有滚动消息，避免滚动错乱
        handler.sendEmptyMessageDelayed(MSG_SCROL, periodTime);
    }

    /**
     * 停止滚动
     *
     * 当在Activity不可见时调用
     */
    public void stopScroll(){
        isCancel = true;
    }

    public interface LimitScrollViewAdapter {
        public int getCount();

        public View getView(int position);
    }



}
