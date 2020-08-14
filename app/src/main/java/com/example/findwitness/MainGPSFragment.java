package com.example.findwitness;

import android.Manifest;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainGPSFragment extends Fragment {
    Button btn_start;
    Button btn_finish;
    Button btn_list;
    //핸들러
    GPS_handler handler = new GPS_handler();
    //데이터 베이스 만들기
    private SQLiteDatabase db;
    private GPSdatabaseHelper dbHelper;

    ArrayList<GPSListViewItem> gpsList;

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

        this.InitializeData();
        ListView listView = view.findViewById(R.id.gps_list);
        final GPSListViewAdapter adapter;
        adapter = new GPSListViewAdapter(getActivity(),gpsList);
        listView.setAdapter(adapter);

        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("RRRRRRRRR","start");
                running = true;
                //Thread thread1 = new GPS_Thread();
                //thread1.start();
                String temp = "20200814|122055|27.3425465|111.223456";
                Thread thread2 = new Network_Thread(temp);
                thread2.start();
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
                boolean existdata = exist_database_data();   // 데이터가 존재하면 true, 존재하지 않으면 false

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
    class Network_Thread extends Thread{
        private String parameter;
        public Network_Thread(String parameter) {
            this.parameter = parameter;
        }
        @Override
        public void run() {
            while(running){
                try{
                    // message = NET_handler.obtainMessage();
                    //Bundle bundle = new Bundle();
                    //bundle.putBoolean("ok", true);
                    //message.setData(bundle);
                    //NET_handler.sendMessage(message);
                    Thread.sleep(1000);

                    try {
                        String[] data = parameter.split("\\|");
                        //URL url = new URL("http://192.168.0.7:8080/servelet/login");
                        //URL url = new URL("http://localhost:8080/servelet_test/Login");
                        URL url = new URL("http://192.168.0.8:8080/servelet_test/Login");
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                        Log.i("쓰레드", "접속시도");
                        if (conn != null) {
                            Log.i("쓰레드", "접속성공");
                            conn.setConnectTimeout(10000);
                            conn.setRequestMethod("POST");

                            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                            conn.setDoOutput(true);
                            conn.setDoInput(true);

                            String sendMsg = "time=" + data[1] + "&date=" + data[0] + "&lati=" +
                                    data[2] + "&long=" + data[3];

                            OutputStream os = conn.getOutputStream();
                            os.write(sendMsg.getBytes("utf-8"));
                            //os.write(sendMsg2.getBytes("utf-8"));

                            os.flush();
                            os.close();
                            int resCode = conn.getResponseCode();
                            Log.i("응답코드", ""+resCode);
                            if (resCode == HttpURLConnection.HTTP_OK) {
                                Log.i("쓰레드", "응답수신");
                                InputStream is = conn.getInputStream();
                                InputStreamReader isr = new InputStreamReader(is);
                                BufferedReader br = new BufferedReader(isr);
                                StringBuilder strBuilder = new StringBuilder();String line = null;
                                while ((line = br.readLine()) != null) {
                                    strBuilder.append(line + "\n");
                                }

                                //jsonStr = strBuilder.toString();
                                br.close();
                                conn.disconnect();
                            }
                            Log.i("쓰레드", "응답처리완료");
                        }
                    }catch (Exception ex){
                        Log.e("접속요류", ""+ex);
                    }


                } catch (InterruptedException ex){
                    Log.e("GPS_Thread running","Exception in thread",ex);
                }
            }
        }
    }
    class Network_handler extends Handler{
        @Override
        public void handleMessage(@NonNull Message msg) {
            Bundle bundle = msg.getData();
            boolean run = bundle.getBoolean("ok");
            if(run){
                Log.d("net","성공!");
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
    public boolean exist_database_data(){
        boolean exist = true;
        Cursor c1 = db.rawQuery("select * from " + "gps", null);
        int size = c1.getCount();
        if(c1.getCount() == 0) exist = false;
        return exist;
    }
    // init data
    public void InitializeData()
    {
        Cursor c1 = db.rawQuery("select * from " + "gps", null);
        gpsList = new ArrayList<GPSListViewItem>();

        int text = c1.getCount();
        String now = "성공 " + text;
        //Toast.makeText(getApplicationContext(), now , Toast.LENGTH_LONG).show();

        for(int i=0; i< text; i++){
            c1.moveToNext();

            String latitude = c1.getString(1);
            String longitude = c1.getString(2);
            String date = c1.getString(3);
            String time = c1.getString(4);

            double latitude_d = Double.parseDouble(latitude);
            double longitude_d = Double.parseDouble(longitude);
            String address = getCurrentAddress(latitude_d, longitude_d);

            date = date_makeup(date);
            time = time_makeup(time);

            gpsList.add(new GPSListViewItem(date,time,latitude,longitude,address));
            //Toast.makeText(getApplicationContext(),latitude + ", " + longitude + "," + date + ",//" + time, Toast.LENGTH_SHORT).show();
        }
        c1.close();
        //gpsList.add(new GPSListViewItem("a","b","c","d","e"));
    }
    public String date_makeup(String date){
        String date_make;
        date_make = date.substring(0,4) + "-" + date.substring(4,6) + "-" + date.substring(6,8);
        return date_make;
    }
    public String time_makeup(String time){
        int size = time.length();
        String time_make;
        if(size == 6){
            time_make = time.substring(0,2) + ":" + time.substring(2,4) + ":" + time.substring(4,6);
        }
        else{
            time_make = time.substring(0,1) + ":" + time.substring(1,3) + ":" + time.substring(3,5);
        }
        return time_make;
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
    //주소 찾기
    public String getCurrentAddress( double latitude, double longitude) {

        //지오코더... GPS를 주소로 변환
        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());

        List<Address> addresses;

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 7);
        } catch (IOException ioException) {
            //네트워크 문제
            //Toast.makeText(this, "지오코더 서비스 사용불가", Toast.LENGTH_LONG).show();
            return "지오코더 서비스 사용불가";
        } catch (IllegalArgumentException illegalArgumentException) {
            //Toast.makeText(this, "잘못된 GPS 좌표", Toast.LENGTH_LONG).show();
            return "잘못된 GPS 좌표";

        }
        if (addresses == null || addresses.size() == 0) {
            //Toast.makeText(this, "주소 미발견", Toast.LENGTH_LONG).show();
            return "주소 미발견";
        }

        Address address = addresses.get(0);
        return address.getAddressLine(0).toString();
    }
    @Override
    public void onDestroy() {
        running = false;
        super.onDestroy();
    }
}
