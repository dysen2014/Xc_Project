package com.dysen.load;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Html;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.dysen.info.DataInfo;
import com.dysen.login_register.LoginDemo;
import com.dysen.qj.wMeter.R;
import com.dysen.myUtil.MyActivityTools;
import com.dysen.myUtil.QuitAllActivity;
import com.dysen.myUtil.ToastDemo;

import com.dysen.type.about.AboutActivity;
import com.dysen.type.ms.MeterSysActivity;
import com.dysen.type.iot.IotActivity;
import com.dysen.type.user.UserActivity;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

/**
 * sen dy 2015-2-27 下午2:41:12
 * 描述: 主页
 */
public class UserDemo extends MyActivityTools {

	String HTTP_IP ;
	public static Button btnLoginJoin;
	public static TextView tvLoginJoin;
	static Button btnLogin;
	static Button btnRegister;
	static Button btnOk;
	static Button btnBefore;
	static Button btnAfter;
	static LinearLayout llPgb;
	private static Handler handler;
	private String uNameLogin, uPwdLogin, uNameRegister, uPwdRegister,
			uPwdRegisterAgain, uPhoneRegister, uEmailRegister, emailFormat;
	public static DataInfo data;
	private String updateUrl;
	private byte[] sendData;
	public static boolean isLoginSuccess, isRegisterSuccess;
	boolean flag_check, isPwdLogin, isPwdRegister, isPwdRegisterAgain;
	String strMsg;
	public static String userName = "dysen";
	public static Integer loginId;
	WebView wbLogin;
	ProgressBar pgb;
	private boolean running;
	static TextView tvPgb;
	private static final int TIMEOUT = 10000;// 10秒
	private static final String encoding = "utf-8";
	static private String str;
	AlertDialog alert;

	static URL url;
	static HttpURLConnection conn;
	static boolean isConnActive;
	CountDownTimer cdTimer;
	Intent intent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.user_demo);
		boolean bl = FlymeSetStatusBarLightMode(getWindow(), true);
//		ToastDemo.myHint(this,   "bl:"+bl);
		boolean b =MIUISetStatusBarLightMode(getWindow(), true);
//		ToastDemo.myHint(this,   "b:"+b);

		// 设置一个Adapter,使用自定义的Adapter
//		this.setListAdapter(new TextImageAdapter(this));
		LinearLayout llUser = (LinearLayout) findViewById(R.id.ll_user);

		idInit();
		HTTP_IP = this.getText(R.string.HTTP_IP).toString();
		System.out.println("IP地址：" + HTTP_IP);

		btnLoginJoin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// if (isLoginSuccess) {
				myDialog();
				// }
			}
		});
		// myUserImg();
	}

	Spinner spinner;
	boolean blMenuItem = true;;

	private void idInit() {

		wbLogin = (WebView) this.findViewById(R.id.wb_login);
		btnLoginJoin = (Button) this.findViewById(R.id.btn_login_join);
		tvLoginJoin = (TextView) this.findViewById(R.id.tv_login_join);
	}

	
	public void btnUser(View v) {
		if (!isLoginSuccess) {
			myDialog();
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
		if (!isLoginSuccess) {
			myDialog();
		}else {
			intent = new Intent(this, MeterSysActivity.class);
			intent.putExtra("login_id", loginId);
			intent.putExtra("user_name", userName);
			startActivity(intent);
//			startActivityForResult(intent, 2);
		}
	}

	public void btnAlertor(View v) {
		if (!isLoginSuccess) {
			myDialog();
		}else {
			intent = new Intent(this, IotActivity.class);
			intent.putExtra("login_id", loginId);
			startActivity(intent);
		}
	}

	public void btnAbout(View v) {
		if (!isLoginSuccess) {
			myDialog();
		}else {
			intent = new Intent(this, AboutActivity.class);
			intent.putExtra("login_id", loginId);
			startActivity(intent);
		}
	}

	/**
	 * dysen 2015-6-12 下午4:40:30 info: 登录图像
	 */
	public void myUserImg() {
		wbLogin.setVisibility(View.VISIBLE);
		wbLogin.setBackgroundColor(Color.parseColor("#00000000"));
		wbLogin.loadUrl(HTTP_IP + "/img/user/buddy_"
				+ (int) (Math.random() * 10) + "_mb5ucom.png");
		// wbLogin.loadUrl(HTTP_IP+"/app/style/images"+ "/user/buddy_"
		// + (int) (Math.random() * 10) + "_mb5ucom.png");
	}

	/**
	 * dysen 2015-3-28 下午2:56:57 info: 自定义弹框
	 */
	public AlertDialog.Builder alertDemo(String str) {

		AlertDialog.Builder alert = new AlertDialog.Builder(this)
				.setTitle(str)
				.setInverseBackgroundForced(true)
				.setPositiveButton("知道咯 \ue021",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();

							}
						});

		return alert;
	}

	// 设置回退
	private static boolean isExit = false;
	private static boolean hasTask = false;
	Timer tExit = new Timer();
	TimerTask task = new TimerTask() {
		@Override
		public void run() {
			isExit = false;
			hasTask = true;
		}
	};

	// 覆盖Activity类的onKeyDown(int keyCoder,KeyEvent event)方法
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {

			if (isExit == false) {
				isExit = true;
				Toast.makeText(this, "再按一次退出程序\ue021", Toast.LENGTH_SHORT)
						.show();
				if (!hasTask) {
					tExit.schedule(task, 2000);// 2s 内点击两次返回键则退出
				}
			} else {
				// finish();
				 QuitApp();
				// startActivity(new Intent(this, ButtonBar.class));
			}

		}
		return false;
	}

	public void QuitApp() {
		new AlertDialog.Builder(this)
				.setTitle("提示 ")
				// \ue114
				.setMessage("您 ! 确定要退出  ")
				// \ue021 \ue020
				.setPositiveButton("确定 ",// \ue021
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int whichButton) {

								finish(); // 加这个
											// 可以退出整个程序(所有
											// activity)
								// 退出整个程序
								QuitAllActivity.getInstance().exit();

							}
						})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int whichButton) {
						new ToastDemo().myHint(UserDemo.this, "欢迎回来 ");// \ue021\ue021\ue021
					}
				}).show();
	}

	/**
	 * sen dy
	 * 
	 * 2015-6-23 下午4:06:27
	 * 
	 * info: 提示用户登录,注册
	 */
	public void myDialog() {
		View v = LayoutInflater.from(this).inflate(R.layout.login_register,
				null);
		Button btnCancel, btnOkPress;
		btnCancel = (Button) v.findViewById(R.id.btn_cancel_normal);
		btnOkPress = (Button) v.findViewById(R.id.btn_ok_press);

		btnCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				alert.dismiss();
			}
		});
		btnOkPress.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				alert.dismiss();

				startActivityForResult(new Intent(UserDemo.this, LoginDemo.class), 1);
//				isLoginSuccess = true;
//				btnLoginJoin
//						.setBackgroundResource(R.drawable.pressed);
//				tvLoginJoin.setText(Html
//						.fromHtml("<font  color=\"green\"><u>"
//								+ userName + "</u></font>"));// "登录成功"
			}
		});

		alert = new AlertDialog.Builder(this).setTitle("提示 ").setView(v).show();
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

}
