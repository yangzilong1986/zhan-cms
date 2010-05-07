package zt.cms.bm.app;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
import java.util.logging.*;

import zt.cmsi.biz.*;
import zt.cmsi.mydb.*;
import zt.cmsi.pub.*;
import zt.cmsi.pub.define.*;
import zt.platform.db.*;
import zt.platform.form.config.*;
import zt.platform.form.control.*;
import zt.platform.form.util.*;
import zt.platform.form.util.event.*;
import zt.platform.user.*;
import zt.cmsi.client.*;

public class BMAppInactiveLoan extends FormActions {
  public static Logger logger = Logger.getLogger("zt.cms.bm.app.BMAppInactiveLoan");

  private String flag = null; //窗体是否可读
  private String BMNo = null; //业务号
  private String BMTransNo = null; //业务明细号
  private String OPERATOR = null; //业务员
  private Param param = null; //参数封装对象，用于传递
  private String ClientNo = null; //客户编号
  private String TYPENO=null;//业务类型


 private boolean isJustWeihu = false;
  /**
   * 扩展基类的load方法
   */
  public int load(SessionContext ctx, DatabaseConnection conn,
                  FormInstance instance, ErrorMessages msgs,
                  EventManager manager, String parameter) {
    //BMPARAM
    param = (Param) ctx.getRequestAttribute(ParamName.ParamName);
    if (param == null) {
      param = (Param) ctx.getAttribute(ParamName.ParamName);
    }
    else {
      ctx.setAttribute(ParamName.ParamName, null);
    }
    if (param == null) {
      msgs.add("参数错误，参数对象不存在！");
      return -1;
    }
    //Flag
    flag = (String) param.getParam(ParamName.Flag);
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
    //BMNo
    BMNo = (String) param.getParam(ParamName.BMNo);
    if (BMNo == null || BMNo.length() < 1) {
      msgs.add("参数错误，业务号不存在！");
      return -1;
    }
    //BMTransNo
    Object tmp = param.getParam(ParamName.BMTransNo);
    BMTransNo = (tmp == null) ? null : param.getParam(ParamName.BMTransNo).toString();

    if(ctx.getRequestAttribute("isJustWeihu")==null){
        if (flag.equals("write") && (BMTransNo == null || BMTransNo.length() < 1)) {
            msgs.add("参数错误，业务明细号不存在！");
            return -1;
        }
    }else{
        this.isJustWeihu = true;
    }

    //ClientNo
    BMTableData bmtbldata = BMTable.getBMTable(BMNo);
    ClientNo=bmtbldata.clientNo;
    //OPERATOR
    UserManager um = (UserManager) ctx.getAttribute(SystemAttributeNames.USER_INFO_NAME);
    OPERATOR = um.getUserName();
    if (OPERATOR == null || OPERATOR.length() < 1) {
      msgs.add("系统错误，获取业务操作员信息失败！");
      return -1;
    }
    //按照传入的业务号在详细信息登记表中中查找记录,如果找到,则显示此记录,没有则进入新增加状态。
    String formid = instance.getFormid();
    instance.useCloneFormBean();
    FormBean formBean = instance.getFormBean();
    String tblName = formBean.getTbl().trim();
    if (DBUtil.getCellValue(conn, tblName, "BMNO", "BMNO='" + BMNo + "'") != null) {
      instance.setValue("BMNO", BMNo);
      trigger(manager, instance, EventType.EDIT_VIEW_EVENT_TYPE, Event.BRANCH_CONTINUE_TYPE);
    }
    //SYSBTN_CANCEL
    if (instance.isReadonly()) {
      ElementBean ebx=formBean.getElement("SYSBTN_CANCEL");
      ebx.setComponetTp(6);
    }

    if (instance.getFormBean().getElement("CREDITCLASS") != null) {
  ElementBean creditClass = instance.getFormBean().getElement("CREDITCLASS");
  creditClass.setEnutpname("CreditClass");
  RecordSet clients = conn.executeQuery("select creditclass from cmindvclient where clientno='" + ClientNo + "'");
  if (clients.next()) {
    instance.setValue("CREDITCLASS", clients.getInt("CREDITCLASS"));
  }
  else {
    clients = conn.executeQuery("select creditclass from cmcorpclient where clientno='" + ClientNo + "'");
    if (clients.next()) {
      instance.setValue("CREDITCLASS", clients.getInt("CREDITCLASS"));
    }else{
      msgs.add("得不到客户信用等级信息！");
      return -1;
    }
  }
}

    return 0;
  }

