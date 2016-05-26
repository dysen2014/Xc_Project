package com.dysen.test.scanner;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.dysen.main.R;
import com.dysen.myUtil.MyActivityTools;

import java.util.Objects;

public class ScannerDemo extends MyActivityTools {

    TextView tvScanner;
    public static String result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner_demo);

        tvScanner = (TextView) this.findViewById(R.id.tv_scaner_context);
    }

    public void btnScanner(View v){
        send4Result(this, ScannerUtil.class, 1);
    }

    /**
     * @param context 上下文
     * @param cl    目标类
     * @param id    activity id码
     */
    public void send4Result(Context context, Class cl, int id){
        Intent intent = new Intent(context, cl);
        intent.putExtra("request_code", id);
        startActivityForResult(intent, id);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        String str = data.getStringExtra("result");
        tvScanner.setText("扫码结果：\n"+str);
        result = str;
    }
}
