package zt.cms.bm.inactloan;

/**
 *  Description of the Class
 *
 *@author     Administrator
 *@created    2004年1月6日
 */

import zt.cms.bm.common.*;
import zt.cms.pub.*;
import zt.cmsi.biz.*;
import zt.cmsi.pub.*;
import zt.platform.db.*;
import zt.platform.form.control.*;
import zt.platform.form.util.*;
import zt.platform.form.util.event.*;

public class ManagePageAction extends FormActions {
  String bmNo = "";
  public int load(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                  EventManager manager, String parameter) {
    RightChecker.checkReadonly(ctx, conn, instance);
    bmNo = ctx.getParameter("BMNO");
    if (bmNo != null && !bmNo.equals("")) {
      instance.setValue("BMNO", ctx.getParameter("BMNO"));
      trigger(manager, instance, EventType.EDIT_VIEW_EVENT_TYPE,
              Event.BRANCH_CONTINUE_TYPE);
    }
    else {
      trigger(manager, instance, EventType.INSERT_VIEW_EVENT_TYPE,
              Event.BRANCH_CONTINUE_TYPE);
    }
    return 0;
  }

  public int beforeEdit(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                        EventManager manager, SqlAssistor assistor) {
    UpToDateApp data = BMTable.getUpToDateApp(this.bmNo.trim());
    int ifRespLoan = 0;
    if (data != null) {
      ifRespLoan = data.ifRespLoan.intValue();
      String str = "select clientname from bmtable where bmno='" + this.bmNo + "'";
      RecordSet rs = conn.executeQuery(str);
      String clientName = "";
      if (rs.next()) {
        clientName = DBUtil.fromDB(rs.getString("clientname"));
      }
      instance.setValue("CLIENTNAME", clientName);
      instance.setValue("DECIDEDBY", SCUser.getName(data.decidedBy));
      instance.setValue("CLIENTMGR", SCUser.getName(data.clientMgr));
    }
    String ADMINEDBY=DBUtil.getCellValue(conn,"bminactloan","ADMINEDBY","bmno='" + this.bmNo + "'");
    instance.setValue("ADMINEDBY", SCUser.getName(ADMINEDBY));
    if (ifRespLoan == 1) {
      instance.setValue("IFRESPLOAN", "是");
      if (data.firstResp != null) {
        instance.setValue("FIRSTRESP", SCUser.getName(data.firstResp));
      }
      if (data.firstRespPct != null) {
        instance.setValue("FISRTRESPPCT", data.firstRespPct.toString());
      }
    }
    else {
      instance.setValue("IFRESPLOAN", "否");
    }
    return 0;
  }

  public int buttonEvent(SessionContext ctx, DatabaseConnection conn, FormInstance instance,
                         String button,
                         ErrorMessages msgs, EventManager manager) {
    Param param = ParamFactory.getParamByCtx(ctx);
    ctx.setRequestAtrribute("BMPARAM", param);
    param.addParam("BMNO", this.bmNo);
    ctx.setRequestAtrribute("BMNO", this.bmNo);
    RightChecker.transReadOnly(ctx, conn, instance);

    if (button.equals("ADMCHANGE")) {
      trigger(manager, "BMLOANADMCHANGEPAGE", null);
    }
    else if (button.equals("VIEWADMCHANGE")) {
      trigger(manager, "BMLOANADMCHANGELIST", null);
    }
    else if (button.equals("NOTIFY")) {
      trigger(manager, "BMILNOTIFILIST", null);
    }
    else if (button.equals("PROSECUTION")) {
      trigger(manager, "BMILPROSECUTIONLIST", null);
    }
    else if (button.equals("CHECK")) {
      ctx.setRequestAtrribute("title", "风险检查");
      trigger(manager, "BMPLC1", null);
    }
    else if (button.equals("VIEWTAI")) {
      trigger(manager, "BMRQL0", null);
    }
    else if (button.equals("PAYMENTORDER")) {
      trigger(manager, "BMILPAYMENTORDERLIST", null);
    }
    else if (button.equals("PAYBACKAGREEMENT")) {
      trigger(manager, "BMILPAYBACKAGREEMENTLIST", null);
    }
    else if (button.equals("RECALLAPP")) {
      trigger(manager, "BMILRECALLAPPLIST", null);
    }
    else if (button.equals("PENALTY")) {
      ctx.setRequestAtrribute("BMPARAM", param);
      trigger(manager, "BMILPENALTYLIST", null);
    }
    else if (button.equals("GUARANTOR")) {
      param.addParam("flag", "read");
      ctx.setRequestAtrribute("BMNO", param.getBmNo());
      ctx.setRequestAtrribute("BMPARAM", param);
      trigger(manager, "BMGUARANTORLIST", null);
    }
    return 0;
  }
}
