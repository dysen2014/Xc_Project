package com.yichun.meyq.view.fragment;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebStorage;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.yichun.meyq.R;

/**
 * 整吧
 */
public class Fragment_Zhenba extends Fragment  {
	// 发现
	private Activity ctx;
	private View layout;
	public WebView mWebView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (layout == null) {
			ctx = this.getActivity();
			layout = ctx.getLayoutInflater().inflate(R.layout.fragment_zhenba,
					null);
			mWebView = (WebView) layout.findViewById(R.id.wb_zhenba);
			mWebView.loadUrl("http://m.jd.com/?usid=FVYT3IM5AHOXM3FSBLGHUM557I5DT4XXHOJH4L2HVBUSVA2OE37CKH2JGJTS5PTE3VDNCZHYSE52MLNNR5ZNUW5X7U&utm_source=www.jd.com&utm_medium=tuiguang&utm_campaign=t_pcmtiaozhuan_pcmtiaozhuan");//"http://www.baidu.com"
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

	private void initViews() {
		// TODO Auto-generated method stub

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
//				Fragment_Zhenba.this.startActivity(intent);
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
