package zt.cms.report;

/**
 * <p>Title: �Ŵ�����ϵͳ</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: ������Ϣ�������޹�˾</p>
 * @author Yusg
 * @version 1.0
 */

import java.sql.*;
import zt.platform.db.DatabaseConnection;
import zt.cmsi.mydb.MyDB;

public class ScbranchLevel {
  DatabaseConnection dbc = null;  //���ݿ�����
	Connection conn = null;         //���ݿ�����
  public String strSname;         //������
  public String strChgId;         //�Ŵ����ź�
  public String strBnkId;         //�������
  public int intBrhType;          //��������
  public String strUpBrh;         //�ϼ�����
  public int intBrhLevel=-1;      //���㼶��

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
      System.out.println("�����ݿ�ȡ������ʧ�ܣ�");
    }
    finally
    {
      MyDB.getInstance().apReleaseConn(1);
    }
  }
}
