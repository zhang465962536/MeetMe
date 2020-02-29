package com.example.framework.db;

import com.example.framework.utils.LogUtils;

import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;

import java.util.List;

//实现本地数据库增删改查操作
public class LitePalHelper {

    private static volatile LitePalHelper mInstnce = null;

    private LitePalHelper(){

    }

    public static LitePalHelper getInstance(){
        if(mInstnce == null){
            synchronized (LitePalHelper.class){
                if(mInstnce == null){
                    mInstnce = new LitePalHelper();
                }
            }
        }
        return mInstnce;
    }

    //保存数据基类
    private void baseSave(LitePalSupport support){
        support.save();
    }

    //保存新朋友数据
    public void saveNewFriend(String msg, String id) {
        LogUtils.i("saveNewFriend");
        NewFriend newFriend = new NewFriend();
        newFriend.setMsg(msg);
        newFriend.setUserId(id);
        newFriend.setIsAgree(-1);
        newFriend.setSaveTime(System.currentTimeMillis());
        baseSave(newFriend);
    }

    //查询所有数据基类
    private List<? extends LitePalSupport> baseQuery(Class cls){
        return LitePal.findAll(cls);
    }

    //查询新朋友的类
    public List<NewFriend> queryNewFriend(){
        return (List<NewFriend>) baseQuery(NewFriend.class);
    }

    //更改新朋友请求状态
    public void updateNewFriend(String userId,int agree){
        NewFriend newFriend = new NewFriend();
        newFriend.setIsAgree(agree);
        newFriend.updateAll("userId = ?",userId);
    }
}
