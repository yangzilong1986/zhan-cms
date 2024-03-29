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

  //用三个数组：
  //brhid，只记录brhid，用来当索引
  //upbrh，记录brhid，upbrh的关系
  //brhinfo，记录其他所有信息
  private static String[] brhid = null;
  private static String[][] upbrh = null;
  private static String[][] brhinfo = null;

  //
  private static HashMap allsubbrh1 = new HashMap(0, 1); //所有子网点
  private static HashMap allsubbrh2 = new HashMap(0, 1); //纯实子网点
  private static HashMap allsubbrh3 = new HashMap(0, 1); //带虚子网点

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
    //申请数据库资源
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
      Debug.debug(Debug.TYPE_ERROR,"读取SCBranch表出现错误，错误描述为：" + ex.getMessage());
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
   * NULL数据处理，并转码
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
   * 索引branchid
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
   * 判断某一个网点是否存在
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
   * 根据branchid返回其上级网点（检索）
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
   * 根据branchid返回其上一级各级别（信贷部门、联社、市办）的父网点
   * 如果自身符合要找的网点级别，则返回自己（20040317）
   * p_brhlevel：父网点级别
   */
  public static String getSupBrh(String p_brhid, int p_brhlevel) {
    checkDirty();

    if (p_brhid == null) {
      return null;
    }
    String brhid = p_brhid.trim();
    int i = 1;
    int loop = 4; //防止循环网点
    while (i <= loop) {
      int idx = getIdx(brhid);
      //自身不存在
      if (idx < 0) {
        return null;
      }
      //网点级别对照
      int level = Integer.parseInt(brhinfo[idx][4]);
      if (level == p_brhlevel) {
        return brhid;
      }
      //寻找上级网点
      brhid = getSupBrh(brhid);
      i++;
    }
    return null;
  }

  /**
   * 根据branchid返回其
   * 直接下级
   * 所有子网点
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
   * 根据branchid返回其
   * 直接下级
   * 所有子网点
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
   * 根据branchid返回其
   * 直接下级
   * 纯实子网点
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
   * 根据branchid返回其
   * 直接下级
   * 纯虚子网点
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
   * 根据branchid返回其
   * 所有下级
   * 所有子网点
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
    //从map里面取
    if (allsubbrh1.size() > 0) {
      String tmp = (String) allsubbrh1.get(brhid);
      if (tmp != null) {
        return tmp;
      }
    }
    //没有，则生成
    StringBuffer allsub = new StringBuffer();
    String levsub = getSubBrh1(brhid);
    while (levsub != null && levsub.length() > 0) {
      //筛掉"本级网点"中的循环网点，生成新的"本级网点"
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
      //"本级网点"加入到"总网点"数据中
      allsub.append(levsub + ",");
      //搜索"本级网点"中每个网点的直接下级网点，共同组成"下级网点"
      String[] levsubary = levsub.split(",");
      levsub = "";
      for (int i = 0; i < levsubary.length; i++) {
        String tmp = getSubBrh1(levsubary[i]); //避免读数据库
        if (tmp != null && tmp.length() > 0) {
          levsub += tmp + ",";
        }
      }
      if (levsub.length() > 0) {
        levsub = levsub.substring(0, levsub.length() - 1);
        //新的"下级网点"再进入循环
      }
    }
    //去掉最后的逗号
    String temp = allsub.toString();
    if (temp.length() > 0) {
      temp = temp.substring(0, temp.length() - 1);
    }
    //放到map里去
    allsubbrh1.put(brhid, temp);
    return allsub.toString();
  }

  /**
   * 根据branchid返回其
   * 所有下级
   * 纯实子网点
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
    //从map里面取
    if (allsubbrh2.size() > 0) {
      String tmp = (String) allsubbrh2.get(brhid);
      if (tmp != null) {
        return tmp;
      }
    }
    //没有，则生成
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
    //放到map里去
    allsubbrh2.put(brhid, allsub);
    return allsub;
  }

  /**
   * 根据branchid返回其
   * 所有下级
   * 纯虚子网点
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
    //从map里面取
    if (allsubbrh3.size() > 0) {
      String tmp = (String) allsubbrh3.get(brhid);
      if (tmp != null) {
        return tmp;
      }
    }
    //没有，则生成
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
    //放到map里去
    allsubbrh3.put(brhid, allsub);
    return allsub;
  }

  /**
   * 根据branchid返回其
   * 所有下级
   * 所有子网点及其自身不管虚实
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
   * 根据branchid返回其所有下级纯实子网点，如果自己是实网点也包含在内。
   * 函数名称本该为getAllSubBrhAndSelf2，因为已使用，暂不做修改
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
   * 根据branchid返回其所有下级纯虚子网点，如果自己是虚网点也包含在内。
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
   * 检查p_brhid是否是p_upbrh的下级网点
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
   * 根据branchid返回其各种属性
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
