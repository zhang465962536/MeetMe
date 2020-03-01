package com.example.meetme.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.framework.bmob.BmobManager;
import com.example.framework.cloud.CloudManager;
import com.example.framework.db.LitePalHelper;
import com.example.framework.db.NewFriend;
import com.example.framework.entity.Constants;
import com.example.framework.event.EventManager;
import com.example.framework.gson.TextBean;
import com.example.framework.utils.CommonUtils;
import com.example.framework.utils.LogUtils;
import com.example.framework.utils.SpUtils;
import com.google.gson.Gson;

import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.rong.imlib.OnReceiveMessageListener;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Message;
import io.rong.message.TextMessage;

//融云服务
public class CloudService extends Service {

   private Disposable disposable;
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
                if (objectName.equals(CloudManager.MSG_TEXT_NAME)) {
                    //获取消息主体
                    TextMessage textMessage = (TextMessage) message.getContent();
                    //获取发送过来的消息
                    String content = textMessage.getContent();
                    LogUtils.i(" content " + content);
                    //解析消息
                    TextBean textBean = new Gson().fromJson(content, TextBean.class);
                    //普通消息
                    if (textBean.getType().equals(CloudManager.TYPE_TEXT)) {

                    } else if (textBean.getType().equals(CloudManager.TYPE_ADD_FRIEND)) {
                        //添加好友消息 存入本地数据库
                        LogUtils.i("添加好友消息");
                        //查询数据库 如果有重复的则不添加
                        disposable = Observable.create(new ObservableOnSubscribe<List<NewFriend>>() {
                            @Override
                            public void subscribe(ObservableEmitter<List<NewFriend>> emitter) throws Exception {
                                emitter.onNext(LitePalHelper.getInstance().queryNewFriend());
                                emitter.onComplete();
                            }
                        }).subscribeOn(Schedulers.newThread())
                                //回调到主线程更新Ui
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Consumer<List<NewFriend>>() {
                                    @Override
                                    public void accept(List<NewFriend> newFriends) throws Exception {
                                        boolean isHave = false;
                                        if(CommonUtils.isEmpty(newFriends)){
                                            for(int j = 0; j < newFriends.size() ;j ++){
                                              NewFriend newFriend = newFriends.get(j);
                                              if(message.getSenderUserId().equals(newFriend.getUserId())){
                                                    isHave = true;
                                                    break;
                                              }
                                            }
                                            //防止重复添加
                                            if(!isHave){ //如果数据库中不存在该好友 就添加到好友申请列表中
                                                LitePalHelper.getInstance().saveNewFriend(textBean.getMsg(), message.getSenderUserId());
                                            }
                                        }else {
                                            //如果查询到 没有好友 也是需要去添加的
                                            LitePalHelper.getInstance().saveNewFriend(textBean.getMsg(), message.getSenderUserId());
                                        }
                                    }
                                });


                    } else if (textBean.getType().equals(CloudManager.TYPE_ARGEED_FRIEND)) {
                        //同意添加好友消息
                        //添加到好友列表
                        BmobManager.getInstance().addFriend(message.getSenderUserId(), new SaveListener<String>() {
                            @Override
                            public void done(String s, BmobException e) {
                                if(e == null){
                                    //刷新好友列表
                                    EventManager.post(EventManager.FLAG_UPDATE_FRIEND_LIST);
                                }
                            }
                        });
                    }
                }
                return false;
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(disposable.isDisposed()){
            disposable.dispose();
        }
    }
}
