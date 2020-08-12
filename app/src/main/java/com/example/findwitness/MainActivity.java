package com.example.findwitness;

import android.graphics.Color;
import android.os.Bundle;
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
    Button btn_gps;
    FragmentManager fragmentManager = getSupportFragmentManager();
    MainSearchFragment mainSearchFragment;
    MainChatFragment mainChatFragment;
    MainGPSFragment mainGPSFragment;
    String strWhite = "#FFFFFF";
    String strBlue = "#78A2DB";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        btn_search = findViewById(R.id.main_btn_search);
        btn_chat = findViewById(R.id.main_btn_chat);
        btn_gps = findViewById(R.id.main_btn_gps);

        mainSearchFragment = new MainSearchFragment();
        mainChatFragment = new MainChatFragment();
        mainGPSFragment = new MainGPSFragment();

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.main_frame, mainSearchFragment); //초기화면 설정
        fragmentTransaction.commit();
    }
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.main_btn_search:
                btn_search.setBackgroundResource(R.drawable.main_selected_chat);
                btn_chat.setBackgroundResource(R.drawable.main_chat);
                btn_gps.setBackgroundResource(R.drawable.main_white_back);
                btn_gps.setTextColor(Color.parseColor(strBlue));
                replaceFragment(mainSearchFragment);
                break;
            case R.id.main_btn_chat:
                btn_search.setBackgroundResource(R.drawable.main_search);
                btn_chat.setBackgroundResource(R.drawable.main_selected_chat);
                btn_gps.setBackgroundResource(R.drawable.main_white_back);
                btn_gps.setTextColor(Color.parseColor(strBlue));
                replaceFragment(mainChatFragment);
                break;
            case R.id.main_btn_gps:
                btn_search.setBackgroundResource(R.drawable.main_search);
                btn_chat.setBackgroundResource(R.drawable.main_chat);
                btn_gps.setBackgroundResource(R.drawable.main_blue_back);
                btn_gps.setTextColor(Color.parseColor(strWhite));
                replaceFragment(mainGPSFragment);
                break;
        }
    }
    public void replaceFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_frame, fragment).commit();      // Fragment로 사용할 MainActivity내의 layout공간을 선택합니다.
    }
}
