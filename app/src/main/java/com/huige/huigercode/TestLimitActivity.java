package com.huige.huigercode;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;

import com.huige.library.utils.log.LogLevel;
import com.huige.library.utils.log.LogUtils;
import com.huige.library.widget.LimitScrollView;

/**
 * Author : huiGer
 * Time   : 2018/8/6 0006 下午 05:36.
 * Desc   : 测试滚动广告
 */
public class TestLimitActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        LimitScrollView limitScrollView = (LimitScrollView) findViewById(R.id.limitView);
        limitScrollView.setAdapter(new LimitScrollView.LimitScrollViewAdapter() {
            @Override
            public int getCount() {
                return 3;
            }

            @Override
            public View getView(int position) {

                View view = LayoutInflater.from(TestLimitActivity.this).inflate(R.layout.limit_scrollview, null);

                return view;
            }
        });
        limitScrollView.startScroll();

        LogUtils.d("你好啊");
        LogUtils.init().setLogLevel(LogLevel.NONE);
        LogUtils.d("我不好");
    }
}
