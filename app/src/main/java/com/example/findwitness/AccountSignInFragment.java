package com.example.findwitness;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

public class AccountSignInFragment  extends Fragment {
    /*EditText e_id, e_pw;
    String s_id, s_pw;
    e_id = (EditText) findViewById(R.id.login_email);
    e_pw = (EditText) findViewById(R.id.login_password);
    s_id = e_id.getText().toString();
    s_pw = e_id.getText().toString();*/
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
        Button signInBtn = view.findViewById(R.id.login_signin_button);
        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("RRRRRRRRR","GoToMain");
                Intent intent=new Intent(((AccountActivity)getActivity()),MainActivity.class);
                startActivity(intent);
            }
        });

        Button signUpBtn = view.findViewById(R.id.login_signup_button);
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((AccountActivity) getActivity()).txt.setText("SIGN UP");
                ((AccountActivity) getActivity()).signUpFragment = new AccountSignUpFragment();
                ((AccountActivity)getActivity()).fm.beginTransaction().replace(R.id.LayoutFragment,(((AccountActivity) getActivity()).signUpFragment)).commit();/*프래그먼트 매니저가 프래그먼트를 담당한다!*/
            }
        });
    }
}
