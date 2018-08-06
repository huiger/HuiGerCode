package com.huige.huigercode;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;


public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    public void onClick(View view){
        Intent intent = null;
        switch (view.getId()) {
            case R.id.tv_to_likeView:
                intent = new Intent(this, LikeActivity.class);
                break;
            case R.id.tv_to_tabView:
                intent = new Intent(this, TabViewActivity.class);
                break;
            case R.id.tv_to_loadVIew:
                intent = new Intent(this, LoadingViewActivity.class);
                break;
            case R.id.tv_to_page:
                intent = new Intent(this, PageActivity.class);
                break;
            case R.id.tv_to_test:
                intent = new Intent(this, TestActivity.class);
                break;
            case R.id.tv_to_keyboard:
                intent = new Intent(this, TestKeyBoardActivity.class);
                break;
            default:

        }

        if(intent != null) {
            startActivity(intent);
        }
    }

}
