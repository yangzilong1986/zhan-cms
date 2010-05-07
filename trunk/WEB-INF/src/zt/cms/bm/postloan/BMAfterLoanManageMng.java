package zt.cms.bm.postloan;
/*
 *  Copyright (c) 2004 Email: liwei@zhongtian.biz
 *  $Id: BMAfterLoanManageMng.java,v 1.1 2005/06/28 07:00:26 jgo Exp $
 */

import zt.cms.bm.bill.*;
import zt.cms.report.*;
import zt.cmsi.pub.*;
import zt.cmsi.pub.define.*;
import zt.platform.db.*;
import zt.platform.form.control.*;
import zt.platform.form.util.*;
import zt.platform.form.util.event.*;

public class BMAfterLoanManageMng extends FormActions {
  private String strFlag = null;
  private boolean isAccept = false;
  private String strbmno = null;

  public int load(SessionContext ctx, DatabaseConnection conn, FormInstance instance,
                  ErrorMessages msgs,
                  EventManager manager, String parameter) {

    strFlag = String.valueOf(ctx.getRequestAttribute("flag"));
    Param pm = (Param) ctx.getRequestAttribute(ParamName.ParamName);
    strbmno = (String) pm.getParam(ParamName.BMNo);
    strFlag = (String) pm.getParam(ParamName.Flag);
    if (strFlag == null || strFlag.equals("read")) {
      instance.setReadonly(true);
      System.out.println("realonly:true");
    }
    else {
      instance.setReadonly(false);
      System.out.println("realonly:false");
    }
    this.isAccept = Function.isDataExist(conn, "RQLOANLEDGER", "BMNO", strbmno,
                                         Function.STRING_TYPE);
    System.out.println("isAccept:" + isAccept);
    if (isAccept) {
      if (strbmno != null) {
        instance.setValue("BMNO", strbmno);
      }
      if (!instance.isReadonly()) {
        instance.setReadonly(true);
      }
      trigger(manager, instance, EventType.EDIT_VIEW_EVENT_TYPE, Event.BRANCH_CONTINUE_TYPE);
    }
    else {
      msgs.clear();
      msgs.add("对不起，没有查询到任何记录。");
      return -1;
    }

    return 0;
  }

  public int buttonEvent(SessionContext ctx, DatabaseConnection conn, FormInstance instance,
                         String button,
                         ErrorMessages msgs, EventManager manager) {
    if (button == null || button.equals("")) {
      msgs.clear();
      msgs.add("请求的服务不存在！");
      return -1;
    }
    if (strbmno == null || strbmno.equals("")) {
      msgs.clear();
      msgs.add("此业务号格式非法。");
      return -1;
    }

    if (strFlag == null || strFlag.equals("")) {
      msgs.clear();
      msgs.add("读写标示格式非法。");
      return -1;
    }

    Param pm = new Param();
    pm.addParam(ParamName.BMNo, strbmno);
    pm.addParam(ParamName.Flag, strFlag);

    ctx.setRequestAtrribute(ParamName.ParamName, pm);

    if (button.equals("AFTERLOANCHECK")) { //贷后检查
      this.trigger(manager, "BMPLC1", null);
    }
    else if (button.equals("LOANHANDANDTAKE")) { //贷款交接
      this.trigger(manager, "BMLRC0", null);
      return 0;
    }
    else if (button.equals("TIEUPSTATUSADJ")) { //占用形态调整
      this.trigger(manager, "BMLTC0", null);
      return 0;
    }
    else if (button.equals("LOANHATHISSEARCH")) { //贷款交接历史查询
      this.trigger(manager, "BMLRC1", null);
      return 0;
    }else if (button.equals("PAYMENTORDER")) {
      trigger(manager, "BMILPAYMENTORDERLIST", null);
    }else if (button.equals("PROSECUTION")) {
      trigger(manager, "BMILPROSECUTIONLIST", null);
    }


    else if (button.equals("LOANATTERMNOTICE")) { //贷款到期通知书
//      SystemDate sd = new SystemDate();
//      String strSystemDate = sd.getSystemDate5("-");
//      String strsql = Function.getSqlByBeanName(instance);
//      String strBindFile = Function.getBandFile(instance);
//      RepParamObject rpobj = new RepParamObject(strsql, strbmno, strBindFile, strSystemDate);
//      ctx.setRequestAtrribute("REPPARAMOBJECT", rpobj);
//      ctx.setTarget("/jspreport/template.jsp");
      ctx.setRequestAtrribute("BMNO", strbmno);
      ctx.setTarget("/jspreport/bmilnotifi.jsp");
      return 0;
    }
    else if (button.equals("TIEUPSTADJHIS")) { //占用形态调整历史查询
      pm.addParam(ParamName.Flag, "read");
      this.trigger(manager, "BMLTC1", null);
      return 0;
    }
    if (button.equals("NOTIFY")) {
      trigger(manager, "BMILNOTIFILIST", null);
    }

    return 0;
  }
}
