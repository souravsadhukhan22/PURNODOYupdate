package com.geniustechnoindia.purnodaynidhi.model;

public class SetGetLoanDueReport {
    String loanCode,ApplicantName,phNo,totalDueEmiNo,totalDueEmiAmnt;

    public String getLoanCode() {
        return loanCode;
    }

    public void setLoanCode(String loanCode) {
        this.loanCode = loanCode;
    }

    public String getApplicantName() {
        return ApplicantName;
    }

    public void setApplicantName(String applicantName) {
        ApplicantName = applicantName;
    }

    public String getPhNo() {
        return phNo;
    }

    public void setPhNo(String phNo) {
        this.phNo = phNo;
    }

    public String getTotalDueEmiNo() {
        return totalDueEmiNo;
    }

    public void setTotalDueEmiNo(String totalDueEmiNo) {
        this.totalDueEmiNo = totalDueEmiNo;
    }

    public String getTotalDueEmiAmnt() {
        return totalDueEmiAmnt;
    }

    public void setTotalDueEmiAmnt(String totalDueEmiAmnt) {
        this.totalDueEmiAmnt = totalDueEmiAmnt;
    }
}
