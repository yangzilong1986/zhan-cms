package zt.cms.bm.appplus;


import zt.platform.form.control.FormActions;
import zt.platform.form.control.FormActions;
import zt.platform.form.control.FormActions;
import zt.platform.form.control.*;
import zt.platform.db.DatabaseConnection;
import zt.platform.db.RecordSet;
import zt.platform.form.util.FormInstance;
import zt.platform.form.util.event.ErrorMessages;
import zt.platform.form.util.event.EventManager;
import zt.platform.form.util.SqlAssistor;
import java.sql.PreparedStatement;
import java.sql.*;
import java.util.logging.*;
import zt.platform.form.util.event.EventType;
import zt.platform.form.util.event.Event;
import zt.cms.pub.code.SerialNumber;
import zt.platform.form.util.SessionAttributes;
import zt.cms.cm.common.RightChecker;
import zt.cmsi.pub.*;
import zt.cms.bm.common.ParamFactory;
import zt.platform.form.util.*;
import zt.platform.user.*;
import zt.cms.pub.*;
import zt.cms.pub.*;
import zt.platform.db.*;
import zt.platform.form.config.*;
import zt.platform.form.control.*;
import zt.platform.form.util.*;
import zt.platform.form.util.event.*;
import zt.platform.user.*;
import zt.cmsi.pub.define.*;
import zt.cms.bm.common.*;
import zt.cmsi.mydb.MyDB;
import zt.cmsi.biz.*;
import java.math.*;
import java.util.*;
import zt.cmsi.biz.*;



public class BMPldgBillRedisPageAction extends FormActions {
    /**
 *  Description of the Field
 */
public String bmNo = "";
public String brhid="";




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
    //RightChecker.checkReadonly(ctx,conn,instance);
    Param param = (Param) ctx.getRequestAttribute("BMPARAM");
    //System.out.println("Param:"+param);
    this.bmNo = param.getBmNo();
    BMTableData data = BMTable.getBMTable(this.bmNo);
    this.brhid=data.brhID;
    if (ctx.getParameter("BILLDISNO") != null && !ctx.getParameter("BILLDISNO").equals("")) {
        instance.setValue("BILLDISNO",ctx.getParameter("BILLDISNO"));
        instance.setValue("BMNO",ctx.getParameter("BMNO"));
        trigger(manager, instance, EventType.EDIT_VIEW_EVENT_TYPE,
                Event.BRANCH_CONTINUE_TYPE);
    } else {
        trigger(manager, instance, EventType.INSERT_VIEW_EVENT_TYPE,
                Event.BRANCH_CONTINUE_TYPE);
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
 *@return           Description of the Return Value
 */
public int beforeInsert(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
        EventManager manager) {
    instance.setValue("BMNO", this.bmNo);
    instance.setFieldReadonly("BMNO", true);
    instance.setValue("OPERATOR", SessionInfo.getLoginUserName(ctx));
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
    if(validBillDisNo(Integer.parseInt(ctx.getParameter("BILLDISNO")),conn,msgs)&&
       !hasDuplicate(Integer.parseInt(ctx.getParameter("BILLDISNO")),conn,msgs)){
//        int pledgeNo = (int) SerialNumber.getNextSN("BMPLDGBILLREDIS",
//            "BILLDISNO");
//        instance.setValue("BILLDISNO", pledgeNo);
//        assistor.setSqlFieldValue(assistor.getDefaultTbl(), "BILLDISNO",
//                                  pledgeNo + "");

        String operator = SessionInfo.getLoginUserName(ctx);
        instance.setValue("OPERATOR", operator);
        assistor.setSqlFieldValue(assistor.getDefaultTbl(), "OPERATOR",
                                  operator + "");
        return 0;
    }else{
        return -1;
    }



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
        if(validBillDisNo(Integer.parseInt(ctx.getParameter("BILLDISNO")),conn,msgs)){
        String operator = SessionInfo.getLoginUserName(ctx);
        instance.setValue("OPERATOR", operator);
        assistor.setSqlFieldValue(assistor.getDefaultTbl(), "OPERATOR",
                                  operator + "");
        return 0;
    }else{

        return -1;
    }

}

public int preReference(SessionContext ctx, DatabaseConnection conn,
                        FormInstance instance, ErrorMessages msgs,
                        EventManager manager, String reffldnm,
                        SqlWhereUtil sqlWhereUtil) {
    sqlWhereUtil.addWhereField("BMBILLDIS", "BILLDISSTATUS", "1",
                           SqlWhereUtil.DataType_Number,
                           sqlWhereUtil.OperatorType_Equals);



    sqlWhereUtil.addWhereField("BMBILLDIS", "BRHID", this.brhid,
                   SqlWhereUtil.DataType_Character,
                   sqlWhereUtil.OperatorType_Equals);

    return 0;
}

public boolean validBillDisNo(int billDisNo,DatabaseConnection con,ErrorMessages msgs){
        RecordSet rs =con.executeQuery("select * from BMBILLDIS where billdisstatus=1 and billdisno="+billDisNo+" and brhid='"+brhid+"'");
        if(rs.next()){
            return true;
        }else{
            msgs.add("不合法贴现台帐代码");
            return false;
        }
}
    public boolean hasDuplicate(int billDisNo,DatabaseConnection con,ErrorMessages msgs){
        RecordSet rs =con.executeQuery("select * from BMPLDGBILLREDIS where billdisno="+billDisNo+" and bmno='"+bmNo+"'");
        if(rs.next()){
            msgs.add("已经存在的台帐代码");
            return true;
        }else{
            return false;
        }
    }


}
