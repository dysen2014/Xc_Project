package com.dysen.type.ms;

import android.os.Handler;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.dysen.afinalUtil.tBook;
import com.dysen.myUtil.MyActivityTools;
import com.dysen.myUtil.ToastDemo;
import com.dysen.myUtil.adapter_util.AdUpdate;
import com.dysen.myUtil.db.DbUtils;
import com.dysen.qj.wMeter.R;

import java.util.ArrayList;
import java.util.List;

public class UpdateActivity  extends MyActivityTools {

    Button btnBook;
    AdUpdate updateAdapter;//绑定数据的adpter
    ExpandableListView listView; //控件
    List<tBook> fatherList;   //放置父类数据
    TextView tv;
    Handler handler;
    List<tBook> mBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ms_book);
        boolean bl = FlymeSetStatusBarLightMode(getWindow(), true);
//		ToastDemo.myHint(this,   "bl:"+bl);
        boolean b =MIUISetStatusBarLightMode(getWindow(), true);
//		ToastDemo.myHint(this,   "b:"+b);


        idInit();
        fatherList = new ArrayList<tBook>();

        getBook();
    }

    private void idInit() {

        tv = (TextView) this.findViewById(R.id.tv_);

        TextView tvHint = (TextView) this.findViewById(R.id.tv_hint);
        tvHint.setText("数据上传");
        btnBook = (Button) this.findViewById(R.id.btn_);
        listView = (ExpandableListView) this.findViewById(R.id.listView);

    }

    private void getBook() {

        if (DbUtils.checkTableIsEmpty(dbBook)){
            mBook = dbBook.findAll(tBook.class);

            mBookHandle(mBook);
            tv.setVisibility(View.GONE);
        }else {
            tv.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 给 listview 填充数据
     */
    private void entryValue(List<tBook> fatherList){

        //绑定数据
        if (updateAdapter == null) {
            updateAdapter = new AdUpdate(fatherList, UpdateActivity.this);

//            for(int i=0;i<updateAdapter.getGroupCount();i++) {
//                System.out.println(updateAdapter.father.get(i).getCodeName()+"***********&&&&&&&local&&&&&&&&&****************" + updateAdapter.father.get(i).getChilds().get(0).getCodeName());
//            }

            listView.setAdapter(updateAdapter);
        }
    }

    /**
     * 表册数据处理
     * @param mBook
     */
    private void mBookHandle(List<tBook> mBook) {

//            System.out.println(mBook.size()+"集合处理前："+JSON.toJSONString(mBook))

        if (mBook != null && mBook.size() > 0) {
            for (tBook l : mBook) {

                if (l.getPid() == null || l.getPid().equals("")) {

                    fatherList.add(l);
                }
            }
            for (int i = 0; i < fatherList.size(); i++) {
                List<tBook> cls = new ArrayList<tBook>();
                for (tBook l : mBook) {
                    if (fatherList.get(i).getCode().equals(l.getPid())) {
                        cls.add(l);
                    }
                }
                fatherList.get(i).setChilds(cls);
            }
//                System.out.println("集合处理后:"+JSON.toJSONString(fatherList));

            entryValue(fatherList);
        }else {
            ToastDemo.myHint(this, "mBook 数据为空");
        }
    }
}
