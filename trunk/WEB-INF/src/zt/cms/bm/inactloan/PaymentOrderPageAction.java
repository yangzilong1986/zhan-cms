package zt.cms.bm.inactloan;

import java.sql.*;

import zt.platform.db.*;
import zt.platform.form.control.*;
import zt.platform.form.util.*;
import zt.platform.form.util.event.*;
import zt.cms.pub.code.*;
import zt.cmsi.pub.*;
import zt.cms.pub.*;
import zt.cms.bm.common.ParamFactory;
import zt.cms.bm.common.SessionInfo;
import zt.cms.bm.common.*;
public class PaymentOrderPageAction extends FormActions {
    Param param = null;


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
        RightChecker.checkReadonly(ctx,conn,instance);
        param = ParamFactory.getParamByCtx(ctx);
        param.addParam("BMNO", ctx.getRequestAttribute("BMNO"));

        if (ctx.getParameter("SEQNO") != null) {
            instance.setValue("SEQNO",ctx.getParameter("SEQNO").trim());
            instance.setValue("BMNO",param.getBmNo());
            trigger(manager, instance, EventType.EDIT_VIEW_EVENT_TYPE,
                    Event.BRANCH_CONTINUE_TYPE);
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
    public int beforeInsert(SessionContext ctx, DatabaseConnection conn,
                            FormInstance instance, ErrorMessages msgs,
                            EventManager manager) {
        instance.setValue("BMNO", param.getBmNo());
        instance.setFieldReadonly("BMNO", true);
        String operator = SessionInfo.getLoginUserName(ctx);
        instance.setValue("OPERATOR", operator);

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
        int seqNo = (int) SerialNumber.getNextSN("BMILPAYMENTORDER", "SEQNO");
        instance.setValue("SEQNO", seqNo);
        assistor.setSqlFieldValue(assistor.getDefaultTbl(), "SEQNO",
                seqNo + "");

        String operator = SessionInfo.getLoginUserName(ctx);
        instance.setValue("OPERATOR", operator);
        assistor.setSqlFieldValue(assistor.getDefaultTbl(), "OPERATOR",
                operator + "");

        return 0;
    }

}