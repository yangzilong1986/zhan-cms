package zt.cms.migration;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
import zt.cmsi.mydb.MyDB;
import zt.platform.db.DatabaseConnection;

import java.sql.*;
import java.sql.ResultSet;

import java.sql.Types;


public class test {
  public test() {
  }

  public static void ttt()
  {
    DatabaseConnection dc = MyDB.getInstance().apGetConn();
    Connection conn = dc.getConnection();

    CallableStatement cstmt = null;
    ResultSet rs = null;
    String strSql = "call PROcedure2(?,?)";
    try
    {
      //  strSql="{call Q10010(?,?)}";

      cstmt = conn.prepareCall(strSql);
      for(int i=1;i<=5;i++)
      {
        cstmt.setString(1, "A"+i);

        cstmt.registerOutParameter(2,Types.VARCHAR);

        cstmt.execute();
        String ret = cstmt.getString(2);
        if(cstmt.wasNull())
          System.out.println("NULL returned");
        else
          System.out.println(ret);
      }

      cstmt.close();

    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
    finally{
      MyDB.getInstance().apReleaseConn(0);
    }


  }

  public static void main(String[] args) {
    test test1 = new test();
    test.ttt();
  }

}