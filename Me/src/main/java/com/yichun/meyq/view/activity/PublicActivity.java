package com.yichun.meyq.view.activity;

import android.annotation.TargetApi;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.yichun.meyq.Constants;
import com.yichun.meyq.R;
import com.yichun.meyq.common.Utils;
import com.yichun.meyq.myUtils.SystemBarTintManager;
import com.yichun.meyq.view.BaseActivity;

//公共页面
public class PublicActivity extends BaseActivity implements OnClickListener {
	private TextView txt_title;
	private ImageView img_back;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		setContentView(R.layout.activity_web);
		initViews(this, R.color.app_main_color);

		super.onCreate(savedInstanceState);
	}

	@Override
	protected void initControl() {
		txt_title = (TextView) findViewById(R.id.txt_title);
		String Name = getIntent().getStringExtra(Constants.NAME);
		txt_title.setText(Name);
		img_back = (ImageView) findViewById(R.id.img_back);
		img_back.setVisibility(View.VISIBLE);
	}

	@Override
	protected void initView() {

	}

	@Override
	protected void initData() {
	}

	@Override
	protected void setListener() {
		img_back.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.img_back:
			Utils.finish(PublicActivity.this);
			break;
		default:
			break;
		}
	}

}
