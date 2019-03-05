package com.fanfanlicai.bean;

import java.math.BigDecimal;

public class TiXianBean {
	private String name; //提现票名称
	private double amount; //加息年化利率
	private String createTime;//获得时间:yyyy-MM-dd HH:mm:ss
	private String useTime; //使用时间:yyyy-MM-dd HH:mm:ss
	private String expireTime; //失效时间
	private String status;//状态：1可使用、2使用中、3已使用
	private String id;//提现票ID
	private String fromSource;//获得来源
	private String specialDesc;//特别说明
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getUseTime() {
		return useTime;
	}
	public void setUseTime(String useTime) {
		this.useTime = useTime;
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
	public String getFromSource() {
		return fromSource;
	}
	public void setFromSource(String fromSource) {
		this.fromSource = fromSource;
	}
	public String getSpecialDesc() {
		return specialDesc;
	}
	public void setSpecialDesc(String specialDesc) {
		this.specialDesc = specialDesc;
	}
	public String getExpireTime() {
		return expireTime;
	}
	public void setExpireTime(String expireTime) {
		this.expireTime = expireTime;
	}
	
}
