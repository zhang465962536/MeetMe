package com.example.meetme.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.framework.bmob.BmobManager;
import com.example.framework.bmob.IMUser;
import com.example.framework.entity.Constants;
import com.example.framework.manager.DialogManager;
import com.example.framework.utils.LogUtils;
import com.example.framework.utils.SpUtils;
import com.example.framework.utils.ToastUtil;
import com.example.framework.view.DialogView;
import com.example.framework.view.LoadingView;
import com.example.framework.view.TouchPictureV;
import com.example.meetme.MainActivity;
import com.example.meetme.R;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.QueryListener;

//登录页面
/**
 * 1.点击发送的按钮，弹出一个提示框，图片验证码，验证通过之后
 * 2.!发送验证码，@同时按钮变成不可点击，@按钮开始倒计时，倒计时结束，@按钮可点击，@文字变成“发送”
 * 3.通过手机号码和验证码进行登录
 * 4.登录成功之后获取本地对象
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText et_phone;
    private EditText et_code;
    private Button btn_send_code;
    private Button btn_login;
    private TextView tv_test_login;
    private TextView tv_user_agreement;

    private DialogView mCodeView;
    private TouchPictureV mPictureV;
    private LoadingView mLoadingView;
    private static final int H_TIME = 1001;

    //60S倒计时
    private static int TIME = 60;
    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case H_TIME:
                    TIME --;
                    btn_send_code.setText(TIME + "s");
                    if(TIME > 0){
                        btn_send_code.setEnabled(false);
                        mHandler.sendEmptyMessageDelayed(H_TIME,1000);
                    }else {
                        btn_send_code.setEnabled(true);
                        btn_send_code.setText(getString(R.string.text_login_send));
                    }
                    break;
            }
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
    }

    private void initView() {
        //初始化Dialog
        initDialogView();

        et_phone = (EditText) findViewById(R.id.et_phone);
        et_code = (EditText) findViewById(R.id.et_code);
        btn_send_code = (Button) findViewById(R.id.btn_send_code);
        btn_login = (Button) findViewById(R.id.btn_login);
        tv_test_login = (TextView) findViewById(R.id.tv_test_login);
        tv_user_agreement = (TextView) findViewById(R.id.tv_user_agreement);

        tv_test_login.setOnClickListener(this);
        btn_send_code.setOnClickListener(this);
        btn_login.setOnClickListener(this);
        //取出保存的手机号码显示在EditText
        String phone = SpUtils.getInstance().getString(Constants.SP_PHONE, "");
        if (!TextUtils.isEmpty(phone)) {
            et_phone.setText(phone);
        }
    }

    private void initDialogView() {

        mLoadingView = new LoadingView(this);

        mCodeView = DialogManager.getInstance().initView(this,R.layout.dialog_code_view);
        mPictureV = mCodeView.findViewById(R.id.mPictureV);
        mPictureV.setViewResultListener(new TouchPictureV.OnViewResultListener() {
            @Override
            public void onResult() {
                sendSMS();
                DialogManager.getInstance().hide(mCodeView);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_send_code:
                final String phone = et_phone.getText().toString().trim();
                if(!TextUtils.isEmpty(phone)){
                    DialogManager.getInstance().show(mCodeView);
                }else {
                    ToastUtil.QuickToast(getString(R.string.text_login_phone_null));
                    return;
                }
                break;
            case R.id.btn_login:
                login();
            case R.id.tv_test_login:
                startActivity(new Intent(this, TestLoginActivity.class));
                break;
        }
    }

    //登录
    private void login() {
        // 1 判断手机号码和验证码不为空
        final String phone = et_phone.getText().toString().trim();
        if(TextUtils.isEmpty(phone)){
            ToastUtil.QuickToast(getString(R.string.text_login_phone_null));
            return;
        }

        final String code = et_code.getText().toString().trim();
        if(TextUtils.isEmpty(code)){
            ToastUtil.QuickToast(getString(R.string.text_login_code_null));
            return;
        }

        //显示Loading
        mLoadingView.show("正在登录...");

        BmobManager.getInstance().signOrLoginByMobilePhone(phone, code, new LogInListener<IMUser>() {
            @Override
            public void done(IMUser imUser, BmobException e) {
                if(e == null){
                    //登录成功
                    mLoadingView.hide();
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    //把手机号码保存下来
                    SpUtils.getInstance().putString(Constants.SP_PHONE,phone);
                    finish();
                }else {
                    if(e.getErrorCode() == 207){
                        ToastUtil.QuickToast(getString(R.string.text_login_code_error));
                    }else {
                        ToastUtil.QuickToast("ERROR " + e.toString());
                    }
                }
            }
        });
    }

    //发送短信验证码
    private void sendSMS() {
        // 1 获取手机号码
        String phone = et_phone.getText().toString().trim();
        if(TextUtils.isEmpty(phone)){
            ToastUtil.QuickToast(getString(R.string.text_login_phone_null));
            return;
        }
        //请求短信验证码
        BmobManager.getInstance().requestSMS(phone, new QueryListener<Integer>() {
            @Override
            public void done(Integer integer, BmobException e) {
                if(e == null){
                    btn_send_code.setEnabled(false);
                    mHandler.sendEmptyMessage(H_TIME);
                    ToastUtil.QuickToast(getString(R.string.text_user_resuest_succeed));
                }else {
                    LogUtils.e("SMS "  + e.toString());
                    ToastUtil.QuickToast(getString(R.string.text_user_resuest_fail));
                }
            }
        });
    }

}
