package com.dysen.test;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.dysen.main.R;
import com.dysen.myUtil.ListDataUtil;
import com.dysen.myUtil.MyActivityTools;
import com.dysen.myUtil.MyListView;

import java.util.ArrayList;
import java.util.List;

public class ListLoadRefresh extends MyActivityTools {

    MyListView lv;
    int i=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_list_load_refresh);

        lv = (MyListView) this.findViewById(R.id.lv);
        setData(ListDataUtil.addData(listS, i+""), lv);
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
//                                myFindBindNumber(UserDemo.loginId);
//                                listItemAdapter.notifyDataSetChanged();
                                setData(ListDataUtil.addData(listS, ++i+""), lv);
                                lv.onRefreshComplete();
                            }
                        }.execute(null, null, null);
                    }
                }
        );
    }

    /**
     * 设置数据
     */
    public ArrayList<String> listS = new ArrayList<String>();
    public void setData(List<String> data, ListView list) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.layout_list_1, data);
        list.setAdapter(adapter);
    }
}
