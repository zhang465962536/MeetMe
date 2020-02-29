package com.example.meetme.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.framework.cloud.CloudManager;
import com.example.framework.entity.Constants;
import com.example.framework.utils.LogUtils;
import com.example.framework.utils.SpUtils;

import io.rong.imlib.OnReceiveMessageListener;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Message;

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
               return false;
           }
       });
    }

}
