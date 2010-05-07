package zt.cms.bm.bulletin;

import zt.platform.form.control.FormActions;

import java.sql.*;

import zt.platform.db.*;
import zt.platform.form.control.*;
import zt.platform.form.util.*;
import zt.platform.form.util.event.*;
import zt.cms.pub.code.*;
import zt.cmsi.pub.*;
import zt.cms.pub.*;
import zt.cms.bm.common.ParamFactory;
import zt.cms.bm.common.*;

/**
 *  Description of the Class
 *
 *@author     Administrator
 *@created    2004Äê2ÔÂ18ÈÕ
 */
public class BulletinPageAction extends FormActions {
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
        if (ctx.getParameter("SEQNO") != null) {
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
        instance.setValue("PUBLISHEDBY", SessionInfo.getLoginUserName(ctx));
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
    public int preInsert(SessionContext ctx, DatabaseConnection conn,
            FormInstance instance, ErrorMessages msgs,
            EventManager manager, SqlAssistor assistor) {
        int seqNo = (int) SerialNumber.getNextSN(assistor.getDefaultTbl(), "SEQNO");
        assistor.setSqlFieldValue(assistor.getDefaultTbl(), "SEQNO",seqNo + "");

        String operator = SessionInfo.getLoginUserName(ctx);
        instance.setValue("PUBLISHEDBY", operator);
        assistor.setSqlFieldValue(assistor.getDefaultTbl(), "PUBLISHEDBY",
                operator + "");

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
    public int preEdit(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
            EventManager manager, SqlAssistor assistor) {
        String operator = SessionInfo.getLoginUserName(ctx);
        instance.setValue("PUBLISHEDBY", operator);
        assistor.setSqlFieldValue(assistor.getDefaultTbl(), "PUBLISHEDBY",
                operator + "");
        return 0;
    }

}
