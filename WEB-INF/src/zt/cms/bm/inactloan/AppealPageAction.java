package zt.cms.bm.inactloan;

import zt.platform.form.control.FormActions;
import zt.platform.form.control.FormActions;

import java.sql.*;

import zt.platform.db.*;
import zt.platform.form.control.*;
import zt.platform.form.util.*;
import zt.platform.form.util.event.*;
import zt.cms.pub.code.*;
import zt.cmsi.pub.*;
import zt.cms.pub.*;
import zt.cms.bm.common.ParamFactory;
import zt.cms.bm.common.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class AppealPageAction extends FormActions
{
    public int load(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
            EventManager manager, String parameter) {

        if (ctx.getRequestAttribute("SEQNO") != null) {
            instance.setValue("SEQNO",Integer.parseInt((String)ctx.getRequestAttribute("SEQNO")) );
            trigger(manager, instance, EventType.EDIT_VIEW_EVENT_TYPE,
                    Event.BRANCH_CONTINUE_TYPE);
        }else{
            msgs.add("SEQNO²»´æÔÚ!");
            return -1;
        }
        return 0;
    }
    public int preEdit(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
            EventManager manager, SqlAssistor assistor) {
        String operator = SessionInfo.getLoginUserName(ctx);
        instance.setValue("OPERATOR", operator);
        assistor.setSqlFieldValue(assistor.getDefaultTbl(), "OPERATOR",
                operator + "");
        return 0;
    }


}
