package com.dysen.afinalUtil;

/**
 * 作者：沈迪 [dysen] on 2016-03-18 14:08.
 * 邮箱：dysen@outlook.com | dy.sen@qq.com
 * 描述：表数据
 */
public class tMeter {

    int id;
    /**
     * accountFeeAll : 199.39
     * code : 111
     * contactAddr : 公安巷
     * meterID : 11110301
     * readEnd : 2970
     * readStart : 2843
     * timeAccount : 05-12-31 10:17:21
     * statusAccount : 0(未抄)
     * used : 127
     * userName : 张宏伟
     * statusRead : 读表状态
     * statusUpdate : 上传状态
     */

    private double accountFeeAll;
    private String code;
    private String contactAddr;
    private String meterID;
    private int readEnd;
    private int readStart;
    private String timeAccount;
    private String used;
    private String userName;
    private String statusRead;
    private String statusUpdate;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getStatusUpdate() {
        return statusUpdate;
    }

    public void setStatusUpdate(String statusUpdate) {
        this.statusUpdate = statusUpdate;
    }

    public String getStatusRead() {
        return statusRead;
    }

    public void setStatusRead(String statusAccount) {
        this.statusRead = statusAccount;
    }

    public double getAccountFeeAll() {
        return accountFeeAll;
    }

    public void setAccountFeeAll(double accountFeeAll) {
        this.accountFeeAll = accountFeeAll;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getContactAddr() {
        return contactAddr;
    }

    public void setContactAddr(String contactAddr) {
        this.contactAddr = contactAddr;
    }

    public String getMeterID() {
        return meterID;
    }

    public void setMeterID(String meterID) {
        this.meterID = meterID;
    }

    public int getReadEnd() {
        return readEnd;
    }

    public void setReadEnd(int readEnd) {
        this.readEnd = readEnd;
    }

    public int getReadStart() {
        return readStart;
    }

    public void setReadStart(int readStart) {
        this.readStart = readStart;
    }

    public String getTimeAccount() {
        return timeAccount;
    }

    public void setTimeAccount(String timeAccount) {
        this.timeAccount = timeAccount;
    }

    public String getUsed() {
        return used;
    }

    public void setUsed(String used) {
        this.used = used;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
