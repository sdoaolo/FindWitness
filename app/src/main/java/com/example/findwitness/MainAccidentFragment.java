package com.example.findwitness;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.example.findwitness.Adapter.AccidentListViewAdapter;
import com.example.findwitness.Item.AccidentListViewItem;

import java.util.ArrayList;

public class MainAccidentFragment  extends Fragment {
    ArrayList<AccidentListViewItem> accidentList;

    public MainAccidentFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_accident, container, false);
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
        accidentList.add(new AccidentListViewItem("","","",""));

    }
}
