package com.geniustechnoindia.purnodaynidhi.dl;


import android.util.Log;
import android.widget.Toast;

import com.geniustechnoindia.purnodaynidhi.bean.MemberData;
import com.geniustechnoindia.purnodaynidhi.bean.TempDataBean;
import com.geniustechnoindia.purnodaynidhi.mssql.SqlManager;
import com.geniustechnoindia.purnodaynidhi.store.GlobalStore;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;


public class MemberManagement {
    Connection cn;

    public MemberManagement(){
        cn= new SqlManager().getSQLConnection();
    }

    public ArrayList<MemberData> getMemberDataList(String SearchBy, String MemberCode, String MemberName,
                                                   String FromDate, String ToDate){
        ArrayList<MemberData> mList = new ArrayList<MemberData>();
        MemberData mData;
        try{
            if (cn != null){
                CallableStatement smt = cn.prepareCall("{call ADROID_GetMemberView(?,?,?,?,?)}");
                smt.setString("@SearchBy",SearchBy);
                smt.setString("@MemberCode",MemberCode);
                smt.setString("@MemberName",MemberName);
                smt.setString("@FromDate",FromDate);
                smt.setString("@ToDate",ToDate);
                smt.execute();
                ResultSet rs=smt.getResultSet();
                while(rs.next()){
                    mData = new MemberData();
                    mData.setMemberCode(rs.getString("MemberCode"));
                    mData.setMemberName(rs.getString("MemberName"));
                    mData.setDateOfJoin(rs.getString("DateOfJoin"));
                    mData.setFather(rs.getString("Father"));
                    mData.setAddress(rs.getString("Address"));
                    mData.setPhoneNo(rs.getString("PhoneNo"));
                    mData.setMemberDOB(rs.getString("MemberDOB"));
                    mData.setGender(rs.getString("Gender"));
                    mData.setRegAmt(rs.getDouble("RegAmt"));
                    mData.setChequeNo(rs.getString("ChequeNo"));
                    mData.setChequeDate(rs.getString("ChequeDate"));
                    mData.setNominee(rs.getString("Nominee"));
                    mData.setNomineeRelation(rs.getString("NomineeRelation"));
                    mData.setNomineeDOB(rs.getString("NomineeDOB"));
                    mData.setSponsorCode(rs.getString("SponsorCode"));
                    mData.setErrorCode(0);
                    mList.add(mData);
                }
            }else{
                mData = new MemberData();
                mData.setErrorCode(2);
                mData.setErrorString("Network related problem.");
                mList.add(mData);
            }
        }catch (Exception ex){
            mData = new MemberData();
            mData.setErrorCode(1);
            mData.setErrorString(ex.getMessage().toString());
            mList.add(mData);
            /*ErrorManagement<MemberData> m=new ErrorManagement<MemberData>(MemberData.class);
            mData = m.setErrorCode(1,ex.getMessage().toString());*/
        }
        return mList;
    }

