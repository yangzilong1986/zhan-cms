package zt.cms.bm.app;

import java.util.logging.*;

import zt.cmsi.pub.*;
import zt.platform.db.*;
import zt.platform.form.config.*;
import zt.platform.form.control.*;
import zt.platform.form.util.*;
import zt.platform.form.util.event.*;
import zt.platform.user.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class BMTypePage extends FormActions {
  public static Logger logger = Logger.getLogger("zt.cms.bm.app.BMTypePage");

  private String flag = null; //窗体是否可读
  private String typeNo = null; //业务类型代码

  public int load(SessionContext ctx, DatabaseConnection conn,
                  FormInstance instance, ErrorMessages msgs,
                  EventManager manager, String parameter) {
    //主键、内存变量类型的数据或一些需要特殊处理的数据库字段数据
    typeNo = (String) ctx.getParameter("TYPENO");
    //窗体的读写控制
    System.out.println(typeNo);
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
    System.out.println(flag);
    //主键不为空则进入编辑状态
    if (typeNo != null) {
      //设置instance主键的值
      instance.setValue("TYPENO", typeNo);
      //流程转移到编辑状态
      trigger(manager, instance, EventType.EDIT_VIEW_EVENT_TYPE,Event.BRANCH_CONTINUE_TYPE);
      return 0;
    }
    //主键为空，进入添加状态
    else{
      //默认状态trigger到添加状态
      return 0;
    }
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

}
