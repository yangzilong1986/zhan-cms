package zt.cms.xf.contract;

import zt.platform.db.DatabaseConnection;
import zt.platform.db.RecordSet;
import zt.platform.form.util.FormInstance;
import zt.platform.form.util.event.ErrorMessages;
import zt.platform.form.util.event.EventManager;
import zt.platform.form.util.SqlAssistor;
import zt.platform.form.util.SqlField;

import java.sql.PreparedStatement;
import java.util.Date;
import java.util.Map;
import java.util.Calendar;
import java.util.logging.Logger;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.text.DateFormat;

import zt.platform.form.control.SessionContext;
import zt.platform.form.control.FormActions;
import zt.platform.form.util.*;
import zt.platform.form.util.event.*;
import zt.platform.form.util.datatype.*;
import zt.platform.form.config.FormBean;
import zt.platform.form.config.SystemAttributeNames;
import zt.platform.utils.Debug;
import zt.platform.user.UserManager;
import zt.cms.pub.code.SerialNumber;
import zt.cms.xf.account.Airth;
import zt.cms.xf.common.dto.*;
import zt.cms.xf.common.dao.XfcontractpaysDao;
import zt.cms.xf.common.dao.XfactcutpaymainDao;
import zt.cms.xf.common.dao.XfcontractDao;
import zt.cms.xf.common.factory.XfcontractpaysDaoFactory;
import zt.cms.xf.common.factory.XfactcutpaymainDaoFactory;
import zt.cms.xf.common.factory.XfcontractDaoFactory;
import zt.cms.xf.common.constant.XFContractStatus;
import zt.cms.xf.common.constant.XFBankCode;
import zt.cms.xf.common.constant.XFCommon;
import zt.cmsi.mydb.MyDB;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;

/**
 * Created by IntelliJ IDEA.
 * User: zhanrui
 * Date: 2009-3-14
 * Time: 20:24:05
 * To change this template use File | Settings | File Templates.
 */
public class XFContractPage extends FormActions {

    public static Logger logger = Logger.getLogger("zt.cms.xf.contract");
    private boolean isNewPaySchedule = false;
    private String superformid = null;
    private boolean isFHStatus = false;        //复核状态
    private boolean isCXStatus = false;        //查询状态
    private boolean isJBStatus = false;        //经办录入状态（新增合同或修改驳回合同）

