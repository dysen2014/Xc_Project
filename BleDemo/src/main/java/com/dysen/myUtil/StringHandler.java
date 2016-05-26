package com.dysen.myUtil;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * java String 与各种进制字符之间的转换
 */
public class StringHandler {

	private static int intTime;

	public static void main(String[] args) throws UnsupportedEncodingException {
		String content = "技术性问题EDF%&^%#_|~";
		System.out.println(bin2hex(content));
		System.out.println(hex2bin(bin2hex(content)));

		// myScanning("");
	}

	/**
	 * bytes 数组 截取
	 * @param src
	 * @param begin
	 * @param count
     * @return
     */
	public static byte[] subBytes(byte[] src, int begin, int count) {
		byte[] bs = new byte[count];
		for (int i=begin; i<begin+count; i++) bs[i-begin] = src[i];
		return bs;
	}

	/**
	 * 把十六进制字符串 转换成 byte数组
	 *
	 * @param str
	 * @return
	 */
	public static byte[] myHexStr2Byte(String str) {

		byte[] c = new byte[str.length() / 2];

		for (int i = 0; i < c.length; i++) {

			byte a = Byte.decode("0x" + str.charAt(i * 2)).byteValue();
			a = (byte) (a << 4);
			byte b = Byte.decode("0x" + str.charAt(i * 2 + 1)).byteValue();
			c[i] = (byte) (a ^ b);

			System.out.print(c[i] + "\t");
		}
		return c;
	}

	/**
	 * 求和
	 *
	 * @param mbyte
	 */
	public static String mySum(byte[] mbyte) {

		int m = 0, sum = 0;
		String strSum = "";
		for (int i = 1; i < mbyte.length - 1; i++) {
			System.out.println(mbyte[i] + "\t");
			if (mbyte[i] < 0) {
				m = mbyte[i] + 256;
			} else {
				m = mbyte[i];
			}
			if (sum > 255) {
				sum -= 256;
			}
			sum += m;
		}
		strSum = Integer.toHexString(sum).toUpperCase();
		System.out.println(strSum + "******sum****");
		return strSum;
	}

