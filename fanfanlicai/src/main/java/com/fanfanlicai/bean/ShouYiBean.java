package com.fanfanlicai.bean;

public class ShouYiBean {
	//交易类型：201：买入饭盒，153：提现，202：饭盒基本收益，203：饭盒活动加息收益，
	//251：赎回，401：邀请奖金，402：活动送奖金入账，451：使用奖金，101：充值 601 饭盒买入 651 新饭盒赎回
	private String transType; 
	//交易状态：0待支付、1支付成功
	private String status; 
	// 金额
	private double amount;
	// transType=201是奖金，transType=153是手续费
	private double fee; 
	private String iconUrl;
	//交易名称
	private String transName;
	//交易时间
	private String transTime;
	//红包（饭碗资产流水中有明细）
	private double useRedbag;


	public double getUseRedbag() {
		return useRedbag;
	}

	public void setUseRedbag(double useRedbag) {
		this.useRedbag = useRedbag;
	}

	public String getTransType() {
		return transType;
	}
	public void setTransType(String transType) {
		this.transType = transType;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public double getFee() {
		return fee;
	}
	public void setFee(double fee) {
		this.fee = fee;
	}
	public String getIconUrl() {
		return iconUrl;
	}
	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}
	public String getTransName() {
		return transName;
	}
	public void setTransName(String transName) {
		this.transName = transName;
	}
	public String getTransTime() {
		return transTime;
	}
	public void setTransTime(String transTime) {
		this.transTime = transTime;
	}
}
