package com.dysen.load;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.text.Html;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dysen.login_register.LoginDemo;
import com.dysen.myUtil.MyActivityTools;
import com.dysen.nfcdemo.NFCDemoActivity;
import com.dysen.qj.wMeter.R;
import com.dysen.type.about.AboutActivity;
import com.dysen.type.iot.IotActivity;
import com.dysen.type.ms.BookActivity;
import com.dysen.type.ms.MeterSysActivity;
import com.dysen.type.ms.PaymentHistoryActivity;
import com.dysen.type.ms.UpdateActivity;
import com.dysen.type.user.UserActivity;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    String HTTP_IP ;
    int loginId;
    String userName;
    boolean isLoginSuccess;
    TextView tvLoginJoin;
    Button btnLoginJoin;
    AlertDialog alert;
    LinearLayout llLogin;
    Intent intent;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE); // 无title

        setContentView(R.layout.activity_main);
        boolean bl = MyActivityTools.FlymeSetStatusBarLightMode(getWindow(), true);
//		ToastDemo.myHint(this,   "bl:"+bl);
        boolean b =MyActivityTools.MIUISetStatusBarLightMode(getWindow(), true);
//		ToastDemo.myHint(this,   "b:"+b);


//        TextView tv = (TextView) this.findViewById(R.id.tv_user);
//        Typeface iconfont = Typeface.createFromAsset(getAssets(), "iconfont/iconfont.ttf");
//        tv.setTypeface(iconfont);

        HTTP_IP = this.getText(R.string.HTTP_IP).toString();
        System.out.println("IP地址：" + HTTP_IP);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setVisibility(View.GONE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();

                View v = LayoutInflater.from(MainActivity.this).inflate(R.layout.activity_function_menu,
                        null);
                v.setBackgroundColor(Color.argb(0,0,0,0));
                alert = MyActivityTools.myDialog(MainActivity.this, v, "功能菜单", Gravity.BOTTOM);
//               alert = new AlertDialog.Builder(MainActivity.this).create();
//
//                alert.show();
                alert.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失

                alert.setOnKeyListener(new DialogInterface.OnKeyListener() {
                    @Override
                    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                        if (keyCode == event.KEYCODE_BACK)
                            fab.setVisibility(View.VISIBLE);;
                        return false;
                    }
                });
                if (alert.isShowing())
                    fab.setVisibility(View.GONE);
                else
                    fab.setVisibility(View.VISIBLE);

                alert.getWindow().setGravity(Gravity.BOTTOM);
                alert.getWindow().setContentView(v);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View v =  navigationView.inflateHeaderView(R.layout.nav_header_main);
        tvLoginJoin = (TextView) v.findViewById(R.id.tv_login_join);
        btnLoginJoin = (Button) v.findViewById(R.id.btn_login_join);
        llLogin = (LinearLayout) v.findViewById(R.id.ll_login);
        btnLoginJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLoginSuccess)
                    myDialog("您要重新登录");
                else
                myDialog("请登录");
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        if (!isLoginSuccess) {
            myDialog("请登录");
        }else {
            // Handle navigation view item clicks here.
            int id = item.getItemId();

            if (id == R.id.nav_book) {
                // Handle the camera action
                startActivity(new Intent(this, BookActivity.class));
            } else if (id == R.id.nav_nfc) {
            startActivity(new Intent(this, NFCDemoActivity.class));
            } else if (id == R.id.nav_count_find) {
//            startActivity(new Intent(this, .class));
            } else if (id == R.id.nav_update) {
                startActivity(new Intent(this, UpdateActivity.class));
            } else if (id == R.id.nav_payment) {
                startActivity(new Intent(this, PaymentHistoryActivity.class));
            } else if (id == R.id.nav_fillorleakage) {
//            startActivity(new Intent(this, .class));
            }
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * sen dy
     *
     * 2015-6-23 下午4:06:27
     *
     * info: 提示用户登录,注册
     */
    public void myDialog(String str) {
        View v = LayoutInflater.from(this).inflate(R.layout.login_register,
                null);

        Button btnCancel, btnOkPress;
        TextView textView;
        textView = (TextView) v.findViewById(R.id.tv_hint_title);
//        if (!"".equals(str))
            textView.setText(str);
        btnCancel = (Button) v.findViewById(R.id.btn_cancel_normal);
        btnOkPress = (Button) v.findViewById(R.id.btn_ok_press);

        btnCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                alert.dismiss();
            }
        });
        btnOkPress.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                alert.dismiss();

                startActivityForResult(new Intent(MainActivity.this, LoginDemo.class), 1);
//				isLoginSuccess = true;
//				btnLoginJoin
//						.setBackgroundResource(R.drawable.pressed);
//				tvLoginJoin.setText(Html
//						.fromHtml("<font  color=\"green\"><u>"
//								+ userName + "</u></font>"));// "登录成功"
            }
        });

        alert = new AlertDialog.Builder(this).create();
//                setView(v).show();
        alert.show();
        alert.getWindow().setContentView(v);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        loginId = data.getExtras().getInt("login_id");
        userName = data.getExtras().getString("user_name");
        isLoginSuccess = data.getExtras().getBoolean("flag_login");

        if (isLoginSuccess) {

            btnLoginJoin
                    .setBackgroundResource(R.drawable.pressed);
            tvLoginJoin.setText(Html
                    .fromHtml("<font  color=\"green\"><u>"
                            + userName + "</u></font>"));// "登录成功"
            // 得到新Activity关闭后返回的数据
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void btnUser(View v) {
        if (alert != null)
        if(alert.isShowing()) {
            alert.dismiss();
            fab.setVisibility(View.VISIBLE);
        }

        if (!isLoginSuccess) {
            myDialog("请登录");
        }else {
            intent = new Intent(this, UserActivity.class);
            intent.putExtra("login_id", loginId);
            intent.putExtra("user_name", userName);

            startActivity(intent);
        }
    }

    /**
     *	dysen
     *	2015-8-27 下午2:56:40
     *	info: 无线自动抄表系统(WAMR) wireless auto meter read
     */
    public void btnWAMR(View v) {
        if (alert != null)
        if(alert.isShowing()) {
            alert.dismiss();
            fab.setVisibility(View.VISIBLE);
        }

        if (!isLoginSuccess) {
            myDialog("请登录");
        }else {
            intent = new Intent(this, MeterSysActivity.class);
            intent.putExtra("login_id", loginId);
            intent.putExtra("user_name", userName);

            startActivity(intent);
//			startActivityForResult(intent, 2);
        }
    }

    public void btnAlertor(View v) {
        if (alert != null)
        if(alert.isShowing()) {
            alert.dismiss();
            fab.setVisibility(View.VISIBLE);
        }

        if (!isLoginSuccess) {
            myDialog("请登录");
        }else {
            intent = new Intent(this, IotActivity.class);
            intent.putExtra("login_id", loginId);

            startActivity(intent);
        }
    }

    public void btnAbout(View v) {
        if (alert != null)
        if(alert.isShowing()) {
            alert.dismiss();
            fab.setVisibility(View.VISIBLE);
        }

        if (!isLoginSuccess) {
            myDialog("请登录");
        }else {
            intent = new Intent(this, AboutActivity.class);
            intent.putExtra("login_id", loginId);

            startActivity(intent);
        }
    }
}
