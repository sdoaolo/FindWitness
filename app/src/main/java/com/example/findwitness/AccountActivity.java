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
   /* public interface onKeyBackPressedListener { void onBackKey(); }
    private onKeyBackPressedListener mOnKeyBackPressedListener;
    public void setOnKeyBackPressedListener(onKeyBackPressedListener listener) {
        mOnKeyBackPressedListener = listener;
    } //메인에서 토스트를 띄우며 종료확인을 하기 위해 필드선언
    EndToast endToast = new EndToast(this);

    @Override public void onBackPressed() {
        if (mOnKeyBackPressedListener != null) {
            mOnKeyBackPressedListener.onBackKey();
        } else {
            //쌓인 BackStack 여부에 따라 Toast를 띄울지, 뒤로갈지
            if(getSupportFragmentManager().getBackStackEntryCount()==0){
                //* 종료 EndToast Bean 사용
                endToast.showEndToast("종료하려면 한번 더 누르세요.");
            }else{
                super.onBackPressed();
            }
        }
    }*/
}
