package zt.cms.bm.inactloan;

import zt.platform.form.control.FormActions;

import java.sql.*;
import zt.platform.db.*;
import zt.platform.form.control.*;
import zt.platform.form.util.*;
import zt.platform.form.util.event.*;
import zt.cms.bm.common.*;
import zt.cmsi.pub.*;
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

public class PenaltyPageAction
    extends FormActions {

    String bmno = "";
    Param param =null;
    public int load(SessionContext ctx, DatabaseConnection conn, FormInstance instance,
                    ErrorMessages msgs,
                    EventManager manager, String parameter)
    {
        super.load(ctx, conn, instance, msgs, manager, parameter);
        this.bmno = ( (Param) ctx.getRequestAttribute("BMPARAM")).getBmNo();

        param = ParamFactory.getParamByCtx(ctx);
        param.addParam("BMNO", ctx.getRequestAttribute("BMNO"));

        if (ctx.getParameter("SEQNO") != null) {
            instance.setValue("SEQNO", ctx.getParameter("SEQNO"));
            trigger(manager, instance, EventType.EDIT_VIEW_EVENT_TYPE,
                    Event.BRANCH_CONTINUE_TYPE);
        }
        else {
            trigger(manager, instance, EventType.INSERT_VIEW_EVENT_TYPE,
                    Event.BRANCH_CONTINUE_TYPE);
        }
        return 0;

    }

    public int preInsert(SessionContext ctx, DatabaseConnection conn,
            FormInstance instance, ErrorMessages msgs,
            EventManager manager, SqlAssistor assistor) {
        int seqNo = (int) SerialNumber.getNextSN(assistor.getDefaultTbl(),"SEQNO");
        assistor.setSqlFieldValue(assistor.getDefaultTbl(),"SEQNO",seqNo+"");

        instance.setValue("BMNO", this.bmno);
        assistor.setSqlFieldValue(assistor.getDefaultTbl(), "BMNO",this.bmno + "");

        return 0;
    }


}
