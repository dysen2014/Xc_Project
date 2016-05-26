package com.yichun.meyq;

import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.message.BasicNameValuePair;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.chat.EMChatManager;

import com.yichun.meyq.common.Utils;
import com.yichun.meyq.dialog.WarnTipDialog;
import com.yichun.meyq.dialog.TitleMenu.ActionItem;
import com.yichun.meyq.dialog.TitleMenu.TitlePopup;

import com.yichun.meyq.myUtils.SystemBarTintManager;
import com.yichun.meyq.view.activity.AddGroupChatActivity;
import com.yichun.meyq.view.activity.GetMoneyActivity;
import com.yichun.meyq.view.activity.LoginActivity;
import com.yichun.meyq.view.activity.PublicActivity;
import com.yichun.meyq.view.fragment.Fragment_Meiba;
import com.yichun.meyq.view.fragment.Fragment_Zhenba;
import com.yichun.meyq.view.fragment.Fragment_Xueba;
import com.yichun.meyq.view.fragment.Fragment_Me;
import com.yichun.meyq.zxing.CaptureActivity;

public class MainActivity extends FragmentActivity implements OnClickListener {
	private TextView txt_title;
	private ImageView img_right, img_back;
	private WarnTipDialog Tipdialog;
	protected static final String TAG = "MainActivity";
	private TitlePopup titlePopup;
	private TextView unreaMsgdLabel;// 未读消息textview
	private TextView unreadAddressLable;// 未读通讯录textview
	private TextView unreadFindLable;// 发现
	private Fragment[] fragments;
	public Fragment_Meiba meibaFragment;
	private Fragment_Xueba xuebaFragment;
	private Fragment_Zhenba zhenbaFragment;
	private Fragment_Me profileFragment;
	private ImageView[] imagebuttons;
	private TextView[] textviews;
	private String connectMsg = "";
	private int index;
	private int currentTabIndex;// 当前fragment的index
	WebView mWebView;
	public WebView_XueBa webViewXueBa;
	ImageView ivAddr;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);
		App.getInstance2().addActivity(this);
		findViewById();
