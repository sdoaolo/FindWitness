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
import android.widget.Toast;

import androidx.fragment.app.Fragment;

public class MainSearchFragment extends Fragment {
    //데이터베이스 생성
    private SQLiteDatabase db;
    private GPSdatabaseHelper dbHelper;
    Button btnDate, btnTime;
    LinearLayout pickerLayout;
    DatePicker datePicker;
    TimePicker timePicker;
    TextView appliedText,appliedDateText,appliedTimeText;
    Button btnApply;
    int nHourDay, nMinute,mHour,mMinute,mSecond;
    Boolean isSearchDate = true;
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
        appliedText = view.findViewById(R.id.PickerText);
        btnApply = view.findViewById(R.id.search_apply_btn);


        appliedDateText = view.findViewById(R.id.appliedDateText);
        appliedTimeText = view.findViewById(R.id.appliedTimeText);

        datePicker.init(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(),
                new DatePicker.OnDateChangedListener() {
                    //값이 바뀔때마다 텍스트뷰의 값을 바꿔준다.
                    @Override
                    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // TODO Auto-generated method stub
                        //monthOfYear는 0값이 1월을 뜻하므로 1을 더해줌 나머지는 같다.
                        appliedDateText.setText(String.format("Aplied Date : %d-%d-%d", year,monthOfYear + 1, dayOfMonth));
                    }
                });
        /*
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);
        mSecond = c.get(Calendar.SECOND);
        timePicker.setOnTimeChangedListener(this);

        timePicker.init(mHour,mMinute, mSecond,
                new DatePicker.OnDateChangedListener() {
                    //값이 바뀔때마다 텍스트뷰의 값을 바꿔준다.
                    @Override
                    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // TODO Auto-generated method stub
                        //monthOfYear는 0값이 1월을 뜻하므로 1을 더해줌 나머지는 같다.
                        appliedDateText.setText(String.format("Aplied Time : %d:%d (%s)", year,monthOfYear + 1, dayOfMonth));
                    }
                });

        TimePickerDialog.OnTimeSetListener timeSetListenr = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                c.set(Calendar.HOUR_OF_DAY, hourOfDay);
                c.set(Calendar.MINUTE, minute);
            }
        };
        appliedTimeText.setText("Aplied Date : %d-%d-%d",hourOfDay+"시 "+minute+"분");
*/

        //날짜 시간 조회 기능 넣기 !
        btnDate = view.findViewById(R.id.search_date);
        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSearchDate = true;
                pickerLayout.setVisibility(View.VISIBLE);
                datePicker.setVisibility(View.VISIBLE);
                timePicker.setVisibility(View.GONE);
                btnApply.setVisibility(View.VISIBLE);
                appliedText.setVisibility(View.VISIBLE);
                appliedText.setText("날짜 조회");
            }
        });

        btnTime = view.findViewById(R.id.search_time);
        btnTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSearchDate = false;
                pickerLayout.setVisibility(View.VISIBLE);
                datePicker.setVisibility(View.GONE);
                timePicker.setVisibility(View.VISIBLE);
                btnApply.setVisibility(View.VISIBLE);
                appliedText.setVisibility(View.VISIBLE);
                appliedText.setText("시간 조회");
                timePicker.setIs24HourView(false);
                //timePicker.setOnTimeChangedListener(this);
            }
        });
        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isSearchDate){
                    String result = String.format("%d년 %d월 %d일", datePicker.getYear(), datePicker.getMonth() + 1, datePicker.getDayOfMonth());
                    Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();
                    pickerLayout.setVisibility(View.GONE);
                    datePicker.setVisibility(View.GONE);
                    timePicker.setVisibility(View.GONE);
                    btnApply.setVisibility(View.GONE);
                    appliedText.setVisibility(View.GONE);
                    appliedText.setText("날짜 조회");







                }else{

                }
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
