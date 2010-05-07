package zt.cms.bm.pd;

import zt.cms.pub.*;
import zt.platform.db.*;
import zt.platform.form.control.*;
import zt.platform.form.util.*;
import zt.platform.form.util.event.*;
import zt.platform.user.*;
import zt.platform.form.config.*;

public class BMPDAsMainListAction extends FormActions {
  private String flag = null; //�����Ƿ�ɶ�
  /**
   * ��չ�����load����
   */
  public int load(SessionContext ctx, DatabaseConnection conn,
                  FormInstance instance, ErrorMessages msgs,
                  EventManager manager, String parameter) {
    //Flag
    flag = (String) ctx.getParameter("flag");
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
    return 0;
  }

  public int preFind(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                     EventManager manager, SqlWhereUtil sqlWhereUtil) {
    if (super.preFind(ctx, conn, instance, msgs, manager, sqlWhereUtil) >= 0) {
      UserManager um = (UserManager) ctx.getAttribute(SystemAttributeNames.USER_INFO_NAME);
      //BRHID���û����㣩
      String mybrhid = SCUser.getBrhId(um.getUserName());
      //APPBRHIDs���û������µ�����ʵ���㣬�����Լ���
      String SUBBRHIDs = SCBranch.getAllSubBrhAndSelf1(mybrhid);
      if (SUBBRHIDs != null) {
          SUBBRHIDs = "'" + SUBBRHIDs.replaceAll(",", "','") + "'";
      }
      sqlWhereUtil.addWhereField("BMPDASMAIN", "BRHID", SUBBRHIDs, SqlWhereUtil.DataType_Sql, sqlWhereUtil.OperatorType_In);
    }
    return 0;
  }

  public int buttonEvent(SessionContext ctx, DatabaseConnection conn, FormInstance instance, String button,
                         ErrorMessages msgs, EventManager manager) {
    ctx.setRequestAtrribute("flag",flag);
    trigger(manager, "BMPDASMAINPAGE", null);
    return 0;
  }
}
