/*
 *  Copyright (c) 2004 Email: liwei@zhongtian.biz
 *  $Id: BMErrLedgerMng.java,v 1.1 2005/06/28 07:00:26 jgo Exp $
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

/**
 *  ���ԣ���������������,�޸ģ�ɾ��(���ֵ)
 *
 *@author     Administrator
 *@created    2003��12��2��
 */
public class BMErrLedgerMng
    extends FormActions {

//  private String clientno;

    private String strFlag = null;
    private boolean isAccept = false;
    private String strbmno = null;

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

        /*if ( strFlag == null|| strFlag.equals("read")) {
            instance.setReadonly(true);
            System.out.println("realonly:true");
                 } else {
            instance.setReadonly(false);
            System.out.println("realonly:false");
                 }*/
        // System.out.println("7345"+String.valueOf(ctx.getRequestAttribute("flag")));
        strbmno = ctx.getParameter("BMNO") == null ? null : ctx.getParameter("BMNO").trim();
        //System.out.println("strbmno:"+strbmno);
        if (strbmno != null &&
            Function.isDataExist(conn, "BTERRLEDGER", "BMNO", strbmno, Function.STRING_TYPE)) {
            instance.setValue("BMNO", strbmno);
            trigger(manager, instance, EventType.EDIT_VIEW_EVENT_TYPE, Event.BRANCH_CONTINUE_TYPE);
        }
        else {
            msgs.clear();
            msgs.add("�Բ���û�в�ѯ���κμ�¼��");
            return -1;
        }

        return 0;
    }

    public int buttonEvent(SessionContext ctx, DatabaseConnection conn, FormInstance instance,
                           String button,
                           ErrorMessages msgs, EventManager manager)
    {
        if (button == null || button.equals("")) {
            msgs.clear();
            msgs.add("����ķ��񲻴��ڣ�");
            return -1;
        }

        if (button.equals("SEARCHLOANTABLE")) {
            Param pm = new Param();
            pm.addParam(ParamName.BMNo, strbmno);
            pm.addParam(ParamName.Flag, strFlag);
            ctx.setRequestAtrribute(ParamName.ParamName, pm);
            this.trigger(manager, "BMRQL0", null);

        }
        else if (button.equals("SELFSAVE")) {

//            MyDB mydb = MyDB.getInstance();
//            DatabaseConnection conn1 = mydb.apGetConn();
//            ctx.setAttribute("CONN",conn1);
            String sbmno = ctx.getParameter("SBMNO").trim();
            trigger(manager, instance, EventType.EDIT_EVENT_TYPE, Event.BRANCH_CONTINUE_TYPE);
        }
        return 0;
    }

//    public int preReference(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
//            EventManager manager, String reffldnm,SqlWhereUtil sqlWhereUtil) {
//        System.out.println("reffldnm:"+reffldnm);
//        return 0;
//    }

    public int beforeEdit(SessionContext ctx, DatabaseConnection conn, FormInstance instance,
                          ErrorMessages msgs,
                          EventManager manager, SqlAssistor assistor)
    {
        instance.setFieldDisabled("BMNO", false);
        return 0;
    }

    public int postEditOk(SessionContext ctx, DatabaseConnection conn, FormInstance instance,
                          ErrorMessages msgs,
                          EventManager manager)
    {
        MyDB.getInstance().addDBConn(conn);
        LoanGranted lg = new LoanGranted();
        String strUserName = Function.getUserName(ctx);
        int a = -1;
        if ( (a = lg.reIssueGrant(strbmno, strUserName)) < 0) {
            MyDB.getInstance().releaseDBConn();
            return a;
        }
        ctx.setRequestAtrribute("title", Function.getFormTitleByInstance(instance));
        ctx.setRequestAtrribute("msg", "����Ȩ�����ѳɹ���");
        ctx.setRequestAtrribute("flag", "1");
        ctx.setRequestAtrribute("isback", "0");
        ctx.setTarget("/showinfo.jsp");

        MyDB.getInstance().releaseDBConn();

        return 0;
    }

}
