/*
 *  Copyright (c) 2002 Email: sundj@zhongtian.biz
 *  $Id: ListAction.java,v 1.1 2005/06/28 07:00:31 jgo Exp $
 */
package zt.cms.cm.corpestate;

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
 *@created    2003Äê11ÔÂ14ÈÕ
 *@version    1.0
 */

public class ListAction extends FormActions {
    /**
     *  Constructor for the ListAction object
     */
    String strClientNO = null;
    public ListAction() { }



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

        trigger(manager, instance, EventType.FIND_EVENT_TYPE,
                Event.BRANCH_CONTINUE_TYPE);

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
        String clientno = strClientNO;
        try
        {
          countps.setString(1, strClientNO);
        }
        catch(Exception e)
        { }

        if (clientno == null) {
            return -1;
        } else {
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
            trigger(manager, "CMESSP", null);
            ctx.setRequestAtrribute("isEdit", "true");
            ctx.setRequestAtrribute("CLIENTNO", strClientNO);
        } else {
            trigger(manager, "CMESSP", null);
            ctx.setRequestAtrribute("CLIENTNO", strClientNO);
        }
        return 0;
    }

}
