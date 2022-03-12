package com.geniustechnoindia.purnodaynidhi.bean;

public class AdminInfoStaticData {

    private static String adminUserName,adminPassword,adminUserTypeId,adminOfcId,adminIsActivate,adminId;
    private static boolean loginStatus;

    public static boolean isLoginStatus() {
        return loginStatus;
    }

    public static void setLoginStatus(boolean loginStatus) {
        AdminInfoStaticData.loginStatus = loginStatus;
    }

    public static String getAdminUserName() {
        return adminUserName;
    }

    public static void setAdminUserName(String adminUserName) {
        AdminInfoStaticData.adminUserName = adminUserName;
    }

    public static String getAdminPassword() {
        return adminPassword;
    }

    public static void setAdminPassword(String adminPassword) {
        AdminInfoStaticData.adminPassword = adminPassword;
    }

    public static String getAdminUserTypeId() {
        return adminUserTypeId;
    }

    public static void setAdminUserTypeId(String adminUserTypeId) {
        AdminInfoStaticData.adminUserTypeId = adminUserTypeId;
    }

    public static String getAdminOfcId() {
        return adminOfcId;
    }

    public static void setAdminOfcId(String adminOfcId) {
        AdminInfoStaticData.adminOfcId = adminOfcId;
    }

    public static String getAdminIsActivate() {
        return adminIsActivate;
    }

    public static void setAdminIsActivate(String adminIsActivate) {
        AdminInfoStaticData.adminIsActivate = adminIsActivate;
    }

    public static String getAdminId() {
        return adminId;
    }

    public static void setAdminId(String adminId) {
        AdminInfoStaticData.adminId = adminId;
    }
}
