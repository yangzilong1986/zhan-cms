package zt.cms.pub;

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

import zt.platform.db.*;
import zt.cmsi.mydb.*;
import zt.platform.utils.*;

public class SCBranch {
  private static Logger logger = Logger.getLogger("zt.cms.pub.SCBranch");

  //���������飺
  //brhid��ֻ��¼brhid������������
  //upbrh����¼brhid��upbrh�Ĺ�ϵ
  //brhinfo����¼����������Ϣ
  private static String[] brhid = null;
  private static String[][] upbrh = null;
  private static String[][] brhinfo = null;

  //
  private static HashMap allsubbrh1 = new HashMap(0, 1); //����������
  private static HashMap allsubbrh2 = new HashMap(0, 1); //��ʵ������
  private static HashMap allsubbrh3 = new HashMap(0, 1); //����������

  //
  private static int rowcount = 0;
  private static boolean isDirty = true;

  public synchronized static void setDirty()
  {
    isDirty = true;
  }

  public synchronized static void checkDirty()
  {
    if(isDirty == true)
    {
      SCBranch.loaddata();
      isDirty = false;
    }
  }

  private static void loaddata()
  {
    Debug.debug(Debug.TYPE_MESSAGE,"SCBranch loading ...");
    //�������ݿ���Դ
    //ConnectionManager manager = ConnectionManager.getInstance();

    //DatabaseConnection con = dc.getConnection();
    Connection cn = null;
    Statement st = null;
    //sql
    String sql = "select a.brhid,a.upbrh,a.lname,a.sname,a.bnkid,a.brhtype,a.brhlevel,a.brhstatus,(select brhlevel from SCBranchApp where a.BrhID=SCBranchApp.BrhID) as brhlevel2 from SCBranch a";
    try {
      DatabaseConnection dc = MyDB.getInstance().apGetConn();
      cn = dc.getConnection();
      st = cn.createStatement();
      ResultSet rs = st.executeQuery(sql);

      rowcount = 0;
      while (rs.next()) {
        rowcount++;
      }
      rs = st.executeQuery(sql);

      if (rowcount > 0) {
        brhid = new String[rowcount];
        upbrh = new String[rowcount][2];
        brhinfo = new String[rowcount][6];
        for (int i = 0; i < rowcount; i++) {
          rs.next();
          brhid[i] = convert(rs.getString("BRHID"));
          upbrh[i][0] = brhid[i];
          upbrh[i][1] = convert(rs.getString("UPBRH"));
          brhinfo[i][0] = convert(rs.getString("LNAME"));
          brhinfo[i][1] = convert(rs.getString("SNAME"));
          brhinfo[i][2] = convert(rs.getString("BNKID"));
          brhinfo[i][3] = convert(rs.getString("BRHTYPE"));
          //System.out.println(brhinfo[i][3]);
          //following line is changed by GZL in order to get BrhLevel from SCBranchApp
          if (rs.getString("BRHLEVEL2") == null) {
            brhinfo[i][4] = convert(rs.getString("BRHLEVEL"));
          }
          else {
            brhinfo[i][4] = convert(rs.getString("BRHLEVEL2"));
          }

          brhinfo[i][5] = convert(rs.getString("BRHSTATUS"));
        }
      }
      rs.close();
    }
    catch (Exception ex) {
      rowcount = 0;
      Debug.debug(ex);
      Debug.debug(Debug.TYPE_ERROR,"��ȡSCBranch�����ִ��󣬴�������Ϊ��" + ex.getMessage());
    }
    finally {
      try {
        if (st != null) {
          st.close();
        }
      }
      catch (Exception e) {}
      //manager.releaseConnection(con);
      MyDB.getInstance().apReleaseConn(0);
    }
    System.out.println("SCBranch loading is ok !");
  }

  /**
   * NULL���ݴ�������ת��
   */
  private static String convert(String p_str) {
    if (p_str == null) {
      return "";
    }
    try {
      //return new String(p_str.trim().getBytes("iso-8859-1"), "GBK");
      return DBUtil.fromDB(p_str.trim());
    }
    catch (Exception ex) {
      return "";
    }
  }

  /**
   * ����branchid
   */
  private static int getIdx(String p_brhid) {
    if (rowcount < 1) {
      return -1;
    }
    for (int i = 0; i < rowcount; i++) {
      if (brhid[i].equals(p_brhid)) {
        return i;
      }
    }
    return -1;
  }

