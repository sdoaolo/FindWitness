package com.example.findwitness.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.findwitness.Item.GPSListViewItem;
import com.example.findwitness.R;

import java.util.ArrayList;

public class GPSListViewAdapter  extends BaseAdapter {
    Context mContext = null;
    LayoutInflater mLayoutInflater = null;
    ArrayList<GPSListViewItem> sample;

    public GPSListViewAdapter(Context context, ArrayList<GPSListViewItem> data) {
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
    public GPSListViewItem getItem(int position) {
        return sample.get(position);
    }

    @Override
    public View getView(int position, View converView, ViewGroup parent) {
        View view = mLayoutInflater.inflate(R.layout.gps_listview_item, null);

        TextView gps_date = (TextView)view.findViewById(R.id.gps_date);
        TextView gps_time = (TextView)view.findViewById(R.id.gps_time);
        TextView gps_latitude = (TextView)view.findViewById(R.id.gps_latitude);
        TextView gps_longitude = (TextView)view.findViewById(R.id.gps_longitude);
        TextView gps_address = (TextView)view.findViewById(R.id.gps_address);

        gps_date.setText(sample.get(position).getDate());
        gps_time.setText(sample.get(position).getTime());
        gps_latitude.setText(sample.get(position).getLatitude());
        gps_longitude.setText(sample.get(position).getLongitude());
        gps_address.setText(sample.get(position).getAddress());

        return view;
    }
}