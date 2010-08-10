/*
 *  Copyright (c) 2004 Email: liwei@zhongtian.biz
 *  $Id: BMRQLMLDyn.java,v 1.1 2005/06/28 07:00:23 jgo Exp $
 */
package zt.cms.bm.ledger;

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
/**
 *  ���ԣ������������������,�޸ģ�ɾ��(���ֵ)
 *
 *@author     Administrator
 *@created    2003��12��2��
 */
public class BMRQLMLDyn
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

    String strFlag = null;

    public int load(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
            EventManager manager, String parameter) {
           strFlag = ctx.getParameter("flag")==null?null:ctx.getParameter("flag").trim();

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
//
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

        String strbmno = ctx.getParameter("BMNO")==null?null:ctx.getParameter("BMNO").trim();
        String stractno = ctx.getParameter("ACTNO")==null?null:ctx.getParameter("BMNO").trim();
        String strcnlno = ctx.getParameter("CNLNO")==null?null:ctx.getParameter("BMNO").trim();

        Param pm = new Param();
        pm.addParam(ParamName.Flag,strFlag);
        pm.addParam(ParamName.BMNo,strbmno);
        pm.addParam("ACTNO",stractno);
        pm.addParam("CNLNO",strcnlno);
        ctx.setRequestAtrribute(ParamName.ParamName,pm);
        if ((button != null) && (button.equals(SessionAttributes.REQUEST_BUTTON_ADD_NAME)
                                 || button.equals(SessionAttributes.REQUEST_EDIT_BUTTON_VALUE))) {
            this.trigger(manager, "BMRQLM", null);
        }
        //
        return 0;
    }
//    public int preDelete(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
//            EventManager manager, SqlAssistor assistor) {
//        String strmenuid = ctx.getParameter("MENUID");
//        System.out.println("MENUID:"+strmenuid);
//        String strsql = null;
//        boolean isPIDUsed = false;
//
//        if(strmenuid!=null){
//            strsql = "select * from ptmenu where parentid='"+strmenuid+"'";
//        }
//        if(strsql!=null){
//            RecordSet rs = null;
//            try {
//                rs = conn.executeQuery(strsql);
//            }
//            catch (Exception ex) {
//                ex.printStackTrace();
//                return -1;
//            }
//            if(rs.next()){
//                isPIDUsed = true;
//            }
//        }
//        if( isPIDUsed ){
//            msgs.clear();
//            msgs.add("�ò˵����������Ӳ˵�ʹ�ã�����ɾ���������Ķ������ļ���");
//            return -1;
//        }
//        return 0;
//    }

}