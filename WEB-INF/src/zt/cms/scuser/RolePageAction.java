/*
 *  Copyright (c) 2002 Email: sundj@zhongtian.biz
 *  $Id: RolePageAction.java,v 1.1 2005/06/28 07:00:35 jgo Exp $
 */
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
import java.util.logging.*;
import zt.cms.cm.common.RightChecker;
import zt.cmsi.pub.define.UserRoleMan;

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
 *@created    2003年11月17日
 *@version    1.0
 */

public class RolePageAction extends FormActions {
    /**
     *  Constructor for the RolePageAction object
     *
     *@author    Administrator
     *@since     2003年12月2日
     */

    public static Logger logger = Logger.getLogger("zt.cms.scuser.RolePageAction");


    /**
     *  Constructor for the RolePageAction object
     */
    public RolePageAction() { }


    /**
     *  Description of the Method
     *
     *@param  ctx        Description of the Parameter
     *@param  conn       Description of the Parameter
     *@param  instance   Description of the Parameter
     *@param  msgs       Description of the Parameter
     *@param  manager    Description of the Parameter
     *@param  parameter  Description of the Parameter
     *@return            Description of the Return Value
     */
    public int load(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
            EventManager manager, String parameter) {
        RightChecker.checkReadonly(ctx, conn, instance);
        instance.setValue("USERNO", ctx.getParameter("USERNO").trim());
        if (ctx.getAttribute("isEdit") != null) {
            trigger(manager, instance, EventType.EDIT_VIEW_EVENT_TYPE, Event.BRANCH_CONTINUE_TYPE);
        } else {
            trigger(manager, instance, EventType.INSERT_VIEW_EVENT_TYPE, Event.BRANCH_CONTINUE_TYPE);
        }
        return 0;
    }


    /**
     *  Description of the Method
     *
     *@param  ctx       Description of the Parameter
     *@param  conn      Description of the Parameter
     *@param  instance  Description of the Parameter
     *@param  msgs      Description of the Parameter
     *@param  manager   Description of the Parameter
     *@return           Description of the Return Value
     */
    public int beforeInsert(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
            EventManager manager) {
        int userno = Integer.parseInt(ctx.getParameter("USERNO").trim());
        instance.setValue("USERNO", userno);
        instance.setFieldReadonly("USERNO", true);
        UserRoleMan.setDirty(true);
        return 0;
    }

    public int postEditOk(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
            EventManager manager) {
        UserRoleMan.setDirty(true);
        return 0;
    }

    public int postDeleteOk(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
            EventManager manager) {
        UserRoleMan.setDirty(true);
        return 0;
    }

    public int postInsertOk(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
            EventManager manager) {
        UserRoleMan.setDirty(true);
        return 0;
    }
}
