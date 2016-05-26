/** * 邮箱: dysen@outlook.com | dy.sen@qq.com * * 作者: 沈迪 [ dysen ] * * 日期: 2015-9-6  下午4:32:04  2015   *  * 描述:  * */package com.dysen.myUtil;import android.content.Context;import android.graphics.Color;import android.view.Gravity;import android.view.LayoutInflater;import android.view.View;import android.webkit.WebView;import android.webkit.WebViewClient;import android.widget.ImageView;import android.widget.LinearLayout;import android.widget.TextView;import android.widget.Toast;import com.dysen.qj.wMeter.R;/** * @author dysen * */public class ToastDemo {	/**	 * dysen 2015-4-18 上午10:37:19 info: 提示内容	 */	public static void myHint1(Context context, String str) {			Toast toast = Toast.makeText(context, str + " !", Toast.LENGTH_LONG);				toast.setGravity(Gravity.CENTER, 0, 0);        LinearLayout toastView = (LinearLayout) toast.getView();        ImageView imageCodeProject = new ImageView(context);//        imageCodeProject.setImageResource(R.drawable.enn_logo);          toastView.setBackgroundColor(Color.parseColor("#00000000"));        toastView.addView(imageCodeProject, 0);          toast.show();	}		/**       * Basic Standard Toast       * 标准提示信息方式       */  	public static void myHint(Context context, String str) {		Toast toast = null ;		if(toast == null){   		    toast = Toast.makeText(context, str, Toast.LENGTH_LONG);		} else {			toast = null;		}  //		toast.setText("这样木有延时！！！") ;   		toast.show() ;    }            /**       * Adding an Image to the Standard Toast       * 在标准显示方式基础上添加图片       */  	public static void myHint2(Context context, String str) {		       Toast toast = Toast.makeText(context, str, Toast.LENGTH_LONG);        toast.setGravity(Gravity.CENTER, 0, 0);        LinearLayout toastView = (LinearLayout) toast.getView();        ImageView imageCodeProject = new ImageView(context);        imageCodeProject.setImageResource(R.drawable.blue);        toastView.setBackgroundColor(Color.parseColor("#00000000"));        toastView.addView(imageCodeProject, 0);          toast.show();      }            /**       * Creating a Toast with Custom Layout       * 创建自定义的提示信息方式       */  	public static void myHint3(Context context, String tt, String str  ) {		         View v = LayoutInflater.from(context).inflate(R.layout.activity_toast, null);//        ImageView image = (ImageView) layout.findViewById(R.id.tvImageToast);  //        image.setImageResource(R.drawable.page);          WebView wv = (WebView) v.findViewById(R.id.wb);        wv.getSettings().setJavaScriptEnabled(true);    	/* Prevent WebView from Opening the Browser */    	wv.setWebViewClient(new InsideWebViewClient());    	new InsideWebViewClient().shouldOverrideUrlLoading(wv, "http://img14.poco.cn/mypoco/myphoto/20130302/17/3926359420130302172458075_640.jpg");    	        TextView title = (TextView) v.findViewById(R.id.tv_title);        title.setText(tt);          TextView text = (TextView) v.findViewById(R.id.tv_text);        text.setText(str);          Toast toast = new Toast(context);        toast.setGravity(Gravity.RIGHT | Gravity.TOP, 12, 40);        toast.setDuration(Toast.LENGTH_LONG);        toast.setView(v);          toast.show();      } 		/* Class that prevents opening the Browser */	private static class InsideWebViewClient extends WebViewClient {		@Override		public boolean shouldOverrideUrlLoading(WebView view, String url) {			view.loadUrl(url);			return true;		}	}}