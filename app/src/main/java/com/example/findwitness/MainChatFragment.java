package com.example.findwitness;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.findwitness.Adapter.ChatListViewAdapter;
import com.example.findwitness.Chat.ChatActivity;
import com.example.findwitness.Item.ChatListViewItem;

import java.util.ArrayList;
import java.util.List;


public class MainChatFragment extends Fragment {
    String SearchInfoDate , SearchInfoTime,checkBundle;
    String nickname[], id[];
    List<ChatListViewItem> chatList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main_chat, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView recent_list_date = view.findViewById(R.id.recent_list_date);
        TextView recent_list_time = view.findViewById(R.id.recent_list_time);
        Bundle bundle = getArguments();
        if(bundle != null){
            SearchInfoDate = bundle.getString("SearchDate");
            SearchInfoTime = bundle.getString("SearchTime");
            checkBundle = bundle.getString("requireServer");
            if(checkBundle != "" ) nickname_id_find(checkBundle);

            ((MainActivity)getActivity()).btn_search.setBackgroundResource(R.drawable.main_search);
            ((MainActivity)getActivity()).btn_list.setBackgroundResource(R.drawable.main_blue_back);
            ((MainActivity)getActivity()).btn_list.setTextColor(Color.parseColor(((MainActivity)getActivity()).strWhite));
            recent_list_date.setText(SearchInfoDate);
            recent_list_time.setText(SearchInfoTime);

            ListView listView = view.findViewById(R.id.recent_list);
            final ChatListViewAdapter myAdapter = new ChatListViewAdapter(getActivity(),chatList);

            listView.setAdapter(myAdapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                @Override
                public void onItemClick(AdapterView parent, View v, int position, long id){

                    ChatActivity chatActivity = new ChatActivity();
                    Intent intent = new Intent(getActivity(),chatActivity.getClass());

                    ArrayList<String> userInfo = new ArrayList<String>(2);
                    userInfo.add(chatList.get(position).getUserName());
                    userInfo.add(chatList.get(position).getUserId());
                    intent.putExtra("userinfo",userInfo);
                    startActivity(intent);
                }
            });
        }
    }
    public void InitializeMovieData(int size, String[] nicknames, String[]ids)
    {
        chatList = new ArrayList<ChatListViewItem>();
        for(int i = 0 ;i<size;i++){
            chatList.add(new ChatListViewItem(nicknames[i],ids[i]));
        }
    }
    public void nickname_id_find(String data){
        String[] list = data.split(",");
        int size = list.length;
        nickname = new String[size];
        id = new String[size];
        for(int i=0; i<size; i++){
            String temp_list[] = list[i].split(":");
            id[i] = temp_list[0];
            nickname[i] = temp_list[1];
        }
        this.InitializeMovieData(size,nickname,id);
    }
}

