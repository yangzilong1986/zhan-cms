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

import java.util.logging.Logger;

public class ProsecutionAlertList extends FormActions {

    public static Logger logger = Logger.getLogger("zt.cms.bm.inactloan.ProsecutionAlertList");
    private String flag = null;
    private String OPERATOR = null;

    public int load(SessionContext ctx, DatabaseConnection conn,
                    FormInstance instance, ErrorMessages msgs,
                    EventManager manager, String parameter) {
        //flag
        flag = "read";
        if (ctx.getRequestAttribute("flag") != null) {
            flag = ctx.getRequestAttribute("flag").toString().trim();
        } else if (ctx.getParameter("flag") != null) {
            flag = ctx.getParameter("flag").toString().trim();
        }
        if (flag.equals("write")) {
            instance.setReadonly(false);
        } else {
            instance.setReadonly(true);
        }
        //OPERATOR
        UserManager um = (UserManager) ctx.getAttribute(SystemAttributeNames.USER_INFO_NAME);
        OPERATOR = um.getUserName();
        if (OPERATOR == null || OPERATOR.length() < 1) {
            msgs.add("系统错误，获取业务操作员信息失败！");
            return -1;
        }
        return 0;
    }

    public int preFind(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                       EventManager manager, SqlWhereUtil sqlWhereUtil) {
        //String value = ClientSum.getClientSum(CLIENTNO,"CMINDVDEBT","AMT",conn);
        //ctx.setAfterBody("AMT="+value+"");
        //BRHID（用户网点）
        String BRHID = SCUser.getBrhId(OPERATOR);
        if (BRHID == null || BRHID.length() < 1) {
            msgs.add("用户网点不存在！");
            return -1;
        }
        //SUBBRHIDs（用户网点下的所有实网点，包括自己）
        String SUBBRHIDs = SCBranch.getSubBranchAll(BRHID);
        if (SUBBRHIDs == null || SUBBRHIDs.length() < 1) {
            msgs.add("下属网点不存在！");
            return -1;
        } else {
            SUBBRHIDs = "'" + SUBBRHIDs.replaceAll(",", "','") + "'";
        }

        int alertMonth = SetupManager.getIntProperty("INACTLOAN:PROALERTMONTH");
        if (alertMonth <= 0) {
            alertMonth = 1;
        }

        //APPDATE
        String APPDATE = SystemDate.getSystemDate2();
        String APPDATE2 = "cast('" + APPDATE + "' as \"DATE\")-24 MONTH";
        APPDATE = "cast('" + APPDATE + "' as \"DATE\")-" + (24 - alertMonth) + " MONTH";

        //
        //logger.info(SUBBRHIDs);
        //logger.info(APPDATE);
        //sqlWhereUtil
        if (super.preFind(ctx, conn, instance, msgs, manager, sqlWhereUtil) >= 0) {
            sqlWhereUtil.addWhereField("b", "BRHID", SUBBRHIDs,
                    SqlWhereUtil.DataType_Sql,
                    sqlWhereUtil.OperatorType_In);
            sqlWhereUtil.addWhereField("a", "NOWENDDATE", APPDATE,
                    SqlWhereUtil.DataType_Sql,
                    sqlWhereUtil.OperatorType_Lower_Equals);
            sqlWhereUtil.addWhereField("a", "NOWENDDATE", APPDATE2,
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
