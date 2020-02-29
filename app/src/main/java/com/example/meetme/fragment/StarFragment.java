package com.example.meetme.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.framework.adapter.CloudTagAdapter;
import com.example.framework.base.BaseFragment;
import com.example.framework.utils.ToastUtil;
import com.example.meetme.R;
import com.example.meetme.ui.AddFriendActivity;
import com.moxun.tagcloudlib.view.TagCloudView;

import java.util.ArrayList;
import java.util.List;

//星球
public class StarFragment extends BaseFragment implements View.OnClickListener {

    private TextView tv_star_title;
    private ImageView iv_camera;
    private ImageView iv_add;
    private TextView tv_connect_status;
    private TagCloudView mCloudView;
    private TextView tv_random;
    private LinearLayout ll_random;
    private TextView tv_soul;
    private LinearLayout ll_soul;
    private TextView tv_fate;
    private LinearLayout ll_fate;
    private TextView tv_love;
    private LinearLayout ll_love;

    private List<String> mStarList = new ArrayList<>();
    private CloudTagAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_star, null);
        initView(view);
        return view;
    }

    private void initView(View view) {
        iv_camera = view.findViewById(R.id.iv_camera);
        iv_add = view.findViewById(R.id.iv_add);
        tv_connect_status = view.findViewById(R.id.tv_connect_status);

        tv_star_title = view.findViewById(R.id.tv_star_title);

        mCloudView = view.findViewById(R.id.mCloudView);

        ll_random = view.findViewById(R.id.ll_random);
        ll_soul = view.findViewById(R.id.ll_soul);
        ll_fate = view.findViewById(R.id.ll_fate);
        ll_love = view.findViewById(R.id.ll_love);

        iv_camera.setOnClickListener(this);
        iv_add.setOnClickListener(this);

        ll_random.setOnClickListener(this);
        ll_soul.setOnClickListener(this);
        ll_fate.setOnClickListener(this);
        ll_love.setOnClickListener(this);

        for(int i = 0; i < 20;i ++){
            mStarList.add("Star " + i);
        }
        //数据绑定
        adapter = new CloudTagAdapter(getActivity(),mStarList);
        mCloudView.setAdapter(adapter);
        //j监听点击事件
        mCloudView.setOnTagClickListener(new TagCloudView.OnTagClickListener() {
            @Override
            public void onItemClick(ViewGroup parent, View view, int position) {
                ToastUtil.QuickToast("position " + position);
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_camera:
                //扫描二维码
                break;
            case R.id.iv_add:
                //添加好友
                startActivity(new Intent(getActivity(), AddFriendActivity.class));
                break;
            case R.id.ll_random:

                break;
            case R.id.ll_soul:

                break;
            case R.id.ll_fate:

                break;
            case R.id.ll_love:

                break;
        }
    }
}
