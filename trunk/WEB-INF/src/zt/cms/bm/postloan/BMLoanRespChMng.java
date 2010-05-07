/*
 *  Copyright (c) 2004 Email: liwei@zhongtian.biz
 *  $Id: BMLoanRespChMng.java,v 1.1 2005/06/28 07:00:26 jgo Exp $
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
import zt.cms.pub.code.SerialNumber;
import zt.cms.bm.bill.*;
import zt.cmsi.biz.*;
import zt.cmsi.pub.define.*;
import zt.cmsi.mydb.*;
import zt.platform.db.*;
import zt.cmsi.pub.*;
import java.math.*;
import zt.cmsi.pub.code.*;

/**
 *  测试：多表－－－数据添加,修改，删除(获得值)
 *
 *@author     Administrator
 *@created    2003年12月2日
 */
public class BMLoanRespChMng
    extends FormActions {

//  private String clientno;

    private boolean isAccept = false;
    private String strFlag = null;
    private String strBmNo = null;
    private Param pm = null;

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
        pm = (Param) ctx.getRequestAttribute(ParamName.ParamName);
        if (pm != null) {
            strFlag = (String) pm.getParam(ParamName.Flag);
            strBmNo = (String) pm.getParam(ParamName.BMNo);
            //System.out.println("strFlag:"+strFlag+"\nstrBmNo:"+strBmNo);
        }
        if (strBmNo == null || strBmNo.equals("")) {
            msgs.clear();
            msgs.add("此业务号格式不正确。");
            return -1;
        }

        if (strFlag.equals("read") || strFlag == null) {
            instance.setReadonly(true);
        }
        else {
            //取默认值
        }

        /*if ( strFlag == null|| strFlag.equals("read")) {
            instance.setReadonly(true);
            System.out.println("realonly:true");
                 } else {
            instance.setReadonly(false);
            System.out.println("realonly:false");
                 }*/
        // System.out.println("7345"+String.valueOf(ctx.getRequestAttribute("flag")));
//        strcnlno = ctx.getParameter("CNLNO") == null ? null : ctx.getParameter("CNLNO").trim();
        //System.out.println("strbmno:"+strbmno);
//        if (Function.isDataExist(conn, "BMLOANRESPCHANGE", "BMNO", strBmNo, Function.STRING_TYPE)) {
//            instance.setValue("BMNO", strBmNo);
//            trigger(manager, instance, EventType.EDIT_VIEW_EVENT_TYPE, Event.BRANCH_CONTINUE_TYPE);
//        }
//        else {
            trigger(manager, instance, EventType.INSERT_VIEW_EVENT_TYPE, Event.BRANCH_CONTINUE_TYPE);
//        }

        return 0;
    }

    public int beforeInsert(SessionContext ctx, DatabaseConnection conn, FormInstance instance,
                            ErrorMessages msgs,
                            EventManager manager)
    {
        //System.out.println("BMLoanRespChMng.beforeInsert(line:104):" + 1);
        if (!this.isAccept && strFlag.equals(strFlag)) {
            SystemDate sd = new SystemDate();
            String strcurrdate = sd.getSystemDate5("");
            instance.setValue("CREATEDATE", strcurrdate);
            instance.setFieldReadonly("CREATEDATE", true);

            instance.setValue("BMNO", strBmNo);
            LoanLedgerMan llm = new LoanLedgerMan();
            LoanLedger ll = null;
            if (strBmNo != null) {
                ll = llm.getLoanLedger(strBmNo);
            }
            if (ll == null) {
                msgs.clear();
                msgs.add("没有取到公共模块");
                return -1;
            }
            else {
                String strfirstResp = ll.firstResp;
                String strfirstRespPct = ll.firstRespPct.toString();
                System.out.println("BMLoanRespChMng.beforeInsert(line:126):"+strfirstResp);
                System.out.println("BMLoanRespChMng.beforeInsert(line:127):"+strfirstRespPct);
                instance.setValue("OLDRESP", strfirstResp);
                instance.setValue("OLDPCT", strfirstRespPct);
            }

            String strUserName = Function.getUserName(ctx);
            //System.out.println("username:"+strUserName);
            if (strUserName != null) {
                instance.setValue("OPERATOR", strUserName);
            }
            else {
                return -1;
            }

        }
        return 0;
    }

    public int buttonEvent(SessionContext ctx, DatabaseConnection conn, FormInstance instance,
                           String button,
                           ErrorMessages msgs, EventManager manager)
    {
        if (button == null) {
            msgs.clear();
            msgs.add("没有值！");
            return -1;
        }

        String strevent = ctx.getParameter(SessionAttributes.REQUEST_EVENT_VALUE_NAME).trim();
        if (button.equals("SAVEBUTTON")) {
//            System.out.println("BMLoanRespChMng.buttonEvent(line:155):" + 1);
//            if (Integer.parseInt(strevent) == EventType.EDIT_SMALL_QUERY_EVENT_TYPE) {
//                System.out.println("BMLoanRespChMng.buttonEvent(line:157):" + 1);
//                this.trigger(manager, instance, EventType.EDIT_EVENT_TYPE,
//                             Event.BRANCH_CONTINUE_TYPE);
//            }
            if (Integer.parseInt(strevent) == EventType.INSERT_SMALL_QUERY_EVENT_TYPE) {
//                System.out.println("BMLoanRespChMng.buttonEvent(line:162):" + 2);
                this.trigger(manager, instance, EventType.INSERT_EVENT_TYPE,
                             Event.BRANCH_CONTINUE_TYPE);
            }
        }
        return 0;
    }

