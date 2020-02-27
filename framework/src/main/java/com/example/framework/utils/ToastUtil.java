package com.example.framework.utils;

import android.content.Context;
import android.widget.Toast;

public class ToastUtil {

    private static Context mContext;

    public static void initToast(Context context){
        mContext = context;
    }

    public static void QuickToast(String text){
        Toast.makeText(mContext,text,Toast.LENGTH_SHORT).show();
    }
}
