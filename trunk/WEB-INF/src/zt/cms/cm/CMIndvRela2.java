package zt.cms.cm;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
import java.sql.*;
import java.util.logging.*;

import zt.platform.db.*;
import zt.platform.form.control.*;
import zt.platform.form.util.*;
import zt.platform.form.util.event.*;

public class CMIndvRela2
    extends FormActions {

  private static Logger logger = Logger.getLogger("zt.cms.cm.CMIndvRela2");
  private String CLIENTNO=null;
  private String flag=null;
  public int load(SessionContext ctx, DatabaseConnection conn,
                  FormInstance instance, ErrorMessages msgs,
                  EventManager manager, String parameter) {
    //flag
    flag = "read";
    if (ctx.getRequestAttribute("flag") != null) {
      flag = ctx.getRequestAttribute("flag").toString().trim();
    }
    if (flag.equals("write")) {
      instance.setReadonly(false);
    }
    else {
      instance.setReadonly(true);
    }
    //CLIENTNO
    if (ctx.getRequestAttribute("CLIENTNO") != null) {
      CLIENTNO = ctx.getRequestAttribute("CLIENTNO").toString().trim();
    }
    return 0;
  }

  public int preFind(SessionContext ctx, DatabaseConnection conn,
                     FormInstance instance, ErrorMessages msgs,
                     EventManager manager, PreparedStatement ps,
                     PreparedStatement countps) {
    try {
      ps.setString(1, CLIENTNO);
      countps.setString(1,CLIENTNO);
    }
    catch (Exception ex) {
      msgs.add(ex.getMessage());
      return -1;
    }
    return 0;
  }

  public int buttonEvent(SessionContext ctx, DatabaseConnection conn,
                         FormInstance instance, String button,
                         ErrorMessages msgs, EventManager manager) {
    ctx.setRequestAtrribute("CLIENTNO",CLIENTNO);
    ctx.setRequestAtrribute("flag",flag);
    if (button != null &&
        button.equals(SessionAttributes.REQUEST_EDIT_BUTTON_VALUE)) {
      trigger(manager, "100002", null);
    }
    if (button != null &&
        button.equals(SessionAttributes.REQUEST_BUTTON_ADD_NAME)) {
      trigger(manager, "100002", null);
    }
    return 0;
  }

}
