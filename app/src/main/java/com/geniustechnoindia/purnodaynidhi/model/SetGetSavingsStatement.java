package com.geniustechnoindia.purnodaynidhi.model;

public class SetGetSavingsStatement {
    String tDate, payMode, depositAmnt, withdrawAmnt;

    public String getDate() {
        return tDate;
    }

    public void settDate(String tDate) {
        this.tDate = tDate;
    }

    public String getPayMode() {
        return payMode;
    }

    public void setPayMode(String payMode) {
        this.payMode = payMode;
    }

    public String getDepositAmnt() {
        return depositAmnt;
    }

    public void setDepositAmnt(String depositAmnt) {
        this.depositAmnt = depositAmnt;
    }

    public String getWithdrawAmnt() {
        return withdrawAmnt;
    }

    public void setWithdrawAmnt(String withdrawAmnt) {
        this.withdrawAmnt = withdrawAmnt;
    }
}
