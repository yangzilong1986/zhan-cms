package zt.cms.bm.inactloan;

import java.sql.*;
import zt.platform.db.*;
import zt.platform.form.control.*;
import zt.platform.form.util.*;
import zt.platform.form.util.event.*;
import zt.cms.bm.common.*;

public class BMInactLoanListAction2 extends FormActions {
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
       System.out.println("EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEe");
       trigger(manager, "BMINACTLOANPAGE2", null);
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
           EventManager manager, SqlWhereUtil sqlWhereUtil) {
       if (super.preFind(ctx, conn, instance, msgs, manager, sqlWhereUtil) >= 0) {
           String strinsql = SessionInfo.getLoginAllSubBrhIds(ctx);
           sqlWhereUtil.addWhereField("BMTABLE", "BRHID", strinsql, SqlWhereUtil.DataType_Sql, sqlWhereUtil.OperatorType_In);
       }
       return 0;
   }


}