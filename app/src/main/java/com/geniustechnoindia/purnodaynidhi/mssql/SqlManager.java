package com.geniustechnoindia.purnodaynidhi.mssql;

import android.os.StrictMode;

import java.sql.Connection;
import java.sql.DriverManager;

public class SqlManager {

    public Connection getSQLConnection(){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection conn = null;
        String driver="net.sourceforge.jtds.jdbc.Driver";
        try
        {
            Class.forName(driver).newInstance();
            String connString="jdbc:jtds:sqlserver://" + "65.1.99.186:1232" + ";" + "databaseName=" +
                    "GTECH_1908PRNDYND" + ";user=" + "DEV_SVKSR186" + ";password=" + "b!2c!vT#xTyvT3v^fg{d!sx8ED" + ";";

            conn= DriverManager.getConnection(connString);
        }
        catch(Exception ex){
            conn = null;
        }
        return conn;
    }

}
