package zt.cms.bm.inactloan;

/**
 *已置换或核销的不良贷款列表
 *
 *@author     wxj
 *@created    2004年5月21日
 */
import zt.cms.bm.common.*;
import zt.platform.db.*;
import zt.platform.form.control.*;
import zt.platform.form.util.*;
import zt.platform.form.util.event.*;

public class BMZHBLListAction extends FormActions {
  String flag = null;
  public int load(SessionContext ctx, DatabaseConnection conn,
                  FormInstance instance, ErrorMessages msgs,
                  EventManager manager, String parameter) {
    //flag
    flag = (String) ctx.getParameter("flag");
    if (flag == null) {
      flag = "read";
    }
    if (flag.equals("write")) {
      instance.setReadonly(false);
    }
    else {
      instance.setReadonly(true);
    }
    return 0;
  }

  public int preFind(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                     EventManager manager, SqlWhereUtil sqlWhereUtil) {
    if (super.preFind(ctx, conn, instance, msgs, manager, sqlWhereUtil) >= 0) {
      String sql = SessionInfo.getLoginAllSubBrhIds(ctx);
      sqlWhereUtil.addWhereField("BMINACTLOANEXCHANGE", "BRHID", sql, SqlWhereUtil.DataType_Sql, sqlWhereUtil.OperatorType_In);
    }
    return 0;
  }

  public int buttonEvent(SessionContext ctx, DatabaseConnection conn, FormInstance instance, String button,
                         ErrorMessages msgs, EventManager manager) {
    ctx.setRequestAtrribute("flag",flag);
    trigger(manager, "BMZHBLPAGE", null);
    return 0;
  }

}
