package com.dysen.type.ms;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.dysen.myUtil.MyActivityTools;
import com.dysen.myUtil.MyRandom;
import com.dysen.myUtil.ToastDemo;
import com.dysen.myUtil.adapter_util.AdPaymentHistory;
import com.dysen.myUtil.adapter_util.AdUserView;
import com.dysen.myUtil.adapter_util.ContentItem;
import com.dysen.qj.wMeter.R;

import java.util.ArrayList;

public class PaymentHistoryActivity extends MyActivityTools implements AdapterView.OnItemClickListener {

    TextView tvHint;
    String userName, meterNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ms_payment_history);
        boolean bl = FlymeSetStatusBarLightMode(getWindow(), true);
//		ToastDemo.myHint(this,   "bl:"+bl);
        boolean b =MIUISetStatusBarLightMode(getWindow(), true);
//		ToastDemo.myHint(this,   "b:"+b);


        userName = getIntent().getExtras().getString("user_name");
        meterNum = getIntent().getExtras().getString("meter_num");

        tvHint = (TextView) this.findViewById(R.id.tv_hint);
        tvHint.setText(userName+"(\t"+meterNum+")");
        tvHint.setTextSize(12);

        ArrayList<ContentItem> objects = new ArrayList<ContentItem>();

        for (int i=0; i<11; i++){
            objects.add(new ContentItem((i+1)+"", MyRandom.random2Int(100), MyRandom.random2Int(100)+100, 0,
                    0, 2, MyRandom.random2Int(1,  6), 0, (MyRandom.random2Int(7)%2==0?"已缴":"未缴")));
        }

        AdPaymentHistory adapter = new AdPaymentHistory(this, objects);

        ListView lv = (ListView) findViewById(R.id.lv_history);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Intent i;

        switch (position) {
            case 0:
//                i = new Intent(this, LineChartActivity1.class);
//                startActivity(i);
                ToastDemo.myHint(this, "asddddddafqwf");
                break;
        }
    }
}
