package zt.cms.bm.inactloan;
/**
 *已置换或核销的不良贷款还款详细
 *
 *@author     wxj
 *@created    2004年5月21日
 */
import zt.platform.db.*;
import zt.platform.form.control.*;
import zt.platform.form.util.*;
import zt.platform.form.util.event.*;
import zt.platform.user.*;
import zt.platform.form.config.*;
import zt.cms.pub.*;
import java.math.*;

public class BMILEXRecallAppPage extends FormActions {
  String flag = null;
  String ilno = null;
  String seqno=null;
  BigDecimal oldAmt = null;
  public int load(SessionContext ctx, DatabaseConnection conn,
                  FormInstance instance, ErrorMessages msgs,
                  EventManager manager, String parameter) {
    //flag
    flag = (String) ctx.getRequestAttribute("flag");
    if (flag == null) {
      flag = "read";
    }
    if (flag.equals("write")) {
      instance.setReadonly(false);
    }
    else {
      instance.setReadonly(true);
    }
    //ilno
    ilno = ctx.getRequestAttribute("ILNO").toString();
    seqno = ctx.getParameter("SEQNO");
    if (seqno != null && seqno.trim().length() > 0) {
      //trigger
      instance.setValue("SEQNO", seqno);
      trigger(manager, instance, EventType.EDIT_VIEW_EVENT_TYPE,
              Event.BRANCH_CONTINUE_TYPE);
    }
    return 0;
  }

  public int beforeInsert(SessionContext ctx, DatabaseConnection conn,
                          FormInstance instance, ErrorMessages msgs,
                          EventManager manager) {
    instance.setValue("ILNO", ilno);
    return 0;
  }

  public int preInsert(SessionContext ctx, DatabaseConnection conn,
                       FormInstance instance, ErrorMessages msgs,
                       EventManager manager, SqlAssistor assistor) {
    seqno = zt.cms.pub.code.SerialNumber.getNextSN("BMILEXRECALLAPP","SEQNO")+"";
    assistor.setSqlFieldValue(assistor.getDefaultTbl(), "seqno", seqno);
    return 0;
  }

  public int postInsertOk(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
        EventManager manager) {
    String ilno = ctx.getParameter("ILNO");

    if(ilno != null)
    {
      String sql = "update BMINACTLOANEXCHANGE set nowbal=amt- (select sum(amt) from BMILEXRECALLAPP where BMILEXRECALLAPP.ilno='" +
          ilno + "') where BMINACTLOANEXCHANGE.ilno='" + ilno + "'";
      if (conn.executeUpdate(sql) >= 0) {
        return 0;
      }
      else {
        return -1;
      }
    }
    else
      return -1;
}

public int preEdit(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
        EventManager manager, SqlAssistor assistor) {
        RecordSet rs = conn.executeQuery("select  amt from BMILEXRECALLAPP where seqno="+this.seqno);
    if(rs.next()){
        oldAmt = new BigDecimal(rs.getString("AMT"));
    }
    return 0;
}



public int postEditOk(SessionContext ctx, DatabaseConnection conn, FormInstance instance,
                      ErrorMessages msgs,
                      EventManager manager)
{
  String ilno = ctx.getParameter("ILNO");

  if(ilno != null)
  {
    String sql = "update BMINACTLOANEXCHANGE set nowbal=amt- (select sum(amt) from BMILEXRECALLAPP where BMILEXRECALLAPP.ilno='" +
        ilno + "') where BMINACTLOANEXCHANGE.ilno='" + ilno + "'";
    if (conn.executeUpdate(sql) >= 0) {
      return 0;
    }
    else {
      return -1;
    }
  }
  else
    return -1;

}


public int postDeleteOk(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
        EventManager manager) {
    String ilno = ctx.getParameter("ILNO");

    if(ilno != null)
    {
      String sql = "update BMINACTLOANEXCHANGE set nowbal=amt- (select sum(amt) from BMILEXRECALLAPP where BMILEXRECALLAPP.ilno='" +
          ilno + "') where BMINACTLOANEXCHANGE.ilno='" + ilno + "'";
      if (conn.executeUpdate(sql) >= 0) {
        return 0;
      }
      else {
        return -1;
      }
    }
    else
      return -1;

}


}
