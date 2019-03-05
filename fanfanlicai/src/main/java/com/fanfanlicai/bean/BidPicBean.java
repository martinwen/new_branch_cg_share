package com.fanfanlicai.bean;

/**
 * @author lijinliu
 * @date 20180201
 * 标的项目信息--图片信息
 */

public class BidPicBean {
    private String bid;
    private String createTime;
    private String id;
    private String picType;
    private String picUrl;
    private String seqNum;

    public String getBid() {
        return bid;
    }

    public void setBid(String bid) {
        this.bid = bid;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPicType() {
        return picType;
    }

    public void setPicType(String picType) {
        this.picType = picType;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getSeqNum() {
        return seqNum;
    }

    public void setSeqNum(String seqNum) {
        this.seqNum = seqNum;
    }
}
