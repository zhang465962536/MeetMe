package com.example.meetme;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.framework.base.BaseUIActivity;
import com.example.framework.bmob.BmobManager;
import com.example.framework.entity.Constants;
import com.example.framework.manager.DialogManager;
import com.example.framework.utils.LogUtils;
import com.example.framework.utils.SpUtils;
import com.example.framework.view.DialogView;
import com.example.meetme.fragment.ChatFragment;
import com.example.meetme.fragment.MeFragment;
import com.example.meetme.fragment.SquareFragment;
import com.example.meetme.fragment.StarFragment;
import com.example.meetme.ui.FirstUploadActivity;

import java.util.List;
//主页

/**
 * 1.初始化Frahment
 * 2.显示Fragment
 * 3.隐藏所有的Fragment
 * 4.恢复Fragment
 * 优化的手段
 */
public class MainActivity extends BaseUIActivity implements View.OnClickListener {

    //星球
    private ImageView iv_star;
    private TextView tv_star;
    private LinearLayout ll_star;
    private StarFragment mStarFragment = null;
    private FragmentTransaction mStarTransaction = null;

    //广场
    private ImageView iv_square;
    private TextView tv_square;
    private LinearLayout ll_square;
    private SquareFragment mSquareFragment = null;
    private FragmentTransaction mSquareTransaction = null;

    //聊天
    private ImageView iv_chat;
    private TextView tv_chat;
    private LinearLayout ll_chat;
    private ChatFragment mChatFragment = null;
    private FragmentTransaction mChatTransaction = null;

    //我的
    private ImageView iv_me;
    private TextView tv_me;
    private LinearLayout ll_me;
    private MeFragment mMeFragment = null;
    private FragmentTransaction mMeTransaction = null;

    private DialogView mUploadView;

