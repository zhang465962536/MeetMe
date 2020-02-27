package com.example.meetme;

import android.Manifest;
import android.content.res.AssetFileDescriptor;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.example.framework.base.BaseUIActivity;
import com.example.framework.bmob.BmobManager;
import com.example.framework.bmob.IMUser;
import com.example.framework.utils.LogUtils;
import com.example.framework.manager.MediaPlayerManager;
import com.example.framework.utils.ToastUtil;

import cn.bmob.v3.BmobUser;

public class MainActivity extends BaseUIActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }


}
