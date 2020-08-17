package com.example.findwitness;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.example.findwitness.Chat.ChatActivity;

import java.util.ArrayList;

public class MainChattingFragment extends Fragment {
    ArrayList<ChattingListViewItem> chattingList;
    public MainChattingFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_chatting, container, false);
        Log.d("LLLLLLLLLL","Im in Chatting Fragment : onCreate View");
        return inflater.inflate(R.layout.fragment_main_chatting, container, false);
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d("LLLLLLLLLL","Im in Chatting Fragment : on View Created");
        //listview
        this.InitializeMovieData();

        ListView listView = view.findViewById(R.id.recent_list);
        final ChttingListVIewAdapter myAdapter = new ChttingListVIewAdapter(getActivity(),chattingList);

        listView.setAdapter(myAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id){
                Log.d("LLLLLLLLLL","item clicked: ");
                //
                //((MainActivity)getActivity()).userNickName
                ChatActivity chatActivity = new ChatActivity();
                Intent intent = new Intent(getActivity(),chatActivity.getClass());
                intent.putExtra("UserName", ((MainActivity)getActivity()).userNickName);
                Log.d("LLLLLLLLLL","activity Start ");

                startActivity(intent);

    }
});
    }
    public void InitializeMovieData()
    {
        chattingList = new ArrayList<ChattingListViewItem>();
        chattingList.add(new ChattingListViewItem("USER_1","마지막 텍스트","12:06","4"));
        chattingList.add(new ChattingListViewItem("USER_2","안녕하세요!","08:23","1"));
    }
}


