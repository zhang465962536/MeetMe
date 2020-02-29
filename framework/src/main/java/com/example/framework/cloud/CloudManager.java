package com.example.framework.cloud;

import android.content.Context;

import com.example.framework.utils.LogUtils;

import io.rong.imlib.RongIMClient;

//融云管理
public class CloudManager {
    //Url
    public static final String TOKEN_URL = "http://api-cn.ronghub.com/user/getToken.json";
    //Key
    public static final String CLOUD_KEY = "pwe86ga5p9s76";
    public static final String CLOUD_SECRET = "pcxLQxRQcDvdK";

    //ObjectName
    public static final String MSG_TEXT_NAME = "RC:TxtMsg";
    public static final String MSG_IMAGE_NAME = "RC:ImgMsg";
    public static final String MSG_LOCATION_NAME = "RC:LBSMsg";

    //Msg Type

    //普通消息
    public static final String TYPE_TEXT = "TYPE_TEXT";
    //添加好友消息
    public static final String TYPE_ADD_FRIEND = "TYPE_ADD_FRIEND";
    //同意添加好友的消息
    public static final String TYPE_ARGEED_FRIEND = "TYPE_ARGEED_FRIEND";

    //来电铃声
    public static final String callAudioPath = "http://downsc.chinaz.net/Files/DownLoad/sound1/201501/5363.wav";
    //挂断铃声
    public static final String callAudioHangup = "http://downsc.chinaz.net/Files/DownLoad/sound1/201501/5351.wav";


    private static volatile CloudManager mInstance = null;

    public CloudManager() {
    }

    public static CloudManager getInstance() {
        if (mInstance == null) {
            synchronized (CloudManager.class) {
                if (mInstance == null) {
                    mInstance = new CloudManager();
                }
            }
        }
        return mInstance;
    }

    //初始化融云SDK
    public void initCloud(Context context) {
        RongIMClient.init(context);
    }

    //连接融云服务器
    public void connect(String token) {
        RongIMClient.connect(token, new RongIMClient.ConnectCallback() {
            /*Token 错误。可以从下面两点检查
            1.  Token 是否过期，如果过期您需要向 App Server 重新请求一个新的 Token
            2.  token 对应的 appKey 和工程里设置的 appKey 是否一致*/
            @Override
            public void onTokenIncorrect() {
                LogUtils.e("Token Error");
            }


            /**
             * 连接融云成功
             * @param userid 当前 token 对应的用户 id
             */
            @Override
            public void onSuccess(String userid) {
                LogUtils.e("连接成功   " + userid);
            }

            /** 连接融云失败
             * @param errorCode 错误码，可到官网 查看错误码对应的注释
             */
            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                LogUtils.e("连接失败   " + errorCode);
            }
        });
    }

    //断开与融云服务器的连接。当调用此接口断开连接后，仍然可以接收 Push 消息。
    //断开连接
    public void disconnect(){
        RongIMClient.getInstance().disconnect();

    }

    //断开与融云服务器的连接，并且不再接收 Push 消息
    //退出登录
    public void logout(){
        RongIMClient.getInstance().logout();
    }

    //接收消息的监听器
    public void setOnReceiveMessageListener(RongIMClient.OnReceiveMessageListener listener){
        RongIMClient.setOnReceiveMessageListener(listener);
    }
}
