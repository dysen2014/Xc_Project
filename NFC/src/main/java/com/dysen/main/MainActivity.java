package com.dysen.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;

import com.dysen.nfcdemo.NFCDemoActivity;
import com.dysen.nfcdemo.NFCMifareActivity;
import com.dysen.nfcdemo.R;

/**
 * 作者：沈迪 [dysen] on 2016-03-31 09:18.
 * 邮箱：dysen@outlook.com | dy.sen@qq.com
 * 描述：
 */
public class MainActivity extends Activity implements View.OnClickListener{

    Button btnMifare, btnOther;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        idInit();
    }

    private void idInit() {
        btnMifare = (Button) this.findViewById(R.id.btn_mifare);
        btnOther = (Button) this.findViewById(R.id.btn_other);
        btnMifare.setOnClickListener(this);
        btnOther.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_mifare:
//                startActivity(new Intent(this, NFCMifareActivity.class));
                startActivity(new Intent(this, NFCDemoActivity.class));
                break;
            case R.id.btn_other:
                startActivity(new Intent(this, NFCDemoActivity.class));
                break;
        }
    }
}
