package zt.cms.bm.inactloan;

/**
 *已置换或核销的不良贷款还款列表
 *
 *@author     wxj
 *@created    2004年5月21日
 */
import java.sql.*;

import zt.cms.cm.common.*;
import zt.platform.db.*;
import zt.platform.form.control.*;
import zt.platform.form.util.*;
import zt.platform.form.util.event.*;
import zt.platform.form.config.*;

public class BMILEXRecallAppList extends FormActions {
  private String ilno = null;
  private String flag = null;
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
    //ilno
    ilno = ctx.getRequestAttribute("ILNO").toString().trim();
//    FormBean fb=instance.getFormBean();
//    ElementBean eb=fb.getElement("ILNO");
//    eb.setIsPrimaryKey(true);
    return 0;
  }

  public int preFind(SessionContext ctx, DatabaseConnection conn,
                     FormInstance instance, ErrorMessages msgs,
                     EventManager manager, PreparedStatement ps,
                     PreparedStatement countps) {
    //String value = ClientSum.getClientSum(ilno, "BMILEXRECALLAPP", "AMT", conn);
    String value = "";
    String getStr = "select sum(amt) from BMILEXRECALLAPP where ilno='"+this.ilno+"'";
    RecordSet rs = conn.executeQuery(getStr);
    if(rs.next()){
        value = DBUtil.doubleToStr1(rs.getDouble(0)) ;
        ctx.setAfterBody("AMT=" + value + "");
    }

    try {
      ps.setString(1, ilno);
      countps.setString(1, ilno);
    }
    catch (Exception ex) {
      return -1;
    }
    return 0;
  }

  public int buttonEvent(SessionContext ctx, DatabaseConnection conn,
                         FormInstance instance, String button,
                         ErrorMessages msgs, EventManager manager) {
    ctx.setRequestAtrribute("flag", flag);
    ctx.setRequestAtrribute("ILNO", ilno);
    if (button.equals(SessionAttributes.REQUEST_EDIT_BUTTON_VALUE)) {
      trigger(manager, "BMILEXRECALLPAGE", null);
    }
    if (button.equals(SessionAttributes.REQUEST_BUTTON_ADD_NAME)) {
      trigger(manager, "BMILEXRECALLPAGE", null);
    }
    return 0;
  }

}
