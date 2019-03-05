package com.fanfanlicai.bean;

public class ShuHuiBean {

	public String transTime;//交易时间
	public String amount;//赎回金额
	public String status;//赎回状态0-赎回中；1-赎回成功

	public String getTransTime() {
		return transTime;
	}

	public void setTransTime(String transTime) {
		this.transTime = transTime;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
