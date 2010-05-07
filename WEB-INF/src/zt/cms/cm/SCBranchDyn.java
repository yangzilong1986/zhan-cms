/*
 *  Copyright (c) 2004 Email: liwei@zhongtian.biz
 *  $Id: SCBranchDyn.java,v 1.1 2005/06/28 07:00:30 jgo Exp $
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
 *  测试：多表－－－数据添加,修改，删除(获得值)
 *
 *@author     Administrator
 *@created    2003年12月2日
 */
public class SCBranchDyn
         extends FormActions {
//    public String strDeptName;
//    public String strDeptDesc;
//    public String strDeptTel;
//    public String[] strDeptScope;
//    public String strIsEnable;
//    public String strDeptBoss;
//    public StringBuffer strBuf;

    private String clientno;


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
        String strclientno = ctx.getParameter("CLIENTNO");
        if (strclientno != null) {
            try {
                ps.setString(1, strclientno);
                countps.setString(1, strclientno);
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
                return -1;
            }
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
        if ((button != null) && button.equals(SessionAttributes.REQUEST_EDIT_BUTTON_VALUE)) {
            this.trigger(manager, "CMSB00", null);
        }
        if ((button != null) && button.equals(SessionAttributes.REQUEST_BUTTON_ADD_NAME)) {
            this.trigger(manager, "CMSB00", null);
        }

        //
        return 0;
    }
}
