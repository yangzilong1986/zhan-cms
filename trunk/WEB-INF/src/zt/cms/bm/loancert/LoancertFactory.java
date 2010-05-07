package zt.cms.bm.loancert;

import java.sql.*;
import java.util.logging.*;

import zt.cmsi.biz.*;
import zt.cmsi.mydb.*;
import zt.cmsi.pub.cenum.EnumValue;
import zt.platform.db.*;
import zt.platform.utils.*;

public class LoancertFactory {
  static Logger logger = Logger.getLogger("zt.cms.bm.loancert.LoancertFactory");

  public static Loancert findLoancertById(String id) {

    String str =
      "select bmcreditlimit.*,cmclient.appbrhid from bmcreditlimit,cmclient "
      + " where bmcreditlimit.clientno=cmclient.clientno "
      + " and cmclient.id='" + id + "' and bmcreditlimit.typeno=" +
      EnumValue.BMType_DaiKuanZhengDaiKuan;

    Connection con = null;
    Statement st = null;

    DatabaseConnection dc = MyDB.getInstance().apGetConn();

    Debug.debug(Debug.TYPE_SQL, str);

    try {
      con = dc.getConnection();
      st = con.createStatement();
      ResultSet rs = null;

      rs = st.executeQuery(str);
      if (rs.next()) {
        Loancert loancert = new Loancert();
        loancert.setClientNo(rs.getString("CLIENTNO"));
        loancert.setLimitApproved(rs.getDouble("LIMITAPPROVED"));
        loancert.setLoanBal(rs.getBigDecimal("LOANBAL"));
        loancert.setDisabled(rs.getInt("DISABLED"));

        loancert.setStartDate(util.dateToCalendar(rs.getDate("STARTDATE")));
        loancert.setEndDate(util.dateToCalendar(rs.getDate("ENDDATE")));
        loancert.setLimitCommit(rs.getBigDecimal("LIMITCOMMIT"));
        loancert.setCreditLimit(rs.getBigDecimal("CREDITLIMIT"));
        loancert.setLastModified(util.dateToCalendar(rs.getDate("LASTMODIFIED")));
        loancert.setHasBadLoan(rs.getInt("HASBADLOAN"));

        loancert.setBrhid(rs.getString("APPBRHID").trim());

        loancert.setIfRespLoan(rs.getInt("IFRESPLOAN"));
        loancert.setFirstResp(rs.getString("FIRSTRESP").trim());
        loancert.setDecidedby(rs.getString("DECIDEDBY").trim());

        //logger.info(loancert.toString());
        MyDB.getInstance().apReleaseConn(1);
        return loancert;

      }
      else {
        return null;
      }
    }
    catch (Exception e) {
      return null;
    }
    finally {
      MyDB.getInstance().apReleaseConn(1);
    }
  }

  public static Loancert findLoancertByClientNo(String id) {

    String str =
      "select bmcreditlimit.*,cmclient.appbrhid from bmcreditlimit,cmclient "
      + " where bmcreditlimit.clientno=cmclient.clientno "
      + " and cmclient.clientno='" + id + "' and bmcreditlimit.typeno=" +
      EnumValue.BMType_DaiKuanZhengDaiKuan;

    Connection con = null;
    Statement st = null;

    DatabaseConnection dc = MyDB.getInstance().apGetConn();

    Debug.debug(Debug.TYPE_SQL, str);

    try {
      con = dc.getConnection();
      st = con.createStatement();
      ResultSet rs = null;

      rs = st.executeQuery(str);
      if (rs.next()) {
        Loancert loancert = new Loancert();
        loancert.setClientNo(rs.getString("CLIENTNO"));
        loancert.setLimitApproved(rs.getDouble("LIMITAPPROVED"));
        loancert.setLoanBal(rs.getBigDecimal("LOANBAL"));
        loancert.setDisabled(rs.getInt("DISABLED"));

        loancert.setStartDate(util.dateToCalendar(rs.getDate("STARTDATE")));
        loancert.setEndDate(util.dateToCalendar(rs.getDate("ENDDATE")));
        loancert.setLimitCommit(rs.getBigDecimal("LIMITCOMMIT"));
        loancert.setCreditLimit(rs.getBigDecimal("CREDITLIMIT"));
        loancert.setLastModified(util.dateToCalendar(rs.getDate("LASTMODIFIED")));
        loancert.setHasBadLoan(rs.getInt("HASBADLOAN"));

        loancert.setBrhid(rs.getString("APPBRHID").trim());

        loancert.setIfRespLoan(rs.getInt("IFRESPLOAN"));
        loancert.setFirstResp(rs.getString("FIRSTRESP").trim());
        loancert.setDecidedby(rs.getString("DECIDEDBY").trim());
        loancert.setOperator(rs.getString("OPERATOR").trim());

        //logger.info(loancert.toString());
        MyDB.getInstance().apReleaseConn(1);
        return loancert;

      }
      else {
        return null;
      }
    }
    catch (Exception e) {
      return null;
    }
    finally {
      MyDB.getInstance().apReleaseConn(1);
    }
  }

}