    public boolean insertMember(MemberData m){
        boolean rValue = false;
        try{
            if (cn != null){
                CallableStatement smt = cn.prepareCall("{call ADROID_InsertOrUpdateMember(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");

                smt.setString("@Action","Insert");
                smt.setString("@MemberCode","");
                smt.setString("@FormNo","");
                smt.setString("@OfficeID", GlobalStore.GlobalValue.getOfficeID());
                smt.setString("@EMAIL",m.getEmailId());
                smt.setString("@MemberName",m.getMemberName());
                smt.setString("@MemberDOB",m.getMemberDOB());
                smt.setString("@Age","0");
                smt.setString("@Father",m.getFather());
                smt.setString("@Address",m.getAddress());
                //smt.setString("@District",m.getDistrict());
                smt.setString("@State",m.getState());
                smt.setString("@PIN",m.getPinCode());
                smt.setString("@Phone",m.getPhoneNo());
                smt.setString("@NomineeCode",m.getNomineeCode());
                smt.setString("@Nominee",m.getNominee());
                smt.setString("@NomineeDOB",m.getNomineeDOB());
                smt.setInt("@NomineeAge",m.getNomineeAge());
                smt.setString("@NRelation",m.getNomineeRelation());
                smt.setDouble("@RegAmt",m.getRegAmt());
                smt.setDouble("@PassBookCharge",0);
                smt.setDouble("@ShareAmount",m.getShareAmount());
                smt.setDouble("@NoOfShare",0);
                smt.setDouble("@ThriftFundAmount",0);
                smt.setString("@FromCode","");
                smt.setString("@ToCode","");
                smt.setString("@PayType","");
                smt.setString("@DepACCLCode","");
                smt.setString("@ChequeNo","");
                smt.setString("@ChequeDate",m.getDateOfJoin());
                smt.setString("@BankName",m.getBankName());
                //smt.setString("@BranchName",m.getBranchName());
                smt.setString("@IFSCCode",m.getBankIfscCode());
                smt.setString("@Remarks","");
                smt.setString("@UserName",GlobalStore.GlobalValue.getUserName());
                smt.setString("@MemberType","");
                smt.setString("@BloodGr",m.getBloodGroup());
                smt.setString("@Sex",m.getGender());
                smt.setString("@Occupation","");
                smt.setString("@Edu","");
                smt.setString("@Idproof",m.getSelectedIdProofName());
                smt.setString("@IdproofNo",m.getSelectedIdProofNo());
                smt.setString("@AddrProof",m.getSelectedAddressProofName());
                smt.setString("@AddrProofNo",m.getSelectedAddressProofNo());
                smt.setString("@SignProof",m.getSignProofName());
                smt.setString("@PAN",m.getPanNo());
                smt.setString("@PassportNo","");
                smt.setString("@AccountNo",m.getBankAccNo());
                smt.setObject("@Pics", m.getImageEncodedString());
                smt.setObject("@Signature", m.getSignatureEncodingString());

                smt.setObject("@idFront", m.getIdFrontEncodingString());
                smt.setObject("@idBack", m.getIdBackEncodingString());
                smt.setObject("@panPic", m.getPanEncodingString());

                //smt.setObject("@IDPhoto",m.getIdEncodingString());
                //smt.setObject("@AddrPhoto",m.getAddressEncodingString());


                //smt.setObject("@BankPhoto",m.getBankAccEncodingString());

                smt.setString("@SponsorCode",m.getIntroCode());
                smt.setString("@IsPinconToUniMember","0");
                smt.registerOutParameter("@ReturnVoucherNo",java.sql.Types.VARCHAR);
                smt.setString("@USERBCODE","");
                smt.setString("@RetirementDate","");
                smt.registerOutParameter("@MCode",java.sql.Types.VARCHAR);
                smt.registerOutParameter("@ErrorCode",java.sql.Types.INTEGER);

                smt.executeUpdate();
                Log.d("smt", "insertMember: "+smt);
                Integer ReturnERRORCode  = smt.getInt("@ErrorCode");
                TempDataBean.NewMemberErrorCode=ReturnERRORCode;
                TempDataBean.tempMemberCode = smt.getString("@MCode");
                if (ReturnERRORCode == 0){
                    rValue=true;
                }else{
                    rValue=false;
                }
            }
        }catch (Exception ex){
            rValue=false;
            /*ErrorManagement<MemberData> m=new ErrorManagement<MemberData>(MemberData.class);
            mData = m.setErrorCode(1,ex.getMessage().toString());*/
        }
        return rValue;    }

