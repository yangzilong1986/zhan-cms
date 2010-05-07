package zt.cms.xf.query;

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

import java.util.logging.Logger;
import java.sql.PreparedStatement;

import zt.platform.form.util.SessionAttributes;

public class XFActCutPayDetlQuery extends FormActions {

    public static Logger logger = Logger.getLogger("zt.cms.xf.account.XFActPayDetlList");


    public int preFind(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                       EventManager manager, PreparedStatement ps, PreparedStatement countps) {
        try {
            String contractno = (String)ctx.getAttribute("CONTRACTNO");
            ps.setString(1, contractno);
            countps.setString(1, contractno);
        } catch (Exception ex) {
            msgs.add(ex.getMessage());
            return -1;
        }
        return 0;
    }
    
    public int buttonEvent(SessionContext ctx, DatabaseConnection conn, FormInstance instance, String button,
                           ErrorMessages msgs, EventManager manager) {

        ctx.setAttribute("SUPERFORMID", instance.getFormid());
        if (button != null && button.equals(SessionAttributes.REQUEST_EDIT_BUTTON_VALUE)) {
            trigger(manager, "XFACTCUTPAYDETLPAGE", null);
        }
        return 0;
    }


}