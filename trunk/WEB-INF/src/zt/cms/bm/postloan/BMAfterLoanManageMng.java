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
      msgs.add("�Բ���û�в�ѯ���κμ�¼��");
      return -1;
    }

    return 0;
  }

  public int buttonEvent(SessionContext ctx, DatabaseConnection conn, FormInstance instance,
                         String button,
                         ErrorMessages msgs, EventManager manager) {
    if (button == null || button.equals("")) {
      msgs.clear();
      msgs.add("����ķ��񲻴��ڣ�");
      return -1;
    }
    if (strbmno == null || strbmno.equals("")) {
      msgs.clear();
      msgs.add("��ҵ��Ÿ�ʽ�Ƿ���");
      return -1;
    }

    if (strFlag == null || strFlag.equals("")) {
      msgs.clear();
      msgs.add("��д��ʾ��ʽ�Ƿ���");
      return -1;
    }

    Param pm = new Param();
    pm.addParam(ParamName.BMNo, strbmno);
    pm.addParam(ParamName.Flag, strFlag);

    ctx.setRequestAtrribute(ParamName.ParamName, pm);

    if (button.equals("AFTERLOANCHECK")) { //������
      this.trigger(manager, "BMPLC1", null);
    }
    else if (button.equals("LOANHANDANDTAKE")) { //�����
      this.trigger(manager, "BMLRC0", null);
      return 0;
    }
    else if (button.equals("TIEUPSTATUSADJ")) { //ռ����̬����
      this.trigger(manager, "BMLTC0", null);
      return 0;
    }
    else if (button.equals("LOANHATHISSEARCH")) { //�������ʷ��ѯ
      this.trigger(manager, "BMLRC1", null);
      return 0;
    }else if (button.equals("PAYMENTORDER")) {
      trigger(manager, "BMILPAYMENTORDERLIST", null);
    }else if (button.equals("PROSECUTION")) {
      trigger(manager, "BMILPROSECUTIONLIST", null);
    }


    else if (button.equals("LOANATTERMNOTICE")) { //�����֪ͨ��
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
    else if (button.equals("TIEUPSTADJHIS")) { //ռ����̬������ʷ��ѯ
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
