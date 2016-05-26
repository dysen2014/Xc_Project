package com.dysen.myUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 把 Hex 字符串转成 String 字符串
 * 
 * @author dysen
 * @version 2015-1-17 下午2:16:22
 */
public class MyUtils {

	/**
	 * 字符序列转换为16进制字符串
	 * @param src
	 * @return
	 */
	private String bytesToHexString(byte[] src) {
		StringBuilder stringBuilder = new StringBuilder("0x");
		if (src == null || src.length <= 0) {
			return null;
		}
		char[] buffer = new char[2];
		for (int i = 0; i < src.length; i++) {
			buffer[0] = Character.forDigit((src[i] >>> 4) & 0x0F, 16);
			buffer[1] = Character.forDigit(src[i] & 0x0F, 16);
			System.out.println(buffer);
			stringBuilder.append(buffer);
		}
		return stringBuilder.toString();
	}

	/**
	 * （十六进制）字符串转 char 数组
	 * @param str
	 * @return
	 */
	public static char[] Byte2Hex(String str) {

		String digital = "0123456789ABCDEF";
		char[] hex2char = str.toCharArray();
		char[] bytes = new char[str.length() / 2];
		int temp1, temp2;
		String strHex = "";

		for (int i = 0; i < bytes.length; i++) {

			temp1 = digital.indexOf(hex2char[2 * i]) * 16;
			temp2 = temp1 + digital.indexOf(hex2char[2 * i + 1]);

			/*
			 * int a1 = (int)temp2/ 16 ; //得到 高位 int a2 = (int)temp2 % 16 ;
			 * //得到低位
			 */
			bytes[i] = (char) ((temp2 & 0xff));

		}
		// System.out.print( new String(bytes));
		return bytes;
	}

	/**
	 * （十六进制）字符串转 byte 数组
	 * @param str
	 * @return
	 */
	public static byte[] StrtoByte(String str){
		int i,n = 0;
		char[] bos_ch = Byte2Hex(str);
		for (i = 0; i < bos_ch.length; i++) {
			if (bos_ch[i] == 0x0a)
				n++;
		}
		byte[] bos_new = new byte[bos_ch.length + n];
		n = 0;
		for (i = 0; i < bos_ch.length; i++) { // 手机中换行为0a,将其改为0d 0a后再发送
			if (bos_ch[i] == 0x0a) {
				bos_new[n] = 0x0d;
				n++;
				bos_new[n] = 0x0a;
			} else {
				bos_new[n] = (byte) bos_ch[i];
			}
			n++;
		}
		return bos_new;
	}

	public static String myHex2Str(String str) {
		String s = "";
		String sstr = "";
		s = str.replace(" ", "");// 去掉字符中间的空格
		s = s.replace("\n", "");
		for (int i = 0; i < s.length() - 2; i++, i++) {

			sstr += (char) ((int) Integer
					.valueOf(s.substring(0 + i, 2 + i), 16));
		}
		return sstr;
	}
	
	/**
	 * CRC 校验
	 */
	public static short[] crcDemo(String str) {

		short[] data = new short[str.length() / 2];
		// { 00,01,00,00,00,00,00,0xF9,00,0x08,0x08,01,00,00,00,00,00,00,00};
		short ss = 0, index = 0;
		for (int i = 0; i < str.length(); i++, i++) {

			ss = Short.valueOf(str.substring(i, i + 2), 16);
			data[index] = ss;
			index++;
		}

		short[] temdata = new short[data.length + 2];
		// unsigned char alen = *aStr – 2; //CRC16只计算前两部分
		short crc;
		crc = 0x31e3; // (X**16 + X**15 + X**2 + 1)
		for (int i = 0; i < data.length; i++) {
			crc ^= (data[i] << 8);
			int aflag = 0;
			for (int j = 0; j < 8; j++) {
				aflag = crc & 0x8000;

				if (aflag == 32768)
					crc = (short) ((crc << 1) ^ 0x1021);
				else if (aflag == 0)
					crc = (short) (crc << 1);

			}
		}
		System.arraycopy(data, 0, temdata, 0, data.length);
		temdata[temdata.length - 2] = (short) ((crc >> 8) & 0xFF);
		temdata[temdata.length - 1] = (short) (crc & 0xFF);
		return temdata;
	}

