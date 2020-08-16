package com.example.findwitness.Chat;

/*
        과거에 대화던 내용 불러오기 위해서는 상대방 닉네임과 제 닉네임이 필요합니다.
        임시로 제가 이름을 지정했습니다.
        혹시 테스트하실 분은 이름 변경해서 사용하면 됩니다!~!
        >>>>
        String real_my_nickname = "지혜";
        tring your_nickname = "수헌";


        <간단 설명>
        서버-클라이언트의 connection에 성공하면
        내가 보낸 메시지를 서버가 받고 나를 제외한 상대방에게 뿌려준다.
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

    private EditText editMessage;
    private Button sendButton;
    public static Socket mSocket;
    private ChatApp app;
    private boolean mTyping;
    private String mUsername;
    TextView typingView;
    private Boolean isConnected;
    private String TAG="-->>";
    private RecyclerView recyclerView;
    private MessageAdapter mAdapter;
    private List<Message> messageList;
    private static final int TIMER=500;
    private static final int REQUEST_CODE=0;
    private Handler typingHandler=new Handler(); // //유저가 치고 있는지 여부를 통해 메시지 내용뷰에서 충돌이 안 생김ㅇㅇ

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

        dbHelper = new ChatSQLiteHelper(this,dbName,null,1);
        sqlite = new ChatSQLiteControl(dbHelper);
        database = dbHelper.getReadableDatabase();


        //toolbar 선언 및 어쩌구 저쩌구 3줄 없앰.
        toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        isConnected=false;
        chatting_opponent = (TextView)findViewById(R.id.chatting_opponent);
        chatting_opponent.setText("INPUT STRING"); //INPUT USER NICKNAME STRING
        mTyping=false;
        app=new ChatApp();
        Log.d("LLLLLLLLLL","Hello");
        initializeSocket();
        Log.d("LLLLLLLLLL","ByeBye");
        signIn();
        pastContentPickUP thread = new pastContentPickUP();
        thread.start();
        setUpUI();
    }

    class pastContentPickUP extends Thread{
        @Override
        public void run(){
            try {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String real_my_nickname = "지혜";
                        // 로그인화면에서 이름입력받은 값으로 real_my_nickname하면 에러남 :  NullPointerException
                        String your_nickname = "수헌"; // 나중에 이름 받아오면 변경하자
                        //TextView my_nickname = (EditText) findViewById(R.id.username_editText);
                        //String real_my_nickname = my_nickname.getText().toString();
                        String sql = "select * from chat where sender=" + "\'" + your_nickname + "\'";
                        Cursor cursor = database.rawQuery(sql, null);
                        if (cursor.moveToFirst() != false) {
                            while (cursor.moveToNext()) {
                                int rcv = cursor.getInt(2);//rcv값 뽑기
                                int send = cursor.getInt(3);//send값 뽑기
                                String message = cursor.getString(4); //메시지 내용 뽑기

                                //상대방에게 받은 메시지
                                if (rcv == 1) {
                                    addMessage(your_nickname, message, Message.TYPE_MESSAGE_RECEIVED);
                                } else if (send == 1) { //내가 쓴 메시지
                                    addMessage(real_my_nickname, message, Message.TYPE_MESSAGE_SENT);
                                }

                            }
                        }
                    }
                });
            } catch (Exception e){
                e.printStackTrace();
            }
        }
}


    public void initializeSocket(){

        // mSocket.on 서버에게 받은 이벤트 (Emitter.Listener 로 받은 내용 처리함)


        mSocket=app.getSocket();
        Log.d("LLLLLLLLL","get socket"+mSocket);
        mSocket.on(Socket.EVENT_DISCONNECT,onDisconnect);
        mSocket.on(Socket.EVENT_CONNECT_ERROR,onConnectError);
        mSocket.on("user joined",onUserJoined);
        mSocket.on("user left",onUserLeft);
        mSocket.on("new message",onNewMessage);
        mSocket.on("typing",onTyping);
        mSocket.on("stop typing",onStopTyping);
    }
    private void signIn(){
        mUsername=null;
        Intent i=new Intent(this,LoginActivity.class); //login activity에서
        startActivityForResult(i,REQUEST_CODE);  // >>onActivityResult
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode!= Activity.RESULT_OK) {
            finish();
            return;
        }
        isConnected=true;
        Snackbar.make(findViewById(android.R.id.content),"Welcome to Socket Chat", Snackbar.LENGTH_SHORT).show();
        mUsername=data.getStringExtra("username");
        int numUsers=data.getIntExtra("numUsers",1);
        addParticipantsLog(numUsers);
    }

    public void setUpUI(){
        messageList=new ArrayList<>();
        recyclerView=(RecyclerView)findViewById(R.id.recycler_view);
        mAdapter=new MessageAdapter(messageList);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        editMessage=(EditText)findViewById(R.id.editMessage);
        typingView=findViewById(R.id.typing);
        sendButton=(Button)findViewById(R.id.sendButton);

        editMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(mUsername==null)
                    return;
                if(!mSocket.connected())
                    return;
                if(TextUtils.isEmpty(editMessage.getText()))
                    return;
                if(mTyping==false) {
                    mTyping = true;
                    mSocket.emit("typing"); //나 치고 있딴다^^라고 서버에게 보내기

                }
                typingHandler.removeCallbacks(onTypingTimeout);
                typingHandler.postDelayed(onTypingTimeout,TIMER);
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
        if(isConnected) {
            mSocket.disconnect();
            isConnected = false;
        }


        mSocket.off(Socket.EVENT_DISCONNECT,onDisconnect);
        mSocket.off(Socket.EVENT_CONNECT_ERROR,onConnectError);
        mSocket.off("user joined",onUserJoined);
        mSocket.off("user left",onUserLeft);
        mSocket.off("new message",onNewMessage);
        mSocket.off("typing",onTyping);
        mSocket.off("stop typing",onStopTyping);

        mTyping=false;

    }


    private Emitter.Listener onDisconnect=new Emitter.Listener() {
        @Override
        public void call(Object... args) { //EVENT_DISCONNECT 이벤트 받음
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    isConnected=false;
                    Log.w(TAG, "onDisconnect");
                }
            });
        }
    };

    private Emitter.Listener onConnectError=new Emitter.Listener() {
        @Override
        public void call(Object... args) { //EVENT_CONNECT_ERROR 이벤트 받음
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.e(TAG,"error connecting");
                }
            });
        }
    };

    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {

            Log.w(TAG,"onNewMesage");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data= (JSONObject)args[0]; //상대방 채팅창 참가
                    String username=null;
                    String message=null;
                    try {
                        username=data.getString("username"); //메시지 보낸 사람(나이거나 상대방이다.)
                        message=data.getString("message"); //메시지 내용
                    } catch (JSONException e) {
                        Log.e(TAG,e.getMessage());
                        e.printStackTrace();
                    }

                    addMessage(username,message,Message.TYPE_MESSAGE_RECEIVED);//받은 메시지
                    sqlite.insert(username,1,0,message,date,time);
                    Log.d(tag, "insert 성공~!");
                }
            });
        }
    };


    private Emitter.Listener onUserJoined = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            Log.w(TAG,"onUserJoined");
            runOnUiThread(new Runnable() {
                @SuppressLint("StringFormatInvalid")
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String username=null;
                    int numUsers;
                    try {
                        username = data.getString("username");
                        numUsers = data.getInt("numUsers");
                    } catch (JSONException e) {
                        Log.e(TAG, e.getMessage());
                        return;
                    }

                    addLog(username+" has joined");  //누가 참가했다 알리기
                    addParticipantsLog(numUsers); //총 몇명인지 알리기
                }
            });
        }
    };

    private Emitter.Listener onUserLeft = new Emitter.Listener() {
        @Override
        public void call(final Object... args) { //user left 이벤트 받음

            Log.w(TAG,"onUserLeft");
            runOnUiThread(new Runnable() {
                @SuppressLint("StringFormatInvalid")
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String username;
                    int numUsers;
                    try {
                        username = data.getString("username");
                        numUsers = data.getInt("numUsers");
                    } catch (JSONException e) {
                        Log.e(TAG, e.getMessage());
                        return;
                    }

                    addLog(username+" left"); //누가 떠났는지 말해줌
                    addParticipantsLog(numUsers); //남은 사람들 몇명인지 알랴줌

                    removeTyping();
                }
            });
        }
    };

    private Emitter.Listener onTyping = new Emitter.Listener() {
        @Override
        public void call(final Object... args) { //typing 이벤트 받음

            Log.w(TAG,"onTyping");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String username;
                    try {
                        username = data.getString("username");
                    } catch (JSONException e) {
                        Log.e(TAG, e.getMessage());
                        return;
                    }
                    addTyping(username); //누가 메시지 입력했는지 알랴줌

                }
            });
        }
    };

    private Emitter.Listener onStopTyping = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            Log.w(TAG,"onStopTyping");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    removeTyping();
                }
            });
        }
    };

    private void addLog(String message){
        messageList.add(new Message(Message.TYPE_LOG,message));
        mAdapter.notifyItemInserted(messageList.size()-1);
        scrollUp();
    }

    private void addMessage(String username,String message, int messageType){
        messageList.add(new Message(messageType,username,message));
        mAdapter.notifyItemInserted(messageList.size()-1);
        scrollUp();
    }

    private void addParticipantsLog(int numUsers) { //몇명있는지 알랴줌
        SimpleDateFormat format2 = new SimpleDateFormat ( "yyyy년 MM월dd일 HH시mm분ss초");
        String format_time2 = format2.format (System.currentTimeMillis());
        String currentDate = format_time2.substring(0,12);
        addLog(currentDate);
        //addLog("There are "+numUsers+" users in the chat room");
        scrollUp();
    }

    private void addTyping(String username){
        typingView.setText(username.trim()+" is typing");
    }

    private void removeTyping() {
        typingView.setText(null);
    }

    private void attemptSend() { //메시지 쓰고 보냅니다.
        if (mUsername==null) return;
        if (!mSocket.connected()) return;
        if(mTyping) { //true를 false로 고침
            mTyping = false;
            mSocket.emit("stop typing"); //이벤트 보내기
        }
        String message = editMessage.getText().toString().trim(); //trim: 문자열 공백 제거

        if (TextUtils.isEmpty(message)) { //메시지가 비어있지 않으면 editMessage로 focuss

            editMessage.requestFocus();
            return;
        }

        editMessage.setText("");
        addMessage(mUsername, message, Message.TYPE_MESSAGE_SENT); // 내가 보낸 메시지
        sqlite.insert(mUsername,0,1,message,date,time);
        Log.d(tag, "insert 성공~!");


        // perform the sending message attempt.
        mSocket.emit("new message", message); //새로운 메시지 이벤트 보내기 (서버에게 >>서버가 그 메시지 뿌려줌)

    }

    private Runnable onTypingTimeout=new Runnable() {
        @Override
        public void run() {
            if(mTyping==false) //치고 있지 않음.

                return;

            mTyping=false;
            mSocket.emit("stop typing"); //stop typing 이벤트 전송
        }
    };
    private void scrollUp(){ //리스트의 가장 마지막을 보여주도록 스크롤을 이동
        recyclerView.scrollToPosition(mAdapter.getItemCount()-1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.test_menu,menu);
        return true;
    }

    //로그아웃 (( 나중에 없애쟈..^^))

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout:
                logout();
        }
        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        mSocket.disconnect();
        isConnected=false;
        mTyping=false;
        messageList.clear();
        this.recreate();
    }
}