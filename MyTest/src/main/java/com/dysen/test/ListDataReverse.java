package com.dysen.test;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.dysen.main.R;
import com.dysen.myUtil.ListDataUtil;
import com.dysen.myUtil.MyActivityTools;

import java.util.ArrayList;
import java.util.List;

public class ListDataReverse extends MyActivityTools {

    ListView lv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE); //设置标题栏样式
        //透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setContentView(R.layout.activity_list_data_reverse);


        lv = (ListView) this.findViewById(R.id.lv);
    }

    public static List<?> listReverse(List<String> list) {
        int size = list.size();
        ArrayList<String> ls = new ArrayList<String>();
        for (int i=size; i >0; i--){
            System.out.print("--------------:"+list.get(i-1));
            ls.add(list.get(i-1));
        }
        return ls;
    }
    /**
     *  我的 列表
     * @param data
     * @param list
     */
    public void myList(List<String> data, ListView list){

        listReverse(data);//listview 内容倒序
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.layout_list_1, data);
        list.setAdapter(adapter);
    }

    /**
     * 设置数据
     */
    public ArrayList<String> listS = new ArrayList<String>();
    private ArrayList<String> listR = new ArrayList<String>();

    int i = 0;
   public void btnAdd(View v){

       myList(ListDataUtil.addData(listS, i++ + ""), lv);
   }

    public void btnClear(View v){
        myList(ListDataUtil.clearData(listS), lv);
    }
}
