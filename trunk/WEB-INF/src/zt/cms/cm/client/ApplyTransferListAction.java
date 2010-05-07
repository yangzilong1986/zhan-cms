package zt.cms.cm.client;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

import zt.cms.bm.common.*;
import zt.cms.cm.common.RightChecker;
import zt.platform.db.*;
import zt.platform.form.control.*;
import zt.platform.form.util.*;
import zt.platform.form.util.event.*;
import zt.platform.user.*;
import zt.platform.form.config.*;
import zt.cms.pub.*;
public class ApplyTransferListAction extends FormActions {

  public int preFind(SessionContext ctx, DatabaseConnection conn, FormInstance instance,
                     ErrorMessages msgs,
                     EventManager manager, SqlWhereUtil sqlWhereUtil) {
    if (super.preFind(ctx, conn, instance, msgs, manager, sqlWhereUtil) >= 0) {
      UserManager um = (UserManager) ctx.getAttribute(SystemAttributeNames.USER_INFO_NAME);

      //BRHID（用户网点）
      String BRHID = SCUser.getBrhId(um.getUserName());
      if (BRHID == null || BRHID.length() < 1) {
        msgs.add("用户网点不存在！");
        return -1;
      }
      //SUBBRHIDs（用户网点下的所有实网点，包括自己）
      String SUBBRHIDs = SCBranch.getSubBranchAll(BRHID);
      if (SUBBRHIDs == null || SUBBRHIDs.length() < 1) {
        msgs.add("下属网点不存在！");
        return -1;
      }
      else {
        SUBBRHIDs = "'" + SUBBRHIDs.replaceAll(",", "','") + "'";
      }

      sqlWhereUtil.addWhereField("CMCLIENTTRANSFER", "APPLYBRHID",
                                 SUBBRHIDs,
                                 SqlWhereUtil.DataType_Sql,
                                 sqlWhereUtil.OperatorType_In);
      if (sqlWhereUtil.toSqlWhere(true).indexOf("APPROVED") == -1) {
        sqlWhereUtil.addWhereField("CMCLIENTTRANSFER", "APPROVED",
                                   "0",
                                   SqlWhereUtil.DataType_Number,
                                   sqlWhereUtil.OperatorType_Equals);

      }
    }
    return 0;
  }

  public int buttonEvent(SessionContext ctx, DatabaseConnection conn, FormInstance instance,
                         String button,
                         ErrorMessages msgs, EventManager manager) {
    RightChecker.transReadOnly(ctx, conn, instance);
    trigger(manager, "ApplyTransferPage", null);
    return 0;
  }

}
