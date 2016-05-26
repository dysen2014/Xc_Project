package com.dysen.type.ms;

import android.app.AlertDialog;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Vibrator;

import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.dysen.afinalUtil.tMeter;
import com.dysen.myUtil.MyActivityTools;
import com.dysen.myUtil.MyDateUtils;
import com.dysen.myUtil.ToastDemo;
import com.dysen.myUtil.httpUtil.UpDateData;
import com.dysen.myUtil.picpopupwindow.SelectPicPopupWindow;
import com.dysen.qj.wMeter.R;

import java.util.Date;
import java.util.List;

public class WaterEntryActivity extends MyActivityTools {

    TextView tvHint;
    TextView tvUp, tvNext, tvUserName, tvUserNum, tvUserPhone, tvUserAddr, tvMeterNum, tvLast, tvThis, tvWaterVolume;
    EditText etNum, etNum2;
    Button btnUp, btnNext, btnOperation, btnDel;
    Button btnEntryOk;

    long num;//当前止码
    int  i=0;
    List<tMeter> list;
    Intent intent;
    int lastNum, thisNum,  readMtSum, readMtCompleted;
    String meterNum, userNum;
    int id;

    SoundPool sp;//声明一个SoundPool
    int musicId;//定义一个整型用load（）；来设置suondID
    int resId;
    //自定义的弹出框类
    SelectPicPopupWindow menuWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ms_water_entry);
        boolean bl = FlymeSetStatusBarLightMode(getWindow(), true);
//		ToastDemo.myHint(this,   "bl:"+bl);
        boolean b =MIUISetStatusBarLightMode(getWindow(), true);
//		ToastDemo.myHint(this,   "b:"+b);


        sp = new SoundPool(1, AudioManager.STREAM_MUSIC, 5);//第一个参数为同时播放数据流的最大个数，第二数据流类型，第三为声音质量
