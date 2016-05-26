package com.dysen.myUtil.WifiPwdUtil;
        import java.io.BufferedReader;
        import java.io.DataInputStream;
        import java.io.DataOutputStream;
        import java.io.InputStreamReader;
        import java.util.ArrayList;
        import java.util.List;
        import java.util.regex.Matcher;
        import java.util.regex.Pattern;

/**
 * 作者：沈迪 [dysen] on 2016-03-07 15:26.
 * 邮箱：dysen@outlook.com | dy.sen@qq.com
 * 描述：访问终端wifi文件工具类
 */
public class wifiManage {

    /**
     * * 1、通过Runtime.getRuntime().exec("su")获取root权限。
     * 2、通过process.getOutputStream()和process.getInputStream()获取终端的输入流和输出流。
     * 3、通过dataOutputStream.writeBytes("cat /data/misc/wifi/*.conf\n")往终端中输入命令。
     *    注意，这里必须要有\n作为换行，否则会与后一个exit命令作为一个命令，最终导致命令执行失败，无法得到结果。
     * 4、通过dataInputStream获取命令执行结果，并以UTF-8的编码转换成字符串。
     * 5、使用正则表达式过滤出wifi的用户名和密码。
     */
    public List<WifiInfo> Read() throws Exception {
        List<WifiInfo> wifiInfos=new ArrayList<WifiInfo>();

        Process process = null;
        DataOutputStream dataOutputStream = null;
        DataInputStream dataInputStream = null;
        StringBuffer wifiConf = new StringBuffer();
        try {
            process = Runtime.getRuntime().exec("su");//获取root权限
            //获取终端的输入流和输出流
            dataOutputStream = new DataOutputStream(process.getOutputStream());
            dataInputStream = new DataInputStream(process.getInputStream());
            //往终端中输入命令
            //注意，这里必须要有\n作为换行，否则会与后一个exit命令作为一个命令，最终导致命令执行失败，无法得到结果
            dataOutputStream
                    .writeBytes("cat /data/misc/wifi/*.conf\n");
            dataOutputStream.writeBytes("exit\n");
            dataOutputStream.flush();
            //通过dataInputStream获取命令执行结果，并以UTF-8的编码转换成字符串
            InputStreamReader inputStreamReader = new InputStreamReader(
                    dataInputStream, "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(
                    inputStreamReader);
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                wifiConf.append(line);
            }
            bufferedReader.close();
            inputStreamReader.close();
            process.waitFor();
        } catch (Exception e) {
            throw e;
        } finally {
            try {
                if (dataOutputStream != null) {
                    dataOutputStream.close();
                }
                if (dataInputStream != null) {
                    dataInputStream.close();
                }
                process.destroy();
            } catch (Exception e) {
                throw e;
            }
        }

        Pattern network = Pattern.compile("network=\\{([^\\}]+)\\}", Pattern.DOTALL);
        Matcher networkMatcher = network.matcher(wifiConf.toString() );
        while (networkMatcher.find() ) {
            String networkBlock = networkMatcher.group();
            Pattern ssid = Pattern.compile("ssid=\"([^\"]+)\"");
            Matcher ssidMatcher = ssid.matcher(networkBlock);

            if (ssidMatcher.find() ) {
                WifiInfo wifiInfo=new WifiInfo();
                wifiInfo.Ssid=ssidMatcher.group(1);
                Pattern psk = Pattern.compile("psk=\"([^\"]+)\"");
                Matcher pskMatcher = psk.matcher(networkBlock);
                if (pskMatcher.find() ) {
                    wifiInfo.Password=pskMatcher.group(1);
                } else {
                    wifiInfo.Password="无密码";
                }
                wifiInfos.add(wifiInfo);
            }
        }
        return wifiInfos;
    }
}
