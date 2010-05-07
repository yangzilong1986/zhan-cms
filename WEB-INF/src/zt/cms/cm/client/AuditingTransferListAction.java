package zt.cms.cm.client;
import java.util.logging.*;

import zt.cms.cm.common.*;
import zt.cms.pub.*;
import zt.platform.db.*;
import zt.platform.form.config.*;
import zt.platform.form.control.*;
import zt.platform.form.util.*;
import zt.platform.form.util.event.*;
import zt.platform.user.*;
import zt.cms.bm.common.SessionInfo;
import zt.cmsi.pub.define.*;
/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class AuditingTransferListAction
    extends FormActions {
    public int preFind(SessionContext ctx, DatabaseConnection conn, FormInstance instance,
                       ErrorMessages msgs,
                       EventManager manager, SqlWhereUtil sqlWhereUtil)
    {
        if (super.preFind(ctx, conn, instance, msgs, manager, sqlWhereUtil) >= 0) {
            sqlWhereUtil.addWhereField("CMCLIENTTRANSFER", "AUDITINGBRHID",
                                       "%" + SessionInfo.getLoginBrhId(ctx),
                                       SqlWhereUtil.DataType_Character,
                                       sqlWhereUtil.OperatorType_Like);

            if (sqlWhereUtil.toSqlWhere(true).indexOf("APPROVED") == -1) {
                sqlWhereUtil.addWhereField("CMCLIENTTRANSFER", "APPROVED",
                                           "0",
                                           SqlWhereUtil.DataType_Number,
                                           sqlWhereUtil.OperatorType_Equals);

            }

        }
        return 0;
    }

    public int buttonEvent(SessionContext ctx, DatabaseConnection conn, FormInstance instance,
                           String button,
                           ErrorMessages msgs, EventManager manager)
    {

        //CLIENTNO=4 &APPLYBRHID=907030000 &FORMERBRHID=907060000'
        String clientNo = ctx.getParameter("CLIENTNO").trim();
        String applyBrhid = ctx.getParameter("APPLYBRHID").trim();
        String formerBrhid = ctx.getParameter("FORMERBRHID").trim();


        String but = ctx.getParameter("Plat_Form_Click_Column_Name");
        String user = SessionInfo.getLoginUserName(ctx);

        if (but.trim().equals("editbutton1")) {
            String str1 = "update cmclienttransfer set APPROVED=1,AUDITINGDATE='"+SystemDate.getSystemDate2()+"',AUDITINGUSER='"+user+"' where clientno='" + clientNo +
                "' and applyBrhid='" + applyBrhid + "' and formerbrhid='" + formerBrhid + "' ";
            String str2 = "update cmindvclient set appbrhid='" + applyBrhid + "' where clientno='" +
                clientNo + "'";
            String str3 = "update cmcorpclient set appbrhid='" + applyBrhid + "' where clientno='" +
                clientNo + "'";

            conn.executeUpdate(str1);
            conn.executeUpdate(str2);
            conn.executeUpdate(str3);
            ctx.setRequestAtrribute("msg", "客户转移成功");
            ctx.setTarget("/showinfo.jsp");
        }
        else {
            String str1 = "update cmclienttransfer set APPROVED=2,AUDITINGDATE='"+SystemDate.getSystemDate2()+"',AUDITINGUSER='"+user+"' where clientno='" + clientNo +
                "' and applyBrhid='" + applyBrhid + "' and formerbrhid='" + formerBrhid + "' ";
            conn.executeUpdate(str1);
            ctx.setRequestAtrribute("msg", "拒绝客户转移成功");
            ctx.setTarget("/showinfo.jsp");
        }


        return 0;
    }

}