	/**
	 * 将byte数组转成十六进制字符串：例 byte[0x2B, 0x44, 0xEF,0xD9]--> "2B44EFD9"
	 * @param data
	 * @return
	 */
	public static String BytesToHexString(byte[] data) {
		StringBuffer s = new StringBuffer();
		for (int i = 0; i < data.length; i++) {
			String hex = Integer.toHexString(data[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			s.append(hex);
		}
		return s.toString();
	}

	/**
	 * dysen 2015-5-15 上午11:56:46 info: 查找 src 字符串中 s 字符串出现的下标
	 */
	public static List<Integer> seachString(String src, String s) {
		src = src.toUpperCase();
		List<Integer> list = new ArrayList();
		for (int i = 0; i < src.length(); i++) {
			int j = src.indexOf(s.toUpperCase(), i);
			if (j == -1) {
				break;
			} else {
				i = j;
				list.add(j);
			}
		}
		for (int i = 0; i < list.size(); i++) {
			System.out.println("第" + i+1 + "出现的下标：" + list.get(i));
		}
		return list;
	}

	/**
	 * 将十六进制字符串0803EAAE01 累加求和返回十六进制字符串FA 主要用于效验和
	 * 
	 * @param str
	 *            十六进制字符串
	 * @return
	 */
	public static String HexSUM(String str) {
		byte[] b = HexStringBytes(str);
		int i = 0;
		for (byte a : b) {
			i += a;

			// 如果为负数则加256
			if (i < 0) {
				i += 256;
			} else {
				i = i % 256;
			}
		}
		String s = Integer.toHexString(i).toUpperCase();
		if (s.length() < 2) {
			s = "0" + s;
		}
		// System.out.println(i + "  " + s);
		return s;
	}

	/**
	 * int转十六进制字符串高低位转换 原十六进制：423a35c7译十六进制：c7353a42
	 * 
	 * @param j
	 * int
	 * @return
	 */
	public static String HexHightLowConvert(long j) {
		byte byte4 = (byte) (j & 0xff);// 取第一位byte
		byte byte3 = (byte) ((j & 0xff00) >> 8);// 取第二位byte
		byte byte2 = (byte) ((j & 0xff0000) >> 16);// 取第三位byte
		byte byte1 = (byte) ((j & 0xff000000) >> 24);// 取第四位byte
		// 转成十六进制字符串
		String a = Integer.toHexString((byte1 & 0xff));
		String b = Integer.toHexString((byte2 & 0xff));
		String c = Integer.toHexString((byte3 & 0xff));
		String d = Integer.toHexString((byte4 & 0xff));
		// 小于十六前面补0
		if (a.length() < 2) {
			a = 0 + a;
		}
		if (b.length() < 2) {
			b = 0 + b;
		}
		if (c.length() < 2) {
			c = 0 + c;
		}
		if (d.length() < 2) {
			d = 0 + d;
		}
		// 拼成换过的十六进制字符串
		a = d + c + b + a;
		// System.out.println("原十六进制：" + Integer.toHexString(j));
		// System.out.println("译十六进制：" + a);
		return a;
	}

	/**
	 * 将指定字符串src，以每两个字符分割转换为16进制形式 如："2B44EFD9" --> byte[]{0x2B, 0x44, 0xEF, 0xD9}
	 * @param src
	 * @return
	 */
	public static byte[] HexStringBytes(String src) {
		// 声明返回数组
		byte[] b = new byte[src.length() / 2];
		for (int i = 0; i < src.length() / 2; i++) {
			// 取byte中的高位字符串
			byte a = Byte.decode("0x" + src.charAt(i * 2)).byteValue();
			a = (byte) (a << 4);
			byte c = Byte.decode("0x" + src.charAt(i * 2 + 1)).byteValue();
			b[i] = (byte) (a ^ c);
		}
		return b;
	}
}
