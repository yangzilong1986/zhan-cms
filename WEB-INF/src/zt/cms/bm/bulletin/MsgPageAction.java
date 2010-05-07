package zt.cms.bm.bulletin;

import zt.platform.form.control.FormActions;

import java.sql.*;

import zt.platform.db.*;
import zt.platform.form.control.*;
import zt.platform.form.util.*;
import zt.platform.form.util.event.*;
import zt.cms.pub.code.*;
import zt.cmsi.pub.*;
import zt.cms.pub.*;
import zt.cms.bm.common.ParamFactory;
import zt.cms.bm.common.*;
import java.text.*;
import java.util.*;

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
 *@created    2004年2月19日
 *@version    1.0
 */

public class MsgPageAction extends FormActions {
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
        RightChecker.checkReadonly(ctx, conn, instance);
        return 0;
    }

    public int beforeInsert(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
        EventManager manager) {
    if(ctx.getParameter("TOUSER")!=null&&!ctx.getParameter("TOUSER").equals("")){
        instance.setValue("TOUSER",ctx.getParameter("TOUSER"));
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
    public int preInsert(SessionContext ctx, DatabaseConnection conn,
            FormInstance instance, ErrorMessages msgs,
            EventManager manager, SqlAssistor assistor) {
        String toUser = ctx.getParameter("TOUSER");
        RecordSet rs = conn.executeQuery("select * from scuser where loginname='"+toUser+"'");
        if(!rs.next()){
            ctx.setTarget("/showinfo.jsp");
            ctx.setRequestAtrribute("msg","\""+toUser+"\"是不存在的用户");
            return -1;
        }


        int seqNo = (int) SerialNumber.getNextSN(assistor.getDefaultTbl(), "MSGNO");
        if(seqNo>0){
          assistor.setSqlFieldValue(assistor.getDefaultTbl(), "MSGNO", seqNo + "");

          String operator = SessionInfo.getLoginUserName(ctx);
          if(operator == null)
          {
            msgs.add("未取得当前用户信息,请重新登录!");
            return -1;
          }
          instance.setValue("FROMUSER", operator);
          assistor.setSqlFieldValue(assistor.getDefaultTbl(), "FROMUSER",
                                    operator + "");

          SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
          String createDate = format.format(new java.util.Date());
          instance.setValue("CREATEDATE", createDate);
          assistor.setSqlFieldValue(assistor.getDefaultTbl(), "CREATEDATE",
                                    createDate + "");

          return 0;
        }else{
          msgs.add("数据库连接故障！请稍候再试");
          return -1;
        }
    }

}
