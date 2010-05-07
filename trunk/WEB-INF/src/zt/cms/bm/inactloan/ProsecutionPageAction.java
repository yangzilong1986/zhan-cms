package zt.cms.bm.inactloan;


import java.sql.*;

import zt.platform.db.*;
import zt.platform.form.control.*;
import zt.platform.form.util.*;
import zt.platform.form.util.event.*;
import zt.cms.pub.code.*;
import zt.cmsi.pub.*;
import zt.cms.pub.*;
import zt.cms.bm.common.ParamFactory;
import zt.cms.bm.common.SessionInfo;

/**
 *  Description of the Class
 *
 *@author     Administrator
 *@created    2004年1月6日
 */
public class ProsecutionPageAction extends CommonPageAction {
    /**
     *  Gets the tableName attribute of the CommonPageAction object
     *
     *@return    The tableName value
     */
    int seqno = -1;

    public String getTableName() {
        return "BMILPROSECUTION";
    }

    public int load(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
        EventManager manager, String parameter) {
    super.load(ctx,conn,instance,msgs,manager,parameter);
    if (ctx.getParameter("SEQNO") != null) {
            instance.setValue(getAutoIncrementField(), ctx.getParameter(getAutoIncrementField()));
            trigger(manager, instance, EventType.EDIT_VIEW_EVENT_TYPE,
                    Event.BRANCH_CONTINUE_TYPE);
            this.seqno=Integer.parseInt(ctx.getParameter("SEQNO").trim());
        }



    return 0;
}



    public int beforeInsert(SessionContext ctx, DatabaseConnection conn, FormInstance instance,
                            ErrorMessages msgs,
                            EventManager manager)
    {
        instance.setFieldReadonly("ERSHEN",true);
        instance.setFieldReadonly("SHENSU",true);
       return super.beforeInsert(ctx, conn, instance, msgs, manager);
    }

    public int beforeEdit(SessionContext ctx, DatabaseConnection conn, FormInstance instance,
                          ErrorMessages msgs,
                          EventManager manager, SqlAssistor assistor)
    {
        String str = "select ifappeal,ifshensu from " + assistor.getDefaultTbl() + " where seqno=" +
            this.seqno;
        RecordSet rs = conn.executeQuery(str);
        if (rs.next()) {
            if (rs.getInt("IFAPPEAL") == 0) {
                instance.setFieldReadonly("ERSHEN", true);
            }else{
                instance.setFieldReadonly("ERSHEN", false);
            }
            if (rs.getInt("IFSHENSU") == 0) {
                instance.setFieldReadonly("SHENSU", true);
            }else{
                instance.setFieldReadonly("SHENSU", false);
            }
        }

        return 0;
    }

    public int buttonEvent(SessionContext ctx, DatabaseConnection conn, FormInstance instance, String button,
           ErrorMessages msgs, EventManager manager) {
       Param param = new Param();
       param.addParam("BMNO",this.param.getBmNo());
       ctx.setRequestAtrribute("SEQNO",this.seqno+"");
       if(this.seqno!=-1){
           if (button.equals("ERSHEN")) {
               ctx.setRequestAtrribute("BMPARAM", param);
               trigger(manager, "APPEALPAGE", null);
           }
           else {
               ctx.setRequestAtrribute("BMPARAM", param);
               trigger(manager, "SHENSUPAGE", null);
           }
       }else{
           msgs.add("请先输入诉讼基本资料！");
           return -1;
       }
       return 0;
   }




}
