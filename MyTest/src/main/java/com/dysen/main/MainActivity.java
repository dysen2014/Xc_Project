package com.dysen.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.dysen.myUtil.MyActivityTools;
import com.dysen.test.GetOuterIp;
import com.dysen.test.GetPhoneInfo;
import com.dysen.test.GetWifiPwd;
import com.dysen.test.Html5Demo;
import com.dysen.test.JsDemo;
import com.dysen.test.ListDataReverse;
import com.dysen.test.ListLoadRefresh;
import com.dysen.test.Test;
import com.dysen.test.scanner.ScannerDemo;

public class MainActivity extends MyActivityTools {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void btnGetOuterIp(View v){
        startActivity(new Intent(this, GetOuterIp.class));
    }

    public void btnHtml5(View v){
        startActivity(new Intent(this, Html5Demo.class));
    }
    public void btnJsDemo(View v){
        startActivity(new Intent(this, JsDemo.class));
    }

    public void btnDataReverse(View v){
        startActivity(new Intent(this,ListDataReverse.class));
    }

    public void btnLoadRefresh(View v){
        startActivity(new Intent(this,ListLoadRefresh.class));
    }

    public void btnScanner(View v){
        startActivity(new Intent(this, ScannerDemo.class));
    }

    public void btnGetWifiPwd(View v){
        startActivity(new Intent(this, GetWifiPwd.class));
    }

    public void btnGetPhoneInfo(View v){
        startActivity(new Intent(this, GetPhoneInfo.class));
    }

    public void btnTest(View v){
        startActivity(new Intent(this, Test.class));
    }



//    public void btnDataReverse(View v){
//        startActivity(new Intent(this, .class));
//    }
}