   /* public boolean insertMember(MemberData m){
        boolean rValue = false;
        try{
            if (cn != null){
                CallableStatement smt = cn.prepareCall("{call ADROID_InsertOrUpdateMember_Temp(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
                smt.setString("@Action","Insert");
                smt.setString("@MemberCode","");
                smt.setString("@FormNo","");
                smt.setString("@OfficeID", GlobalStore.GlobalValue.getOfficeID());
                smt.setString("@EMAIL",m.getEmailId());
                smt.setString("@MemberName",m.getMemberName());
                smt.setString("@MemberDOB",m.getMemberDOB());
                smt.setString("@Age","0");
                smt.setString("@Father",m.getFather());
                smt.setString("@Address",m.getAddress());
                smt.setString("@State",m.getState());
                smt.setString("@PIN",m.getPinCode());
                smt.setString("@Phone",m.getPhoneNo());
                smt.setString("@NomineeCode",m.getNomineeCode());
                smt.setString("@Nominee",m.getNominee());
                smt.setString("@NomineeDOB",m.getNomineeDOB());
                smt.setString("@NRelation",m.getNomineeRelation());
                smt.setDouble("@RegAmt",m.getRegAmt());
                smt.setDouble("@PassBookCharge",0);
                smt.setDouble("@ShareAmount",m.getShareAmount());
                smt.setDouble("@NoOfShare",0);
                smt.setDouble("@ThriftFundAmount",0);
                smt.setString("@FromCode","");
                smt.setString("@ToCode","");
                smt.setString("@PayType","");
                smt.setString("@DepACCLCode","");
                smt.setString("@ChequeNo","");
                smt.setString("@ChequeDate",m.getDateOfJoin());
                smt.setString("@BankName",m.getBankName());
                smt.setString("@IFSCCode",m.getBankIfscCode());
                smt.setString("@Remarks","");
                smt.setString("@UserName",GlobalStore.GlobalValue.getUserName());
                smt.setString("@MemberType","");
                smt.setString("@BloodGr",m.getBloodGroup());
                smt.setString("@Sex",m.getGender());
                smt.setString("@Occupation","");
                smt.setString("@Edu","");
                smt.setString("@Idproof",m.getSelectedIdProofName());
                smt.setString("@IdproofNo",m.getSelectedIdProofNo());
                smt.setString("@AddrProof",m.getSelectedAddressProofName());
                smt.setString("@AddrProofNo",m.getSelectedAddressProofNo());
                smt.setString("@SignProof",m.getSignProofName());
                smt.setString("@PAN",m.getPanNo());
                smt.setString("@PassportNo","");
                smt.setString("@AccountNo",m.getBankAccNo());
                smt.setObject("@Pics", m.getImageEncodedString());
                smt.setObject("@Signature", m.getSignatureEncodingString());

                smt.setObject("@IDPhoto",m.getIdEncodingString());
                smt.setObject("@AddrPhoto",m.getAddressEncodingString());
                //smt.setObject("@BankPhoto",m.getBankAccEncodingString());

                smt.setString("@SponsorCode",GlobalStore.GlobalValue.getArrangerMemberCode());
                smt.setString("@IsPinconToUniMember","0");
                smt.registerOutParameter("@ReturnVoucherNo",java.sql.Types.VARCHAR);
                smt.setString("@USERBCODE","");
                smt.setString("@RetirementDate","");
                smt.registerOutParameter("@MCode",java.sql.Types.VARCHAR);
                smt.registerOutParameter("@ErrorCode",java.sql.Types.INTEGER);
                smt.executeUpdate();
                Integer ReturnERRORCode  = smt.getInt("@ErrorCode");
                TempDataBean.NewMemberErrorCode=ReturnERRORCode;
                if (ReturnERRORCode == 0){
                    rValue=true;
                }else{
                    rValue=false;
                }
            }
        }catch (Exception ex){
            rValue=false;
            *//*ErrorManagement<MemberData> m=new ErrorManagement<MemberData>(MemberData.class);
            mData = m.setErrorCode(1,ex.getMessage().toString());*//*
        }
        return rValue;
    }*/


