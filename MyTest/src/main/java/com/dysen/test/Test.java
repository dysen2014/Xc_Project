package com.dysen.test;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.SystemClock;
import android.widget.TextView;

import com.dysen.main.R;
import com.dysen.myUtil.ToastDemo;

import java.util.Date;

public class Test extends Activity {

    TextView tv;
    String s1, s2;
    int i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        do{
            s1 = new Date().toLocaleString();
            SystemClock.sleep(2000);
            s2 = new Date().toLocaleString();
            System.out.println(s1 + "--->" + s2);
            ToastDemo.myHint(this, s1 + "--->" + s2);
            tv.setText(s1+"--->"+s2);
            ++i;
        }while (i < 5);
    }
}
