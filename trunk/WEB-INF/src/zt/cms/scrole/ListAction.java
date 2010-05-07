/*
 *  Copyright (c) 2002 Email: sundj@zhongtian.biz
 *  $Id: ListAction.java,v 1.1 2005/06/28 07:00:32 jgo Exp $
 */
package zt.cms.scrole;

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
 *@created    2003Äê11ÔÂ17ÈÕ
 *@version    1.0
 */

public class ListAction extends FormActions {
    /**
     *  Constructor for the ListAction object
     */
    public ListAction() { }


    /**
     *  Description of the Method
     *
     *@param  ctx       Description of the Parameter
     *@param  conn      Description of the Parameter
     *@param  instance  Description of the Parameter
     *@param  button    Description of the Parameter
     *@param  msgs      Description of the Parameter
     *@param  manager   Description of the Parameter
     *@return           Description of the Return Value /** Description of the
     *      Method
     *@return           Description of the Return Value
     */
    public int buttonEvent(SessionContext ctx, DatabaseConnection conn, FormInstance instance, String button,
            ErrorMessages msgs, EventManager manager) {
        RightChecker.transReadOnly(ctx, conn, instance);
        if (button != null && button.equals(SessionAttributes.REQUEST_BUTTON_ADD_NAME)) {
            trigger(manager, "SCROLP", null);
        } else {
            trigger(manager, "SCROLP", null);
        }
        return 0;
    }

}
