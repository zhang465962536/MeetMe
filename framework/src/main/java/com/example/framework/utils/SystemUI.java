package com.example.framework.utils;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.view.View;

//沉浸式状态栏 实现
public class SystemUI {

    public static void fixSystemUI(Activity activity){
        //沉浸式状态栏 Android5.0以上手机才能支持 所以需要判断手机系统
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //获取最顶层的View
            //使用 SYSTEM_UI_FLAG_FULLSCREEN 会看不到Android自带的 系统栏
          /*  适合游戏界面
          getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE //显示状态栏
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION //隐藏状态栏
                    |View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    |View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    |View.SYSTEM_UI_FLAG_FULLSCREEN //全屏
                    |View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    ); */
            activity.getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }
}
