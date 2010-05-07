package zt.cms.bm.workbench.db;
/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

import java.util.*;
import java.util.logging.*;

import zt.cms.bm.workbench.*;
import zt.cmsi.mydb.*;
import zt.cmsi.pub.define.*;
import zt.platform.db.*;

public class AffairFactory {
  public static Logger logger = Logger.getLogger( "zt.cms.bm.workbench.db.AffairFactory");

  public static Collection findAffairsByBrhId(String brhId) {
    return findAffairsByBrhIdAndConditions(brhId, "");
  }

  public static Collection findAffairsByBrhIdAndConditions(String brhId, String otherCondition) {
    return findAffairsByBrhIdAndConditionsWithPage(brhId, otherCondition, 0, -1);
  }

  public static Collection findAffairsByBrhIdAndConditionsWithPage(String brhId, String otherCondition, int page, int userNo) {
    DatabaseConnection con = MyDB.getInstance().apGetConn();
    if(con==null) return null;
    Collection affairs = new Vector();
    String sql = "select bmtable.typeno,bmtrans.bmacttype,bmtrans.createdate,bmtable.brhid,bmtable.clientname,"
      + "bmtable.bmstatus,bmtrans.operator,bmtrans.viewed,bmtable.bmno bmno,bmtrans.bmtransno,bmtableapp.finalamt"
      + " from bmtable,bmtrans,bmtableapp where bmtrans.transstatus=" + Affair.executing()
      + " and bmtrans.bmno=bmtable.bmno "
      + " and bmtable.bmno=bmtableapp.bmno "
      + " and bmtable.bmstatus < " + Affair.granted()
      + " and bmtrans.operbrhid='" + brhId + "'"
      + otherCondition +
      " order by bmtable.createdate desc,bmtable.typeno,bmtrans.bmacttype";
    logger.info(sql);
    try{
      RecordSet rs = con.executeQuery(sql);
      int index = 0;
      int allIndex = 0;
      int beginIndex = (page - 1) * 10;
      if (page <= 0) {
        beginIndex = 0;
      }
      while (rs.next()) {
        Affair affair = new Affair();
        affair.setTypeNo(rs.getInt("typeno"));
        affair.setBmActType(rs.getInt("bmacttype"));
        affair.setCreateDate(rs.getCalendar("createdate"));
        affair.setBrhId(rs.getString("brhid"));
        affair.setClientName(DBUtil.fromDB(rs.getString("clientname")));
        affair.setActStatus(rs.getInt("bmstatus"));
        affair.setOperator(rs.getString("operator"));
        affair.setBmStatusNo(rs.getInt("bmstatus"));
        affair.setAppAmt(rs.getDouble("finalamt"));
        int viewed = 0;
        viewed = rs.getInt("viewed");
        if (viewed == 0) {
          affair.setViewed(false);
        }
        else {
          affair.setViewed(true);
        }
        affair.setBmNo(rs.getString("bmno"));
        affair.setBmTransNo(rs.getInt("bmtransno"));
        if (BMRoute.getInstance().hasRightToAct(userNo, affair.getTypeNo(), affair.getBmActType()) >= 0) {
          allIndex++;
          if (allIndex > beginIndex) {
            index++;
            affairs.add(affair);
          }
        }
      }
    }
    catch(Exception ex){
      System.out.println("工作台异常："+DBUtil.fromDB(ex.getMessage()));
      ex.printStackTrace();
    }
    finally{
      if(con != null) MyDB.getInstance().apReleaseConn(0);
    }
    return affairs;
  }

  public static int getAllAffairNumber(int userNo,String brhid){
    String rightStr = "     ";
    for (int i = 1; i < 31; i++) {
      for (int j = 1; j <7 ; j++) {
        if(i==17){
            break;
          }
        if(BMRoute.getInstance().hasRightToAct(userNo,i,j)>=0){
          rightStr += " (bmtable.typeno="+i+" and bmtrans.bmacttype="+j+") or ";
        }
      }
    }
    rightStr = rightStr.substring(0,rightStr.length()-3);



    String sql = "select count(bmtable.bmno)"
      + " from bmtable,bmtrans where bmtrans.transstatus=" + Affair.executing()
      + " and bmtable.bmstatus < " + Affair.granted()
      + " and ("+rightStr+")"
      + " and bmtrans.operbrhid='"+brhid+"'"
      + " and bmtable.bmno = bmtrans.bmno";
    logger.info("sql:"+sql);
    DatabaseConnection con = MyDB.getInstance().apGetConn();
    RecordSet rs = con.executeQuery(sql);
    if(rs.next()){
      MyDB.getInstance().apReleaseConn(0);
      return rs.getInt(0);
    }else{
      MyDB.getInstance().apReleaseConn(0);
      return -1;
    }


  }
}
