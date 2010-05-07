package zt.cms.bm.ledger;
/*
 *  Copyright (c) 2004 Email: liwei@zhongtian.biz
 *  $Id: BMAcptBillTMDyn.java,v 1.1 2005/06/28 07:00:23 jgo Exp $
 */

import zt.cms.bm.bill.*;
import zt.platform.db.*;
import zt.platform.form.control.*;
import zt.platform.form.util.*;
import zt.platform.form.util.event.*;

public class BMAcptBillTMDyn extends FormActions {
  String strFlag = null;

  public int load(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                  EventManager manager, String parameter) {
    strFlag = ctx.getParameter("flag") == null ? null : ctx.getParameter("flag").trim();

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
      String strinsql = Function.getAllSubBrhIds(msgs, ctx);
      sqlWhereUtil.addWhereField("BMACPTBILL", "BRHID", strinsql, SqlWhereUtil.DataType_Sql, sqlWhereUtil.OperatorType_In);
    }
    return 0;
  }

  public int buttonEvent(SessionContext ctx, DatabaseConnection conn, FormInstance instance,
                         String button,
                         ErrorMessages msgs, EventManager manager) {

    ctx.setRequestAtrribute("flag", strFlag);

    if ( (button != null) && (button.equals(SessionAttributes.REQUEST_BUTTON_ADD_NAME)
                              || button.equals(SessionAttributes.REQUEST_EDIT_BUTTON_VALUE))) {
      this.trigger(manager, "BMABTM", null);
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
