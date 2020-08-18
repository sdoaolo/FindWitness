package com.example.findwitness.Item;

public class ChattingListViewItem {
    private String nickName;
    private String context;
    private String time;
    private String chatting_num;
    public ChattingListViewItem(String nickName,String context,String time,String chatting_num) {
        this.nickName = nickName;
        this.context = context;
        this.time = time;
        this.chatting_num = chatting_num;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getChatting_num() {
        return chatting_num;
    }

    public void setChatting_num(String chatting_num) {
        this.chatting_num = chatting_num;
    }
}
