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

import com.example.findwitness.Chat.ChatActivity;

import java.util.ArrayList;


public class MainChatFragment extends Fragment {
    String SearchResultDate , SearchResultTime;
    ArrayList<ChatListViewItem> chatList;
    public MainChatFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_chat, container, false);
        return inflater.inflate(R.layout.fragment_main_chat, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView recent_list_date = view.findViewById(R.id.recent_list_date);
        TextView recent_list_time = view.findViewById(R.id.recent_list_time);

        Bundle bundle = getArguments();  //번들 받기. getArguments() 메소드로 받음.
        if(bundle != null){
            SearchResultDate = bundle.getString("SearchDate"); //Name 받기.
            SearchResultTime = bundle.getString("SearchTime"); //Name 받기.
            ((MainActivity)getActivity()).btn_search.setBackgroundResource(R.drawable.main_search);
            ((MainActivity)getActivity()).btn_list.setBackgroundResource(R.drawable.main_blue_back);
            ((MainActivity)getActivity()).btn_list.setTextColor(Color.parseColor(((MainActivity)getActivity()).strWhite));
            recent_list_date.setText(SearchResultDate);
            recent_list_time.setText(SearchResultTime);
        }


        //please set Text View of Recent List Info (date, time)

        //listview
        this.InitializeMovieData();

        ListView listView = view.findViewById(R.id.recent_list);
        final ChatListViewAdapter myAdapter = new ChatListViewAdapter(getActivity(),chatList);

        listView.setAdapter(myAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id){
                Log.d("cccccccccccccc","item clicked: "+myAdapter.getItem(position).getUserName()); //
                ChatActivity chatActivity = new ChatActivity();
                Intent intent = new Intent(getActivity(),chatActivity.getClass());
                //putExtra의 첫 값은 식별 태그, 뒤에는 다음 화면에 넘길 값(user nickname을 넘겨 줌)
                Log.d("lllllllllllllll","클릭에러발생");
                intent.putExtra("nickname",chatList.get(position).getUserName());

                startActivity(intent);
            }
        });
    }
    public void InitializeMovieData()
    {
        chatList = new ArrayList<ChatListViewItem>();
        chatList.add(new ChatListViewItem("test 1"));
        chatList.add(new ChatListViewItem("test 2"));
    }
}

