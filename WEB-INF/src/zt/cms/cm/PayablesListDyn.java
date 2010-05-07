/*
 *  Copyright (c) 2004 Email: liwei@zhongtian.biz
 *  $Id: PayablesListDyn.java,v 1.1 2005/06/28 07:00:27 jgo Exp $
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
import zt.cms.cm.common.ClientSum;

/**
 *  该业务名称 客户管理-企业负债情况(List式)
 *
 *@author     Administrator
 *@created    2003年11月29日
 */
public class PayablesListDyn
         extends ListDynBase {
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
        super.triggerListBase("CMCP00");
        return super.buttonEvent(ctx, conn, instance, button, msgs, manager);
    }

    public int preFind(SessionContext ctx, DatabaseConnection conn, FormInstance instance,
        ErrorMessages msgs,
        EventManager manager, PreparedStatement ps, PreparedStatement countps) {
        String value = ClientSum.getClientSum(strClientNO,"CMCORPPAYABLES","AMT",conn);
        String value2 = ClientSum.getClientSum(strClientNO,"CMCORPPAYABLES","BAL",conn);
        ctx.setAfterBody("AMT="+value+";BAL="+value2+"");

        return super.preFind(ctx,conn,instance,msgs,manager,ps,countps);
    }


}



