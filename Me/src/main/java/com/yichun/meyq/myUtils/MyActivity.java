package com.yichun.meyq.myUtils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.yichun.meyq.R;

/**
 * 作者：沈迪 [dysen] on 2016-05-18 07:09.
 * 邮箱：dysen@outlook.com | dy.sen@qq.com
 * 描述：
 */
public class MyActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //实现淡入淡出的效果
        overridePendingTransition(android.R.anim.slide_in_left,android.
                R.anim.slide_out_right);

        //类似 iphone 的进入和退出时的效果
//        overridePendingTransition(R.anim.zoomin, R.anim.zoomout);

    }

    private SystemBarTintManager tintManager;
    @TargetApi(19)
    public void initViews(Activity activity, int rId) {

        requestWindowFeature(Window.FEATURE_NO_TITLE); // 无title

//        //实现淡入淡出的效果
//        overridePendingTransition(android.R.anim.slide_in_left,android.
//                R.anim.slide_out_right);
//
//        //类似 iphone 的进入和退出时的效果
////        overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//            tintManager = new SystemBarTintManager(activity);
////            tintManager.setStatusBarTintColor(getResources().getColor(R.color.app_main_color));
//            tintManager.setStatusBarTintResource(rId);
//            tintManager.setStatusBarTintEnabled(true);
//        }
    }
}
