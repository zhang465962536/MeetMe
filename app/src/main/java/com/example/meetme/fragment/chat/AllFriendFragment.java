package com.example.meetme.fragment.chat;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.framework.adapter.CommomViewHolder;
import com.example.framework.adapter.CommonAdapter;
import com.example.framework.base.BaseFragment;
import com.example.framework.bmob.BmobManager;
import com.example.framework.bmob.Friend;
import com.example.framework.bmob.IMUser;
import com.example.framework.utils.CommonUtils;
import com.example.framework.utils.LogUtils;
import com.example.meetme.R;
import com.example.meetme.model.AllFriendModel;
import com.example.meetme.ui.UserInfoActivity;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

//全部好友
public class AllFriendFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
    private View item_empty_view;
    private RecyclerView mAllFriendView;
    private SwipeRefreshLayout mAllFriendRefreshLayout;

    private CommonAdapter<AllFriendModel> mAllFriendAdapter;
    private List<AllFriendModel> mList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_record, null);
        initView(view);
        return view;
    }

    private void initView(View view) {
        item_empty_view = view.findViewById(R.id.item_empty_view);
        mAllFriendView = view.findViewById(R.id.mAllFriendView);
        mAllFriendRefreshLayout = view.findViewById(R.id.mAllFriendRefreshLayout);

        mAllFriendRefreshLayout.setOnRefreshListener(this);

        mAllFriendView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAllFriendView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));

        mAllFriendAdapter = new CommonAdapter<>(mList, new CommonAdapter.OnBindDataListener<AllFriendModel>() {
            @Override
            public void onBindViewHolder(AllFriendModel model, CommomViewHolder viewHolder, int type, int position) {
                viewHolder.setImageUrl(getActivity(), R.id.iv_photo, model.getUrl());
                viewHolder.setText(R.id.tv_nickname, model.getNickName());
                viewHolder.setImageResource(R.id.iv_sex, model.isSex()
                        ? R.drawable.img_boy_icon : R.drawable.img_girl_icon);
                viewHolder.setText(R.id.tv_desc, model.getDesc());

                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        UserInfoActivity.startActivity(getActivity(),model.getUserId());
                    }
                });
            }



            @Override
            public int getLayoutId(int type) {
                return R.layout.layout_all_friend_item;
            }
        });

        mAllFriendView.setAdapter(mAllFriendAdapter);

        queryMyFriend();
    }

    //查询我的好友
    private void queryMyFriend() {
        mAllFriendRefreshLayout.setRefreshing(true);
        BmobManager.getInstance().queryMyFriends(new FindListener<Friend>() {
            @Override
            public void done(List<Friend> list, BmobException e) {
                mAllFriendRefreshLayout.setRefreshing(false);
                if (e == null) {
                    if (CommonUtils.isEmpty(list)) {
                        //有数据
                        item_empty_view.setVisibility(View.GONE);
                        mAllFriendView.setVisibility(View.VISIBLE);
                        if (mList.size() > 0) {
                            mList.clear();
                        }

                        for (int i = 0; i < list.size(); i++) {
                            Friend friend = list.get(i);
                            //获取好友列表ID 查询到详细信息
                            String userId = friend.getFriendUser().getObjectId();
                            BmobManager.getInstance().queryObjectIdUser(userId, new FindListener<IMUser>() {
                                @Override
                                public void done(List<IMUser> list, BmobException e) {
                                    if (e == null) {
                                        if (CommonUtils.isEmpty(list)) {
                                            //好友信息
                                            IMUser imUser = list.get(0);
                                            AllFriendModel model = new AllFriendModel();
                                            //填充数据
                                            setModelToUI(model, imUser);
                                        }
                                    }
                                }
                            });
                        }
                    }else {
                        //全部好友 没有数据  显示空布局
                        item_empty_view.setVisibility(View.VISIBLE);
                        mAllFriendView.setVisibility(View.GONE);
                    }
                }
            }
        });
    }

    private void setModelToUI(AllFriendModel model, IMUser imUser) {
        model.setUserId(imUser.getObjectId());
        model.setUrl(imUser.getPhoto());
        model.setNickName(imUser.getNickName());
        model.setSex(imUser.isSex());
        model.setDesc(getString(R.string.text_all_friend_desc) + imUser.getDesc());
        mList.add(model);
        mAllFriendAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRefresh() {
        if (mAllFriendRefreshLayout.isRefreshing()) {
            //如果在刷新 就查询
            queryMyFriend();
        }
    }
}
