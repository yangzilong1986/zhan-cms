package zt.cms.bm.inactloan;

import java.sql.*;

import zt.platform.db.*;
import zt.platform.form.control.*;
import zt.platform.form.util.*;
import zt.platform.form.util.event.*;
import zt.cms.pub.code.*;
import zt.platform.form.control.FormActions;
import zt.platform.form.control.SessionContext;
import zt.platform.db.DatabaseConnection;
import zt.platform.form.util.FormInstance;
import zt.platform.form.util.event.ErrorMessages;
import zt.platform.form.util.event.EventManager;
import zt.platform.form.util.SessionAttributes;
import zt.platform.user.UserManager;
import zt.platform.form.config.SystemAttributeNames;
import zt.platform.form.util.SqlWhereUtil;
import zt.cms.pub.SCUser;
import zt.cms.pub.SCBranch;

public class BMInactLoanPageAction2 extends FormActions {
    public int load(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
            EventManager manager, String parameter) {
        String bmNo = (String) ctx.getParameter("BMNO");
        System.out.println("BMNO:"+bmNo);
        if (bmNo != null && !bmNo.equals("")) {
            instance.setValue("BMNO", ctx.getParameter("BMNO"));
            trigger(manager, instance, EventType.EDIT_VIEW_EVENT_TYPE,
                    Event.BRANCH_CONTINUE_TYPE);
        } else {
            trigger(manager, instance, EventType.INSERT_VIEW_EVENT_TYPE,
                    Event.BRANCH_CONTINUE_TYPE);
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
      //APPBRHIDs（用户网点下的所有实网点）
      String SUBBRHIDs = SCBranch.getSubBranchAll(BRHID);
      if (SUBBRHIDs != null && SUBBRHIDs.length() > 0) {
        SUBBRHIDs = BRHID + "," + SUBBRHIDs;
        SUBBRHIDs = "'" + SUBBRHIDs.replaceAll(",", "','") + "'";
      }
      else {
        SUBBRHIDs = "'" + BRHID + "'";
      }
      //sqlWhereUtil
      if (reffldnm.equals("APPBRHID")) {
        sqlWhereUtil.addWhereField("SCBRANCH", "BRHID", SUBBRHIDs,
                                   SqlWhereUtil.DataType_Sql,
                                   sqlWhereUtil.OperatorType_In);
      }
      if (reffldnm.equals("MNTBRHID")) {
        sqlWhereUtil.addWhereField("SCBRANCH", "BRHID", SUBBRHIDs,
                                   SqlWhereUtil.DataType_Sql,
                                   sqlWhereUtil.OperatorType_In);
      }
      return 0;
    }

}