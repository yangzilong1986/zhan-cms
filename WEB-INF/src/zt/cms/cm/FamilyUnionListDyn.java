package zt.cms.cm;

/**
 *  测试：多表－－－数据添加,修改，删除(获得值)
 *
 *@author     Administrator
 *@created    2003年12月2日
 */

import zt.cms.bm.common.*;
import zt.platform.db.*;
import zt.platform.form.control.*;
import zt.platform.form.util.*;
import zt.platform.form.util.event.*;
import zt.platform.user.*;
import zt.cms.pub.*;
import zt.platform.form.config.*;

public class FamilyUnionListDyn extends FormActions {
  private String strFlag = null;

  public int load(SessionContext ctx, DatabaseConnection conn, FormInstance instance,
                  ErrorMessages msgs,
                  EventManager manager, String parameter) {
    strFlag = ctx.getParameter("flag").trim();
    if (strFlag != null) {

      if (strFlag.equals("write")) {
        instance.setReadonly(false);
      }
      else if (strFlag.equals("read")) {
        instance.setReadonly(true);
      }
    }
    return 0;
  }

  public int preReference(SessionContext ctx, DatabaseConnection conn,
                          FormInstance instance, ErrorMessages msgs,
                          EventManager manager, String reffldnm,
                          SqlWhereUtil sqlWhereUtil) {
    UserManager um = (UserManager) ctx.getAttribute(SystemAttributeNames.USER_INFO_NAME);
    //BRHID（用户网点）
    String BRHID = SCUser.getBrhId(um.getUserName());
    if (BRHID == null || BRHID.length() < 1) {
      return -1;
    }
    if (reffldnm.equals("BRHID")) {
      //APPBRHIDs（用户网点下的所有实网点）
      String SUBBRHIDs = SCBranch.getSubBranchAll(BRHID);
      if (SUBBRHIDs == null || SUBBRHIDs.length() < 1) {
        msgs.add("下属网点不存在！");
        return -1;
      }
      else {
        SUBBRHIDs = "'" + SUBBRHIDs.replaceAll(",", "','") + "'";
      }
      //sqlWhereUtil

      sqlWhereUtil.addWhereField("SCBRANCH", "BRHID", SUBBRHIDs,
                                 SqlWhereUtil.DataType_Sql,
                                 sqlWhereUtil.OperatorType_In);
    }
    return 0;
  }

  public int buttonEvent(SessionContext ctx, DatabaseConnection conn, FormInstance instance,
                         String button,
                         ErrorMessages msgs, EventManager manager) {
    if ( (button != null) && (button.equals(SessionAttributes.REQUEST_BUTTON_ADD_NAME)
                              || button.equals(SessionAttributes.REQUEST_EDIT_BUTTON_VALUE))) {
      if (strFlag != null) {
        ctx.setRequestAtrribute("flag", strFlag);
      }
      this.trigger(manager, "CMFU00", null);
    }
    return 0;
  }

  public int preFind(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                     EventManager manager, SqlWhereUtil sqlWhereUtil) {
    if (super.preFind(ctx, conn, instance, msgs, manager, sqlWhereUtil) >= 0) {
      String strinsql = SessionInfo.getLoginAllSubBrhIds(ctx);
      sqlWhereUtil.addWhereField("CMFAMILYUNION", "BRHID", strinsql, SqlWhereUtil.DataType_Sql, sqlWhereUtil.OperatorType_In);
    }
    return 0;
  }
}
