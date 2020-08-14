package com.example.findwitness;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ChttingListVIewAdapter  extends BaseAdapter {
    Context mContext = null;
    LayoutInflater mLayoutInflater = null;
    ArrayList<com.example.findwitness.ChattingListViewItem> sample;

    public ChttingListVIewAdapter(Context context, ArrayList<com.example.findwitness.ChattingListViewItem> data) {
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
    public com.example.findwitness.ChattingListViewItem getItem(int position) {
        return sample.get(position);
    }

    @Override
    public View getView(int position, View converView, ViewGroup parent) {
        View view = mLayoutInflater.inflate(R.layout.chatting_list_item, null);
        TextView chatting_user_name = (TextView)view.findViewById(R.id.chatting_user_name);
        TextView chatting_context_last = (TextView)view.findViewById(R.id.chatting_context_last);
        TextView chatting_last_time = (TextView)view.findViewById(R.id.chatting_last_time);
        TextView chatting_num = (TextView)view.findViewById(R.id.chatting_num);

        chatting_user_name.setText(sample.get(position).getNickName());
        chatting_context_last.setText(sample.get(position).getContext());
        chatting_last_time.setText(sample.get(position).getTime());
        chatting_num.setText(sample.get(position).getChatting_num());
        return view;
    }
}