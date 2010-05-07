package zt.cms.xf.contract;

/**
 * 贷款发放一览处理.
 * User: zhanrui
 * Date: 2009-3-25
 * Time: 17:30:25
 * To change this template use File | Settings | File Templates.
 */
import zt.platform.form.control.FormActions;
import zt.platform.form.control.SessionContext;
import zt.platform.db.DatabaseConnection;
import zt.platform.form.util.FormInstance;

import zt.platform.form.util.event.*;

import java.sql.PreparedStatement;

import zt.platform.form.util.SessionAttributes;
import zt.cms.xf.common.constant.XFContractStatus;
import zt.cms.xf.common.constant.XFFlowStatus;

public class XFCreditIssueListFH extends FormActions{


    public int load(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                    EventManager manager, String parameter) {

        ctx.setAttribute("FLOWSTATUS", XFFlowStatus.FANGKUAN_FUHE);
        return 0;
    }

    public int preFind(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                       EventManager manager, PreparedStatement ps, PreparedStatement countps) {
        try {
            ps.setString(1, XFContractStatus.FANGKUAN_DAISHENHE);
            countps.setString(1, XFContractStatus.FANGKUAN_DAISHENHE);
        } catch (Exception ex) {
            msgs.add(ex.getMessage());
            return -1;
        }
        return 0;
    }

    public int buttonEvent(SessionContext ctx, DatabaseConnection conn, FormInstance instance, String button,
                ErrorMessages msgs, EventManager manager) {
        if (button != null && button.equals(SessionAttributes.REQUEST_EDIT_BUTTON_VALUE)) {
            ctx.setAttribute("SUPERFORMID", instance.getFormid());
            //trigger(manager, "XFACTINFOPAGE", null);
            trigger(manager, "XFCONTRACTPAGE", null);
        }

        return 0;
    }

}