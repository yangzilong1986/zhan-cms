/*
 *  Copyright (c) 2004 Email: liwei@zhongtian.biz
 *  $Id: BMTXTMMng.java,v 1.1 2005/06/28 07:00:23 jgo Exp $
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
import zt.cms.pub.code.SerialNumber;
import zt.cms.bm.bill.*;
import zt.cmsi.biz.*;
import zt.cmsi.pub.define.*;
import zt.cmsi.pub.*;
import zt.cmsi.pub.code.*;

/**
 *  测试：多表－－－数据添加,修改，删除(获得值)
 *
 *@author     Administrator
 *@created    2003年12月2日
 */
public class BMTXTMMng
    extends FormActions {

//  private String clientno;

    private String strFlag = null;
    private boolean isAccept = false;
    private String strbilldisno = null;

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
                    EventManager manager, String parameter)
    {

        strFlag = String.valueOf(ctx.getRequestAttribute("flag"));

        if (strFlag == null || strFlag.equals("read")) {
            instance.setReadonly(true);
            System.out.println("realonly:true");
        }
        else {
            instance.setReadonly(false);
            System.out.println("realonly:false");
        }
        // System.out.println("7345"+String.valueOf(ctx.getRequestAttribute("flag")));
        strbilldisno = ctx.getParameter("BILLDISNO") == null ? null :
            ctx.getParameter("BILLDISNO").trim();
        if (strbilldisno == null) {
            if (strFlag != null && strFlag.equals("write")) {
                trigger(manager, instance, EventType.INSERT_VIEW_EVENT_TYPE,
                        Event.BRANCH_CONTINUE_TYPE);
            }
            else {
                msgs.clear();
                msgs.add("您没有足够的权限增加记录。");
                return -1;
            }
        }
        else {

            this.isAccept = Function.isDataExist(conn, "BMBILLDIS", "BILLDISNO", strbilldisno,
                                                 Function.INTEGER_TYPE);
            System.out.println("isAccept:" + isAccept);
            if (isAccept) {
                //System.out.println("1111:"+stracptbillno);
                if (strbilldisno != null) {
                    instance.setValue("BILLDISNO", strbilldisno);
                }
//            if (!instance.isReadonly()) {
//                instance.setReadonly(true);
//            }
                trigger(manager, instance, EventType.EDIT_VIEW_EVENT_TYPE,
                        Event.BRANCH_CONTINUE_TYPE);
            }
            else {
                msgs.clear();
                msgs.add("对不起，没有查询到任何记录。");
                return -1;
            }
        }
        return 0;
    }

    public int beforeInsert(SessionContext ctx, DatabaseConnection conn, FormInstance instance,
                            ErrorMessages msgs,
                            EventManager manager)
    {

        instance.setValue("BILLDISNO", "0");
        instance.setFieldReadonly("BILLDISNO", true);
        instance.setFieldVisible("BILLDISNO", true);
//        System.out.println("BMAcptBillTMMng.beforeInsert(line:114):"+instance.getValue("BILLDISNO").isVisible());
        return 0;
    }

    public int preInsert(SessionContext ctx, DatabaseConnection conn, FormInstance instance,
                         ErrorMessages msgs,
                         EventManager manager, SqlAssistor assistor)
    {
        String strNowBrhid = ctx.getParameter("BRHID");
        if (!Function.isNowBrhidRight(ctx, strNowBrhid, Function.getBrhid(ctx), "业务网点不在维护网点内。",
                                      msgs)) {
            return -1;
        }

        int iacptbillno = BMBILLDIS.getNextNo();
        String strBillDisno = String.valueOf(iacptbillno);
        assistor.setSqlFieldValue(assistor.getDefaultTbl(), "BILLDISNO", strBillDisno);
        return 0;
    }

    public int postEditOk(SessionContext ctx, DatabaseConnection conn, FormInstance instance,
                          ErrorMessages msgs,
                          EventManager manager)
    {
        ctx.setRequestAtrribute("title", Function.getFormTitleByInstance(instance));
        ctx.setRequestAtrribute("msg", "保存操作已成功！");
        ctx.setRequestAtrribute("flag", "1");
        ctx.setRequestAtrribute("isback", "0");
        ctx.setTarget("/showinfo.jsp");

        return 0;
    }

    public int postInsertOk(SessionContext ctx, DatabaseConnection conn, FormInstance instance,
                            ErrorMessages msgs,
                            EventManager manager)
    {
        ctx.setRequestAtrribute("title", Function.getFormTitleByInstance(instance));
        ctx.setRequestAtrribute("msg", "增加操作已成功！");
        ctx.setRequestAtrribute("flag", "1");
        ctx.setRequestAtrribute("isback", "0");
        ctx.setTarget("/showinfo.jsp");

        return 0;
    }

    public int preReference(SessionContext ctx, DatabaseConnection conn,
                            FormInstance instance, ErrorMessages msgs,
                            EventManager manager, String reffldnm,
                            SqlWhereUtil sqlWhereUtil)
    {
        String strinsql = Function.getAllSubBrhIds(msgs, ctx);

        if (reffldnm.equals("BRHID")) {
            sqlWhereUtil.addWhereField("SCBRANCH", "BRHID", strinsql,
                                       SqlWhereUtil.DataType_Sql,
                                       sqlWhereUtil.OperatorType_In);

        }
        return 0;
    }

    public int preEdit(SessionContext ctx, DatabaseConnection conn, FormInstance instance,
                       ErrorMessages msgs,
                       EventManager manager, SqlAssistor assistor)
    {
        String strNowBrhid = ctx.getParameter("BRHID");
        if (!Function.isNowBrhidRight(ctx, strNowBrhid, Function.getBrhid(ctx), "业务网点不在维护网点内。",
                                      msgs)) {
            return -1;
        }
        return 0;
    }

//    public int buttonEvent(SessionContext ctx, DatabaseConnection conn, FormInstance instance,
//                           String button,
//                           ErrorMessages msgs, EventManager manager)
//    {
//
//
//
//        if (button == null || button.equals("")) {
//            msgs.clear();
//            msgs.add("请求的服务不存在！");
//            return -1;
//        }
//
//        String strbilldisstatus = ctx.getParameter("BILLDISSTATUS").trim();
//        System.out.println("flag:"+strFlag+"\nbilldisno:"+strbilldisno);
//        Param pm = new Param();
//        pm.addParam(ParamName.Flag,strFlag);
//        pm.addParam("BILLDISNO",strbilldisno);
//
//        System.out.println("strbilldisstatus:"+strbilldisstatus);
//        if(button.equals("ACPTBILLBUTTON")){
//            if(strbilldisstatus!=null){
//                if(strbilldisstatus.equals("1")||strbilldisstatus.equals("2")||
//                   strbilldisstatus.equals("3")||strbilldisstatus.equals("4")){
//                    ctx.setRequestAtrribute(ParamName.ParamName,pm);
//                    this.trigger(manager, "BMBDH0", null);
//                }else{
//                    pm.addParam("FORMID","BMBDH0");
//                    ctx.setRequestAtrribute(ParamName.ParamName,pm);
//                    this.trigger(manager, "BMBDH0", null);
//                }
//            }
//
//            //查看汇票承兑
//            //this.trigger(manager,"BMHPC0",null);
//        }
//
//        return 0;
//    }

}
