package zt.cms.bm.app;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 * modifed by yusg 2004/04/26
 */
import java.util.logging.*;

import zt.cmsi.biz.*;
import zt.cmsi.client.*;
import zt.cmsi.mydb.*;
import zt.cmsi.pub.*;
import zt.cmsi.pub.define.*;
import zt.platform.db.*;
import zt.platform.form.config.*;
import zt.platform.form.control.*;
import zt.platform.form.util.*;
import zt.platform.form.util.event.*;
import zt.platform.user.*;
import java.math.*;
import zt.platform.utils.*;
import zt.cmsi.pub.cenum.EnumValue;

public class BMAppLoanContinuation
    extends FormActions {
  public static Logger logger = Logger.getLogger(
      "zt.cms.bm.app.BMAppLoanContinuation");

  private String flag = null; //窗体是否可读
  private String BMNo = null; //业务号
  private String BMTransNo = null; //业务明细号
  private String OrigBMNo = null; //原业务号
  private String ClientNo = null; //客户编号
  private String ClientName = null; //客户名称
  private String BrhID = null; //业务网点
  private String OPERATOR = null; //业务员
  private String AdminedBy = null; //清收人
  private Param param = null; //参数封装对象，用于传递
  private String TYPENO = null; //业务类型
  private Integer period = null;

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
    BMTransNo = (tmp == null) ? null :
        param.getParam(ParamName.BMTransNo).toString();

    if (ctx.getRequestAttribute("isJustWeihu") == null) {
      if (flag.equals("write") && (BMTransNo == null || BMTransNo.length() < 1)) {
        msgs.add("参数错误，业务明细号不存在！");
        return -1;
      }
    }
    else {
      this.isJustWeihu = true;
    }

    //BrhID
    BrhID = (String) param.getParam(ParamName.BrhID);
    if (BrhID == null || BrhID.length() < 1) {
      //msgs.add("参数错误，业务网点不存在！");
      //return -1;
      BrhID = "";
    }
    //OPERATOR
    UserManager um = (UserManager) ctx.getAttribute(SystemAttributeNames.
        USER_INFO_NAME);
    OPERATOR = um.getUserName();
    if (OPERATOR == null || OPERATOR.length() < 1) {
      msgs.add("系统错误，获取业务操作员信息失败！");
      return -1;
    }
    //原业务号
    UpToDateApp bmtblappdata = BMTable.getUpToDateApp(BMNo);
    OrigBMNo = bmtblappdata.origBMNo;
    if (OrigBMNo == null || OrigBMNo.length() < 1) {
      msgs.add("系统错误，原业务号不存在！");
      return -1;
    }
    //客户名称
    BMTableData bmtbldata = BMTable.getBMTable(BMNo);
    ClientNo = bmtbldata.clientNo;
    ClientName = bmtbldata.clientName;
    if (ClientName == null || ClientName.length() < 1) {
      msgs.add("系统错误，客户名称不存在！");
      return -1;
    }
    //BMSTATUS
    if (flag.equals("write") &&
        bmtbldata.bmStatus == zt.cmsi.pub.cenum.EnumValue.BMStatus_QuXiao) {
      msgs.add("对不起，该笔业务已经被取消，请手工刷新工作台！");
      return -1;
    }

    //按照传入的业务号在详细信息登记表中中查找记录,如果找到,则显示此记录,没有则进入新增加状态。
    String formid = instance.getFormid();
    instance.useCloneFormBean();
    FormBean formBean = instance.getFormBean();
    String tblName = formBean.getTbl().trim();
    if (DBUtil.getCellValue(conn, tblName, "BMNO", "BMNO='" + BMNo + "'") != null) {
      instance.setValue("BMNO", BMNo);
      trigger(manager, instance, EventType.EDIT_VIEW_EVENT_TYPE,
              Event.BRANCH_CONTINUE_TYPE);
    }
    //SYSBTN_CANCEL
    if (instance.isReadonly()) {
      ElementBean ebx = formBean.getElement("SYSBTN_CANCEL");
      ebx.setComponetTp(6);
    }
    return 0;
  }

  /**
   * 扩展基类的beforeInsert方法，点击“添加”按钮后响应的事件
   */
  public int beforeInsert(SessionContext ctx, DatabaseConnection conn,
                          FormInstance instance, ErrorMessages msgs,
                          EventManager manager) {
    instance.setValue("BMNO", BMNo);
    instance.setValue("APPDATE", SystemDate.getSystemDate5(""));
    instance.setValue("CLIENTNAME", ClientName);
    instance.setValue("BRHID", BrhID);
    instance.setValue("OPERATOR", OPERATOR);
    //BMTableApp中获得登记借款金额，申请放款日，申请还款日。
    UpToDateApp bmtblappdata = BMTable.getUpToDateApp(BMNo);
    instance.setValue("APPAMT", bmtblappdata.appAmt.toString());
    instance.setValue("APPSTARTDATE",
                      util.calToString(bmtblappdata.appStartDate));
    instance.setValue("APPENDDATE", util.calToString(bmtblappdata.appEndDate));
    //根据原业务号从贷款台帐(RQLoanLedger)中获得币种, 原合同号, 原帐号, 原借据号码, 建帐金额, 余额
    LoanLedger RQLoanLedger = LoanLedgerMan.getLoanLedger(OrigBMNo);
    instance.setValue("CURNO", RQLoanLedger.curNo); //币种
    instance.setValue("ORIGCONTRACTNO", RQLoanLedger.contractNo); //原合同号
    instance.setValue("ORIGACCOUNTNO", RQLoanLedger.actNo); //原帐号
    instance.setValue("ORIGDUEBILLNO", RQLoanLedger.cnlNo); //原借据号码
    instance.setValue("LEDGERAMT", RQLoanLedger.contractAMt.toString()); //建帐金额(合同金额)
    instance.setValue("APPAMT", RQLoanLedger.nowBal.toString()); //余额
    this.period = RQLoanLedger.perimon;

    return 0;
  }

  /**
   * 扩展基类的preInsert方法，对实际业务做insert前的处理
   */
  public int preInsert(SessionContext ctx, DatabaseConnection conn,
                       FormInstance instance, ErrorMessages msgs,
                       EventManager manager, SqlAssistor assistor) {
    String appmonths, chgtype, frate;
    chgtype = ctx.getParameter("CHGTYPE");
    appmonths = ctx.getParameter("APPMONTHS");
    frate = ctx.getParameter("FRATE");
    //System.out.println("---------------------" + chgtype + appmonths + frate);
    if (chgtype != null) {
      if (chgtype.compareToIgnoreCase("RATECHG") == 0) {
        if (appmonths != null && frate != null && appmonths.length() > 0 &&
            frate.length() > 0) {
          int appMonthInt;
          BigDecimal fratef = null;
          try {
            appMonthInt = Integer.parseInt(appmonths);
            fratef = new BigDecimal(frate);
//        modify by sdj
            fratef = fratef.divide(new BigDecimal(100), 4,
                                   BigDecimal.ROUND_CEILING);
          }
          catch (Exception e) {
            Debug.debug(e);
            msgs.add("申请期限或浮动利率数据格式错误！");
            return -1;
          }

          if(this.period != null) appMonthInt += period.intValue();
          System.out.println("---------------------" + appMonthInt);

          BMBaseInst baseint = BMBaseInst.getBaseInst(EnumValue.BMType_ZhanQi,appMonthInt);
          if (baseint == null) {
            msgs.add("未取得基准利率设置，请检查系统的基准利率设置！");
            return -1;
          }

          //System.out.println("baseint ===================" + baseint.interest);

          instance.setValue("BRATE", "" + baseint.interest);
          BigDecimal finalrate = baseint.interest.multiply(fratef.add(new
              BigDecimal(1)));
          instance.setValue("RATE", "" + finalrate);
          msgs.add("已经获得基准利率：成功.请继续输入.");
          return -1;
        }
        else {
          msgs.add("请继续输入...");
          return -1;
        }
      }
    }

    assistor.setSqlFieldValue(assistor.getDefaultTbl(), "BMNO", BMNo);
    int LOANTYPE3 = instance.getIntValue("LOANTYPE3");
    int appPlus = getAppPlus(BMNo, LOANTYPE3, conn);
    if (appPlus < 1) {
      if (LOANTYPE3 == 210) {
        msgs.add("担保方式为保证，保证人信息不可以为空！");
        return -1;
      }
      if (LOANTYPE3 == 220) {
        msgs.add("担保方式为抵押，抵押物信息不可以为空！");
        return -1;
      }
      if (LOANTYPE3 == 230) {
        msgs.add("担保方式为质押，质押物信息不可以为空！");
        return -1;
      }
    }

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
      if (finish.trim().equals("1") && (!isJustWeihu)) {
        flg = BMTrans.compltTrans(BMNo, Integer.parseInt(BMTransNo), OPERATOR);
        if (flg >= 0) {
          ctx.setRequestAtrribute("title", "贷款业务详细信息登记");
          ctx.setRequestAtrribute("msg", "信息保存成功，并提交到第三级审批！");
          ctx.setRequestAtrribute("flag", "1");
          ctx.setTarget("/showinfo.jsp");
        }
      }
      else {
        ctx.setRequestAtrribute("title", "贷款业务详细信息登记");
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
   * 扩展基类的postFindOk方法，对实际业务做find成功后的处理
   */
  public int postFindOk(SessionContext ctx, DatabaseConnection conn,
                        FormInstance instance, ErrorMessages msgs,
                        EventManager manager, RecordSet result) {
    instance.setValue("CLIENTNAME", ClientName);
    instance.setValue("ADMINEDBY", AdminedBy);
    return 0;
  }

  /**
   * 扩展基类的beforeEdit方法，点击“添加”按钮后响应的事件
   */
  public int beforeEdit(SessionContext ctx, DatabaseConnection conn,
                        FormInstance instance, ErrorMessages msgs,
                        EventManager manager, SqlAssistor assistor) {
    instance.setValue("CLIENTNAME", ClientName);
    return 0;
  }

  /**
   * 扩展基类的preEdit方法，对实际业务做edit前的处理
   */
  public int preEdit(SessionContext ctx, DatabaseConnection conn,
                     FormInstance instance, ErrorMessages msgs,
                     EventManager manager, SqlAssistor assistor) {
    assistor.setSqlFieldValue(assistor.getDefaultTbl(), "BMNO", BMNo);
    assistor.setSqlFieldValue(assistor.getDefaultTbl(), "OPERATOR", OPERATOR);

    int LOANTYPE3 = instance.getIntValue("LOANTYPE3");
    int appPlus = getAppPlus(BMNo, LOANTYPE3, conn);
    if (appPlus < 1) {
      if (LOANTYPE3 == 210) {
        msgs.add("担保方式为保证，保证人信息不可以为空！");
        return -1;
      }
      if (LOANTYPE3 == 220) {
        msgs.add("担保方式为抵押，抵押物信息不可以为空！");
        return -1;
      }
      if (LOANTYPE3 == 230) {
        msgs.add("担保方式为质押，质押物信息不可以为空！");
        return -1;
      }
    }

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
      if (finish.trim().equals("1") && (!isJustWeihu)) {
        flg = BMTrans.compltTrans(BMNo, Integer.parseInt(BMTransNo), OPERATOR);
        if (flg >= 0) {
          ctx.setRequestAtrribute("title", "贷款业务详细信息登记");
          ctx.setRequestAtrribute("msg", "信息保存成功，并提交到第三级审批！");
          ctx.setRequestAtrribute("flag", "1");
          ctx.setTarget("/showinfo.jsp");
        }
      }
      else {
        ctx.setRequestAtrribute("title", "贷款业务详细信息登记");
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
    else if (button.equals("BTN_DZDJ")) {
      trigger(manager, "BMPLDGPDASSETLIST", null);
    }
    //显示客户信息
    else if (button.equals("BTN_CLIENT")) {
      ctx.setRequestAtrribute("CLIENTNO", ClientNo);
      CMClient cc = CMClientMan.getCMClient(ClientNo);
      boolean isIndv = cc.ifIndv;
      if (isIndv) {
        trigger(manager, "100001", null);
      }
      else {
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

  /**
   * 根据不同的担保方式获取是否存在实际担保的内容
   * -1：业务号（p_bmno）参数错误
   * -2：担保类型（p_loantype3）参数错误
   * -3：数据库异常
   */
  public int getAppPlus(String p_bmno, int p_loantype3,
                        DatabaseConnection p_conn) {
    if (p_bmno == null || p_bmno.trim().length() < 1)return -1;
    String sql = "select count(*) from ";
    String sql_from_where = null;
    //信用担保不需要担保
    if (p_loantype3 == 100) {
      return 0;
    }
    //保证担保
    else if (p_loantype3 == 210) {
      sql_from_where = "BMPLDGSECURITY where BMNO='" + p_bmno + "'";
    }
    //抵押担保
    else if (p_loantype3 == 220) {
      sql_from_where = "BMPLDGMORT where BMNO='" + p_bmno +
          "' and PLDGMORTTYPE=1";
    }
    //质押担保
    else if (p_loantype3 == 230) {
      sql_from_where = "BMPLDGMORT where BMNO='" + p_bmno +
          "' and PLDGMORTTYPE=5";
    }
    //
    if (sql_from_where == null)return -2;
    sql += sql_from_where;
    try {
      RecordSet rs = p_conn.executeQuery(sql);
      int tmp = 0;
      if (rs.next()) {
        tmp = rs.getInt(0);
      }
      rs.close();
      return tmp;
    }
    catch (Exception ex) {
      return -3;
    }
  }

}
