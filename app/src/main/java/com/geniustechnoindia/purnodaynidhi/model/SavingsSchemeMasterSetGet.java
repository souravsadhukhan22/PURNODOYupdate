package com.geniustechnoindia.purnodaynidhi.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SavingsSchemeMasterSetGet {

    @SerializedName("TypeCode")
    @Expose
    private String typeCode;
    @SerializedName("TypeName")
    @Expose
    private String typeName;
    @SerializedName("MinimumOpeningBalance")
    @Expose
    private Double minimumOpeningBalance;
    @SerializedName("ROI")
    @Expose
    private Double roi;
    @SerializedName("isActive")
    @Expose
    private Boolean isActive;
    @SerializedName("PrefixName")
    @Expose
    private String prefixName;
    @SerializedName("FromLCode")
    @Expose
    private String fromLCode;
    @SerializedName("IntLCode")
    @Expose
    private String intLCode;
    @SerializedName("SetMinimumBalance")
    @Expose
    private Boolean setMinimumBalance;
    @SerializedName("AccountClosingDuration")
    @Expose
    private Integer accountClosingDuration;

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public Double getMinimumOpeningBalance() {
        return minimumOpeningBalance;
    }

    public void setMinimumOpeningBalance(Double minimumOpeningBalance) {
        this.minimumOpeningBalance = minimumOpeningBalance;
    }

    public Double getRoi() {
        return roi;
    }

    public void setRoi(Double roi) {
        this.roi = roi;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getPrefixName() {
        return prefixName;
    }

    public void setPrefixName(String prefixName) {
        this.prefixName = prefixName;
    }

    public String getFromLCode() {
        return fromLCode;
    }

    public void setFromLCode(String fromLCode) {
        this.fromLCode = fromLCode;
    }

    public String getIntLCode() {
        return intLCode;
    }

    public void setIntLCode(String intLCode) {
        this.intLCode = intLCode;
    }

    public Boolean getSetMinimumBalance() {
        return setMinimumBalance;
    }

    public void setSetMinimumBalance(Boolean setMinimumBalance) {
        this.setMinimumBalance = setMinimumBalance;
    }

    public Integer getAccountClosingDuration() {
        return accountClosingDuration;
    }

    public void setAccountClosingDuration(Integer accountClosingDuration) {
        this.accountClosingDuration = accountClosingDuration;
    }

    @Override
    public String toString() {
        return typeName;
    }
}