    public int load(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                    EventManager manager, String parameter) {

        boolean isNewRecord = false;
        superformid = (String) ctx.getAttribute("SUPERFORMID");

        instance.setReadonly(true);
        instance.getFormBean().getElement("ACCOUNTOPER").setComponetTp(6); //设置放款按钮为隐藏
        instance.getFormBean().getElement("CREDITISSUELISTFH").setComponetTp(6);  //设置放款列表查看按钮为隐藏
        instance.getFormBean().getElement("CREDITISSUEFH").setComponetTp(6);  //设置放款复核按钮为隐藏
        instance.getFormBean().getElement("CREDITISSUEFHDISMISSAL").setComponetTp(6);  //设置放款复核按钮为隐藏
        instance.getFormBean().getElement("CONFIRMBUTTON").setComponetTp(6); //设置复核按钮为隐藏
        instance.getFormBean().getElement("PAYSCHEDULE").setComponetTp(6); //设置放款计划按钮为隐藏
        instance.getFormBean().getElement("DISMISSALBUTTON").setComponetTp(6); //设置放款计划按钮为隐藏

//        //设置分期付款人在财务公司开立的内部帐号栏位可显示
//        instance.getFormBean().getElement("CLIENTACT").setComponetTp(4);

        if (superformid != null) {
            if (superformid.equals("XFCONTRACTLINK")) {
                ctx.setAttribute("SUPERFORMID", "XFCONTRACTPAGE");
                isNewRecord = true;
                instance.setReadonly(false);
                if (getAppInfo(ctx, conn, instance, msgs) == -1) {
                    return -1;
                }
                isNewPaySchedule = true;

            } else if (superformid.equals("XFCREDITISSUELIST")) {     //贷款发放经办处理
                instance.getFormBean().getElement("ACCOUNTOPER").setComponetTp(15);
            } else if (superformid.equals("XFCREDITISSUELISTWH")) {     //贷款发放维护处理
                instance.getFormBean().getElement("ACCOUNTOPER").setComponetTp(15);
            } else if (superformid.equals("XFCREDITISSUELISTFH")) {     //贷款发放复核处理
                instance.getFormBean().getElement("CREDITISSUELISTFH").setComponetTp(15);
                instance.getFormBean().getElement("CREDITISSUEFH").setComponetTp(15);
                instance.getFormBean().getElement("CREDITISSUEFHDISMISSAL").setComponetTp(15);
            } else if (superformid.equals("XFCONTRACTLISTFH")) {
                isFHStatus = true;
//                UserManager um = (UserManager) ctx.getAttribute(SystemAttributeNames.USER_INFO_NAME);
//                instance.getFormBean().getElement("CHECKERID").setDefaultValue(um.getUserName());
//                instance.setValue("CHECKERID",um.getUserName());
//                instance.getFormBean().getElement("UPDATORID").setDefaultValue(um.getUserName());

                instance.getFormBean().getElement("CONFIRMBUTTON").setComponetTp(15); //设置复核按钮为系统按钮
                instance.getFormBean().getElement("DISMISSALBUTTON").setComponetTp(15); //设置驳回按钮为系统按钮

            } else if (superformid.equals("XFCONTRACTLISTCX")
                    || superformid.equals("XFCONTRACTLISTDY")
                    || superformid.equals("XFAPPQUERYLIST")) {
                isCXStatus = true;
                //设置按钮为隐藏
                instance.getFormBean().getElement("CONFIRMBUTTON").setComponetTp(6);
                instance.getFormBean().getElement("DISMISSALBUTTON").setComponetTp(6);
                instance.getFormBean().getElement("PAYSCHEDULE").setComponetTp(6);

//                //设置分期付款人在财务公司开立的内部帐号栏位可显示
//                instance.getFormBean().getElement("CLIENTACT").setComponetTp(4);

            } else if (superformid.equals("XFCONTRACTLIST")) {
                isJBStatus = true;
                instance.setReadonly(false);
            }
        }

        //ctx.setAttribute("SUPERFORMID", null);


        if (isNewRecord) {
            trigger(manager, instance, EventType.INSERT_VIEW_EVENT_TYPE, Event.BRANCH_CONTINUE_TYPE);
        } else {
            String contractno = ctx.getParameter("CONTRACTNO");
            if (contractno == null) {
                contractno = (String) ctx.getAttribute("CONTRACTNO");
            }

            if (contractno != null && contractno.trim().length() != 0) {
                instance.setValue("CONTRACTNO", contractno);
                trigger(manager, instance, EventType.EDIT_VIEW_EVENT_TYPE, Event.BRANCH_CONTINUE_TYPE);
            } else {
                trigger(manager, instance, EventType.INSERT_VIEW_EVENT_TYPE, Event.BRANCH_CONTINUE_TYPE);
            }
        }
        return 0;

    }

    public int beforeEdit(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                          EventManager manager, SqlAssistor assistor) {

        if (isFHStatus) {
            UserManager um = (UserManager) ctx.getAttribute(SystemAttributeNames.USER_INFO_NAME);
            //instance.getFormBean().getElement("CHECKERID").setDefaultValue(um.getUserName());
            instance.setValue("CHECKERID", um.getUserName());
            instance.setValue("UPDATORID", um.getUserName());
            //instance.getFormBean().getElement("UPDATORID").setDefaultValue(um.getUserName());
            //TODO 利用DB类型隐藏字段进行更新...
        }

        return 0;
    }

    public int postInsertOk(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                            EventManager manager) {


        ctx.setRequestAtrribute("msg", "合同信息增加成功！");
        ctx.setRequestAtrribute("flag", "1");
        ctx.setRequestAtrribute("isback", "0");

        ctx.setTarget("/showinfo.jsp");
        instance.setReadonly(true);

        if (generateNewPaySchedule(ctx, conn, instance, msgs) < 0) {
            //TODO:
        }

        return 0;
    }

