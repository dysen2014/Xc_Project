package com.dysen.myUtil;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/**
 * 作者：沈迪 [dysen] on 2015-12-21 16:33.
 * 邮箱：dysen@outlook.com | dy.sen@qq.com
 * 描述：自定义 Activity 样式
 */
public class MyActivityTools extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /* 全屏显示 */
        requestWindowFeature(Window.FEATURE_NO_TITLE); // 无title
//		 getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//		 WindowManager.LayoutParams.FLAG_FULLSCREEN); // 全屏
        //透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //透明导航栏
//		getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

    }

    public void myBack(View v){
        finish();
    }
}
