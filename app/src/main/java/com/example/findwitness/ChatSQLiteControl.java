package com.example.findwitness;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

public class ChatSQLiteControl {

    ChatSQLiteHelper helper;
    SQLiteDatabase sqlite;

    //생성자
    public ChatSQLiteControl(ChatSQLiteHelper _helper){
        this.helper = _helper;
    }

    //DB Insert
    public void insert(String sender,int rcv,int send,String message,String date,String time){
        sqlite = helper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(helper.SENDER,sender);
        values.put(helper.RCV,rcv);
        values.put(helper.SEND,send);
        values.put(helper.MESSAGE,message);
        values.put(helper.DATE,date);
        values.put(helper.TIME,time);

        sqlite.insert(helper.TABLE_NAME,null,values);

    }
    //DB select
    public void select(){

    }

    public void db_close(){
        sqlite.close();
        helper.close();
    }



}
