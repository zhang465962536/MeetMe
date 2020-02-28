package com.example.meetme.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;

import com.example.framework.adapter.CommomViewHolder;
import com.example.framework.adapter.CommonAdapter;
import com.example.framework.bmob.BmobManager;
import com.example.framework.bmob.IMUser;
import com.example.framework.utils.CommonUtils;
import com.example.framework.utils.LogUtils;
import com.example.meetme.R;
import com.example.meetme.adapter.AddFriendAdapter;
import com.example.meetme.model.AddFriendModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

//从通讯录导入好友
public class ContactFirendActivity extends AppCompatActivity {
    private RecyclerView mContactView;
    //通过键值对存储姓名手机
    private Map<String,String> mContactMap  = new HashMap<>();

    private CommonAdapter mContactAdapter;
    private List<AddFriendModel> mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_firend);

        initView();
    }

    private void initView() {
        mContactView =  findViewById(R.id.mContactView);
        mContactView.setLayoutManager(new LinearLayoutManager(this));
        mContactView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        mContactAdapter = new CommonAdapter<>(mList, new CommonAdapter.OnBindDataListener<AddFriendModel>() {
            @Override
            public void onBindViewHolder(final AddFriendModel model, CommomViewHolder viewHolder, int type, int position) {
                //设置头像
                viewHolder.setImageUrl(ContactFirendActivity.this, R.id.iv_photo, model.getPhoto());
                //设置性别
                viewHolder.setImageResource(R.id.iv_sex,
                        model.isSex() ? R.drawable.img_boy_icon : R.drawable.img_girl_icon);
                //设置昵称
                viewHolder.setText(R.id.tv_nickname, model.getNickName());
                //年龄
                viewHolder.setText(R.id.tv_age, model.getAge() + getString(R.string.text_search_age));
                //设置描述
                viewHolder.setText(R.id.tv_desc, model.getDesc());

                if (model.isContact()) {
                    viewHolder.getView(R.id.ll_contact_info).setVisibility(View.VISIBLE);
                    viewHolder.setText(R.id.tv_contact_name, model.getContactName());
                    viewHolder.setText(R.id.tv_contact_phone, model.getContactPhone());
                }

                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                      /*  UserInfoActivity.startActivity(ContactFirendActivity.this,
                                model.getUserId());*/
                    }
                });
            }

            @Override
            public int getLayoutId(int type) {
                return R.layout.layout_search_user_item;
            }
        });

        mContactView.setAdapter(mContactAdapter);

        loadContact();
        //遍历云端数据 进行通讯录手机号码对比
        loadUser();

    }

    //加载用户
    private void loadUser() {
        LogUtils.i("mContactMap.size() " + mContactMap.size());
        if(mContactMap.size() > 0){
            for(final Map.Entry<String,String> entry : mContactMap.entrySet()){
                LogUtils.i("测试 与通讯录相同的手机 " + " name " + entry.getKey() + "  phone " + entry.getValue());
                BmobManager.getInstance().queryPhoneUser(entry.getValue(), new FindListener<IMUser>() {
                    @Override
                    public void done(List<IMUser> list, BmobException e) {
                        if(e == null){
                            if(CommonUtils.isEmpty(list)){
                                IMUser imUser = list.get(0);
                                addContent(imUser,entry.getKey(),entry.getValue());
                                LogUtils.i("测试 与通讯录相同的手机 " + imUser.toString() + "name " + entry.getKey() + "  phone " + entry.getValue());

                            }
                        }
                    }
                });
            }
        }
    }

    //加载联系人
    /**
     * 1.拿到用户的联系人列表
     * 2.查询我们的PrivateSet
     * 3.过滤一遍联系人列表
     * 4.去显示
     */
    private void loadContact() {
        Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        String name;
        String phone;
        while (cursor.moveToNext()){
            name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            phone = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            LogUtils.i("name:" + name + " phone:" + phone);
            //此时得到的 phone 内容是 179 8196 4403 需要将空格去除
            phone = phone.replace(" ", "").replace("-", "");
            mContactMap.put(name, phone);
        }
    }

    //添加内容
    private void addContent(IMUser imUser,String name,String phone) {
        AddFriendModel model = new AddFriendModel();
        model.setType(AddFriendAdapter.TYPE_CONTENT);
        model.setUserId(imUser.getObjectId());
        model.setPhoto(imUser.getPhoto());
        model.setSex(imUser.isSex());
        model.setAge(imUser.getAge());
        model.setNickName(imUser.getNickName());
        model.setDesc(imUser.getDesc());

        model.setContact(true);
        model.setContactName(name);
        model.setContactPhone(phone);

        mList.add(model);
        mContactAdapter.notifyDataSetChanged();
    }


}
