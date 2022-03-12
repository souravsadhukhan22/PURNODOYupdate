package com.geniustechnoindia.purnodaynidhi.bean;


public class LoanEMIBrakupData extends ErrorData {
    private String PayDate;
    private Integer Period,DateDifference,LateFineAmt;
    private Float Payment,Interest,Principle,CurrentBalance,LateFinePerc;

    public Integer getLateFineAmt() {
        return LateFineAmt;
    }

    public void setLateFineAmt(Integer lateFineAmt) {
        LateFineAmt = lateFineAmt;
    }

    public Float getLateFinePerc() {
        return LateFinePerc;
    }

    public void setLateFinePerc(Float lateFinePerc) {
        LateFinePerc = lateFinePerc;
    }

    public Integer getDateDifference() {
        return DateDifference;
    }

    public void setDateDifference(Integer dateDifference) {
        DateDifference = dateDifference;
    }

    public String getPayDate() {
        return PayDate;
    }

    public void setPayDate(String payDate) {
        PayDate = payDate;
    }

    public Integer getPeriod() {
        return Period;
    }

    public void setPeriod(Integer period) {
        Period = period;
    }

    public Float getPayment() {
        return Payment;
    }

    public void setPayment(Float payment) {
        Payment = payment;
    }

    public Float getInterest() {
        return Interest;
    }

    public void setInterest(Float interest) {
        Interest = interest;
    }

    public Float getPrinciple() {
        return Principle;
    }

    public void setPrinciple(Float principle) {
        Principle = principle;
    }

    public Float getCurrentBalance() {
        return CurrentBalance;
    }

    public void setCurrentBalance(Float currentBalance) {
        CurrentBalance = currentBalance;
    }
}
