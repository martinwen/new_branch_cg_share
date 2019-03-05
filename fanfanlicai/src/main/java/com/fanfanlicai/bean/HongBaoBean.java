package com.fanfanlicai.bean;

public class HongBaoBean {
	private String id;//红包ID
	private String name; //红包名称
	private String reward; //红包金额
	private String status;//状态：1可使用、2已使用、3已过期
	private String useCondition;//起投金额
	private String productName; //适用产品
	private String createTime; //获得时间
	private String useTime; //使用时间
	private String expiredTime; //失效时间
	private String specialDesc;//特别说明
	private String fromSource;//获得来源

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getReward() {
		return reward;
	}

	public void setReward(String reward) {
		this.reward = reward;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getUseCondition() {
		return useCondition;
	}

	public void setUseCondition(String useCondition) {
		this.useCondition = useCondition;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
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

	public String getExpiredTime() {
		return expiredTime;
	}

	public void setExpiredTime(String expiredTime) {
		this.expiredTime = expiredTime;
	}

	public String getSpecialDesc() {
		return specialDesc;
	}

	public void setSpecialDesc(String specialDesc) {
		this.specialDesc = specialDesc;
	}

	public String getFromSource() {
		return fromSource;
	}

	public void setFromSource(String fromSource) {
		this.fromSource = fromSource;
	}
}
