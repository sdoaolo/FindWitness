package com.example.findwitness.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.findwitness.Item.AccidentListViewItem;
import com.example.findwitness.R;

import java.util.ArrayList;

public class AccidentListViewAdapter  extends BaseAdapter {
    Context mContext = null;
    LayoutInflater mLayoutInflater = null;
    ArrayList<AccidentListViewItem> sample;

    public AccidentListViewAdapter(Context context, ArrayList<AccidentListViewItem> data) {
        mContext = context;
        sample = data;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return sample.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public AccidentListViewItem getItem(int position) {
        return sample.get(position);
    }

    @Override
    public View getView(int position, View converView, ViewGroup parent) {
        View view = mLayoutInflater.inflate(R.layout.statistics_listview_item, null);

        TextView accident_year = (TextView)view.findViewById(R.id.accident_year);
        TextView accident_num = (TextView)view.findViewById(R.id.accident_num);
        TextView accident_died = (TextView)view.findViewById(R.id.accident_died);
        TextView accident_hurt = (TextView)view.findViewById(R.id.accident_hurt);

        accident_year.setText(sample.get(position).getYear());
        accident_num.setText(sample.get(position).getNum());
        accident_died.setText(sample.get(position).getDied());
        accident_hurt.setText(sample.get(position).getHurt());

        return view;
    }
}