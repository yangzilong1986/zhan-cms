/*
 *  Copyright (c) 2004 Email: liwei@zhongtian.biz
 *  $Id: PTResourceMng.java,v 1.1 2005/06/28 07:00:27 jgo Exp $
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
public class PTResourceMng
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
        String strresourceid = ctx.getParameter("RESOURCEID");
        if (instance.getFormid().equals("PTRMNG") && strresourceid != null) {
            instance.setValue("RESOURCEID", strresourceid.trim());
            trigger(manager, instance, EventType.EDIT_VIEW_EVENT_TYPE, Event.BRANCH_CONTINUE_TYPE);
        }
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
    public int preInsert(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
            EventManager manager, SqlAssistor assistor) {
        boolean isrexist = false;
        boolean ispexist = false;
        isrexist = this.IsRIDExist(ctx,conn);
        ispexist = this.IsPIDExist(ctx,conn);
        if (isrexist || (!ispexist)) {
            msgs.clear();
            msgs.add("新建失败。原因:");
            if(isrexist){
                msgs.add("@该资源标识符已经被占用。");
            }
            if(!ispexist){
                msgs.add("@该父资源不存在。");
            }
            return -1;
        }
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
    public int preEdit(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
            EventManager manager, SqlAssistor assistor) {
        boolean isrexist = false;
        boolean ispexist = false;
        isrexist = this.IsRIDExist(ctx,conn);
        ispexist = this.IsPIDExist(ctx,conn);
        if (isrexist || (!ispexist)) {
            if(!ispexist){
                msgs.clear();
                msgs.add("新建失败。原因:");
                msgs.add("@该父资源不存在。");
                return -1;
            }
        }
        if(!isrexist){
            msgs.clear();
            msgs.add("新建失败。原因:");
            msgs.add("@需要的记录不存在！");
            return -1;
        }

        return 0;
    }


    /**
     *  资源标示符是否存在
     *
     *@param  ctx   Description of the Parameter
     *@param  conn  Description of the Parameter
     *@return       Description of the Return Value
     */
    public boolean IsRIDExist(SessionContext ctx, DatabaseConnection conn) {
        String strresourceid = ctx.getParameter("RESOURCEID");
        String strsql = null;

        if (strresourceid != null) {
            strsql = "select * from ptresource where RESOURCEID='" + strresourceid + "'";
        }
        return this.IsDataExist(conn, strsql);
    }


    /**
     *  父资源是否存在
     *
     *@param  ctx   Description of the Parameter
     *@param  conn  Description of the Parameter
     *@return       Description of the Return Value
     */
    public boolean IsPIDExist(SessionContext ctx, DatabaseConnection conn) {
        String strparentid = ctx.getParameter("PARENTID");
        String strsql = null;
        if(strparentid==null||strparentid.equals("")){
            return true;
        }else{
            strsql = "select * from ptresource where RESOURCEID='" + strparentid + "'";
        }
        return this.IsDataExist(conn, strsql);
    }


    /**
     *  判断是否存在记录
     *
     *@param  conn    Description of the Parameter
     *@param  strsql  Description of the Parameter
     *@return         Description of the Return Value
     */
    public static boolean IsDataExist(DatabaseConnection conn, String strsql) {
        boolean isObjectExist = false;
        if (strsql != null) {
            RecordSet rs = null;
            try {
                rs = conn.executeQuery(strsql);
            } catch (Exception ex) {
                ex.printStackTrace();
                return isObjectExist;
            }
            if (rs.next()) {
                isObjectExist = true;
            }
        }
        return isObjectExist;
    }
    public int postDeleteFail(SessionContext ctx, DatabaseConnection conn, FormInstance instance,
                              ErrorMessages msgs,
                              EventManager manager)
    {
        if (msgs.size() != 0) {
            for (int i = 0; i < msgs.size(); i++) {
                ErrorMessage em = msgs.get(i);

                System.out.println("error" + i + ":" + em.getMessage());
                System.out.println("argument"+i+":"+em.getArguments());
                if (em.getMessage().equals("-532")) {
                    msgs.clear();
                    msgs.add("该资源已被某角色使用。请您阅读帮助文件。");
                    break;
                }

            }
        }
        return 0;
    }

}
