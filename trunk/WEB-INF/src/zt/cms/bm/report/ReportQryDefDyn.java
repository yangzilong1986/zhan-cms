/*
 *  Copyright (c) 2004 Email: liwei@zhongtian.biz
 *  $Id: ReportQryDefDyn.java,v 1.1 2005/06/28 07:00:26 jgo Exp $
 */
package zt.cms.bm.report;

import zt.platform.db.DatabaseConnection;
import zt.platform.db.RecordSet;
import zt.platform.form.util.FormInstance;
import zt.platform.form.util.event.ErrorMessages;
import zt.platform.form.util.event.EventManager;
import zt.platform.form.util.SqlAssistor;
import zt.platform.form.util.SqlField;
import java.sql.PreparedStatement;
import zt.platform.form.control.SessionContext;
import zt.platform.form.control.FormActions;
import zt.platform.form.util.*;
import zt.platform.form.util.event.*;
import zt.platform.form.util.datatype.*;
import java.sql.*;
/**
 *  ���ԣ���������������,�޸ģ�ɾ��(���ֵ)
 *
 *@author     Administrator
 *@created    2003��12��2��
 */
public class ReportQryDefDyn
         extends FormActions {

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
        String strFlag = String.valueOf(ctx.getAttribute("flag"));

        if (strFlag.equals("read") || strFlag == null) {
            instance.setReadonly(true);
        } else {
            instance.setReadonly(false);
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
    public int buttonEvent(SessionContext ctx, DatabaseConnection conn, FormInstance instance,
            String button,
            ErrorMessages msgs, EventManager manager) {
        System.out.println("button:"+ button);
        if ((button != null) && (button.equals(SessionAttributes.REQUEST_BUTTON_ADD_NAME)
                                 || button.equals(SessionAttributes.REQUEST_EDIT_BUTTON_VALUE))) {
            this.trigger(manager, "REPORTQRYDEF0", null);
        }


        //
        return 0;
    }


}