    private static final int UPLOAD_REQUEST_CODE = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();


    }


    private void initView() {

        requestPermiss();

        iv_star = (ImageView) findViewById(R.id.iv_star);
        tv_star = (TextView) findViewById(R.id.tv_star);
        ll_star = (LinearLayout) findViewById(R.id.ll_star);

        iv_square = (ImageView) findViewById(R.id.iv_square);
        tv_square = (TextView) findViewById(R.id.tv_square);
        ll_square = (LinearLayout) findViewById(R.id.ll_square);

        iv_chat = (ImageView) findViewById(R.id.iv_chat);
        tv_chat = (TextView) findViewById(R.id.tv_chat);
        ll_chat = (LinearLayout) findViewById(R.id.ll_chat);

        iv_me = (ImageView) findViewById(R.id.iv_me);
        tv_me = (TextView) findViewById(R.id.tv_me);
        ll_me = (LinearLayout) findViewById(R.id.ll_me);

        ll_star.setOnClickListener(this);
        ll_square.setOnClickListener(this);
        ll_chat.setOnClickListener(this);
        ll_me.setOnClickListener(this);

        //设置文本
        tv_star.setText(getString(R.string.text_main_star));
        tv_square.setText(getString(R.string.text_main_square));
        tv_chat.setText(getString(R.string.text_main_chat));
        tv_me.setText(getString(R.string.text_main_me));

        initFragment();

        //切换默认的选项卡
        checkMainTab(0);

        //检查TOKEN
        checkToken();
    }

    //检查TOKEN
    private void checkToken() {
        LogUtils.i("checkToken");
        if(mUploadView != null){
            DialogManager.getInstance().hide(mUploadView);
        }
        ////获取TOKEN 需要三个参数 1.用户ID 2.头像地址 3.昵称
        String token = SpUtils.getInstance().getString(Constants.SP_TOKEN, "");
        if(!TextUtils.isEmpty(token)){
            startCloudService();
        }else {
            // 有三个参数
            String tokenPhoto = BmobManager.getInstance().getUser().getTokenPhoto();
            String tokenName = BmobManager.getInstance().getUser().getTokenNickName();
            if (!TextUtils.isEmpty(tokenPhoto) && !TextUtils.isEmpty(tokenName)) {
                //创建Token
                createToken();
            }else {
                //创建上传提示框
                createUploadDialog();
            }
        }
    }

    // 创建上传提示框
    private void createUploadDialog() {
        mUploadView = DialogManager.getInstance().initView(this,R.layout.dialog_first_upload);
        //对话框外部无法点击
        mUploadView.setCancelable(false);
        ImageView iv_go_upload = mUploadView.findViewById(R.id.iv_go_upload);
        iv_go_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirstUploadActivity.startActivity(MainActivity.this);
            }
        });
        DialogManager.getInstance().show(mUploadView);
    }

    //创建TOKEN
    private void createToken() {
        LogUtils.e("createToken");
    }

    //启动云服务去连接融云服务
    private void startCloudService() {
    }

    //初始化4个Fragment
    private void initFragment() {
        //星球
        if (mStarFragment == null) {
            mStarFragment = new StarFragment();
            mStarTransaction = getSupportFragmentManager().beginTransaction();
            mStarTransaction.add(R.id.mMainLayout, mStarFragment);
            mStarTransaction.commit();
        }

        //广场
        if (mSquareFragment == null) {
            mSquareFragment = new SquareFragment();
            mSquareTransaction = getSupportFragmentManager().beginTransaction();
            mSquareTransaction.add(R.id.mMainLayout, mSquareFragment);
            mSquareTransaction.commit();
        }

        //聊天
        if (mChatFragment == null) {
            mChatFragment = new ChatFragment();
            mChatTransaction = getSupportFragmentManager().beginTransaction();
            mChatTransaction.add(R.id.mMainLayout, mChatFragment);
            mChatTransaction.commit();
        }

        //我的
        if (mMeFragment == null) {
            mMeFragment = new MeFragment();
            mMeTransaction = getSupportFragmentManager().beginTransaction();
            mMeTransaction.add(R.id.mMainLayout, mMeFragment);
            mMeTransaction.commit();
        }
    }

    //展示Fragment
    private void showFragment(Fragment fragment) {
        if (fragment != null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            hideAllFragment(transaction);
            transaction.show(fragment);
            transaction.commitAllowingStateLoss();
        }
    }

    //隐藏所有的Fragment
    private void hideAllFragment(FragmentTransaction transaction) {
        if (mStarFragment != null) {
            transaction.hide(mStarFragment);
        }
        if (mSquareFragment != null) {
            transaction.hide(mSquareFragment);
        }
        if (mChatFragment != null) {
            transaction.hide(mChatFragment);
        }
        if (mMeFragment != null) {
            transaction.hide(mMeFragment);
        }
    }

    //切换主页选项卡
    private void checkMainTab(int index) {
        switch (index) {
            case 0:
                showFragment(mStarFragment);

                iv_star.setImageResource(R.drawable.img_star_p);
                iv_square.setImageResource(R.drawable.img_square);
                iv_chat.setImageResource(R.drawable.img_chat);
                iv_me.setImageResource(R.drawable.img_me);

                tv_star.setTextColor(getResources().getColor(R.color.colorAccent));
                tv_square.setTextColor(Color.BLACK);
                tv_chat.setTextColor(Color.BLACK);
                tv_me.setTextColor(Color.BLACK);

                break;
            case 1:
                showFragment(mSquareFragment);

                iv_star.setImageResource(R.drawable.img_star);
                iv_square.setImageResource(R.drawable.img_square_p);
                iv_chat.setImageResource(R.drawable.img_chat);
                iv_me.setImageResource(R.drawable.img_me);

                tv_star.setTextColor(Color.BLACK);
                tv_square.setTextColor(getResources().getColor(R.color.colorAccent));
                tv_chat.setTextColor(Color.BLACK);
                tv_me.setTextColor(Color.BLACK);

                break;
            case 2:
                showFragment(mChatFragment);

                iv_star.setImageResource(R.drawable.img_star);
                iv_square.setImageResource(R.drawable.img_square);
                iv_chat.setImageResource(R.drawable.img_chat_p);
                iv_me.setImageResource(R.drawable.img_me);

                tv_star.setTextColor(Color.BLACK);
                tv_square.setTextColor(Color.BLACK);
                tv_chat.setTextColor(getResources().getColor(R.color.colorAccent));
                tv_me.setTextColor(Color.BLACK);

                break;
            case 3:
                showFragment(mMeFragment);

                iv_star.setImageResource(R.drawable.img_star);
                iv_square.setImageResource(R.drawable.img_square);
                iv_chat.setImageResource(R.drawable.img_chat);
                iv_me.setImageResource(R.drawable.img_me_p);

                tv_star.setTextColor(Color.BLACK);
                tv_square.setTextColor(Color.BLACK);
                tv_chat.setTextColor(Color.BLACK);
                tv_me.setTextColor(getResources().getColor(R.color.colorAccent));

                break;
        }
    }


    /**
     * 防止重叠
     * 当应用的内存紧张的时候，系统会回收掉Fragment对象
     * 再一次进入的时候会重新创建Fragment
     * 非原来对象，我们无法控制，导致重叠
     * 还原fragment即可
     */
    @Override
    public void onAttachFragment(@NonNull Fragment fragment) {
        if (mStarFragment != null && fragment instanceof StarFragment) {
            mStarFragment = (StarFragment) fragment;
        }
        if (mSquareFragment != null && fragment instanceof SquareFragment) {
            mSquareFragment = (SquareFragment) fragment;
        }
        if (mChatFragment != null && fragment instanceof ChatFragment) {
            mChatFragment = (ChatFragment) fragment;
        }
        if (mMeFragment != null && fragment instanceof MeFragment) {
            mMeFragment = (MeFragment) fragment;
        }
    }

    //请求权限
    private void requestPermiss() {
        //危险权限
        request(new OnPermissionsResult() {
            @Override
            public void OnSuccess() {

            }

            @Override
            public void OnFail(List<String> noPermissions) {
                LogUtils.i("noPermissions:" + noPermissions.toString());
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_star:
                checkMainTab(0);
                break;
            case R.id.ll_square:
                checkMainTab(1);
                break;
            case R.id.ll_chat:
                checkMainTab(2);
                break;
            case R.id.ll_me:
                checkMainTab(3);
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == UPLOAD_REQUEST_CODE){
                //说明上传头像成功
                checkToken();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
