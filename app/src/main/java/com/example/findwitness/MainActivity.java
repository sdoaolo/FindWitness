package com.example.findwitness;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity {
    ImageButton btn_search;
    ImageButton btn_chat;
    Button btn_gps, btn_list, btn_accident;
    FragmentManager fragmentManager = getSupportFragmentManager();
    MainSearchFragment mainSearchFragment;
    MainChatFragment mainChatFragment;
    MainGPSFragment mainGPSFragment;
    MainChattingFragment mainChattingFragment;
    MainSelectFragment mainSelectFragment;
    MainAccidentFragment mainAccidentFragment;

    String strWhite = "#FFFFFF";
    String strBlue = "#78A2DB";
    int priNum;
    String userNickName;
    //private GpsTracker gpsTracker;
    boolean running;
    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    String[] REQUIRED_PERMISSIONS  = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
    FragmentTransaction fragmentTransaction;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_main);
        btn_search = findViewById(R.id.main_btn_search);
        btn_chat = findViewById(R.id.main_btn_chat);
        btn_gps = findViewById(R.id.main_btn_gps);
        btn_list = findViewById(R.id.main_btn_list);
        btn_accident = findViewById(R.id.main_btn_accident);

        mainSearchFragment = new MainSearchFragment();
        mainChatFragment = new MainChatFragment();
        mainGPSFragment = new MainGPSFragment();
        mainChattingFragment = new MainChattingFragment();
        mainSelectFragment = new MainSelectFragment();
        mainAccidentFragment = new MainAccidentFragment();

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.main_frame, mainSearchFragment); //초기화면 설정
        fragmentTransaction.commit();

        /////////////////////
        Intent intent = getIntent();
        int prinum_main = intent.getIntExtra("pri_num", 0);
        String nickname = intent.getStringExtra("nickname");

        priNum = prinum_main;
        userNickName = nickname;

        Log.d("hhhhhhhhhhhhhh","priNum: "+priNum);
        Log.d("hhhhhhhhhhhhhh","userNickName: "+userNickName);


    }
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.main_btn_search:
                btn_search.setBackgroundResource(R.drawable.main_selected_search);
                btn_chat.setBackgroundResource(R.drawable.main_chat);
                btn_gps.setBackgroundResource(R.drawable.main_white_back);
                btn_gps.setTextColor(Color.parseColor(strBlue));
                btn_list.setBackgroundResource(R.drawable.main_white_back);
                btn_list.setTextColor(Color.parseColor(strBlue));
                btn_accident.setBackgroundResource(R.drawable.main_white_back);
                btn_accident.setTextColor(Color.parseColor(strBlue));
                replaceFragment(mainSearchFragment);
                break;
            case R.id.main_btn_list: //list
                btn_search.setBackgroundResource(R.drawable.main_search);
                btn_chat.setBackgroundResource(R.drawable.main_chat);
                btn_gps.setBackgroundResource(R.drawable.main_white_back);
                btn_gps.setTextColor(Color.parseColor(strBlue));
                btn_list.setBackgroundResource(R.drawable.main_blue_back);
                btn_list.setTextColor(Color.parseColor(strWhite));
                btn_accident.setBackgroundResource(R.drawable.main_white_back);
                btn_accident.setTextColor(Color.parseColor(strBlue));
                replaceFragment(mainChatFragment);
                break;
            case R.id.main_btn_chat: //chatting
                btn_search.setBackgroundResource(R.drawable.main_search);
                btn_chat.setBackgroundResource(R.drawable.main_selected_chat);
                btn_gps.setBackgroundResource(R.drawable.main_white_back);
                btn_gps.setTextColor(Color.parseColor(strBlue));
                btn_list.setBackgroundResource(R.drawable.main_white_back);
                btn_list.setTextColor(Color.parseColor(strBlue));
                btn_accident.setBackgroundResource(R.drawable.main_white_back);
                btn_accident.setTextColor(Color.parseColor(strBlue));
                replaceFragment(mainChattingFragment);
                break;
            case R.id.main_btn_gps:
                btn_search.setBackgroundResource(R.drawable.main_search);
                btn_chat.setBackgroundResource(R.drawable.main_chat);
                btn_gps.setBackgroundResource(R.drawable.main_blue_back);
                btn_gps.setTextColor(Color.parseColor(strWhite));
                btn_list.setBackgroundResource(R.drawable.main_white_back);
                btn_list.setTextColor(Color.parseColor(strBlue));
                btn_accident.setBackgroundResource(R.drawable.main_white_back);
                btn_accident.setTextColor(Color.parseColor(strBlue));
                replaceFragment(mainGPSFragment);
                break;
            case R.id.main_btn_accident:
                btn_search.setBackgroundResource(R.drawable.main_search);
                btn_chat.setBackgroundResource(R.drawable.main_chat);
                btn_gps.setBackgroundResource(R.drawable.main_white_back);
                btn_gps.setTextColor(Color.parseColor(strBlue));
                btn_list.setBackgroundResource(R.drawable.main_white_back);
                btn_list.setTextColor(Color.parseColor(strBlue));
                btn_accident.setBackgroundResource(R.drawable.main_blue_back);
                btn_accident.setTextColor(Color.parseColor(strWhite));
                replaceFragment(mainAccidentFragment);
                break;
        }
    }
    public void replaceFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_frame, fragment).commit();      // Fragment로 사용할 MainActivity내의 layout공간을 선택합니다.
    }

}
