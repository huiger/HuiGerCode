package com.huige.huigercode;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.huige.library.widget.ItemLayout;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        ((ItemLayout)findViewById(R.id.title)).setOnItemClickListener(new ItemLayout.OnItemClickListener() {
            @Override
            public void onLeftClick() {
                super.onLeftClick();
                Toast.makeText(TestActivity.this, "left", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRightClick() {
                super.onRightClick();
                Toast.makeText(TestActivity.this, "right", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
