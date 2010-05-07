package zt.cms.scuser;

import zt.platform.form.control.FormActions;
import zt.platform.form.control.FormActions;
import zt.platform.db.*;
import zt.platform.form.control.*;
import zt.platform.form.util.*;
import zt.platform.form.util.event.*;
import zt.cms.bm.common.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class CMListAction extends FormActions
{
    public int buttonEvent(SessionContext ctx, DatabaseConnection conn, FormInstance instance, String button,
    ErrorMessages msgs, EventManager manager) {
    RightChecker.transReadOnly(ctx,conn,instance);
    trigger(manager, "CLIENTMANAGERPAGE", null);
    return 0;
}

public int preFind(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
        EventManager manager, SqlWhereUtil sqlWhereUtil) {
    if (super.preFind(ctx, conn, instance, msgs, manager, sqlWhereUtil) >= 0) {
        String strinsql = SessionInfo.getLoginAllSubBrhIds(ctx);
        sqlWhereUtil.addWhereField("SCUSER", "BRHID", strinsql, SqlWhereUtil.DataType_Sql, sqlWhereUtil.OperatorType_In);
    }
    return 0;
}



}
