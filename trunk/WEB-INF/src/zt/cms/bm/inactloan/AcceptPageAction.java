package zt.cms.bm.inactloan;

import zt.cms.bm.common.*;
import zt.cms.pub.*;
import zt.cmsi.biz.*;
import zt.cmsi.pub.*;
import zt.platform.db.*;
import zt.platform.form.control.*;
import zt.platform.form.util.*;
import zt.platform.form.util.event.*;
import zt.platform.form.config.*;
/**
 *  Description of the Class
 *
 *@author     Administrator
 *@created    2004��1��5��
 */
public class AcceptPageAction extends FormActions {
  /**
   *  Constructor for the AcceptPageAction object
   */
  public AcceptPageAction() {}

  String bmno = "";

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


    this.bmno = (String) ctx.getParameter("BMNO");
    String bmNo = (String) ctx.getParameter("BMNO");
    System.out.println("BMNO:" + bmNo);
    if (bmNo != null && !bmNo.equals("")) {
      instance.setValue("BMNO", ctx.getParameter("BMNO"));
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
    return 0;
  }

  public int beforeEdit(SessionContext ctx, DatabaseConnection conn,
                        FormInstance instance, ErrorMessages msgs,
                        EventManager manager, SqlAssistor assistor) {
    UpToDateApp data = BMTable.getUpToDateApp(this.bmno);
    String str = "select clientname from bmtable where bmno='" + this.bmno + "'";
    RecordSet rs = conn.executeQuery(str);
    String clientName = "";
    if (rs.next()) {
      clientName = DBUtil.fromDB(rs.getString("clientname"));
    }
    instance.setValue("CLIENTNAME", clientName);
    instance.setValue("DECIDEDBY", data.decidedBy);
    instance.setValue("CLIENTMGR", data.clientMgr);
    int ifRespLoan = data.ifRespLoan.intValue();
    if (ifRespLoan == 1) {
      instance.setValue("IFRESPLOAN", "��");
      instance.setValue("FIRSTRESP", data.firstResp);
      instance.setValue("FISRTRESPPCT", data.firstRespPct.toString());
    }
    else {
      instance.setValue("IFRESPLOAN", "��");
    }

    instance.setValue("IFACCEPTED", 1);
    return 0;
  }

  public int preReference(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                          EventManager manager, String reffldnm, SqlWhereUtil sqlWhereUtil) {
    String SUBBRHIDs = SCBranch.getAllSubBrhAndSelf1(SessionInfo.getLoginBrhId(ctx));
    if (SUBBRHIDs == null || SUBBRHIDs.length() < 1) {
      msgs.add("�������㲻���ڣ�");
      return -1;
    }
    else {
      SUBBRHIDs = "'" + SUBBRHIDs.replaceAll(",", "','") + "'";
      sqlWhereUtil.addWhereField("SCUSER", "BRHID", SUBBRHIDs,
                                 SqlWhereUtil.DataType_Sql,
                                 sqlWhereUtil.OperatorType_In,
                                 sqlWhereUtil.RelationOperator_And);
    }
    return 0;
  }

  public int postInsertOk(SessionContext ctx, DatabaseConnection conn,
                          FormInstance instance, ErrorMessages msgs,
                          EventManager manager) {
    String IFACCEPTED=instance.getStringValue("IFACCEPTED");
    ctx.setRequestAtrribute("title", "�����������");
    if(IFACCEPTED.trim().equals("1")){
      ctx.setRequestAtrribute("msg", "������ɣ�<font color='green'>ͬ��</font>���ոñʲ������");
    }
    else{
      ctx.setRequestAtrribute("msg", "������ɣ�<font color='red'>�ܾ�</font>���ոñʲ������");
    }
    ctx.setRequestAtrribute("flag", "1");
    ctx.setTarget("/showinfo.jsp");
    return 0;
  }

  public int postEditOk(SessionContext ctx, DatabaseConnection conn,
                          FormInstance instance, ErrorMessages msgs,
                          EventManager manager) {
      String IFACCEPTED=instance.getStringValue("IFACCEPTED");
      ctx.setRequestAtrribute("title", "�����������");
      if(IFACCEPTED.trim().equals("1")){
        ctx.setRequestAtrribute("msg", "������ɣ�<font color='green'>ͬ��</font>���ոñʲ������");
      }
      else{
        ctx.setRequestAtrribute("msg", "������ɣ�<font color='red'>�ܾ�</font>���ոñʲ������");
      }
      ctx.setRequestAtrribute("flag", "1");
      ctx.setTarget("/showinfo.jsp");
      return 0;
  }

  public int preEdit(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                     EventManager manager, SqlAssistor assistor) {
    String operator = SessionInfo.getLoginUserName(ctx);
    instance.setValue("OPERATOR", operator);
    assistor.setSqlFieldValue(assistor.getDefaultTbl(), "OPERATOR",
                              operator + "");
    return 0;
  }

  public int buttonEvent(SessionContext ctx, DatabaseConnection conn, FormInstance instance, String button,
                         ErrorMessages msgs, EventManager manager) {
    Param param = new Param();
    param.addParam("BMNO", this.bmno);
    if (button.equals("PENALTY")) {
      ctx.setRequestAtrribute("BMPARAM", param);
      trigger(manager, "BMILPENALTYLIST", null);
    }
    else {
      ctx.setRequestAtrribute("BMPARAM", param);
      trigger(manager, "BMGUARANTORLIST", null);
    }
    return 0;
  }

}
