package com.geniustechnoindia.purnodaynidhi.model;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProviderNewSetGet {

    @SerializedName("NAME")
    @Expose
    private String nAME;
    @SerializedName("CODE")
    @Expose
    private String cODE;
    @SerializedName("TYPE")
    @Expose
    private String tYPE;
    @SerializedName("REMARKS")
    @Expose
    private String rEMARKS;

    public String getNAME() {
        return nAME;
    }

    public void setNAME(String nAME) {
        this.nAME = nAME;
    }

    public String getCODE() {
        return cODE;
    }

    public void setCODE(String cODE) {
        this.cODE = cODE;
    }

    public String getTYPE() {
        return tYPE;
    }

    public void setTYPE(String tYPE) {
        this.tYPE = tYPE;
    }

    public String getREMARKS() {
        return rEMARKS;
    }

    public void setREMARKS(String rEMARKS) {
        this.rEMARKS = rEMARKS;
    }

    @NonNull
    @Override
    public String toString() {
        return nAME;
    }
}