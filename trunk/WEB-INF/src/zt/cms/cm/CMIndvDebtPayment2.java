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
import zt.cms.cm.common.ClientSum;

public class CMIndvDebtPayment2
    extends FormActions {

  public static Logger logger = Logger.getLogger("zt.cms.cm.CMIndvDebtPayment2");
  private String CLIENTNO=null;
  private String flag=null;
  public int load(SessionContext ctx, DatabaseConnection conn,
                  FormInstance instance, ErrorMessages msgs,
                  EventManager manager, String parameter) {
    //flag
    flag="read";
    if(ctx.getRequestAttribute("flag")!=null){
      flag = ctx.getRequestAttribute("flag").toString().trim();
    }
    if(flag.equals("write")){
      instance.setReadonly(false);
    }
    else{
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

    String value2 = ClientSum.getClientSum(CLIENTNO, "CMINDVDEBTPAYMENT", "LOANAMT", conn);
    String value3 = ClientSum.getClientSum(CLIENTNO, "CMINDVDEBTPAYMENT", "REPAIDLOAN", conn);
    String value4 = ClientSum.getClientSum(CLIENTNO, "CMINDVDEBTPAYMENT", "REPAIDINTEREST", conn);

    ctx.setAfterBody("LOANAMT=" + value2 + ";REPAIDLOAN="+value3+ ";REPAIDINTEREST="+value4);

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
      trigger(manager, "100005", null);
    }
    if (button != null &&
        button.equals(SessionAttributes.REQUEST_BUTTON_ADD_NAME)) {
      trigger(manager, "100005", null);
    }
    return 0;
  }

}
