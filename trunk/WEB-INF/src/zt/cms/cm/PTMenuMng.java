/*
 *  Copyright (c) 2004 Email: liwei@zhongtian.biz
 *  $Id: PTMenuMng.java,v 1.1 2005/06/28 07:00:27 jgo Exp $
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
import zt.cms.pub.code.SerialNumber;

/**
 *  测试：多表－－－数据添加,修改，删除(获得值)
 *
 *@author     Administrator
 *@created    2003年12月2日
 */
public class PTMenuMng
         extends FormActions {

    private String clientno;


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
        String strmenuid = ctx.getParameter("MENUID");
        if (instance.getFormid().equals("PTMMNG") && strmenuid != null) {
            instance.setValue("MENUID", strmenuid.trim());
            trigger(manager, instance, EventType.EDIT_VIEW_EVENT_TYPE, Event.BRANCH_CONTINUE_TYPE);
        }
        return 0;
    }

    public int preDelete(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
            EventManager manager, SqlAssistor assistor) {
        String strmenuid = ctx.getParameter("MENUID");
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
        return 0;
    }

}
