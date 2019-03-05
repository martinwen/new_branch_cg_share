package com.fanfanlicai.bean;

/**
 * @author lijinliu
 * @date 20180129
 * 直投数据
 */
public class DirectBean {

    /**
     * 项目名称
     **/
    public String name;
    /**
     * 投资金额
     **/
    public String investAmount;
    /**
     * 投资时间
     **/
    public String investTime;
    /**
     * 预期年化
     **/
    public String yearRate;
    /**
     * 总收益
     **/
    public String recInterest;
    /**
     * 到期时间
     **/
    public String endTime;
    /**
     * 投资时长单位 投资时长单位：1日、2周、3月、4年
     **/
    public String unitType;
    /**
     * 投资期限
     **/
    public String investDeadline;
    /**
     * 状态：1交易完成、2部分还款、3已还清
     **/
    public String status;
    /**
     * 投资记录id
     **/
    public String recordId;
    /**
     * 交易流水号
     **/
    public String transSeq;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInvestAmount() {
        return investAmount;
    }

    public void setInvestAmount(String investAmount) {
        this.investAmount = investAmount;
    }

    public String getInvestTime() {
        return investTime;
    }

    public void setInvestTime(String investTime) {
        this.investTime = investTime;
    }

    public String getYearRate() {
        return yearRate;
    }

    public void setYearRate(String yearRate) {
        this.yearRate = yearRate;
    }

    public String getRecInterest() {
        return recInterest;
    }

    public void setRecInterest(String recInterest) {
        this.recInterest = recInterest;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getUnitType() {
        return unitType;
    }

    public void setUnitType(String unitType) {
        this.unitType = unitType;
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

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public String getTransSeq() {
        return transSeq;
    }

    public void setTransSeq(String transSeq) {
        this.transSeq = transSeq;
    }
}
