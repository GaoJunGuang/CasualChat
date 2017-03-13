package com.gjg.casualchat.model.bean;

/**
 * Created by JunGuang_Gao
 * on 2017/2/26  22:45.
 * 类描述：
 */

public class PickContactInfo {
    private UserInfo user;      // 联系人
    private boolean isChecked;  // 是否被选择的标记

    public PickContactInfo(UserInfo user, boolean isChecked) {
        this.user = user;
        this.isChecked = isChecked;
    }

    public PickContactInfo() {
    }

    public UserInfo getUser() {
        return user;
    }

    public void setUser(UserInfo user) {
        this.user = user;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setIsChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }
}
