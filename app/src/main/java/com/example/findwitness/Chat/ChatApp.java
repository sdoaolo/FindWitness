package com.example.findwitness.Chat;

import android.app.Application;

import java.net.URISyntaxException;
import io.socket.client.IO;
import io.socket.client.Socket;

public class ChatApp extends Application {
    //private final String CHAT_URL="http://172.20.10.2:8005/"; //지은
    private final String CHAT_URL="http://192.168.219.100:8005/"; //지혜
    private Socket mSocket;
    {
        try {
            mSocket= IO.socket(CHAT_URL);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
    public Socket getSocket(){
        return mSocket;
    }
}