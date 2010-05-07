package zt.cms.bm.inactloan;

/**
 * <p>Title: 信贷管理系统</p>
 * <p>Description: 潍坊信贷</p>
 * <p>Copyright: Copyright (c) 2003  中天信息技术有限公司</p>
 * <p>Company: 中天信息技术有限公司</p>
 * @author Yusg
 * @version 1.0
 */

import zt.cms.bm.common.*;
import zt.platform.db.*;
import zt.platform.form.control.*;
import zt.platform.form.util.*;
import zt.platform.form.util.event.*;

public class BMHXBLListAction extends FormActions {
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
    trigger(manager, "BMHXBLPAGE", null);
    return 0;
  }

}
