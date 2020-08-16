package com.example.findwitness.Chat;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.findwitness.R;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import static com.example.findwitness.Chat.ChatActivity.mSocket;

public class LoginActivity  extends AppCompatActivity {
    private EditText mUsernameEditText;
    private Button mLogin;
    private String mUsername;
    private Boolean isConnected;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_login_activity);
        mUsernameEditText=(EditText)findViewById(R.id.username_editText);
        mLogin=(Button)findViewById(R.id.loginactivity_login_btn);
        isConnected=false;
        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("LLLLLLLLLL","login button Clicked!");
                login(view);
            }
        });
        Log.d("LLLLLLLLLL","soccet  on?");
        mSocket.on(Socket.EVENT_CONNECT,onConnect);
        Log.d("LLLLLLLLLL","soccet  on good ! Login?");
        mSocket.on("login",onLogin);
        Log.d("LLLLLLLLLL","soccet  Login good");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSocket.off("login",onLogin);
        mSocket.off(Socket.EVENT_CONNECT,onConnect);
    }

    private void login(View v) { //로그인 버튼 클릭시 로그인 함수
        String username=mUsernameEditText.getText().toString().trim(); //유저 이름 변수 여기다가 할당.
        if(TextUtils.isEmpty(username)){
            Snackbar.make(v,"Enter username",Snackbar.LENGTH_SHORT).show();
            mUsernameEditText.requestFocus();
            return;
        }
        mUsername=username;
        Log.d("LLLLLLLLLL","socket connect test 전");
        mSocket.connect();
        Log.d("LLLLLLLLLL","socket connect test 후");
    }

    private Emitter.Listener onConnect=new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            if(!isConnected){
                isConnected=true;
                mSocket.emit("add user",mUsername);
            }
            else{
                Log.w("-->>","onConnect Failure");
            }
        }
    };

    private Emitter.Listener onLogin=new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            JSONObject data= (JSONObject) args[0];
            int numUsers=0;
            try {
                numUsers=data.getInt("numUsers");
            }catch (JSONException e){
                e.printStackTrace();
            }
            Intent i=new Intent();
            i.putExtra("username",mUsername);
            i.putExtra("numUsers",numUsers);
            setResult(RESULT_OK,i);
            finish();
        }
    };
}

