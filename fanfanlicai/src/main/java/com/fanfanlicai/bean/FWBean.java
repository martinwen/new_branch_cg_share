package com.fanfanlicai.bean;

public class FWBean {

    public String transTime;//买入时间
    public String yearRate;//预期年华率
    public String investAmount;//投资金额
    public String endTime; //到期时间
    public String investDeadline; // 投资期限
    public String status;//投资状态 :1：还款中；3：已完成；4：回款中
    public String recInterest;//待收收益
    public String waitRepayAmount; // statu=1待收本息；status=3 已结本息
    public String transUuid;//交易流水号
    public String type;//新老饭碗分类 1老，2新
    public String transTimeStr;//带时分秒时间
    public String proRepayInterest;//直投 预计收益

    public String getTransTime() {
        return transTime;
    }

    public void setTransTime(String transTime) {
        this.transTime = transTime;
    }

    public String getYearRate() {
        return yearRate;
    }

    public void setYearRate(String yearRate) {
        this.yearRate = yearRate;
    }

    public String getInvestAmount() {
        return investAmount;
    }

    public void setInvestAmount(String investAmount) {
        this.investAmount = investAmount;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getInvestDeadline() {
        return investDeadline;
    }

    public void setInvestDeadline(String investDeadline) {
        this.investDeadline = investDeadline;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getWaitRepayAmount() {
        return waitRepayAmount;
    }

    public void setWaitRepayAmount(String waitRepayAmount) {
        this.waitRepayAmount = waitRepayAmount;
    }

    public String getTransUuid() {
        return transUuid;
    }

    public void setTransUuid(String transUuid) {
        this.transUuid = transUuid;
    }

    public String getRecInterest() {
        return recInterest;
    }

    public void setRecInterest(String recInterest) {
        this.recInterest = recInterest;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTransTimeStr() {
        return transTimeStr;
    }

    public void setTransTimeStr(String transTimeStr) {
        this.transTimeStr = transTimeStr;
    }

    public String getProRepayInterest() {
        return proRepayInterest;
    }

    public void setProRepayInterest(String proRepayInterest) {
        this.proRepayInterest = proRepayInterest;
    }
}
