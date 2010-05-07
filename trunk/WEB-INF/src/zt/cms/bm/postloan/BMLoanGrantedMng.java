package zt.cms.bm.postloan;
/*
 *  ������Ȩ����
 *  $Id: BMLoanGrantedMng.java,v 1.1 2005/06/28 07:00:26 jgo Exp $
 */

import zt.platform.form.config.*;
import zt.cmsi.biz.*;
import zt.cmsi.mydb.*;
import zt.platform.db.*;
import zt.platform.form.control.*;
import zt.platform.form.util.*;
import zt.platform.form.util.event.*;

public class BMLoanGrantedMng extends FormActions {
  private String strAuthorizedStatus=null;
  private String strFlag = null;
  private String strbmno = null;
  String strLoanType = null; //ҵ������

  public int load(SessionContext ctx, DatabaseConnection conn, FormInstance instance,
                  ErrorMessages msgs,
                  EventManager manager, String parameter) {
    strFlag = String.valueOf(ctx.getRequestAttribute("flag"));
    strbmno = ctx.getParameter("BMNO") == null ? null : ctx.getParameter("BMNO").trim();
    strLoanType=DBUtil.getCellValue(conn,"bmtableapp","typeno","bmno='" + strbmno + "'");
    //���ݲ�ͬ����Ȩ״̬����ʾ��ͬ�İ�ť��
    strAuthorizedStatus=DBUtil.getCellValue(conn,"BMLOANGRANTED","AUTHORIZEDSTATUS","bmno='" + strbmno + "'");
    if(strAuthorizedStatus==null){
      msgs.add("���ݴ���");
      return -1;
    }
    strAuthorizedStatus=strAuthorizedStatus.trim();
    instance.useCloneFormBean();
    FormBean fb=instance.getFormBean();
    ElementBean eb1=fb.getElement("AUTHORBUTTON");
    ElementBean eb2=fb.getElement("NONAUTHORBUTTON");
    ElementBean eb3=fb.getElement("CANCELBUTTON");
    if(strAuthorizedStatus.equals("1")){
      eb2.setDisabled(true);
    }
    else if(strAuthorizedStatus.equals("2")){
      eb1.setDisabled(true);
      eb3.setDisabled(true);
    }
    else{
      eb1.setDisabled(true);
      eb2.setDisabled(true);
      eb3.setDisabled(true);
    }
    //����ת��
    instance.setValue("BMNO", strbmno);
    trigger(manager, instance, EventType.EDIT_VIEW_EVENT_TYPE, Event.BRANCH_CONTINUE_TYPE);
    return 0;
  }

  public int beforeEdit(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                        EventManager manager, SqlAssistor assistor) {
    String strsql = "select t.clientname,p.FINALAMT,p.FINALENDDATE,p.FINALRATE from bmtable t,bmtableapp p where t.bmno = p.bmno and t.bmno='" + strbmno + "'";
    String strClientName, strFinalAmt, strFinalEndDate, strFinalRate;
    RecordSet rs = null;
    try {
      rs = conn.executeQuery(strsql);
      if (!rs.next()) {
        msgs.add("û�пͻ���¼������¼��");
        return -1;
      }
      strClientName = rs.getString(0);
      strFinalAmt = rs.getString(1);
      strFinalEndDate = rs.getString(2);
      strFinalRate = rs.getString(3);
      rs.close();
      instance.setValue("CLIENTNAME", DBUtil.fromDB(strClientName));
      instance.setValue("LOANAMT", DBUtil.fromDB(strFinalAmt));
      instance.setValue("FINALENDDATE", DBUtil.fromDB(strFinalEndDate));
      instance.setValue("FINALRATE", DBUtil.fromDB(strFinalRate));
    }
    catch (Exception ex) {
      ex.printStackTrace();
      msgs.add("�ͻ���¼������¼�쳣��");
      return -1;
    }
    return 0;
  }

  public int buttonEvent(SessionContext ctx, DatabaseConnection conn, FormInstance instance,
                         String button,
                         ErrorMessages msgs, EventManager manager) {
    MyDB.getInstance().addDBConn(conn);
    try {
      LoanGranted lg = new LoanGranted();
      //������Ȩ
      if (button.equals("AUTHORBUTTON")) {
        int a = -1;
        if ( (a = lg.sendLoanGrant(strbmno, true)) < 0) {
          msgs.clear();
          msgs.add("������Ȩʧ�ܣ�");
          return a;
        }
        else {
          ctx.setRequestAtrribute("BMNO", strbmno);
          if (strLoanType.equals("12")) {
            ctx.setTarget("/jspreport/loanbizpo.jsp");
          }
          else {
            ctx.setTarget("/jspreport/loanbiz.jsp");
          }
        }
      }
      //ȡ����Ȩ
      else if (button.equals("NONAUTHORBUTTON")) {
        int a = -1;
        if ( (a = lg.sendLoanGrant(strbmno, false)) < 0) {
          msgs.clear();
          msgs.add("ȡ����Ȩʧ�ܣ�");
          return a;
        }
        else {
          ctx.setRequestAtrribute("title", "��Ȩ����");
          ctx.setRequestAtrribute("msg", "ȡ����Ȩ�ɹ���");
          ctx.setRequestAtrribute("flag", "1");
          ctx.setRequestAtrribute("isback", "0");
          ctx.setTarget("/showinfo.jsp");
        }
      }
      //ȡ��ҵ��
      else if (button.equals("CANCELBUTTON")) {
        int a = BMTable.cancelBMTable(strbmno,"000000");
        if (a < 0) {
          msgs.add("ȡ��ҵ��ʧ�ܣ�");
          return a;
        }
        else {
          ctx.setRequestAtrribute("title", "��Ȩ");
          ctx.setRequestAtrribute("msg", "ȡ��ҵ��ɹ���");
          ctx.setRequestAtrribute("flag", "1");
          ctx.setRequestAtrribute("isback", "0");
          ctx.setTarget("/showinfo.jsp");
        }
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    finally {
      MyDB.getInstance().releaseDBConn();
    }
    return 0;
  }
}
