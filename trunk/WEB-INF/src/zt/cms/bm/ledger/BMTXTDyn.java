package zt.cms.bm.ledger;
/**
 *  测试：多表－－－数据添加,修改，删除(获得值)
 *
 *@author     Administrator
 *@created    2003年12月2日
 */

import java.util.logging.*;
import zt.cms.bm.bill.*;
import zt.platform.db.*;
import zt.platform.form.control.*;
import zt.platform.form.util.*;
import zt.platform.form.util.event.*;
import zt.platform.form.config.*;

public class BMTXTDyn extends FormActions {
  private static Logger logger = Logger.getLogger("zt.cms.bm.ledger.BMTXTDyn");
  String strFlag = null;
  String clientNo = null;
  public int load(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                  EventManager manager, String parameter) {
    strFlag = ctx.getParameter("flag") == null ? null : ctx.getParameter("flag").trim();
    this.clientNo = ctx.getRequestAttribute("CLIENTNO") == null ? null : (String)ctx.getRequestAttribute("CLIENTNO");

    if(strFlag == null)
    {
      strFlag = ctx.getRequestAttribute("flag")==null? null:(String)ctx.getRequestAttribute("flag");
    }

    //System.out.println("flag="+strFlag+" CLIETN="+this.clientNo);

    //维护时“操作”按钮改为“维护”,否则为“查询”
    instance.useCloneFormBean();
    FormBean fb = instance.getFormBean();
    ElementBean eb = fb.getElement("operation");
    if(this.clientNo == null)
    {
      if (strFlag.equals("write")) {
        fb.setTitle("贴现(转贴现)台帐维护");
        eb.setCaption("操作");
      }
      else {
        fb.setTitle("贴现(转贴现)台帐查询");
        eb.setCaption("查询");
      }
    }
    else
    {
        fb.setTitle("贴现(转贴现)查询");
        eb.setCaption("查询");
    }
    return 0;
  }

  public int preFind(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                     EventManager manager, SqlWhereUtil sqlWhereUtil) {
    if (strFlag.equals("read") || strFlag == null) {
      instance.setReadonly(true);
    }
    else {
      instance.setReadonly(false);
    }
    if (super.preFind(ctx, conn, instance, msgs, manager, sqlWhereUtil) >= 0) {
      if(this.clientNo == null)
      {
        String strinsql = Function.getAllSubBrhIds(msgs, ctx);
        sqlWhereUtil.addWhereField("BMBILLDIS", "BRHID", strinsql,
                                   SqlWhereUtil.DataType_Sql,
                                   sqlWhereUtil.OperatorType_In);
      }
      else
      {
        sqlWhereUtil.addWhereField("BMBILLDIS", "CLIENTNO", this.clientNo,
                                   SqlWhereUtil.DataType_Character,
                                   sqlWhereUtil.OperatorType_Equals);
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

    ctx.setRequestAtrribute("flag", strFlag);
    if ( (button != null) && (button.equals(SessionAttributes.REQUEST_BUTTON_ADD_NAME)
                              || button.equals(SessionAttributes.REQUEST_EDIT_BUTTON_VALUE))) {
      this.trigger(manager, "BMTXT0", null);
    }
    return 0;
  }

  public int preReference(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                          EventManager manager, String reffldnm, SqlWhereUtil sqlWhereUtil) {

    String strinsql = Function.getAllSubBrhIds(msgs, ctx);
    sqlWhereUtil.addWhereField("SCBRANCH", "BRHID", strinsql, SqlWhereUtil.DataType_Sql, sqlWhereUtil.OperatorType_In);

    return 0;
  }

}
