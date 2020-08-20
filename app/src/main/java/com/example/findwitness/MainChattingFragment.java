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

import com.example.findwitness.Adapter.ChttingListVIewAdapter;
import com.example.findwitness.Chat.ChatActivity;
import com.example.findwitness.Item.ChattingListViewItem;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class MainChattingFragment extends Fragment {
    List<ChattingListViewItem> chattingList=new ArrayList<ChattingListViewItem>();
    public static Socket mSocket;
    private String TAG = "-->>";
    private Boolean isConnected;
    private String MY_NICKNAME = "엄마";
    String CHAT_URL="http://192.168.219.102:8004/"; //>>채팅리스트화면용

    public MainChattingFragment() {
        // Required empty public constructor
        isConnected = false;

        try {
            mSocket= IO.socket(CHAT_URL);
        } catch (URISyntaxException e) {
            Log.e("URISyntaxException error", e.getMessage());
        }
        mSocket.on(Socket.EVENT_DISCONNECT, onDisconnect);
        mSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
        mSocket.connect();
        mSocket.on(Socket.EVENT_CONNECT, onConnect); //MY_NICKNAME 이름 전달함.
        mSocket.emit("person");
        mSocket.on("person",onNewPerson);//사람들 값 받아오기.마지막메시지,시간,메시지갯수

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main_chatting, container, false);
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //listview

        ListView listView = view.findViewById(R.id.recent_list);
        final ChttingListVIewAdapter myAdapter = new ChttingListVIewAdapter(getActivity(),chattingList);

        listView.setAdapter(myAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id){

                ChatActivity chatActivity = new ChatActivity();
                Intent intent = new Intent(getActivity(),chatActivity.getClass());
                intent.putExtra("UserName", ((MainActivity)getActivity()).userNickName);
                //나-상대방의 고유번호랑 닉네임 보내야함

                startActivity(intent);

            }
        });
    }


    private Emitter.Listener onDisconnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            isConnected = false;
            Log.w(TAG, "onDisconnect");
        }
    };


    private Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.e(TAG, "error connecting");
        }
    };

    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            if (!isConnected) {
                isConnected = true;
                mSocket.emit("chatlist add user", MY_NICKNAME);

            } else {
                Log.w("-->>", "onConnect Failure");
            }
        }
    };
    private Emitter.Listener onNewPerson = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.w(TAG, "onNewPerson");
            JSONObject data = (JSONObject) args[0]; //상대방 채팅창 참가
            String user = null;
            String text = null;
            String time = null;
            String chat_num = null;
            try {
                user = data.getString("user"); //메시지 보낸 사람
                text = data.getString("text"); //메시지 내용
                time = data.getString("time");
                chat_num = data.getString("chat_num");
                time = time.substring(0,2) + "시 " + time.substring(2,4) + "분";

            } catch (JSONException e) {
                Log.e("JSONException error", e.getMessage());
            }


            chattingList.add(new ChattingListViewItem(user,text,time,chat_num));
            Log.d(TAG, "정보 가져오기 성공!~~!!!");


        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (isConnected) {
            mSocket.disconnect();
            isConnected = false;
        }

        mSocket.off(Socket.EVENT_CONNECT, onConnect);
        mSocket.off(Socket.EVENT_DISCONNECT, onDisconnect);
        mSocket.off(Socket.EVENT_CONNECT_ERROR, onConnectError);
        mSocket.off("person",onNewPerson);

    }
}


