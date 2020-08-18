package com.example.findwitness.Chat;

/*
        과거에 대화던 내용 불러오기 위해서는 상대방 닉네임과 제 닉네임이 필요합니다.
        임시로 제가 이름을 지정했습니다.
        혹시 테스트하실 분은 이름 변경해서 사용하면 됩니다!~!
        +
        채팅도 이름 바꿔서 쓰면 됩니다.

        >>>>
        SENDER_NICKNAME ,  MY_NICKNAME
        >>>>
        <간단 설명>
        서버-클라이언트의 connection에 성공하면
        내가 보낸 메시지를 서버가 받고 내가 보내고 싶은 사람한테 뿌려준다.
* */

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.findwitness.ChatSQLiteControl;
import com.example.findwitness.ChatSQLiteHelper;
import com.example.findwitness.R;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class ChatActivity<data> extends AppCompatActivity {

    //닉네임
    //나중에 입력값받아야함
    private String MY_NICKNAME = "수헌";
    private String YOU_NICKNAME = "지혜";

    //고유번호 >> 채팅방이름에 이용할거임.
    private int MY_NUM = 2;
    private int YOU_NUM = 1;
    private String CHAT_ROOM_NAME;

    private EditText editMessage;
    private Button sendButton;
    public static Socket mSocket;
    private ChatApp app;
    private Boolean isConnected;
    private String TAG = "-->>";
    private RecyclerView recyclerView;
    private MessageAdapter mAdapter;
    private List<Message> messageList;
    private static final int TIMER = 500;
    private static final int REQUEST_CODE = 0;

    Toolbar toolbar;
    TextView chatting_opponent;

    /* DATABASE */
    //데이터 베이스
    SQLiteDatabase database;
    ChatSQLiteControl sqlite;
    private ChatSQLiteHelper dbHelper;
    String dbName = "chat.db";
    String tag = "SQLite"; // Log 에 사용할 tag (DB용)


    SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd"); //날짜
    SimpleDateFormat format2 = new SimpleDateFormat("HH:mm:ss"); //시간
    Calendar calendar = Calendar.getInstance();
    String date = format1.format(calendar.getTime());
    String time = format2.format(calendar.getTime());


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Intent intent = getIntent();
        //MY_NICKNAME = intent.getStringExtra("UserName");
        Log.d("hhhhhhhhhhhhhh","userNickName: "+MY_NICKNAME);


        dbHelper = new ChatSQLiteHelper(this, dbName, null, 1);
        sqlite = new ChatSQLiteControl(dbHelper);
        database = dbHelper.getReadableDatabase();
        toolbar = findViewById(R.id.my_toolbar);

        Log.d("lllllllllllllll","toolbar");
        setSupportActionBar(toolbar);
        Log.d("lllllllllllllll","toolbar2");
        isConnected = false;
        chatting_opponent = (TextView) findViewById(R.id.chatting_opponent);
        chatting_opponent.setText("INPUT STRING"); //INPUT USER NICKNAME STRING

        if(MY_NUM>YOU_NUM){
            CHAT_ROOM_NAME = YOU_NUM+"&"+MY_NUM ;
        }else {
            CHAT_ROOM_NAME = MY_NUM + "&"+YOU_NUM;
        }

        app = new ChatApp();
        Log.d("LLLLLLLLL", "appt");


        initializeSocket();
        mSocket.connect();
        mSocket.on(Socket.EVENT_CONNECT, onConnect); //MY_NICKNAME 이름 전달함.
        //mSocket.on(Socket.EVENT_CONNECT, onConnect); //MY_NICKNAME 이름 전달함.
        Log.d("LLLLLLLLL", "mSocket.connect");
        //mSocket.emit("chat room name", CHAT_ROOM_NAME); //서버DB에서 못받은 메시지 받기 위해
        mSocket.on("login", onLogin);
        //mSocket.on("login2", onSave); //환영 인사 + 날짜 표시(흠..옮겨야 할 것 같긴헌디큐큐..)

        //사용자 로컬 DB용
        pastContentPickUP thread = new pastContentPickUP();
        thread.start();



        setUpUI();




    }
    class pastContentPickUP extends Thread {
        @Override
        public void run() {
            try {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String sql = "select * from chat where sender=" + "\'" + YOU_NICKNAME + "\'";
                        Cursor cursor = database.rawQuery(sql, null);
                        int size=cursor.getCount();
                        if (cursor.moveToFirst() != false) {
                            while (cursor.moveToNext()) {
                                int rcv = cursor.getInt(2);//rcv값 뽑기
                                int send = cursor.getInt(3);//send값 뽑기
                                String message = cursor.getString(4); //메시지 내용// 뽑기


                                //상대방에게 받은 메시지
                                if (rcv == 1) {
                                    addMessage(YOU_NICKNAME, message, Message.TYPE_MESSAGE_RECEIVED);
                                } else if (send == 1) { //내가 쓴 메시지
                                    addMessage(MY_NICKNAME, message, Message.TYPE_MESSAGE_SENT);
                                }
                                //mAdapter.notifyDataSetChanged();

                            }
                        }
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public void initializeSocket(){
        mSocket = app.getSocket();
        mSocket.on(Socket.EVENT_DISCONNECT, onDisconnect);
        mSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
        mSocket.on("new message", onNewMessage);
        mSocket.on("login2", onSave);

    }


    public void setUpUI() {
        messageList = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mAdapter = new MessageAdapter(messageList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        editMessage = (EditText) findViewById(R.id.editMessage);
        sendButton = (Button) findViewById(R.id.sendButton);

        editMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (MY_NICKNAME == null)
                    return;
                if (!mSocket.connected())
                    return;
                if (TextUtils.isEmpty(editMessage.getText()))
                    return;

                //typingHandler.removeCallbacks(onTypingTimeout);
                //typingHandler.postDelayed(onTypingTimeout, TIMER);

            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptSend();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isConnected) {
            mSocket.disconnect();
            isConnected = false;
        }

        mSocket.off(Socket.EVENT_CONNECT, onConnect);
        mSocket.off(Socket.EVENT_DISCONNECT, onDisconnect);
        mSocket.off(Socket.EVENT_CONNECT_ERROR, onConnectError);
        mSocket.off("login", onLogin);
        mSocket.off("new message", onNewMessage);
        mSocket.off("login2", onSave);

    }


    private Emitter.Listener onDisconnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) { //EVENT_DISCONNECT 이벤트 받음
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    isConnected = false;
                    Log.w(TAG, "onDisconnect");
                }
            });
        }
    };

    private Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(Object... args) { //EVENT_CONNECT_ERROR 이벤트 받음
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.e(TAG, "error connecting");
                }
            });
        }
    };

    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {

            Log.w(TAG, "onNewMesage");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0]; //상대방 채팅창 참가
                    String username = null;
                    String message = null;
                    try {
                        username = data.getString("username"); //메시지 보낸 사람
                        message = data.getString("message"); //메시지 내용
                    } catch (JSONException e) {
                        Log.e(TAG, e.getMessage());
                        e.printStackTrace();
                    }
                    Log.d("222222222222",username);

                    Log.d("222222222222",message);

                    addMessage(username, message, Message.TYPE_MESSAGE_RECEIVED);//받은 메시지
                    sqlite.insert(username, 1, 0, message, date, time);
                    Log.d(tag, "insert 성공~!");
                }
            });
        }
    };



    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            if (!isConnected) {
                isConnected = true;
                mSocket.emit("chat add user", MY_NICKNAME,CHAT_ROOM_NAME);
                //mSocket.emit("save message"); //서버DB에서 못받은 메시지 받기 위해

            } else {
                Log.w("-->>", "onConnect Failure");
            }
        }
    };

    private Emitter.Listener onLogin = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            isConnected = true;
            Snackbar.make(findViewById(android.R.id.content), "Welcome to Socket Chat", Snackbar.LENGTH_SHORT).show();
            SimpleDateFormat format2 = new SimpleDateFormat("yyyy년 MM월dd일 HH시mm분ss초");
            String format_time2 = format2.format(System.currentTimeMillis());
            String currentDate = format_time2.substring(0, 12);
            addLog(currentDate);
            scrollUp();
        }
    };


    private Emitter.Listener onSave = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {

            Log.w(TAG, "onSave");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0]; //상대방 채팅창 참가
                    String username = null;
                    String message = null;
                    try {
                        username = data.getString("username"); //메시지 보낸 사람
                        message = data.getString("message"); //메시지 내용
                    } catch (JSONException e) {
                        Log.e(TAG, e.getMessage());
                        e.printStackTrace();
                    }
                    Log.d("LLLLLLLLLLLLLL:onsave",username);

                    addMessage(username, message, Message.TYPE_MESSAGE_RECEIVED);//받은 메시지
                    sqlite.insert(username, 1, 0, message, date, time);
                    Log.d(tag, "insert 성공~!");
                }
            });
        }
    };



    private void addLog(String message) {
        messageList.add(new Message(Message.TYPE_LOG, message));
        mAdapter.notifyDataSetChanged();
        scrollUp();
    }

    private void addMessage(String username, String message, int messageType) {
        messageList.add(new Message(messageType, username, message));
        Log.d("이름이 왜 아느ㄸ지",username);
        mAdapter.notifyDataSetChanged();
        scrollUp();
    }

    private void attemptSend() { //메시지 쓰고 보냅니다.
        if (MY_NICKNAME == null) return;
        if (!mSocket.connected()) return;
        String message = editMessage.getText().toString().trim(); //trim: 문자열 공백 제거

        if (TextUtils.isEmpty(message)) { //메시지가 비어있지 않으면 editMessage로 focuss

            editMessage.requestFocus();
            return;
        }

        editMessage.setText("");
        addMessage(MY_NICKNAME, message, Message.TYPE_MESSAGE_SENT); // 내가 보낸 메시지
        sqlite.insert(YOU_NICKNAME, 0, 1, message, date, time);
        Log.d(tag, "insert 성공~!");


        // perform the sending message attempt.
        mSocket.emit("new message", message, YOU_NICKNAME, MY_NICKNAME,CHAT_ROOM_NAME); //새로운 메시지 이벤트 보내기 (서버에게 >>서버가 그 메시지 뿌려줌)

    }


    private void scrollUp() { //리스트의 가장 마지막을 보여주도록 스크롤을 이동
        recyclerView.scrollToPosition(mAdapter.getItemCount()-1);
    }

}