    public int preInsert(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                         EventManager manager, SqlAssistor assistor) {
        instance.setValue("OPERATEDATE", "#SYSDATE#");
        instance.setValue("CREATEDATE", "#SYSDATE#");
        assistor.setSqlFieldValue(assistor.getDefaultTbl(), "OPERATEDATE", "#SYSDATE#");
        assistor.setSqlFieldValue(assistor.getDefaultTbl(), "CREATEDATE", "#SYSDATE#");

//        String str = instance.getFormBean().getElement("CONTRACTAMT").getDefaultValue();
        String str = instance.getStringValue("CONTRACTAMT");

        instance.setValue("PRINCIPALAMT", str);
        assistor.setSqlFieldValue(assistor.getDefaultTbl(), "PRINCIPALAMT", str);

        return 0;
    }

    public int preUpdate(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                         EventManager manager, SqlAssistor assistor) {

        //? no use?
        instance.setValue("CHECKDATE", "#SYSDATE#");
        instance.setValue("UPDATEDATE", "#SYSDATE#");
        assistor.setSqlFieldValue(assistor.getDefaultTbl(), "CHECKDATE", "#SYSDATE#");
        assistor.setSqlFieldValue(assistor.getDefaultTbl(), "UPDATEDATE", "#SYSDATE#");

        if (isJBStatus) {
            instance.setValue("CSTATUS", "1");
            assistor.setSqlFieldValue(assistor.getDefaultTbl(), "CSTATUS", "1");
        }
//        //设置审核员ID
//        UserManager um = (UserManager) ctx.getAttribute(SystemAttributeNames.USER_INFO_NAME);
//        instance.getFormBean().getElement("CHECKERID").setDefaultValue(um.getUserName());
//        instance.getFormBean().getElement("UPDATORID").setDefaultValue(um.getUserName());
//        instance.getFormBean().getElement("UPDATEFORM").setDefaultValue("XFCONTRACTPAGE");

        return 0;
    }

    public int preEdit(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                       EventManager manager, SqlAssistor assistor) {
        instance.setValue("CHECKDATE", "#SYSDATE#");
        instance.setValue("UPDATEDATE", "#SYSDATE#");
        assistor.setSqlFieldValue(assistor.getDefaultTbl(), "CHECKDATE", "#SYSDATE#");
        assistor.setSqlFieldValue(assistor.getDefaultTbl(), "UPDATEDATE", "#SYSDATE#");

        if (isJBStatus) {
            instance.setValue("CSTATUS", "1");
            assistor.setSqlFieldValue(assistor.getDefaultTbl(), "CSTATUS", "1");
            UserManager um = (UserManager) ctx.getAttribute(SystemAttributeNames.USER_INFO_NAME);
            assistor.setSqlFieldValue(assistor.getDefaultTbl(), "OPERATORID", um.getUserName());
            assistor.setSqlFieldValue(assistor.getDefaultTbl(), "OPERATORDATE", "#SYSDATE#");
        }

        return 0;
    }

    public int postEditOk(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                          EventManager manager) {
        ctx.setRequestAtrribute("msg", "合同信息修改成功！");
        ctx.setRequestAtrribute("flag", "1");
        ctx.setRequestAtrribute("isback", "0");
        ctx.setTarget("/showinfo.jsp");
        instance.setReadonly(true);

        return 0;
    }


