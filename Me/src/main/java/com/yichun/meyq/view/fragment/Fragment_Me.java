package com.yichun.meyq.view.fragment;

import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yichun.meyq.Constants;
import com.yichun.meyq.GloableParams;
import com.yichun.meyq.R;
import com.yichun.meyq.common.UserUtils;
import com.yichun.meyq.common.Utils;
import com.yichun.meyq.view.activity.LoginActivity;
import com.yichun.meyq.view.activity.MyCodeActivity;
import com.yichun.meyq.view.activity.PublicActivity;
import com.yichun.meyq.view.activity.SettingActivity;

/**
 * 我
 */
public class Fragment_Me extends Fragment implements OnClickListener {
	private Activity ctx;
	private View layout;
	private TextView tvname, tv_accout;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (layout == null) {
			ctx = this.getActivity();
			layout = ctx.getLayoutInflater().inflate(R.layout.fragment_profile,
					null);
			initViews();
			initData();
			setOnListener();
		} else {
			ViewGroup parent = (ViewGroup) layout.getParent();
			if (parent != null) {
				parent.removeView(layout);
			}
		}
		return layout;
	}

	private void initViews() {
		tvname = (TextView) layout.findViewById(R.id.tvname);
		tv_accout = (TextView) layout.findViewById(R.id.tvmsg);
		String id = Utils.getValue(getActivity(), Constants.User_ID);
		if (id.isEmpty())
			id = "dysen2014";
		tv_accout.setText(this.getText(R.string.user_name) + id);
		if (GloableParams.UserInfos != null) {
			String name = UserUtils.getUserName(ctx);
			if (name != null && !TextUtils.isEmpty(name))
				tvname.setText(name);
		}
	}

	private void setOnListener() {
		layout.findViewById(R.id.view_user).setOnClickListener(this);//用户
		layout.findViewById(R.id.txt_order).setOnClickListener(this);//订单
		layout.findViewById(R.id.txt_collect).setOnClickListener(this);//收藏
		layout.findViewById(R.id.txt_post).setOnClickListener(this);//帖子
		layout.findViewById(R.id.txt_card).setOnClickListener(this);
		layout.findViewById(R.id.txt_smail).setOnClickListener(this);
		layout.findViewById(R.id.txt_setting).setOnClickListener(this);//设置
	}

	private void initData() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.view_user://	用户
			if(true){
				Utils.start_Activity(getActivity(), LoginActivity.class);
			}else {
				Utils.start_Activity(getActivity(), MyCodeActivity.class);
			}
			break;
			case R.id.txt_order:// 订单
				Utils.start_Activity(getActivity(), PublicActivity.class,
						new BasicNameValuePair(Constants.NAME,
								getString(R.string.my_order)));
				break;
		case R.id.txt_post:// 帖子
			Utils.start_Activity(getActivity(), PublicActivity.class,
					new BasicNameValuePair(Constants.NAME,
							getString(R.string.my_post)));
			break;
		case R.id.txt_collect:// 收藏
			Utils.start_Activity(getActivity(), PublicActivity.class,
					new BasicNameValuePair(Constants.NAME,
							getString(R.string.my_collection)));
			break;
		case R.id.txt_card:// 卡包
			Utils.start_Activity(getActivity(), PublicActivity.class,
					new BasicNameValuePair(Constants.NAME,
							getString(R.string.card_bag)));
			break;
		case R.id.txt_smail:// 表情
			Utils.start_Activity(getActivity(), PublicActivity.class,
					new BasicNameValuePair(Constants.NAME,
							getString(R.string.expression)));
			break;
		case R.id.txt_setting:// 设置
			Utils.start_Activity(getActivity(), SettingActivity.class);
			break;
		default:
			break;
		}
	}
}