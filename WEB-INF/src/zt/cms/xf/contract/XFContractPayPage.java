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
import java.util.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.text.ParsePosition;
import java.math.BigDecimal;

import zt.platform.form.control.SessionContext;
import zt.platform.form.control.FormActions;
import zt.platform.form.util.*;
import zt.platform.form.util.event.*;
import zt.platform.form.util.datatype.*;
import zt.platform.form.config.ElementBean;
import zt.platform.utils.Debug;
import zt.cms.pub.code.SerialNumber;
import zt.cms.xf.account.Airth;
import zt.cmsi.pub.Param;
import zt.cmsi.pub.ParamName;
import zt.cmsi.biz.CriCheckResult;
import zt.cmsi.mydb.MyDB;

/**
 * 根据合同金额。期数，签订日期，手续费率 生成分歧计划
 * User: zhanrui
 * Date: 2009-3-14
 * Time: 20:24:05
 * To change this template use File | Settings | File Templates.
 */
public class XFContractPayPage extends FormActions {

    private String contractno = null;
    private double contractamt;
    private double totalamt;
    private double interest;       //总利息
    private int duration;
    private double servicecharge;
    private String startdate = null;

    private String paybackact = null;
    private String paybackactname = null;
    private String paybackbankid = null;

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
        //eb.setDefaultValue("0001");
        //instance.setValue("APPNO", "0001");
        //eb.setCaption("nono");
        //trigger(manager, instance, EventType.EDIT_VIEW_EVENT_TYPE, Event.BRANCH_CONTINUE_TYPE);
        contractno = ctx.getParameter("CONTRACTNO");
        //contractno = (String)ctx.getAttribute("CONTARCTNO");
        if (contractno == null) {
            return -1;
        }

        if (getContractInfo(ctx, conn, msgs) == -1) {
            return -1;
        }

        //instance.setValue("CONTRACTNO",contractno);
        instance.getFormBean().getElement("CONTRACTNO").setDefaultValue(contractno);
        instance.getFormBean().getElement("CONTRACTAMT").setDefaultValue(Double.toString(contractamt));
        instance.getFormBean().getElement("TOTALAMT").setDefaultValue(Double.toString(totalamt));
        instance.getFormBean().getElement("SERVICECHARGE").setDefaultValue(Double.toString(servicecharge));
        instance.getFormBean().getElement("DURATION").setDefaultValue(Integer.toString(duration));
        instance.getFormBean().getElement("STARTDATE").setDefaultValue(startdate);
//        instance.setValue("CONTRACTAMT",contractamt);
//        instance.setValue("DURATION",duration);
//        instance.setValue("SERVICEHARGE",servicecharge);

//        instance.setValue("STARTDATE",startdate);

