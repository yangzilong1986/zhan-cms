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
import zt.cms.pub.code.*;
import zt.platform.db.*;
import zt.platform.form.control.*;
import zt.platform.form.util.*;
import zt.platform.form.util.event.*;
import zt.cms.pub.*;

public class GuarantorPageAction extends FormActions {
  String id = "";
  String seqno = "";
  public int load(SessionContext ctx, DatabaseConnection conn, FormInstance instance,
                  ErrorMessages msgs,
                  EventManager manager, String parameter) {
    int userNo = SessionInfo.getLoginUserNo(ctx);
String roleStr = "select * from scuserrole where userno="+userNo+" and roleno=1";
RecordSet roleRs = conn.executeQuery(roleStr);
if(roleRs.next()){
  instance.useCloneFormBean();
  instance.getFormBean().setUseDelete(true);
}



    if (ctx.getParameter("SEQNO") != null) {
      instance.setValue("SEQNO", ctx.getParameter("SEQNO"));
      this.seqno = ctx.getParameter("SEQNO");
      trigger(manager, instance, EventType.EDIT_VIEW_EVENT_TYPE,
              Event.BRANCH_CONTINUE_TYPE);
    }
    else {
      trigger(manager, instance, EventType.INSERT_VIEW_EVENT_TYPE, Event.BRANCH_CONTINUE_TYPE);
    }
    return 0;
  }

  public int beforeInsert(SessionContext ctx, DatabaseConnection conn, FormInstance instance,
                          ErrorMessages msgs,
                          EventManager manager) {
    instance.setFieldReadonly("CREDIT", true);
    instance.setFieldReadonly("TOCUSTOMER1", true);
    instance.setFieldReadonly("TOCUSTOMER2", true);
    String operator = SessionInfo.getLoginUserName(ctx);
    instance.setValue("OPERATOR", operator);
    return 0;
  }

  public int preInsert(SessionContext ctx, DatabaseConnection conn,
                       FormInstance instance, ErrorMessages msgs,
                       EventManager manager, SqlAssistor assistor) {
    int seqNo = (int) SerialNumber.getNextSN("CMGUARANTOR", "SEQNO");
    assistor.setSqlFieldValue(assistor.getDefaultTbl(), "SEQNO", seqNo + "");
    String operator = SessionInfo.getLoginUserName(ctx);
    instance.setValue("OPERATOR", operator);
    assistor.setSqlFieldValue(assistor.getDefaultTbl(), "OPERATOR", operator + "");
    if (ctx.getParameter("IDTYPE").trim().equals("1")) {
      String id = ctx.getParameter("ID").trim();
      if (!validId(id, msgs, conn, "-1")) {
        return -1;
      }
    }

    return 0;
  }

  public int beforeEdit(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                        EventManager manager, SqlAssistor assistor) {
    this.id = instance.getStringValue("ID");
    //如果原保证人的维护者和当前操作者不在同一个网点，不能维护
    String opera1=DBUtil.getCellValue(conn,"CMGUARANTOR","OPERATOR","SEQNO="+this.seqno+"");
    String brhid1=SCUser.getBrhId(opera1);
    if(brhid1==null || brhid1.length()<1){
      return 0;
    }
    String opera2=SessionInfo.getLoginUserName(ctx);
    String brhid2=SCUser.getBrhId(opera2);
    if(!brhid1.trim().equals(brhid2.trim())){
      msgs.add("该保证人不在本网点维护范围内！");
      instance.setReadonly(true);
    }
    return 0;
  }

  public int preEdit(SessionContext ctx, DatabaseConnection conn, FormInstance instance,
                     ErrorMessages msgs,
                     EventManager manager, SqlAssistor assistor) {
    String operator = SessionInfo.getLoginUserName(ctx);
    instance.setValue("OPERATOR", operator);
    assistor.setSqlFieldValue(assistor.getDefaultTbl(), "OPERATOR", operator + "");
    if (ctx.getParameter("IDTYPE").trim().equals("1")) {
      String id = ctx.getParameter("ID").trim();
      if (!validId2(id, msgs, conn, ctx.getParameter("SEQNO"))) {
        return -1;
      }
    }
    return 0;
  }

