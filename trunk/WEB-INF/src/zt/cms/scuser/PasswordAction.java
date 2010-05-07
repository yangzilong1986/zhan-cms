package zt.cms.scuser;

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

public class PasswordAction extends FormActions {
    /**
     *  Constructor for the PasswordAction object
     */

    public static Logger logger = Logger.getLogger("zt.cms.scuser.PasswordAction");

    public int load(SessionContext ctx, DatabaseConnection conn, FormInstance instance,ErrorMessages msgs,
            EventManager manager, String parameter) {
        RightChecker.checkReadonly(ctx, conn, instance);
        return 0;
    }

    /**
     *  Constructor for the PasswordAction object
     */
    public PasswordAction() { }


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
    public int buttonEvent(SessionContext ctx, DatabaseConnection conn, FormInstance instance, String button,
            ErrorMessages msgs, EventManager manager) {
        UserManager um = (UserManager) ctx.getAttribute(SystemAttributeNames.USER_INFO_NAME);
        try {
            String newPwd = ctx.getParameter("NEWPWD").trim();
            String confirmPwd = ctx.getParameter("CONFIRMPWD").trim();

            //logger.info("new pwd is : " + newPwd + ", and confirm pwd is " + confirmPwd);

            if (newPwd.length() != 6) {
                msgs.add("新密码应该为六位");
                trigger(manager, instance, EventType.INSERT_VIEW_EVENT_TYPE, Event.BRANCH_CONTINUE_TYPE);
                return 0;
            }



            if (!newPwd.equals(confirmPwd)) {
                msgs.add("确认密码新密码不相同");
                trigger(manager, instance, EventType.INSERT_VIEW_EVENT_TYPE, Event.BRANCH_CONTINUE_TYPE);
                return 0;
            }

            String str = "select userpwd from scuser where userno=" + um.getUserId();
            logger.info("Sql is :" + str);
            RecordSet rs = conn.executeQuery(str);

            if (rs.next()) {
                String password = rs.getString("userpwd");
                if (password.equals(PageAction.getEncryptPassword(ctx.getParameter("OLDPWD").trim()))) {
                    int ret=conn.executeUpdate("update scuser set userpwd='" + PageAction.getEncryptPassword(newPwd) +
                            "' where userno=" +
                            Integer.parseInt(um.getUserId()));
                   if(ret>0){
                       msgs.add("修改密码成功");
                       trigger(manager, instance, EventType.INSERT_VIEW_EVENT_TYPE, Event.BRANCH_CONTINUE_TYPE);
                   }
                } else {
                    msgs.add("原密码不正确");
                    trigger(manager, instance, EventType.INSERT_VIEW_EVENT_TYPE, Event.BRANCH_CONTINUE_TYPE);
                    return 0;
                }
            } else {
                logger.warning("Could not find the user of userno :" + um.getUserId() + ", this hould not take place!");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            return 0;
        }
        return 0;
    }

}
