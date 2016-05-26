package com.dysen.type.ms;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.dysen.afinalUtil.tBook;
import com.dysen.myUtil.MyListView;
import com.dysen.myUtil.ToastDemo;
import com.dysen.myUtil.adapter_util.AdBook;
import com.dysen.myUtil.db.DbUtils;
import com.dysen.myUtil.httpUtil.HttpRequest;
import com.dysen.myUtil.tintManager.SystemBarTintManager;
import com.dysen.qj.wMeter.R;
import com.dysen.myUtil.MyActivityTools;
import net.tsz.afinal.FinalDb;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.List;

public class BookActivity extends MyActivityTools implements View.OnClickListener {

    Button btnBook, btnIcon;
    AdBook bookAdapter;//绑定数据的adpter
    ExpandableListView listView; //控件
    List<tBook> fatherList;   //放置父类数据
    TextView tv;
    Handler handler;
    List<tBook> mBook;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ms_book);
        boolean bl = FlymeSetStatusBarLightMode(getWindow(), true);
//		ToastDemo.myHint(this,   "bl:"+bl);
        boolean b =MIUISetStatusBarLightMode(getWindow(), true);
//		ToastDemo.myHint(this,   "b:"+b);

//        initViews();
        idInit();
        fatherList = new ArrayList<tBook>();

        getBook();
    }

    private SystemBarTintManager tintManager;
    @TargetApi(19)
    private void initViews() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            tintManager = new SystemBarTintManager(this);
//				tintManager.setStatusBarTintColor(getResources().getColor(R.color.app_main_color));
//            tintManager.setStatusBarTintColor(Color.rgb(255, 89, 68));
//            tintManager.setStatusBarTintResource(R.drawable.tint_top);
            tintManager.setStatusBarTintEnabled(true);
        }
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
        if (bookAdapter == null) {
            bookAdapter = new AdBook(fatherList, BookActivity.this);

//            for(int i=0;i<bookAdapter.getGroupCount();i++) {
//                System.out.println(bookAdapter.father.get(i).getCodeName()+"***********&&&&&&&local&&&&&&&&&****************" + bookAdapter.father.get(i).getChilds().get(0).getCodeName());
//            }

            listView.setAdapter(bookAdapter);

        }
    }

    /**
     * 表册数据处理
     * @param mBook
     */
    private void mBookHandle(List<tBook> mBook) {

//            System.out.println(mBook.size()+"集合处理前："+JSON.toJSONString(mBook));

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

    String t = "";
    /**
     * 获得表册数据
     */
    public String dlBook() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                t = HttpRequest.sendPost(HTTP_IP+"local.json", "");
                System.out.println("t:" + t);
        if ("".equals(t)) {
            System.out.println("下载数据为空");

            return ;
        } else {
            mBook = JSON.parseArray(t, tBook.class);
            dbBook.deleteByWhere(tBook.class, "id>0");
            for (tBook tb : mBook)  {
                dbBook.save(tb);//把下载的数据存入数据库
            }

            finish();
            startActivity(new Intent(BookActivity.this, BookActivity.class));
        }
                    }
        }).start();

//                FinalHttp fp = new FinalHttp();
//        try {
//            fp.post(HTTP_IP + "local.json", new AjaxCallBack<String>() {
//                @Override
//                public void onSuccess(String t) {
//                    super.onSuccess(t);
//                    if ("".equals(t)) {
//                        System.out.println("下载数据为空");
//                        return;
//                    } else {
//                        mBook = JSON.parseArray(t, tBook.class);
//                        dbBook.deleteByWhere(tBook.class, "id>0");
//                        for (tBook tb : mBook) {
//
//                            dbBook.save(tb);//把下载的数据存入数据库
//                        }
//
//                        finish();
//                        startActivity(new Intent(BookActivity.this, BookActivity.class));
//
//                    }
//                }
//
//                @Override
//                public void onFailure(Throwable t, String strMsg) {
//                    super.onFailure(t, strMsg);
//                    ToastDemo.myHint(BookActivity.this, strMsg);
//                    System.out.println("http访问异常：" + strMsg);
//                }
//
//                @Override
//                public void onLoading(long count, long current) {
//                    super.onLoading(count, current);
//                    if (current == count)
//                        System.out.println("下载进度："+current/count);
//                }
//            });
//        }catch (Exception e){
//            System.out.println("http网络异常，请检查服务器是否打开或终端设备网络是否开启");
//        }

        return t;

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_){

            tv.setVisibility(View.VISIBLE);
            dlBook();
            if (t.isEmpty()){
                tv.setText("数据，下载失败！\n请检查网络或服务器是否开启！！！");
            }
        }
    }

    private void idInit() {

        tv = (TextView) this.findViewById(R.id.tv_);
        btnBook = (Button) this.findViewById(R.id.btn_);
        TextView tvHint = (TextView) this.findViewById(R.id.tv_hint);
        tvHint.setText("潜江抄表片区");
        listView = (ExpandableListView) this.findViewById(R.id.listView);

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();

                listView.setSelection(0);

                fab.setVisibility(View.GONE);
            }
        });
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // 不滚动时保存当前滚动到的位置
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {

                    int position = listView.getFirstVisiblePosition();
                    if (position > 0) {
                        fab.setVisibility(View.VISIBLE);
                    } else {
                        fab.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                fab.setVisibility(View.GONE);
            }
        });

        btnBook.setBackgroundResource(android.R.drawable.ic_menu_search);
//        btnBook.setText("下载");
        btnBook.setOnClickListener(this);
    }
}
