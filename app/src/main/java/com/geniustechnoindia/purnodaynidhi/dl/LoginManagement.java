package com.geniustechnoindia.purnodaynidhi.dl;

import com.geniustechnoindia.purnodaynidhi.bean.AppData;
import com.geniustechnoindia.purnodaynidhi.mssql.SqlManager;
import com.geniustechnoindia.purnodaynidhi.store.GlobalStore;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;

public class LoginManagement {
    Connection cn;

    public LoginManagement(){
        cn= new SqlManager().getSQLConnection();
    }

    // Agent Login
    public boolean isLoginAgentSuccessful(String userName, String password){
        boolean rValue=false;
        String ut="";
        AppData ad;
        try{
            if (cn != null){
                CallableStatement smt=cn.prepareCall("{call ADROID_Agent_LoginValidation(?,?)}");
                smt.setString(1,userName);
                smt.setString(2,password);
                smt.execute();
                ResultSet rs=smt.getResultSet();
                while(rs.next()){
                    ad = new AppData();
                    ad.setUserName(rs.getString("UserName"));
                    ad.setArrangerMemberCode(rs.getString("MemberCode"));
                    ad.setUserOriginalName(rs.getString("UserOriginalName"));
                    ad.setUserTypeID(rs.getInt("UserTypeID"));
                    ad.setOfficeID(rs.getString("OfficeID"));
                    GlobalStore.GlobalValue = ad;
                    rValue=true;
                }
            }
        }catch(Exception ex){
            rValue = false;
        }
        finally {
            if (cn != null) {
                try {
                    cn.close();
                } catch (Exception e) {
                    //
                }
            }
        }
        return rValue;
    }

    // Customer
    public boolean isLoginMemberSuccessful(String username, String password, int memberDob, String memberPhone){
        boolean rValue=false;
        String ut="";
        AppData ad;
        try{
            if (cn != null){
                CallableStatement smt=cn.prepareCall("{call ADROID_MemberLogin(?,?,?,?)}");
                smt.setString("@username",username);
                smt.setString("@password",password);
                smt.setInt("@MemberDOB",memberDob);
                smt.setString("@Phone",memberPhone);
                smt.execute();
                ResultSet rs=smt.getResultSet();
                while(rs.next()){
                    ad = new AppData();
                    ad.setMemberCode(rs.getString("MemberCode"));
                    ad.setMemberName(rs.getString("MemberName"));
                    ad.setOfficeID(rs.getString("OfficeID"));
                    ad.setDateOfJoin(rs.getString("DateOfJoin"));
                    ad.setDateOfEnt(rs.getString("DateOfJoin"));
                    ad.setMemberDob(rs.getString("MemberDOB"));
                    ad.setPhone(rs.getString("MemberPhone"));
                    //ad.setAppEasyPinSetOrNot(rs.getString("appEasyPinIsSetOrNot"));
                    //ad.setAppEasyPin(rs.getString("appEasyPin"));
                    GlobalStore.GlobalValue = ad;
                    rValue=true;
                }
            }
        }catch(Exception ex){
            rValue = false;
        }
        finally {
            if (cn != null) {
                try {
                    cn.close();
                } catch (Exception e) {
                    //
                }
            }
        }
        return rValue;
    }



}
