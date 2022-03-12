package com.geniustechnoindia.purnodaynidhi.model;

public class SetGetMemberPolicyList {
    private String policyCode,applicantName,dateOfCom,maturityDate,applicantPhone,mode,emailID;
    private int term,amount,depositAmt,maturityAmt;
    private boolean ismatured;

    public String getApplicantName() {
        return applicantName;
    }

    public void setApplicantName(String applicantName) {
        this.applicantName = applicantName;
    }

    public String getEmailID() {
        return emailID;
    }

    public void setEmailID(String emailID) {
        this.emailID = emailID;
    }

    public boolean isIsmatured() {
        return ismatured;
    }

    public void setIsmatured(boolean ismatured) {
        this.ismatured = ismatured;
    }

    public String getPolicyCode() {
        return policyCode;
    }

    public void setPolicyCode(String policyCode) {
        this.policyCode = policyCode;
    }

    public String getDateOfCom() {
        return dateOfCom;
    }

    public void setDateOfCom(String dateOfCom) {
        this.dateOfCom = dateOfCom;
    }

    public String getMaturityDate() {
        return maturityDate;
    }

    public void setMaturityDate(String maturityDate) {
        this.maturityDate = maturityDate;
    }

    public String getApplicantPhone() {
        return applicantPhone;
    }

    public void setApplicantPhone(String applicantPhone) {
        this.applicantPhone = applicantPhone;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public int getTerm() {
        return term;
    }

    public void setTerm(int term) {
        this.term = term;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getDepositAmt() {
        return depositAmt;
    }

    public void setDepositAmt(int depositAmt) {
        this.depositAmt = depositAmt;
    }

    public int getMaturityAmt() {
        return maturityAmt;
    }

    public void setMaturityAmt(int maturityAmt) {
        this.maturityAmt = maturityAmt;
    }

}
