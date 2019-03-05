package com.fanfanlicai.bean;

public class BankListBean {

	public double authPosSingleLimit;
	public double authPosDailyLimit;
	public double authPosMonthlyLimit;
	public String bankNo;

	public double getAuthPosSingleLimit() {
		return authPosSingleLimit;
	}

	public void setAuthPosSingleLimit(double authPosSingleLimit) {
		this.authPosSingleLimit = authPosSingleLimit;
	}

	public String getBankNo() {
		return bankNo;
	}

	public void setBankNo(String bankNo) {
		this.bankNo = bankNo;
	}

	public double getAuthPosMonthlyLimit() {
		return authPosMonthlyLimit;
	}

	public void setAuthPosMonthlyLimit(double authPosMonthlyLimit) {
		this.authPosMonthlyLimit = authPosMonthlyLimit;
	}

	public double getAuthPosDailyLimit() {
		return authPosDailyLimit;
	}

	public void setAuthPosDailyLimit(double authPosDailyLimit) {
		this.authPosDailyLimit = authPosDailyLimit;
	}

	public String bankCode;
	public String bankName;
	public String bankPic;
	public double quickSingleLimit;
	public double quickDailyLimit;
	public double webSingleLimit;
	public double webDailyLimit;

	public double getQuickSingleLimit() {
		return quickSingleLimit;
	}

	public void setQuickSingleLimit(double quickSingleLimit) {
		this.quickSingleLimit = quickSingleLimit;
	}

	public double getWebDailyLimit() {
		return webDailyLimit;
	}

	public void setWebDailyLimit(double webDailyLimit) {
		this.webDailyLimit = webDailyLimit;
	}

	public double getWebSingleLimit() {
		return webSingleLimit;
	}

	public void setWebSingleLimit(double webSingleLimit) {
		this.webSingleLimit = webSingleLimit;
	}

	public double getQuickDailyLimit() {
		return quickDailyLimit;
	}

	public void setQuickDailyLimit(double quickDailyLimit) {
		this.quickDailyLimit = quickDailyLimit;
	}

	public String getBankPic() {
		return bankPic;
	}
	public void setBankPic(String bankPic) {
		this.bankPic = bankPic;
	}
	public String getBankCode() {
		return bankCode;
	}
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
}
