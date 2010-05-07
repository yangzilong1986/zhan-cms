package zt.cms.bm.app;
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

public class BMAppCriteriaPage extends FormActions {
  public static Logger logger = Logger.getLogger(
    "zt.cms.bm.app.BMRLPage");

  private String flag = null; //窗体是否可读
  private String BRHID = null; //业务网点

  public int load(SessionContext ctx, DatabaseConnection conn,
                  FormInstance instance, ErrorMessages msgs,
                  EventManager manager, String parameter) {
    //主键、内存变量类型的数据或一些需要特殊处理的数据库字段数据
    String typeno, checkpoint;
    BRHID = (String) ctx.getParameter("BRHID");
    typeno = (String) ctx.getParameter("TYPENO");
    checkpoint = (String) ctx.getParameter("CHECKPOINT");

    //主键不为空则进入编辑状态
    if (BRHID != null) {
      //设置instance主键的值
      instance.setValue("BRHID", BRHID);
      instance.setValue("TYPENO", typeno);
      instance.setValue("CHECKPOINT", checkpoint);
      //流程转移到编辑状态
      trigger(manager, instance, EventType.EDIT_VIEW_EVENT_TYPE,
              Event.BRANCH_CONTINUE_TYPE);
    }
    return 0;
  }
  public int postInsertOk(SessionContext ctx, DatabaseConnection conn,
                          FormInstance instance, ErrorMessages msgs,
                          EventManager manager) {
    zt.cmsi.pub.define.BMAppCriteria.refresh();
    return 0;
  }

  public int postDeleteOk(SessionContext ctx, DatabaseConnection conn,
                          FormInstance instance,
                          ErrorMessages msgs,
                          EventManager manager) {
    zt.cmsi.pub.define.BMAppCriteria.refresh();
    return 0;
  }

  public int postEditOk(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                        EventManager manager) {
    zt.cmsi.pub.define.BMAppCriteria.refresh();
    return 0;
  }

}
