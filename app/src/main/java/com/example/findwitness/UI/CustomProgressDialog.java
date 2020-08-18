package com.example.findwitness.UI;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;

import com.example.findwitness.R;

public class CustomProgressDialog extends Dialog{
    public CustomProgressDialog(Context context) {
        super(context);
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    setContentView(R.layout.custom_dialog);
    }
}
