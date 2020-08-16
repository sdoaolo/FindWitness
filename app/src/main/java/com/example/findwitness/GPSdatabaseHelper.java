package com.example.findwitness;
import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.nfc.Tag;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import static android.content.ContentValues.TAG;

public class GPSdatabaseHelper extends SQLiteOpenHelper {
    public GPSdatabaseHelper(@Nullable Context context, String name) {
        super(context, name, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            String DROP_SQL = "drop table if exists " + "gps";
            db.execSQL(DROP_SQL);
        } catch(Exception ex){
            Log.e(TAG, "Exception in DROP_SQL", ex);
        }
        String Create_SQL = "create table gps (_ID INTEGER PRIMARY KEY AUTOINCREMENT, LATITUDE TEXT, LONGITUDE TEXT, _DATE TEXT, _TIME TEXT);";
        try{
            db.execSQL(Create_SQL);
        }catch (Exception ex){
            Log.e(TAG, "Exception in Create_SQL", ex);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
