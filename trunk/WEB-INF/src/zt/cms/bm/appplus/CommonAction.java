package zt.cms.bm.appplus;

import java.sql.*;

import zt.cms.bm.common.*;
import zt.cmsi.pub.*;
import zt.platform.db.*;
import zt.platform.form.control.*;
import zt.platform.form.util.*;
import zt.platform.form.util.event.*;

public abstract class CommonAction extends FormActions {
  private String bmNo = "";
  Param param=null;
  public abstract String getPageFormId();

  /**
   *  Description of the Method
   *
   *@param  ctx       Description of the Parameter
   *@param  conn      Description of the Parameter
   *@param  instance  Description of the Parameter
   *@param  button    Description of the Parameter
   *@param  msgs      Description of the Parameter
   *@param  manager   Description of the Parameter
   *@return           Description of the Return Value
   */
  public int load(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                  EventManager manager, String parameter) {
    RightChecker.checkReadonly(ctx, conn, instance);
    param = (Param) ctx.getRequestAttribute(ParamName.ParamName);
    this.bmNo = param.getBmNo();
    return 0;
  }

  public int buttonEvent(SessionContext ctx, DatabaseConnection conn, FormInstance instance, String button,
                         ErrorMessages msgs, EventManager manager) {
    RightChecker.transReadOnly(ctx, conn, instance);
    ctx.setRequestAtrribute(ParamName.ParamName, param);
    trigger(manager, getPageFormId(), null);
    return 0;
  }

  /**
   *  Description of the Method
   *
   *@param  ctx       Description of the Parameter
   *@param  conn      Description of the Parameter
   *@param  instance  Description of the Parameter
   *@param  msgs      Description of the Parameter
   *@param  manager   Description of the Parameter
   *@param  ps        Description of the Parameter
   *@param  countps   Description of the Parameter
   *@return           Description of the Return Value
   */
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