    public boolean insertMemberr(MemberData m){
        boolean rValue = false;
        try{
            if (cn != null){
                CallableStatement smt = cn.prepareCall("{call ADROID_InsertOrUpdateMember(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
                smt.setString("@Action","Insert");
                smt.setString("@MemberCode","");
                smt.setString("@FormNo",m.getFormNo());
                smt.setString("@OfficeID", GlobalStore.GlobalValue.getOfficeID());
                smt.setString("@EMAIL",m.getEmailId());
                smt.setString("@MemberName",m.getMemberName());
                smt.setString("@MemberDOB",m.getMemberDOB());
                smt.setString("@Age","0");
                smt.setString("@Father",m.getFather());
                smt.setString("@Address",m.getAddress());
                smt.setString("@District",m.getDistrict());
                smt.setString("@State",m.getState());
                smt.setString("@PIN",m.getPinCode());
                smt.setString("@Phone",m.getPhoneNo());
                smt.setString("@NomineeCode",m.getNomineeCode());
                smt.setString("@Nominee",m.getNominee());
                smt.setString("@NomineeDOB",m.getNomineeDOB());
                smt.setString("@NRelation",m.getNomineeRelation());
                smt.setDouble("@RegAmt",m.getRegAmt());
                smt.setDouble("@PassBookCharge",0);
                smt.setDouble("@ShareAmount",m.getShareAmount());
                smt.setDouble("@NoOfShare",0);
                smt.setDouble("@ThriftFundAmount",0);
                smt.setString("@FromCode","");
                smt.setString("@ToCode","");
                smt.setString("@PayType","");
                smt.setString("@DepACCLCode","");
                smt.setString("@ChequeNo","");
                smt.setString("@ChequeDate",m.getDateOfJoin());
                smt.setString("@BankName",m.getBankName());
                smt.setString("@BranchName",m.getBranchName());
                smt.setString("@IFSCCode",m.getBankIfscCode());
                smt.setString("@Remarks","");
                smt.setString("@UserName",GlobalStore.GlobalValue.getUserName());
                smt.setString("@MemberType","");
                smt.setString("@BloodGr",m.getBloodGroup());
                smt.setString("@Sex",m.getGender());
                smt.setString("@Occupation","");
                smt.setString("@Edu","");
                smt.setString("@Idproof",m.getSelectedIdProofName());
                smt.setString("@IdproofNo",m.getSelectedIdProofNo());
                smt.setString("@AddrProof",m.getSelectedAddressProofName());
                smt.setString("@AddrProofNo",m.getSelectedAddressProofNo());
                //smt.setString("@SignProof",m.getSignProofName());
                smt.setString("@SignProof","");
                smt.setString("@PAN",m.getPanNo());

                smt.setString("@Combination",m.getmStr_combination());

                smt.setString("@PassportNo","");
                smt.setString("@AccountNo",m.getBankAccNo());
                smt.setObject("@Pics", m.getImageEncodedString());
                smt.setObject("@Signature", m.getSignatureEncodingString());

                smt.setObject("@IDPhoto",m.getIdEncodingString());
                smt.setObject("@AddrPhoto",m.getAddressEncodingString());


                //smt.setObject("@BankPhoto",m.getBankAccEncodingString());

                smt.setString("@SponsorCode",GlobalStore.GlobalValue.getArrangerMemberCode());
                smt.setString("@IsPinconToUniMember","0");
                smt.registerOutParameter("@ReturnVoucherNo",java.sql.Types.VARCHAR);
                smt.setString("@USERBCODE","");
                smt.setString("@RetirementDate","");
                smt.registerOutParameter("@MCode",java.sql.Types.VARCHAR);
                smt.registerOutParameter("@ErrorCode",java.sql.Types.INTEGER);
                smt.executeUpdate();
                Integer ReturnERRORCode  = smt.getInt("@ErrorCode");
                TempDataBean.NewMemberErrorCode=ReturnERRORCode;
                TempDataBean.tempMemberCode = smt.getString("@MCode");
                if (ReturnERRORCode == 0){
                    rValue=true;
                }else{
                    rValue=false;
                }
            }
        }catch (Exception ex){
            rValue=false;
            //ErrorManagement<MemberData> m=new ErrorManagement<MemberData>(MemberData.class);
            //mData = m.setErrorCode(1,ex.getMessage().toString());
        }
        return rValue;
    }
}
