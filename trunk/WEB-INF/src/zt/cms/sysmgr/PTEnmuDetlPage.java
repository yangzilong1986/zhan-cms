package zt.cms.sysmgr;
/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

import zt.platform.db.DatabaseConnection;
import zt.platform.form.config.ElementBean;
import zt.platform.form.config.FormBean;
import zt.platform.form.control.FormActions;
import zt.platform.form.control.SessionContext;
import zt.platform.form.util.FormInstance;
import zt.platform.form.util.event.ErrorMessages;
import zt.platform.form.util.event.Event;
import zt.platform.form.util.event.EventManager;
import zt.platform.form.util.event.EventType;

import java.util.logging.Logger;

public class PTEnmuDetlPage extends FormActions {

    private static Logger logger = Logger.getLogger("zt.cms.cm.PTEnmuDetlPage");
    private String ENUID = null;
    private String ENUTP = null;
    private String flag = null;

    public int load(SessionContext ctx, DatabaseConnection conn,
                    FormInstance instance, ErrorMessages msgs,
                    EventManager manager, String parameter) {
        //flag
        flag = ctx.getRequestAttribute("flag").toString().trim();
        if (flag.equals("write")) {
            instance.setReadonly(false);
        } else {
            instance.setReadonly(true);
        }
        //ENUID
        ENUID = ctx.getParameter("ENUID");
        if (ENUID == null) {
            msgs.add("错误的参数：ENUID为空！");
            return -1;
        }
        instance.setValue("ENUID", ENUID);
        ENUID = ENUID.toString().trim();
        //ENUTP
        ENUTP = ctx.getParameter("ENUTP");
        if (ENUTP != null) {
            instance.setValue("ENUTP", ENUTP);
            trigger(manager, instance, EventType.EDIT_VIEW_EVENT_TYPE, Event.BRANCH_CONTINUE_TYPE);
        } else {
            instance.useCloneFormBean();
            FormBean fb = instance.getFormBean();
            ElementBean eb = fb.getElement("ENUTP");
            eb.setIsPrimaryKey(false);
            eb.setReadonly(false);
        }
        return 0;
    }

    public int beforeInsert(SessionContext ctx, DatabaseConnection conn,
                            FormInstance instance, ErrorMessages msgs,
                            EventManager manager) {
        instance.setValue("ENUID", ENUID);
        return 0;
    }
}
