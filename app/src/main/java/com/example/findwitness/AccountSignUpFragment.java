package com.example.findwitness;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

public class AccountSignUpFragment extends Fragment {
    EditText idEmail, password, checkPassword, nickName;
    String id, pw ,checkPw, name;
    public AccountSignUpFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sign_up, container, false);
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        idEmail = view.findViewById(R.id.signup_email);
        password = view.findViewById(R.id.signup_password);
        checkPassword = view.findViewById(R.id.signup_check_password);
        nickName = view.findViewById(R.id.signup_nickname);

        idEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 입력되는 텍스트에 변화가 있을 때
            }
            @Override
            public void afterTextChanged(Editable arg0) {
                id = idEmail.toString();
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // 입력하기 전에
            }
        });

        Button check_overlap = view.findViewById(R.id.check_overlap);
        check_overlap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("RRRRRRRRR","중복체크");
                //중복 됐을때 메세지창 띄우는 코드

            }
        });
        Button signIn_Btn = view.findViewById(R.id.signup_button);
        signIn_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("RRRRRRRRR","GO TO SIGN IN");
                /*((AccountActivity)getActivity()).fm = getActivity().getSupportFragmentManager();
                ((AccountActivity)getActivity()).fm .beginTransaction().remove(AccountSignUpFragment.this).commit();
                ((AccountActivity)getActivity()).fm.popBackStack();*/

                //password 체크
                if(password.toString() == checkPassword.toString()){

                }

                ((AccountActivity)getActivity()).replaceFragment(((AccountActivity)getActivity()).signInFragment);
                ((AccountActivity)getActivity()).txt.setText("SIGN IN");
            }
        });
    }
}
