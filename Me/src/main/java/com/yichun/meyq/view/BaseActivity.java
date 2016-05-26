package com.yichun.meyq.view;

import org.apache.http.message.BasicNameValuePair;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.WindowManager;

import com.yichun.meyq.App;
import com.yichun.meyq.R;
import com.yichun.meyq.common.Utils;
import com.yichun.meyq.dialog.FlippingLoadingDialog;
import com.yichun.meyq.myUtils.SystemBarTintManager;
import com.yichun.meyq.net.NetClient;

public abstract class BaseActivity extends Activity {
	protected Activity context;
	protected NetClient netClient;
	protected FlippingLoadingDialog mLoadingDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		App.getInstance2().addActivity(this);
		netClient = new NetClient(this);
		initControl();
		initView();
		initData();
		setListener();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	public void onPause() {
		super.onPause();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Utils.finish(this);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private SystemBarTintManager tintManager;
	@TargetApi(19)
	public void initViews(Activity activity, int rId) {

//		//实现淡入淡出的效果
//		overridePendingTransition(android.R.anim.slide_in_left,android.
//				R.anim.slide_out_right);
//
//		//类似 iphone 的进入和退出时的效果
////        overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
//
//		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//			getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//			getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//			tintManager = new SystemBarTintManager(activity);
////				tintManager.setStatusBarTintColor(getResources().getColor(R.color.app_main_color));
////			tintManager.setStatusBarTintColor(Color.rgb(255, 89, 68));
//			tintManager.setStatusBarTintResource(rId);
//			tintManager.setStatusBarTintEnabled(true);
//		}
	}

	/**
	 * 绑定控件id
	 */
	protected abstract void initControl();

	/**
	 * 初始化控件
	 */
	protected abstract void initView();

	/**
	 * 初始化数据
	 */
	protected abstract void initData();

	/**
	 * 设置监听
	 */
	protected abstract void setListener();

	/**
	 * 打开 Activity
	 * 
	 * @param activity
	 * @param cls
	 * @param name
	 */
	public void start_Activity(Activity activity, Class<?> cls,
			BasicNameValuePair... name) {
		Utils.start_Activity(activity, cls, name);
	}

	/**
	 * 关闭 Activity
	 * 
	 * @param activity
	 */
	public void finish(Activity activity) {
		Utils.finish(activity);
	}

	/**
	 * 判断是否有网络连接
	 */
	public boolean isNetworkAvailable(Context context) {
		return Utils.isNetworkAvailable(context);
	}

	public FlippingLoadingDialog getLoadingDialog(String msg) {
		if (mLoadingDialog == null)
			mLoadingDialog = new FlippingLoadingDialog(this, msg);
		return mLoadingDialog;
	}
}