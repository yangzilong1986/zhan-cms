package zt.cms.bm.inactloan;

import java.util.logging.*;
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
 *@created    2004年1月5日
 */
public class NotifiListAction extends CommonAction {
  /**
   *  Gets the pageFormId attribute of the NotifiListAction object
   *
   *@return    The pageFormId value
   */
  private static Logger logger = Logger.getLogger("zt.cms.bm.inactloan.NotifiListAction");
  public int buttonEvent(SessionContext ctx, DatabaseConnection conn, FormInstance instance, String button,
                         ErrorMessages msgs, EventManager manager) {

    ctx.setRequestAtrribute("BMNO", this.bmno);
    Param param = new Param();
    param.addParam("BMNO", this.bmno);
    ctx.setRequestAtrribute("BMPARAM", param);
    //BMILNOTIFIPAGE
    RightChecker.transReadOnly(ctx, conn, instance);
    if(button==null) return 0;
    if (button.equals(SessionAttributes.REQUEST_BUTTON_ADD_NAME)) {
      trigger(manager, getPageFormId(), null);
    }
    if (button.equals(SessionAttributes.REQUEST_EDIT_BUTTON_VALUE)) {
      //打印
      String CLICK_COLUMN_NAME=ctx.getParameter(SessionAttributes.CLICK_COLUMN_NAME);
      if (CLICK_COLUMN_NAME!=null && CLICK_COLUMN_NAME.equals("print")) {
        ctx.setRequestAtrribute("BMNO", this.bmno);
        ctx.setTarget("/jspreport/bmilnotifi.jsp");
      }
      //详细
      else{
        trigger(manager, getPageFormId(), null);
      }
    }
    return 0;
  }

  public String getPageFormId() {
    return "BMILNOTIFIPAGE";
  }
}
