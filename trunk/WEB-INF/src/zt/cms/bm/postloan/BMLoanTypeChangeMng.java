/*
 *  Copyright (c) 2004 Email: liwei@zhongtian.biz
 *  $Id: BMLoanTypeChangeMng.java,v 1.1 2005/06/28 07:00:26 jgo Exp $
 */
package zt.cms.bm.postloan;

import zt.cms.bm.bill.*;
import zt.cmsi.biz.*;
import zt.cmsi.pub.*;
import zt.cmsi.pub.code.*;
import zt.cmsi.pub.define.*;
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
public class BMLoanTypeChangeMng extends FormActions {

//  private String clientno;

  private boolean isAccept = false;
  private String strFlag = null;
  private String strBmNo = null;
  private String strSeqNo = null;
  private Param pm = null;

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
  public int load(SessionContext ctx, DatabaseConnection conn, FormInstance instance,
                  ErrorMessages msgs,
                  EventManager manager, String parameter) {
    pm = (Param) ctx.getRequestAttribute(ParamName.ParamName);
    if (pm != null) {
      strFlag = (String) pm.getParam(ParamName.Flag);
      strBmNo = (String) pm.getParam(ParamName.BMNo);
      if (strBmNo == null || strBmNo.equals("")) {
        msgs.clear();
        msgs.add("��ҵ��Ÿ�ʽ����ȷ��");
        return -1;
      }
      if (strFlag.equals("read") || strFlag == null) {
        instance.setReadonly(true);
      }
      else {
        //ȡĬ��ֵ
      }

      if (pm.ifExist(ParamName.PK2Str)) {
        strSeqNo = (String) pm.getParam(ParamName.PK2Str);
        if (strSeqNo == null || strSeqNo.equals("")) {
          msgs.clear();
          msgs.add("��¼��Ŵ���");
          return -1;
        }
        else {

          this.isAccept = Function.isDataExist(conn, "BMLOANTYPECHANGE", "BMNO", strBmNo,
                                               Function.STRING_TYPE);

          if (this.isAccept) {
            instance.setValue("BMNO", strBmNo);
            instance.setValue("SEQNO", strSeqNo);
            trigger(manager, instance, EventType.EDIT_VIEW_EVENT_TYPE,
                    Event.BRANCH_CONTINUE_TYPE);
          }
          else {
            msgs.clear();
            msgs.add("�Բ���û�в�ѯ���κμ�¼��");
            return -1;
          }
        }
      }
      else {
        trigger(manager, instance, EventType.INSERT_VIEW_EVENT_TYPE,
                Event.BRANCH_CONTINUE_TYPE);
      }
    }
    else {
      msgs.clear();
      msgs.add("û�����������");
      return -1;
    }
    return 0;
  }

  public int beforeInsert(SessionContext ctx, DatabaseConnection conn, FormInstance instance,
                          ErrorMessages msgs,
                          EventManager manager) {
    if (!this.isAccept && strFlag.equals(strFlag)) {
      SystemDate sd = new SystemDate();
      String strcurrdate = sd.getSystemDate5("");
      instance.setValue("CREATEDATE", strcurrdate);
      instance.setFieldReadonly("CREATEDATE", true);

      instance.setValue("BMNO", strBmNo);
      LoanLedgerMan llm = new LoanLedgerMan();
      LoanLedger ll = null;
      if (strBmNo != null) {
        ll = llm.getLoanLedger(strBmNo);
      }
      if (ll == null) {
        msgs.clear();
        msgs.add("û��ȡ������ģ��");
        return -1;
      }
      else {
        String strloanCat2 = ll.loanCat2.toString();
        String strcrtRate = ll.crtRate.toString();

        instance.setValue("OLDLOANCAT2", strloanCat2);
        instance.setValue("OLDRATE", strcrtRate);
      }
      int iseqno = BMLoanTypeChange.getNextNo();
      if (iseqno <= 0) {
        return -1;
      }
      else {
        instance.setValue("SEQNO", iseqno);
      }
      String strUserName = Function.getUserName(ctx);
      if (strUserName != null) {
        instance.setValue("OPERATOR", strUserName);
      }
      else {
        return -1;
      }
    }

    return 0;
  }

  public int buttonEvent(SessionContext ctx, DatabaseConnection conn, FormInstance instance,
                         String button,
                         ErrorMessages msgs, EventManager manager) {

    return 0;
  }
}
