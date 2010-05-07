/*
 *  Copyright (c) 2004 Email: liwei@zhongtian.biz
 *  $Id: PTMenuListDyn.java,v 1.1 2005/06/28 07:00:27 jgo Exp $
 */
package zt.cms.cm;

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
/**
 *  测试：多表－－－数据添加,修改，删除(获得值)
 *
 *@author     Administrator
 *@created    2003年12月2日
 */
public class PTMenuListDyn
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
    public int load(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
            EventManager manager, String parameter) {
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
        String strFlag = String.valueOf(ctx.getAttribute("flag"));

        if (strFlag.equals("read") || strFlag == null) {
            instance.setReadonly(true);
        } else {
            instance.setReadonly(false);
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
        System.out.println("button:"+ button);
        if ((button != null) && (button.equals(SessionAttributes.REQUEST_BUTTON_ADD_NAME)
                                 || button.equals(SessionAttributes.REQUEST_EDIT_BUTTON_VALUE))) {
            this.trigger(manager, "PTMMNG", null);
        }

        if ((button != null) && button.equals(SessionAttributes.REQUEST_BUTTON_DELETE_NAME)) {
            String strmenuid = ctx.getParameter("MENUID");
            System.out.println("MENUID:"+strmenuid);
            String strsql = null;
            boolean isPIDUsed = false;

            if(strmenuid!=null){
                strsql = "select * from ptmenu where parentid='"+strmenuid+"'";
            }
            if(strsql!=null){
                RecordSet rs = null;
                try {
                    rs = conn.executeQuery(strsql);
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                    return -1;
                }
                if(rs.next()){
                    isPIDUsed = true;
                }
            }
            if( isPIDUsed ){
                msgs.clear();
                msgs.add("该菜单已有其他子菜单使用，具体删除步骤请阅读帮助文件。");
                return -1;
            }
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
//            msgs.add("该菜单已有其他子菜单使用，具体删除步骤请阅读帮助文件。");
//            return -1;
//        }
//        return 0;
//    }

}
