package zt.cms.cm.client;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

import zt.cms.cm.common.*;
import zt.cms.pub.*;
import zt.platform.db.*;
import zt.platform.form.config.*;
import zt.platform.form.control.*;
import zt.platform.form.util.*;
import zt.platform.form.util.event.*;
import zt.platform.user.*;
import zt.cmsi.pub.confitem;

public class GuarantorListAction extends FormActions {
  private boolean firstRun = true;
  public int load(SessionContext ctx, DatabaseConnection conn, FormInstance instance,
                  ErrorMessages msgs,
                  EventManager manager, String parameter) {
    RightChecker.checkReadonly(ctx, conn, instance);
    return 0;
  }

  public int preFind(SessionContext ctx, DatabaseConnection conn, FormInstance instance,
                     ErrorMessages msgs,
                     EventManager manager, SqlWhereUtil sqlWhereUtil) {
    if (super.preFind(ctx, conn, instance, msgs, manager, sqlWhereUtil) < 0) return -1;

    if (firstRun == true) {
      firstRun = false;
      return confitem.QUERY_FORM_INPUT_CRITERIA_CODE;
    }

    String myName, myIDType, myID;
    myName = instance.getStringValue("NAME");
    myIDType = instance.getStringValue("IDTYPE");
    myID = instance.getStringValue("ID");
    System.out.println(":"+myName + ":"+ myIDType+":"+myID);


    if ( (instance.getStringValue("NAME") != null && instance.getStringValue("NAME").length() > 0) ||
        //(instance.getStringValue("IDTYPE") != null && instance.getStringValue("IDTYPE").length() > 0) ||
        (instance.getStringValue("ID") != null && instance.getStringValue("ID").length() > 0)) {
      return 0;
    }
    StringBuffer strBuf = new StringBuffer();
    strBuf.append("'");
    UserManager um = (UserManager) ctx.getAttribute(SystemAttributeNames.USER_INFO_NAME);
    if(um==null){
      msgs.add("用户会话已超时或已失效，请重新登录！");
      return -1;
    }
    //BRHID（用户网点）
    String BRHID = SCUser.getBrhId(um.getUserName());
    if (BRHID == null || BRHID.length() < 1) {
      return -1;
    }
    RecordSet rs = conn.executeQuery("select loginname from scuser where brhid='" + BRHID + "'");
    while (rs.next()) {
      String strLogin = rs.getString(0);
      if (strLogin != null) {
        strBuf.append(strLogin.trim());
        strBuf.append("','");
      }
    }

    //strBuf.append("'");
    if(strBuf.length() == 1)
      strBuf.append("'");
    else
      strBuf.delete(strBuf.length()-2,strBuf.length());

    sqlWhereUtil.addWhereField("CMGUARANTOR", "OPERATOR", strBuf.toString(),
                               SqlWhereUtil.DataType_Sql,
                               sqlWhereUtil.OperatorType_In);
    return 0;
  }

  public int buttonEvent(SessionContext ctx, DatabaseConnection conn, FormInstance instance,
                         String button,
                         ErrorMessages msgs, EventManager manager) {
    RightChecker.transReadOnly(ctx, conn, instance);
    trigger(manager, "CMGUARANTORPAGE", null);
    return 0;
  }
}