    public int buttonEvent(SessionContext ctx, DatabaseConnection conn, FormInstance instance, String button,
                           ErrorMessages msgs, EventManager manager) {

        if (button == null) {
            return -1;
        }
        if (button.equals("PAYSCHEDULE")) {
//                trigger(manager, "XFCONTRACTPAYLIST", null);
        } else if (button.equals("CONFIRMBUTTON")) {
            if (isFHStatus) {
                if (doConfirm(ctx, conn, instance, msgs) == 0) {
                    ctx.setRequestAtrribute("msg", "合同已审核通过！");
                    ctx.setRequestAtrribute("flag", "1");
                    ctx.setRequestAtrribute("isback", "0");
                    ctx.setTarget("/showinfo.jsp");
                    instance.setReadonly(true);
                } else {
                    return -1;
                }
            }
        } else if (button.equals("DISMISSALBUTTON")) {
            if (isFHStatus) {
                if (doDismissal(ctx, conn, instance, msgs) == 0) {
                    ctx.setRequestAtrribute("msg", "合同已驳回！");
                    ctx.setRequestAtrribute("flag", "1");
                    ctx.setRequestAtrribute("isback", "0");
                    ctx.setTarget("/showinfo.jsp");
                    instance.setReadonly(true);

                } else {
                    return -1;
                }
            }
        } else if (button.equals("ACCOUNTOPER")) { //贷款发放处理：输入帐号
            trigger(manager, "XFCLIENTACT", null);
        } else if (button.equals("CREDITISSUELISTFH")) {  //贷款发放列表查看处理
            trigger(manager, "XFACTPAYDETLLISTFH", null);
        } else if (button.equals("CREDITISSUEFH")) {  //贷款发放审核处理
            if (doConfirmForCreditIssue(ctx, conn, instance, msgs) == 0) {
                ctx.setRequestAtrribute("msg", "合同放款审核已通过！");
                ctx.setRequestAtrribute("flag", "1");
                ctx.setRequestAtrribute("isback", "0");
                ctx.setTarget("/showinfo.jsp");
                instance.setReadonly(true);
            } else {
                ctx.setRequestAtrribute("msg", "合同放款审核出现问题，请查询！");
                ctx.setRequestAtrribute("flag", "1");
                ctx.setRequestAtrribute("isback", "0");
                ctx.setTarget("/showinfo.jsp");
                instance.setReadonly(true);
                return -1;
            }
        } else if (button.equals("CREDITISSUEFHDISMISSAL")) {  //贷款发放审核驳回处理
            if (doDismissalForCreditIssue(ctx, conn, instance, msgs) == 0) {
                ctx.setRequestAtrribute("msg", "合同放款已驳回！");
                ctx.setRequestAtrribute("flag", "1");
                ctx.setRequestAtrribute("isback", "0");
                ctx.setTarget("/showinfo.jsp");
                instance.setReadonly(true);
            } else {
                ctx.setRequestAtrribute("msg", "合同放款驳回出现问题，请查询！");
                ctx.setRequestAtrribute("flag", "1");
                ctx.setRequestAtrribute("isback", "0");
                ctx.setTarget("/showinfo.jsp");
                instance.setReadonly(true);
                return -1;
            }
        }
        return 0;
    }


