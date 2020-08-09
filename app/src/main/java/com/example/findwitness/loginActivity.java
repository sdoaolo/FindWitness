package com.example.findwitness;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class loginActivity  extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        Intent intent=new Intent(this.getIntent());
        //String s=intent.getStringExtra(“text”);
        // textView=(TextView)findViewById(R.id.textview);
        //textView.setText(s);
    }
}
