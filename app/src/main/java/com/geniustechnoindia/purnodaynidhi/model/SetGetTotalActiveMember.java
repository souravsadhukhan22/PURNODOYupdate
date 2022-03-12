package com.geniustechnoindia.purnodaynidhi.model;

public class SetGetTotalActiveMember {


    String ItemNo;
    String MemberCode;
    String MemberName;
    String Address;
    String Phone;

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }



    public String getItemNo() {
        return ItemNo;
    }

    public void setItemNo(String itemNo) {
        ItemNo = itemNo;
    }

    public String getMemberCode() {
        return MemberCode;
    }

    public void setMemberCode(String memberCode) {
        MemberCode = memberCode;
    }

    public String getMemberName() {
        return MemberName;
    }

    public void setMemberName(String memberName) {
        MemberName = memberName;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public SetGetTotalActiveMember(String itemNo, String memberCode, String memberName,String address, String phone) {
        ItemNo = itemNo;
        MemberCode = memberCode;
        MemberName = memberName;
        Address=address;
        Phone = phone;

    }
}
