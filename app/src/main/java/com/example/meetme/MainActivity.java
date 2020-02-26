package com.example.meetme;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.framework.utils.LogUtils;
import com.example.framework.utils.TimeUtils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LogUtils.i("heihei");
        LogUtils.e("xiaoheihei love you");

        LogUtils.i(TimeUtils.formatDuring(System.currentTimeMillis()));
    }
}
