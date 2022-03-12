package com.geniustechnoindia.purnodaynidhi.dl;

import com.geniustechnoindia.purnodaynidhi.model.BalanceSetGet;
import com.geniustechnoindia.purnodaynidhi.mssql.SqlManager;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;

public class BalanceManagement {
    Connection connection;

    public BalanceManagement(){
        connection = new SqlManager().getSQLConnection();
    }

    public BalanceSetGet getBalance(String memberCode){
        BalanceSetGet balanceSetGet = new BalanceSetGet();
            try{
                if (connection != null){
                    CallableStatement smt = connection.prepareCall("{call USP_WalletDetailsArrangerCodeWise(?,?)}");         // Change SP name
                    smt.setString("@ArrangerCode",memberCode);                                                 // Change parameter
                    smt.setString("@UserName","");
                    smt.execute();
                    ResultSet rs=smt.getResultSet();
                    while(rs.next()){
                        balanceSetGet.setBalance(rs.getString("WBal"));                                           // Change key
                    }
                } else {
                    balanceSetGet.setErrorCode(2);
                    balanceSetGet.setErrorString("Network related problem.");
                }
            } catch(Exception ex){
                balanceSetGet.setErrorCode(1);
                balanceSetGet.setErrorString(ex.getMessage());
            }
        return balanceSetGet;
    }

}
