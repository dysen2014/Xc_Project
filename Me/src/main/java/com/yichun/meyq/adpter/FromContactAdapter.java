package com.yichun.meyq.adpter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yichun.meyq.Constants;
import com.yichun.meyq.R;
import com.yichun.meyq.bean.User;
import com.yichun.meyq.chat.ChatActivity;
import com.yichun.meyq.common.ViewHolder;
import com.yichun.meyq.view.activity.FriendMsgActivity;

public class FromContactAdapter extends BaseAdapter {
	protected Context context;
	protected List<User> UserInfos;

	public FromContactAdapter(Context ctx, List<User> UserInfos) {
		context = ctx;
		this.UserInfos = UserInfos;
	}

	@Override
	public int getCount() {
		return UserInfos.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.layout_item_newfriend, parent, false);
		}
		ImageView img_avar = ViewHolder.get(convertView, R.id.img_photo);
		TextView txt_name = ViewHolder.get(convertView, R.id.txt_name);
		TextView txt_msg = ViewHolder.get(convertView, R.id.txt_msg);
		final TextView txt_add = ViewHolder.get(convertView, R.id.txt_add);
		final User user = UserInfos.get(position);
		txt_name.setText(user.getUserName());
		txt_add.setText("添加");
		txt_msg.setText("微信号:" + user.getTelephone() + "   "
				+ user.getUserName());
		txt_add.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

			}
		});
		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, FriendMsgActivity.class);
				intent.putExtra(Constants.NAME, user.getUserName());
				intent.putExtra(Constants.TYPE, ChatActivity.CHATTYPE_SINGLE);
				intent.putExtra(Constants.User_ID, user.getTelephone());
				context.startActivity(intent);
			}
		});
		return convertView;
	}
}
