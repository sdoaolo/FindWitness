package com.example.findwitness;

import android.database.Cursor;
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

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainSelectFragment extends Fragment {

    ArrayList<GPSListViewItem> selectGpsList;

    //핸들러
    Server_handler handler = new Server_handler();
    double latitude = 0.0, longitude = 0.0;
    String timeResult, dateResult,Search="",num;
    public MainSelectFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_select, container, false);
        return inflater.inflate(R.layout.fragment_main_select, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        GPSTracker gpsTracker = new GPSTracker(getActivity());

        latitude = gpsTracker.getLatitude();
        longitude = gpsTracker.getLongitude();
        TextView currentGps = view.findViewById(R.id.currentGpsTextView);
        currentGps.setText("current gps : "+latitude+", "+longitude);

        Log.d("ssssssssssss","들어왔어!");
        Bundle bundle = getArguments();  //번들 받기. getArguments() 메소드로 받음.
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
                Log.d("ssssssssssss","fragment chat로 화면 전환");
                Log.d("ssssssssssss","item clicked: " + selectGpsList.get(position).getAddress());
                String temp_data = data_format("37.1234543","-127.43223313",timeResult,dateResult);

                //서버와 통신
                Thread thread = new Network_server_Thread(temp_data);
                thread.start();

                Bundle bundle = new Bundle();
                bundle.putString("SearchDate",dateResult);
                bundle.putString("SearchTime",timeResult);
                bundle.putString("requireServer","10:suin,4:girmf");
                ((MainActivity)getActivity()).mainChatFragment.setArguments(bundle);
                ((MainActivity)getActivity()).replaceFragment(((MainActivity)getActivity()).mainChatFragment);



        }
        });

    }
    public String data_format(String longtit, String latit, String time_s, String date_s){
        String result = "";

        String date_temp[] = date_s.split(" ");
        String time_temp[] = time_s.split(" : ");
        date_s = date_temp[0].substring(0,4);
        if(date_temp[1].length() > 2) date_s += date_temp[1].substring(0,2);
        else date_s += "0" + date_temp[1].substring(0,1);
        if(date_temp[2].length() > 2) date_s += date_temp[2].substring(0,2);
        else date_s += "0" + date_temp[2].substring(0,1);

        if(time_temp[0].length() == 1) time_s = "0" + time_temp[0];
        else time_s = time_temp[0];
        if(time_temp[1].length() == 1) time_s += "0" + time_temp[1];
        else time_s += time_temp[1];

        result = String_to_seven(longtit) + "," + String_to_seven(latit) + "," + date_s + "," + time_s;

        return result;
    }
    public String String_to_seven(String str){
        String temp[] = str.split("\\."), zero = "0000000", ans;
        int num = temp[1].length();
        if(num > 7) temp[1] = temp[1].substring(0,7);
        else if(num < 7) temp[1] += zero.substring(0,7-num);
        ans = temp[0] + "." + temp[1];
        return ans;
    }
    public void InitializeGpsData()
    {
        selectGpsList = new ArrayList<GPSListViewItem>();
        if(Search != ""){
            String search_ary[] = Search.split("\\|");
            int search_num = Integer.parseInt(num);
            for(int i=0; i<search_num; i++) {
                String record[] = search_ary[i].split("#");
                selectGpsList.add(new GPSListViewItem(dateResult, timeResult, record[0], record[1], record[2]));
            }
        }
        else {    // 결과가 없을 경우
            selectGpsList.add(new GPSListViewItem("200818", "12:22", "123", "235", "ddd"));
            selectGpsList.add(new GPSListViewItem("200818", "12:22", "125", "234", "aaaa"));
            selectGpsList.add(new GPSListViewItem("200818", "12:22", "123", "235", "ccc"));
            selectGpsList.add(new GPSListViewItem("200818", "12:22", "125", "234", "dddd"));
        }
    }
    class Network_server_Thread extends Thread{
        String server_data;
        public Network_server_Thread(String data){
            this.server_data = data;
        }
        @Override
        public void run() {// longitude, latitude, date, time
            Log.d("네트워크 쓰레드 시작", "개수 : ");
            String server_gps[] = this.server_data.split(",");
            try {
                URL url = new URL("http://192.168.0.4:8080/servelet/login");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                Log.i("쓰레드", "접속시도");
                if (conn != null) {
                    Log.i("쓰레드", "접속성공");
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
                    Log.i("응답코드", "" + resCode);
                    if (resCode == HttpURLConnection.HTTP_OK) {
                        Log.i("쓰레드", "응답수신");
                        InputStream is = conn.getInputStream();
                        InputStreamReader isr = new InputStreamReader(is);
                        BufferedReader br = new BufferedReader(isr);
                        StringBuilder strBuilder = new StringBuilder();
                        String line = null;
                        while ((line = br.readLine()) != null) {
                            strBuilder.append(line + "\n");
                        }
                        String temp = strBuilder.toString();
                        //서버에 응답받은 것을 핸들러로 전달
                        Message message = handler.obtainMessage();
                        Bundle bundle = new Bundle();
                        bundle.putString("account_list", temp);
                        message.setData(bundle);
                        handler.sendMessage(message);

                        Log.d("응답", temp);
                        br.close();
                        conn.disconnect();
                    }
                    Log.i("쓰레드", "응답처리완료");
                }
            } catch (Exception ex) {
                Log.e("접속요류", "" + ex);
            }
        }
    }
    class Server_handler extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            Bundle message = msg.getData();
            String account = message.getString("account_list");
            Log.d("서버에서 회원정보 받음", account);
            /*
            Bundle bundle = new Bundle();
            bundle.putString("SearchDate",dateResult);
            bundle.putString("SearchTime",timeResult);
            bundle.putString("requireServer",account);
            ((MainActivity)getActivity()).mainChatFragment.setArguments(bundle);
            ((MainActivity)getActivity()).replaceFragment(((MainActivity)getActivity()).mainChatFragment);
            */

        }
    }

}
