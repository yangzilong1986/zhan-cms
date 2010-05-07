package zt.cms.bm.cp;

import zt.platform.form.control.FormActions;
import zt.platform.db.*;
import zt.platform.form.control.*;
import zt.platform.form.util.*;
import zt.platform.form.util.event.*;
import zt.cms.pub.code.*;
import zt.cms.bm.common.*;
import zt.cms.pub.*;
import zt.cmsi.biz.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class ConfPageAction extends FormActions
{
    public int load(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
            EventManager manager, String parameter) {
        RightChecker.checkReadonly(ctx,conn,instance);
        String brhid = (String) ctx.getParameter("BRHID");
        if (brhid != null && !brhid.equals("")) {
            instance.setValue("BRHID", ctx.getParameter("BRHID"));
            trigger(manager, instance, EventType.EDIT_VIEW_EVENT_TYPE,
                    Event.BRANCH_CONTINUE_TYPE);
        } else {
            trigger(manager, instance, EventType.INSERT_VIEW_EVENT_TYPE,
                    Event.BRANCH_CONTINUE_TYPE);
        }
        return 0;
    }


}
