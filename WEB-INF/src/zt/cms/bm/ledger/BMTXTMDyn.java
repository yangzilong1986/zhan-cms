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

public class BMTXTMDyn extends FormActions {
  private static Logger logger = Logger.getLogger("zt.cms.bm.ledger.BMTXTMDyn");
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
      sqlWhereUtil.addWhereField("BMBILLDIS", "BRHID", strinsql, SqlWhereUtil.DataType_Sql, sqlWhereUtil.OperatorType_In);
    }
    return 0;
  }

  public int buttonEvent(SessionContext ctx, DatabaseConnection conn, FormInstance instance,
                         String button,
                         ErrorMessages msgs, EventManager manager) {
    ctx.setRequestAtrribute("flag", strFlag);
    if ( (button != null) && (button.equals(SessionAttributes.REQUEST_BUTTON_ADD_NAME)
                              || button.equals(SessionAttributes.REQUEST_EDIT_BUTTON_VALUE))) {
      this.trigger(manager, "BMTXTM", null);
    }
    return 0;
  }

}
