package zt.cms.bm.inactloan;

import java.sql.*;
import zt.platform.db.*;
import zt.platform.form.control.*;
import zt.platform.form.util.*;
import zt.platform.form.util.event.*;
import zt.cms.bm.common.*;
import zt.cmsi.pub.*;

/**
 *  Description of the Class
 *
 *@author     Administrator
 *@created    2004Äê1ÔÂ5ÈÕ
 */
public class AdmChangeListAction extends FormActions {
    /**
     *  Description of the Method
     *
     *@param  ctx        Description of the Parameter
     *@param  conn       Description of the Parameter
     *@param  instance   Description of the Parameter
     *@param  msgs       Description of the Parameter
     *@param  manager    Description of the Parameter
     *@param  parameter  Description of the Parameter
     *@return            Description of the Return Value
     */
    String bmno="";


    public int load(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
            EventManager manager, String parameter) {
        RightChecker.checkReadonly(ctx,conn,instance);
        this.bmno = ((Param)ctx.getRequestAttribute("BMPARAM")).getBmNo();
        return 0;
    }



    /**
     *  Description of the Method
     *
     *@param  ctx       Description of the Parameter
     *@param  conn      Description of the Parameter
     *@param  instance  Description of the Parameter
     *@param  button    Description of the Parameter
     *@param  msgs      Description of the Parameter
     *@param  manager   Description of the Parameter
     *@return           Description of the Return Value
     */
    public int buttonEvent(SessionContext ctx, DatabaseConnection conn, FormInstance instance, String button,
            ErrorMessages msgs, EventManager manager) {
        ctx.setRequestAtrribute("couldNotChange","true");
        ctx.setRequestAtrribute("BMNO", this.bmno);
        Param param=new Param();
        param.addParam("BMNO",this.bmno);
        ctx.setRequestAtrribute("BMPARAM",param);


        trigger(manager, "BMLOANADMCHANGEPAGE", null);
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
     *@param  ps        Description of the Parameter
     *@param  countps   Description of the Parameter
     *@return           Description of the Return Value
     */
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
