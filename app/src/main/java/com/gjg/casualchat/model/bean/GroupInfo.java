package com.gjg.casualchat.model.bean;

/**
 * Created by JunGuang_Gao
 * on 2017/2/19  17:00.
 * 类描述：
 */

public class GroupInfo {
    private String groupName;   // 群名称
    private String groupId;     // 群id
    private String invitePerson;// 邀请人

    public GroupInfo() {
    }

    public GroupInfo(String groupName, String groupId, String invitePerson) {
        this.groupName = groupName;
        this.groupId = groupId;
        this.invitePerson = invitePerson;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getinvitePerson() {
        return invitePerson;
    }

    public void setinvitePerson(String invitePerson) {
        this.invitePerson = invitePerson;
    }

    @Override
    public String toString() {
        return "GroupInfo{" +
                "groupName='" + groupName + '\'' +
                ", groupId='" + groupId + '\'' +
                ", invitePerson='" + invitePerson + '\'' +
                '}';
    }
}
