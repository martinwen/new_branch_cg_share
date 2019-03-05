package com.fanfanlicai.bean;

/**
 * @author lijinliu
 * @date 20180202
 * 标的数据
 */
public class BidBean {
    //标的id
    public String bid;
    //用户id
    public String userId;
    //交易流水号
    public String transUuid;
    //标的代码
    public String borrowCode;
    //标的名称
    public String borrowName;
    //出借金额
    public String investMoney;
    //买入时间
    public String matchTimeStr;
    //到期时间
    public String deadline;
    //借款类型 1：个人借款 2企业借款
    public String loanType;
    //借款公司
    public String trulyLoanCompany;
    //借款人身份证
    public String trulyIdcard;
    //借款人姓名
    public String trulyUserName;
    //借款公司法人
    public String companyLegal;
    //标的期限
    public String borrowDay;
    //已还款类型：0待还款；1正常还款；2提前还款"
    public String repayedType;
    public String matchId;

    public String getBid() {
        return bid;
    }

    public void setBid(String bid) {
        this.bid = bid;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTransUuid() {
        return transUuid;
    }

    public void setTransUuid(String transUuid) {
        this.transUuid = transUuid;
    }

    public String getBorrowCode() {
        return borrowCode;
    }

    public void setBorrowCode(String borrowCode) {
        this.borrowCode = borrowCode;
    }

    public String getBorrowName() {
        return borrowName;
    }

    public void setBorrowName(String borrowName) {
        this.borrowName = borrowName;
    }

    public String getInvestMoney() {
        return investMoney;
    }

    public void setInvestMoney(String investMoney) {
        this.investMoney = investMoney;
    }

    public String getMatchTimeStr() {
        return matchTimeStr;
    }

    public void setMatchTimeStr(String matchTimeStr) {
        this.matchTimeStr = matchTimeStr;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getLoanType() {
        return loanType;
    }

    public void setLoanType(String loanType) {
        this.loanType = loanType;
    }

    public String getTrulyLoanCompany() {
        return trulyLoanCompany;
    }

    public void setTrulyLoanCompany(String trulyLoanCompany) {
        this.trulyLoanCompany = trulyLoanCompany;
    }

    public String getTrulyIdcard() {
        return trulyIdcard;
    }

    public void setTrulyIdcard(String trulyIdcard) {
        this.trulyIdcard = trulyIdcard;
    }

    public String getTrulyUserName() {
        return trulyUserName;
    }

    public void setTrulyUserName(String trulyUserName) {
        this.trulyUserName = trulyUserName;
    }

    public String getCompanyLegal() {
        return companyLegal;
    }

    public void setCompanyLegal(String companyLegal) {
        this.companyLegal = companyLegal;
    }

    public String getBorrowDay() {
        return borrowDay;
    }

    public void setBorrowDay(String borrowDay) {
        this.borrowDay = borrowDay;
    }

    public String getRepayedType() {
        return repayedType;
    }

    public void setRepayedType(String repayedType) {
        this.repayedType = repayedType;
    }

    public String getMatchId() {
        return matchId;
    }

    public void setMatchId(String matchId) {
        this.matchId = matchId;
    }
}
