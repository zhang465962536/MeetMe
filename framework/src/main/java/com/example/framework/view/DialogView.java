package com.example.framework.view;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.view.WindowManager;

//自定义提示框
import androidx.annotation.NonNull;

public class DialogView extends Dialog {

    //需要给透明的主题 所有使用两个参数的 构造方法
    public DialogView(@NonNull Context context,int layout,int style,int gravity) {
        super(context, style);
        setContentView(layout);
        Window window = getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.gravity = gravity;
        window.setAttributes(layoutParams);

    }




}