	/**
	 * 求和 参数 字符串
	 *
	 * @param mbyte
	 */
	public static String mySum2(String str) {

		int m = 0, sum = 0;
		byte[] mbyte = new byte[str.length() / 2];

		for (int i = 0; i < mbyte.length; i++) {

			byte a = Byte.decode("0x" + str.charAt(i * 2)).byteValue();
			a = (byte) (a << 4);
			byte b = Byte.decode("0x" + str.charAt(i * 2 + 1)).byteValue();
			mbyte[i] = (byte) (a ^ b);

			System.out.print(mbyte[i] + "	");

		}
		System.out.println(""); // 换行
		for (int i = 0; i < mbyte.length; i++) {
			System.out.print(str.substring(i * 2, i * 2 + 2) + "	");
		}
		System.out.println(""); // 换行

		for (int i = 1; i < mbyte.length - 1; i++) {
			if (mbyte[i] < 0) {
				m = mbyte[i] + 256;
			} else {
				m = mbyte[i];
			}
			if (sum > 255) {
				sum -= 256;
			}
			sum += m;
		}
		String strSum = Integer.toHexString(sum).toUpperCase();
		System.out.println("校验和：" + strSum);
		return strSum;
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
	 * 累加和
	 */

	public static ArrayList<Integer> myScan2(char[] ch) {

		ArrayList<Integer> al, al2;
		al = new ArrayList<Integer>();
		char sum = 0;
		int a = 0;
		for (int i = 0; i < ch.length - 40; i++) {
			a++;
			// System.out.println(a + "-------------------------------");
			if (ch[i] == 0x3C && ch[i + 8] == 0x22) {
				// System.out.println("111111111111111111111");
				for (int j = i + 1; j < ch.length - 41 * (ch.length / 41 - 1)
						- ch.length % 41 + i - 1; j++) {
					// System.out.println(j
					// + "====================================");
					sum += ch[j];
					if (sum > 255) {

						sum -= 256;
					}
					// System.out.println((int) sum + "##############"
					// + (int) ch[i + 40]);

				}
				if (sum == ch[i + 40]) {

					// System.out
					// .println("join ****************************************** "
					// + ch.length);
					al.add(i);
					sum = 0;
					i += 40;
				}
			}
		}

		return al;
	}

	public static ArrayList<Integer> myScan(char[] ch1) {

		char index, sum = 0;

		ArrayList<Integer> al, al2;
		al = new ArrayList<Integer>();
		al2 = new ArrayList<Integer>();

		for (int i = 0; i < ch1.length; i++) {

			// System.out.println((ch1[i]) + "----" + (int) ch1[i]);

			if (ch1[i] == 0x3C && ch1[i + 8] == 0x22) {
				al2.add(i);
			}
		}
		for (int k = 0; k < al2.size(); k++) {

			// System.out.println("111111");

			// System.out.println("22222222");
			for (int j = al2.get(k) + 1; j < (41 + al2.get(k)) - 1; j++) {
				index = ch1[j];

				sum += index;
				if (sum > 255) {

					sum -= 256;
				}
				// System.out.println(index + "#######" + (int) sum);

			}
			// System.out.println((int) ch1[al2.get(k) + 40] + "*********"
			// + ch1.length);
			if (sum == ch1[al2.get(k) + 40]) {
				al.add(al2.get(k));
				sum = 0;

				// System.out.println("OKOKOKOK");
			}

		}

		// System.out.println(Hex2Byte(ch1));

		return al;

	}

	/**
	 * 判断是否是单抄回来的表数据
	 */
	public static ArrayList<Integer> myScanning2(String strRx) {

		ArrayList<Integer> al;
		int sum = 0, j = 0;
		int a = 0;
		String strSum = "";

		al = new ArrayList<Integer>();

		for (int i = 0; i < strRx.length() - 80; i++) {
			a++;
			// System.out.println(a + "----------第一个for执行几次---------");
			j = i + 16;

			if ("3C".equals(strRx.substring(i, i + 2))
					&& "22".equals(strRx.substring(j, j + 2))) {
				// System.out.println("11111111111111111111111");
				for (int k = i + 2; k < strRx.length() - 82
						* (strRx.length() / 82 - 1) - strRx.length() % 82 + i
						- 2; k++, k++) {
					//
					// System.out.println(k
					// + "==============第二个for k的值================"
					// + strRx.substring(k, k + 2));
					sum += Integer.valueOf(strRx.substring(k, k + 2), 16);
					if (sum > 255) {
						sum -= 256;
					} else if (sum <= 15) { // 当 ll 小于 10 时 判断无发进入

						strSum = "0" + Integer.toHexString(sum);
					} else {
						strSum = Integer.toHexString(sum);
					}

				}
				// System.out.println(strRx.substring(i + 80, i + 82)
				// + "%%%%%%%%%%%%%%%%%%%%%%%" + strSum);
				if (strRx.substring(i + 80, i + 82)
						.equals(strSum.toUpperCase())) {

					// System.out.println("join..........................");
					al.add(i);
					sum = 0;
					i += 81;

				}
			}
		}
		for (int i = 0; i < al.size(); i++) {
			// System.out.println(al.get(i)
			// + "^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
		}
		return al;
	}

	/**
	 * 判断是否是群抄回来的表数据
	 *
	 * @param strRx
	 * @return
	 */
	public static ArrayList<Integer> myScanning(String strRx) {

		int intSrc = 0, index = 0;
		ArrayList<Integer> al;

		al = new ArrayList<Integer>();

		for (int i = 0; i < strRx.length() - 78; i++, i++) {
			if ("EAEB05".equals(strRx.substring(i, i + 6))) {
				intSrc = (Integer.valueOf(strRx.substring(i + 6, i + 8), 16) - Integer
						.valueOf(23 + "", 16)) * 2;
				System.out.println(intSrc);
				if ("EC".equals(strRx.substring(i + 78 + intSrc, i + 80
						+ intSrc))) {
					index = i;
					al.add(index);
					System.out.println(al.size() + "*****" + al.get(0));
				}
			}
		}
		return al;

	}

	/**
	 * 十进制转成十六进制
	 *
	 * @param l
	 * @return
	 */
	public static String Decimal2Hex(long l) {

		String str = Long.toHexString(l);

		System.out.println(str);
		return str;

	}

	/**
	 * 求16进制字符串的和
	 */
	public static long myCheckSum(String str) {

		long l = 0;
		StringHandler strH = new StringHandler();

		for (int i = 0; i < str.length() - 1; i++, i++) {

			l += StringHandler.Hex2Decimal(str.substring(i, i + 2));
			System.out.println(l);
			if (l > 255) {
				l -= 256;
			}
		}
		// System.out.println(strH.Decimal2Hex(l));

		return l;
	}

	/**
	 * 十六进制转成十进制
	 * */
	public static long Hex2Decimal(String str) {

		long result = Long.valueOf(str, 16);

		return result;
	}

	/**
	 * 拼接网号
	 */
	public static String pNetId(String strNet) {
		String strSet1 = "0000";

		char[] chNetId = new char[4];
		String strNetId = Integer.toHexString(Integer.parseInt(strNet));
		chNetId = (strSet1.substring(0, strSet1.length() - strNetId.length()) + strNetId)
				.toCharArray();

		return myConver(chNetId);
	}

	/**
	 * 拼接表号
	 */
	public static String pMeterId(String strMetrId) {

		String strSet2 = "00000000";

		char[] chMeterNum = new char[8];
		String strMeterNum = Integer.toHexString(Integer.parseInt(strMetrId));
		chMeterNum = (strSet2.substring(0,
				strSet2.length() - strMeterNum.length()) + strMeterNum)
				.toCharArray();

		return myConver(chMeterNum);
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

	/* *
	 * 逆置
	 */
	public static char[] myConver2(char[] ch, int offset) {

		char[] temp = new char[1];
		// int count = j / 19 * 19 ; // 仅有当 j 等于 19的倍数 时 count 才等于 相应值 否则等于 0
		int count = offset;

		for (int i = count; i < ch.length; i++) {

			if (i == 2 + count | i == 5 + count | i == 14 + count) {
				temp[0] = ch[i];
				ch[i] = ch[i + 1];
				ch[i + 1] = temp[0];
			} else if (i == 4 + count | i == 13 + count) {
				temp[0] = ch[i];
				ch[i] = ch[i + 3];
				ch[i + 3] = temp[0];
			}
			// System.out.print(ch[i ]+" ");
		}

		return ch;
	}

	/*
	 * 字符转 16 进制
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

	/*
	 * 16 进制 转字符
	 */
	public static String Hex2Byte(char bin[]) {
		char[] digital = "0123456789ABCDEF".toCharArray();
		StringBuffer sb = new StringBuffer("");
		char[] bs = bin;
		int bit;
		for (int i = 0; i < bs.length; i++) {
			// System.out.println("bs  "+bs);
			bit = (bs[i] & 0xf0) >> 4;
			sb.append(digital[bit]);
			// System.out.println("bit1 " + bit);
			bit = bs[i] & 0x0f;
			sb.append(digital[bit]);
			// System.out.println("bit2 " + bit);
		}
		// System.out.print(sb.toString());
		return sb.toString();
	}

	public static String myStrToHex(int[] int1) {

		String str = "";
		String strHex;

		for (int i = 0; i < int1.length; i++) {

			strHex = Integer.toHexString(int1[i]);

			if (strHex.length() == 2) {
				strHex = strHex;
			} else if (strHex.length() == 1) {
				strHex = '0' + strHex;
			}
			str += strHex.toUpperCase();
			System.out.println(str);
		}

		return str;

	}

	/**
	 * 十六进制转换字符串
	 *
	 * @param hex
	 *            String 十六进制
	 * @return String 转换后的字符串
	 */
	/*
	 * public static String hex2bin(String hex) { String digital =
	 * "0123456789ABCDEF"; char[] hex2char = hex.toCharArray(); byte[] bytes =
	 * new byte[hex.length() / 2]; int temp; for (int i = 0; i < bytes.length;
	 * i++) { temp = digital.indexOf(hex2char[2 * i]) * 16; temp +=
	 * digital.indexOf(hex2char[2 * i + 1]); bytes[i] = (byte) (temp & 0xff); }
	 * System.out.println(new String(bytes)); return new String(bytes); }
	 */

	public static String hex2bin(String hex)
			throws UnsupportedEncodingException {
		String str = null;
		String digital = "0123456789ABCDEF";
		byte[] bytes = new byte[hex.length() / 2];
		char[] hex2char = hex.toCharArray();
		// byte[] bytes = new byte[hex.length() / 2];
		int temp;
		for (int i = 0; i < (hex.length()) / 2; i++) {
			temp = digital.indexOf(hex2char[2 * i]) * 16;// System.out.println("temp--"+temp);
			temp += digital.indexOf(hex2char[2 * i + 1]);

			// System.out.println("temp2--"+temp);
			str = Integer.toHexString(temp & 0xff);
			if (str.length() == 1) {
				str = '0' + str; // 这里不能用 str += '0' 比如 1会变成 10 而不是 01;
			}
			str = str.toUpperCase();
			bytes = str.getBytes("UTF-8");
			System.out.print(str + " ");
			// return bytes;
		}
		return new String(bytes, "UTF-8");
	}

	/**
	 * 字符串转换成十六进制值
	 *
	 * @param bin
	 *            String 我们看到的要转换成十六进制的字符串
	 * @return
	 */
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
		System.out.println(sb.toString());
		return sb.toString();
	}

	/**
	 * java字节码转字符串
	 *
	 * @param b
	 * @return
	 */
	public static String byte2hex(byte[] b) { // 一个字节的数，

		// 转成16进制字符串

		String hs = "";
		String tmp = "";
		for (int n = 0; n < b.length; n++) {
			// 整数转成十六进制表示

			tmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
			if (tmp.length() == 1) {
				hs = hs + "0" + tmp;
			} else {
				hs = hs + tmp;
			}
		}
		tmp = null;
		return hs.toUpperCase(); // 转成大写

	}

	/**
	 * 字符串转java字节码
	 *
	 * @param b
	 * @return
	 */
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

}