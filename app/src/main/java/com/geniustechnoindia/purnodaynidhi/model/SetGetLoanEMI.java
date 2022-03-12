package com.geniustechnoindia.purnodaynidhi.model;

public class SetGetLoanEMI {
    String loanCode,emiNo,emiDueDate,paymentDate,actualEmiAmnt,emiAmount,principleAmount,interestAmount,currentBalance,payMode,remarks;

    public String getPayMode() {
        return payMode;
    }

    public void setPayMode(String payMode) {
        this.payMode = payMode;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getLoanCode() {
        return loanCode;
    }

    public void setLoanCode(String loanCode) {
        this.loanCode = loanCode;
    }

    public String getEmiNo() {
        return emiNo;
    }

    public void setEmiNo(String emiNo) {
        this.emiNo = emiNo;
    }

    public String getEmiDueDate() {
        return emiDueDate;
    }

    public void setEmiDueDate(String emiDueDate) {
        this.emiDueDate = emiDueDate;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getActualEmiAmnt() {
        return actualEmiAmnt;
    }

    public void setActualEmiAmnt(String actualEmiAmnt) {
        this.actualEmiAmnt = actualEmiAmnt;
    }

    public String getEmiAmount() {
        return emiAmount;
    }

    public void setEmiAmount(String emiAmount) {
        this.emiAmount = emiAmount;
    }

    public String getPrincipleAmount() {
        return principleAmount;
    }

    public void setPrincipleAmount(String principleAmount) {
        this.principleAmount = principleAmount;
    }

    public String getInterestAmount() {
        return interestAmount;
    }

    public void setInterestAmount(String interestAmount) {
        this.interestAmount = interestAmount;
    }

    public String getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(String currentBalance) {
        this.currentBalance = currentBalance;
    }
}
