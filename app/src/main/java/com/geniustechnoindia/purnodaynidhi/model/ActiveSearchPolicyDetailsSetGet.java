package com.geniustechnoindia.purnodaynidhi.model;


import androidx.annotation.NonNull;

public class ActiveSearchPolicyDetailsSetGet {

    private String policyCode,name,plan,term,mode,amount,maturityAmt;

    public String getPolicyCode() {
        return policyCode;
    }

    public void setPolicyCode(String policyCode) {
        this.policyCode = policyCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlan() {
        return plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getMaturityAmt() {
        return maturityAmt;
    }

    public void setMaturityAmt(String maturityAmt) {
        this.maturityAmt = maturityAmt;
    }

    @NonNull
    @Override
    public String toString() {
        return policyCode;
    }
}
