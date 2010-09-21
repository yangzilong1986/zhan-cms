package zt.cms.scuser;

/**
 *  Title: </p> <p>
 *
 *  Description: </p> <p>
 *
 *  Copyright: Copyright (c) 2003</p> <p>
 *
 *  Company: </p>
 *
 *@author not attributable
 *@created 2003年11月17日
 *@version 1.0
 */

import com.ebis.encrypt.*;
import zt.cms.cm.common.*;
import zt.cms.pub.*;
import zt.cms.pub.code.*;
import zt.platform.db.*;
import zt.platform.form.control.*;
import zt.platform.form.util.*;
import zt.platform.form.util.event.*;

public class PageAction extends FormActions {

    public PageAction() {
    }

    public int load(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                    EventManager manager, String parameter) {

        RightChecker.checkReadonly(ctx, conn, instance);

        String temp = ctx.getParameter("USERNO");
        if (temp != null && temp.trim().length() != 0) {
            int userNo = Integer.parseInt(temp.trim());
            instance.setValue("USERNO", userNo);
            trigger(manager, instance, EventType.EDIT_VIEW_EVENT_TYPE, Event.BRANCH_CONTINUE_TYPE);
        } else {
            trigger(manager, instance, EventType.INSERT_VIEW_EVENT_TYPE, Event.BRANCH_CONTINUE_TYPE);
        }
        return 0;
    }

    public int beforeInsert(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                            EventManager manager) {
        instance.setFieldReadonly("SEQNO", true);
        instance.setFieldReadonly("VIEWROLE", true);
        instance.setFieldReadonly("RESETPAS", true);
        return 0;
    }

    public int beforeEdit(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                          EventManager manager, SqlAssistor assistor) {
        if (ctx.getRequestAttribute("isChange") != null) {
            instance.setFieldReadonly("LOGINNAME", false);
            instance.setFieldReadonly("USERNAME", false);
        } else {
            instance.setFieldReadonly("LOGINNAME", true);
        }
        return 0;
    }

    public int preInsert(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                         EventManager manager, SqlAssistor assistor) {
        String loginName = ctx.getParameter("LOGINNAME");
        RecordSet rs = conn.executeQuery("select * from SCUSER where loginname='" + loginName + "'");
        if (rs.next()) {
            msgs.add("登陆名已存在，请选择其他登陆名");
            ctx.setRequestAtrribute("isChange", "true");
            return -1;
        } else {

            int seqno = (int) SerialNumber.getNextSN("SCUSER", "USERNO");
            instance.setValue("USERNO", seqno);
            assistor.setSqlFieldValue(assistor.getDefaultTbl(), "USERPWD",
                    getEncryptPassword("111111"));
            assistor.setSqlFieldValue(assistor.getDefaultTbl(), "USERNO",
                    seqno + "");
            return 0;
        }
    }

    public int buttonEvent(SessionContext ctx, DatabaseConnection conn, FormInstance instance, String button,
                           ErrorMessages msgs, EventManager manager) {
        RightChecker.transReadOnly(ctx, conn, instance);
        if (button.equals("VIEWROLE")) {
            ctx.setTarget("/userrole/userrole.jsp");
        } else if (button.equals("RESETPAS")) {
            conn.executeUpdate("update ptuser set passwd='" + getEncryptPassword("111111") + "' where userid =" + ctx.getParameter("USERNO") + "");
            trigger(manager, "SCUSEP", null);
        } else if (button.equals("UNLOCKUSER")) {
            conn.executeUpdate("update ptuser set errorlogin=0 " + " where userid =" + ctx.getParameter("USERNO") + "");
            trigger(manager, "SCUSEP", null);

        }
        return 0;
    }

    public int postInsertOk(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                            EventManager manager) {
        SCUser.setDirty(true);
        return 0;
    }

    public int postEditOk(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                          EventManager manager) {
        SCUser.setDirty(true);
        return 0;
    }

    public int postDeleteOk(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                            EventManager manager) {
        SCUser.setDirty(true);
        return 0;
    }

    public void postField(SessionContext ctx, FormInstance instance, String fieldname,
                          ErrorMessages msgs, EventManager manager) {

//     System.out.println("=========================yes called================="+fieldname);
//     String v = (String)ctx.getRequestAttribute("USERNAME");
//     instance.setHTMLFieldValue("USERJOB","测试"+v);
//     instance.setHTMLFieldReadOnly("USERSTATUS",true);
//     instance.setHTMLFieldDisabled("USERTYPE",true);
//     instance.setHTMLShowMessage("致意","你好!这里是JGO的ACTIVEX MESSAGE!");
//     //instance.setHTMLFieldReadOnly("LOGINNAME",true);
//     instance.setHTMLFieldHidden("LOGINNAME",true);
        return;
    }

    public static String getEncryptPassword(String rawPassword) {
        EncryptData ed = new EncryptData();
        return new String(ed.enPasswd(rawPassword.getBytes()));
    }

}
