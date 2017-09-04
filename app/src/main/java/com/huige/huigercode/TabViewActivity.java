package com.huige.huigercode;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.huige.library.widget.TabLayout.OnTabTitleClickListener;
import com.huige.library.widget.TabLayout.TabScrollView;

import java.util.ArrayList;
import java.util.List;

public class TabViewActivity extends AppCompatActivity implements OnTabTitleClickListener {

    private TabScrollView tabScrollView;
    private ViewPager viewPager;
    private List<String> titles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_view);

        findView();

        initView();


    }

    private void initView() {
        titles = new ArrayList<>();
        titles.add("好友");
        titles.add("动态");
        titles.add("最新");


        tabScrollView.initData(titles, viewPager, 0);
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return new ContentFragment();
            }

            @Override
            public int getCount() {
                return titles.size();
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
//                super.destroyItem(container, position, object);
            }
        });

        tabScrollView.setOnTabTitleClickListener(this);
    }

    private void findView() {
        tabScrollView = (TabScrollView) findViewById(R.id.tabScrollView);
        viewPager = (ViewPager) findViewById(R.id.viewPager);


    }

    @Override
    public void onTitleClickListener(View view, int position) {
        Toast.makeText(TabViewActivity.this, titles.get(position), Toast.LENGTH_SHORT).show();
    }
}
