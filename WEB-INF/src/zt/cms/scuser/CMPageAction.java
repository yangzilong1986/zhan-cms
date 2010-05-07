package zt.cms.scuser;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

import zt.cms.bm.common.*;
import zt.cms.pub.*;
import zt.cms.pub.code.*;
import zt.platform.db.*;
import zt.platform.form.control.*;
import zt.platform.form.util.*;
import zt.platform.form.util.event.*;

public class CMPageAction extends FormActions {
  public int load(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                  EventManager manager, String parameter) {
    RightChecker.checkReadonly(ctx, conn, instance);
    String USERNO = (String) ctx.getParameter("USERNO");
    if (USERNO != null && !USERNO.equals("")) {
      instance.setValue("USERNO", ctx.getParameter("USERNO"));
      trigger(manager, instance, EventType.EDIT_VIEW_EVENT_TYPE,
              Event.BRANCH_CONTINUE_TYPE);
    }
    else {
      trigger(manager, instance, EventType.INSERT_VIEW_EVENT_TYPE,
              Event.BRANCH_CONTINUE_TYPE);
    }
    return 0;
  }

  public int preReference(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                          EventManager manager, String reffldnm, SqlWhereUtil sqlWhereUtil) {
    String SUBBRHIDs = SCBranch.getAllSubBrhAndSelf1(SessionInfo.getLoginBrhId(ctx));
    if (SUBBRHIDs == null || SUBBRHIDs.length() < 1) {
      msgs.add("下属网点不存在！");
      return -1;
    }
    else {
      SUBBRHIDs = "'" + SUBBRHIDs.replaceAll(",", "','") + "'";
      sqlWhereUtil.addWhereField("SCBRANCH", "BRHID", SUBBRHIDs,
                                 SqlWhereUtil.DataType_Sql,
                                 sqlWhereUtil.OperatorType_In,
                                 sqlWhereUtil.RelationOperator_And);
    }
    return 0;
  }

  public int preInsert(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                       EventManager manager, SqlAssistor assistor) {
    String loginName = ctx.getParameter("LOGINNAME");
    RecordSet rs = conn.executeQuery("select * from SCUSER where loginname='" + loginName + "'");
    if (rs.next()) {
      msgs.add("登陆名已存在，请选择其他登陆名");
      return -1;
    }
    else {

      int seqno = (int) SerialNumber.getNextSN("SCUSER", "USERNO");
      instance.setValue("USERNO", seqno);
      assistor.setSqlFieldValue(assistor.getDefaultTbl(), "USERNO",
                                seqno + "");
      return 0;
    }
  }

  public int postInsertOk(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                          EventManager manager) {
    SCUser.setDirty(true);
    return 0;
  }

  public int postEditOk(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                        EventManager manager) {
    SCUser.setDirty(true);
    return 0;
  }

  public int postDeleteOk(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                          EventManager manager) {
    SCUser.setDirty(true);
    return 0;
  }

}
