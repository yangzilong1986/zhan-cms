/*
 *  Copyright (c) 2004 Email: liwei@zhongtian.biz
 *  $Id: BMRQLMng.java,v 1.1 2005/06/28 07:00:23 jgo Exp $
 */
package zt.cms.bm.ledger;

import java.util.logging.*;
import zt.cms.bm.bill.*;
import zt.cmsi.pub.*;
import zt.platform.db.*;
import zt.platform.form.control.*;
import zt.platform.form.util.*;
import zt.platform.form.util.event.*;

/**
 *  ���ԣ���������������,�޸ģ�ɾ��(���ֵ)
 *
 *@author     Administrator
 *@created    2003��12��2��
 */
public class BMRQLMng
    extends FormActions {

  private static Logger logger = Logger.getLogger("zt.cms.bm.ledger.BMRQLMng");
  Param pm = null;
  private String strFlag = null;
  private boolean isAccept = false;
  private String strbmno = null;
  private String stractno = null;
  private String strcnlno = null;

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
  public int load(SessionContext ctx, DatabaseConnection conn,
                  FormInstance instance, ErrorMessages msgs,
                  EventManager manager, String parameter) {
    strFlag = String.valueOf(ctx.getRequestAttribute("flag"));
    pm = (Param) ctx.getRequestAttribute(ParamName.ParamName);
    strFlag = (String) pm.getParam(ParamName.Flag);
    strbmno = (String) pm.getParam(ParamName.BMNo);
    stractno = (String) pm.getParam("ACTNO");
    strcnlno = (String) pm.getParam("CNLNO");
    if (strFlag == null || strFlag.equals("read")) {
      instance.setReadonly(true);
    }
    else {
      instance.setReadonly(false);
    }

    String CLIENTNAME=DBUtil.getCellValue(conn,"BMTABLE","CLIENTNAME","BMNO='"+strbmno+"'");
    String ACCNO=DBUtil.getCellValue(conn,"RQLOANLEDGER","ACCNO","BMNO='"+strbmno+"'");
    String ACCNAME=DBUtil.getCellValue(conn,"SCHOSTACC","ACCNAME","ACCNO='"+ACCNO+"'");
    CLIENTNAME=DBUtil.fromDB(CLIENTNAME);
    ACCNAME=DBUtil.fromDB(ACCNAME);
    logger.info(CLIENTNAME);
    logger.info(ACCNAME);
    instance.setValue("CLIENTNAME", CLIENTNAME);
    instance.setValue("ACCNAME", ACCNAME);

    this.isAccept = Function.isDataExist(conn, "RQLOANLEDGER", "BMNO", strbmno,Function.STRING_TYPE);
    logger.info("isAccept:"+isAccept);
    if (isAccept) {
      if (strbmno != null) {
        instance.setValue("BMNO", strbmno.trim());
      }
      if (!instance.isReadonly()) {
        instance.setReadonly(true);
      }
      trigger(manager, instance, EventType.EDIT_VIEW_EVENT_TYPE,Event.BRANCH_CONTINUE_TYPE);
    }
    else {
      if (Function.isDataExist(conn, "RQLOANLEDGER",("where ACTNO='" + stractno + "' and CNLNO='" + strcnlno + "'"))) {
        instance.setValue("ACTNO", stractno);
        instance.setValue("CNLNO", strcnlno);
        trigger(manager, instance, EventType.EDIT_VIEW_EVENT_TYPE,Event.BRANCH_CONTINUE_TYPE);
      }
      else {
        msgs.clear();
        msgs.add("�Բ���û�в�ѯ���κμ�¼��");
        return -1;
      }
    }

    return 0;
  }

  /**
   * ��չ�����postFindOk��������ʵ��ҵ����find�ɹ���Ĵ���
   */
  public int postFindOk(SessionContext ctx, DatabaseConnection conn,
                        FormInstance instance, ErrorMessages msgs,
                        EventManager manager, RecordSet result) {
    return 0;
  }

  /**
   * ��չ�����buttonEvent��������Ӧ�Զ��尴ť�ĵ���¼�
   * Add by wxj at 20040218
   */
  public int buttonEvent(SessionContext ctx, DatabaseConnection conn,
                         FormInstance instance, String button,
                         ErrorMessages msgs, EventManager manager) {
    if (strbmno == null || strbmno.length() < 1) {
      msgs.add("��������ҵ��Ų����ڣ�");
      return -1;
    }

    ctx.setRequestAtrribute(ParamName.ParamName, pm);
    if (button.equals("BTN_HKMX")) {
      trigger(manager, "RQPAYBACKLIST", null);
    }
    else if (button.equals("BTN_DKLX")) {
      trigger(manager, "RQDUEINTRST", null);
    }
    return 0;
  }

}
