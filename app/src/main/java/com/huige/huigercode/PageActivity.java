package com.huige.huigercode;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.huige.library.widget.ItemLayout;
import com.huige.library.widget.viewpage.HorizontalPageTransformer;

import java.util.ArrayList;
import java.util.List;

public class PageActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private List<Fragment> fragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page);

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        fragments = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            fragments.add(new ContentFragment());
        }
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }
        });
        viewPager.setOffscreenPageLimit(3);
        viewPager.setPageTransformer(true, new HorizontalPageTransformer(viewPager));

        ItemLayout itemLayout = (ItemLayout)findViewById(R.id.itemLayout);
//        itemLayout.setOnItemClickListener(new ItemLayout.OnItemClickListener() {
//            @Override
//            public void onRightClick() {
//                Log.d("msg", "PageActivity -> onRightClick: " + "");
//            }
//        });

        itemLayout.setOnItemClickListener(new ItemLayout.OnSingleItemClickListener() {
            @Override
            public void onItemClick(View v) {
                Log.d("msg", "PageActivity -> onItemClick: " + "");
            }
        });

//        itemLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.d("msg", "PageActivity -> onClick: " + "");
//            }
//        });
    }
}
