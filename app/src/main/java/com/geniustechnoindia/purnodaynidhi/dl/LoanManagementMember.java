package com.geniustechnoindia.purnodaynidhi.dl;

import com.geniustechnoindia.purnodaynidhi.bean.ErrorData;
import com.geniustechnoindia.purnodaynidhi.bean.LoanData;
import com.geniustechnoindia.purnodaynidhi.bean.LoanEMIPaymentData;
import com.geniustechnoindia.purnodaynidhi.mssql.SqlManager;
import com.geniustechnoindia.purnodaynidhi.store.GlobalStore;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class LoanManagementMember {

    Connection cn;
    public LoanManagementMember(){ this.cn = new SqlManager().getSQLConnection();}

    public ArrayList<LinkedHashMap<String, String>> getLoanStatementReportData(String LoanCode, String MemberCode) throws Exception {

        ArrayList<LinkedHashMap<String, String>> list=new ArrayList<LinkedHashMap<String, String>>();
        LinkedHashMap<String, String> reportRow;

        if(cn!=null){
            CallableStatement smt=cn.prepareCall("{call ADROID_GetLoanStatement(?,?)}");
            smt.setString(1, LoanCode);
            smt.setString(2, MemberCode);
            //smt.setString(3, GlobalData.getUserType().toString());
            smt.execute();
            ResultSet rs=smt.getResultSet();
            while(rs.next()){
                reportRow = new LinkedHashMap<String, String>();
                reportRow.put("PolicyCode",rs.getString("LoanCode"));
                reportRow.put("InstNo", String.valueOf(rs.getInt("EMINo")));
                reportRow.put("RenewDate",rs.getString("PaymentDate"));
                reportRow.put("Amount", String.valueOf(rs.getFloat("DR")));
                reportRow.put("LateFine", String.valueOf(rs.getFloat("CR")));
                list.add(reportRow);
            }
        }
        return list;
    }

    public ErrorData insertLoanEMI(LoanEMIPaymentData lpd){
        ErrorData errData= new ErrorData();
        try{
            if (cn != null){
                CallableStatement smt = cn.prepareCall("{call ADROID_InsertOrUpdateLoanEMIPaymentMember(?,?,?,?,?,?)}");
                smt.setString("@LoanCode",lpd.getLoanCode());
                smt.setString("@UserName", GlobalStore.GlobalValue.getMemberCode());
                smt.setString("@EMIDetails",lpd.getEMIDetails());
                smt.setString("@PayUTransID",lpd.getPayUTransID());
                smt.setBoolean("@isLatePaid",lpd.getDueLateFine());
                smt.registerOutParameter("@IsError", Types.INTEGER);
                smt.executeUpdate();
                errData.setErrorCode(smt.getInt("@IsError"));
            }else{
                errData.setErrorCode(2);
                errData.setErrorString("Connection failed");
            }
        }catch(Exception ex){
            errData.setErrorCode(1);
            errData.setErrorString(ex.getMessage().toString());
        }
        return  errData;
    }

    public LoanData getLoanData(String loanCode){
        LoanData loanData = new LoanData();
        try{
            if (cn != null){
                CallableStatement smt = cn.prepareCall("{call ADROID_GetLoanDetails(?)}");
                smt.setString("@LoanCode",loanCode);
                smt.execute();
                ResultSet rs=smt.getResultSet();
                while(rs.next()){
                    loanData.setLoanCode(rs.getString("LoanCode"));
                    loanData.setHolderName(rs.getString("HolderName"));
                    loanData.setLoanTableID(rs.getString("LoanTableID"));
                    loanData.setLoanDate(rs.getString("LoanDate"));
                    loanData.setLoanApproveAmount(rs.getFloat("LoanApproveAmount"));
                    loanData.setLoanTerm(rs.getInt("LoanTerm"));
                    loanData.setEMIAmount(rs.getFloat("EMIAmount"));
                    loanData.setEMIMode(rs.getString("EMIMode"));
                    loanData.setCurrBalance(rs.getString("CurrantBalance"));
                    loanData.setEMIPercentage(rs.getFloat("EMIPercentage"));
                    loanData.setNetLoanAmount(rs.getFloat("NetLoanAmount"));
                    loanData.setLCode(rs.getString("LCode"));
                    loanData.setInstLCode(rs.getString("InstLCode"));
                    loanData.setReducingEMI(rs.getBoolean("IsReducingEMI"));
                    loanData.setLastEMINo(rs.getInt("EMINo"));
                    loanData.setServerDate(rs.getInt("ServerDate"));
                    loanData.setPhNo(rs.getString("phno"));
                    loanData.setErrorCode(0);
                }
            }else{
                loanData.setErrorCode(2);
                loanData.setErrorString("Network related problem.");
            }
        }catch(Exception ex){
            loanData.setErrorCode(1);
            loanData.setErrorString(ex.getMessage().toString());
        }
        return loanData;
    }
}
