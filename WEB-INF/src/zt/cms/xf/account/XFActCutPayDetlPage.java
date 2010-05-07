package zt.cms.xf.account;

import zt.platform.form.control.FormActions;
import zt.platform.form.control.SessionContext;
import zt.platform.db.DatabaseConnection;
import zt.platform.form.util.FormInstance;

import zt.platform.form.util.event.*;
import zt.platform.form.config.SystemAttributeNames;
import zt.platform.user.UserManager;
import zt.platform.utils.Debug;
import zt.cmsi.mydb.MyDB;
import zt.cms.xf.common.dto.Xfactpaydetl;
import zt.cms.xf.common.dto.XfactpaydetlPk;
import zt.cms.xf.common.dto.Xfcontract;
import zt.cms.xf.common.dto.XfcontractPk;
import zt.cms.xf.common.dao.XfactpaydetlDao;
import zt.cms.xf.common.dao.XfcontractDao;
import zt.cms.xf.common.factory.XfactpaydetlDaoFactory;
import zt.cms.xf.common.factory.XfcontractDaoFactory;
import zt.cms.xf.common.constant.XFContractStatus;

public class XFActCutPayDetlPage extends FormActions {

    private String journalno;

    public int load(SessionContext ctx, DatabaseConnection conn, FormInstance instance,
                    ErrorMessages msgs, EventManager manager, String parameter) {

        journalno = ctx.getParameter("JOURNALNO");

        instance.setValue("JOURNALNO", journalno);
        
        trigger(manager, instance, EventType.EDIT_VIEW_EVENT_TYPE, Event.BRANCH_CONTINUE_TYPE);
        return 0;
    }

    public int buttonEvent(SessionContext ctx, DatabaseConnection conn, FormInstance instance, String button,
                           ErrorMessages msgs, EventManager manager) {


        return 0;
    }

}