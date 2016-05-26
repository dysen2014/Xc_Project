package com.dysen.myUtil;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.dysen.afinalUtil.tBook;
import com.dysen.afinalUtil.tGateWay;
import com.dysen.afinalUtil.tLogin;
import com.dysen.afinalUtil.tMeter;
import com.dysen.afinalUtil.tMeter3;
import com.dysen.afinalUtil.tUser;
import com.dysen.qj.wMeter.R;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.FinalDb;

import org.kymjs.kjframe.KJDB;
import org.kymjs.kjframe.KJHttp;
import org.kymjs.kjframe.http.HttpParams;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

/**
 * 作者：沈迪 [dysen] on 2015-12-21 16:33.
 * 邮箱：dysen@outlook.com | dy.sen@qq.com
 * 描述：自定义 Activity 样式
 */
public class MyActivityTools<T> extends FinalActivity {

    public static FinalDb dbBook, dbMeter, db;
    public static tUser user;
    public static tLogin login;
    public static tGateWay gateWay;
    public static tBook book;
    public static tMeter meter;
    public static KJHttp kjt;
    public static HttpParams params;
    public static KJDB kjdb;
    public static String HTTP_IP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /* 全屏显示 */

//		 getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//		 WindowManager.LayoutParams.FLAG_FULLSCREEN); // 全屏
//        requestWindowFeature(Window.FEATURE_NO_TITLE); // 无title
        //透明状态栏
//      getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //透明导航栏
//		getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        //实现淡入淡出的效果
        overridePendingTransition(android.R.anim.slide_in_left,android.
                R.anim.slide_out_right);

        //类似 iphone 的进入和退出时的效果
//        overridePendingTransition(R.anim.zoomin, R.anim.zoomout);

//        getWindow().requestFeature(Window.FEATURE_NO_TITLE);// 无title
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            Window window = getWindow();
//            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS    //透明状态栏
//                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION); //透明导航栏
//            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.setStatusBarColor(Color.TRANSPARENT);
//            window.setNavigationBarColor(Color.TRANSPARENT);
//        }


//        kjdb = KJDB.create(this, "wMeter");
//        kjt = new KJHttp();
//        params = new HttpParams();
        HTTP_IP = this.getText(R.string.HTTP_IP).toString();
        System.out.println("ip:"+HTTP_IP);

        dbBook = FinalDb.create(this, "tBook");
        dbMeter = FinalDb.create(this, "tMeter");
        db = FinalDb.create(this);

        user = new tUser();
        login = new tLogin();
        gateWay = new tGateWay();
        meter = new tMeter();
        book = new tBook();
    }

    /**
     * 设置状态栏字体图标为深色，需要MIUIV6以上
     * @param window 需要设置的窗口
     * @param dark 是否把状态栏字体及图标颜色设置为深色
     * @return  boolean 成功执行返回true
     *
     */
    public static boolean MIUISetStatusBarLightMode(Window window, boolean dark) {
        boolean result = false;
        if (window != null) {
            Class clazz = window.getClass();
            try {
                int darkModeFlag = 0;
                Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                darkModeFlag = field.getInt(layoutParams);
                Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
                if(dark){
                    extraFlagField.invoke(window,darkModeFlag,darkModeFlag);//状态栏透明且黑色字体
                }else{
                    extraFlagField.invoke(window, 0, darkModeFlag);//清除黑色字体
                }
                result=true;
            }catch (Exception e){

            }
        }
        return result;
    }

    /**
     * 设置状态栏图标为深色和魅族特定的文字风格
     * 可以用来判断是否为Flyme用户
     * @param window 需要设置的窗口
     * @param dark 是否把状态栏字体及图标颜色设置为深色
     * @return  boolean 成功执行返回true
     *
     */
    public static boolean FlymeSetStatusBarLightMode(Window window, boolean dark) {
        boolean result = false;
        if (window != null) {
            try {
                WindowManager.LayoutParams lp = window.getAttributes();
                Field darkFlag = WindowManager.LayoutParams.class
                        .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags = WindowManager.LayoutParams.class
                        .getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meizuFlags.setAccessible(true);
                int bit = darkFlag.getInt(null);
                int value = meizuFlags.getInt(lp);
                if (dark) {
                    value |= bit;
                } else {
                    value &= ~bit;
                }
                meizuFlags.setInt(lp, value);
                window.setAttributes(lp);
                result = true;
            } catch (Exception e) {

            }
        }
        return result;
    }


    /**
     * 弹框提示
     * @param context
     * @param view
     * @param id
     * @return
     */
    public static AlertDialog myDialog(Context context, View view, String str ,int id){

        AlertDialog alert = new AlertDialog.Builder(context).create();

        alert.show();
        alert.setTitle(str);
        alert.getWindow().setGravity(id);
        alert.getWindow().setContentView(view);

        return alert;
    }

    public void myBack(View v){
        finish();
    }

//    public static void addHostIp(String ip){
//
//        user.setHostIp(ip);
//        db.save(user);
//    }

    /**
     * 添加网关参数
     * @param hostIp    本地IP
     * @param serIp     服务器IP
     * @param serPort   服务器端口
     * @param gateway   网关名称
     */
    public static void addGateWayCmd2(String hostIp, String serIp, String serPort, String gateway){

        gateWay.setHostIp(hostIp);
        gateWay.setSerIp(serIp);
        gateWay.setSerPort(serPort);
        gateWay.setGateWay(gateway);
        db.save(gateWay);
    }

    /**
     * 添加网关参数
     */
    public void addGateWayCmd(T t){

        db.save(t);
    }

    /**
     * 添加登录数据
     * @param name  登录账号
     * @param pwd   登录密码
     */
    public static void addLoginData2(String name, String pwd){

        login.setLoginName(name);
        login.setLoginPwd(pwd);
        db.save(login);
    }

    /**
     * 添加登录数据
     */
    public void addLoginData(T t){

        db.save(t);
    }

    /**
     * 删除数据
     * @param t   实体类
     * @param id    条件 id( =, <, >id)
     */
    public void delData(Class<T> t, String id) {

        db.deleteByWhere(t, id);

    }

    /**
     * 更新数据
     * @param t   实体类
     * @param id    条件 id( =, <, >id)
     */
    public void updateData(Class<T> t, String id) {
        db.update(t, id);
    }

    /**
     * 查找数据
     * @param t 实体类
     * @return  返回实体类泛型的集合
     */
    public List<T> findAllData(Class<T> t){
        List<T> list = db.findAll(t);

        return list;
    }
}
