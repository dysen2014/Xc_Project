package com.dysen.test;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.TextView;

import com.dysen.main.R;
import com.dysen.myUtil.MyActivityTools;
import com.dysen.myUtil.PhoneInfo;


/**
 * 作者：沈迪 [dysen] on 2016-03-25 10:19.
 * 邮箱：dysen@outlook.com | dy.sen@qq.com
 * 描述：
 */
public class GetPhoneInfo extends MyActivityTools {

    TextView tvInfo;
    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.activity_phone_info);

        tvInfo = (TextView) this.findViewById(R.id.tv_phone_info);

        PhoneInfo phoneInfo = new PhoneInfo(this);

        tvInfo.setText("devices id: " + phoneInfo.getDeviceId()+
        "getPhoneModule: "+phoneInfo.getPhoneModule()+
        "getSerialNumber: "+phoneInfo.getSerialNumber()+
        "getPhoneNumber: " + phoneInfo.getPhoneNumber()+
        "getMacAddress: " + phoneInfo.getMacAddress()+
        "getCpuInfo: "+phoneInfo.getCpuInfo()+
        "getTotalMemory: " + phoneInfo.getTotalMemory());
    }
}