//		initViews();
		initTabView();
		// initVersion();
		setOnListener();
		initPopWindow();
	}

	private void initTabView() {
		unreaMsgdLabel = (TextView) findViewById(R.id.unread_msg_number);
		unreadAddressLable = (TextView) findViewById(R.id.unread_address_number);
		unreadFindLable = (TextView) findViewById(R.id.unread_find_number);
		meibaFragment = new Fragment_Meiba();
		xuebaFragment = new Fragment_Xueba();
		zhenbaFragment = new Fragment_Zhenba();
		profileFragment = new Fragment_Me();
		webViewXueBa = new WebView_XueBa();

		fragments = new Fragment[] { meibaFragment, xuebaFragment,
				zhenbaFragment, profileFragment };
		imagebuttons = new ImageView[4];
		imagebuttons[0] = (ImageView) findViewById(R.id.ib_meiba);
		imagebuttons[1] = (ImageView) findViewById(R.id.ib_xueba);
		imagebuttons[2] = (ImageView) findViewById(R.id.ib_zhenba);
		imagebuttons[3] = (ImageView) findViewById(R.id.ib_profile);

		imagebuttons[0].setSelected(true);
		textviews = new TextView[4];
		textviews[0] = (TextView) findViewById(R.id.tv_meiba);
		textviews[1] = (TextView) findViewById(R.id.tv_xueba);
		textviews[2] = (TextView) findViewById(R.id.tv_zhenba);
		textviews[3] = (TextView) findViewById(R.id.tv_profile);
		textviews[0].setTextColor(Color.rgb(255,89,168));
		// 添加显示第一个fragment
		getSupportFragmentManager().beginTransaction()
				.add(R.id.fragment_container, meibaFragment)
				.add(R.id.fragment_container, xuebaFragment)
				.add(R.id.fragment_container, profileFragment)
				.add(R.id.fragment_container, zhenbaFragment)
				.hide(xuebaFragment).hide(profileFragment)
				.hide(zhenbaFragment).show(meibaFragment).commit();
	}

	public void onTabClicked(View view) {
		img_right.setVisibility(View.GONE);
		switch (view.getId()) {
		case R.id.re_meiba:

			index = 0;
			txt_title.setText("美吧");
			img_back.setVisibility(View.VISIBLE);
			img_back.setImageResource(R.drawable.icon_back);
//			img_right.setImageResource(R.drawable.icon_add);
			break;
		case R.id.re_xueba:
			index = 1;
			txt_title.setText("学吧");
			img_back.setVisibility(View.VISIBLE);
			img_back.setImageResource(R.drawable.icon_back);
//			img_right.setImageResource(R.drawable.icon_titleaddfriend);
			break;
		case R.id.re_zhenba:
			index = 2;
			txt_title.setText("整吧");
			img_back.setVisibility(View.VISIBLE);
			img_back.setImageResource(R.drawable.icon_back);
			break;
		case R.id.re_profile:
			index = 3;
			txt_title.setText("我");
			img_back.setVisibility(View.GONE);
			break;
		}
		if (currentTabIndex != index) {
			FragmentTransaction trx = getSupportFragmentManager()
					.beginTransaction();
			trx.hide(fragments[currentTabIndex]);
			if (!fragments[index].isAdded()) {
				trx.add(R.id.fragment_container, fragments[index]);
			}
			trx.show(fragments[index]).commit();

		}
		imagebuttons[currentTabIndex].setSelected(false);
		// 把当前tab设为选中状态
		imagebuttons[index].setSelected(true);
		textviews[currentTabIndex].setTextColor(0xFF999999);//F03861
		textviews[index].setTextColor(Color.rgb(255,89,168));
		currentTabIndex = index;
	}

	private void initPopWindow() {
		// 实例化标题栏弹窗
		titlePopup = new TitlePopup(this, LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		titlePopup.setItemOnClickListener(onitemClick);
		// 给标题栏弹窗添加子类
		titlePopup.addAction(new ActionItem(this, R.string.menu_groupchat,
				R.drawable.icon_menu_group));
		titlePopup.addAction(new ActionItem(this, R.string.menu_addfriend,
				R.drawable.icon_menu_addfriend));
		titlePopup.addAction(new ActionItem(this, R.string.menu_qrcode,
				R.drawable.icon_menu_sao));
		titlePopup.addAction(new ActionItem(this, R.string.menu_money,
				R.drawable.abv));
	}

	private TitlePopup.OnItemOnClickListener onitemClick = new TitlePopup.OnItemOnClickListener() {

		@Override
		public void onItemClick(ActionItem item, int position) {
			// mLoadingDialog.show();
			switch (position) {
			case 0:// 发起群聊
				Utils.start_Activity(MainActivity.this,
						AddGroupChatActivity.class);
				break;
			case 1:// 添加朋友
				Utils.start_Activity(MainActivity.this, PublicActivity.class,
						new BasicNameValuePair(Constants.NAME, "添加朋友"));
				break;
			case 2:// 扫一扫
				Utils.start_Activity(MainActivity.this, CaptureActivity.class);
				break;
			case 3:// 收钱
				Utils.start_Activity(MainActivity.this, GetMoneyActivity.class);
				break;
			default:
				break;
			}
		}
	};

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	private void findViewById() {
		txt_title = (TextView) findViewById(R.id.txt_title);
		img_right = (ImageView) findViewById(R.id.img_right);
		img_back = (ImageView) this.findViewById(R.id.img_back);

//		ivAddr = (ImageView) this.findViewById(R.id.img_back);
//		ivAddr.setBackgroundResource(R.drawable.a6a);
//		ivAddr.setOnClickListener(this);
	}

	private SystemBarTintManager tintManager;
	@TargetApi(19)
	private void initViews() {

			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
				getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
				getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
				tintManager = new SystemBarTintManager(this);
//				tintManager.setStatusBarTintColor(getResources().getColor(R.color.app_main_color));
//				tintManager.setStatusBarTintColor(Color.rgb(255,89,68));
				tintManager.setStatusBarTintResource(R.color.app_main_color);
				tintManager.setStatusBarTintEnabled(true);
			}
		// 设置消息页面为初始页面

//		img_back.setVisibility(View.VISIBLE);
//		img_back.setImageResource(R.drawable.icon_back);
//		img_right.setVisibility(View.VISIBLE);
//		img_right.setImageResource(R.drawable.icon_add);
	}

	private void setOnListener() {
		img_back.setOnClickListener(this);
//		img_right.setOnClickListener(this);

	}

	private int keyBackClickCount = 0;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			switch (keyBackClickCount++) {
			case 0:
				Toast.makeText(this, "再次按返回键退出", Toast.LENGTH_SHORT).show();
				Timer timer = new Timer();
				timer.schedule(new TimerTask() {
					@Override
					public void run() {
						keyBackClickCount = 0;
					}
				}, 3000);
				break;
			case 1://3S 内两次点击返回按钮(退出程序)
				EMChatManager.getInstance().logout();// 退出美医圈
				App.getInstance2().exit();
				finish();
				overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
				break;
			}
			return true;
		}else if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
			mWebView.goBack();
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.img_right://功能按钮
			if (index == 0) {
				titlePopup.show(findViewById(R.id.layout_bar));
			} else {
				Utils.start_Activity(MainActivity.this, PublicActivity.class,
						new BasicNameValuePair(Constants.NAME, "添加"));
			}
			break;
			case R.id.img_back://返回到上一页面
				switch (index) {
					case 0:// 美吧(返回)
						img_back.setVisibility(View.VISIBLE);
						backWeb(meibaFragment.mWebView);
						break;
					case 1:// 学吧(返回)
						img_back.setVisibility(View.VISIBLE);
						backWeb(xuebaFragment.mWebView);

						break;
					case 2:// 整吧(返回)
						img_back.setVisibility(View.VISIBLE);
						backWeb(zhenbaFragment.mWebView);
						break;
					case 3:// 我
						break;
					default:
						break;
				}
//				meibaFragment.mWebView.goBack();
//				startActivity(new Intent(this, LoginActivity.class));
				break;
		default:
			break;
		}
	}

	public void backWeb(WebView mWebView){
		if (mWebView.canGoBack())
			mWebView.goBack();
		else
			img_back.setVisibility(View.GONE);
	}

	private void initVersion() {
		// TODO 检查版本更新
		String versionInfo = Utils.getValue(this, Constants.VersionInfo);
		if (!TextUtils.isEmpty(versionInfo)) {
			Tipdialog = new WarnTipDialog(this,
					"发现新版本：  1、更新啊阿三达到阿德阿   2、斯顿阿斯顿撒旦？");
			Tipdialog.setBtnOkLinstener(onclick);
			Tipdialog.show();
		}
	}

	private DialogInterface.OnClickListener onclick = new DialogInterface.OnClickListener() {

		@Override
		public void onClick(DialogInterface dialog, int which) {
			Utils.showLongToast(MainActivity.this, "正在下载...");// TODO
			Tipdialog.dismiss();
		}
	};
}