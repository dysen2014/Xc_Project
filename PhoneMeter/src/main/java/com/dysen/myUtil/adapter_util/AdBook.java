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
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.dysen.afinalUtil.tBook;
import com.dysen.afinalUtil.tMeter;
import com.dysen.myUtil.MyActivityTools;
import com.dysen.myUtil.PercentDemo;
import com.dysen.myUtil.httpUtil.HttpRequest;
import com.dysen.myUtil.httpUtil.UpDateData;
import com.dysen.qj.wMeter.R;
import com.dysen.type.ms.UserViewActivity;

import net.tsz.afinal.FinalHttp;

import java.util.List;

/**
 *  自定义Adapter
 */
public class AdBook extends BaseExpandableListAdapter {

    public List<tBook> father;
    private Context context;
    Intent intent;
    int grpPosition, chdPosition;

    public AdBook(List<tBook> faList, Context context) {  //初始化数据
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

        view = LayoutInflater.from(context).inflate(
                R.layout.activity_ms_sub_book_item, null);
        TextView textView = (TextView) view
                .findViewById(R.id.textview1);
        TextView tvCountAll = (TextView) view
                .findViewById(R.id.tv_count_all);
        TextView tvReadCount = (TextView) view
                .findViewById(R.id.tv_read_count);
        TextView tvReadCompleted = (TextView) view
                .findViewById(R.id.tv_read_completed);
        Button btnDl = (Button) view.findViewById(R.id.btn_dl);

        final List<tMeter> listAll = MyActivityTools.dbMeter.findAllByWhere(tMeter.class, "code=" + "\'" + father.get(groupPosition).getChilds().get(childPosition).getCode() + "\'");

        if (listAll.size() > 0) {
            btnDl.setText("上传");
            List<tMeter> list = MyActivityTools.dbMeter.findAllByWhere(tMeter.class, "statusRead=" + "\'" + 1 + "\'");
            tvCountAll.setText("总户数："+listAll.size());
            tvReadCount.setText("已抄数："+list.size());
            tvReadCompleted.setText("抄表百分比："+ PercentDemo.getPercent((long)list.size(), (long)listAll.size()));
            if(list.size() < listAll.size()){
                btnDl.setText("查看");
                btnDl.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        intent.putExtra("code", father.get(groupPosition).getChilds().get(childPosition).getCode());
                        intent.putExtra("pid", father.get(groupPosition).getChilds().get(childPosition).getPid());
                        context.startActivity(intent);
                    }
                });
            }else {
//            HttpRequest.sendPost("http://192.168.0.222/admin/search/uploadFlowDate", JSON.toJSONString(listAll));
                btnDl.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                    Intent it = new Intent(this, )
                        UpDateData up = new UpDateData("http://192.168.0.222/admin/search/uploadFlowDate", JSON.toJSONString(listAll));
                        System.out.println("服务器应答：" + up.upDateStr);
                    }
                });
            }

        }else {
           btnDl.setText("下载");
            btnDl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    intent.putExtra("code", father.get(groupPosition).getChilds().get(childPosition).getCode());
                    intent.putExtra("pid", father.get(groupPosition).getChilds().get(childPosition).getPid());
                    context.startActivity(intent);
                }
            });
        }

        textView.setText(father.get(groupPosition).getChilds().get(childPosition).getCodeName());
        textView.setTextColor(Color.BLACK);
        return view;
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