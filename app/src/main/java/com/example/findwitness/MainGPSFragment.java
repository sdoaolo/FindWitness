package com.example.findwitness;

import android.Manifest;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Address;

import android.nfc.Tag;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;


import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainGPSFragment extends Fragment {
    Button btn_start;
    Button btn_finish;
    Button btn_list;
    //핸들러
    GPS_handler handler = new GPS_handler();
    //데이터 베이스 만들기
    private SQLiteDatabase db;
    private GPSdatabaseHelper dbHelper;

    //private GpsTracker gpsTracker;
    boolean running;
    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    String[] REQUIRED_PERMISSIONS  = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

    public MainGPSFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_gps, container, false);
        return inflater.inflate(R.layout.fragment_main_gps, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btn_start = view.findViewById(R.id.btn_start);
        btn_finish = view.findViewById(R.id.btn_finish);
        btn_list = view.findViewById(R.id.btn_list);

        // 데이터베이스 생성
        boolean isOpen = openDatabase();
        if(!isOpen) {
            //Toast.makeText(getApplicationContext(), "실패", Toast.LENGTH_SHORT).show();
        }

        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("RRRRRRRRR","start");
                running = true;
                Thread thread1 = new GPS_Thread();
                thread1.start();
            }
        });
        btn_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("RRRRRRRRR","finish");
                running = false;
                Toast.makeText(getActivity(),"Thread is stop",Toast.LENGTH_SHORT).show();
            }
        });
        btn_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("RRRRRRRRR","showlist");
            }
        });
    }
    class GPS_Thread extends Thread{
        @Override
        public void run() {
            while(running){
                try{
                    Message message = handler.obtainMessage();
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("start", true);
                    message.setData(bundle);
                    handler.sendMessage(message);

                    Thread.sleep(3000);
                } catch (InterruptedException ex){
                    Log.e("GPS_Thread running","Exception in thread",ex);
                }
            }
        }
    }
    class GPS_handler extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            Bundle bundle = msg.getData();
            boolean run = bundle.getBoolean("start");
            if(run){
                GPSTracker gpsTracker = new GPSTracker(getActivity());

                double latitude = gpsTracker.getLatitude();
                double longitude = gpsTracker.getLongitude();

                //데이터 베이스에 삽입
                long now = System.currentTimeMillis();
                //Toast.makeText(getApplicationContext(),temp,Toast.LENGTH_SHORT).show();
                Date date = new Date(now);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd/HH:mm:ss");

                String getTime = sdf.format(date), dat, time;
                dat = getTime.substring(0,4) + getTime.substring(5,7) + getTime.substring(8,10);
                time = getTime.substring(11,13) + getTime.substring(14,16) + getTime.substring(17,19);
                //Toast.makeText(getApplicationContext(), time,Toast.LENGTH_SHORT).show();
                Log.d("데이터 : ", "시간 : " + time);

                insertRecord(latitude, longitude, dat, time);
            }
        }
    }

    //데이터 베이스
    private boolean openDatabase(){
        dbHelper = new GPSdatabaseHelper(getActivity(),"mydb");
        db = dbHelper.getWritableDatabase();
        return true;
    }
    public void insertRecord(double latitude, double longitude, String data, String time){
        db.execSQL("insert into gps(latitude, longitude, data, time) values(" +
                latitude + "," + longitude + "," + data + "," + time + ");");
    }

    @Override
    public void onDestroy() {
        running = false;
        super.onDestroy();
    }
}
