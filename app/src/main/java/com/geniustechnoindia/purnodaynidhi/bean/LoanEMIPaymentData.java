package com.geniustechnoindia.purnodaynidhi.bean;



public class LoanEMIPaymentData extends LoanData {
    private String OfficeID,PayMode,DepACCLCode,SavingsAC,
                    FromCode,ToCode,ChequeNo,UserName,EMIDetails,Remarks;
    private Integer PaymentDate,TotalPaymentAmount,ActualEMIAmount,ChequeDate;
    private Float BounceCharge,RecoveryCharge,ExcessAmount;
    private Boolean IsDueLateFine;
    private String sbCode,payUTransID;

    public Float getExcessAmount() {
        return ExcessAmount;
    }

    public void setExcessAmount(Float excessAmount) {
        ExcessAmount = excessAmount;
    }

    public String getPayUTransID() {
        return payUTransID;
    }

    public void setPayUTransID(String payUTransID) {
        this.payUTransID = payUTransID;
    }

    public String getSbCode() {
        return sbCode;
    }

    public void setSbCode(String sbCode) {
        this.sbCode = sbCode;
    }

    public String getOfficeID() {
        return OfficeID;
    }

    public void setOfficeID(String officeID) {
        OfficeID = officeID;
    }

    public String getPayMode() {
        return PayMode;
    }

    public void setPayMode(String payMode) {
        PayMode = payMode;
    }

    public String getDepACCLCode() {
        return DepACCLCode;
    }

    public void setDepACCLCode(String depACCLCode) {
        DepACCLCode = depACCLCode;
    }

    public String getSavingsAC() {
        return SavingsAC;
    }

    public void setSavingsAC(String savingsAC) {
        SavingsAC = savingsAC;
    }

    public String getFromCode() {
        return FromCode;
    }

    public void setFromCode(String fromCode) {
        FromCode = fromCode;
    }

    public String getToCode() {
        return ToCode;
    }

    public void setToCode(String toCode) {
        ToCode = toCode;
    }

    public String getChequeNo() {
        return ChequeNo;
    }

    public void setChequeNo(String chequeNo) {
        ChequeNo = chequeNo;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getEMIDetails() {
        return EMIDetails;
    }

    public void setEMIDetails(String EMIDetails) {
        this.EMIDetails = EMIDetails;
    }

    public String getRemarks() {
        return Remarks;
    }

    public void setRemarks(String remarks) {
        Remarks = remarks;
    }

    public Integer getPaymentDate() {
        return PaymentDate;
    }

    public void setPaymentDate(Integer paymentDate) {
        PaymentDate = paymentDate;
    }

    public Integer getTotalPaymentAmount() {
        return TotalPaymentAmount;
    }

    public void setTotalPaymentAmount(Integer totalPaymentAmount) {
        TotalPaymentAmount = totalPaymentAmount;
    }

    public Integer getActualEMIAmount() {
        return ActualEMIAmount;
    }

    public void setActualEMIAmount(Integer actualEMIAmount) {
        ActualEMIAmount = actualEMIAmount;
    }

    public Integer getChequeDate() {
        return ChequeDate;
    }

    public void setChequeDate(Integer chequeDate) {
        ChequeDate = chequeDate;
    }

    public Float getBounceCharge() {
        return BounceCharge;
    }

    public void setBounceCharge(Float bounceCharge) {
        BounceCharge = bounceCharge;
    }

    public Float getRecoveryCharge() {
        return RecoveryCharge;
    }

    public void setRecoveryCharge(Float recoveryCharge) {
        RecoveryCharge = recoveryCharge;
    }

    public Boolean getDueLateFine() {
        return IsDueLateFine;
    }

    public void setDueLateFine(Boolean dueLateFine) {
        IsDueLateFine = dueLateFine;
    }
}
