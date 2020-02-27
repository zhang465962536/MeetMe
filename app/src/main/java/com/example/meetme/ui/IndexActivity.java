package com.example.meetme.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.example.framework.entity.Constants;
import com.example.framework.utils.SpUtils;
import com.example.meetme.MainActivity;
import com.example.meetme.R;

//启动页面
/*
* 1.把启动页设置全屏
* 2.延迟进入主页
* 3.根据具体逻辑进入主页还是引导页还是登录页
* 4.适配刘海屏
* */
public class IndexActivity extends AppCompatActivity {

    private static final int SKIP_MAIN = 1000;

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {
            switch (message.what){
                case SKIP_MAIN:
                    startMain();
                    break;
            }
            return false;
        }
    });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);

        mHandler.sendEmptyMessageDelayed(SKIP_MAIN,2 * 1000);

    }

    //进入主页
    private void startMain() {
       // 1. 判断APP 是否是第一次启动
        boolean isFirstApp = SpUtils.getInstance().getBoolean(Constants.SP_IS_FIRST_APP, true);
        Intent intent = new Intent();
        if(isFirstApp){
            //跳转到引导页
            intent.setClass(this,GuideActivity.class);
            //跳转之后 设置 不是第一次启动
            SpUtils.getInstance().putBoolean(Constants.SP_IS_FIRST_APP,false);
        }else {
            // 2 如果非第一次启动 判断 是否登录过
            String token = SpUtils.getInstance().getString(Constants.SP_TOKEN,"");
            if(TextUtils.isEmpty(token)){
                //跳转到登录页
                intent.setClass(this,LoginActivity.class);
            }else {
                //跳转到主页
                intent.setClass(this, MainActivity.class);
            }
        }
        startActivity(intent);
        finish();
    }
}
