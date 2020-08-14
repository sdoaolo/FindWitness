package com.example.findwitness;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ChatActivity<data> extends AppCompatActivity {


    //데이터 베이스
    SQLiteDatabase database;
    ChatSQLiteControl sqlite;
    private ChatSQLiteHelper dbHelper;
    String dbName = "chat.db";
    String tag = "SQLite"; // Log 에 사용할 tag (DB용)
    String tag2 = "SOCKEt"; // Log 에 사용할 tag (소켓용)

    // 서버 접속 여부를 판별하기 위한 변수
    boolean isConnect = false;

    EditText edit1;
    Button btn1;
    LinearLayout container;
    ScrollView scroll;
    ProgressDialog pro;
    // 어플 종료시 스레드 중지를 위해...
    boolean isRunning=false;
    // 서버와 연결되어있는 소켓 객체
    Socket member_socket;
    // 사용자 닉네임( 내 닉넴과 일치하면 내가보낸 말풍선으로 설정 아니면 반대설정)
    String user_nickname;
    //String your_nickname;

    SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat format2 = new SimpleDateFormat("HH:mm:ss");
    Calendar calendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        //DB
        dbHelper = new ChatSQLiteHelper(this,dbName,null,1);
        sqlite = new ChatSQLiteControl(dbHelper);
        database = dbHelper.getReadableDatabase();

        Intent intent = getIntent();
        //목격자닉네임 클릭시 채팅창에 목격자닉네임이 뜸
        TextView you_nickname = (TextView)findViewById(R.id.tv_User_Nickname);
        you_nickname.setText(intent.getStringExtra("nickname"));
        String your_nickname = you_nickname.getText().toString();

        edit1 = findViewById(R.id.editText);
        btn1 = findViewById(R.id.button);
        container=findViewById(R.id.container);
        scroll=findViewById(R.id.scroll);

        //과거에 대화했던 내용 있다면 불러오기
        String sql = "select * from chat where sender="+"\'"+your_nickname+"\'";
        Cursor cursor = database.rawQuery(sql,null);
        if(cursor.moveToFirst()!=false){
            while (cursor.moveToNext()){
                int rcv = cursor.getInt(2);//rcv값 뽑기
                int send = cursor.getInt(3);//send값 뽑기
                String message = cursor.getString(4); //메시지 내용 뽑기
                // 텍스트뷰의 객체를 생성
                TextView tv=new TextView(ChatActivity.this);
                tv.setTextColor(Color.BLACK);
                tv.setTextSize(TypedValue.COMPLEX_UNIT_SP,22);
                //상대방에게 받은 메시지
                if (rcv==1){
                    tv.setBackgroundResource(R.drawable.activity_background);
                    String content = message;
                    tv.setText(content);

                //내가 쓴 메시지
                } else if(send==1){
                    tv.setBackgroundResource(R.drawable.activity_background);
                    String content =message;
                    tv.setText(content);
                }
                container.addView(tv);
                // 제일 하단으로 스크롤 한다
                scroll.fullScroll(View.FOCUS_DOWN);


            }
        }


    }

    // 버튼과 연결된 메소드
    public void btnMethod(View v) {
        if (isConnect == false) {   //접속전
            //사용자가 입력한 닉네임을 받는다.
            String nickName = edit1.getText().toString(); //textview(사용자 id?nickname?)로 입력받음
            if (nickName.length() > 0 && nickName != null) {
                //서버에 접속한다.
                // ProgressDialog : 오래걸릴 때 사용자에게 보여줌(기다려라..^^)
                pro = ProgressDialog.show(this, null, "접속중입니다"); //폰에서 보임- 보이면 99%확률로 연결안된겨^^..
                // 접속 스레드 가동
                ConnectionThread thread = new ConnectionThread();
                thread.start();

            }
            // 닉네임이 입력되지않을경우 다이얼로그창 띄운다.
            else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("닉네임을 입력해주세요");
                builder.setPositiveButton("확인", null);
                builder.show();
            }
        } else {                  // 접속 후
            // 입력한 문자열을 가져온다.
            String msg=edit1.getText().toString();
            // 송신 스레드 가동(서버에게 메시지 보내기)
            SendToServerThread thread=new SendToServerThread(member_socket,msg);
            thread.start();
        }
    }

    // 서버접속 처리하는 스레드 클래스 - 안드로이드에서 네트워크 관련 동작은 항상
    // 메인스레드가 아닌 스레드에서 처리해야 한다.
    class ConnectionThread extends Thread {

        @Override
        public void run() {
            try {
                // 접속한다.
                final Socket socket = new Socket("192.168.219.100", 30000); //host: 서버ip
                member_socket=socket;
                // 미리 입력했던 닉네임을 서버로 전달한다.
                String nickName = edit1.getText().toString();
                user_nickname=nickName;     // 화자에 따라 말풍선을 바꿔주기위해
                // 스트림을 추출
                OutputStream os = socket.getOutputStream();
                DataOutputStream dos = new DataOutputStream(os);
                // 닉네임을 송신한다.
                dos.writeUTF(nickName);
                // ProgressDialog 를 제거한다.
                // runOnUiThread : 현재 스레드가 UI 스레드가 아니라면 행동은 UI 스레드의 자원 사용 이벤트 큐에 들어가게 되는 것 입니다.
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pro.dismiss();
                        edit1.setText("");
                        edit1.setHint("메세지 입력");
                        btn1.setText("전송");
                        // 접속 상태를 true로 셋팅한다.
                        isConnect=true;
                        // 메세지 수신을 위한 스레드 가동
                        isRunning=true;
                        MessageThread thread=new MessageThread(socket);
                        thread.start();
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class MessageThread extends Thread {
        Socket socket;
        DataInputStream dis;

        public MessageThread(Socket socket) {
            try {
                this.socket = socket;
                InputStream is = socket.getInputStream();
                dis = new DataInputStream(is);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            try{
                while (isRunning){
                    // 서버로부터 데이터를 수신받는다.
                    //if(dis.readUTF() != null)
                    final String msg=dis.readUTF();
                    int idx = msg.indexOf(":");
                    final String real_nickname = msg.substring(0, idx-1); //누가 보낸건지 파악
                    //System.out.println(message_nickname);
                    final String real_message = msg.substring(idx+2); // 메시지 내용
                    //System.out.println(real_message);
                    //System.out.println("내이름 : "+user_nickname);

                    // 화면에 출력
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // 텍스트뷰의 객체를 생성
                            TextView tv=new TextView(ChatActivity.this);
                            TextView tv2 = (TextView)findViewById(R.id.tv_User_Nickname);
                            String your_nickname = tv2.getText().toString();

                            int align;
                            tv.setTextColor(Color.BLACK);
                            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP,22);
                            String date = format1.format(calendar.getTime());
                            String time = format2.format(calendar.getTime());

                            //System.out.println("내이름2 :"+user_nickname+",");
                            //System.out.println("내이름3 :"+real_nickname+",");
                            // 메세지의 시작 이름이 내 닉네임과 일치한다면(나의 말)
                            if(msg.startsWith(user_nickname)){
                                tv.setBackgroundResource(R.drawable.activity_background);
                                align = Gravity.RIGHT;
                                container.setGravity(align);
                                String content = real_message;
                                tv.setText(content);
                                sqlite.insert(user_nickname,0,1,real_message,date,time);
                                Log.d(tag, "insert 성공~!");


                            } else{ // 메시지의 네임이 앞에서 message_nickname이라면 >> 목격자의 이름(your_nickname)으로 바꿈
                                //(상대 방 말 or 서버 말)
                                tv.setBackgroundResource(R.drawable.activity_background);
                                align = Gravity.LEFT;
                                container.setGravity(align);
                                String content2 = real_message;
                                tv.setText(content2);
                                sqlite.insert(your_nickname,1,0,real_message,date,time);
                                Log.d(tag, "insert 성공~!");

                            }

                            container.addView(tv);
                            // 제일 하단으로 스크롤 한다
                            scroll.fullScroll(View.FOCUS_DOWN);

                        }
                    });
                }

            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    // 서버에 데이터를 전달하는 스레드
    class SendToServerThread extends Thread{
        Socket socket;
        String msg;
        DataOutputStream dos;

        public SendToServerThread(Socket socket, String msg){
            try{
                this.socket=socket;
                this.msg=msg;
                OutputStream os=socket.getOutputStream();
                dos=new DataOutputStream(os);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            try{
                // 서버로 데이터를 보낸다.
                dos.writeUTF(msg);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        edit1.setText("");
                    }
                });
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try{
            member_socket.close();
            //isRunning=false;

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    protected void onStop() {
        super.onStop();
        try {
            member_socket.close();
            isRunning=false;
        } catch (Exception e){
            Log.d(tag2,"error");
            e.printStackTrace();
        }
    }


}