        return 0;
    }

    public int buttonEvent(SessionContext ctx, DatabaseConnection conn, FormInstance instance,
                           String button, ErrorMessages msgs, EventManager manager) {

        if (button.equals("NEXT")) {
            String appno = ctx.getParameter("APPNO");
            if (generateNewPaySchedule(ctx, conn, instance, msgs) == -1) {
                return -1;
            }
            instance.setReadonly(true);
            ctx.setRequestAtrribute("msg", "分期还款计划生成成功，请查询！");
            ctx.setRequestAtrribute("flag", "1");
            ctx.setRequestAtrribute("isback", "0");

            ctx.setTarget("/showinfo.jsp");
        }
        return 0;

    }

    private int getContractInfo(SessionContext ctx, DatabaseConnection conn, ErrorMessages msgs) {
        String sql = "select * from xfcontract where contractno='" + contractno + "'";
        MyDB.getInstance().addDBConn(conn);
        try {
            RecordSet rs = conn.executeQuery(sql);
            int count = 0;
            if (rs.next()) {
                duration = rs.getInt("DURATION");
                contractamt = rs.getDouble("CONTRACTAMT");
                servicecharge = rs.getDouble("SERVICECHARGE");
                Calendar cal = rs.getCalendar("STARTDATE");
                cal.add(Calendar.MONTH, 1);
                cal.set(Calendar.DAY_OF_MONTH, 13);

                DateFormat format = new SimpleDateFormat("yyyyMMdd");
                startdate = format.format(cal.getTime());

//                int ca = (int)contractamt*100;
//                int sc = (int)servicecharge*10000;
//                totalamt =   contractamt + (double)(ca*sc*duration)/1000000;
//                String str = Double.toString(totalamt);
//                BigDecimal bd = new  BigDecimal(str).setScale(2,BigDecimal.ROUND_HALF_UP);
//                totalamt = bd.doubleValue();

                //TODO: 计算精度问题
                interest = Airth.mul(Airth.mul(contractamt, duration), servicecharge);
                interest = Airth.round(interest, 2);    //总利息

                totalamt = Airth.add(contractamt, interest);
                totalamt = Airth.round(totalamt, 2);

                //Calendar cal  = rs.getCalendar("STARTDATE");
                //SimpleDateFormat sdf = new
                paybackact = rs.getString("PAYBACKACT");
                paybackactname = rs.getString("PAYBACKACTNAME");
                paybackbankid = rs.getString("PAYBACKBANKID");
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


    /*
    功能：生成还款计划
    本方法用于手工调整还款计划，点击“生成还款计划”时调用
    与XFcontracgPage中的方法基本类似，计算方法未抽共通
    本方法中出帐日期根据用户输入计算，未设置为统一的出帐日期，如每月13日
     */

    private int generateNewPaySchedule(SessionContext ctx, DatabaseConnection conn, FormInstance instance,
                                       ErrorMessages msgs) {

        try {
            MyDB.getInstance().addDBConn(conn);
            String contractno = ctx.getParameter("CONTRACTNO");

            String sql = "select count(*)  from xfcontractpays where contractno='" + contractno + "'";
            RecordSet rs = conn.executeQuery(sql);
            int count = 0;
            if (rs.next()) {
                count = rs.getInt(0);
            }
            if (count > 0) {
                String sqlDelete = "delete from xfcontractpays where contractno='" + contractno.trim() + "'";
                conn.executeUpdate(sqlDelete);
                //conn.commit();
            }


//            String sSql = "insert into xfcontractpays " +
//                    "(contractno,cpaamt, poano, startdate,PAYBACKACT,PAYBACKACTNAME,PAYBACKBANKID) " +
//                    "values " + "('" + contractno + "',?,?,?,?,?,?)";

            String sSql = "insert into xfcontractpays " +
                    "(contractno,cpaamt, poano, startdate,PAYBACKACT,PAYBACKACTNAME,PAYBACKBANKID,PRINCIPALAMT,SERVICECHARGEFEE) " +
                    "values " + "('" + contractno + "',?,?,?,?,?,?,?,?)";

            PreparedStatement ps = conn.getPreparedStatement(sSql);

            double cpaamt = Airth.div(totalamt, duration, 2);   //每月还款总金额

            double cpainterest = Airth.mul(contractamt,servicecharge);  //每月手续费
            cpainterest = Airth.round(cpainterest, 2);  //每月手续费

            double cpaprincipal = cpaamt - cpainterest;   //每月本金
            

//            double cpainterest = Airth.div(interest, duration, 2);

            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sFmt = new SimpleDateFormat("yyyyMMdd");

            //获得当前用户输入的日期
            startdate = ctx.getParameter("STARTDATE");

            cal.setTime(sFmt.parse(startdate));
            cal.add(Calendar.MONTH, -1);

            for (int i = 1; i <= duration; i++) {
                if (i == duration) {
                    cpaamt = cpaamt * 100;
                    ps.setDouble(1, totalamt - (cpaamt * (duration - 1)) / 100);

                    cpaprincipal = cpaprincipal * 100;
                    ps.setDouble(7, contractamt - (cpaprincipal * (duration - 1)) / 100);

                    cpainterest = cpainterest * 100;
                    ps.setDouble(8, interest - (cpainterest * (duration - 1)) / 100);

                } else {
                    ps.setDouble(1, cpaamt);
                    ps.setDouble(7, cpaprincipal);
                    ps.setDouble(8, cpainterest);

                }
                ps.setInt(2, i);

                cal.add(Calendar.MONTH, 1);
                java.sql.Date sqlDate = new java.sql.Date(cal.getTime().getTime());
                ps.setDate(3, sqlDate);

                ps.setString(4, paybackact);
                ps.setString(5, paybackactname);
                ps.setString(6, paybackbankid);

                ps.executeUpdate();
            }
            rs.close();

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