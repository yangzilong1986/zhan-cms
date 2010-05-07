package zt.cms.pub;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
import java.util.logging.*;
import zt.cmsi.pub.*;
import zt.platform.db.*;
import zt.cms.pub.code.*;

public class SCUser {
  private static Logger logger = Logger.getLogger("zt.cms.pub.SCUser");
  //���������飺
  //usrid��ֻ��¼usrid������������
  //usrbrh����¼usrid��usrbrh�Ĺ�ϵ
  //usrinfo����¼����������Ϣ
  private static String[] usrid=null;
  private static String[][] usrbrh=null;
  private static String[][] usrinfo=null;
  private static int rowcount=-1;
  private static boolean isDirty=false;
  static{
    System.out.println("SCUser initing ...");
    init();
    System.out.println("SCUser inited ok !");
  }
  private static synchronized void init(){
    //�������ݿ���Դ
    ConnectionManager manager = ConnectionManager.getInstance();
    DatabaseConnection con = manager.getConnection();
    //sql
    String sql="select * from SCUser";
    try{
      RecordSet rs=con.executeQuery(sql);
      rowcount=rs.getRecordCount();
      if(rowcount>0){
        usrid = new String[rowcount];
        usrbrh = new String[rowcount][2];
        usrinfo = new String[rowcount][5];
        for(int i=0;i<rowcount;i++){
          rs.next();
          usrid[i]=convert(rs.getString("LOGINNAME"));
          usrbrh[i][0]=usrid[i];
          usrbrh[i][1]=convert(rs.getString("BRHID"));
          usrinfo[i][0]=convert(rs.getString("USERNO"));
          usrinfo[i][1]=convert(rs.getString("USERTYPE"));
          usrinfo[i][2]=convert(rs.getString("USERNAME"));
          usrinfo[i][3]=convert(rs.getString("USERJOB"));
          usrinfo[i][4]=convert(rs.getString("USERSTATUS"));
        }
      }
      rs.close();
    }
    catch(Exception ex){
      rowcount=0;
      logger.severe("��ȡSCUser����ִ��󣬴�������Ϊ��"+ex.getMessage());
    }
    finally {
      manager.releaseConnection(con);
    }
    isDirty=false;
  }
  /**
   * NULL���ݴ�����ת��
   */
  private static String convert(String p_str){
    if(p_str==null) return "";
    try{
      //return new String(p_str.trim().getBytes("iso-8859-1"), "GBK");
      return DBUtil.fromDB(p_str.trim());
    }
    catch(Exception ex){
      return "";
    }
  }

  /**
   * ����branchid
   */
  private static int getIdx(String p_lgnname){
    if(rowcount<1) return -1;
    for(int i=0;i<rowcount;i++){
      if(usrid[i].equals(p_lgnname)) return i;
    }
    return -1;
  }

  /**
   * ����lgnname��������������
   */
  public static String getBrhId(String p_lgnname){
    if(isDirty) init();
    int idx=getIdx(p_lgnname);
    if(idx>=0) return usrbrh[idx][1];
    return null;
  }

  /**
   * ����branchid�������������
   */
  public static int getUsrNo(String p_lgnname){
    if(isDirty) init();
    int idx=getIdx(p_lgnname);
    if(idx<0) return ErrorCode.USER_NOT_FOUND;
    return Integer.parseInt(usrinfo[idx][0]);
  }

  /**
   * �ж��û��Ƿ����
   */
  public static boolean isExist(String p_lgnname){
    if(isDirty) init();
    int idx=getIdx(p_lgnname);
    if(idx<0) return false;
    return true;
  }

  public static String getType(String p_lgnname){
    if(isDirty) init();
    int idx=getIdx(p_lgnname);
    if(idx<0) return null;
    return usrinfo[idx][1];
  }
  public static String getName(String p_lgnname){
    if(isDirty)
      {
        init();
      }
    int idx=getIdx(p_lgnname);
    if(idx<0)
      {
        return null;
      }
    return usrinfo[idx][2];
  }
  public static String getJob(String p_lgnname){
    if(isDirty) init();
    int idx=getIdx(p_lgnname);
    if(idx<0) return null;
    return usrinfo[idx][3];
  }
  public static String getStatus(String p_lgnname){
    if(isDirty) init();
    int idx=getIdx(p_lgnname);
    if(idx<0) return null;
    return usrinfo[idx][4];
  }

  public static void main(String[] args) {
    System.out.println(getUsrNo("1"));
    System.out.println(getBrhId("1"));
  }

  public static void setDirty(boolean isDirty){
     SCUserRoleBranchImpl.setAllUserDirty();
     SCUser.setDirtyLocal(true);
  }
  public static void setDirtyLocal(boolean isDirty){
    SCUser.isDirty = isDirty;
  }

}
