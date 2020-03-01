package com.example.meetme.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
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
import com.example.framework.utils.LogUtils;
import com.example.meetme.R;
import com.example.meetme.model.ChatModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


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
        /* et_input_msg = (EditText) findViewById(R.id.et_input_msg);*/
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
                        break;
                    case TYPE_LEFT_LOCATION:
                        break;

                    case TYPE_RIGHT_TEXT:
                        viewHolder.setText(R.id.tv_right_text, model.getText());
                        viewHolder.setImageUrl(ChatActivity.this, R.id.iv_right_photo, meUserPhoto);
                        break;
                    case TYPE_RIGHT_IMAGE:
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

        addText(0,"你好呀！张小黑");
        addText(1,"你好，嘿嘿嘿！");
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
}
