package zt.cms.bm.bulletin;

import zt.platform.form.control.FormActions;
import java.sql.*;
import zt.platform.db.*;
import zt.platform.form.control.*;
import zt.platform.form.util.*;
import zt.platform.form.util.event.*;
import zt.cms.bm.common.*;
import zt.cmsi.pub.*;

/**
 *  Description of the Class
 *
 *@author     Administrator
 *@created    2004Äê2ÔÂ18ÈÕ
 */
public class BulletinListAction extends FormActions {
    /**
     *  Constructor for the BulletinListAction object
     */
    public BulletinListAction() { }


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
        super.load(ctx, conn, instance, msgs, manager, parameter);
        RightChecker.checkReadonly(ctx, conn, instance);
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
        trigger(manager, "GLBULLETINPAGE", null);
        return 0;
    }

}
