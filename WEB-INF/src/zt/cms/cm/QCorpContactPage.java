package zt.cms.cm;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

import zt.cms.pub.code.*;
import zt.platform.db.*;
import zt.platform.form.control.*;
import zt.platform.form.util.*;
import zt.platform.form.util.event.*;

public class QCorpContactPage
    extends FormActions {
  private String strClientNO = null;
  private String strFlag = null;
  public int load(SessionContext ctx, DatabaseConnection conn, FormInstance instance,
                  ErrorMessages msgs,
                  EventManager manager, String parameter) {
    strClientNO = String.valueOf(ctx.getRequestAttribute("CLIENTNO"));
    strFlag = String.valueOf(ctx.getRequestAttribute("flag"));
    String strSeqNO = ctx.getParameter("SEQNO");

    if (strFlag.equals("write")) {
      instance.setReadonly(false);
    }
    else {
      instance.setReadonly(true);

    }
    if (strSeqNO != null) {
      instance.setValue("CLIENTNO", strClientNO);
      instance.setValue("SEQNO", Integer.parseInt(strSeqNO.trim()));

      trigger(manager, instance, EventType.EDIT_VIEW_EVENT_TYPE, Event.BRANCH_CONTINUE_TYPE);
    }

    return 0;
  }

  public int beforeInsert(SessionContext ctx, DatabaseConnection conn, FormInstance instance,
                          ErrorMessages msgs,
                          EventManager manager) {
    instance.setFieldReadonly("CLIENTNO", true);
    instance.setFieldReadonly("SEQNO", true);

    instance.setValue("CLIENTNO", strClientNO);

    return 0;
  }

  public int preInsert(SessionContext ctx, DatabaseConnection conn, FormInstance instance,
                       ErrorMessages msgs,
                       EventManager manager, SqlAssistor assistor) {
    int iseqno = (int) SerialNumber.getNextSN("CMCORPCONTACT", "SEQNO");
    String strSeqno = String.valueOf(iseqno);

    assistor.setSqlFieldValue(assistor.getDefaultTbl(), "SEQNO", strSeqno);

    if (strClientNO == null || iseqno < 0) {
      return -1;
    }
    else {
      return 0;
    }

  }

}
