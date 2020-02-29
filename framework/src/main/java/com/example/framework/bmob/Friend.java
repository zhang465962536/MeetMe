package com.example.framework.bmob;

import cn.bmob.v3.BmobObject;

//在 把bmob 建立 Friend表
public class Friend extends BmobObject {
    //我自己
    private IMUser user;
    //朋友
    private IMUser friendUser;

    public IMUser getUser() {
        return user;
    }

    public void setUser(IMUser user) {
        this.user = user;
    }

    public IMUser getFriendUser() {
        return friendUser;
    }

    public void setFriendUser(IMUser friendUser) {
        this.friendUser = friendUser;
    }
}
