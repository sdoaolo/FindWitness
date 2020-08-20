package com.example.findwitness.Adapter;

        import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.findwitness.Item.ChatListViewItem;
import com.example.findwitness.R;

import java.util.List;

public class ChatListViewAdapter  extends BaseAdapter {
    Context mContext = null;
    LayoutInflater mLayoutInflater = null;
    List<ChatListViewItem> sample;

    public ChatListViewAdapter(Context context, List<ChatListViewItem> data) {
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
    public ChatListViewItem getItem(int position) {
        return sample.get(position);
    }

    @Override
    public View getView(int position, View converView, ViewGroup parent) {
        View view = mLayoutInflater.inflate(R.layout.chat_list_item, null);

        TextView chat_context = (TextView)view.findViewById(R.id.chat_list_user_name);

        chat_context.setText(sample.get(position).getUserName());

        return view;
    }
}