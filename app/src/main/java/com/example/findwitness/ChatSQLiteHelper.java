package com.example.findwitness;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class ChatSQLiteHelper extends SQLiteOpenHelper{

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "chat.db";
    public static final String TABLE_NAME = "chat";
    public static final String TID = "_id";
    public static final String SENDER = "sender";
    public static final String RCV = "rcv";
    public static final String SEND = "send";
    public static final String MESSAGE = "message";
    public static final String DATE = "date";
    public static final String TIME = "time";

    public ChatSQLiteHelper(Context context, String name) {
        super(context, name, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        String sql= "create table if not exists "+TABLE_NAME+ "("
                +TID+" integer PRIMARY KEY autoincrement,"
                +SENDER+" text,"+RCV+" integer,"+SEND+" integer,"
                +MESSAGE+" text,"+DATE+" text,"+TIME+" text);";

        db.execSQL(sql);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "drop table if exists student;";
        db.execSQL(sql);
        onCreate(db); // 다시 테이블 생성

    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }


}
