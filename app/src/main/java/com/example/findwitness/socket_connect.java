package com.example.findwitness;

import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class socket_connect extends AppCompatActivity {


    static String response;
    static Socket socket = null;
    static int i;

    public socket_connect() {
    }

    public static void connectSocket(String addr, int port){
        try{
            socket = new Socket(addr, port);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    /*public static void sendMessage(String message){

        OutputStream outStream;
        InputStream inStream;
        int bytesRead;

        try{
            //송신
            outStream = socket.getOutputStream();
            outStream.write(message.getBytes());//("UTF-8"));

            //수신
            byte[] buffer = new byte[1024];
            inStream = socket.getInputStream();
            bytesRead = inStream.read(buffer);
            response = new String(buffer, 0, bytesRead);  // ,"UTF-8");
        }catch (Exception e){
            e.printStackTrace();
        }
    }*/

    public static void sendMessage(String message){
        OutputStream out;
        ByteArrayOutputStream byteArrayOutputStream;
        InputStream inputStream;
        int bytesRead = 0;
        try {
            //송신
            out = socket.getOutputStream();
            out.write(message.getBytes());
            while(true) {
                try {
                    //수신
                    byte[] buffer = new byte[1024];
                    inputStream = socket.getInputStream();
                    byteArrayOutputStream = new ByteArrayOutputStream(1024);
                    /*
                     * notice:
                     * inputStream.read() will block if no data return
                     */
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        byteArrayOutputStream.write(buffer, 0, bytesRead);
                        response += byteArrayOutputStream.toString("UTF-8");
                    }

                    break;

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static String getMessage(){
        return response;
    }

}
