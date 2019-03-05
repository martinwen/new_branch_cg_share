package com.fanfanlicai.bean;

public class FHInvestingBean {

	public String borrowCode;//项目编号
	public String borrowName;//项目名称
	public String borrowMoney;//借款金额
	public String days; //借款周期（天）
	public String deadlineStr; // 债权到期日
	public String trulyIdcard;//借款人身份证号
	public String matchTime; // 匹配时间 
	public String trulyUserName;//借款人姓名
	public String id;//合同
	public String trulyLoanType ;//借款类型：个人借款：1,机构借款:2
	public String trulyLoanCompany;//借款公司名称

	public String isQianyi;//是否迁移
	public String companyName;//担保机构
	public String loanItemInfo;//项目介绍
	public String riskControl;//风控措施
	public String picuUrl;//图片信息

	public String getIsQianyi() {
		return isQianyi;
	}

	public void setIsQianyi(String isQianyi) {
		this.isQianyi = isQianyi;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getLoanItemInfo() {
		return loanItemInfo;
	}

	public void setLoanItemInfo(String loanItemInfo) {
		this.loanItemInfo = loanItemInfo;
	}

	public String getRiskControl() {
		return riskControl;
	}

	public void setRiskControl(String riskControl) {
		this.riskControl = riskControl;
	}

	public String getPicuUrl() {
		return picuUrl;
	}

	public void setPicuUrl(String picuUrl) {
		this.picuUrl = picuUrl;
	}

	public String getBorrowCode() {
		return borrowCode;
	}
	public void setBorrowCode(String borrowCode) {
		this.borrowCode = borrowCode;
	}
	public String getBorrowName() {
		return borrowName;
	}
	public void setBorrowName(String borrowName) {
		this.borrowName = borrowName;
	}
	public String getBorrowMoney() {
		return borrowMoney;
	}
	public void setBorrowMoney(String borrowMoney) {
		this.borrowMoney = borrowMoney;
	}
	public String getDays() {
		return days;
	}
	public void setDays(String days) {
		this.days = days;
	}
	public String getDeadlineStr() {
		return deadlineStr;
	}
	public void setDeadlineStr(String deadlineStr) {
		this.deadlineStr = deadlineStr;
	}
	public String getMatchTime() {
		return matchTime;
	}
	public void setMatchTime(String matchTime) {
		this.matchTime = matchTime;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	public String getTrulyIdcard() {
		return trulyIdcard;
	}

	public void setTrulyIdcard(String trulyIdcard) {
		this.trulyIdcard = trulyIdcard;
	}

	public String getTrulyUserName() {
		return trulyUserName;
	}

	public void setTrulyUserName(String trulyUserName) {
		this.trulyUserName = trulyUserName;
	}

	public String getTrulyLoanType() {
		return trulyLoanType;
	}

	public void setTrulyLoanType(String trulyLoanType) {
		this.trulyLoanType = trulyLoanType;
	}

	public String getTrulyLoanCompany() {
		return trulyLoanCompany;
	}

	public void setTrulyLoanCompany(String trulyLoanCompany) {
		this.trulyLoanCompany = trulyLoanCompany;
	}
}
