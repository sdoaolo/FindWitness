package com.example.findwitness;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import static java.lang.Thread.sleep;

public class AccountSignUpFragment extends Fragment {

    Button signUn_Btn, email_same_check_Btn, nickname_same_check_Btn;
    EditText signUp_email, signUp_password, signUp_password_check, sighUp_Nickname;
    String email, password="", password_check="", nickname, message = "", response = "", email_Check_Result = "false", nickname_Check_Result = "false", password_Check_Result = "false";
    TextView password_check_result_Tv;
    private Context context;

    String address = "192.168.0.88";
    int port = Integer.parseInt("9999");

    public AccountSignUpFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = container.getContext();
        return inflater.inflate(R.layout.fragment_sign_up, container, false);
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        email_same_check_Btn = view.findViewById(R.id.btn_same_email_check);
        nickname_same_check_Btn = view.findViewById(R.id.btn_same_nickname_check);
        signUp_email = view.findViewById(R.id.signup_email);
        signUp_password = view.findViewById(R.id.signup_password);
        signUp_password_check = view.findViewById(R.id.signup_check_password);
        sighUp_Nickname = view.findViewById(R.id.signup_nickname);
        password_check_result_Tv = view.findViewById(R.id.TextView_password_check_result);

        Toast.makeText(context, "w", Toast.LENGTH_SHORT).show();

        /*new Thread(new Runnable() {
            @Override
            public void run() {
                //socket_connect.connectSocket("test");
                socket_connect.connectSocket("192.168.0.8", 9999);
            }
        }).start();*/

        email_same_check_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = signUp_email.getText().toString();

                if(email.equals("")){
                    Toast.makeText(context, "이메일을 입력하세요.", Toast.LENGTH_SHORT).show();
                }else {
                    //message = "CHECK_ID : " + email;
                    message = "SAMECHECKID:" + email+ "\n";

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            socket_connect.sendMessage(message);
                        }
                    }).start();

                    try {
                        sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    response = socket_connect.getMessage();

                    if (response.equals("OK")) {
                        Toast.makeText(context, "사용 가능한 아이디입니다.", Toast.LENGTH_SHORT).show();
                        email_Check_Result = "true";
                    } else if(response.equals("NO")){
                        Toast.makeText(context, "이미 사용중인 아이디입니다.", Toast.LENGTH_SHORT).show();
                        signUp_email.setText("");
                        email_Check_Result = "false";
                    }
                }
            }
        });

        /*signUp_email.addTextChangedListener(new TextWatcher() { 검사 뒤 다시 변경했을 경우 사용
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                email_Check_Result = "false";
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });*/

        nickname_same_check_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nickname = sighUp_Nickname.getText().toString();

                if(nickname.equals("")){
                    Toast.makeText(context, "닉네임을 입력하세요.", Toast.LENGTH_SHORT).show();
                }else {
                    //message = "CHECK_ID : " + nickname;
                    message = "SAMECHECKNICKNAME:" + nickname+ "\n";

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            socket_connect.sendMessage(message);
                        }
                    }).start();

                    try {
                        sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    response = socket_connect.getMessage();

                    if (response.equals("OK")) {
                        Toast.makeText(context, "사용 가능한 닉네임입니다.", Toast.LENGTH_SHORT).show();
                        nickname_Check_Result = "true";
                    }else if(response.equals("NO")){
                        Toast.makeText(context, "이미 사용중인 닉네임입니다.", Toast.LENGTH_SHORT).show();
                        sighUp_Nickname.setText("");
                        nickname_Check_Result = "false";
                    }
                }
            }
        });

        signUp_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //password = signUp_password.getText().toString();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!password_check.equals("")){
                    password_equal_check();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                password = signUp_password.getText().toString();
                if(!password_check.equals("")){
                    password_equal_check();
                }
            }
        });

        signUp_password_check.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //password_check = signUp_password_check.getText().toString();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!password.equals("")){
                    password_equal_check();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                password_check = signUp_password_check.getText().toString();
                if(!password.equals("")){
                    password_equal_check();
                }
            }
        });



        signUn_Btn = view.findViewById(R.id.signup_button);
        signUn_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(email_Check_Result.equals("false")){
                    Toast.makeText(context, "email 중복 체크를 해주십시오.", Toast.LENGTH_SHORT).show();
                }else if(nickname_Check_Result.equals("false")){
                    Toast.makeText(context, "닉네임 중복 체크를 해주십시오.", Toast.LENGTH_SHORT).show();
                }else if(password_Check_Result.equals("false")){
                    Toast.makeText(context, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                }else{

                    //message = "ID : "+email+", PASSWORD : " +password+", NICKNAME : "+nickname;
                    message = "SIGNUP:"+email+","+password+","+nickname+ "\n";
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            socket_connect.sendMessage(message);
                        }
                    }).start();

                    try {
                        sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    response = socket_connect.getMessage();

                    Log.d("RRRRRRRRR", "회원가입 응답 : "+response);

                    if(response.equals("success")){
                        Log.d("RRRRRRRRR", "GO TO SIGN IN");
                        Toast.makeText(context, "회원가입 성공", Toast.LENGTH_SHORT).show();
                        /*((AccountActivity)getActivity()).fm = getActivity().getSupportFragmentManager();
                        ((AccountActivity)getActivity()).fm .beginTransaction().remove(AccountSignUpFragment.this).commit();
                        ((AccountActivity)getActivity()).fm.popBackStack();*/
                        ((AccountActivity) getActivity()).replaceFragment(((AccountActivity) getActivity()).signInFragment);
                        ((AccountActivity) getActivity()).txt.setText("SIGN IN");
                    }else if(response.equals("fail")){
                        Log.d("RRRRRRRRR", "SIGN UP FAIL");
                        Toast.makeText(context, "회원가입 실패", Toast.LENGTH_SHORT).show();
                    }else{
                        Log.d("RRRRRRRRR", "SIGN UP FAIL ERROR");
                        Toast.makeText(context, "오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
                        /*signUp_email.setText("");
                        signUp_password.setText("");
                        signUp_password_check.setText("");
                        sighUp_Nickname.setText("");*/

                    }
                }
            }
        });
    }

    public void password_equal_check()
    {
        if(!password_check.equals("")){
            if(password.equals(password_check)){
                password_check_result_Tv.setText("비밀번호가 일치합니다.");
                password_check_result_Tv.setTextColor(Color.parseColor("#00FF00"));
                password_Check_Result = "true";
            }else{
                password_check_result_Tv.setText("비밀번호가 일치하지 않습니다.");
                password_check_result_Tv.setTextColor(Color.parseColor("#FF0000"));
                password_Check_Result = "false";
            }
        }
    }
/*
    public void onClick(View view){
        switch (view.getId()){
            case R.id.signup_button:

                break;
        }
    }
*/
}
