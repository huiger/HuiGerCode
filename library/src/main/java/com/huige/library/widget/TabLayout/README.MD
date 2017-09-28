## 使用指南
1. xml中引用TabScrollView
```xml
    <com.huige.library.widget.TabLayout.TabScrollView
            android:id="@+id/tabScrollView"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            app:tabTitlePadding="20dp"
            app:tabTitleSize="15sp"/>

    <android.support.v4.view.ViewPager
        android:id="@+id/viewPager"
        android:layout_width="match_parent"
        android:layout_height="match_parent"/>
```
2.  代码中使用
```java
    titles = new ArrayList<>();
    titles.add("好友");
    titles.add("动态");
    titles.add("最新");

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
    tabScrollView.initData(titles, viewPager, 1);
```

## 注意事项
1. initData()需要在viewPage设置适配器后调用

