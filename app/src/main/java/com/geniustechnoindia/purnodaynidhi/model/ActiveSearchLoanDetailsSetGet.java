package com.geniustechnoindia.purnodaynidhi.model;


import androidx.annotation.NonNull;

public class ActiveSearchLoanDetailsSetGet {
    private String loanCode,name,loanTerm,emiMode,loanApproveAmt,emiAmt;

    public String getLoanCode() {
        return loanCode;
    }

    public void setLoanCode(String loanCode) {
        this.loanCode = loanCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLoanTerm() {
        return loanTerm;
    }

    public void setLoanTerm(String loanTerm) {
        this.loanTerm = loanTerm;
    }

    public String getEmiMode() {
        return emiMode;
    }

    public void setEmiMode(String emiMode) {
        this.emiMode = emiMode;
    }

    public String getLoanApproveAmt() {
        return loanApproveAmt;
    }

    public void setLoanApproveAmt(String loanApproveAmt) {
        this.loanApproveAmt = loanApproveAmt;
    }

    public String getEmiAmt() {
        return emiAmt;
    }

    public void setEmiAmt(String emiAmt) {
        this.emiAmt = emiAmt;
    }

    @NonNull
    @Override
    public String toString() {
        return loanCode;
    }
}
