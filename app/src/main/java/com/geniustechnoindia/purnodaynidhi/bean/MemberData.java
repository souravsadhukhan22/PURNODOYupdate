package com.geniustechnoindia.purnodaynidhi.bean;



public class MemberData extends ErrorData {
    private String memberCode,memberName,phoneNo,payType,panNo,nominee,
            nomineeRelation,dateOfJoin,memberDOB,nomineeDOB,chequeDate,
            father,address,gender,chequeNo,sponsorCode, pinCode,introCode;

    private int nomineeAge;

    private byte[] imageEncodedString;
    private byte[] signatureEncodingString;
    private byte[] idEncodingString;
    private byte[] addressEncodingString;
    private byte[] bankAccEncodingString;

    private byte[] idFrontEncodingString;
    private byte[] idBackEncodingString;
    private byte[] panEncodingString;

    public int getNomineeAge() {
        return nomineeAge;
    }

    public void setNomineeAge(int nomineeAge) {
        this.nomineeAge = nomineeAge;
    }

    public byte[] getIdFrontEncodingString() {
        return idFrontEncodingString;
    }

    public void setIdFrontEncodingString(byte[] idFrontEncodingString) {
        this.idFrontEncodingString = idFrontEncodingString;
    }

    public byte[] getIdBackEncodingString() {
        return idBackEncodingString;
    }

    public void setIdBackEncodingString(byte[] idBackEncodingString) {
        this.idBackEncodingString = idBackEncodingString;
    }

    public byte[] getPanEncodingString() {
        return panEncodingString;
    }

    public void setPanEncodingString(byte[] panEncodingString) {
        this.panEncodingString = panEncodingString;
    }

    private String mStr_combination,formNo;

    public String getIntroCode() {
        return introCode;
    }

    public void setIntroCode(String introCode) {
        this.introCode = introCode;
    }

    public String getmStr_combination() {
        return mStr_combination;
    }

    public void setmStr_combination(String mStr_combination) {
        this.mStr_combination = mStr_combination;
    }

    public String getFormNo() {
        return formNo;
    }

    public void setFormNo(String formNo) {
        this.formNo = formNo;
    }

    public byte[] getIdEncodingString() {
        return idEncodingString;
    }

    private String district;

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public void setIdEncodingString(byte[] idEncodingString) {
        this.idEncodingString = idEncodingString;
    }

    public byte[] getAddressEncodingString() {
        return addressEncodingString;
    }

    public void setAddressEncodingString(byte[] addressEncodingString) {
        this.addressEncodingString = addressEncodingString;
    }

    public byte[] getBankAccEncodingString() {
        return bankAccEncodingString;
    }

    public void setBankAccEncodingString(byte[] bankAccEncodingString) {
        this.bankAccEncodingString = bankAccEncodingString;
    }

    private String emailId;
    private String state;
    private String nomineeCode;
    private String bloodGroup;
    private String selectedIdProofName;
    private String selectedAddressProofName;
    private String selectedIdProofNo;
    private String selectedAddressProofNo;
    private float shareAmount;
    private String signProofName;

    public String getSignProofName() {
        return signProofName;
    }

    public void setSignProofName(String signProofName) {
        this.signProofName = signProofName;
    }

    public float getShareAmount() {
        return shareAmount;
    }

    public void setShareAmount(float shareAmount) {
        this.shareAmount = shareAmount;
    }

    private String bankName,branchName,bankAccNo,bankIfscCode;

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankAccNo() {
        return bankAccNo;
    }

    public void setBankAccNo(String bankAccNo) {
        this.bankAccNo = bankAccNo;
    }

    public String getBankIfscCode() {
        return bankIfscCode;
    }

    public void setBankIfscCode(String bankIfscCode) {
        this.bankIfscCode = bankIfscCode;
    }

    public String getSelectedIdProofNo() {
        return selectedIdProofNo;
    }

    public void setSelectedIdProofNo(String selectedIdProofNo) {
        this.selectedIdProofNo = selectedIdProofNo;
    }

    public String getSelectedAddressProofNo() {
        return selectedAddressProofNo;
    }

    public void setSelectedAddressProofNo(String selectedAddressProofNo) {
        this.selectedAddressProofNo = selectedAddressProofNo;
    }

    public String getSelectedIdProofName() {
        return selectedIdProofName;
    }

    public void setSelectedIdProofName(String selectedIdProofName) {
        this.selectedIdProofName = selectedIdProofName;
    }

    public String getSelectedAddressProofName() {
        return selectedAddressProofName;
    }

    public void setSelectedAddressProofName(String selectedAddressProofName) {
        this.selectedAddressProofName = selectedAddressProofName;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getNomineeCode() {
        return nomineeCode;
    }

    public void setNomineeCode(String nomineeCode) {
        this.nomineeCode = nomineeCode;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public byte[] getSignatureEncodingString() {
        return signatureEncodingString;
    }

    public void setSignatureEncodingString(byte[] signatureEncodingString) {
        this.signatureEncodingString = signatureEncodingString;
    }

    public byte[] getImageEncodedString() {
        return imageEncodedString;
    }

    public void setImageEncodedString(byte[] imageEncodedString) {
        this.imageEncodedString = imageEncodedString;
    }

    private double regAmt;

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public String getSponsorCode() {
        return sponsorCode;
    }

    public void setSponsorCode(String sponsorCode) {
        this.sponsorCode = sponsorCode;
    }

    public String getChequeNo() {
        return chequeNo;
    }

    public void setChequeNo(String chequeNo) {
        this.chequeNo = chequeNo;
    }

    public double getRegAmt() {
        return regAmt;
    }

    public void setRegAmt(double regAmt) {
        this.regAmt = regAmt;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMemberCode() {
        return memberCode;
    }

    public void setMemberCode(String memberCode) {
        this.memberCode = memberCode;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getPanNo() {
        return panNo;
    }

    public void setPanNo(String panNo) {
        this.panNo = panNo;
    }

    public String getNominee() {
        return nominee;
    }

    public void setNominee(String nominee) {
        this.nominee = nominee;
    }

    public String getNomineeRelation() {
        return nomineeRelation;
    }

    public void setNomineeRelation(String nomineeRelation) {
        this.nomineeRelation = nomineeRelation;
    }

    public String getDateOfJoin() {
        return dateOfJoin;
    }

    public void setDateOfJoin(String dateOfJoin) {
        this.dateOfJoin = dateOfJoin;
    }

    public String getMemberDOB() {
        return memberDOB;
    }

    public void setMemberDOB(String memberDOB) {
        this.memberDOB = memberDOB;
    }

    public String getNomineeDOB() {
        return nomineeDOB;
    }

    public void setNomineeDOB(String nomineeDOB) {
        this.nomineeDOB = nomineeDOB;
    }

    public String getChequeDate() {
        return chequeDate;
    }

    public void setChequeDate(String chequeDate) {
        this.chequeDate = chequeDate;
    }

    public String getFather() {
        return father;
    }

    public void setFather(String father) {
        this.father = father;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
