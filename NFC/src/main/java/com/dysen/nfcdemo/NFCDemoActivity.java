package com.dysen.nfcdemo;

import android.app.PendingIntent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.Uri;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.os.Bundle;
import java.io.IOException;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.app.Activity;
import android.content.Intent;
import android.os.Parcelable;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dysen.myUtil.MeterIdUtil;
import com.dysen.myUtil.MyUtils;
import com.dysen.myUtil.ToastDemo;

public class NFCDemoActivity extends Activity {

    NfcAdapter nfcAdapter;
    TextView Title, Promt;
    Button Read, Write;
    Spinner sSector, sBlock;
    EditText Key, Data, Sector, Block, wMeterId;
    TextView tvBlockCount;
    RadioGroup RadioGrp;
    RadioButton RadioBtn1,RadioBtn2;
    boolean flag = true;

    PendingIntent mPendingIntent;
    IntentFilter[]  mIntentFilter;
    String[][] mTechList;
    Intent it;
    long meterId;

    LinearLayout llData;

    private ArrayAdapter<CharSequence> adSector, adBlock;
    String strBlock="0", strSector="0", ssBlock = "0";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc_demo);


        idInit();// 资源 id 初始化
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        nfcCheck();

        //适配器
        adSector= ArrayAdapter.createFromResource(this, R.array.sector, android.R.layout.simple_spinner_item);
        //设置样式 为适配器设置下拉列表下拉时的菜单样式
        adSector.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //加载适配器 将适配器添加到下拉列表上
        sSector.setAdapter(adSector);
        //适配器
        adBlock= ArrayAdapter.createFromResource(this, R.array.block, android.R.layout.simple_spinner_item);
        //设置样式 为适配器设置下拉列表下拉时的菜单样式
        adBlock.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //加载适配器 将适配器添加到下拉列表上
        sBlock.setAdapter(adBlock);

        sSector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                strSector = adSector.getItem(position).toString();
                Sector.setText("第:");
//                Block.setText(adBlock.getItem(0));
                if (!"".equals(strSector) && !"".equals(strBlock)){
                    System.out.println("第"+strSector+"区第"+strBlock+"块");
                    ssBlock = (Integer.parseInt(strSector)*4 + Integer.parseInt(strBlock))+"";
                    tvBlockCount.setText("第"+ssBlock+"块");
                    System.out.println("第"+ssBlock+"块");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sBlock.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                strBlock = adBlock.getItem(position).toString();
                        //Block.getText().toString();

                Block.setText("区第:");
                if (!"".equals(strSector) && !"".equals(strBlock)){
                    System.out.println("第"+strSector+"区第"+strBlock+"块");
                    ssBlock = (Integer.parseInt(strSector)*4 + Integer.parseInt(strBlock))+"";
                    tvBlockCount.setText("第"+ssBlock+"块");
                    System.out.println("第"+ssBlock+"块");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        IntentFilter intentFilter = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        IntentFilter intentFilter2 = new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED);
        IntentFilter intentFilter3 = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        try {
            intentFilter.addDataType("*/**");
        } catch (IntentFilter.MalformedMimeTypeException e) {
            e.printStackTrace();
        }
        mIntentFilter = new IntentFilter[]{intentFilter, intentFilter2, intentFilter3};

        Read.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
//                llData.setVisibility(View.VISIBLE);
                meterId = processIntent(it);//读数据
                ToastDemo.myHint(NFCDemoActivity.this, "当前表号："+meterId);
            }
        });

        Write.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                Log.i("Savils", "start");
                llData.setVisibility(View.VISIBLE);
                processIntent2(it);//写数据
                Log.i("Savils", "final");
            }
        });
        RadioGrp.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub
                if(checkedId == RadioBtn1.getId())
                    flag = true;
                if(checkedId == RadioBtn2.getId())
                    flag = false;
            }
        });
    }

    /**
     * nfc 检查 设备是否支持 或 是否 开启
     */
    private void nfcCheck() {
        if (nfcAdapter == null) {
            Title.setText("设备不支持NFC！");
            finish();
            return;
        }else {
            if (!nfcAdapter.isEnabled()) {
                Title.setText("请在系统设置中先启用NFC功能！");
                Intent intent = new Intent(Settings.ACTION_NFC_SETTINGS);
                startActivity(intent);
                return;
            }
        }
    }

    private void idInit() {
        Title = (TextView)findViewById(R.id.Title);
        Promt = (TextView) findViewById(R.id.Promt);
        Read = (Button)findViewById(R.id.Read);
        Write = (Button)findViewById(R.id.Write);
        Read.setEnabled(false);
        Write.setEnabled(false);
        sSector = (Spinner)findViewById(R.id.sSector);
        sBlock = (Spinner)findViewById(R.id.sBlock);
        Sector = (EditText)findViewById(R.id.Sector);
        Block = (EditText)findViewById(R.id.Block);
        Key =(EditText)findViewById(R.id.Key);
        wMeterId = (EditText)findViewById(R.id.meterId);
        Data = (EditText)findViewById(R.id.Data);
        tvBlockCount = (TextView)findViewById(R.id.BlockCount) ;
        llData = (LinearLayout)findViewById(R.id.ll_data);

        Key.setText("2F5F23FEC22F");
        Data.setText("");
        RadioGrp = (RadioGroup)findViewById(R.id.RadioGrp);
        RadioBtn1 = (RadioButton)findViewById(R.id.RadioBtn1);
        RadioBtn2 = (RadioButton)findViewById(R.id.RadioBtn2);
    }

    public void resolveIntent(final Intent intent){
        String action = intent.getAction();
        //得到是否检测到ACTION_TECH_DISCOVERED触发
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
            //处理该intent
            Title.setText("Mifare卡连接成功1");
            NdefMessage[] msg = null;
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            Parcelable[] rawMsg = intent.getParcelableArrayExtra(NfcAdapter.ACTION_NDEF_DISCOVERED);
            if (rawMsg != null){
                msg = new NdefMessage[rawMsg.length];
                for (int i=0; i<rawMsg.length; i++){
                    msg[i] = (NdefMessage) rawMsg[i];
                }
            }else {
                byte[] empty = new byte[]{};
                NdefRecord record = new NdefRecord(NdefRecord.TNF_UNKNOWN, empty, empty, empty);
                NdefMessage message = new NdefMessage(new NdefRecord[]{record});
                msg = new NdefMessage[]{message};
            }
            Promt.setText("Scan a " + intent.getParcelableExtra(NfcAdapter.EXTRA_TAG));
            processNDEFMsg(msg);

        }else if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(intent.getAction())) {
                //处理该intent
                Title.setText("Mifare卡连接成功2");
//            processIntent(intent);
        }else if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())) {
            //处理该intent
            Title.setText("Mifare卡连接成功3");
//            processIntent(intent);
        }else{

        }

        it = intent;
