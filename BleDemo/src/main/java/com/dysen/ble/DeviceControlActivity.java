package com.dysen.ble;

import java.io.File;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.dysen.myUtil.MyStringConversion;
import com.dysen.myUtil.MyUtils;
import com.dysen.myUtil.ToastDemo;

/**
 * @author dy
 *
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
@SuppressLint("NewApi")
public class DeviceControlActivity extends Activity implements OnClickListener {
	private final static String TAG = DeviceControlActivity.class
			.getSimpleName();

	public static final String EXTRAS_DEVICE_NAME = "DEVICE_NAME";
	public static final String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";

	private static final String HTTP_IP = "http://192.168.1.217:80";

	private TextView mConnectionState;
	private TextView mDataReceive, mDataSend;
	private String mDeviceName;
	private String mDeviceAddress;
	private ExpandableListView mGattServicesList;
	private BluetoothLeService mBluetoothLeService;
	private ArrayList<ArrayList<BluetoothGattCharacteristic>>
			mGattCharacteristics = new ArrayList<ArrayList<BluetoothGattCharacteristic>>();
	private boolean mConnected = false;
	static boolean flagHex = true;
	private BluetoothGattCharacteristic mNotifyCharacteristic;

	private final String LIST_NAME = "NAME";
	private final String LIST_UUID = "UUID";
	// byte[] WriteBytes = null;
	byte[] WriteBytes = new byte[20];

	private TextView tvTx, tvRx;
	boolean isHex, isShowFlag, isChFlag;
	EditText etSend;
	String cmdName = "";
	LinearLayout llNetMeterId, llSum;
	TextView tvAddr, tvSum;
	EditText etAddr, etSum, etTime, etData;
	String strBleAddr;
	long bleAddr = 1, intervalTime;
	int groupNum, childNum;
	Button btnOpenColose, btnReadMeter, btnSaleGas, btnPrice;
	String verifyCmd;

	private Handler handler;
	private String updateUrl;
	byte[] sendData;
	private static final int UART_PROFILE_CONNECTED = 20;
	private static final int UART_PROFILE_DISCONNECTED = 21;
	private int mState = UART_PROFILE_DISCONNECTED;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.gatt_services_characteristics);

		// 将该项目中包含的原始intent检索出来 赋值给一个Intent类型的变量intent
		final Intent intent = getIntent();
		mDeviceName = intent.getStringExtra(EXTRAS_DEVICE_NAME);
		mDeviceAddress = intent.getStringExtra(EXTRAS_DEVICE_ADDRESS);
		verifyCmd = intent.getStringExtra("verify_cmd");

		String connStat = "成功连接 " + mDeviceName + " 设备";
		((TextView) findViewById(R.id.device_address)).setText(mDeviceAddress);
		mGattServicesList = (ExpandableListView) findViewById(R.id.gatt_services_list);

		// 隐藏 list 列表
		mGattServicesList.setVisibility(View.GONE);
		mConnectionState = (TextView) findViewById(R.id.connection_state);
		mDataReceive = (TextView) findViewById(R.id.receiveData);
		mDataSend = (TextView) findViewById(R.id.sendData);
		tvTx = (TextView) findViewById(R.id.tv_tx);
		tvRx = (TextView) findViewById(R.id.tv_rx);
		tvSum = (TextView) this.findViewById(R.id.tv_sum);
		etSum = (EditText) this.findViewById(R.id.et_sum);
		etTime = (EditText) this.findViewById(R.id.et_time);
		etData = (EditText) this.findViewById(R.id.et_data);
		etSend = (EditText) this.findViewById(R.id.et_send);
		llSum = (LinearLayout) this.findViewById(R.id.ll_sum);

		btnPrice = (Button) this.findViewById(R.id.btn_price);

		btnPrice.setOnClickListener(this);

		getActionBar().setTitle(mDeviceName);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
		bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);

	}

	/**
	 *	dysen
	 *	2015-4-18 上午10:37:19
	 *	info:	提示内容
	 */
	public void myHint(String str){
		Toast.makeText(this, str, Toast.LENGTH_LONG).show();
	}

	@Override
	public void onClick(View v) {
//		if ( "".equals(etSum.getText().toString())) {
//			etSum.setText(1 + ""); // 默认金额 1元 = 100分
//		}
//		intervalTime = Long.parseLong(etSum.getText().toString());
		switch (v.getId()) {
			case R.id.btn_price:

					mySend(bleAddr);

				break;
		}
	}

	/**
	 * 获取 Activity 传过来的参数
	 */
	long mConcentratorId, mNetId, mMeterId;
	private String open, colose, readMeter, saleGas;
	private String openCmd, coloseCmd, readMeterCmd, saleGasCmd;
	private String openRec, coloseRec, readMeterRec, saleGasRec;

	// Code to manage Service lifecycle.
	private final ServiceConnection mServiceConnection = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName componentName,
									   IBinder service) {
			mBluetoothLeService = ((BluetoothLeService.LocalBinder) service)
					.getService();
			if (!mBluetoothLeService.initialize()) {
				Log.e(TAG, "Unable to initialize Bluetooth");
				finish();
			}
			// Automatically connects to the device upon successful start-up
			// initialization.
			mBluetoothLeService.connect(mDeviceAddress);
		}

		@Override
		public void onServiceDisconnected(ComponentName componentName) {
			mBluetoothLeService = null;
		}
	};

	private void service_init() {
		Intent bindIntent = new Intent(this, BluetoothLeService.class);
		bindService(bindIntent, mServiceConnection, Context.BIND_AUTO_CREATE);

		LocalBroadcastManager.getInstance(this).registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
	}
	private IntentFilter makeGattUpdateIntentFilter() {
		final IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
		intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
		intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
		intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
		intentFilter.addAction(BluetoothLeService.DEVICE_DOES_NOT_SUPPORT_UART);
		return intentFilter;
	}

	int charaProp;
	BluetoothGattCharacteristic characteristic;
	private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
		
		String currentDateTimeString = DateFormat.getTimeInstance().format(new Date());
			final String action = intent.getAction();
			if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
				mConnected = true;

				updateConnectionState(R.string.connected, mConnected);
				invalidateOptionsMenu();

				mState = UART_PROFILE_CONNECTED;

			} else if (BluetoothLeService.ACTION_GATT_DISCONNECTED
					.equals(action)) {
				mConnected = false;
				updateConnectionState(R.string.disconnected, mConnected);
				invalidateOptionsMenu();
				clearUI();
				mBluetoothLeService.enableTXNotification();
				mState = UART_PROFILE_DISCONNECTED;
				mBluetoothLeService.close();
//				mBluetoothLeService.connect(mDeviceAddress);// 重新连接蓝牙

			} else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED
					.equals(action)) {
				mBluetoothLeService.enableTXNotification();
			} else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {

				displayData(intent
						.getStringExtra(BluetoothLeService.EXTRA_DATA));
			}else if (action.equals(mBluetoothLeService.DEVICE_DOES_NOT_SUPPORT_UART)){
				ToastDemo.myHint(DeviceControlActivity.this, "Device doesn't support UART. Disconnecting");
				mBluetoothLeService.disconnect();
			}
		}
	};

	public static String bin2hex(String bin) {
		char[] digital = "0123456789ABCDEF".toCharArray();
		StringBuffer sb = new StringBuffer("");
		byte[] bs = bin.getBytes();
		int bit;
		for (int i = 0; i < bs.length; i++) {
			bit = (bs[i] & 0x0f0) >> 4;
			sb.append(digital[bit]);
			bit = bs[i] & 0x0f;
			sb.append(digital[bit]);
		}
		return sb.toString();
	}

	public static byte[] hex2byte(byte[] b) {
		if ((b.length % 2) != 0) {
			throw new IllegalArgumentException("长度不是偶数");
		}
		byte[] b2 = new byte[b.length / 2];
		for (int n = 0; n < b.length; n += 2) {
			String item = new String(b, n, 2);
			// 两位一组，表示一个字节,把这样表示的16进制字符串，还原成一个进制字节
			b2[n / 2] = (byte) Integer.parseInt(item, 16);
		}
		b = null;
		return b2;
	}

	public AlertDialog.Builder alertDemo(String str) {

		Builder alert = new AlertDialog.Builder(this)
				.setTitle(str)
				.setInverseBackgroundForced(true)
				.setPositiveButton("知道咯",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
												int which) {
								dialog.dismiss();

							}
						});

		return alert;
	}

	TextView tvWkUp;
	private boolean connFlag;
	View alertView;

	private void clearUI() {
		mGattServicesList.setAdapter((SimpleExpandableListAdapter) null);
		mDataReceive.setText(R.string.receive_data);
	}

	@Override
	protected void onResume() {
		super.onResume();
		registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
		if (mBluetoothLeService != null) {
			final boolean result = mBluetoothLeService.connect(mDeviceAddress);
			Log.d(TAG, "Connect request result=" + result);
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		unregisterReceiver(mGattUpdateReceiver);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		Log.d(TAG, "onDestroy()");

		try {
			LocalBroadcastManager.getInstance(this).unregisterReceiver(mGattUpdateReceiver);
		} catch (Exception ignore) {
			Log.e(TAG, ignore.toString());
		}
		unbindService(mServiceConnection);
		mBluetoothLeService.stopSelf();
		mBluetoothLeService = null;

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.gatt_services, menu);
		if (mConnected) {
			menu.findItem(R.id.menu_connect).setVisible(false);
			menu.findItem(R.id.menu_disconnect).setVisible(true);
		} else {
			menu.findItem(R.id.menu_connect).setVisible(true);
			menu.findItem(R.id.menu_disconnect).setVisible(false);
		}
		menu.add(1, 1, 1, "关\t于");

		return super.onCreateOptionsMenu(menu);
		// return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if ("".equals(etSum.getText().toString())) {
			etSum.setText(1 + ""); // 默认金额 1元 = 100分
		}
		intervalTime = Long.parseLong(etSum.getText().toString());

		switch (item.getItemId()) {
			case R.id.menu_connect:
				mBluetoothLeService.connect(mDeviceAddress);
				return true;
			case R.id.menu_disconnect:
				mBluetoothLeService.disconnect();
				return true;
			case android.R.id.home:
				onBackPressed();
				return true;
			case 1: // 关于
				new AlertDialog.Builder(this)
						.setTitle("手机抄表系统")
						.setInverseBackgroundForced(true)
						.setMessage(
								"作者： sendy\n公司： 信控科技有限公司  \n电话：(+86)027-87373101\n"
										+ "\t\t\t\t\t\t本公司坐落于中国湖北省武汉市,供应远程自动化控制 GPRS无线传输模块等 !!! ")
						.show();
				return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 *	dysen
	 *	2015-4-18 下午12:26:10
	 *	info:	价格
	 */
	public void mySend(long addr){
		String secFunc = "";
		if (!mConnected) {
			myHint("请先连接蓝牙设备,再做操作");
		} else {
			secFunc = "5F";
			String bleAddr = MyStringConversion.myInverseMeterId(addr, 8)
					.toUpperCase();

			String strCheckSum = "";
			String ss = "6830" + secFunc + bleAddr + "10" +
					"00010203040506070809";
			strCheckSum = MyUtils.HexSUM(ss).toUpperCase();
			String sendCmd = "";
			if ( "".equals(etData.getText().toString())) {
				sendCmd = ss + strCheckSum + "16"+"AABBCCDD";
//				sendCmd += sendCmd;
			}else{
				sendCmd = etData.getText().toString();
			}

			mDataSend.setText(MyStringConversion.myStr(sendCmd));

			if ( "".equals(etTime.getText().toString())) {
				etTime.setText(1000 + ""); //
			}
					mySendMsg(sendCmd);
		}
	}

	/**
	 *
	 * sendy 2015-1-15 下午3:07:39 函数说明
	 */
	private void mySendMsg(String sendData) {

			if (!"".equals(sendData)) {
				if (!flagHex) {
					// changeCmd(); // 选择命令发送
					// write string
					WriteBytes = sendData.getBytes();
					mDataSend.setText(sendData);
					isHex = false;
					// flagHex = false;
				} else if (flagHex) {
					WriteBytes = hex2byte(sendData.getBytes());
					mDataReceive.setText(cmdName);
					// mDataSend.setText(cmdName);
					System.out.println("此时功能号为：" + cmdName);
					isHex = true;
				}
				mDataReceive.setTextSize(20);
				// mDataSend.setTextSize(20);
			}

			tvTx.setText("Tx " + WriteBytes.length);
			tvTx.setTextColor(Color.WHITE);
			tvTx.setTextSize(18);

			// tvTx.setVisibility(View.GONE);

		mBluetoothLeService.writeRXCharacteristic(WriteBytes);

//			System.out.println("发送的数据："+MyStringConversion.myStr(sendData));
			mDataSend.setText("发送的数据：\n"+MyStringConversion.myStr(sendData));
	}

	private void updateConnectionState(final int resourceId, final boolean flag) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {

				if (flag) {
					mConnectionState.setText(mDeviceName);
					mConnectionState.setTextColor(Color.GREEN);
					mConnectionState.setTextSize(20);

					// mConnectionState.setText(getText(resourceId) +
					// mDeviceName
					// + "设备");
				} else {
					mConnectionState.setText(mDeviceName);
					mConnectionState.setTextColor(Color.RED);
					// mConnectionState.setText(getText(resourceId) +
					// mDeviceName
					// + "设备");
				}
			}
		});
	}

	String str = "";

	private boolean flag;

	private File fd;


	String ss = "";
	int i = 0;
	private String strParse = "";
	private boolean isInfo;
	String sMore = "";
	List<Integer> lis;
	private void displayData(String data) {
		String sSingle;

		String RxCmd;
		if (data != null) {
			System.out.println(data + "*************data***************");

			str += data;
			mDataReceive.setText(str);

		}
		/**************** parse 解析数据 ******************/
		ss = str.replace(" ", "");// 去掉字符中间的空格
		ss = ss.replace("\n", "");
		// System.out.println(ss.trim().length()+"ss的长度："+ss);
		if (isHex) {

			if (!ss.isEmpty()) {
				tvRx.setText("Rx " + ss.trim().length() / 2);
				System.out.println("ss:"+ss);
//				myPrase(ss, bleAddr));
//				if ("".equals(myPrase(ss, bleAddr))){
//					mDataReceive.setText("其它数据：\n");
//				+ MyStringConversion.myStr(ss));
//				}else {
				mDataReceive.setText(MyUtils.myStr(ss));
//							myPrase(ss, bleAddr));
//				}

				mDataReceive.setTextSize(18);
				// myFileWrite();
			} else {
				tvRx.setText("Rx " + ss.trim().length() / 2);
				mDataReceive.setText(MyUtils.myStr(ss));
				mDataReceive.setTextSize(18);
			}
			// str = strParse = "";
		} else {
			tvRx.setText("Rx " + ss.trim().length());
			mDataReceive.setText(str);
			// str = strParse = "";
		}
		if ("".equals(sMore)) {
			sMore = data;
		}
		System.out.println(ss.substring(ss.length()-2, ss.length()));
		if (ss.substring(ss.length()-2, ss.length()).contains("16")) {
			System.out.println("join--------------");
			str = "";
		}

		tvRx.setTextColor(Color.WHITE);
		tvRx.setTextSize(18);
		// tvRx.setVisibility(View.GONE); //隐藏
		mDataReceive.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View arg0) {

				new AlertDialog.Builder(DeviceControlActivity.this)
						.setTitle(" 是否清除  !!!")// Whether to remove
						.setPositiveButton("是",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface arg0,
														int arg1) {
										myClaer(); // 清空接收区
									}

								}).setNegativeButton("否", null).show();
				return false;
			}
		});

	}

	/**
	 * 清除函数 sen dy 2015-2-7 上午8:51:56
	 */
	private void myClaer() {
		str = strParse = "";
		mDataReceive.setText(strParse);
	}

	// Demonstrates how to iterate through the supported GATT
	// Services/Characteristics.
	// In this sample, we populate the data structure that is bound to the
	// ExpandableListView
	// on the UI.
	private void displayGattServices(List<BluetoothGattService> gattServices) {
		if (gattServices == null)
			return;
		String uuid = null;
		String unknownServiceString = getResources().getString(
				R.string.unknown_service);
		String unknownCharaString = getResources().getString(
				R.string.unknown_characteristic);
		ArrayList<HashMap<String, String>> gattServiceData = new ArrayList<HashMap<String, String>>();
		ArrayList<ArrayList<HashMap<String, String>>> gattCharacteristicData = new ArrayList<ArrayList<HashMap<String, String>>>();
		mGattCharacteristics = new ArrayList<ArrayList<BluetoothGattCharacteristic>>();

		// Loops through available GATT Services.
		for (BluetoothGattService gattService : gattServices) {
			HashMap<String, String> currentServiceData = new HashMap<String, String>();
			uuid = gattService.getUuid().toString();
			currentServiceData.put(LIST_NAME,
					SampleGattAttributes.lookup(uuid, unknownServiceString));
			currentServiceData.put(LIST_UUID, uuid);
			gattServiceData.add(currentServiceData);

			ArrayList<HashMap<String, String>> gattCharacteristicGroupData = new ArrayList<HashMap<String, String>>();
			List<BluetoothGattCharacteristic> gattCharacteristics = gattService
					.getCharacteristics();
			ArrayList<BluetoothGattCharacteristic> charas = new ArrayList<BluetoothGattCharacteristic>();

			// Loops through available Characteristics.
			for (BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
				charas.add(gattCharacteristic);
				HashMap<String, String> currentCharaData = new HashMap<String, String>();
				uuid = gattCharacteristic.getUuid().toString();
				currentCharaData.put(LIST_NAME,
						SampleGattAttributes.lookup(uuid, unknownCharaString));
				currentCharaData.put(LIST_UUID, uuid);
				gattCharacteristicGroupData.add(currentCharaData);
			}
			mGattCharacteristics.add(charas);
			gattCharacteristicData.add(gattCharacteristicGroupData);
		}

		SimpleExpandableListAdapter gattServiceAdapter = new SimpleExpandableListAdapter(
				this, gattServiceData,
				android.R.layout.simple_expandable_list_item_2, new String[] {
				LIST_NAME, LIST_UUID }, new int[] { android.R.id.text1,
				android.R.id.text2 }, gattCharacteristicData,
				android.R.layout.simple_expandable_list_item_2, new String[] {
				LIST_NAME, LIST_UUID }, new int[] { android.R.id.text1,
				android.R.id.text2 });
		mGattServicesList.setAdapter(gattServiceAdapter);
	}
}
