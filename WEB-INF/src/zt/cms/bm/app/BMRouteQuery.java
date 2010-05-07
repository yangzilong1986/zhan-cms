package zt.cms.bm.app;


import java.sql.*;
import java.util.logging.*;

import zt.platform.db.*;
import zt.platform.form.control.*;
import zt.platform.form.util.*;
import zt.platform.form.util.event.*;

/**
 *  Description of the Class
 *
 *@author     Administrator
 *@created    2004Äê1ÔÂ9ÈÕ
 */
public class BMRouteQuery extends FormActions {

    private static Logger logger = Logger.getLogger("zt.cms.bm.app.BMRouteQuery");
//    private String CHECKPOINT = null;
    private String flag = null;


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
    public int load(SessionContext ctx, DatabaseConnection conn,
            FormInstance instance, ErrorMessages msgs,
            EventManager manager, String parameter) {
        flag = "write";
        if (ctx.getRequestAttribute("flag") != null) {
            flag = ctx.getRequestAttribute("flag").toString().trim();
        }
        if (flag.equals("write")) {
            instance.setReadonly(false);
        } else {
            instance.setReadonly(true);
        }
        flag = "write";
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
    public int preFind(SessionContext ctx, DatabaseConnection conn,
            FormInstance instance, ErrorMessages msgs,
            EventManager manager, PreparedStatement ps,
            PreparedStatement countps) {
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
    public int buttonEvent(SessionContext ctx, DatabaseConnection conn, FormInstance instance, String button, ErrorMessages msgs, EventManager manager) {

        ctx.setRequestAtrribute("flag", flag);

        if (button != null && button.equals(SessionAttributes.REQUEST_EDIT_BUTTON_VALUE)) {
            trigger(manager, "SYSROUTEPAGE", null);
        }
        if (button != null && button.equals(SessionAttributes.REQUEST_BUTTON_ADD_NAME)) {
            trigger(manager, "SYSROUTEPAGE", null);
        }
        return 0;
    }
}
