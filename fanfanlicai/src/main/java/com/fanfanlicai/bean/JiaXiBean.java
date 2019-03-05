package com.fanfanlicai.bean;

public class JiaXiBean {
	private String name; //加息券名称
	private double reward; //加息年化利率
	private String startTime;//加息时间
	private String endTime; //结束时间
	private String expiredTime; //失效时间
	private String days;//加息天数
	private String status;//状态：1可使用、2使用中、3已使用
	private String id;//加息券ID
	private String source;//获得来源
	private String remark;//特别说明
	private String upgrade;//天天饭局升级

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getReward() {
		return reward;
	}
	public void setReward(double reward) {
		this.reward = reward;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getDays() {
		return days;
	}
	public void setDays(String days) {
		this.days = days;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getExpiredTime() {
		return expiredTime;
	}
	public void setExpiredTime(String expiredTime) {
		this.expiredTime = expiredTime;
	}
	public String getUpgrade() {
		return upgrade;
	}
	public void setUpgrade(String upgrade) {
		this.upgrade = upgrade;
	}
	
}
