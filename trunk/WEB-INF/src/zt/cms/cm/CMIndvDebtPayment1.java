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

public class CMIndvDebtPayment1
    extends FormActions {
  private String CLIENTNO=null;
  public static Logger logger = Logger.getLogger("zt.cms.cm.CMIndvDebtPayment1");
  public int load(SessionContext ctx, DatabaseConnection conn,
                  FormInstance instance, ErrorMessages msgs,
                  EventManager manager, String parameter) {
    //flag
    String flag = "read";
    if (ctx.getRequestAttribute("flag") != null) {
      flag = ctx.getRequestAttribute("flag").toString().trim();
    }
    //Readonly
    if (flag.equals("write")) {
      instance.setReadonly(false);
    }
    else {
      instance.setReadonly(true);
    }
    //CLIENTNO,SEQNO
    if (ctx.getRequestAttribute("CLIENTNO") != null) {
      CLIENTNO = ctx.getRequestAttribute("CLIENTNO").toString().trim();
    }
    if (ctx.getParameter("SEQNO") != null) {
      String SEQNO = ctx.getParameter("SEQNO").toString();
      instance.setValue("CLIENTNO", CLIENTNO);
      instance.setValue("SEQNO", SEQNO);
      trigger(manager, instance, EventType.EDIT_VIEW_EVENT_TYPE,
              Event.BRANCH_CONTINUE_TYPE);
    }
    else {
      instance.setValue("CLIENTNO", CLIENTNO);
      trigger(manager, instance, EventType.INSERT_VIEW_EVENT_TYPE,
              Event.BRANCH_CONTINUE_TYPE);
    }
    return 0;
  }

  public int preFind(SessionContext ctx, DatabaseConnection conn,
                     FormInstance instance, ErrorMessages msgs,
                     EventManager manager, SqlAssistor assistor) {
    return 0;
  }

  public int preFind(SessionContext ctx, DatabaseConnection conn,
                     FormInstance instance, ErrorMessages msgs,
                     EventManager manager, PreparedStatement ps,
                     PreparedStatement countps) {
    return 0;
  }

  public int beforeInsert(SessionContext ctx, DatabaseConnection conn,
                          FormInstance instance, ErrorMessages msgs,
                          EventManager manager) {
    instance.setFieldReadonly("CLIENTNO", true);
    instance.setFieldReadonly("SEQNO", true);
    instance.setValue("CLIENTNO", CLIENTNO);

    return 0;
  }

  public int preInsert(SessionContext ctx, DatabaseConnection conn,
                       FormInstance instance, ErrorMessages msgs,
                       EventManager manager, SqlAssistor assistor) {
    String SEQNO = zt.cms.pub.code.SerialNumber.getNextSN("CMINDVDEBTPAYMENT",
        "SEQNO") + "";
    assistor.setSqlFieldValue(assistor.getDefaultTbl(), "SEQNO", SEQNO);
    return 0;
  }

}
