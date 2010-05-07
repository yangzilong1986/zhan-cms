package zt.cms.cm;

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

import zt.platform.db.*;
import zt.platform.form.control.*;
import zt.platform.form.util.*;
import zt.platform.form.util.event.*;

public class QCorpContactList
    extends FormActions {
  public static Logger logger = Logger.getLogger("zt.cms.cm.QCorpContactList");
  private String strClientNO = null;
  private String strFlag = null;

  public int load(SessionContext ctx, DatabaseConnection conn, FormInstance instance,
                  ErrorMessages msgs,
                  EventManager manager, String parameter) {
    strFlag = String.valueOf(ctx.getRequestAttribute("flag"));
    //strClientNO=ctx.getParameter("CLIENTNO");
    strClientNO = String.valueOf(ctx.getRequestAttribute("CLIENTNO"));
    return 0;
  }

  public int preFind(SessionContext ctx, DatabaseConnection conn, FormInstance instance,
                     ErrorMessages msgs,
                     EventManager manager, PreparedStatement ps, PreparedStatement countps) {
    try {
      ps.setString(1, strClientNO);
      countps.setString(1, strClientNO);
      if (strFlag.equals("write")) {
        instance.setReadonly(false);
      }
      else {
        instance.setReadonly(true);
      }
      return 0;
    }
    catch (Exception ex) {
      logger.severe(ex.getMessage());
      return -1;
    }
  }

  public int buttonEvent(SessionContext ctx, DatabaseConnection conn, FormInstance instance,
                         String button,
                         ErrorMessages msgs, EventManager manager) {
    ctx.setRequestAtrribute("CLIENTNO", strClientNO);
    ctx.setRequestAtrribute("flag", strFlag);

    if (button != null && button.equals(SessionAttributes.REQUEST_EDIT_BUTTON_VALUE)) {
      trigger(manager, "CMCT02", null);
    }
    if (button != null && button.equals(SessionAttributes.REQUEST_BUTTON_ADD_NAME)) {
      trigger(manager, "CMCT02", null);
    }
    return 0;
  }

}
