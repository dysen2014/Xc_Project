package com.dysen.myUtil.adapter_util;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.dysen.afinalUtil.tBook;
import com.dysen.afinalUtil.tMeter;
import com.dysen.myUtil.MyActivityTools;
import com.dysen.myUtil.PercentDemo;
import com.dysen.myUtil.ToastDemo;
import com.dysen.myUtil.httpUtil.HttpRequest;
import com.dysen.myUtil.httpUtil.UpDateData;
import com.dysen.qj.wMeter.R;
import com.dysen.type.ms.UserViewActivity;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.kymjs.kjframe.http.HttpCallBack;

import java.util.List;
import java.util.Map;

/**
 *  自定义Adapter
 */
public class AdUpdate extends BaseExpandableListAdapter {

    public List<tBook> father;
    private Context context;
    Intent intent;
    int grpPosition, chdPosition;
    tMeter meter;

    TextView tvUpdateStatus;

    public AdUpdate(List<tBook> faList, Context context) {  //初始化数据
        this.father = faList;
        this.context = context;
        intent = new Intent(context, UserViewActivity.class);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return father.get(groupPosition).getChilds().get(childPosition);   //获取父类下面的每一个子类项
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;  //子类位置
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) { //显示子类数据的iew
        View view = null;
        grpPosition = groupPosition;
        chdPosition = childPosition;

        meter = new tMeter();
        view = LayoutInflater.from(context).inflate(
                R.layout.activity_ms_sub_book_item, null);
        final TextView textView = (TextView) view
                .findViewById(R.id.textview1);
        tvUpdateStatus = (TextView) view
                .findViewById(R.id.tv_update_status);
        textView.setTextColor(Color.BLUE);
        TextView tvCountAll = (TextView) view
                .findViewById(R.id.tv_count_all);
        TextView tvReadCount = (TextView) view
                .findViewById(R.id.tv_read_count);
        TextView tvReadCompleted = (TextView) view
                .findViewById(R.id.tv_read_completed);
        Button btnDl = (Button) view.findViewById(R.id.btn_dl);
        btnDl.setVisibility(View.GONE);

        final List<tMeter> listAll = MyActivityTools.dbMeter.findAllByWhere(tMeter.class, "code=" + "\'" + father.get(groupPosition).getChilds().get(childPosition).getCode() + "\'");

        if (listAll.size() > 0) {
            final List<tMeter> l = MyActivityTools.dbMeter.findAllByWhere(tMeter.class, "statusRead=" + "\'" + 1 + "\'"+"AND statusUpdate=" + "\'" + 1 + "\'");
            tvCountAll.setText("总户数："+listAll.size());
            tvReadCount.setText("已上传数："+l.size());
            tvReadCompleted.setText("上传百分比："+ PercentDemo.getPercent((long)l.size(), (long)listAll.size()));

            final List<tMeter> list = MyActivityTools.dbMeter.findAllByWhere(tMeter.class, "statusRead=" + "\'" + 1 + "\'"+"AND statusUpdate=" + "\'" + "0" + "\'");
            if(list.size() > 0){
                btnDl.setVisibility(View.VISIBLE);
                btnDl.setText("上传");
//            HttpRequest.sendPost("http://192.168.0.222/admin/search/uploadFlowDate", JSON.toJSONString(listAll));
                btnDl.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        updateData(list);
                    }
                });
            }else {
                btnDl.setVisibility(View.GONE);
//                ToastDemo.myHint(context, "无上传数据");
            }
        }else {
//            ToastDemo.myHint(context, "无数据");
        }

        textView.setText(father.get(groupPosition).getChilds().get(childPosition).getCodeName());
        textView.setTextColor(Color.BLACK);
        return view;
    }

    private void updateData(final List<tMeter> list) {

        new Thread(new Runnable() {
            @Override
            public void run() {
               String t = HttpRequest.sendPost("http://192.168.0.222/admin/search/uploadFlowData", JSON.toJSONString(list));
                System.out.println("t:" + t);
                if ("".equals(t)) {
                    System.out.println("上传失败");

                    return ;
                } else {
                    tvUpdateStatus.setText("返回信息是+"+ t.toString());
                        System.out.println("返回信息是+"+ t.toString());
                        meter.setStatusUpdate("1");
                        meter.setReadStart(list.get(0).getReadStart());
                        meter.setReadEnd(list.get(0).getReadEnd());
                        meter.setAccountFeeAll(Double.valueOf(list.get(0).getUsed())*2);
                        MyActivityTools.dbMeter.update(meter, "statusRead=" + "\'" + 1 + "\'");
                }
            }
        }).start();

//                    Intent it = new Intent(this, )
//                        UpDateData up = new UpDateData("http://192.168.0.222/admin/search/uploadFlowData", JSON.toJSONString(list));
//            FinalHttp fh = new FinalHttp();
//            AjaxParams params = new AjaxParams();
//            params.put("data", JSON.toJSONString(list));
//            try {
//                fh.post("http://192.168.0.222:8888/admin/search/uploadFlowData", params, new AjaxCallBack(){
//
//                    @Override
//                    public void onLoading(long count, long current) {
//                        tvUpdateStatus.setText("上传进度："+current+"/"+count);
//                    }
//
//                    @Override
//                    public void onSuccess(Object t) {
//                        tvUpdateStatus.setText("返回信息是+"+ t.toString());
//                        System.out.println("返回信息是+"+ t.toString());
//                        meter.setStatusUpdate("1");
//                        meter.setReadStart(list.get(0).getReadStart());
//                        meter.setReadEnd(list.get(0).getReadEnd());
//                        meter.setAccountFeeAll(Double.valueOf(list.get(0).getUsed())*2);
//                        MyActivityTools.dbMeter.update(meter, "statusRead=" + "\'" + 1 + "\'");
//                    }
//
//                    @Override
//                    public void onFailure(Throwable t, String strMsg) {
//                        super.onFailure(t, strMsg);
//                        //加载失败的时候回调
//                        tvUpdateStatus.setText("返回信息是+"+ strMsg);
//                        System.out.println("返回信息是+"+ strMsg);
//                    }
//
//                    @Override
//                    public void onStart() {
//                        super.onStart();
//                        //开始http请求的时候回调
//                    }
//                });
//            }catch (Exception e){
//
//                tvUpdateStatus.setText("上传失败,网络异常");
//                System.out.println("上传失败,网络异常");
//            }
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return father.get(groupPosition).getChilds().size();  //子类item的总数
    }

    @Override
    public Object getGroup(int groupPosition) {   //父类数据
        return father.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return father.size();  ////父类item总数
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;   //父类位置
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(
                R.layout.activity_ms_book_item, null);
        TextView textView = (TextView) view.findViewById(R.id.textview);
        ImageView imageview = (ImageView) view.findViewById(R.id.imageview);
        textView.setText(father.get(groupPosition).getCodeName());
        if (isExpanded)
            imageview.setBackgroundResource(R.drawable.up);
        else
            imageview.setBackgroundResource(R.drawable.down);
        return view;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {  //点击子类触发事件
//        Toast.makeText(context,
//                "第" + groupPosition + "大项，第" + childPosition + "小项被点击了",
//                Toast.LENGTH_LONG).show();
        return true;

    }

}