  public int buttonEvent(SessionContext ctx, DatabaseConnection conn, FormInstance instance,
                         String button,
                         ErrorMessages msgs, EventManager manager) {
    RecordSet rs = conn.executeQuery("select * from cmguarantor where seqno=" + this.seqno);
    if (rs.next()) {
      ctx.setRequestAtrribute("GNAME", DBUtil.fromDB(rs.getString("NAME")));
      ctx.setRequestAtrribute("GIDTYPE", rs.getString("IDTYPE"));
      ctx.setRequestAtrribute("GID", rs.getString("ID"));
      ctx.setRequestAtrribute("GCREDITCLASS", rs.getString("CREDITCLASS"));
      ctx.setRequestAtrribute("GOPERATOR", rs.getString("OPERATOR"));
      ctx.setRequestAtrribute("flag", "write");
    }
    else {
      msgs.add("取出信息失败");
      return -1;
    }
    if (button.equals("CREDIT")) {
      String id = rs.getString("ID");
      ctx.setRequestAtrribute("ID", id);
      trigger(manager, "GUARANTORCREDITLIST", null);
      return 0;
    }
    else if (button.equals("TOCUSTOMER1")) {
      trigger(manager, "100001", null);
    }
    else if (button.equals("TOCUSTOMER2")) {
      trigger(manager, "CMCC02", null);
    }
    return 0;
  }

  public boolean validId(String id, ErrorMessages msgs, DatabaseConnection conn, String seqno) {
    String id2 = "";
    if (id.length() == 18) {
      id2 = id.substring(0, 6) + id.substring(8, 17);
    }
    else if (id.length() == 15) {
      id2 = id.substring(0, 6) + "19" + id.substring(6, 15);
    }
    else {
      msgs.add("个人证件号码长度不正确，请仔细检查您输入的号码！");
      return false;
    }
    String tmp = "";
    if (id.length() == 18) {
      tmp = id2;
      id2 = id;
      id = tmp;
    }
//    String str1 = "select * from CMGUARANTOR where id='" + id + "' and seqno<>" + seqno + "";
//    String str2 = "select * from CMGUARANTOR where id like '" + id2 + "%' and seqno<>" + seqno + "";
//    RecordSet rs1 = conn.executeQuery(str1);
//    RecordSet rs2 = conn.executeQuery(str2);
//    if (rs1.next() || rs2.next()) {
//      msgs.add("个人证件号码已经存在，请仔细检查您输入的号码！");
//      return false;
//    }

    String str1 = "select * from CMGUARANTOR where (id='" + id + "' and seqno<>" + seqno +
    ") or ( id like '" + id2 + "%' and seqno<>" + seqno + ")";
    RecordSet rs1 = conn.executeQuery(str1);
    if (rs1.next()) {
      msgs.add("个人证件号码已经存在，请仔细检查您输入的号码！");
      return false;
    }


//    if (DBUtil.getCellValue(conn, "CMCLIENT", "CLIENTNO", "ID='" + id + "'") != null ||
//        DBUtil.getCellValue(conn, "CMCLIENT", "CLIENTNO", "ID LIKE '" + id2 + "%'") != null) {
//      msgs.add("个人证件号码已经在客户表存在，请仔细检查您输入的号码！");
//      return false;
//    }
    String str3 = "select * from cmindvclient where id='" + id + "' or id like '" + id2 + "%'";

    RecordSet rs3 = conn.executeQuery(str3);
    if (rs3.next()) {
      msgs.add("个人证件号码已经在客户表存在，请仔细检查您输入的号码！");
      return false;
    }

    return true;
  }


  public boolean validId2(String id, ErrorMessages msgs, DatabaseConnection conn, String seqno) {
    String id2 = "";
    if (id.length() == 18) {
      id2 = id.substring(0, 6) + id.substring(8, 17);
    }
    else if (id.length() == 15) {
      id2 = id.substring(0, 6) + "19" + id.substring(6, 15);
    }
    else {
      msgs.add("个人证件号码长度不正确，请仔细检查您输入的号码！");
      return false;
    }
    String tmp = "";
    if (id.length() == 18) {
      tmp = id2;
      id2 = id;
      id = tmp;
    }


    String str1 = "select * from CMGUARANTOR where (id='" + id + "' and seqno<>" + seqno +
    ") or ( id like '" + id2 + "%' and seqno<>" + seqno + ")";
    RecordSet rs1 = conn.executeQuery(str1);
    if (rs1.next()) {
      msgs.add("个人证件号码已经存在，请仔细检查您输入的号码！");
      return false;
    }



    return true;
  }

  public int preDelete(SessionContext ctx, DatabaseConnection conn,
                       FormInstance instance, ErrorMessages msgs,
                       EventManager manager, SqlAssistor assistor) {
    RecordSet rs = conn.executeQuery("select * from cmguarantor where seqno=" + this.seqno);
    String id = null;
    if (rs.next()) {
      id = rs.getString("ID");
      String bmStr = "select * from bmguarantor where id='" + id + "'";
      //System.out.println(bmStr);
      RecordSet bmRs = conn.executeQuery(bmStr);
      if (bmRs.next()) {
        msgs.add("还有担保的贷款,不能删除！");
        return -1;
      }
    }
    else {
      msgs.add("取出信息失败");
      return -1;
    }



    return 0;
  }
}
