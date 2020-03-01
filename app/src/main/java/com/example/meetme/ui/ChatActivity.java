package com.example.meetme.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.framework.adapter.CommomViewHolder;
import com.example.framework.adapter.CommonAdapter;
import com.example.framework.base.BaseBackActivity;
import com.example.framework.base.BaseUIActivity;
import com.example.framework.bmob.BmobManager;
import com.example.framework.cloud.CloudManager;
import com.example.framework.entity.Constants;
import com.example.framework.event.EventManager;
import com.example.framework.event.MessageEvent;
import com.example.framework.gson.TextBean;
import com.example.framework.helper.FileHelper;
import com.example.framework.utils.CommonUtils;
import com.example.framework.utils.LogUtils;
import com.example.meetme.R;
import com.example.meetme.model.ChatModel;
import com.google.gson.Gson;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Message;
import io.rong.message.ImageMessage;
import io.rong.message.TextMessage;


/**
 * 发送文本逻辑
 * 1.跳转到ChatActivity
 * 2.实现我们的聊天列表 适配器
 * 3.加载我们的历史记录
 * 4.实时更新我们的聊天信息
 * 5.发送消息
 * <p>
 * 发送图片逻辑
 * 1.读取(相机和相册)
 * 2.发送图片消息
 * 3.完成我们适配器的UI
 * 4.完成Service的图片接收逻辑
 * 5.通知UI刷新
 * <p>
 * 发送图片逻辑
 * 1.读取(相机和相册)
 * 2.发送图片消息
 * 3.完成我们适配器的UI
 * 4.完成Service的图片接收逻辑
 * 5.通知UI刷新
 * <p>
 * 发送图片逻辑
 * 1.读取(相机和相册)
 * 2.发送图片消息
 * 3.完成我们适配器的UI
 * 4.完成Service的图片接收逻辑
 * 5.通知UI刷新
 * <p>
 * 发送图片逻辑
 * 1.读取(相机和相册)
 * 2.发送图片消息
 * 3.完成我们适配器的UI
 * 4.完成Service的图片接收逻辑
 * 5.通知UI刷新
 * <p>
 * 发送图片逻辑
 * 1.读取(相机和相册)
 * 2.发送图片消息
 * 3.完成我们适配器的UI
 * 4.完成Service的图片接收逻辑
 * 5.通知UI刷新
 * <p>
 * 发送图片逻辑
 * 1.读取(相机和相册)
 * 2.发送图片消息
 * 3.完成我们适配器的UI
 * 4.完成Service的图片接收逻辑
 * 5.通知UI刷新
 */
/**
 * 发送图片逻辑
 * 1.读取(相机和相册)
 * 2.发送图片消息
 * 3.完成我们适配器的UI
 * 4.完成Service的图片接收逻辑
 * 5.通知UI刷新
 */

/**
 * 发送地址的逻辑
 * 1.获取地址
 * 2.发送位置消息
 * 不能忘记：
 * 1.历史消息
 * 2.适配器
 * 3.发送消息
 */
//好友聊天界面
public class ChatActivity extends BaseBackActivity implements View.OnClickListener {

    //左边 消息
    public static final int TYPE_LEFT_TEXT = 0;
    public static final int TYPE_LEFT_IMAGE = 1;
    public static final int TYPE_LEFT_LOCATION = 2;

    //右边 消息
    public static final int TYPE_RIGHT_TEXT = 3;
    public static final int TYPE_RIGHT_IMAGE = 4;
    public static final int TYPE_RIGHT_LOCATION = 5;

    private static final int LOCATION_REQUEST_CODE = 1888;

    private static final int CHAT_INFO_REQUEST_CODE = 1889;

    //聊天列表
    private RecyclerView mChatView;
    //输入框
    private EditText et_input_msg;
    //发送按钮
    private Button btn_send_msg;
    //语音输入
    private LinearLayout ll_voice;
    //相机
    private LinearLayout ll_camera;
    //图片
    private LinearLayout ll_pic;
    //位置
    private LinearLayout ll_location;

    //背景主题
    private LinearLayout ll_chat_bg;

