package com.geniustechnoindia.purnodaynidhi.model;

public class AdminCashSheetSetGet {

    private String receivedName,paymentName;
    private Double receivedAmt,paymentAmt;

    public String getReceivedName() {
        return receivedName;
    }

    public void setReceivedName(String receivedName) {
        this.receivedName = receivedName;
    }

    public String getPaymentName() {
        return paymentName;
    }

    public void setPaymentName(String paymentName) {
        this.paymentName = paymentName;
    }

    public Double getReceivedAmt() {
        return receivedAmt;
    }

    public void setReceivedAmt(Double receivedAmt) {
        this.receivedAmt = receivedAmt;
    }

    public Double getPaymentAmt() {
        return paymentAmt;
    }

    public void setPaymentAmt(Double paymentAmt) {
        this.paymentAmt = paymentAmt;
    }
}
