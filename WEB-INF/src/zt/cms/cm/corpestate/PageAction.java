/*
 *  Copyright (c) 2002 Email: sundj@zhongtian.biz
 *  $Id: PageAction.java,v 1.1 2005/06/28 07:00:31 jgo Exp $
 */
package zt.cms.cm.corpestate;

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
import java.util.logging.*;
import zt.platform.form.util.event.EventType;
import zt.platform.form.util.event.Event;
import zt.cms.pub.code.SerialNumber;
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
 *@created    2003年11月15日
 *@version    1.0
 */

public class PageAction extends FormActions {

    /**
     *  Description of the Field
     *
     *@author    Administrator
     *@since     2003年12月2日
     */
    String strClientNO = null;
    public static Logger logger = Logger.getLogger("zt.cms.cm.corpestate.PageAction");


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
        return 0;
    }


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
        strClientNO=String.valueOf(ctx.getRequestAttribute("CLIENTNO"));

        if (ctx.getRequestAttribute("isEdit") != null) {
            instance.setValue("CLIENTNO", strClientNO);
            instance.setValue("SEQNO", ctx.getParameter("SEQNO"));
            trigger(manager, instance, EventType.EDIT_VIEW_EVENT_TYPE,
                    Event.BRANCH_CONTINUE_TYPE);
        } else {
            trigger(manager, instance, EventType.INSERT_VIEW_EVENT_TYPE,
                    Event.BRANCH_CONTINUE_TYPE);
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
        instance.setValue("CLIENTNO", strClientNO);
        //instance.setFieldReadonly("SEQNO", true);
        instance.setFieldReadonly("CLIENTNO", true);
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
    public int postInsertOk(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
            EventManager manager) {
        //trigger(manager, "CMESSL", null);
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
     *@param  assistor  Description of the Parameter
     *@return           Description of the Return Value
     */
    public int preInsert(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
            EventManager manager, SqlAssistor assistor) {
        int seqno = (int) SerialNumber.getNextSN("CMCORPESTATE", "SEQNO");
        assistor.setSqlFieldValue(assistor.getDefaultTbl(), "SEQNO", seqno + "");
        return 0;
    }

}
