package com.example.findwitness;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
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

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainSearchFragment extends Fragment {
    //데이터베이스 생성
    private SQLiteDatabase db;
    String date, time, Search = "", num;

    Button btnDate, btnTime,btnApply,btnSearch ;
    LinearLayout pickerLayout;
    DatePicker datePicker;
    TimePicker timePicker;
    TextView appliedText,appliedDateText,appliedTimeText;
    Boolean isSearchDate = true,isAppliedDate=false,isAppliedTime = false;

    String timeResult, dateResult;



    //ArrayList<String> SearchResultList;

    public MainSearchFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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
        btnSearch = view.findViewById(R.id.btn_search);

        //SearchResultList = new ArrayList<String>(2);
        //SearchResultList[0]=dateResult;

        appliedDateText = view.findViewById(R.id.appliedDateText);
        appliedTimeText = view.findViewById(R.id.appliedTimeText);
        Date current = Calendar.getInstance().getTime();
        String dt = new SimpleDateFormat("yyyy-MM-dd",Locale.getDefault()).format((current));
        appliedDateText.setText("Aplied Date : "+dt);

        //데이터베이스 열기
        GPSdatabaseHelper dbHelper = new GPSdatabaseHelper(getActivity(),"mydb");
        db = dbHelper.getWritableDatabase();

        datePicker.init(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(),
                new DatePicker.OnDateChangedListener() {
                    //값이 바뀔때마다 텍스트뷰의 값을 바꿔준다.
                    @Override
                    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // TODO Auto-generated method stub
                        //monthOfYear는 0값이 1월을 뜻하므로 1을 더해줌 나머지는 같다.
                        appliedDateText.setText(String.format("Aplied Date : %d-%d-%d", year,monthOfYear + 1, dayOfMonth));
                        Log.d("eeee", appliedDateText.toString());
                    }
                });
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int hour, int min) {
                appliedTimeText.setText(String.format("Aplied Time : %d : %d ", hour,min));
                timeResult = String.format("%d : %d", hour,min);
                Log.d("eee", appliedTimeText.toString());
            }
        });

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
                timePicker.setIs24HourView(true);
                //timePicker.setOnTimeChangedListener(this);
            }
        });
        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isSearchDate){
                    dateResult = String.format("%d년 %d월 %d일", datePicker.getYear(), datePicker.getMonth() + 1, datePicker.getDayOfMonth());
                    Toast.makeText(getActivity(), dateResult, Toast.LENGTH_SHORT).show();
                    date = dateResult;
                    pickerLayout.setVisibility(View.GONE);
                    datePicker.setVisibility(View.GONE);
                    timePicker.setVisibility(View.GONE);
                    btnApply.setVisibility(View.GONE);
                    appliedText.setVisibility(View.GONE);
                    appliedText.setText("날짜 조회");
                    isAppliedDate = true;
                    checkApplied();
                }else{
                    Toast.makeText(getActivity(), timeResult, Toast.LENGTH_SHORT).show();
                    time = timeResult;
                    pickerLayout.setVisibility(View.GONE);
                    datePicker.setVisibility(View.GONE);
                    timePicker.setVisibility(View.GONE);
                    btnApply.setVisibility(View.GONE);
                    appliedText.setVisibility(View.GONE);
                    appliedText.setText("시간 조회");
                    isAppliedTime = true;
                    checkApplied();
                }
            }
        });


        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("ssssssssssss","데이터 보낼거임");
                search_gps();
                Bundle bundle = new Bundle();
                bundle.putString("SearchDate",dateResult);
                bundle.putString("SearchTime",timeResult);
                bundle.putString("SearchResult",Search);
                bundle.putString("SearchResult_num", num);
                ((MainActivity)getActivity()).mainSelectFragment.setArguments(bundle);
                Log.d("ssssssssssss","이동합니다!");
                ((MainActivity)getActivity()).replaceFragment(((MainActivity)getActivity()).mainSelectFragment);
                //fragment chat로 화면 전환 ㅇㅇ
                //search_gps();
            }

        });
    }

    public void checkApplied(){
        if(isAppliedDate && isAppliedTime){
            btnSearch.setVisibility(View.VISIBLE);
        }else{
            btnSearch.setVisibility((View.GONE));
        }
    }
    public void search_gps(){
        String date_temp[] = date.split(" ");
        String time_temp[] = time.split(" : ");
        date = date_temp[0].substring(0,4);
        if(date_temp[1].length() > 2) {
            date += date_temp[1].substring(0,2);
        }
        else {
            date += "0" + date_temp[1].substring(0,1);
        }
        if(date_temp[2].length() > 2) {
            date += date_temp[2].substring(0,2);
        }
        else {
            date += "0" + date_temp[2].substring(0,1);
        }

        if(time_temp[0].length() == 1) {
            time = "0" + time_temp[0];
        }
        else {
            time = time_temp[0];
        }
        if(time_temp[1].length() == 1) {
            time += "0" + time_temp[1];
        }
        else {
            time += time_temp[1];
        }
        time = time + "%";
        Log.d("변환","date : " + date + " time  :" + time);

        //데이터베이스 조회
        Cursor c1 = db.rawQuery("select DISTINCT LATITUDE,LONGITUDE from gps where _DATE = '" +
                date + "' AND _TIME LIKE '" + time + "';", null);
        int number = c1.getCount();
        num = Integer.toString(number);
        for(int i=0; i< number; i++){
            c1.moveToNext();
            String latitude = c1.getString(0);
            String longitude = c1.getString(1);
            String address = getCurrentAddress(Double.parseDouble(latitude),Double.parseDouble(longitude));
            //Log.d("데이터 : ","latitude : " + latitude + "longitude : " + longitude);
            Search += "|" + latitude + "#" + longitude + "#" + address;
        }
        if(number != 0) {
            Search = Search.substring(1,Search.length());
        }

        Log.d("개수", "" + number);
        Log.d("서치",Search);
        c1.close();
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
}