//    public int preEdit(SessionContext ctx, DatabaseConnection conn, FormInstance instance,
//                       ErrorMessages msgs,
//                       EventManager manager, SqlAssistor assistor)
//    {
//        //System.out.println("BMLoanRespChMng.preEdit(line:175):" + 1);
//        String strbmno = ctx.getParameter("BMNO");
//        String strnewresp = ctx.getParameter("NEWRESP");
//        String strpct = ctx.getParameter("NEWPCT");
//        MyDB.getInstance().addDBConn(conn);
//        LoanAdm ladm = new LoanAdm();
//        int a = -1;
//        if ( (a = ladm.loanRespChange(strbmno, strnewresp, BigDecimal.valueOf(Long.parseLong(strpct)))) <
//            0) {
//            System.out.println("a(error):" + a);
//            msgs.add("错误:" + a);
//            return a;
//        }
//        else {
//            System.out.println("a(success):" + a);
//        }
//
//        MyDB.getInstance().releaseDBConn();
//        return 0;
//    }

    public int preInsert(SessionContext ctx, DatabaseConnection conn, FormInstance instance,
                         ErrorMessages msgs,
                         EventManager manager, SqlAssistor assistor)
    {
        int iseqno = BMLoanRespChange.getNextNo();
        String strSeqno = String.valueOf(iseqno);

        assistor.setSqlFieldValue(assistor.getDefaultTbl(), "SEQNO", strSeqno);

        //System.out.println("BMLoanRespChMng.preInsert(line:195):" + 1);
        String strbmno = ctx.getParameter("BMNO");
        String strnewresp = ctx.getParameter("NEWRESP");
        String strpct = ctx.getParameter("NEWPCT");
        MyDB.getInstance().addDBConn(conn);
        LoanAdm ladm = new LoanAdm();
        int a = -1;
        if ( (a = ladm.loanRespChange(strbmno, strnewresp,
                                      BigDecimal.valueOf(Long.parseLong(strpct)))) < 0) {
            System.out.println("a(error):" + a);
            msgs.add("错误:" + a);
            return a;
        }
        else {
            System.out.println("a(success):" + a);
        }

        MyDB.getInstance().releaseDBConn();
        return 0;

    }
//    public int beforeEdit(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
//            EventManager manager, SqlAssistor assistor) {
//        if (!this.isAccept && strFlag.equals(strFlag)) {
//            SystemDate sd = new SystemDate();
//            String strcurrdate = sd.getSystemDate5("");
//            instance.setValue("CREATEDATE", strcurrdate);
//            instance.setFieldReadonly("CREATEDATE", true);
//
//            instance.setValue("BMNO", strBmNo);
//            LoanLedgerMan llm = new LoanLedgerMan();
//            LoanLedger ll = null;
//            if (strBmNo != null) {
//                ll = llm.getLoanLedger(strBmNo);
//            }
//            if (ll == null) {
//                msgs.clear();
//                msgs.add("没有取到公共模块");
//                return -1;
//            }
//            else {
//                String strfirstResp = ll.firstResp;
//                String strfirstRespPct = ll.firstRespPct.toString();
//                System.out.println("BMLoanRespChMng.beforeInsert(line:126):"+strfirstResp);
//                System.out.println("BMLoanRespChMng.beforeInsert(line:127):"+strfirstRespPct);
//                instance.setValue("OLDRESP", strfirstResp);
//                instance.setValue("OLDPCT", strfirstRespPct);
//            }
//
//            String strUserName = Function.getUserName(ctx);
//            System.out.println("username:"+strUserName);
//            if (strUserName != null) {
//                instance.setValue("OPERATOR", strUserName);
//            }
//            else {
//                return -1;
//            }
//
//        }
//        return 0;
//
//    }

}