  /**
   * 扩展基类的beforeInsert方法，点击“添加”按钮后响应的事件
   */
  public int beforeInsert(SessionContext ctx, DatabaseConnection conn,
                          FormInstance instance, ErrorMessages msgs,
                          EventManager manager) {
    //BrhID
    String BrhID = (String) param.getParam(ParamName.BrhID);
    if (BrhID == null || BrhID.length() < 1) {
      BrhID="";
    }
    instance.setValue("BMNO", BMNo);
    instance.setValue("APPDATE", SystemDate.getSystemDate5(""));
    instance.setValue("OPERATOR", OPERATOR);
    instance.setValue("BRHID", BrhID);
    //不良核销
    TYPENO=DBUtil.getCellValue(conn, "BMTABLE", "TYPENO", "BMNO='" + BMNo + "'");
    if(TYPENO.equals("17")){
      //原业务号
      UpToDateApp bmtblappdata = BMTable.getUpToDateApp(BMNo);
      String OrigBMNo=bmtblappdata.origBMNo;
      if (OrigBMNo == null || OrigBMNo.length() < 1) {
        msgs.add("系统错误，原业务号不存在！");
        return -1;
      }
      instance.setValue("ORIGBMNO", OrigBMNo);
      //客户名称
      BMTableData bmtbldata = BMTable.getBMTable(BMNo);
      String ClientName = bmtbldata.clientName;
      if (ClientName == null || ClientName.length() < 1) {
        msgs.add("系统错误，客户名称不存在！");
        return -1;
      }
      instance.setValue("CLIENTNAME", ClientName);
      //不良贷款台帐(bminactloan)中获得清收人
      InactLoan bminactloan = InactLoanMan.getInactLoan(OrigBMNo);
      String AdminedBy=bminactloan.adminedBy;
      if (AdminedBy == null || AdminedBy.length() < 1) {
        msgs.add("系统错误，中获得清收人不存在！");
        return -1;
      }
      instance.setValue("ADMINEDBY", AdminedBy); //清收人
      //根据原业务号从贷款台帐(RQLoanLedger)中获得币种, 原合同号, 原帐号, 原借据号码, 建帐金额, 当前余额
      LoanLedger RQLoanLedger = LoanLedgerMan.getLoanLedger(OrigBMNo);
      instance.setValue("CURNO", RQLoanLedger.curNo); //币种
      instance.setValue("ORIGCONTRACTNO", RQLoanLedger.contractNo); //原合同号
      instance.setValue("ORIGACCOUNTNO", RQLoanLedger.actNo); //原帐号
      instance.setValue("ORIGDUEBILLNO", RQLoanLedger.cnlNo); //原借据号码
      instance.setValue("LEDGERAMT", RQLoanLedger.contractAMt.toString()); //建帐金额(合同金额)
      instance.setValue("BAL", RQLoanLedger.nowBal.toString()); //当前余额
    }
    return 0;
  }

  /**
   * 扩展基类的preInsert方法，对实际业务做insert前的处理
   */
  public int preInsert(SessionContext ctx, DatabaseConnection conn,
                       FormInstance instance, ErrorMessages msgs,
                       EventManager manager, SqlAssistor assistor) {
    assistor.setSqlFieldValue(assistor.getDefaultTbl(), "BMNO", BMNo);
    return 0;
  }

  /**
   * 扩展基类的postInsertOk方法，对实际业务做insert后的处理
   */
  public int postInsertOk(SessionContext ctx, DatabaseConnection conn,
                          FormInstance instance, ErrorMessages msgs,
                          EventManager manager) {
    String finish = (String) ctx.getParameter("finish");
    if (finish == null || finish.length() < 1) {
      msgs.add("系统错误，是否完成标志为空！");
      return -1;
    }
    try {
      MyDB.getInstance().addDBConn(conn);
      int flg = 0;
      if (finish.trim().equals("1") && !isJustWeihu) {
        flg = BMTrans.compltTrans(BMNo, Integer.parseInt(BMTransNo), OPERATOR);
        if (flg >= 0) {
          ctx.setRequestAtrribute("title", "业务登记详细信息登记");
          ctx.setRequestAtrribute("msg", "信息保存成功，并提交到第三级审批！");
          ctx.setRequestAtrribute("flag", "1");
          ctx.setTarget("/showinfo.jsp");
        }
      }
      else {
        ctx.setRequestAtrribute("title", "业务登记详细信息登记");
        ctx.setRequestAtrribute("msg", "信息保存成功！");
        ctx.setRequestAtrribute("flag", "1");
        ctx.setTarget("/showinfo.jsp");
      }

      return flg;
    }
    catch (Exception ex) {
      msgs.add("未知原因导致的系统错误！");
      return -2;
    }
    finally {
      MyDB.getInstance().releaseDBConn();
    }
  }

