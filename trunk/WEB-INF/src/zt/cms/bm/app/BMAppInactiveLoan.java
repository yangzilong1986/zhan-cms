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

  private String flag = null; //�����Ƿ�ɶ�
  private String BMNo = null; //ҵ���
  private String BMTransNo = null; //ҵ����ϸ��
  private String OPERATOR = null; //ҵ��Ա
  private Param param = null; //������װ�������ڴ���
  private String ClientNo = null; //�ͻ����
  private String TYPENO=null;//ҵ������


 private boolean isJustWeihu = false;
  /**
   * ��չ�����load����
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
      msgs.add("�������󣬲������󲻴��ڣ�");
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
      msgs.add("��������ҵ��Ų����ڣ�");
      return -1;
    }
    //BMTransNo
    Object tmp = param.getParam(ParamName.BMTransNo);
    BMTransNo = (tmp == null) ? null : param.getParam(ParamName.BMTransNo).toString();

    if(ctx.getRequestAttribute("isJustWeihu")==null){
        if (flag.equals("write") && (BMTransNo == null || BMTransNo.length() < 1)) {
            msgs.add("��������ҵ����ϸ�Ų����ڣ�");
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
      msgs.add("ϵͳ���󣬻�ȡҵ�����Ա��Ϣʧ�ܣ�");
      return -1;
    }
    //���մ����ҵ�������ϸ��Ϣ�ǼǱ����в��Ҽ�¼,����ҵ�,����ʾ�˼�¼,û�������������״̬��
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
      msgs.add("�ò����ͻ����õȼ���Ϣ��");
      return -1;
    }
  }
}

    return 0;
  }

  /**
   * ��չ�����beforeInsert�������������ӡ���ť����Ӧ���¼�
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
    //��������
    TYPENO=DBUtil.getCellValue(conn, "BMTABLE", "TYPENO", "BMNO='" + BMNo + "'");
    if(TYPENO.equals("17")){
      //ԭҵ���
      UpToDateApp bmtblappdata = BMTable.getUpToDateApp(BMNo);
      String OrigBMNo=bmtblappdata.origBMNo;
      if (OrigBMNo == null || OrigBMNo.length() < 1) {
        msgs.add("ϵͳ����ԭҵ��Ų����ڣ�");
        return -1;
      }
      instance.setValue("ORIGBMNO", OrigBMNo);
      //�ͻ�����
      BMTableData bmtbldata = BMTable.getBMTable(BMNo);
      String ClientName = bmtbldata.clientName;
      if (ClientName == null || ClientName.length() < 1) {
        msgs.add("ϵͳ���󣬿ͻ����Ʋ����ڣ�");
        return -1;
      }
      instance.setValue("CLIENTNAME", ClientName);
      //��������̨��(bminactloan)�л��������
      InactLoan bminactloan = InactLoanMan.getInactLoan(OrigBMNo);
      String AdminedBy=bminactloan.adminedBy;
      if (AdminedBy == null || AdminedBy.length() < 1) {
        msgs.add("ϵͳ�����л�������˲����ڣ�");
        return -1;
      }
      instance.setValue("ADMINEDBY", AdminedBy); //������
      //����ԭҵ��ŴӴ���̨��(RQLoanLedger)�л�ñ���, ԭ��ͬ��, ԭ�ʺ�, ԭ��ݺ���, ���ʽ��, ��ǰ���
      LoanLedger RQLoanLedger = LoanLedgerMan.getLoanLedger(OrigBMNo);
      instance.setValue("CURNO", RQLoanLedger.curNo); //����
      instance.setValue("ORIGCONTRACTNO", RQLoanLedger.contractNo); //ԭ��ͬ��
      instance.setValue("ORIGACCOUNTNO", RQLoanLedger.actNo); //ԭ�ʺ�
      instance.setValue("ORIGDUEBILLNO", RQLoanLedger.cnlNo); //ԭ��ݺ���
      instance.setValue("LEDGERAMT", RQLoanLedger.contractAMt.toString()); //���ʽ��(��ͬ���)
      instance.setValue("BAL", RQLoanLedger.nowBal.toString()); //��ǰ���
    }
    return 0;
  }

  /**
   * ��չ�����preInsert��������ʵ��ҵ����insertǰ�Ĵ���
   */
  public int preInsert(SessionContext ctx, DatabaseConnection conn,
                       FormInstance instance, ErrorMessages msgs,
                       EventManager manager, SqlAssistor assistor) {
    assistor.setSqlFieldValue(assistor.getDefaultTbl(), "BMNO", BMNo);
    return 0;
  }

  /**
   * ��չ�����postInsertOk��������ʵ��ҵ����insert��Ĵ���
   */
  public int postInsertOk(SessionContext ctx, DatabaseConnection conn,
                          FormInstance instance, ErrorMessages msgs,
                          EventManager manager) {
    String finish = (String) ctx.getParameter("finish");
    if (finish == null || finish.length() < 1) {
      msgs.add("ϵͳ�����Ƿ���ɱ�־Ϊ�գ�");
      return -1;
    }
    try {
      MyDB.getInstance().addDBConn(conn);
      int flg = 0;
      if (finish.trim().equals("1") && !isJustWeihu) {
        flg = BMTrans.compltTrans(BMNo, Integer.parseInt(BMTransNo), OPERATOR);
        if (flg >= 0) {
          ctx.setRequestAtrribute("title", "ҵ��Ǽ���ϸ��Ϣ�Ǽ�");
          ctx.setRequestAtrribute("msg", "��Ϣ����ɹ������ύ��������������");
          ctx.setRequestAtrribute("flag", "1");
          ctx.setTarget("/showinfo.jsp");
        }
      }
      else {
        ctx.setRequestAtrribute("title", "ҵ��Ǽ���ϸ��Ϣ�Ǽ�");
        ctx.setRequestAtrribute("msg", "��Ϣ����ɹ���");
        ctx.setRequestAtrribute("flag", "1");
        ctx.setTarget("/showinfo.jsp");
      }

      return flg;
    }
    catch (Exception ex) {
      msgs.add("δ֪ԭ���µ�ϵͳ����");
      return -2;
    }
    finally {
      MyDB.getInstance().releaseDBConn();
    }
  }

  /**
   * ��չ�����preEdit��������ʵ��ҵ����editǰ�Ĵ���
   */
  public int preEdit(SessionContext ctx, DatabaseConnection conn,
                     FormInstance instance, ErrorMessages msgs,
                     EventManager manager, SqlAssistor assistor) {
    assistor.setSqlFieldValue(assistor.getDefaultTbl(), "BMNO", BMNo);
    assistor.setSqlFieldValue(assistor.getDefaultTbl(), "OPERATOR", OPERATOR);
    return 0;
  }

  /**
   * ��չ�����postEditOk��������ʵ��ҵ����edit��Ĵ���
   */
  public int postEditOk(SessionContext ctx, DatabaseConnection conn,
                        FormInstance instance, ErrorMessages msgs,
                        EventManager manager) {
    String finish = (String) ctx.getParameter("finish");
    if (finish == null || finish.length() < 1) {
      msgs.add("ϵͳ�����Ƿ���ɱ�־Ϊ�գ�");
      return -1;
    }
    try {
      MyDB.getInstance().addDBConn(conn);
      int flg = 0;
      if (finish.trim().equals("1") && !isJustWeihu) {
        flg = BMTrans.compltTrans(BMNo, Integer.parseInt(BMTransNo), OPERATOR);
        if (flg >= 0) {
          ctx.setRequestAtrribute("title", "ҵ��Ǽ���ϸ��Ϣ�Ǽ�");
          ctx.setRequestAtrribute("msg", "��Ϣ����ɹ������ύ��������������");
          ctx.setRequestAtrribute("flag", "1");
          ctx.setTarget("/showinfo.jsp");
        }
      }
      else {
        ctx.setRequestAtrribute("title", "ҵ��Ǽ���ϸ��Ϣ�Ǽ�");
        ctx.setRequestAtrribute("msg", "��Ϣ����ɹ���");
        ctx.setRequestAtrribute("flag", "1");
        ctx.setTarget("/showinfo.jsp");
      }
      return flg;
    }
    catch (Exception ex) {
      msgs.add("δ֪ԭ���µ�ϵͳ����");
      return -2;
    }
    finally {
      MyDB.getInstance().releaseDBConn();
    }
  }

  /**
   * ��չ�����buttonEvent��������Ӧ�Զ��尴ť�ĵ���¼�
   */
  public int buttonEvent(SessionContext ctx, DatabaseConnection conn,
                         FormInstance instance, String button,
                         ErrorMessages msgs, EventManager manager) {
    if (BMNo == null || BMNo.length() < 1) {
      msgs.add("ҵ����δ�Ǽǣ�");
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
    //��ʾ�ͻ���Ϣ
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
        ctx.setRequestAtrribute("title", "����ҵ����ϸ��Ϣ�Ǽ�");
        ctx.setRequestAtrribute("msg", "�鿴��ʷ������¼��״̬�²���ȡ���Ǽǣ��뵽����̨����");
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
