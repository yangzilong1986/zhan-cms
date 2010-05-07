package zt.cms.bm.appplus;

import zt.cms.bm.common.*;
import zt.cmsi.pub.*;
import zt.platform.db.*;
import zt.platform.form.config.*;
import zt.platform.form.control.*;
import zt.platform.form.util.*;
import zt.platform.form.util.event.*;

/**
 *  Description of the Class
 *
 *@author     Administrator
 *@created    2004Äê1ÔÂ2ÈÕ
 * modified  by  yusg   2004/04/27
 */
public class PledgePageAction extends CommonPageAction {
  /**
   *  Constructor for the PledgePageAction object
   */
  public PledgePageAction() {}

  /**
   *  Gets the tableName attribute of the PledgePageAction object
   *
   *@return    The tableName value
   */
  public String getTableName() {
    return "BMPLDGMORT";
  }

  public int load(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                  EventManager manager, String parameter) {

    RightChecker.checkReadonly(ctx, conn, instance);
    Param param = (Param) ctx.getRequestAttribute("BMPARAM");
    this.bmNo = param.getBmNo();
    String formid = instance.getFormid();
    if (formid.endsWith("1")) {
      instance.useCloneFormBean();
      FormBean fb = instance.getFormBean();
      ElementBean eb = fb.getElement("PLEDGETYPE");
      eb.setEnutpname("PledgeTypeB");
    }

    if (ctx.getParameter("PLEDGENO") != null && !ctx.getParameter("PLEDGENO").equals("")) {
      instance.setValue("PLEDGENO", ctx.getParameter("PLEDGENO"));
      trigger(manager, instance, EventType.EDIT_VIEW_EVENT_TYPE,
              Event.BRANCH_CONTINUE_TYPE);
    }
    else {
      trigger(manager, instance, EventType.INSERT_VIEW_EVENT_TYPE,
              Event.BRANCH_CONTINUE_TYPE);
    }
    return 0;
  }

}
