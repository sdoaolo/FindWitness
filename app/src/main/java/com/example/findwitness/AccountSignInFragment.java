package com.example.findwitness;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import static java.lang.Thread.sleep;

public class AccountSignInFragment  extends Fragment {

    Button signInBtn, signUpBtn, guestLoginBtn;
    EditText Edt_login, Edt_pw;
    String input_id, input_password;
    static String message = "", response = "";
    static int pri_num = 0;
    String nickname = "";

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

        guestLoginBtn = view.findViewById(R.id.guest_login);
        guestLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pri_num = 88888;
                nickname = "GUEST";
                Intent intent=new Intent(((AccountActivity)getActivity()),MainActivity.class);
                intent.putExtra("pri_num", pri_num);
                intent.putExtra("nickname", nickname);
                startActivity(intent);
            }
        });

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // 로그인 버튼 클릭

                input_id = Edt_login.getText().toString();
                input_password = Edt_pw.getText().toString();
                input_password = Hashing.hashing(input_password);

                if (input_id.equals("") || input_password.equals("")) {

                } else {

                    message = "LOGIN:" + input_id + "," + input_password + "\n";



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


                    try {
                        if (res[0].equals("Login_success")) {
                            pri_num = Integer.parseInt(res[1]);
                            nickname = res[2];

                            Intent intent=new Intent(((AccountActivity)getActivity()),MainActivity.class);
                            intent.putExtra("pri_num", pri_num);
                            intent.putExtra("nickname", nickname);
                            startActivity(intent);
                        } else if (res[0].equals("Login_fail")) {

                        } else {

                        }
                    } catch (Exception e) {

                        e.printStackTrace();
                    }
                }
            }
        });

        signUpBtn = view.findViewById(R.id.login_signup_button);
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // 회원가입 버튼 클릭
                ((AccountActivity) getActivity()).txt.setText("SIGN UP");
                ((AccountActivity) getActivity()).signUpFragment = new AccountSignUpFragment();
                ((AccountActivity)getActivity()).fm.beginTransaction().replace(R.id.LayoutFragment,(((AccountActivity) getActivity()).signUpFragment)).commit();/*프래그먼트 매니저가 프래그먼트를 담당한다!*/
            }
        });
    }



}