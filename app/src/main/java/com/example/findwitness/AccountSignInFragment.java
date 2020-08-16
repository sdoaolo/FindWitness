package com.example.findwitness;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

public class AccountSignInFragment  extends Fragment {
    /*EditText e_id, e_pw;
    String s_id, s_pw;
    e_id = (EditText) findViewById(R.id.login_email);
    e_pw = (EditText) findViewById(R.id.login_password);
    s_id = e_id.getText().toString();
    s_pw = e_id.getText().toString();*/
    Button signInBtn, signUpBtn, guestLoginBtn;
    EditText Edt_login, Edt_pw;
    String input_id, input_password;
    static String message = "", response = "";
    int pri_num = 0;
    String nickname = "";
//    String address = "192.168.0.88";
//    int port = Integer.parseInt("8888");

    private boolean isFragmentSignIn = true ;
    public AccountSignInFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);

        return inflater.inflate(R.layout.fragment_sign_in, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        signInBtn = view.findViewById(R.id.login_signin_button);

        Edt_login = view.findViewById(R.id.login_email);
        Edt_pw = view.findViewById(R.id.login_password);


        ////////////////////////////////////////////////////////
        Intent intent=new Intent(((AccountActivity)getActivity()),MainActivity.class);
        startActivity(intent);
        ////////////////////////////////////////////////////////


        guestLoginBtn = view.findViewById(R.id.guest_login);
        guestLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // 회원가입 버튼 클릭
                Intent intent=new Intent(((AccountActivity)getActivity()),MainActivity.class);
                startActivity(intent);
            }
        });
        /*new Thread(new Runnable() {
            @Override
            public void run() {
                //socket_connect.connectSocket("성공?");
                socket_connect.connectSocket("192.168.0.8", 9999);
            }
        }).start();*/


/*        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // 로그인 버튼 클릭
                Log.d("RRRRRRRRR","GoToMain");
                input_id = Edt_login.getText().toString();
                input_password = Edt_pw.getText().toString();

                if (input_id.equals("") || input_password.equals("")) {
                    Log.d("RRRRRRRRR","아이디와 비밀번호를 입력하여 주십시오.");
                    //Toast.makeText(sign_in_Activity.this, "아이디와 비밀번호를 입력하여 주십시오.", Toast.LENGTH_SHORT).show();
                    //    Edt_login.setText("");
                    //    Edt_pw.setText("");
                } else {
                    //message = "ID : " + input_id + "," + "PASSWORD : " + input_password + "\n";
                    message = "LOGIN:" + input_id + "," + input_password + "\n";
                    //message = input_id + "/" + input_password;


                    //Toast.makeText(sign_in_Activity.this, message, Toast.LENGTH_SHORT).show();
                    Log.d("RRRRRRRRR",message);

                    //서버에 보내고 응답 받아오기
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            socket_connect.sendMessage(message);
                        }
                    }).start();

                    try {
                        sleep(6000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    response = socket_connect.getMessage();
                    String[] res = response.split(",");
                    Log.d("RRRRRRRRR","res[0] : "+res[0]);
                    Log.d("RRRRRRRRR","res[1] : "+res[1]);
                    Log.d("RRRRRRRRR","res[2] : "+res[2]);

                    try {
                        if (res[0].equals("Login_success")) {
                            pri_num = Integer.parseInt(res[1]);
                            nickname = res[2];

                            //Toast.makeText(AccountSignInFragment.this, "로그인 성공", Toast.LENGTH_LONG).show();
                            Log.d("RRRRRRRRR","로그인 성공"+pri_num+","+nickname);
                            Intent intent=new Intent(((AccountActivity)getActivity()),MainActivity.class);
                            intent.putExtra("pri_num", pri_num);
                            intent.putExtra("nickname", nickname);
                            startActivity(intent);
                            //finish();

                        } else if (res[0].equals("Login_fail")) {
                            //Toast.makeText(getApplicationContext(), "로그인에 실패했습니다.\n아이디와 비밀번호를 확인해주세요.", Toast.LENGTH_LONG).show();
                            Log.d("RRRRRRRRR","로그인 실패");
                        } else {
                            //Toast.makeText(getApplicationContext(), "알 수 없는 오류입니다.", Toast.LENGTH_LONG).show();
                            Log.d("RRRRRRRRR","알 수 없는 오류 발생");
                            Log.d("RRRRRRRRR",response);
                        }
                    } catch (Exception e) {
                        //Toast.makeText(getApplicationContext(), "오류가 발생했습니다.", Toast.LENGTH_LONG).show();
                        Log.d("RRRRRRRRR","Exception e 발생");
                        e.printStackTrace();
                    }
                }
                //Intent intent=new Intent(((AccountActivity)getActivity()),MainActivity.class);
                //startActivity(intent);
            }
        });

        signUpBtn = view.findViewById(R.id.login_signup_button);
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // 회원가입 버튼 클릭
                ((AccountActivity) getActivity()).txt.setText("SIGN UP");
                ((AccountActivity) getActivity()).signUpFragment = new AccountSignUpFragment();
                ((AccountActivity)getActivity()).fm.beginTransaction().replace(R.id.LayoutFragment,(((AccountActivity) getActivity()).signUpFragment)).commit();/*프래그먼트 매니저가 프래그먼트를 담당한다!*/
/*            }
        });*/
    }
}