package com.huige.huigercode;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import com.huige.library.widget.keyboard.KeyboardManager;
import com.huige.library.widget.keyboard.KeyboardType;

public class TestKeyBoardActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_key_board);
        EditText et = (EditText) findViewById(R.id.et);
        EditText et2 = (EditText) findViewById(R.id.et2);

        KeyboardManager.getInstance().init(TestKeyBoardActivity.this,KeyboardType.NUMBER, et);
        KeyboardManager.getNewInstance().init(this, KeyboardType.ABC , et2);

    }
}
