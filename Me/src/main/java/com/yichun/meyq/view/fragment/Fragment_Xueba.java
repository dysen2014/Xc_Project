package com.yichun.meyq.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebStorage;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.yichun.meyq.Constants;
import com.yichun.meyq.GloableParams;
import com.yichun.meyq.R;
import com.yichun.meyq.bean.User;
import com.yichun.meyq.chat.ChatActivity;
import com.yichun.meyq.common.Utils;
import com.yichun.meyq.view.activity.FriendMsgActivity;
import com.yichun.meyq.view.activity.GroupListActivity;
import com.yichun.meyq.view.activity.NewFriendsListActivity;
import com.yichun.meyq.view.activity.PublishUserListActivity;
import com.yichun.meyq.view.activity.SearchActivity;
import com.yichun.meyq.widght.SideBar;

/**
 * 学吧
 */
public class Fragment_Xueba extends Fragment implements OnClickListener,
		OnItemClickListener {
	private Activity ctx;
	private View layout, layout_head;
	private ListView lvContact;
	private SideBar indexBar;
	private TextView mDialogText;
	private WindowManager mWindowManager;
	public WebView mWebView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (layout == null) {
			ctx = this.getActivity();
			layout = ctx.getLayoutInflater().inflate(R.layout.fragment_xueba,
					null);
			mWebView = (WebView) layout.findViewById(R.id.wb_xueba);
			//http://192.168.0.11:8888/
			mWebView.loadUrl("http://sendy1211.eicp.net:18888/html/index.html");//"http://www.baidu.com"
			mWebView.setWebChromeClient(mChromeClient);
			mWebView.setWebViewClient(mWebViewClient);

			WebSettings webSettings = mWebView.getSettings();
			webSettings.setUseWideViewPort(true);//设定支持viewport
			webSettings.setLoadWithOverviewMode(true);   //自适应屏幕
			webSettings.setBuiltInZoomControls(true);
			webSettings.setDisplayZoomControls(false);
			webSettings.setSupportZoom(true);//设定支持缩放

			CookieManager cm = CookieManager.getInstance();
			// 开启Javascript脚本
			webSettings.setJavaScriptEnabled(true);

			// 启用localStorage 和 essionStorage
			webSettings.setDomStorageEnabled(true);

			// 开启应用程序缓存
			webSettings.setAppCacheEnabled(true);
			webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
			webSettings.setAppCacheMaxSize(1024 * 1024 * 10);// 设置缓冲大小，我设的是10M
			webSettings.setAllowFileAccess(true);


			// 启用地理定位
			webSettings.setGeolocationEnabled(true);

			// 开启插件（对flash的支持）
//        webSettings.setPluginsEnabled(true);
			webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
			webSettings.setJavaScriptCanOpenWindowsAutomatically(true);

		} else {
			ViewGroup parent = (ViewGroup) layout.getParent();
			if (parent != null) {
				parent.removeView(layout);
			}
		}
		return layout;
	}

	@Override
	public void onDestroy() {
		mWindowManager.removeView(mDialogText);
		super.onDestroy();
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_search:// 搜索好友及公众号
			Utils.start_Activity(getActivity(), SearchActivity.class);
			break;
		case R.id.layout_addfriend:// 添加好友
			Utils.start_Activity(getActivity(), NewFriendsListActivity.class);
			break;
		case R.id.layout_group:// 群聊
			Utils.start_Activity(getActivity(), GroupListActivity.class);
			break;
		case R.id.layout_public:// 公众号
			Utils.start_Activity(getActivity(), PublishUserListActivity.class);
			break;
		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		User user = GloableParams.UserInfos.get(arg2 - 1);
		if (user != null) {
			Intent intent = new Intent(getActivity(), FriendMsgActivity.class);
			intent.putExtra(Constants.NAME, user.getUserName());
			intent.putExtra(Constants.TYPE, ChatActivity.CHATTYPE_SINGLE);
			intent.putExtra(Constants.User_ID, user.getTelephone());
			getActivity().startActivity(intent);
			getActivity().overridePendingTransition(R.anim.push_left_in,
					R.anim.push_left_out);
		}
	}

	private WebChromeClient mChromeClient = new WebChromeClient() {

		private View myView = null;
		private CustomViewCallback myCallback = null;

		// 配置权限 （在WebChromeClinet中实现）
		@Override
		public void onGeolocationPermissionsShowPrompt(String origin,
													   GeolocationPermissions.Callback callback) {
			callback.invoke(origin, true, false);
			super.onGeolocationPermissionsShowPrompt(origin, callback);
		}

		// 扩充数据库的容量（在WebChromeClinet中实现）
		@Override
		public void onExceededDatabaseQuota(String url,
											String databaseIdentifier, long currentQuota,
											long estimatedSize, long totalUsedQuota,
											WebStorage.QuotaUpdater quotaUpdater) {

			quotaUpdater.updateQuota(estimatedSize * 2);
		}

		// 扩充缓存的容量
		@Override
		public void onReachedMaxAppCacheSize(long spaceNeeded,
											 long totalUsedQuota, WebStorage.QuotaUpdater quotaUpdater) {

			quotaUpdater.updateQuota(spaceNeeded * 2);
		}

		// Android 使WebView支持HTML5 Video（全屏）播放的方法
		@Override
		public void onShowCustomView(View view, CustomViewCallback callback) {
			if (myCallback != null) {
				myCallback.onCustomViewHidden();
				myCallback = null;
				return;
			}

			ViewGroup parent = (ViewGroup) mWebView.getParent();
			parent.removeView(mWebView);
			parent.addView(view);
			myView = view;
			myCallback = callback;
			mChromeClient = this;
		}

		@Override
		public void onHideCustomView() {
			if (myView != null) {
				if (myCallback != null) {
					myCallback.onCustomViewHidden();
					myCallback = null;
				}

				ViewGroup parent = (ViewGroup) myView.getParent();
				parent.removeView(myView);
				parent.addView(mWebView);
				myView = null;
			}
		}
	};
	private WebViewClient mWebViewClient = new WebViewClient() {
		// 处理页面导航
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			// 记得消耗掉这个事件。给不知道的朋友再解释一下，
			// Android中返回True的意思就是到此为止吧,事件就会不会冒泡传递了，我们称之为消耗掉
			return true;

			//webview中判断当前url是否是重定向
//			WebView.HitTestResult hit = view.getHitTestResult();
//			if (hit != null) {
//				Intent intent = new Intent();
//				intent.setAction( "android.intent.action.VIEW");
//				Uri content_url = Uri. parse(url);
//				intent.setData(content_url);
//				Fragment_Xueba.this.startActivity(intent);
//				return true;
//			} else {
//				view.loadUrl(url);
//				return true;
//			}
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			//获取屏幕高度，另外因为网页可能进行缩放了，所以需要乘以缩放比例得出的才是实际的尺寸
			Log.e("HEHE", mWebView.getContentHeight() * mWebView.getScale() + "");
			CookieManager cookieManager = CookieManager.getInstance();
			String CookieStr = cookieManager.getCookie(url);
			Log.e("HEHE", "Cookies = " + CookieStr);
			super.onPageFinished(view, url);
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			super.onPageStarted(view, url, favicon);
		}
	};

}
