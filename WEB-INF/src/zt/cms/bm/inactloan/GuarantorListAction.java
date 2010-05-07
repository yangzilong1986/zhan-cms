package zt.cms.bm.inactloan;
import java.sql.*;
import zt.platform.db.*;
import zt.platform.form.control.*;
import zt.platform.form.util.*;
import zt.platform.form.util.event.*;
import zt.cms.bm.common.*;
import zt.cmsi.pub.*;

public class GuarantorListAction
    extends FormActions {
    String bmno = "";

    public int load(SessionContext ctx, DatabaseConnection conn, FormInstance instance,
                    ErrorMessages msgs,
                    EventManager manager, String parameter)
    {
        super.load(ctx, conn, instance, msgs, manager, parameter);
        RightChecker.checkReadonly(ctx, conn, instance);
        this.bmno = ( (Param) ctx.getRequestAttribute("BMPARAM")).getBmNo();
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
public int buttonEvent(SessionContext ctx, DatabaseConnection conn, FormInstance instance,
                       String button,
                       ErrorMessages msgs, EventManager manager)
{

    ctx.setRequestAtrribute("BMNO", this.bmno);
    Param param = new Param();
    param.addParam("BMNO", this.bmno);
    ctx.setRequestAtrribute("BMPARAM", param);

    RightChecker.transReadOnly(ctx, conn, instance);
    trigger(manager, "BMGUARANTORPAGE", null);
    return 0;
}




    /**
     *  Description of the Method
     *
     *@param  ctx       Description of the Parameter
     *@param  conn      Description of the Parameter
     *@param  instance  Description of the Parameter
     *@param  msgs      Description of the Parameter
     *@param  manager   Description of the Parameter
     *@param  assistor  Description of the Parameter
     *@return           Description of the Return Value
     */


}
