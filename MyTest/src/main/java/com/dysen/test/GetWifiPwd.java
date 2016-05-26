package com.dysen.test;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.dysen.main.R;
import com.dysen.myUtil.WifiPwdUtil.WifiInfo;
import com.dysen.myUtil.WifiPwdUtil.wifiManage;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：沈迪 [dysen] on 2016-03-07 15:26.
 * 邮箱：dysen@outlook.com | dy.sen@qq.com
 * 描述：
 * 1、通过Runtime.getRuntime().exec("su")获取root权限。
 * 2、通过process.getOutputStream()和process.getInputStream()获取终端的输入流和输出流。
 * 3、通过dataOutputStream.writeBytes("cat /data/misc/wifi/*.conf\n")往终端中输入命令。
 *    注意，这里必须要有\n作为换行，否则会与后一个exit命令作为一个命令，最终导致命令执行失败，无法得到结果。
 * 4、通过dataInputStream获取命令执行结果，并以UTF-8的编码转换成字符串。
 * 5、使用正则表达式过滤出wifi的用户名和密码。
 */
public class GetWifiPwd extends Activity {
    private wifiManage wifiManage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_wifi_pwd);

            wifiManage = new wifiManage();
            try {
                Init();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    public void Init() throws Exception {
        List<WifiInfo> wifiInfos = wifiManage.Read();
        ListView wifiInfosView=(ListView)findViewById(R.id.WifiInfosView);
//        WifiAdapter ad = new WifiAdapter(wifiInfos, GetWifiPwd.this);
//        wifiInfosView.setAdapter(ad);
        List<String> data = new ArrayList<String>();
        for (int i = 0; i < wifiInfos.size(); i++){
            data.add("Wifi:"+wifiInfos.get(i).Ssid+"\n密码:"+wifiInfos.get(i).Password);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.layout_list_1, data);
        wifiInfosView.setAdapter(adapter);
    }

    public class WifiAdapter extends BaseAdapter {

        List<WifiInfo> wifiInfos =null;
        Context con;

        public WifiAdapter(List<WifiInfo> wifiInfos,Context con){
            this.wifiInfos =wifiInfos;
            this.con = con;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return wifiInfos.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return wifiInfos.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            convertView = LayoutInflater.from(con).inflate(android.R.layout.simple_expandable_list_item_1, null);
            TextView tv = (TextView)convertView.findViewById(android.R.id.text1);
            tv.setText("Wifi:"+wifiInfos.get(position).Ssid+"\n密码:"+wifiInfos.get(position).Password);
            return convertView;
        }

    }
}
