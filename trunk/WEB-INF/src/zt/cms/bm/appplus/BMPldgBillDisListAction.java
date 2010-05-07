package zt.cms.bm.appplus;
/**
 *  Description of the Class
 *
 *@author     Administrator
 *@created    2004年1月6日
 */

import java.sql.*;

import zt.cmsi.pub.*;
import zt.platform.db.*;
import zt.platform.form.control.*;
import zt.platform.form.util.*;
import zt.platform.form.util.event.*;
import zt.platform.form.config.*;

public class BMPldgBillDisListAction extends FormActions {
    private String bmNo = "";
    private String TYPE=null;
    Param param=null;

    public int load(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                    EventManager manager, String parameter) {
      param = (Param) ctx.getRequestAttribute(ParamName.ParamName);
      this.bmNo = param.getBmNo();
      TYPE=(String)ctx.getRequestAttribute("TYPE");
      if(TYPE!=null){
        instance.useCloneFormBean();
        FormBean fb = instance.getFormBean();
        if (TYPE.equals("ZR")) {
          fb.setTitle("转贴现-转入票据登记");
        }
      }
      return 0;
    }

    public int buttonEvent(SessionContext ctx, DatabaseConnection conn, FormInstance instance, String button,
                           ErrorMessages msgs, EventManager manager) {
      ctx.setRequestAtrribute(ParamName.ParamName, param);
      ctx.setRequestAtrribute("TYPE",TYPE);
      trigger(manager, "BMPLDGBILLDISPAGE", null);
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
