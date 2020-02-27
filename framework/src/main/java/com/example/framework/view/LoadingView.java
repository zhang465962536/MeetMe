package com.example.framework.view;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.framework.R;
import com.example.framework.manager.DialogManager;
import com.example.framework.utils.AnimUtils;

//自定义加载提示框
public class LoadingView {
    private DialogView mLodingView;
    private ImageView iv_loding;
    private TextView tv_loding_text;
    private ObjectAnimator mAnim;

    public LoadingView(Context context){
        mLodingView = DialogManager.getInstance().initView(context, R.layout.dialog_loding);
        iv_loding = mLodingView.findViewById(R.id.iv_loding);
        tv_loding_text = mLodingView.findViewById(R.id.tv_loding_text);
        mAnim = AnimUtils.rotation(iv_loding);
    }

    //设置加载的提示文本
    public void setLoadingText(String text){
        if(!TextUtils.isEmpty(text)){
            tv_loding_text.setText(text);
        }
    }

    public void show(){
        mAnim.start();
        DialogManager.getInstance().show(mLodingView);
    }

    public void show(String text){
        mAnim.start();
        setLoadingText(text);
        DialogManager.getInstance().show(mLodingView);
    }

    public void hide(){
        mAnim.pause();
        DialogManager.getInstance().hide(mLodingView);
    }

    //点击外部是否可以消失
    public void setCancelable(boolean flag) {
        mLodingView.setCancelable(flag);
    }
}
