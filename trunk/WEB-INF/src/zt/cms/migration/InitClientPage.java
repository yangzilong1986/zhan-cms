package zt.cms.migration;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
import com.zt.util.*;
import zt.cmsi.migration.*;
import zt.cmsi.pub.*;
import zt.cmsi.pub.ErrorCode;
import zt.platform.db.*;
import zt.platform.form.config.*;
import zt.platform.form.control.*;
import zt.platform.form.util.*;
import zt.platform.form.util.event.*;
import zt.platform.user.*;
import zt.platform.utils.*;

public class InitClientPage extends FormActions {

  private String strFlag = null; //��д��־
  private Param paramg = null; //��õı�������
  private Param params = null; //���͵ı�������
  private String strClientNO = null; //�ͻ���

  public int load(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                  EventManager manager, String parameter) {

    paramg = (Param) ctx.getRequestAttribute(ParamName.ParamName);
    if (paramg == null) {
      msgs.add("���ݲ������޷�������������");
      return -99;
    }

    strFlag = (String) paramg.getParam(ParamName.Flag);
    strClientNO = (String) paramg.getParam(ParamName.CLientNo);

    if (strClientNO == null) {
      msgs.add("δ�ҵ�����Ŀͻ��ţ��޷�����������������");
      return -99;
    }

    Debug.debug(Debug.TYPE_MESSAGE, "InitClientPage!Client=" + strClientNO);
    instance.setValue("CLIENTNO", strClientNO);
    trigger(manager, instance, EventType.EDIT_VIEW_EVENT_TYPE, Event.BRANCH_CONTINUE_TYPE);

    return 0;
  }

  public int beforeInsert(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                          EventManager manager) {
    return -1;
  }

  public int preInsert(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                       EventManager manager, SqlAssistor assistor) {
    return -1;
  }

  public int buttonEvent(SessionContext ctx, DatabaseConnection conn, FormInstance instance,
                         String button, ErrorMessages msgs, EventManager manager) {

    UserManager um = (UserManager) ctx.getAttribute(SystemAttributeNames.USER_INFO_NAME);
    String OPERATOR = null;
    try {
      OPERATOR = um.getUserName();
    }
    catch (Exception ex) {
      return ErrorCode.CANNOT_FOUND_CURRENT_USERNO;
    }

//    String str = "select bmtableapp.firstresp,bmtableapp.decidedby ";
//    str+="from bmtable,initclient,bmtableapp ";
//    str+="where initclient.clientno='" + this.strClientNO + "' ";
//    str+="and initclient.clientno=bmtable.clientno ";
//    str+="and bmtableapp.bmno=bmtable.bmno ";
//    str+="and bmtable.bmstatus<10";

    String str = "select bmtableapp.firstresp,bmtableapp.decidedby ";
    str+="from bmtable,bmtableapp ";
    str+="where bmtable.clientno='" + this.strClientNO + "' ";
    str+="and bmtableapp.bmno=bmtable.bmno ";
    str+="and bmtable.bmstatus<10";

    RecordSet rs = conn.executeQuery(str);
    while (rs.next()) {
      String firstResp = rs.getString("firstresp");
      if (firstResp == null || firstResp.trim().equals("")) {
        msgs.add("�ÿͻ�δ�������ĵ�һ�����˲�����Ϊ��");
        return -1;
      }
      String decidedby = rs.getString("decidedby");
      if (decidedby == null || decidedby.trim().equals("")) {
        msgs.add("�ÿͻ�δ�������ľ����˲�����Ϊ��");
        return -1;
      }
    }

    if (button != null && button.compareToIgnoreCase("NewIndvClient") == 0) {
      if (strClientNO == null) {
        return -99;
      }
      else {
        ctx.setRequestAtrribute("flag", "write");
        ctx.setRequestAtrribute("INITCLIENTNO", strClientNO);
        trigger(manager, "100001", null);
      }
    }
    else if (button != null && button.compareToIgnoreCase("NewCorpClient") == 0) {
      if (strClientNO == null) {
        return -99;
      }
      else {
        ctx.setRequestAtrribute("flag", "write");
        ctx.setRequestAtrribute("INITCLIENTNO", strClientNO);
        trigger(manager, "CMCC02", null);
      }
    }
    else if (button != null && button.compareToIgnoreCase("ExistUser") == 0) {

      String newClientNo = (String) ctx.getParameter("NCLIENTNO");
      Debug.debug(Debug.TYPE_MESSAGE, "New Client No is " + newClientNo);
      int ret = ClientMigration.clientMrig(strClientNO, newClientNo, OPERATOR);
      if (ret < 0) {
        String msg = PropertyManager.getProperty("" + ret);
        if (msg == null) {
          msg = "" + ret;
        }
        ctx.setRequestAtrribute("title", "�ͻ���Ϣ����");
        ctx.setRequestAtrribute("msg", "����ʧ��:" + msg);
        ctx.setRequestAtrribute("flag", "0");
        ctx.setTarget("/showinfo.jsp");
      }
      else {
        ctx.setRequestAtrribute("title", "�ͻ���Ϣ����");
        ctx.setRequestAtrribute("msg", "����ɹ���ɣ�");
        ctx.setRequestAtrribute("flag", "1");
        ctx.setTarget("/showinfo.jsp");
      }
    }

    return 0;
  }
}
