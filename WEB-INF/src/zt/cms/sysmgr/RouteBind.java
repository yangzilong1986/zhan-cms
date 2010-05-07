package zt.cms.sysmgr;


import zt.cmsi.pub.define.BMRouteBind;
import zt.platform.form.control.FormActions;
import zt.platform.form.control.SessionContext;
import zt.platform.form.util.FormInstance;
import zt.platform.form.util.event.ErrorMessages;
import zt.platform.form.util.event.EventManager;
import zt.platform.db.DatabaseConnection;

import java.util.logging.Logger;


public class RouteBind extends FormActions {

    private static Logger logger = Logger.getLogger("zt.bm.sysmgr.RouteBind");
    private String bndtype = null;
    private String bndid = null;
    private String flag = null;

    public int load(SessionContext ctx, DatabaseConnection conn,
                    FormInstance instance, ErrorMessages msgs,
                    EventManager manager, String parameter) {
        return 0;
    }

    public int postDeleteOk(SessionContext ctx, DatabaseConnection conn,
                            FormInstance instance,
                            ErrorMessages msgs,
                            EventManager manager) {

        //System.out.println("===========called========================");
        BMRouteBind.setDirty();
        return 0;
    }


    public int buttonEvent(SessionContext ctx, DatabaseConnection conn,
                           FormInstance instance, String button,
                           ErrorMessages msgs, EventManager manager) {
        //System.out.println("===========called============222============");
        BMRouteBind.setDirty();
        trigger(manager, "ROUTEBINDPAGE", null);
        return 0;
    }
}
