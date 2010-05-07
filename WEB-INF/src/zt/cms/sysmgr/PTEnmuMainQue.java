package zt.cms.sysmgr;

import zt.platform.db.DatabaseConnection;
import zt.platform.form.control.FormActions;
import zt.platform.form.control.SessionContext;
import zt.platform.form.util.FormInstance;
import zt.platform.form.util.SessionAttributes;
import zt.platform.form.util.event.ErrorMessages;
import zt.platform.form.util.event.EventManager;

import java.sql.PreparedStatement;
import java.util.logging.Logger;

public class PTEnmuMainQue extends FormActions {
    private static Logger logger = Logger.getLogger("zt.cms.sysmgr.PTEnmuMainQue");
    String flag = null;

    public int load(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                    EventManager manager, String parameter) {
        flag = ctx.getParameter("flag");
        return 0;
    }

    public int preFind(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                       EventManager manager, PreparedStatement ps, PreparedStatement countps) {
        return 0;
    }

    public int buttonEvent(SessionContext ctx, DatabaseConnection conn, FormInstance instance,
                           String button,
                           ErrorMessages msgs, EventManager manager) {
        ctx.setRequestAtrribute("flag", flag);
        if ((button != null) && (button.equals(SessionAttributes.REQUEST_BUTTON_ADD_NAME)
                || button.equals(SessionAttributes.REQUEST_EDIT_BUTTON_VALUE))) {
            this.trigger(manager, "ENMUDETLLIST", null);
        }
        return 0;
    }
}
