package zt.cms.xf.contract;

import zt.platform.db.DatabaseConnection;
import zt.platform.db.RecordSet;
import zt.platform.form.util.FormInstance;
import zt.platform.form.util.event.ErrorMessages;
import zt.platform.form.util.event.EventManager;
import zt.platform.form.util.SqlAssistor;
import zt.platform.form.util.SqlField;

import java.sql.PreparedStatement;
import java.util.Vector;

import zt.platform.form.control.SessionContext;
import zt.platform.form.control.FormActions;
import zt.platform.form.util.*;
import zt.platform.form.util.event.*;
import zt.platform.form.util.datatype.*;
import zt.platform.form.config.ElementBean;
import zt.platform.utils.Debug;
import zt.cms.pub.code.SerialNumber;
import zt.cmsi.pub.Param;
import zt.cmsi.pub.ParamName;
import zt.cmsi.biz.CriCheckResult;
import zt.cmsi.mydb.MyDB;

/**
 * Created by IntelliJ IDEA.
 * User: zhanrui
 * Date: 2009-3-14
 * Time: 20:24:05
 * To change this template use File | Settings | File Templates.
 */
public class XFContractLink extends FormActions {


    public int load(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs, EventManager manager, String parameter) {
        /*
        String strmenuid = ctx.getParameter("MENUID");
        if (instance.getFormid().equals("PTMMNG") && strmenuid != null) {
            instance.setValue("MENUID", strmenuid.trim());
            trigger(manager, instance, EventType.EDIT_VIEW_EVENT_TYPE, Event.BRANCH_CONTINUE_TYPE);
        }
        */
        //instance.setHTMLFieldValue("APPNO","123");
        //ElementBean eb = instance.getFormBean().getElement("APPNO");
        //eb.setDefaultValue(ctx.getParameter("APPNO"));

        String  appno = ctx.getParameter("APPNO");
        instance.setValue("APPNO", appno);

        String sql = "select clientname from xfvappinfo where appno='" + appno + "'";
        RecordSet rs = conn.executeQuery(sql);
        if (rs.next()) {
            instance.setValue("CLIENTNAME", rs.getString(0));
        }
        rs.close();

//        instance.setHTMLFieldDisabled("APPNO", true);
        //eb.setCaption("nono");
        trigger(manager, instance, EventType.EDIT_VIEW_EVENT_TYPE, Event.BRANCH_CONTINUE_TYPE);
        return 0;
    }

    public int buttonEvent(SessionContext ctx, DatabaseConnection conn, FormInstance instance,
                           String button, ErrorMessages msgs, EventManager manager) {

        /*
         params = new Param();
         params.addParam(ParamName.Flag, strFlag);
         params.addParam(ParamName.BMNo, strBmNo);
         params.addParam(ParamName.BMTransNo, strBmTransNo);

         ctx.setRequestAtrribute(ParamName.ParamName, params);
         int iseqno = (int) SerialNumber.getNextSN("CMBLACKLIST", "SEQNO");
             String strSeqno = String.valueOf(iseqno);
             assistor.setSqlFieldValue(assistor.getDefaultTbl(), "SEQNO", strSeqno);

        */

        if (button.equals("NEXT")) {
            //instance.setValue("MENUID", strmenuid.trim());
            String appno = ctx.getParameter("APPNO");
            //appno = instance.getFormBean().getElement("APPNO").
            //appno = (String)ctx.getRequestAttribute("APPNO");
            //Param param=null;

            trigger(manager, "XFCONTRACTPAGE", null);

            if (checkAppInfo(ctx, conn, msgs) == -1) {
                msgs.add("请重新输入申请单编号！");

                String errormsg = msgs.toString();   //TODO: 错误信息处理
                ctx.setRequestAtrribute("msg", errormsg);
                ctx.setTarget("/showinfo.jsp");
                instance.setReadonly(true);

                return (-1);
            }

             if (checkContractInfo(ctx, conn, msgs) == -1) {
                  msgs.add("　请重新选择申请单！");
                  return (-1);
             }

            ctx.setAttribute("SUPERFORMID", "XFCONTRACTLINK");

            return 0;
        } else {

            System.out.println("button:" + button);
            /*
                if ( button != null && button.equals(SessionAttributes.REQUEST_BUTTON_ADD_NAME) )  {
                    trigger(manager,"XFCONTRACTPAGE",null);
                }
                if ( button != null && button.equals(SessionAttributes.REQUEST_EDIT_BUTTON_VALUE) ) {
                    trigger(manager,"XFCONTRACTPAGE",null);
                }
            */
            return 0;

        }
    }

    private int checkAppInfo(SessionContext ctx, DatabaseConnection conn, ErrorMessages msgs) {

        try {
            MyDB.getInstance().addDBConn(conn);

            String appno = ctx.getParameter("APPNO");

            String appstatus = null;

            //TODO: zt.cmsi.pub.cenum.EnumValue
            String sql = "select appstatus from xfapp where appno='" + appno + "'";
            RecordSet rs = conn.executeQuery(sql);
            int count = 0;
            if (rs.next()) {
                appstatus = rs.getString(0);
                count = 1;
            }
            rs.close();
            if (count == 0) {
                msgs.add("此申请单不存在!");
                msgs.add("申请单编号:  " + appno);
                return -1;
            } else {
                //确认是否为审批完成状态。
                if (Integer.parseInt(appstatus) != 11) {//TODO:  zt.cmsi.pub.cenum.EnumValue
                    msgs.add("此申请单未经审批确认！");
                    msgs.add("　审批状态 = " + appstatus);
                    return -1;
                }
            }

        } catch (Exception e) {
            msgs.add("发生异常：" + e.getMessage());
            Debug.debug(e);
            return -1;
        } finally {
            MyDB.getInstance().releaseDBConn();
        }
        return 0;
    }

    private int checkContractInfo(SessionContext ctx, DatabaseConnection conn, ErrorMessages msgs) {

        try {
            MyDB.getInstance().addDBConn(conn);

            String contractno = ctx.getParameter("CONTRACTNO");

            String sql = "select count(*) from xfcontract where contractno='" + contractno + "'";
            RecordSet rs = conn.executeQuery(sql);

            int count = 0;
            if (rs.next()) {
                count = rs.getInt(0);
            }
            rs.close();
            if (count > 0) {
                msgs.add("此合同号：" + contractno + "已存在!");
                return -1;
            }
        } catch (Exception e) {
            msgs.add("发生异常：" + e.getMessage());
            Debug.debug(e);
            return -1;
        } finally {
            MyDB.getInstance().releaseDBConn();
        }
        return 0;
    }




}
