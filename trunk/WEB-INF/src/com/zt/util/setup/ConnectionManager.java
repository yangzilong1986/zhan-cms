package com.zt.util.setup;


import java.sql.*;
import zt.cmsi.mydb.MyDB;
import zt.platform.db.DatabaseConnection;

/**
 *  Description of the Class
 *
 *@author     sun
 *@created    2003Äê10ÔÂ8ÈÕ
 */
public class ConnectionManager {
    /**
     *  Gets the connection attribute of the ConnectionManager class
     *
     *@return    The connection value
     */
    public static Connection getConnection() {
        DatabaseConnection conn = MyDB.getInstance().apGetConn();
        return conn.getConnection();
    }


    /**
     *  Description of the Method
     */
    public static void releaseConnection() {
        MyDB.getInstance().apReleaseConn(0);
    }
}