    //对方用户信息
    private String yourUserId;
    private String yourUserName;
    private String yourUserPhoto;

    //自己的信息
    private String meUserPhoto;

    //列表
    private CommonAdapter<ChatModel> mChatAdapter;
    private List<ChatModel> mList = new ArrayList<>();

    //图片文件
    private File uploadFile = null;

    public static void startActivity(Context mContext, String userId, String userName, String userPhoto) {
       /* if (!CloudManager.getInstance().isConnect()) {
            Toast.makeText(mContext, mContext.getString(R.string.text_server_status), Toast.LENGTH_SHORT).show();
            return;
        }*/
        Intent intent = new Intent(mContext, ChatActivity.class);
        intent.putExtra(Constants.INTENT_USER_ID, userId);
        intent.putExtra(Constants.INTENT_USER_NAME, userName);
        intent.putExtra(Constants.INTENT_USER_PHOTO, userPhoto);
        mContext.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        initView();
    }

    private void initView() {
        mChatView = (RecyclerView) findViewById(R.id.mChatView);
        et_input_msg = (EditText) findViewById(R.id.et_input_msg);
        btn_send_msg = (Button) findViewById(R.id.btn_send_msg);

        ll_voice = (LinearLayout) findViewById(R.id.ll_voice);
        ll_camera = (LinearLayout) findViewById(R.id.ll_camera);
        ll_pic = (LinearLayout) findViewById(R.id.ll_pic);
        ll_location = (LinearLayout) findViewById(R.id.ll_location);
        ll_chat_bg = (LinearLayout) findViewById(R.id.ll_chat_bg);

        btn_send_msg.setOnClickListener(this);
        ll_voice.setOnClickListener(this);
        ll_camera.setOnClickListener(this);
        ll_pic.setOnClickListener(this);
        ll_location.setOnClickListener(this);

        mChatView.setLayoutManager(new LinearLayoutManager(this));
        mChatAdapter = new CommonAdapter<>(mList, new CommonAdapter.OnMoreBindDataListener<ChatModel>() {
            @Override
            public int getItemType(int position) {
                return mList.get(position).getType();
            }

            @Override
            public void onBindViewHolder(ChatModel model, CommomViewHolder viewHolder, int type, int position) {
                switch (model.getType()) {
                    case TYPE_LEFT_TEXT:
                        viewHolder.setText(R.id.tv_left_text, model.getText());
                        viewHolder.setImageUrl(ChatActivity.this, R.id.iv_left_photo, yourUserPhoto);
                        break;
                    case TYPE_LEFT_IMAGE:
                        viewHolder.setImageUrl(ChatActivity.this, R.id.iv_left_img, model.getImgUrl());
                        viewHolder.setImageUrl(ChatActivity.this, R.id.iv_left_photo, yourUserPhoto);

                        viewHolder.getView(R.id.iv_left_img).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                               ImagePreviewActivity.startActivity(
                                        ChatActivity.this, true, model.getImgUrl());
                            }
                        });
                        break;
                    case TYPE_LEFT_LOCATION:
                        break;

                    case TYPE_RIGHT_TEXT:
                        viewHolder.setText(R.id.tv_right_text, model.getText());
                        viewHolder.setImageUrl(ChatActivity.this, R.id.iv_right_photo, meUserPhoto);
                        break;
                    case TYPE_RIGHT_IMAGE:
                        if (TextUtils.isEmpty(model.getImgUrl())) {
                            if (model.getLocalFile() != null) {
                                //加载本地文件
                                viewHolder.setImageFile(ChatActivity.this, R.id.iv_right_img, model.getLocalFile());
                                viewHolder.getView(R.id.iv_right_img).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        ImagePreviewActivity.startActivity(ChatActivity.this, false, model.getLocalFile().getPath());
                                    }
                                });
                            }
                        } else {
                            viewHolder.setImageUrl(ChatActivity.this, R.id.iv_right_img, model.getImgUrl());
                            viewHolder.getView(R.id.iv_right_img).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    ImagePreviewActivity.startActivity(ChatActivity.this, true, model.getImgUrl());
                                }
                            });
                        }
                        viewHolder.setImageUrl(ChatActivity.this, R.id.iv_right_photo, meUserPhoto);
                        break;
                    case TYPE_RIGHT_LOCATION:
                        break;
                    default:
                        break;
                }
            }

            @Override
            public int getLayoutId(int type) {
                switch (type) {
                    case TYPE_LEFT_TEXT:
                        return R.layout.layout_chat_left_text;
                    case TYPE_LEFT_IMAGE:
                        return R.layout.layout_chat_left_img;
                    case TYPE_LEFT_LOCATION:
                        return R.layout.layout_chat_left_location;

                    case TYPE_RIGHT_TEXT:
                        return R.layout.layout_chat_right_text;
                    case TYPE_RIGHT_IMAGE:
                        return R.layout.layout_chat_right_img;
                    case TYPE_RIGHT_LOCATION:
                        return R.layout.layout_chat_right_location;
                    default:
                        break;

                }
                return 0;
            }
        });

        mChatView.setAdapter(mChatAdapter);

        //加载好友对方相关信息
        loadMeInfo();
        //测试使用
        //addText(0,"你好呀！张小黑");
        //addText(1,"你好，嘿嘿嘿！");
        queryMessage();
    }

    //查询聊天记录
    private void queryMessage() {
        //先从本地查询
        CloudManager.getInstance().getHistoryMessage(yourUserId, new RongIMClient.ResultCallback<List<Message>>() {
            @Override
            public void onSuccess(List<Message> messages) {
                if (CommonUtils.isEmpty(messages)) {
                    try {
                        parsingListMessage(messages);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    //如果本地没有聊天记录 去服务器查询
                    queryRemoteMessage();
                }
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                LogUtils.e("errorCode " + errorCode);
            }
        });
    }

    //查询服务器历史记录
    private void queryRemoteMessage() {
        CloudManager.getInstance().getRemoteHistoryMessages(yourUserId, new RongIMClient.ResultCallback<List<Message>>() {
            @Override
            public void onSuccess(List<Message> messages) {
                if (CommonUtils.isEmpty(messages)) {
                    //解析历史记录
                    try {
                        parsingListMessage(messages);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                LogUtils.e(" errorCode " + errorCode);
            }
        });
    }

    //解析历史记录
    private void parsingListMessage(List<Message> messages) {
        //对消息列表进行倒序处理 否则无法正常显示
        Collections.reverse(messages);
        //遍历消息
        for (int i = 0; i < messages.size(); i++) {
            Message m = messages.get(i);
            String objectName = m.getObjectName();
            if (objectName.equals(CloudManager.MSG_TEXT_NAME)) {
                TextMessage textMessage = (TextMessage) m.getContent();
                String msg = textMessage.getContent();
                LogUtils.i(" msg " + msg);
                try {
                    TextBean textBean = new Gson().fromJson(msg, TextBean.class);
                    if (textBean.getType().equals(CloudManager.TYPE_TEXT)) {
                        //添加到UI 判断是对方发送 还是 我自己发送
                        if (m.getSenderUserId().equals(yourUserId)) {
                            //对方发送
                            addText(0, textBean.getMsg());
                        } else {
                            //我自己发送
                            addText(1, textBean.getMsg());
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

            } else if (objectName.equals(CloudManager.MSG_IMAGE_NAME)) {
                ImageMessage imageMessage = (ImageMessage) m.getContent();
                String url = imageMessage.getRemoteUri().toString();
                if (!TextUtils.isEmpty(url)) {
                    LogUtils.i("url:" + url);
                    if (m.getSenderUserId().equals(yourUserId)) {
                        addImage(0, url);
                    } else {
                        addImage(1, url);
                    }
                }
            } else if (objectName.equals(CloudManager.MSG_LOCATION_NAME)) {

            }
        }
    }


    private void loadMeInfo() {
        Intent intent = getIntent();
        yourUserId = intent.getStringExtra(Constants.INTENT_USER_ID);
        yourUserName = intent.getStringExtra(Constants.INTENT_USER_NAME);
        yourUserPhoto = intent.getStringExtra(Constants.INTENT_USER_PHOTO);

        meUserPhoto = BmobManager.getInstance().getUser().getPhoto();

        LogUtils.i("yourUserPhoto:" + yourUserPhoto);
        LogUtils.i("meUserPhoto:" + meUserPhoto);

        //设置标题  即 好友名称
        if (!TextUtils.isEmpty(yourUserName)) {
            getSupportActionBar().setTitle(yourUserName);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_send_msg:
                String inputText = et_input_msg.getText().toString().trim();
                if (TextUtils.isEmpty(inputText)) {
                    return;
                }
                CloudManager.getInstance().sendTextMessage(inputText, CloudManager.TYPE_TEXT, yourUserId);
                addText(1, inputText);
                //清空输入框
                et_input_msg.setText("");
                break;
            case R.id.ll_voice:

                break;
            case R.id.ll_camera:
                FileHelper.getInstance().toCamera(this);
                break;
            case R.id.ll_pic:
                FileHelper.getInstance().toAlbum(this);
                break;
            case R.id.ll_location:
               // LocationActivity.startActivity(this, true, 0, 0, "", LOCATION_REQUEST_CODE);
        }
    }

    //添加数据的基类
    private void baseAddItem(ChatModel model) {
        mList.add(model);
        mChatAdapter.notifyDataSetChanged();
        //滑动到底部
        mChatView.scrollToPosition(mList.size() - 1);
    }

    //添加文字
    private void addText(int index, String text) {
        LogUtils.i("Chat : " + text);
        ChatModel model = new ChatModel();
        if (index == 0) {
            model.setType(TYPE_LEFT_TEXT);
        } else {
            model.setType(TYPE_RIGHT_TEXT);
        }
        model.setText(text);
        baseAddItem(model);
    }

    //添加图片  使用网络链接
    private void addImage(int index, String url) {
        ChatModel model = new ChatModel();
        if (index == 0) {
            model.setType(TYPE_LEFT_IMAGE);
        } else {
            model.setType(TYPE_RIGHT_IMAGE);
        }
        model.setImgUrl(url);
        baseAddItem(model);
    }

    //添加图片  本地图片
    private void addImage(int index, File file) {
        ChatModel model = new ChatModel();
        if (index == 0) {
            model.setType(TYPE_LEFT_IMAGE);
        } else {
            model.setType(TYPE_RIGHT_IMAGE);
        }
        model.setLocalFile(file);
        baseAddItem(model);
    }

    //接收Event事件  即接收对方发过来的消息
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {

        //如果事件发过来的用户id 正好是 正在聊天 对方的id 即显示聊天消息
        if (!event.getUserId().equals(yourUserId)) {
            return;
        }
        switch (event.getType()) {
            //接收发送文字事件
            case EventManager.FLAG_SEND_TEXT:
                LogUtils.i("event " + event);
                addText(0, event.getText());
                break;

            //接收发送图片事件
            case EventManager.FLAG_SEND_IMAGE:
                LogUtils.i("event " + event);
                addText(0, event.getImgUrl());
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == FileHelper.CAMEAR_REQUEST_CODE) {
                uploadFile = FileHelper.getInstance().getTempFile();
            } else if (requestCode == FileHelper.ALBUM_REQUEST_CODE) {
                Uri uri = data.getData();
                if (uri != null) {
                    //String path = uri.getPath();
                    //获取真实的地址
                    String path = FileHelper.getInstance().getRealPathFromURI(this, uri);
                    //LogUtils.e("path:" + path);
                    if (!TextUtils.isEmpty(path)) {
                        uploadFile = new File(path);
                    }
                }
            } else if (requestCode == LOCATION_REQUEST_CODE) {


            } else if (requestCode == CHAT_INFO_REQUEST_CODE) {
                finish();
            }
            if (uploadFile != null) {
                //发送图片消息
                CloudManager.getInstance().sendImageMessage(yourUserId, uploadFile);
                //更新列表
                addImage(1,uploadFile);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
