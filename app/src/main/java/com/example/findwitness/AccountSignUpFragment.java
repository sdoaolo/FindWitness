package com.example.findwitness;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

public class AccountSignUpFragment extends Fragment {
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

        Button signIn_Btn = view.findViewById(R.id.signup_button);
        signIn_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("RRRRRRRRR","GO TO SIGN IN");
                /*((AccountActivity)getActivity()).fm = getActivity().getSupportFragmentManager();
                ((AccountActivity)getActivity()).fm .beginTransaction().remove(AccountSignUpFragment.this).commit();
                ((AccountActivity)getActivity()).fm.popBackStack();*/
                ((AccountActivity)getActivity()).replaceFragment(((AccountActivity)getActivity()).signInFragment);
                ((AccountActivity)getActivity()).txt.setText("SIGN IN");
            }
        });
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
