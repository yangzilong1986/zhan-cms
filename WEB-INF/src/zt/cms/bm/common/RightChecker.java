package zt.cms.bm.common;

import zt.platform.form.control.FormActions;
import zt.platform.form.control.FormActions;
import zt.platform.form.control.*;
import zt.platform.db.DatabaseConnection;
import zt.platform.db.RecordSet;
import zt.platform.form.util.FormInstance;
import zt.platform.form.util.event.ErrorMessages;
import zt.platform.form.util.event.EventManager;
import zt.platform.form.util.SqlAssistor;
import java.sql.PreparedStatement;
import java.sql.*;
import java.util.logging.*;
import zt.platform.form.util.event.EventType;
import zt.platform.form.util.event.Event;
import zt.cms.pub.code.SerialNumber;
import zt.platform.form.util.SessionAttributes;
import java.util.logging.*;
import zt.cmsi.pub.*;

/**
 *  <p>
 *
 *  Title: </p> <p>
 *
 *  Description: </p> <p>
 *
 *  Copyright: Copyright (c) 2003</p> <p>
 *
 *  Company: </p>
 *
 *@author     not attributable
 *@created    2003年11月20日
 *@version    1.0
 */

public class RightChecker {
    /**
     *  Constructor for the RightChecker object
     */
    public RightChecker() { }


    /**
     *  Description of the Field
     *
     *@author    Administrator
     *@since     2003年12月2日
     */
    public static Logger logger = Logger.getLogger("zt.cms.cm.common.RightChecker");


    /**
     *  Gets the readonly attribute of the RightChecker class
     *
     *@param  ctx       Description of the Parameter
     *@param  conn      Description of the Parameter
     *@param  instance  Description of the Parameter
     */
    public static void checkReadonly(SessionContext ctx, DatabaseConnection conn, FormInstance instance) {
        Param param = (Param) ctx.getRequestAttribute("BMPARAM");
        String flag=ctx.getParameter("flag");
        if (param == null) {
            if (flag != null && flag.equals("write")) {
                instance.setReadonly(false);
            } else {
                instance.setReadonly(true);
            }
        } else {
            if(flag==null || flag.trim().length()<1) flag = (String) param.getParam(ParamName.Flag);
            if (flag != null && flag.equals("write")) {
                instance.setReadonly(false);
            } else {
                instance.setReadonly(true);
            }
        }
    }


    /**
     *  Description of the Method
     *
     *@param  ctx       Description of the Parameter
     *@param  conn      Description of the Parameter
     *@param  instance  Description of the Parameter
     */
    public static void transReadOnly(SessionContext ctx, DatabaseConnection conn, FormInstance instance) {
        if (instance.isReadonly() == true) {
            Param param = (Param) ctx.getRequestAttribute("BMPARAM");
            if (param == null) {
                param=new Param();
            }
            ctx.setRequestAtrribute("BMPARAM",param);
            param.addParam("flag", "read");
            ctx.setRequestAtrribute("flag", "read");
        } else {
            Param param = (Param) ctx.getRequestAttribute("BMPARAM");
            if (param == null) {
                param=new Param();
            }
            ctx.setRequestAtrribute("BMPARAM",param);
            param.addParam("flag", "write");
            ctx.setRequestAtrribute("flag", "write");
        }

    }

}
