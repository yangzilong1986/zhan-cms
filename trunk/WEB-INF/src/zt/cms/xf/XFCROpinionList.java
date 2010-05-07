package zt.cms.xf;


import zt.cms.cm.common.RightChecker;
import zt.platform.db.DatabaseConnection;
import zt.platform.form.config.SystemAttributeNames;
import zt.platform.form.control.FormActions;
import zt.platform.form.control.SessionContext;
import zt.platform.form.util.FormInstance;
import zt.platform.form.util.SessionAttributes;
import zt.platform.form.util.event.ErrorMessages;
import zt.platform.form.util.event.EventManager;
import zt.platform.user.UserManager;

import java.sql.PreparedStatement;


public class XFCROpinionList extends FormActions {
    //private String flag = null;  //窗体是否可读
    private String APPNO = null;
    private String UMOP = null; //业务员
    private String APPAMT = null; //申请金额

    public int load(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                    EventManager manager, String parameter) {

        RightChecker.checkReadonly(ctx, conn, instance);

        APPNO = String.valueOf(ctx.getRequestAttribute("APPNO"));
        APPAMT = String.valueOf(ctx.getRequestAttribute("APPAMT"));
        UserManager um = (UserManager) ctx.getAttribute(SystemAttributeNames.USER_INFO_NAME);
        UMOP = um.getUserName();

        String str = "select * from XFOPINION where APPNO='" + APPNO + "' and OPERATOR='" + UMOP + "'";
        if (conn.isExist(str)) {
            instance.useCloneFormBean();
            instance.getFormBean().setUseAdd(false);
        }

//        trigger(manager, instance, EventType.FIND_EVENT_TYPE,
//                Event.BRANCH_CONTINUE_TYPE);
        return 0;
    }

    public int preFind(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                       EventManager manager, PreparedStatement ps, PreparedStatement countps) {
        try {
            ps.setString(1, this.APPNO);
            countps.setString(1, this.APPNO);
        } catch (Exception ex) {
            msgs.add(ex.getMessage());
            return -1;
        }
        String str = "select * from XFOPINION where APPNO='" + APPNO + "' and OPERATOR='" + UMOP + "'";
        if (conn.isExist(str)) {
            instance.useCloneFormBean();
            instance.getFormBean().setUseAdd(false);
        }
        return 0;
    }

    public int buttonEvent(SessionContext ctx, DatabaseConnection conn, FormInstance instance, String button,
                           ErrorMessages msgs, EventManager manager) {

        RightChecker.transReadOnly(ctx, conn, instance);

        ctx.setRequestAtrribute("APPNO", APPNO);
        ctx.setRequestAtrribute("APPAMT", APPAMT);
        UserManager um = (UserManager) ctx.getAttribute(SystemAttributeNames.USER_INFO_NAME);
        UMOP = um.getUserName();

        if (um == null) {
            msgs.add("用户会话已超时或已失效，请重新登录！");
            return -1;
        } else if (button != null && button.equals(SessionAttributes.REQUEST_EDIT_BUTTON_VALUE)) {
            trigger(manager, "XFCROPINION", null);
        } else {
            int rowcount = Integer.parseInt((String) ctx.getRequestAttribute(SessionAttributes.REQUEST_LIST_ROWCOUNT_NAME));
            ctx.setRequestAtrribute("OPINIONNO", String.valueOf(rowcount + 1));
            trigger(manager, "XFCROPINION", null);
        }
        return 0;
    }

}