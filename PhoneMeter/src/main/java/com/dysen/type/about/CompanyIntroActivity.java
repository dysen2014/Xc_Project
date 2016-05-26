package com.dysen.type.about;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.TextView;

import com.dysen.myUtil.ToastDemo;
import com.dysen.qj.wMeter.R;
import com.dysen.myUtil.MyActivityTools;

/**
 * 公司简介
 */
public class CompanyIntroActivity extends MyActivityTools {

    String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_intro);

        phone = ((TextView) CompanyIntroActivity.this.findViewById(R.id.tv_tel)).getText().toString();

        this.findViewById(R.id.tv_tel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (phone.length() != 0) {
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:亲爱的" + phone));
                    if (ActivityCompat.checkSelfPermission(CompanyIntroActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    startActivity(intent);
                } else {
                    ToastDemo.myHint(CompanyIntroActivity.this, "请输入号码");
                }
            }
        });
    }

    public void btnBack(View v){
        finish();
    }
}
