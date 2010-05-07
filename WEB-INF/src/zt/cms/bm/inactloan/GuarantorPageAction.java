package zt.cms.bm.inactloan;

import java.util.logging.*;

import zt.cms.bm.appplus.*;
import zt.cms.bm.common.*;
import zt.cms.cm.common.RightChecker;
import zt.cms.pub.code.*;
import zt.cmsi.client.*;
import zt.cmsi.pub.*;
import zt.platform.db.*;
import zt.platform.form.config.*;
import zt.platform.form.control.*;
import zt.platform.form.util.*;
import zt.platform.form.util.event.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class GuarantorPageAction extends zt.cms.bm.appplus.CommonPageAction {
  private static Logger logger = Logger.getLogger("zt.cms.bm.appplus.BMPldgSecurityPageAction");
  private String PLEDGENO = null; //流水号
  private String ClientNo = null; //保证人客户编号
  private String ID = null; //保证人证件号码

  public String getTableName() {
    return "BMGUARANTOR";
  }

  public int load(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                  EventManager manager, String parameter) {
    RightChecker.checkReadonly(ctx, conn, instance);
    Param param = (Param) ctx.getRequestAttribute("BMPARAM");
    this.bmNo = param.getBmNo();
    PLEDGENO = ctx.getParameter("SEQNO");
    instance.useCloneFormBean();
    if (PLEDGENO != null && !PLEDGENO.equals("")) {
      FormBean fb = instance.getFormBean();
      ElementBean eb1 = fb.getElement("IDTYPE");
      ElementBean eb2 = fb.getElement("CREDITCLASS");
      eb1.setComponetTp(1);
      eb2.setComponetTp(1);
      instance.setValue("SEQNO", ctx.getParameter("SEQNO"));
      trigger(manager, instance, EventType.EDIT_VIEW_EVENT_TYPE, Event.BRANCH_CONTINUE_TYPE);
    }
    return 0;
  }

  public int preInsert(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                       EventManager manager, SqlAssistor assistor) {
    //pledgeNo
    int pledgeNo = (int) SerialNumber.getNextSN(getTableName(), "SEQNO");
    PLEDGENO = pledgeNo + "";
    instance.setValue("SEQNO", pledgeNo);
    assistor.setSqlFieldValue(assistor.getDefaultTbl(), "SEQNO", pledgeNo + "");
    //operator
    String operator = SessionInfo.getLoginUserName(ctx);
    assistor.setSqlFieldValue(assistor.getDefaultTbl(), "OPERATOR", operator + "");
    //CLIENTNAME,ID,IDTYPE,CREDITCLASS
    ID = ctx.getParameter("ID").trim();
    String ID2 = "";
    if (ID.length() == 18) {
      ID2 = ID.substring(0, 6) + ID.substring(8, 17);
    }
    else if (ID.length() == 15) {
      ID2 = ID.substring(0, 6) + "19" + ID.substring(6, 15);
    }
//    else {
//      msgs.add("个人证件号码长度不正确，请仔细检查您输入的号码！");
//      return -1;
//    }

    String sql = "select * from CMWARRANTOR where id='" + ID + "'";
    RecordSet rs = conn.executeQuery(sql);
    if (rs == null) {
      msgs.add("数据库查询错误，请联系系统管理员！");
      return -1;
    }
    if (!rs.next()) {
      if (ID.length() == 15) {
        rs = conn.executeQuery("select * from CMWARRANTOR where id like '" + ID2 + "%'");
      }
      else if (ID.length() == 18) {
        rs = conn.executeQuery("select * from CMWARRANTOR where id='" + ID2 + "'");
      }
      if (!rs.next()) {
        msgs.add("证件号码为" + ID + "的担保人不存在，请到担保人维护中登记担保人。");
        return -1;
      }
      else {
        ID = ID2;
      }
    }
    assistor.setSqlFieldValue(assistor.getDefaultTbl(), "CLIENTNAME", rs.getString("NAME").trim());
    assistor.setSqlFieldValue(assistor.getDefaultTbl(), "CLIENTNO", rs.getString("CLIENTNO"));
    assistor.setSqlFieldValue(assistor.getDefaultTbl(), "ID", rs.getString("ID"));
    assistor.setSqlFieldValue(assistor.getDefaultTbl(), "IDTYPE", rs.getString("IDTYPE"));
    assistor.setSqlFieldValue(assistor.getDefaultTbl(), "CREDITCLASS", rs.getString("CREDITCLASS"));
    return 0;
  }

  public int postInsertOk(SessionContext ctx, DatabaseConnection conn,
                          FormInstance instance, ErrorMessages msgs,
                          EventManager manager) {
    instance.setValue("SEQNO", PLEDGENO);
    trigger(manager, instance, EventType.EDIT_VIEW_EVENT_TYPE, Event.BRANCH_CONTINUE_TYPE);
    FormBean fb = instance.getFormBean();
    ElementBean eb1 = fb.getElement("IDTYPE");
    ElementBean eb2 = fb.getElement("CREDITCLASS");
    eb1.setComponetTp(1);
    eb2.setComponetTp(1);
    return 0;
  }

  public int preEdit(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                     EventManager manager, SqlAssistor assistor) {
    //operator
    String operator = SessionInfo.getLoginUserName(ctx);
    assistor.setSqlFieldValue(assistor.getDefaultTbl(), "OPERATOR", operator + "");
    //CLIENTNAME,ID,IDTYPE,CREDITCLASS
    ID = ctx.getParameter("ID").trim();
    String ID2 = "";
    if (ID.length() == 18) {
      ID2 = ID.substring(0, 6) + ID.substring(8, 17);
    }
    else if (ID.length() == 15) {
      ID2 = ID.substring(0, 6) + "19" + ID.substring(6, 15);
    }
//    else {
//      msgs.add("个人证件号码长度不正确，请仔细检查您输入的号码！");
//      return -1;
//    }

    String sql = "select * from CMWARRANTOR where id='" + ID + "'";
    RecordSet rs = conn.executeQuery(sql);
    if (rs == null) {
      msgs.add("数据库查询错误，请联系系统管理员！");
      return -1;
    }
    if (!rs.next()) {
      if (ID.length() == 15) {
        rs = conn.executeQuery("select * from CMWARRANTOR where id like '" + ID2 + "%'");
      }
      else if (ID.length() == 18) {
        rs = conn.executeQuery("select * from CMWARRANTOR where id='" + ID2 + "'");
      }
      if (!rs.next()) {
        msgs.add("证件号码为" + ID + "的担保人不存在，请到担保人维护中登记担保人。");
        return -1;
      }
      else {
        ID = ID2;
      }
    }
    assistor.setSqlFieldValue(assistor.getDefaultTbl(), "CLIENTNAME", rs.getString("NAME").trim());
    assistor.setSqlFieldValue(assistor.getDefaultTbl(), "CLIENTNO", rs.getString("CLIENTNO"));
    assistor.setSqlFieldValue(assistor.getDefaultTbl(), "ID", rs.getString("ID"));
    assistor.setSqlFieldValue(assistor.getDefaultTbl(), "IDTYPE", rs.getString("IDTYPE"));
    assistor.setSqlFieldValue(assistor.getDefaultTbl(), "CREDITCLASS", rs.getString("CREDITCLASS"));
    return 0;
  }

  public int postEditOk(SessionContext ctx, DatabaseConnection conn,
                        FormInstance instance, ErrorMessages msgs,
                        EventManager manager) {
    instance.setValue("SEQNO", PLEDGENO);
    trigger(manager, instance, EventType.EDIT_VIEW_EVENT_TYPE, Event.BRANCH_CONTINUE_TYPE);
    return 0;
  }

  public int buttonEvent(SessionContext ctx, DatabaseConnection conn, FormInstance instance, String button,
                         ErrorMessages msgs, EventManager manager) {
    ctx.setRequestAtrribute("CLIENTNO", ClientNo);
    CMClient cc = CMClientMan.getCMClient(ClientNo);
    boolean isIndv = cc.ifIndv;
    if (isIndv) {
      trigger(manager, "100001", null);
    }
    else {
      trigger(manager, "CMCC02", null);
    }
    return 0;
  }

}
