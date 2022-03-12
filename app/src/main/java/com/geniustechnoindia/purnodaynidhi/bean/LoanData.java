package com.geniustechnoindia.purnodaynidhi.bean;


public class LoanData extends ErrorData {
    private String LoanCode,LoanTableID,HolderName,LoanDate,EMIMode,LCode,InstLCode,phone;
    private Integer LoanTerm;
    private Float LoanApproveAmount,EMIAmount,EMIPercentage,NetLoanAmount;
    private Boolean IsReducingEMI;
    private Integer LastEMINo,ServerDate;

    private Float processingFee;
    private String memberID,phoneNo;

    private String currBalance;
    private String phNo;

    private String holderPhoto;

    public String getPhNo() {
        return phNo;
    }

    public void setPhNo(String phNo) {
        this.phNo = phNo;
    }

    public String getCurrBalance() {
        return currBalance;
    }

    public void setCurrBalance(String currBalance) {
        this.currBalance = currBalance;
    }

    public String getMemberID() {
        return memberID;
    }

    public void setMemberID(String memberID) {
        this.memberID = memberID;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public Float getProcessingFee() {
        return processingFee;
    }

    public void setProcessingFee(Float processingFee) {
        this.processingFee = processingFee;
    }

    public Integer getServerDate() {
        return ServerDate;
    }

    public void setServerDate(Integer serverDate) {
        ServerDate = serverDate;
    }

    public Integer getLastEMINo() {
        return LastEMINo;
    }

    public void setLastEMINo(Integer lastEMINo) {
        LastEMINo = lastEMINo;
    }

    public String getLCode() {
        return LCode;
    }

    public void setLCode(String LCode) {
        this.LCode = LCode;
    }

    public String getInstLCode() {
        return InstLCode;
    }

    public void setInstLCode(String instLCode) {
        InstLCode = instLCode;
    }

    public Boolean getReducingEMI() {
        return IsReducingEMI;
    }

    public void setReducingEMI(Boolean reducingEMI) {
        IsReducingEMI = reducingEMI;
    }

    public String getLoanCode() {
        return LoanCode;
    }

    public void setLoanCode(String loanCode) {
        LoanCode = loanCode;
    }

    public String getLoanTableID() {
        return LoanTableID;
    }

    public void setLoanTableID(String loanTableID) {
        LoanTableID = loanTableID;
    }

    public String getHolderName() {
        return HolderName;
    }

    public void setHolderName(String holderName) {
        HolderName = holderName;
    }

    public String getLoanDate() {
        return LoanDate;
    }

    public void setLoanDate(String loanDate) {
        LoanDate = loanDate;
    }

    public String getEMIMode() {
        return EMIMode;
    }

    public void setEMIMode(String EMIMode) {
        this.EMIMode = EMIMode;
    }

    public Integer getLoanTerm() {
        return LoanTerm;
    }

    public void setLoanTerm(Integer loanTerm) {
        LoanTerm = loanTerm;
    }

    public Float getLoanApproveAmount() {
        return LoanApproveAmount;
    }

    public void setLoanApproveAmount(Float loanApproveAmount) {
        LoanApproveAmount = loanApproveAmount;
    }

    public Float getEMIAmount() {
        return EMIAmount;
    }

    public void setEMIAmount(Float EMIAmount) {
        this.EMIAmount = EMIAmount;
    }

    public Float getEMIPercentage() {
        return EMIPercentage;
    }

    public void setEMIPercentage(Float EMIPercentage) {
        this.EMIPercentage = EMIPercentage;
    }

    public Float getNetLoanAmount() {
        return NetLoanAmount;
    }

    public void setNetLoanAmount(Float netLoanAmount) {
        NetLoanAmount = netLoanAmount;
    }

    public String getPhone() {
        return phone;
    }

    public Object getHolderPhoto() {
        return holderPhoto;
    }

    public void setHolderPhoto(String holderPhoto) {
        this.holderPhoto = holderPhoto;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
