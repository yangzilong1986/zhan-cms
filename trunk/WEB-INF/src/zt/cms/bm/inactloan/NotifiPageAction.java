package zt.cms.bm.inactloan;
/**
 *  Description of the Class
 *
 *@author     Administrator
 *@created    2004Äê1ÔÂ5ÈÕ
 * modified  by  yusg  2004/04/27
 * modified  by  wxj at 040513
 */

import zt.platform.db.*;
import zt.platform.form.control.*;
import zt.platform.form.util.*;
import zt.platform.form.util.event.*;

public class NotifiPageAction extends CommonPageAction {
  private String strBmNo = null;

  public String getTableName() {
    return "BMILNOTIFI";
  }

  public String getAutoIncrementField() {
    return "NOTIFNO";
  }

  public int load(SessionContext ctx, DatabaseConnection conn, FormInstance instance,
                  ErrorMessages msgs,
                  EventManager manager, String parameter) {
    strBmNo = (String) ctx.getRequestAttribute("BMNO");
    super.load(ctx, conn, instance, msgs, manager, parameter);
    return 0;
  }

  public int postInsertOk(SessionContext ctx, DatabaseConnection conn, FormInstance instance,
                          ErrorMessages msgs,
                          EventManager manager) {
    String strNotifyNo = super.strNotifyNo;
    ctx.setRequestAtrribute("BMNO", this.strBmNo);
    ctx.setRequestAtrribute("NOTIFNO", strNotifyNo);
    ctx.setTarget("/jspreport/bmilnotifi.jsp");
    return 0;
  }

  public int postEditOk(SessionContext ctx, DatabaseConnection conn, FormInstance instance,
                        ErrorMessages msgs,
                        EventManager manager) {
    ctx.setRequestAtrribute("BMNO", this.strBmNo);
    ctx.setTarget("/jspreport/bmilnotifi.jsp");
    return 0;
  }

  public int preReference(SessionContext ctx, DatabaseConnection conn,
                          FormInstance instance, ErrorMessages msgs,
                          EventManager manager, String reffldnm,
                          SqlWhereUtil sqlWhereUtil) {
    sqlWhereUtil.addWhereField("BMLOANCLIENT", "BMNO", this.strBmNo.trim(),
                               SqlWhereUtil.DataType_Character,
                               sqlWhereUtil.OperatorType_Equals);
    return 0;
  }
}
