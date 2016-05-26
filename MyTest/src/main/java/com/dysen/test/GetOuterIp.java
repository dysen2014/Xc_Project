package com.dysen.test;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.WindowManager;
import android.widget.TextView;

import com.dysen.main.R;
import com.dysen.myUtil.IsNetworkAvailable;
import com.dysen.myUtil.MyActivityTools;
import com.dysen.myUtil.NetUtilsTest;
import com.dysen.myUtil.ToastDemo;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class GetOuterIp extends MyActivityTools {

    static URL infoUrl = null;
    static InputStream inStream = null;
    String outerIp;
    NetUtilsTest mNetUtils;
    int mIp;
    String localIp;
    TextView tvContent, tvNetType;
    String mLoadingWords;
    LoadHandler mLoadHandler;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //        requestWindowFeature(Window.FEATURE_NO_TITLE); //设置标题栏样式
        //透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setContentView(R.layout.activity_get_outer_ip);

        mLoadHandler = new LoadHandler();

        tvContent = (TextView) this.findViewById(R.id.tv_content);
        tvNetType = (TextView) this.findViewById(R.id.tv_net_type);

        mNetUtils = new NetUtilsTest();
        if (IsNetworkAvailable.myIsNetworkAvailable(GetOuterIp.this)) {
            String sType = IsNetworkAvailable.getCurrentNetType(GetOuterIp.this);
            if ("wifi".equals(sType)) {//wifi
                mIp = mNetUtils.getWifiIpAddress(GetOuterIp.this);
                ToastDemo.myHint(GetOuterIp.this, "wifi 网络！");

            } else{//手机移动数据
                mIp = NetUtilsTest.ip2Int(NetUtilsTest.get3GIpAddress());
                ToastDemo.myHint(GetOuterIp.this, "手机移动数据！" + sType);
//                tvNetType.setText("当前网络类型："+"手机移动数据！");
            }
            tvNetType.setText("当前网络类型：" + Html.fromHtml("<font  color=\"green\"><u>"
                    + sType+ "</u></font>") + " 网络！" );
            tvNetType.setTextSize(20);
            tvNetType.setTextColor(Color.parseColor("#008000"));
            localIp = NetUtilsTest.int2Ip(mIp);
//                    MyStringConversion.myInverseStrConver(Integer.toHexString(mIp), 8);

            new Thread(runnable).start();
        } else
            ToastDemo.myHint(GetOuterIp.this, "请先打开网络，再操作！");
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            try {
                infoUrl = new URL("http://1212.ip138.com/ic.asp");
//                  infoUrl = new URL("http://www.ip138.com/");
                URLConnection connection = infoUrl.openConnection();
                HttpURLConnection httpConnection = (HttpURLConnection) connection;
                int responseCode = httpConnection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    inStream = httpConnection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inStream, "gb2312"));
                    StringBuilder strber = new StringBuilder();
                    String line = null;
                    while ((line = reader.readLine()) != null)
                        strber.append(line + "\n");
                    inStream.close();
                    //从反馈的结果中提取出IP地址
//                        int start = strber.indexOf("[");
//                        int end = strber.indexOf("]", start + 1);
                    int start = strber.indexOf("<center>");
                    int end = strber.indexOf("</center>", start + 8);
                    line = strber.substring(start + 8, end);
//                      System.out.println(strber+"********************:"+line);
                    outerIp = line;
                }
//                tv.setText("本地ip：" + localIp + "\n" + outerIp);
                resultJson("本地IP：" + localIp + "\n" + outerIp.replace("[", " ").replace("]", " "));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };


    /**
     * m接收子线程的内容传给主线程处理
     */
    private void resultJson(String data) {

        try {
            Message message = Message.obtain();
            message.obj = data;

            mLoadHandler.sendMessage(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //主线程中的handler
    class LoadHandler extends Handler {
        /**
         * 接受子线程传递的消息机制
         */
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            // 刷新页面的文字
            tvContent.setText(msg.obj.toString());
            tvContent.setTextSize(20);
        }
    }

}
