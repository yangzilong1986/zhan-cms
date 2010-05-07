/*
 *  Copyright (c) 2004 Email: liwei@zhongtian.biz
 *  $Id: SCBranchMng.java,v 1.1 2005/06/28 07:00:30 jgo Exp $
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
public class SCBranchMng
         extends FormActions {

    private String strbrhid = null;


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
    public int load(SessionContext ctx, DatabaseConnection conn, FormInstance instance,
            ErrorMessages msgs,
            EventManager manager, String parameter) {
        instance.setValue("BRHID", ctx.getParameter("BRHID"));
        strbrhid = ctx.getParameter("BRHID");
        if (strbrhid != null) {
            this.trigger(manager, instance, EventType.FIND_EVENT_TYPE, Event.BRANCH_CONTINUE_TYPE);
        }
        return 0;
    }

    public int buttonEvent(SessionContext ctx, DatabaseConnection conn,
                           FormInstance instance, String button,
                           ErrorMessages msgs, EventManager manager) {

      if(strbrhid != null)
      {
        ctx.setRequestAtrribute("BRHID", strbrhid);

        if (button.equals("CMSBRH")) {
          trigger(manager, "CMSBAP", null);
        }
        return 0;
      }
      else
      {
        msgs.add("没有当前网点代码!");
        return -1;
      }
    }
}
