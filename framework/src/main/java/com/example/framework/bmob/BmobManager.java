package com.example.framework.bmob;

import android.content.Context;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.QueryListener;

//管理Bomb 管理类
public class BmobManager {
    private static final String BMOB_SDK_ID ="f8efae5debf319071b44339cf51153fc";
    private volatile static BmobManager mInstance = null;

    public BmobManager() {
    }

    public static BmobManager getInstance(){
        if(mInstance == null){
            synchronized (BmobManager.class){
                if (mInstance == null){
                    mInstance = new BmobManager();
                }
            }
        }
        return mInstance;
    }

    //初始化 bmob
    public void initBmob(Context context){
        Bmob.initialize(context,BMOB_SDK_ID);
    }

    //发送短信验证码
    public void requestSMS(String phone, QueryListener<Integer> listener){
        BmobSMS.requestSMSCode(phone,"",listener);
    }

    //通过手机号码注册或者登陆
    public void signOrLoginByMobilePhone(String phone, String code, LogInListener<IMUser> listener) {
        BmobUser.signOrLoginByMobilePhone(phone, code, listener);
    }

//获取本地对象
    public IMUser getUser(){
        return BmobUser.getCurrentUser(IMUser.class);
    }
}
