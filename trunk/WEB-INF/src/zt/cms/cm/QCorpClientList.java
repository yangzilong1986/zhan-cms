package zt.cms.cm;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
import java.util.logging.*;

import zt.cms.pub.*;
import zt.platform.db.*;
import zt.platform.form.config.*;
import zt.platform.form.control.*;
import zt.platform.form.util.*;
import zt.platform.form.util.event.*;
import zt.platform.user.*;
import zt.cmsi.pub.confitem;

public class QCorpClientList extends FormActions {
  public static Logger logger = Logger.getLogger("zt.cms.cm.QCorpClientList");
  private boolean firstRun = true;
  private String strFlag = null;
  public int load(SessionContext ctx, DatabaseConnection conn, FormInstance instance,
                  ErrorMessages msgs,
                  EventManager manager, String parameter) {
    strFlag = ctx.getParameter("flag");
    if (strFlag.equals("write")) {
      instance.setReadonly(false);
    }
    else {
      instance.setReadonly(true);
    }
    return 0;
  }

  public int buttonEvent(SessionContext ctx, DatabaseConnection conn, FormInstance instance,
                         String button,
                         ErrorMessages msgs, EventManager manager) {
    if (button != null && button.equals(SessionAttributes.REQUEST_BUTTON_ADD_NAME)) {
      //ctx.setAttribute("flag",strFlag);
      ctx.setRequestAtrribute("flag", strFlag);
      trigger(manager, "CMCC02", null);
    }
    else if (button != null && button.equals(SessionAttributes.REQUEST_EDIT_BUTTON_VALUE)) {
      //ctx.setAttribute("flag",strFlag);
      ctx.setRequestAtrribute("flag", strFlag);
      trigger(manager, "CMCC02", null);
    }
    return 0;
  }

  public int preFind(SessionContext ctx, DatabaseConnection conn, FormInstance instance,
                     ErrorMessages msgs,
                     EventManager manager, SqlWhereUtil sqlWhereUtil) {
    if (super.preFind(ctx, conn, instance, msgs, manager, sqlWhereUtil) < 0) {
      return -1;
    }

    if (firstRun == true) {
      firstRun = false;
      return confitem.QUERY_FORM_INPUT_CRITERIA_CODE;
    }

    if ( (instance.getStringValue("NAME") != null && instance.getStringValue("NAME").length() > 0) ||
        (instance.getStringValue("ID") != null && instance.getStringValue("ID").length() > 0) ||
        (instance.getStringValue("MNTBRHID") != null && instance.getStringValue("MNTBRHID").length() > 0) ||
        (instance.getStringValue("ICBREGCODE") != null && instance.getStringValue("ICBREGCODE").length() > 0) ||
        (instance.getStringValue("APPBRHID") != null && instance.getStringValue("APPBRHID").length() > 0)) {
      return 0;
    }
    UserManager um = (UserManager) ctx.getAttribute(SystemAttributeNames.USER_INFO_NAME);
    //BRHID（用户网点）
    String BRHID = SCUser.getBrhId(um.getUserName());
    if (BRHID == null || BRHID.length() < 1) {
      return -1;
    }
    //SUBBRHIDs（用户网点下的所有实网点）
    String SUBBRHIDs = SCBranch.getSubBranchAll(BRHID);
    if (SUBBRHIDs == null || SUBBRHIDs.length() < 1) {
      msgs.add("下属网点不存在！");
      return -1;
    }
    else {
      SUBBRHIDs = "'" + SUBBRHIDs.replaceAll(",", "','") + "'";
    }
    //sqlWhereUtil
    sqlWhereUtil.addWhereField("CMCORPCLIENT", "APPBRHID", SUBBRHIDs,
                               SqlWhereUtil.DataType_Sql,
                               sqlWhereUtil.OperatorType_In);
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
    if (reffldnm.equals("APPBRHID")) {
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

    if (reffldnm.equals("MNTBRHID")) {
      //APPBRHIDs（用户网点下的所有虚网点）
      //String SUBBRHIDs = SCBranch.getAllSubBrhAndSelf3(BRHID);
      String SUBBRHIDs = SCBranch.getAllSubBrhAndSelf1(BRHID);
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

}
