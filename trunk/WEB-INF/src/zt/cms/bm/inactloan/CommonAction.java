package zt.cms.bm.inactloan;

import java.sql.*;
import zt.platform.db.*;
import zt.platform.form.control.*;
import zt.platform.form.util.*;
import zt.platform.form.util.event.*;
import zt.cms.bm.common.*;
import zt.cmsi.pub.*;

public abstract class CommonAction extends FormActions {
    String bmno = "";


    public abstract String getPageFormId();

    public int buttonEvent(SessionContext ctx, DatabaseConnection conn, FormInstance instance, String button,
        ErrorMessages msgs, EventManager manager) {

    ctx.setRequestAtrribute("BMNO", this.bmno);
    Param param=new Param();
    param.addParam("BMNO",this.bmno);
    ctx.setRequestAtrribute("BMPARAM",param);

    RightChecker.transReadOnly(ctx,conn,instance);
    trigger(manager, getPageFormId(), null);
    return 0;
}



    public int load(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
        EventManager manager, String parameter) {
    super.load(ctx, conn, instance, msgs, manager, parameter);
    RightChecker.checkReadonly(ctx,conn,instance);
    this.bmno = ((Param)ctx.getRequestAttribute("BMPARAM")).getBmNo();
    return 0;
}



    public int preFind(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
        EventManager manager, PreparedStatement ps, PreparedStatement countps) {
    try {
        ps.setString(1, this.bmno);
        countps.setString(1, this.bmno);
    } catch (Exception ex) {
        ex.printStackTrace();
    }
    return 0;
}

}