package zt.cms.bm.appplus;
/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
import java.util.logging.*;

import java.sql.*;

import zt.cmsi.pub.*;
import zt.platform.db.*;
import zt.platform.form.control.*;
import zt.platform.form.util.*;
import zt.platform.form.util.event.*;

public class BMPldgPDILLoanList extends FormActions {
  private Param param = null;
  String BMNo=null;

  public int load(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                  EventManager manager, String parameter) {
    param = (Param) ctx.getRequestAttribute(ParamName.ParamName);
    BMNo = param.getBmNo();
    return 0;
  }

  public int preFind(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                     EventManager manager, PreparedStatement ps, PreparedStatement countps) {
    try {
      ps.setString(1, BMNo);
      countps.setString(1, BMNo);
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
    return 0;
  }

  public int buttonEvent(SessionContext ctx, DatabaseConnection conn, FormInstance instance, String button,
                         ErrorMessages msgs, EventManager manager) {
    ctx.setRequestAtrribute(ParamName.ParamName, param);
    trigger(manager, "BMPLDGLLP", null);
    return 0;
  }
}
