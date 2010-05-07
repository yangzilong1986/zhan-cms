/*
 *  Copyright (c) 2004 Email: liwei@zhongtian.biz
 *  $Id: BlackListMng.java,v 1.1 2005/06/28 07:00:27 jgo Exp $
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
import zt.cms.pub.code.SerialNumber;

/**
 *  测试：多表－－－数据添加,修改，删除(获得值)
 *
 *@author     Administrator
 *@created    2003年12月2日
 */
public class BlackListMng
         extends FormActions {
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

        String strFlag = String.valueOf(ctx.getRequestAttribute("flag"));
        String strSeqNO = ctx.getParameter("SEQNO");
        if (instance.getFormid().equals("CMBL00") && strSeqNO != null) {
            instance.setValue("SEQNO", Integer.parseInt(strSeqNO.trim()));

            if (strFlag.equals("read") || strFlag == null) {
                instance.setReadonly(true);
            } else {
                instance.setReadonly(false);
            }

            trigger(manager, instance, EventType.EDIT_VIEW_EVENT_TYPE, Event.BRANCH_CONTINUE_TYPE);
        }

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
     *@return           Description of the Return Value
     */
    public int beforeInsert(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
            EventManager manager) {
        instance.setFieldReadonly("SEQNO", true);

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
    public int preInsert(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
            EventManager manager, SqlAssistor assistor) {

        int iseqno = (int) SerialNumber.getNextSN("CMBLACKLIST", "SEQNO");
        String strSeqno = String.valueOf(iseqno);
        assistor.setSqlFieldValue(assistor.getDefaultTbl(), "SEQNO", strSeqno);

        if (iseqno < 0) {
            return -1;
        } else {
            return 0;
        }

    }

}
