package zt.cms.xf.account;

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

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Connection;
import java.util.Date;
import java.util.logging.Logger;
import java.text.SimpleDateFormat;
import java.math.BigDecimal;

import zt.platform.form.util.SessionAttributes;
import zt.platform.form.config.SystemAttributeNames;
import zt.platform.user.UserManager;
import zt.platform.utils.Debug;
import zt.cmsi.mydb.MyDB;
import zt.cms.xf.common.dao.XfactcutpaymainDao;
import zt.cms.xf.common.dao.XfactcutpaydetlDao;
import zt.cms.xf.common.factory.XfactcutpaymainDaoFactory;
import zt.cms.xf.common.factory.XfactcutpaydetlDaoFactory;
import zt.cms.xf.common.dto.Xfactcutpaymain;
import zt.cms.xf.common.dto.Xfactcutpaydetl;
import zt.cms.xf.common.dto.XfactcutpaydetlPk;
import zt.cms.xf.common.dto.XfactcutpaymainPk;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;

public class XFActCutPayMainList extends FormActions {

    public static Logger logger = Logger.getLogger("zt.cms.xf.account.XFActPayDetlList");

    public int buttonEvent(SessionContext ctx, DatabaseConnection conn, FormInstance instance, String button,
                           ErrorMessages msgs, EventManager manager) {

        ctx.setAttribute("SUPERFORMID", instance.getFormid());
        // ctx.setAttribute("SUPERFORMID", ctx..getAttribute());
//        if (button != null && button.equals(SessionAttributes.REQUEST_BUTTON_ADD_NAME)) {
//
//            trigger(manager, "XFCONTRACTLINK", null);
//            //trigger(manager, instance, EventType.INSERT_VIEW_EVENT_TYPE, Event.BRANCH_CONTINUE_TYPE);
//        }
        if (button != null && button.equals(SessionAttributes.REQUEST_EDIT_BUTTON_VALUE)) {
            trigger(manager, "XFACTCUTPAYMAINPAGE", null);
        }

        if (button != null && button.equals("BILLSBUTTON")) {
            //doGenerateBills(ctx, conn, instance, msgs);
            trigger(manager, "XFACTCUTPAYLINK", null);
        }

        if (button != null && button.equals("ODBGENERATEBUTTON")) {
            //doGenerateBills(ctx, conn, instance, msgs);
            ctx.setAttribute("BUTTONNAME", "ODBGENERATEBUTTON");
            trigger(manager, "XFDATELINK", null);
        }

        return 0;
    }


}