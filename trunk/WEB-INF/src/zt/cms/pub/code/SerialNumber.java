//Source file: e:\\java\\zt\\cms\\pub\\code\\SerialNumber.java

package zt.cms.pub.code;

/**
 * SerialNumber class get next sequence number of a given sequence in Oracle
 * Database.
 * The parameter is the sequence name.
 */
import java.util.logging.*;
import java.util.*;
import zt.platform.db.*;
import zt.cmsi.mydb.MyDB;
import com.zt.util.PropertyManager;
import java.net.*;

public class SerialNumber {
  private static Logger logger = Logger.getLogger("zt.cms.pub.code.SerialNumber");
  static HashMap SNMap=new HashMap(0,1);
  /**
   * �����ݿ��ж�ȡp_tblName����p_fldName�ֶε����ֵ
   */
  private static long getDBMaxNo(String p_tblName, String p_fldName) {
    //�������ݿ���Դ
    //ConnectionManager manager = ConnectionManager.getInstance();
    //DatabaseConnection con = manager.getConnection();

    //above two lines getting DB connection are commented by GZL,
    //cause it maybe cause DB lock if we get a new db connection by using platform's way
    DatabaseConnection con = MyDB.getInstance().apGetConn();

    //sql
    //String sql="select max(cast("+p_fldName+" as integer)) ";
    // above line is modified by JGO, due to overflow of big integer
    String sql="select max(cast("+p_fldName+" as int)) ";
    sql+="from "+p_tblName;
    logger.info(sql);
    try{
      RecordSet rs=con.executeQuery(sql);
      if (rs.next()) {
        String temp=rs.getString(0);
        if(temp==null) return 0;
        return Long.parseLong(temp.trim());
      }
      else {
        System.out.println("��ȡ"+p_tblName+"��"+p_fldName+"�ֶε����ֵ����δ֪����");
        return -1;
      }
    }
    catch(Exception ex){
      System.out.println("��ȡ"+p_tblName+"��"+p_fldName+"�ֶε����ֵ�����쳣��"+ex.getMessage());
      return -1;
    }
    finally {
      //manager.releaseConnection(con);
      MyDB.getInstance().apReleaseConn(0);
    }
  }

  /**
   * ��ȡ��һ�����к�
   */
  public static long getNextSN(String p_tblName, String p_fldName) {
    /*
    String url = PropertyManager.getProperty("main_server")+"clusterserialnumber";
    com.caucho.hessian.client.HessianProxyFactory factory = new com.caucho.hessian.client.HessianProxyFactory();
    try {
      zt.cms.pub.code.ClusterSerialNumber sn = (zt.cms.pub.code.ClusterSerialNumber) factory.create(zt.cms.pub.code.ClusterSerialNumber.class, url);
      return sn.getNextSN(p_tblName,p_fldName);
    }
    catch (MalformedURLException ex) {
      ex.printStackTrace();
      return -1;
    }
    */

   /**
    * because we donot use Cluster server, so I commented above lines to speed up the system
    * on 2004-09-05
    * JGO
    */
   return SerialNumber.getNextSNLocal(p_tblName, p_fldName);

  }


  /**
 * ��ȡ��һ�����к�
 */
public static synchronized long getNextSNLocal(String p_tblName, String p_fldName) {
  long sn=getCurSN(p_tblName,p_fldName);
  if(sn>=0){
    sn++;
    String keyName=getKeyName(p_tblName,p_fldName);
    SNMap.put(keyName,new Long(sn).toString());
    return sn;
  }
  else{
    return sn;
  }
}


  /**
   * ��ȡ��ǰ���к�
   */
  public static synchronized long getCurSN(String p_tblName, String p_fldName) {
    long sn=-1;
    String keyName=getKeyName(p_tblName,p_fldName);
    if(keyName==null) return -1;
    if(SNMap.size()<1 || !SNMap.containsKey(keyName)){
      //��������һ��keyNameʱ�������ݿ��ж�ȡ���ֵ�����ŵ�SNMap
      sn=getDBMaxNo(p_tblName,p_fldName);
      if(sn>=0){
        SNMap.put(keyName,new Long(sn).toString());
      }
    }
    else{
      //���򣬴�SNMapȡ��ǰֵ
      try{
        sn = Long.parseLong(SNMap.get(keyName).toString());
      }
      catch(Exception ex){
        return -1;
      }
    }
    return sn;
  }
  /**
   * keyName
   */
  private static String getKeyName(String p_tblName, String p_fldName) {
    if(p_tblName==null || p_fldName==null) return null;
    String keyName=p_tblName.trim().toLowerCase()+"|"+p_fldName.trim().toLowerCase();
    return keyName;
  }
}
