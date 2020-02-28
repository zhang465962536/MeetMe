package com.example.framework.bmob;

import android.content.Context;

import java.io.File;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

//管理Bomb 管理类
public class BmobManager {
    private static final String BMOB_SDK_ID = "5ce2e4e38f8b69676a6337097e7e8f43";
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

    //判断是否登录过
    public boolean isLogin(){
        return BmobUser.isLogin();
    }

    //第一次上传图片
    public void uploadFirstPhoto(final String nickName, File file, final OnUploadPhotoListener listener){
        /**
         * 1.上传文件拿到地址
         * 2.更新用户信息
         */
        final IMUser imUser =getUser();
        final BmobFile bmobFile = new BmobFile(file);
        bmobFile.uploadblock(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                if(e == null){
                    //上传成功
                    imUser.setNickName(nickName);
                    imUser.setPhoto(bmobFile.getFileUrl());

                    imUser.setTokenNickName(nickName);
                    imUser.setTokenPhoto(bmobFile.getFileUrl());

                    //更新用户信息
                    imUser.update(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if(e == null){
                                listener.OnUpdateDone();
                            }else {
                                listener.OnUpdateFail(e);
                            }
                        }
                    });
                }else {
                    listener.OnUpdateFail(e);
                }
            }
        });
    }

    public interface OnUploadPhotoListener {

        void OnUpdateDone();

        void OnUpdateFail(BmobException e);
    }
}
