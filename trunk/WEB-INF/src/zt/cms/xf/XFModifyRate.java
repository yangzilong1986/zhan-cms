package zt.cms.xf;

import zt.cms.cm.common.RightChecker;
import zt.platform.db.DatabaseConnection;
import zt.platform.form.control.FormActions;
import zt.platform.form.control.SessionContext;
import zt.platform.form.util.FormInstance;
import zt.platform.form.util.event.ErrorMessages;
import zt.platform.form.util.event.Event;
import zt.platform.form.util.event.EventManager;
import zt.platform.form.util.event.EventType;

import java.util.logging.Logger;

public class XFModifyRate extends FormActions {
    public static Logger logger = Logger.getLogger("zt.cms.xf.XFModifyRate");

    private String flag = null;  //窗体是否可读
    private String APPNO = null; //申请单号

    public int load(SessionContext ctx, DatabaseConnection conn,
                    FormInstance instance, ErrorMessages msgs,
                    EventManager manager, String parameter) {

        RightChecker.checkReadonly(ctx, conn, instance);
        
        //主键、内存变量类型的数据或一些需要特殊处理的数据库字段数据
        APPNO = String.valueOf(ctx.getRequestAttribute("APPNO"));
        //主键不为空则进入编辑状态
        if (APPNO != null) {
            //设置instance主键的值
            instance.setValue("APPNO", APPNO);
            //流程转移到编辑状态
            trigger(manager, instance, EventType.EDIT_VIEW_EVENT_TYPE,
                    Event.BRANCH_CONTINUE_TYPE);
        }
        return 0;
    }


    public int postEditOk(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                          EventManager manager) {
        ctx.setRequestAtrribute("msg", "维护手续费率成功！");
        ctx.setRequestAtrribute("reloadOperner", "1");
        ctx.setTarget("/showinfo.jsp");
        return 0;
    }

    public int buttonEvent(SessionContext ctx, DatabaseConnection conn,
                           FormInstance instance, String button,
                           ErrorMessages msgs, EventManager manager) {
        return 0;
    }

}