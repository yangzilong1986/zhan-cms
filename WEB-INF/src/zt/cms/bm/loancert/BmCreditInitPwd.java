package zt.cms.bm.loancert;

/**
 * <p>Title: New Porgram</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: Dreem</p>
 * @author JGO(GZL)
 * @version 1.1
 */

import zt.platform.form.control.FormActions;
import zt.platform.form.control.FormActions;
import zt.platform.form.control.FormActions;
import zt.platform.form.control.*;
import zt.platform.db.DatabaseConnection;
import zt.platform.db.RecordSet;
import zt.platform.form.util.FormInstance;
import zt.platform.form.util.event.ErrorMessages;
import zt.platform.form.util.event.EventManager;
import zt.platform.form.util.SqlAssistor;
import java.sql.PreparedStatement;
import java.sql.*;
import zt.platform.form.util.event.EventType;
import zt.platform.form.util.event.Event;
import zt.platform.form.util.SessionAttributes;

import zt.cms.pub.code.SerialNumber;
import zt.cms.cm.common.RightChecker;
import zt.platform.user.*;
import zt.cms.pub.*;
import zt.platform.db.*;
import zt.platform.form.config.*;
import zt.platform.form.control.*;
import zt.platform.form.util.*;
import zt.platform.form.util.event.*;
import java.util.logging.*;
import zt.cms.cm.common.RightChecker;
import zt.cmsi.pub.*;
import com.ebis.encrypt.*;

/**
 *  <p>
 *
 *  Title: </p> <p>
 *
 *  Description: </p> <p>
 *
 *  Copyright: Copyright (c) 2003</p> <p>
 *
 *  Company: </p>
 *
 *@author     not attributable
 *@created    2003年11月27日
 *@version    1.0
 */

public class BmCreditInitPwd
    extends FormActions {
  /**
   *  Constructor for the PasswordAction object
   */

  public static Logger logger = Logger.getLogger("zt.cms.scuser.PasswordAction");
  private String clientNo;
  private Param paramg = null;

  public int load(SessionContext ctx, DatabaseConnection conn,
                  FormInstance instance, ErrorMessages msgs,
                  EventManager manager, String parameter) {
    paramg = (Param) ctx.getRequestAttribute(ParamName.ParamName);
    if (paramg == null) {
      msgs.add("参数错误，参数对象不存在！");
      return -1;
    }
    //strClientNO
    clientNo = (String) paramg.getParam(ParamName.CLientNo);
    if (clientNo == null) {
      msgs.add("参数错误，客户号不存在！");
      return -1;
    }
    RightChecker.checkReadonly(ctx, conn, instance);
    return 0;
  }

  /**
   *  Constructor for the PasswordAction object
   */
  public BmCreditInitPwd() {}

  /**
   *  Description of the Method
   *
   *@param  ctx       Description of the Parameter
   *@param  conn      Description of the Parameter
   *@param  instance  Description of the Parameter
   *@param  button    Description of the Parameter
   *@param  msgs      Description of the Parameter
   *@param  manager   Description of the Parameter
   *@return           Description of the Return Value
   */
  public int buttonEvent(SessionContext ctx, DatabaseConnection conn,
                         FormInstance instance, String button,
                         ErrorMessages msgs, EventManager manager) {
    UserManager um = (UserManager) ctx.getAttribute(SystemAttributeNames.
        USER_INFO_NAME);
    try {
      String newPwd = ctx.getParameter("NEWPWD").trim();
      String confirmPwd = ctx.getParameter("CONFIRMPWD").trim();

      //logger.info("new pwd is : " + newPwd + ", and confirm pwd is " + confirmPwd);

      if (newPwd.length() != 6) {
        msgs.add("新密码应该为六位");
        trigger(manager, instance, EventType.INSERT_VIEW_EVENT_TYPE,
                Event.BRANCH_CONTINUE_TYPE);
        return 0;
      }

      if (!newPwd.equals(confirmPwd)) {
        msgs.add("确认密码和新密码不相同");
        trigger(manager, instance, EventType.INSERT_VIEW_EVENT_TYPE,
                Event.BRANCH_CONTINUE_TYPE);
        return 0;
      }

      zt.platform.cachedb.ConnectionManager db = zt.platform.cachedb.
          ConnectionManager.getInstance();
      String password = db.getCellValue("passwd", "bmcreditlimit",
                                        "typeno=1 and clientno='" + clientNo +
                                        "'");

      EncryptData ed = new EncryptData();
      //String encrypted = new String(ed.enPasswd(passwd.getBytes()));

      if (true) {
        boolean ret = db.ExecCmd("update bmcreditlimit set passwd='" +
                                 new String(ed.enPasswd(newPwd.getBytes())) +
                                 "' where typeno=1 and clientno='" + clientNo+ "'");
        if (ret == true) {
          msgs.add("设置密码成功");
          trigger(manager, instance, EventType.INSERT_VIEW_EVENT_TYPE,
                  Event.BRANCH_CONTINUE_TYPE);
        }
        else {
          msgs.add("设置密码失败!");
          trigger(manager, instance, EventType.INSERT_VIEW_EVENT_TYPE,
                  Event.BRANCH_CONTINUE_TYPE);
        }
      }


    }
    catch (Exception ex) {
      ex.printStackTrace();
      return 0;
    }
    return 0;
  }

}
