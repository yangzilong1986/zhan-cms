package zt.cms.tool;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
import java.sql.*;
import java.util.*;
import java.util.logging.*;

public class DBAssistor {
  //���ñ���
  private String DriverName="COM.ibm.db2.jdbc.app.DB2Driver";
  private String url = "jdbc:db2:newdb2";
  private String user = "cms";
  private String passwd = "gbssxalxal";
  //���Զ���
  private static Logger log = Logger.getLogger("zt.cms.tool.PtTblInfo");
  //��Ա����
  private Vector connPool=new Vector();
  private int poolSize=10;
  /**
   * ���캯������ȡ��������ݿ����ӡ�
   */
  public DBAssistor() throws Exception{
    String SQL="SELECT COUNT(*) FROM SYSIBM.SYSTABLES";
    try{
      //�������
      Connection conn = getConn();
      if(conn==null) throw new Exception("�޷��������ݿ�����");
      Statement stmt=conn.createStatement();
      ResultSet rs=stmt.executeQuery(SQL);
      //�������
      if(rs.next()){
        if(rs.getInt(1)<1){
          throw new Exception("���ݿ����Ӽ��ʧ��");
        }
        else{
          freeConn(conn);
        }
      }
      else{
        throw new Exception("���ݿ�������Ч");
      }
    }
    catch(Exception ex){
      throw new Exception(ex.getMessage());
    }
  }
  /**
   * ȡ�����ݿ����ӣ����Ի���أ�
   */
  public Connection getConn(){
    if(connPool.size()>0){
      Connection conn=(Connection)connPool.remove(0);
      try{
        if (conn.isClosed()) {
          conn = getConn();
        }
      }
      catch(Exception ex){
        log.info(ex.getMessage());
        conn = getConn();
      }
      return conn;
    }
    else{
      return newConn();
    }
  }
  /**
   * �������ݿ�����
   */
  public Connection newConn(){
    try{
      //�������
      Driver driver = (Driver)Class.forName(DriverName).newInstance();
      DriverManager.registerDriver(driver);
      Connection conn = DriverManager.getConnection(url, user, passwd);
      return conn;
    }
    catch(Exception ex){
      log.info(ex.getMessage());
      return null;
    }
  }
  /**
   * �������ݿ����ӣ��Żػ���أ�
   */
  public void freeConn(Connection p_conn){
    try{
      if (connPool.size() >= poolSize) {
        p_conn.close();
      }
      else {
        connPool.addElement(p_conn);
      }
    }
    catch(Exception ex){
      log.info(ex.getMessage());
    }
  }

  public static String Encoding(String p_value,String p_srccode,String p_objcode){
    if(p_value==null) return null;
    byte[] tmp;
    try{
      //����p_srccode����
      if (p_srccode == null) {
        tmp = p_value.getBytes();
      }
      else {
        tmp = p_value.getBytes(p_srccode);
      }
      //����p_objcodeת��
      if (p_objcode == null) return new String(tmp);
      return new String(tmp,p_objcode);
    }
    catch(Exception ex){
      return p_value;
    }
  }

  //���ݿ����ת��
  public static String cvtcoding(String p_val){
    if(p_val==null) return null;
    try{
      return new String(p_val.getBytes("ISO-8859-1"), "GBK");
    }
    catch(Exception ex){
      System.exit(0);
      return null;
    }
  }

}
