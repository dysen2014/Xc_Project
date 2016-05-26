package com.dysen.myUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * sen dy 2015-1-29 上午11:51:27 info: 字符转换(高地位)
 */
public class MyStringConversion {

	/**
	 * sen dy 2015-1-29 上午11:51:27
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	/**
			 * byte -str
			 */
		public static String byte2Str(byte[] b, boolean isHex){

			byte[] buffer = b;
			char[] ch = new char[b.length];
			String str = "";

			if (isHex){
				for (int j = 0; j < buffer.length; j++) {

					if (buffer[j] < 0) {

						ch[j] = (char) (buffer[j] + 256);
					} else {
						ch[j] = (char) buffer[j];
					}
				}
			str = new String(ch);
		}else {
			str = new String(b);
		}
		return str;
	}

	/**
	 * dysen 2015-6-10 上午10:14:08 info: 判断字符串是否只包含0-9a-zA-Z
	 */
	public static boolean isHexStr(String str) {

		Pattern p = Pattern
				.compile("[0-9a-fA-F]+");

		Matcher m = p.matcher(str);

		System.out.println(m.matches() + "---");

		return m.matches();

	}



	public static char[] Byte2Hex(String str) {

		String digital = "0123456789ABCDEF";
		char[] hex2char = str.toCharArray();
		char[] bytes = new char[str.length() / 2];
		int temp1, temp2;
		String strHex = "";

		for (int i = 0; i < bytes.length; i++) {

			temp1 = digital.indexOf(hex2char[2 * i]) * 16;
			temp2 = temp1 + digital.indexOf(hex2char[2 * i + 1]);


//			 int a1 = (int)temp2/ 16 ; //得到 高位
//			 int a2 = (int)temp2 % 16 ;//得到低位

			bytes[i] = (char) ((temp2 & 0xff));
		}
		// System.out.print( new String(bytes));
		return bytes;
	}

	/**
	 * 字符转 16 进制
	 */
	public static byte[] myStr2Byte(String str) {
		int i = 0;
		int n = 0;
		byte[] bos_new = null;

		if (isHexStr(str)) {

			// 16进制
			char[] bos_ch = Byte2Hex(str); // 把字符串换成十六进制

			bos_new = new byte[bos_ch.length];
			n = 0;
			for (i = 0; i < bos_ch.length; i++) { // 手机中换行为0a,将其改为0d 0a后再发送

				bos_new[n] = (byte) bos_ch[i];
				n++;
			}
		}
		return bos_new;
	}
	
	/**
	 * 把普通(十六进制)字符串 以十六进制显示 sen dy 2014-12-2 下午5:10:12
	 */
	public static String myStr(String str) {
		String s = "";
		// String str = //"3C0001000100000022000803EAAE01";
		// "3C000000000000007900080901000550380260205018";
		for (int i = 0; i < str.length() - 1; i++, i++) {
			s += str.substring(i, i + 2) + " ";
		}
		// System.out.println(s);
		return s;
	}

	/**
	 * 变长字符串 00 sen dy 2015-2-11 上午8:54:51
	 */
	public static String myVariableStr(int l) {
		String str = "";
		for (int i = 0; i < l; i++) {
			str += 0;
		}
		return str;
	}

	/**
	 * 通过把十进制的1转换成01， 0001 等 sen dy 2015-1-29 下午3:35:35
	 */
	public static String myInverseStr(String id, int len) {

		String strSet1 = "0000";
		strSet1 = myVariableStr(len);
		String str = "";
		char[] ch = new char[len];

		if ("".equals(id)) {
			// alertDemo("参数不能为空").show();
		} else {
			String strId = String.valueOf(id);

			str = strSet1.substring(0, strSet1.length() - strId.length())
					+ strId;
			// ch = str.toCharArray();
			// str = StringHandler.myConver(ch).toUpperCase();
		}
		return str;
	}

