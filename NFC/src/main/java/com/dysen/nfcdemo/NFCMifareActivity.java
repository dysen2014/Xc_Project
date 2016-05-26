package com.dysen.nfcdemo;

import android.app.Activity;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.dysen.myUtil.MyUtils;

import java.io.IOException;

/**
 * 作者：沈迪 [dysen] on 2016-03-30 17:07.
 * 邮箱：dysen@outlook.com | dy.sen@qq.com
 * 描述：
 */
public class NFCMifareActivity extends Activity {
    NfcAdapter nfcAdapter;
    TextView Title, Promt;
    Button Read, Write;
    EditText Sector, Key, Block, Data;
    RadioGroup RadioGrp;
    RadioButton RadioBtn1,RadioBtn2;
    boolean flag = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc_mifare);

        idInit();// 资源 id 初始化
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        nfcCheck();

        Read.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(getIntent().getAction()))
                    processIntent(getIntent());
            }
        });

        Write.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                Log.i("Savils", "start");
                if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(getIntent().getAction()))
                    processIntent2(getIntent());
                Log.i("Savils", "final");
            }
        });

        RadioGrp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

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
        Sector = (EditText)findViewById(R.id.Sector);
        Block = (EditText)findViewById(R.id.Block);
        Key =(EditText)findViewById(R.id.Key);
        Data = (EditText)findViewById(R.id.Data);
        RadioGrp = (RadioGroup)findViewById(R.id.RadioGrp);
        RadioBtn1 = (RadioButton)findViewById(R.id.RadioBtn1);
        RadioBtn2 = (RadioButton)findViewById(R.id.RadioBtn2);
    }


    @Override
    protected void onResume() {
        super.onResume();
        //得到是否检测到ACTION_TECH_DISCOVERED触发
        if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(getIntent().getAction())) {
            //处理该intent
            Title.setText("Mifare卡连接成功");
        }
    }


    /**
     * Parses the NDEF Message from the intent and prints to the TextView
     */
    private void processIntent(Intent intent) {
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
            metaInfo += "卡片类型：" + typeS + "\n共" + sectorCount + "个扇区\n共"
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
    }


    private void processIntent2(Intent intent){
        int sector;
        int block;
        String temp3;
        String temp2;
        String temp = Block.getText().toString();
        temp2 = Data.getText().toString();
        temp3 = Sector.getText().toString();
        block = Integer.parseInt(temp);
        sector = Integer.parseInt(temp3);
        byte[] bos_new = MyUtils.StrtoByte(temp2);
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
        if (auth)
        {
            // the last block of the sector is used for KeyA and KeyB cannot be overwritted
            try {
                mfc.writeBlock(block,bos_new);
                //mfc.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            Title.setText("正在扫描Mifare卡");
            Sector.setText("");
            Block.setText("");
            Data.setText("");
            Toast.makeText(NFCMifareActivity.this, "Write Success!", Toast.LENGTH_SHORT).show();
        }
    }

    private byte[] getKey(){
        String str = Key.getText().toString();
        byte[] bos_new = MyUtils.StrtoByte(str);
        return bos_new;
    }
}
