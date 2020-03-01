package com.example.meetme.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.framework.base.BaseFragment;
import com.example.meetme.R;
import com.example.meetme.fragment.chat.AllFriendFragment;
import com.example.meetme.fragment.chat.CallRecordFragment;
import com.example.meetme.fragment.chat.ChatRecordFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

//聊天
public class ChatFragment extends BaseFragment {

    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    private String[] mTitle;

    private List<Fragment> mFragmentList = new ArrayList<>();
    private ChatRecordFragment mChatRecordFragment;
    private CallRecordFragment mCallRecordFragment;
    private AllFriendFragment mAllFriendFragment;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat,null);
        initView(view);
        return view;
    }

    private void initView(View view) {
        //聊天记录 通话记录 全部好友
        mTitle = new String[]{getString(R.string.text_chat_tab_title_1)
                , getString(R.string.text_chat_tab_title_2)
                , getString(R.string.text_chat_tab_title_3)};

        mChatRecordFragment = new ChatRecordFragment();
        mCallRecordFragment = new CallRecordFragment();
        mAllFriendFragment = new AllFriendFragment();

        mFragmentList.add(mChatRecordFragment);
        mFragmentList.add(mCallRecordFragment);
        mFragmentList.add(mAllFriendFragment);

        mTabLayout = view.findViewById(R.id.mTabLayout);
        mViewPager = view.findViewById(R.id.mViewPager);

        for (int i = 0; i < mTitle.length; i++) {
            mTabLayout.addTab(mTabLayout.newTab().setText(mTitle[i]));
        }

        //预加载
        mViewPager.setOffscreenPageLimit(mTitle.length);
        mViewPager.setAdapter(new ChatPagerAdapter(getFragmentManager()));
        //绑定ViewPager 进行联动
        mTabLayout.setupWithViewPager(mViewPager);

        //监听TabLayout 滑动
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            //选择Tab 就切换它的样式
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                defTabStyle(tab, 20);
            }

            //解绑 滑动得时候Tab Title 字体变大
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.setCustomView(null);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        //默认第一个选中
        defTabStyle(mTabLayout.getTabAt(0), 20);
    }

    //设置Tab样式
    private void defTabStyle(TabLayout.Tab tab, int size) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_tab_text, null);
        TextView tv_tab = view.findViewById(R.id.tv_tab);
        tv_tab.setText(tab.getText());
        tv_tab.setTextColor(Color.WHITE);
        tv_tab.setTextSize(size);
        tab.setCustomView(tv_tab);
    }

    class ChatPagerAdapter extends FragmentStatePagerAdapter {

        public ChatPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            //super.destroyItem(container, position, object);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mTitle.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitle[position];
        }
    }
}
