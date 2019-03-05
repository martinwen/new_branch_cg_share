package com.fanfanlicai.bean;

public class YouXianGouBean {
	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getExpiredTime() {
		return expiredTime;
	}

	public void setExpiredTime(String expiredTime) {
		this.expiredTime = expiredTime;
	}

	public String getUseTime() {
		return useTime;
	}

	public void setUseTime(String useTime) {
		this.useTime = useTime;
	}

	public String getFrom_source() {
		return from_source;
	}

	public void setFrom_source(String from_source) {
		this.from_source = from_source;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	private String amount;//优先购额度
	private String createTime; //生效时间
	private String expiredTime; //失效时间
	private String useTime;//使用时间
	private String from_source;//来源
	private String id; //ID
	private String productName; //饭盒
	private String status; //可使用：1，  已使用：2，   已失效：3

}
