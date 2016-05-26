package com.dysen.type.ms;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.dysen.afinalUtil.tMeter;
import com.dysen.load.UserDemo;
import com.dysen.myUtil.MyActivityTools;
import com.dysen.myUtil.MyRandom;
import com.dysen.myUtil.ToastDemo;
import com.dysen.myUtil.adapter_util.ContentItem;
import com.dysen.myUtil.adapter_util.MyAdapter;
import com.dysen.qj.wMeter.R;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import org.kymjs.kjframe.http.HttpCallBack;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MListActivity extends MyActivityTools implements OnItemClickListener {

    EditText etReadMDate;
    TextView tvReadMAddr, tvReadMName;
    MyActivityTools myTools;
    private List<tMeter> meterData;
    String str = "";
    int code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ms_mlist);
        boolean bl = FlymeSetStatusBarLightMode(getWindow(), true);
//		ToastDemo.myHint(this,   "bl:"+bl);
        boolean b =MIUISetStatusBarLightMode(getWindow(), true);
//		ToastDemo.myHint(this,   "b:"+b);

        setTitle("表册");

        code = getIntent().getExtras().getInt("code");

        str = "武汉洪山区,"+"sendy,"+new Date().toLocaleString()+",已计算,"+"雄楚大道名都花园,"+ 100+","+ MyRandom.random2Int(100)+",dy"+",1010110"
                +",13294162501"+",雄楚大道名都花园"+","+MyRandom.random2Int(100)+","+MyRandom.random2Int(100)+100+",详情"+",-1"+","+",1000";
        myTools = new MyActivityTools();

        tvReadMAddr = (TextView) this.findViewById(R.id.tv_readM_addr);
        tvReadMName = (TextView) this.findViewById(R.id.tv_readM_name);
        etReadMDate = (EditText) this.findViewById(R.id.et_readM_date);

        tvReadMAddr.setText("武汉洪山区");
        tvReadMName.setText(UserDemo.userName);
        etReadMDate.setText(new Date().toLocaleString());

        ArrayList<ContentItem> objects = new ArrayList<ContentItem>();

        /**
         * 判断添加的数据是否在数据库已存在
         */
        List<tMeter> list = db.findAll(tMeter.class);
        if (list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                tvReadMAddr.setText(list.get(i).getContactAddr());
                tvReadMAddr.setTextSize(12);
//                tvReadMName.setText(list.get(i).get);
                tvReadMName.setTextSize(12);
                etReadMDate.setText(list.get(i).getTimeAccount());
                etReadMDate.setTextSize(12);
                objects.add(new ContentItem(list.get(i).getTimeAccount(), "", list.get(i).getContactAddr(),
                        Long.valueOf(list.get(i).getUsed()), 0));
            }
        }else{

            params.putHeaders("Cookie", "cookie不能告诉你");
            kjt.jsonPost("http://duzhaohui.f3322.net" + "/app/login/login1?"+"name=" + "dysen" + "&&pw=" + "123" + "&&type=" + 3, params, new HttpCallBack() {
                @Override
                public void onSuccess(String t) {
                    super.onSuccess(t);
                    ToastDemo.myHint(MListActivity.this, t);
                }
            });
            for (int i=0; i<5; i++){
//              objects.add(new ContentItem("2015-12-16", "已计算", "雄楚大道名都花园" + i + "栋", 100, MyRandom.random2Int(100)));
                objects.add(new ContentItem("武汉洪山区", UserDemo.userName, new Date().toLocaleString(), "已计算", "雄楚大道名都花园", 1000, MyRandom.random2Int(1000)
                    , "dy", MyRandom.random2Int(1, 1000), "13294162501", "雄楚大道名都花园" + MyRandom.random2Int(1, 19) + "栋" + MyRandom.random2Int(1, 6) + "单元" + MyRandom.random2Int(1, 11) + (MyRandom.random2Int(7) % 2 == 0 ? "A" : "B")
                    , MyRandom.random2Int(100), MyRandom.random2Int(100) + 100, "详情", "", MyRandom.random2Int(1000)));
            }
        }

        MyAdapter adapter = new MyAdapter(this, objects);

        ListView lv = (ListView) findViewById(R.id.lv_mlist);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Intent i;

        switch (position) {
            case 0:
                i = new Intent(this, UserViewActivity.class);
                startActivity(i);
                ToastDemo.myHint(this, "");
                break;
        }
    }

    public List<tMeter> getMeterData(String str) {
       String[] s = str.split(",");
       List l = java.util.Arrays.asList(s);
        meterData = l;
        meterData.add(meter);
        return meterData;
    }
}
