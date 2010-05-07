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

public class XFCreditIssueList extends FormActions {


    public int load(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                    EventManager manager, String parameter) {

        ctx.setAttribute("FLOWSTATUS", XFFlowStatus.FANGKUAN_JINGBAN);
        return 0;
    }

    public int preFind(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                       EventManager manager, PreparedStatement ps, PreparedStatement countps) {
        try {
            //查询条件为 合同签订完毕 或合同放款驳回
            ps.setString(1, XFContractStatus.QIANDING_TONGGUO);
            //ps.setString(2, XFContractStatus.FANGKUAN_BOHUI);
            countps.setString(1, XFContractStatus.QIANDING_TONGGUO);
            //countps.setString(2, XFContractStatus.FANGKUAN_BOHUI);
        } catch (Exception ex) {
            msgs.add(ex.getMessage());
            return -1;
        }
        return 0;
    }

    public int buttonEvent(SessionContext ctx, DatabaseConnection conn, FormInstance instance, String button,
                           ErrorMessages msgs, EventManager manager) {
        if (button != null && button.equals(SessionAttributes.REQUEST_BUTTON_ADD_NAME)) {
            trigger(manager, "XFCONTRACTPAYPAGE", null);
            //trigger(manager, instance, EventType.INSERT_VIEW_EVENT_TYPE, Event.BRANCH_CONTINUE_TYPE);
        }
        if (button != null && button.equals(SessionAttributes.REQUEST_EDIT_BUTTON_VALUE)) {
            ctx.setAttribute("SUPERFORMID", instance.getFormid());
            //trigger(manager, "XFACTINFOPAGE", null);
            trigger(manager, "XFCONTRACTPAGE", null);
        }

        return 0;
    }

}