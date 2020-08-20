package com.example.findwitness;

import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class socket_connect extends AppCompatActivity {

    static String response="";
    static InetSocketAddress sockAdr = null;
    static Socket socket = null;
    static int i;

    public socket_connect() {
    }

    public static void connectSocket(String addr, int port){
        try{
            socket = new Socket(addr, port);
        }catch (Exception e){
            Log.e("Exception error", e.getMessage());
        }
    }
    /*public static void connectSocket(String data) {
        OutputStream os = null;
        OutputStreamWriter osw = null;
        BufferedWriter bw = null;

        InputStream is = null;
        InputStreamReader isr = null;
        BufferedReader br = null;

        try {
            socket = new Socket("192.168.0.8", 9999);
            os = socket.getOutputStream();
            osw = new OutputStreamWriter(os);
            bw = new BufferedWriter(osw);            //서버로 전송을 위한 OutputStream

            is = socket.getInputStream();
            isr = new InputStreamReader(is);
            br = new BufferedReader(isr);        // 서버로부터 Data를 받음

            bw.write(data);
            bw.newLine();
            bw.flush();

            String receiveData = "";
            receiveData = br.readLine();        // 서버로부터 데이터 한줄 읽음
            System.out.println("서버로부터 받은 데이터 : " + receiveData);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                bw.close();
                osw.close();
                os.close();
                br.close();
                isr.close();
                is.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }*/

    /*public static void connectSocket(String addr, int port, int timeout){
        sockAdr = new InetSocketAddress(addr, port);
        socket = new Socket();

        try {
            socket.connect(sockAdr, timeout);
        } catch (SocketTimeoutException e){
            if(socket.isConnected()){
                try {
                    socket.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            connectSocket(addr, port, timeout);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/
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
        response = "";
        OutputStream out;
        ByteArrayOutputStream byteArrayOutputStream;
        InputStream inputStream;
        int bytesRead = 0;
        try {
            //송신

            //if(!socket.isConnected()){
            connectSocket("192.168.0.8", 9999);
            //}

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
                    Log.e("Exception error", e.getMessage());
                }
            }
        } catch (Exception e) {
            Log.e("Exception error", e.getMessage());
        }
    }

    public static String getMessage(){
        //String response_two = response;
        //response = "";
        //return response_two;
        return response;
    }

}
