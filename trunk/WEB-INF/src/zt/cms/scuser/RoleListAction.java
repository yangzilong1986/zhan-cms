/*
 *  Copyright (c) 2002 Email: sundj@zhongtian.biz
 *  $Id: RoleListAction.java,v 1.1 2005/06/28 07:00:35 jgo Exp $
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
 *@created    2003年11月17日
 *@version    1.0
 */

public class RoleListAction extends FormActions {
    /**
     *  Constructor for the RoleListAction object
     */
    public RoleListAction() { }


    /**
     *  Description of the Field
     *
     *@author    Administrator
     *@since     2003年12月2日
     */
    public static Logger logger = Logger.getLogger("zt.cms.scuser.RolePageAction");


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
        trigger(manager, instance, EventType.FIND_EVENT_TYPE, Event.BRANCH_CONTINUE_TYPE);
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
     *@param  ps        Description of the Parameter
     *@param  countps   Description of the Parameter
     *@return           Description of the Return Value
     */
    public int preFind(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
            EventManager manager, PreparedStatement ps, PreparedStatement countps) {
        String userno = ctx.getParameter("USERNO");
        if (userno == null) {
            return -1;
        } else {
            try {
                ps.setString(1, userno);
                countps.setString(1, userno);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return 0;
        }
    }


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
        RightChecker.transReadOnly(ctx, conn, instance);
        if (button != null && button.equals(SessionAttributes.REQUEST_EDIT_BUTTON_VALUE)) {
            trigger(manager, "SCUSRP", null);
            ctx.setRequestAtrribute("isEdit", "true");
        } else {
            trigger(manager, "SCUSRP", null);
        }
        return 0;
    }

}
