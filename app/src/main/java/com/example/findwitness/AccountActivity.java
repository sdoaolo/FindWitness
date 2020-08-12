package com.example.findwitness;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class AccountActivity  extends AppCompatActivity {
    private boolean isFragmentSignIn = true ;
    FragmentManager fm = getSupportFragmentManager();
    AccountSignInFragment signInFragment;
    AccountSignUpFragment signUpFragment;
    TextView txt;

    MainActivity mainActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        Intent intent=new Intent(this.getIntent());
        signInFragment = new AccountSignInFragment();

        txt = (TextView) findViewById(R.id.login_text);
        txt.setText("SIGN IN");

        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.add(R.id.LayoutFragment, signInFragment); //초기화면 설정
        fragmentTransaction.commit();
    }

    public void replaceFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.LayoutFragment, fragment).commit();      // Fragment로 사용할 MainActivity내의 layout공간을 선택합니다.
    }
    /*
    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content);
            if (fragment instanceof AccountSignUpFragment) {
                ((AccountSignUpFragment) fragment).onBackPressed();
//                     getSupportFragmentManager().popBackStack();
            }
        }*/
}
