package com.dysen.test;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.ImageView;

import com.dtr.zbar.build.CameraManager;
import com.dtr.zbar.build.CameraPreview;
import com.dysen.main.R;
import com.dysen.myUtil.ToastDemo;
import com.dysen.test.scanner.ScannerDemo;
import com.dysen.test.scanner.ScannerUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;

public class JsDemo extends Activity {
    private WebView contentWebView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_js_demo);


            contentWebView = (WebView) findViewById(R.id.webview);        // 启用javascript
            contentWebView.getSettings().setJavaScriptEnabled(true);        // 从assets目录下面的加载html
//            contentWebView.loadUrl("file:///android_asset/web.html");
        contentWebView.loadUrl("http://192.168.0.11:8081/web.html");
            contentWebView.addJavascriptInterface(JsDemo.this,"android");

            //Button按钮 无参调用HTML js方法
            findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {                // 无参数调用 JS的方法
                    contentWebView.loadUrl("javascript:javacalljs()");

                }
            });

            //Button按钮 有参调用HTML js方法
            findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {                // 传递参数调用JS的方法
                    contentWebView.loadUrl("javascript:javacalljswith(" + "'http://blog.csdn.net/Leejizhou'" + ")");
                }
            });


        }

        //由于安全原因 targetSdkVersion>=17需要加 @JavascriptInterface
        //JS调用Android JAVA方法名和HTML中的按钮 onclick后的别名后面的名字对应
        @JavascriptInterface
        public void startFunction(){

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ToastDemo.myHint(JsDemo.this, "打开相机");
//                    new AlertDialog.Builder(JsDemo.this).setMessage(text).show();
//                    startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE),  1);
                    startActivity(new Intent(MediaStore.ACTION_IMAGE_CAPTURE));
                }
            });
        }    @JavascriptInterface
        public void startFunction(final String text){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ToastDemo.myHint(JsDemo.this, "打开电话");
                    startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel://" +text)));

                }
            });
        }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            String sdStatus = Environment.getExternalStorageState();
            if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用

                return;
            }
            String name = new DateFormat().format("yyyyMMdd_hhmmss", Calendar.getInstance(Locale.CHINA)) + ".jpg";
            ToastDemo.myHint(this, name);
            Bundle bundle = data.getExtras();
            Bitmap bitmap = (Bitmap) bundle.get("data");// 获取相机返回的数据，并转换为Bitmap图片格式

            FileOutputStream b = null;
            //???????????????????????????????为什么不能直接保存在系统相册位置呢？？？？？？？？？？？？
            File file = new File("/storage/emulated/0/myImage/");
            file.mkdirs();// 创建文件夹
            String fileName = "/storage/emulated/0/myImage/"+name;

            try {
                b = new FileOutputStream(fileName);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
                    b.flush();
                    b.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            ((ImageView) findViewById(R.id.img)).setImageBitmap(bitmap);// 将图片显示在ImageView里
        }
    }
}
