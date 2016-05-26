package com.dysen.myUtil.adapter_util;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dysen.afinalUtil.tMeter;
import com.dysen.myUtil.MyActivityTools;
import com.dysen.qj.wMeter.R;
import com.dysen.type.ms.UserViewActivity;

import java.util.List;

/**
 * 作者：沈迪 [dysen] on 2016-03-17 12:26.
 * 邮箱：dysen@outlook.com | dy.sen@qq.com
 * 描述：用户浏览页 自定义 Adapter
 */
public class AdUserView extends ArrayAdapter<tMeter> {

//    private Typeface mTypeFaceLight;
//    private Typeface mTypeFaceRegular;
    public List<tMeter> lMeter;
    private Context context;

    public AdUserView(Context context, List<tMeter> list) {
        super(context, 0 , list);
        this.lMeter = list;
        this.context = context;
        }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = LayoutInflater.from(context).inflate(R.layout.activity_ms_user_view_item, null);
        TextView tvUserViewName, tvUserViewPhone, tvUserViewAddr, tvUserViewInfo, tvUserViewItem,
                tvUserViewNum, tvUserViewLast, tvUserViewThis, tvUserViewWaterVolume;

            tvUserViewItem = (TextView) convertView.findViewById(R.id.tv_user_view_item);
            tvUserViewName = (TextView) convertView.findViewById(R.id.tv_user_view_name);
            tvUserViewNum = (TextView) convertView.findViewById(R.id.tv_user_view_num);
            tvUserViewPhone = (TextView) convertView.findViewById(R.id.tv_user_view_phone);
            tvUserViewAddr = (TextView) convertView.findViewById(R.id.tv_user_view_addr);
            tvUserViewLast = (TextView) convertView.findViewById(R.id.tv_user_view_last);
            tvUserViewThis = (TextView) convertView.findViewById(R.id.tv_user_view_this);
            tvUserViewInfo = (TextView) convertView.findViewById(R.id.tv_user_view_info);
            tvUserViewWaterVolume = (TextView) convertView.findViewById(R.id.tv_user_view_water_volume);

        if (lMeter.size() > 0){

            tvUserViewItem.setText("第 "+lMeter.get(position).getId()+" 户");
            tvUserViewName.setText("姓名："+lMeter.get(position).getUserName());
//        tvUserViewNum.setText("用户编号："+lMeter.get(position).ge);
            tvUserViewPhone.setText("表号："+lMeter.get(position).getMeterID());
            tvUserViewAddr.setText("地址："+lMeter.get(position).getContactAddr());
            tvUserViewLast.setText("上月止码："+lMeter.get(position).getReadStart());
            tvUserViewThis.setText("本月止码："+lMeter.get(position).getReadEnd());
            System.out.println("抄表stustas："+lMeter.get(position).getStatusRead());
            if (lMeter.get(position).getReadEnd() < lMeter.get(position).getReadStart()) {
                if ("1".equals(lMeter.get(position).getStatusRead())) {
//            c.uViewInfo = "已抄";
                    tvUserViewInfo.setText("抄表状态：" + "已抄"+("\t(异常)"));
                    tvUserViewInfo.setTextColor(Color.BLUE);
                } else {
//            c.uViewInfo = "未抄";
                    tvUserViewInfo.setText("抄表状态：" + "未抄"+("\t(异常)"));
                    tvUserViewInfo.setTextColor(Color.BLUE);
                }
                tvUserViewWaterVolume.setTextColor(Color.BLUE);
            }else{
                if ("1".equals(lMeter.get(position).getStatusRead())) {
                    tvUserViewInfo.setText("抄表状态：" + "已抄");
                    tvUserViewInfo.setTextColor(Color.GREEN);
                }else {
                    tvUserViewInfo.setText("抄表状态：" + "未抄");
                    tvUserViewInfo.setTextColor(Color.RED);
                }
            }

            tvUserViewWaterVolume.setText("水量：" + (lMeter.get(position).getReadEnd() - lMeter.get(position).getReadStart()));
        }else {

        }
        return convertView;
    }

}
