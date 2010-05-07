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
  //配置变量
  private String DriverName="COM.ibm.db2.jdbc.app.DB2Driver";
  private String url = "jdbc:db2:newdb2";
  private String user = "cms";
  private String passwd = "gbssxalxal";
  //调试对象
  private static Logger log = Logger.getLogger("zt.cms.tool.PtTblInfo");
  //成员变量
  private Vector connPool=new Vector();
  private int poolSize=10;
  /**
   * 构造函数，获取并检查数据库连接。
   */
  public DBAssistor() throws Exception{
    String SQL="SELECT COUNT(*) FROM SYSIBM.SYSTABLES";
    try{
      //获得连接
      Connection conn = getConn();
      if(conn==null) throw new Exception("无法创建数据库连接");
      Statement stmt=conn.createStatement();
      ResultSet rs=stmt.executeQuery(SQL);
      //检测连接
      if(rs.next()){
        if(rs.getInt(1)<1){
          throw new Exception("数据库连接检测失败");
        }
        else{
          freeConn(conn);
        }
      }
      else{
        throw new Exception("数据库连接无效");
      }
    }
    catch(Exception ex){
      throw new Exception(ex.getMessage());
    }
  }
  /**
   * 取得数据库连接（来自缓冲池）
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
   * 创建数据库连接
   */
  public Connection newConn(){
    try{
      //获得连接
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
   * 缓存数据库连接（放回缓冲池）
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
      //按照p_srccode读码
      if (p_srccode == null) {
        tmp = p_value.getBytes();
      }
      else {
        tmp = p_value.getBytes(p_srccode);
      }
      //按照p_objcode转码
      if (p_objcode == null) return new String(tmp);
      return new String(tmp,p_objcode);
    }
    catch(Exception ex){
      return p_value;
    }
  }

  //数据库编码转换
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
