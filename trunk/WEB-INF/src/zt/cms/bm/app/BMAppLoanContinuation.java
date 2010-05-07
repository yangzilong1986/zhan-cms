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

  private String flag = null; //�����Ƿ�ɶ�
  private String BMNo = null; //ҵ���
  private String BMTransNo = null; //ҵ����ϸ��
  private String OrigBMNo = null; //ԭҵ���
  private String ClientNo = null; //�ͻ����
  private String ClientName = null; //�ͻ�����
  private String BrhID = null; //ҵ������
  private String OPERATOR = null; //ҵ��Ա
  private String AdminedBy = null; //������
  private Param param = null; //������װ�������ڴ���
  private String TYPENO = null; //ҵ������
  private Integer period = null;

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
    BMTransNo = (tmp == null) ? null :
        param.getParam(ParamName.BMTransNo).toString();

    if (ctx.getRequestAttribute("isJustWeihu") == null) {
      if (flag.equals("write") && (BMTransNo == null || BMTransNo.length() < 1)) {
        msgs.add("��������ҵ����ϸ�Ų����ڣ�");
        return -1;
      }
    }
    else {
      this.isJustWeihu = true;
    }

    //BrhID
    BrhID = (String) param.getParam(ParamName.BrhID);
    if (BrhID == null || BrhID.length() < 1) {
      //msgs.add("��������ҵ�����㲻���ڣ�");
      //return -1;
      BrhID = "";
    }
    //OPERATOR
    UserManager um = (UserManager) ctx.getAttribute(SystemAttributeNames.
        USER_INFO_NAME);
    OPERATOR = um.getUserName();
    if (OPERATOR == null || OPERATOR.length() < 1) {
      msgs.add("ϵͳ���󣬻�ȡҵ�����Ա��Ϣʧ�ܣ�");
      return -1;
    }
    //ԭҵ���
    UpToDateApp bmtblappdata = BMTable.getUpToDateApp(BMNo);
    OrigBMNo = bmtblappdata.origBMNo;
    if (OrigBMNo == null || OrigBMNo.length() < 1) {
      msgs.add("ϵͳ����ԭҵ��Ų����ڣ�");
      return -1;
    }
    //�ͻ�����
    BMTableData bmtbldata = BMTable.getBMTable(BMNo);
    ClientNo = bmtbldata.clientNo;
    ClientName = bmtbldata.clientName;
    if (ClientName == null || ClientName.length() < 1) {
      msgs.add("ϵͳ���󣬿ͻ����Ʋ����ڣ�");
      return -1;
    }
    //BMSTATUS
    if (flag.equals("write") &&
        bmtbldata.bmStatus == zt.cmsi.pub.cenum.EnumValue.BMStatus_QuXiao) {
      msgs.add("�Բ��𣬸ñ�ҵ���Ѿ���ȡ�������ֹ�ˢ�¹���̨��");
      return -1;
    }

    //���մ����ҵ�������ϸ��Ϣ�ǼǱ����в��Ҽ�¼,����ҵ�,����ʾ�˼�¼,û�������������״̬��
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
   * ��չ�����beforeInsert�������������ӡ���ť����Ӧ���¼�
   */
  public int beforeInsert(SessionContext ctx, DatabaseConnection conn,
                          FormInstance instance, ErrorMessages msgs,
                          EventManager manager) {
    instance.setValue("BMNO", BMNo);
    instance.setValue("APPDATE", SystemDate.getSystemDate5(""));
    instance.setValue("CLIENTNAME", ClientName);
    instance.setValue("BRHID", BrhID);
    instance.setValue("OPERATOR", OPERATOR);
    //BMTableApp�л�õǼǽ�������ſ��գ����뻹���ա�
    UpToDateApp bmtblappdata = BMTable.getUpToDateApp(BMNo);
    instance.setValue("APPAMT", bmtblappdata.appAmt.toString());
    instance.setValue("APPSTARTDATE",
                      util.calToString(bmtblappdata.appStartDate));
    instance.setValue("APPENDDATE", util.calToString(bmtblappdata.appEndDate));
    //����ԭҵ��ŴӴ���̨��(RQLoanLedger)�л�ñ���, ԭ��ͬ��, ԭ�ʺ�, ԭ��ݺ���, ���ʽ��, ���
    LoanLedger RQLoanLedger = LoanLedgerMan.getLoanLedger(OrigBMNo);
    instance.setValue("CURNO", RQLoanLedger.curNo); //����
    instance.setValue("ORIGCONTRACTNO", RQLoanLedger.contractNo); //ԭ��ͬ��
    instance.setValue("ORIGACCOUNTNO", RQLoanLedger.actNo); //ԭ�ʺ�
    instance.setValue("ORIGDUEBILLNO", RQLoanLedger.cnlNo); //ԭ��ݺ���
    instance.setValue("LEDGERAMT", RQLoanLedger.contractAMt.toString()); //���ʽ��(��ͬ���)
    instance.setValue("APPAMT", RQLoanLedger.nowBal.toString()); //���
    this.period = RQLoanLedger.perimon;

    return 0;
  }

  /**
   * ��չ�����preInsert��������ʵ��ҵ����insertǰ�Ĵ���
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
            msgs.add("�������޻򸡶��������ݸ�ʽ����");
            return -1;
          }

          if(this.period != null) appMonthInt += period.intValue();
          System.out.println("---------------------" + appMonthInt);

          BMBaseInst baseint = BMBaseInst.getBaseInst(EnumValue.BMType_ZhanQi,appMonthInt);
          if (baseint == null) {
            msgs.add("δȡ�û�׼�������ã�����ϵͳ�Ļ�׼�������ã�");
            return -1;
          }

          //System.out.println("baseint ===================" + baseint.interest);

          instance.setValue("BRATE", "" + baseint.interest);
          BigDecimal finalrate = baseint.interest.multiply(fratef.add(new
              BigDecimal(1)));
          instance.setValue("RATE", "" + finalrate);
          msgs.add("�Ѿ���û�׼���ʣ��ɹ�.���������.");
          return -1;
        }
        else {
          msgs.add("���������...");
          return -1;
        }
      }
    }

    assistor.setSqlFieldValue(assistor.getDefaultTbl(), "BMNO", BMNo);
    int LOANTYPE3 = instance.getIntValue("LOANTYPE3");
    int appPlus = getAppPlus(BMNo, LOANTYPE3, conn);
    if (appPlus < 1) {
      if (LOANTYPE3 == 210) {
        msgs.add("������ʽΪ��֤����֤����Ϣ������Ϊ�գ�");
        return -1;
      }
      if (LOANTYPE3 == 220) {
        msgs.add("������ʽΪ��Ѻ����Ѻ����Ϣ������Ϊ�գ�");
        return -1;
      }
      if (LOANTYPE3 == 230) {
        msgs.add("������ʽΪ��Ѻ����Ѻ����Ϣ������Ϊ�գ�");
        return -1;
      }
    }

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
      if (finish.trim().equals("1") && (!isJustWeihu)) {
        flg = BMTrans.compltTrans(BMNo, Integer.parseInt(BMTransNo), OPERATOR);
        if (flg >= 0) {
          ctx.setRequestAtrribute("title", "����ҵ����ϸ��Ϣ�Ǽ�");
          ctx.setRequestAtrribute("msg", "��Ϣ����ɹ������ύ��������������");
          ctx.setRequestAtrribute("flag", "1");
          ctx.setTarget("/showinfo.jsp");
        }
      }
      else {
        ctx.setRequestAtrribute("title", "����ҵ����ϸ��Ϣ�Ǽ�");
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
   * ��չ�����postFindOk��������ʵ��ҵ����find�ɹ���Ĵ���
   */
  public int postFindOk(SessionContext ctx, DatabaseConnection conn,
                        FormInstance instance, ErrorMessages msgs,
                        EventManager manager, RecordSet result) {
    instance.setValue("CLIENTNAME", ClientName);
    instance.setValue("ADMINEDBY", AdminedBy);
    return 0;
  }

  /**
   * ��չ�����beforeEdit�������������ӡ���ť����Ӧ���¼�
   */
  public int beforeEdit(SessionContext ctx, DatabaseConnection conn,
                        FormInstance instance, ErrorMessages msgs,
                        EventManager manager, SqlAssistor assistor) {
    instance.setValue("CLIENTNAME", ClientName);
    return 0;
  }

  /**
   * ��չ�����preEdit��������ʵ��ҵ����editǰ�Ĵ���
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
        msgs.add("������ʽΪ��֤����֤����Ϣ������Ϊ�գ�");
        return -1;
      }
      if (LOANTYPE3 == 220) {
        msgs.add("������ʽΪ��Ѻ����Ѻ����Ϣ������Ϊ�գ�");
        return -1;
      }
      if (LOANTYPE3 == 230) {
        msgs.add("������ʽΪ��Ѻ����Ѻ����Ϣ������Ϊ�գ�");
        return -1;
      }
    }

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
      if (finish.trim().equals("1") && (!isJustWeihu)) {
        flg = BMTrans.compltTrans(BMNo, Integer.parseInt(BMTransNo), OPERATOR);
        if (flg >= 0) {
          ctx.setRequestAtrribute("title", "����ҵ����ϸ��Ϣ�Ǽ�");
          ctx.setRequestAtrribute("msg", "��Ϣ����ɹ������ύ��������������");
          ctx.setRequestAtrribute("flag", "1");
          ctx.setTarget("/showinfo.jsp");
        }
      }
      else {
        ctx.setRequestAtrribute("title", "����ҵ����ϸ��Ϣ�Ǽ�");
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
    else if (button.equals("BTN_DZDJ")) {
      trigger(manager, "BMPLDGPDASSETLIST", null);
    }
    //��ʾ�ͻ���Ϣ
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

  /**
   * ���ݲ�ͬ�ĵ�����ʽ��ȡ�Ƿ����ʵ�ʵ���������
   * -1��ҵ��ţ�p_bmno����������
   * -2���������ͣ�p_loantype3����������
   * -3�����ݿ��쳣
   */
  public int getAppPlus(String p_bmno, int p_loantype3,
                        DatabaseConnection p_conn) {
    if (p_bmno == null || p_bmno.trim().length() < 1)return -1;
    String sql = "select count(*) from ";
    String sql_from_where = null;
    //���õ�������Ҫ����
    if (p_loantype3 == 100) {
      return 0;
    }
    //��֤����
    else if (p_loantype3 == 210) {
      sql_from_where = "BMPLDGSECURITY where BMNO='" + p_bmno + "'";
    }
    //��Ѻ����
    else if (p_loantype3 == 220) {
      sql_from_where = "BMPLDGMORT where BMNO='" + p_bmno +
          "' and PLDGMORTTYPE=1";
    }
    //��Ѻ����
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
