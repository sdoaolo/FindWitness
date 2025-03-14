package com.example.findwitness;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.findwitness.Adapter.GPSListViewAdapter;
import com.example.findwitness.Item.GPSListViewItem;
import com.example.findwitness.UI.CustomProgressDialog;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainSelectFragment extends Fragment {

    List<GPSListViewItem> selectGpsList;
    Boolean isDataExist;
    private CustomProgressDialog customProgressDialog;

    Server_handler handler = new Server_handler();
    double latitude = 0.0, longitude = 0.0;
    String timeResult, dateResult,Search="",num;
    public MainSelectFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main_select, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        GPSTracker gpsTracker = new GPSTracker(getActivity());
        gpsTracker.getLocation();

        latitude = gpsTracker.getLatitude();
        longitude = gpsTracker.getLongitude();
        TextView currentGps = view.findViewById(R.id.currentGpsTextView);
        currentGps.setText("current gps : "+latitude+", "+longitude);

        Bundle bundle = getArguments();
        if(bundle != null){
            dateResult = bundle.getString("SearchDate");
            timeResult = bundle.getString("SearchTime");
            Search =  bundle.getString("SearchResult");
            num = bundle.getString("SearchResult_num");
        }

        this.InitializeGpsData();

        final ListView listView = view.findViewById(R.id.select_gps_list);
        final GPSListViewAdapter myAdapter = new GPSListViewAdapter(getActivity(),selectGpsList);

        listView.setAdapter(myAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id){

                customProgressDialog = new CustomProgressDialog(getActivity());
                // 위에서 테두리를 둥글게 했지만 다이얼로그 자체가 네모라 사각형 여백이 보입니다. 아래 코드로 다이얼로그 배경을 투명처리합니다.
                customProgressDialog .getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                customProgressDialog.show(); // 보여주기

                if(isDataExist) {
                    String temp_data = data_format(selectGpsList.get(position).getLongitude(),selectGpsList.get(position).getLatitude(),timeResult,dateResult);

                    Thread thread = new Network_server_Thread(temp_data);
                    thread.start();

                }

        }
        });

    }
    public String data_format(String longtit, String latit, String time_s, String date_s){
        String result = "";

        String date_temp[] = date_s.split(" ");
        String time_temp[] = time_s.split(" : ");
        date_s = date_temp[0].substring(0,4);
        if(date_temp[1].length() > 2) {date_s += date_temp[1].substring(0,2);}
        else {date_s += "0" + date_temp[1].substring(0,1);}
        if(date_temp[2].length() > 2) {date_s += date_temp[2].substring(0,2);}
        else {date_s += "0" + date_temp[2].substring(0,1);}

        if(time_temp[0].length() == 1) {time_s = "0" + time_temp[0];}
        else {time_s = time_temp[0];}
        if(time_temp[1].length() == 1) {time_s += "0" + time_temp[1];}
        else {time_s += time_temp[1];}

        result = String_to_seven(longtit) + "," + String_to_seven(latit) + "," + date_s + "," + time_s;

        return result;
    }
    public String String_to_seven(String str){
        String temp[] = str.split("\\."), zero = "0000000", ans;
        int num = temp[1].length();
        if(num > 7) {
            temp[1] = temp[1].substring(0,7);
        } else if(num < 7) {
            temp[1] += zero.substring(0,7-num);
        }
        ans = temp[0] + "." + temp[1];
        return ans;
    }
    public void InitializeGpsData()
    {
        selectGpsList = new ArrayList<GPSListViewItem>();

        if(!Search.equals("")){
            isDataExist = true;
            String search_ary[] = Search.split("\\|");
            int search_num = Integer.parseInt(num);
            for(int i=0; i<search_num; i++) {
                String record[] = search_ary[i].split("#");
                selectGpsList.add(new GPSListViewItem(dateResult, timeResult, record[0], record[1], record[2]));
            }
        }
        else {
            isDataExist = false;
            selectGpsList.add(new GPSListViewItem("해당 날짜와 시간에 gps 기록이 없습니다.", "", "", "", ""));
        }
    }
    class Network_server_Thread extends Thread{
        String server_data;
        public Network_server_Thread(String data){
            this.server_data = data;
        }
        @Override
        public void run() {// longitude, latitude, date, time

            String server_gps[] = this.server_data.split(",");

            Message message = handler.obtainMessage();
            Bundle bundle = new Bundle();

            try {
                URL url = new URL("http://192.168.0.4:8080/servelet/login");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                if (conn != null) {

                    conn.setConnectTimeout(10000);
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);

                    String sendMsg = "long=" + server_gps[0] + "&date=" + server_gps[2] + "&lati="
                            + server_gps[1] + "&time=" + server_gps[3] + "&save=0";

                    OutputStream os = conn.getOutputStream();
                    os.write(sendMsg.getBytes("utf-8"));

                    os.flush();
                    os.close();
                    int resCode = conn.getResponseCode();

                    if (resCode == HttpURLConnection.HTTP_OK)
                    {

                        InputStream is = conn.getInputStream();
                        InputStreamReader isr = new InputStreamReader(is);
                        BufferedReader br = new BufferedReader(isr);
                        StringBuilder strBuilder = new StringBuilder();
                        String line = null;

                        while ( !(line = br.readLine()).equals(null)) {
                            line = line + "\n";
                            strBuilder.append(line);
                        }
                        String temp = strBuilder.toString();

                        bundle.putString("account_list", "");
                        message.setData(bundle);
                        handler.sendMessage(message);

                        is.close();
                        isr.close();
                        br.close();
                        conn.disconnect();
                    }
                }
            } catch (Exception ex) {
                bundle.putString("account_list", "");
                message.setData(bundle);
                handler.sendMessage(message);
            }
        }
    }

    class Server_handler extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            Bundle message = msg.getData();
            String account = message.getString("account_list");

            customProgressDialog.dismiss();
            Bundle bundle = new Bundle();
            bundle.putString("SearchDate",dateResult);
            bundle.putString("SearchTime",timeResult);
            bundle.putString("requireServer",account);
            ((MainActivity)getActivity()).mainChatFragment.setArguments(bundle);
            ((MainActivity)getActivity()).replaceFragment(((MainActivity)getActivity()).mainChatFragment);
        }
    }

}
