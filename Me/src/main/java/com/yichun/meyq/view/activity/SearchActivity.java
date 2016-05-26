package com.yichun.meyq.view.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.yichun.meyq.R;
import com.yichun.meyq.common.Utils;
import com.yichun.meyq.view.BaseActivity;

//搜索
public class SearchActivity extends BaseActivity implements OnClickListener {
	private TextView txt_title;
	private ImageView img_back;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_web);
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void initControl() {
		txt_title = (TextView) findViewById(R.id.txt_title);
		txt_title.setText("搜索");
		img_back = (ImageView) findViewById(R.id.img_back);
		img_back.setVisibility(View.VISIBLE);
	}

	@Override
	protected void initView() {

	}

	@Override
	protected void initData() {
		// TODO 根据时间排序加载 订阅号信息列表
	}

	@Override
	protected void setListener() {
		img_back.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.img_back:
			Utils.finish(SearchActivity.this);
			break;
		default:
			break;
		}
	}

}
