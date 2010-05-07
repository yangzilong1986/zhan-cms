package zt.cms.xf;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

import zt.cms.cm.common.RightChecker;
import zt.cms.util.Workflow;
import zt.cmsi.pub.define.BMRouteBindNode;
import zt.cmsi.pub.cenum.EnumValue;
import zt.platform.db.DatabaseConnection;
import zt.platform.form.config.ElementBean;
import zt.platform.form.config.FormBean;
import zt.platform.form.config.SystemAttributeNames;
import zt.platform.form.control.FormActions;
import zt.platform.form.control.SessionContext;
import zt.platform.form.util.FormInstance;
import zt.platform.form.util.SessionAttributes;
import zt.platform.form.util.SqlWhereUtil;
import zt.platform.form.util.event.ErrorMessages;
import zt.platform.form.util.event.Event;
import zt.platform.form.util.event.EventManager;
import zt.platform.form.util.event.EventType;
import zt.platform.user.UserManager;


public class XFConfirmList extends FormActions {
    //private String strAppNO = null;

    public int load(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                    EventManager manager, String parameter) {
        RightChecker.checkReadonly(ctx, conn, instance);
        trigger(manager, instance, EventType.FIND_EVENT_TYPE,
                Event.BRANCH_CONTINUE_TYPE);
        return 0;
    }

    public int preFind(SessionContext ctx, DatabaseConnection conn, FormInstance instance,
                       ErrorMessages msgs,
                       EventManager manager, SqlWhereUtil sqlWhereUtil) {
        if (super.preFind(ctx, conn, instance, msgs, manager, sqlWhereUtil) >= 0) {
            FormBean fb = instance.getFormBean();
            UserManager um = (UserManager) ctx.getAttribute(SystemAttributeNames.USER_INFO_NAME);

            if (um == null) {
                msgs.add("用户会话已超时或已失效，请重新登录！");
                return -1;
            } else if (fb.getId().equals("XFCONFIRMLIST")) {//审批列表界面
                BMRouteBindNode dt = Workflow.getRouteBindByUserTp(String.valueOf(um.getUser().getStatus()));
                if (dt != null) {//获得的流程信息不为空
                    instance.setValue("APPSTATUS", dt.status.split(EnumValue.SPLIT_STR)[0]);
                    ElementBean ebx = fb.getElement("APPSTATUS");
                    ebx.setReadonly(true);

                    sqlWhereUtil.addWhereField(fb.getTbl(), "APPSTATUS", Workflow.statusToSql(dt.status),
                            SqlWhereUtil.DataType_Sql, SqlWhereUtil.OperatorType_In);
                    if (dt.limitcondition != null)
                        sqlWhereUtil.addWhereField(fb.getTbl(), "APPAMT", dt.limitcondition,
                                SqlWhereUtil.DataType_Number, SqlWhereUtil.OperatorType_No);
                } else           //获得的流程信息为空——没有权限的人查询则返回空列表
                    sqlWhereUtil.addWhereField(fb.getTbl(), "APPSTATUS", "",
                            SqlWhereUtil.DataType_Sql, SqlWhereUtil.OperatorType_In);
            }
        }
        return 0;
    }

    public int buttonEvent(SessionContext ctx, DatabaseConnection conn, FormInstance instance, String button,
                           ErrorMessages msgs, EventManager manager) {
        RightChecker.transReadOnly(ctx, conn, instance);
        UserManager um = (UserManager) ctx.getAttribute(SystemAttributeNames.USER_INFO_NAME);
        if (um == null) {
            msgs.add("用户会话已超时或已失效，请重新登录！");
            return -1;
        } else if (button != null && button.equals(SessionAttributes.REQUEST_EDIT_BUTTON_VALUE)) {
            String CLICK_COLUMN_NAME = ctx.getParameter(SessionAttributes.CLICK_COLUMN_NAME);
            if (CLICK_COLUMN_NAME != null && CLICK_COLUMN_NAME.equals("operation")) {//详细
                ctx.setTarget("/consume/application.jsp");
            } else { //审批
                trigger(manager, "XFCONFIRMPAGE10", null);
            }
        }
        return 0;
    }

}