package com.example.meetme.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.framework.cloud.CloudManager;
import com.example.framework.db.LitePalHelper;
import com.example.framework.entity.Constants;
import com.example.framework.gson.TextBean;
import com.example.framework.utils.LogUtils;
import com.example.framework.utils.SpUtils;
import com.google.gson.Gson;

import io.rong.imlib.OnReceiveMessageListener;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Message;
import io.rong.message.TextMessage;

//融云服务
public class CloudService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        linkCloudServer();
    }

    //连接云服务
    private void linkCloudServer() {
        //获取Token
        String token = SpUtils.getInstance().getString(Constants.SP_TOKEN, "");
        LogUtils.e(" TOKEN  " + token);
        //连接服务
        CloudManager.getInstance().connect(token);
        //接收消息
       CloudManager.getInstance().setOnReceiveMessageListener(new RongIMClient.OnReceiveMessageListener() {
           @Override
           public boolean onReceived(Message message, int i) {
               LogUtils.i(" message  " + message);
               String objectName = message.getObjectName();
               //文本消息
               if(objectName.equals(CloudManager.MSG_TEXT_NAME)){
                   //获取消息主体
                   TextMessage textMessage = (TextMessage) message.getContent();
                    //获取发送过来的消息
                   String content = textMessage.getContent();
                   LogUtils.i(" content " + content);
                   //解析消息
                   TextBean textBean = new Gson().fromJson(content, TextBean.class);
                   //普通消息
                   if(textBean.getType().equals(CloudManager.TYPE_TEXT)){

                   }else if(textBean.getType().equals(CloudManager.TYPE_ADD_FRIEND)){
                       //添加好友消息 存入本地数据库
                        LogUtils.i("添加好友消息");
                       LitePalHelper.getInstance().saveNewFriend(textBean.getMsg(),message.getSenderUserId());
                   }else if(textBean.getType().equals(CloudManager.TYPE_ARGEED_FRIEND)){
                       //同意添加好友消息

                   }
               }
               return false;
           }
       });
    }

}