  /**
   * 扩展基类的preEdit方法，对实际业务做edit前的处理
   */
  public int preEdit(SessionContext ctx, DatabaseConnection conn,
                     FormInstance instance, ErrorMessages msgs,
                     EventManager manager, SqlAssistor assistor) {
    assistor.setSqlFieldValue(assistor.getDefaultTbl(), "BMNO", BMNo);
    assistor.setSqlFieldValue(assistor.getDefaultTbl(), "OPERATOR", OPERATOR);
    return 0;
  }

  /**
   * 扩展基类的postEditOk方法，对实际业务做edit后的处理
   */
  public int postEditOk(SessionContext ctx, DatabaseConnection conn,
                        FormInstance instance, ErrorMessages msgs,
                        EventManager manager) {
    String finish = (String) ctx.getParameter("finish");
    if (finish == null || finish.length() < 1) {
      msgs.add("系统错误，是否完成标志为空！");
      return -1;
    }
    try {
      MyDB.getInstance().addDBConn(conn);
      int flg = 0;
      if (finish.trim().equals("1") && !isJustWeihu) {
        flg = BMTrans.compltTrans(BMNo, Integer.parseInt(BMTransNo), OPERATOR);
        if (flg >= 0) {
          ctx.setRequestAtrribute("title", "业务登记详细信息登记");
          ctx.setRequestAtrribute("msg", "信息保存成功，并提交到第三级审批！");
          ctx.setRequestAtrribute("flag", "1");
          ctx.setTarget("/showinfo.jsp");
        }
      }
      else {
        ctx.setRequestAtrribute("title", "业务登记详细信息登记");
        ctx.setRequestAtrribute("msg", "信息保存成功！");
        ctx.setRequestAtrribute("flag", "1");
        ctx.setTarget("/showinfo.jsp");
      }
      return flg;
    }
    catch (Exception ex) {
      msgs.add("未知原因导致的系统错误！");
      return -2;
    }
    finally {
      MyDB.getInstance().releaseDBConn();
    }
  }

  /**
   * 扩展基类的buttonEvent方法，响应自定义按钮的点击事件
   */
  public int buttonEvent(SessionContext ctx, DatabaseConnection conn,
                         FormInstance instance, String button,
                         ErrorMessages msgs, EventManager manager) {
    if (BMNo == null || BMNo.length() < 1) {
      msgs.add("业务尚未登记！");
      return -1;
    }

    ctx.setRequestAtrribute(ParamName.ParamName, param);

    if (button.equals("BTN_DYDJ")) {
      trigger(manager, "BMPLEDGELIST", null);
    }
    else if (button.equals("BTN_DBDJ")) {
      trigger(manager, "BMPLDGSECURITYLIST", null);
    }
    else if (button.equals("BTN_TXDJ")) {
      trigger(manager, "BMPLDGBILLDISLIST", null);
    }
    else if (button.equals("BTN_ZTXDJ")) {
      trigger(manager, "BMPLDGBILLREDISLIST", null);
    }
    else if (button.equals("BTN_BLDJ")) {
      trigger(manager, "BMPLDGLLL", null);
    }
    else if (button.equals("BTN_DZDJ")) {
      trigger(manager, "BMPLDGPDASSETLIST", null);
    }
    //显示客户信息
    else if(button.equals("BTN_CLIENT")){
      ctx.setRequestAtrribute("CLIENTNO",ClientNo);
      CMClient cc=CMClientMan.getCMClient(ClientNo);
      boolean isIndv=cc.ifIndv;
      if(isIndv){
        trigger(manager, "100001", null);
      }
      else{
        trigger(manager, "CMCC02", null);
      }
    }
    else if (button.equals("SYSBTN_CANCEL")) {
      if (instance.isReadonly()) {
        ctx.setRequestAtrribute("title", "贷款业务详细信息登记");
        ctx.setRequestAtrribute("msg", "查看历史审批记录中状态下不能取消登记，请到工作台办理！");
        ctx.setRequestAtrribute("flag", "1");
        ctx.setTarget("/showinfo.jsp");
      }
      else {
        ctx.setRequestAtrribute("BMNo", BMNo);
        ctx.setRequestAtrribute("BMTransNo", BMTransNo);
        ctx.setRequestAtrribute("OPERATOR", OPERATOR);
        ctx.setTarget("/jsp/bm/bmappcancle.jsp");
      }
    }
    return 0;
  }

}
