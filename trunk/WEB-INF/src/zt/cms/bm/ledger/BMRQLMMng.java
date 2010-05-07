/*
 *  Copyright (c) 2004 Email: liwei@zhongtian.biz
 *  $Id: BMRQLMMng.java,v 1.1 2005/06/28 07:00:23 jgo Exp $
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

/**
 *  测试：多表－－－数据添加,修改，删除(获得值)
 *
 *@author     Administrator
 *@created    2003年12月2日
 */
public class BMRQLMMng
         extends FormActions {

//  private String clientno;

    private String strFlag = null;
    private boolean isAccept = false;
    private String strbmno = null;
    private String stractno = null;
    private String strcnlno = null;


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

        strFlag = String.valueOf(ctx.getRequestAttribute("flag"));
        Param pm = (Param)ctx.getRequestAttribute(ParamName.ParamName);
        strFlag = (String)pm.getParam(ParamName.Flag);
        strbmno = (String)pm.getParam(ParamName.BMNo);
        stractno = (String)pm.getParam("ACTNO");
        strcnlno = (String)pm.getParam("CNLNO");
        System.out.println("BMRQLMng.load(line:64):"+strFlag);
        System.out.println("BMRQLMng.load(line:65):"+strbmno);
        System.out.println("BMRQLMng.load(line:66):"+stractno);
        System.out.println("BMRQLMng.load(line:67):"+strcnlno);
        if ( strFlag == null|| strFlag.equals("read")) {
            instance.setReadonly(true);
            System.out.println("realonly:true");
        } else {
            instance.setReadonly(false);
            System.out.println("realonly:false");
        }
        // System.out.println("7345"+String.valueOf(ctx.getRequestAttribute("flag")));


        this.isAccept = Function.isDataExist(conn, "RQLOANLEDGER", "BMNO", strbmno,Function.STRING_TYPE);
        System.out.println("isAccept:"+isAccept);
        if (isAccept) {
            //System.out.println("1111:"+stracptbillno);
            if (strbmno != null) {
                instance.setValue("BMNO", strbmno.trim());
            }
//            if (!instance.isReadonly()) {
//                instance.setReadonly(true);
//            }
            trigger(manager, instance, EventType.EDIT_VIEW_EVENT_TYPE, Event.BRANCH_CONTINUE_TYPE);
        } else{
            if(Function.isDataExist(conn,"RQLOANLEDGER",("where ACTNO='"+stractno+"' and CNLNO='"+strcnlno+"'"))){
                instance.setValue("ACTNO",stractno);
                instance.setValue("CNLNO",strcnlno);
                trigger(manager, instance, EventType.EDIT_VIEW_EVENT_TYPE, Event.BRANCH_CONTINUE_TYPE);
            }else{
                msgs.clear();
                msgs.add("对不起，没有查询到任何记录。");
                return -1;
            }
        }

        return 0;
    }

    public int postEditOk(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
            EventManager manager) {
        ctx.setRequestAtrribute("title", Function.getFormTitleByInstance(instance));
        ctx.setRequestAtrribute("msg", "保存操作已成功！");
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


}
