package com.example.framework.base;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.framework.utils.ToastUtil;

//Fragment 基类
public class BaseFragment extends Fragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    //判断窗口权限
    protected boolean checkWindowPermissions(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            return Settings.canDrawOverlays(getActivity());
        }
        return true;
    }

    //请求窗口权限
    protected void requestWindowPermissions(){
        ToastUtil.QuickToast("申请窗口权限，暂时没做UI交互");
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getActivity().getPackageName()));
        startActivityForResult(intent, BaseActivity.PERMISSION_WINDOW_REQUEST_CODE);
    }
}