  /**
   * �ж�ĳһ�������Ƿ����
   */
  public static boolean isExist(String p_brhid) {
    checkDirty();
    if (p_brhid == null) {
      return false;
    }
    String brhid = p_brhid.trim();
    int idx = getIdx(brhid);
    if (idx >= 0) {
      return true;
    }
    return false;
  }

  /**
   * ����branchid�������ϼ����㣨������
   */
  public static String getSupBrh(String p_brhid) {
    checkDirty();
    if (p_brhid == null) {
      return null;
    }
    String brhid = p_brhid.trim();
    int idx = getIdx(brhid);
    if (idx >= 0) {
      if (upbrh[idx][1].equals(brhid)) {
        return null;
      }
      return upbrh[idx][1];
    }
    return null;
  }

  /**
   * ����branchid��������һ���������Ŵ����š����硢�а죩�ĸ�����
   * �����������Ҫ�ҵ����㼶���򷵻��Լ���20040317��
   * p_brhlevel�������㼶��
   */
  public static String getSupBrh(String p_brhid, int p_brhlevel) {
    checkDirty();

    if (p_brhid == null) {
      return null;
    }
    String brhid = p_brhid.trim();
    int i = 1;
    int loop = 4; //��ֹѭ������
    while (i <= loop) {
      int idx = getIdx(brhid);
      //����������
      if (idx < 0) {
        return null;
      }
      //���㼶�����
      int level = Integer.parseInt(brhinfo[idx][4]);
      if (level == p_brhlevel) {
        return brhid;
      }
      //Ѱ���ϼ�����
      brhid = getSupBrh(brhid);
      i++;
    }
    return null;
  }

  /**
   * ����branchid������
   * ֱ���¼�
   * ����������
   */
  public static String getSubBrh0(String p_brhid) {
    checkDirty();

    if (p_brhid == null) {
      return null;
    }
    String brhid = p_brhid.trim();
    String sub = getSubBrh1(brhid);
    if (sub != null) {
      sub = brhid + "," + sub;
    }
    return sub;
  }

