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

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
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

    private String SENDER_NICKNAME = "수헌"; //나중에 입력값받아야함
    private String MY_NICKNAME = "지혜";


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



        //DB용
        //pastContentPickUP thread = new pastContentPickUP();
        //thread.start();


        app = new ChatApp();
        Log.d("LLLLLLLLL", "appt");

        initializeSocket();
        mSocket.connect();

        mSocket.on(Socket.EVENT_CONNECT, onConnect); //이름 전달함.
        Log.d("LLLLLLLLL", "mSocket.connect");
        mSocket.on("login", onLogin);


        //DB용
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
                        String sql = "select * from chat where sender=" + "\'" + SENDER_NICKNAME + "\'";
                        Cursor cursor = database.rawQuery(sql, null);
                        int size=cursor.getCount();
                        if (cursor.moveToFirst() != false) {
                            while (cursor.moveToNext()) {
                                int rcv = cursor.getInt(2);//rcv값 뽑기
                                int send = cursor.getInt(3);//send값 뽑기
                                String message = cursor.getString(4); //메시지 내용// 뽑기


                                //상대방에게 받은 메시지
                                if (rcv == 1) {
                                    addMessage(SENDER_NICKNAME, message, Message.TYPE_MESSAGE_RECEIVED);
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

                    addMessage(username, message, Message.TYPE_MESSAGE_RECEIVED);//받은 메시지
                    sqlite.insert(username, 1, 0, message, date, time);
                    Log.d(tag, "insert 성공~!");
                }
            });
        }
    };


    //추가한 부분
    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            if (!isConnected) {
                isConnected = true;
                mSocket.emit("add user", MY_NICKNAME);

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
            //scrollUp();
        }
    };

    private void addLog(String message) {
        messageList.add(new Message(Message.TYPE_LOG, message));
        mAdapter.notifyItemInserted(messageList.size());
        scrollUp();
    }

    private void addMessage(String username, String message, int messageType) {
        messageList.add(new Message(messageType, username, message));
        mAdapter.notifyItemInserted(messageList.size() );
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
        sqlite.insert(SENDER_NICKNAME, 0, 1, message, date, time);
        Log.d(tag, "insert 성공~!");


        // perform the sending message attempt.
        mSocket.emit("new message", message, SENDER_NICKNAME, MY_NICKNAME); //새로운 메시지 이벤트 보내기 (서버에게 >>서버가 그 메시지 뿌려줌)

    }


    private void scrollUp() { //리스트의 가장 마지막을 보여주도록 스크롤을 이동
        recyclerView.scrollToPosition(mAdapter.getItemCount() - 1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.test_menu, menu);
        return true;
    }

}