package com.fanfanlicai.bean;

public class FTBean {

	public String pro_code;//项目编号
	public String id;//投资记录ID
	public String trans_seq;//交易流水号
	public String days;//投资天数
	public String rec_fee; //赎回手续费
	public String status;//投资状态 :1投资成功、2已赎回、3回款中

	public String getForbidRedeemTime() {
		return forbidRedeemTime;
	}

	public void setForbidRedeemTime(String forbidRedeemTime) {
		this.forbidRedeemTime = forbidRedeemTime;
	}

	public String invest_amount; // 投资金额
	public String total_interest;//收益
	public String trans_time;//投资时间
	public String year_rate;//年化收益率
	public String forbidRedeemTime;//年化收益率
	public boolean canRedeemFlag;//赎回按钮是否可点击

	public boolean isCanRedeemFlag() {
		return canRedeemFlag;
	}

	public void setCanRedeemFlag(boolean canRedeemFlag) {
		this.canRedeemFlag = canRedeemFlag;
	}


	
	public String getPro_code() {
		return pro_code;
	}
	public void setPro_code(String pro_code) {
		this.pro_code = pro_code;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTrans_seq() {
		return trans_seq;
	}
	public void setTrans_seq(String trans_seq) {
		this.trans_seq = trans_seq;
	}
	public String getDays() {
		return days;
	}
	public void setDays(String days) {
		this.days = days;
	}
	public String getRec_fee() {
		return rec_fee;
	}
	public void setRec_fee(String rec_fee) {
		this.rec_fee = rec_fee;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getInvest_amount() {
		return invest_amount;
	}
	public void setInvest_amount(String invest_amount) {
		this.invest_amount = invest_amount;
	}
	public String getTotal_interest() {
		return total_interest;
	}
	public void setTotal_interest(String total_interest) {
		this.total_interest = total_interest;
	}
	public String getTrans_time() {
		return trans_time;
	}
	public void setTrans_time(String trans_time) {
		this.trans_time = trans_time;
	}
	public String getYear_rate() {
		return year_rate;
	}
	public void setYear_rate(String year_rate) {
		this.year_rate = year_rate;
	}
	

}
