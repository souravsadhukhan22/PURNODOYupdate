package com.geniustechnoindia.purnodaynidhi.model;


import androidx.annotation.NonNull;

public class ActiveSearchSBDetailsSetGet {

    private String accCode,accType,appliName;

    public String getAccCode() {
        return accCode;
    }

    public void setAccCode(String accCode) {
        this.accCode = accCode;
    }

    public String getAccType() {
        return accType;
    }

    public void setAccType(String accType) {
        this.accType = accType;
    }

    public String getAppliName() {
        return appliName;
    }

    public void setAppliName(String appliName) {
        this.appliName = appliName;
    }

    @NonNull
    @Override
    public String toString() {
        return accCode;
    }
}
