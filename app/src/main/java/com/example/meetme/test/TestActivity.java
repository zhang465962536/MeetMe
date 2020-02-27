package com.example.meetme.test;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.framework.utils.ToastUtil;
import com.example.framework.view.TouchPictureV;
import com.example.meetme.R;

//测试专用类
public class TestActivity extends AppCompatActivity {

    private TouchPictureV TouchPictureV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        initView();
    }

    private void initView() {
        TouchPictureV = (TouchPictureV) findViewById(R.id.TouchPictureV);
        TouchPictureV.setViewResultListener(new TouchPictureV.OnViewResultListener() {
            @Override
            public void onResult() {
                ToastUtil.QuickToast(getApplicationContext(),"验证通过");
            }
        });
    }
}
