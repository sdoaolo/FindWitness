package com.example.findwitness;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.fragment.app.Fragment;

public class MainSearchFragment extends Fragment {
    //데이터베이스 생성
    private SQLiteDatabase db;
    private GPSdatabaseHelper dbHelper;
    Button btnDate, btnTime;
    LinearLayout pickerLayout;
    DatePicker datePicker;
    TimePicker timePicker;
    TextView PickerText;
    int nHourDay, nMinute;

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
        datePicker = view.findViewById(R.id.datePicker);
        timePicker = view.findViewById(R.id.timePicker);
        PickerText = view.findViewById(R.id.PickerText);
        //날짜 시간 조회 기능 넣기 !
        btnDate = view.findViewById(R.id.search_date);
        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickerLayout.setVisibility(View.VISIBLE);
                datePicker.setVisibility(View.VISIBLE);
                timePicker.setVisibility(View.GONE);
                PickerText.setVisibility(View.VISIBLE);
                PickerText.setText("날짜 검색");
            }
        });

        btnTime = view.findViewById(R.id.search_time);
        btnTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickerLayout.setVisibility(View.VISIBLE);
                datePicker.setVisibility(View.GONE);
                timePicker.setVisibility(View.VISIBLE);
                PickerText.setVisibility(View.VISIBLE);
                PickerText.setText("시간 검색");
                timePicker.setIs24HourView(false);
                //timePicker.setOnTimeChangedListener(this);
            }
        });
    }


    /*

    @Override
    public void onTimeChanged(TimePicker timePicker, int hourOfDay, int minute){
        nHourDay = hourOfDay;
        nMinute = minute;
    }

    */
}
