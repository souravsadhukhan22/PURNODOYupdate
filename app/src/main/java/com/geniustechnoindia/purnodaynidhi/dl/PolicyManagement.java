package com.geniustechnoindia.purnodaynidhi.dl;

import com.geniustechnoindia.purnodaynidhi.bean.PolicyData;
import com.geniustechnoindia.purnodaynidhi.bean.RenewalData;
import com.geniustechnoindia.purnodaynidhi.mssql.SqlManager;
import com.geniustechnoindia.purnodaynidhi.others.TempData;
import com.geniustechnoindia.purnodaynidhi.store.GlobalStore;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;


public class PolicyManagement {

    Connection cn;

    public PolicyManagement(){
        cn= new SqlManager().getSQLConnection();
    }

    public ArrayList<PolicyData> getPolicyDataList(String FromDate, String ToDate){

        ArrayList<PolicyData> mList = new ArrayList<PolicyData>();
        PolicyData mData;
        try{
            if (cn != null){
                CallableStatement smt = cn.prepareCall("{call ADROID_GetPolicyList(?,?)}");
                smt.setString("@FromDate",FromDate);
                smt.setString("@ToDate",ToDate);
                smt.execute();
                ResultSet rs=smt.getResultSet();
                while(rs.next()){
                    mData = new PolicyData();
                    mData.setPolicyCode(rs.getString("PolicyCode"));
                    mData.setArrangerCode(rs.getString("ArrangerCode"));
                    mData.setDateOfCom(rs.getString("DateOfCom"));
                    mData.setPTable(rs.getString("PTable"));
                    mData.setAmount(rs.getFloat("Amount"));
                    mData.setMaturityDate(rs.getString("CDate"));
                    mData.setErrorCode(0);
                    mList.add(mData);
                }
            }else{
                mData = new PolicyData();
                mData.setErrorCode(2);
                mData.setErrorString("Network related problem.");
                mList.add(mData);
            }
        }catch (Exception ex){
            mData = new PolicyData();
            mData.setErrorCode(1);
            mData.setErrorString(ex.getMessage().toString());
            mList.add(mData);
            /*ErrorManagement<MemberData> m=new ErrorManagement<MemberData>(MemberData.class);
            mData = m.setErrorCode(1,ex.getMessage().toString());*/
        }
        return mList;
    }

    public RenewalData getPolicyDataForRenwal(String PolicyCode){
        RenewalData rData = new RenewalData();
        try{
            if (cn != null){
                CallableStatement smt = cn.prepareCall("{call ADROID_GetPolicyForRenewal(?,?)}");
                smt.setString("@PolicyCode",PolicyCode);
                String usr=GlobalStore.GlobalValue.getUserName();
                smt.setString("@ACode",GlobalStore.GlobalValue.getUserName());

                smt.execute();
                ResultSet rs=smt.getResultSet();
                while(rs.next()){
                    rData.setPolicyCode(rs.getString("PolicyCode"));
                    rData.setDateOfCom(rs.getString("DateOfCom"));
                    rData.setApplicantName(rs.getString("ApplicantName"));
                    rData.setPlanCode(rs.getString("PlanCode"));
                    rData.setPTable(rs.getString("PTable"));
                    rData.setTerm(rs.getInt("Term"));
                    rData.setLAST_DEP_DATE(rs.getString("LAST_DEP_DATE"));
                    rData.setPolicyAmount(rs.getString("PolicyAmount"));
                    rData.setAmount(rs.getDouble("Amount"));
                    rData.setLastInstNo(rs.getInt("LastInstNo"));
                    rData.setMode(rs.getString("Mode"));
                    rData.setMaturityDate(rs.getString("CDate"));
                    TempData.phoneNumber = rs.getString("PhoneNo");
                    rData.setAmountUptoPrevMonth(rs.getString("TillAmount"));
                    rData.setIsLateFineApplicable(rs.getString("IsLateFineApplicable"));
                    rData.setLatePercentage(rs.getString("LatePercentage"));
                    rData.setHolderPhoto(rs.getString("Pics"));
                }
            } else {
                rData.setErrorCode(2);
                rData.setErrorString("Network related problem.");
            }
        }catch(Exception ex){
            rData.setErrorCode(1);
            rData.setErrorString(ex.getMessage().toString());
        }
        return rData;
    }

    public int insertRenewal(String PolicyCode, String InstNoFrom, String InstNoTo, float lateFine, boolean isLateFine, boolean isPaid) {
        boolean rValue = false;
        int ReturnERRORCode = 1;
        try {
            if (cn != null) {
                CallableStatement smt = cn.prepareCall("{call ADROID_InsertRenewal(?,?,?,?,?,?,?,?,?,?,?)}");
                smt.setString("@PolicyCode", PolicyCode);
                smt.setString("@FromInstNo", InstNoFrom);
                smt.setString("@ToInstNo", InstNoTo);
                smt.setString("@UserName", GlobalStore.GlobalValue.getUserName());
                smt.setString("@loginOfficeID", GlobalStore.GlobalValue.getOfficeID());
                smt.setFloat("@lateFine", lateFine);
                smt.setBoolean("@isLateFine",isLateFine);
                smt.setBoolean("@isLatePaid",isPaid);
                smt.setString("@CollectionLocation","");
                smt.registerOutParameter("@ReturnVoucherNo", java.sql.Types.VARCHAR);
                smt.registerOutParameter("@IsError", java.sql.Types.INTEGER);
                smt.executeUpdate();
                ReturnERRORCode = smt.getInt("@IsError");
                /*if (ReturnERRORCode == 0) {
                    rValue = true;
                } else {
                    rValue = false;
                }*/
                return ReturnERRORCode;
            }
        } catch (Exception ex) {
            //rValue = false;
            return ReturnERRORCode;
            /*ErrorManagement<MemberData> m=new ErrorManagement<MemberData>(MemberData.class);
            mData = m.setErrorCode(1,ex.getMessage().toString());*/
        }
        return ReturnERRORCode;
    }
}
