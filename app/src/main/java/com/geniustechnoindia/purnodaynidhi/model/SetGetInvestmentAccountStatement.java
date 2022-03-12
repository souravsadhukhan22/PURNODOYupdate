package com.geniustechnoindia.purnodaynidhi.model;

public class SetGetInvestmentAccountStatement {
    String policyCode, instNo, renewDate, amount, lateFine;

    public String getPolicyCode() {
        return policyCode;
    }

    public void setPolicyCode(String policyCode) {
        this.policyCode = policyCode;
    }

    public String getInstNo() {
        return instNo;
    }

    public void setInstNo(String instNo) {
        this.instNo = instNo;
    }

    public String getRenewDate() {
        return renewDate;
    }

    public void setRenewDate(String renewDate) {
        this.renewDate = renewDate;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getLateFine() {
        return lateFine;
    }

    public void setLateFine(String lateFine) {
        this.lateFine = lateFine;
    }
}
