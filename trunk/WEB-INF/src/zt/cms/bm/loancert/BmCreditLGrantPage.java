package zt.cms.bm.loancert;

/**
 * <p>Title: �Ŵ�����</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003 ������Ϣ</p>
 * <p>Company: ������Ϣ</p>
 * @author YUSG
 * @version 1.0
 */
import java.math.*;

import zt.cmsi.client.*;
import zt.cmsi.pub.*;
import zt.platform.db.*;
import zt.platform.form.config.*;
import zt.platform.form.control.*;
import zt.platform.form.util.*;
import zt.platform.form.util.event.*;
import zt.platform.user.*;

/******************************************************
 *
 *  ����֤������ϸ
 *
 *****************************************************/
public class BmCreditLGrantPage extends FormActions {
  private String strFlag = null; //��д��־
  private Param paramg = null; //��õı�������
  private Param params = null; //���͵ı�������
  private String strClientNO = null; //�ͻ���
  private String strUserName = null; //��ǰ��½�û���

  public int load(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                  EventManager manager, String parameter) {

    paramg = (Param) ctx.getRequestAttribute(ParamName.ParamName);
    if (paramg == null) {
      msgs.add("���ݲ������޷���Ȩ����������");
      return -1;
    }

    strFlag = (String) paramg.getParam(ParamName.Flag);
    strClientNO = (String) paramg.getParam(ParamName.CLientNo);

    if (strClientNO == null) {
      msgs.add("���ݲ������޷���Ȩ����������");
      return -1;
    }
    else {
      UserManager um = (UserManager) ctx.getAttribute(SystemAttributeNames.
        USER_INFO_NAME);
      try {
        strUserName = um.getUserName();
      }
      catch (Exception e) {
        e.printStackTrace();
      }

      int iCount = 0;
      String strSql = "select count(*) from bmcreditlimit where clientno='";
      strSql = strSql + strClientNO + "'";

      RecordSet rs = conn.executeQuery(strSql);
      if (rs.next()) {
        iCount = rs.getInt(0);
      }
      if (iCount > 0) {
        msgs.add("����֤�Ѿ����ţ��뵽����֤ά��ģ�����ά����");
        return -1;
      }

      if (strFlag.equals(ParamName.Flag_WRITE)) {
        instance.setReadonly(false);
      }
      else {
        instance.setReadonly(true);
      }
      return 0;
    }
  }

  public int beforeInsert(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                          EventManager manager) {
    CMClient cmct = CMClientMan.getCMClient(strClientNO);
    String strName = cmct.name;

    instance.setValue("NAME", strName);
    instance.setValue("OPERATOR", strUserName);
    instance.setValue("CLIENTNO", strClientNO);
    return 0;
  }

  public int preInsert(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                       EventManager manager, SqlAssistor assistor) {
    //BigDecimal bd = BMReviewLimit.getInstance().getReviewLimitofUsr(strUserName, EnumValue.BMType_DaiKuanZhengDaiKuan);
    BigDecimal bd = new BigDecimal(100000000);
    //above line is changed by GZL, we dont check review limit when issuing Loan Card

    String strLa = ctx.getParameter("CREDITLIMIT").trim();
    float fLa = Float.parseFloat(strLa);
    if (bd == null) {
      msgs.add("û���ҵ����û�������������Ŷ�����ã�����ϵϵͳ����Ա��");
      return -1;
    }
    float fbd = bd.floatValue();

    if (fbd < fLa) {
      msgs.add("���Ŷ�ȳ�������Ȩ�޹涨��������޸ģ�");
      return -1;
    }
    return 0;
  }
  public int postInsertOk(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
        EventManager manager) {
    ctx.setRequestAtrribute("msg"," ����֤���ųɹ�");
    ctx.setTarget("/showinfo.jsp");

    return 0;
}

}
