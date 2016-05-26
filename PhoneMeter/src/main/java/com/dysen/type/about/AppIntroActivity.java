package com.dysen.type.about;

import android.os.Bundle;
import android.view.View;

import com.dysen.qj.wMeter.R;
import com.dysen.myUtil.MyActivityTools;

/**
 * 应用介绍
 */
public class AppIntroActivity extends MyActivityTools {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_intro);
        boolean bl = FlymeSetStatusBarLightMode(getWindow(), true);
//		ToastDemo.myHint(this,   "bl:"+bl);
        boolean b =MIUISetStatusBarLightMode(getWindow(), true);
//		ToastDemo.myHint(this,   "b:"+b);

    }

    public void btnBack(View v){
        finish();
    }

}
