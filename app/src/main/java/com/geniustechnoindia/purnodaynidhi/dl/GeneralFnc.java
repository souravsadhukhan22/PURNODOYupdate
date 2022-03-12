package com.geniustechnoindia.purnodaynidhi.dl;

import com.geniustechnoindia.purnodaynidhi.mssql.SqlManager;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;


public class GeneralFnc {

    Connection cn;

    public GeneralFnc() {
        cn = new SqlManager().getSQLConnection();
    }

    public ArrayList<String> getRelation() {
        ArrayList<String> arrayList = new ArrayList<String>();
        arrayList.add("");
        try {
            if (cn != null) {
                CallableStatement smt = cn.prepareCall("{call ADROID_GetRelationListUserWise}");
                smt.execute();
                ResultSet rs = smt.getResultSet();
                while (rs.next()) {
                    arrayList.add(rs.getString("RelationName"));
                }
            }
        } catch (Exception ex) {
            //rValue = false;
            ex.getMessage();
        } finally {
            if (cn != null) {
                try {
                    cn.close();
                } catch (Exception e) {
                    //
                }
            }
        }
        return arrayList;
    }


    // ADROID_GetSystemConfigInfoByConfigField
    public ArrayList<String> getShareAmount() {
        ArrayList<String> arrayList = new ArrayList<String>();
        try {
            if (cn != null) {
                CallableStatement smt = cn.prepareCall("{call ADROID_GetSystemConfigInfoByConfigField}");
                smt.execute();
                ResultSet rs = smt.getResultSet();
                while (rs.next()) {
                    arrayList.add(rs.getString("ConfigValue"));
                }
            }
        } catch (Exception ex) {
            //rValue = false;
            ex.getMessage();
        } finally {
            if (cn != null) {
                try {
                    cn.close();
                } catch (Exception e) {
                    //
                }
            }
        }
        return arrayList;
    }


    public ArrayList<String> getStatelist(){
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("");
        try{
            if(cn != null){
                CallableStatement smt = cn.prepareCall("{call ADROID_GetStateListUserWise}");
                smt.execute();
                ResultSet rs = smt.getResultSet();
                while (rs.next()){
                    arrayList.add(rs.getString("statename"));
                }
            }
        } catch(Exception e){
            e.getMessage();
        } finally {
            if(cn != null){
                try{
                    cn.close();
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        return arrayList;
    }



    public ArrayList<String> getDistlist(String stateName){
        ArrayList<String> arrayList = new ArrayList<>();
        try{
            if(cn != null){
                CallableStatement smt = cn.prepareCall("{call ADROID_GetDistListStateWise(?)}");
                smt.setString("@STATENAME",stateName);
                smt.execute();
                ResultSet rs = smt.getResultSet();
                while (rs.next()){
                    arrayList.add(rs.getString("DISTRICTNAME"));
                }
            }
        } catch(Exception e){
            e.getMessage();
        } finally {
            if(cn != null){
                try{
                    cn.close();
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        return arrayList;
    }


}
