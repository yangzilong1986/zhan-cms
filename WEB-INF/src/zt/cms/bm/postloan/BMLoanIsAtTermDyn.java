/*
 *  Copyright (c) 2004 Email: liwei@zhongtian.biz
 *  $Id: BMLoanIsAtTermDyn.java,v 1.1 2005/06/28 07:00:26 jgo Exp $
 */
package zt.cms.bm.postloan;

import zt.platform.db.DatabaseConnection;
import zt.platform.db.RecordSet;
import zt.platform.form.util.FormInstance;
import zt.platform.form.util.event.ErrorMessages;
import zt.platform.form.util.event.EventManager;
import zt.platform.form.util.SqlAssistor;
import zt.platform.form.util.SqlField;
import java.sql.PreparedStatement;
import zt.platform.form.control.SessionContext;
import zt.platform.form.control.FormActions;
import zt.platform.form.util.*;
import zt.platform.form.util.event.*;
import zt.platform.form.util.datatype.*;
import java.sql.*;
import zt.cmsi.pub.*;
import zt.cms.bm.bill.*;
import zt.cmsi.pub.confitem;

/**
 *  测试：多表－－－数据添加,修改，删除(获得值)
 *
 *@author     Administrator
 *@created    2003年12月2日
 */
public class BMLoanIsAtTermDyn
         extends FormActions {

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
    boolean firstRun = true;
    String strFlag = null;
    public int load(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
            EventManager manager, String parameter) {

           strFlag = ctx.getParameter("flag").trim();
          // System.out.println("BMLoanIsAtTermDyn.load(line:48):"+strFlag);
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
//    public int preFind(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
//            EventManager manager, PreparedStatement ps, PreparedStatement countps) {
//        //System.out.println("strFlag:"+strFlag);
//        instance.
//        if (strFlag.equals("read") || strFlag == null) {
//            instance.setReadonly(true);
//        } else {
//            instance.setReadonly(false);
//        }
//        return 0;
//
//    }

    public int preFind(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
            EventManager manager, SqlWhereUtil sqlWhereUtil) {

        if(firstRun == true)
        {
          firstRun = false;
          return confitem.QUERY_FORM_INPUT_CRITERIA_CODE;
        }


        if (strFlag.equals("read") || strFlag == null) {
            instance.setReadonly(true);
        } else {
            instance.setReadonly(false);
        }

        if (super.preFind(ctx, conn, instance, msgs, manager, sqlWhereUtil) >= 0) {
            // sqlWhereUtil.addWhereField("BMTABLE","BMNO","2");
            //sqlWhereUtil.addWhereField("BMTABLE","BMNO","BMTABLE.BMNO in ('1','2')",3);
            String strinsql = Function.getAllSubBrhIds(msgs, ctx);
            //System.out.println("insql:"+strinsql);
            sqlWhereUtil.addWhereField("RQLOANLEDGER", "BRHID", strinsql, SqlWhereUtil.DataType_Sql, sqlWhereUtil.OperatorType_In);
        }
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
    public int buttonEvent(SessionContext ctx, DatabaseConnection conn, FormInstance instance,
            String button,
            ErrorMessages msgs, EventManager manager) {
        //System.out.println("12341234444444444444444444444444444444444444444444444414512351234");
        String strbmno = ctx.getParameter("BMNO") == null ? null : ctx.getParameter("BMNO").trim();
        //System.out.println("BMLoanIsAtTermDyn.buttonEvent(line:113):"+strbmno);
        Param pm = new Param();
        pm.addParam(ParamName.Flag,strFlag);
        pm.addParam(ParamName.BMNo,strbmno);
        ctx.setRequestAtrribute(ParamName.ParamName, pm);

        if ((button != null) && (button.equals(SessionAttributes.REQUEST_BUTTON_ADD_NAME)
                                 || button.equals(SessionAttributes.REQUEST_EDIT_BUTTON_VALUE))) {
            this.trigger(manager, "BMALM0", null);
        }
        //
        return 0;
    }
    public int preReference(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
        EventManager manager, String reffldnm,SqlWhereUtil sqlWhereUtil) {

        String strinsql = Function.getAllSubBrhIds(msgs, ctx);
        sqlWhereUtil.addWhereField("SCBRANCH", "BRHID", strinsql, SqlWhereUtil.DataType_Sql, sqlWhereUtil.OperatorType_In);

    return 0;
}


}
