/*
 *  Copyright (c) 2004 Email: liwei@zhongtian.biz
 *  $Id: BMPLCDyn.java,v 1.1 2005/06/28 07:00:26 jgo Exp $
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

/**
 *  测试：多表－－－数据添加,修改，删除(获得值)
 *
 *@author     Administrator
 *@created    2003年12月2日
 */
public class BMPLCDyn
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

    private String strFlag = null;
    private String strBmNo = null;
    private Param pm = null;

    public int load(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
            EventManager manager, String parameter) {
  System.out.println("TTTTTTTTTTTTT");

            if(ctx.getRequestAttribute("title")!=null){
                //System.out.println("TTTTTTTTTTTTT");
                instance.useCloneFormBean();
                instance.getFormBean().setTitle("风险检查");
            }
            pm = (Param)ctx.getRequestAttribute(ParamName.ParamName);
            if(pm!=null){
                strFlag = (String) pm.getParam(ParamName.Flag);
                strBmNo = (String) pm.getParam(ParamName.BMNo);
            }
            if(strBmNo==null || strBmNo.equals("")){
                msgs.clear();
                msgs.add("此业务号格式不正确。");
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
     *@param  ps        Description of the Parameter
     *@param  countps   Description of the Parameter
     *@return           Description of the Return Value
     */



    public int preFind(SessionContext ctx, DatabaseConnection conn, FormInstance instance,
                       ErrorMessages msgs,
                       EventManager manager, SqlWhereUtil sqlWhereUtil) {

        if (strFlag == null || strFlag.equals("read")) {
            instance.setReadonly(true);
        } else {
            instance.setReadonly(false);
        }

        if (super.preFind(ctx, conn, instance, msgs, manager, sqlWhereUtil) >= 0) {
            if(strBmNo!=null){
                sqlWhereUtil.addWhereField("BMPOSTLOANCHECK","BMNO",strBmNo,SqlWhereUtil.DataType_Character,SqlWhereUtil.OperatorType_Equals);
            }
            System.out.println("BMPLCDyn.preFind(line:95)Bmno:"+strBmNo);
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
        Param pm = new Param();
        pm.addParam(ParamName.BMNo,strBmNo);
        pm.addParam(ParamName.Flag,strFlag);
        ctx.setRequestAtrribute(ParamName.ParamName, pm);

        ctx.setRequestAtrribute("title","风险检查");

        if ((button != null) && (button.equals(SessionAttributes.REQUEST_BUTTON_ADD_NAME)
                                 || button.equals(SessionAttributes.REQUEST_EDIT_BUTTON_VALUE))) {
            this.trigger(manager, "BMPLC0", null);
        }

        return 0;
    }


}
