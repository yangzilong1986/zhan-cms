/*
 *  Copyright (c) 2004 Email: liwei@zhongtian.biz
 *  $Id: ListDynBase.java,v 1.1 2005/06/28 07:00:27 jgo Exp $
 */
package zt.cms.cm;

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
 *  该业务名称 客户管理-List式基类
 *
 *@author     Administrator
 *@created    2003年11月29日
 */
public class ListDynBase
         extends FormActions {
//    public String strDeptName;
//    public String strDeptDesc;
//    public String strDeptTel;
//    public String[] strDeptScope;
//    public String strIsEnable;
//    public String strDeptBoss;
//    public StringBuffer strBuf;

    /**
     *  Description of the Field
     *
     *@author    Administrator
     *@since     2003年12月2日
     */
    protected String strClientNO = null;
    /**
     *  Description of the Field
     *
     *@author    Administrator
     *@since     2003年12月2日
     */
    protected String strFlag = null;
    /**
     *  Description of the Field
     *
     *@author    Administrator
     *@since     2003年12月2日
     */
    protected String toFormid = null;

    public int load(SessionContext ctx, DatabaseConnection conn, FormInstance instance,ErrorMessages msgs,
        EventManager manager, String parameter)
    {
        strClientNO=String.valueOf(ctx.getRequestAttribute("CLIENTNO"));
        strFlag = String.valueOf(ctx.getRequestAttribute("flag"));

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
    public int preFind(SessionContext ctx, DatabaseConnection conn, FormInstance instance,
            ErrorMessages msgs,
            EventManager manager, PreparedStatement ps, PreparedStatement countps) {
        //strClientNO = ctx.getParameter("CLIENTNO");
        //strClientNO=String.valueOf(ctx.getRequestAttribute("CLIENTNO"));
        //strFlag = String.valueOf(ctx.getRequestAttribute("flag"));
        try {
            ps.setString(1, strClientNO);
            countps.setString(1,strClientNO);
            if (strFlag.equals("write")) {
                instance.setReadonly(false);
            } else if (strFlag.equals("read")) {
                instance.setReadonly(true);
            }
            return 0;
        } catch (SQLException se) {
            se.printStackTrace();
            return -1;
        }

    }


    /**
     *  Description of the Method
     *
     *@param  toformid  Description of the Parameter
     */
    public void triggerListBase(String toformid) {
        toFormid = toformid;
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
        ctx.setRequestAtrribute("CLIENTNO", strClientNO);
        ctx.setRequestAtrribute("flag", strFlag);

        if (button != null && button.equals(SessionAttributes.REQUEST_EDIT_BUTTON_VALUE)) {
            trigger(manager, toFormid, null);
        }
        if (button != null && button.equals(SessionAttributes.REQUEST_BUTTON_ADD_NAME)) {
            trigger(manager, toFormid, null);
        }
        return 0;
    }
}
