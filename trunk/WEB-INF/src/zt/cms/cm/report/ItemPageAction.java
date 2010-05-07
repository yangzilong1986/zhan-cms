/*
 *  Copyright (c) 2002 Email: sundj@zhongtian.biz
 *  $Id: ItemPageAction.java,v 1.1 2005/06/28 07:00:31 jgo Exp $
 */
package zt.cms.cm.report;


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
 *  Description of the Class
 *
 *@author     Administrator
 *@created    2003Äê11ÔÂ20ÈÕ
 */
public class ItemPageAction extends FormActions {

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

        if (ctx.getRequestAttribute("isEdit") != null) {
            String reportType = ctx.getParameter("REPORTTYPE");
            String itemNo = ctx.getParameter("ITEMNO").trim();
            //System.out.println(reportType + ":" + itemNo);

            instance.setValue("ITEMNO", itemNo);
            instance.setValue("REPORTTYPE", reportType);

            trigger(manager, instance, EventType.EDIT_VIEW_EVENT_TYPE,
                    Event.BRANCH_CONTINUE_TYPE);
        } else if (ctx.getRequestAttribute("addItem") != null) {
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
        String reportType = ctx.getParameter("REPORTTYPE");

        if (reportType != null) {
            //System.out.println("" + reportType);
            instance.setValue("REPORTTYPE", reportType);
            instance.setFieldReadonly("REPORTTYPE", true);

            String sql = "select max(ITEMNO) from CMREPORTITEM where REPORTTYPE='" + reportType + "'";
            RecordSet rs = conn.executeQuery(sql);
            int no = 1;
            if (rs.next()) {
                no = rs.getInt(0);
            }
            no++;

            instance.setValue("ITEMNO", no);
            instance.setValue("PRINTNO", no);
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
     *@param  assistor  Description of the Parameter
     *@return           Description of the Return Value
     */
    public int beforeEdit(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
            EventManager manager, SqlAssistor assistor) {
//        String reportType = ctx.getParameter("REPORTTYPE");
//        String itemNo = ctx.getParameter("ITEMNO");
//        System.out.println(reportType + ":" + itemNo);
//
//        instance.setValue("ITEMNO", itemNo);
//        instance.setValue("REPORTTYPE", reportType);
//
//        assistor.setSqlFieldValue(assistor.getDefaultTbl(), "REPORTTYPE", reportType);
//        assistor.setSqlFieldValue(assistor.getDefaultTbl(), "ITEMNO", itemNo);
        return 0;
    }

}
