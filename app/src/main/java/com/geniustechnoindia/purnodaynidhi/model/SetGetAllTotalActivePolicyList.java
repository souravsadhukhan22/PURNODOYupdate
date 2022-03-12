package com.geniustechnoindia.purnodaynidhi.model;

public class SetGetAllTotalActivePolicyList {
    String ItemNo;
    String PolicyCode;
    String ApplicantName;
    String ApplicantPhone;
    String DepositeAmount;

    public SetGetAllTotalActivePolicyList(String itemNo, String policyCode, String applicantName, String applicantPhone, String depositeAmount) {
        ItemNo = itemNo;
        PolicyCode = policyCode;
        ApplicantName = applicantName;
        ApplicantPhone = applicantPhone;
        DepositeAmount = depositeAmount;
    }

    public String getItemNo() {
        return ItemNo;
    }

    public void setItemNo(String itemNo) {
        ItemNo = itemNo;
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

    public String getApplicantPhone() {
        return ApplicantPhone;
    }

    public void setApplicantPhone(String applicantPhone) {
        ApplicantPhone = applicantPhone;
    }

    public String getDepositeAmount() {
        return DepositeAmount;
    }

    public void setDepositeAmount(String depositeAmount) {
        DepositeAmount = depositeAmount;
    }
}
