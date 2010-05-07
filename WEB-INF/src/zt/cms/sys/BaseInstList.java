package zt.cms.sys;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

import java.util.logging.*;

import zt.platform.db.*;
import zt.platform.form.control.*;
import zt.platform.form.util.*;
import zt.platform.form.util.event.*;

public class BaseInstList extends FormActions {

  private static Logger logger = Logger.getLogger("zt.cms.sys.BaseInstList");
  private String flag = null;

  public int load(SessionContext ctx, DatabaseConnection conn,
                  FormInstance instance, ErrorMessages msgs,
                  EventManager manager, String parameter) {
    flag = "write";
    if (ctx.getRequestAttribute("flag") != null) {
      flag = ctx.getRequestAttribute("flag").toString().trim();
    }
    if (flag.equals("write")) {
      instance.setReadonly(false);
    }
    else {
      instance.setReadonly(true);
    }
    flag = "write";
    return 0;
  }

  public int postDeleteOk(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                          EventManager manager) {
    zt.cmsi.pub.define.BMTypeOpen.refresh();
    return 0;
  }

  public int buttonEvent(SessionContext ctx, DatabaseConnection conn, FormInstance instance, String button, ErrorMessages msgs, EventManager manager) {

    ctx.setRequestAtrribute("flag", flag);

    if (button != null && button.equals(SessionAttributes.REQUEST_EDIT_BUTTON_VALUE)) {
      trigger(manager, "SYSBASEPAGE", null);
    }
    if (button != null && button.equals(SessionAttributes.REQUEST_BUTTON_ADD_NAME)) {
      trigger(manager, "SYSBASEPAGE", null);
    }
    return 0;
  }

}
