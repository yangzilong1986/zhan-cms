package zt.cms.bm.app;

/**
 *  Description of the Class
 *
 *@author     Administrator
 *@created    2004Äê1ÔÂ9ÈÕ
 */

import java.util.logging.*;

import zt.platform.db.*;
import zt.platform.form.control.*;
import zt.platform.form.util.*;
import zt.platform.form.util.event.*;

public class BMRLList extends FormActions {

  private static Logger logger = Logger.getLogger("zt.cms.bm.app.BMRLList");

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

  public int postDeleteOk(SessionContext ctx, DatabaseConnection conn,
                          FormInstance instance,
                          ErrorMessages msgs,
                          EventManager manager) {
    zt.cmsi.pub.define.BMReviewLimit.refresh();
    return 0;
  }

  public int buttonEvent(SessionContext ctx, DatabaseConnection conn, FormInstance instance, String button, ErrorMessages msgs, EventManager manager) {

    ctx.setRequestAtrribute("flag", flag);

    if (button != null && button.equals(SessionAttributes.REQUEST_EDIT_BUTTON_VALUE)) {
      trigger(manager, "SYSRLPAGE", null);
    }
    if (button != null && button.equals(SessionAttributes.REQUEST_BUTTON_ADD_NAME)) {
      trigger(manager, "SYSRLPAGE", null);
    }
    return 0;
  }
}
