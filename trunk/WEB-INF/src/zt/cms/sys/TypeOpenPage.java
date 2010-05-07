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

import zt.cmsi.pub.code.*;
import zt.platform.db.*;
import zt.platform.form.control.*;
import zt.platform.form.util.*;
import zt.platform.form.util.event.*;

public class TypeOpenPage extends FormActions {
  public static Logger logger = Logger.getLogger("zt.cms.bm.app.BMRLPage");
  private String flag = null; //窗体是否可读
  private String seqno = null; //业务网点

  public int load(SessionContext ctx, DatabaseConnection conn,
                  FormInstance instance, ErrorMessages msgs,
                  EventManager manager, String parameter) {
    //主键BRHID
    seqno = (String) ctx.getParameter("SEQNO");
    //窗体的读写控制
    flag = (String) ctx.getRequestAttribute("flag");
    if (flag == null) {
      flag = "read";
    }
    if (flag.equals("write")) {
      instance.setReadonly(false);
    }
    else {
      instance.setReadonly(true);
    }
    //主键不为空则进入编辑状态
    if (seqno != null) {
      instance.setValue("SEQNO", seqno);
      //流程转移到编辑状态
      trigger(manager, instance, EventType.EDIT_VIEW_EVENT_TYPE, Event.BRANCH_CONTINUE_TYPE);
    }
    return 0;
  }

  /**
   * 扩展基类的beforeEdit方法，点击“添加”按钮后响应的事件
   */
  public int beforeEdit(SessionContext ctx, DatabaseConnection conn,
                        FormInstance instance, ErrorMessages msgs,
                        EventManager manager, SqlAssistor assistor) {
    return 0;
  }

  /**
   * 扩展基类的preEdit方法，对实际业务做edit前的处理
   */
  public int preEdit(SessionContext ctx, DatabaseConnection conn,
                     FormInstance instance, ErrorMessages msgs,
                     EventManager manager, SqlAssistor assistor) {
    return 0;
  }

  public int preInsert(SessionContext ctx, DatabaseConnection conn,
                       FormInstance instance, ErrorMessages msgs,
                       EventManager manager, SqlAssistor assistor) {
    int seqNo = (int) BMTypeOpen.getNextNo();
    assistor.setSqlFieldValue(assistor.getDefaultTbl(), "SEQNO", seqNo + "");
    return 0;
  }

  public int postInsertOk(SessionContext ctx, DatabaseConnection conn,
                          FormInstance instance, ErrorMessages msgs,
                          EventManager manager) {
    zt.cmsi.pub.define.BMTypeOpen.refresh();
    return 0;
  }

  public int postDeleteOk(SessionContext ctx, DatabaseConnection conn,
                          FormInstance instance,
                          ErrorMessages msgs,
                          EventManager manager) {
    zt.cmsi.pub.define.BMTypeOpen.refresh();
    return 0;
  }

  public int postEditOk(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                        EventManager manager) {
    zt.cmsi.pub.define.BMTypeOpen.refresh();
    return 0;
  }
}
