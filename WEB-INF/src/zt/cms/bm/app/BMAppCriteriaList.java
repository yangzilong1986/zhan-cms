package zt.cms.bm.app;

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

public class BMAppCriteriaList
    extends FormActions {

  private static Logger logger = Logger.getLogger("zt.cms.bm.app.BMTypeList");
  private String flag=null;
  public int load(SessionContext ctx, DatabaseConnection conn,
                  FormInstance instance, ErrorMessages msgs,
                  EventManager manager, String parameter) {
    return 0;
  }

  public int preFind(SessionContext ctx, DatabaseConnection conn,
                     FormInstance instance, ErrorMessages msgs,
                     EventManager manager, PreparedStatement ps,
                     PreparedStatement countps) {
    return 0;
  }

  public int postDeleteOk(SessionContext ctx, DatabaseConnection conn,
                          FormInstance instance,
                          ErrorMessages msgs,
                          EventManager manager) {
    zt.cmsi.pub.define.BMAppCriteria.refresh();
    return 0;
  }

  public int buttonEvent(SessionContext ctx, DatabaseConnection conn,
                         FormInstance instance, String button,
                         ErrorMessages msgs, EventManager manager) {
    ctx.setRequestAtrribute("flag",flag);
    if (button != null && button.equals(SessionAttributes.REQUEST_EDIT_BUTTON_VALUE)) {
      trigger(manager, "SYSAPCPAGE", null);
    }
    if (button != null && button.equals(SessionAttributes.REQUEST_BUTTON_ADD_NAME)) {
      trigger(manager, "SYSAPCPAGE", null);
    }
    return 0;
  }

}
