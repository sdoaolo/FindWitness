package com.example.findwitness.Item;

public class ChatListViewItem {
    private String userName;
    private String userId;
    public ChatListViewItem(String userName,String userId) { this.userName = userName; this .userId = userId;}

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

}
