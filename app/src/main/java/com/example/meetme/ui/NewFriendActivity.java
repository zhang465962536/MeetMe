package com.example.meetme.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.ViewStub;

import com.example.framework.adapter.CommomViewHolder;
import com.example.framework.adapter.CommonAdapter;
import com.example.framework.bmob.BmobManager;
import com.example.framework.bmob.IMUser;
import com.example.framework.cloud.CloudManager;
import com.example.framework.db.LitePalHelper;
import com.example.framework.db.NewFriend;
import com.example.framework.event.EventManager;
import com.example.framework.utils.CommonUtils;
import com.example.framework.utils.LogUtils;
import com.example.meetme.R;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * 1.查询好友的申请列表
 * 2.通过适配器显示出来
 * 3.如果同意则添加对方为自己的好友
 * 4.并且发送给对方自定义的消息
 * 5.对方将我添加到好友列表
 */
//好友申请页面
public class NewFriendActivity extends AppCompatActivity {
    private ViewStub item_empty_view;
    private RecyclerView mNewFriendView;

    private Disposable disposable;

    private CommonAdapter<NewFriend> mNewFriendAdapter;
    private List<NewFriend> mList = new ArrayList<>();

    /**
     * 实际上这种问题还不是最高效的
     * 因为通过ID获取ImUser是存在网络延迟的
     * 我们可以通过另一种方式处理
     * 看ll_yes的点击事件
     */
    private List<IMUser> mUserList = new ArrayList<>();

    //对方用户
    private IMUser imUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_friend);

        initView();
    }

    private void initView() {

        mNewFriendView = (RecyclerView) findViewById(R.id.mNewFriendView);

        mNewFriendView.setLayoutManager(new LinearLayoutManager(this));
        mNewFriendView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        queryNewFriend();
       /* mList = LitePalHelper.getInstance().queryNewFriend();
        LogUtils.i( " mList   " + mList.size());*/
        mNewFriendAdapter = new CommonAdapter<>(mList, new CommonAdapter.OnBindDataListener<NewFriend>() {
            @Override
            public void onBindViewHolder(final NewFriend model, final CommomViewHolder viewHolder, int type, final int position) {

                //根据Id查询用户信息
                BmobManager.getInstance().queryObjectIdUser(model.getUserId(), new FindListener<IMUser>() {
                    @Override
                    public void done(List<IMUser> list, BmobException e) {
                        //填充具体属性
                        if (e == null) {
                            imUser = list.get(0);
                            mUserList.add(imUser);
                            //设置具体属性
                            setUserInfo(model,viewHolder,imUser);
                        }
                    }
                });

                //同意申请
                viewHolder.getView(R.id.ll_yes).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        /**
                         * 1.同意则刷新当前的Item
                         * 2.将好友添加到自己的好友列表
                         * 3.通知对方我已经同意了
                         * 4.对方将我添加到好友列表
                         * 5.刷新好友列表
                         */
                        updataItem(position,0);
                        BmobManager.getInstance().addFriend(imUser, new SaveListener<String>() {
                            @Override
                            public void done(String s, BmobException e) {
                                if(e == null){
                                    //保存成功

                                    //通知对方
                                    CloudManager.getInstance().sendTextMessage("已经成功添加为好友",CloudManager.TYPE_ARGEED_FRIEND,imUser.getObjectId());
                                    //刷新好友列表
                                    EventManager.post(EventManager.FLAG_UPDATE_FRIEND_LIST);
                                }
                            }
                        });
                    }
                });

                //拒绝申请
                viewHolder.getView(R.id.ll_no).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        updataItem(position,1);
                    }
                });
            }

            @Override
            public int getLayoutId(int type) {
                return R.layout.layout_new_friend_item;
            }
        });


        mNewFriendView.setAdapter(mNewFriendAdapter);
    }

    //更新Item
    private void updataItem(int position, int i) {
        NewFriend newFriend = mList.get(position);
        //更新数据库
        LitePalHelper.getInstance().updateNewFriend(newFriend.getUserId(),i);
        //更新本地数据源
        newFriend.setIsAgree(i);
        mList.set(position,newFriend);
        mNewFriendAdapter.notifyDataSetChanged();
    }

    //通过id 填充用户信息
    private void setUserInfo( NewFriend model,CommomViewHolder viewHolder,IMUser imUser) {
        viewHolder.setImageUrl(NewFriendActivity.this, R.id.iv_photo,
                imUser.getPhoto());
        viewHolder.setImageResource(R.id.iv_sex, imUser.isSex() ?
                R.drawable.img_boy_icon : R.drawable.img_girl_icon);
        viewHolder.setText(R.id.tv_nickname, imUser.getNickName());
        viewHolder.setText(R.id.tv_age, imUser.getAge()
                + getString(R.string.text_search_age));
        viewHolder.setText(R.id.tv_desc, imUser.getDesc());
        viewHolder.setText(R.id.tv_msg, model.getMsg());

        if (model.getIsAgree() == 0) {
            viewHolder.getView(R.id.ll_agree).setVisibility(View.GONE);
            viewHolder.getView(R.id.tv_result).setVisibility(View.VISIBLE);
            viewHolder.setText(R.id.tv_result, getString(R.string.text_new_friend_agree));
        } else if (model.getIsAgree() == 1) {
            viewHolder.getView(R.id.ll_agree).setVisibility(View.GONE);
            viewHolder.getView(R.id.tv_result).setVisibility(View.VISIBLE);
            viewHolder.setText(R.id.tv_result, getString(R.string.text_new_friend_no_agree));
        }
    }

    //在本地数据库查找好友申请记录
    private void queryNewFriend(){
        //在子线程中获取好友申请列表然后在主线程中更新Ui  使用Rxjava 线程调度
        disposable = Observable.create(new ObservableOnSubscribe<List<NewFriend>>() {
            @Override
            public void subscribe(ObservableEmitter<List<NewFriend>> emitter) throws Exception {
                emitter.onNext(LitePalHelper.getInstance().queryNewFriend());
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.newThread())
                //回调到主线程更新Ui
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<NewFriend>>() {
                    @Override
                    public void accept(List<NewFriend> newFriends) throws Exception {
                        //更新UI
                        if (CommonUtils.isEmpty(newFriends)) {
                            mList.addAll(newFriends);
                            mNewFriendAdapter.notifyDataSetChanged();
                        } else {
                            //showViewStub();
                            mNewFriendView.setVisibility(View.GONE);
                        }
                    }
                });

        LitePalHelper.getInstance().queryNewFriend();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(disposable.isDisposed()){
            disposable.dispose();
        }
    }
}
