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

public class BMRoutePage extends FormActions {
  public static Logger logger = Logger.getLogger("zt.cms.bm.app.BMRoutePage");

  private String flag = null; //窗体是否可读
  private String bmActType = null; //业务类型代码

  public int load(SessionContext ctx, DatabaseConnection conn,
                  FormInstance instance, ErrorMessages msgs,
                  EventManager manager, String parameter) {
    //主键、内存变量类型的数据或一些需要特殊处理的数据库字段数据
    bmActType = (String) ctx.getParameter("BMACTTYPE");
    System.out.println(ctx.getRequestAttribute("BMACTTYPE") +"1*************************************");

    //窗体的读写控制
    flag = (String) ctx.getRequestAttribute("flag");
    System.out.println(ctx.getRequestAttribute("flag") +"1*************************************");
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
    if (bmActType != null) {
      //设置instance主键的值
      instance.setValue("BMACTTYPE", bmActType);
      //流程转移到编辑状态
      trigger(manager, instance, EventType.EDIT_VIEW_EVENT_TYPE,Event.BRANCH_CONTINUE_TYPE);
      System.out.println(bmActType+"2*************************************");
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
