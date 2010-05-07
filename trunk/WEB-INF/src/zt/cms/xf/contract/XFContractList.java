package zt.cms.xf.contract;

/**
 * Created by IntelliJ IDEA.
 * User: zhanrui
 * Date: 2009-3-14
 * Time: 9:19:52
 * To change this template use File | Settings | File Templates.
 */

import zt.platform.form.control.FormActions;
import zt.platform.form.control.SessionContext;
import zt.platform.db.DatabaseConnection;
import zt.platform.form.util.FormInstance;

import zt.platform.form.util.event.*;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import zt.platform.form.util.SessionAttributes;

public class XFContractList extends FormActions{


    public int buttonEvent(SessionContext ctx, DatabaseConnection conn, FormInstance instance, String button,
                ErrorMessages msgs, EventManager manager) {

        ctx.setAttribute("SUPERFORMID", instance.getFormid());
        if (button != null && button.equals(SessionAttributes.REQUEST_BUTTON_ADD_NAME)) {

            trigger(manager, "XFCONTRACTLINK", null);
            //trigger(manager, instance, EventType.INSERT_VIEW_EVENT_TYPE, Event.BRANCH_CONTINUE_TYPE);
        }
        if (button != null && button.equals(SessionAttributes.REQUEST_EDIT_BUTTON_VALUE)) {
            String buttonname = ctx.getParameter(SessionAttributes.CLICK_COLUMN_NAME);
            if (buttonname.equals("PAYSCHEDULE")) {
                trigger(manager, "XFCONTRACTPAYLIST", null);
            } else if (buttonname.equals("OPRATION")) {
                trigger(manager, "XFCONTRACTPAGE", null);
            }
        }
        return 0;
    }

}