    private int getAppInfo(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs) {

        try {
            MyDB.getInstance().addDBConn(conn);

            String appno = ctx.getParameter("APPNO");
            String id = "";
            String idtype = "";
            String name = "";

            //TODO: zt.cmsi.pub.cenum.EnumValue
            //String sql = "select idtype,id, name from xfvappinfo where appno='" + appno + "'";
            String sql = "select * from xfvappinfo where appno='" + appno + "'";
            RecordSet rs = conn.executeQuery(sql);
            int count = 0;
            if (rs.next()) {

                //TODO: PRODUCT表的使用
//               String productsql = "select * from product where productid= " + appno +
//                       " and  duration = " + appno +
//                       " and clientid = " + ;

                FormBean fb = instance.getFormBean();

                fb.getElement("APPNO").setDefaultValue(appno);
                fb.getElement("CONTRACTNO").setDefaultValue(ctx.getParameter("CONTRACTNO"));
                fb.getElement("APPTYPE").setDefaultValue(rs.getString("APPTYPE"));

                fb.getElement("INTERESTTYPE").setDefaultValue("1");

                //设置签订日期为当前日期
                fb.getElement("STARTDATE").setDefaultValue(rs.getString("STARTDATE"));


                fb.getElement("CSTATUS").setDefaultValue("1");
                fb.getElement("CONTRACTTYPE").setDefaultValue("1");
                fb.getElement("CURNO").setDefaultValue("1");

                fb.getElement("DURATION").setDefaultValue(rs.getString("DURATION"));

                fb.getElement("CONTRACTAMT").setDefaultValue(String.valueOf(new Double(rs.getDouble("CONTRACTAMT"))));
                fb.getElement("RECEIVEAMT").setDefaultValue(String.valueOf(new Double(rs.getDouble("RECEIVEAMT"))));
                fb.getElement("COMMAMT").setDefaultValue(String.valueOf(new Double(rs.getDouble("COMMAMT"))));

//                Double receiveamt = new Double(rs.getDouble("RECEIVEAMT"));
//                fb.getElement("RECEIVEAMT").setDefaultValue(String.valueOf(receiveamt));
//                Double commamt = new Double(rs.getDouble("COMMAMT"));
//                fb.getElement("COMMAMT").setDefaultValue(String.valueOf(commamt));


                Double dTemp = new Double(rs.getDouble("SERVICECHARGE"));
                fb.getElement("SERVICECHARGE").setDefaultValue(String.valueOf(dTemp));
//                dTemp = new Double(rs.getDouble("LATEFEERATE"));
                dTemp = new Double(0);
                fb.getElement("LATEFEERATE").setDefaultValue(String.valueOf(dTemp));
//                dTemp = new Double(rs.getDouble("BREACHFEERATE"));
                fb.getElement("BREACHFEERATE").setDefaultValue(String.valueOf(dTemp));
//                dTemp = new Double(rs.getDouble("LOWESTLATEFEE"));
                fb.getElement("LOWESTLATEFEE").setDefaultValue(String.valueOf(dTemp));
//                dTemp = new Double(rs.getDouble("MANAGERFEERATE"));
                fb.getElement("MANAGERFEERATE").setDefaultValue(String.valueOf(dTemp));

                fb.getElement("PARTNERACT").setDefaultValue(rs.getString("PARTNERACT"));
                fb.getElement("PARTNERNAME").setDefaultValue(rs.getString("PARTNERNAME"));

//               fb.getElement("COMMNAME").setDefaultValue(rs.getString("COMMNAME"));
//               fb.getElement("COMMTYPE").setDefaultValue(rs.getString("COMMTYPE"));
//               fb.getElement("COMMNUM").setDefaultValue(rs.getString("COMMNUM"));

                fb.getElement("CLIENTID").setDefaultValue(rs.getString("CLIENTID"));
                fb.getElement("CLIENTIDTYPE").setDefaultValue(rs.getString("CLIENTIDTYPE"));
                fb.getElement("CLIENTNAME").setDefaultValue(rs.getString("CLIENTNAME"));
                fb.getElement("CLIENTNO").setDefaultValue(rs.getString("CLIENTNO"));
                fb.getElement("PAYBACKACT").setDefaultValue(rs.getString("PAYBACKACT"));

                fb.getElement("CUSTOMER_CODE").setDefaultValue(rs.getString("CUSTOMER_CODE"));
                fb.getElement("SIGN_ACCOUNT_NO").setDefaultValue(rs.getString("SIGN_ACCOUNT_NO"));

                String bankid = rs.getString("PAYBACKBANKID");
                fb.getElement("PAYBACKBANKID").setDefaultValue(bankid);

                //合同签约地默认为青岛
                fb.getElement("PLACE").setDefaultValue("青岛");
                //设置客户开户行为建行的默认代扣机构
                if (bankid.equals(XFBankCode.BANKCODE_CCB)) {
                    fb.getElement("WITHHOLDBANKNAME").setDefaultValue("中国建设银行青岛市海尔路支行");
                } else if (bankid.equals(XFBankCode.BANKCODE_ALIPAY)) {
                    fb.getElement("WITHHOLDBANKNAME").setDefaultValue("支付宝（中国）网络技术有限公司");
                } else if (bankid.equals(XFBankCode.BANKCODE_BILL99)) {
                    fb.getElement("WITHHOLDBANKNAME").setDefaultValue("上海快钱信息服务有限公司");
                } else {
                    //null
                }
                //帐号名称默认使用客户名字
                fb.getElement("PAYBACKACTNAME").setDefaultValue(rs.getString("CLIENTNAME"));

                UserManager um = (UserManager) ctx.getAttribute(SystemAttributeNames.USER_INFO_NAME);
                fb.getElement("OPERATORID").setDefaultValue(um.getUserName());
                fb.getElement("CREATORID").setDefaultValue(um.getUserName());
                //fb.getElement("CREATORFORM").setDefaultValue("XFCONTRACTPAGE");

//               idtype = rs.getString("IDTYPE");
//               id = rs.getString("ID");
//               name = rs.getString("NAME");  //todo: DB null判断
                count = 1;
            }
            rs.close();
            if (count == 0) {
                msgs.add("此申请单不存在!");
                msgs.add("申请单编号:  " + appno);
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


    /*
    功能：自动生成还款计划
    本方法用于自动调整还款计划，在合同输入后自动调用
    与XFcontracgpayPage中的方法基本类似，计算方法未抽共通
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


            String sSql = "insert into xfcontractpays " +
                    "(contractno,cpaamt, poano, startdate,PAYBACKACT,PAYBACKACTNAME,PAYBACKBANKID,PRINCIPALAMT,SERVICECHARGEFEE) " +
                    "values " + "('" + contractno + "',?,?,?,?,?,?,?,?)";


            PreparedStatement ps = conn.getPreparedStatement(sSql);


            double contractamt = Double.parseDouble(ctx.getParameter("CONTRACTAMT"));
            double servicecharge = Double.parseDouble(ctx.getParameter("SERVICECHARGE"));
            int duration = Integer.parseInt(ctx.getParameter("DURATION"));

            double interest = Airth.mul(Airth.mul(contractamt, duration), servicecharge);
            interest = Airth.round(interest, 2);    //总利息

            double totalamt = Airth.add(contractamt, interest);
            totalamt = Airth.round(totalamt, 2);    //总扣款金额（合同金额+利息）


            String paybackact = ctx.getParameter("PAYBACKACT");
            String paybackactname = ctx.getParameter("PAYBACKACTNAME");
            String paybackbankid = ctx.getParameter("PAYBACKBANKID");
            //TODO: 开户行名称

//            double cpaamt = Airth.div(totalamt, duration, 2);   //每月还款总金额
//            double cpaprincipal = Airth.div(contractamt, duration, 2);   //每月本金
//            double cpainterest = cpaamt-cpaprincipal;  //每月手续费
            double cpaamt = Airth.div(totalamt, duration, 2);   //每月还款总金额
            double cpainterest = Airth.mul(contractamt, servicecharge);  //每月手续费
            cpainterest = Airth.round(cpainterest, 2);  //每月手续费
            double cpaprincipal = cpaamt - cpainterest;   //每月本金


            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sFmt = new SimpleDateFormat("yyyyMMdd");

            //获得当前用户输入的日期
            String startdate = ctx.getParameter("STARTDATE");
            cal.setTime(sFmt.parse(startdate));

            cal.set(Calendar.DAY_OF_MONTH, XFCommon.COMMON_CHARGEOFFDATE);


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

    private int doConfirm(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs) {

        try {
            //TODO: 嵌套事务处理
            //conn.setAuto(false);
            MyDB.getInstance().addDBConn(conn);
            String contractno = ctx.getParameter("CONTRACTNO");

            UserManager um = (UserManager) ctx.getAttribute(SystemAttributeNames.USER_INFO_NAME);

//            String sql = "update xfcontract set cstatus = '3', checkerid = " +
//                    "'" + um.getUserName() + "'" +
//                    ", updatorid = " +
//                    "'" + um.getUserName() + "'" +
//                    ",checkdate = sysdate, updatedate = sysdate  " +
//                    "where contractno='" + contractno + "'";

            String sql = "update xfcontract set cstatus = " + XFContractStatus.QIANDING_TONGGUO +
                    ", checkerid = " +
                    "'" + um.getUserName() + "'" +
                    ", updatorid = " +
                    "'" + um.getUserName() + "'" +
                    ",checkdate = sysdate, updatedate = sysdate  " +
                    "where contractno='" + contractno + "'";

            logger.info(sql);

//            if (doGenMainBills(ctx,conn,instance,msgs) == -1) {
//                msgs.add("在根据本合同的分期还款计划生成扣款帐单时发生错误，合同复核失败！");
//                return -1;
//            }
            if (conn.executeUpdate(sql) < 0) {
                msgs.add("合同复核失败！");
                return -1;
            }

            msgs.add("此合同已确认通过！");

            //根据还款计划生成每期还款主帐单


            return 0;
        } catch (Exception e) {
            msgs.add("发生异常：" + e.getMessage());
            Debug.debug(e);
            return -1;
        } finally {
            MyDB.getInstance().releaseDBConn();
        }
    }

    private int doGenMainBills(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs) {

        try {
//            MyDB.getInstance().addDBConn(conn);
            String contractno = ctx.getParameter("CONTRACTNO");

            UserManager um = (UserManager) ctx.getAttribute(SystemAttributeNames.USER_INFO_NAME);

            XfcontractpaysDao paysdao = XfcontractpaysDaoFactory.create(conn.getConnection());
            Xfcontractpays[] pays = paysdao.findWhereContractnoEquals(contractno);

            XfactcutpaymainDao cutpaydao = XfactcutpaymainDaoFactory.create(conn.getConnection());
            Xfactcutpaymain xfactcutpaymain = new Xfactcutpaymain();


            if (pays.length == 0) {
                return -1;
            }

            for (int i = 0; i < pays.length; i++) {
                //BeanUtils.copyProperties(pays[i], xfactcutpaymain);
                //PropertyUtils.copyProperties(pays[i], xfactcutpaymain);
                xfactcutpaymain.setContractno(pays[i].getContractno());
                xfactcutpaymain.setPoano(pays[i].getPoano());
                xfactcutpaymain.setPaybackamt(pays[i].getCpaamt());
                xfactcutpaymain.setPaybackdate(pays[i].getStartdate());
                xfactcutpaymain.setPaybackact(pays[i].getPaybackact());
                xfactcutpaymain.setPaybackbankid(pays[i].getPaybackbankid());
                xfactcutpaymain.setPaybackbankno(pays[i].getPaybackbankno());
                xfactcutpaymain.setPaybackactname(pays[i].getPaybackactname());

                //设置本金和手续费数额
                xfactcutpaymain.setPrincipalamt(pays[i].getPrincipalamt());
                xfactcutpaymain.setServicechargefee(pays[i].getServicechargefee());


                xfactcutpaymain.setClientname(ctx.getParameter("CLIENTNAME"));
                xfactcutpaymain.setClientact(ctx.getParameter("CLIENTACT"));
                xfactcutpaymain.setClientno(ctx.getParameter("CLIENTNO"));
                xfactcutpaymain.setClientidtype(ctx.getParameter("CLIENTIDTYPE"));
                xfactcutpaymain.setClientid(ctx.getParameter("CLIENTID"));

                xfactcutpaymain.setCustomerCode(ctx.getParameter("CUSTOMER_CODE"));
                xfactcutpaymain.setSignAccountNo(ctx.getParameter("SIGN_ACCOUNT_NO"));

                //设置分期帐单状态
                xfactcutpaymain.setPbstatus("0");  //TODO: 可去掉


                xfactcutpaymain.setOperatorid(um.getUserName());
                xfactcutpaymain.setCreatorid(um.getUserName());
                xfactcutpaymain.setOperatedate(new Date());
                xfactcutpaymain.setCreatedate(new Date());

                xfactcutpaymain.setClosedcd("0");    //设置未结清标志
                xfactcutpaymain.setChargeoffcd("0");    //设置未出帐标志
                xfactcutpaymain.setOverduecd("0");    //设置未逾期标志

                xfactcutpaymain.setOdbClosedcd("0");    //设置逾期帐单未结清标志
                xfactcutpaymain.setOdbChargeoffcd("0");    //设置逾期未出帐标志

                xfactcutpaymain.setOdbPaybackamt(BigDecimal.valueOf(0));
                xfactcutpaymain.setOdbBreachfee(BigDecimal.valueOf(0));
                xfactcutpaymain.setOdbLatefee(BigDecimal.valueOf(0));

                //设置提前还款信息 20090803 zhanrui
                xfactcutpaymain.setPrecutpaycd("0");
                xfactcutpaymain.setPrecutpaydate(null);

                //TODO:全部未进行出帐处理的扣款计划允许删除？！！
                //String sqlDelete = "delete from xfactcutpaymail where contractno='" + contractno.trim() + "'";
                //conn.executeUpdate(sqlDelete);

                cutpaydao.insert(xfactcutpaymain);
            }


        } catch (Exception e) {
            msgs.add("发生异常：" + e.getMessage());
            Debug.debug(e);
            return -1;
        }
//        } finally {
//            MyDB.getInstance().releaseDBConn();
//        }
        return 0;
    }

    private int doDismissal(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs) {

        try {
            MyDB.getInstance().addDBConn(conn);
            String contractno = ctx.getParameter("CONTRACTNO");

            UserManager um = (UserManager) ctx.getAttribute(SystemAttributeNames.USER_INFO_NAME);

            String sql = "update xfcontract set cstatus = " + XFContractStatus.QIANDING_BOHUI +
                    ", checkerid = " +
                    "'" + um.getUserName() + "'" +
                    ", updatorid = " +
                    "'" + um.getUserName() + "'" +
                    ",checkdate = sysdate, updatedate = sysdate  " +
                    "where contractno='" + contractno + "'";

            logger.info(sql);

            if (conn.executeUpdate(sql) < 0) {
                msgs.add("合同复核失败！");
                return -1;
            }
            msgs.add("此合同已驳回！");
            return 0;
        } catch (Exception e) {
            msgs.add("发生异常：" + e.getMessage());
            Debug.debug(e);
            return -1;
        } finally {
            MyDB.getInstance().releaseDBConn();
        }
    }


    private int doConfirmForCreditIssue(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs) {

        try {
            //TODO: 嵌套事务处理
            //conn.setAuto(false);
//            MyDB.getInstance().addDBConn(conn);
            String contractno = ctx.getParameter("CONTRACTNO");

            UserManager um = (UserManager) ctx.getAttribute(SystemAttributeNames.USER_INFO_NAME);

            if (doGenMainBills(ctx, conn, instance, msgs) == -1) {
                logger.info("在根据本合同的分期还款计划生成扣款帐单时发生错误，合同复核失败！");
                return -1;
            }


            String sql = "update xfcontract set cstatus = " + XFContractStatus.FANGKUAN_TONGGUO +
                    " where contractno='" + contractno + "'";

            logger.info(sql);

            if (conn.executeUpdate(sql) < 0) {
                msgs.add("合同放款复核失败！");
                return -1;
            }
            msgs.add("此合同放款已确认通过！");
            return 0;
        } catch (Exception e) {
            msgs.add("发生异常：" + e.getMessage());
            Debug.debug(e);
            return -1;
        }
//        } finally {
//            MyDB.getInstance().releaseDBConn();
//        }
    }

    private int doDismissalForCreditIssue(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs) {

        try {
            MyDB.getInstance().addDBConn(conn);
            String contractno = ctx.getParameter("CONTRACTNO");

            UserManager um = (UserManager) ctx.getAttribute(SystemAttributeNames.USER_INFO_NAME);

            String sql = "update xfcontract set cstatus = " + XFContractStatus.FANGKUAN_BOHUI +
                    " where contractno='" + contractno + "'";

            logger.info(sql);

            if (conn.executeUpdate(sql) < 0) {
                return -1;
            }
            return 0;
        } catch (Exception e) {
            Debug.debug(e);
            return -1;
        } finally {
            MyDB.getInstance().releaseDBConn();
        }
    }


}
