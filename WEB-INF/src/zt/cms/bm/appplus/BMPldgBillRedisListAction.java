package zt.cms.bm.appplus;

import java.sql.*;

import zt.cms.bm.common.*;
import zt.cmsi.pub.*;
import zt.platform.db.*;
import zt.platform.form.control.*;
import zt.platform.form.util.*;
import zt.platform.form.util.event.*;
import zt.platform.form.config.*;
/**
 *  Description of the Class
 *
 *@author     Administrator
 *@created    2004年1月6日
 */
public class BMPldgBillRedisListAction extends FormActions {
  String bmNo = "";
  private String TYPE=null;
  public int load(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                  EventManager manager, String parameter) {
    RightChecker.checkReadonly(ctx, conn, instance);
    Param param = (Param) ctx.getRequestAttribute("BMPARAM");
    this.bmNo = param.getBmNo();
    TYPE=(String)ctx.getRequestAttribute("TYPE");
    if(TYPE!=null){
      instance.useCloneFormBean();
      FormBean fb = instance.getFormBean();
      if (TYPE.equals("ZC")) {
        fb.setTitle("转贴现-转出票据登记");
      }
    }

    return 0;
  }

  public int buttonEvent(SessionContext ctx, DatabaseConnection conn, FormInstance instance, String button,
                         ErrorMessages msgs, EventManager manager) {
    RightChecker.transReadOnly(ctx, conn, instance);
    Param param = new Param();
    param.addParam("BMNO", this.bmNo);
    ctx.setRequestAtrribute("BMPARAM", param);
    trigger(manager, "BMPLDGBILLREDISPAGE", null);
    return 0;
  }

  public int preFind(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                     EventManager manager, PreparedStatement ps, PreparedStatement countps) {
    try {
      ps.setString(1, this.bmNo);
      countps.setString(1, this.bmNo);
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
    return 0;
  }

}
