package com.example.framework.base;

import android.app.ActionBar;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.framework.utils.SystemUI;

public class BaseUIActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //沉浸式状态栏
        SystemUI.fixSystemUI(this);
    }
}
