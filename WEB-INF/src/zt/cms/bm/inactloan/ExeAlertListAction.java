package zt.cms.bm.inactloan;

import com.zt.util.setup.SetupManager;
import zt.cms.bm.common.RightChecker;
import zt.cms.pub.SCBranch;
import zt.cms.pub.SCUser;
import zt.cmsi.pub.define.SystemDate;
import zt.platform.db.DatabaseConnection;
import zt.platform.form.config.SystemAttributeNames;
import zt.platform.form.control.FormActions;
import zt.platform.form.control.SessionContext;
import zt.platform.form.util.FormInstance;
import zt.platform.form.util.SqlWhereUtil;
import zt.platform.form.util.event.ErrorMessages;
import zt.platform.form.util.event.EventManager;
import zt.platform.user.UserManager;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */

public class ExeAlertListAction extends FormActions {
    public int preFind(SessionContext ctx, DatabaseConnection conn, FormInstance instance,
                       ErrorMessages msgs,
                       EventManager manager, SqlWhereUtil sqlWhereUtil) {

        UserManager um = (UserManager) ctx.getAttribute(SystemAttributeNames.USER_INFO_NAME);
        String OPERATOR = um.getUserName();
        String BRHID = SCUser.getBrhId(OPERATOR);
        if (BRHID == null || BRHID.length() < 1) {
            msgs.add("用户网点不存在！");
            return -1;
        }

        String SUBBRHIDs = SCBranch.getSubBranchAll(BRHID);
        if (SUBBRHIDs == null || SUBBRHIDs.length() < 1) {
            msgs.add("下属网点不存在！");
            return -1;
        } else {
            SUBBRHIDs = "'" + SUBBRHIDs.replaceAll(",", "','") + "'";
        }
        //APPDATE
        int alertMonth = SetupManager.getIntProperty("INACTLOAN:EXEALERTMONTH");
        if (alertMonth <= 0) {
            alertMonth = 1;
        }

        String theName = "(case when (case when coalesce(effectivedate,'1900-01-01') > coalesce(aplsentencedate,'1900-01-01') then coalesce(effectivedate,'1900-01-01') else coalesce(aplsentencedate,'1900-01-01') end ) > coalesce(susentencedate,'1900-01-01') then (case when coalesce(effectivedate,'1900-01-01') > coalesce(aplsentencedate,'1900-01-01') then coalesce(effectivedate,'1900-01-01') else coalesce(aplsentencedate,'1900-01-01') end ) else coalesce(susentencedate,'1900-01-01') end ) ";
        String APPDATE = SystemDate.getSystemDate2();
        String APPDATE2 = "cast('" + APPDATE + "' as \"DATE\")-6 MONTH";
        APPDATE = "cast('" + APPDATE + "' as \"DATE\")-" + (6 - alertMonth) + " MONTH";

        //
        //logger.info(SUBBRHIDs);
        //logger.info(APPDATE);
        //sqlWhereUtil
        if (super.preFind(ctx, conn, instance, msgs, manager, sqlWhereUtil) >= 0) {
            sqlWhereUtil.addWhereField("b", "BRHID", SUBBRHIDs,
                    SqlWhereUtil.DataType_Sql,
                    sqlWhereUtil.OperatorType_In);
            sqlWhereUtil.addWhereField("", theName, APPDATE,
                    SqlWhereUtil.DataType_Sql,
                    sqlWhereUtil.OperatorType_Lower_Equals);
            sqlWhereUtil.addWhereField("", theName, APPDATE2,
                    SqlWhereUtil.DataType_Sql,
                    sqlWhereUtil.OperatorType_Greater_Equals);

        }
        return 0;
    }

    public int buttonEvent(SessionContext ctx, DatabaseConnection conn, FormInstance instance,
                           String button,
                           ErrorMessages msgs, EventManager manager) {
        RightChecker.transReadOnly(ctx, conn, instance);
        trigger(manager, "BMINACTLOANMANAGE2", null);
        return 0;
    }
}

