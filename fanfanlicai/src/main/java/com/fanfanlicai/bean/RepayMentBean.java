package com.fanfanlicai.bean;

/**
 * @author lijinliu
 * @date 20180202
 * 标的回款计划
 */
public class RepayMentBean {
    //记录ID
    public String id;
    //交易流水号
    public String transUuid;
    //交易状态
    public String status;
    //本金
    public String capital;
    //收益
    public String interest;
    //还款时间
    public String endTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTransUuid() {
        return transUuid;
    }

    public void setTransUuid(String transUuid) {
        this.transUuid = transUuid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCapital() {
        return capital;
    }

    public void setCapital(String capital) {
        this.capital = capital;
    }

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
