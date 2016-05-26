package com.dysen.myUtil;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

/**
 * 作者：沈迪 [dysen] on 2016-01-25 14:57.
 * 邮箱：dysen@outlook.com | dy.sen@qq.com
 * 描述：
 */
public class NetUtilsTest {

    /**
     * 获取本机WIFI iP
      */
    public int getWifiIpAddress(Context context) {

        //获取wifi服务
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        //判断wifi是否开启
        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        }
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        // 获取32位整型IP地址
        int ipAddress = wifiInfo.getIpAddress();
        return ipAddress;
    }

    /**
     * 把整型地址转换成 "*.*.*.*" 地址
     * @param ipAddress
     * @return
     */
    public static String int2Ip(long ipAddress) {
//        System.out.println(""+Integer.toHexString(i).toUpperCase());
        //返回整型地址转换成“*.*.*.*”地址
//        return String.format("%d.%d.%d.%d",
//                (ipAddress & 0xff), (ipAddress >> 8 & 0xff),
//                (ipAddress >> 16 & 0xff), (ipAddress >> 24 & 0xff));
        return (ipAddress & 0xFF ) + "." +
                ((ipAddress >> 8 ) & 0xFF) + "." +
                ((ipAddress >> 16 ) & 0xFF) + "." +
                ( ipAddress >> 24 & 0xFF) ;
    }

    /**
     * 把 "*.*.*.*" 地址转换成整型地址
     * @param ipAddress
     * @return
     */
    public static int ip2Int(String ipAddress) {

        String ss = "";
        String[] str  = (ipAddress.replace(".", ",")).split(",");//. 无法分割
        for(int i = 0; i<str.length; i++){
            ss+= Integer.toHexString(Integer.valueOf(str[i])).length()<2?"0"+Integer.toHexString(Integer.valueOf(str[i])):Integer.toHexString(Integer.valueOf(str[i]));
//            System.out.println("ss:"+ss);
        }
        return Integer.valueOf(MyStringConversion.myInverseStrConver(ss, 8), 16);
    }


    /**
     * 3G网络IP
     */
    public static String get3GIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces();
                 en.hasMoreElements();
                    ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf
                        .getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()
                            && inetAddress instanceof Inet4Address) {
                        // if (!inetAddress.isLoopbackAddress() && inetAddress
                        // instanceof Inet6Address) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getLocalIpAddress2() {
        String ipAddress = null;
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface iface : interfaces) {
                if (iface.getDisplayName().equals("eth0")) {
                    List<InetAddress> addresses = Collections.list(iface.getInetAddresses());
                    for (InetAddress address : addresses) {
                        if (address instanceof Inet4Address) {
                            ipAddress = address.getHostAddress();
                        }
                    }
                } else if (iface.getDisplayName().equals("wlan0")) {
                    List<InetAddress> addresses = Collections.list(iface.getInetAddresses());
                    for (InetAddress address : addresses) {
                        if (address instanceof Inet4Address) {
                            ipAddress = address.getHostAddress();
                        }
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return ipAddress;
    }
}