//
        initBtn();

        i = getIntent().getExtras().getInt("item")-1;//传过来的item 为1
        System.out.println("i:"+i);

        getEntryData(i);
    }

    /**
     * 实例化SelectPicPopupWindow
     */
    private void initSelectPicPopupWindow() {

        //实例化SelectPicPopupWindow
        menuWindow = new SelectPicPopupWindow(this, new View.OnClickListener(){//为弹出窗口实现监听类

            public void onClick(View v) {
                menuWindow.dismiss();

                if (v.getId() == R.id.btn_entry_ok) {//数据录入

                    if ("".equals(etNum.getText().toString())) {
                        ToastDemo.myHint(WaterEntryActivity.this, "录入的数据不能为空，请重新录入");
                    }else {
                        List<tMeter> list = dbMeter.findAllByWhere(tMeter.class, "id=" + "\'" + id + "\'");
                        if (list.size() >0) {
                            for (int i = 0; i < list.size(); i++) {
                                System.out.println(id+"上传状态："+list.get(i).getStatusUpdate()
                                        +"抄表状态："+list.get(i).getStatusRead() +"起码："+list.get(i).getReadStart());
                            }
                        }

                        if ("1".equals(list.get(0).getStatusUpdate())){//已上传
                            ToastDemo.myHint(WaterEntryActivity.this, "数据已更新！不能重复抄表！");

                        }else {
                            if("1".equals(list.get(0).getStatusRead())){//已抄表

                                lastNum = list.get(0).getReadStart();//上次起码也是(当次起码)
                                thisNum = Integer.valueOf(etNum.getText().toString());
                                if (thisNum < lastNum) {//起码大于止码时

                                    myDialog("重新录入的数据可能有误！是否更新？");//提示操作员是否更新数据
                                }else {//正常录入数据
                                    myDialog("数据已经录入，是否继续重新录入");
                                }
                            }else{
                                lastNum = list.get(0).getReadEnd();//上次起码也是(当次起码)
                                thisNum = Integer.valueOf(etNum.getText().toString());//当次止码
                                System.out.println("上月止码："+lastNum+"\t本月止码："+thisNum);
                                if (thisNum < lastNum) {//起码大于止码时

                                    myDialog("录入的数据可能有误！是否更新？");//提示操作员是否更新数据
                                } else if (thisNum == lastNum) {//起码等于止码时

                                    ToastDemo.myHint(WaterEntryActivity.this, "该用户本月无用水记录！");//提示操作员是否更新数据
                                    updateEntry();
                                }else {//正常录入数据

                                    updateEntry();
                                }
                            }
                        }
                    }
                } else if(v.getId() == R.id.btn_payment_history){//查看历史

                    intent = new Intent(WaterEntryActivity.this, PaymentHistoryActivity.class);
                    intent.putExtra("meter_num", tvMeterNum.getText().toString());
                    intent.putExtra("user_name", tvUserName.getText().toString());
                    startActivity(intent);
                }else if(v.getId() == R.id.btn_data_update){//数据上传

                    startActivity(new Intent(WaterEntryActivity.this, UpdateActivity.class));
                }
            }
        });
        //显示窗口
        menuWindow.showAtLocation(this.findViewById(R.id.main), Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0); //设置layout在PopupWindow中显示的位置
    }


    /**
     * 更新录入数据
     */
    private void updateEntry() {

        meter.setReadEnd(thisNum);
        meter.setMeterID(meterNum);
        meter.setReadStart(lastNum);
        meter.setTimeAccount(MyDateUtils.formatDate(new Date(), "yy-MM-dd HH:mm:ss"));
        meter.setStatusRead("1");//标志已抄
        meter.setStatusUpdate("0");//标志未上传
        meter.setAccountFeeAll(Double.valueOf(thisNum - lastNum)*2);
//        meter.setUserNum(userNum);
//        meter.setReadMtCompleted(readMtCompleted);
//        List<tMeter> l = dbMeter.findAllByWhere(tMeter.class, "id=" + "\'" + id + "\'");
//        long lThis1 = l.get(0).getReadEnd();
//        long lLast1 = l.get(0).getReadStart();
        dbMeter.update(meter, "id=" + "\'" + id + "\'");
//        l = dbMeter.findAllByWhere(tMeter.class, "id=" + "\'" + id + "\'");
//        long lThis2 = l.get(0).getReadEnd();
//        long lLast2 = l.get(0).getReadStart();
//        System.out.println(lThis1 + "******" + lThis2);
//        if (lThis1 == lThis2 && lLast1 == lLast2)
//            ToastDemo.myHint(WaterEntryActivity.this, "录入失败");
//        else {
//            ToastDemo.myHint(WaterEntryActivity.this, "录入成功");
            // 添加震动效果，提示用户删除完成
            Vibrator mVibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            mVibrator.vibrate(1000);
            ++i;
            getEntryData(i);
//        }
    }

    public void initBtn(){

        btnOperation = (Button) this.findViewById(R.id.btn_entry_operation);
        btnDel = (Button) this.findViewById(R.id.btn_entry_del);
        btnUp = (Button) this.findViewById(R.id.btn_entry_up);
        btnNext = (Button) this.findViewById(R.id.btn_entry_next);
        tvHint = (TextView) this.findViewById(R.id.tv_hint);
        tvUp = (TextView) this.findViewById(R.id.tv_entry_up);
        tvUserName = (TextView) this.findViewById(R.id.tv_entry_user_name);
        tvUserNum = (TextView) this.findViewById(R.id.tv_entry_user_num);
        tvUserPhone = (TextView) this.findViewById(R.id.tv_entry_user_phone);
        tvUserAddr = (TextView) this.findViewById(R.id.tv_entry_user_addr);
        tvMeterNum = (TextView) this.findViewById(R.id.tv_entry_meter_num);
        tvLast = (TextView) this.findViewById(R.id.tv_entry_last);
        tvThis = (TextView) this.findViewById(R.id.tv_entry_this);
        tvNext = (TextView) this.findViewById(R.id.tv_entry_next);
        tvWaterVolume = (TextView) this.findViewById(R.id.tv_entry_this_volume);

        etNum = (EditText) this.findViewById(R.id.et_entry_num);
        etNum2 = (EditText) this.findViewById(R.id.et_entry_num2);

        this.findViewById(R.id.btn_entry_1).setOnClickListener(new BtnClick());
        this.findViewById(R.id.btn_entry_2).setOnClickListener(new BtnClick());
        this.findViewById(R.id.btn_entry_3).setOnClickListener(new BtnClick());
        this.findViewById(R.id.btn_entry_4).setOnClickListener(new BtnClick());
        this.findViewById(R.id.btn_entry_5).setOnClickListener(new BtnClick());
        this.findViewById(R.id.btn_entry_6).setOnClickListener(new BtnClick());
        this.findViewById(R.id.btn_entry_7).setOnClickListener(new BtnClick());
        this.findViewById(R.id.btn_entry_8).setOnClickListener(new BtnClick());
        this.findViewById(R.id.btn_entry_9).setOnClickListener(new BtnClick());
        this.findViewById(R.id.btn_entry_0).setOnClickListener(new BtnClick());

        btnOperation.setOnClickListener(new BtnClick());
        btnDel.setOnClickListener(new BtnClick());
        btnUp.setOnClickListener(new BtnClick());
        btnNext.setOnClickListener(new BtnClick());

        btnDel.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {

                // 添加震动效果，提示用户删除完成
                Vibrator mVibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                mVibrator.vibrate(200);
                etNum2.setText("");
                etNum.setText("");
                tvWaterVolume.setText("月使用量");
                return false;
            }
        });
    }

    private class BtnClick implements View.OnClickListener{

        @Override
        public void onClick(View v) {

            if(v.getId() == R.id.btn_entry_1){

                keyPressed(KeyEvent.KEYCODE_1);
               resId = R.raw.s1;

            }else if(v.getId() == R.id.btn_entry_2){

                keyPressed(KeyEvent.KEYCODE_2);
                resId = R.raw.s2;
            }else if(v.getId() == R.id.btn_entry_3){

                keyPressed(KeyEvent.KEYCODE_3);
                resId = R.raw.s3;
            }else if(v.getId() == R.id.btn_entry_4){

                keyPressed(KeyEvent.KEYCODE_4);
                resId = R.raw.s4;
            }else if(v.getId() == R.id.btn_entry_5){

                keyPressed(KeyEvent.KEYCODE_5);
                resId = R.raw.s5;
            }else if(v.getId() == R.id.btn_entry_6){

                keyPressed(KeyEvent.KEYCODE_6);
                resId = R.raw.s6;
            }else if(v.getId() == R.id.btn_entry_7){

                keyPressed(KeyEvent.KEYCODE_7);
                resId = R.raw.s7;
            }else if(v.getId() == R.id.btn_entry_8){

                keyPressed(KeyEvent.KEYCODE_8);
                resId = R.raw.s8;
            }else if(v.getId() == R.id.btn_entry_9){

                keyPressed(KeyEvent.KEYCODE_9);
                resId = R.raw.s9;
            }else if(v.getId() == R.id.btn_entry_0){

                keyPressed(KeyEvent.KEYCODE_0);
                resId = R.raw.s0;
            }else if(v.getId() == R.id.btn_entry_up){
                resId = R.raw.s_up;
                --i;
                getEntryData(i);

            }else if(v.getId() == R.id.btn_entry_next){
                resId = R.raw.s_next;
                ++i;
                getEntryData(i);

            }else if(v.getId() == R.id.btn_entry_operation){
                resId = R.raw.s_operation;
                initSelectPicPopupWindow();

            }else if(v.getId() == R.id.btn_entry_del){
                resId = R.raw.s_clear;
                keyPressed(KeyEvent.KEYCODE_DEL);
            }

            musicId = sp.load(WaterEntryActivity.this, resId, 1); //把你的声音素材放到res/raw里，第2个参数即为资源文件，第3个为音乐的优先级
            System.out.println("resId=" + resId + "**********musicId=" + musicId);
            sp.play(musicId, 1, 1, 0, 0, 1);//id, 左声道， 右声道， 优先级，循环，速率
            String ss = etNum.getText().toString();

            if (!"".equals(ss)) {
                    tvWaterVolume.setText("月使用量：" + (Integer.valueOf(ss) - lastNum));
                    btnDel.setEnabled(true);
                    btnDel.setBackgroundResource(R.drawable.btn_enable);
            }else {
                tvWaterVolume.setText("月使用量：" + (0 - lastNum));
                btnDel.setEnabled(false);
                btnDel.setBackgroundResource(R.drawable.btn_dis_enable);
            }
        }
    }

    private void keyPressed(int keyCode) {
        KeyEvent event = new KeyEvent(KeyEvent.ACTION_DOWN, keyCode);

            etNum2.onKeyDown(keyCode, event);
            etNum.onKeyDown(keyCode, event);
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
    }

    /**
     * 从库中获取用户相关信息，方便 操作员录入数据
     * @param p0
     */
    private void getEntryData(int p0) {

        i = p0;
        etNum2.setText("");
        etNum.setText("");
        tvWaterVolume.setText("月使用量");

        list = dbMeter.findAll(tMeter.class);
        if (i < list.size() && i >= 0){
            tvHint.setText(list.get(i).getContactAddr());
            if (i != 0) {
                tvUp.setText("上一户   " + list.get(i - 1).getUserName() + "\t(" + list.get(i - 1).getMeterID() + ")");
                btnUp.setEnabled(true);
                btnUp.setBackgroundResource(R.drawable.btn_enable);
            }else {
                btnUp.setEnabled(false);
                btnUp.setBackgroundResource(R.drawable.btn_dis_enable);
                tvUp.setText("");
                i = 0;
            }
            tvUserName.setText("用户姓名："+list.get(i).getUserName());
//            tvUserNum.setText("用户编号："+list.get(i).getUserNum());
//            tvUserPhone.setText("用户电话：" + list.get(i).getUserPhone());
            tvUserAddr.setText("用户住址：" +list.get(i).getContactAddr());
            tvMeterNum.setText("水表编号：" +list.get(i).getMeterID());
            lastNum = Integer.valueOf(list.get(i).getReadStart());//上月止码
            /**
             * 1月 起码(0) --- 止码(a)
             * 2月 起码(a) --- 止码(b)
             * 3月 起码(b) --- 止码(c)
             * 上月止码变成本月起码 本月止码变成下月起码
             */
            lastNum = Integer.valueOf(list.get(i).getReadStart());//本月起码
            thisNum = Integer.valueOf(list.get(i).getReadEnd());
            if (thisNum < lastNum){
                tvLast.setText("上月止码："+thisNum+ "("+lastNum+")");
            }else{
                lastNum = thisNum;
                tvLast.setText("上月止码："+lastNum);
            }
            meterNum = list.get(i).getMeterID();//表编号
//            userNum = list.get(i).getUserNum();//用户编号
            readMtSum = Integer.valueOf(list.size());//总户数
//            readMtCompleted = list.get(i).getReadMtCompleted();//已抄户数
            id = list.get(i).getId();

            tvThis.setText("本月止码：");//+list.get(i).getThis()

            if (i != list.size()-1) {
                tvNext.setText("下一户 " + list.get(i + 1).getUserName() + "\t(" + list.get(i + 1).getMeterID() + ")");
                btnNext.setEnabled(true);
                btnNext.setBackgroundResource(R.drawable.btn_enable);
            }else {
                tvNext.setText("");
                i = list.size()-1;
                btnNext.setEnabled(false);
                btnNext.setBackgroundResource(R.drawable.btn_dis_enable);
            }
//            objects.add(new ContentItem(i+1, list.get(i).getUserName(), list.get(i).getUserNum(), list.get(i).getUserPhone(), list.get(i).getUserAddr()
//                    , list.get(i).getmLast(), list.get(i).getmThis(), list.get(i).getInfo()));
        } else
        if(i < 0) {
            ToastDemo.myHint(this, "当前已是第一个用户");
            i = 0;
        }else {
            ToastDemo.myHint(this, "当前已是最后一个用户");
            i = list.size() - 1;
        }
    }

//    public void btn(View v){
//
//    }

    AlertDialog alert;

    /**
     * 提示操作员(防止数据录入无效数据)
     */
    public void myDialog(String str) {
        View v = LayoutInflater.from(this).inflate(R.layout.login_register,
                null);
        TextView tvHintTitle;
        Button btnCancel, btnOkPress;

        tvHintTitle = (TextView) v.findViewById(R.id.tv_hint_title);
        btnCancel = (Button) v.findViewById(R.id.btn_cancel_normal);
        btnOkPress = (Button) v.findViewById(R.id.btn_ok_press);

        tvHintTitle.setText(str);
        btnOkPress.setText("更新");
        btnCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                alert.dismiss();
            }
        });
        btnOkPress.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                alert.dismiss();
                updateEntry();
            }
        });

        alert = new AlertDialog.Builder(this).setTitle("提示 ").setView(v).show();
    }
}
