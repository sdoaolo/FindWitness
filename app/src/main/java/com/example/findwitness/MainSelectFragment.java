package com.example.findwitness;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.findwitness.Adapter.GPSListViewAdapter;
import com.example.findwitness.Item.GPSListViewItem;

import java.util.ArrayList;

public class MainSelectFragment extends Fragment {

    ArrayList<GPSListViewItem> selectGpsList;


    String latitude="123", longtitude="456";
    String timeResult, dateResult,Search;
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
        Log.d("ssssssssssss","들어왔어!");
        Bundle bundle = getArguments();  //번들 받기. getArguments() 메소드로 받음.
        if(bundle != null){
            dateResult = bundle.getString("SearchDate");
            timeResult = bundle.getString("SearchTime");
            Search =  bundle.getString("SearchResult");
        }
        TextView currentGps = view.findViewById(R.id.currentGpsTextView);
        currentGps.setText("current gps : "+latitude+", "+longtitude);

        this.InitializeGpsData();

        final ListView listView = view.findViewById(R.id.select_gps_list);
        final GPSListViewAdapter myAdapter = new GPSListViewAdapter(getActivity(),selectGpsList);

        listView.setAdapter(myAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id){
                Log.d("ssssssssssss","fragment chat로 화면 전환");
                Log.d("ssssssssssss","item clicked: " + selectGpsList.get(position).getAddress());
                Bundle bundle = new Bundle();

                bundle.putString("SearchDate",dateResult);
                bundle.putString("SearchTime",timeResult);
                bundle.putString("requireServer","requireServer");
                ((MainActivity)getActivity()).mainChatFragment.setArguments(bundle);
                ((MainActivity)getActivity()).replaceFragment(((MainActivity)getActivity()).mainChatFragment);
        }
        });

    }
    public void InitializeGpsData()
    {
        selectGpsList = new ArrayList<GPSListViewItem>();
        selectGpsList.add(new GPSListViewItem("200818","12:22","123","235","ddd"));
        selectGpsList.add(new GPSListViewItem("200818","12:22","125","234","aaaa"));
        selectGpsList.add(new GPSListViewItem("200818","12:22","123","235","ccc"));
        selectGpsList.add(new GPSListViewItem("200818","12:22","125","234","dddd"));
    }
}
