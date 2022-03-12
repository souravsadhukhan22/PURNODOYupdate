package com.geniustechnoindia.purnodaynidhi.model;

public class MemberLoanListSetGet {
    private String loanCode,memberCode,holderName,addr,phone,emiMode;
    private boolean isClose;
    private int emiAmt;

    public String getEmiMode() {
        return emiMode;
    }

    public void setEmiMode(String emiMode) {
        this.emiMode = emiMode;
    }

    public int getEmiAmt() {
        return emiAmt;
    }

    public void setEmiAmt(int emiAmt) {
        this.emiAmt = emiAmt;
    }

    public String getLoanCode() {
        return loanCode;
    }

    public void setLoanCode(String loanCode) {
        this.loanCode = loanCode;
    }

    public String getMemberCode() {
        return memberCode;
    }

    public void setMemberCode(String memberCode) {
        this.memberCode = memberCode;
    }

    public String getHolderName() {
        return holderName;
    }

    public void setHolderName(String holderName) {
        this.holderName = holderName;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isClose() {
        return isClose;
    }

    public void setClose(boolean close) {
        isClose = close;
    }
}
