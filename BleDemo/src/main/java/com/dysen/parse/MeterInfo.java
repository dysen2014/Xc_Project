package com.dysen.parse;

public class MeterInfo {

	long meterId = 0;
	long long_meterEnd; // 读数
	double dscaling_factor = 0.0; // 比例因子
	// int scaling_factor = 0; // 比例因子
	String meterSty = ""; // 表类型
	double meterVoltage1, meterVoltage2; // 表电压1,2
	String meterStat1 = "", meterStat2 = "";// 表状态1，2
	long meterPrepaid; // 表预付费
	long meterStar; // 表底数
	long meterTemperature; // 表温度
	long meterDate, // 时间
			reserve; // 保留
	long concentratorId, netId;
	long userId;
	private String srcString;

	public String getSrcString() {
		return srcString;
	}

	public void setSrcString(String srcString) {
		this.srcString = srcString;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public MeterInfo() {

	}

	public MeterInfo(long concentratorId, long netId) {

		super();

		this.concentratorId = concentratorId;
		this.netId = netId;
	}

	public MeterInfo(long meterId, long concentratorId, long netId) {

		super();
		this.meterId = meterId;
		this.concentratorId = concentratorId;
		this.netId = netId;
	}

	public long getConcentratorId() {
		return concentratorId;
	}

	public void setConcentratorId(long concentratorId) {
		this.concentratorId = concentratorId;
	}

	public long getNetId() {
		return netId;
	}

	public void setNetId(long netId) {
		this.netId = netId;
	}

	public MeterInfo(long meterId) {
		this.meterId = meterId;
	}

	public MeterInfo(long meterId, long long_meterEnd, // 读数
					 double dscaling_factor, // 比例因子
					 // int scaling_factor = 0; // 比例因子
					 String meterSty, // 表类型
					 double meterVoltage1, double meterVoltage2, // 表电压1,2
					 String meterStat1, String meterStat2,// 表状态1，2
					 long meterPrepaid, // 表预付费
					 long meterStar, // 表底数
					 long meterTemperature, // 表温度
					 long meterDate, // 时间
					 long reserve // 保留
	) {

		this.meterId = meterId;
		this.long_meterEnd = long_meterEnd;
		this.dscaling_factor = dscaling_factor;
		this.meterSty = meterSty;
		this.meterVoltage1 = meterVoltage1;
		this.meterVoltage2 = meterVoltage2;
		this.meterStat1 = meterStat1;
		this.meterStat2 = meterStat2;
		this.meterPrepaid = meterPrepaid;
		this.meterStar = meterStar;
		this.meterTemperature = meterTemperature;
		this.meterDate = meterDate;
		this.reserve = reserve;
	}

	public long getMeterId() {
		return meterId;
	}

	public void setMeterId(long meterId) {
		this.meterId = meterId;
	}

	public long getLong_meterEnd() {
		return long_meterEnd;
	}

	public void setLong_meterEnd(long long_meterEnd) {
		this.long_meterEnd = long_meterEnd;
	}

	public double getDscaling_factor() {
		return dscaling_factor;
	}

	public void setDscaling_factor(double dscaling_factor) {
		this.dscaling_factor = dscaling_factor;
	}

	public String getMeterSty() {
		return meterSty;
	}

	public void setMeterSty(String meterSty) {
		this.meterSty = meterSty;
	}

	public double getMeterVoltage1() {
		return meterVoltage1;
	}

	public void setMeterVoltage1(double meterVoltage1) {
		this.meterVoltage1 = meterVoltage1;
	}

	public double getMeterVoltage2() {
		return meterVoltage2;
	}

	public void setMeterVoltage2(double meterVoltage2) {
		this.meterVoltage2 = meterVoltage2;
	}

	public String getMeterStat1() {
		return meterStat1;
	}

	public void setMeterStat1(String meterStat1) {
		this.meterStat1 = meterStat1;
	}

	public String getMeterStat2() {
		return meterStat2;
	}

	public void setMeterStat2(String meterStat2) {
		this.meterStat2 = meterStat2;
	}

	public long getMeterPrepaid() {
		return meterPrepaid;
	}

	public void setMeterPrepaid(long meterPrepaid) {
		this.meterPrepaid = meterPrepaid;
	}

	public long getMeterStar() {
		return meterStar;
	}

	public void setMeterStar(long meterStar) {
		this.meterStar = meterStar;
	}

	public long getMeterTemperature() {
		return meterTemperature;
	}

	public void setMeterTemperature(long meterTemperature) {
		this.meterTemperature = meterTemperature;
	}

	public long getMeterDate() {
		return meterDate;
	}

	public void setMeterDate(long meterDate) {
		this.meterDate = meterDate;
	}

	public long getReserve() {
		return reserve;
	}

	public void setReserve(long reserve) {
		this.reserve = reserve;
	}

	/********************************************************************/

	// private Integer id;
	// private Short version; // 版本
	// private Timestamp readtime; // 读表时间
	// private String devSn; // 设备号
	// private Integer meterSn; // 表号
	// private String iniValue; // 初始值
	// private String remaingas; // 剩余气量
	// private String totalGas; // 全部气量
	// private String pressure; // 压力
	// private String temperature; // 温度
	// private String statusByte; // 状态位
	// private String status; // 状态
	// private Short pathCount; // 路径数
	// private String userId; // 用户ID
	// private Double rate; // 等级/价格
	// private String netNo; // 网号
	// private Timestamp meterClock; // 表时间
	// private Integer path0;
	// private Integer path1;
	// private Integer path2;
	// private Integer path3;
	// private Integer path4;
	// private Integer path5;
	// private Integer path6;
	// private Integer path7;
	// private Integer path8;
	// private Integer path9;
	// private Integer path10;
	// private Integer path11;
	// private Integer path12;
	// private Integer path13;
	// private Integer path14;
	// private Integer path15;
	// private Double vbat1;
	// private Double vbat2;

}
