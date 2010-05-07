package zt.cms.bm.loancert;

/**
 * <p>Title: ����֤ά����ϸ</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003 ������Ϣ</p>
 * <p>Company: ������Ϣ</p>
 * @author YUSG
 * @version 1.0
 */
import java.math.*;

import zt.cmsi.biz.*;
import zt.cmsi.pub.*;
import zt.cmsi.pub.define.*;
import zt.platform.db.*;
import zt.platform.form.config.*;
import zt.platform.form.control.*;
import zt.platform.form.util.*;
import zt.platform.form.util.event.*;
import zt.platform.user.*;
import zt.cmsi.client.*;
import com.ebis.encrypt.*;

public class BmCreditLMainPage
    extends FormActions {
  private String strFlag = null; //��д��־
  private Param paramg = null; ///��õı�������
  private String strClientNO = null; //�ͻ���
  private String strTypeNo = null; //ҵ�����ʹ���
  private String strUserName = null; //��ǰ��½�û���
  private CMClient cmclient = null; //�ͻ�����

  public int load(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                  EventManager manager, String parameter) {
    //Edit by wxj at 20040223
    strFlag=(String) ctx.getRequestAttribute("flag");
    strClientNO=(String) ctx.getRequestAttribute("CLIENTNO");
    if(strFlag==null || strClientNO==null)
    {
        paramg = (Param) ctx.getRequestAttribute(ParamName.ParamName);
        if (paramg == null) {
            msgs.add("�������󣬲������󲻴��ڣ�");
            return -1;
        }
        //flag
        strFlag = paramg.getParam(ParamName.Flag).toString();
        if (strFlag == null) {
            msgs.add("�������󣬴����д��־�����ڣ�");
            return -1;
        }
        if (strFlag.equals(ParamName.Flag_WRITE)) {
            instance.setReadonly(false);
        }
        else {
            instance.setReadonly(true);
        }
        //strClientNO
        strClientNO = (String) paramg.getParam(ParamName.CLientNo);
        if (strClientNO == null) {
            msgs.add("�������󣬿ͻ��Ų����ڣ�");
            return -1;
        }
        //strTypeNo
        Object tmp = paramg.getParam(ParamName.BMType);
        strTypeNo = (tmp == null ? null : tmp.toString().trim());
    }
    else
    {
        instance.setReadonly(true);
        strTypeNo = "1";
    }
    //strUserName
    UserManager um = (UserManager) ctx.getAttribute(SystemAttributeNames.USER_INFO_NAME);
    try {
      strUserName = um.getUserName();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    //cmclient
    cmclient=CMClientMan.getCMClient(strClientNO);
    instance.setValue("CLIENTNO", strClientNO);
    instance.setValue("TYPENO", strTypeNo);
    instance.setValue("OPERATOR", strUserName);
    instance.setValue("CLIENTNAME",cmclient.name);

    //���մ���Ŀͻ��ź�ҵ�����ʹ�����Ҽ�¼,����ҵ�,������޸�״̬,û�����������״̬��
    if (strTypeNo != null && strTypeNo.length() > 0) {
      String formid = instance.getFormid();
      FormBean formBean = FormBeanManager.getForm(formid);
      String tblName = formBean.getTbl().trim();
      if (DBUtil.getCellValue(conn, tblName, "CLIENTNO", "CLIENTNO='" + strClientNO + "' and TYPENO=" + strTypeNo) != null) {
        trigger(manager, instance, EventType.EDIT_VIEW_EVENT_TYPE,
                Event.BRANCH_CONTINUE_TYPE);
      }
      else{
        ctx.setRequestAtrribute("title", "����֤��ѯ");
        ctx.setRequestAtrribute("msg", "�Բ��𣬸ÿͻ��Ĵ���֤�����ڣ�");
        ctx.setRequestAtrribute("flag", "0");
        ctx.setTarget("/showinfo.jsp");
      }
    }
    return 0;
  }

  public int beforeEdit(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                        EventManager manager, SqlAssistor assistor) {
    //Edit by wxj at 20040223
    String str = "";
    CreditLimitData cldata = CreditLimit.getCreditLimit(strClientNO, Integer.parseInt(strTypeNo));
    if (cldata != null) {
      String strKyjeInt = null; //���ý�����������
      String strKyjeDec = null; //���ý���С������
      float fKyje = cldata.creditLimit.floatValue() - cldata.loanBal.floatValue();
      String strKyje = String.valueOf(fKyje);

      int iLength = strKyje.length();
      int iDot = strKyje.indexOf(".");
      if (iLength > iDot + 2) {
        strKyjeInt = strKyje.substring(0, iDot);
        strKyjeDec = strKyje.substring(iDot + 1, iDot + 3);
      }
      else if (iLength > iDot + 1) {
        strKyjeInt = strKyje.substring(0, iDot);
        strKyjeDec = strKyje.substring(iDot + 1, iDot + 2);
        strKyjeDec = strKyjeDec + "0";
      }
      else {
        strKyjeInt = strKyje.substring(0, iDot);
        strKyjeDec = "00";
      }
      str = strKyjeInt + "." + strKyjeDec;
    }
    String strDate = SystemDate.getSystemDate5(null);
    instance.setValue("LASTMODIFIED", strDate);
    instance.setValue("KYJE", str);
    return 0;
  }

  public int preEdit(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                     EventManager manager, SqlAssistor assistor) {
    //BigDecimal bd = BMReviewLimit.getInstance().getReviewLimitofUsr(strUserName, EnumValue.BMType_DaiKuanZhengDaiKuan);
    BigDecimal bd = new BigDecimal(100000000);
    //above line is changed by GZL, we dont check review limit when issuing Loan Card

    String strLa = ctx.getParameter("CREDITLIMIT").trim();
    String passwd = ctx.getParameter("PASSWD").trim();
    if(passwd.length() <= 0) passwd = null;

    if(passwd != null && passwd.length() < 6 )
    {
      msgs.add("���볤�ȱ���Ϊ6λ��");
      return -1;
    }

    if(passwd != null && passwd.length() <= 10)
    {
      EncryptData ed = new EncryptData();
      String encrypted = new String(ed.enPasswd(passwd.getBytes()));
      assistor.setSqlFieldValue(assistor.getDefaultTbl(), "PASSWD",encrypted);
    }


    float fLa = Float.parseFloat(strLa);
    float fbd = bd.floatValue();

    if (fbd < fLa) {
      msgs.add("���Ŷ�ȳ�������Ȩ�޹涨��������޸ģ�");
      return -1;
    }
    return 0;
  }
}
