package zt.cms.bm.ledger;

import java.util.logging.*;

import zt.cms.bm.bill.*;
import zt.platform.db.*;
import zt.platform.form.config.*;
import zt.platform.form.control.*;
import zt.platform.form.util.*;
import zt.platform.form.util.event.*;
import zt.cms.pub.*;
import zt.platform.user.*;

/**
 *  测试：多表－－－数据添加,修改，删除(获得值)
 *
 *@author     Administrator
 *@created    2003年12月2日
 */
public class BMAcptBillTDyn extends FormActions {
  private static Logger logger = Logger.getLogger("zt.cms.bm.ledger.BMAcptBillTDyn");
  String strFlag = null;
  String clientNo = null;

  public int load(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                  EventManager manager, String parameter) {
    //strFlag
    strFlag = ctx.getParameter("flag") == null ? null : ctx.getParameter("flag").trim();
    this.clientNo = ctx.getRequestAttribute("CLIENTNO") == null ? null : (String)ctx.getRequestAttribute("CLIENTNO");

    if(strFlag == null)
    {
      strFlag = ctx.getRequestAttribute("flag")==null? null:(String)ctx.getRequestAttribute("flag");
    }

    //System.out.println("flag="+strFlag+" CLIETN="+this.clientNo);
    if ( strFlag == null || strFlag.equals("read") ) {
      instance.setReadonly(true);
    }
    else {
      instance.setReadonly(false);
    }
    //维护时“操作”按钮改为“维护”,否则为“查询”
    instance.useCloneFormBean();
    FormBean fb = instance.getFormBean();
    ElementBean eb = fb.getElement("operation");
    if(this.clientNo == null)
    {
      if (strFlag.equals("write")) {
        fb.setTitle("银行承兑汇票台帐维护");
        eb.setCaption("操作");
      }
      else {
        fb.setTitle("银行承兑汇票台帐查询");
        eb.setCaption("查询");
      }
    }
    else
    {
        fb.setTitle("银行承兑汇票查询");
        eb.setCaption("查询");
    }
    return 0;
  }

  public int preFind(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                     EventManager manager, SqlWhereUtil sqlWhereUtil) {

    if (super.preFind(ctx, conn, instance, msgs, manager, sqlWhereUtil) >= 0) {
      if(this.clientNo == null)
      {
        UserManager um = (UserManager) ctx.getAttribute(SystemAttributeNames.
            USER_INFO_NAME);
        //BRHID（用户网点）
        String mybrhid = SCUser.getBrhId(um.getUserName());
        if (mybrhid == null || mybrhid.length() < 1) {
          msgs.add("当前用户无关联网点！");
          return -1;
        }
        //APPBRHIDs（用户网点下的所有实网点，包括自己）
        String SUBBRHIDs = SCBranch.getAllSubBrhAndSelf1(mybrhid);
        if (SUBBRHIDs == null || SUBBRHIDs.length() < 1) {
          msgs.add("下属网点不存在！");
          return -1;
        }
        else {
          SUBBRHIDs = "'" + SUBBRHIDs.replaceAll(",", "','") + "'";
        }
        sqlWhereUtil.addWhereField("BMACPTBILL", "BRHID", SUBBRHIDs,
                                   SqlWhereUtil.DataType_Sql,
                                   sqlWhereUtil.OperatorType_In);
      }
      else
      {
        sqlWhereUtil.addWhereField("BMACPTBILL", "CLIENTNO", this.clientNo,
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
      this.trigger(manager, "BMABT0", null);
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
