package com.geniustechnoindia.purnodaynidhi.model;


import androidx.annotation.NonNull;

public class PlanCodeSetGet {
    private String sName;
    private String aFlag;
    private String fullName;
    private String prefix;
    private String modeFlag;

    public String getSName() {
        return sName;
    }

    public void setSName(String sName) {
        this.sName = sName;
    }

    public String getAFlag() {
        return aFlag;
    }

    public void setAFlag(String aFlag) {
        this.aFlag = aFlag;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getModeFlag() {
        return modeFlag;
    }

    public void setModeFlag(String modeFlag) {
        this.modeFlag = modeFlag;
    }

    @NonNull
    @Override
    public String toString() {
        return fullName;
    }
}
