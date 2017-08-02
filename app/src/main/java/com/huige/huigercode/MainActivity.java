package com.huige.huigercode;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.huige.library.likeview.LikeView;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final LikeView likeView = (LikeView) findViewById(R.id.likeView);
        likeView.setSelectLike();
        likeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("msg", "MainActivity -> 点赞状态：" + likeView.getStatus());
            }
        });
    }
}
