package com.geniustechnoindia.purnodaynidhi.bean;



public class PolicyData extends ErrorData {
    private String PolicyCode,ApplicantName,ArrangerCode,CollectorCode,
            DateOfCom,MaturityDate,PTable,Mode,PlanCode,amountUptoPrevMonth,isLateFineApplicable,latePercentage,dueDate,LAST_DEP_DATE,policyAmount;
    private int Term;
    private double Amount,DepositeAmount,MaturityAmount;
    private String holderPhoto;

    public String getHolderPhoto() {
        return holderPhoto;
    }

    public void setHolderPhoto(String holderPhoto) {
        this.holderPhoto = holderPhoto;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getAmountUptoPrevMonth() {
        return amountUptoPrevMonth;
    }

    public void setAmountUptoPrevMonth(String amountUptoPrevMonth) {
        this.amountUptoPrevMonth = amountUptoPrevMonth;
    }

    public String getIsLateFineApplicable() {
        return isLateFineApplicable;
    }

    public void setIsLateFineApplicable(String isLateFineApplicable) {
        this.isLateFineApplicable = isLateFineApplicable;
    }

    public String getLatePercentage() {
        return latePercentage;
    }

    public void setLatePercentage(String latePercentage) {
        this.latePercentage = latePercentage;
    }

    public String getPlanCode() {
        return PlanCode;
    }

    public void setPlanCode(String planCode) {
        PlanCode = planCode;
    }

    public String getPolicyCode() {
        return PolicyCode;
    }

    public void setPolicyCode(String policyCode) {
        PolicyCode = policyCode;
    }

    public String getApplicantName() {
        return ApplicantName;
    }

    public void setApplicantName(String applicantName) {
        ApplicantName = applicantName;
    }

    public String getArrangerCode() {
        return ArrangerCode;
    }

    public void setArrangerCode(String arrangerCode) {
        ArrangerCode = arrangerCode;
    }

    public String getCollectorCode() {
        return CollectorCode;
    }

    public void setCollectorCode(String collectorCode) {
        CollectorCode = collectorCode;
    }

    public String getDateOfCom() {
        return DateOfCom;
    }

    public void setDateOfCom(String dateOfCom) {
        DateOfCom = dateOfCom;
    }

    public String getMaturityDate() {
        return MaturityDate;
    }

    public void setMaturityDate(String maturityDate) {
        MaturityDate = maturityDate;
    }

    public String getPTable() {
        return PTable;
    }

    public void setPTable(String PTable) {
        this.PTable = PTable;
    }

    public String getMode() {
        return Mode;
    }

    public void setMode(String mode) {
        Mode = mode;
    }

    public int getTerm() {
        return Term;
    }

    public void setTerm(int term) {
        Term = term;
    }

    public double getAmount() {
        return Amount;
    }

    public void setAmount(double amount) {
        Amount = amount;
    }

    public double getDepositeAmount() {
        return DepositeAmount;
    }

    public void setDepositeAmount(double depositeAmount) {
        DepositeAmount = depositeAmount;
    }

    public double getMaturityAmount() {
        return MaturityAmount;
    }

    public void setMaturityAmount(double maturityAmount) {
        MaturityAmount = maturityAmount;
    }

    public void setLAST_DEP_DATE(String LAST_DEP_DATE) {
        this.LAST_DEP_DATE = LAST_DEP_DATE;
    }

    public String getLAST_DEP_DATE() {
        return LAST_DEP_DATE;
    }
    public void setPolicyAmount(String policyAmount) {
        this.policyAmount = policyAmount;
    }

    public String getPolicyAmount() {
        return policyAmount;
    }
}
