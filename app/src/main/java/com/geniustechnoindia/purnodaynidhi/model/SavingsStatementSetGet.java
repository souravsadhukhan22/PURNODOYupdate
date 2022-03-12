package com.geniustechnoindia.purnodaynidhi.model;

public class SavingsStatementSetGet {
    String slNo,tDate, depositAmount, withdrawlAmount, paymode,balance,particular;

    public String getParticular() {
        return particular;
    }

    public void setParticular(String particular) {
        this.particular = particular;
    }

    public String getSlNo() {
        return slNo;
    }

    public void setSlNo(String slNo) {
        this.slNo = slNo;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String gettDate() {
        return tDate;
    }

    public void settDate(String tDate) {
        this.tDate = tDate;
    }

    public String getDepositAmount() {
        return depositAmount;
    }

    public void setDepositAmount(String depositAmount) {
        this.depositAmount = depositAmount;
    }

    public String getWithdrawlAmount() {
        return withdrawlAmount;
    }

    public void setWithdrawlAmount(String withdrawlAmount) {
        this.withdrawlAmount = withdrawlAmount;
    }

    public String getPaymode() {
        return paymode;
    }

    public void setPaymode(String paymode) {
        this.paymode = paymode;
    }
}
