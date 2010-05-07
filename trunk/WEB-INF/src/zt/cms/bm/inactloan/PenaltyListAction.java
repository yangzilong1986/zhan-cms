package zt.cms.bm.inactloan;

import zt.platform.form.control.FormActions;
import zt.platform.db.*;
import zt.platform.form.control.*;
import zt.platform.form.util.*;
import zt.platform.form.util.event.*;
import zt.cms.pub.code.*;
import zt.cms.bm.common.*;
import zt.cms.pub.*;
import zt.cmsi.biz.*;
import zt.cms.bm.common.*;
import zt.cmsi.pub.*;
import java.sql.*;
/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class PenaltyListAction
    extends FormActions {
    String bmno="";

    public int preFind(SessionContext ctx, DatabaseConnection conn, FormInstance instance,
                       ErrorMessages msgs,
                       EventManager manager, PreparedStatement ps, PreparedStatement countps)
    {
        try {
            String str = "select sum( PENALTYAMT ) from bmilpenalty  where bmno = '"+this.bmno+"'";
            RecordSet rs = conn.executeQuery(str);
            String value = "0.00";
            if(rs.next()){
                value = DBUtil.doubleToStr1(rs.getDouble(0));
            }
            //System.out.println("value=="+value);
            ctx.setAfterBody("PENALTYAMT=" + value + "");
            ps.setString(1, this.bmno);
            countps.setString(1,this.bmno);
        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }

        return 0;
    }

    public int load(SessionContext ctx, DatabaseConnection conn, FormInstance instance,
                    ErrorMessages msgs,
                    EventManager manager, String parameter)
    {
        //RightChecker.checkReadonly(ctx, conn, instance);
        this.bmno = ( (Param) ctx.getRequestAttribute("BMPARAM")).getBmNo();
        //System.out.println("bmno=="+bmno);
        return 0;
    }

    public int buttonEvent(SessionContext ctx, DatabaseConnection conn, FormInstance instance,
                           String button,
                           ErrorMessages msgs, EventManager manager)
    {
        Param param = new Param();
        param.addParam("BMNO", this.bmno);
        ctx.setRequestAtrribute("BMPARAM", param);
        trigger(manager, "BMILPENALTYPAGE", null);

        return 0;
    }


}
