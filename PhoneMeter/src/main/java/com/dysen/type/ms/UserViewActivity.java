package com.dysen.type.ms;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.dysen.afinalUtil.tMeter;
import com.dysen.login_register.LoginDemo;
import com.dysen.myUtil.MyActivityTools;
import com.dysen.myUtil.listView.MyListView;
import com.dysen.myUtil.ToastDemo;
import com.dysen.myUtil.adapter_util.AdUserView;
import com.dysen.myUtil.httpUtil.HttpRequest;
import com.dysen.qj.wMeter.R;
import java.util.List;

    public class UserViewActivity extends MyActivityTools implements AdapterView.OnItemClickListener {

    TextView tvHint;
    String code, pid;
    List<tMeter> lMeter;
    String data = "";
    Intent intent;
    MyListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ms_user_view);
        boolean bl = FlymeSetStatusBarLightMode(getWindow(), true);
//		ToastDemo.myHint(this,   "bl:"+bl);
        boolean b =MIUISetStatusBarLightMode(getWindow(), true);
//		ToastDemo.myHint(this,   "b:"+b);


        tvHint = (TextView) this.findViewById(R.id.tv_hint);
        code = getIntent().getStringExtra("code");
        pid = getIntent().getStringExtra("pid");
        System.out.println("pid="+pid+"\ncode="+code);
        intent = new Intent(UserViewActivity.this, UserViewActivity.class);

            getMeter();

    }

        /**
         * 填充数据
         * @param lMeter
         */
        private void entryValue(List<tMeter> lMeter){

            final AdUserView adapter = new AdUserView(this, lMeter);

            lv = (MyListView) findViewById(R.id.lv_user_view);
            lv.setAdapter(adapter);

            //  下拉刷新
            lv.setonRefreshListener(
                    new MyListView.OnRefreshListener() {
                        @Override
                        public void onRefresh() {
                            new AsyncTask<Void, Void, Void>() {
                                protected Void doInBackground(Void... params) {
                                    try {
                                        Thread.sleep(1000);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    return null;
                                }
                                @Override
                                protected void onPostExecute(Void result) {

                                    getMeter();
                                    adapter.notifyDataSetChanged();
                                    lv.onRefreshComplete();
                                }
                            }.execute(null, null, null);
                        }
                    }
            );

            lv.setOnItemClickListener(this);

            final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        final Button fab = (Button) findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();

                    lv.setSelection(0);

                    fab.setVisibility(View.GONE);
                }
            });
            lv.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {
                    // 不滚动时保存当前滚动到的位置
                    if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {

                        int position = lv.getFirstVisiblePosition();
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
        }

        /**
         * 获得表数据(数据库中)
         */
        public void getMeter(){
            lMeter = dbMeter.findAllByWhere(tMeter.class, "code=" + "\'" + code + "\'");
            if (lMeter.size() >0) {
                for (int i = 0; i < lMeter.size(); i++) {
                    tvHint.setText(lMeter.get(i).getContactAddr() + "(" + lMeter.get(i).getTimeAccount() + ")");
                    tvHint.setTextSize(12);
                    entryValue(lMeter);//填充数据
                }
            }else {//"http://duzhaohui.f3322.net:8080/admin/search/findFlowByCode?code="+code
                View v = LayoutInflater.from(this).inflate(
                        R.layout.gif_view, null);
                LoginDemo.myStartGif(this, v, "正在下载表数据 ");//	启动gif动画
                dlBook();
            }
        }

        /**
     * 下载数据
     */
    public void dlBook() {//"http://duzhaohui.f3322.net:80/local.json"

        new Thread(new Runnable() {
            @Override
            public void run() {
                String t = HttpRequest.sendPost(HTTP_IP+"/tMeter.json", "");
                if ("".equals(t)) {
                    ToastDemo.myHint(UserViewActivity.this, "下载数据为空");
                    return;
                } else {
                    System.out.println("下载数据："+t);
                    lMeter = JSON.parseArray(t, tMeter.class);
                    dbMeter.deleteByWhere(tMeter.class, "code=" + "\'" + code + "\'");
                    for (tMeter tm : lMeter) {

                        tm.setStatusRead("0");
                        tm.setStatusUpdate("0");
                        dbMeter.save(tm);//把下载的数据存入数据库
                    }
                }
                intent.putExtra("code", code);
                intent.putExtra("pid", pid);
                LoginDemo.alert.cancel();
                finish();
                startActivity(intent);
            }
        }).start();

//        FinalHttp fp = new FinalHttp();
//        fp.get("http://duzhaohui.f3322.net:80/tMeter.json", null, new AjaxCallBack<String>() {
//            @Override
//            public void onSuccess(String t) {
//                super.onSuccess(t);
//
//                if ("".equals(t)) {
//                    ToastDemo.myHint(UserViewActivity.this, "下载数据为空");
//                    return;
//                } else {
//                    System.out.println("下载数据："+t);
//                    lMeter = JSON.parseArray(t, tMeter.class);
//                    dbMeter.deleteByWhere(tMeter.class, "code=" + "\'" + code + "\'");
//                    for (tMeter tm : lMeter) {
//                        dbMeter.save(tm);//把下载的数据存入数据库
//                    }
//                }
//                intent.putExtra("code", code);
//                intent.putExtra("pid", pid);
//                LoginDemo.alert.cancel();
//                finish();
//                startActivity(intent);
//            }
//
//            @Override
//            public void onFailure(Throwable t, String strMsg) {
//                super.onFailure(t, strMsg);
//                ToastDemo.myHint(UserViewActivity.this, strMsg);
//                System.out.println("http访问异常："+strMsg);
//            }
//            @Override
//            public void onLoading(long count, long current) {
//                super.onLoading(count, current);
//                if (current == count)
//                    System.out.println("http网络异常，请检查服务器是否打开或终端设备网络是否开启");
//            }
//        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Intent i = new Intent(this, WaterEntryActivity.class);

        i.putExtra("item", position);
        System.out.println(position+"************"+id);
        startActivity(i);
//       ToastDemo.myHint(this, "");

    }
}
