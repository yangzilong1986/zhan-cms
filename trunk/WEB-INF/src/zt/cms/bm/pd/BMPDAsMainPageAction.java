package zt.cms.bm.pd;

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
import zt.cmsi.pub.define.*;
import zt.platform.db.*;
import zt.platform.form.config.*;
import zt.platform.form.control.*;
import zt.platform.form.util.*;
import zt.platform.form.util.event.*;
import zt.platform.user.*;
import zt.cms.bm.bill.*;

public class BMPDAsMainPageAction extends FormActions {
  public static Logger logger = Logger.getLogger("zt.cms.bm.pd.BMPDAsMainPageAction");

  private String flag = null; //窗体是否可读
  private String PDMAINNO = null; //抵债资产基本信息业务号
  private String BMNo = null; //业务号
  private String OPERATOR = null; //业务员

  public int load(SessionContext ctx, DatabaseConnection conn,
                  FormInstance instance, ErrorMessages msgs,
                  EventManager manager, String parameter) {
    //Flag
    flag = (String) ctx.getRequestAttribute("flag");
    if (flag == null) {
      flag = "read";
    }
    flag = flag.toLowerCase();
    if (flag.equals("write")) {
      instance.setReadonly(false);
    }
    else {
      instance.setReadonly(true);
    }
    //OPERATOR
    UserManager um = (UserManager) ctx.getAttribute(SystemAttributeNames.USER_INFO_NAME);
    OPERATOR = um.getUserName();
    if (OPERATOR == null || OPERATOR.length() < 1) {
      msgs.add("系统错误，获取业务操作员信息失败！");
      return -1;
    }
    //PDMAINNO
    PDMAINNO = (String) ctx.getParameter("PDMAINNO");
    //修改
    instance.useCloneFormBean();
    if (PDMAINNO != null && PDMAINNO.trim().length() > 0) {
      instance.setValue("PDMAINNO", PDMAINNO);
      trigger(manager, instance, EventType.EDIT_VIEW_EVENT_TYPE, Event.BRANCH_CONTINUE_TYPE);
    }
    else {
      FormBean fb = instance.getFormBean();
      ElementBean eb = fb.getElement("BTN_DZDJ");
      eb.setReadonly(true);
    }
    return 0;
  }

  public int beforeInsert(SessionContext ctx, DatabaseConnection conn,
                          FormInstance instance, ErrorMessages msgs,
                          EventManager manager) {
    //String BRHID = SCUser.getBrhId(OPERATOR);
    instance.setValue("BMNO", BMNo);
    instance.setValue("APPDATE", SystemDate.getSystemDate5(""));
    instance.setValue("OPERATOR", OPERATOR);
    //instance.setValue("BRHID", BRHID);
    return 0;
  }

  public int preInsert(SessionContext ctx, DatabaseConnection conn,
                       FormInstance instance, ErrorMessages msgs,
                       EventManager manager, SqlAssistor assistor) {
    PDMAINNO = zt.cms.pub.code.SerialNumber.getNextSN("BMPDASMAIN", "PDMAINNO") + "";
    assistor.setSqlFieldValue(assistor.getDefaultTbl(), "PDMAINNO", PDMAINNO);
    return 0;
  }

  public int postInsertOk(SessionContext ctx, DatabaseConnection conn,
                          FormInstance instance, ErrorMessages msgs,
                          EventManager manager) {
    instance.setValue("PDMAINNO", PDMAINNO);
    trigger(manager, instance, EventType.EDIT_VIEW_EVENT_TYPE, Event.BRANCH_CONTINUE_TYPE);
    FormBean fb = instance.getFormBean();
    ElementBean eb = fb.getElement("BTN_DZDJ");
    eb.setReadonly(false);
    return 0;
  }

  public int beforeEdit(SessionContext ctx, DatabaseConnection conn,
                        FormInstance instance, ErrorMessages msgs,
                        EventManager manager, SqlAssistor assistor) {
    //String BRHID = SCUser.getBrhId(OPERATOR);
    //instance.setValue("BRHID", BRHID);
    return 0;
  }

  public int preEdit(SessionContext ctx, DatabaseConnection conn,
                     FormInstance instance, ErrorMessages msgs,
                     EventManager manager, SqlAssistor assistor) {
    assistor.setSqlFieldValue(assistor.getDefaultTbl(), "OPERATOR", OPERATOR);
    return 0;
  }

  public int postEditOk(SessionContext ctx, DatabaseConnection conn,
                        FormInstance instance, ErrorMessages msgs,
                        EventManager manager) {
    instance.setValue("PDMAINNO", PDMAINNO);
    trigger(manager, instance, EventType.EDIT_VIEW_EVENT_TYPE, Event.BRANCH_CONTINUE_TYPE);
    return 0;
  }

  public int preDelete(SessionContext ctx, DatabaseConnection conn, FormInstance instance,
                       ErrorMessages msgs,
                       EventManager manager, SqlAssistor assistor) {
    StringBuffer strBufPdNo = new StringBuffer();
    StringBuffer strBufObjId = new StringBuffer();
    RecordSet rs = null;

    strBufPdNo.append("delete from bmpdtrans where pdno in (");
    strBufObjId.append("delete from glblob where objid in (");

    if (PDMAINNO == null || PDMAINNO == "") {
      return -1;
    }
    else {
      String strSql = "select pdno,photono from bmpdassets where pdmainno='" + PDMAINNO +
        "'";
      rs = conn.executeQuery(strSql);
      while (rs.next()) {
        int pdno = rs.getInt(0);
        int photono = rs.getInt(1);
        strBufPdNo.append(pdno + ",");
        strBufObjId.append(photono + ",");
      }
      strBufPdNo.append("-100)");
      strBufObjId.append("-100)");
      conn.executeUpdate(strBufPdNo.toString()); //删除抵债资产处置情况
      conn.executeUpdate(strBufObjId.toString()); //删除抵债资产图片
      return 0;
    }
  }

  public int postDeleteOk(SessionContext ctx, DatabaseConnection conn, FormInstance instance,
                          ErrorMessages msgs,
                          EventManager manager) {
    return 0;
  }

  public int buttonEvent(SessionContext ctx, DatabaseConnection conn,
                         FormInstance instance, String button,
                         ErrorMessages msgs, EventManager manager) {
    ctx.setRequestAtrribute("PDMAINNO", PDMAINNO);
    if (button.equals("BTN_BLDJ")) {
      trigger(manager, "BMPLDGLLL", null);
    }
    else if (button.equals("BTN_DZDJ")) {
      trigger(manager, "BMPDASSETSLIST", null);
    }
    return 0;
  }

  public int preReference(SessionContext ctx, DatabaseConnection conn,
                          FormInstance instance, ErrorMessages msgs,
                          EventManager manager, String reffldnm,
                          SqlWhereUtil sqlWhereUtil)
  {


      if (reffldnm.equals("BRHID")) {
          String strinsql = Function.getAllSubBrhIds(msgs, ctx);
          sqlWhereUtil.addWhereField("SCBRANCH", "BRHID", strinsql,
                                     SqlWhereUtil.DataType_Sql,
                                     sqlWhereUtil.OperatorType_In);

      }
      return 0;
  }

}