	/**
	 * 通过把十进制的1转换成01， 0100 等 sen dy 2015-1-29 下午3:35:35
	 */
	public static String myInverseStrConver(String id, int len) {

		String strSet1 = "0000";
		strSet1 = myVariableStr(len);
		String str = "";
		char[] ch = new char[len];

		if ("".equals(id)) {
			// alertDemo("参数不能为空").show();
		} else {
			String strId = String.valueOf(id);

			str = strSet1.substring(0, strSet1.length() - strId.length())
					+ strId;
			ch = str.toCharArray();
			str = myConver(ch).toUpperCase();
		}
		return str;
	}

	/**
	 * 通过把十进制的1转换成01， 00 01 等 sen dy 2015-1-29 下午3:35:35
	 */
	public static String myInverse(long id, int len) {

		String strSet1 = "0000";
		strSet1 = myVariableStr(len);
		String str = "";
		char[] ch = new char[len];

		if ("".equals(id)) {
			// alertDemo("参数不能为空").show();
		} else {
			String strId = Long.toHexString(id);

			ch = (strSet1.substring(0, strSet1.length() - strId.length()) + strId)
					.toCharArray();

			str = new String(ch);
			// StringHandler.myConver(ch).toUpperCase();
		}
		return str;
	}

	/**
	 * 通过把十进制的1转换成01, 01 00, 01 00 00 00 等 sen dy 2015-1-29 下午3:35:35
	 */
	public static String myInverseConver(long id, int len) {

		String strSet1 = "0000";
		strSet1 = myVariableStr(len);
		String strNet = "";
		char[] ch = new char[len];

		if ("".equals(id)) {
			// alertDemo("参数不能为空").show();
		} else {
			String strNetId = Long.toHexString(id);

			ch = (strSet1.substring(0, strSet1.length() - strNetId.length()) + strNetId)
					.toCharArray();

			strNet = myConver(ch).toUpperCase();
		}
		return strNet;
	}

	/**
	 * 通过把十进制的网号逆置转换 sen dy 2015-1-29 下午3:35:35
	 */
	public static String myInverseNetId(long mNetId, int l) {

		String strSet1 = "0000";
		strSet1 = myVariableStr(l);
		String strNet = "";
		char[] chNetId = new char[4];

		if ("".equals(mNetId)) {
			// alertDemo("参数不能为空").show();
		} else {
			String strNetId = Long.toHexString(Long.parseLong(mNetId + ""));

			chNetId = (strSet1.substring(0,
					strSet1.length() - strNetId.length()) + strNetId)
					.toCharArray();

			strNet = myConver(chNetId).toUpperCase();
		}
		return strNet;
	}

	/**
	 * 通过把十进制的表号逆置转换 sen dy 2015-1-29 下午3:35:19
	 */
	public static String myInverseMeterId(long mMeterId, int l) {

		String strSet2 = "00000000";
		strSet2 = myVariableStr(l);
		String strMeter = "";
		char[] chMeterNum = new char[8];

		if ("".equals(mMeterId)) {
			// alertDemo("参数不能为空").show();
		} else {
			String strMeterNum = Long
					.toHexString(Long.parseLong(mMeterId + ""));
			chMeterNum = (strSet2.substring(0,
					strSet2.length() - strMeterNum.length()) + strMeterNum)
					.toCharArray();

			strMeter = myConver(chMeterNum).toUpperCase();
		}
		return strMeter;
	}

	/**
	 * 逆置
	 */
	public static String myConver(char[] ch) {
		// char[] ch = {'1','2','3','4','5','6','7','8'};
		char[] temp = new char[1];

		for (int i = 0; i < ch.length / 2; i++) {
			temp[0] = ch[i];
			ch[i] = ch[ch.length - i - 1];
			ch[ch.length - i - 1] = temp[0];

		}
		for (int i = 0; i < ch.length; i++) {
			if (i % 2 == 0) {
				temp[0] = ch[i];
				ch[i] = ch[i + 1];
				ch[i + 1] = temp[0];
			}
		}

		System.out.println("...." + new String(ch));
		return new String(ch);
	}
}
