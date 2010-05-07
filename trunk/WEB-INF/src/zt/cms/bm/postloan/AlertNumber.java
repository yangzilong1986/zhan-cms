package zt.cms.bm.postloan;
import com.zt.util.setup.SetupManager;
import zt.cmsi.pub.define.*;
import java.util.logging.*;
import zt.platform.db.*;
import zt.cmsi.mydb.MyDB;
public class AlertNumber {
  static Logger logger = Logger.getLogger("zt.cms.bm.postloan.AlertNumber");

  public static int getProsecutionAlertNumber(String branch){
    String APPDATE = SystemDate.getSystemDate2();
    int alertMonth = SetupManager.getIntProperty("INACTLOAN:PROALERTMONTH");
    if (alertMonth <= 0) {
      alertMonth = 1;
    }

    String sql = "with "
      + " a1 as(select a.bmno,coalesce((select max(\"DATE\") from bmilnotifi where bmno=a.BMNO),a.NOWENDDATE) as nowenddate "
      + " from RQLOANledger a "
      + " union "
      + " select bmno,max(\"DATE\") as nowenddate "
      + " from bmilnotifi "
      + " where  pdnotiftype=5 "
      + " group by bmno,notifiedby  "
      + " order by bmno) "
      + " select count(*) "
      + " from a1 a ,rqloanledger b "
      + " where a.bmno=b.bmno "
      + " and b.isclosed= 0 "
      + " and b.BRHID in (" + branch + ")  "
      + " and  a.NOWENDDATE<=(cast('" + APPDATE + "'as \"DATE\")-" + (24 - alertMonth) + " MONTH)  "
      + " and  a.NOWENDDATE>=(cast('" + APPDATE + "' as \"DATE\")-24 MONTH) "
      + " and (select count(*) from bmilprosecution x where x.IFCLOSED=0 and x.bmno=a.bmno)=0";
    logger.info("sql:" + sql);
    DatabaseConnection con = MyDB.getInstance().apGetConn();
    RecordSet rs = con.executeQuery(sql);
    if (rs.next()) {
      MyDB.getInstance().apReleaseConn(0);
      return rs.getInt(0);
    }
    else {
      MyDB.getInstance().apReleaseConn(0);
      return -1;
    }

  }

  public static int getExeAlertNumber(String branch){
    String APPDATE = SystemDate.getSystemDate2();
    int alertMonth = SetupManager.getIntProperty("INACTLOAN:EXEALERTMONTH");
    if (alertMonth <= 0) {
        alertMonth = 1;
    }
    String theName = "(case when (case when coalesce(effectivedate,'1900-01-01') > coalesce(aplsentencedate,'1900-01-01') then coalesce(effectivedate,'1900-01-01') else coalesce(aplsentencedate,'1900-01-01') end ) > coalesce(susentencedate,'1900-01-01') then (case when coalesce(effectivedate,'1900-01-01') > coalesce(aplsentencedate,'1900-01-01') then coalesce(effectivedate,'1900-01-01') else coalesce(aplsentencedate,'1900-01-01') end ) else coalesce(susentencedate,'1900-01-01') end ) ";

    String sql = "select count(*) "
               + " from rqloanledger a,bmtable b,bmilprosecution c "
               + " where a.bmno = b.bmno and b.bmno = c.bmno "
               + " and "+theName+"<=(cast('2004-07-21' as \"DATE\")-"+(6-alertMonth)+" MONTH) "
               + " and "+theName+">=(cast('2004-07-21' as \"DATE\")-"+6+" MONTH) "
               + " and b.brhid in ("+branch+")";
    logger.info("sql:" + sql);
    DatabaseConnection con = MyDB.getInstance().apGetConn();
    RecordSet rs = con.executeQuery(sql);
    if (rs.next()) {
      MyDB.getInstance().apReleaseConn(0);
      return rs.getInt(0);
    }
    else {
      MyDB.getInstance().apReleaseConn(0);
      return -1;
    }

  }
}
