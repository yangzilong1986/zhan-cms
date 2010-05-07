package zt.cms.bm.inactloan;
/**
 *  Description of the Class
 *
 *@author     Administrator
 *@created    2004Äê1ÔÂ6ÈÕ
 */

import zt.cms.bm.common.*;
import zt.cms.pub.code.*;
import zt.cmsi.pub.*;
import zt.platform.db.*;
import zt.platform.form.control.*;
import zt.platform.form.util.*;
import zt.platform.form.util.event.*;

public abstract class CommonPageAction extends FormActions {
  public abstract String getTableName();
  public String strNotifyNo = null;

  public String getAutoIncrementField() {
    return "SEQNO";
  }

  Param param = null;

  public int load(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                  EventManager manager, String parameter) {
    RightChecker.checkReadonly(ctx, conn, instance);
    param = ParamFactory.getParamByCtx(ctx);
    param.addParam("BMNO", ctx.getRequestAttribute("BMNO"));

    if (ctx.getParameter(getAutoIncrementField()) != null) {
      instance.setValue(getAutoIncrementField(), ctx.getParameter(getAutoIncrementField()));
      trigger(manager, instance, EventType.EDIT_VIEW_EVENT_TYPE, Event.BRANCH_CONTINUE_TYPE);
    }
    else {
      trigger(manager, instance, EventType.INSERT_VIEW_EVENT_TYPE, Event.BRANCH_CONTINUE_TYPE);
    }
    return 0;
  }

  public int beforeInsert(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                          EventManager manager) {
    instance.setValue("BMNO", param.getBmNo());
    instance.setFieldReadonly("BMNO", true);
    instance.setValue("OPERATOR", SessionInfo.getLoginUserName(ctx));
    return 0;
  }

  public int preInsert(SessionContext ctx, DatabaseConnection conn,
                       FormInstance instance, ErrorMessages msgs,
                       EventManager manager, SqlAssistor assistor) {
    int seqNo = (int) SerialNumber.getNextSN(getTableName(), getAutoIncrementField());
    this.strNotifyNo = String.valueOf(seqNo);
    assistor.setSqlFieldValue(assistor.getDefaultTbl(), getAutoIncrementField(),
                              seqNo + "");

    String operator = SessionInfo.getLoginUserName(ctx);
    instance.setValue("OPERATOR", operator);
    assistor.setSqlFieldValue(assistor.getDefaultTbl(), "OPERATOR",
                              operator + "");

    return 0;
  }

  public int preEdit(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                     EventManager manager, SqlAssistor assistor) {
    String operator = SessionInfo.getLoginUserName(ctx);
    instance.setValue("OPERATOR", operator);
    assistor.setSqlFieldValue(assistor.getDefaultTbl(), "OPERATOR",
                              operator + "");
    return 0;
  }

}
