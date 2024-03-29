package zt.cms.bm.appplus;

import zt.cms.bm.common.*;
import zt.cms.cm.common.RightChecker;
import zt.cms.pub.*;
import zt.cms.pub.code.*;
import zt.cmsi.biz.*;
import zt.cmsi.pub.*;
import zt.platform.db.*;
import zt.platform.form.config.*;
import zt.platform.form.control.*;
import zt.platform.form.util.*;
import zt.platform.form.util.event.*;
import zt.platform.user.*;

/**
 *  Description of the Class
 *
 *@author     Administrator
 *@created    2004年1月6日
 */
public abstract class CommonPageAction extends FormActions {

  /**
   *  Description of the Field
   */
  public String bmNo = "";

  /**
   *  Gets the tableName attribute of the CommonPageAction object
   *
   *@return    The tableName value
   */
  public abstract String getTableName();

  /**
   *  Description of the Method
   *
   *@param  ctx        Description of the Parameter
   *@param  conn       Description of the Parameter
   *@param  instance   Description of the Parameter
   *@param  msgs       Description of the Parameter
   *@param  manager    Description of the Parameter
   *@param  parameter  Description of the Parameter
   *@return            Description of the Return Value
   */
  public int load(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                  EventManager manager, String parameter) {

    RightChecker.checkReadonly(ctx, conn, instance);
    Param param = (Param) ctx.getRequestAttribute("BMPARAM");
    this.bmNo = param.getBmNo();
    if (ctx.getParameter("PLEDGENO") != null && !ctx.getParameter("PLEDGENO").equals("")) {
      instance.setValue("PLEDGENO", ctx.getParameter("PLEDGENO"));
      trigger(manager, instance, EventType.EDIT_VIEW_EVENT_TYPE,
              Event.BRANCH_CONTINUE_TYPE);
    }
    else {
      trigger(manager, instance, EventType.INSERT_VIEW_EVENT_TYPE,
              Event.BRANCH_CONTINUE_TYPE);
    }
    return 0;
  }

  /**
   *  Description of the Method
   *
   *@param  ctx       Description of the Parameter
   *@param  conn      Description of the Parameter
   *@param  instance  Description of the Parameter
   *@param  msgs      Description of the Parameter
   *@param  manager   Description of the Parameter
   *@return           Description of the Return Value
   */
  public int beforeInsert(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                          EventManager manager) {
    instance.setValue("BMNO", this.bmNo);
    instance.setFieldReadonly("BMNO", true);
    instance.setValue("OPERATOR", SessionInfo.getLoginUserName(ctx));
    return 0;
  }

  /**
   *  Description of the Method
   *
   *@param  ctx       Description of the Parameter
   *@param  conn      Description of the Parameter
   *@param  instance  Description of the Parameter
   *@param  msgs      Description of the Parameter
   *@param  manager   Description of the Parameter
   *@param  assistor  Description of the Parameter
   *@return           Description of the Return Value
   */
  public int preInsert(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                       EventManager manager, SqlAssistor assistor) {
    int pledgeNo = (int) SerialNumber.getNextSN(getTableName(), "PLEDGENO");
    System.out.println(getTableName() + "--------" + pledgeNo);
    instance.setValue("PLEDGENO", pledgeNo);
    assistor.setSqlFieldValue(assistor.getDefaultTbl(), "PLEDGENO", pledgeNo + "");

    String operator = SessionInfo.getLoginUserName(ctx);
    instance.setValue("OPERATOR", operator);
    assistor.setSqlFieldValue(assistor.getDefaultTbl(), "OPERATOR",
                              operator + "");

    return 0;
  }

  /**
   *  Description of the Method
   *
   *@param  ctx       Description of the Parameter
   *@param  conn      Description of the Parameter
   *@param  instance  Description of the Parameter
   *@param  msgs      Description of the Parameter
   *@param  manager   Description of the Parameter
   *@param  assistor  Description of the Parameter
   *@return           Description of the Return Value
   */
  public int preEdit(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                     EventManager manager, SqlAssistor assistor) {
    String operator = SessionInfo.getLoginUserName(ctx);
    instance.setValue("OPERATOR", operator);
    assistor.setSqlFieldValue(assistor.getDefaultTbl(), "OPERATOR",
                              operator + "");
    return 0;
  }

  public int preReference(SessionContext ctx, DatabaseConnection conn,
                          FormInstance instance, ErrorMessages msgs,
                          EventManager manager, String reffldnm,
                          SqlWhereUtil sqlWhereUtil) {
    UserManager um = (UserManager) ctx.getAttribute(SystemAttributeNames.
      USER_INFO_NAME);
    if (reffldnm.equals("APPBRHID") || reffldnm.equals("CLIENTMGR") ||
        reffldnm.equals("UNIONNO") || reffldnm.equals("CLIENTNO")) {
      //BRHID（用户网点）
      String BRHID = SCUser.getBrhId(um.getUserName());
      System.out.println("BRHID:" + BRHID);
      if (BRHID == null || BRHID.length() < 1) {
        return -1;
      }
      //APPBRHIDs（用户网点下的所有实网点，包括自己）
      String SUBBRHIDs = SCBranch.getSubBranchAll(BRHID);
      if (SUBBRHIDs == null || SUBBRHIDs.length() < 1) {
        msgs.add("下属网点不存在！");
        return -1;
      }
      else {
        SUBBRHIDs = "'" + SUBBRHIDs.replaceAll(",", "','") + "'";
      }
      //sqlWhereUtil
      if (reffldnm.equals("APPBRHID")) {
        sqlWhereUtil.addWhereField("SCBRANCH", "BRHID", SUBBRHIDs,
                                   SqlWhereUtil.DataType_Sql,
                                   sqlWhereUtil.OperatorType_In);
      }
      if (reffldnm.equals("CLIENTMGR")) {
        sqlWhereUtil.addWhereField("SCUSER", "BRHID", SUBBRHIDs,
                                   SqlWhereUtil.DataType_Sql,
                                   sqlWhereUtil.OperatorType_In,
                                   sqlWhereUtil.RelationOperator_And);
        sqlWhereUtil.addWhereField("SCUSER", "USERTYPE", "3",
//                                   SqlWhereUtil.DataType_Number,
                                   SqlWhereUtil.DataType_Character,
                                   sqlWhereUtil.OperatorType_Not_Equals);
      }
      if (reffldnm.equals("UNIONNO")) {
        sqlWhereUtil.addWhereField("CMFAMILYUNION", "BRHID", SUBBRHIDs,
                                   SqlWhereUtil.DataType_Sql,
                                   sqlWhereUtil.OperatorType_In);
      }

      if (reffldnm.trim().equals("CLIENTNO")) {
        sqlWhereUtil.addWhereField("CMCLIENT", "APPBRHID", SUBBRHIDs,
                                   SqlWhereUtil.DataType_Sql,
                                   sqlWhereUtil.OperatorType_In);
        BMTableData data = BMTable.getBMTable(this.bmNo);
        sqlWhereUtil.addWhereField("CMCLIENT", "CLIENTNO", data.clientNo,
                                   SqlWhereUtil.DataType_Character,
                                   sqlWhereUtil.OperatorType_Not_Equals);

      }
      else {    }

    }
    return 0;
  }

}
