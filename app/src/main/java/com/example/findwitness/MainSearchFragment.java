package com.example.findwitness;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.fragment.app.Fragment;

public class MainSearchFragment extends Fragment {
    //데이터베이스 생성
    private SQLiteDatabase db;
    private GPSdatabaseHelper dbHelper;
    Button btnDate, btnTime;
    FrameLayout pickerLayout;
    public MainSearchFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_search, container, false);
        return inflater.inflate(R.layout.fragment_main_search, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pickerLayout = view.findViewById(R.id.pickerLayout);


        //날짜 시간 조회 기능 넣기 !
        btnDate = view.findViewById(R.id.search_date);
        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickerLayout.setVisibility(View.VISIBLE);
            }
        });

        btnTime = view.findViewById(R.id.search_time);
        btnTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickerLayout.setVisibility(View.VISIBLE);
            }
        });




    }
}
