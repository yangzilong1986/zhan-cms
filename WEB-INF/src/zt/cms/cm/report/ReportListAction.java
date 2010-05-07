/*
 *  Copyright (c) 2002 Email: sundj@zhongtian.biz
 *  $Id: ReportListAction.java,v 1.1 2005/06/28 07:00:31 jgo Exp $
 */
package zt.cms.cm.report;

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
 *@created    2003Äê11ÔÂ24ÈÕ
 *@version    1.0
 */

public class ReportListAction extends FormActions {
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
    private String entityNo;

    public int load(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
            EventManager manager, String parameter) {
        RightChecker.checkReadonly(ctx, conn, instance);
        //String entityNo = ctx.getParameter("CLIENTNO");
        String entityNo = String.valueOf(ctx.getRequestAttribute("CLIENTNO"));

        this.entityNo=entityNo;
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
        //instance.setReadonly();
//        String entityNo = ctx.getParameter("CLIENTNO");
//        if(entityNo != null && entityNo.trim().length() != 0){
//            this.entityNo=entityNo;
//        }

//        if (entityNo == null || entityNo.trim().length() == 0) {
//            entityNo = ctx.getParameter("entityNo");
//            if (entityNo == null || entityNo.trim().length() == 0) {
//                entityNo = ctx.getParameter("ENTITYNO");
//                if(entityNo == null || entityNo.trim().length() == 0){
//                    //entityNo=(String)ctx.getAttribute("ENTITYNO");
//                   entityNo= this.entityNo;
//                }
//            }
//        }
//        System.out.println("=============" + entityNo);

        try {
            ps.setString(1, this.entityNo);
            countps.setString(1, this.entityNo);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return 0;
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
        //ctx.setRequestAtrribute("ENTITYNO",this.entityNo);
        ctx.setTarget("/report/report.jsp?ENTITYNO="+this.entityNo);
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
    public int preDelete(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
            EventManager manager, SqlAssistor assistor) {
        return 0;
    }

}
