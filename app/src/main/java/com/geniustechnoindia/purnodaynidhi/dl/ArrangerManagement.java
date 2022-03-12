package com.geniustechnoindia.purnodaynidhi.dl;


import com.geniustechnoindia.purnodaynidhi.bean.ArrangerData;
import com.geniustechnoindia.purnodaynidhi.mssql.SqlManager;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;

public class ArrangerManagement {

    Connection cn;

    public ArrangerManagement(){
        cn= new SqlManager().getSQLConnection();
    }

    public ArrayList<ArrangerData> getArrangerDataList(String SearchBy, String MemberCode, String MemberName,
                                                       String FromDate, String ToDate){
        ArrayList<ArrangerData> mList = new ArrayList<ArrangerData>();
        ArrangerData mData;
        try{
            if (cn != null){
                CallableStatement smt = cn.prepareCall("{call ADROID_GetArrangerView(?,?,?,?,?)}");
                smt.setString("@SearchBy",SearchBy);
                smt.setString("@ArrangerCode",MemberCode);
                smt.setString("@ArrangerName",MemberName);
                smt.setString("@FromDate",FromDate);
                smt.setString("@ToDate",ToDate);
                smt.execute();
                ResultSet rs=smt.getResultSet();
                while(rs.next()){
                    mData = new ArrangerData();
                    mData.setArrangerCode(rs.getString("ArrangerCode"));
                    mData.setArrangerName(rs.getString("ArrangerName"));
                    mData.setDateOfJoin(rs.getString("DateOfJoin"));
                    mData.setFather(rs.getString("Father"));
                    mData.setAddress(rs.getString("Address"));
                    mData.setPhoneNo(rs.getString("PhoneNo"));
                    mData.setArrangerDOB(rs.getString("ArrangerDOB"));
                    mData.setGender(rs.getString("Gender"));
                    mData.setRegAmt(rs.getDouble("RegAmt"));
                    mData.setChequeNo(rs.getString("ChequeNo"));
                    mData.setRank(rs.getString("Rank"));
                    mData.setNominee(rs.getString("Nominee"));
                    mData.setNomineeRelation(rs.getString("NomineeRelation"));
                    mData.setNomineeDOB(rs.getString("NomineeDOB"));
                    mData.setSponsorCode(rs.getString("SponsorCode"));
                    mData.setErrorCode(0);
                    mList.add(mData);
                }
            }else{
                mData = new ArrangerData();
                mData.setErrorCode(2);
                mData.setErrorString("Network related problem.");
                mList.add(mData);
            }
        }catch (Exception ex){
            mData = new ArrangerData();
            mData.setErrorCode(1);
            mData.setErrorString(ex.getMessage().toString());
            mList.add(mData);
            /*ErrorManagement<MemberData> m=new ErrorManagement<MemberData>(MemberData.class);
            mData = m.setErrorCode(1,ex.getMessage().toString());*/
        }
        return mList;
    }

    public ArrayList<ArrangerData> getArrangerTreeList(String ArrangerCode){
        ArrayList<ArrangerData> mList = new ArrayList<ArrangerData>();
        ArrangerData mData;
        try{
            if (cn != null){
                CallableStatement smt = cn.prepareCall("{call ADROID_GetArrangerTeamTree(?)}");
                smt.setString("@ArrangerCode",ArrangerCode);
                smt.execute();
                ResultSet rs=smt.getResultSet();
                while(rs.next()){
                    mData = new ArrangerData();
                    mData.setArrangerCode(rs.getString("ArrangerCode"));
                    mData.setArrangerName(rs.getString("Name"));
                    mData.setDateOfJoin(rs.getString("DateOfJoin"));
                    mData.setPhoneNo(rs.getString("Phone"));;
                    mData.setRank(rs.getString("Rank"));
                    mData.setSponsorCode(rs.getString("SponsorCode"));
                    mData.setOfficeName(rs.getString("OfficeName"));
                    mData.setErrorCode(0);
                    mList.add(mData);
                }
            }else{
                mData = new ArrangerData();
                mData.setErrorCode(2);
                mData.setErrorString("Network related problem.");
                mList.add(mData);
            }
        }catch (Exception ex){
            mData = new ArrangerData();
            mData.setErrorCode(1);
            mData.setErrorString(ex.getMessage().toString());
            mList.add(mData);
            /*ErrorManagement<MemberData> m=new ErrorManagement<MemberData>(MemberData.class);
            mData = m.setErrorCode(1,ex.getMessage().toString());*/
        }

        return mList;
    }


}
