package com.huige.huigercode;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.huige.codelibrary.BaseTitleLayout;
import com.huige.codelibrary.TitleLayoutClickListener;

public class MainActivity extends AppCompatActivity {

    private BaseTitleLayout titleLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        titleLayout = (BaseTitleLayout) findViewById(R.id.titleLayout);

        titleLayout.setOnTitleClickListener(new TitleLayoutClickListener() {
            @Override
            public void onLeftClickListener() {
                super.onLeftClickListener();
                Log.d("msg", "MainActivity -> onLeftClickListener: " + "left　Ｃｌｉｃｋ");

            }

            @Override
            public void onRightTextClickListener() {
                super.onRightTextClickListener();
                Log.d("msg", "MainActivity -> onRightTextClickListener: " + "RightText　Ｃｌｉｃｋ");
            }

            @Override
            public void onRightImg1ClickListener() {
                super.onRightImg1ClickListener();
                Log.d("msg", "MainActivity -> onRightImg1ClickListener: " + "RightImg1　Ｃｌｉｃｋ");
            }

            @Override
            public void onRightImg2ClickListener() {
                super.onRightImg2ClickListener();
                Log.d("msg", "MainActivity -> onRightImg2ClickListener: " + "RightImg2　Ｃｌｉｃｋ");
            }
        });

    }
}
