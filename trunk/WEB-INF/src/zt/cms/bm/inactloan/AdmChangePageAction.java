package zt.cms.bm.inactloan;

import zt.platform.form.control.FormActions;

import java.sql.*;

import zt.platform.db.*;
import zt.platform.form.control.*;
import zt.platform.form.util.*;
import zt.platform.form.util.event.*;
import zt.cms.pub.code.*;
import zt.cmsi.pub.*;
import zt.cms.pub.*;
import zt.cms.bm.common.*;
import zt.cmsi.pub.define.*;
import zt.cmsi.mydb.MyDB;
import zt.cmsi.biz.*;

/**
 *  Description of the Class
 *
 *@author     Administrator
 *@created    2004Äê1ÔÂ5ÈÕ
 */
public class AdmChangePageAction extends FormActions {
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
    String bmno="";

    public int load(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
            EventManager manager, String parameter) {
        RightChecker.checkReadonly(ctx,conn,instance);


        Param param = (Param) ctx.getRequestAttribute("PARAM");
        this.bmno = ((Param)ctx.getRequestAttribute("BMPARAM")).getBmNo();
        if (ctx.getRequestAttribute("couldNotChange") != null) {
            instance.setValue("SEQNO",ctx.getParameter("SEQNO"));
            trigger(manager, instance, EventType.EDIT_VIEW_EVENT_TYPE,
                    Event.BRANCH_CONTINUE_TYPE);
            instance.setReadonly(true);
        } else {

            trigger(manager, instance, EventType.INSERT_VIEW_EVENT_TYPE,
                    Event.BRANCH_CONTINUE_TYPE);
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
        instance.setValue("OLDADM", ctx.getParameter("ADMINEDBY"));
        instance.setValue("CREATEDATE", SystemDate.getSystemDate5(""));
        instance.setFieldReadonly("OLDADM", true);
        instance.setFieldReadonly("CREATEDATE", true);
        instance.setValue("BMNO", this.bmno);
        instance.setFieldReadonly("BMNO", true);
        instance.setValue("OPERATOR", SessionInfo.getLoginUserName(ctx));
        instance.setValue("OLDADM",InactLoanMan.getInactLoan(this.bmno).adminedBy) ;


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
    public int preInsert(SessionContext ctx, DatabaseConnection conn,
            FormInstance instance, ErrorMessages msgs,
            EventManager manager, SqlAssistor assistor) {
        int seqNo = (int) SerialNumber.getNextSN("BMLOANADMCHANGE", "SEQNO");
        //System.out.println("Seqno:"+seqNo);

        instance.setValue("SEQNO", seqNo);
        assistor.setSqlFieldValue(assistor.getDefaultTbl(), "SEQNO",
                seqNo + "");
        MyDB.getInstance().addDBConn(conn);
        return InactLoanMan.loanAdmByChange(this.bmno,ctx.getParameter("NEWADM"));
    }

}
