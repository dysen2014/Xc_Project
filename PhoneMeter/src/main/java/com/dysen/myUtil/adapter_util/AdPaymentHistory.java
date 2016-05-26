package com.dysen.myUtil.adapter_util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.dysen.qj.wMeter.R;

import java.util.List;

/**
 * 作者：沈迪 [dysen] on 2016-03-17 15:13.
 * 邮箱：dysen@outlook.com | dy.sen@qq.com
 * 描述：
 */
public class AdPaymentHistory extends ArrayAdapter<ContentItem> {

//    private Typeface mTypeFaceLight;
//    private Typeface mTypeFaceRegular;

        public AdPaymentHistory(Context context, List<ContentItem> objects) {
            super(context, 0, objects);

//        mTypeFaceLight = Typeface.createFromAsset(context.getAssets(), "OpenSans-Light.ttf");
//        mTypeFaceRegular = Typeface.createFromAsset(context.getAssets(), "OpenSans-Regular.ttf");
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ContentItem c = getItem(position);

            ViewHolder holder = null;

            if (convertView == null) {

                holder = new ViewHolder();

                convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_ms_payment_history_item, null);
                holder.tvHistoryDate = (TextView) convertView.findViewById(R.id.tv_history_date);
                holder.tvLast = (TextView) convertView.findViewById(R.id.tv_history_last);
                holder.tvThis = (TextView) convertView.findViewById(R.id.tv_history_this);
                holder.tvInfo = (TextView) convertView.findViewById(R.id.tv_history_info);
                holder.tvWaterVolume = (TextView) convertView.findViewById(R.id.tv_history_water_volume);
                holder.tvPrice = (TextView) convertView.findViewById(R.id.tv_history_price);
                holder.tvLateFees = (TextView) convertView.findViewById(R.id.tv_history_latefees);
                holder.tvWaterFees = (TextView) convertView.findViewById(R.id.tv_history_waterfees);
                holder.tvPaymentStatus = (TextView) convertView.findViewById(R.id.tv_history_payment_status);
//            holder.tv = (TextView) convertView.findViewById(R.id.tv_user_view_);

                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.tvHistoryDate.setText("水量日期："+c.hDate);
            holder.tvLast.setText("上月止码："+c.hLast);
            holder.tvThis.setText("本月止码："+c.hThis);
            holder.tvPrice.setText("当前水价："+c.hPrice);
            holder.tvLateFees.setText("滞纳金："+c.hLateFees);
            holder.tvInfo.setText("加减数量："+c.hInfo);
            holder.tvWaterVolume.setText("实用水量："+c.hWaterVolume);
            holder.tvWaterFees.setText("总水费："+c.hWaterFees);
            holder.tvPaymentStatus.setText("缴费状态："+c.hPaymentStatus);

            return convertView;
        }

        private class ViewHolder {

            TextView tvHistoryDate, tvLast, tvThis, tvInfo, tvWaterVolume,
                    tvPrice, tvLateFees, tvWaterFees, tvPaymentStatus;
        }
}
