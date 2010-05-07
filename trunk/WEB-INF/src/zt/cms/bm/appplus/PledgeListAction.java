package zt.cms.bm.appplus;

import java.sql.*;

import zt.cms.bm.common.*;
import zt.cmsi.pub.*;
import zt.platform.db.*;
import zt.platform.form.control.*;
import zt.platform.form.util.*;
import zt.platform.form.util.event.*;

/**
 *  Description of the Class
 *
 *@author     Administrator
 *@created    2004Äê1ÔÂ2ÈÕ
 */
public class PledgeListAction extends FormActions {
    private String bmNo = "";
    Param param=null;

    public int load(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                    EventManager manager, String parameter) {
      RightChecker.checkReadonly(ctx, conn, instance);
      param = (Param) ctx.getRequestAttribute(ParamName.ParamName);
      if (param == null || param.getBmNo()==null) {
        this.bmNo = ctx.getParameter("BMNO");
      }
      else{
        this.bmNo = param.getBmNo();
      }
      return 0;

    }

    public int buttonEvent(SessionContext ctx, DatabaseConnection conn, FormInstance instance, String button,
                           ErrorMessages msgs, EventManager manager) {
      RightChecker.transReadOnly(ctx, conn, instance);
      ctx.setRequestAtrribute(ParamName.ParamName, param);
      String formid=instance.getFormid();
      if(formid.endsWith("1")){
        trigger(manager, "BMPLEDGEPAGE1", null);
      }
      else{
        trigger(manager, "BMPLEDGEPAGE", null);
      }
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
