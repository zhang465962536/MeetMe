package com.example.meetme.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;

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

    private AddFriendAdapter mAddFriendAdapter;
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

        loadContact();
        //遍历云端数据 进行通讯录手机号码对比
        loadUser();
       /* mAddFriendAdapter = new AddFriendAdapter(this,mList);
        mContactView.setAdapter(mAddFriendAdapter);

        mAddFriendAdapter.setOnClickListener(new AddFriendAdapter.OnClickListener() {
            @Override
            public void OnClick(int position) {

            }
        });*/
    }

    //加载用户
    private void loadUser() {
        if(mContactMap.size() > 0){
            for(final Map.Entry<String,String> entry : mContactMap.entrySet()){
                BmobManager.getInstance().queryPhoneUser(entry.getValue(), new FindListener<IMUser>() {
                    @Override
                    public void done(List<IMUser> list, BmobException e) {
                        if(e == null){
                            if(CommonUtils.isEmpty(list)){
                                IMUser imUser = list.get(0);
                                addContent(imUser,entry.getKey(),entry.getValue());

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
        mAddFriendAdapter.notifyDataSetChanged();
    }


}
