package zt.cms.bm.ledger;

/**
 *信贷贷款台帐查询
 *
 *@author     sundj
 *@created    2003年12月2日
 *@update     wxj
 */
import java.util.logging.*;

import zt.cms.bm.bill.*;
import zt.cms.cm.common.*;
import zt.cmsi.pub.*;
import zt.platform.db.*;
import zt.platform.form.control.*;
import zt.platform.form.util.*;
import zt.platform.form.util.event.*;

public class BMRQLLDyn extends FormActions {

  /**
   *  Description of the Method
   *
   *@param  ctx        Description of the Parameter
   *@param  conn       Description of the Parameter
   *@param  instance   Description of the Parameter
   *@param  msgs       Description of the Parameter
   *@param  manager    Description of the Parameter
   *@param  parameter  Description of the Parameter
   *@return            Description of the Return Value
   */
  private static Logger logger = Logger.getLogger("zt.cms.bm.ledger.BMRQLLDyn");
  String strFlag = null;
  boolean firstRun = true;
  String clientNo =null;

  public int load(SessionContext ctx, DatabaseConnection conn, FormInstance instance,
                  ErrorMessages msgs,
                  EventManager manager, String parameter) {
    strFlag = ctx.getParameter("flag") == null ? "" : ctx.getParameter("flag").trim();
    if (strFlag.equals("")) {
      strFlag = ctx.getAttribute("flag") == null ? "" :
        (String) ctx.getAttribute("flag");
    }
    clientNo = (String) ctx.getRequestAttribute("CLIENTNO");
    return 0;
  }

  public int preFind(SessionContext ctx, DatabaseConnection conn, FormInstance instance,
                     ErrorMessages msgs,
                     EventManager manager, SqlWhereUtil sqlWhereUtil) {

    if (firstRun == true && clientNo == null) {
      firstRun = false;
      return confitem.QUERY_FORM_INPUT_CRITERIA_CODE;
    }

    if (clientNo != null) {
      //String value = ClientSum.getClientSum2(clientNo, "RQLOANLEDGER", "CONTRACTAMT",conn);
      //String value2 = ClientSum.getClientSum2(clientNo, "RQLOANLEDGER", "NOWBAL",conn);
      // changed by JGO, too slow if we add following line
      //ctx.setAfterBody("CONTRACTAMT=" + value + ";NOWBAL=" + value2);
      sqlWhereUtil.addWhereField("BMTABLE", "CLIENTNO", clientNo, SqlWhereUtil.DataType_Character,
                                 sqlWhereUtil.OperatorType_Equals);
    }
    if (strFlag.equals("read") || strFlag == null) {
      instance.setReadonly(true);
    }
    else {
      instance.setReadonly(false);
    }

    if(clientNo == null)
    {
      if (super.preFind(ctx, conn, instance, msgs, manager, sqlWhereUtil) >= 0) {
        String strinsql = Function.getAllSubBrhIds(msgs, ctx);
        sqlWhereUtil.addWhereField("RQLOANLEDGER", "BRHID", strinsql,
                                   SqlWhereUtil.DataType_Sql,
                                   sqlWhereUtil.OperatorType_In);
      }
    }
    return 0;
  }

  /**
   *  Description of the Method
   *
   *@param  ctx       Description of the Parameter
   *@param  conn      Description of the Parameter
   *@param  instance  Description of the Parameter
   *@param  button    Description of the Parameter
   *@param  msgs      Description of the Parameter
   *@param  manager   Description of the Parameter
   *@return           Description of the Return Value
   */
  public int buttonEvent(SessionContext ctx, DatabaseConnection conn, FormInstance instance,
                         String button,
                         ErrorMessages msgs, EventManager manager) {

    String strbmno = ctx.getParameter("BMNO") == null ? null : ctx.getParameter("BMNO").trim();
    String stractno = ctx.getParameter("ACTNO") == null ? null : ctx.getParameter("BMNO").trim();
    String strcnlno = ctx.getParameter("CNLNO") == null ? null : ctx.getParameter("BMNO").trim();

    Param pm = new Param();
    pm.addParam(ParamName.Flag, strFlag);
    pm.addParam(ParamName.BMNo, strbmno);
    pm.addParam("ACTNO", stractno);
    pm.addParam("CNLNO", strcnlno);
    ctx.setRequestAtrribute(ParamName.ParamName, pm);
    if ( (button != null) && (button.equals(SessionAttributes.REQUEST_BUTTON_ADD_NAME)
                              || button.equals(SessionAttributes.REQUEST_EDIT_BUTTON_VALUE))) {
      this.trigger(manager, "BMRQL0", null);
    }
    return 0;
  }

  public int preReference(SessionContext ctx, DatabaseConnection conn, FormInstance instance,
                          ErrorMessages msgs,
                          EventManager manager, String reffldnm, SqlWhereUtil sqlWhereUtil) {

    String strinsql = Function.getAllSubBrhIds(msgs, ctx);
    sqlWhereUtil.addWhereField("SCBRANCH", "BRHID", strinsql, SqlWhereUtil.DataType_Sql,
                               sqlWhereUtil.OperatorType_In);
    return 0;
  }

}
