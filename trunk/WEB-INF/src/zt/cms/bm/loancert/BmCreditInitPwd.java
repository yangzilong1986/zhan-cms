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
 *@created    2003��11��27��
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
      msgs.add("�������󣬲������󲻴��ڣ�");
      return -1;
    }
    //strClientNO
    clientNo = (String) paramg.getParam(ParamName.CLientNo);
    if (clientNo == null) {
      msgs.add("�������󣬿ͻ��Ų����ڣ�");
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
        msgs.add("������Ӧ��Ϊ��λ");
        trigger(manager, instance, EventType.INSERT_VIEW_EVENT_TYPE,
                Event.BRANCH_CONTINUE_TYPE);
        return 0;
      }

      if (!newPwd.equals(confirmPwd)) {
        msgs.add("ȷ������������벻��ͬ");
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
          msgs.add("��������ɹ�");
          trigger(manager, instance, EventType.INSERT_VIEW_EVENT_TYPE,
                  Event.BRANCH_CONTINUE_TYPE);
        }
        else {
          msgs.add("��������ʧ��!");
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
