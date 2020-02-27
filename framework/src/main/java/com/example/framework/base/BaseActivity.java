package com.example.framework.base;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.framework.utils.LogUtils;
import com.example.framework.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/*
 *  ----BaseActivity  : 做所有的统一功能 语言切换 请求权限
 *        ---BaseUIActivity : 单一功能  沉浸式状态栏
 *        ---BaseBackActivity: 返回键
 *      ......
 * */
public class BaseActivity extends AppCompatActivity {
    //申请运行时权限的Code
    private static final int PERMISSION_REQUEST_CODE = 1000;
    //申请窗口权限的Code
    public static final int PERMISSION_WINDOW_REQUEST_CODE = 1001;

    //申明所需权限
    private String[] mStrPermission = {
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.ACCESS_FINE_LOCATION

    };

    //保存没有同意的权限
    private List<String> mPerList = new ArrayList<>();
    //保存没有同意的失败权限
    private List<String> mPerNoList = new ArrayList<>();

    private OnPermissionsResult permissionsResult;
    //一个方法请求权限
    protected void request(OnPermissionsResult permissionsResult){
        if(!checkPermissionsAll()){
            requestPermissionAll(permissionsResult);
        }
    }

    //动态权限  判断单个权限
    protected boolean checkPermissions(String permissions){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            int check = checkSelfPermission(permissions);
            return check == PackageManager.PERMISSION_GRANTED;
        }
        return false;
    }

    //判断多个权限
    protected boolean checkPermissionsAll(){
        mPerList.clear();
        for (int i = 0; i < mStrPermission.length; i++) {
            boolean check = checkPermissions(mStrPermission[i]);
            //如果不同意则请求
            if (!check) {
                mPerList.add(mStrPermission[i]);
            }
        }
        return mPerList.size() > 0 ? false : true;
    }

    //请求权限 可以请求多个
    protected void requestPersimission(String [] mPermissions){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            requestPermissions(mPermissions,PERMISSION_REQUEST_CODE);
        }
    }

    //申请所有权限
    protected void requestPermissionAll(OnPermissionsResult permissionsResult){
        this.permissionsResult =permissionsResult;
        requestPersimission((String[]) mPerList.toArray(new String[mPerList.size()]));
    }

    //监听权限状态
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mPerList.clear();
        if(requestCode == PERMISSION_REQUEST_CODE){
            if(grantResults.length >0){
                for(int i = 0;i < grantResults.length ;i ++){
                    if(grantResults[i] == PackageManager.PERMISSION_DENIED){
                        //有请求失败的权限
                        mPerNoList.add(permissions[i]);
                    }
                }
                if(permissionsResult != null){
                    if(mPerNoList.size() == 0){
                        permissionsResult.OnSuccess();
                    }else {
                        permissionsResult.OnFail(mPerNoList);
                    }
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    protected interface OnPermissionsResult {
        void OnSuccess();

        void OnFail(List<String> noPermissions);
    }

    //判断窗口权限
    protected boolean checkWindowPermissions(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            return Settings.canDrawOverlays(this);
        }
        return true;
    }

    //请求窗口权限
    protected void requestWindowPermissions() {
        ToastUtil.QuickToast("申请窗口权限，暂时没做UI交互");
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION
                , Uri.parse("package:" + getPackageName()));
        startActivityForResult(intent, PERMISSION_WINDOW_REQUEST_CODE);
    }

}