  /**
   * ����branchid������
   * ֱ���¼�
   * ����������
   */
  public static String getSubBrh1(String p_brhid) {
    checkDirty();

    if (p_brhid == null) {
      return null;
    }
    String brhid = p_brhid.trim();
    if (rowcount < 1) {
      return null;
    }
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < rowcount; i++) {
      if (upbrh[i][1].equals(brhid)) {
        sb.append(upbrh[i][0] + ",");
      }
    }
    String sub = sb.toString();
    if (sub.length() > 0) {
      sub = sub.substring(0, sub.length() - 1);
    }
    return sub;
  }

  /**
   * ����branchid������
   * ֱ���¼�
   * ��ʵ������
   */
  public static String getSubBrh2(String p_brhid) {
    checkDirty();

    if (p_brhid == null) {
      return null;
    }
    String brhid = p_brhid.trim();
    if (rowcount < 1) {
      return null;
    }
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < rowcount; i++) {
      if (upbrh[i][1].equals(brhid) && !brhinfo[i][3].equals("9")) {
        sb.append(upbrh[i][0] + ",");
      }
    }
    String sub = sb.toString();
    if (sub.length() > 0) {
      sub = sub.substring(0, sub.length() - 1);
    }
    return sub;
  }

  /**
   * ����branchid������
   * ֱ���¼�
   * ����������
   */
  public static String getSubBrh3(String p_brhid) {
    checkDirty();

    if (p_brhid == null) {
      return null;
    }
    String brhid = p_brhid.trim();
    if (rowcount < 1) {
      return null;
    }
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < rowcount; i++) {
      if (upbrh[i][1].equals(brhid) && brhinfo[i][3].equals("9")) {
        sb.append(upbrh[i][0] + ",");
      }
    }
    String sub = sb.toString();
    if (sub.length() > 0) {
      sub = sub.substring(0, sub.length() - 1);
    }
    return sub;
  }

  /**
   * ����branchid������
   * �����¼�
   * ����������
   */
  public static String getAllSubBrh1(String p_brhid) {
    checkDirty();

    if (p_brhid == null) {
      return null;
    }
    String brhid = p_brhid.trim();
    if (rowcount < 1) {
      return null;
    }
    //��map����ȡ
    if (allsubbrh1.size() > 0) {
      String tmp = (String) allsubbrh1.get(brhid);
      if (tmp != null) {
        return tmp;
      }
    }
    //û�У�������
    StringBuffer allsub = new StringBuffer();
    String levsub = getSubBrh1(brhid);
    while (levsub != null && levsub.length() > 0) {
      //ɸ��"��������"�е�ѭ�����㣬�����µ�"��������"
      String[] tmplevsubary = levsub.split(",");
      if (allsub.length() > 0) {
        levsub = "";
        for (int i = 0; i < tmplevsubary.length; i++) {
          if (allsub.indexOf(tmplevsubary[i]) < 0) {
            levsub += tmplevsubary[i] + ",";
          }
        }
        if (levsub.length() > 0) {
          levsub = levsub.substring(0, levsub.length() - 1);
        }
      }
      //"��������"���뵽"������"������
      allsub.append(levsub + ",");
      //����"��������"��ÿ�������ֱ���¼����㣬��ͬ���"�¼�����"
      String[] levsubary = levsub.split(",");
      levsub = "";
      for (int i = 0; i < levsubary.length; i++) {
        String tmp = getSubBrh1(levsubary[i]); //��������ݿ�
        if (tmp != null && tmp.length() > 0) {
          levsub += tmp + ",";
        }
      }
      if (levsub.length() > 0) {
        levsub = levsub.substring(0, levsub.length() - 1);
        //�µ�"�¼�����"�ٽ���ѭ��
      }
    }
    //ȥ�����Ķ���
    String temp = allsub.toString();
    if (temp.length() > 0) {
      temp = temp.substring(0, temp.length() - 1);
    }
    //�ŵ�map��ȥ
    allsubbrh1.put(brhid, temp);
    return allsub.toString();
  }

  /**
   * ����branchid������
   * �����¼�
   * ��ʵ������
   */
  public static String getAllSubBrh2(String p_brhid) {
    checkDirty();

    if (p_brhid == null) {
      return null;
    }
    String brhid = p_brhid.trim();
    if (rowcount < 1) {
      return null;
    }
    //��map����ȡ
    if (allsubbrh2.size() > 0) {
      String tmp = (String) allsubbrh2.get(brhid);
      if (tmp != null) {
        return tmp;
      }
    }
    //û�У�������
    String allsub = getAllSubBrh1(brhid);
    String[] tmp = allsub.split(",");
    allsub = "";
    for (int i = 0; i < tmp.length; i++) {
      String brhtp = getBrhtype(tmp[i]);
      if (brhtp != null && !brhtp.equals("9")) {
        allsub += tmp[i] + ",";
      }
    }
    if (allsub.length() > 0) {
      allsub = allsub.substring(0, allsub.length() - 1);
    }
    //�ŵ�map��ȥ
    allsubbrh2.put(brhid, allsub);
    return allsub;
  }

  /**
   * ����branchid������
   * �����¼�
   * ����������
   */
  public static String getAllSubBrh3(String p_brhid) {
    checkDirty();

    if (p_brhid == null) {
      return null;
    }
    String brhid = p_brhid.trim();
    if (rowcount < 1) {
      return null;
    }
    //��map����ȡ
    if (allsubbrh3.size() > 0) {
      String tmp = (String) allsubbrh3.get(brhid);
      if (tmp != null) {
        return tmp;
      }
    }
    //û�У�������
    String allsub = getAllSubBrh1(brhid);
    String[] tmp = allsub.split(",");
    allsub = "";
    for (int i = 0; i < tmp.length; i++) {
      String brhtp = getBrhtype(tmp[i]);
      if (brhtp != null && brhtp.equals("9")) {
        allsub += tmp[i] + ",";
      }
    }
    if (allsub.length() > 0) {
      allsub = allsub.substring(0, allsub.length() - 1);
    }
    //�ŵ�map��ȥ
    allsubbrh3.put(brhid, allsub);
    return allsub;
  }

  /**
   * ����branchid������
   * �����¼�
   * ���������㼰������������ʵ
   */
  public static String getAllSubBrhAndSelf1(String p_brhid) {
    checkDirty();

    if (p_brhid == null) {
      return null;
    }
    String brhid = p_brhid.trim();
    String allsub = getAllSubBrh1(brhid);
    if (allsub != null && allsub.trim().length()>0) {
      allsub = brhid + "," + allsub;
    }
    else{
      return p_brhid;
    }
    return allsub;
  }

  /**
   * ����branchid�����������¼���ʵ�����㣬����Լ���ʵ����Ҳ�������ڡ�
   * �������Ʊ���ΪgetAllSubBrhAndSelf2����Ϊ��ʹ�ã��ݲ����޸�
   */
  public static String getSubBranchAll(String p_brhid) {
    checkDirty();

    if (p_brhid == null) {
      return null;
    }
    String brhid = p_brhid.trim();
    String temp = getAllSubBrh2(brhid);
    if (getBrhtype(brhid).equals("9")) {
      return temp;
    }
    if (temp != null && temp.trim().length() > 0) {
      temp = brhid + "," + temp;
    }
    else {
      temp = brhid;
    }
    return temp;
  }

  /**
   * ����branchid�����������¼����������㣬����Լ���������Ҳ�������ڡ�
   */
  public static String getAllSubBrhAndSelf3(String p_brhid) {
    checkDirty();

    if (p_brhid == null) {
      return null;
    }
    String brhid = p_brhid.trim();
    String temp = getAllSubBrh3(brhid);
    if (!getBrhtype(brhid).equals("9")) {
      return temp;
    }
    if (temp != null && temp.trim().length()>0) {
      temp = brhid + "," + temp;
    }
    else {
      temp = brhid;
    }
    return temp;
  }

  /**
   * ���p_brhid�Ƿ���p_upbrh���¼�����
   */
  public static boolean checkSub(String p_brhid, String p_upbrh) {
    checkDirty();

    if (p_brhid == null || p_upbrh == null) {
      return false;
    }
    String brhid = p_brhid.trim();
    String upbrh = p_upbrh.trim();
    if (rowcount < 1) {
      return false;
    }
    String tmp1 = getAllSubBrh1(upbrh);
    if (tmp1 == null) {
      return false;
    }
    String[] tmp2 = tmp1.split(",");
    if (tmp2 == null) {
      return false;
    }
    for (int i = 0; i < tmp2.length; i++) {
      if (tmp2[i].equals(brhid)) {
        return true;
      }
    }
    return false;
  }

  /**
   * ����branchid�������������
   */
  public static String getLName(String p_brhid) {
    checkDirty();

    if (p_brhid == null) {
      return null;
    }
    String brhid = p_brhid.trim();
    int idx = getIdx(brhid);
    if (idx < 0) {
      return null;
    }
    return brhinfo[idx][0];
  }

  public static String getSName(String p_brhid) {
    checkDirty();

    if (p_brhid == null) {
      return null;
    }
    String brhid = p_brhid.trim();
    int idx = getIdx(brhid);
    if (idx < 0) {
      return null;
    }
    return brhinfo[idx][1];
  }

  public static String getBnkid(String p_brhid) {
    checkDirty();

    if (p_brhid == null) {
      return null;
    }
    if (p_brhid == null) {
      return null;
    }
    String brhid = p_brhid.trim();
    int idx = getIdx(brhid);
    if (idx < 0) {
      return null;
    }
    return brhinfo[idx][2];
  }

  public static String getBrhtype(String p_brhid) {
    checkDirty();

    if (p_brhid == null) {
      return null;
    }
    String brhid = p_brhid.trim();
    int idx = getIdx(brhid);
    if (idx < 0) {
      return null;
    }
    return brhinfo[idx][3];
  }

  public static String getBrhlevel(String p_brhid) {
    checkDirty();

    if (p_brhid == null) {
      return null;
    }
    String brhid = p_brhid.trim();
    int idx = getIdx(brhid);
    if (idx < 0) {
      return null;
    }
    return brhinfo[idx][4];
  }

  public static String getBrhstatus(String p_brhid) {
    checkDirty();

    if (p_brhid == null) {
      return null;
    }
    String brhid = p_brhid.trim();
    int idx = getIdx(brhid);
    if (idx < 0) {
      return null;
    }
    return brhinfo[idx][5];
  }

  public static void main(String[] args) {
    System.out.println("------------------------------------");
    //System.out.println(getSupBranch("907060550"));
    //System.out.println(getSubBranch("907069999"));
    //for(int i=0;i<5000;i++){
    String a = getSupBrh("907020100", 2);
    System.out.println(a);
    //}
    //System.out.println(getSubBranchAll("907069999"));
    //System.out.println(getAllSubBrh1("907999999"));
    //System.out.println(getAllSubBrh2("907999999"));
    //System.out.println(getAllSubBrh3("907999999"));
    //System.out.println("------------------------------------");
    //System.out.println(checkSub("907060550","907060599"));
//    System.out.println(checkSub("907060551","907060599"));
//    System.out.println(checkSub("907060599","907060599"));
//    System.out.println(checkSub("907010000","907019999"));
//    System.out.println(getLName("907060550"));
//    System.out.println(getSName("907060550"));
//    System.out.println(getBnkid("907060550"));
//    System.out.println(getBrhtype("907060550"));
//    System.out.println(getBrhlevel("907060550"));
//    System.out.println(getBrhstatus("907060550"));
    //   System.out.println(getSupBrh("907091500",3));
  }
}