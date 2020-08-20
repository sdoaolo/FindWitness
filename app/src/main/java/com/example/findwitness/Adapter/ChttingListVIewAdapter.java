package com.example.findwitness.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.findwitness.Item.ChattingListViewItem;
import com.example.findwitness.R;

import java.util.List;

public class ChttingListVIewAdapter  extends BaseAdapter {
    Context mContext = null;
    LayoutInflater mLayoutInflater = null;
    List<ChattingListViewItem> sample;

    public ChttingListVIewAdapter(Context context, List<ChattingListViewItem> data) {
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
    public ChattingListViewItem getItem(int position) {
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