package com.example.findwitness;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.example.findwitness.Adapter.AccidentListViewAdapter;
import com.example.findwitness.Item.AccidentListViewItem;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MainAccidentFragment  extends Fragment {
    List<AccidentListViewItem> accidentList;
    String [] traffic_accident_array;

    public MainAccidentFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main_accident, container, false);
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.InitializeData();
        ListView listView = view.findViewById(R.id.statistics_listView);
        final AccidentListViewAdapter adapter;
        adapter = new AccidentListViewAdapter(getActivity(),accidentList);
        listView.setAdapter(adapter);

    }
    public void InitializeData()
    {
        accidentList = new ArrayList<AccidentListViewItem>();

        loadItemsFromFile();

        for(int i = 0; i < traffic_accident_array.length; i++){
            accidentList.add(new AccidentListViewItem(traffic_accident_array[i]));
        }
    }

    private void loadItemsFromFile(){
        InputStream is = getResources().openRawResource(R.raw.accident);
        try {
            byte[] readStr = new byte[is.available()];
            is.read(readStr);
            is.close();
            String accident_data = new String(readStr);
            traffic_accident_array = accident_data.split(";");
            for(int i = 0; i < traffic_accident_array.length; i++){
                Log.d("kk", "trraffkc_accident_array["+i+"] : "+traffic_accident_array[i]);
            }
        } catch (IOException e) {
            Log.e("IOException error", e.getMessage());

        }

    }
}
