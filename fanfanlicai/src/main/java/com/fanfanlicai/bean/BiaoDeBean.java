package com.fanfanlicai.bean;

public class BiaoDeBean {
    //债权ID
    private String bid;
    //产品ID 直投才有
    private String pid;
    //借款类型:1个人 2 机构
    private String trulyLoanType;
    //借款人名称  trulyLoanType =1 时取
    private String trulyUserName;
    //借款人身份证 trulyLoanType =1 时取
    private String trulyIdcard;
    //公司名称 trulyLoanType =2 时取
    private String trulyLoanCompany;
    //法人姓名 trulyLoanType =2 时取
    private String trulyCompanyLegalPersion;
    //标的名称
    private String borrowName;
    //借款金额
    private String borrowMoney;
    //借款期限
    private String term;

    public String getBid() {
        return bid;
    }

    public void setBid(String bid) {
        this.bid = bid;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getTrulyLoanType() {
        return trulyLoanType;
    }

    public void setTrulyLoanType(String trulyLoanType) {
        this.trulyLoanType = trulyLoanType;
    }

    public String getTrulyUserName() {
        return trulyUserName;
    }

    public void setTrulyUserName(String trulyUserName) {
        this.trulyUserName = trulyUserName;
    }

    public String getTrulyIdcard() {
        return trulyIdcard;
    }

    public void setTrulyIdcard(String trulyIdcard) {
        this.trulyIdcard = trulyIdcard;
    }

    public String getTrulyLoanCompany() {
        return trulyLoanCompany;
    }

    public void setTrulyLoanCompany(String trulyLoanCompany) {
        this.trulyLoanCompany = trulyLoanCompany;
    }

    public String getTrulyCompanyLegalPersion() {
        return trulyCompanyLegalPersion;
    }

    public void setTrulyCompanyLegalPersion(String trulyCompanyLegalPersion) {
        this.trulyCompanyLegalPersion = trulyCompanyLegalPersion;
    }

    public String getBorrowName() {
        return borrowName;
    }

    public void setBorrowName(String borrowName) {
        this.borrowName = borrowName;
    }

    public String getBorrowMoney() {
        return borrowMoney;
    }

    public void setBorrowMoney(String borrowMoney) {
        this.borrowMoney = borrowMoney;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }
}
