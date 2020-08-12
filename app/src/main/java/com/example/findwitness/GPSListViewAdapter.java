package com.example.findwitness;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class GPSListViewAdapter  extends BaseAdapter {
    LayoutInflater inflater = null;
    private ArrayList<GPSListViewItem> m_oData = null;
    private int nListCnt = 0;

    public GPSListViewAdapter(ArrayList<GPSListViewItem> _oData)
    {
        m_oData = _oData;
        nListCnt = m_oData.size();
    }

    @Override
    public int getCount()
    {
        Log.i("TAG", "getCount");
        return nListCnt;
    }

    @Override
    public Object getItem(int position)
    {
        return null;
    }

    @Override
    public long getItemId(int position)
    {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        if (convertView == null)
        {
            final Context context = parent.getContext();
            if (inflater == null)
            {
                inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            }
            convertView = inflater.inflate(R.layout.gps_listview_item, parent, false);
        }

        TextView date = (TextView) convertView.findViewById(R.id.gps_date);
        TextView time = (TextView) convertView.findViewById(R.id.gps_time);
        TextView latitude = (TextView) convertView.findViewById(R.id.gps_latitude);
        TextView longitude = (TextView) convertView.findViewById(R.id.gps_longitude);
/*
        date.setText(m_oData.get(position).strTitle);
        time.setText(m_oData.get(position).strDate);
        latitude.setText(m_oData.get(position).strTitle);
        longitude.setText(m_oData.get(position).strDate);*/
        return convertView;
    }

    public void addItem(String text1, String text2) {
   /*     ListViewItem item = new ListViewItem();
        item.setRowtext1(text1);
        item.setRowtext2(text2);

        listViewItemList.add(item);*/
    }

    public void clearItem(){
     //   listViewItemList.clear();
    }
}