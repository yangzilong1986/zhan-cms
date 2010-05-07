/*
 *  Copyright (c) 2002 Email: sundj@zhongtian.biz
 *  $Id: ListAction.java,v 1.1 2005/06/28 07:00:31 jgo Exp $
 */
package zt.cms.cm.constructingproj;

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
 *@created    2003年11月20日
 *@version    1.0
 */

public class ListAction extends FormActions {
    /**
     *  Description of the Field
     *
     *@author    Administrator
     *@since     2003年12月2日
     */
    String strClientNO = null;
    public static Logger logger = Logger.getLogger("zt.cms.cm.constructingproj.ListAction");


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
        strClientNO=String.valueOf(ctx.getRequestAttribute("CLIENTNO"));

        RightChecker.checkReadonly(ctx, conn, instance);
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
        String clientno = strClientNO;
        try
        {
          countps.setString(1, strClientNO);
        }
        catch(Exception e)
        { }
        if (clientno == null) {
            logger.warning("nono");
            return -1;
        } else {
            logger.info("CLIENTNO : " + clientno);
            instance.setValue("CLIENTNO", clientno);
            try {
                ps.setString(1, clientno);
                countps.setString(1, clientno);
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
            trigger(manager, "CMCPDP", null);
            ctx.setRequestAtrribute("isEdit", "true");
            ctx.setRequestAtrribute("CLIENTNO", strClientNO);
        } else {
            trigger(manager, "CMCPDP", null);
            ctx.setRequestAtrribute("CLIENTNO", strClientNO);
        }
        return 0;
    }

}
