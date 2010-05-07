package zt.cms.report;

/**
 * <p>Title: 信贷管理系统</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: 中天信息技术有限公司</p>
 * @author Yusg
 * @version 1.0
 */

import java.sql.*;
import zt.platform.db.DatabaseConnection;
import zt.cmsi.mydb.MyDB;

public class ScbranchLevel {
  DatabaseConnection dbc = null;  //数据库连接
	Connection conn = null;         //数据库连接
  public String strSname;         //网点简称
  public String strChgId;         //信贷部门号
  public String strBnkId;         //县联社号
  public int intBrhType;          //网点类型
  public String strUpBrh;         //上级网点
  public int intBrhLevel=-1;      //网点级别

  public ScbranchLevel(String brhid)
  {
    getRecord(brhid);
  }
  public void getRecord(String brhid)
  {
    ResultSet rs=null;
    String strSql=null;

    strSql="select sname,chgid,bnkid,brhtype,upbrh,brhlevel from scbranch where brhid='";
    strSql=strSql+brhid+"'";

    try
    {
      dbc = MyDB.getInstance().apGetConn();
      conn = dbc.getConnection();
      Statement st=conn.createStatement();
      rs=st.executeQuery(strSql);
      if(rs.next())
      {
        this.strSname=rs.getString(1);
        this.strChgId=rs.getString(2);
        this.strBnkId=rs.getString(3);
        this.intBrhType=rs.getInt(4);
        this.strUpBrh=rs.getString(5);
        this.intBrhLevel=rs.getInt(6);
      }
    }
    catch(SQLException se)
    {
      System.out.println("从数据库取表数据失败！");
    }
    finally
    {
      MyDB.getInstance().apReleaseConn(1);
    }
  }
}