//        meterId = processIntent(it);//读数据
//        ToastDemo.myHint(NFCDemoActivity.this, "当前表号："+meterId);

        Read.setEnabled(true);
        Write.setEnabled(true);

    }

    private void processNDEFMsg(NdefMessage[] msg) {

        if (msg == null || msg.length == 0){

            return;
        }else {

            for (int i=0; i< msg.length; i++){
                int length = msg[i].getRecords().length;
                NdefRecord[] records = msg[i].getRecords();
                for (int j=0; j< length; j++){
                    for(NdefRecord record : records){
                        parseRTDUriRecord(record);
                    }
                }
            }
        }
    }

    private void parseRTDUriRecord(NdefRecord record) {

        Uri uri = MyNFCRecordParse.parseWellKnownUriRecord(record);
        Promt.setText("Uri:\n"+uri);
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        resolveIntent(intent);
        it = intent;
    }

    @Override
    protected void onResume() {
        super.onResume();
        //得到是否检测到ACTION_TECH_DISCOVERED触发
        if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(getIntent().getAction())) {
            //处理该intent
            Title.setText("Mifare卡连接成功");
        }
        if (nfcAdapter != null)
        nfcAdapter.enableForegroundDispatch(this, mPendingIntent, mIntentFilter, mTechList);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (nfcAdapter != null)
        nfcAdapter.disableForegroundDispatch(this);
    }

    /**
     * 读数据
     * Parses the NDEF Message from the intent and prints to the TextView
     */
    private long processIntent(Intent intent) {
        it = intent;

        //取出封装在intent中的TAG
        Tag tagFromIntent = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        for (String tech : tagFromIntent.getTechList()) {
            System.out.println(tech);
        }
        boolean auth = false;
        //读取TAG
        MifareClassic mfc = MifareClassic.get(tagFromIntent);

        try {
            String metaInfo = "";
            //Enable I/O operations to the tag from this TagTechnology object.
            mfc.connect();

            int type = mfc.getType();//获取TAG的类型
            int sectorCount = mfc.getSectorCount();//获取TAG中包含的扇区数
            String typeS = "";
            switch (type) {
                case MifareClassic.TYPE_CLASSIC:
                    typeS = "TYPE_CLASSIC";
                    break;
                case MifareClassic.TYPE_PLUS:
                    typeS = "TYPE_PLUS";
                    break;
                case MifareClassic.TYPE_PRO:
                    typeS = "TYPE_PRO";
                    break;
                case MifareClassic.TYPE_UNKNOWN:
                    typeS = "TYPE_UNKNOWN";
                    break;
            }
            metaInfo += tagFromIntent +"\n卡片类型：" + typeS + "\n共" + sectorCount + "个扇区\n共"
                    + mfc.getBlockCount() + "个块\n存储空间: " + mfc.getSize() + "B\n";
            for (int j = 0; j < sectorCount; j++) {
                //Authenticate a sector with key A.
                if(flag)
                    auth = mfc.authenticateSectorWithKeyA(j,getKey());
                else
                    auth = mfc.authenticateSectorWithKeyB(j,getKey());
                int bCount;
                int bIndex;
                if (auth) {
                    metaInfo += "Sector " + j + ":验证成功\n";
                    // 读取扇区中的块
                    bCount = mfc.getBlockCountInSector(j);
                    bIndex = mfc.sectorToBlock(j);
                    for (int i = 0; i < bCount; i++) {
                        byte[] data = mfc.readBlock(bIndex);
                        metaInfo += "Block " + bIndex + " : "
                                + MyUtils.BytesToHexString(data) + "\n";
                        System.out.println("第"+j+"区第"+bIndex+"块");
                        if(j == 0 && bIndex == 2) {//第一个区第三块
//                            System.out.println("获得当前块的内容："+MyUtils.BytesToHexString(mfc.readBlock(bIndex)));
//                            Promt.setText("获得当前块的内容："+MyUtils.BytesToHexString(mfc.readBlock(bIndex)));
                            Promt.setText("当前编号："+MeterIdUtil.getMeterId(MyUtils.BytesToHexString(mfc.readBlock(bIndex))));
                            meterId = MeterIdUtil.getMeterId(MyUtils.BytesToHexString(mfc.readBlock(bIndex)));
//                            System.out.println("meterId:"+l);
                        }
                        bIndex++;
                    }
                } else {
                    metaInfo += "Sector " + j + ":验证失败\n";
                }
            }
            Promt.setText(metaInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Write.setEnabled(false);
//        Read.setEnabled(false);
        return  meterId;
    }


    /**
     * 写数据
     * @param intent
     */
    private void processIntent2(Intent intent){
        int sector;
        int block;
        String temp3;
        String temp2;
        String temp = strBlock;//0--3块
        temp2 = Data.getText().toString();
        temp3 = strSector;//0--15区
        //程序里 0--3， 0--15 ；
        block = Integer.parseInt(temp);
        sector = Integer.parseInt(temp3);
        System.out.println(sector+"**********************************"+block+"--"+Integer.parseInt(ssBlock));
        block = Integer.parseInt(ssBlock);
        String mId = wMeterId.getText().toString();
        byte[] bos_new = MyUtils.HexStringBytes(temp2);

        if(!mId.isEmpty()) {

            bos_new = MyUtils.HexStringBytes(MeterIdUtil.setMeterIdFormat(Long.valueOf(mId)));
            Tag tagFromIntent = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            boolean auth = false;
            //读取TAG
            MifareClassic mfc = MifareClassic.get(tagFromIntent);
            try {
                if (mfc.isConnected());

                mfc.connect();System.out.println("mfc的状态："+mfc.isConnected());
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            try {
                if(flag)
                    auth = mfc.authenticateSectorWithKeyA(0,getKey());
                else
                    auth = mfc.authenticateSectorWithKeyB(0,getKey());
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            if (auth) {
                // the last block of the sector is used for KeyA and KeyB cannot be overwritted
                try {
                    mfc.writeBlock(2,bos_new);
                    //mfc.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                Title.setText("正在扫描Mifare卡");
//            Sector.setText("");
//            Block.setText("");
//            Data.setText("");
                Toast.makeText(NFCDemoActivity.this, "Write Success!", Toast.LENGTH_SHORT).show();
                Write.setEnabled(false);
                Read.setEnabled(false);
            }
        }else {

        }
        if (!temp2.isEmpty()){

            bos_new = MyUtils.HexStringBytes(temp2);
            Tag tagFromIntent = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            boolean auth = false;
            //读取TAG
            MifareClassic mfc = MifareClassic.get(tagFromIntent);
            try {
                mfc.connect();
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            try {
                if(flag)
                    auth = mfc.authenticateSectorWithKeyA(sector,getKey());
                else
                    auth = mfc.authenticateSectorWithKeyB(sector,getKey());
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            if (auth) {
                // the last block of the sector is used for KeyA and KeyB cannot be overwritted
                try {
                    mfc.writeBlock(block,bos_new);
                    //mfc.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                Title.setText("正在扫描Mifare卡");
//            Sector.setText("");
//            Block.setText("");
//            Data.setText("");
                Toast.makeText(NFCDemoActivity.this, "Write Success!", Toast.LENGTH_SHORT).show();
                Write.setEnabled(false);
                Read.setEnabled(false);
            }
        }else{

        }
    }

    private byte[] getKey(){
        String str = Key.getText().toString();
        byte[] bos_new = MyUtils.StrtoByte(str);
        return bos_new;
    }
}

