package com.geniustechnoindia.purnodaynidhi.model;


import androidx.annotation.NonNull;

public class PlanTableSetGet {
    private String scheme;
    private String sTable;
    private Integer sTerm;
    private Integer mTerm;
    private Integer minAmount;
    private Double mAmt;
    private Integer misMonthlyRent;
    private String prefix;
    private Integer rOI;
    private Integer multyOf;
    private String modeFlag;
    private Integer mlyMat;
    private Integer qlyMat;
    private Integer hlyMat;
    private Integer ylyMat;

    public String getScheme() {
        return scheme;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    public String getSTable() {
        return sTable;
    }

    public void setSTable(String sTable) {
        this.sTable = sTable;
    }

    public Integer getSTerm() {
        return sTerm;
    }

    public void setSTerm(Integer sTerm) {
        this.sTerm = sTerm;
    }

    public Integer getMTerm() {
        return mTerm;
    }

    public void setMTerm(Integer mTerm) {
        this.mTerm = mTerm;
    }

    public Integer getMinAmount() {
        return minAmount;
    }

    public void setMinAmount(Integer minAmount) {
        this.minAmount = minAmount;
    }

    public Double getMAmt() {
        return mAmt;
    }

    public void setMAmt(Double mAmt) {
        this.mAmt = mAmt;
    }

    public Integer getMisMonthlyRent() {
        return misMonthlyRent;
    }

    public void setMisMonthlyRent(Integer misMonthlyRent) {
        this.misMonthlyRent = misMonthlyRent;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public Integer getROI() {
        return rOI;
    }

    public void setROI(Integer rOI) {
        this.rOI = rOI;
    }

    public Integer getMultyOf() {
        return multyOf;
    }

    public void setMultyOf(Integer multyOf) {
        this.multyOf = multyOf;
    }

    public String getModeFlag() {
        return modeFlag;
    }

    public void setModeFlag(String modeFlag) {
        this.modeFlag = modeFlag;
    }

    public Integer getMlyMat() {
        return mlyMat;
    }

    public void setMlyMat(Integer mlyMat) {
        this.mlyMat = mlyMat;
    }

    public Integer getQlyMat() {
        return qlyMat;
    }

    public void setQlyMat(Integer qlyMat) {
        this.qlyMat = qlyMat;
    }

    public Integer getHlyMat() {
        return hlyMat;
    }

    public void setHlyMat(Integer hlyMat) {
        this.hlyMat = hlyMat;
    }

    public Integer getYlyMat() {
        return ylyMat;
    }

    public void setYlyMat(Integer ylyMat) {
        this.ylyMat = ylyMat;
    }


    @NonNull
    @Override
    public String toString() {
        return sTable;
    }
}
