package com.example.meetme.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.framework.base.BaseBackActivity;
import com.example.framework.base.BaseUIActivity;
import com.example.framework.bmob.BmobManager;
import com.example.framework.bmob.IMUser;
import com.example.framework.helper.FileHelper;
import com.example.framework.manager.DialogManager;
import com.example.framework.utils.LogUtils;
import com.example.framework.utils.ToastUtil;
import com.example.framework.view.DialogView;
import com.example.framework.view.LoadingView;
import com.example.meetme.R;

import java.io.File;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import de.hdodenhof.circleimageview.CircleImageView;

public class FirstUploadActivity extends BaseBackActivity implements View.OnClickListener {
    private File uploadFile = null;

    private TextView tv_camera;
    private TextView tv_ablum;
    private TextView tv_cancel;

    //圆形头像
    private CircleImageView iv_photo;
    private EditText et_nickname;
    private Button btn_upload;

    private LoadingView mLodingView;
    private DialogView mPhotoSelectView;

    //跳转
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_upload);

        initView();
    }

    public static void startActivity(Activity mActivity) {
        Intent intent = new Intent(mActivity, FirstUploadActivity.class);
        mActivity.startActivity(intent);
    }

    private void initView() {
        //选择框
        initPhotoView();

        iv_photo = (CircleImageView) findViewById(R.id.iv_photo);
        et_nickname = (EditText) findViewById(R.id.et_nickname);
        btn_upload = (Button) findViewById(R.id.btn_upload);

        iv_photo.setOnClickListener(this);
        btn_upload.setOnClickListener(this);

        btn_upload.setEnabled(false);

        et_nickname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    btn_upload.setEnabled(uploadFile != null);
                } else {
                    btn_upload.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    //初始化选择框
    private void initPhotoView() {
            mLodingView = new LoadingView(this);
            mLodingView.setLoadingText(getString(R.string.text_upload_photo_loding));

            mPhotoSelectView = DialogManager.getInstance().initView(this,R.layout.dialog_select_photo, Gravity.BOTTOM);
        tv_camera = (TextView) mPhotoSelectView.findViewById(R.id.tv_camera);
        tv_camera.setOnClickListener(this);
        tv_ablum = (TextView) mPhotoSelectView.findViewById(R.id.tv_ablum);
        tv_ablum.setOnClickListener(this);
        tv_cancel = (TextView) mPhotoSelectView.findViewById(R.id.tv_cancel);
        tv_cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_camera:
                DialogManager.getInstance().hide(mPhotoSelectView);
                FileHelper.getInstance().toCamera(this);
               /* if (!checkPermissions(Manifest.permission.CAMERA)) {
                    requestPermission(new String[]{Manifest.permission.CAMERA});
                }else {
                    //跳转到相机
                    FileHelper.getInstance().toCamera(this);
                }*/
                break;
                case R.id.tv_ablum:
                DialogManager.getInstance().hide(mPhotoSelectView);
                //跳转到相册
                FileHelper.getInstance().toAlbum(this);
                break;
            case R.id.tv_cancel:
                DialogManager.getInstance().hide(mPhotoSelectView);
                break;
            case R.id.iv_photo:
                //显示选择提示框
                DialogManager.getInstance().show(mPhotoSelectView);
                break;
            case R.id.btn_upload:
                uploadPhoto();
                break;
        }
    }

    //上传头像
    private void uploadPhoto() {
        //如果条件没有满足，是走不到这里的 所以不需要判断
        String nickName = et_nickname.getText().toString().trim();
        mLodingView.show();
        BmobManager.getInstance().uploadFirstPhoto(nickName, uploadFile, new BmobManager.OnUploadPhotoListener() {
            @Override
            public void OnUpdateDone() {
                mLodingView.hide();
                finish();
            }

            @Override
            public void OnUpdateFail(BmobException e) {
            mLodingView.hide();
                ToastUtil.QuickToast(e.toString());
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        LogUtils.i("requestCode  " + requestCode);
        if(requestCode == FileHelper.CAMEAR_REQUEST_CODE){
            //跳转相机
            FileHelper.getInstance().startPhotoZoom(this,FileHelper.getInstance().getTempFile());
        }else if(requestCode == FileHelper.ALBUM_REQUEST_CODE){
            //跳转图库
            Uri uri = data.getData();
            if(uri != null){
                //获取图片真实的地址
                String path = FileHelper.getInstance().getRealPathFromURI(this,uri);
                if(!TextUtils.isEmpty(path)){
                    FileHelper.getInstance().startPhotoZoom(this,uploadFile);
                }
            }
        }else if(requestCode == FileHelper.CAMERA_CROP_RESULT){
            LogUtils.i("CAMERA_CROP_RESULT");
            uploadFile = new File(FileHelper.getInstance().getCropPath());
            LogUtils.i("uploadPhotoFile:" + uploadFile.getPath());
        }
        //设置头像
        if(uploadFile != null){
            Bitmap mBitmap = BitmapFactory.decodeFile(uploadFile.getPath());
            iv_photo.setImageBitmap(mBitmap);

            //判断当前的输入框
            String nickName = et_nickname.getText().toString().trim();
            btn_upload.setEnabled(!TextUtils.isEmpty(nickName));
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


}
