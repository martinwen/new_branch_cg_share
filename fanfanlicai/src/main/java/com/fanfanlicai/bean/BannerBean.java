package com.fanfanlicai.bean;

public class BannerBean {
	private String imgSrc;
	private String linkUrl;
	private String title;
	private String isNeedLogin;


	public String getIsNeedLogin() {
		return isNeedLogin;
	}

	public void setIsNeedLogin(String isNeedLogin) {
		this.isNeedLogin = isNeedLogin;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getImgSrc() {
		return imgSrc;
	}
	public void setImgSrc(String imgSrc) {
		this.imgSrc = imgSrc;
	}
	public String getLinkUrl() {
		return linkUrl;
	}
	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}
	
}
