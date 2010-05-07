package zt.cms.bm.app;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

import java.util.logging.*;

import zt.cmsi.pub.code.*;
import zt.platform.db.*;
import zt.platform.form.control.*;
import zt.platform.form.util.*;
import zt.platform.form.util.event.*;

public class BMRLPage extends FormActions {
  public static Logger logger = Logger.getLogger("zt.cms.bm.app.BMRLPage");
  private String seqno = null; //业务网点

  public int load(SessionContext ctx, DatabaseConnection conn,
                  FormInstance instance, ErrorMessages msgs,
                  EventManager manager, String parameter) {
    seqno = (String) ctx.getParameter("SEQNO");
    //主键不为空则进入编辑状态
    if (seqno != null) {
      //设置instance主键的值
      instance.setValue("SEQNO", seqno);
      //流程转移到编辑状态
      trigger(manager, instance, EventType.EDIT_VIEW_EVENT_TYPE, Event.BRANCH_CONTINUE_TYPE);
    }
    return 0;
  }

  public int preInsert(SessionContext ctx, DatabaseConnection conn,
                       FormInstance instance, ErrorMessages msgs,
                       EventManager manager, SqlAssistor assistor) {
    int seqNo = (int) BMReviewLimitCode.getNextNo();
    assistor.setSqlFieldValue(assistor.getDefaultTbl(), "SEQNO", seqNo + "");
    return 0;
  }

  public int postInsertOk(SessionContext ctx, DatabaseConnection conn,
                          FormInstance instance, ErrorMessages msgs,
                          EventManager manager) {
    zt.cmsi.pub.define.BMReviewLimit.refresh();
    return 0;
  }

  public int postDeleteOk(SessionContext ctx, DatabaseConnection conn,
                          FormInstance instance,
                          ErrorMessages msgs,
                          EventManager manager) {
    zt.cmsi.pub.define.BMReviewLimit.refresh();
    return 0;
  }

  public int postEditOk(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                        EventManager manager) {
    zt.cmsi.pub.define.BMReviewLimit.refresh();
    return 0;
  }

}
